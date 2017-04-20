package com.example.taphan.core1.chat;

/**
 * Created by taphan on 22.03.2017.
 * A single message contains a string and a boolean indicating whether it is a message from bot or user
 * left = true: this is a bot message
 * left = false: this is a user message
 * feedback boolean indicates whether the message is a feedback or not
 */

public class ChatMessage {
    public boolean left;
    public String message;
    public boolean feedback;

    public ChatMessage(boolean left, String message) {
        super();
        this.left = left;
        this.message = message;
        feedback = false;
    }

    public void setFeedbackTrue() {
        feedback = true;
    }
}
