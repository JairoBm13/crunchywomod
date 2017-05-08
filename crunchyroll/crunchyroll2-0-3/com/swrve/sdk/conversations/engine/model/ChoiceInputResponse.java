// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.engine.model;

import java.io.Serializable;

public class ChoiceInputResponse implements Serializable
{
    public String answerID;
    public String answerText;
    public String fragmentTag;
    public String questionID;
    
    public String getAnswerID() {
        return this.answerID;
    }
    
    public String getAnswerText() {
        return this.answerText;
    }
    
    public String getQuestionID() {
        return this.questionID;
    }
    
    public void setAnswerID(final String answerID) {
        this.answerID = answerID;
    }
    
    public void setAnswerText(final String answerText) {
        this.answerText = answerText;
    }
    
    public void setFragmentTag(final String fragmentTag) {
        this.fragmentTag = fragmentTag;
    }
    
    public void setQuestionID(final String questionID) {
        this.questionID = questionID;
    }
}
