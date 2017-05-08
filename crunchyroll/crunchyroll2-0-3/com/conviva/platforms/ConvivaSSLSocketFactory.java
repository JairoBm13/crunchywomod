// 
// Decompiled by Procyon v0.5.30
// 

package com.conviva.platforms;

import java.net.UnknownHostException;
import java.io.IOException;
import java.net.Socket;
import java.security.UnrecoverableKeyException;
import java.security.KeyStoreException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.TrustManager;
import java.security.KeyStore;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.SSLSocketFactory;

public class ConvivaSSLSocketFactory extends SSLSocketFactory
{
    private SSLContext sslContext;
    
    public ConvivaSSLSocketFactory(final KeyStore keyStore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(keyStore);
        (this.sslContext = SSLContext.getInstance("TLS")).init(null, new TrustManager[] { new X509TrustManager() {
                @Override
                public void checkClientTrusted(final X509Certificate[] array, final String s) throws CertificateException {
                }
                
                @Override
                public void checkServerTrusted(final X509Certificate[] array, final String s) throws CertificateException {
                }
                
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            } }, null);
    }
    
    public ConvivaSSLSocketFactory(final SSLContext sslContext) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super((KeyStore)null);
        this.sslContext = SSLContext.getInstance("TLS");
        this.sslContext = sslContext;
    }
    
    public Socket createSocket() throws IOException {
        return this.sslContext.getSocketFactory().createSocket();
    }
    
    public Socket createSocket(final Socket socket, final String s, final int n, final boolean b) throws IOException, UnknownHostException {
        return this.sslContext.getSocketFactory().createSocket(socket, s, n, b);
    }
}
