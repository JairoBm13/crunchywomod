// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.exceptions;

public class ApiUnknownException extends ApiException
{
    private static final long serialVersionUID = 3713933249424959267L;
    
    public ApiUnknownException() {
    }
    
    public ApiUnknownException(final String s) {
        super(s);
    }
    
    public ApiUnknownException(final String s, final Throwable t) {
        super(s, t);
    }
    
    public ApiUnknownException(final Throwable t) {
        super(t);
    }
}
