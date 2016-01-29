package com.tools.forth;

import java.util.HashMap;

/**
 * Created by Nikos Glikis on 23/1/2016.
 */
public class HttpRequestInformation
{
    public final String METHODS_POST = "POST";
    public final String METHODS_GET = "GET";
    public final String METHODS_PUT = "PUT";
    public final String METHODS_DELETE = "DELETE";
    public final String METHODS_PATCH = "PATCH";
    public final String METHODS_LINK = "LINK";

    String method = METHODS_GET;
    String url;
    String body;
    HashMap<String, String> headers = new HashMap<String, String>();

    public void setCookie(String cookie)
    {
        this.setHeader("Cookie", cookie);
    }

    public boolean isMethodPost()
    {
        return method.equals(METHODS_POST);
    }

    public boolean isMethodGet()
    {
        return method.equals(METHODS_GET);
    }

    public boolean isMethodDelete()
    {
        return method.equals(METHODS_DELETE);
    }

    public boolean isMethodPut()
    {
        return method.equals(METHODS_PUT);
    }

    public boolean isMethodPatch()
    {
        return method.equals(METHODS_PATCH);
    }

    public boolean isMethodLink()
    {
        return method.equals(METHODS_LINK);
    }

    public void setMethodPost()
    {
        this.method = METHODS_POST;
    }

    public void setMethodGet()
    {
        this.method = METHODS_GET;
    }

    public void setMethodPut()
    {
        this.method = METHODS_PUT;
    }

    public void setMethodDelete()
    {
        this.method = METHODS_DELETE;
    }

    public void setMethodPatch()
    {
        this.method = METHODS_PATCH;
    }

    public void setMethodLink()
    {
        this.method = METHODS_LINK;
    }


    public void setHeader(String key, String value)
    {
        headers.put(key, value);
    }

    public HashMap<String, String> getHeaders()
    {
        return headers;
    }

    public String getHeader( String key)
    {
        return headers.get(key);
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean hasHeader(String key)
    {
        String header = headers.get(key);

        if (header == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public String getMethod()
    {
        return method;
    }
}
