package com.example.taphan.core1;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.taphan.core1.chat.ChatActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.example.taphan.core1.LongListMatchers.withItemContent;

/**
 * Created by hallvard on 24.04.17.
 */

@RunWith(AndroidJUnit4.class)
public class ChatActivityTest {
    public static final String TEST_QUESTION = "What is SEMAT?";
    @Rule
    public IntentsTestRule<ChatActivity> mActivityRule = new IntentsTestRule<ChatActivity>(ChatActivity.class){
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            return new Intent(targetContext, ChatActivity.class);
        }
    };

    @Test
    public void askQuestionTest() {
        onView(withId(R.id.msg)).perform(typeText(TEST_QUESTION), closeSoftKeyboard());
        onView(withId(R.id.send)).perform(click());
        onData(withItemContent(TEST_QUESTION))
                .inAdapterView(withId(R.id.msgview))
                .atPosition(2)
                .check(matches(isDisplayed()));

    }


}
