// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

final class MessageDigestHashFunction extends AbstractStreamingHashFunction
{
    private final String algorithmName;
    private final int bits;
    
    MessageDigestHashFunction(final String algorithmName) {
        this.algorithmName = algorithmName;
        this.bits = getMessageDigest(algorithmName).getDigestLength() * 8;
    }
    
    private static MessageDigest getMessageDigest(final String s) {
        try {
            return MessageDigest.getInstance(s);
        }
        catch (NoSuchAlgorithmException ex) {
            throw new AssertionError((Object)ex);
        }
    }
    
    @Override
    public int bits() {
        return this.bits;
    }
    
    @Override
    public Hasher newHasher() {
        return new MessageDigestHasher(getMessageDigest(this.algorithmName));
    }
    
    private static class MessageDigestHasher implements Hasher
    {
        private final MessageDigest digest;
        private boolean done;
        private final ByteBuffer scratch;
        
        private MessageDigestHasher(final MessageDigest digest) {
            this.digest = digest;
            this.scratch = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        }
        
        private void checkNotDone() {
            Preconditions.checkState(!this.done, (Object)"Cannot use Hasher after calling #hash() on it");
        }
        
        @Override
        public HashCode hash() {
            this.done = true;
            return HashCodes.fromBytesNoCopy(this.digest.digest());
        }
        
        @Override
        public Hasher putChar(final char c) {
            this.checkNotDone();
            this.scratch.putChar(c);
            this.digest.update(this.scratch.array(), 0, 2);
            this.scratch.clear();
            return this;
        }
        
        @Override
        public Hasher putInt(final int n) {
            this.checkNotDone();
            this.scratch.putInt(n);
            this.digest.update(this.scratch.array(), 0, 4);
            this.scratch.clear();
            return this;
        }
        
        @Override
        public Hasher putString(final CharSequence charSequence) {
            for (int i = 0; i < charSequence.length(); ++i) {
                this.putChar(charSequence.charAt(i));
            }
            return this;
        }
    }
}
