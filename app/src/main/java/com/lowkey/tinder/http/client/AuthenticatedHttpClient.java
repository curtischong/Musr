package com.lowkey.tinder.http.client;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Decorator class of AnonymousHttpClient.
 * @author Diego Mariani
 * @since 05-2017
 */
public class AuthenticatedHttpClient implements HttpClient {

    /**
     * Base http client without authentication
     */
    private com.lowkey.tinder.http.client.AnonymousHttpClient http;

    /**
     * Access Token used to initialize http client headers
     */
    private String token;

    /**
     * @param http
     * @param token
     */
    public AuthenticatedHttpClient(com.lowkey.tinder.http.client.AnonymousHttpClient http, String token) {
        this.http = http;
        this.token = token;
    }

    /**
     * Used to create an authenticated get request. Accepts HttpPostRequest instance.
     * @param request
     * @return response as a string
     * @throws IOException
     */
    public String get(com.lowkey.tinder.http.request.HttpGetRequest request) throws IOException {
        Headers headersBuilder = Headers.of(buildHeaders());
        Request finalRequest = new Request.Builder()
                .url(request.getUrl())
                .headers(headersBuilder)
                .build();
        Response response = http.getHttp().newCall(finalRequest).execute();
        return response.body().string();
    }

    /**
     * Used to create an authenticated post request. Accepts HttpPostRequest instance.
     * @param request
     * @return response as a string
     * @throws IOException
     */
    public String post(com.lowkey.tinder.http.request.HttpPostRequest request) throws IOException {
        RequestBody body = RequestBody.create(com.lowkey.tinder.http.client.AnonymousHttpClient.JSON, request.getBody());
        Headers headersBuild = Headers.of(buildHeaders());
        Request finalRequesst = new Request.Builder()
                .url(request.getUrl())
                .post(body)
                .headers(headersBuild)
                .build();
        Response response = http.getHttp().newCall(finalRequesst).execute();
        return response.body().string();
    }

    /**
     * Builds the http headers needed for http token authentication.
     * @return Headers as an HashMap
     */
    private Map<String, String> buildHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("User-Agent", "Tinder/6.9.1 (iPhone; iOS 10.2; Scale/2.00)");
        headers.put("X-Auth-Token", token);
        headers.put("Host", "api.gotinder.com");
        headers.put("Authorization", String.format("Token token=\"%s\"", token));
        return headers;
    }
}
