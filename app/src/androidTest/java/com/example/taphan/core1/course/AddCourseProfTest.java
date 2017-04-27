package com.example.taphan.core1.course;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.test.ActivityInstrumentationTestCase2;

import com.example.taphan.core1.R;
import com.example.taphan.core1.ToastMatcher;
import com.example.taphan.core1.user.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.taphan.core1.login.LoginActivity.globalUser;
import static org.hamcrest.Matchers.anything;

public class AddCourseProfTest {
    private static final String TEST_COURSE = "TDT4100";
    private static final String INVALID_TEST_COURSE = "TDT41111";

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
    public ActivityTestRule<AddCourseActivity> mActivityRule = new ActivityTestRule<AddCourseActivity>(
            AddCourseActivity.class){
        @Override
        protected void beforeActivityLaunched() {
            globalUser = new User();
            globalUser.setUserType("Professor");
        }
    };

    // Check that when user add a course, the text field for user input will be emptied
    @Test
    public void changeText_sameActivity() {
        // Type course code and then press the button.
        onView(ViewMatchers.withId(R.id.enter_course))
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

    @Test
    public void invalidCourseTest() {
        onView(withId(R.id.enter_course)).perform(typeText(INVALID_TEST_COURSE),
                closeSoftKeyboard());
        onView(withId(R.id.add_course_button)).perform(click());
        String toastMsg = getResourceString(R.string.add_course_failed);
        onView(withText(toastMsg)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    private String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        return targetContext.getResources().getString(id);
    }
}
