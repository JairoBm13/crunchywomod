// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.network;

import java.io.InputStream;

public interface PinningInfoProvider
{
    String getKeyStorePassword();
    
    InputStream getKeyStoreStream();
    
    long getPinCreationTimeInMillis();
    
    String[] getPins();
}
