// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import android.util.Log;

public class BaseListener<T> implements ApiTaskListener<T>
{
    @Override
    public void onCancel() {
    }
    
    @Override
    public void onException(final Exception ex) {
        Log.e("BaseListener", "onException()", (Throwable)ex);
    }
    
    @Override
    public void onFinally() {
    }
    
    @Override
    public void onInterrupted(final Exception ex) {
    }
    
    @Override
    public void onPreExecute() {
    }
    
    @Override
    public void onSuccess(final T t) {
    }
}
