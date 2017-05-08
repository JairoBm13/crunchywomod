// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.extractor;

import java.io.IOException;
import java.io.EOFException;
import com.google.android.exoplayer.SampleHolder;
import java.nio.ByteBuffer;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.upstream.Allocation;
import java.util.concurrent.LinkedBlockingDeque;
import com.google.android.exoplayer.upstream.Allocator;

final class RollingSampleBuffer
{
    private final int allocationLength;
    private final Allocator allocator;
    private final LinkedBlockingDeque<Allocation> dataQueue;
    private final SampleExtrasHolder extrasHolder;
    private final InfoQueue infoQueue;
    private Allocation lastAllocation;
    private int lastAllocationOffset;
    private final ParsableByteArray scratch;
    private long totalBytesDropped;
    private long totalBytesWritten;
    
    public RollingSampleBuffer(final Allocator allocator) {
        this.allocator = allocator;
        this.allocationLength = allocator.getIndividualAllocationLength();
        this.infoQueue = new InfoQueue();
        this.dataQueue = new LinkedBlockingDeque<Allocation>();
        this.extrasHolder = new SampleExtrasHolder();
        this.scratch = new ParsableByteArray(32);
        this.lastAllocationOffset = this.allocationLength;
    }
    
    private void dropDownstreamTo(final long n) {
        for (int n2 = (int)(n - this.totalBytesDropped) / this.allocationLength, i = 0; i < n2; ++i) {
            this.allocator.release(this.dataQueue.remove());
            this.totalBytesDropped += this.allocationLength;
        }
    }
    
    private static void ensureCapacity(final ParsableByteArray parsableByteArray, final int n) {
        if (parsableByteArray.limit() < n) {
            parsableByteArray.reset(new byte[n], n);
        }
    }
    
    private int prepareForAppend(final int n) {
        if (this.lastAllocationOffset == this.allocationLength) {
            this.lastAllocationOffset = 0;
            this.lastAllocation = this.allocator.allocate();
            this.dataQueue.add(this.lastAllocation);
        }
        return Math.min(n, this.allocationLength - this.lastAllocationOffset);
    }
    
    private void readData(long n, final ByteBuffer byteBuffer, int i) {
        while (i > 0) {
            this.dropDownstreamTo(n);
            final int n2 = (int)(n - this.totalBytesDropped);
            final int min = Math.min(i, this.allocationLength - n2);
            final Allocation allocation = this.dataQueue.peek();
            byteBuffer.put(allocation.data, allocation.translateOffset(n2), min);
            n += min;
            i -= min;
        }
    }
    
    private void readData(long n, final byte[] array, final int n2) {
        int min;
        for (int i = 0; i < n2; i += min) {
            this.dropDownstreamTo(n);
            final int n3 = (int)(n - this.totalBytesDropped);
            min = Math.min(n2 - i, this.allocationLength - n3);
            final Allocation allocation = this.dataQueue.peek();
            System.arraycopy(allocation.data, allocation.translateOffset(n3), array, i, min);
            n += min;
        }
    }
    
