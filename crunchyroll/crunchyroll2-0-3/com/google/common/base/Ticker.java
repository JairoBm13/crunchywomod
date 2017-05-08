// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.base;

public abstract class Ticker
{
    private static final Ticker SYSTEM_TICKER;
    
    static {
        SYSTEM_TICKER = new Ticker() {
            @Override
            public long read() {
                return Platform.systemNanoTime();
            }
        };
    }
    
    public static Ticker systemTicker() {
        return Ticker.SYSTEM_TICKER;
    }
    
    public abstract long read();
}
