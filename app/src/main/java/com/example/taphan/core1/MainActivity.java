package com.example.taphan.core1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.taphan.core1.questionDatabase.Answer;
import com.example.taphan.core1.questionDatabase.Course;
import com.example.taphan.core1.questionDatabase.Question;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private DatabaseReference mDatabase; //database variables
    private DatabaseReference courseBranch;
    private DatabaseReference questionBranch;
    private DatabaseReference answerBranch;    // end

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // opens an instance of the database and makes three main branches, one for each type
        // of objects
        DatabaseControll dbc = new DatabaseControll();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        courseBranch = mDatabase.child("Course");
        questionBranch = mDatabase.child("Question");
        answerBranch = mDatabase.child("Answer");
        courseBranch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Course course = dataSnapshot.getValue(Course.class);
                System.out.println(course.getCourseCode());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        questionBranch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Question question = dataSnapshot.getValue(Question.class);
                System.out.println(question);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        answerBranch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Answer answer = dataSnapshot.getValue(Answer.class);
                System.out.println(answer);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        // Only for demo
        dbc.addCourseDatabase(courseBranch, "TDT4140", "Pekka Kalevi Abrahamsson", "pekka.abrahamsson@ntnu.no");
        dbc.addQuestionDatabase(questionBranch, courseBranch, "TDT4140","When is the project due?");
        dbc.addQuestionDatabase(questionBranch, courseBranch, "TDT4140","When is the deadline for our project?");
        dbc.addCourseDatabase(courseBranch, "TDT4145", "Svein Inge something", "Svein@ntnu.no");
        dbc.addAnswerDatabase(); //some answer
        //

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.jsonText);
        inputText = (EditText) findViewById(R.id.edit_message);
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // Read fagkode from user input and find general information about the subject
            String input = inputText.getText().toString();
                String[] subject = input.split(" ");
                subject[0] = "http://www.ime.ntnu.no/api/course/en/" + subject[0];
            new JSONTask().execute(subject);
            }
        });
    }


    public class JSONTask extends AsyncTask<String, String,String> {

        private String result; // variable to solve the problem of wrong return value in searchJson method
        /**
         * @param params = paramaters from execute(String... params)
         * The first parameter is URL of JSON, the following parameters are search keywords
         * @return value (String) if search is successful, null if otherwise
         */
        @Override
        public String doInBackground(String... params) {
            result = "Not found";
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
