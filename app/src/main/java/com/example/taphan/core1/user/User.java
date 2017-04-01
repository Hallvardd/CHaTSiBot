package com.example.taphan.core1.user;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class User {

    //TODO: make code more secure in case of invalid data entry/access.
    private String userID; // this is the key and must therefore be unique.
    private String userType;
    private HashMap<String, ArrayList<String>> questionsAsked;
    private String email;

    public User(){
        questionsAsked = new HashMap<>();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public HashMap<String, ArrayList<String>> getQuestionsAsked() {
        return questionsAsked;
    }

    public void setQuestionsAsked(HashMap<String, ArrayList<String>> questionsAsked) {
        this.questionsAsked = questionsAsked;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getCourses(){
        return questionsAsked.keySet().toArray(new String[0]);
    }
}
