package com.lowkey.tinder.like;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class LikeResponse {
    private JSONParser parser;
    private String response;

    public LikeResponse(String res) {
        response = res;
        parser = new JSONParser();
    }

    public Like getLike() throws Exception {
        JSONObject jsonObject = (JSONObject) parser.parse(response);
        return Like.Builder()
                .setLikesRemaining(Math.toIntExact((Long) jsonObject.get("likes_remaining")))
                .setMatch((Boolean) jsonObject.get("match"));
    }
}
