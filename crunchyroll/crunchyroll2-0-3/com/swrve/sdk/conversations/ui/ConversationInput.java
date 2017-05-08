// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.ui;

import java.util.Map;

public interface ConversationInput
{
    boolean isValid();
    
    void onReplyDataRequired(final Map<String, Object> p0);
}
