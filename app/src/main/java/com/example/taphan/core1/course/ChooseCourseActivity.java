package com.example.taphan.core1.course;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.taphan.core1.R;
import com.example.taphan.core1.chat.ChatActivity;


/**
 * Created by taphan on 23.03.2017.
 */

public class ChooseCourseActivity extends AppCompatActivity {
    public static final String TAG = "ChooseCourseActivity";

    ListView listView;
    ArrayAdapter adapter;

    // A global Course object which contains info about currently chosen course (to be used in ChatActivity)

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        // Add items to course list when initialized
        listView = (ListView) findViewById(R.id.courseview);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, AddCourseActivity.globalCourse.getCourseKeys());
        listView.setAdapter(adapter); // Use a default adapter

        // When an item in the list of courses is chosen, redirect user to ChatActivity with the corresponding course
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String chosenCourse = (parent.getItemAtPosition(position) + "").trim(); // Find the course key that was chosen
                AddCourseActivity.globalCourse.setChosenCourse(chosenCourse);
                Intent chat = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(chat); // Start chat activity with saved chosen course as a global variable
            }
        });

    }


}
