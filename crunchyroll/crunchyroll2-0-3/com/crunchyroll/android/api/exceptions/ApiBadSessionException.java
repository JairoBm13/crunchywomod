// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.exceptions;

public final class ApiBadSessionException extends ApiErrorException
{
    private static final long serialVersionUID = -4220482867277632928L;
    
    public ApiBadSessionException(final Throwable t, final String s, final ApiErrorCode apiErrorCode) {
        super(t, s, apiErrorCode);
    }
    
    @Override
    public String toString() {
        return "ApiBadSessionException [throwable=" + this.throwable + ", message=" + this.message + ", errorCode=" + this.errorCode + "]";
    }
}
