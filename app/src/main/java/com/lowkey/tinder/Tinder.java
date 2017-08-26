package com.lowkey.tinder;

import android.util.Log;

import com.lowkey.tinder.auth.AuthRequest;
import com.lowkey.tinder.auth.AuthResponse;
import com.lowkey.tinder.http.client.AuthenticatedHttpClient;
import com.lowkey.tinder.http.request.HttpPostRequest;
import com.lowkey.tinder.http.client.AnonymousHttpClient;
import com.lowkey.tinder.like.Like;
import com.lowkey.tinder.like.LikeResponse;
import com.lowkey.tinder.match.Match;
import com.lowkey.tinder.match.MatchRequest;
import com.lowkey.tinder.profile.Profile;
import com.lowkey.tinder.profile.ProfileRequest;
import com.lowkey.tinder.profile.ProfileResponse;
import com.lowkey.tinder.user.User;
import com.lowkey.tinder.recommendation.RecommendationRequest;
import com.lowkey.tinder.recommendation.RecommendationResponse;

import okhttp3.*;

import java.util.ArrayList;

/**
 * @author Diego Mariani
 * @since 05-2017
 */
public class Tinder {

    /**
     * Base url for the tinder apis
     */
    public static final String BASE_URL = "https://api.gotinder.com";

    /**
     * Non authenticated http client.
     */
    private AnonymousHttpClient anonymousHttpClient;

    /**
     * Authenticated http client. Can performs http request to the private tinder api endpoints.
     */
    private AuthenticatedHttpClient authenticatedHttpClient;

    private Tinder(String facebookAccessToken) throws Exception {
        anonymousHttpClient = new AnonymousHttpClient(new OkHttpClient());
        authenticatedHttpClient = new AuthenticatedHttpClient(anonymousHttpClient, getAccessToken(facebookAccessToken));
    }

    /**
     * Build the Tinder client given the access token.
     * @param facebookAccessToken
     * @return Tinder
     * @throws Exception
     */
    public static Tinder fromAccessToken(String facebookAccessToken) throws Exception {
        return new Tinder(facebookAccessToken);
    }

    /**
     * Returns a list of recommendations.
     * @return recommendations
     * @throws Exception
     */
    public ArrayList<User> getRecommendations() throws Exception {
        RecommendationResponse recommendationResponse = new RecommendationResponse(
                authenticatedHttpClient.get(
                        new RecommendationRequest(BASE_URL + RecommendationRequest.URI)
                )
        );
        return recommendationResponse.getRecommendations();
    }

    /**
     * Returns the user profile information and settings.
     *
     * @return Profile
     */
    public Profile getProfile() throws Exception {
        ProfileResponse profileResponse = new ProfileResponse(authenticatedHttpClient.get(new ProfileRequest(BASE_URL + ProfileRequest.URI)));
        return profileResponse.getProfile();
    }

    /**
     * Likes a given user and returns a Like object
     * @param user
     * @return Like
     */
    public Like like(User user) throws Exception {
        LikeResponse likeResponse = new LikeResponse(
                authenticatedHttpClient.get(
                        new com.lowkey.tinder.like.LikeRequest(
                            BASE_URL + com.lowkey.tinder.like.LikeRequest.URI,
                            user.getId(),
                            user.getContentHash(),
                            user.getsNumber()
                        )
                )
        );
        return likeResponse.getLike();
    }

    /**
     * Return my tinder matches available until now as an array list
     * @return my tinder matches
     * @throws Exception
     */
    public ArrayList<Match> getMatches() throws Exception {
        com.lowkey.tinder.match.MatchResponse matchResponse = new com.lowkey.tinder.match.MatchResponse(
                authenticatedHttpClient.post(new MatchRequest(BASE_URL + MatchRequest.URI))
        );
        return matchResponse.getMatches();
    }

    /**
     * Retrieve the tinder access token in order to query the tinder api, given the facebook access token.
     * @param facebookAccessToken
     * @return accessToken
     * @throws Exception
     */
    private String getAccessToken(String facebookAccessToken) throws Exception {
        HttpPostRequest request = new AuthRequest(BASE_URL + AuthRequest.URI, facebookAccessToken);
        AuthResponse authResponse = new AuthResponse(anonymousHttpClient.post(request));
        return authResponse.getToken();
    }
}
