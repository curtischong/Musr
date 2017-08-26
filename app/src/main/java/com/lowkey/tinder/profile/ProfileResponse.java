package com.lowkey.tinder.profile;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Diego Mariani
 * @since 05-2017
 */
public class ProfileResponse {

    /**
     * Profile's response as a string
     */
    private String response;

    /**
     * JSONParser used to parse the json response
     */
    private JSONParser parser;

    /**
     * ProfileResponse constructor. Accepts the response to be parsed as String.
     * @param response
     */
    public ProfileResponse(String response) {
        this.response = response;
        parser = new JSONParser();
    }

    /**
     * Return your tinder profile settings.
     *
     * @see Profile
     * @return Profile Profile settings object
     * @throws org.json.simple.parser.ParseException on invalid json response
     * @throws java.text.ParseException on invalid date format
     */
    public Profile getProfile() throws java.text.ParseException, org.json.simple.parser.ParseException {
        JSONObject jsonResponse = (JSONObject) parser.parse(response);
        JSONObject jsonPosition = (JSONObject) jsonResponse.get("pos");
        Position position = Position.Builder()
                .setLat((Double) jsonPosition.get("lat"))
                .setLon((Double) jsonPosition.get("lon"))
                .setLastRecordedAt(new Date((new Timestamp((Long) jsonPosition.get("at"))).getTime()));
        JSONArray jsonPhotos = (JSONArray) jsonResponse.get("photos");
        ArrayList<Photo> photos = new ArrayList<Photo>();
        for (int i = 0; i < jsonPhotos.size(); i++) {
            JSONObject jsonPhoto = (JSONObject) jsonPhotos.get(i);
            Photo photo = Photo.Builder()
                    .setId((String) jsonPhoto.get("id"))
                    .setFbId((String) jsonPhoto.get("fbId"))
                    .setFileName((String) jsonPhoto.get("fileName"))
                    .setExtension((String) jsonPhoto.get("extension"))
                    .setUrl((String) jsonPhoto.get("url"));
            photos.add(photo);
        }
        return Profile.Builder()
                .setName((String) jsonResponse.get("name"))
                .setAgeFilterMax(Math.toIntExact((Long) jsonResponse.get("age_filter_max")))
                .setAgeFilterMin(Math.toIntExact((Long) jsonResponse.get("age_filter_min")))
                .setId((String) jsonResponse.get("_id"))
                .setBio((String) jsonResponse.get("bio"))
                .setBirthDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse((String) jsonResponse.get("birth_date")))
                .setCreateDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse((String) jsonResponse.get("create_date")))
                .setBlend((String) jsonResponse.get("blend"))
                .setCanCreateSquad((Boolean) jsonResponse.get("can_create_squad"))
                .setDiscoverable((Boolean) jsonResponse.get("discoverable"))
                .setDistanceFilter(Math.toIntExact((Long) jsonResponse.get("distance_filter")))
                .setFacebookId((String) jsonResponse.get("facebook_id"))
                .setPhotoOptimizerEnabled((Boolean) jsonResponse.get("photo_optimizer_enabled"))
                .setPosition(position)
                .setPhotos(photos);
    }
}
