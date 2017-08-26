package com.lowkey.tinder.auth;

import android.util.Log;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class AuthResponse {

    public static final String TOKEN_KEY = "api_token";

    private String response;
    private JSONParser parser;

    public AuthResponse(String res) {
        response = res;
        parser = new JSONParser();
    }

    public String getToken() throws Exception {
        JSONObject jsonRes = (JSONObject) parser.parse(response);
        Log.i("test", response);
        JSONObject tokenData = (JSONObject) jsonRes.get("data");
        if (tokenData == null) {
            throw new AuthenticationException("Unable to retrieve access token. Field 'data' not found in response");
        }
        String token = (String) tokenData.get(TOKEN_KEY);
        if (token == null) {
            throw new AuthenticationException("Unable to retrieve access token. Field 'api_token' not found in response");
        }

        return token;
    }
}
