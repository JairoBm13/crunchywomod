// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.network;

import java.security.GeneralSecurityException;
import java.util.Enumeration;
import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.NoSuchAlgorithmException;
import java.security.KeyStoreException;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.security.Principal;
import java.util.HashMap;

class SystemKeyStore
{
    private final HashMap<Principal, X509Certificate> trustRoots;
    final KeyStore trustStore;
    
    public SystemKeyStore(final InputStream inputStream, final String s) {
        final KeyStore trustStore = this.getTrustStore(inputStream, s);
        this.trustRoots = this.initializeTrustedRoots(trustStore);
        this.trustStore = trustStore;
    }
    
    private KeyStore getTrustStore(InputStream inputStream, final String s) {
        try {
            final KeyStore instance = KeyStore.getInstance("BKS");
            inputStream = new BufferedInputStream(inputStream);
            try {
                instance.load(inputStream, s.toCharArray());
                return instance;
            }
            finally {
                ((BufferedInputStream)inputStream).close();
            }
        }
        catch (KeyStoreException ex) {
            throw new AssertionError((Object)ex);
        }
        catch (NoSuchAlgorithmException ex2) {
            throw new AssertionError((Object)ex2);
        }
        catch (CertificateException ex3) {
            throw new AssertionError((Object)ex3);
        }
        catch (IOException ex4) {
            throw new AssertionError((Object)ex4);
        }
    }
    
    private HashMap<Principal, X509Certificate> initializeTrustedRoots(final KeyStore keyStore) {
        HashMap<X500Principal, X509Certificate> hashMap;
        try {
            hashMap = (HashMap<X500Principal, X509Certificate>)new HashMap<Principal, X509Certificate>();
            final Enumeration<String> aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                final X509Certificate x509Certificate = (X509Certificate)keyStore.getCertificate(aliases.nextElement());
                if (x509Certificate != null) {
                    hashMap.put(x509Certificate.getSubjectX500Principal(), x509Certificate);
                }
            }
        }
        catch (KeyStoreException ex) {
            throw new AssertionError((Object)ex);
        }
        return (HashMap<Principal, X509Certificate>)hashMap;
    }
    
    public X509Certificate getTrustRootFor(final X509Certificate x509Certificate) {
        final X509Certificate x509Certificate2 = this.trustRoots.get(x509Certificate.getIssuerX500Principal());
        if (x509Certificate2 == null) {
            return null;
        }
        if (x509Certificate2.getSubjectX500Principal().equals(x509Certificate.getSubjectX500Principal())) {
            return null;
        }
        try {
            x509Certificate.verify(x509Certificate2.getPublicKey());
            return x509Certificate2;
        }
        catch (GeneralSecurityException ex) {
            return null;
        }
    }
    
    public boolean isTrustRoot(final X509Certificate x509Certificate) {
        final X509Certificate x509Certificate2 = this.trustRoots.get(x509Certificate.getSubjectX500Principal());
        return x509Certificate2 != null && x509Certificate2.getPublicKey().equals(x509Certificate.getPublicKey());
    }
}
