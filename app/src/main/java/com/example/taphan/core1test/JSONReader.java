package com.example.taphan.core1test;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class JSONReader {


    public String getJSON(String url, int timeout) {
        HttpURLConnection c = null;
        BufferedReader br = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();
            switch (status) {
                case 200:
                case 201:
                    br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = br.readLine()) != null ) {
                        sb.append(line);
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (c != null) { // Else would get an error trying to close a null object
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    public Map<String, Object> jsonToMap(String t){
        Map<String, Object> retMap = new Gson().fromJson(t, new TypeToken<HashMap<String, Object>>() {}.getType());
        return retMap;
    }

    public static void main(String[] args) {
        JSONReader a = new JSONReader();
        Scanner uIn = new Scanner(System.in);
        String input = uIn.next(); // Test: input=tdt4105
        uIn.close();
        String subjectInfo = (a.getJSON("http://www.ime.ntnu.no/api/course/en/" + input, 1000));
        Map<String, Object> map = a.jsonToMap(subjectInfo);
        Map<String, Object> course = (Map<String, Object>) map.get("course");
        System.out.println(course);

    }
}