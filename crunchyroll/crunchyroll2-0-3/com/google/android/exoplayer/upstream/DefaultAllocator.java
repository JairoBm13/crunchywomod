// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.upstream;

import com.google.android.exoplayer.util.Util;
import java.util.Arrays;
import com.google.android.exoplayer.util.Assertions;

public final class DefaultAllocator implements Allocator
{
    private int allocatedCount;
    private Allocation[] availableAllocations;
    private int availableCount;
    private final int individualAllocationSize;
    private final byte[] initialAllocationBlock;
    
    public DefaultAllocator(final int n) {
        this(n, 0);
    }
    
    public DefaultAllocator(final int individualAllocationSize, final int availableCount) {
        final boolean b = true;
        Assertions.checkArgument(individualAllocationSize > 0);
        Assertions.checkArgument(availableCount >= 0 && b);
        this.individualAllocationSize = individualAllocationSize;
        this.availableCount = availableCount;
        this.availableAllocations = new Allocation[availableCount + 100];
        if (availableCount > 0) {
            this.initialAllocationBlock = new byte[availableCount * individualAllocationSize];
            for (int i = 0; i < availableCount; ++i) {
                this.availableAllocations[i] = new Allocation(this.initialAllocationBlock, i * individualAllocationSize);
            }
        }
        else {
            this.initialAllocationBlock = null;
        }
    }
    
    @Override
    public Allocation allocate() {
        synchronized (this) {
            ++this.allocatedCount;
            Allocation allocation;
            if (this.availableCount > 0) {
                final Allocation[] availableAllocations = this.availableAllocations;
                final int availableCount = this.availableCount - 1;
                this.availableCount = availableCount;
                allocation = availableAllocations[availableCount];
                this.availableAllocations[this.availableCount] = null;
            }
            else {
                allocation = new Allocation(new byte[this.individualAllocationSize], 0);
            }
            return allocation;
        }
    }
    
    @Override
    public int getIndividualAllocationLength() {
        return this.individualAllocationSize;
    }
    
    @Override
    public int getTotalBytesAllocated() {
        synchronized (this) {
            return this.allocatedCount * this.individualAllocationSize;
        }
    }
    
    @Override
    public void release(final Allocation allocation) {
        while (true) {
            while (true) {
                synchronized (this) {
                    if (allocation.data != this.initialAllocationBlock) {
                        if (allocation.data.length != this.individualAllocationSize) {
                            final boolean b = false;
                            Assertions.checkArgument(b);
                            --this.allocatedCount;
                            if (this.availableCount == this.availableAllocations.length) {
                                this.availableAllocations = Arrays.copyOf(this.availableAllocations, this.availableAllocations.length * 2);
                            }
                            this.availableAllocations[this.availableCount++] = allocation;
                            this.notifyAll();
                            return;
                        }
                    }
                }
                final boolean b = true;
                continue;
            }
        }
    }
    
    @Override
    public void trim(int availableCount) {
        while (true) {
            while (true) {
                Label_0193: {
                    Label_0172: {
                        final int max;
                        int n;
                        synchronized (this) {
                            max = Math.max(0, Util.ceilDivide(availableCount, this.individualAllocationSize) - this.allocatedCount);
                            availableCount = this.availableCount;
                            if (max >= availableCount) {
                                return;
                            }
                            availableCount = max;
                            if (this.initialAllocationBlock == null) {
                                break Label_0172;
                            }
                            availableCount = this.availableCount - 1;
                            n = 0;
                            if (n <= availableCount) {
                                final Allocation allocation = this.availableAllocations[n];
                                if (allocation.data == this.initialAllocationBlock) {
                                    ++n;
                                    break Label_0193;
                                }
                                final Allocation allocation2 = this.availableAllocations[n];
                                if (allocation2.data != this.initialAllocationBlock) {
                                    --availableCount;
                                    break Label_0193;
                                }
                                final Allocation[] availableAllocations = this.availableAllocations;
                                final int n2 = n + 1;
                                availableAllocations[n] = allocation2;
                                final Allocation[] availableAllocations2 = this.availableAllocations;
                                n = availableCount - 1;
                                availableAllocations2[availableCount] = allocation;
                                availableCount = n;
                                n = n2;
                                break Label_0193;
                            }
                        }
                        availableCount = Math.max(max, n);
                        if (availableCount >= this.availableCount) {
                            return;
                        }
                    }
                    Arrays.fill(this.availableAllocations, availableCount, this.availableCount, null);
                    this.availableCount = availableCount;
                    return;
                }
                continue;
            }
        }
    }
}
