package com.example.taphan.core1.course;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by taphan on 23.03.2017.
 * A course object that includes all course keys for current user
 */

public class Course {

    private String courseKey;
    private String courseName;

    public Course() {}
    public Course(String courseKey, String courseName) {
        this.courseKey = courseKey;
        this.courseName = courseName;
    }

    public String getCourseKey() {
        return courseKey;
    }

    public void setCourseKey(String courseKey) {
        this.courseKey = courseKey;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

}
