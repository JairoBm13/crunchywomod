// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.exceptions;

public class ApiNetworkException extends ApiException
{
    private static final long serialVersionUID = -1942694477214147676L;
    
    public ApiNetworkException() {
    }
    
    public ApiNetworkException(final String s) {
        super(s);
    }
    
    public ApiNetworkException(final String s, final Throwable t) {
        super(s, t);
    }
    
    public ApiNetworkException(final Throwable t) {
        super(t);
    }
}
