package com.example.taphan.core1.course;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.taphan.core1.ProfActivity;
import com.example.taphan.core1.R;
import com.example.taphan.core1.chat.ChatActivity;
import com.example.taphan.core1.user.User;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.taphan.core1.login.LoginActivity.globalUser;

/**
 * Created by taphan on 23.03.2017.
 */

public class AddCourseActivity extends AppCompatActivity {
    public static final String TAG = "AddCourseActivity";

    private EditText enterCourse;
    private Button addCourseButton;
    public static Course globalCourse;
    private DatabaseReference mDatabase;
    private final static String users = "users";
    ListView listView;
    CourseAdapter adapter;
    String userType;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        // Prevent crash when doing unit test
        if(globalUser == null) {
            globalUser = new User();
            globalUser.setUserType("TA");
            globalUser.setIsTa(false);
        }
        userType = globalUser.getUserType();

        enterCourse = (EditText) findViewById(R.id.enter_course);
        addCourseButton = (Button) findViewById(R.id.add_course_button);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Add items to course list when initialized
        listView = (ListView) findViewById(R.id.courseview);
        adapter = new CourseAdapter(getApplicationContext(), R.layout.course);
        listView.setAdapter(adapter); // Use a default adapter

        // Check for userType, and display the right list of courses
        final ArrayList<Course> userCourse;
        if((userType.equalsIgnoreCase("TA")&& globalUser.getIsTa())|| userType.equalsIgnoreCase("Professor"))
            userCourse = globalUser.gettCourses();
        else
            userCourse = globalUser.getuCourses();
        for(Course course : userCourse)
            adapter.add(course);

        // Add a course to the Course object containing all courses globalCourse.getCourseKey()
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String course = enterCourse.getText().toString();

                // Add course to Firebase for current user
                // Make sure that a user can only be associated with one course either as a Professor/TA or as a student
                if(!globalUser.getuCourses().contains(course) && !globalUser.gettCourses().contains(course)) {
                    // Check if the course name is valid
                    JSONTask task = new JSONTask();
                    task.execute("http://www.ime.ntnu.no/api/course/en/", course, "name");
                }
                enterCourse.setText("");
            }
        });

        // When an item in the list of courses is chosen, redirect user to ChatActivity with the corresponding course
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                globalCourse = (Course) parent.getItemAtPosition(position); // Find
                Bundle courseCodeBundle = new Bundle();

                // Transfer chosen course code to new activity (which will be set to title of page)
                courseCodeBundle.putString("courseCode", globalCourse.getCourseKey()); // TODO determine is bundle or global variable is best fit for transferring chosen course to the next activity
                Intent chat;// the course key that was chosen

                // Start the right activity according to user type
                if((userType.equals("TA")&& globalUser.getIsTa() == true)|| userType.equalsIgnoreCase("Professor")){
                    chat = new Intent(getApplicationContext(), ProfActivity.class);
                    chat.putExtras(courseCodeBundle);

                } else {
                    chat = new Intent(getApplicationContext(), ChatActivity.class);
                    chat.putExtras(courseCodeBundle);
                }
                startActivity(chat); // Start chat activity with saved chosen course as a global variable

            }
        });

    }

    // JSON Task to check whether the added course is in correct format
    public class JSONTask extends AsyncTask<String, String,String> {
        private String result; // variable to solve the problem of wrong return value in searchJson method

        /**
         * @param params = paramaters from execute(String... params)
         * This method is called when method jsonTask.execute() is called
         * The first parameter is URL of JSON, the following parameters are search keywords
         * @return value (String) if search is successful, "Not found" if otherwise
         */
        @Override
        public String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader br = null;
            try {
                // Connect to JSON
                URL u = new URL(params[0] + params[1]);
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
                if(parentObject.getString("course").equals("null")){
                    return result; // If user enters invalid course name, returns Not found
                } else {
                    // Else the name of course will be returned
                    return searchJson(parentObject, null, "object", "course", params);
                }

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
         * @param searchkey - a list of keywords to search, currently only support ONE keyword at index 2
         * @return the result of search, null if unsuccessful
         * @throws JSONException
         */
        private String searchJson(JSONObject parentObject, JSONArray parentArray, String parentType, String key, String... searchkey) throws JSONException {
            // Choose to only handle information on courses, and not from cache or request
            if(parentArray != null) {
                int i = 0;
                while (i < parentArray.length()) {
                    JSONObject currentObject = parentArray.getJSONObject(i);
                    Iterator iterator = currentObject.keys();
                    while(iterator.hasNext()) {
                        String nextKey = (String) iterator.next();
                        if(currentObject.get(nextKey) instanceof JSONObject) {
                            searchJson(currentObject.getJSONObject(nextKey), null,"object", nextKey, searchkey);
                        } else if(currentObject.get(nextKey) instanceof String && nextKey.equals(searchkey[2])) {
                            result = searchkey[1] + "," +  currentObject.getString(nextKey) ; // Return courseKey,courseName
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
                        searchJson(currentObject.getJSONObject(nextKey), null,"object", nextKey, searchkey);
                    } else if(currentObject.get(nextKey) instanceof JSONArray) {
                        searchJson(null, currentObject.getJSONArray(nextKey),"array", nextKey, searchkey);
                    } else if(currentObject.get(nextKey) instanceof String && nextKey.equals(searchkey[2])) {
                        result = searchkey[1] + "," +  currentObject.getString(nextKey);
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
            if(result.equals("Not found")) {
                // Make a popup to indicate incorrect user input of course key
                Toast.makeText(getApplicationContext(), getString(R.string.add_course_failed), Toast.LENGTH_LONG).show();
            } else{
                // Put it into our list of added courses for current user
                String[] courseKeyName = result.split(",");
                String courseKey = courseKeyName[0];
                String courseName = courseKeyName[1];
                Course course = new Course(courseKey, courseName);
                adapter.add(course);
                Log.d(TAG, "Added to adapter");
                if((userType.equals("TA")&& globalUser.getIsTa())|| userType.equalsIgnoreCase("Professor")) {
                    globalUser.addtCourse(course);
                    Log.d(TAG, "A teaching course was added");
                } else {
                    globalUser.adduCourse(course);
                    Log.d(TAG, "A student course was added");
                }
                // writes the user back to the database, only if the userObject has a userID, and is not empty.
                if (!globalUser.getUserID().equals("")) {
                    mDatabase.child(users).child(globalUser.getUserID()).setValue(globalUser);
                }
            }
        }

    }

}