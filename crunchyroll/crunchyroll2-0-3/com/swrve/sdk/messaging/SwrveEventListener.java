// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.messaging;

import android.content.Context;
import com.swrve.sdk.conversations.SwrveConversation;
import com.swrve.sdk.config.SwrveConfigBase;
import com.swrve.sdk.SwrveHelper;
import com.swrve.sdk.SwrveBase;
import java.lang.ref.WeakReference;
import com.swrve.sdk.conversations.ISwrveConversationListener;
import com.swrve.sdk.ISwrveEventListener;

public class SwrveEventListener implements ISwrveEventListener
{
    private ISwrveConversationListener conversationListener;
    private ISwrveMessageListener messageListener;
    private WeakReference<SwrveBase<?, ?>> talk;
    
    public SwrveEventListener(final SwrveBase<?, ?> swrveBase, final ISwrveMessageListener messageListener, final ISwrveConversationListener conversationListener) {
        this.talk = new WeakReference<SwrveBase<?, ?>>(swrveBase);
        this.messageListener = messageListener;
        this.conversationListener = conversationListener;
    }
    
    @Override
    public void onEvent(final String s) {
        Label_0060: {
            if (this.conversationListener == null || SwrveHelper.isNullOrEmpty(s)) {
                break Label_0060;
            }
            final SwrveBase<?, SwrveConfigBase> swrveBase = this.talk.get();
            if (swrveBase == null || !swrveBase.getConfig().isTalkEnabled()) {
                break Label_0060;
            }
            final SwrveConversation conversationForEvent = swrveBase.getConversationForEvent(s);
            if (conversationForEvent == null) {
                break Label_0060;
            }
            this.conversationListener.onMessage(conversationForEvent);
            return;
        }
        if (this.messageListener == null || SwrveHelper.isNullOrEmpty(s)) {
            return;
        }
        final SwrveBase<?, SwrveConfigBase> swrveBase2 = this.talk.get();
        if (swrveBase2 == null || !swrveBase2.getConfig().isTalkEnabled()) {
            return;
        }
        SwrveOrientation swrveOrientation = SwrveOrientation.Both;
        final Context context = swrveBase2.getContext();
        if (context != null) {
            swrveOrientation = SwrveOrientation.parse(context.getResources().getConfiguration().orientation);
        }
        final SwrveMessage messageForEvent = swrveBase2.getMessageForEvent(s, swrveOrientation);
        if (messageForEvent != null) {
            this.messageListener.onMessage(messageForEvent, true);
        }
    }
}
