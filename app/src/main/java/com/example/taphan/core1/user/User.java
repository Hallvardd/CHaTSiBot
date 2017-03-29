package com.example.taphan.core1.user;

/**
 * Created by taphan on 24.03.2017.
 */

public class User {
    private String userType;
    private String courses;

    public User() {
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setCourses(String courses) {
        this.courses = courses;
    }

    public String getUserType() {
        return userType;
    }

    public String getCourses() {
        return courses;
    }
}
