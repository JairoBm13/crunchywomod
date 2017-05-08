// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.util;

import android.util.Log;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;

public final class Logger
{
    private final String mName;
    
    public Logger(final String mName) {
        this.mName = mName;
    }
    
    public static String errorStringFromIntegers(final int n, final int n2) {
        String s = "";
        String s2 = "";
        switch (n) {
            case 1: {
                s = "Unknown";
                break;
            }
            case 100: {
                s = "Server died";
                break;
            }
        }
        switch (n2) {
            case -1004: {
                s2 = "Network I/O";
                break;
            }
            case 200: {
                s2 = "Invalid for progressive playback";
                break;
            }
            case -1007: {
                s2 = "Media malformed";
                break;
            }
            case -1010: {
                s2 = "Media unsupported";
                break;
            }
            case -110: {
                s2 = "Timed out";
                break;
            }
        }
        return String.format("what=%s, extra=%s", s, s2);
    }
    
    public void debug() {
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length > 3) {
            final StackTraceElement stackTraceElement = stackTrace[3];
            this.debug("%s:%d", stackTraceElement.getMethodName(), stackTraceElement.getLineNumber());
        }
    }
    
    public void debug(final String s, final Object... array) {
        if (CrunchyrollApplication.isDebuggable()) {
            Log.d(this.mName, String.format(s, array));
        }
    }
    
    public void error(final String s, final Throwable t) {
        Log.e(this.mName, s, t);
    }
    
    public void error(final String s, final Object... array) {
        Log.e(this.mName, String.format(s, array));
    }
    
    public void info(final String s, final Object... array) {
        if (CrunchyrollApplication.isDebuggable()) {
            Log.i(this.mName, String.format(s, array));
        }
    }
    
    public void verbose(final String s, final Object... array) {
        if (CrunchyrollApplication.isDebuggable()) {
            Log.v(this.mName, String.format(s, array));
        }
    }
    
    public void warn(final String s, final Object... array) {
        Log.w(this.mName, String.format(s, array));
    }
}
