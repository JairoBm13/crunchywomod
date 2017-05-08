// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.qa;

import com.swrve.sdk.SwrveHelper;
import com.swrve.sdk.rest.RESTResponse;
import com.swrve.sdk.messaging.SwrveMessage;
import com.swrve.sdk.conversations.SwrveConversation;
import android.util.Log;
import org.json.JSONArray;
import java.util.Map;
import java.util.Iterator;
import org.json.JSONException;
import com.swrve.sdk.rest.IRESTResponseListener;
import java.util.Date;
import com.swrve.sdk.rest.RESTClient;
import java.util.concurrent.Executors;
import java.util.Locale;
import org.json.JSONObject;
import java.util.HashSet;
import com.swrve.sdk.SwrveBase;
import java.util.concurrent.ExecutorService;
import com.swrve.sdk.rest.IRESTClient;
import java.text.SimpleDateFormat;
import java.lang.ref.WeakReference;
import java.util.Set;

public class SwrveQAUser
{
    private static Set<WeakReference<SwrveQAUser>> bindedObjects;
    protected final SimpleDateFormat deviceTimeFormat;
    private long lastSessionRequestTime;
    private long lastTriggerRequestTime;
    private boolean logging;
    private String loggingUrl;
    private boolean resetDevice;
    private IRESTClient restClient;
    protected ExecutorService restClientExecutor;
    private SwrveBase<?, ?> swrve;
    
    static {
        SwrveQAUser.bindedObjects = new HashSet<WeakReference<SwrveQAUser>>();
    }
    
    public SwrveQAUser(final SwrveBase<?, ?> swrve, final JSONObject jsonObject) {
        this.deviceTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ", Locale.US);
        this.swrve = swrve;
        this.resetDevice = jsonObject.optBoolean("reset_device_state", false);
        this.logging = jsonObject.optBoolean("logging", false);
        if (this.logging) {
            this.restClientExecutor = Executors.newSingleThreadExecutor();
            this.restClient = new RESTClient();
            this.loggingUrl = jsonObject.optString("logging_url", (String)null);
        }
    }
    
    private boolean canMakeRequest() {
        return this.swrve != null && this.isLogging();
    }
    
    private boolean canMakeSessionRequest() {
        if (this.canMakeRequest()) {
            final long time = new Date().getTime();
            if (this.lastSessionRequestTime == 0L || time - this.lastSessionRequestTime > 1000L) {
                this.lastSessionRequestTime = time;
                return true;
            }
        }
        return false;
    }
    
    private boolean canMakeTriggerRequest() {
        if (this.canMakeRequest()) {
            final long time = new Date().getTime();
            if (this.lastTriggerRequestTime == 0L || time - this.lastTriggerRequestTime > 500L) {
                this.lastTriggerRequestTime = time;
                return true;
            }
        }
        return false;
    }
    
    private void makeRequest(final String s, final JSONObject jsonObject) throws JSONException {
        jsonObject.put("version", 1);
        jsonObject.put("client_time", (Object)this.deviceTimeFormat.format(new Date()));
        this.restClientExecutor.execute(new Runnable() {
            final /* synthetic */ String val$body = jsonObject.toString();
            
            @Override
            public void run() {
                SwrveQAUser.this.restClient.post(s, this.val$body, new RESTResponseListener(s));
            }
        });
    }
    
