package com.example.taphan.core1;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.taphan.core1.login.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;





@RunWith(AndroidJUnit4.class)
public class LoginUserTest {
    public static final String TEST_STUDENT_EMAIL = "test@test.test";
    public static final String TEST_PROF_EMAIL = "test@test.test";
    public static final String TEST_TA_EMAIL = "test@test.test";
    public static final String TEST_PW = "test123";


    @Rule
    public IntentsTestRule<LoginActivity> mActivityRule = new IntentsTestRule<LoginActivity>(LoginActivity.class){
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            return new Intent(targetContext, LoginActivity.class);
        }
    };

    // When a student or professor email is used to log in, the next page will be InfoActivity
    @Test
    public void studentLogin() throws InterruptedException {
        long time = 100;
        onView(withId(R.id.email)).perform(typeText(TEST_STUDENT_EMAIL), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(TEST_PW), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());

        // testing if the add_course button is visible, and thus info_activity is being run.
        onView(withId(R.id.add_course)).check(matches(isDisplayed()));

    }


    // Checking for toast message when invalid email/password
/*
    @Test
    public void emptyPasswordTest() {
        onView(withId(R.id.email)).perform(typeText(TEST_STUDENT_EMAIL), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());

        onView(withText("Enter password!")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }
*/




}
