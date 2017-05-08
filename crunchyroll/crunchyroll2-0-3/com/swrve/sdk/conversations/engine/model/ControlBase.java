// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.engine.model;

public abstract class ControlBase extends ConversationAtom
{
    protected ControlActions action;
    protected String target;
    
    public ControlActions getActions() {
        return this.action;
    }
    
    public String getTarget() {
        return this.target;
    }
    
    public boolean hasActions() {
        return this.action != null;
    }
}
