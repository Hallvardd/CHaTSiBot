package com.example.taphan.core1;



import android.widget.TextView;

import com.example.taphan.core1.questionDatabase.Answer;
import com.example.taphan.core1.questionDatabase.Question;
import com.example.taphan.core1.questionDatabase.State;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DatabaseController {
    private final static String questionBranchName ="questions";
    private final static String uaQuestionBranchName = "unansweredQuestions";

    public DatabaseController() {}

    public void searchDatabase(final DatabaseReference database, String path, final String questionTxt, final TextView textView){
        /*
        searchDatabase() uses the returned keywords from API-AI to find if a question has an answer or not. If the
        question is answered the path will exist with and the answer branch will contain a String
        containing an reference to an answer object.
        if the question is not answered the answer branch will be empty and an unanswered question will
        be added to the database. The to avoid duplicates a reference to the question will also be
        added to the path.
        */

        final String lcPath = path.toLowerCase(); // sets path to lowercase
        final String[] pathArray = lcPath.split("-");
        DatabaseReference d = database;
        for(String s:pathArray){ // for each list in the
            d = d.child(s);
        }
        final String courseCode = pathArray[0];
        final DatabaseReference dbQuestionPath = d.child("state");
        dbQuestionPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    DatabaseReference uaQuestionDB = database.child(courseCode).child(uaQuestionBranchName);
                    String key = uaQuestionDB.push().getKey();
                    Question q = new Question(key,questionTxt,lcPath); // a question object is created with reference to the path.
                    textView.setText("The question has been sent to your professor.");
                    uaQuestionDB.child(key).setValue(q); // The question is added to the unanswered question branch of the database, allowing the professor to read it.
                    dbQuestionPath.setValue(new State("NA",key));
                }
                else {
                    State snap = dataSnapshot.getValue(State.class);
                    textView.setText(snap.getAnswerID());
                    if (!snap.getAnswerID().equals("NA")){
                        String answerID = snap.getAnswerID();
                        textView.setText(answerID);
                    }
                    else if (!snap.getQuestionID().isEmpty()) {
                        textView.setText("The question has already been asked \nThe question will be added to your list of asked questions");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    void addAnswerToDatabase(final DatabaseReference database, final String questionID, String courseCode, final String answerTxt){
        Answer answer = new Answer();
        courseCode = courseCode.toLowerCase();
        final DatabaseReference uaqDatabase = database.child(courseCode).child(uaQuestionBranchName);
        final DatabaseReference qDatabase = database.child(courseCode).child(questionBranchName);
        final String newQuestionID = qDatabase.push().getKey();
        uaqDatabase.child(questionID).addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Question q = dataSnapshot.getValue(Question.class);
                String [] path = q.getQuestionPath().split("-");
                q.setQuestionID(newQuestionID);
                DatabaseReference pTQ = database;
                for(String p:path){                             // Iterates over the questions keywords and "extends" the branch.
                    pTQ = pTQ.child(p);
                }
                final DatabaseReference pathToQuestion = pTQ.child("state"); // adds state as the final branch of the database Reference,
                // each asked question will have "state as their final branch"
                pathToQuestion.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) { // the DataSnapshot consists of one and only one State() object
                        State snap = dataSnapshot.getValue(State.class);
                        snap.setAnswerID(answerTxt); // possible to use an answer object later.
                        snap.setQuestion(newQuestionID);
                        pathToQuestion.setValue(snap);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //q.setRefAnsID(answerKey);
                qDatabase.child(newQuestionID).setValue(q); // Adds the question to the answered question database
                uaqDatabase.child(questionID).removeValue(); // Deletes the question so it won't appear in the professors feed.
                // NB! as of now we don't keep the answered questions saved in the database. this should be implemented later, to allow bad answers and questions to be deleted
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //    aDatabase.child(answerKey).setValue(answer); not used for now!
    }
}


