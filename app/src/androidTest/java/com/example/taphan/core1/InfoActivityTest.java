package com.example.taphan.core1;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.taphan.core1.course.AddCourseActivity;
import com.example.taphan.core1.course.AppInfoActivity;
import com.example.taphan.core1.course.InfoActivity;
import com.example.taphan.core1.login.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by taphan on 09.04.2017.
 */

@RunWith(AndroidJUnit4.class)
public class InfoActivityTest {

    @Rule
    public IntentsTestRule<InfoActivity> mActivity = new IntentsTestRule<InfoActivity>(InfoActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            return new Intent(targetContext, InfoActivity.class);
        }
    };

    // Test button click will lead to correct activity
    @Test
    public void addCourseNext() {
        onView(withId(R.id.add_course)).perform(click());
        intended(hasComponent(AddCourseActivity.class.getName()));
    }

    @Test
    public void appInfoNext() {
        onView(withId(R.id.app_info)).perform(click());
        intended(hasComponent(AppInfoActivity.class.getName()));
    }

    @Test
    public void signOutNext() {
        onView(withId(R.id.sign_out)).perform(click());
        intended(hasComponent(LoginActivity.class.getName()));
    }


}
