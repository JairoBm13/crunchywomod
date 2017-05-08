// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.cache.disc.naming;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import com.nostra13.universalimageloader.utils.L;
import java.security.MessageDigest;

public class Md5FileNameGenerator implements FileNameGenerator
{
    private static final String HASH_ALGORITHM = "MD5";
    private static final int RADIX = 36;
    
    private byte[] getMD5(byte[] digest) {
        try {
            final MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(digest);
            digest = instance.digest();
            return digest;
        }
        catch (NoSuchAlgorithmException ex) {
            L.e(ex);
            return null;
        }
    }
    
    @Override
    public String generate(final String s) {
        return new BigInteger(this.getMD5(s.getBytes())).abs().toString(36);
    }
}
