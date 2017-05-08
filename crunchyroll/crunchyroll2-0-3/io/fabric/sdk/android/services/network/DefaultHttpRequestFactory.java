// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.network;

import javax.net.ssl.HttpsURLConnection;
import java.util.Map;
import java.util.Locale;
import io.fabric.sdk.android.DefaultLogger;
import javax.net.ssl.SSLSocketFactory;
import io.fabric.sdk.android.Logger;

public class DefaultHttpRequestFactory implements HttpRequestFactory
{
    private boolean attemptedSslInit;
    private final Logger logger;
    private PinningInfoProvider pinningInfo;
    private SSLSocketFactory sslSocketFactory;
    
    public DefaultHttpRequestFactory() {
        this(new DefaultLogger());
    }
    
    public DefaultHttpRequestFactory(final Logger logger) {
        this.logger = logger;
    }
    
    private SSLSocketFactory getSSLSocketFactory() {
        synchronized (this) {
            if (this.sslSocketFactory == null && !this.attemptedSslInit) {
                this.sslSocketFactory = this.initSSLSocketFactory();
            }
            return this.sslSocketFactory;
        }
    }
    
    private SSLSocketFactory initSSLSocketFactory() {
        synchronized (this) {
            this.attemptedSslInit = true;
            try {
                final SSLSocketFactory sslSocketFactory = NetworkUtils.getSSLSocketFactory(this.pinningInfo);
                this.logger.d("Fabric", "Custom SSL pinning enabled");
                return sslSocketFactory;
            }
            catch (Exception ex) {
                this.logger.e("Fabric", "Exception while validating pinned certs", ex);
                final SSLSocketFactory sslSocketFactory = null;
            }
        }
    }
    
    private boolean isHttps(final String s) {
        return s != null && s.toLowerCase(Locale.US).startsWith("https");
    }
    
    private void resetSSLSocketFactory() {
        synchronized (this) {
            this.attemptedSslInit = false;
            this.sslSocketFactory = null;
        }
    }
    
    @Override
    public HttpRequest buildHttpRequest(final HttpMethod httpMethod, final String s, final Map<String, String> map) {
        HttpRequest httpRequest = null;
        switch (httpMethod) {
            default: {
                throw new IllegalArgumentException("Unsupported HTTP method!");
            }
            case GET: {
                httpRequest = HttpRequest.get(s, map, true);
                break;
            }
            case POST: {
                httpRequest = HttpRequest.post(s, map, true);
                break;
            }
            case PUT: {
                httpRequest = HttpRequest.put(s);
                break;
            }
            case DELETE: {
                httpRequest = HttpRequest.delete(s);
                break;
            }
        }
        if (this.isHttps(s) && this.pinningInfo != null) {
            final SSLSocketFactory sslSocketFactory = this.getSSLSocketFactory();
            if (sslSocketFactory != null) {
                ((HttpsURLConnection)httpRequest.getConnection()).setSSLSocketFactory(sslSocketFactory);
            }
        }
        return httpRequest;
    }
    
    @Override
    public void setPinningInfoProvider(final PinningInfoProvider pinningInfo) {
        if (this.pinningInfo != pinningInfo) {
            this.pinningInfo = pinningInfo;
            this.resetSSLSocketFactory();
        }
    }
}
