package com.lowkey.tinder.profile;

/**
 * @author Diego Mariani
 * @since 05-2017
 */
public class ProcessedPhoto {

    private String url;
    private int width;
    private int height;

    public String getUrl() {
        return url;
    }

    public ProcessedPhoto setUrl(String url) {
        this.url = url;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public ProcessedPhoto setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public ProcessedPhoto setHeight(int height) {
        this.height = height;
        return this;
    }
}
