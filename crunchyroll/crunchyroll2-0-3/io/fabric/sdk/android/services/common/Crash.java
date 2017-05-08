// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.common;

public abstract class Crash
{
    private final String sessionId;
    
    public Crash(final String sessionId) {
        this.sessionId = sessionId;
    }
    
    public String getSessionId() {
        return this.sessionId;
    }
    
    public static class FatalException extends Crash
    {
        public FatalException(final String s) {
            super(s);
        }
    }
}
