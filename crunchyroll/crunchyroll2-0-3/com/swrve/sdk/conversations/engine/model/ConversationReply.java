// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.engine.model;

import java.util.HashMap;

public class ConversationReply
{
    private String control;
    private HashMap<String, Object> data;
    
    public ConversationReply() {
        this.data = new HashMap<String, Object>();
    }
    
    public HashMap<String, Object> getData() {
        return this.data;
    }
    
    public void setControl(final String control) {
        this.control = control;
    }
}
