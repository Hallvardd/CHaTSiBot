package com.example.taphan.core1.questionDatabase;


import java.util.ArrayList;

public class Question {

    // A question can only have one answer and refer to only one course.

    private String questionID; //key
    private String questionTxt;
    private String questionPath;


    private ArrayList<String> studentsListeners; // A list of the students which have asked the question.
    // these are notified when the question is answered and moved to a new location.

    public Question(){

    }

    public Question(String questionID, String questionTxt, String questionPath){
        this.questionID = questionID;
        this.questionTxt = questionTxt;
        this.questionPath = questionPath;
        this.studentsListeners = new ArrayList<>();

    }

    public void addSListener(String userID){
        if(!studentsListeners.contains(userID)){
            studentsListeners.add(userID);
        }
    }
    public void removeSListener(String userID){
        if(studentsListeners.contains(userID)){
            studentsListeners.remove(userID);
        }
    }

    public void setStudentsListeners(ArrayList<String> studentsListeners) {
        this.studentsListeners = studentsListeners;
    }
    public ArrayList<String> getStudentsListeners(){
        return studentsListeners;
    }

    public String getQuestionID(){
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getQuestionTxt() {
        return questionTxt;
    }

    public void setQuestionTxt(String question) {
        this.questionTxt = question;
    }

    public void setQuestionPath(String questionPath){
        this.questionPath = questionPath;
    }
    public String getQuestionPath(){
        return  questionPath;
    }

}
