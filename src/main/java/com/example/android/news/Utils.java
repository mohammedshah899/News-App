package com.example.android.news;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormatSymbols;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Utils {

    private static final String LOG_TAG = Utils.class.getSimpleName();

    private Utils(){
    }

    public static List<CustomObject> fetchURLData(String stringUrl){
        URL url = CreateUrl(stringUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG, "Error Occurred", e);
        }
        return extractJsonData(jsonResponse);
    }

    private static URL CreateUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error Occurred", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";
        if (url == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG, "Help Error, Forced Stop" + urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG, "Error occurred", e);
        }finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<CustomObject> extractJsonData(String jsonResponse){
        if (TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        // Create a new List of Custom Objects
        List<CustomObject> data = new ArrayList<>();

        try {
            JSONObject baseJsonRoot = new JSONObject(jsonResponse);
            JSONObject responseObject = baseJsonRoot.getJSONObject("response");
            int pageSize = responseObject.getInt("pageSize");
            int pages= responseObject.getInt("pages");
            int currentPage = responseObject.getInt("currentPage");
            JSONArray results = responseObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++){
                JSONObject elements = results.getJSONObject(i);
                String publicationDate = elements.getString("webPublicationDate");
                String sectionName = elements.getString("sectionName");
                String webUrl = elements.getString("webUrl");
                String pillarName = elements.getString("pillarName");
                JSONObject fields = elements.getJSONObject("fields");
                String headLine = fields.getString("headline");
                String trailText = fields.getString("trailText");
                String image = fields.getString("thumbnail");

                String date = getDate(publicationDate);
                String time = getTime(publicationDate);

                System.out.println(time);
                System.out.println(date);


                CustomObject listData = new CustomObject(headLine, trailText, webUrl, pillarName, sectionName, image);
                data.add(listData);

            }
        }catch (JSONException e){
            Log.e(LOG_TAG, "Error Occurred", e);
        }
        return data;
    }

    private static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }

    private static String getDate(String Date){
        String[] dateArray = Date.split("T");
        String[] dateElements = dateArray[0].split("-");
        String year = dateElements[0];
        String month = dateElements[1];
        String day = dateElements[2];
        String strDate = getMonth(Integer.parseInt(month))+" "+day+"'"+year;
        return strDate;
    }

    private static String getTime(String Time){
        String time = Time;
        String strTime;
        String[] timeArray  = time.split("T");
        String[] timeElements = timeArray[1].split(":");
        int hours = Integer.parseInt(timeElements[0]);
        int minutes = Integer.parseInt(timeElements[1]);

        if (hours < 13){
            strTime = hours+":"+minutes +" AM";
        }
        else{
            strTime = hours+":"+minutes+" PM";
        }
        return strTime;
    }


}
