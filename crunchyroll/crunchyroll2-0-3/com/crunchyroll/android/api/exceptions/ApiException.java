// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.exceptions;

public class ApiException extends RuntimeException
{
    private static final long serialVersionUID = -53934242587787724L;
    
    public ApiException() {
    }
    
    public ApiException(final String s) {
        super(s);
    }
    
    public ApiException(final String s, final Throwable t) {
        super(s, t);
    }
    
    public ApiException(final Throwable t) {
        super(t);
    }
}
