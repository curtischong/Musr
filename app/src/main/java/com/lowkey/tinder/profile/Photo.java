package com.lowkey.tinder.profile;

import java.util.ArrayList;

/**
 * @author Diego Mariani
 * @since 05-2017
 */
public class Photo {
    private String fileName;
    private String id;
    private String fbId;
    private String extension;
    private ArrayList<ProcessedPhoto> processedPhotos;
    private double selectRate;
    private double successRate;
    private String url;

    public String getFileName() {
        return fileName;
    }

    public Photo setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getId() {
        return id;
    }

    public Photo setId(String id) {
        this.id = id;
        return this;
    }

    public String getFbId() {
        return fbId;
    }

    public Photo setFbId(String fbId) {
        this.fbId = fbId;
        return this;
    }

    public String getExtension() {
        return extension;
    }

    public Photo setExtension(String extension) {
        this.extension = extension;
        return this;
    }

    public ArrayList<ProcessedPhoto> getProcessedPhotos() {
        return processedPhotos;
    }

    public Photo setProcessedPhotos(ArrayList<ProcessedPhoto> processedPhotos) {
        this.processedPhotos = processedPhotos;
        return this;
    }

    public double getSelectRate() {
        return selectRate;
    }

    public Photo setSelectRate(double selectRate) {
        this.selectRate = selectRate;
        return this;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public Photo setSuccessRate(double successRate) {
        this.successRate = successRate;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Photo setUrl(String url) {
        this.url = url;
        return this;
    }

    public static Photo Builder() {
        return new Photo();
    }
}
