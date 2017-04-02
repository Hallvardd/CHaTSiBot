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

    public State(String answerID, String questionID){
        this.answer = answerID;
        this.questionID = questionID;

    }

    public void setQuestion(String questionID){
        this.questionID = questionID;

    }
    public String getQuestionID(){
        return questionID;
    }


    public void setAnswer(String answer){
        this.answer = answer;
    }

    public String getAnswer(){
        return answer;
    }


}