package com.example.taphan.core1.questionDatabase;


public class Question {

    // A question can only have one answer and refer to only one course.

    private String questionID; //key
    private String refAnsID;
    private String questionTxt;
    private String refCourseCode;

    public Question(){

    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getRefAnsID() {
        return refAnsID;
    }

    public void setRefAnsID(String refAnsID) {
        this.refAnsID = refAnsID;
    }

    public String getQuestionTxt() {
        return questionTxt;
    }

    public void setQuestionTxt(String question) {
        this.questionTxt = question;
    }

    public String getRefCourseCode() {
        return refCourseCode;
    }

    public void setRefCourseCode(String refCourseCode) {
        this.refCourseCode = refCourseCode;
    }
}

