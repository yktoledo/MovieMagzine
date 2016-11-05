package org.app.anand.moviemagzine2.Web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by User on 11/3/2016.
 */

public class HttpManager {

    public static String getData(RequestPackage p)
    {


        BufferedReader reader = null;
        String uri = p.getUri();
        if(p.getMethod().equals("GET")) {
            uri += "?" + p.getEncodedParams();
        }

        try
        {
            URL url=new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(p.getMethod());
            //JSONObject json = new JSONObject(p.getParams());
            //String params = "params" + json.toString();

            StringBuilder sb=new StringBuilder();
            reader=new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while((line= reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }

            return sb.toString();

        }catch(Exception e)
        {
            e.printStackTrace();
            return null;
        } finally
        {
            if(reader != null)
            {
                try
                {
                    reader.close();
                } catch (IOException ioe)
                {
                    ioe.printStackTrace();
                    return null;
                }
            }
        }

    }
}
