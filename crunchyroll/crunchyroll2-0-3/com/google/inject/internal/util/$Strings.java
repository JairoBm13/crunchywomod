// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

public class $Strings
{
    public static String capitalize(final String s) {
        if (s.length() != 0) {
            final char char1 = s.charAt(0);
            final char upperCase = Character.toUpperCase(char1);
            if (char1 != upperCase) {
                return upperCase + s.substring(1);
            }
        }
        return s;
    }
}
