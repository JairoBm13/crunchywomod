// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.exceptions;

public class ApiBadResponseException extends ApiException
{
    private static final long serialVersionUID = 5231252255792463299L;
    
    public ApiBadResponseException() {
    }
    
    public ApiBadResponseException(final String s) {
        super(s);
    }
    
    public ApiBadResponseException(final String s, final Throwable t) {
        super(s, t);
    }
    
    public ApiBadResponseException(final Throwable t) {
        super(t);
    }
}
