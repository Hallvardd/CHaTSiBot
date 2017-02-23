package com.example.taphan.core1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.taphan.core1";
    private TextView textView;
    private EditText inputText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.jsonText);
        inputText = (EditText) findViewById(R.id.edit_message);
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // Read fagkode from user input and find general information about the subject
            String subject = inputText.getText().toString();
            new JSONTask().execute("http://www.ime.ntnu.no/api/course/en/" + subject, "studyLevelCode");
            }
        });
    }


    public class JSONTask extends AsyncTask<String, String,String> {

        /**
         * @param params = paramaters from execute(String... params)
         * The first parameter is URL of JSON, the following parameters are search keywords
         * @return value (String) if search is successful, null if otherwise
         */
        @Override
        public String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader br = null;
            try {
                // Connect to JSON
                URL u = new URL(params[0]);
                connection = (HttpURLConnection) u.openConnection();
                connection.connect();

                // Use a buffer to store JSON info
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer buffer = new StringBuffer();
                String line="";
                while ((line = br.readLine()) != null ) {
                    buffer.append(line);
                }

                // Begin parsing JSON, choose JSON objects and arrays to get hold of correct info
                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);

                return searchJson(parentObject, null,"object", "course", params);

            } catch (MalformedURLException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                // Close connection, check for null objects in order to avoid error
                if (connection != null) {
                    try {
                        connection.disconnect();
                        if(br != null) {
                            br.close();}
                    } catch (Exception ex) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return null;
        }


        /**
         * Helper method for searching after simple information in JSON
         * @param parentObject - can be either a JSONObject or null
         * @param parentArray - can be either JSONArray or null
         * @param key - key to the JSONObject
         * @param searchkeys - a list of keywords to search, currently only support ONE keyword
         * @return the result of search, null if unsuccessful
         * @throws JSONException
         */
        private String searchJson(JSONObject parentObject, JSONArray parentArray, String parentType, String key, String... searchkeys) throws JSONException {
            // Choose to only handle information on courses, and not from cache or request
            if(key.equals(searchkeys[1])) {
                System.out.println("test key equals");
                return key;
            }
            if(parentArray != null) {
                int i = 0;
                while (i < parentArray.length()) {
                    JSONObject currentObject = parentArray.getJSONObject(i);
                    Iterator iterator = currentObject.keys();
                    while(iterator.hasNext()) {
                        String nextKey = (String) iterator.next();
                        if(currentObject.get(nextKey) instanceof JSONObject) {
                            searchJson(currentObject.getJSONObject(nextKey), null,"object", nextKey, searchkeys);
                        } else if(currentObject.get(nextKey) instanceof String) {
                            if (nextKey.equals(searchkeys[1])) {
                                System.out.println("Found the key in array! " + nextKey);
                                return currentObject.getString(nextKey);
                            }
                        }
                    }
                    i++;
                }
            } else if(parentObject != null){
                JSONObject currentObject = parentObject;
                // In order to get past educationalRole-person, need to check for parent type
                if(!parentType.equals("object")) {
                    currentObject = parentObject.getJSONObject(key);
                }
                Iterator iterator = currentObject.keys();
                while(iterator.hasNext()) {
                    String nextKey = (String) iterator.next();
                    if(currentObject.get(nextKey) instanceof JSONObject) {
                        searchJson(currentObject.getJSONObject(nextKey), null,"object", nextKey, searchkeys);
                    } else if(currentObject.get(nextKey) instanceof String) {
                        if (nextKey.equals(searchkeys[1])) {
                            System.out.println("Found the word! " + nextKey);
                            return currentObject.getString(nextKey);
                        }
                    } else if(currentObject.get(nextKey) instanceof JSONArray) {
                        searchJson(null, currentObject.getJSONArray(nextKey),"array", nextKey, searchkeys);
                    }
                }
            }
            return "Not found";
        }


        /**
         * Convert JSONObject into a map, can then iterate through map (must iterate twice, use as last option)
         * @param json
         * @param out
         * @return a map containing all JSONObject
         * @throws JSONException
         */
        private Map<String,String> parse(JSONObject json , Map<String,String> out) throws JSONException{
            Iterator<String> keys = json.keys();
            while(keys.hasNext()){
                String key = keys.next();
                String val = null;
                if ( json.get(key) instanceof JSONObject ) {
                    JSONObject value = json.getJSONObject(key);
                    parse(value,out);
                }

                else {
                    val = json.getString(key);
                }

                if(val != null){
                    out.put(key,val);
                }
            }
            return out;
        }


        @Override
        protected void onPostExecute(String result) {
            // Execute our Json reader and store desired information in result
            super.onPostExecute(result);
            textView.setText(result);

        }
    }

}
