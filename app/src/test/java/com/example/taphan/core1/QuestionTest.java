package com.example.taphan.core1;

import com.example.taphan.core1.questionDatabase.Question;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionTest {

    private String questionID = "123abc";
    private String questionTxt = "what is a broom?";
    private String questionPath = "SUB101-definition-broom";

    @Test
    public void testQuestionConstructor() throws NoSuchFieldException, IllegalAccessException {
        final Question testConstructorQuestion = new Question(questionID, questionTxt, questionPath);
        final Field questionIDField = testConstructorQuestion.getClass().getDeclaredField("questionID");
        final Field questionTxtField = testConstructorQuestion.getClass().getDeclaredField("questionTxt");
        final Field questionPathField = testConstructorQuestion.getClass().getDeclaredField("questionPath");
        questionIDField.setAccessible(true);
        questionTxtField.setAccessible(true);
        questionPathField.setAccessible(true);
        Assert.assertEquals(questionIDField.get(testConstructorQuestion), questionID);
        Assert.assertEquals(questionTxtField.get(testConstructorQuestion), questionTxt);
        Assert.assertEquals(questionPathField.get(testConstructorQuestion), questionPath);
    }


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

    @Test
    public void testSetStudentListeners() throws NoSuchFieldException, IllegalAccessException {
        ArrayList<String> studentListeners = new ArrayList<>((List<String>) Arrays.asList("stud1","stud2"));
        final Question testSetStudentListeners = new Question();
        testSetStudentListeners.setStudentsListeners(studentListeners);
        final Field setStudentField = testSetStudentListeners.getClass().getDeclaredField("studentsListeners");
        setStudentField.setAccessible(true);
        Assert.assertEquals(setStudentField.get(testSetStudentListeners),studentListeners);
    }

    @Test
    public void testGetStudentListeners() throws NoSuchFieldException, IllegalAccessException {
        ArrayList<String> studentListeners = new ArrayList<>((List<String>) Arrays.asList("stud1","stud2"));
        final Question testGetStudentListeners = new Question();
        final Field getStudentField = testGetStudentListeners.getClass().getDeclaredField("studentsListeners");
        getStudentField.setAccessible(true);
        getStudentField.set(testGetStudentListeners,studentListeners);
        Assert.assertEquals(testGetStudentListeners.getStudentsListeners(),studentListeners);
    }

    @Test
    public void testAddStudentListener(){
        Question testQuestion = new Question();
        String stud1 = "stud1";
        String stud2 = "stud2";
        String stud3 = "stud3";
        ArrayList<String> testList = new ArrayList<>((List<String>) Arrays.asList(stud1, stud2));
        ArrayList<String> assignList = new ArrayList<>((List<String>) Arrays.asList(stud1, stud2));
        // assigns list to testUser
        testQuestion.setStudentsListeners(assignList);
        // Attempts to add course already in list
        testQuestion.addSListener(stud1);
        Assert.assertEquals(testQuestion.getStudentsListeners(),testList);
        // Attempts to add new course not in list
        testQuestion.addSListener(stud3);
        Assert.assertNotSame(testQuestion.getStudentsListeners(),testList);
        Assert.assertTrue(testQuestion.getStudentsListeners().contains(stud3));

    }


    @Test
    public void testRemoveStudentListener(){
        Question testQuestion = new Question();
        String stud1 = "stud1";
        String stud2 = "stud2";
        String stud3 = "stud3";
        ArrayList<String> testList = new ArrayList<>((List<String>) Arrays.asList(stud1, stud2));
        ArrayList<String> assignList = new ArrayList<>((List<String>) Arrays.asList(stud1, stud2));
        // assigns list to testUser
        testQuestion.setStudentsListeners(assignList);
        // Attempts to remove course not in list
        testQuestion.removeSListener(stud3);
        Assert.assertEquals(testQuestion.getStudentsListeners(),testList);
        // Attempts to remove course in list
        testQuestion.removeSListener(stud2);
        Assert.assertNotSame(testQuestion.getStudentsListeners(),testList);
        Assert.assertFalse(testQuestion.getStudentsListeners().contains(stud2));

    }


}
