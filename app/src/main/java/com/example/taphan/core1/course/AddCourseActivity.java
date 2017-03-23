package com.example.taphan.core1.course;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.taphan.core1.R;

/**
 * Created by taphan on 23.03.2017.
 * When the addCourseButton is clicked, the course should be added to the list in ChooseCourseActivity
 */

public class AddCourseActivity extends AppCompatActivity {
    public static final String TAG = "AddCourseActivity";

    private EditText enterCourse;
    private Button addCourseButton;
    public static Course globalCourse;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        enterCourse = (EditText) findViewById(R.id.enter_course);
        addCourseButton = (Button) findViewById(R.id.add_course_button);
        globalCourse = new Course();

        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalCourse.add(enterCourse.getText().toString());
            }
        });
    }

}