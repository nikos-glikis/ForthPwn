package com.tools.forth;

import java.util.ArrayList;
import java.util.Vector;

public class HttpResult
{
    byte content[];
    String cookie;
    ArrayList<HttpHeader> headers = new ArrayList<HttpHeader>();

    /**
     * Adds a header. (can be multiple Set-Cookie for example)
     */
    public void addHeader(String key, String value)
    {
        headers.add(new HttpHeader(key, value));
    }

    public ArrayList<HttpHeader> getHeaders(String key)
    {
        ArrayList<HttpHeader> relevant = new ArrayList<HttpHeader>();

        for (HttpHeader header : headers)
        {
            if (header.getKey().equals(key))
            {
                relevant.add(header);
            }
        }
        return relevant;
    }

    /**
     * Returns A Vector containing all Cookies Values.
     * @return
     */
    public Vector<String> getCookiesVector()
    {
        ArrayList<HttpHeader> httpHeaders = getHeaders("Set-Cookie");
        Vector<String> cookies = new Vector<String>();
        for (HttpHeader httpHeader : httpHeaders)
        {
            cookies.add(httpHeader.getValue());
        }
        return cookies;
    }

    /**
     * Returns cookies from com.tools.forth.HttpResult as browser is sending them.
     * //TODO clean these a bit.
     * @return
     */
    public String getCookiesString()
    {
        ArrayList<HttpHeader> httpHeaders = getHeaders("Set-Cookie");
        String cookies = "";
        for (HttpHeader httpHeader : httpHeaders)
        {
            //cookies.add(httpHeader.getValue());
            cookies += cookies += httpHeader.getValue() +"; ";
        }
        return cookies;
    }

    public ArrayList<HttpHeader> getHeaders()
    {
        return headers;
    }

    public byte[] getContent()
    {
        return content;
    }

    public String getContentAsString()
    {
        return new String(content);
    }

    public void setContent(byte[] content)
    {
        this.content = content;
    }

    public String getCookie()
    {
        return cookie;
    }

    public void setCookie(String cookie)
    {
        this.cookie = cookie;
    }
}
