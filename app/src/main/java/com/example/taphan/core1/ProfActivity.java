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
    private static final String TAG = "ChatActivity";

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
        tv =(TextView) findViewById(R.id.resulttv);
        fillQList("TAC101");
        String s ="Well";
        for(Question q:qList){
            s += "Hello" + q.getQuestionTxt();
        }



        // User input is accepted by both pressing "Send" button and the "Enter" key
        chatText = (EditText) findViewById(R.id.msg);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return (event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) && sendStudentQuestion();
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendStudentQuestion();
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
    }

    public void fillQList(String course){
        DatabaseReference subjectQuestionsRef = mDatabase.child(course).child(uaQuestionBranchName);
        subjectQuestionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot qSnap: dataSnapshot.getChildren()){
                    Question q = qSnap.getValue(Question.class);
                    qList.add(q);
                    tv.setText("Funker");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void addQuestionToList(Question q){
        qList.add(q);
        tv.setText(q.getQuestionTxt());
    }

    private boolean sendStudentQuestion() {
        // Implement code to handle answer input from bot after user input here
        chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString()));
        chatText.setText("");
        side = !side; // Switch side everytime there is a new message
        sendProfAnswer();
        return true;
    }

    private boolean sendProfAnswer() {
        // Implement code for answer from bot here
        chatArrayAdapter.add(new ChatMessage(side, "I am a bot"));
        chatText.setText("");
        side = !side; // Switch side everytime there is a new message
        return true;
    }


    /*
    private boolean sendChatMessage() {
        // Implement code to handle answer input from bot after user input here
        chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString()));
        chatText.setText("");
        side = !side; // Switch side everytime there is a new message
        sendBotMessage();
        return true;
    }

    private boolean sendBotMessage() {
        // Implement code for answer from bot here
        chatArrayAdapter.add(new ChatMessage(side, "I am a bot"));
        chatText.setText("");
        side = !side; // Switch side everytime there is a new message
        return true;
    }
    */

}
