// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

import java.util.logging.Logger;

public final class $Stopwatch
{
    private static final Logger logger;
    private long start;
    
    static {
        logger = Logger.getLogger($Stopwatch.class.getName());
    }
    
    public $Stopwatch() {
        this.start = System.currentTimeMillis();
    }
    
    public long reset() {
        final long currentTimeMillis = System.currentTimeMillis();
        try {
            return (this.start = currentTimeMillis) - this.start;
        }
        finally {
            this.start = currentTimeMillis;
        }
    }
    
    public void resetAndLog(final String s) {
        $Stopwatch.logger.fine(s + ": " + this.reset() + "ms");
    }
}
