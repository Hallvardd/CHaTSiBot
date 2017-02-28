package com.example.taphan.core1.questionDatabase;

import java.util.ArrayList;

// A Course is referenced by questions and is a directory for questions
// If a course is not in the database it should be added by accessing the IME dataAPI,
// where subjectCode, emailAddress and profName should be added to the database.
// If the lookup fails, the course does not exist, and should thus not be created

public class Course{

    public static String COURSE_CODE = "courseCode";

    private String courseCode; //Key
    private String emailAddress;
    private String profName;
    private ArrayList<String> questionKeys;


    public Course(){}

    public void addQuestion(String questionID){
        questionKeys.add(questionID);
    }

    public void removeQuestion(String key){
        String question = questionKeys.get(questionKeys.indexOf(key));
        // Will need to access the database to find a way to remove the question;


    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getProfName() {
        return profName;
    }

    public void setProfName(String profName) {
        this.profName = profName;
    }
}
