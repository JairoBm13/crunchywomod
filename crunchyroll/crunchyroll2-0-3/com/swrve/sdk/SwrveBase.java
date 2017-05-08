// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import com.swrve.sdk.messaging.ISwrveMessageListener;
import com.swrve.sdk.messaging.SwrveEventListener;
import com.swrve.sdk.conversations.ISwrveConversationListener;
import java.util.Collection;
import com.swrve.sdk.localstorage.ILocalStorage;
import java.util.LinkedHashMap;
import java.io.UnsupportedEncodingException;
import org.json.JSONArray;
import android.content.SharedPreferences$Editor;
import com.swrve.sdk.rest.RESTResponse;
import com.swrve.sdk.rest.IRESTResponseListener;
import java.util.Locale;
import com.swrve.sdk.conversations.engine.model.ChoiceInputResponse;
import com.swrve.sdk.conversations.engine.model.UserInputResult;
import android.app.Activity;
import com.swrve.sdk.messaging.SwrveMessageFormat;
import com.swrve.sdk.messaging.SwrveCampaign;
import com.swrve.sdk.messaging.SwrveOrientation;
import com.swrve.sdk.messaging.SwrveMessage;
import org.json.JSONException;
import android.view.Display;
import java.util.GregorianCalendar;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.os.Build$VERSION;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import com.swrve.sdk.messaging.SwrveConversationCampaign;
import com.swrve.sdk.messaging.SwrveBaseCampaign;
import java.util.ArrayList;
import android.content.Context;
import java.io.File;
import com.swrve.sdk.conversations.SwrveConversation;
import java.util.Map;
import java.util.HashMap;
import android.util.Log;
import com.swrve.sdk.messaging.SwrveActionType;
import com.swrve.sdk.messaging.SwrveButton;
import java.util.Date;
import com.swrve.sdk.config.SwrveConfigBase;

public abstract class SwrveBase<T, C extends SwrveConfigBase> extends SwrveImp<T, C> implements ISwrveBase<T, C>
{
    private boolean checkCampaignRules(final int n, final String s, final String s2, final Date date) {
        if (n == 0) {
            this.noMessagesWereShown(s2, "No " + s + "s available");
            return false;
        }
        if (!s2.equalsIgnoreCase("Swrve.Messages.showAtSessionStart") && this.isTooSoonToShowMessageAfterLaunch(date)) {
            this.noMessagesWereShown(s2, "{App throttle limit} Too soon after launch. Wait until " + this.timestampFormat.format(this.showMessagesAfterLaunch));
            return false;
        }
        if (this.isTooSoonToShowMessageAfterDelay(date)) {
            this.noMessagesWereShown(s2, "{App throttle limit} Too soon after last " + s + ". Wait until " + this.timestampFormat.format(this.showMessagesAfterDelay));
            return false;
        }
        if (this.hasShowTooManyMessagesAlready()) {
            this.noMessagesWereShown(s2, "{App Throttle limit} Too many " + s + "s shown");
            return false;
        }
        return true;
    }
    
    protected void _buttonWasPressedByUser(final SwrveButton swrveButton) {
        if (swrveButton.getActionType() != SwrveActionType.Dismiss) {
            final String string = "Swrve.Messages.Message-" + swrveButton.getMessage().getId() + ".click";
            Log.i("SwrveSDK", "Sending click event: " + string + "(" + swrveButton.getName() + ")");
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("name", swrveButton.getName());
            final HashMap<String, Object> hashMap2 = new HashMap<String, Object>();
            hashMap2.put("name", string);
            this.queueEvent("event", hashMap2, hashMap, false);
        }
    }
    
    protected void _conversationCallWasAccessedByUser(final SwrveConversation swrveConversation, final String s, final String s2) {
        if (swrveConversation != null) {
            final String string = this.getEventForConversation(swrveConversation) + ".call";
            Log.d("SwrveSDK", "Sending view conversation event: " + string);
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            final HashMap<String, String> hashMap2 = new HashMap<String, String>();
            hashMap2.put("name", string);
            hashMap.put("event", "call");
            hashMap.put("page", s);
            hashMap.put("control", s2);
            hashMap.put("conversation", Integer.toString(swrveConversation.getId()));
            this.queueEvent("event", (Map<String, Object>)hashMap2, hashMap, false);
            this.saveCampaignSettings();
        }
    }
    
    protected void _conversationDeeplinkWasAccessedByUser(final SwrveConversation swrveConversation, final String s, final String s2) {
        if (swrveConversation != null) {
            final String string = this.getEventForConversation(swrveConversation) + swrveConversation.getId() + ".deeplink";
            Log.d("SwrveSDK", "Sending view conversation event: " + string);
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            final HashMap<String, String> hashMap2 = new HashMap<String, String>();
            hashMap2.put("name", string);
            hashMap.put("event", "deeplink");
            hashMap.put("page", s);
            hashMap.put("control", s2);
            hashMap.put("conversation", Integer.toString(swrveConversation.getId()));
            this.queueEvent("event", (Map<String, Object>)hashMap2, hashMap, false);
            this.saveCampaignSettings();
        }
    }
    
    protected void _conversationEncounteredError(final SwrveConversation swrveConversation, final String s, final Exception ex) {
        if (swrveConversation != null) {
            final String string = this.getEventForConversation(swrveConversation) + ".error";
            if (ex != null) {
                Log.e("SwrveSDK", "Sending error conversation event: " + string, (Throwable)ex);
            }
            else {
                Log.e("SwrveSDK", "Sending error conversations event: (No Exception) " + string);
            }
            Log.d("SwrveSDK", "Sending error conversation event: " + string);
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            final HashMap<String, String> hashMap2 = new HashMap<String, String>();
            hashMap2.put("name", string);
            hashMap.put("event", "error");
            hashMap.put("page", s);
            hashMap.put("conversation", Integer.toString(swrveConversation.getId()));
            this.queueEvent("event", (Map<String, Object>)hashMap2, hashMap, false);
            this.saveCampaignSettings();
        }
    }
    
    protected void _conversationLinkVisitWasAccessedByUser(final SwrveConversation swrveConversation, final String s, final String s2) {
        if (swrveConversation != null) {
            final String string = this.getEventForConversation(swrveConversation) + swrveConversation.getId() + ".visit";
            Log.d("SwrveSDK", "Sending view conversation event: " + string);
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            final HashMap<String, String> hashMap2 = new HashMap<String, String>();
            hashMap2.put("name", string);
            hashMap.put("event", "visit");
            hashMap.put("page", s);
            hashMap.put("control", s2);
            hashMap.put("conversation", Integer.toString(swrveConversation.getId()));
            this.queueEvent("event", (Map<String, Object>)hashMap2, hashMap, false);
            this.saveCampaignSettings();
        }
    }
    
    protected void _conversationPageWasViewedByUser(final SwrveConversation swrveConversation, final String s) {
        if (swrveConversation != null) {
            swrveConversation.getCampaign();
            final String string = this.getEventForConversation(swrveConversation) + ".impression";
            Log.d("SwrveSDK", "Sending view conversation event: " + string);
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            final HashMap<String, String> hashMap2 = new HashMap<String, String>();
            hashMap2.put("name", string);
            hashMap.put("event", "impression");
            hashMap.put("page", s);
            hashMap.put("conversation", Integer.toString(swrveConversation.getId()));
            this.queueEvent("event", (Map<String, Object>)hashMap2, hashMap, false);
            this.saveCampaignSettings();
        }
    }
    
    protected void _conversationTransitionedToOtherPage(final SwrveConversation swrveConversation, final String s, final String s2, final String s3) {
        if (swrveConversation != null) {
            final String string = this.getEventForConversation(swrveConversation) + ".navigation";
            Log.d("SwrveSDK", "Sending view conversation event: " + string);
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            final HashMap<String, String> hashMap2 = new HashMap<String, String>();
            hashMap2.put("name", string);
            hashMap.put("event", "navigation");
            hashMap.put("control", s3);
            hashMap.put("to", s2);
            hashMap.put("page", s);
            hashMap.put("conversation", Integer.toString(swrveConversation.getId()));
            this.queueEvent("event", (Map<String, Object>)hashMap2, hashMap, false);
            this.saveCampaignSettings();
        }
    }
    
