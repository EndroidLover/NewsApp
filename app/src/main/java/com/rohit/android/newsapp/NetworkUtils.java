package com.rohit.android.newsapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class NetworkUtils {




    public static String BASEURL = "https://content.guardianapis.com/search?api-key=9180f8ae-3436-47a4-ada2-8fdd3b4fe531";
    public static ArrayList<News> performOperations(String s){
        // getting a built url
        URL url = buildurl(s);
        //Log.e("buildurl","url build hon ton baad######"+url.toString());
        // making an http request
        String inputStream = null;

        try {
            inputStream = makeHttpRequest(url);
        } catch (IOException e) {
           Log.e("NetworkUtils","Cannot get the http",e);
        }

        ArrayList<News> news = parseJson(inputStream);

        return news;

    }


    public static URL buildurl(String s){
        Uri uri = Uri.parse(BASEURL).buildUpon().appendQueryParameter("q",s).build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        HttpURLConnection urlConnection = null;
        urlConnection = (HttpURLConnection)url.openConnection();
        InputStream is = null;
          if (urlConnection.getResponseCode() == 200){
              Log.e("network_Utilites","aagi http request "+urlConnection.getResponseCode());
        is = urlConnection.getInputStream();
        }
            else
              Log.e("networkUtilities","cannot get the http response********"+urlConnection.getResponseCode());

        Scanner scanner = new Scanner(is);
        scanner.useDelimiter("\\A");
        boolean hasInput = scanner.hasNext();
        if(hasInput)
            return scanner.next();
        else
            return null;
    }

    public static ArrayList<News> parseJson(String JsonResponse){

        ArrayList<News> news = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(JsonResponse);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for(int i=0;i<3;i++){
                JSONObject object = results.getJSONObject(i);
                String title = object.getString("webTitle");
                String url = object.getString("webUrl");
                news.add(new News(title,url));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return news;
    }
}
