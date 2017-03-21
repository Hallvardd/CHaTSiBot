package com.example.taphan.core1;



import android.widget.TextView;

import com.example.taphan.core1.questionDatabase.Answer;
import com.example.taphan.core1.questionDatabase.Question;
import com.example.taphan.core1.questionDatabase.State;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;



class DatabaseController {
    private final static String questionBranchName ="questions";
    private final static String answerBranchName ="answers";
    private final static String uaQuestionBranchName = "unansweredQuestions";

    DatabaseController() {}
    /*
     searchDatabase() uses the returned keywords from API-AI to find if a question has an answer or not. If the
     question is answered the path will exist with and the answer branch will contain a String
     containing an reference to an answer object.

     if the question is not answered the answer branch will be empty and an unanswered question will
     be added to the database. The to avoid duplicates a reference to the question will also be
     added to the path.
     */
    void searchDatabase(final DatabaseReference database, final String path, final String questionTxt, final TextView textView){
        final String[] pathArray = path.split("-");
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
                    Question q = new Question(key,questionTxt,path); // a question object is created with reference to the path.
                    textView.setText(key);
                    uaQuestionDB.child(key).setValue(q); // The question is added to the unanswered question branch of the database, allowing the professor to read it.
                    dbQuestionPath.setValue(new State("NA",key));
                }
                else {
                    State snap = dataSnapshot.getValue(State.class);
                    textView.setText(snap.getAnswerID());
                    if (!snap.getAnswerID().equals("NA")){
                        String answerID = snap.getAnswerID();
                        DatabaseReference answerDb = database.child(courseCode).child(answerBranchName).child(answerID);
                        answerDb.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Answer answer = dataSnapshot.getValue(Answer.class);
                                // setText view as the answer
                                textView.setText(answer.getAnswerTxt());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                    else if (!snap.getQuestionID().isEmpty()) {
                        textView.setText("The question has already been asked \nThe question will be added to your list of asked questions");
                        // Todo add question to a students list of asked questions.
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void addCourseDatabase(DatabaseReference database, String courseCode, String profName, String email){
        DatabaseReference courseDatabase = database.child(courseCode);
        courseDatabase.child("profName").setValue(profName);
        courseDatabase.child("email").setValue(email);
        courseDatabase.child("questions");
        courseDatabase.child("answers");
    }


    void addAnswerToDatabase(final DatabaseReference database, final String questionID, String answerTxt){
        Answer answer = new Answer();
        final DatabaseReference aDatabase = database.child(answerBranchName);
        final DatabaseReference uaqDatabase = database.child(uaQuestionBranchName);
        final DatabaseReference qDatabase = database.child(questionBranchName);
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
    void addAnsweredQuestion(DatabaseReference database, String courseCode, String questionTxt, String questionKeywords, final String answerID, String questionPath){
        final DatabaseReference qDatabase = database.child(courseCode).child(questionBranchName);
        final DatabaseReference aDatabase = database.child(courseCode).child(answerBranchName);
        final String key = qDatabase.push().getKey();
        Question q = new Question(key,questionTxt, questionPath);
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

