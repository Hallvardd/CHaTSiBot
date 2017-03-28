package com.example.taphan.core1;


import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.example.taphan.core1.layoutClass.ChatArrayAdapter;
import com.example.taphan.core1.layoutClass.ChatMessage;
import com.example.taphan.core1.questionDatabase.Answer;
import com.example.taphan.core1.questionDatabase.Question;
import com.example.taphan.core1.questionDatabase.State;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfActivity extends AppCompatActivity {
    private static final String TAG = "ProfActivity";
    Bundle bundle;
    private TextView tv; // this variable is only here to use for adding test values to the database. And should be deleted
    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private DatabaseController dbc; // creates a databaseController to access firebase data.
    private DatabaseReference mDatabase; //database reference to our firebase database.
    private DatabaseReference uaqDatabase;
    private String courseCode; // placeholder for variable deciding which questions to read from and answer.
    private ArrayList<Question> qList;
    private String lastQuestion; // used to check if the question has already been printed, to avoid duplicates
    private final static String questionBranchName ="questions";
    private final static String uaQuestionBranchName = "unansweredQuestions";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof);

        buttonSend = (Button) findViewById(R.id.send);
        listView = (ListView) findViewById(R.id.msgview);
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);

        lastQuestion = ""; // Initial setup, no questions have been displayed.

        bundle = getIntent().getExtras(); //
        courseCode = bundle.getString("courseCode");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uaqDatabase = mDatabase.child(courseCode.toLowerCase()).child(uaQuestionBranchName);
        dbc = new DatabaseController();

        qList = new ArrayList<>();
        tv = (TextView) findViewById(R.id.resulttv);


        // User input is accepted by both pressing "Send" button and the "Enter" key
        chatText = (EditText) findViewById(R.id.msg);

        /*chatText.setOnKeyListener(new View.OnKeyListener() {
        // User input is accepted by both pressing "Send" button and the "Enter" key
        chatText = (EditText) findViewById(R.id.msg);


        //ONLY FOR ADDING TEST VALUES
        /*
        dbc.searchDatabase(mDatabase,"TDT4140-Definition-Software-App","What is an application?", tv);
        dbc.searchDatabase(mDatabase,"TDT4140-Definition-Software-Word","What is MS Word?", tv);
        */

        // fills the list with questions for the first time.
        fillQList(courseCode);

        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return (event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) && answerQuestion();
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                answerQuestion();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        // to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged(){
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });

        // refills lists when database is altered in real time
        uaqDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fillQList(courseCode);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fillQList(courseCode);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
               fillQList(courseCode);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                fillQList(courseCode);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean answerQuestion(){
        if(!qList.isEmpty()) {
            Question q = qList.get(0);
            String answerTxt = (chatText.getText().toString());
            addAnswerToDatabase(mDatabase, q.getQuestionID(), courseCode, answerTxt); //takes in attributes from the question and uses it to answer the question and delete the unanswered question.
            sendProfAnswer(answerTxt);
        }
        return true;
    }

    public void setQList(ArrayList<Question> qList){
        this.qList = qList;
    }

    public void fillQList(String courseCode){

        uaqDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<Question> questionList = new ArrayList<Question>();
            boolean firstItem = true;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot qSnap: dataSnapshot.getChildren()){
                    Question q = qSnap.getValue(Question.class);
                    questionList.add(q);
                    if(firstItem){
                        if(!q.getQuestionTxt().equals(lastQuestion)) { // checks if the question has already been printed to the screen.
                            lastQuestion = q.getQuestionTxt();
                            sendStudentQuestion(q.getQuestionTxt());
                        }
                        tv.setText(q.getQuestionTxt());
                        firstItem = false;
                    }
                }
                setQList(questionList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private boolean sendProfAnswer(String answer) {
        // Implement code to handle answer input from bot after globalUser input here
        // Professor answers are displayed on the left side of the screen. This is determined by the false value in the parameter of ChatMessage.
        chatArrayAdapter.add(new ChatMessage(false, chatText.getText().toString()));
        chatText.setText("");
        return true;
    }

    private boolean sendStudentQuestion(String questionTxt) {
        // Implement code for answer from bot here
        // Student questions are displayed on the right side of the screen. This is determined by the true value in the parameter of ChatMessage.
        chatArrayAdapter.add(new ChatMessage(true, questionTxt));
        chatText.setText("");
        return true;
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
