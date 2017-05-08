// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.ui;

import java.io.Serializable;
import com.swrve.sdk.conversations.engine.model.UserInputResult;
import java.util.HashMap;
import com.swrve.sdk.conversations.engine.model.ConversationPage;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.swrve.sdk.conversations.SwrveConversation;
import android.support.v4.app.FragmentActivity;

public class ConversationActivity extends FragmentActivity
{
    private ConversationFragment conversationFragment;
    private SwrveConversation localConversation;
    
    @Override
    public void onBackPressed() {
        int onBackPressed = 1;
        while (true) {
            try {
                onBackPressed = (this.conversationFragment.onBackPressed() ? 1 : 0);
                if (onBackPressed != 0) {
                    super.onBackPressed();
                }
            }
            catch (NullPointerException ex) {
                Log.e("SwrveSDK", "Could not call the ConversationFragments onBackPressed()", (Throwable)ex);
                continue;
            }
            break;
        }
    }
    
    public void onCreate(Bundle extras) {
        super.onCreate(extras);
        final Intent intent = this.getIntent();
        if (intent != null) {
            extras = intent.getExtras();
            if (extras != null) {
                this.localConversation = (SwrveConversation)extras.getSerializable("conversation");
            }
        }
        try {
            if (this.localConversation != null) {
                (this.conversationFragment = ConversationFragment.create(this.localConversation)).commitConversationFragment(this.getSupportFragmentManager());
                this.setRequestedOrientation(7);
                return;
            }
            Log.e("SwrveSDK", "Could not render ConversationActivity. No SwrveConversation was detected");
            this.finish();
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Could not render ConversationActivity.", (Throwable)ex);
            this.finish();
        }
    }
    
    protected void onRestoreInstanceState(final Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        if (bundle != null && this.localConversation != null) {
            this.conversationFragment = ConversationFragment.create(this.localConversation);
            final ConversationPage page = (ConversationPage)bundle.getSerializable("page");
            final HashMap userInteractionData = (HashMap)bundle.getSerializable("userdata");
            if (page != null) {
                this.conversationFragment.setPage(page);
            }
            if (userInteractionData != null) {
                this.conversationFragment.setUserInteractionData(userInteractionData);
            }
            this.conversationFragment.commitConversationFragment(this.getSupportFragmentManager());
        }
    }
    
    @Override
    protected void onSaveInstanceState(final Bundle bundle) {
        bundle.putSerializable("page", (Serializable)this.conversationFragment.getPage());
        bundle.putSerializable("userdata", (Serializable)this.conversationFragment.getUserInteractionData());
        super.onSaveInstanceState(bundle);
    }
}
