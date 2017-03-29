package com.example.taphan.core1.user;

import java.util.ArrayList;

/**
 * Created by taphan on 24.03.2017.
 */

public class User {
    private String userType;
    private String userID;
    private ArrayList<String> courses;

    public User() {
        courses = new ArrayList<>();
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void addCourse(String course) {
        courses.add(course);
    }

    public ArrayList<String> getCourses() {
        return courses;
    }
}
