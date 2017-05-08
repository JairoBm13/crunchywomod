// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.JsonDeserializer;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.util.NameTransformer;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import java.io.Serializable;

public final class BeanPropertyMap implements Serializable, Iterable<SettableBeanProperty>
{
    private final Bucket[] _buckets;
    private final int _hashMask;
    private int _nextBucketIndex;
    private final int _size;
    
    public BeanPropertyMap(final Collection<SettableBeanProperty> collection) {
        this._nextBucketIndex = 0;
        this._size = collection.size();
        final int size = findSize(this._size);
        this._hashMask = size - 1;
        final Bucket[] buckets = new Bucket[size];
        for (final SettableBeanProperty settableBeanProperty : collection) {
            final String name = settableBeanProperty.getName();
            final int n = name.hashCode() & this._hashMask;
            buckets[n] = new Bucket(buckets[n], name, settableBeanProperty, this._nextBucketIndex++);
        }
        this._buckets = buckets;
    }
    
    private BeanPropertyMap(final Bucket[] buckets, final int size, final int nextBucketIndex) {
        this._nextBucketIndex = 0;
        this._buckets = buckets;
        this._size = size;
        this._hashMask = buckets.length - 1;
        this._nextBucketIndex = nextBucketIndex;
    }
    
    private SettableBeanProperty _findWithEquals(final String s, final int n) {
        for (Bucket next = this._buckets[n]; next != null; next = next.next) {
            if (s.equals(next.key)) {
                return next.value;
            }
        }
        return null;
    }
    
    private static final int findSize(int n) {
        if (n <= 32) {
            n += n;
        }
        else {
            n += n >> 2;
        }
        int i;
        for (i = 2; i < n; i += i) {}
        return i;
    }
    
    public BeanPropertyMap assignIndexes() {
        final Bucket[] buckets = this._buckets;
        final int length = buckets.length;
        int i = 0;
        int n = 0;
        while (i < length) {
            for (Bucket next = buckets[i]; next != null; next = next.next, ++n) {
                next.value.assignIndex(n);
            }
            ++i;
        }
        return this;
    }
    
    public SettableBeanProperty find(final String s) {
        final int n = this._hashMask & s.hashCode();
        final Bucket bucket = this._buckets[n];
        if (bucket == null) {
            return null;
        }
        Bucket bucket2 = bucket;
        if (bucket.key == s) {
            return bucket.value;
        }
        Bucket next;
        do {
            next = bucket2.next;
            if (next == null) {
                return this._findWithEquals(s, n);
            }
            bucket2 = next;
        } while (next.key != s);
        return next.value;
    }
    
    public SettableBeanProperty[] getPropertiesInInsertionOrder() {
        final SettableBeanProperty[] array = new SettableBeanProperty[this._nextBucketIndex];
        final Bucket[] buckets = this._buckets;
        for (int length = buckets.length, i = 0; i < length; ++i) {
            for (Bucket next = buckets[i]; next != null; next = next.next) {
                array[next.index] = next.value;
            }
        }
        return array;
    }
    
    @Override
    public Iterator<SettableBeanProperty> iterator() {
        return new IteratorImpl(this._buckets);
    }
    
    public void remove(final SettableBeanProperty settableBeanProperty) {
        final String name = settableBeanProperty.getName();
        final int n = name.hashCode() & this._buckets.length - 1;
        Bucket next = this._buckets[n];
        int n2 = 0;
        Bucket bucket = null;
        while (next != null) {
            if (n2 == 0 && next.key.equals(name)) {
                n2 = 1;
            }
            else {
                bucket = new Bucket(bucket, next.key, next.value, next.index);
            }
            next = next.next;
        }
        if (n2 == 0) {
            throw new NoSuchElementException("No entry '" + settableBeanProperty + "' found, can't remove");
        }
        this._buckets[n] = bucket;
    }
    
    public BeanPropertyMap renameAll(final NameTransformer nameTransformer) {
        if (nameTransformer == null || nameTransformer == NameTransformer.NOP) {
            return this;
        }
        final Iterator<SettableBeanProperty> iterator = this.iterator();
        final ArrayList<SettableBeanProperty> list = new ArrayList<SettableBeanProperty>();
        while (iterator.hasNext()) {
            final SettableBeanProperty settableBeanProperty = iterator.next();
            final SettableBeanProperty withName = settableBeanProperty.withName(nameTransformer.transform(settableBeanProperty.getName()));
            final JsonDeserializer<Object> valueDeserializer = withName.getValueDeserializer();
            SettableBeanProperty withValueDeserializer = withName;
            if (valueDeserializer != null) {
                final JsonDeserializer<Object> unwrappingDeserializer = valueDeserializer.unwrappingDeserializer(nameTransformer);
                withValueDeserializer = withName;
                if (unwrappingDeserializer != valueDeserializer) {
                    withValueDeserializer = withName.withValueDeserializer(unwrappingDeserializer);
                }
            }
            list.add(withValueDeserializer);
        }
        return new BeanPropertyMap(list);
    }
    