    private void readEncryptionData(final SampleHolder sampleHolder, final SampleExtrasHolder sampleExtrasHolder) {
        final long offset = sampleExtrasHolder.offset;
        this.readData(offset, this.scratch.data, 1);
        final long n = offset + 1L;
        final byte b = this.scratch.data[0];
        boolean b2;
        if ((b & 0x80) != 0x0) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        final byte b3 = (byte)(b & 0x7F);
        if (sampleHolder.cryptoInfo.iv == null) {
            sampleHolder.cryptoInfo.iv = new byte[16];
        }
        this.readData(n, sampleHolder.cryptoInfo.iv, b3);
        long n2 = n + b3;
        int unsignedShort;
        if (b2) {
            this.readData(n2, this.scratch.data, 2);
            n2 += 2L;
            this.scratch.setPosition(0);
            unsignedShort = this.scratch.readUnsignedShort();
        }
        else {
            unsignedShort = 1;
        }
        final int[] numBytesOfClearData = sampleHolder.cryptoInfo.numBytesOfClearData;
        int[] array = null;
        Label_0174: {
            if (numBytesOfClearData != null) {
                array = numBytesOfClearData;
                if (numBytesOfClearData.length >= unsignedShort) {
                    break Label_0174;
                }
            }
            array = new int[unsignedShort];
        }
        final int[] numBytesOfEncryptedData = sampleHolder.cryptoInfo.numBytesOfEncryptedData;
        int[] array2 = null;
        Label_0206: {
            if (numBytesOfEncryptedData != null) {
                array2 = numBytesOfEncryptedData;
                if (numBytesOfEncryptedData.length >= unsignedShort) {
                    break Label_0206;
                }
            }
            array2 = new int[unsignedShort];
        }
        if (b2) {
            final int n3 = unsignedShort * 6;
            ensureCapacity(this.scratch, n3);
            this.readData(n2, this.scratch.data, n3);
            final long n4 = n2 + n3;
            this.scratch.setPosition(0);
            int n5 = 0;
            while (true) {
                n2 = n4;
                if (n5 >= unsignedShort) {
                    break;
                }
                array[n5] = this.scratch.readUnsignedShort();
                array2[n5] = this.scratch.readUnsignedIntToInt();
                ++n5;
            }
        }
        else {
            array2[array[0] = 0] = sampleHolder.size - (int)(n2 - sampleExtrasHolder.offset);
        }
        sampleHolder.cryptoInfo.set(unsignedShort, array, array2, sampleExtrasHolder.encryptionKeyId, sampleHolder.cryptoInfo.iv, 1);
        final int n6 = (int)(n2 - sampleExtrasHolder.offset);
        sampleExtrasHolder.offset += n6;
        sampleHolder.size -= n6;
    }
    
    public int appendData(final ExtractorInput extractorInput, int n, final boolean b) throws IOException, InterruptedException {
        n = this.prepareForAppend(n);
        n = extractorInput.read(this.lastAllocation.data, this.lastAllocation.translateOffset(this.lastAllocationOffset), n);
        if (n != -1) {
            this.lastAllocationOffset += n;
            this.totalBytesWritten += n;
            return n;
        }
        if (b) {
            return -1;
        }
        throw new EOFException();
    }
    
    public void appendData(final ParsableByteArray parsableByteArray, int i) {
        while (i > 0) {
            final int prepareForAppend = this.prepareForAppend(i);
            parsableByteArray.readBytes(this.lastAllocation.data, this.lastAllocation.translateOffset(this.lastAllocationOffset), prepareForAppend);
            this.lastAllocationOffset += prepareForAppend;
            this.totalBytesWritten += prepareForAppend;
            i -= prepareForAppend;
        }
    }
    
    public void clear() {
        this.infoQueue.clear();
        while (!this.dataQueue.isEmpty()) {
            this.allocator.release(this.dataQueue.remove());
        }
        this.totalBytesDropped = 0L;
        this.totalBytesWritten = 0L;
        this.lastAllocation = null;
        this.lastAllocationOffset = this.allocationLength;
    }
    
    public void commitSample(final long n, final int n2, final long n3, final int n4, final byte[] array) {
        this.infoQueue.commitSample(n, n2, n3, n4, array);
    }
    
    public long getWritePosition() {
        return this.totalBytesWritten;
    }
    
    public boolean peekSample(final SampleHolder sampleHolder) {
        return this.infoQueue.peekSample(sampleHolder, this.extrasHolder);
    }
    
    public boolean readSample(final SampleHolder sampleHolder) {
        if (!this.infoQueue.peekSample(sampleHolder, this.extrasHolder)) {
            return false;
        }
        if (sampleHolder.isEncrypted()) {
            this.readEncryptionData(sampleHolder, this.extrasHolder);
        }
        sampleHolder.ensureSpaceForWrite(sampleHolder.size);
        this.readData(this.extrasHolder.offset, sampleHolder.data, sampleHolder.size);
        this.dropDownstreamTo(this.infoQueue.moveToNextSample());
        return true;
    }
    
    public void skipSample() {
        this.dropDownstreamTo(this.infoQueue.moveToNextSample());
    }
    
    private static final class InfoQueue
    {
        private int absoluteReadIndex;
        private int capacity;
        private byte[][] encryptionKeys;
        private int[] flags;
        private long[] offsets;
        private int queueSize;
        private int relativeReadIndex;
        private int relativeWriteIndex;
        private int[] sizes;
        private long[] timesUs;
        
