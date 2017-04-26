package com.example.taphan.core1.login;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.example.taphan.core1.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class LoginIntentTest {

    @Rule
    public IntentsTestRule<LoginActivity> mActivity = new IntentsTestRule<LoginActivity>(LoginActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            return new Intent(targetContext, LoginActivity.class);
        }
    };

    // Test clicking on "Forgot password" and "Sign up" to lead to right page
    @Test
    public void forgotPasswordNextIntent() {
        onView(ViewMatchers.withId(R.id.btn_reset_password)).perform(click());
        intended(hasComponent(ResetPasswordActivity.class.getName()));
    }

    @Test
    public void signupNextIntent() {
        onView(withId(R.id.btn_signup)).perform(click());
        intended(hasComponent(SignupActivity.class.getName()));
    }

}
