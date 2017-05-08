// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.engine.model;

public class InputBase extends ConversationAtom
{
    private boolean error;
    private boolean optional;
    
    public InputBase() {
        this.optional = false;
    }
    
    public boolean hasError() {
        return this.error;
    }
    
    public boolean isOptional() {
        return this.optional;
    }
    
    public void setError(final boolean error) {
        this.error = error;
    }
}
