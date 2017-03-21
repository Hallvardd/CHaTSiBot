package com.example.taphan.core1;

/**
 * Created by Charles on 21.03.2017.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.taphan.core1.MessageAdapter;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;


import java.util.ArrayList;

public class ProfActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView greeting;

    EditText messageInput;
    public Button sendButton;

    MessageAdapter messageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof);

        greeting = (TextView) findViewById(R.id.botMsg);
        greeting.setText("Hello professor! I am Indian too!");

        messageInput = (EditText) findViewById(R.id.message_input);
        sendButton = (Button) findViewById(R.id.send_button);
        
        sendButton.setOnClickListener(this);
        messageAdapter = new MessageAdapter(this, new ArrayList<String>());
        final ListView messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);


    }



    @Override
    public void onClick(View v) {
        postMessage();
    }

    private void postMessage() {
        // TODO post to Adapter???

    }

}
