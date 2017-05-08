// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.cache;

import java.util.Random;
import java.security.PrivilegedActionException;
import java.security.AccessController;
import java.lang.reflect.Field;
import java.security.PrivilegedExceptionAction;
import sun.misc.Unsafe;

abstract class Striped64 extends Number
{
    static final int NCPU;
    private static final Unsafe UNSAFE;
    private static final long baseOffset;
    private static final long busyOffset;
    static final ThreadHashCode threadHashCode;
    transient volatile long base;
    transient volatile int busy;
    transient volatile Cell[] cells;
    
    static {
        threadHashCode = new ThreadHashCode();
        NCPU = Runtime.getRuntime().availableProcessors();
        try {
            UNSAFE = getUnsafe();
            baseOffset = Striped64.UNSAFE.objectFieldOffset(Striped64.class.getDeclaredField("base"));
            busyOffset = Striped64.UNSAFE.objectFieldOffset(Striped64.class.getDeclaredField("busy"));
        }
        catch (Exception ex) {
            throw new Error(ex);
        }
    }
    
    private static Unsafe getUnsafe() {
        try {
            return Unsafe.getUnsafe();
        }
        catch (SecurityException ex2) {
            try {
                return AccessController.doPrivileged((PrivilegedExceptionAction<Unsafe>)new PrivilegedExceptionAction<Unsafe>() {
                    @Override
                    public Unsafe run() throws Exception {
                        final Field declaredField = Unsafe.class.getDeclaredField("theUnsafe");
                        declaredField.setAccessible(true);
                        return (Unsafe)declaredField.get(null);
                    }
                });
            }
            catch (PrivilegedActionException ex) {
                throw new RuntimeException("Could not initialize intrinsics", ex.getCause());
            }
        }
    }
    
    final boolean casBase(final long n, final long n2) {
        return Striped64.UNSAFE.compareAndSwapLong(this, Striped64.baseOffset, n, n2);
    }
    
    final boolean casBusy() {
        return Striped64.UNSAFE.compareAndSwapInt(this, Striped64.busyOffset, 0, 1);
    }
    
    abstract long fn(final long p0, final long p1);
    
    final void retryUpdate(final long n, final HashCode hashCode, boolean b) {
        int code = hashCode.code;
        int n2 = 0;
    Label_0151_Outer:
        while (true) {
            final Cell[] cells = this.cells;
            while (true) {
                Label_0373: {
                    if (cells == null) {
                        break Label_0373;
                    }
                    final int length = cells.length;
                    if (length <= 0) {
                        break Label_0373;
                    }
                    Object cells2 = cells[length - 1 & code];
                    boolean b2 = false;
                    int i = 0;
                    Label_0173: {
                        Label_0166: {
                            final Cell cell;
                            if (cells2 == null) {
                                if (this.busy != 0) {
                                    break Label_0166;
                                }
                                cell = new Cell(n);
                                if (this.busy != 0 || !this.casBusy()) {
                                    break Label_0166;
                                }
                            }
                            else {
                                if (!b) {
                                    b2 = true;
                                    i = n2;
                                    break Label_0173;
                                }
                                final long value = ((Cell)cells2).value;
                                if (((Cell)cells2).cas(value, this.fn(value, n))) {
                                    break Label_0151;
                                }
                                if (length >= Striped64.NCPU || this.cells != cells) {
                                    i = 0;
                                    b2 = b;
                                    break Label_0173;
                                }
                                if (n2 == 0) {
                                    i = 1;
                                    b2 = b;
                                    break Label_0173;
                                }
                                i = n2;
                                b2 = b;
                                if (this.busy != 0) {
                                    break Label_0173;
                                }
                                i = n2;
                                b2 = b;
                                if (!this.casBusy()) {
                                    break Label_0173;
                                }
                            Label_0348_Outer:
                                while (true) {
                                    while (true) {
                                        Label_0484: {
                                            try {
                                                if (this.cells == cells) {
                                                    cells2 = new Cell[length << 1];
                                                    i = 0;
                                                    break Label_0484;
                                                }
                                                while (true) {
                                                    this.busy = 0;
                                                    n2 = 0;
                                                    continue Label_0151_Outer;
                                                    this.cells = (Cell[])cells2;
                                                    continue Label_0348_Outer;
                                                }
                                            }
                                            finally {
                                                this.busy = 0;
                                            }
                                            break Label_0373;
                                        }
                                        while (i < length) {
                                            cells2[i] = cells[i];
                                            ++i;
                                        }
                                        continue;
                                    }
                                }
                            }
                            final boolean b3 = false;
                            try {
                                final Cell[] cells3 = this.cells;
                                boolean b4 = b3;
                                if (cells3 != null) {
                                    final int length2 = cells3.length;
                                    b4 = b3;
                                    if (length2 > 0) {
                                        final int n3 = length2 - 1 & code;
                                        b4 = b3;
                                        if (cells3[n3] == null) {
                                            cells3[n3] = cell;
                                            b4 = true;
                                        }
                                    }
                                }
                                this.busy = 0;
                                if (b4) {
                                    hashCode.code = code;
                                    return;
                                }
                                continue Label_0151_Outer;
                            }
                            finally {
                                this.busy = 0;
                            }
                        }
                        i = 0;
                        b2 = b;
                    }
                    final int n4 = code ^ code << 13;
                    final int n5 = n4 ^ n4 >>> 17;
                    code = (n5 ^ n5 << 5);
                    n2 = i;
                    b = b2;
                    continue Label_0151_Outer;
                }
                if (this.busy == 0 && this.cells == cells && this.casBusy()) {
                    boolean b5 = false;
                    try {
                        if (this.cells == cells) {
                            final Cell[] cells4 = new Cell[2];
                            cells4[code & 0x1] = new Cell(n);
                            this.cells = cells4;
                            b5 = true;
                        }
                        this.busy = 0;
                        if (b5) {
                            continue;
                        }
                        continue Label_0151_Outer;
                    }
                    finally {
                        this.busy = 0;
                    }
                }
                final long base = this.base;
                if (this.casBase(base, this.fn(base, n))) {
                    continue;
                }
                break;
            }
        }
    }
    
    static final class Cell
    {
        private static final Unsafe UNSAFE;
        private static final long valueOffset;
        volatile long value;
        
        static {
            try {
                UNSAFE = getUnsafe();
                valueOffset = Cell.UNSAFE.objectFieldOffset(Cell.class.getDeclaredField("value"));
            }
            catch (Exception ex) {
                throw new Error(ex);
            }
        }
        
        Cell(final long value) {
            this.value = value;
        }
        
        final boolean cas(final long n, final long n2) {
            return Cell.UNSAFE.compareAndSwapLong(this, Cell.valueOffset, n, n2);
        }
    }
    
    static final class HashCode
    {
        static final Random rng;
        int code;
        
        static {
            rng = new Random();
        }
        
        HashCode() {
            int nextInt;
            if ((nextInt = HashCode.rng.nextInt()) == 0) {
                nextInt = 1;
            }
            this.code = nextInt;
        }
    }
    
    static final class ThreadHashCode extends ThreadLocal<HashCode>
    {
        public HashCode initialValue() {
            return new HashCode();
        }
    }
}
