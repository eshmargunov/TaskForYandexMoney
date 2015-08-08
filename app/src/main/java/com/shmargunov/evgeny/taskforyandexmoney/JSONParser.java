package com.shmargunov.evgeny.taskforyandexmoney;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Evgeny on 07.08.2015.
 */
public class JSONParser {

    static InputStream is = null;
    static JSONArray jObj = null;
    static String json = null;

    public JSONParser(){

    }

    public JSONArray getJSONfromURL(String url){
        //get data(InputStream is) from URL
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }catch (ClientProtocolException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        //Parse response from URl to String
        try {
            BufferedReader BR = new BufferedReader(new InputStreamReader(is, "UTF-8"),8);
            StringBuilder SB = new StringBuilder();
            String line = null;
            while ((line = BR.readLine()) != null){
                SB.append(line + "\n");
            }
            is.close();
            json = SB.toString();
        }catch (Exception e){
            Log.e("Buffer error", "Error converting res" +
                    "ult " + e.toString());
        }

        // get JSON from String
        try {
            jObj = new JSONArray(json);
        }catch (JSONException e){
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return  jObj;
    }
}
