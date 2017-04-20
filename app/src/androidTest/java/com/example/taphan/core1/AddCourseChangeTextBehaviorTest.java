/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.taphan.core1;

import android.app.Activity;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import com.example.taphan.core1.course.AddCourseActivity;
import com.example.taphan.core1.user.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.taphan.core1.login.LoginActivity.globalUser;
import static org.hamcrest.Matchers.anything;


/**
 * Basic tests showcasing simple view matchers and actions like {@link ViewMatchers#withId},
 * {@link ViewActions#click} and {@link ViewActions#typeText}.
 * <p>
 * Note that there is no need to tell Espresso that a view is in a different {@link Activity}.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddCourseChangeTextBehaviorTest {

    public static final String TEST_COURSE = "TDT4100";

    /**
     * A JUnit {@link Rule @Rule} to launch your activity under test. This is a replacement
     * for {@link ActivityInstrumentationTestCase2}.
     * <p>
     * Rules are interceptors which are executed for each test method and will run before
     * any of your setup code in the {@link Before @Before} method.
     * <p>
     * {@link ActivityTestRule} will create and launch of the activity for you and also expose
     * the activity under test. To get a reference to the activity you can use
     * the {@link ActivityTestRule#getActivity()} method.
     */
    @Rule
    public ActivityTestRule<AddCourseActivity> mActivityRule = new ActivityTestRule<>(
            AddCourseActivity.class);

    // Check that when user add a course, the text field for user input will be emptied
    @Test
    public void changeText_sameActivity() {
        globalUser = new User();
        // Type text and then press the button.
        onView(withId(R.id.enter_course))
                .perform(typeText(TEST_COURSE), closeSoftKeyboard());
        onView(withId(R.id.add_course_button)).perform(click());

        // Check that the text was changed.
        onView(withId(R.id.enter_course)).check(matches(withText("")));
    }

    // By clicking on an item in the course listView, the title of next chat page corresponds to the clicked item in list
    @Test
    public void changeText_newActivity() {
        // Type text and then press the button.
        onView(withId(R.id.enter_course)).perform(typeText(TEST_COURSE),
                closeSoftKeyboard());
        onView(withId(R.id.add_course_button)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.courseview)).atPosition(0).perform(click());

        // This view is in a different Activity, no need to tell Espresso.
        onView(withId(R.id.chat_title)).check(matches(withText(TEST_COURSE)));
    }
}