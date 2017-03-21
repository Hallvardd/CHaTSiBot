package com.example.taphan.core1;

/**
 * Created by Charles on 21.03.2017.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ScrollView;
import android.widget.TextView;

import com.example.taphan.core1.Messages.MessageAdapter;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;


import java.util.ArrayList;

public class ProfActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView greeting;

    EditText messageInput;
    Button sendButton;

    MessageAdapter messageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof);

        greeting = (TextView) findViewById(R.id.bot_msg);
        greeting.setText("Hello professor! I am Indian too!");

        messageInput = (EditText) findViewById(R.id.message_input);
        sendButton = (Button) findViewById(R.id.send_button);

        sendButton.setOnClickListener(this);
        messageAdapter = new MessageAdapter(this, new ArrayList<String>());
        final ScrollView messagesView = (ScrollView) findViewById(R.id.messages_view);
        //messagesView.setAdapter(messageAdapter);

        // initialize Pusher
        Pusher pusher = new Pusher("70d36468efcb07076e81");

        // subscribe to our "messages" channel
        Channel channel = pusher.subscribe("messages");

        // listen for the "new_message" event
        channel.bind("new_message", new SubscriptionEventListener() {

            @Override
            public void onEvent(String channelName, String eventName, final String data) {
                messageAdapter.add(data);
            }

        });

        // connect to the Pusher API
        pusher.connect();

    }



    @Override
    public void onClick(View v) {
        postMessage();
    }

    private void postMessage() {
        // TODO post to Adapter???
    }
}