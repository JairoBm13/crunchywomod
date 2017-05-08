// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.util;

public class LoggerFactory
{
    public static Logger getLogger(final Class<?> clazz) {
        return new Logger(clazz.getSimpleName());
    }
    
    public static Logger getLogger(final String s) {
        return new Logger(s);
    }
}
