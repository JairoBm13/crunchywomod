// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.network;

import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.util.LinkedList;
import java.security.cert.X509Certificate;

final class CertificateChainCleaner
{
    public static X509Certificate[] getCleanChain(final X509Certificate[] array, final SystemKeyStore systemKeyStore) throws CertificateException {
        final LinkedList<X509Certificate> list = new LinkedList<X509Certificate>();
        int n = 0;
        if (systemKeyStore.isTrustRoot(array[0])) {
            n = 1;
        }
        list.add(array[0]);
        int n2 = 1;
        int n3;
        while (true) {
            n3 = n;
            if (n2 >= array.length) {
                break;
            }
            if (systemKeyStore.isTrustRoot(array[n2])) {
                n = 1;
            }
            n3 = n;
            if (!isValidLink(array[n2], array[n2 - 1])) {
                break;
            }
            list.add(array[n2]);
            ++n2;
        }
        final X509Certificate trustRoot = systemKeyStore.getTrustRootFor(array[n2 - 1]);
        if (trustRoot != null) {
            list.add(trustRoot);
            n3 = 1;
        }
        if (n3 != 0) {
            return list.toArray(new X509Certificate[list.size()]);
        }
        throw new CertificateException("Didn't find a trust anchor in chain cleanup!");
    }
    
    private static boolean isValidLink(final X509Certificate x509Certificate, final X509Certificate x509Certificate2) {
        if (!x509Certificate.getSubjectX500Principal().equals(x509Certificate2.getIssuerX500Principal())) {
            return false;
        }
        try {
            x509Certificate2.verify(x509Certificate.getPublicKey());
            return true;
        }
        catch (GeneralSecurityException ex) {
            return false;
        }
    }
}
