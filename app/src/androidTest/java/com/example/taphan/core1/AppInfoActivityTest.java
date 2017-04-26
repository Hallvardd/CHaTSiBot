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
    private static final String info = "  Professors often receive questions that they have already answered or\n" +
            "        where the answers can easily be found. ChaTSiBot is a communication tool that innovates\n" +
            "        communication in a teaching environment by automatically linking the answers of already\n" +
            "        answered questions to similar new questions.\n" +
            "        \\n\\n\\nThe team is:\n" +
            "        \\nCharles Sørbø Edvardsen,\\nHallvard Stemshaug, \\nTruc Anh Nguyen Phan, \\nSiren Johansen\n";
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
        onView(withId(R.id.Info)).check(matches(withText(info)));
    }



}
