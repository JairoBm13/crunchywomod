// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.util;

import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import java.lang.reflect.Array;
import java.util.HashSet;

public final class ArrayBuilders
{
    private BooleanBuilder _booleanBuilder;
    private ByteBuilder _byteBuilder;
    private DoubleBuilder _doubleBuilder;
    private FloatBuilder _floatBuilder;
    private IntBuilder _intBuilder;
    private LongBuilder _longBuilder;
    private ShortBuilder _shortBuilder;
    
    public ArrayBuilders() {
        this._booleanBuilder = null;
        this._byteBuilder = null;
        this._shortBuilder = null;
        this._intBuilder = null;
        this._longBuilder = null;
        this._floatBuilder = null;
        this._doubleBuilder = null;
    }
    
    public static <T> Iterable<T> arrayAsIterable(final T[] array) {
        return new ArrayIterator<T>(array);
    }
    
    public static <T> HashSet<T> arrayToSet(final T[] array) {
        final HashSet<T> set = new HashSet<T>();
        if (array != null) {
            for (int length = array.length, i = 0; i < length; ++i) {
                set.add(array[i]);
            }
        }
        return set;
    }
    
    public static Object getArrayComparator(final Object o) {
        return new Object() {
            final /* synthetic */ Class val$defaultValueType = o.getClass();
            final /* synthetic */ int val$length = Array.getLength(o);
            
            @Override
            public boolean equals(final Object o) {
                final boolean b = false;
                boolean b2;
                if (o == this) {
                    b2 = true;
                }
                else {
                    b2 = b;
                    if (o != null) {
                        b2 = b;
                        if (o.getClass() == this.val$defaultValueType) {
                            b2 = b;
                            if (Array.getLength(o) == this.val$length) {
                                for (int i = 0; i < this.val$length; ++i) {
                                    final Object value = Array.get(o, i);
                                    final Object value2 = Array.get(o, i);
                                    if (value != value2 && value != null && !value.equals(value2)) {
                                        return false;
                                    }
                                }
                                return true;
                            }
                        }
                    }
                }
                return b2;
            }
        };
    }
    
    public static <T> HashSet<T> setAndArray(final Set<T> set, final T[] array) {
        final HashSet<T> set2 = new HashSet<T>();
        if (set != null) {
            set2.addAll((Collection<?>)set);
        }
        if (array != null) {
            for (int length = array.length, i = 0; i < length; ++i) {
                set2.add(array[i]);
            }
        }
        return set2;
    }
    
    public BooleanBuilder getBooleanBuilder() {
        if (this._booleanBuilder == null) {
            this._booleanBuilder = new BooleanBuilder();
        }
        return this._booleanBuilder;
    }
    
    public ByteBuilder getByteBuilder() {
        if (this._byteBuilder == null) {
            this._byteBuilder = new ByteBuilder();
        }
        return this._byteBuilder;
    }
    
    public DoubleBuilder getDoubleBuilder() {
        if (this._doubleBuilder == null) {
            this._doubleBuilder = new DoubleBuilder();
        }
        return this._doubleBuilder;
    }
    
    public FloatBuilder getFloatBuilder() {
        if (this._floatBuilder == null) {
            this._floatBuilder = new FloatBuilder();
        }
        return this._floatBuilder;
    }
    
    public IntBuilder getIntBuilder() {
        if (this._intBuilder == null) {
            this._intBuilder = new IntBuilder();
        }
        return this._intBuilder;
    }
    
    public LongBuilder getLongBuilder() {
        if (this._longBuilder == null) {
            this._longBuilder = new LongBuilder();
        }
        return this._longBuilder;
    }
    
    public ShortBuilder getShortBuilder() {
        if (this._shortBuilder == null) {
            this._shortBuilder = new ShortBuilder();
        }
        return this._shortBuilder;
    }
    
    private static final class ArrayIterator<T> implements Iterable<T>, Iterator<T>
    {
        private final T[] _array;
        private int _index;
        
        public ArrayIterator(final T[] array) {
            this._array = array;
            this._index = 0;
        }
        
        @Override
        public boolean hasNext() {
            return this._index < this._array.length;
        }
        
        @Override
        public Iterator<T> iterator() {
            return this;
        }
        
        @Override
        public T next() {
            if (this._index >= this._array.length) {
                throw new NoSuchElementException();
            }
            return (T)this._array[this._index++];
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    public static final class BooleanBuilder extends PrimitiveArrayBuilder<boolean[]>
    {
        public final boolean[] _constructArray(final int n) {
            return new boolean[n];
        }
    }
    
    public static final class ByteBuilder extends PrimitiveArrayBuilder<byte[]>
    {
        public final byte[] _constructArray(final int n) {
            return new byte[n];
        }
    }
    
    public static final class DoubleBuilder extends PrimitiveArrayBuilder<double[]>
    {
        public final double[] _constructArray(final int n) {
            return new double[n];
        }
    }
    
    public static final class FloatBuilder extends PrimitiveArrayBuilder<float[]>
    {
        public final float[] _constructArray(final int n) {
            return new float[n];
        }
    }
    
    public static final class IntBuilder extends PrimitiveArrayBuilder<int[]>
    {
        public final int[] _constructArray(final int n) {
            return new int[n];
        }
    }
    
    public static final class LongBuilder extends PrimitiveArrayBuilder<long[]>
    {
        public final long[] _constructArray(final int n) {
            return new long[n];
        }
    }
    
    public static final class ShortBuilder extends PrimitiveArrayBuilder<short[]>
    {
        public final short[] _constructArray(final int n) {
            return new short[n];
        }
    }
}
