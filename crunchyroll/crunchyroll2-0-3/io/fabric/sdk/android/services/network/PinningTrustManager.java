// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.network;

import java.util.Iterator;
import java.util.Arrays;
import java.security.MessageDigest;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.TrustManagerFactory;
import io.fabric.sdk.android.Fabric;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import javax.net.ssl.TrustManager;
import java.util.List;
import java.security.cert.X509Certificate;
import java.util.Set;
import javax.net.ssl.X509TrustManager;

class PinningTrustManager implements X509TrustManager
{
    private final Set<X509Certificate> cache;
    private final long pinCreationTimeMillis;
    private final List<byte[]> pins;
    private final SystemKeyStore systemKeyStore;
    private final TrustManager[] systemTrustManagers;
    
    public PinningTrustManager(final SystemKeyStore systemKeyStore, final PinningInfoProvider pinningInfoProvider) {
        this.pins = new LinkedList<byte[]>();
        this.cache = Collections.synchronizedSet(new HashSet<X509Certificate>());
        this.systemTrustManagers = this.initializeSystemTrustManagers(systemKeyStore);
        this.systemKeyStore = systemKeyStore;
        this.pinCreationTimeMillis = pinningInfoProvider.getPinCreationTimeInMillis();
        final String[] pins = pinningInfoProvider.getPins();
        for (int length = pins.length, i = 0; i < length; ++i) {
            this.pins.add(this.hexStringToByteArray(pins[i]));
        }
    }
    
    private void checkPinTrust(X509Certificate[] cleanChain) throws CertificateException {
        if (this.pinCreationTimeMillis == -1L || System.currentTimeMillis() - this.pinCreationTimeMillis <= 15552000000L) {
            cleanChain = CertificateChainCleaner.getCleanChain(cleanChain, this.systemKeyStore);
            for (int length = cleanChain.length, i = 0; i < length; ++i) {
                if (this.isValidPin(cleanChain[i])) {
                    return;
                }
            }
            throw new CertificateException("No valid pins found in chain!");
        }
        Fabric.getLogger().w("Fabric", "Certificate pins are stale, (" + (System.currentTimeMillis() - this.pinCreationTimeMillis) + " millis vs " + 15552000000L + " millis) " + "falling back to system trust.");
    }
    
    private void checkSystemTrust(final X509Certificate[] array, final String s) throws CertificateException {
        final TrustManager[] systemTrustManagers = this.systemTrustManagers;
        for (int length = systemTrustManagers.length, i = 0; i < length; ++i) {
            ((X509TrustManager)systemTrustManagers[i]).checkServerTrusted(array, s);
        }
    }
    
    private byte[] hexStringToByteArray(final String s) {
        final int length = s.length();
        final byte[] array = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            array[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return array;
    }
    
    private TrustManager[] initializeSystemTrustManagers(final SystemKeyStore systemKeyStore) {
        try {
            final TrustManagerFactory instance = TrustManagerFactory.getInstance("X509");
            instance.init(systemKeyStore.trustStore);
            return instance.getTrustManagers();
        }
        catch (NoSuchAlgorithmException ex) {
            throw new AssertionError((Object)ex);
        }
        catch (KeyStoreException ex2) {
            throw new AssertionError((Object)ex2);
        }
    }
    
    private boolean isValidPin(final X509Certificate x509Certificate) throws CertificateException {
        try {
            final byte[] digest = MessageDigest.getInstance("SHA1").digest(x509Certificate.getPublicKey().getEncoded());
            final Iterator<byte[]> iterator = this.pins.iterator();
            while (iterator.hasNext()) {
                if (Arrays.equals(iterator.next(), digest)) {
                    return true;
                }
            }
            return false;
        }
        catch (NoSuchAlgorithmException ex) {
            throw new CertificateException(ex);
        }
    }
    
    @Override
    public void checkClientTrusted(final X509Certificate[] array, final String s) throws CertificateException {
        throw new CertificateException("Client certificates not supported!");
    }
    
    @Override
    public void checkServerTrusted(final X509Certificate[] array, final String s) throws CertificateException {
        if (this.cache.contains(array[0])) {
            return;
        }
        this.checkSystemTrust(array, s);
        this.checkPinTrust(array);
        this.cache.add(array[0]);
    }
    
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
