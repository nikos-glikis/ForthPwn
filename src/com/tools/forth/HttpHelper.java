package com.tools.forth;

/**
 * Created by Nikos Glikis
 *
 * Uses HTTP to make basic requests.
 *
 */
public class HttpHelper
{
    public static HttpResult basicPostRequest(String url, String body) throws Exception
    {
        return basicPostRequest(url, body);
    }

    public static HttpResult basicPostRequest(String url, String body, String cookie) throws Exception
    {
        HttpRequestInformation httpRequestInformation = new HttpRequestInformation();
        httpRequestInformation.setUrl(url);
        if (cookie != null)
        {
            httpRequestInformation.setCookie(cookie);
        }
        httpRequestInformation.setMethodPost();
        httpRequestInformation.setBody(body);

        HttpResult httpResult = HTTP.request(httpRequestInformation);

        return httpResult;
    }
    public static HttpResult basicGetRequest(String url) throws Exception
    {
        return basicGetRequest(url, null);
    }

    public static HttpResult basicGetRequest(String url, String cookie) throws Exception
    {
        HttpRequestInformation httpRequestInformation = new HttpRequestInformation();
        httpRequestInformation.setUrl(url);
        if (cookie != null)
        {
            httpRequestInformation.setCookie(cookie);
        }
        httpRequestInformation.setMethodGet();

        HttpResult httpResult = HTTP.request(httpRequestInformation);

        return httpResult;
    }
}
