// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.util;

public final class MimeTypes
{
    private static String getTopLevelType(final String s) {
        final int index = s.indexOf(47);
        if (index == -1) {
            throw new IllegalArgumentException("Invalid mime type: " + s);
        }
        return s.substring(0, index);
    }
    
    public static boolean isAudio(final String s) {
        return getTopLevelType(s).equals("audio");
    }
    
    public static boolean isVideo(final String s) {
        return getTopLevelType(s).equals("video");
    }
}
