// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.network;

import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

public final class NetworkUtils
{
    public static final SSLSocketFactory getSSLSocketFactory(final PinningInfoProvider pinningInfoProvider) throws KeyManagementException, NoSuchAlgorithmException {
        final SSLContext instance = SSLContext.getInstance("TLS");
        instance.init(null, new TrustManager[] { new PinningTrustManager(new SystemKeyStore(pinningInfoProvider.getKeyStoreStream(), pinningInfoProvider.getKeyStorePassword()), pinningInfoProvider) }, null);
        return instance.getSocketFactory();
    }
}
