package com.example.taphan.core1.chat;

/**
 * Created by Charles on 21.03.2017.
 * The main class for chatting with bot
 */

import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.TextView;

import com.example.taphan.core1.R;
import com.google.gson.JsonElement;

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

import static com.example.taphan.core1.course.AddCourseActivity.globalCourse;

public class ChatActivity extends AppCompatActivity implements AIListener{
    private static final String TAG = "ChatActivity";

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private TextView title;
    private String currentCourse; // The current course for this chat activity

    private AIConfiguration config;
    private AIDataService aiDataService;
    private AIService aiService;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        // Set title of current chat activity corresponds to current course
        title = (TextView) findViewById(R.id.chat_title);
        currentCourse = globalCourse.getChosenCourse();
        title.setText(currentCourse);

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

        // The necessary base code to connect and use API.AI
        config = new AIConfiguration("be12980a15414ff0a8726764bb4edd79",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiDataService = new AIDataService(this,config);
        aiService.setListener(this);
    }

    private boolean sendChatMessage() throws AIServiceException{
        // Implement code to handle answer input from bot after user input here
        chatArrayAdapter.add(new ChatMessage(true, chatText.getText().toString()));
        listenButtonOnClick();
        chatText.setText("");
        //sendBotMessage();
        return true;
    }

    private void sendBotMessage(String message) {
        // Implement code for answer from bot here
        chatArrayAdapter.add(new ChatMessage(false, message));
        chatText.setText("");
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
                    if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                        for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                            parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
                        }
                    }

                    // Send til databasen for å finne svar, kall en metode
                    // Hvis returnert False, legg den inn i unansweredQuestions in database

                    // Send answer from bot
                    sendBotMessage("Query:" + result.getResolvedQuery() +
                            "\nAction: " + result.getAction() +
                            "\nParameters: " + parameterString);
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

}
