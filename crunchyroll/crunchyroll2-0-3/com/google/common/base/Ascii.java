// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.base;

public final class Ascii
{
    public static boolean isUpperCase(final char c) {
        return c >= 'A' && c <= 'Z';
    }
    
    public static char toLowerCase(final char c) {
        char c2 = c;
        if (isUpperCase(c)) {
            c2 = (char)(c ^ ' ');
        }
        return c2;
    }
    
    public static String toLowerCase(final String s) {
        final int length = s.length();
        final StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            sb.append(toLowerCase(s.charAt(i)));
        }
        return sb.toString();
    }
}
