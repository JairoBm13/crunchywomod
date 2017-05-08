// 
// Decompiled by Procyon v0.5.30
// 

package com.google.gdata.util.common.base;

public abstract class UnicodeEscaper
{
    private static final ThreadLocal<char[]> DEST_TL;
    
    static {
        DEST_TL = new ThreadLocal<char[]>() {
            @Override
            protected char[] initialValue() {
                return new char[1024];
            }
        };
    }
    
    protected static final int codePointAt(final CharSequence charSequence, final int n, final int n2) {
        if (n >= n2) {
            throw new IndexOutOfBoundsException("Index exceeds specified range");
        }
        final int n3 = n + 1;
        final char char1 = charSequence.charAt(n);
        if (char1 < '\ud800' || char1 > '\udfff') {
            return char1;
        }
        if (char1 > '\udbff') {
            throw new IllegalArgumentException("Unexpected low surrogate character '" + char1 + "' with value " + (int)char1 + " at index " + (n3 - 1));
        }
        if (n3 == n2) {
            return -char1;
        }
        final char char2 = charSequence.charAt(n3);
        if (Character.isLowSurrogate(char2)) {
            return Character.toCodePoint(char1, char2);
        }
        throw new IllegalArgumentException("Expected low surrogate but got char '" + char2 + "' with value " + (int)char2 + " at index " + n3);
    }
    
    private static final char[] growBuffer(final char[] array, final int n, final int n2) {
        final char[] array2 = new char[n2];
        if (n > 0) {
            System.arraycopy(array, 0, array2, 0, n);
        }
        return array2;
    }
    
    public String escape(final String s) {
        final int length = s.length();
        final int nextEscapeIndex = this.nextEscapeIndex(s, 0, length);
        if (nextEscapeIndex == length) {
            return s;
        }
        return this.escapeSlow(s, nextEscapeIndex);
    }
    
    protected abstract char[] escape(final int p0);
    
    protected final String escapeSlow(final String s, int n) {
        final int length = s.length();
        char[] array = UnicodeEscaper.DEST_TL.get();
        final int n2 = 0;
        int n3 = 0;
        int i = n;
        n = n2;
        while (i < length) {
            final int codePoint = codePointAt(s, i, length);
            if (codePoint < 0) {
                throw new IllegalArgumentException("Trailing high surrogate at end of input");
            }
            final char[] escape = this.escape(codePoint);
            char[] array2 = array;
            int n4 = n;
            if (escape != null) {
                final int n5 = i - n3;
                final int n6 = n + n5 + escape.length;
                char[] growBuffer = array;
                if (array.length < n6) {
                    growBuffer = growBuffer(array, n, length - i + n6 + 32);
                }
                int n7 = n;
                if (n5 > 0) {
                    s.getChars(n3, i, growBuffer, n);
                    n7 = n + n5;
                }
                array2 = growBuffer;
                n4 = n7;
                if (escape.length > 0) {
                    System.arraycopy(escape, 0, growBuffer, n7, escape.length);
                    n4 = n7 + escape.length;
                    array2 = growBuffer;
                }
            }
            if (Character.isSupplementaryCodePoint(codePoint)) {
                n = 2;
            }
            else {
                n = 1;
            }
            n3 = i + n;
            i = this.nextEscapeIndex(s, n3, length);
            array = array2;
            n = n4;
        }
        final int n8 = length - n3;
        char[] growBuffer2 = array;
        int n9 = n;
        if (n8 > 0) {
            n9 = n + n8;
            growBuffer2 = array;
            if (array.length < n9) {
                growBuffer2 = growBuffer(array, n, n9);
            }
            s.getChars(n3, length, growBuffer2, n);
        }
        return new String(growBuffer2, 0, n9);
    }
    
    protected int nextEscapeIndex(final CharSequence charSequence, int i, final int n) {
        while (i < n) {
            final int codePoint = codePointAt(charSequence, i, n);
            if (codePoint < 0 || this.escape(codePoint) != null) {
                break;
            }
            int n2;
            if (Character.isSupplementaryCodePoint(codePoint)) {
                n2 = 2;
            }
            else {
                n2 = 1;
            }
            i += n2;
        }
        return i;
    }
}
