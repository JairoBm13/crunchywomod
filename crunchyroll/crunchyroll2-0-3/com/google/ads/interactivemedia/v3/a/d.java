// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;

public enum d implements e
{
    a {
        @Override
        public String a(final Field field) {
            return field.getName();
        }
    }, 
    b {
        @Override
        public String a(final Field field) {
            return b(field.getName());
        }
    }, 
    c {
        @Override
        public String a(final Field field) {
            return b(b(field.getName(), " "));
        }
    }, 
    d {
        @Override
        public String a(final Field field) {
            return b(field.getName(), "_").toLowerCase();
        }
    }, 
    e {
        @Override
        public String a(final Field field) {
            return b(field.getName(), "-").toLowerCase();
        }
    };
    
    private static String a(final char c, final String s, final int n) {
        if (n < s.length()) {
            return c + s.substring(n);
        }
        return String.valueOf(c);
    }
    
    private static String b(final String s) {
        final StringBuilder sb = new StringBuilder();
        int n;
        char c;
        for (n = 0, c = s.charAt(0); n < s.length() - 1 && !Character.isLetter(c); ++n, c = s.charAt(n)) {
            sb.append(c);
        }
        String string;
        if (n == s.length()) {
            string = sb.toString();
        }
        else {
            string = s;
            if (!Character.isUpperCase(c)) {
                return sb.append(a(Character.toUpperCase(c), s, n + 1)).toString();
            }
        }
        return string;
    }
    
    private static String b(final String s, final String s2) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            if (Character.isUpperCase(char1) && sb.length() != 0) {
                sb.append(s2);
            }
            sb.append(char1);
        }
        return sb.toString();
    }
}
