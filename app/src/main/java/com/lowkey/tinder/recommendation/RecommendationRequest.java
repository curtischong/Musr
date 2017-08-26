package com.lowkey.tinder.recommendation;

import com.lowkey.tinder.http.request.HttpGetRequest;

/**
 * @author Diego Mariani
 * @since 05-2017
 */
public class RecommendationRequest implements HttpGetRequest {

    /**
     * URI for the recommendation request
     */
    public static final String URI = "/recs/core";

    /**
     * Final url that will be used to retrieve recommendations
     */
    private String url;

    /**
     * @param url
     */
    public RecommendationRequest(String url) {
        this.url = url;
    }

    /**
     * @return return recommendation final url
     */
    public String getUrl() {
        return url;
    }
}
