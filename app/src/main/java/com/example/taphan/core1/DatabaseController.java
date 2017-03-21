package com.example.taphan.core1;



import android.widget.TextView;

import com.example.taphan.core1.questionDatabase.Answer;
import com.example.taphan.core1.questionDatabase.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;



class DatabaseController {
    private final static String questionBranchName ="questions";
    private final static String answerBranchName ="answers";
    private final static String uaQuestionBranchName = "unansweredQuestions";

    DatabaseController() {
    }
    // Adding courses will only be done when courses are used for the first time.
    // courses are not object, but branches of the database, the question database and
    // answer database in these branches are on the same level as the course info
    void addCourseDatabase(DatabaseReference database, String courseCode, String profName, String email){
        DatabaseReference courseDatabase = database.child(courseCode);
        courseDatabase.child("profName").setValue(profName);
        courseDatabase.child("email").setValue(email);
        courseDatabase.child("questions");
        courseDatabase.child("answers");
    }

    void addUnansweredQuestionToDB(DatabaseReference database, String courseCode, String text, String questionKeywords){
        DatabaseReference courseQuestionDatabase = database.child(courseCode).child(uaQuestionBranchName);
        String key = courseQuestionDatabase.push().getKey();
        Question question = new Question(key,text);
        courseQuestionDatabase.child(key).setValue(question);
    }

    void addAnswerToDatabase(final DatabaseReference database, final String courseCode, final String questionID, String answerTxt){
        Answer answer = new Answer();
        final DatabaseReference aDatabase = database.child(courseCode).child(answerBranchName);
        final DatabaseReference uaqDatabase = database.child(courseCode).child(uaQuestionBranchName);
        final DatabaseReference qDatabase = database.child(courseCode).child(questionBranchName);
        final String answerKey = aDatabase.push().getKey();
        final String newQuestionID = qDatabase.push().getKey();

        answer.setAnswerTxt(answerTxt);
        answer.setAnswerID(answerKey);
        answer.addQuestion(newQuestionID);
        uaqDatabase.child(questionID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Question q = dataSnapshot.getValue(Question.class);
                q.setRefAnsID(answerKey);
                qDatabase.child(newQuestionID).setValue(q);
                uaqDatabase.child(questionID).removeValue();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        aDatabase.child(answerKey).setValue(answer);
    }

    // A answer can answer several questions, this method adds a reference to from a question to a existing answer
    // if there is a match
    void addAnsweredQuestion(DatabaseReference database, String courseCode, String questionTxt, String questionKeywords, final String answerID){
        final DatabaseReference qDatabase = database.child(courseCode).child(questionBranchName);
        final DatabaseReference aDatabase = database.child(courseCode).child(answerBranchName);
        final String key = qDatabase.push().getKey();
        Question q = new Question(key,questionTxt);
        q.setRefAnsID(answerID);
        qDatabase.child(key).setValue(q);
        aDatabase.child(answerID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Answer a = dataSnapshot.getValue(Answer.class);
                a.addQuestion(key);
                aDatabase.child(answerID).setValue(a);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}

