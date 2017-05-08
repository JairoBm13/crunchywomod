// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.extractor.ts;

import com.google.android.exoplayer.util.Assertions;
import java.util.Arrays;

final class NalUnitTargetBuffer
{
    private boolean isCompleted;
    private boolean isFilling;
    public byte[] nalData;
    public int nalLength;
    private final int targetType;
    
    public NalUnitTargetBuffer(final int targetType, final int n) {
        this.targetType = targetType;
        (this.nalData = new byte[n + 3])[2] = 1;
    }
    
    public void appendToNalUnit(final byte[] array, final int n, int n2) {
        if (!this.isFilling) {
            return;
        }
        n2 -= n;
        if (this.nalData.length < this.nalLength + n2) {
            this.nalData = Arrays.copyOf(this.nalData, (this.nalLength + n2) * 2);
        }
        System.arraycopy(array, n, this.nalData, this.nalLength, n2);
        this.nalLength += n2;
    }
    
    public boolean endNalUnit(final int n) {
        if (!this.isFilling) {
            return false;
        }
        this.nalLength -= n;
        this.isFilling = false;
        return this.isCompleted = true;
    }
    
    public boolean isCompleted() {
        return this.isCompleted;
    }
    
    public void startNalUnit(final int n) {
        final boolean b = true;
        Assertions.checkState(!this.isFilling);
        this.isFilling = (n == this.targetType && b);
        if (this.isFilling) {
            this.nalLength = 3;
            this.isCompleted = false;
        }
    }
}
