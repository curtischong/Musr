package com.lowkey.tinder.recommendation;

import com.lowkey.tinder.user.Photo;
import com.lowkey.tinder.user.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * @author Diego Mariani
 * @since 05-2017
 */
public class RecommendationResponse {

    /**
     * Recommendation's response as a string
     */
    private String response;

    /**
     * JSONParser used to parse the json response
     */
    private JSONParser parser;

    /**
     * @param response
     */
    public RecommendationResponse(String response) {
        this.response = response;
        parser = new JSONParser();
    }

    /**
     * This method returns a list of users recommended by tinder api.
     * If you have finished your likes, you will get an empty list.
     *
     * @return users as array list
     * @throws org.json.simple.parser.ParseException on SimpleDate date format parsing is invalid
     * @throws java.text.ParseException on json parsing if the json response is invalid
     */
    public ArrayList<User> getRecommendations() throws java.text.ParseException, org.json.simple.parser.ParseException {
        JSONObject jsonResponse = (JSONObject) parser.parse(response);
        JSONArray results = (JSONArray) jsonResponse.get("results");
        ArrayList<User> users = new ArrayList<User>();
        for (int i = 0; i < results.size(); i++) {
            JSONObject item = (JSONObject) results.get(i);
            if (item.get("name") == "Tinder Team") {
                return new ArrayList<User>();
            }
            JSONArray jsonPhotos = (JSONArray) item.get("photos");
            ArrayList<Photo> photos = new ArrayList<Photo>();
            for (int j = 0; j < jsonPhotos.size(); j++) {
                JSONObject jsonPhoto = (JSONObject) jsonPhotos.get(j);
                Photo photo = Photo.Builder()
                        .setId((String) jsonPhoto.get("id"))
                        .setUrl((String) jsonPhoto.get("url"));
                photos.add(photo);

            }
            User recommendedUser = User.Builder()
                    .setId((String) item.get("_id"))
                    .setBirthDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse((String) item.get("birth_date")))
                    .setDistance((Long) item.get("distance_mi"))
                    .setName((String) item.get("name"))
                    .setContentHash((String) item.get("content_hash"))
                    .setPhotos(photos)
                    .setGender(Math.toIntExact((Long) item.get("gender")));
            users.add(recommendedUser);
        }
        return users;
    }
}
