// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import java.util.Map;
import java.util.LinkedHashMap;

public class zzku<K, V>
{
    private int size;
    private final LinkedHashMap<K, V> zzabn;
    private int zzabo;
    private int zzabp;
    private int zzabq;
    private int zzabr;
    private int zzabs;
    private int zzabt;
    
    private int zzc(final K k, final V v) {
        final int size = this.sizeOf(k, v);
        if (size < 0) {
            throw new IllegalStateException("Negative size: " + k + "=" + v);
        }
        return size;
    }
    
    protected V create(final K k) {
        return null;
    }
    
    protected void entryRemoved(final boolean b, final K k, final V v, final V v2) {
    }
    
    public final void evictAll() {
        this.trimToSize(-1);
    }
    
    public final V get(final K k) {
        if (k == null) {
            throw new NullPointerException("key == null");
        }
        V v;
        synchronized (this) {
            v = this.zzabn.get(k);
            if (v != null) {
                ++this.zzabs;
                return v;
            }
            ++this.zzabt;
            // monitorexit(this)
            v = this.create(k);
            if (v == null) {
                return null;
            }
        }
        synchronized (this) {
            ++this.zzabq;
            final K i;
            final V put = this.zzabn.put(i, v);
            if (put != null) {
                this.zzabn.put(i, put);
            }
            else {
                this.size += this.zzc(i, v);
            }
            // monitorexit(this)
            if (put != null) {
                this.entryRemoved(false, i, v, put);
                return put;
            }
        }
        this.trimToSize(this.zzabo);
        return v;
    }
    
    public final V put(final K k, final V v) {
        if (k == null || v == null) {
            throw new NullPointerException("key == null || value == null");
        }
        synchronized (this) {
            ++this.zzabp;
            this.size += this.zzc(k, v);
            final V put = this.zzabn.put(k, v);
            if (put != null) {
                this.size -= this.zzc(k, put);
            }
            // monitorexit(this)
            if (put != null) {
                this.entryRemoved(false, k, put, v);
            }
            this.trimToSize(this.zzabo);
            return put;
        }
    }
    
    protected int sizeOf(final K k, final V v) {
        return 1;
    }
    
    @Override
    public final String toString() {
        int n = 0;
        synchronized (this) {
            final int n2 = this.zzabs + this.zzabt;
            if (n2 != 0) {
                n = this.zzabs * 100 / n2;
            }
            return String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", this.zzabo, this.zzabs, this.zzabt, n);
        }
    }
    
    public void trimToSize(final int n) {
        while (true) {
            synchronized (this) {
                if (this.size < 0 || (this.zzabn.isEmpty() && this.size != 0)) {
                    throw new IllegalStateException(this.getClass().getName() + ".sizeOf() is reporting inconsistent results!");
                }
            }
            if (this.size <= n || this.zzabn.isEmpty()) {
                break;
            }
            final Map.Entry<K, V> entry = this.zzabn.entrySet().iterator().next();
            final K key = entry.getKey();
            final V value = entry.getValue();
            this.zzabn.remove(key);
            this.size -= this.zzc(key, value);
            ++this.zzabr;
            // monitorexit(this)
            this.entryRemoved(true, key, value, null);
        }
    }
    // monitorexit(this)
}
