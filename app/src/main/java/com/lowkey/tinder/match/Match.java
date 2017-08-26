package com.lowkey.tinder.match;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Diego Mariani
 * @since 05-2017
 */
public class Match {

    /**
     * Match id
     */
    private String id;

    /**
     * List of messages exchanged in a given match
     */
    private ArrayList<com.lowkey.tinder.message.Message> messages;

    /**
     * Last activity of both people of the Match
     */
    private Date lastActivityDate;

    /**
     * If there is a new message in the match.
     */
    private boolean isNewMMessage;

    public String getId() {
        return id;
    }

    public Match setId(String id) {
        this.id = id;
        return this;
    }

    public ArrayList<com.lowkey.tinder.message.Message> getMessages() {
        return messages;
    }

    public Match setMessages(ArrayList<com.lowkey.tinder.message.Message> messages) {
        this.messages = messages;
        return this;
    }

    public Date getLastActivityDate() {
        return lastActivityDate;
    }

    public Match setLastActivityDate(Date lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
        return this;
    }

    public boolean isNewMMessage() {
        return isNewMMessage;
    }

    public Match setNewMMessage(boolean newMMessage) {
        isNewMMessage = newMMessage;
        return this;
    }

    public static Match Builder() {
        return new Match();
    }
}
