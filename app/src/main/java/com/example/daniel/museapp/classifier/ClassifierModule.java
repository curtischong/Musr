package com.example.daniel.museapp.classifier;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.choosemuse.libmuse.Eeg;
import com.choosemuse.libmuse.Muse;
import com.choosemuse.libmuse.MuseArtifactPacket;
import com.choosemuse.libmuse.MuseDataListener;
import com.choosemuse.libmuse.MuseDataPacket;
import com.choosemuse.libmuse.MuseDataPacketType;
import com.example.daniel.museapp.MainActivity;
import com.example.daniel.museapp.signal.BandPowerExtractor;
import com.example.daniel.museapp.signal.FFT;
import com.example.daniel.museapp.signal.Filter;
import com.example.daniel.museapp.utils.Save;

//import com.eeg_project.components.signal.NoiseDetector;

import java.util.LinkedList;

/**
 * Bridged native module for classifier.
 * Starts ClassifierDataListener to record data from Muse
 */

public class ClassifierModule implements BufferListener {

    // ---------------------------------------------------------
    // Variables

    public static final int FFT_LENGTH = 256;
    public static final int NUM_CHANNELS = 4;
    private boolean isListening;
    public int samplingRate = 256;
    private int nbBins;
    public ClassifierDataListener dataListener;
    public EpochBuffer eegBuffer;
    private HandlerThread dataThread;
    private Handler dataHandler;
    private FFT fft;
    public BandPowerExtractor bandExtractor;
    private int stage;
    private int stageMax;

    public LinkedList<double[]>[] data = (LinkedList<double[]>[]) new LinkedList<?>[3];
    public double[][] dataPoints = new double[3][4];

    // grab reference to global Muse
    private MainActivity mActivity;

    public ClassifierModule(MainActivity mActivity) {
        this.mActivity = mActivity;
    }

    // ---------------------------------------------------------
    // Bridged methods

    public void init() {
        if(mActivity.muse != null) {
            if (!mActivity.muse.isLowEnergy()) {
                samplingRate = 220;
            }
        }
        stage = 0;
        stageMax = 2;
        for(int i = 0; i <= stageMax; i++){
            data[i] = new LinkedList<>();
        }
        fft = new FFT(samplingRate, FFT_LENGTH, samplingRate);
        nbBins = fft.getFreqBins().length;
        bandExtractor = new BandPowerExtractor(fft.getFreqBins());
        dataListener = new ClassifierDataListener();
    }

    public void bumpMode() {
        if (stage > stageMax) {
            return;
        }
        int length = data[stage].size();
        double[] averages = new double[3];
        double[] seconds = new double[4];
        for (double[] bands : data[stage]) {
            double frontRatio = bands[1] / bands[2];
            double backRatio = bands[0] / bands[3];
            double averageRatio = (frontRatio + backRatio) / 2;
            averages[0] += frontRatio;
            averages[1] += backRatio;
            averages[2] += averageRatio;
            seconds[0] += bands[0];
            seconds[1] += bands[1];
            seconds[2] += bands[2];
            seconds[3] += bands[3];
        }
        averages[0] /= length;
        averages[1] /= length;
        averages[2] /= length;
        seconds[0] /= length;
        seconds[1] /= length;
        seconds[2] /= length;
        seconds[3] /= length;
        dataPoints[stage][0] = seconds[0];
        dataPoints[stage][1] = seconds[1];
        dataPoints[stage][2] = seconds[2];
        dataPoints[stage][3] = seconds[3];

        Log.i("test", "-- " + stage + " --");
        Log.i("test", "front: " + averages[0]);
        Log.i("test", "back: " + averages[1]);
        Log.i("test", "combined: " + averages[2]);
        Log.i("test", "left back: " + seconds[0]);
        Log.i("test", "left front: " + seconds[1]);
        Log.i("test", "right front: " + seconds[2]);
        Log.i("test", "right back: " + seconds[3]);
        Log.i("test", "-- end --");
        stage ++;
        if(stage > stageMax) {
            this.stopCollecting();
        }
    }

    public void collectData() {
        isListening = true;

        eegBuffer = new EpochBuffer(samplingRate, NUM_CHANNELS, samplingRate);
        eegBuffer.addListener(this);

        mActivity.muse.registerDataListener(dataListener, MuseDataPacketType.EEG);
        startThread();
    }

