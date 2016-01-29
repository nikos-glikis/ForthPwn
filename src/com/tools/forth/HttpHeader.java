package com.tools.forth;

/**
 * Created by Nikos Glikis on 23/1/2016.
 */
public class HttpHeader
{
    String key;
    String value;

    public HttpHeader(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
