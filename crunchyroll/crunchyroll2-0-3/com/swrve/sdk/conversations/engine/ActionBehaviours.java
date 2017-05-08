// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.engine;

import android.os.Bundle;
import android.content.ActivityNotFoundException;
import android.util.Log;
import android.content.Intent;
import android.app.Activity;
import android.net.Uri;

public class ActionBehaviours
{
    public static void openDeepLink(final Uri data, final Activity activity) {
        final String string = data.toString();
        final Intent setData = new Intent("android.intent.action.VIEW").setData(data);
        setData.addFlags(268435456);
        setData.addFlags(67108864);
        try {
            activity.startActivity(setData);
        }
        catch (ActivityNotFoundException ex) {
            Log.e("SwrveSDK", "Could not launch activity for uri: " + string + ". Possibly badly formatted deep link", (Throwable)ex);
            activity.finish();
        }
    }
    
    public static void openDialer(final Uri uri, final Activity activity) {
        activity.startActivity(new Intent("android.intent.action.DIAL", uri));
    }
    
    public static void openIntentWebView(final Uri uri, final Activity activity, final String s) {
        final Intent intent = new Intent("android.intent.action.VIEW", uri);
        final Bundle bundle = new Bundle();
        bundle.putString("referrer", s);
        intent.putExtra("com.android.browser.headers", bundle);
        activity.startActivity(intent);
    }
}
