// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.utils;

import com.nostra13.universalimageloader.core.ImageLoader;
import android.util.Log;

public final class L
{
    private static final String LOG_FORMAT = "%1$s\n%2$s";
    private static volatile boolean writeDebugLogs;
    private static volatile boolean writeLogs;
    
    static {
        L.writeDebugLogs = false;
        L.writeLogs = true;
    }
    
    public static void d(final String s, final Object... array) {
        if (L.writeDebugLogs) {
            log(3, null, s, array);
        }
    }
    
    @Deprecated
    public static void disableLogging() {
        writeLogs(false);
    }
    
    public static void e(final String s, final Object... array) {
        log(6, null, s, array);
    }
    
    public static void e(final Throwable t) {
        log(6, t, null, new Object[0]);
    }
    
    public static void e(final Throwable t, final String s, final Object... array) {
        log(6, t, s, array);
    }
    
    @Deprecated
    public static void enableLogging() {
        writeLogs(true);
    }
    
    public static void i(final String s, final Object... array) {
        log(4, null, s, array);
    }
    
    private static void log(final int n, final Throwable t, String message, final Object... array) {
        if (!L.writeLogs) {
            return;
        }
        String s = message;
        if (array.length > 0) {
            s = String.format(message, array);
        }
        if (t != null) {
            if (s == null) {
                message = t.getMessage();
            }
            else {
                message = s;
            }
            s = String.format("%1$s\n%2$s", message, Log.getStackTraceString(t));
        }
        Log.println(n, ImageLoader.TAG, s);
    }
    
    public static void w(final String s, final Object... array) {
        log(5, null, s, array);
    }
    
    public static void writeDebugLogs(final boolean writeDebugLogs) {
        L.writeDebugLogs = writeDebugLogs;
    }
    
    public static void writeLogs(final boolean writeLogs) {
        L.writeLogs = writeLogs;
    }
}
