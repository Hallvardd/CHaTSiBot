package com.example.taphan.core1.questionDatabase;

import java.util.ArrayList;

/**
 * Created by Halst on 06.03.2017.
 */

public class Professor implements User {

    private String userName; // this is the key and must therefore be unique.
    private String firstName;
    private String lastName;
    private String password; // use regex to verify.
    private ArrayList<Question> unansweredQuestions;

    Professor(){
        this.unansweredQuestions = new ArrayList<>();
    }

    Professor(String userName, String firstName, String lastName, String passWord){
        this.unansweredQuestions = new ArrayList<>();
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = passWord;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String passWord) {
        this.password = passWord;
    }
}
