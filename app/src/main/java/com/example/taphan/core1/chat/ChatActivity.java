package com.example.taphan.core1.chat;

/**
 * Created by Charles on 21.03.2017.
 * The main class for chatting with bot
 */

import android.database.DataSetObserver;
import android.os.AsyncTask;
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

import com.example.taphan.core1.R;
import com.example.taphan.core1.course.AddCourseActivity;
import com.example.taphan.core1.questionDatabase.Question;
import com.example.taphan.core1.questionDatabase.State;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Map;

import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import static com.example.taphan.core1.login.LoginActivity.globalUser;

public class ChatActivity extends AppCompatActivity implements AIListener{
    private static final String TAG = "ChatActivity";

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private TextView title;
    private TextView invisible;
    private String currentCourse; // The current course for this chat activity

    private AIConfiguration config;
    private AIDataService aiDataService;
    private AIService aiService;

    private DatabaseReference mDatabase; //database reference to our firebase database.
    private String course; // placeholder for variable deciding which questions to read from and answer.
    private ArrayList<Question> qList;
    private final static String uaQuestionBranchName = "unansweredQuestions"; // path to unanswered questions.
    private final static String users = "users";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        // Set title of current chat activity corresponds to current course
        title = (TextView) findViewById(R.id.chat_title);
        currentCourse = AddCourseActivity.globalCourse.getCourseKey();
        title.setText(currentCourse.toUpperCase());

        // Make an invisible TextView to store information from database, and send it to bot
        invisible = (TextView) findViewById(R.id.invisible_text);

        buttonSend = (Button) findViewById(R.id.send);

        // The listview to show chat bubbles
        listView = (ListView) findViewById(R.id.msgview);
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);

        // User input is accepted by both pressing "Send" button and the "Enter" key
        chatText = (EditText) findViewById(R.id.msg);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    try{
                        return sendChatMessage();
                    } catch(AIServiceException e) {}
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try{
                    sendChatMessage();
                } catch (AIServiceException e) {}
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        // to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });


        // Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // The necessary base code to connect and use API.AI
        config = new AIConfiguration("be12980a15414ff0a8726764bb4edd79",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiDataService = new AIDataService(this,config);
        aiService.setListener(this);

        // Send a welcome message
        String welcomeMsg = "Welcome! I am CHaTSiBot, here at your service. Please ask a question " +
                "and press Enter or click on that button to the right.";
        sendBotMessage(welcomeMsg);
    }

    private boolean sendChatMessage() throws AIServiceException{
        // Implement code to handle answer input from bot after globalUser input here
        chatArrayAdapter.add(new ChatMessage(true, chatText.getText().toString()));
        listenButtonOnClick();
        chatText.setText("");
        //sendBotMessage();
        return true;
    }

    private void sendBotMessage(String message) {
        // Implement code for answer from bot here
        chatArrayAdapter.add(new ChatMessage(false, message));
    }

    // API.AI code
    public void listenButtonOnClick() throws AIServiceException {
        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery(chatText.getText().toString());

        new AsyncTask<AIRequest, Void, AIResponse>() {
            @Override
            protected AIResponse doInBackground(AIRequest... requests) {
                final AIRequest request = requests[0];
                try {
                    final AIResponse response = aiDataService.request(aiRequest);
                    return response;
                } catch (AIServiceException e) {
                }
                return null;
            }
            @Override
            protected void onPostExecute(AIResponse aiResponse) {
                if (aiResponse != null) {
                    //   process aiResponse here
                    // Get parameters
                    Result result = aiResponse.getResult();
                    String parameterString = "";
                    String key = "";
                    String value = "";
                    if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                        for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                            key = entry.getKey();
                            value = entry.getValue() + "";
                            parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
                        }
                    }

                    // Hvis returnert False, legg den inn i unansweredQuestions in database
                    searchDatabase(mDatabase, key, result.getResolvedQuery());
                }
            }
        }.execute(aiRequest);
    }

    @Override
    public void onResult(final AIResponse response) {

    }

    // Handle error
    @Override
    public void onError(AIError error) {
        //resultTextView.setText(error.toString());
    }


    @Override
    public void onAudioLevel(final float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    public void searchDatabase(final DatabaseReference database, String path, final String questionTxt){
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
        final String courseCode = currentCourse;
        final DatabaseReference dbQuestionPath = d.child("state");
        dbQuestionPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    DatabaseReference uaQuestionDB = database.child(courseCode).child(uaQuestionBranchName);
                    String key = uaQuestionDB.push().getKey();
                    Question q = new Question(key,questionTxt,lcPath); // a question object is created with reference to the path.
                    q.addSListener(globalUser.getUserID());
                    globalUser.putUnansweredQuestion(courseCode,key);
                    mDatabase.child(users).child(globalUser.getUserID()).setValue(globalUser);
                    sendBotMessage("The question has been sent to your professor.");
                    uaQuestionDB.child(key).setValue(q); // The question is added to the unanswered question branch of the database, allowing the professor to read it.
                    dbQuestionPath.setValue(new State("NA",key));
                }
                else {
                    State snap = dataSnapshot.getValue(State.class);
                    if (!snap.getAnswer().equals("NA")){
                        String answerID = snap.getAnswer();
                        sendBotMessage(answerID);

                    }
                    else if (!snap.getQuestionID().isEmpty()){
                        // adds the question to the list of asked questions if the question has already been asked.
                        globalUser.putUnansweredQuestion(courseCode,snap.getQuestionID());
                        mDatabase.child(users).child(globalUser.getUserID()).setValue(globalUser);
                        sendBotMessage("The question has already been asked. I'll add it to your personal question list");

                        //Updating user
                        globalUser.putUnansweredQuestion(currentCourse, snap.getQuestionID());
                        mDatabase.child(users).child(globalUser.getUserID()).setValue(globalUser);

                        // Adding the user to the questions list of users listening.
                        final DatabaseReference questionRef = mDatabase.child(courseCode).child(uaQuestionBranchName).child(snap.getQuestionID());
                        questionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Question unasweredQuestion = dataSnapshot.getValue(Question.class);
                                if (!unasweredQuestion.getStudentsListeners().contains(globalUser.getUserID())){
                                    unasweredQuestion.addSListener(globalUser.getUserID());
                                    questionRef.setValue(unasweredQuestion);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e(TAG,"Error upon reading database: searchDatabase() case: Question already answered");

                            }
                        });

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
