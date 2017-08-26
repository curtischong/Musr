package com.lowkey.tinder.match;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * @author Diego Mariani
 * @since 05-2017
 */
public class MatchResponse {

    /**
     * Match's response as a string
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
    public MatchResponse(String response) {
        this.response = response;
        parser = new JSONParser();
    }

    /**
     * @return list of matches
     */
    public ArrayList<Match> getMatches() throws org.json.simple.parser.ParseException, java.text.ParseException {
        System.out.println(response);
        JSONArray jsonResponseArray = (JSONArray) parser.parse(response);
        ArrayList<Match> matches = new ArrayList<Match>();
        for (int i = 0; i < jsonResponseArray.size(); i++) {
            JSONObject jsonMatch = (JSONObject) jsonResponseArray.get(i);
            JSONArray jsonMessages = (JSONArray) jsonMatch.get("messages");
            ArrayList<com.lowkey.tinder.message.Message> messages = new ArrayList<com.lowkey.tinder.message.Message>();
            for (int j = 0; j < jsonMessages.size(); j++) {
                JSONObject jsonMessage = (JSONObject) jsonMessages.get(j);
                com.lowkey.tinder.message.Message.Type messageType = com.lowkey.tinder.message.Message.Type.GIF;
                if (jsonMessage.get("type") == null) {
                    messageType = com.lowkey.tinder.message.Message.Type.TEXT;
                }
                com.lowkey.tinder.message.Message message = com.lowkey.tinder.message.Message.Builder()
                        .setMessage((String) jsonMessage.get("message"))
                        .setType(messageType)
                        .setFrom((String) jsonMessage.get("from"))
                        .setTo((String) jsonMessage.get("to"))
                        .setMatchId((String) jsonMessage.get("match_id"))
                        .setId((String) jsonMessage.get("id"));
                messages.add(message);
            }
            Match match = Match.Builder()
                    .setId((String) jsonMatch.get("_id"))
                    .setMessages(messages)
                    .setLastActivityDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse((String) jsonMatch.get("last_activity_date")));
            matches.add(match);
        }
        return matches;
    }
}
