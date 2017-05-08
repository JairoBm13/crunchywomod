// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android;

import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase$CursorFactory;
import android.content.SharedPreferences;
import android.annotation.TargetApi;
import java.io.File;
import android.content.Context;
import android.content.ContextWrapper;

class FabricContext extends ContextWrapper
{
    private final String componentName;
    private final String componentPath;
    
    public FabricContext(final Context context, final String componentName, final String componentPath) {
        super(context);
        this.componentName = componentName;
        this.componentPath = componentPath;
    }
    
    public File getCacheDir() {
        return new File(super.getCacheDir(), this.componentPath);
    }
    
    public File getDatabasePath(final String s) {
        final File file = new File(super.getDatabasePath(s).getParentFile(), this.componentPath);
        file.mkdirs();
        return new File(file, s);
    }
    
    @TargetApi(8)
    public File getExternalCacheDir() {
        return new File(super.getExternalCacheDir(), this.componentPath);
    }
    
    @TargetApi(8)
    public File getExternalFilesDir(final String s) {
        return new File(super.getExternalFilesDir(s), this.componentPath);
    }
    
    public File getFilesDir() {
        return new File(super.getFilesDir(), this.componentPath);
    }
    
    public SharedPreferences getSharedPreferences(final String s, final int n) {
        return super.getSharedPreferences(this.componentName + ":" + s, n);
    }
    
    public SQLiteDatabase openOrCreateDatabase(final String s, final int n, final SQLiteDatabase$CursorFactory sqLiteDatabase$CursorFactory) {
        return SQLiteDatabase.openOrCreateDatabase(this.getDatabasePath(s), sqLiteDatabase$CursorFactory);
    }
    
    @TargetApi(11)
    public SQLiteDatabase openOrCreateDatabase(final String s, final int n, final SQLiteDatabase$CursorFactory sqLiteDatabase$CursorFactory, final DatabaseErrorHandler databaseErrorHandler) {
        return SQLiteDatabase.openOrCreateDatabase(this.getDatabasePath(s).getPath(), sqLiteDatabase$CursorFactory, databaseErrorHandler);
    }
}
