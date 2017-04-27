package com.example.taphan.core1.ProfActivtyTest;

import com.example.taphan.core1.ProfActivity;
import com.example.taphan.core1.questionDatabase.Question;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hallvard on 26.04.17.
 */
public class ProfActivityTest {



    @Test
    public void testAddAnswerToDatabase(){

    }

    @Test
    public void testSetQList() throws NoSuchFieldException, IllegalAccessException {
        Question q1 = new Question("1","test101","test?");
        Question q2 = new Question("2","test102","test?");
        ArrayList<Question> questions = new ArrayList<>((List<Question>) Arrays.asList(q1,q2));
        final ProfActivity testSetStudentListeners = new ProfActivity();
        testSetStudentListeners.setQList(questions);
        final Field setStudentField = testSetStudentListeners.getClass().getDeclaredField("qList");
        setStudentField.setAccessible(true);
        Assert.assertEquals(setStudentField.get(testSetStudentListeners),questions);
    }


}
