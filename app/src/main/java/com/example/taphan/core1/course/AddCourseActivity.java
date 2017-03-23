package com.example.taphan.core1.course;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.taphan.core1.R;
import com.example.taphan.core1.chat.ChatActivity;

import java.util.ArrayList;

/**
 * Created by taphan on 23.03.2017.
 */

public class AddCourseActivity extends AppCompatActivity {
    public static final String TAG = "AddCourseActivity";

    private EditText enterCourse;
    private Button addCourseButton;
    ListView listView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        enterCourse = (EditText) findViewById(R.id.enter_course);
        addCourseButton = (Button) findViewById(R.id.add_course_button);

        // Add items to course list when initialized
        listView = (ListView) findViewById(R.id.courseview);
        final CourseAdapter adapter = new CourseAdapter(getApplicationContext(), R.layout.course);
        listView.setAdapter(adapter); // Use a default adapter

        // Add a course to the Course object containing all courses
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "msg");
                String course = enterCourse.getText().toString();
                adapter.add(new Course(course));
            }
        });

        // When an item in the list of courses is chosen, redirect user to ChatActivity with the corresponding course
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String chosenCourse = (parent.getItemAtPosition(position) + "").trim(); // Find the course key that was chosen
                Intent chat = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(chat); // Start chat activity with saved chosen course as a global variable
            }
        });

    }



}