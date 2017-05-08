// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.util;

public final class ParsableByteArray
{
    public byte[] data;
    private int limit;
    private int position;
    
    public ParsableByteArray() {
    }
    
    public ParsableByteArray(final int n) {
        this.data = new byte[n];
        this.limit = this.data.length;
    }
    
    public ParsableByteArray(final byte[] data) {
        this.data = data;
        this.limit = data.length;
    }
    
    public ParsableByteArray(final byte[] data, final int limit) {
        this.data = data;
        this.limit = limit;
    }
    
    public int bytesLeft() {
        return this.limit - this.position;
    }
    
    public int getPosition() {
        return this.position;
    }
    
    public int limit() {
        return this.limit;
    }
    
    public void readBytes(final ParsableBitArray parsableBitArray, final int n) {
        this.readBytes(parsableBitArray.data, 0, n);
        parsableBitArray.setPosition(0);
    }
    
    public void readBytes(final byte[] array, final int n, final int n2) {
        System.arraycopy(this.data, this.position, array, n, n2);
        this.position += n2;
    }
    
    public int readInt() {
        return (this.data[this.position++] & 0xFF) << 24 | (this.data[this.position++] & 0xFF) << 16 | (this.data[this.position++] & 0xFF) << 8 | (this.data[this.position++] & 0xFF);
    }
    
    public int readUnsignedByte() {
        return this.data[this.position++] & 0xFF;
    }
    
    public int readUnsignedInt24() {
        return (this.data[this.position++] & 0xFF) << 16 | (this.data[this.position++] & 0xFF) << 8 | (this.data[this.position++] & 0xFF);
    }
    
    public int readUnsignedIntToInt() {
        final int int1 = this.readInt();
        if (int1 < 0) {
            throw new IllegalStateException("Top bit not zero: " + int1);
        }
        return int1;
    }
    
    public int readUnsignedShort() {
        return (this.data[this.position++] & 0xFF) << 8 | (this.data[this.position++] & 0xFF);
    }
    
    public void reset(final byte[] data, final int limit) {
        this.data = data;
        this.limit = limit;
        this.position = 0;
    }
    
    public void setLimit(final int limit) {
        Assertions.checkArgument(limit >= 0 && limit <= this.data.length);
        this.limit = limit;
    }
    
    public void setPosition(final int position) {
        Assertions.checkArgument(position >= 0 && position <= this.limit);
        this.position = position;
    }
    
    public void skipBytes(final int n) {
        this.setPosition(this.position + n);
    }
}
