// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.persistence;

import android.annotation.TargetApi;
import android.os.Build$VERSION;
import android.content.SharedPreferences$Editor;
import io.fabric.sdk.android.Kit;
import android.content.SharedPreferences;
import android.content.Context;

public class PreferenceStoreImpl implements PreferenceStore
{
    private final Context context;
    private final String preferenceName;
    private final SharedPreferences sharedPreferences;
    
    public PreferenceStoreImpl(final Context context, final String preferenceName) {
        if (context == null) {
            throw new IllegalStateException("Cannot get directory before context has been set. Call Fabric.with() first");
        }
        this.context = context;
        this.preferenceName = preferenceName;
        this.sharedPreferences = this.context.getSharedPreferences(this.preferenceName, 0);
    }
    
    public PreferenceStoreImpl(final Kit kit) {
        this(kit.getContext(), kit.getClass().getName());
    }
    
    @Override
    public SharedPreferences$Editor edit() {
        return this.sharedPreferences.edit();
    }
    
    @Override
    public SharedPreferences get() {
        return this.sharedPreferences;
    }
    
    @TargetApi(9)
    @Override
    public boolean save(final SharedPreferences$Editor sharedPreferences$Editor) {
        if (Build$VERSION.SDK_INT >= 9) {
            sharedPreferences$Editor.apply();
            return true;
        }
        return sharedPreferences$Editor.commit();
    }
}
