package com.example.taphan.core1;

import com.example.taphan.core1.login.EmailValidator;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by taphan on 02.04.2017.
 */

public class EmailValidatorTest {
    private EmailValidator emailValidator = new EmailValidator();
    private static final String[] validEmail = new String[]{"mkyong@yahoo.com",
            "mkyong-100@yahoo.com", "mkyong.100@yahoo.com",
            "mkyong111@mkyong.com", "mkyong-100@mkyong.net",
            "mkyong.100@mkyong.com.au", "mkyong@1.com",
            "mkyong@gmail.com.com", "mkyong+100@gmail.com",
            "mkyong-100@yahoo-test.com"};
    private static final String[] invalidEmail = new String[]{"mkyong", "mkyong@.com.my",
            "mkyong123@gmail.a", "mkyong123@.com", "mkyong123@.com.com",
            ".mkyong@mkyong.com", "mkyong()*@gmail.com", "mkyong@%*.com",
            "mkyong..2002@gmail.com", "mkyong.@gmail.com",
            "mkyong@mkyong@gmail.com", "mkyong@gmail.com.1a"};

    @Test
    public void ValidEmailTest() {

        for (String temp : validEmail) {
            boolean valid = emailValidator.validate(temp);
            Assert.assertEquals(valid, true);
        }

    }

    @Test
    public void InValidEmailTest() {

        for (String temp : invalidEmail) {
            boolean valid = emailValidator.validate(temp);
            Assert.assertEquals(valid, false);
        }
    }
}
