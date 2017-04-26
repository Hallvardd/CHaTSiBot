package com.example.taphan.core1.chat;

/**
 * Created by Charles on 21.03.2017.
 * The main class for chatting with bot
 */

import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
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
import com.example.taphan.core1.course.Course;
import com.example.taphan.core1.languageProcessing.AccessTokenLoader;
import com.example.taphan.core1.languageProcessing.ApiFragment;
import com.example.taphan.core1.languageProcessing.model.TokenInfo;
import com.example.taphan.core1.questionDatabase.Question;
import com.example.taphan.core1.questionDatabase.State;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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


public class ChatActivity extends AppCompatActivity implements AIListener, ApiFragment.Callback {
    private static final String TAG = "ChatActivity";

    private String client_access_token = "854903e0917e42c384b1e59d1b99af42";
    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private TextView title;

    //keeps track of the current question, for use in ChatArrayAdapter
    //where question can be sent back to the professor.
    public static String currentCourse; // The current course for this chat activity
    public static String currentQuestion;

    private AIConfiguration config;
    private AIDataService aiDataService;
    private AIService aiService;

    private DatabaseReference mDatabase; //database reference to our firebase database.
    private ArrayList<Question> qList;
    private final static String uaQuestionBranchName = "unansweredQuestions"; // path to unanswered questions.
    private final static String users = "users";

    //Cloud API
    private static final String FRAGMENT_API = "api";
    private static final int LOADER_ACCESS_TOKEN = 1;
    //Cloud API syntax analysis
    private static final List<String> FILL_VERBS = Arrays.asList("be", "is", "are");
    private static final String AUX = "AUX";
    private static final String VERB = "VERB";
    private static final String NOUN = "NOUN";
    private static final String ROOT = "ROOT";
    private static final String XCOMP = "XCOMP";
    private String nlpSyntax;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        if(AddCourseActivity.globalCourse == null) {
            AddCourseActivity.globalCourse = new Course();
            AddCourseActivity.globalCourse.setCourseKey("tdt4140");
        }

        // Set title of current chat activity corresponds to current course
        title = (TextView) findViewById(R.id.chat_title);
        currentCourse = AddCourseActivity.globalCourse.getCourseKey();
        title.setText(currentCourse.toUpperCase());

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

        // Database reference.
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // The necessary base code to connect and use API.AI
        config = new AIConfiguration(client_access_token,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiDataService = new AIDataService(this,config);
        aiService.setListener(this);

        final FragmentManager fm = getSupportFragmentManager();
        // Prepare the API
        if (getApiFragment() == null) {
            fm.beginTransaction().add(new ApiFragment(), FRAGMENT_API).commit();
        }
        prepareApi();

        // Send a welcome message
        String welcomeMsg = "Welcome! I am CHaTSiBot, here at your service. Please ask a question " +
                "and press Enter or click on that button to the right.";
        sendBotMessage(welcomeMsg);
    }

    private boolean sendChatMessage() throws AIServiceException{
        // Implement code to handle answer input from bot after globalUser input here
        // checks for empty strings, and for strings containing only white spaces.
        if(!chatText.getText().toString().isEmpty() && (chatText.getText().toString().trim().length() > 0)) {
            chatArrayAdapter.add(new ChatMessage(true, chatText.getText().toString()));
            startAnalyze();

        }
        return true;
    }

    private void sendBotMessage(String message) {
        // Implement code for answer from bot here
        chatArrayAdapter.add(new ChatMessage(false, message));
    }

    private void sendBotFeedback(ChatMessage message) {
        chatArrayAdapter.add(message);
    }


    // Language processing
    private ApiFragment getApiFragment() {
        return (ApiFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_API);
    }

    private void prepareApi() {
        // Initiate token refresh
        getSupportLoaderManager().initLoader(LOADER_ACCESS_TOKEN, null,
                new LoaderManager.LoaderCallbacks<String>() {
                    @Override
                    public Loader<String> onCreateLoader(int id, Bundle args) {
                        return new AccessTokenLoader(ChatActivity.this);
                    }

                    @Override
                    public void onLoadFinished(Loader<String> loader, String token) {
                        getApiFragment().setAccessToken(token);
                    }

                    @Override
                    public void onLoaderReset(Loader<String> loader) {
                    }
                });
    }

    private void startAnalyze() {
        final String text = chatText.getText().toString();
        getApiFragment().analyzeSyntax(text);
    }


