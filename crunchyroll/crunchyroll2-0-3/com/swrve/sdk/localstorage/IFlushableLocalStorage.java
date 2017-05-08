// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.localstorage;

public interface IFlushableLocalStorage
{
    void flushCache(final IFastInsertLocalStorage p0);
    
    void flushEvents(final IFastInsertLocalStorage p0);
}
