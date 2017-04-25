package com.example.taphan.core1;

import com.example.taphan.core1.course.Course;
import com.example.taphan.core1.user.User;

import junit.framework.Assert;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class UserTest{
    private static final String userID = "123abc";
    private static final String userTypeStudent = "Student";
    private static final String userTypeProfessor = "Professor";
    private static final String email = "test@email.com";
    private static final boolean isTA = true;


    // Tests if the constructor sets the proper variables
    @Test
    public void testUserConstructor() throws NoSuchFieldException, IllegalAccessException {
        User testConstructorUser = new User();
        final Field userIDField = testConstructorUser.getClass().getDeclaredField("userID");
        final Field userTypeField = testConstructorUser.getClass().getDeclaredField("userType");
        final Field isTAField = testConstructorUser.getClass().getDeclaredField("isTA");
        final Field unansweredQuestionsField = testConstructorUser.getClass().getDeclaredField("unansweredQuestions");
        final Field answeredQuestionsField = testConstructorUser.getClass().getDeclaredField("answeredQuestions");
        final Field uCoursesField = testConstructorUser.getClass().getDeclaredField("uCourses");
        final Field tCoursesField = testConstructorUser.getClass().getDeclaredField("tCourses");
        userIDField.setAccessible(true);
        userTypeField.setAccessible(true);
        isTAField.setAccessible(true);
        unansweredQuestionsField.setAccessible(true);
        answeredQuestionsField.setAccessible(true);
        uCoursesField.setAccessible(true);
        tCoursesField.setAccessible(true);
        Assert.assertEquals(userIDField.get(testConstructorUser),"");
        Assert.assertEquals(userTypeField.get(testConstructorUser),"");
        Assert.assertTrue((Boolean) isTAField.get(testConstructorUser));
        Assert.assertNotNull(unansweredQuestionsField.get(testConstructorUser));
        Assert.assertNotNull(answeredQuestionsField.get(testConstructorUser));
        Assert.assertNotNull(uCoursesField.get(testConstructorUser));
        Assert.assertNotNull(tCoursesField.get(testConstructorUser));
    }

    @Test
    public void testGetUserID() throws NoSuchFieldException, IllegalAccessException {
        final User testGettersUser = new User();
        final Field idGetterField = testGettersUser.getClass().getDeclaredField("userID");
        idGetterField.setAccessible(true);
        idGetterField.set(testGettersUser, userID);
        Assert.assertEquals(testGettersUser.getUserID(), userID);
    }

    @Test
    public void testSetUserID() throws NoSuchFieldException, IllegalAccessException {
        final User testSettersUser = new User();
        testSettersUser.setUserID(userID);
        final Field userIDField = testSettersUser.getClass().getDeclaredField("userID");
        userIDField.setAccessible(true);
        Assert.assertEquals(userIDField.get(testSettersUser), userID);

    }

    @Test
    public void testGetUserTypeProfessor() throws NoSuchFieldException, IllegalAccessException {
        final User testGettersUser = new User();
        final Field typeGetterField = testGettersUser.getClass().getDeclaredField("userType");
        typeGetterField.setAccessible(true);
        typeGetterField.set(testGettersUser, userTypeProfessor);
        Assert.assertEquals(testGettersUser.getUserType(), userTypeProfessor);

    }

    @Test
    public void testSetUserTypeProfessor() throws NoSuchFieldException, IllegalAccessException {
        final User testSettersUser = new User();
        testSettersUser.setUserType(userTypeProfessor);
        final Field userTypeField = testSettersUser.getClass().getDeclaredField("userType");
        userTypeField.setAccessible(true);
        Assert.assertEquals(userTypeField.get(testSettersUser), userTypeProfessor);

    }

    @Test
    public void testGetUserTypeStudent() throws NoSuchFieldException, IllegalAccessException {
        final User testGettersUser = new User();
        final Field typeGetterField = testGettersUser.getClass().getDeclaredField("userType");
        typeGetterField.setAccessible(true);
        typeGetterField.set(testGettersUser, userTypeStudent);
        Assert.assertEquals(testGettersUser.getUserType(), userTypeStudent);

    }

    @Test
    public void testSetUserTypeStudent() throws NoSuchFieldException, IllegalAccessException {
        final User testSettersUser = new User();
        testSettersUser.setUserType(userTypeStudent);
        final Field userTypeField = testSettersUser.getClass().getDeclaredField("userType");
        userTypeField.setAccessible(true);
        Assert.assertEquals(userTypeField.get(testSettersUser), userTypeStudent);

    }

    @Test
    public void testGetEmail() throws NoSuchFieldException, IllegalAccessException {
        final User testGettersUser = new User();
        final Field emailField = testGettersUser.getClass().getDeclaredField("email");
        emailField.setAccessible(true);
        emailField.set(testGettersUser, email);
        Assert.assertEquals(testGettersUser.getEmail(), email);

    }

    @Test
    public void testSetEmail() throws NoSuchFieldException, IllegalAccessException {
        final User testSettersUser = new User();
        testSettersUser.setEmail(email);
        final Field emailField = testSettersUser.getClass().getDeclaredField("email");
        emailField.setAccessible(true);
        Assert.assertEquals(emailField.get(testSettersUser), email);

    }

    @Test
    public void testGetIsTA() throws NoSuchFieldException, IllegalAccessException {
        final User testGettersUser = new User();
        final Field isTAField = testGettersUser.getClass().getDeclaredField("isTA");
        isTAField.setAccessible(true);
        isTAField.set(testGettersUser, isTA);
        Assert.assertEquals(testGettersUser.getIsTa(), isTA);
    }

    @Test
    public void testSetIsTA() throws IllegalAccessException, NoSuchFieldException {
        final User testSettersUser = new User();
        testSettersUser.setIsTa(isTA);
        final Field isTAField = testSettersUser.getClass().getDeclaredField("isTA");
        isTAField.setAccessible(true);
        Assert.assertEquals(isTAField.get(testSettersUser), isTA);

    }


    /*Functionality tests:
     *
     * !NB these tests are reliant on the objects getter and setter methods. Please assure that the
     * above getter and setter tests are successful before relying on the following tests:
     *
     */

    //Tests adding and removing values from HashMaps
    @Test
    public void testPutUnansweredQuestions(){
        User testUser = new User();
        String firstCourseCode = "TEST123";
        String secondCourseCode = "TEST456";
        //Creates two clone HashMaps
        HashMap<String, ArrayList<String>> testMap = new HashMap<>();
        HashMap<String, ArrayList<String>> assignmentMap = new HashMap<>();
        testMap.put(firstCourseCode,new ArrayList<>((List<String>)Arrays.asList("1","2")));
        assignmentMap.put(firstCourseCode,new ArrayList<>((List<String>)Arrays.asList("1","2")));
        //One is set as User.unansweredQuestions
        testUser.setUnansweredQuestions(assignmentMap);
        // Tries to put something that is already in the map in the map.
        testUser.putUnansweredQuestion(firstCourseCode, "1");
        // If the unansweredQuestions remains unchanged the if clause has been evoked.
        Assert.assertEquals(testUser.getUnansweredQuestions(),testMap);
        // Value not in map, checks for value added, no new course added:
        testUser.putUnansweredQuestion(firstCourseCode,"3");
        Assert.assertEquals(testUser.getUnansweredQuestions().keySet(),testMap.keySet());
        Assert.assertNotSame(testUser.getUnansweredQuestions(),(testMap));
        // Course not in map. Adds the course.
        testUser.putUnansweredQuestion(secondCourseCode,"1");
        Assert.assertNotSame(testUser.getUnansweredQuestions().keySet(),testMap.keySet());
    }

    @Test
    public void testPutAnsweredQuestions(){
        User testUser = new User();
        String firstCourseCode = "TEST123";
        String secondCourseCode = "TEST456";
        //Creates two clone HashMaps
        HashMap<String, ArrayList<String>> testMap = new HashMap<>();
        HashMap<String, ArrayList<String>> assignmentMap = new HashMap<>();
        testMap.put(firstCourseCode,new ArrayList<>((List<String>)Arrays.asList("1","2")));
        assignmentMap.put(firstCourseCode,new ArrayList<>((List<String>)Arrays.asList("1","2")));
        //One is set as User.answeredQuestions
        testUser.setAnsweredQuestions(assignmentMap);
        // Tries to put something that is already in the map in the map.
        testUser.putAnsweredQuestion(firstCourseCode, "1");
        // If the answeredQuestions remains unchanged the if clause has been evoked.
        Assert.assertEquals(testUser.getAnsweredQuestions(),testMap);
        // Value not in map, checks for value added, no new course added:
        testUser.putAnsweredQuestion(firstCourseCode,"3");
        Assert.assertEquals(testUser.getAnsweredQuestions().keySet(),testMap.keySet());
        Assert.assertNotSame(testUser.getAnsweredQuestions(),(testMap));
        // Course not in map. Adds the course.
        testUser.putAnsweredQuestion(secondCourseCode,"1");
        Assert.assertNotSame(testUser.getAnsweredQuestions().keySet(),testMap.keySet());

    }


    @Test
    public void testRemoveUnansweredQuestion(){
        User testUser = new User();
        String firstCourseCode = "TEST123";
        String secondCourseCode = "TEST456";
        //Creates two clone HashMaps
        HashMap<String, ArrayList<String>> testMap = new HashMap<>();
        HashMap<String, ArrayList<String>> assignmentMap = new HashMap<>();
        testMap.put(firstCourseCode,new ArrayList<>((List<String>)Arrays.asList("1","2")));
        assignmentMap.put(firstCourseCode,new ArrayList<>((List<String>)Arrays.asList("1","2")));
        //One is set as User.unansweredQuestions
        testUser.setUnansweredQuestions(assignmentMap);

        // Value not in map, checks if the map stays the same:
        testUser.removeUnansweredQuestion(firstCourseCode,"3");
        Assert.assertEquals(testUser.getUnansweredQuestions().keySet(),testMap.keySet());
        Assert.assertEquals(testUser.getUnansweredQuestions(),(testMap));
        // Course not in map. Nothing happens
        testUser.removeUnansweredQuestion(secondCourseCode,"1");
        Assert.assertEquals(testUser.getUnansweredQuestions().keySet(),testMap.keySet());
        Assert.assertEquals(testUser.getUnansweredQuestions().keySet(),testMap.keySet());
        // Tries to remove an actual value from the map.
        testUser.removeUnansweredQuestion(firstCourseCode, "1");
        // If the unansweredQuestions remains unchanged the if clause has been evoked.
        Assert.assertNotSame(testUser.getUnansweredQuestions(),testMap);
    }

    @Test
    public void testRemoveAnsweredQuestion(){
        User testUser = new User();
        String firstCourseCode = "TEST123";
        String secondCourseCode = "TEST456";
        //Creates two clone HashMaps
        HashMap<String, ArrayList<String>> testMap = new HashMap<>();
        HashMap<String, ArrayList<String>> assignmentMap = new HashMap<>();
        testMap.put(firstCourseCode,new ArrayList<>((List<String>)Arrays.asList("1","2")));
        assignmentMap.put(firstCourseCode,new ArrayList<>((List<String>)Arrays.asList("1","2")));
        //One is set as User.unansweredQuestions
        testUser.setAnsweredQuestions(assignmentMap);

        // Value not in map, checks if the map stays the same:
        testUser.removeAnsweredQuestion(firstCourseCode,"3");
        Assert.assertEquals(testUser.getAnsweredQuestions().keySet(),testMap.keySet());
        Assert.assertEquals(testUser.getAnsweredQuestions(),(testMap));
        // Course not in map. Nothing happens
        testUser.removeAnsweredQuestion(secondCourseCode,"1");
        Assert.assertEquals(testUser.getAnsweredQuestions().keySet(),testMap.keySet());
        Assert.assertEquals(testUser.getAnsweredQuestions().keySet(),testMap.keySet());
        // Tries to remove an actual value from the map.
        testUser.removeAnsweredQuestion(firstCourseCode, "1");
        // If the unansweredQuestions remains unchanged the if clause has been evoked.
        Assert.assertNotSame(testUser.getAnsweredQuestions(),testMap);

    }

    // Adding and removing courses from arrayList
    @Test
    public void testAddtCourse(){
        User testUser = new User();
        Course course1 = new Course("TEST101", "test1");
        Course course2 = new Course("TEST102", "test2");
        Course course3 = new Course("TEST103", "test3");
        ArrayList<Course> testList = new ArrayList<>((List<Course>) Arrays.asList(course1, course2));
        ArrayList<Course> assignList = new ArrayList<>((List<Course>) Arrays.asList(course1, course2));
        // assigns list to testUser
        testUser.settCourses(assignList);
        // Attempts to add course already in list
        testUser.addtCourse(course1);
        Assert.assertEquals(testUser.gettCourses(),testList);
        // Attempts to add new course not in list
        testUser.addtCourse(course3);
        Assert.assertNotSame(testUser.gettCourses(),testList);
        Assert.assertTrue(testUser.gettCourses().contains(course3));
    }

    @Test
    public void testAdduCourse(){
        User testUser = new User();
        Course course1 = new Course("TEST101", "test1");
        Course course2 = new Course("TEST102", "test2");
        Course course3 = new Course("TEST103", "test3");
        ArrayList<Course> testList = new ArrayList<>((List<Course>) Arrays.asList(course1, course2));
        ArrayList<Course> assignList = new ArrayList<>((List<Course>) Arrays.asList(course1, course2));
        // assigns list to testUser
        testUser.setuCourses(assignList);
        // Attempts to add course already in list
        testUser.adduCourse(course1);
        Assert.assertEquals(testUser.getuCourses(),testList);
        // Attempts to add new course not in list
        testUser.adduCourse(course3);
        Assert.assertNotSame(testUser.getuCourses(),testList);
        Assert.assertTrue(testUser.getuCourses().contains(course3));

    }

    @Test
    public void testRemovetCourse(){
        User testUser = new User();
        Course course1 = new Course("TEST101", "test1");
        Course course2 = new Course("TEST102", "test2");
        Course course3 = new Course("TEST103", "test3");
        ArrayList<Course> testList = new ArrayList<>((List<Course>) Arrays.asList(course1, course2));
        ArrayList<Course> assignList = new ArrayList<>((List<Course>) Arrays.asList(course1, course2));
        // assigns list to testUser
        testUser.settCourses(assignList);
        // Attempts to remove course not in list
        testUser.removetCourse(course3);
        Assert.assertEquals(testUser.gettCourses(),testList);
        // Attempts to remove course in list
        testUser.removetCourse(course2);
        Assert.assertNotSame(testUser.gettCourses(),testList);
        Assert.assertFalse(testUser.gettCourses().contains(course2));

    }

    @Test
    public void testRemoveuCourse(){
        User testUser = new User();
        Course course1 = new Course("TEST101", "test1");
        Course course2 = new Course("TEST102", "test2");
        Course course3 = new Course("TEST103", "test3");
        ArrayList<Course> testList = new ArrayList<>((List<Course>) Arrays.asList(course1, course2));
        ArrayList<Course> assignList = new ArrayList<>((List<Course>) Arrays.asList(course1, course2));
        // assigns list to testUser
        testUser.setuCourses(assignList);
        // Attempts to remove course not in list
        testUser.removeuCourse(course3);
        Assert.assertEquals(testUser.getuCourses(),testList);
        // Attempts to remove course in list
        testUser.removeuCourse(course2);
        Assert.assertNotSame(testUser.getuCourses(),testList);
        Assert.assertFalse(testUser.getuCourses().contains(course2));

    }
}
