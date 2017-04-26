package com.example.taphan.core1;

import com.example.taphan.core1.questionDatabase.Question;
import com.example.taphan.core1.questionDatabase.State;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;



public class StateTest {

    private String answer = "This is an answer";
    private String questionID = "123abc";

    @Test
    public void testSetAnswer() throws NoSuchFieldException, IllegalAccessException{
        final State testSettersState = new State();
        testSettersState.setAnswer(answer);
        final Field answerField = testSettersState.getClass().getDeclaredField("answer");
        answerField.setAccessible(true);
        Assert.assertEquals(answerField.get(testSettersState), answer);
    }

    @Test
    public void testGetAnswer() throws NoSuchFieldException, IllegalAccessException {
        final State testGettersState = new State();
        final Field answerGetterField = testGettersState.getClass().getDeclaredField("answer");
        answerGetterField.setAccessible(true);
        answerGetterField.set(testGettersState, answer);
        Assert.assertEquals(testGettersState.getAnswer(), answer);
    }

    @Test
    public void testSetQuestionID() throws NoSuchFieldException, IllegalAccessException{
        final State testSettersState = new State();
        testSettersState.setQuestionID(questionID);
        final Field questionIDField = testSettersState.getClass().getDeclaredField("questionID");
        questionIDField.setAccessible(true);
        Assert.assertEquals(questionIDField.get(testSettersState), questionID);
    }

    @Test
    public void testGetQuestionID() throws NoSuchFieldException, IllegalAccessException {
        final Question testGettersState = new Question();
        final Field idGetterField = testGettersState.getClass().getDeclaredField("questionID");
        idGetterField.setAccessible(true);
        idGetterField.set(testGettersState, questionID);
        Assert.assertEquals(testGettersState.getQuestionID(), questionID);
    }

}
