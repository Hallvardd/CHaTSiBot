package com.example.taphan.core1.ProfActivtyTest;

import com.example.taphan.core1.chat.ProfActivity;
import com.example.taphan.core1.chat.ChatArrayAdapter;
import com.example.taphan.core1.chat.ChatMessage;
import com.example.taphan.core1.questionDatabase.Question;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.*;


@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseDatabase.class})
public class ProfActivityTest {

    private DatabaseReference mockedDatabaseReference;
    private ChatArrayAdapter mockedChatArrayAdapter;

    @Before
    public void before() {
        mockedDatabaseReference = Mockito.mock(DatabaseReference.class);

        FirebaseDatabase mockedFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);
        when(mockedFirebaseDatabase.getReference()).thenReturn(mockedDatabaseReference);

        PowerMockito.mockStatic(FirebaseDatabase.class);
        when(FirebaseDatabase.getInstance()).thenReturn(mockedFirebaseDatabase);

        mockedChatArrayAdapter = mock(ChatArrayAdapter.class);

        //when(mockedChatArrayAdapter.add(any(ChatMessage.class))).thenReturn(null);

    }

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

    @Test
    public void testSendStudentQuestion(){

    }


}
