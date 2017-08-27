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

//import com.eeg_project.components.signal.NoiseDetector;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;

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
    private int epochs;
    public int samplingRate = 256;
    private int nbBins;
    public ClassifierDataListener dataListener;
    public EpochBuffer eegBuffer;
    private HandlerThread dataThread;
    private Handler dataHandler;
    private FFT fft;
    public BandPowerExtractor bandExtractor;

    public LinkedList<double[]> data = new LinkedList<>();

    private Queue<Double> lband = new LinkedList<Double>();
    private Queue<Double> rband = new LinkedList<Double>();

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
        epochs = 0;
        fft = new FFT(samplingRate, FFT_LENGTH, samplingRate);
        nbBins = fft.getFreqBins().length;
        bandExtractor = new BandPowerExtractor(fft.getFreqBins());
        dataListener = new ClassifierDataListener();
    }

    public void listenData() {
        eegBuffer = new EpochBuffer(samplingRate, NUM_CHANNELS, samplingRate);
        eegBuffer.addListener(this);

        mActivity.muse.registerDataListener(dataListener, MuseDataPacketType.EEG);
        startThread();
    }

    public void collectData() {
        data = new LinkedList<>();
        epochs = 0;
        isListening = true;

        Log.i("test", "collecting...");
    }

    public void stopCollecting() {
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

        private double generatelbandAverage(){
            double tot = 0;
            for(double currVal : lband){
                tot += currVal;
            }

            return tot/lband.size();
        }

        private double generaterbandAverage(){
            double tot = 0;
            for(double currVal : rband){
                tot += currVal;
            }
            return tot/rband.size();
        }

        private void shiftlbandVals(double newVal){
            lband.add(newVal);
            lband.remove();
        }

        private void shiftrbandVals(double newVal){
            rband.add(newVal);
            rband.remove();
        }

        @Override
        public void run() {
//            if (noisePresent(rawBuffer)) {
//                return;
//            }
            getPSD(rawBuffer);
            band2D = bandExtractor.extract2D(PSD);

            if(lband.size() == 30){
                shiftlbandVals(band2D[1][2]);
            }else{
                lband.add(band2D[1][2]);
            }
            if(rband.size() == 30){
                shiftrbandVals(band2D[2][2]);
            }else{
                rband.add(band2D[2][2]);
            }

            double avglband = generatelbandAverage();
            double avgrband = generaterbandAverage();


            if(isListening) {
                if (epochs == 0) {
                    epochs ++;
                    return;
                } else if (epochs > 3) {
                    double leftAverage = 0;
                    double rightAverage = 0;
                    for (double[] epoch : data) {
                        leftAverage += epoch[0];
                        rightAverage += epoch[1];
                    }
                    leftAverage /= data.size();
                    rightAverage /= data.size();
                    Log.i("average", "left: " + (avglband));
                    Log.i("average", "right: " + (avgrband));
                    Log.i("average", "data left: " + leftAverage);
                    Log.i("average", "data right: " + rightAverage);
                    double left = leftAverage - avglband;
                    double right = rightAverage - avgrband;
                    double average = (left + right) / 2;
                    if (average > 0) {
                        Log.i("state", "attracted");
                        Handler mainHandler = new Handler(mActivity.getMainLooper());
                        mainHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                mActivity.likeTinder();
                            }
                        });
                    } else {
                        Log.i("state", "not attracted");
                        Handler mainHandler = new Handler(mActivity.getMainLooper());
                        mainHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                mActivity.dislikeTinder();
                            }
                        });
                    }
                    isListening = false;
                    return;
                }
                bands[0] += band2D[1][2]; // left forehead
                bands[1] += band2D[2][2]; // right forehead
//                for(int i = 0; i < NUM_CHANNELS; i++) {
//                    bands[i] = band2D[i][2];
//                    Log.i("test", i + ": " + bands[i]);
//                }
                data.add(bands);
                epochs ++;
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