package com.lowkey.tinder.http.client;

import java.io.IOException;

/**
 * @author Diego Mariani
 * @since 05-2017
 */
public interface HttpClient {
    String post(com.lowkey.tinder.http.request.HttpPostRequest httpPostRq) throws IOException;
    String get(com.lowkey.tinder.http.request.HttpGetRequest httpGetRq) throws IOException;
}
