// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.api;

public interface PendingResult<R extends Result>
{
    void setResultCallback(final ResultCallback<R> p0);
    
    public interface BatchCallback
    {
        void zzs(final Status p0);
    }
}
