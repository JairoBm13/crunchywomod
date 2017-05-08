// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.engine.model;

import com.swrve.sdk.conversations.engine.model.styles.AtomStyle;
import java.io.Serializable;

public abstract class ConversationAtom implements Serializable
{
    protected AtomStyle style;
    protected String tag;
    protected String type;
    
    public static ConversationAtom create(final String tag, final String type) {
        final BareConversationAtom bareConversationAtom = new BareConversationAtom();
        bareConversationAtom.tag = tag;
        bareConversationAtom.type = type;
        return bareConversationAtom;
    }
    
    public AtomStyle getStyle() {
        return this.style;
    }
    
    public String getTag() {
        return this.tag;
    }
    
    public String getType() {
        return this.type;
    }
    
    private static class BareConversationAtom extends ConversationAtom
    {
    }
}
