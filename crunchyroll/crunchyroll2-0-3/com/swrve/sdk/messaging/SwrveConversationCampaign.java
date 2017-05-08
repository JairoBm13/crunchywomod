// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.messaging;

import android.util.Log;
import java.util.Map;
import java.util.Date;
import org.json.JSONException;
import java.util.Iterator;
import com.swrve.sdk.conversations.engine.model.Content;
import com.swrve.sdk.conversations.engine.model.ConversationAtom;
import com.swrve.sdk.conversations.engine.model.ConversationPage;
import java.util.Set;
import org.json.JSONObject;
import com.swrve.sdk.SwrveBase;
import com.swrve.sdk.conversations.SwrveConversation;
import java.io.Serializable;

public class SwrveConversationCampaign extends SwrveBaseCampaign implements Serializable
{
    protected SwrveConversation conversation;
    
    public SwrveConversationCampaign(final SwrveBase<?, ?> swrveBase, final JSONObject jsonObject, final Set<String> set) throws JSONException {
        super(swrveBase, jsonObject);
        if (jsonObject.has("conversation")) {
            this.conversation = this.createConversation(swrveBase, this, jsonObject.getJSONObject("conversation"));
            final Iterator<ConversationPage> iterator = this.conversation.getPages().iterator();
            while (iterator.hasNext()) {
                for (final ConversationAtom conversationAtom : iterator.next().getContent()) {
                    if ("image".equalsIgnoreCase(conversationAtom.getType().toString())) {
                        set.add(((Content)conversationAtom).getValue());
                    }
                }
            }
        }
    }
    
    protected SwrveConversation createConversation(final SwrveBase<?, ?> swrveBase, final SwrveConversationCampaign swrveConversationCampaign, final JSONObject jsonObject) throws JSONException {
        return new SwrveConversation(swrveBase, swrveConversationCampaign, jsonObject);
    }
    
    public SwrveConversation getConversationForEvent(final String s, final Date date, final Map<Integer, String> map) {
        if (this.checkCampaignLimits(s, date, map, 1, "conversation") && this.conversation != null && this.conversation.isDownloaded()) {
            Log.i("SwrveMessagingSDK", s + " matches a trigger in " + this.id);
            return this.conversation;
        }
        return null;
    }
}
