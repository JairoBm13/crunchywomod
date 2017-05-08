// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.engine.model;

import java.io.Serializable;

public class UserInputResult implements Serializable
{
    public String conversationId;
    public String fragmentTag;
    public String pageTag;
    public Object result;
    public String type;
    
    public String getConversationId() {
        return this.conversationId;
    }
    
    public String getFragmentTag() {
        return this.fragmentTag;
    }
    
    public String getPageTag() {
        return this.pageTag;
    }
    
    public Object getResult() {
        return this.result;
    }
    
    public String getType() {
        return this.type;
    }
    
    public boolean isMultiChoice() {
        return this.type.equalsIgnoreCase("multi-choice");
    }
    
    public boolean isSingleChoice() {
        return this.type.equalsIgnoreCase("choice");
    }
}
