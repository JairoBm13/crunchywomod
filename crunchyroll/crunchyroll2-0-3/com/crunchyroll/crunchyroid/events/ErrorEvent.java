// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.events;

public class ErrorEvent
{
    private final Integer mId;
    private final String mMessage;
    
    public ErrorEvent(final int n, final String mMessage) {
        this.mId = n;
        this.mMessage = mMessage;
    }
    
    public ErrorEvent(final String mMessage) {
        this.mId = null;
        this.mMessage = mMessage;
    }
    
    public int getId() {
        return this.mId;
    }
    
    public String getMessage() {
        return this.mMessage;
    }
}
