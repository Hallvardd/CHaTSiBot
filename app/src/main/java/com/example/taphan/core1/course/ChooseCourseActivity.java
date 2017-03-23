package com.example.taphan.core1.course;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;

import com.example.taphan.core1.R;

/**
 * Created by taphan on 23.03.2017.
 */

public class ChooseCourseActivity extends AppCompatActivity {

    ListView listView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        listView = (ListView) findViewById(R.id.courseview);

    }


}
