package com.lowkey.tinder.profile;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Diego Mariani
 * @since 05-2017
 */
public class Profile {

    private String id;
    private int ageFilterMax;
    private int ageFilterMin;
    private String bio;
    private Date birthDate;
    private String blend;
    private boolean canCreateSquad;
    private int ConnectionCount;
    private Date createDate;
    private boolean discoverable;
    private int distanceFilter;
    private String facebookId;
    private int gender;
    private int genderFilter;
    private String name;
    private String username;
    private Position position;
    private boolean photoOptimizerEnabled;
    private ArrayList<com.lowkey.tinder.profile.Photo> photos;

    public String getId() {
        return id;
    }

    public Profile setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * @return max age filter
     */
    public int getAgeFilterMax() {
        return ageFilterMax;
    }

    public Profile setAgeFilterMax(int ageFilterMax) {
        this.ageFilterMax = ageFilterMax;
        return this;
    }

    public int getAgeFilterMin() {
        return ageFilterMin;
    }

    public Profile setAgeFilterMin(int ageFilterMin) {
        this.ageFilterMin = ageFilterMin;
        return this;
    }

    public String getBio() {
        return bio;
    }

    public Profile setBio(String bio) {
        this.bio = bio;
        return this;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Profile setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public String getBlend() {
        return blend;
    }

    public Profile setBlend(String blend) {
        this.blend = blend;
        return this;
    }

    public boolean isCanCreateSquad() {
        return canCreateSquad;
    }

    public Profile setCanCreateSquad(boolean canCreateSquad) {
        this.canCreateSquad = canCreateSquad;
        return this;
    }

    public int getConnectionCount() {
        return ConnectionCount;
    }

    public Profile setConnectionCount(int connectionCount) {
        ConnectionCount = connectionCount;
        return this;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Profile setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }

    public boolean isDiscoverable() {
        return discoverable;
    }

    public Profile setDiscoverable(boolean discoverable) {
        this.discoverable = discoverable;
        return this;
    }

    public int getDistanceFilter() {
        return distanceFilter;
    }

    public Profile setDistanceFilter(int distanceFilter) {
        this.distanceFilter = distanceFilter;
        return this;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public Profile setFacebookId(String facebookId) {
        this.facebookId = facebookId;
        return this;
    }

    public int getGender() {
        return gender;
    }

    public Profile setGender(int gender) {
        this.gender = gender;
        return this;
    }

    public int getGenderFilter() {
        return genderFilter;
    }

    public Profile setGenderFilter(int genderFilter) {
        this.genderFilter = genderFilter;
        return this;
    }

    public String getName() {
        return name;
    }

    public Profile setName(String name) {
        this.name = name;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Profile setUsername(String username) {
        this.username = username;
        return this;
    }

    public Position getPosition() {
        return position;
    }

    public Profile setPosition(Position position) {
        this.position = position;
        return this;
    }

    public boolean isPhotoOptimizerEnabled() {
        return photoOptimizerEnabled;
    }

    public Profile setPhotoOptimizerEnabled(boolean photoOptimizerEnabled) {
        this.photoOptimizerEnabled = photoOptimizerEnabled;
        return this;
    }

    public ArrayList<com.lowkey.tinder.profile.Photo> getPhotos() {
        return photos;
    }

    public Profile setPhotos(ArrayList<com.lowkey.tinder.profile.Photo> photos) {
        this.photos = photos;
        return this;
    }

    public static Profile Builder() {
        return new Profile();
    }
}
