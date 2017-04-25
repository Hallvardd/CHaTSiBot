package com.example.taphan.core1.chat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taphan.core1.R;
import com.example.taphan.core1.questionDatabase.Question;
import com.example.taphan.core1.questionDatabase.State;
import com.example.taphan.core1.user.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.taphan.core1.login.LoginActivity.globalUser;
import static com.example.taphan.core1.chat.ChatActivity.currentQuestion;
import static com.example.taphan.core1.chat.ChatActivity.currentCourse;




import java.util.ArrayList;
import java.util.List;


/**
 * An Adapter class used for making messages into chat bubbles
 */

public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {
    private static final String TAG = "ChatArrayAdapter";

    private TextView chatText;
    private List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
    private Context context;

    private final static String questionBranchName ="questions";
    private final static String uaQuestionBranchName = "unansweredQuestions";
    public final static String users = "users";
    private DatabaseReference mDatabase; //database reference to our firebase database.
    private DatabaseReference uaqDatabase;


    @Override
    public void add(ChatMessage object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public ChatArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uaqDatabase = mDatabase.child(currentCourse.toLowerCase()).child(uaQuestionBranchName);
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public ChatMessage getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        ChatMessage chatMessageObj = getItem(position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(chatMessageObj.feedback) {
            row = inflater.inflate(R.layout.feedback, parent, false);
            final Button feedbackYesBtn = (Button) row.findViewById(R.id.feedbackYesButton);
            final Button feedbackNoBtn = (Button) row.findViewById(R.id.feedbackNoButton);

            feedbackYesBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(context, "Awesome, is there anything more I can do for you?", Toast.LENGTH_SHORT).show();
                    // Disable buttons after click
                    feedbackYesBtn.setEnabled(false);
                    feedbackNoBtn.setEnabled(false);
                }
            });
            feedbackNoBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(context, "Ok, the question has been sent to your professor.", Toast.LENGTH_SHORT).show();
                    feedbackYesBtn.setEnabled(false);
                    feedbackNoBtn.setEnabled(false);
                    sendQuestionBack(mDatabase,currentQuestion,currentCourse);

                }
            });

        } else {
            if (chatMessageObj.left) {
                row = inflater.inflate(R.layout.right, parent, false);
            } else {
                row = inflater.inflate(R.layout.left, parent, false);
            }
            chatText = (TextView) row.findViewById(R.id.msgr);
            chatText.setText(chatMessageObj.message);
        }
        return row;
    }

    void sendQuestionBack(final DatabaseReference database, final String questionID, final String courseCode){
        final String course = courseCode.toLowerCase();
        final DatabaseReference qDatabase = database.child(course).child(questionBranchName);
        final DatabaseReference uaqDatabase = database.child(course).child(uaQuestionBranchName);
        final String newQuestionID = uaqDatabase.push().getKey();
        qDatabase.child(questionID).addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Question q = dataSnapshot.getValue(Question.class);

                ArrayList<String> tempStudListeners = q.getStudentsListeners();
                tempStudListeners.add(globalUser.getUserID());
                final ArrayList<String> studentListeners = tempStudListeners;

                final String qID = q.getQuestionID();
                String [] path = q.getQuestionPath().split("-");
                q.setQuestionID(newQuestionID);
                uaqDatabase.child(newQuestionID).setValue(q); // Adds the question to the answered question database
                qDatabase.child(questionID).removeValue(); // Deletes the question so it won't appear in the professors feed.

                DatabaseReference pTQ = database; // initialises the path To Question
                for(String p:path){  // Iterates over the questions keywords and "extends" the branch.
                    pTQ = pTQ.child(p);
                }
                final DatabaseReference pathToQuestion = pTQ.child("state"); // adds state as the final branch of the database Reference,
                // each asked question will have "state as their final branch"
                pathToQuestion.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) { // the DataSnapshot consists of one and only one State() object
                        State snap = dataSnapshot.getValue(State.class);
                        snap.setAnswer("NA");
                        snap.setQuestionID(newQuestionID);
                        pathToQuestion.setValue(snap);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                for(String userID:studentListeners){
                    boolean b = false;
                    mDatabase.child(users).child(userID).addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User u = dataSnapshot.getValue(User.class);
                            u.removeAnsweredQuestion(courseCode,qID);
                            u.putUnansweredQuestion(courseCode,newQuestionID);
                            mDatabase.child(users).child(u.getUserID()).setValue(u);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


}