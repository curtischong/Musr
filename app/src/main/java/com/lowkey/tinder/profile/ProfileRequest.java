package com.lowkey.tinder.profile;

/**
 * @author Diego Mariani
 * @since 05-2017
 */
public class ProfileRequest implements com.lowkey.tinder.http.request.HttpGetRequest {
    public static final String URI = "/profile";

    private String url;

    public ProfileRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
       return url;
    }
}
