// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.cast;

import com.google.android.gms.cast.CastDevice;
import android.util.Log;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.cast.Cast;

public class CustomChannel implements MessageReceivedCallback
{
    private final String TAG;
    private final GoogleApiClient mClient;
    
    public CustomChannel(final GoogleApiClient mClient) {
        this.TAG = this.getClass().getSimpleName();
        this.mClient = mClient;
    }
    
    public static String getNamespace() {
        return "urn:x-cast:com.crunchyroll.chromecast";
    }
    
    private void sendMessage(final String s) {
        try {
            Cast.CastApi.sendMessage(this.mClient, getNamespace(), s).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(final Status status) {
                }
            });
        }
        catch (IllegalStateException ex) {
            Log.w(this.TAG, "Unable to send message");
        }
    }
    
    @Override
    public void onMessageReceived(final CastDevice castDevice, final String s, final String s2) {
    }
    
    public void sendConnectMessage() {
        this.sendMessage("{\"type\":\"connect\"}");
    }
    
    public void sendDisconnectMessage() {
        this.sendMessage("{\"type\":\"disconnect\"}");
    }
    
    public void sendSetLocaleMessage(final String s) {
        this.sendMessage("{\"type\":\"change_locale\", \"locale\":\"" + s + "\"}");
    }
    
    public void sendTrackClickthroughMessage() {
        this.sendMessage("{\"type\":\"ad_click\"}");
    }
}
