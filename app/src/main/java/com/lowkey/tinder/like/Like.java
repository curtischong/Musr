package com.lowkey.tinder.like;

/**
 * @author Diego Mariani
 * @since 05-2017
 */
public class Like {

    private boolean isMatch;
    private int likesRemaining;
    private int status;

    public boolean isMatch() {
        return isMatch;
    }

    public Like setMatch(boolean match) {
        isMatch = match;
        return this;
    }

    public int getLikesRemaining() {
        return likesRemaining;
    }

    public Like setLikesRemaining(int likesRemaining) {
        this.likesRemaining = likesRemaining;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Like setStatus(int status) {
        this.status = status;
        return this;
    }

    public static Like Builder() {
        return new Like();
    }
}
