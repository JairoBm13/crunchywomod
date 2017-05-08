// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.base;

final class MediumCharMatcher extends CharMatcher
{
    private final boolean containsZero;
    private final long filter;
    private final char[] table;
    
    private MediumCharMatcher(final char[] table, final long filter, final boolean containsZero, final String s) {
        super(s);
        this.table = table;
        this.filter = filter;
        this.containsZero = containsZero;
    }
    
    private boolean checkFilter(final int n) {
        return 0x1L == (this.filter >> n & 0x1L);
    }
    
    static int chooseTableSize(final int n) {
        int n2;
        if (n == 1) {
            n2 = 2;
        }
        else {
            int n3 = Integer.highestOneBit(n - 1) << 1;
            while (true) {
                n2 = n3;
                if (n3 * 0.5 >= n) {
                    break;
                }
                n3 <<= 1;
            }
        }
        return n2;
    }
    
    static CharMatcher from(final char[] array, final String s) {
        long n = 0L;
        final int length = array.length;
        final boolean b = array[0] == '\0';
        for (int length2 = array.length, i = 0; i < length2; ++i) {
            n |= 1L << array[i];
        }
        final char[] array2 = new char[chooseTableSize(length)];
        final int n2 = array2.length - 1;
        for (int length3 = array.length, j = 0; j < length3; ++j) {
            final char c = array[j];
            int n3;
            for (n3 = (c & n2); array2[n3] != '\0'; n3 = (n3 + 1 & n2)) {}
            array2[n3] = c;
        }
        return new MediumCharMatcher(array2, n, b, s);
    }
    
    @Override
    public boolean matches(final char c) {
        final boolean b = false;
        boolean containsZero;
        if (c == '\0') {
            containsZero = this.containsZero;
        }
        else {
            containsZero = b;
            if (this.checkFilter(c)) {
                final int n = this.table.length - 1;
                int n2 = 0;
                do {
                    containsZero = b;
                    if (this.table[n2] == '\0') {
                        return containsZero;
                    }
                    if (this.table[n2] == c) {
                        return true;
                    }
                } while ((n2 = (n2 + 1 & n)) != (n2 = (c & n)));
                return false;
            }
        }
        return containsZero;
    }
    
    @Override
    public CharMatcher precomputed() {
        return this;
    }
}
