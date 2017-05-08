// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android;

public interface Logger
{
    void d(final String p0, final String p1);
    
    void d(final String p0, final String p1, final Throwable p2);
    
    void e(final String p0, final String p1);
    
    void e(final String p0, final String p1, final Throwable p2);
    
    void i(final String p0, final String p1);
    
    boolean isLoggable(final String p0, final int p1);
    
    void log(final int p0, final String p1, final String p2);
    
    void v(final String p0, final String p1);
    
    void w(final String p0, final String p1);
    
    void w(final String p0, final String p1, final Throwable p2);
}
