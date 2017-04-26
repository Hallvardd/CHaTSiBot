package com.example.taphan.core1.login;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.espresso.util.TreeIterables;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.example.taphan.core1.R;
import com.example.taphan.core1.ToastMatcher;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


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
    /*
    @Test
    public void studentLogin() throws InterruptedException {
        FirebaseAuth.getInstance().signOut();
        //logs out if user is present:
        onView(withId(R.id.email)).perform(typeText(TEST_STUDENT_EMAIL), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(TEST_PW), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        // testing if the add_course button is visible, and thus info_activity is being run.
        onView(isRoot()).perform(waitId(R.id.sign_out, TimeUnit.SECONDS.toMillis(5)));
        //onView(withId(R.id.add_course)).check(matches(isDisplayed()));

    }

    */
    // Checking for toast message when invalid email/password

    @Test
    public void emptyPasswordTest() {
        onView(ViewMatchers.withId(R.id.email)).perform(typeText(TEST_STUDENT_EMAIL), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());

        onView(withText("Enter password!")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    public static ViewAction waitId(final int viewId, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for a specific view with id <" + viewId + "> during " + millis + " millis.";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;
                final Matcher<View> viewMatcher = withId(viewId);

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return;
                        }
                    }

                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);

                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }




}
