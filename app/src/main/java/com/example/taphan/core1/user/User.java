package com.example.taphan.core1.user;


import java.util.ArrayList;
import java.util.HashMap;

public class User {

    private String userID; // this is the key and must therefore be unique.
    private String userType;
    private HashMap<String, ArrayList<String>> questionsAsked;  //(fagkode, list of questions)
    private String email;

    public User() {

    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserType(String type) {
        userType = type;
    }

    public String getUserType() {
        return userType;
    }

    public ArrayList<String> getQuestions(String courseCode) {
        return questionsAsked.get(courseCode);
    }

    public void addQuestion(String courseCode, String questionID) {
        if(!questionsAsked.get(courseCode).contains(questionID)) {
            questionsAsked.get(courseCode).add(questionID);
        }
    }

    public void removeQuestion(String courseCode, String questionID) {
        if(questionsAsked.get(courseCode).contains(questionID)) {
            questionsAsked.get(courseCode).remove(questionID);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
