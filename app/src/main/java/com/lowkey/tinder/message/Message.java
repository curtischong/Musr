package com.lowkey.tinder.message;

import java.util.Date;

/**
 * @author Diego Mariani
 * @since 05-2017
 */
public class Message {

    /**
     * Message unique id
     */
    private String id;

    /**
     * Match unique id where the message belongs to
     */
    private String matchId;

    /**
     * User id of the message receiver
     */
    private String to;

    /**
     * User id of the message sender
     */
    private String from;

    /**
     * Message content. Can be text or gif URL
     */
    private String message;

    /**
     * Date of when the message was sent
     */
    private Date sentDate;

    /**
     * Date of when the message was created
     */
    private Date createdDate;

    /**
     * Type of message if text or gif
     */
    private Type type;

    public enum Type {
        TEXT, GIF
    }

    public String getId() {
        return id;
    }

    public Message setId(String id) {
        this.id = id;
        return this;
    }

    public String getMatchId() {
        return matchId;
    }

    public Message setMatchId(String matchId) {
        this.matchId = matchId;
        return this;
    }

    public String getTo() {
        return to;
    }

    public Message setTo(String to) {
        this.to = to;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public Message setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Message setMessage(String message) {
        this.message = message;
        return this;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public Message setSentDate(Date sentDate) {
        this.sentDate = sentDate;
        return this;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Message setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public Type getType() {
        return type;
    }

    public Message setType(Type type) {
        this.type = type;
        return this;
    }

    public static Message Builder() {
        return new Message();
    }
}
