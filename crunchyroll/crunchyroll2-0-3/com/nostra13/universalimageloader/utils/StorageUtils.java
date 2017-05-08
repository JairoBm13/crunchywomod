// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.utils;

import java.io.IOException;
import android.os.Environment;
import java.io.File;
import android.content.Context;

public final class StorageUtils
{
    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String INDIVIDUAL_DIR_NAME = "uil-images";
    
    public static File getCacheDirectory(final Context context) {
        return getCacheDirectory(context, true);
    }
    
    public static File getCacheDirectory(Context string, final boolean b) {
        final File file = null;
        while (true) {
            try {
                final String externalStorageState = Environment.getExternalStorageState();
                File externalCacheDir = file;
                if (b) {
                    externalCacheDir = file;
                    if ("mounted".equals(externalStorageState)) {
                        externalCacheDir = file;
                        if (hasExternalStoragePermission(string)) {
                            externalCacheDir = getExternalCacheDir(string);
                        }
                    }
                }
                File cacheDir;
                if ((cacheDir = externalCacheDir) == null) {
                    cacheDir = string.getCacheDir();
                }
                File file2;
                if ((file2 = cacheDir) == null) {
                    string = (Context)("/data/data/" + string.getPackageName() + "/cache/");
                    L.w("Can't define system cache directory! '%s' will be used.", string);
                    file2 = new File((String)string);
                }
                return file2;
            }
            catch (NullPointerException ex) {
                final String externalStorageState = "";
                continue;
            }
            catch (IncompatibleClassChangeError incompatibleClassChangeError) {
                final String externalStorageState = "";
                continue;
            }
            break;
        }
    }
    
    private static File getExternalCacheDir(final Context context) {
        File file2;
        final File file = file2 = new File(new File(new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data"), context.getPackageName()), "cache");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                L.w("Unable to create external cache directory", new Object[0]);
                file2 = null;
            }
            else {
                try {
                    new File(file, ".nomedia").createNewFile();
                    return file;
                }
                catch (IOException ex) {
                    L.i("Can't create \".nomedia\" file in application external cache directory", new Object[0]);
                    return file;
                }
            }
        }
        return file2;
    }
    
    public static File getIndividualCacheDirectory(final Context context) {
        return getIndividualCacheDirectory(context, "uil-images");
    }
    
    public static File getIndividualCacheDirectory(final Context context, final String s) {
        final File cacheDirectory = getCacheDirectory(context);
        File file2;
        final File file = file2 = new File(cacheDirectory, s);
        if (!file.exists()) {
            file2 = file;
            if (!file.mkdir()) {
                file2 = cacheDirectory;
            }
        }
        return file2;
    }
    
    public static File getOwnCacheDirectory(final Context context, final String s) {
        File file2;
        final File file = file2 = null;
        if ("mounted".equals(Environment.getExternalStorageState())) {
            file2 = file;
            if (hasExternalStoragePermission(context)) {
                file2 = new File(Environment.getExternalStorageDirectory(), s);
            }
        }
        if (file2 != null) {
            File cacheDir = file2;
            if (file2.exists()) {
                return cacheDir;
            }
            cacheDir = file2;
            if (file2.mkdirs()) {
                return cacheDir;
            }
        }
        return context.getCacheDir();
    }
    
    public static File getOwnCacheDirectory(final Context context, final String s, final boolean b) {
        File file2;
        final File file = file2 = null;
        if (b) {
            file2 = file;
            if ("mounted".equals(Environment.getExternalStorageState())) {
                file2 = file;
                if (hasExternalStoragePermission(context)) {
                    file2 = new File(Environment.getExternalStorageDirectory(), s);
                }
            }
        }
        if (file2 != null) {
            File cacheDir = file2;
            if (file2.exists()) {
                return cacheDir;
            }
            cacheDir = file2;
            if (file2.mkdirs()) {
                return cacheDir;
            }
        }
        return context.getCacheDir();
    }
    
    private static boolean hasExternalStoragePermission(final Context context) {
        return context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }
}
