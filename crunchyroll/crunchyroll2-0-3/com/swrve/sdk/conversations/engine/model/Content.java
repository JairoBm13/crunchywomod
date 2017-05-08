// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.engine.model;

public class Content extends ConversationAtom
{
    protected String height;
    protected String value;
    
    public String getHeight() {
        if (this.height == null || Integer.parseInt(this.height) <= 0) {
            return "0";
        }
        return this.height;
    }
    
    public String getValue() {
        return this.value;
    }
}