        public InfoQueue() {
            this.capacity = 1000;
            this.offsets = new long[this.capacity];
            this.timesUs = new long[this.capacity];
            this.flags = new int[this.capacity];
            this.sizes = new int[this.capacity];
            this.encryptionKeys = new byte[this.capacity][];
        }
        
        public void clear() {
            this.absoluteReadIndex = 0;
            this.relativeReadIndex = 0;
            this.relativeWriteIndex = 0;
            this.queueSize = 0;
        }
        
        public void commitSample(final long n, int capacity, final long n2, int n3, final byte[] array) {
            synchronized (this) {
                this.timesUs[this.relativeWriteIndex] = n;
                this.offsets[this.relativeWriteIndex] = n2;
                this.sizes[this.relativeWriteIndex] = n3;
                this.flags[this.relativeWriteIndex] = capacity;
                this.encryptionKeys[this.relativeWriteIndex] = array;
                ++this.queueSize;
                if (this.queueSize == this.capacity) {
                    capacity = this.capacity + 1000;
                    final long[] offsets = new long[capacity];
                    final long[] timesUs = new long[capacity];
                    final int[] flags = new int[capacity];
                    final int[] sizes = new int[capacity];
                    final byte[][] encryptionKeys = new byte[capacity][];
                    n3 = this.capacity - this.relativeReadIndex;
                    System.arraycopy(this.offsets, this.relativeReadIndex, offsets, 0, n3);
                    System.arraycopy(this.timesUs, this.relativeReadIndex, timesUs, 0, n3);
                    System.arraycopy(this.flags, this.relativeReadIndex, flags, 0, n3);
                    System.arraycopy(this.sizes, this.relativeReadIndex, sizes, 0, n3);
                    System.arraycopy(this.encryptionKeys, this.relativeReadIndex, encryptionKeys, 0, n3);
                    final int relativeReadIndex = this.relativeReadIndex;
                    System.arraycopy(this.offsets, 0, offsets, n3, relativeReadIndex);
                    System.arraycopy(this.timesUs, 0, timesUs, n3, relativeReadIndex);
                    System.arraycopy(this.flags, 0, flags, n3, relativeReadIndex);
                    System.arraycopy(this.sizes, 0, sizes, n3, relativeReadIndex);
                    System.arraycopy(this.encryptionKeys, 0, encryptionKeys, n3, relativeReadIndex);
                    this.offsets = offsets;
                    this.timesUs = timesUs;
                    this.flags = flags;
                    this.sizes = sizes;
                    this.encryptionKeys = encryptionKeys;
                    this.relativeReadIndex = 0;
                    this.relativeWriteIndex = this.capacity;
                    this.queueSize = this.capacity;
                    this.capacity = capacity;
                }
                else {
                    ++this.relativeWriteIndex;
                    if (this.relativeWriteIndex == this.capacity) {
                        this.relativeWriteIndex = 0;
                    }
                }
            }
        }
        
        public long moveToNextSample() {
            synchronized (this) {
                --this.queueSize;
                final int n = this.relativeReadIndex++;
                ++this.absoluteReadIndex;
                if (this.relativeReadIndex == this.capacity) {
                    this.relativeReadIndex = 0;
                }
                long n2;
                if (this.queueSize > 0) {
                    n2 = this.offsets[this.relativeReadIndex];
                }
                else {
                    n2 = this.sizes[n] + this.offsets[n];
                }
                return n2;
            }
        }
        
        public boolean peekSample(final SampleHolder sampleHolder, final SampleExtrasHolder sampleExtrasHolder) {
            synchronized (this) {
                boolean b;
                if (this.queueSize == 0) {
                    b = false;
                }
                else {
                    sampleHolder.timeUs = this.timesUs[this.relativeReadIndex];
                    sampleHolder.size = this.sizes[this.relativeReadIndex];
                    sampleHolder.flags = this.flags[this.relativeReadIndex];
                    sampleExtrasHolder.offset = this.offsets[this.relativeReadIndex];
                    sampleExtrasHolder.encryptionKeyId = this.encryptionKeys[this.relativeReadIndex];
                    b = true;
                }
                return b;
            }
        }
    }
    
    private static final class SampleExtrasHolder
    {
        public byte[] encryptionKeyId;
        public long offset;
    }
}
