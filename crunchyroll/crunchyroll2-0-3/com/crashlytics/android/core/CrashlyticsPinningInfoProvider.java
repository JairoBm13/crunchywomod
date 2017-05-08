// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

import java.io.InputStream;
import io.fabric.sdk.android.services.network.PinningInfoProvider;

class CrashlyticsPinningInfoProvider implements PinningInfoProvider
{
    private final com.crashlytics.android.core.PinningInfoProvider pinningInfo;
    
    public CrashlyticsPinningInfoProvider(final com.crashlytics.android.core.PinningInfoProvider pinningInfo) {
        this.pinningInfo = pinningInfo;
    }
    
    @Override
    public String getKeyStorePassword() {
        return this.pinningInfo.getKeyStorePassword();
    }
    
    @Override
    public InputStream getKeyStoreStream() {
        return this.pinningInfo.getKeyStoreStream();
    }
    
    @Override
    public long getPinCreationTimeInMillis() {
        return -1L;
    }
    
    @Override
    public String[] getPins() {
        return this.pinningInfo.getPins();
    }
}
