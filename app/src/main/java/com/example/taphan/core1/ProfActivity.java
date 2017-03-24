package com.example.taphan.core1;

/**
 * Created by Charles on 21.03.2017.
 */

import android.database.DataSetObserver;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.TextView;

import com.example.taphan.core1.layoutClass.ChatArrayAdapter;
import com.example.taphan.core1.layoutClass.ChatMessage;
import com.example.taphan.core1.questionDatabase.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfActivity extends AppCompatActivity {
    private static final String TAG = "ProfActivity";

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private boolean side = true;
    private TextView tv;

    private DatabaseController dbc; // creates a databaseController to access firebase data.
    private DatabaseReference mDatabase; //database reference to our firebase database.
    private final String course = "TAC101"; // placeholder for variable deciding which questions to read from and answer.
    private ArrayList<Question> qList;
    private final static String uaQuestionBranchName = "unansweredQuestions"; // path to unanswered questions.


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof);

        buttonSend = (Button) findViewById(R.id.send);

        listView = (ListView) findViewById(R.id.msgview);
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        dbc = new DatabaseController();
        qList = new ArrayList<>();
        tv = (TextView) findViewById(R.id.resulttv);

        fillQList("TAC101");

        // User input is accepted by both pressing "Send" button and the "Enter" key
        chatText = (EditText) findViewById(R.id.msg);

        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return (event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) && sendStudentQuestion(chatText.getText().toString());
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendStudentQuestion(chatText.getText().toString());
            }
        });


//        Adding test questions for demonstration should follow this format
//        dbc.searchDatabase(mDatabase,"TAC101-price-vegetable-tomato","How much does a tomato cost?", tv);

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
    }


    public void fillQList(String courseCode){
        DatabaseReference subjectQuestionsRef = mDatabase.child(courseCode).child(uaQuestionBranchName);
        subjectQuestionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean a = true;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot qSnap: dataSnapshot.getChildren()){
                    Question q = qSnap.getValue(Question.class);
                    qList.add(q);
                    if(a){
                        tv.setText(q.getQuestionTxt());
                        sendStudentQuestion(q.getQuestionTxt());
                        a = false;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private boolean sendProfAnswer(String answer) {
        // Implement code to handle answer input from bot after user input here
        chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString()));
        chatText.setText("");
        side = false; // Switch side everytime there is a new message
        return true;
    }

    private boolean sendStudentQuestion(String question) {
        // Implement code for answer from bot here
        chatArrayAdapter.add(new ChatMessage(side, question));
        chatText.setText("");
        side = true; // Switch side everytime there is a new message
        return true;
    }

    public void sendMsg(View view) {
        if(!qList.isEmpty()) {
            Question q = qList.get(0);
            dbc.addAnswerToDatabase(mDatabase,q.getQuestionID(),course,chatText.getText().toString()); //takes in attributes from the question and uses it to answer the question and delete the unanswered question.
            sendProfAnswer(chatText.toString());
            qList.remove(0); // removes the first item of the list, the question is also removed from the database by the above method for answering the question. 
            if(!qList.isEmpty()){
                sendStudentQuestion(qList.get(0).getQuestionTxt());
            }
            else{
                sendStudentQuestion("There are no more questions right now");
            }
        }
    }

}
