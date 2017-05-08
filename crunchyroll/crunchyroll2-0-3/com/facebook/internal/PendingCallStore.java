// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.internal;

import android.os.Parcelable;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import android.os.Bundle;
import java.util.UUID;
import java.util.HashMap;
import com.facebook.widget.FacebookDialog;
import java.util.Map;

public class PendingCallStore
{
    private static final String CALL_ID_ARRAY_KEY = "com.facebook.internal.PendingCallStore.callIdArrayKey";
    private static final String CALL_KEY_PREFIX = "com.facebook.internal.PendingCallStore.";
    private static PendingCallStore mInstance;
    private Map<String, FacebookDialog.PendingCall> pendingCallMap;
    
    public PendingCallStore() {
        this.pendingCallMap = new HashMap<String, FacebookDialog.PendingCall>();
    }
    
    private static void createInstance() {
        synchronized (PendingCallStore.class) {
            if (PendingCallStore.mInstance == null) {
                PendingCallStore.mInstance = new PendingCallStore();
            }
        }
    }
    
    public static PendingCallStore getInstance() {
        if (PendingCallStore.mInstance == null) {
            createInstance();
        }
        return PendingCallStore.mInstance;
    }
    
    private String getSavedStateKeyForPendingCallId(final String s) {
        return "com.facebook.internal.PendingCallStore." + s;
    }
    
    public FacebookDialog.PendingCall getPendingCallById(final UUID uuid) {
        if (uuid == null) {
            return null;
        }
        return this.pendingCallMap.get(uuid.toString());
    }
    
    public void restoreFromSavedInstanceState(final Bundle bundle) {
        final ArrayList stringArrayList = bundle.getStringArrayList("com.facebook.internal.PendingCallStore.callIdArrayKey");
        if (stringArrayList != null) {
            final Iterator<String> iterator = stringArrayList.iterator();
            while (iterator.hasNext()) {
                final FacebookDialog.PendingCall pendingCall = (FacebookDialog.PendingCall)bundle.getParcelable(this.getSavedStateKeyForPendingCallId(iterator.next()));
                if (pendingCall != null) {
                    this.pendingCallMap.put(pendingCall.getCallId().toString(), pendingCall);
                }
            }
        }
    }
    
    public void saveInstanceState(final Bundle bundle) {
        bundle.putStringArrayList("com.facebook.internal.PendingCallStore.callIdArrayKey", new ArrayList((Collection<? extends E>)this.pendingCallMap.keySet()));
        for (final FacebookDialog.PendingCall pendingCall : this.pendingCallMap.values()) {
            bundle.putParcelable(this.getSavedStateKeyForPendingCallId(pendingCall.getCallId().toString()), (Parcelable)pendingCall);
        }
    }
    
    public void stopTrackingPendingCall(final UUID uuid) {
        if (uuid != null) {
            this.pendingCallMap.remove(uuid.toString());
        }
    }
    
    public void trackPendingCall(final FacebookDialog.PendingCall pendingCall) {
        if (pendingCall != null) {
            this.pendingCallMap.put(pendingCall.getCallId().toString(), pendingCall);
        }
    }
}
