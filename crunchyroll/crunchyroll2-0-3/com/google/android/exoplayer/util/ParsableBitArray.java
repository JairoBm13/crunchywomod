// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.util;

public final class ParsableBitArray
{
    private int bitOffset;
    private int byteLimit;
    private int byteOffset;
    public byte[] data;
    
    public ParsableBitArray() {
    }
    
    public ParsableBitArray(final byte[] array) {
        this(array, array.length);
    }
    
    public ParsableBitArray(final byte[] data, final int byteLimit) {
        this.data = data;
        this.byteLimit = byteLimit;
    }
    
    private void assertValidOffset() {
        Assertions.checkState(this.byteOffset >= 0 && this.bitOffset >= 0 && this.bitOffset < 8 && (this.byteOffset < this.byteLimit || (this.byteOffset == this.byteLimit && this.bitOffset == 0)));
    }
    
    private int readExpGolombCodeNum() {
        int n = 0;
        while (!this.readBit()) {
            ++n;
        }
        int bits;
        if (n > 0) {
            bits = this.readBits(n);
        }
        else {
            bits = 0;
        }
        return bits + ((1 << n) - 1);
    }
    
    public int bitsLeft() {
        return (this.byteLimit - this.byteOffset) * 8 - this.bitOffset;
    }
    
    public int peekExpGolombCodedNumLength() {
        final int byteOffset = this.byteOffset;
        final int bitOffset = this.bitOffset;
        int n = 0;
        while (this.byteOffset < this.byteLimit && !this.readBit()) {
            ++n;
        }
        int n2;
        if (this.byteOffset == this.byteLimit) {
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        this.byteOffset = byteOffset;
        this.bitOffset = bitOffset;
        if (n2 != 0) {
            return -1;
        }
        return n * 2 + 1;
    }
    
    public boolean readBit() {
        return this.readBits(1) == 1;
    }
    
    public int readBits(int n) {
        if (n == 0) {
            return 0;
        }
        final int n2 = 0;
        final int n3 = n / 8;
        int i = 0;
        int n4 = n;
        n = n2;
        while (i < n3) {
            int n5;
            if (this.bitOffset != 0) {
                n5 = ((this.data[this.byteOffset] & 0xFF) << this.bitOffset | (this.data[this.byteOffset + 1] & 0xFF) >>> 8 - this.bitOffset);
            }
            else {
                n5 = this.data[this.byteOffset];
            }
            n4 -= 8;
            n |= (n5 & 0xFF) << n4;
            ++this.byteOffset;
            ++i;
        }
        int n6 = n;
        if (n4 > 0) {
            final int n7 = this.bitOffset + n4;
            final byte b = (byte)(255 >> 8 - n4);
            if (n7 > 8) {
                n |= (((this.data[this.byteOffset] & 0xFF) << n7 - 8 | (this.data[this.byteOffset + 1] & 0xFF) >> 16 - n7) & b);
                ++this.byteOffset;
            }
            else {
                final int n8 = n |= ((this.data[this.byteOffset] & 0xFF) >> 8 - n7 & b);
                if (n7 == 8) {
                    ++this.byteOffset;
                    n = n8;
                }
            }
            this.bitOffset = n7 % 8;
            n6 = n;
        }
        this.assertValidOffset();
        return n6;
    }
    
    public int readSignedExpGolombCodedInt() {
        final int expGolombCodeNum = this.readExpGolombCodeNum();
        int n;
        if (expGolombCodeNum % 2 == 0) {
            n = -1;
        }
        else {
            n = 1;
        }
        return n * ((expGolombCodeNum + 1) / 2);
    }
    
    public int readUnsignedExpGolombCodedInt() {
        return this.readExpGolombCodeNum();
    }
    
    public void reset(final byte[] data, final int byteLimit) {
        this.data = data;
        this.byteOffset = 0;
        this.bitOffset = 0;
        this.byteLimit = byteLimit;
    }
    
    public void setPosition(final int n) {
        this.byteOffset = n / 8;
        this.bitOffset = n - this.byteOffset * 8;
        this.assertValidOffset();
    }
    
    public void skipBits(final int n) {
        this.byteOffset += n / 8;
        this.bitOffset += n % 8;
        if (this.bitOffset > 7) {
            ++this.byteOffset;
            this.bitOffset -= 8;
        }
        this.assertValidOffset();
    }
}
