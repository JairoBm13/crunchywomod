// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.hls;

import com.google.android.exoplayer.util.Assertions;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.io.InputStream;
import com.google.android.exoplayer.upstream.DataSourceInputStream;
import java.security.spec.AlgorithmParameterSpec;
import java.security.Key;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import com.google.android.exoplayer.upstream.DataSpec;
import java.io.IOException;
import javax.crypto.CipherInputStream;
import com.google.android.exoplayer.upstream.DataSource;

final class Aes128DataSource implements DataSource
{
    private CipherInputStream cipherInputStream;
    private final byte[] encryptionIv;
    private final byte[] encryptionKey;
    private final DataSource upstream;
    
    public Aes128DataSource(final DataSource upstream, final byte[] encryptionKey, final byte[] encryptionIv) {
        this.upstream = upstream;
        this.encryptionKey = encryptionKey;
        this.encryptionIv = encryptionIv;
    }
    
    @Override
    public void close() throws IOException {
        this.cipherInputStream = null;
        this.upstream.close();
    }
    
    @Override
    public long open(final DataSpec dataSpec) throws IOException {
        Cipher instance;
        SecretKeySpec secretKeySpec;
        IvParameterSpec ivParameterSpec;
        try {
            instance = Cipher.getInstance("AES/CBC/PKCS7Padding");
            secretKeySpec = new SecretKeySpec(this.encryptionKey, "AES");
            ivParameterSpec = new IvParameterSpec(this.encryptionIv);
            final Cipher cipher = instance;
            final int n = 2;
            final SecretKeySpec secretKeySpec2 = secretKeySpec;
            final IvParameterSpec ivParameterSpec2 = ivParameterSpec;
            cipher.init(n, secretKeySpec2, ivParameterSpec2);
            final Aes128DataSource aes128DataSource = this;
            final Aes128DataSource aes128DataSource2 = this;
            final DataSource dataSource = aes128DataSource2.upstream;
            final DataSpec dataSpec2 = dataSpec;
            final DataSourceInputStream dataSourceInputStream = new DataSourceInputStream(dataSource, dataSpec2);
            final Cipher cipher2 = instance;
            final CipherInputStream cipherInputStream = new CipherInputStream(dataSourceInputStream, cipher2);
            aes128DataSource.cipherInputStream = cipherInputStream;
            return -1L;
        }
        catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        catch (NoSuchPaddingException ex2) {
            throw new RuntimeException(ex2);
        }
        try {
            final Cipher cipher = instance;
            final int n = 2;
            final SecretKeySpec secretKeySpec2 = secretKeySpec;
            final IvParameterSpec ivParameterSpec2 = ivParameterSpec;
            cipher.init(n, secretKeySpec2, ivParameterSpec2);
            final Aes128DataSource aes128DataSource = this;
            final Aes128DataSource aes128DataSource2 = this;
            final DataSource dataSource = aes128DataSource2.upstream;
            final DataSpec dataSpec2 = dataSpec;
            final DataSourceInputStream dataSourceInputStream = new DataSourceInputStream(dataSource, dataSpec2);
            final Cipher cipher2 = instance;
            final CipherInputStream cipherInputStream = new CipherInputStream(dataSourceInputStream, cipher2);
            aes128DataSource.cipherInputStream = cipherInputStream;
            return -1L;
        }
        catch (InvalidKeyException ex3) {
            throw new RuntimeException(ex3);
        }
        catch (InvalidAlgorithmParameterException ex4) {
            throw new RuntimeException(ex4);
        }
    }
    
    @Override
    public int read(final byte[] array, int read, int n) throws IOException {
        Assertions.checkState(this.cipherInputStream != null);
        n = (read = this.cipherInputStream.read(array, read, n));
        if (n < 0) {
            read = -1;
        }
        return read;
    }
}
