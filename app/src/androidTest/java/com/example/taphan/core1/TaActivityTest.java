package com.example.taphan.core1;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.example.taphan.core1.course.InfoActivity;
import com.example.taphan.core1.login.TaActivity;
import com.example.taphan.core1.user.User;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.example.taphan.core1.login.LoginActivity.globalUser;

public class TaActivityTest {

    @Rule
    public IntentsTestRule<TaActivity> mActivity = new IntentsTestRule<TaActivity>(TaActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            return new Intent(targetContext, TaActivity.class);
        }
    };

    // Test button click will lead to correct activity
    @Test
    public void chooseStudentActivity() {
        globalUser = new User();
        onView(withId(R.id.ta_choose_student)).perform(click());
        intended(hasComponent(InfoActivity.class.getName()));
        Assert.assertFalse(globalUser.getIsTa());
    }

    @Test
    public void chooseProfActivity() {
        globalUser = new User();
        onView(withId(R.id.ta_choose_student)).perform(click());
        intended(hasComponent(InfoActivity.class.getName()));
        Assert.assertTrue(globalUser.getIsTa());
    }

}
