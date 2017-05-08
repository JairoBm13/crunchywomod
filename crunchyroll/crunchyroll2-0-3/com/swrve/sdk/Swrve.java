// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk;

import org.json.JSONException;
import org.json.JSONObject;
import java.lang.ref.WeakReference;
import android.app.Activity;
import android.content.Context;
import com.swrve.sdk.config.SwrveConfig;

public class Swrve extends SwrveBase<ISwrve, SwrveConfig> implements ISwrve
{
    protected Swrve(final Context context, final int appId, final String apiKey, final SwrveConfig config) {
        if (context instanceof Activity) {
            this.context = new WeakReference<Context>(context.getApplicationContext());
            this.activityContext = new WeakReference<Activity>((Activity)context);
        }
        else {
            this.context = new WeakReference<Context>(context);
        }
        this.appId = appId;
        this.apiKey = apiKey;
        this.config = (C)config;
    }
    
    @Override
    protected void afterBind() {
    }
    
    @Override
    protected void afterInit() {
    }
    
    @Override
    protected void beforeSendDeviceInfo(final Context context) {
    }
    
    @Override
    protected void extraDeviceInfo(final JSONObject jsonObject) throws JSONException {
    }
}
