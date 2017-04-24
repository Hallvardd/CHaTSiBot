package com.example.taphan.core1;

import com.example.taphan.core1.questionDatabase.Question;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

public class QuestionTest {

    private String questionID = "123abc";
    private String questionTxt = "what is a broom?";
    private String questionPath = "SUB101-definition-broom";
/*
    @Test
    public void testQuestionConstructor(){
        Question testConstructorQuestion = new Question();
    }
*/
    @Test
    public void testSetQuestionID() throws NoSuchFieldException, IllegalAccessException{
        final Question testSettersQuestion = new Question();
        testSettersQuestion.setQuestionID(questionID);
        final Field questionIDField = testSettersQuestion.getClass().getDeclaredField("questionID");
        questionIDField.setAccessible(true);
        Assert.assertEquals(questionIDField.get(testSettersQuestion), questionID);
    }

    @Test
    public void testGetQuestionID() throws NoSuchFieldException, IllegalAccessException {
        final Question testGettersQuestion = new Question();
        final Field idGetterField = testGettersQuestion.getClass().getDeclaredField("questionID");
        idGetterField.setAccessible(true);
        idGetterField.set(testGettersQuestion, questionID);
        Assert.assertEquals(testGettersQuestion.getQuestionID(), questionID);
    }

    @Test
    public void testSetQuestionTxt() throws NoSuchFieldException, IllegalAccessException{
        final Question testSettersQuestion = new Question();
        testSettersQuestion.setQuestionTxt(questionTxt);
        final Field questionTxtField = testSettersQuestion.getClass().getDeclaredField("questionTxt");
        questionTxtField.setAccessible(true);
        Assert.assertEquals(questionTxtField.get(testSettersQuestion), questionTxt);
    }

    @Test
    public void testGetQuestionTxt() throws NoSuchFieldException, IllegalAccessException {
        final Question testGettersQuestion = new Question();
        final Field txtGetterField = testGettersQuestion.getClass().getDeclaredField("questionTxt");
        txtGetterField.setAccessible(true);
        txtGetterField.set(testGettersQuestion, questionTxt);
        Assert.assertEquals(testGettersQuestion.getQuestionTxt(), questionTxt);
    }

    @Test
    public void testSetQuestionPath() throws NoSuchFieldException, IllegalAccessException{
        final Question testSettersQuestion = new Question();
        testSettersQuestion.setQuestionPath(questionPath);
        final Field questionPathField = testSettersQuestion.getClass().getDeclaredField("questionPath");
        questionPathField.setAccessible(true);
        Assert.assertEquals(questionPathField.get(testSettersQuestion), questionPath);
    }

    @Test
    public void testGetQuestionPath() throws NoSuchFieldException, IllegalAccessException {
        final Question testGettersQuestion = new Question();
        final Field pathGetterField = testGettersQuestion.getClass().getDeclaredField("questionPath");
        pathGetterField.setAccessible(true);
        pathGetterField.set(testGettersQuestion, questionPath);
        Assert.assertEquals(testGettersQuestion.getQuestionPath(), questionPath);
    }

}
