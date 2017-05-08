// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android;

import android.util.Log;

public class DefaultLogger implements Logger
{
    private int logLevel;
    
    public DefaultLogger() {
        this.logLevel = 4;
    }
    
    public DefaultLogger(final int logLevel) {
        this.logLevel = logLevel;
    }
    
    @Override
    public void d(final String s, final String s2) {
        this.d(s, s2, null);
    }
    
    @Override
    public void d(final String s, final String s2, final Throwable t) {
        if (this.isLoggable(s, 3)) {
            Log.d(s, s2, t);
        }
    }
    
    @Override
    public void e(final String s, final String s2) {
        this.e(s, s2, null);
    }
    
    @Override
    public void e(final String s, final String s2, final Throwable t) {
        if (this.isLoggable(s, 6)) {
            Log.e(s, s2, t);
        }
    }
    
    @Override
    public void i(final String s, final String s2) {
        this.i(s, s2, null);
    }
    
    public void i(final String s, final String s2, final Throwable t) {
        if (this.isLoggable(s, 4)) {
            Log.i(s, s2, t);
        }
    }
    
    @Override
    public boolean isLoggable(final String s, final int n) {
        return this.logLevel <= n;
    }
    
    @Override
    public void log(final int n, final String s, final String s2) {
        this.log(n, s, s2, false);
    }
    
    public void log(final int n, final String s, final String s2, final boolean b) {
        if (b || this.isLoggable(s, n)) {
            Log.println(n, s, s2);
        }
    }
    
    @Override
    public void v(final String s, final String s2) {
        this.v(s, s2, null);
    }
    
    public void v(final String s, final String s2, final Throwable t) {
        if (this.isLoggable(s, 2)) {
            Log.v(s, s2, t);
        }
    }
    
    @Override
    public void w(final String s, final String s2) {
        this.w(s, s2, null);
    }
    
    @Override
    public void w(final String s, final String s2, final Throwable t) {
        if (this.isLoggable(s, 5)) {
            Log.w(s, s2, t);
        }
    }
}
