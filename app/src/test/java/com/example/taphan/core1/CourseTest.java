package com.example.taphan.core1;

import com.example.taphan.core1.course.Course;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

public class CourseTest {
    private final String courseKey = "TDT4140";
    private final String courseName = "Software Development";

    @Test
    public void testCourseConstructor() throws NoSuchFieldException, IllegalAccessException {
        final Course testConstructorCourse = new Course(courseKey,courseName);
        final Field keyField = testConstructorCourse.getClass().getDeclaredField("courseKey");
        final Field nameField = testConstructorCourse.getClass().getDeclaredField("courseName");
        keyField.setAccessible(true);
        nameField.setAccessible(true);
        Assert.assertEquals(keyField.get(testConstructorCourse), courseKey);
        Assert.assertEquals(nameField.get(testConstructorCourse), courseName);
    }


    @Test
    public void testSetCourseName() throws NoSuchFieldException, IllegalAccessException{
        final Course testSettersCourse = new Course();
        testSettersCourse.setCourseName(courseName);
        final Field nameField = testSettersCourse.getClass().getDeclaredField("courseName");
        nameField.setAccessible(true);
        Assert.assertEquals(nameField.get(testSettersCourse), courseName);
    }

    @Test
    public void testGetCourseName() throws NoSuchFieldException, IllegalAccessException {
        final Course testGettersCourse = new Course();
        final Field getterField = testGettersCourse.getClass().getDeclaredField("courseName");
        getterField.setAccessible(true);
        getterField.set(testGettersCourse,courseName);
        Assert.assertEquals(testGettersCourse.getCourseName(),courseName);
    }

    @Test
    public void testSetCourseKey() throws NoSuchFieldException, IllegalAccessException {
        final Course testSettersCourse = new Course();
        testSettersCourse.setCourseKey(courseKey);
        final Field keyField = testSettersCourse.getClass().getDeclaredField("courseKey");
        keyField.setAccessible(true);
        Assert.assertEquals(keyField.get(testSettersCourse), courseKey);

    }

    @Test
    public void testGetCourseKey() throws IllegalAccessException, NoSuchFieldException {
        final Course testGettersCourse = new Course();
        final Field getterField = testGettersCourse.getClass().getDeclaredField("courseKey");
        getterField.setAccessible(true);
        getterField.set(testGettersCourse,courseKey);
        Assert.assertEquals(testGettersCourse.getCourseKey(),courseKey);

    }

}
