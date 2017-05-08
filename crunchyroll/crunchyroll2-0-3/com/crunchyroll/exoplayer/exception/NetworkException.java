// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.exoplayer.exception;

public class NetworkException extends Exception
{
    public NetworkException(final String s, final Throwable t) {
        super(s, t);
    }
    
    public NetworkException(final Throwable t) {
        super(t);
    }
}
