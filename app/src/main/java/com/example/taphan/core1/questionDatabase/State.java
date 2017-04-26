package com.example.taphan.core1.questionDatabase;

/**
 * The state class is used to let a question branch in the database link either to an answer or an
 * unanswered question in the question branch of the database
 *
 */

public class State {
    String answer;
    String questionID;

    public State(){

    }

    public State(String answer, String questionID){
        this.answer = answer;
        this.questionID = questionID;

    }

    public void setQuestionID(String questionID){
        this.questionID = questionID;

    }
    public String getQuestionID(){
        return questionID;
    }


    public void setAnswer(String answerID){
        this.answer = answerID;
    }

    public String getAnswer(){
        return answer;
    }


}