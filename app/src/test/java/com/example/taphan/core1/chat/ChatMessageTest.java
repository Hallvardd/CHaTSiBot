package com.example.taphan.core1.chat;


import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * Created by taphan on 26.04.2017.
 */


public class ChatMessageTest {
    private final boolean left = true;
    private final String message = "Message";
    private final boolean feedback = false;

    @Test
    public void testChatMessageConstructor() throws NoSuchFieldException, IllegalAccessException {
        final ChatMessage testConstructorCourse = new ChatMessage(left,message);
        final Field leftField = testConstructorCourse.getClass().getDeclaredField("left");
        final Field messageField = testConstructorCourse.getClass().getDeclaredField("message");
        final Field feedbackField = testConstructorCourse.getClass().getDeclaredField("feedback");

        Assert.assertEquals(leftField.get(testConstructorCourse), left);
        Assert.assertEquals(messageField.get(testConstructorCourse), message);
        Assert.assertEquals(feedbackField.get(testConstructorCourse), feedback);

    }
}
