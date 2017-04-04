package com.example.taphan.core1.user;


import com.example.taphan.core1.course.Course;
import java.util.ArrayList;
import java.util.HashMap;

public class User {

    //TODO: make code more secure in case of invalid data entry/access.
    private String userID; // this is the key and must therefore be unique.
    private String userType;
    private HashMap<String, ArrayList<String>> unansweredQuestions;
    private HashMap<String, ArrayList<String>> answeredQuestions;
    private ArrayList<Course> uCourses;
    private String email;

    public User(){
        unansweredQuestions = new HashMap<>();
        answeredQuestions = new HashMap<>();
        uCourses = new ArrayList<>();
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

    public HashMap<String, ArrayList<String>> getUnansweredQuestions() {
        return unansweredQuestions;
    }

    public void setUnansweredQuestions(HashMap<String, ArrayList<String>> unansweredQuestions) {
        this.unansweredQuestions = unansweredQuestions;
    }

    public HashMap<String, ArrayList<String>> getAnsweredQuestions() {
        return answeredQuestions;
    }

    public void setAnsweredQuestions(HashMap<String, ArrayList<String>> answeredQuestions) {
        this.answeredQuestions = answeredQuestions;
    }

    public ArrayList<Course> getuCourses() {
        return uCourses;
    }

    public void setuCourses(ArrayList<Course> uCourses) {
        this.uCourses = uCourses;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void putUnansweredQuestion(String courseCode, String questionID){
        // Checks if a entry already exists
        if(unansweredQuestions.containsKey(courseCode)){
            // If the question already is in the list it is not added
            if(!unansweredQuestions.get(courseCode).contains(questionID)) {
                unansweredQuestions.get(courseCode).add(questionID);
            }
        }
        // If there is no entry corresponding to the courseCode, an new entry and ArrayList is made.
        else{
            ArrayList<String> questionIDList = new ArrayList<>();
            questionIDList.add(questionID);
            unansweredQuestions.put(courseCode,questionIDList);
        }

    }

    public void addCourse(Course course){
        if(!uCourses.contains(course)){
            uCourses.add(course);
        }
    }

    public void removeCourse(Course course){
        if(uCourses.contains(course)){
            uCourses.remove(course);
        }
    }

    public void putAnsweredQuestion(String courseCode, String questionID){
        // Checks if a entry already exists
        if(answeredQuestions.containsKey(courseCode)){
            // If the question already is in the list it is not added
            if(!answeredQuestions.get(courseCode).contains(questionID)) {
                answeredQuestions.get(courseCode).add(questionID);
            }
        }
        // If there is no entry corresponding to the courseCode, an new entry and ArrayList is made.
        else{
            ArrayList<String> questionIDList = new ArrayList<>();
            questionIDList.add(questionID);
            answeredQuestions.put(courseCode,questionIDList);
        }

    }

    // if the question id is present in the Map it is removed
    public void removeUnansweredQuestion(String courseCode, String qustionID){
        if(unansweredQuestions.containsKey(courseCode)){
            if(unansweredQuestions.get(courseCode).contains(qustionID)){
                unansweredQuestions.get(courseCode).remove(qustionID);
            }
        }

    }
    // if the question id is present in the Map it is removed
    public void removeAnsweredQuestion(String courseCode, String qustionID){
        if(answeredQuestions.containsKey(courseCode)){
            if(answeredQuestions.get(courseCode).contains(qustionID)){
                answeredQuestions.get(courseCode).remove(qustionID);
            }
        }


    }

}
