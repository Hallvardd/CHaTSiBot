package com.example.taphan.core1.course;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by taphan on 23.03.2017.
 * A course object that includes all course keys for current user
 */

public class Course {

    private ArrayList<String> courseKeys;
    private String chosenCourse;

    public Course() {
        courseKeys = new ArrayList<>();
    }

    public ArrayList<String> getCourseKeys() {
        return courseKeys;
    }

    public void add(String courseKey) {
        courseKeys.add(courseKey);
    }

    public void setChosenCourse(String chosenCourse) {
        this.chosenCourse = chosenCourse;
    }

    public String getChosenCourse() {
        return chosenCourse;
    }

}
