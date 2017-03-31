package com.example.taphan.core1.course;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.taphan.core1.ProfActivity;
import com.example.taphan.core1.R;
import com.example.taphan.core1.chat.ChatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.taphan.core1.login.LoginActivity.globalUser;

/**
 * Created by taphan on 23.03.2017.
 */

public class AddCourseActivity extends AppCompatActivity {
    public static final String TAG = "AddCourseActivity";

    private EditText enterCourse;
    private Button addCourseButton;
    public static Course globalCourse;
    ListView listView;
    private DatabaseReference mDatabase;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        enterCourse = (EditText) findViewById(R.id.enter_course);
        addCourseButton = (Button) findViewById(R.id.add_course_button);

        // Add items to course list when initialized
        listView = (ListView) findViewById(R.id.courseview);
        final CourseAdapter adapter = new CourseAdapter(getApplicationContext(), R.layout.course);
        listView.setAdapter(adapter); // Use a default adapter

        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Add courses from current user to listView
        mDatabase.child("users").child(globalUser.getUserID()).child("courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot cSnapShot: dataSnapshot.getChildren()){
                    String course = cSnapShot.getKey();
                    adapter.add(new Course(course)); // Add to listView and show user their current courses
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // Add a course to the Course object containing all courses globalCourse.getCourseKey()
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String course = enterCourse.getText().toString();
                adapter.add(new Course(course));
                enterCourse.setText("");

                // Add course to Firebase for current user
                // TODO connect to IME Data API to search for course name in accordance to key for better database entry
                mDatabase.child("users").child(globalUser.getUserID()).child("courses").child(course).setValue(course);
            }
        });

        // When an item in the list of courses is chosen, redirect user to ChatActivity with the corresponding course
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                globalCourse = (Course) parent.getItemAtPosition(position); // Find
                Bundle courseCodeBundle = new Bundle();
                courseCodeBundle.putString("courseCode", globalCourse.getCourseKey());
                Intent chat;// the course key that was chosen
                if(globalUser.getUserType().equalsIgnoreCase("Professor")){
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

}