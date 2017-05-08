// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.engine.model;

import java.io.Serializable;

public class ChoiceInputItem implements Serializable
{
    private String answer_id;
    private String answer_text;
    
    public ChoiceInputItem(final String answer_id, final String answer_text) {
        this.answer_id = answer_id;
        this.answer_text = answer_text;
    }
    
    public String getAnswerID() {
        return this.answer_id;
    }
    
    public String getAnswerText() {
        return this.answer_text;
    }
    
    @Override
    public String toString() {
        return this.getAnswerText();
    }
}
