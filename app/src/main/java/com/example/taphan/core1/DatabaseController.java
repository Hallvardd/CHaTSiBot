package com.example.taphan.core1;

import android.util.Log;
import android.widget.Toast;

import com.example.taphan.core1.questionDatabase.Answer;
import com.example.taphan.core1.questionDatabase.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

class DatabaseController {

    private final static String questionBranchName ="questions";
    private final static String answerBranchName ="answers";
    private final static String uaQuestionBranchName = "unansweredQuestions";

    DatabaseController() {

    }

    // Adding courses will only be done when courses are used for the first time.
    void addCourseDatabase(DatabaseReference database, String courseCode, String profName, String email){
        DatabaseReference courseDatabase = database.child(courseCode);
        courseDatabase.child("profName").setValue(profName);
        courseDatabase.child("email").setValue(email);
        courseDatabase.child("questions");
        courseDatabase.child("answers");
    }

    void addUnansweredQuestionToDB(DatabaseReference database, String courseCode, String text){
        DatabaseReference courseQuestionDatabase = database.child(courseCode).child(uaQuestionBranchName);
        Question question = new Question();
        String key = courseQuestionDatabase.push().getKey();
        question.setQuestionID(key);
        question.setQuestionTxt(text);
        courseQuestionDatabase.child(key).setValue(question);
    }

    void addQuestionToDb(DatabaseReference database, String courseCode, String text){
        DatabaseReference courseQuestionDatabase = database.child(courseCode).child(questionBranchName);
        Question question = new Question();
        String key = courseQuestionDatabase.push().getKey();
        question.setQuestionID(key);
        question.setQuestionTxt(text);
        courseQuestionDatabase.child(key).setValue(question);
    }

    void addAnswerToDatabase(DatabaseReference database, final String courseCode,final String questionID, String answerTxt){
        Answer answer = new Answer();
        final DatabaseReference qDatabase = database.child(courseCode).child(questionBranchName);
        final DatabaseReference aDatabase = database.child(courseCode).child(answerBranchName);
        final DatabaseReference uaqDatabase = database.child(courseCode).child(uaQuestionBranchName);
        final String key = aDatabase.push().getKey();
        final String newQuestionID = qDatabase.push().getKey();
        answer.setAnswerTxt(answerTxt);
        answer.setAnswerID(key);
        answer.addQuestion(newQuestionID);
        aDatabase.child(key).setValue(answer);
        uaqDatabase.child(questionID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Question q = dataSnapshot.getValue(Question.class);
                q.setRefAnsID(key);
                qDatabase.child(newQuestionID).setValue(q);
                uaqDatabase.child(questionID).removeValue();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    // A answer can answer several questions, this method adds a reference to from a question to a existing answer
    // if there is a match
    protected void addAnsweredQuestion(DatabaseReference qDatabase,DatabaseReference aDatabase){


    }

    protected void moveQuestion(DatabaseReference database, String courseCode, String questionID){

    }
}

