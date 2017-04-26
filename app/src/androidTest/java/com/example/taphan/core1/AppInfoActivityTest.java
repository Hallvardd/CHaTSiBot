package com.example.taphan.core1;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.example.taphan.core1.course.AppInfoActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by hallvard on 26.04.17.
 */

public class AppInfoActivityTest {
    @Rule
    public IntentsTestRule<AppInfoActivity> mActivity = new IntentsTestRule<AppInfoActivity>(AppInfoActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            return new Intent(targetContext, AppInfoActivity.class);
        }
    };

    @Test
    public void textIsEqualTest(){
        String info = getResourceString(R.string.app_info);
        onView(withId(R.id.Info)).check(matches(withText(info)));
    }


    private String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        return targetContext.getResources().getString(id);
    }
}
