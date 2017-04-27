package com.example.taphan.core1.chat;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.taphan.core1.user.User;

import org.junit.Rule;
import org.junit.runner.RunWith;

import static com.example.taphan.core1.login.LoginActivity.globalUser;

@RunWith(AndroidJUnit4.class)
public class StudActivityTest {

    @Rule
    public ActivityTestRule<StudActivity> mActivityRule = new ActivityTestRule<StudActivity>(
            StudActivity.class){
        @Override
        protected void beforeActivityLaunched() {
            globalUser = new User();
            globalUser.setUserType("Student");
        }
    };
}
