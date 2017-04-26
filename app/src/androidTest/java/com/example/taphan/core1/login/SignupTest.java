package com.example.taphan.core1.login;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.example.taphan.core1.R;
import com.example.taphan.core1.ToastMatcher;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class SignupTest {

    public static final String TEST_EMAIL = "testuser@test.test";
    public static final String TEST_INVALID_EMAIL = "mkyong123@.com";
    public static final String TEST_WRONG_PW = "1234";
    @Rule
    public IntentsTestRule<SignupActivity> mActivity = new IntentsTestRule<SignupActivity>(SignupActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            return new Intent(targetContext, SignupActivity.class);
        }
    };

    @Test
    public void forgotPasswordNextIntent() {
        onView(ViewMatchers.withId(R.id.btn_reset_password)).perform(click());
        intended(hasComponent(ResetPasswordActivity.class.getName()));
    }


    @Test
    public void shortPasswordTest() {
        onView(ViewMatchers.withId(R.id.email)).perform(typeText(TEST_EMAIL), closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.password)).perform(typeText(TEST_WRONG_PW), closeSoftKeyboard());
        onView(withId(R.id.sign_up_button)).perform(click());

        onView(withText("Password too short, enter minimum 6 characters!")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));

    }

    @Test
    public void emptyPasswordTest() {
        onView(ViewMatchers.withId(R.id.email)).perform(typeText(TEST_EMAIL), closeSoftKeyboard());
        onView(withId(R.id.sign_up_button)).perform(click());

        onView(withText("Enter password!")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void invalidPasswordTest() {
        onView(ViewMatchers.withId(R.id.email)).perform(typeText(TEST_INVALID_EMAIL), closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.password)).perform(typeText("test12345678"), closeSoftKeyboard());
        onView(withId(R.id.sign_up_button)).perform(click());

        onView(withText("Invalid email format!")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    // Test radio button
    @Test
    public void radioButtonStudTest() {
        onView(withId(R.id.radio_stud))
                .perform(click());

        onView(withId(R.id.radio_stud))
                .check(matches(isChecked()));

        onView(withId(R.id.radio_prof))
                .check(matches(not(isChecked())));

        onView(withId(R.id.radio_ta))
                .check(matches(not(isChecked())));
    }

    @Test
    public void radioButtonProfTest() {
        onView(withId(R.id.radio_prof))
                .perform(click());

        onView(withId(R.id.radio_prof))
                .check(matches(isChecked()));

        onView(withId(R.id.radio_stud))
                .check(matches(not(isChecked())));

        onView(withId(R.id.radio_ta))
                .check(matches(not(isChecked())));
    }
    @Test
    public void radioButtonTaTest() {
        onView(withId(R.id.radio_ta))
                .perform(click());

        onView(withId(R.id.radio_ta))
                .check(matches(isChecked()));

        onView(withId(R.id.radio_prof))
                .check(matches(not(isChecked())));

        onView(withId(R.id.radio_stud))
                .check(matches(not(isChecked())));
    }
}
