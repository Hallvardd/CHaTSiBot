package com.example.taphan.core1.chat;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.taphan.core1.R;
import com.example.taphan.core1.course.Course;
import com.example.taphan.core1.user.User;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.example.taphan.core1.Delay.waitFor;
import static com.example.taphan.core1.course.AddCourseActivity.globalCourse;
import static com.example.taphan.core1.login.LoginActivity.globalUser;

@RunWith(AndroidJUnit4.class)
public class StudActivityTest {
    private static final String COURSE_KEY = "tdt4100";
    private static final String QUESTION_DB = "What is eclipse?";
    private static final String QUESTION_DB_ALREADY_ASKED = "What is an object?";
    private static final String QUESTION_JSON_EXAM = "When is my exam?";
    private static final String QUESTION_JSON_LECTURER = "Who is my lecturer?";
    private static final Integer millis = 3000;

    @Rule
    public ActivityTestRule<StudActivity> mActivityRule = new ActivityTestRule<StudActivity>(
            StudActivity.class){
        @Override
        protected void beforeActivityLaunched() {
            globalUser = new User();
            globalUser.setUserType("Student");
            globalUser.setUserID("dz264R8jyTdspRLKTuSDDMl0MG23");
            globalCourse = new Course();
            globalCourse.setCourseKey(COURSE_KEY);
        }
    };

    @Test
    public void testDb() throws InterruptedException {
        // Have to delete "eclipse" from database in order for this to work
        onView(withId(R.id.msg)).perform(typeText(QUESTION_DB),
                closeSoftKeyboard());
        onView(withId(R.id.send)).perform(click());
        onView(isRoot()).perform(waitFor(millis));

    }
    @Test
    public void testAlreadyAsked() {
        onView(withId(R.id.msg)).perform(typeText(QUESTION_DB_ALREADY_ASKED),
                closeSoftKeyboard());
        onView(withId(R.id.send)).perform(click());
        onView(isRoot()).perform(waitFor(millis));

    }
    @Test
    public void testJsonExam() {
        onView(withId(R.id.msg)).perform(typeText(QUESTION_JSON_EXAM),
                closeSoftKeyboard());
        onView(withId(R.id.send)).perform(click());
        onView(isRoot()).perform(waitFor(millis));
    }

    @Test
    public void testJsonLecturer() {
        onView(withId(R.id.msg)).perform(typeText(QUESTION_JSON_LECTURER),
                closeSoftKeyboard());
        onView(withId(R.id.send)).perform(click());
        onView(isRoot()).perform(waitFor(millis));
    }


}
