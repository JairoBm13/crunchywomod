// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

public interface ApiTaskListener<T>
{
    void onCancel();
    
    void onException(final Exception p0);
    
    void onFinally();
    
    void onInterrupted(final Exception p0);
    
    void onPreExecute();
    
    void onSuccess(final T p0);
}
