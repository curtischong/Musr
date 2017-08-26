package com.lowkey.tinder.auth;

import org.json.simple.JSONObject;

public class AuthRequest implements com.lowkey.tinder.http.request.HttpPostRequest {

    public static final String URI = "/auth";

    private String url;
    private String token;

    public AuthRequest(String url, String token) {
        this.url = url;
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public String getBody() {
        JSONObject obj = new JSONObject();
        obj.put("token", token);

        return obj.toString();
    }
}
