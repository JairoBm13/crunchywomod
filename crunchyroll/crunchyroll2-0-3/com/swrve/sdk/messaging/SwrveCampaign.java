// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.messaging;

import java.util.Collections;
import java.util.Collection;
import android.util.Log;
import java.util.Map;
import java.util.Date;
import org.json.JSONException;
import java.util.Iterator;
import org.json.JSONArray;
import com.swrve.sdk.SwrveHelper;
import java.util.ArrayList;
import java.util.Set;
import org.json.JSONObject;
import com.swrve.sdk.SwrveBase;
import java.util.List;

public class SwrveCampaign extends SwrveBaseCampaign
{
    protected List<SwrveMessage> messages;
    
    public SwrveCampaign(final SwrveBase<?, ?> swrveBase, final JSONObject jsonObject, final Set<String> set) throws JSONException {
        super(swrveBase, jsonObject);
        this.messages = new ArrayList<SwrveMessage>();
        if (jsonObject.has("messages")) {
            final JSONArray jsonArray = jsonObject.getJSONArray("messages");
            for (int i = 0; i < jsonArray.length(); ++i) {
                final SwrveMessage message = this.createMessage(swrveBase, this, jsonArray.getJSONObject(i));
                if (message.getFormats().size() > 0 && set != null) {
                    for (final SwrveMessageFormat swrveMessageFormat : message.getFormats()) {
                        for (final SwrveButton swrveButton : swrveMessageFormat.getButtons()) {
                            if (!SwrveHelper.isNullOrEmpty(swrveButton.getImage())) {
                                set.add(swrveButton.getImage());
                            }
                        }
                        for (final SwrveImage swrveImage : swrveMessageFormat.getImages()) {
                            if (!SwrveHelper.isNullOrEmpty(swrveImage.getFile())) {
                                set.add(swrveImage.getFile());
                            }
                        }
                    }
                }
                if (message.getFormats().size() > 0) {
                    this.addMessage(message);
                }
            }
        }
    }
    
    protected void addMessage(final SwrveMessage swrveMessage) {
        this.messages.add(swrveMessage);
    }
    
    protected SwrveMessage createMessage(final SwrveBase<?, ?> swrveBase, final SwrveCampaign swrveCampaign, final JSONObject jsonObject) throws JSONException {
        return new SwrveMessage(swrveBase, swrveCampaign, jsonObject);
    }
    
    public SwrveMessage getMessageForEvent(final String s, final Date date, final Map<Integer, String> map) {
        if (this.checkCampaignLimits(s, date, map, this.messages.size(), "message")) {
            Log.i("SwrveMessagingSDK", s + " matches a trigger in " + this.id);
            return this.getNextMessage(map);
        }
        return null;
    }
    
    public List<SwrveMessage> getMessages() {
        return this.messages;
    }
    
    protected SwrveMessage getNextMessage(final Map<Integer, String> map) {
        if (this.randomOrder) {
            final ArrayList<SwrveMessage> list = new ArrayList<SwrveMessage>(this.messages);
            Collections.shuffle(list);
            for (final SwrveMessage swrveMessage : list) {
                if (swrveMessage.isDownloaded()) {
                    return swrveMessage;
                }
            }
        }
        else if (this.next < this.messages.size() && this.messages.get(this.next).isDownloaded()) {
            return this.messages.get(this.next);
        }
        this.logAndAddReason(map, "Campaign " + this.getId() + " hasn't finished downloading.");
        return null;
    }
    
    public void messageDismissed() {
        this.setMessageMinDelayThrottle();
    }
    
    public void messageWasShownToUser() {
        this.incrementImpressions();
        this.setMessageMinDelayThrottle();
        if (!this.isRandomOrder()) {
            final int next = (this.getNext() + 1) % this.getMessages().size();
            this.setNext(next);
            Log.i("SwrveMessagingSDK", "Round Robin: Next message in campaign " + this.getId() + " is " + next);
            return;
        }
        Log.i("SwrveMessagingSDK", "Next message in campaign " + this.getId() + " is random");
    }
}
