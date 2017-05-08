// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.hash;

import java.nio.ByteOrder;
import com.google.common.base.Preconditions;
import java.nio.ByteBuffer;

abstract class AbstractStreamingHashFunction implements HashFunction
{
    protected abstract static class AbstractStreamingHasher extends AbstractHasher
    {
        private final ByteBuffer buffer;
        private final int bufferSize;
        private final int chunkSize;
        
        protected AbstractStreamingHasher(final int n) {
            this(n, n);
        }
        
        protected AbstractStreamingHasher(final int chunkSize, final int bufferSize) {
            Preconditions.checkArgument(bufferSize % chunkSize == 0);
            this.buffer = ByteBuffer.allocate(bufferSize + 7).order(ByteOrder.LITTLE_ENDIAN);
            this.bufferSize = bufferSize;
            this.chunkSize = chunkSize;
        }
        
        private void munch() {
            this.buffer.flip();
            while (this.buffer.remaining() >= this.chunkSize) {
                this.process(this.buffer);
            }
            this.buffer.compact();
        }
        
        private void munchIfFull() {
            if (this.buffer.remaining() < 8) {
                this.munch();
            }
        }
        
        @Override
        public final HashCode hash() {
            this.munch();
            this.buffer.flip();
            if (this.buffer.remaining() > 0) {
                this.processRemaining(this.buffer);
            }
            return this.makeHash();
        }
        
        abstract HashCode makeHash();
        
        protected abstract void process(final ByteBuffer p0);
        
        protected void processRemaining(final ByteBuffer byteBuffer) {
            byteBuffer.position(byteBuffer.limit());
            byteBuffer.limit(this.chunkSize + 7);
            while (byteBuffer.position() < this.chunkSize) {
                byteBuffer.putLong(0L);
            }
            byteBuffer.limit(this.chunkSize);
            byteBuffer.flip();
            this.process(byteBuffer);
        }
        
        @Override
        public final Hasher putChar(final char c) {
            this.buffer.putChar(c);
            this.munchIfFull();
            return this;
        }
        
        @Override
        public final Hasher putInt(final int n) {
            this.buffer.putInt(n);
            this.munchIfFull();
            return this;
        }
        
        @Override
        public final Hasher putString(final CharSequence charSequence) {
            for (int i = 0; i < charSequence.length(); ++i) {
                this.putChar(charSequence.charAt(i));
            }
            return this;
        }
    }
}
