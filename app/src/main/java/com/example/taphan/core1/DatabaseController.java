package com.example.taphan.core1;

import android.util.Log;
import com.example.taphan.core1.questionDatabase.Answer;
import com.example.taphan.core1.questionDatabase.Course;
import com.example.taphan.core1.questionDatabase.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DatabaseController {

    protected DatabaseController() {

    }

    // Adding courses will only be done when courses are used for the first time.
    protected void addCourseDatabase(DatabaseReference cDatabase, String courseCode, String profName, String email){
        Course course = new Course();
        course.setCourseCode(courseCode);
        course.setProfName(profName);
        course.setEmailAddress(email);
        cDatabase.child(courseCode).setValue(course); //Adds a new course entry and sets coursCode as key
    }

    protected void addQuestionDatabase(DatabaseReference qDatabase, String courseCode, String text ){
        Question question = new Question();
        String key = qDatabase.push().getKey();
        question.setQuestionID(key);
        question.setRefCourseCode(courseCode);
        question.setQuestionTxt(text);
        qDatabase.child(key).setValue(question);
    }

    protected void addAnswerDatabase(DatabaseReference aDatabase, DatabaseReference qDatabase, String questionKey, String answerTxt){
        Answer answer = new Answer();
        String key = aDatabase.push().getKey();
        answer.setAnswerTxt(answerTxt);
        // Needs code to access question in database, this needs to be two separate databases, and the question might have to be moved later.
        answer.setAnswerID(key);
        answer.addQuestion(questionKey);
       // qDatabase.updateChildren(); // needs to do something
        aDatabase.child(key).setValue(answer);
    }

    protected void moveQuestion(){
        // should move the a question containing an answer to the answeredQuestionDatabase
    }


    // why did I create this method?
    protected void addQuestionToCourse(DatabaseReference cDatabase, Course course, String questionID){
        course.addQuestion(questionID);
        cDatabase.child(course.getCourseCode()).setValue(course);
    }


}

