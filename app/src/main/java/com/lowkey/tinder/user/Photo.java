package com.lowkey.tinder.user;

import java.util.ArrayList;

/**
 * @author Diego Mariani
 * @since 05-2017
 */
public class Photo {

    /**
     * Photo's id
     */
    private String id;

    /**
     * Photo's url
     */
    private String url;

    /**
     * The original photo is processed to a set of the same photo but with different sizes.
     */
    private ArrayList<com.lowkey.tinder.profile.ProcessedPhoto> processedPhotos;

    /**
     * @return Photo's id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id Photo's id
     * @return Photo's object
     */
    public Photo setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * @return Photo's url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url Photo's url
     * @return Photo's object
     */
    public Photo setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * @return Processed photo as array list
     */
    public ArrayList<com.lowkey.tinder.profile.ProcessedPhoto> getProcessedPhotos() {
        return processedPhotos;
    }

    /**
     * @param processedPhotos
     * @return Photo's object
     */
    public Photo setProcessedPhotos(ArrayList<com.lowkey.tinder.profile.ProcessedPhoto> processedPhotos) {
        this.processedPhotos = processedPhotos;
        return this;
    }

    /**
     * @return Photo's object
     */
    public static Photo Builder() {
        return new Photo();
    }
}
