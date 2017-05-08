// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core.internal.models;

public class ThreadData
{
    public final FrameData[] frames;
    public final int importance;
    public final String name;
    
    public static final class FrameData
    {
        public final long address;
        public final String file;
        public final int importance;
        public final long offset;
        public final String symbol;
    }
}
