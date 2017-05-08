// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.messaging;

import java.util.concurrent.TimeUnit;
import com.swrve.sdk.conversations.ISwrveConversationListener;
import java.util.Collection;
import com.swrve.sdk.localstorage.ILocalStorage;
import com.swrve.sdk.IPostBatchRequestListener;
import com.swrve.sdk.EventHelper;
import java.util.LinkedHashMap;
import java.io.UnsupportedEncodingException;
import android.content.SharedPreferences$Editor;
import com.swrve.sdk.rest.RESTResponse;
import com.swrve.sdk.rest.IRESTResponseListener;
import com.swrve.sdk.conversations.engine.model.ChoiceInputResponse;
import com.swrve.sdk.conversations.engine.model.UserInputResult;
import android.app.Activity;
import com.swrve.sdk.SwrveResourceManager;
import android.view.Display;
import java.util.GregorianCalendar;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.os.Build$VERSION;
import android.annotation.SuppressLint;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import android.content.Context;
import java.io.File;
import com.swrve.sdk.conversations.SwrveConversation;
import java.util.HashMap;
import com.swrve.sdk.ISwrveBase;
import com.swrve.sdk.SwrveImp;
import com.swrve.sdk.config.SwrveConfigBase;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import com.swrve.sdk.SwrveHelper;
import android.util.Log;
import org.json.JSONObject;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.text.SimpleDateFormat;
import com.swrve.sdk.SwrveBase;
import java.util.Date;

public abstract class SwrveBaseCampaign
{
    protected static int DEFAULT_DELAY_FIRST_MESSAGE;
    protected static int DEFAULT_MAX_IMPRESSIONS;
    protected static int DEFAULT_MIN_DELAY_BETWEEN_MSGS;
    protected int delayFirstMessage;
    protected Date endDate;
    protected int id;
    protected int impressions;
    protected int maxImpressions;
    protected int minDelayBetweenMessage;
    protected int next;
    protected boolean randomOrder;
    protected Date showMessagesAfterDelay;
    protected Date showMessagesAfterLaunch;
    protected Date startDate;
    protected transient SwrveBase<?, ?> talkController;
    protected final SimpleDateFormat timestampFormat;
    protected Set<String> triggers;
    
    static {
        SwrveBaseCampaign.DEFAULT_DELAY_FIRST_MESSAGE = 180;
        SwrveBaseCampaign.DEFAULT_MAX_IMPRESSIONS = 99999;
        SwrveBaseCampaign.DEFAULT_MIN_DELAY_BETWEEN_MSGS = 60;
    }
    
    private SwrveBaseCampaign() {
        this.timestampFormat = new SimpleDateFormat("HH:mm:ss ZZZZ", Locale.US);
        this.triggers = new HashSet<String>();
    }
    
    public SwrveBaseCampaign(final SwrveBase<?, ?> talkController, final JSONObject jsonObject) throws JSONException {
        this();
        this.setId(jsonObject.getInt("id"));
        this.setTalkController(talkController);
        Log.i("SwrveMessagingSDK", "Loading campaign " + this.getId());
        this.maxImpressions = SwrveBaseCampaign.DEFAULT_MAX_IMPRESSIONS;
        this.minDelayBetweenMessage = SwrveBaseCampaign.DEFAULT_MIN_DELAY_BETWEEN_MSGS;
        this.showMessagesAfterLaunch = SwrveHelper.addTimeInterval(this.talkController.getInitialisedTime(), SwrveBaseCampaign.DEFAULT_DELAY_FIRST_MESSAGE, 13);
        this.assignCampaignTriggers(this, jsonObject);
        this.assignCampaignRules(this, jsonObject);
        this.assignCampaignDates(this, jsonObject);
    }
    
    protected void assignCampaignDates(final SwrveBaseCampaign swrveBaseCampaign, final JSONObject jsonObject) throws JSONException {
        swrveBaseCampaign.setStartDate(new Date(jsonObject.getLong("start_date")));
        swrveBaseCampaign.setEndDate(new Date(jsonObject.getLong("end_date")));
    }
    
    protected void assignCampaignRules(final SwrveBaseCampaign swrveBaseCampaign, JSONObject jsonObject) throws JSONException {
        jsonObject = jsonObject.getJSONObject("rules");
        swrveBaseCampaign.setRandomOrder(jsonObject.getString("display_order").equals("random"));
        if (jsonObject.has("dismiss_after_views")) {
            this.setMaxImpressions(jsonObject.getInt("dismiss_after_views"));
        }
        if (jsonObject.has("delay_first_message")) {
            this.delayFirstMessage = jsonObject.getInt("delay_first_message");
            this.showMessagesAfterLaunch = SwrveHelper.addTimeInterval(this.talkController.getInitialisedTime(), this.delayFirstMessage, 13);
        }
        if (jsonObject.has("min_delay_between_messages")) {
            this.minDelayBetweenMessage = jsonObject.getInt("min_delay_between_messages");
        }
    }
    
