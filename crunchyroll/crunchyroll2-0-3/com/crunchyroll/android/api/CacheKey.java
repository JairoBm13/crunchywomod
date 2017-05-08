// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api;

import com.google.common.base.Optional;

public class CacheKey
{
    private Class objectClass;
    private Optional objectValue;
    
    public CacheKey(final CacheKey cacheKey) {
        this.objectClass = cacheKey.getObjectClass();
        this.objectValue = cacheKey.getObjectValue();
    }
    
    public CacheKey(final Class objectClass, final Optional objectValue) {
        this.objectClass = objectClass;
        this.objectValue = objectValue;
    }
    
    private Class getObjectClass() {
        return this.objectClass;
    }
    
    private Optional getObjectValue() {
        return this.objectValue;
    }
    
    @Override
    public boolean equals(final Object o) {
        final CacheKey cacheKey = (CacheKey)o;
        return o.getClass().equals(this.getClass()) && this.getObjectClass() != null && cacheKey.getObjectClass().equals(this.getObjectClass()) && this.objectValue.isPresent() && cacheKey.getObjectValue().isPresent() && cacheKey.getObjectValue().get().toString().equals(this.getObjectValue().get().toString());
    }
    
    @Override
    public int hashCode() {
        final boolean b = false;
        int hashCode;
        if (this.objectClass == null) {
            hashCode = 0;
        }
        else {
            hashCode = this.objectClass.hashCode();
        }
        int hashCode2 = b ? 1 : 0;
        if (this.objectValue != null) {
            if (!this.objectValue.isPresent()) {
                hashCode2 = (b ? 1 : 0);
            }
            else {
                hashCode2 = this.objectValue.get().hashCode();
            }
        }
        return (hashCode + 31) * 31 + hashCode2;
    }
    
    @Override
    public String toString() {
        return "Class: " + this.objectClass + " Value: " + this.objectValue;
    }
}