    public void replace(final SettableBeanProperty settableBeanProperty) {
        final String name = settableBeanProperty.getName();
        final int n = name.hashCode() & this._buckets.length - 1;
        Bucket next = this._buckets[n];
        int index = -1;
        Bucket bucket = null;
        while (next != null) {
            if (index < 0 && next.key.equals(name)) {
                index = next.index;
            }
            else {
                bucket = new Bucket(bucket, next.key, next.value, next.index);
            }
            next = next.next;
        }
        if (index < 0) {
            throw new NoSuchElementException("No entry '" + settableBeanProperty + "' found, can't replace");
        }
        this._buckets[n] = new Bucket(bucket, name, settableBeanProperty, index);
    }
    
    public int size() {
        return this._size;
    }
    
    @Override
    public String toString() {
        int n = 0;
        final StringBuilder sb = new StringBuilder();
        sb.append("Properties=[");
        final SettableBeanProperty[] propertiesInInsertionOrder = this.getPropertiesInInsertionOrder();
        for (int length = propertiesInInsertionOrder.length, i = 0; i < length; ++i) {
            final SettableBeanProperty settableBeanProperty = propertiesInInsertionOrder[i];
            if (settableBeanProperty != null) {
                if (n > 0) {
                    sb.append(", ");
                }
                sb.append(settableBeanProperty.getName());
                sb.append('(');
                sb.append(settableBeanProperty.getType());
                sb.append(')');
                ++n;
            }
        }
        sb.append(']');
        return sb.toString();
    }
    
    public BeanPropertyMap withProperty(final SettableBeanProperty settableBeanProperty) {
        final int length = this._buckets.length;
        final Bucket[] array = new Bucket[length];
        System.arraycopy(this._buckets, 0, array, 0, length);
        final String name = settableBeanProperty.getName();
        if (this.find(settableBeanProperty.getName()) == null) {
            final int n = name.hashCode() & this._hashMask;
            array[n] = new Bucket(array[n], name, settableBeanProperty, this._nextBucketIndex++);
            return new BeanPropertyMap(array, this._size + 1, this._nextBucketIndex);
        }
        final BeanPropertyMap beanPropertyMap = new BeanPropertyMap(array, length, this._nextBucketIndex);
        beanPropertyMap.replace(settableBeanProperty);
        return beanPropertyMap;
    }
    
    private static final class Bucket implements Serializable
    {
        public final int index;
        public final String key;
        public final Bucket next;
        public final SettableBeanProperty value;
        
        public Bucket(final Bucket next, final String key, final SettableBeanProperty value, final int index) {
            this.next = next;
            this.key = key;
            this.value = value;
            this.index = index;
        }
    }
    
    private static final class IteratorImpl implements Iterator<SettableBeanProperty>
    {
        private final Bucket[] _buckets;
        private Bucket _currentBucket;
        private int _nextBucketIndex;
        
        public IteratorImpl(Bucket[] buckets) {
            this._buckets = buckets;
            while (true) {
                int i;
                int nextBucketIndex;
                for (i = 0; i < this._buckets.length; i = nextBucketIndex) {
                    buckets = this._buckets;
                    nextBucketIndex = i + 1;
                    final Bucket currentBucket = buckets[i];
                    if (currentBucket != null) {
                        this._currentBucket = currentBucket;
                        this._nextBucketIndex = nextBucketIndex;
                        return;
                    }
                }
                nextBucketIndex = i;
                continue;
            }
        }
        
        @Override
        public boolean hasNext() {
            return this._currentBucket != null;
        }
        
        @Override
        public SettableBeanProperty next() {
            final Bucket currentBucket = this._currentBucket;
            if (currentBucket == null) {
                throw new NoSuchElementException();
            }
            Serializable next;
            for (next = currentBucket.next; next == null && this._nextBucketIndex < this._buckets.length; next = this._buckets[this._nextBucketIndex++]) {}
            this._currentBucket = (Bucket)next;
            return currentBucket.value;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
