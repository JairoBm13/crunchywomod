// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.assist;

public class FailReason
{
    private final Throwable cause;
    private final FailType type;
    
    public FailReason(final FailType type, final Throwable cause) {
        this.type = type;
        this.cause = cause;
    }
    
    public Throwable getCause() {
        return this.cause;
    }
    
    public FailType getType() {
        return this.type;
    }
    
    public enum FailType
    {
        DECODING_ERROR, 
        IO_ERROR, 
        NETWORK_DENIED, 
        OUT_OF_MEMORY, 
        UNKNOWN;
    }
}