    protected void _conversationWasCancelledByUser(final SwrveConversation swrveConversation, final String s) {
        if (swrveConversation != null) {
            final String string = this.getEventForConversation(swrveConversation) + ".cancel";
            Log.d("SwrveSDK", "Sending view conversation event: " + string);
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            final HashMap<String, String> hashMap2 = new HashMap<String, String>();
            hashMap2.put("name", string);
            hashMap.put("event", "cancel");
            hashMap.put("page", s);
            hashMap.put("conversation", Integer.toString(swrveConversation.getId()));
            this.queueEvent("event", (Map<String, Object>)hashMap2, hashMap, false);
            this.saveCampaignSettings();
        }
    }
    
    protected void _conversationWasFinishedByUser(final SwrveConversation swrveConversation, final String s, final String s2) {
        if (swrveConversation != null) {
            final String string = this.getEventForConversation(swrveConversation) + ".done";
            Log.d("SwrveSDK", "Sending view conversation event: " + string);
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            final HashMap<String, String> hashMap2 = new HashMap<String, String>();
            hashMap2.put("name", string);
            hashMap.put("event", "done");
            hashMap.put("page", s);
            hashMap.put("control", s2);
            hashMap.put("conversation", Integer.toString(swrveConversation.getId()));
            this.queueEvent("event", (Map<String, Object>)hashMap2, hashMap, false);
            this.saveCampaignSettings();
        }
    }
    
    protected void _conversationWasStartedByUser(final SwrveConversation swrveConversation, final String s) {
        if (swrveConversation != null) {
            swrveConversation.getCampaign();
            final String string = this.getEventForConversation(swrveConversation) + ".start";
            Log.d("SwrveSDK", "Sending view conversation event: " + string);
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            final HashMap<String, String> hashMap2 = new HashMap<String, String>();
            hashMap2.put("name", string);
            hashMap.put("event", "start");
            hashMap.put("page", s);
            hashMap.put("conversation", Integer.toString(swrveConversation.getId()));
            this.queueEvent("event", (Map<String, Object>)hashMap2, hashMap, false);
            this.saveCampaignSettings();
        }
    }
    
    protected void _event(final String s) {
        this.event(s, null);
    }
    
