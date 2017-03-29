package com.example.taphan.core1.course;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by taphan on 23.03.2017.
 * A course object that includes all course keys for current user
 */

public class Course {

    private String courseKey;

    public Course(String courseKey) {
        this.courseKey = courseKey;
    }

    public String getCourse() {
        return courseKey;
    }

}
