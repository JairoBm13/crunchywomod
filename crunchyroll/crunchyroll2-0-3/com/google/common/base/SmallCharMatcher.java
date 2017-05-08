// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.base;

final class SmallCharMatcher extends CharMatcher
{
    private final boolean containsZero;
    final long filter;
    private final boolean reprobe;
    private final char[] table;
    
    private SmallCharMatcher(final char[] table, final long filter, final boolean containsZero, final boolean reprobe, final String s) {
        super(s);
        this.table = table;
        this.filter = filter;
        this.containsZero = containsZero;
        this.reprobe = reprobe;
    }
    
    static char[] buildTable(final int n, final char[] array, final boolean b) {
        final char[] array2 = new char[n];
        int n2 = 0;
        char[] array3;
        while (true) {
            array3 = array2;
            if (n2 >= array.length) {
                break;
            }
            final char c = array[n2];
            final int n3 = c % n;
            int n4;
            if ((n4 = n3) < 0) {
                n4 = n3 + n;
            }
            if (array2[n4] != '\0' && !b) {
                array3 = null;
                break;
            }
            int n5 = n4;
            if (b) {
                while (true) {
                    n5 = n4;
                    if (array2[n4] == '\0') {
                        break;
                    }
                    n4 = (n4 + 1) % n;
                }
            }
            array2[n5] = c;
            ++n2;
        }
        return array3;
    }
    
    private boolean checkFilter(final int n) {
        return 0x1L == (this.filter >> n & 0x1L);
    }
    
    static CharMatcher from(final char[] array, final String s) {
        long n = 0L;
        final int length = array.length;
        boolean b = false;
        final boolean b2 = array[0] == '\0';
        for (int length2 = array.length, i = 0; i < length2; ++i) {
            n |= 1L << array[i];
        }
        char[] buildTable = null;
        for (int j = length; j < 128; ++j) {
            buildTable = buildTable(j, array, false);
            if (buildTable != null) {
                break;
            }
        }
        char[] buildTable2;
        if ((buildTable2 = buildTable) == null) {
            buildTable2 = buildTable(128, array, true);
            b = true;
        }
        return new SmallCharMatcher(buildTable2, n, b2, b, s);
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
                final int n = c % this.table.length;
                int n2;
                if ((n2 = n) < 0) {
                    n2 = n + this.table.length;
                }
                while (true) {
                    containsZero = b;
                    if (this.table[n2] == '\0') {
                        return containsZero;
                    }
                    if (this.table[n2] == c) {
                        break;
                    }
                    containsZero = b;
                    if (!this.reprobe) {
                        return containsZero;
                    }
                    n2 = (n2 + 1) % this.table.length;
                }
                return true;
            }
        }
        return containsZero;
    }
    
    @Override
    public CharMatcher precomputed() {
        return this;
    }
}
