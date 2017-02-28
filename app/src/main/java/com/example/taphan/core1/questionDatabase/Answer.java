package com.example.taphan.core1.questionDatabase;

import java.util.ArrayList;

/* A answer can be the answer to multiple questions
   Answers exist independent of subjects, and are only referenced by questions.
   Should be deleted if no question reference it
   should contain a list of questions which are "listeners"
*/

public class Answer{

    private String answerID;  // Key
    private String answer;
    private ArrayList<String> listeners;

    public Answer(){

    }

    public String getAnswerID() {
        return answerID;
    }

    public void setAnswerID(String answerID) {
        this.answerID = answerID;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void addListener(String questionKey){
        listeners.add(questionKey);
    }

    /* The removeListener function deletes questions up for deletion from the listener
       list, it also return whether the list is empty or not. If the list is empty the
       answer isn't linked to anything and must be deleted as not to waste space.
     */

    public boolean removeListener(String questionkey){
        listeners.remove(questionkey);
        return listeners.isEmpty();
    }

}