    public void bindToServices() {
        final Iterator<WeakReference<SwrveQAUser>> iterator = SwrveQAUser.bindedObjects.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().get() == this) {
                return;
            }
        }
        SwrveQAUser.bindedObjects.add(new WeakReference<SwrveQAUser>(this));
    }
    
    public boolean isLogging() {
        return this.logging && this.loggingUrl != null;
    }
    
    public boolean isResetDevice() {
        return this.resetDevice;
    }
    
    public void talkSession(final Map<Integer, String> map) {
    Label_0175_Outer:
        while (true) {
            String string = null;
            JSONObject jsonObject = null;
            JSONArray jsonArray = null;
        Label_0218:
            while (true) {
            Label_0213:
                while (true) {
                    String s = null;
                    Label_0206: {
                        try {
                            if (this.canMakeSessionRequest()) {
                                string = this.loggingUrl + "/talk/game/" + this.swrve.getApiKey() + "/user/" + this.swrve.getUserId() + "/session";
                                jsonObject = new JSONObject();
                                jsonArray = new JSONArray();
                                for (final int intValue : map.keySet()) {
                                    s = map.get(intValue);
                                    final JSONObject jsonObject2 = new JSONObject();
                                    jsonObject2.put("id", intValue);
                                    if (s != null) {
                                        break Label_0206;
                                    }
                                    final String s2 = "";
                                    jsonObject2.put("reason", (Object)s2);
                                    if (s != null) {
                                        break Label_0213;
                                    }
                                    final boolean b = true;
                                    jsonObject2.put("loaded", b);
                                    jsonArray.put((Object)jsonObject2);
                                }
                                break Label_0218;
                            }
                        }
                        catch (Exception ex) {
                            Log.e("SwrveSDK", "QA request talk session failed", (Throwable)ex);
                        }
                        return;
                    }
                    final String s2 = s;
                    continue Label_0175_Outer;
                }
                final boolean b = false;
                continue;
            }
            jsonObject.put("campaigns", (Object)jsonArray);
            jsonObject.put("device", (Object)this.swrve.getDeviceInfo());
            this.makeRequest(string, jsonObject);
        }
    }
    
    public void trigger(String s, final SwrveConversation swrveConversation, final Map<Integer, String> map, final Map<Integer, Integer> map2) {
        String string = null;
        JSONObject jsonObject = null;
        boolean b;
        JSONArray jsonArray = null;
        String s2;
        Integer n = null;
        JSONObject jsonObject2;
        int intValue2;
        JSONObject jsonObject3;
        Label_0107_Outer:Label_0231_Outer:
        while (true) {
        Label_0305:
            while (true) {
            Label_0296:
                while (true) {
                Label_0290:
                    while (true) {
                        Label_0284: {
                            try {
                                if (this.canMakeTriggerRequest()) {
                                    string = this.loggingUrl + "/talk/game/" + this.swrve.getApiKey() + "/user/" + this.swrve.getUserId() + "/trigger";
                                    jsonObject = new JSONObject();
                                    jsonObject.put("trigger_name", (Object)s);
                                    if (swrveConversation == null) {
                                        break Label_0284;
                                    }
                                    b = true;
                                    jsonObject.put("displayed", b);
                                    if (swrveConversation == null) {
                                        s = "The loaded campaigns returned no conversation";
                                        jsonObject.put("reason", (Object)s);
                                        jsonArray = new JSONArray();
                                        for (final int intValue : map.keySet()) {
                                            s2 = map.get(intValue);
                                            n = map2.get(intValue);
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("id", intValue);
                                            jsonObject2.put("displayed", false);
                                            if (n != null) {
                                                break Label_0296;
                                            }
                                            intValue2 = -1;
                                            jsonObject2.put("conversation_id", intValue2);
                                            s = s2;
                                            if (s2 == null) {
                                                s = "";
                                            }
                                            jsonObject2.put("reason", (Object)s);
                                            jsonArray.put((Object)jsonObject2);
                                        }
                                        break Label_0305;
                                    }
                                    break Label_0290;
                                }
                            }
                            catch (Exception ex) {
                                Log.e("SwrveSDK", "QA request talk session failed", (Throwable)ex);
                            }
                            return;
                        }
                        b = false;
                        continue Label_0107_Outer;
                    }
                    s = "";
                    continue Label_0231_Outer;
                }
                intValue2 = n;
                continue;
            }
            if (swrveConversation != null) {
                jsonObject3 = new JSONObject();
                jsonObject3.put("id", swrveConversation.getCampaign().getId());
                jsonObject3.put("displayed", true);
                jsonObject3.put("conversation_id", swrveConversation.getId());
                jsonObject3.put("reason", (Object)"");
                jsonArray.put((Object)jsonObject3);
            }
            jsonObject.put("campaigns", (Object)jsonArray);
            this.makeRequest(string, jsonObject);
        }
    }
    
    public void trigger(String s, final SwrveMessage swrveMessage, final Map<Integer, String> map, final Map<Integer, Integer> map2) {
        String string = null;
        JSONObject jsonObject = null;
        boolean b;
        JSONArray jsonArray = null;
        String s2;
        Integer n = null;
        JSONObject jsonObject2;
        int intValue2;
        JSONObject jsonObject3;
        Label_0107_Outer:Label_0231_Outer:
        while (true) {
        Label_0305:
            while (true) {
            Label_0296:
                while (true) {
                Label_0290:
                    while (true) {
                        Label_0284: {
                            try {
                                if (this.canMakeTriggerRequest()) {
                                    string = this.loggingUrl + "/talk/game/" + this.swrve.getApiKey() + "/user/" + this.swrve.getUserId() + "/trigger";
                                    jsonObject = new JSONObject();
                                    jsonObject.put("trigger_name", (Object)s);
                                    if (swrveMessage == null) {
                                        break Label_0284;
                                    }
                                    b = true;
                                    jsonObject.put("displayed", b);
                                    if (swrveMessage == null) {
                                        s = "The loaded campaigns returned no message";
                                        jsonObject.put("reason", (Object)s);
                                        jsonArray = new JSONArray();
                                        for (final int intValue : map.keySet()) {
                                            s2 = map.get(intValue);
                                            n = map2.get(intValue);
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("id", intValue);
                                            jsonObject2.put("displayed", false);
                                            if (n != null) {
                                                break Label_0296;
                                            }
                                            intValue2 = -1;
                                            jsonObject2.put("message_id", intValue2);
                                            s = s2;
                                            if (s2 == null) {
                                                s = "";
                                            }
                                            jsonObject2.put("reason", (Object)s);
                                            jsonArray.put((Object)jsonObject2);
                                        }
                                        break Label_0305;
                                    }
                                    break Label_0290;
                                }
                            }
                            catch (Exception ex) {
                                Log.e("SwrveSDK", "QA request talk session failed", (Throwable)ex);
                            }
                            return;
                        }
                        b = false;
                        continue Label_0107_Outer;
                    }
                    s = "";
                    continue Label_0231_Outer;
                }
                intValue2 = n;
                continue;
            }
            if (swrveMessage != null) {
                jsonObject3 = new JSONObject();
                jsonObject3.put("id", swrveMessage.getCampaign().getId());
                jsonObject3.put("displayed", true);
                jsonObject3.put("message_id", swrveMessage.getId());
                jsonObject3.put("reason", (Object)"");
                jsonArray.put((Object)jsonObject3);
            }
            jsonObject.put("campaigns", (Object)jsonArray);
            this.makeRequest(string, jsonObject);
        }
    }
    
    public void triggerFailure(final String s, final String s2) {
        try {
            if (this.canMakeTriggerRequest()) {
                final String string = this.loggingUrl + "/talk/game/" + this.swrve.getApiKey() + "/user/" + this.swrve.getUserId() + "/trigger";
                final JSONObject jsonObject = new JSONObject();
                jsonObject.put("trigger_name", (Object)s);
                jsonObject.put("displayed", false);
                jsonObject.put("reason", (Object)s2);
                jsonObject.put("campaigns", (Object)new JSONArray());
                this.makeRequest(string, jsonObject);
            }
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "QA request talk session failed", (Throwable)ex);
        }
    }
    
    public void unbindToServices() {
        final Iterator<WeakReference<SwrveQAUser>> iterator = SwrveQAUser.bindedObjects.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().get() == this) {
                iterator.remove();
                break;
            }
        }
    }
    
    private class RESTResponseListener implements IRESTResponseListener
    {
        private String endpoint;
        
        public RESTResponseListener(final String endpoint) {
            this.endpoint = endpoint;
        }
        
        @Override
        public void onException(final Exception ex) {
            Log.e("SwrveSDK", "QA request to " + this.endpoint + " failed", (Throwable)ex);
        }
        
        @Override
        public void onResponse(final RESTResponse restResponse) {
            if (!SwrveHelper.successResponseCode(restResponse.responseCode)) {
                Log.e("SwrveSDK", "QA request to " + this.endpoint + " failed with error code " + restResponse.responseCode + ": " + restResponse.responseBody);
            }
        }
    }
}
