package com.example.taphan.core1.user;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class User {

    //TODO: make code more secure in case of invalid data entry/access.
    private String userID; // this is the key and must therefore be unique.
    private String userType;
    private HashMap<String, ArrayList<String>> questionsAsked;  //(fagkode, list of questions)
    private String email;

    public User() {
        this.questionsAsked = new HashMap<>();
        ArrayList<String> as = new ArrayList<>();
        as.add("noquestion");
        as.add("Every");
        questionsAsked.put("TAC010",as);
    }

    public void createCourseQAsked(String courseCode) {
        questionsAsked.put(courseCode, new ArrayList<String>());
    }

    public String[] getCourses(){
        return questionsAsked.keySet().toArray(new String[0]);
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
