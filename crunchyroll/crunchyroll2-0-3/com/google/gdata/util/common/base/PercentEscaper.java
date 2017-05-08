// 
// Decompiled by Procyon v0.5.30
// 

package com.google.gdata.util.common.base;

public class PercentEscaper extends UnicodeEscaper
{
    private static final char[] UPPER_HEX_DIGITS;
    private static final char[] URI_ESCAPED_SPACE;
    private final boolean plusForSpace;
    private final boolean[] safeOctets;
    
    static {
        URI_ESCAPED_SPACE = new char[] { '+' };
        UPPER_HEX_DIGITS = "0123456789ABCDEF".toCharArray();
    }
    
    public PercentEscaper(final String s, final boolean plusForSpace) {
        if (s.matches(".*[0-9A-Za-z].*")) {
            throw new IllegalArgumentException("Alphanumeric characters are always 'safe' and should not be explicitly specified");
        }
        if (plusForSpace && s.contains(" ")) {
            throw new IllegalArgumentException("plusForSpace cannot be specified when space is a 'safe' character");
        }
        if (s.contains("%")) {
            throw new IllegalArgumentException("The '%' character cannot be specified as 'safe'");
        }
        this.plusForSpace = plusForSpace;
        this.safeOctets = createSafeOctets(s);
    }
    
    private static boolean[] createSafeOctets(final String s) {
        int max = 122;
        final char[] charArray = s.toCharArray();
        for (int length = charArray.length, i = 0; i < length; ++i) {
            max = Math.max(charArray[i], max);
        }
        final boolean[] array = new boolean[max + 1];
        for (int j = 48; j <= 57; ++j) {
            array[j] = true;
        }
        for (int k = 65; k <= 90; ++k) {
            array[k] = true;
        }
        for (int l = 97; l <= 122; ++l) {
            array[l] = true;
        }
        for (int length2 = charArray.length, n = 0; n < length2; ++n) {
            array[charArray[n]] = true;
        }
        return array;
    }
    
    @Override
    public String escape(final String s) {
        final int length = s.length();
        int n = 0;
        String escapeSlow;
        while (true) {
            escapeSlow = s;
            if (n >= length) {
                break;
            }
            final char char1 = s.charAt(n);
            if (char1 >= this.safeOctets.length || !this.safeOctets[char1]) {
                escapeSlow = this.escapeSlow(s, n);
                break;
            }
            ++n;
        }
        return escapeSlow;
    }
    
    @Override
    protected char[] escape(int n) {
        if (n < this.safeOctets.length && this.safeOctets[n]) {
            return null;
        }
        if (n == 32 && this.plusForSpace) {
            return PercentEscaper.URI_ESCAPED_SPACE;
        }
        if (n <= 127) {
            return new char[] { '%', PercentEscaper.UPPER_HEX_DIGITS[n >>> 4], PercentEscaper.UPPER_HEX_DIGITS[n & 0xF] };
        }
        if (n <= 2047) {
            final char c = PercentEscaper.UPPER_HEX_DIGITS[n & 0xF];
            n >>>= 4;
            final char c2 = PercentEscaper.UPPER_HEX_DIGITS[(n & 0x3) | 0x8];
            n >>>= 2;
            return new char[] { '%', PercentEscaper.UPPER_HEX_DIGITS[n >>> 4 | 0xC], PercentEscaper.UPPER_HEX_DIGITS[n & 0xF], '%', c2, c };
        }
        if (n <= 65535) {
            final char c3 = PercentEscaper.UPPER_HEX_DIGITS[n & 0xF];
            n >>>= 4;
            final char c4 = PercentEscaper.UPPER_HEX_DIGITS[(n & 0x3) | 0x8];
            n >>>= 2;
            final char c5 = PercentEscaper.UPPER_HEX_DIGITS[n & 0xF];
            n >>>= 4;
            return new char[] { '%', 'E', PercentEscaper.UPPER_HEX_DIGITS[n >>> 2], '%', PercentEscaper.UPPER_HEX_DIGITS[(n & 0x3) | 0x8], c5, '%', c4, c3 };
        }
        if (n <= 1114111) {
            final char c6 = PercentEscaper.UPPER_HEX_DIGITS[n & 0xF];
            n >>>= 4;
            final char c7 = PercentEscaper.UPPER_HEX_DIGITS[(n & 0x3) | 0x8];
            n >>>= 2;
            final char c8 = PercentEscaper.UPPER_HEX_DIGITS[n & 0xF];
            n >>>= 4;
            final char c9 = PercentEscaper.UPPER_HEX_DIGITS[(n & 0x3) | 0x8];
            n >>>= 2;
            final char c10 = PercentEscaper.UPPER_HEX_DIGITS[n & 0xF];
            n >>>= 4;
            return new char[] { '%', 'F', PercentEscaper.UPPER_HEX_DIGITS[n >>> 2 & 0x7], '%', PercentEscaper.UPPER_HEX_DIGITS[(n & 0x3) | 0x8], c10, '%', c9, c8, '%', c7, c6 };
        }
        throw new IllegalArgumentException("Invalid unicode character value " + n);
    }
    
    @Override
    protected int nextEscapeIndex(final CharSequence charSequence, int i, final int n) {
        while (i < n) {
            final char char1 = charSequence.charAt(i);
            if (char1 >= this.safeOctets.length || !this.safeOctets[char1]) {
                break;
            }
            ++i;
        }
        return i;
    }
}
