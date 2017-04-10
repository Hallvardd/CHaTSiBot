package com.example.taphan.core1.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taphan.core1.R;

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

    @Override
    public void add(ChatMessage object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public ChatArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
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
            Button feedbackYesBtn = (Button) row.findViewById(R.id.feedbackYesButton);
            Button feedbackNoBtn = (Button) row.findViewById(R.id.feedbackNoButton);

            feedbackYesBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(context, "Awesome, is there anything more I can do for you?", Toast.LENGTH_SHORT).show();
                }
            });
            feedbackNoBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(context, "Ok, the question has been sent to your professor.", Toast.LENGTH_SHORT).show();
                    // TODO Implement sending question to professor here

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


}