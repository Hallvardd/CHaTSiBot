package com.example.taphan.core1;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.taphan.core1.questionDatabase.Question;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import ai.api.android.AIDataService;
import com.google.gson.JsonElement;

import com.google.firebase.auth.FirebaseAuth;

import com.example.taphan.core1.login.LoginActivity;

public class MainActivity extends AppCompatActivity implements AIListener {

    private TextView textView;
    private EditText inputText;
    private Button listenButton;
    private TextView resultTextView;
    private AIConfiguration config;
    private AIDataService aiDataService;
    private AIService aiService;
    protected TextView displayDb;
    protected ArrayList<Question> currentQuestions = new ArrayList<>();

    private Button signOutButton;

    private DatabaseReference mDatabase; //database variables
    DatabaseController dbc;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // opens an instance of the database and makes three main branches, one for each type
        // of objects
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.jsonText);
        inputText = (EditText) findViewById(R.id.edit_message);
        displayDb = (TextView) findViewById(R.id.displayDb);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        dbc = new DatabaseController();


        signOutButton = (Button) findViewById(R.id.signOutButton);

        listenButton = (Button) findViewById(R.id.listenButton);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        // CLIENT_ACCESS_TOKEN = a7ccbd15c0db40bfb729a72c12efc15f
        config = new AIConfiguration("be12980a15414ff0a8726764bb4edd79",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiDataService = new AIDataService(this,config);
        aiService.setListener(this);

        }

    public void buttonClick(View view) {

        switch(view.getId()) {

            case R.id.button:

        // Read courseCode from user input and find general information about the subject
        String input = inputText.getText().toString();
        String[] subject = input.split(" ");

        // Finding the requested data in the IME api, should always be called when possible.
        subject[0] = "http://www.ime.ntnu.no/api/course/en/" + subject[0];
        JSONTask task = new JSONTask();
        task.execute(subject);

            case R.id.signOutButton:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));

        }

    }

  // API.AI code
    public void listenButtonOnClick(final View view) throws AIServiceException{
        //resultTextView.setText(inputText.getText().toString());
        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery(inputText.getText().toString());
        new AsyncTask<AIRequest, Void, AIResponse>() {
            @Override
            protected AIResponse doInBackground(AIRequest... requests) {
                final AIRequest request = requests[0];
                try {
                    final AIResponse response = aiDataService.request(aiRequest);
                    return response;
                } catch (AIServiceException e) {
                }
                return null;
            }
            @Override
            protected void onPostExecute(AIResponse aiResponse) {
                if (aiResponse != null) {
                    //   process aiResponse here
                    // Get parameters
                    Result result = aiResponse.getResult();
                    String parameterString = "";
                    if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                        for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                            parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
                        }
                    }

                    // Send til databasen for å finne svar, kall en metode
                    // Hvis returnert False, legg den inn i unansweredQuestions in database

                    // Show results in TextView.
                    resultTextView.setText("Query:" + result.getResolvedQuery() +
                            "\nAction: " + result.getAction() +
                            "\nParameters: " + parameterString);
                }
            }
        }.execute(aiRequest);
    }

    @Override
    public void onResult(final AIResponse response) {

    }

    // Handle error
    @Override
    public void onError(AIError error) {
        resultTextView.setText(error.toString());
    }


    @Override
    public void onAudioLevel(final float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }


    public class JSONTask extends AsyncTask<String, String,String> {
        private String result; // variable to solve the problem of wrong return value in searchJson method
        private String getResult() {
            return result;
        }
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
                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null ) {
                    buffer.append(line);
                }
                // Begin parsing JSON, choose JSON objects and arrays to get hold of correct info
                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                result = "Not found";
                return searchJson(parentObject, null,"object", "course", params);

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
            return result;
        }

        /**
         * Helper method for searching after simple information in JSON
         * @param parentObject - can be either a JSONObject or null
         * @param parentArray - can be either JSONArray or null
         * @param parentType - variable to solve the problem of nested objects
         * @param key - key to the JSONObject
         * @param searchkeys - a list of keywords to search, currently only support ONE keyword
         * @return the result of search, null if unsuccessful
         * @throws JSONException
         */
        private String searchJson(JSONObject parentObject, JSONArray parentArray, String parentType, String key, String... searchkeys) throws JSONException {
            // Choose to only handle information on courses, and not from cache or request
            if(parentArray != null) {
                int i = 0;
                while (i < parentArray.length()) {
                    JSONObject currentObject = parentArray.getJSONObject(i);
                    Iterator iterator = currentObject.keys();
                    while(iterator.hasNext()) {
                        String nextKey = (String) iterator.next();
                        if(currentObject.get(nextKey) instanceof JSONObject) {
                            searchJson(currentObject.getJSONObject(nextKey), null,"object", nextKey, searchkeys);
                        } else if(currentObject.get(nextKey) instanceof String && nextKey.equals(searchkeys[1])) {
                            result = currentObject.getString(nextKey);
                            break;
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
                    } else if(currentObject.get(nextKey) instanceof JSONArray) {
                        searchJson(null, currentObject.getJSONArray(nextKey),"array", nextKey, searchkeys);
                    } else if(currentObject.get(nextKey) instanceof String && nextKey.equals(searchkeys[1])) {
                        result = currentObject.getString(nextKey);
                        break;
                    }
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String newResult) {
            // Execute our Json reader and store desired information in result
            super.onPostExecute(newResult);
            result = newResult;
            textView.setText(newResult);
            System.out.println(result);

        }

        public void signOut() {
            auth.signOut();
        }

    }

}