    protected void assignCampaignTriggers(final SwrveBaseCampaign swrveBaseCampaign, final JSONObject jsonObject) throws JSONException {
        final JSONArray jsonArray = jsonObject.getJSONArray("triggers");
        for (int i = 0; i < jsonArray.length(); ++i) {
            swrveBaseCampaign.getTriggers().add(jsonArray.getString(i).toLowerCase(Locale.US));
        }
    }
    
    protected boolean checkCampaignLimits(final String s, final Date date, final Map<Integer, String> map, final int n, final String s2) {
        if (!this.hasElementForEvent(s)) {
            Log.i("SwrveMessagingSDK", "There is no trigger in " + this.id + " that matches " + s);
            return false;
        }
        if (n == 0) {
            this.logAndAddReason(map, "No " + s2 + "s in campaign " + this.id);
            return false;
        }
        if (this.startDate.after(date)) {
            this.logAndAddReason(map, "Campaign " + this.id + " has not started yet");
            return false;
        }
        if (this.endDate.before(date)) {
            this.logAndAddReason(map, "Campaign " + this.id + " has finished");
            return false;
        }
        if (this.impressions >= this.maxImpressions) {
            this.logAndAddReason(map, "{Campaign throttle limit} Campaign " + this.id + " has been shown " + this.maxImpressions + " times already");
            return false;
        }
        if (!s.equalsIgnoreCase(this.talkController.getAutoShowEventTrigger()) && this.isTooSoonToShowMessageAfterLaunch(date)) {
            this.logAndAddReason(map, "{Campaign throttle limit} Too soon after launch. Wait until " + this.timestampFormat.format(this.showMessagesAfterLaunch));
            return false;
        }
        if (this.isTooSoonToShowMessageAfterDelay(date)) {
            this.logAndAddReason(map, "{Campaign throttle limit} Too soon after last " + s2 + ". Wait until " + this.timestampFormat.format(this.showMessagesAfterDelay));
            return false;
        }
        return true;
    }
    
    public JSONObject createSettings() throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("next", this.next);
        jsonObject.put("impressions", this.impressions);
        return jsonObject;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getNext() {
        return this.next;
    }
    
    public Set<String> getTriggers() {
        return this.triggers;
    }
    
    public boolean hasElementForEvent(String lowerCase) {
        lowerCase = lowerCase.toLowerCase(Locale.US);
        return this.triggers != null && this.triggers.contains(lowerCase);
    }
    
    public void incrementImpressions() {
        ++this.impressions;
    }
    
    public boolean isRandomOrder() {
        return this.randomOrder;
    }
    
    protected boolean isTooSoonToShowMessageAfterDelay(final Date date) {
        return this.showMessagesAfterDelay != null && date.before(this.showMessagesAfterDelay);
    }
    
    protected boolean isTooSoonToShowMessageAfterLaunch(final Date date) {
        return date.before(this.showMessagesAfterLaunch);
    }
    
    public void loadSettings(final JSONObject jsonObject) throws JSONException {
        try {
            if (jsonObject.has("next")) {
                this.next = jsonObject.getInt("next");
            }
            if (jsonObject.has("impressions")) {
                this.impressions = jsonObject.getInt("impressions");
            }
        }
        catch (Exception ex) {
            Log.e("SwrveMessagingSDK", "Error while trying to load campaign settings", (Throwable)ex);
        }
    }
    
    protected void logAndAddReason(final Map<Integer, String> map, final String s) {
        if (map != null) {
            map.put(this.id, s);
        }
        Log.i("SwrveMessagingSDK", s);
    }
    
    protected void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }
    
    protected void setId(final int id) {
        this.id = id;
    }
    
    public void setMaxImpressions(final int maxImpressions) {
        this.maxImpressions = maxImpressions;
    }
    
    protected void setMessageMinDelayThrottle() {
        this.showMessagesAfterDelay = SwrveHelper.addTimeInterval(this.talkController.getNow(), this.minDelayBetweenMessage, 13);
        this.talkController.setMessageMinDelayThrottle();
    }
    
    public void setNext(final int next) {
        this.next = next;
    }
    
    protected void setRandomOrder(final boolean randomOrder) {
        this.randomOrder = randomOrder;
    }
    
    protected void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }
    
    protected void setTalkController(final SwrveBase<?, ?> talkController) {
        this.talkController = talkController;
    }
}
