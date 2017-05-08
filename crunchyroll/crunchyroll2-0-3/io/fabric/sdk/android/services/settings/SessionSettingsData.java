// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.settings;

public class SessionSettingsData
{
    public final int identifierMask;
    public final int logBufferSize;
    public final int maxChainedExceptionDepth;
    public final int maxCustomExceptionEvents;
    public final int maxCustomKeyValuePairs;
    public final boolean sendSessionWithoutCrash;
    
    public SessionSettingsData(final int logBufferSize, final int maxChainedExceptionDepth, final int maxCustomExceptionEvents, final int maxCustomKeyValuePairs, final int identifierMask, final boolean sendSessionWithoutCrash) {
        this.logBufferSize = logBufferSize;
        this.maxChainedExceptionDepth = maxChainedExceptionDepth;
        this.maxCustomExceptionEvents = maxCustomExceptionEvents;
        this.maxCustomKeyValuePairs = maxCustomKeyValuePairs;
        this.identifierMask = identifierMask;
        this.sendSessionWithoutCrash = sendSessionWithoutCrash;
    }
}
