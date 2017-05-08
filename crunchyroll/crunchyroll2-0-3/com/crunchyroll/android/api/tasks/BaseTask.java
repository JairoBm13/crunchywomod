// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.crunchyroll.android.api.ApiResponse;
import com.crunchyroll.android.api.ApiService;
import android.util.Log;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import java.util.Random;
import java.math.BigInteger;
import java.security.SecureRandom;
import android.content.Context;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import com.crunchyroll.android.util.SafeAsyncTask;

public class BaseTask<T> extends SafeAsyncTask<T>
{
    private final String TAG;
    protected CrunchyrollApplication mApplication;
    protected TaskCompleteListener mTaskCompleteListener;
    protected String mTaskId;
    protected ApiTaskListener<T> mTaskListener;
    
    public BaseTask(final Context context) {
        this.TAG = this.getClass().getSimpleName();
        if (context == null) {
            throw new IllegalStateException("Application has not been set.");
        }
        this.mApplication = CrunchyrollApplication.getApp(context);
        this.mTaskId = this.generateTaskId();
    }
    
    private String generateTaskId() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }
    
    protected void broadcastIntent(final String s) {
        LocalBroadcastManager.getInstance((Context)this.mApplication).sendBroadcast(new Intent(s));
    }
    
    @Override
    public T call() throws Exception {
        return null;
    }
    
    public void cancel() {
        Log.d(this.TAG, "cancel()");
        this.cancel(true);
        if (this.mTaskListener != null) {
            this.mTaskListener.onCancel();
            this.mTaskListener.onFinally();
        }
    }
    
    protected ApiService getApiService() {
        return this.mApplication.getApiService();
    }
    
    protected CrunchyrollApplication getApplication() {
        return this.mApplication;
    }
    
    public String getTaskId() {
        return this.mTaskId;
    }
    
    @Override
    protected void onException(final Exception ex) {
        if (this.mTaskListener != null) {
            this.mTaskListener.onException(ex);
        }
    }
    
    @Override
    protected void onFinally() {
        if (this.mTaskListener != null) {
            this.mTaskListener.onFinally();
        }
        if (this.mTaskCompleteListener != null) {
            this.mTaskCompleteListener.onTaskComplete(this.mTaskId);
        }
        this.mTaskListener = null;
        this.mTaskCompleteListener = null;
    }
    
    @Override
    protected void onInterrupted(final Exception ex) {
        Log.d(this.TAG, "onInterrupted()");
        super.onInterrupted(ex);
    }
    
    @Override
    protected void onPreExecute() throws Exception {
        if (this.mTaskListener != null) {
            this.mTaskListener.onPreExecute();
        }
    }
    
    @Override
    protected void onSuccess(final T t) throws Exception {
        if (this.isCancelled()) {
            Log.d(this.TAG, "Data processing interrupted.");
        }
        else if (this.mTaskListener != null) {
            this.mTaskListener.onSuccess(t);
        }
    }
    
    protected <T> T parseResponse(final ApiResponse apiResponse, final TypeReference<T> typeReference) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        return (T)objectMapper.readValue(((JsonNode)apiResponse.body.asParser(objectMapper).readValueAsTree()).path("data").traverse(), typeReference);
    }
    
    public void setTaskCompleteListener(final TaskCompleteListener mTaskCompleteListener) {
        this.mTaskCompleteListener = mTaskCompleteListener;
    }
    
    public BaseTask<T> setTaskListener(final ApiTaskListener<T> mTaskListener) {
        this.mTaskListener = mTaskListener;
        return this;
    }
}
