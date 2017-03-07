package com.example.taphan.core1.questionDatabase;

import java.util.ArrayList;

/* A answer can be the answer to multiple questions
   Answers exist independent of subjects, and are only referenced by questions.
   Should be deleted if no question reference it
   should contain a list of questions which are "listeners"
*/

public class Answer{

    private String answerID;  // Key
    private String answerTxt;
    private ArrayList<String> questions;

    public Answer(){
        questions = new ArrayList<>();

    }

    public String getAnswerID() {
        return answerID;
    }

    public void setAnswerID(String answerID) {
        this.answerID = answerID;
    }

    public String getAnswerTxt() {
        return answerTxt;
    }

    public void setAnswerTxt(String answerTxt) {
        this.answerTxt = answerTxt;
    }

    public void addQuestion(String questionKey){
        questions.add(questionKey);
    }

    /* The removeListener function deletes questions up for deletion from the listener
       list, it also return whether the list is empty or not. If the list is empty the
       answer isn't linked to anything and must be deleted as not to waste space.
     */

    public boolean removeListener(String questionKey){
        questions.remove(questionKey);
        return questions.isEmpty();
    }

}