    protected void _event(final String s, final Map<String, String> map) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("name", s);
        this.queueEvent("event", (Map<String, Object>)hashMap, map);
    }
    
    protected void _flushToDisk() {
        this.storageExecutorExecute(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("SwrveSDK", "Flushing to disk");
                    SwrveBase.this.cachedLocalStorage.flush();
                }
                catch (Exception ex) {
                    Log.e("SwrveSDK", "Flush to disk failed", (Throwable)ex);
                }
            }
        });
    }
    
    protected String _getApiKey() {
        return this.apiKey;
    }
    
    protected String _getAppStoreURLForApp(final int n) {
        return (String)this.appStoreURLs.get(n);
    }
    
    protected File _getCacheDir() {
        return this.cacheDir;
    }
    
    protected C _getConfig() {
        return this.config;
    }
    
    protected Context _getContext() {
        Object activityContext;
        if ((activityContext = this.context.get()) == null) {
            activityContext = this.getActivityContext();
        }
        return (Context)activityContext;
    }
    
    @SuppressLint({ "UseSparseArrays" })
    protected SwrveConversation _getConversationForEvent(final String s) {
        SwrveConversation swrveConversation = null;
        final SwrveConversation swrveConversation2 = null;
        final Date now = this.getNow();
        Map<Integer, String> map = null;
        final Map<Integer, String> map2 = null;
        Object o = null;
        List list = null;
        if (this.campaigns != null) {
            if (!this.checkCampaignRules(this.campaigns.size(), "conversation", s, now)) {
                return null;
            }
            o = list;
            map = map2;
            if (this.qaUser != null) {
                map = new HashMap<Integer, String>();
                o = new HashMap<Integer, Integer>();
            }
            synchronized (this.campaigns) {
                list = new ArrayList<Object>();
                for (final SwrveBaseCampaign swrveBaseCampaign : this.campaigns) {
                    if (swrveBaseCampaign instanceof SwrveConversationCampaign) {
                        final SwrveConversation conversationForEvent = ((SwrveConversationCampaign)swrveBaseCampaign).getConversationForEvent(s, now, map);
                        if (conversationForEvent == null) {
                            continue;
                        }
                        list.add(conversationForEvent);
                    }
                }
            }
            swrveConversation = swrveConversation2;
            if (list.size() > 0) {
                Collections.shuffle(list);
                swrveConversation = list.get(0);
            }
            if (this.qaUser != null && false && swrveConversation != null) {
                for (final SwrveConversation swrveConversation3 : list) {
                    if (swrveConversation3 != swrveConversation) {
                        final int id = swrveConversation3.getCampaign().getId();
                        if (!((Map)o).containsKey(id)) {
                            ((Map<Integer, Integer>)o).put(id, swrveConversation3.getId());
                            new StringBuilder().append("Campaign ");
                            throw new NullPointerException();
                        }
                        continue;
                    }
                }
            }
        }
        // monitorexit(list2)
        if (this.qaUser != null) {
            this.qaUser.trigger(s, swrveConversation, map, (Map<Integer, Integer>)o);
        }
        if (swrveConversation == null) {
            Log.w("SwrveSDK", "Not showing message: no candidate messages for " + s);
        }
        else {
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("id", String.valueOf(swrveConversation.getId()));
            final HashMap<String, String> hashMap2 = new HashMap<String, String>();
            hashMap2.put("name", "Swrve.Conversations.conversation_returned");
            this.queueEvent("event", (Map<String, Object>)hashMap2, hashMap, false);
        }
        return swrveConversation;
    }
    
    protected JSONObject _getDeviceInfo() throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("swrve.device_name", (Object)this.getDeviceName());
        jsonObject.put("swrve.os", (Object)"Android");
        jsonObject.put("swrve.os_version", (Object)Build$VERSION.RELEASE);
        final Context context = this.context.get();
        Label_0427: {
            if (context == null) {
                break Label_0427;
            }
            while (true) {
                try {
                    final Display defaultDisplay = ((WindowManager)context.getSystemService("window")).getDefaultDisplay();
                    final DisplayMetrics displayMetrics = new DisplayMetrics();
                    final int width = defaultDisplay.getWidth();
                    final int height = defaultDisplay.getHeight();
                    defaultDisplay.getMetrics(displayMetrics);
                    final float xdpi = displayMetrics.xdpi;
                    final float ydpi = displayMetrics.ydpi;
                    int n = height;
                    int n2 = width;
                    float n3 = xdpi;
                    float n4 = ydpi;
                    if (width > height) {
                        n2 = height;
                        n4 = xdpi;
                        n3 = ydpi;
                        n = width;
                    }
                    jsonObject.put("swrve.device_width", n2);
                    jsonObject.put("swrve.device_height", n);
                    jsonObject.put("swrve.device_dpi", displayMetrics.densityDpi);
                    jsonObject.put("swrve.android_device_xdpi", (double)n3);
                    jsonObject.put("swrve.android_device_ydpi", (double)n4);
                    jsonObject.put("swrve.conversation_version", 2);
                    if (!SwrveHelper.isNullOrEmpty(this.sim_operator_name)) {
                        jsonObject.put("swrve.sim_operator.name", (Object)this.sim_operator_name);
                    }
                    if (!SwrveHelper.isNullOrEmpty(this.sim_operator_iso_country_code)) {
                        jsonObject.put("swrve.sim_operator.iso_country_code", (Object)this.sim_operator_iso_country_code);
                    }
                    if (!SwrveHelper.isNullOrEmpty(this.sim_operator_code)) {
                        jsonObject.put("swrve.sim_operator.code", (Object)this.sim_operator_code);
                    }
                    jsonObject.put("swrve.language", (Object)this.language);
                    jsonObject.put("swrve.sdk_version", (Object)("Android " + SwrveBase.version));
                    jsonObject.put("swrve.app_store", (Object)this.config.getAppStore());
                    final GregorianCalendar gregorianCalendar = new GregorianCalendar();
                    jsonObject.put("swrve.timezone_name", (Object)gregorianCalendar.getTimeZone().getID());
                    jsonObject.put("swrve.utc_offset_seconds", gregorianCalendar.getTimeZone().getOffset(System.currentTimeMillis()) / 1000);
                    if (!SwrveHelper.isNullOrEmpty(this.userInstallTime)) {
                        jsonObject.put("swrve.install_date", (Object)this.userInstallTime);
                    }
                    this.extraDeviceInfo(jsonObject);
                    return jsonObject;
                }
                catch (Exception ex) {
                    Log.e("SwrveSDK", "Get device screen info failed", (Throwable)ex);
                    continue;
                }
                break;
            }
        }
    }
    
    protected Date _getInitialisedTime() {
        return this.initialisedTime;
    }
    
    protected SwrveMessage _getMessageForEvent(final String s) {
        return this.getMessageForEvent(s, SwrveOrientation.Both);
    }
    
    @SuppressLint({ "UseSparseArrays" })
    protected SwrveMessage _getMessageForEvent(final String s, final SwrveOrientation swrveOrientation) {
        Object o = null;
        final SwrveMessage swrveMessage = null;
        SwrveBaseCampaign campaign = null;
        final Date now = this.getNow();
        Map<Integer, String> map = null;
        final Map<Integer, String> map2 = null;
        Object o2 = null;
        List<Object> list = null;
        if (this.campaigns != null) {
            if (!this.checkCampaignRules(this.campaigns.size(), "message", s, now)) {
                return null;
            }
            o2 = list;
            map = map2;
            if (this.qaUser != null) {
                map = new HashMap<Integer, String>();
                o2 = new HashMap<Integer, Integer>();
            }
            synchronized (this.campaigns) {
                list = new ArrayList<Object>();
                int priority = Integer.MAX_VALUE;
                o = new ArrayList<SwrveMessage>();
                for (final SwrveBaseCampaign swrveBaseCampaign : this.campaigns) {
                    if (swrveBaseCampaign instanceof SwrveCampaign) {
                        final SwrveMessage messageForEvent = ((SwrveCampaign)swrveBaseCampaign).getMessageForEvent(s, now, map);
                        if (messageForEvent == null) {
                            continue;
                        }
                        list.add(messageForEvent);
                        if (messageForEvent.getPriority() > priority) {
                            continue;
                        }
                        priority = messageForEvent.getPriority();
                        if (messageForEvent.getPriority() < priority) {
                            ((List)o).clear();
                        }
                        ((List<SwrveMessage>)o).add(messageForEvent);
                    }
                }
            }
            Collections.shuffle((List)o);
            final Iterator<SwrveMessage> iterator2 = ((List<SwrveMessage>)o).iterator();
            o = swrveMessage;
            while (campaign == null && iterator2.hasNext()) {
                final SwrveMessage swrveMessage2 = iterator2.next();
                if (swrveMessage2.supportsOrientation(swrveOrientation)) {
                    o = swrveMessage2;
                    campaign = swrveMessage2.getCampaign();
                }
                else {
                    if (this.qaUser == null) {
                        continue;
                    }
                    final int id = swrveMessage2.getCampaign().getId();
                    ((Map<Integer, Integer>)o2).put(id, swrveMessage2.getId());
                    map.put(id, "Message didn't support the given orientation: " + swrveOrientation);
                }
            }
            if (this.qaUser != null && campaign != null && o != null) {
                for (final SwrveMessage swrveMessage3 : list) {
                    if (swrveMessage3 != o) {
                        final int id2 = swrveMessage3.getCampaign().getId();
                        if (((Map)o2).containsKey(id2)) {
                            continue;
                        }
                        ((Map<Integer, Integer>)o2).put(id2, swrveMessage3.getId());
                        map.put(id2, "Campaign " + campaign.getId() + " was selected for display ahead of this campaign");
                    }
                }
            }
        }
        // monitorexit(list2)
        if (this.qaUser != null) {
            this.qaUser.trigger(s, (SwrveMessage)o, map, (Map<Integer, Integer>)o2);
        }
        if (o == null) {
            Log.w("SwrveSDK", "Not showing message: no candidate messages for " + s);
        }
        else {
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("id", String.valueOf(((SwrveMessage)o).getId()));
            final HashMap<String, String> hashMap2 = new HashMap<String, String>();
            hashMap2.put("name", "Swrve.Messages.message_returned");
            this.queueEvent("event", (Map<String, Object>)hashMap2, hashMap, false);
        }
        return (SwrveMessage)o;
    }
    
    protected SwrveResourceManager _getResourceManager() {
        return this.resourceManager;
    }
    
    protected String _getUserId() {
        return this.userId;
    }
    
    protected void _messageWasShownToUser(final SwrveMessageFormat swrveMessageFormat) {
        if (swrveMessageFormat != null) {
            this.setMessageMinDelayThrottle();
            --this.messagesLeftToShow;
            final SwrveMessage message = swrveMessageFormat.getMessage();
            final SwrveCampaign campaign = message.getCampaign();
            if (campaign != null) {
                campaign.messageWasShownToUser();
            }
            final String string = "Swrve.Messages.Message-" + message.getId() + ".impression";
            Log.i("SwrveSDK", "Sending view event: " + string);
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("format", swrveMessageFormat.getName());
            hashMap.put("orientation", swrveMessageFormat.getOrientation().name());
            hashMap.put("size", swrveMessageFormat.getSize().x + "x" + swrveMessageFormat.getSize().y);
            final HashMap<String, Object> hashMap2 = new HashMap<String, Object>();
            hashMap2.put("name", string);
            this.queueEvent("event", hashMap2, hashMap, false);
            this.saveCampaignSettings();
        }
    }
    
    protected void _onDestroy(final Activity activity) {
        this.unbindAndShutdown(activity);
    }
    
    protected void _onLowMemory() {
        this.config.setTalkEnabled(false);
    }
    
    protected void _onPause() {
        if (this.campaignsAndResourcesExecutor != null) {
            this.campaignsAndResourcesExecutor.shutdown();
        }
        this.mustCleanInstance = true;
        final Activity activityContext = this.getActivityContext();
        if (activityContext != null) {
            this.mustCleanInstance = this.isActivityFinishing(activityContext);
        }
        Log.i("SwrveSDK", "onPause");
        this.flushToDisk();
        this.generateNewSessionInterval();
    }
    
    protected void _onResume(Activity activityContext) {
        Log.i("SwrveSDK", "onResume");
        if (activityContext != null) {
            this.bindToContext(activityContext);
        }
        this.startCampaignsAndResourcesTimer(true);
        this.disableAutoShowAfterDelay();
        this.queueDeviceInfoNow(false);
        if (this.getSessionTime() > this.lastSessionTick) {
            this.sessionStart();
        }
        else if (this.config.isSendQueuedEventsOnResume()) {
            this.sendQueuedEvents();
        }
        this.generateNewSessionInterval();
        activityContext = this.getActivityContext();
        if (activityContext != null && activityContext.getIntent() != null && activityContext.getIntent().getData() != null) {
            final String queryParameter = activityContext.getIntent().getData().getQueryParameter("referrer");
            if (!SwrveHelper.isNullOrEmpty(queryParameter)) {
                final HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("swrve.referrer_id", queryParameter);
                Log.i("SwrveSDK", "Received referrer, so sending userUpdate:" + hashMap);
                this.userUpdate(hashMap);
            }
        }
    }
    
    protected void _queueConversationEventsCommitedByUser(final SwrveConversation swrveConversation, final ArrayList<UserInputResult> list) {
        if (swrveConversation != null) {
            final String string = this.getEventForConversation(swrveConversation) + ".";
            for (final UserInputResult userInputResult : list) {
                final HashMap<String, String> hashMap = new HashMap<String, String>();
                final HashMap<String, Object> hashMap2 = new HashMap<String, Object>();
                hashMap2.put("name", string + userInputResult.getType());
                hashMap.put("page", userInputResult.getPageTag());
                hashMap.put("conversation", userInputResult.getConversationId());
                hashMap.put("event", userInputResult.getType());
                hashMap.put("fragment", userInputResult.getFragmentTag());
                if (userInputResult.isSingleChoice()) {
                    hashMap.put("result", ((ChoiceInputResponse)userInputResult.getResult()).getAnswerID());
                }
                else if (userInputResult.isMultiChoice()) {
                    final ChoiceInputResponse choiceInputResponse = (ChoiceInputResponse)userInputResult.getResult();
                    hashMap.put("set", choiceInputResponse.getQuestionID());
                    hashMap.put("result", choiceInputResponse.getAnswerID());
                }
                this.queueEvent("event", hashMap2, hashMap, false);
            }
            this.saveCampaignSettings();
        }
    }
    
    protected void _refreshCampaignsAndResources() {
        if (!this.config.isAutoDownloadCampaingsAndResources()) {
            final Date now = this.getNow();
            if (this.campaignsAndResourcesLastRefreshed != null && now.compareTo(new Date(this.campaignsAndResourcesLastRefreshed.getTime() + this.campaignsAndResourcesFlushFrequency)) < 0) {
                Log.i("SwrveSDK", "Request to retrieve campaign and user resource data was rate-limited");
                return;
            }
            this.campaignsAndResourcesLastRefreshed = now;
        }
        this.restClientExecutorExecute(new Runnable() {
            @Override
            public void run() {
                final HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("api_key", SwrveBase.this.apiKey);
                hashMap.put("user", SwrveBase.this.userId);
                hashMap.put("app_version", SwrveBase.this.appVersion);
                hashMap.put("joined", String.valueOf(SwrveBase.this.getOrWaitForInstallTime()));
                if (SwrveBase.this.config.isTalkEnabled()) {
                    hashMap.put("version", String.valueOf(5));
                    hashMap.put("conversation_version", String.valueOf(2));
                    hashMap.put("language", SwrveBase.this.language);
                    hashMap.put("app_store", SwrveBase.this.config.getAppStore());
                    hashMap.put("device_width", String.valueOf(SwrveBase.this.device_width));
                    hashMap.put("device_height", String.valueOf(SwrveBase.this.device_height));
                    hashMap.put("device_dpi", String.valueOf(SwrveBase.this.device_dpi));
                    hashMap.put("android_device_xdpi", String.valueOf(SwrveBase.this.android_device_xdpi));
                    hashMap.put("android_device_ydpi", String.valueOf(SwrveBase.this.android_device_ydpi));
                    hashMap.put("orientation", SwrveBase.this.config.getOrientation().toString().toLowerCase(Locale.US));
                    hashMap.put("device_name", SwrveBase.this.getDeviceName());
                    hashMap.put("os_version", Build$VERSION.RELEASE);
                }
                if (!SwrveHelper.isNullOrEmpty(SwrveBase.this.campaignsAndResourcesLastETag)) {
                    hashMap.put("etag", SwrveBase.this.campaignsAndResourcesLastETag);
                }
                try {
                    SwrveBase.this.restClient.get(SwrveBase.this.config.getContentUrl() + "/api/1/user_resources_and_campaigns", hashMap, new IRESTResponseListener() {
                        public void firstRefreshFinished() {
                            if (!SwrveBase.this.campaignsAndResourcesInitialized) {
                                SwrveBase.this.campaignsAndResourcesInitialized = true;
                                SwrveBase.this.autoShowMessages();
                                SwrveBase.this.invokeResourceListener();
                            }
                        }
                        
                        @Override
                        public void onException(final Exception ex) {
                            this.firstRefreshFinished();
                            Log.e("SwrveSDK", "Error downloading resources and campaigns", (Throwable)ex);
                        }
                        
                        @Override
                        public void onResponse(final RESTResponse restResponse) {
                            Label_0521: {
                                if (restResponse.responseCode != 200) {
                                    break Label_0521;
                                }
                                final SharedPreferences$Editor edit = SwrveBase.this.context.get().getSharedPreferences("swrve_prefs", 0).edit();
                                final String headerValue = restResponse.getHeaderValue("ETag");
                                if (!SwrveHelper.isNullOrEmpty(headerValue)) {
                                    edit.putString("campaigns_and_resources_etag", SwrveBase.this.campaignsAndResourcesLastETag = headerValue);
                                }
                                while (true) {
                                    try {
                                        final JSONObject jsonObject = new JSONObject(restResponse.responseBody);
                                        if (jsonObject.has("flush_frequency")) {
                                            final Integer value = jsonObject.getInt("flush_frequency");
                                            if (value != null) {
                                                SwrveBase.this.campaignsAndResourcesFlushFrequency = value;
                                                edit.putInt("swrve_cr_flush_frequency", (int)SwrveBase.this.campaignsAndResourcesFlushFrequency);
                                            }
                                        }
                                        if (jsonObject.has("flush_refresh_delay")) {
                                            final Integer value2 = jsonObject.getInt("flush_refresh_delay");
                                            if (value2 != null) {
                                                SwrveBase.this.campaignsAndResourcesFlushRefreshDelay = value2;
                                                edit.putInt("swrve_cr_flush_delay", (int)SwrveBase.this.campaignsAndResourcesFlushRefreshDelay);
                                            }
                                        }
                                        if (SwrveBase.this.config.isTalkEnabled() && jsonObject.has("campaigns")) {
                                            final JSONObject jsonObject2 = jsonObject.getJSONObject("campaigns");
                                            SwrveBase.this.updateCampaigns(jsonObject2, null);
                                            SwrveBase.this.saveCampaignsInCache(jsonObject2);
                                            SwrveBase.this.autoShowMessages();
                                            final HashMap<String, String> hashMap = new HashMap<String, String>();
                                            final StringBuilder sb = new StringBuilder();
                                            for (int i = 0; i < SwrveBase.this.campaigns.size(); ++i) {
                                                if (i != 0) {
                                                    sb.append(',');
                                                }
                                                sb.append(SwrveBase.this.campaigns.get(i).getId());
                                            }
                                            hashMap.put("ids", sb.toString());
                                            hashMap.put("count", String.valueOf(SwrveBase.this.campaigns.size()));
                                            final HashMap<String, String> hashMap2 = new HashMap<String, String>();
                                            hashMap2.put("name", "Swrve.Messages.campaigns_downloaded");
                                            SwrveBase.this.queueEvent("event", hashMap2, hashMap, false);
                                        }
                                        if (jsonObject.has("user_resources")) {
                                            final JSONArray jsonArray = jsonObject.getJSONArray("user_resources");
                                            SwrveBase.this.resourceManager.setResourcesFromJSON(jsonArray);
                                            SwrveBase.this.saveResourcesInCache(jsonArray);
                                            if (SwrveBase.this.campaignsAndResourcesInitialized) {
                                                SwrveBase.this.invokeResourceListener();
                                            }
                                        }
                                        edit.commit();
                                        this.firstRefreshFinished();
                                    }
                                    catch (JSONException ex) {
                                        Log.e("SwrveSDK", "Could not parse JSON for campaigns and resources", (Throwable)ex);
                                        continue;
                                    }
                                    break;
                                }
                            }
                        }
                    });
                }
                catch (UnsupportedEncodingException ex) {
                    Log.e("SwrveSDK", "Could not update resources and campaigns, invalid parameters", (Throwable)ex);
                }
            }
        });
    }
    
    protected void _sendQueuedEvents() {
        if (this.userId != null) {
            this.restClientExecutorExecute(new Runnable() {
                @Override
                public void run() {
                    final LinkedHashMap<ILocalStorage, LinkedHashMap<Long, String>> combinedFirstNEvents = SwrveBase.this.cachedLocalStorage.getCombinedFirstNEvents(SwrveBase.this.config.getMaxEventsPerFlush());
                    final LinkedHashMap linkedHashMap = new LinkedHashMap<Long, String>();
                    if (!combinedFirstNEvents.isEmpty()) {
                        Log.i("SwrveSDK", "Sending queued events");
                        Label_0097: {
                            try {
                                final Iterator<ILocalStorage> iterator = combinedFirstNEvents.keySet().iterator();
                                while (iterator.hasNext()) {
                                    linkedHashMap.putAll(combinedFirstNEvents.get(iterator.next()));
                                }
                                break Label_0097;
                            }
                            catch (JSONException ex) {
                                Log.e("SwrveSDK", "Unable to generate event batch", (Throwable)ex);
                            }
                            return;
                        }
                        SwrveBase.this.eventsWereSent = true;
                        final String eventsAsBatch = EventHelper.eventsAsBatch(SwrveBase.this.userId, SwrveBase.this.appVersion, SwrveBase.this.sessionToken, linkedHashMap, SwrveBase.this.cachedLocalStorage);
                        Log.i("SwrveSDK", "Sending " + linkedHashMap.size() + " events to Swrve");
                        SwrveBase.this.postBatchRequest(SwrveBase.this.config, eventsAsBatch, new IPostBatchRequestListener() {
                            @Override
                            public void onResponse(final boolean b) {
                                if (b) {
                                    for (final ILocalStorage localStorage : combinedFirstNEvents.keySet()) {
                                        localStorage.removeEventsById(((LinkedHashMap)combinedFirstNEvents.get(localStorage)).keySet());
                                    }
                                }
                                else {
                                    Log.e("SwrveSDK", "Batch of events could not be sent, retrying");
                                }
                            }
                        });
                    }
                }
            });
        }
    }
    
    protected void _sessionStart() {
        this.queueSessionStart();
        this.storageExecutorExecute(new Runnable() {
            @Override
            public void run() {
                SwrveBase.this.sendQueuedEvents();
            }
        });
    }
    
    protected void _setConversationListener(final ISwrveConversationListener conversationListener) {
        this.conversationListener = conversationListener;
        if (this.conversationListener != null) {
            this.eventListener = new SwrveEventListener(this, this.messageListener, this.conversationListener);
            return;
        }
        this.eventListener = null;
    }
    
    protected void _setMessageListener(final ISwrveMessageListener messageListener) {
        this.messageListener = messageListener;
        if (messageListener != null) {
            this.eventListener = new SwrveEventListener(this, messageListener, this.conversationListener);
            return;
        }
        this.eventListener = null;
    }
    
    protected void _shutdown() throws InterruptedException {
        if (!this.destroyed) {
            Log.i("SwrveSDK", "Shutting down the SDK");
            this.destroyed = true;
            SwrveBase.messageDisplayed = null;
            this.initialisedTime = null;
            this.removeCurrentDialog(null);
            this.activityContext = null;
            SwrveBase.messageDisplayed = null;
            if (this.qaUser != null) {
                this.qaUser.unbindToServices();
                this.qaUser = null;
            }
            this.restClientExecutor.shutdown();
            this.storageExecutor.shutdown();
            this.campaignsAndResourcesExecutor.shutdown();
            this.storageExecutor.awaitTermination(5L, TimeUnit.SECONDS);
            this.restClientExecutor.awaitTermination(5L, TimeUnit.SECONDS);
            this.cachedLocalStorage.close();
        }
    }
    
    protected void _userUpdate(final Map<String, String> map) {
        final HashMap<String, JSONObject> hashMap = new HashMap<String, JSONObject>();
        while (true) {
            try {
                hashMap.put("attributes", new JSONObject((Map)map));
                this.queueEvent("user", (Map<String, Object>)hashMap, null);
            }
            catch (NullPointerException ex) {
                Log.e("SwrveSDK", "JSONException when encoding user attributes", (Throwable)ex);
                continue;
            }
            break;
        }
    }
    
    protected abstract void afterBind();
    
    protected abstract void afterInit();
    
    protected abstract void beforeSendDeviceInfo(final Context p0);
    
    public void buttonWasPressedByUser(final SwrveButton swrveButton) {
        try {
            this._buttonWasPressedByUser(swrveButton);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    public void conversationCallActionCalledByUser(final SwrveConversation swrveConversation, final String s, final String s2) {
        try {
            this._conversationCallWasAccessedByUser(swrveConversation, s, s2);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    public void conversationDeeplinkActionCalledByUser(final SwrveConversation swrveConversation, final String s, final String s2) {
        try {
            this._conversationDeeplinkWasAccessedByUser(swrveConversation, s, s2);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    public void conversationEncounteredError(final SwrveConversation swrveConversation, final String s, final Exception ex) {
        try {
            this._conversationEncounteredError(swrveConversation, s, ex);
        }
        catch (Exception ex2) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex2);
        }
    }
    
    public void conversationEventsCommitedByUser(final SwrveConversation swrveConversation, final ArrayList<UserInputResult> list) {
        try {
            this._queueConversationEventsCommitedByUser(swrveConversation, list);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    public void conversationLinkVisitActionCalledByUser(final SwrveConversation swrveConversation, final String s, final String s2) {
        try {
            this._conversationLinkVisitWasAccessedByUser(swrveConversation, s, s2);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    public void conversationPageWasViewedByUser(final SwrveConversation swrveConversation, final String s) {
        try {
            this._conversationPageWasViewedByUser(swrveConversation, s);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    public void conversationTransitionedToOtherPage(final SwrveConversation swrveConversation, final String s, final String s2, final String s3) {
        try {
            this._conversationTransitionedToOtherPage(swrveConversation, s, s2, s3);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    public void conversationWasCancelledByUser(final SwrveConversation swrveConversation, final String s) {
        try {
            this._conversationWasCancelledByUser(swrveConversation, s);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    public void conversationWasFinishedByUser(final SwrveConversation swrveConversation, final String s, final String s2) {
        try {
            this._conversationWasFinishedByUser(swrveConversation, s, s2);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    public void conversationWasStartedByUser(final SwrveConversation swrveConversation, final String s) {
        try {
            this._conversationWasStartedByUser(swrveConversation, s);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    @Override
    public void event(final String s) {
        try {
            this._event(s);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    @Override
    public void event(final String s, final Map<String, String> map) {
        try {
            this._event(s, map);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    protected abstract void extraDeviceInfo(final JSONObject p0) throws JSONException;
    
    public void flushToDisk() {
        try {
            this._flushToDisk();
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    public String getApiKey() {
        try {
            return this._getApiKey();
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
            return null;
        }
    }
    
    public String getAppStoreURLForApp(final int n) {
        try {
            return this._getAppStoreURLForApp(n);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
            return null;
        }
    }
    
    public File getCacheDir() {
        try {
            return this._getCacheDir();
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
            return null;
        }
    }
    
    public C getConfig() {
        try {
            return this._getConfig();
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
            return null;
        }
    }
    
    public Context getContext() {
        try {
            return this._getContext();
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
            return null;
        }
    }
    
    public SwrveConversation getConversationForEvent(final String s) {
        try {
            return this._getConversationForEvent(s);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
            return null;
        }
    }
    
    public JSONObject getDeviceInfo() {
        try {
            return this._getDeviceInfo();
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
            return null;
        }
    }
    
    protected String getEventForConversation(final SwrveConversation swrveConversation) {
        return "Swrve.Conversations.Conversation-" + swrveConversation.getId();
    }
    
    public Date getInitialisedTime() {
        try {
            return this._getInitialisedTime();
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
            return null;
        }
    }
    
    public SwrveMessage getMessageForEvent(final String s) {
        try {
            return this._getMessageForEvent(s);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
            return null;
        }
    }
    
    public SwrveMessage getMessageForEvent(final String s, final SwrveOrientation swrveOrientation) {
        try {
            return this._getMessageForEvent(s, swrveOrientation);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
            return null;
        }
    }
    
    @Override
    public SwrveResourceManager getResourceManager() {
        try {
            return this._getResourceManager();
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
            return null;
        }
    }
    
    public String getUserId() {
        try {
            return this._getUserId();
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
            return null;
        }
    }
    
    protected T init(final Activity p0) throws IllegalArgumentException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_1        
        //     1: ifnonnull       10
        //     4: ldc_w           "Activity not specified"
        //     7: invokestatic    com/swrve/sdk/SwrveHelper.logAndThrowException:(Ljava/lang/String;)V
        //    10: aload_0        
        //    11: aload_0        
        //    12: invokevirtual   com/swrve/sdk/SwrveBase.getNow:()Ljava/util/Date;
        //    15: putfield        com/swrve/sdk/SwrveBase.initialisedTime:Ljava/util/Date;
        //    18: aload_0        
        //    19: iconst_1       
        //    20: putfield        com/swrve/sdk/SwrveBase.initialised:Z
        //    23: aload_0        
        //    24: aload_0        
        //    25: invokevirtual   com/swrve/sdk/SwrveBase.getSessionTime:()J
        //    28: putfield        com/swrve/sdk/SwrveBase.lastSessionTick:J
        //    31: aload_0        
        //    32: aload_0        
        //    33: getfield        com/swrve/sdk/SwrveBase.config:Lcom/swrve/sdk/config/SwrveConfigBase;
        //    36: invokevirtual   com/swrve/sdk/config/SwrveConfigBase.getUserId:()Ljava/lang/String;
        //    39: putfield        com/swrve/sdk/SwrveBase.userId:Ljava/lang/String;
        //    42: aload_0        
        //    43: aload_1        
        //    44: invokevirtual   com/swrve/sdk/SwrveBase.bindToContext:(Landroid/app/Activity;)Landroid/content/Context;
        //    47: astore          4
        //    49: aload_0        
        //    50: getfield        com/swrve/sdk/SwrveBase.config:Lcom/swrve/sdk/config/SwrveConfigBase;
        //    53: invokevirtual   com/swrve/sdk/config/SwrveConfigBase.isLoadCachedCampaignsAndResourcesOnUIThread:()Z
        //    56: istore_2       
        //    57: aload_0        
        //    58: getfield        com/swrve/sdk/SwrveBase.userId:Ljava/lang/String;
        //    61: invokestatic    com/swrve/sdk/SwrveHelper.isNullOrEmpty:(Ljava/lang/String;)Z
        //    64: ifeq            77
        //    67: aload_0        
        //    68: aload_0        
        //    69: aload           4
        //    71: invokevirtual   com/swrve/sdk/SwrveBase.getUniqueUserId:(Landroid/content/Context;)Ljava/lang/String;
        //    74: putfield        com/swrve/sdk/SwrveBase.userId:Ljava/lang/String;
        //    77: aload_0        
        //    78: aload_0        
        //    79: getfield        com/swrve/sdk/SwrveBase.userId:Ljava/lang/String;
        //    82: invokevirtual   com/swrve/sdk/SwrveBase.checkUserId:(Ljava/lang/String;)V
        //    85: aload_0        
        //    86: aload           4
        //    88: aload_0        
        //    89: getfield        com/swrve/sdk/SwrveBase.userId:Ljava/lang/String;
        //    92: invokevirtual   com/swrve/sdk/SwrveBase.saveUniqueUserId:(Landroid/content/Context;Ljava/lang/String;)V
        //    95: ldc             "SwrveSDK"
        //    97: new             Ljava/lang/StringBuilder;
        //   100: dup            
        //   101: invokespecial   java/lang/StringBuilder.<init>:()V
        //   104: ldc_w           "Your user id is: "
        //   107: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   110: aload_0        
        //   111: getfield        com/swrve/sdk/SwrveBase.userId:Ljava/lang/String;
        //   114: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   117: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   120: invokestatic    android/util/Log.i:(Ljava/lang/String;Ljava/lang/String;)I
        //   123: pop            
        //   124: aload_0        
        //   125: getfield        com/swrve/sdk/SwrveBase.config:Lcom/swrve/sdk/config/SwrveConfigBase;
        //   128: aload_0        
        //   129: getfield        com/swrve/sdk/SwrveBase.appId:I
        //   132: invokevirtual   com/swrve/sdk/config/SwrveConfigBase.generateUrls:(I)V
        //   135: aload_0        
        //   136: getfield        com/swrve/sdk/SwrveBase.config:Lcom/swrve/sdk/config/SwrveConfigBase;
        //   139: invokevirtual   com/swrve/sdk/config/SwrveConfigBase.getLanguage:()Ljava/lang/String;
        //   142: invokestatic    com/swrve/sdk/SwrveHelper.isNullOrEmpty:(Ljava/lang/String;)Z
        //   145: ifeq            660
        //   148: aload_0        
        //   149: invokestatic    java/util/Locale.getDefault:()Ljava/util/Locale;
        //   152: invokestatic    com/swrve/sdk/SwrveHelper.toLanguageTag:(Ljava/util/Locale;)Ljava/lang/String;
        //   155: putfield        com/swrve/sdk/SwrveBase.language:Ljava/lang/String;
        //   158: aload_0        
        //   159: aload_0        
        //   160: getfield        com/swrve/sdk/SwrveBase.config:Lcom/swrve/sdk/config/SwrveConfigBase;
        //   163: invokevirtual   com/swrve/sdk/config/SwrveConfigBase.getAppVersion:()Ljava/lang/String;
        //   166: putfield        com/swrve/sdk/SwrveBase.appVersion:Ljava/lang/String;
        //   169: aload_0        
        //   170: aload_0        
        //   171: getfield        com/swrve/sdk/SwrveBase.config:Lcom/swrve/sdk/config/SwrveConfigBase;
        //   174: invokevirtual   com/swrve/sdk/config/SwrveConfigBase.getNewSessionInterval:()J
        //   177: putfield        com/swrve/sdk/SwrveBase.newSessionInterval:J
        //   180: aload_0        
        //   181: aload_0        
        //   182: getfield        com/swrve/sdk/SwrveBase.apiKey:Ljava/lang/String;
        //   185: aload_0        
        //   186: getfield        com/swrve/sdk/SwrveBase.appId:I
        //   189: aload_0        
        //   190: getfield        com/swrve/sdk/SwrveBase.userId:Ljava/lang/String;
        //   193: invokestatic    com/swrve/sdk/SwrveHelper.generateSessionToken:(Ljava/lang/String;ILjava/lang/String;)Ljava/lang/String;
        //   196: putfield        com/swrve/sdk/SwrveBase.sessionToken:Ljava/lang/String;
        //   199: aload_0        
        //   200: getfield        com/swrve/sdk/SwrveBase.appVersion:Ljava/lang/String;
        //   203: invokestatic    com/swrve/sdk/SwrveHelper.isNullOrEmpty:(Ljava/lang/String;)Z
        //   206: istore_3       
        //   207: iload_3        
        //   208: ifeq            232
        //   211: aload_0        
        //   212: aload           4
        //   214: invokevirtual   android/content/Context.getPackageManager:()Landroid/content/pm/PackageManager;
        //   217: aload           4
        //   219: invokevirtual   android/content/Context.getPackageName:()Ljava/lang/String;
        //   222: iconst_0       
        //   223: invokevirtual   android/content/pm/PackageManager.getPackageInfo:(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;
        //   226: getfield        android/content/pm/PackageInfo.versionName:Ljava/lang/String;
        //   229: putfield        com/swrve/sdk/SwrveBase.appVersion:Ljava/lang/String;
        //   232: aload_0        
        //   233: aload_0        
        //   234: invokevirtual   com/swrve/sdk/SwrveBase.createRESTClient:()Lcom/swrve/sdk/rest/IRESTClient;
        //   237: putfield        com/swrve/sdk/SwrveBase.restClient:Lcom/swrve/sdk/rest/IRESTClient;
        //   240: aload_0        
        //   241: aload_0        
        //   242: invokevirtual   com/swrve/sdk/SwrveBase.createCachedLocalStorage:()Lcom/swrve/sdk/localstorage/MemoryCachedLocalStorage;
        //   245: putfield        com/swrve/sdk/SwrveBase.cachedLocalStorage:Lcom/swrve/sdk/localstorage/MemoryCachedLocalStorage;
        //   248: aload_0        
        //   249: aload_0        
        //   250: invokevirtual   com/swrve/sdk/SwrveBase.createStorageExecutor:()Ljava/util/concurrent/ExecutorService;
        //   253: putfield        com/swrve/sdk/SwrveBase.storageExecutor:Ljava/util/concurrent/ExecutorService;
        //   256: aload_0        
        //   257: aload_0        
        //   258: invokevirtual   com/swrve/sdk/SwrveBase.createRESTClientExecutor:()Ljava/util/concurrent/ExecutorService;
        //   261: putfield        com/swrve/sdk/SwrveBase.restClientExecutor:Ljava/util/concurrent/ExecutorService;
        //   264: aload_0        
        //   265: new             Landroid/util/SparseArray;
        //   268: dup            
        //   269: invokespecial   android/util/SparseArray.<init>:()V
        //   272: putfield        com/swrve/sdk/SwrveBase.appStoreURLs:Landroid/util/SparseArray;
        //   275: aload_0        
        //   276: aload           4
        //   278: invokevirtual   com/swrve/sdk/SwrveBase.findCacheFolder:(Landroid/content/Context;)V
        //   281: aload_0        
        //   282: aload           4
        //   284: invokevirtual   com/swrve/sdk/SwrveBase.beforeSendDeviceInfo:(Landroid/content/Context;)V
        //   287: aload_0        
        //   288: invokevirtual   com/swrve/sdk/SwrveBase.openLocalStorageConnection:()V
        //   291: aload_0        
        //   292: new             Lcom/swrve/sdk/SwrveResourceManager;
        //   295: dup            
        //   296: invokespecial   com/swrve/sdk/SwrveResourceManager.<init>:()V
        //   299: putfield        com/swrve/sdk/SwrveBase.resourceManager:Lcom/swrve/sdk/SwrveResourceManager;
        //   302: iload_2        
        //   303: ifeq            310
        //   306: aload_0        
        //   307: invokevirtual   com/swrve/sdk/SwrveBase.initResources:()V
        //   310: aload_0        
        //   311: invokevirtual   com/swrve/sdk/SwrveBase.queueSessionStart:()V
        //   314: aload_0        
        //   315: invokevirtual   com/swrve/sdk/SwrveBase.generateNewSessionInterval:()V
        //   318: aload_0        
        //   319: invokevirtual   com/swrve/sdk/SwrveBase.getSavedInstallTime:()Ljava/lang/String;
        //   322: invokestatic    com/swrve/sdk/SwrveHelper.isNullOrEmpty:(Ljava/lang/String;)Z
        //   325: ifeq            354
        //   328: aload_0        
        //   329: ldc_w           "Swrve.first_session"
        //   332: invokevirtual   com/swrve/sdk/SwrveBase.event:(Ljava/lang/String;)V
        //   335: aload_0        
        //   336: invokevirtual   com/swrve/sdk/SwrveBase.getNow:()Ljava/util/Date;
        //   339: astore          5
        //   341: aload_0        
        //   342: aload_0        
        //   343: getfield        com/swrve/sdk/SwrveBase.installTimeFormat:Ljava/text/SimpleDateFormat;
        //   346: aload           5
        //   348: invokevirtual   java/text/SimpleDateFormat.format:(Ljava/util/Date;)Ljava/lang/String;
        //   351: putfield        com/swrve/sdk/SwrveBase.userInstallTime:Ljava/lang/String;
        //   354: aload_0        
        //   355: getfield        com/swrve/sdk/SwrveBase.installTime:Ljava/util/concurrent/atomic/AtomicLong;
        //   358: aload_0        
        //   359: invokevirtual   com/swrve/sdk/SwrveBase.getInstallTime:()J
        //   362: invokevirtual   java/util/concurrent/atomic/AtomicLong.set:(J)V
        //   365: aload_0        
        //   366: getfield        com/swrve/sdk/SwrveBase.installTimeLatch:Ljava/util/concurrent/CountDownLatch;
        //   369: invokevirtual   java/util/concurrent/CountDownLatch.countDown:()V
        //   372: aload_1        
        //   373: ldc_w           "swrve_prefs"
        //   376: iconst_0       
        //   377: invokevirtual   android/app/Activity.getSharedPreferences:(Ljava/lang/String;I)Landroid/content/SharedPreferences;
        //   380: astore_1       
        //   381: aload_1        
        //   382: ldc_w           "swrve.referrer_id"
        //   385: aconst_null    
        //   386: invokeinterface android/content/SharedPreferences.getString:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   391: astore          5
        //   393: aload           5
        //   395: invokestatic    com/swrve/sdk/SwrveHelper.isNullOrEmpty:(Ljava/lang/String;)Z
        //   398: ifne            476
        //   401: new             Ljava/util/HashMap;
        //   404: dup            
        //   405: invokespecial   java/util/HashMap.<init>:()V
        //   408: astore          6
        //   410: aload           6
        //   412: ldc_w           "swrve.referrer_id"
        //   415: aload           5
        //   417: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   422: pop            
        //   423: ldc             "SwrveSDK"
        //   425: new             Ljava/lang/StringBuilder;
        //   428: dup            
        //   429: invokespecial   java/lang/StringBuilder.<init>:()V
        //   432: ldc_w           "Received install referrer, so sending userUpdate:"
        //   435: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   438: aload           6
        //   440: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   443: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   446: invokestatic    android/util/Log.i:(Ljava/lang/String;Ljava/lang/String;)I
        //   449: pop            
        //   450: aload_0        
        //   451: aload           6
        //   453: invokevirtual   com/swrve/sdk/SwrveBase.userUpdate:(Ljava/util/Map;)V
        //   456: aload_1        
        //   457: invokeinterface android/content/SharedPreferences.edit:()Landroid/content/SharedPreferences$Editor;
        //   462: ldc_w           "swrve.referrer_id"
        //   465: invokeinterface android/content/SharedPreferences$Editor.remove:(Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;
        //   470: invokeinterface android/content/SharedPreferences$Editor.commit:()Z
        //   475: pop            
        //   476: aload_0        
        //   477: aload           4
        //   479: invokevirtual   com/swrve/sdk/SwrveBase.getDeviceInfo:(Landroid/content/Context;)V
        //   482: aload_0        
        //   483: iconst_1       
        //   484: invokevirtual   com/swrve/sdk/SwrveBase.queueDeviceInfoNow:(Z)V
        //   487: aload_0        
        //   488: getfield        com/swrve/sdk/SwrveBase.config:Lcom/swrve/sdk/config/SwrveConfigBase;
        //   491: invokevirtual   com/swrve/sdk/config/SwrveConfigBase.isTalkEnabled:()Z
        //   494: ifeq            563
        //   497: aload_0        
        //   498: getfield        com/swrve/sdk/SwrveBase.language:Ljava/lang/String;
        //   501: invokestatic    com/swrve/sdk/SwrveHelper.isNullOrEmpty:(Ljava/lang/String;)Z
        //   504: ifeq            703
        //   507: ldc_w           "Language needed to use Talk"
        //   510: invokestatic    com/swrve/sdk/SwrveHelper.logAndThrowException:(Ljava/lang/String;)V
        //   513: iload_2        
        //   514: ifeq            521
        //   517: aload_0        
        //   518: invokevirtual   com/swrve/sdk/SwrveBase.initCampaigns:()V
        //   521: aload_0        
        //   522: getfield        com/swrve/sdk/SwrveBase.messageListener:Lcom/swrve/sdk/messaging/ISwrveMessageListener;
        //   525: ifnonnull       540
        //   528: aload_0        
        //   529: new             Lcom/swrve/sdk/SwrveBase$1;
        //   532: dup            
        //   533: aload_0        
        //   534: invokespecial   com/swrve/sdk/SwrveBase$1.<init>:(Lcom/swrve/sdk/SwrveBase;)V
        //   537: invokevirtual   com/swrve/sdk/SwrveBase.setMessageListener:(Lcom/swrve/sdk/messaging/ISwrveMessageListener;)V
        //   540: aload_0        
        //   541: getfield        com/swrve/sdk/SwrveBase.conversationListener:Lcom/swrve/sdk/conversations/ISwrveConversationListener;
        //   544: ifnonnull       559
        //   547: aload_0        
        //   548: new             Lcom/swrve/sdk/SwrveBase$2;
        //   551: dup            
        //   552: aload_0        
        //   553: invokespecial   com/swrve/sdk/SwrveBase$2.<init>:(Lcom/swrve/sdk/SwrveBase;)V
        //   556: invokevirtual   com/swrve/sdk/SwrveBase.setConversationListener:(Lcom/swrve/sdk/conversations/ISwrveConversationListener;)V
        //   559: aload_0        
        //   560: invokevirtual   com/swrve/sdk/SwrveBase.showPreviousMessage:()V
        //   563: aload_0        
        //   564: aload_1        
        //   565: ldc_w           "swrve_cr_flush_frequency"
        //   568: ldc_w           60000
        //   571: invokeinterface android/content/SharedPreferences.getInt:(Ljava/lang/String;I)I
        //   576: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   579: putfield        com/swrve/sdk/SwrveBase.campaignsAndResourcesFlushFrequency:Ljava/lang/Integer;
        //   582: aload_0        
        //   583: aload_1        
        //   584: ldc_w           "swrve_cr_flush_delay"
        //   587: sipush          5000
        //   590: invokeinterface android/content/SharedPreferences.getInt:(Ljava/lang/String;I)I
        //   595: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   598: putfield        com/swrve/sdk/SwrveBase.campaignsAndResourcesFlushRefreshDelay:Ljava/lang/Integer;
        //   601: aload_0        
        //   602: aload_1        
        //   603: ldc_w           "campaigns_and_resources_etag"
        //   606: aconst_null    
        //   607: invokeinterface android/content/SharedPreferences.getString:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   612: putfield        com/swrve/sdk/SwrveBase.campaignsAndResourcesLastETag:Ljava/lang/String;
        //   615: aload_0        
        //   616: iconst_1       
        //   617: invokevirtual   com/swrve/sdk/SwrveBase.startCampaignsAndResourcesTimer:(Z)V
        //   620: aload_0        
        //   621: invokevirtual   com/swrve/sdk/SwrveBase.disableAutoShowAfterDelay:()V
        //   624: iload_2        
        //   625: ifne            641
        //   628: aload_0        
        //   629: new             Lcom/swrve/sdk/SwrveBase$3;
        //   632: dup            
        //   633: aload_0        
        //   634: invokespecial   com/swrve/sdk/SwrveBase$3.<init>:(Lcom/swrve/sdk/SwrveBase;)V
        //   637: invokevirtual   com/swrve/sdk/SwrveBase.storageExecutorExecute:(Ljava/lang/Runnable;)Z
        //   640: pop            
        //   641: aload_0        
        //   642: invokevirtual   com/swrve/sdk/SwrveBase.sendCrashlyticsMetadata:()V
        //   645: aload_0        
        //   646: invokevirtual   com/swrve/sdk/SwrveBase.afterInit:()V
        //   649: ldc             "SwrveSDK"
        //   651: ldc_w           "Init finished"
        //   654: invokestatic    android/util/Log.i:(Ljava/lang/String;Ljava/lang/String;)I
        //   657: pop            
        //   658: aload_0        
        //   659: areturn        
        //   660: aload_0        
        //   661: aload_0        
        //   662: getfield        com/swrve/sdk/SwrveBase.config:Lcom/swrve/sdk/config/SwrveConfigBase;
        //   665: invokevirtual   com/swrve/sdk/config/SwrveConfigBase.getLanguage:()Ljava/lang/String;
        //   668: putfield        com/swrve/sdk/SwrveBase.language:Ljava/lang/String;
        //   671: goto            158
        //   674: astore_1       
        //   675: ldc             "SwrveSDK"
        //   677: ldc_w           "Swrve init failed"
        //   680: aload_1        
        //   681: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   684: pop            
        //   685: aload_0        
        //   686: areturn        
        //   687: astore          5
        //   689: ldc             "SwrveSDK"
        //   691: ldc_w           "Couldn't get app version from PackageManager. Please provide the app version manually through the config object."
        //   694: aload           5
        //   696: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   699: pop            
        //   700: goto            232
        //   703: aload_0        
        //   704: getfield        com/swrve/sdk/SwrveBase.config:Lcom/swrve/sdk/config/SwrveConfigBase;
        //   707: invokevirtual   com/swrve/sdk/config/SwrveConfigBase.getAppStore:()Ljava/lang/String;
        //   710: invokestatic    com/swrve/sdk/SwrveHelper.isNullOrEmpty:(Ljava/lang/String;)Z
        //   713: ifeq            513
        //   716: ldc_w           "App store needed to use Talk"
        //   719: invokestatic    com/swrve/sdk/SwrveHelper.logAndThrowException:(Ljava/lang/String;)V
        //   722: goto            513
        //    Exceptions:
        //  throws java.lang.IllegalArgumentException
        //    Signature:
        //  (Landroid/app/Activity;)TT;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  10     77     674    687    Ljava/lang/Exception;
        //  77     158    674    687    Ljava/lang/Exception;
        //  158    207    674    687    Ljava/lang/Exception;
        //  211    232    687    703    Ljava/lang/Exception;
        //  232    302    674    687    Ljava/lang/Exception;
        //  306    310    674    687    Ljava/lang/Exception;
        //  310    354    674    687    Ljava/lang/Exception;
        //  354    476    674    687    Ljava/lang/Exception;
        //  476    513    674    687    Ljava/lang/Exception;
        //  517    521    674    687    Ljava/lang/Exception;
        //  521    540    674    687    Ljava/lang/Exception;
        //  540    559    674    687    Ljava/lang/Exception;
        //  559    563    674    687    Ljava/lang/Exception;
        //  563    624    674    687    Ljava/lang/Exception;
        //  628    641    674    687    Ljava/lang/Exception;
        //  641    658    674    687    Ljava/lang/Exception;
        //  660    671    674    687    Ljava/lang/Exception;
        //  689    700    674    687    Ljava/lang/Exception;
        //  703    722    674    687    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0232:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public void messageWasShownToUser(final SwrveMessageFormat swrveMessageFormat) {
        try {
            this._messageWasShownToUser(swrveMessageFormat);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    @Override
    public T onCreate(final Activity activity) throws IllegalArgumentException {
        if (this.destroyed) {
            this.initialised = false;
            this.destroyed = false;
            this.bindCounter.set(0);
        }
        if (!this.initialised) {
            return this.init(activity);
        }
        this.bindToContext(activity);
        this.afterBind();
        this.showPreviousMessage();
        return (T)this;
    }
    
    @Override
    public void onDestroy(final Activity activity) {
        try {
            this._onDestroy(activity);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    @Override
    public void onLowMemory() {
        try {
            this._onLowMemory();
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    @Override
    public void onPause() {
        try {
            this._onPause();
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    @Override
    public void onResume(final Activity activity) {
        try {
            this._onResume(activity);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    public void refreshCampaignsAndResources() {
        try {
            this._refreshCampaignsAndResources();
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    public void sendQueuedEvents() {
        try {
            this._sendQueuedEvents();
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    public void sessionStart() {
        try {
            this._sessionStart();
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    public void setConversationListener(final ISwrveConversationListener swrveConversationListener) {
        try {
            this._setConversationListener(swrveConversationListener);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    public void setMessageListener(final ISwrveMessageListener swrveMessageListener) {
        try {
            this._setMessageListener(swrveMessageListener);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    public void setMessageMinDelayThrottle() {
        this.showMessagesAfterDelay = SwrveHelper.addTimeInterval(this.getNow(), this.minDelayBetweenMessage, 13);
    }
    
    public void shutdown() {
        try {
            this._shutdown();
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
    
    @Override
    public void userUpdate(final Map<String, String> map) {
        try {
            this._userUpdate(map);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
}
