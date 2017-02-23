package com.example.taphan.core1test;

import android.content.Intent;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.taphan.core1test";
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.jsonText);
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask().execute("http://www.ime.ntnu.no/api/course/en/course/tma4105");
            }
        });
    }

    public class JSONTask extends AsyncTask<String, String,String> {

        @Override
        public String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader br = null;
            try {
                URL u = new URL(params[0]);
                connection = (HttpURLConnection) u.openConnection();
                connection.connect();

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer buffer = new StringBuffer();
                String line="";

                while ((line = br.readLine()) != null ) {
                    buffer.append(line);
                }

                // Begin parsing JSON, choose JSON objects and arrays to get hold of correct info
                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);

                /*JSONObject requestObject = parentObject.getJSONObject("request"); // code+year gir course/tma41052016
                String code = requestObject.getString("courseCode");
                int year = requestObject.getInt("year");
                */

                JSONObject courseObject = parentObject.getJSONObject("course");

                /*JSONArray educationalRoleArray = courseObject.getJSONArray("educationalRole");
                JSONObject finalObject = educationalRoleArray.getJSONObject(0);
                String lecturer = finalObject.getString("code");
                JSONObject personObject = finalObject.getJSONObject("person");
                String dispName = personObject.getString("displayName");
                */

                return null; // buffer.toString() is the entire JSON

            } catch (MalformedURLException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) { // Else would get an error trying to close a null object
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

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            textView.setText(result);

        }
    }

   /* public void onClick(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }*/
}
