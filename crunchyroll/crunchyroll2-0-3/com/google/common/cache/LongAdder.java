// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.cache;

import java.io.Serializable;

final class LongAdder extends Striped64 implements Serializable
{
    public void add(final long n) {
        final Cell[] cells = this.cells;
        if (cells == null) {
            final long base = this.base;
            if (this.casBase(base, base + n)) {
                return;
            }
        }
        final boolean b = true;
        final HashCode hashCode = LongAdder.threadHashCode.get();
        final int code = hashCode.code;
        boolean cas = b;
        if (cells != null) {
            final int length = cells.length;
            cas = b;
            if (length >= 1) {
                final Cell cell = cells[length - 1 & code];
                cas = b;
                if (cell != null) {
                    final long value = cell.value;
                    cas = cell.cas(value, value + n);
                    if (cas) {
                        return;
                    }
                }
            }
        }
        this.retryUpdate(n, hashCode, cas);
    }
    
    @Override
    public double doubleValue() {
        return this.sum();
    }
    
    @Override
    public float floatValue() {
        return this.sum();
    }
    
    @Override
    final long fn(final long n, final long n2) {
        return n + n2;
    }
    
    public void increment() {
        this.add(1L);
    }
    
    @Override
    public int intValue() {
        return (int)this.sum();
    }
    
    @Override
    public long longValue() {
        return this.sum();
    }
    
    public long sum() {
        long base = this.base;
        final Cell[] cells = this.cells;
        long n = base;
        if (cells != null) {
            final int length = cells.length;
            int n2 = 0;
            while (true) {
                n = base;
                if (n2 >= length) {
                    break;
                }
                final Cell cell = cells[n2];
                long n3 = base;
                if (cell != null) {
                    n3 = base + cell.value;
                }
                ++n2;
                base = n3;
            }
        }
        return n;
    }
    
    @Override
    public String toString() {
        return Long.toString(this.sum());
    }
}