    @Override
    public void onSyntaxReady(TokenInfo[] tokens) throws AIServiceException {

        nlpSyntax = "";
        String root = "";
        for(TokenInfo t : tokens){
            if(t.partOfSpeech.equals(NOUN) || ((t.label.equals(ROOT) && !t.label.equals(AUX) && !FILL_VERBS.contains(t.lemma)))|| t.label.equals(XCOMP) ){
                if(t.label.equals(ROOT)){
                    root = t.lemma.toLowerCase() +"-";
                }
                else{
                    nlpSyntax += t.lemma.toLowerCase() + "-";
                }
            }
        }
        if(nlpSyntax.length() > 0){
            nlpSyntax = nlpSyntax.substring(0, nlpSyntax.length() - 1);
        }
        nlpSyntax = root + nlpSyntax;
        listenButtonOnClick(nlpSyntax);
        Log.d(TAG, nlpSyntax);
    }
    // End of language processing!!


    // API.AI code
    public void listenButtonOnClick(final String nouns) throws AIServiceException {
        final AIRequest aiRequest = new AIRequest();
        if (!chatText.getText().toString().isEmpty()) {
            aiRequest.setQuery(chatText.getText().toString());
            new AsyncTask<AIRequest, Void, AIResponse>() {
                @Override
                protected AIResponse doInBackground(AIRequest... requests) {
                    final AIRequest request = requests[0];
                    try {
                        final AIResponse response = aiDataService.request(aiRequest);
                        return response;
                    } catch (AIServiceException e) {
                        Log.e(TAG,e.getMessage());
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(AIResponse aiResponse) {
                    // Here comes response from API.AI server
                    if (aiResponse != null) {
                        Result result = aiResponse.getResult();
                        String parameterString = "";
                        String key = "";
                        String value = "";
                        String searchKey = "unknown";
                        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                                value = String.valueOf(entry.getValue());
                            }
                        }

                        // If the user is asking for the exam date or course lecturer, search in Data API for it, else search database\


                        // if the length of the response from API-AI is only two characters, it is empty.
                        if (value.length() > 2) {

                            searchKey = value.replace(",", "-").replace("\"","").replace("[","").replace("]","");
                            Log.d(TAG,searchKey);
                        }

                        switch (searchKey) {
                            case "exam date": {
                                JSONTask task = new JSONTask();
                                task.execute("http://www.ime.ntnu.no/api/course/en/", currentCourse, "date");
                                break;
                            }
                            case "professor": {
                                JSONTask task = new JSONTask();
                                task.execute("http://www.ime.ntnu.no/api/course/en/", currentCourse, "displayName");
                                break;
                            }
                            default:
                                // Creates the string path for database search.
                                searchKey = currentCourse + "-" + searchKey + "-" + nlpSyntax;
                                // Calls database search.
                                searchDatabase(mDatabase, searchKey, result.getResolvedQuery());
                                chatText.setText("");
                                break;
                        }
                    }
                }
            }.execute(aiRequest);
        }

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

    // Check if the string contains only alphabetical characters
    public boolean isAlpha(String name) {
        return name.matches("[a-zA-Z]+");
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

                    // updating and writing global user back to database
                    globalUser.putUnansweredQuestion(courseCode,key);
                    mDatabase.child(users).child(globalUser.getUserID()).setValue(globalUser);

                    sendBotMessage("The question has been sent to your professor.");

                    // The question is added to the unanswered question branch of the database, allowing the professor to read it.
                    uaQuestionDB.child(key).setValue(q);
                    // adds a State() to the last leaf of the unanswered question's tree in the database.
                    dbQuestionPath.setValue(new State("NA",key));
                }
                else {
                    // Found the answer in database
                    State snap = dataSnapshot.getValue(State.class);
                    if (!snap.getAnswer().equals("NA")){
                        String answerID = snap.getAnswer();
                        String questionID = snap.getQuestionID();
                        sendBotMessage(answerID);
                        setCurrentQuestion(snap.getQuestionID());

                        // Send also the feedback button to user
                        ChatMessage message = new ChatMessage(true, "");
                        message.setFeedbackTrue();
                        sendBotFeedback(message);
                    }
                    else if (!snap.getQuestionID().isEmpty()){
                        // adds the question to the list of asked questions if the question has already been asked.
                        globalUser.putUnansweredQuestion(courseCode,snap.getQuestionID());
                        mDatabase.child(users).child(globalUser.getUserID()).setValue(globalUser);
                        sendBotMessage("Waiting for the professor to answer this question. Try again later!");

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


    // This JSONTask currently support two tasks: search for exam date of a subject and its lecturer
    public class JSONTask extends AsyncTask<String, String,String> {
        private String result; // variable to solve the problem of wrong return value in searchJson method

        /**
         * @param params = paramaters from execute(String... params)
         * This method is called when method jsonTask.execute() is called
         * The first parameter is URL of JSON, the following parameters are search keywords
         * @return value (String) if search is successful, "Not found" if otherwise
         */
        @Override
        public String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader br = null;
            try {
                // Connect to JSON
                URL u = new URL(params[0] + params[1]);
                connection = (HttpURLConnection) u.openConnection();
                connection.connect();

                // Use a buffer to store JSON info
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null ) {
                    buffer.append(line);
                }
                // Begin parsing JSON, choose JSON objects and arrays to get hold of correct info
                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                result = "Not found";
                if(parentObject.getString("course").equals("null")){
                    return result; // If user enters invalid course name, returns Not found
                } else {
                    // Else the name of course will be returned
                    return searchJson(parentObject, null, "object", "course", params);
                }

            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                // Close connection, check for null objects in order to avoid error
                if (connection != null) {
                    try {
                        connection.disconnect();
                        if(br != null) {
                            br.close();}
                    } catch (Exception ex) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return result;
        }

        /**
         * Helper method for searching after simple information in JSON
         * @param parentObject - can be either a JSONObject or null
         * @param parentArray - can be either JSONArray or null
         * @param parentType - variable to solve the problem of nested objects
         * @param key - key to the JSONObject
         * @param searchkey - a list of keywords to search, currently only support ONE keyword at index 2
         * @return the result of search, null if unsuccessful
         * @throws JSONException
         */
        private String searchJson(JSONObject parentObject, JSONArray parentArray, String parentType, String key, String... searchkey) throws JSONException {
            // Choose to only handle information on courses, and not from cache or request
            if(parentArray != null) {
                int i = 0;
                while (i < parentArray.length()) {
                    JSONObject currentObject = parentArray.getJSONObject(i);
                    Iterator iterator = currentObject.keys();
                    while(iterator.hasNext()) {
                        String nextKey = (String) iterator.next();
                        if(currentObject.get(nextKey) instanceof JSONObject) {
                            searchJson(currentObject.getJSONObject(nextKey), null,"object", nextKey, searchkey);
                        } else if(currentObject.get(nextKey) instanceof String && nextKey.equals(searchkey[2])) {
                            result = currentObject.getString(nextKey) ; // Return courseKey,courseName
                            break;
                        }
                    }
                    i++;
                }
            } else if(parentObject != null){
                JSONObject currentObject = parentObject;
                // In order to get past educationalRole-person, need to check for parent type
                if(!parentType.equals("object")) {
                    currentObject = parentObject.getJSONObject(key);
                }
                Iterator iterator = currentObject.keys();
                while(iterator.hasNext()) {
                    String nextKey = (String) iterator.next();
                    if(currentObject.get(nextKey) instanceof JSONObject) {
                        searchJson(currentObject.getJSONObject(nextKey), null,"object", nextKey, searchkey);
                    } else if(currentObject.get(nextKey) instanceof JSONArray) {
                        searchJson(null, currentObject.getJSONArray(nextKey),"array", nextKey, searchkey);
                    } else if(currentObject.get(nextKey) instanceof String && nextKey.equals(searchkey[2])) {
                        result = currentObject.getString(nextKey);
                        break;
                    }
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String newResult) {
            super.onPostExecute(newResult);

            // Send message to Bot, if result contains alphabetical characters, the task was to find lecturer, else exam date
            if(isAlpha(newResult)) {
                if (newResult.equalsIgnoreCase("Not found")) {
                    sendBotMessage("Lecturer not found.");
                } else {
                    sendBotMessage("The lecturer of this subject is: " + result);
                }
            } else {
                if (newResult.equalsIgnoreCase("Not found")) {
                    sendBotMessage("Exam date not found.");
                } else {
                    sendBotMessage("The exam date is: " + result);
                }
            }
        }
    }

    public void setCurrentQuestion(String questionID){
        this.currentQuestion = questionID;
    }

}