    public void stopCollecting() {
        Log.i("test", "Hot left back: " + (dataPoints[1][0] - dataPoints[0][0]));
        Log.i("test", "Hot left front: " + (dataPoints[1][1] - dataPoints[0][1]));
        Log.i("test", "Hot right front: " + (dataPoints[1][2] - dataPoints[0][2]));
        Log.i("test", "Hot right back: " + (dataPoints[1][3] - dataPoints[0][3]));

        Log.i("test", "Not Hot left back: " + (dataPoints[2][0] - dataPoints[0][0]));
        Log.i("test", "Not Hot left front: " + (dataPoints[2][1] - dataPoints[0][1]));
        Log.i("test", "Not Hot right front: " + (dataPoints[2][2] - dataPoints[0][2]));
        Log.i("test", "Not Hot right back: " + (dataPoints[2][3] - dataPoints[0][3]));

        isListening = false;
        mActivity.muse.unregisterDataListener(dataListener, MuseDataPacketType.EEG);
        stopThread();
    }


    // ------------------------------------------------------------------------------
    // Helper functions

    public void getEpoch(double[][] buffer) {
        dataHandler.post(new ClassifierRunnable(buffer));
    }

    public void startThread() {
        dataThread = new HandlerThread("dataThread");
        dataThread.start();
        dataHandler = new Handler(dataThread.getLooper());
    }

    public void stopThread() {
        if (dataHandler != null) {

            // Removes all runnables and things from the Handler
            dataHandler.removeCallbacksAndMessages(null);
            dataThread.quit();
        }
    }

    // ------------------------------------------------------------------------------
    // Helper Classes

    // Test this out with different headbands, althought the continually smoothing design is a little bit slower and hard to interpret there are
    // promisingly low accuracies coming out of it
    //

    public class ClassifierRunnable implements Runnable {

        private double[][] rawBuffer;
        private double[][] PSD;
        private double[][] band2D;
        private double[] bands;

        public ClassifierRunnable(double[][] buffer) {
            rawBuffer = buffer;
            PSD = new double[NUM_CHANNELS][nbBins];
            bands = new double[NUM_CHANNELS];
        }

        @Override
        public void run() {
//            if (noisePresent(rawBuffer)) {
//                return;
//            }

            if(isListening) {
                getPSD(rawBuffer);
                band2D = bandExtractor.extract2D(PSD);
                for(int i = 0; i < NUM_CHANNELS; i++) {
                    bands[i] = band2D[i][2];
                }
//                double frontRatio = bands[1] / bands[2];
//                double backRatio = bands[0] / bands[3];
//                Log.i("test", "-- start --");
//                Log.i("test", "front: " + frontRatio);
//                Log.i("test", "back: " + backRatio);
//                Log.i("test", "combined: " + (frontRatio + backRatio) / 2);
//                Log.i("test", "-- end --");
                data[stage].add(bands);
            }

        }

//
//        public boolean noisePresent(double[][] buffer) {
//            for (boolean value : noiseDetector.detectArtefact(buffer)) {
//                if (value) {
//                    return true;
//                }
//            }
//            return false;
//        }

        public void getPSD(double[][] buffer) {
            // [nbch][nbsmp]
            for (int i = 0; i < NUM_CHANNELS; i++) {
                double[] channelPower = fft.computeLogPSD(buffer[i]);
                for (int j = 0; j < channelPower.length; j++) {
                    PSD[i][j] = channelPower[j];
                }
            }
        }

    }


    public class ClassifierDataListener extends MuseDataListener {

        double[] newData;
        boolean filterOn;
        public Filter bandstopFilter;
        public double[][] bandstopFiltState;


        // if connected Muse is a 2016 BLE version, init a bandstop filter to remove 60hz noise
        ClassifierDataListener() {
            if (samplingRate == 256) {
                filterOn = true;
                bandstopFilter = new Filter(samplingRate, "bandstop", 5, 55, 65);
                bandstopFiltState = new double[4][bandstopFilter.getNB()];
            }
            newData = new double[4];
        }

        // Updates eegBuffer with new data from all 4 channels. Bandstop filter for 2016 Muse
        @Override
        public void receiveMuseDataPacket(final MuseDataPacket p, final Muse muse) {
            getEegChannelValues(newData, p);

            if (filterOn) {
                bandstopFiltState = bandstopFilter.transform(newData, bandstopFiltState);
                newData = bandstopFilter.extractFilteredSamples(bandstopFiltState);
            }

            eegBuffer.update(newData);
        }

        // Updates newData array based on incoming EEG channel values
        private void getEegChannelValues(double[] newData, MuseDataPacket p) {
            newData[0] = p.getEegChannelValue(Eeg.EEG1); // Left Ear
            newData[1] = p.getEegChannelValue(Eeg.EEG2); // Left Forehead
            newData[2] = p.getEegChannelValue(Eeg.EEG3); // Right Forehead
            newData[3] = p.getEegChannelValue(Eeg.EEG4); // Right Ear2
        }

        @Override
        public void receiveMuseArtifactPacket(final MuseArtifactPacket p, final Muse muse) {
            // Does nothing for now
        }
    }
}