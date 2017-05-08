// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk;

import android.app.Activity;
import java.util.Map;
import android.content.Context;
import java.lang.ref.WeakReference;
import java.io.File;
import com.swrve.sdk.config.SwrveConfigBase;

public class SwrveBaseEmpty<T, C extends SwrveConfigBase> implements ISwrveBase<T, C>
{
    protected String apiKey;
    private File cacheDir;
    private C config;
    protected WeakReference<Context> context;
    private String language;
    private String userId;
    
    protected SwrveBaseEmpty(final Context context, final String apiKey) {
        this.language = "en-US";
        this.context = new WeakReference<Context>(context.getApplicationContext());
        this.apiKey = apiKey;
        this.config = (C)new SwrveConfigBaseImp();
    }
    
    @Override
    public void event(final String s) {
    }
    
    @Override
    public void event(final String s, final Map<String, String> map) {
    }
    
    @Override
    public SwrveResourceManager getResourceManager() {
        return new SwrveResourceManager();
    }
    
    @Override
    public T onCreate(final Activity activity) throws IllegalArgumentException {
        this.context = new WeakReference<Context>((Context)activity);
        this.language = this.config.getLanguage();
        this.userId = this.config.getUserId();
        this.cacheDir = this.config.getCacheDir();
        if (this.cacheDir == null) {
            this.cacheDir = activity.getCacheDir();
        }
        return (T)this;
    }
    
    @Override
    public void onDestroy(final Activity activity) {
    }
    
    @Override
    public void onLowMemory() {
    }
    
    @Override
    public void onPause() {
    }
    
    @Override
    public void onResume(final Activity activity) {
    }
    
    @Override
    public void userUpdate(final Map<String, String> map) {
    }
    
    private class SwrveConfigBaseImp extends SwrveConfigBase
    {
    }
}
