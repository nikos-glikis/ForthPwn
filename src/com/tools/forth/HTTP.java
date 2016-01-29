package com.tools.forth;


import org.apache.commons.io.IOUtils;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Nikos Glikis
 */
public class HTTP
{
    public static HttpResult request(HttpRequestInformation httpRequestInformation) throws Exception
    {
        URL url;
        HttpURLConnection connection = null;
        try
        {
            // Create connection
            url = new URL(httpRequestInformation.getUrl());
            connection = (HttpURLConnection) url.openConnection();

            Iterator it = httpRequestInformation.getHeaders().entrySet().iterator();
            while (it.hasNext())
            {
                Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
                connection.setRequestProperty(pair.getKey(), pair.getValue());
                it.remove();
            }

            //TODO maybe put below in helper.
            connection.setRequestProperty("Content-Language", "en-US");

            if (httpRequestInformation.isMethodPost())
            {
                connection.setRequestMethod("POST");
                if (!httpRequestInformation.hasHeader("Content-Type"))
                {
                    connection.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");
                    connection.setRequestProperty("Content-Length", ""
                            + Integer.toString(httpRequestInformation.getBody().getBytes().length));
                    connection.setUseCaches(false);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    // Send request
                    DataOutputStream wr = new DataOutputStream(connection
                            .getOutputStream());
                    wr.writeBytes(httpRequestInformation.getBody());
                    wr.flush();
                    wr.close();
                }
                else
                {
                    connection.setRequestProperty("Content-Type",
                            httpRequestInformation.getHeader("Content-Type"));
                }
            }
            else
            {
                connection.setRequestMethod(httpRequestInformation.getMethod());
            }

            InputStream is = connection.getInputStream();
            byte[] bytes = IOUtils.toByteArray(is);

            HttpResult httpResult = new HttpResult();
            httpResult.setContent(bytes);

            Map<String,List<String>> map = connection.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : map.entrySet())
            {
                if (entry.getKey() != null)
                {
                    List<String> l = entry.getValue();
                    Iterator<String> iter = l.iterator();
                    for (String c : l)
                    {
                        httpResult.addHeader(entry.getKey(), c);
                        //cookies += c +"; ";
                    }
                }
            }

            return httpResult;

        }
        catch (Exception e)
        {
            try
            {
                connection.disconnect();
            }
            catch (Exception ee)
            {

            }
            throw e;

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
