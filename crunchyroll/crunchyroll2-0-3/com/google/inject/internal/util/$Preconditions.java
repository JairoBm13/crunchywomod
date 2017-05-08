// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

public final class $Preconditions
{
    public static void checkArgument(final boolean b) {
        if (!b) {
            throw new IllegalArgumentException();
        }
    }
    
    public static void checkArgument(final boolean b, final Object o) {
        if (!b) {
            throw new IllegalArgumentException(String.valueOf(o));
        }
    }
    
    public static void checkArgument(final boolean b, final String s, final Object... array) {
        if (!b) {
            throw new IllegalArgumentException(format(s, array));
        }
    }
    
    public static void checkElementIndex(final int n, final int n2) {
        checkElementIndex(n, n2, "index");
    }
    
    public static void checkElementIndex(final int n, final int n2, final String s) {
        checkArgument(n2 >= 0, "negative size: %s", n2);
        if (n < 0) {
            throw new IndexOutOfBoundsException(format("%s (%s) must not be negative", s, n));
        }
        if (n >= n2) {
            throw new IndexOutOfBoundsException(format("%s (%s) must be less than size (%s)", s, n, n2));
        }
    }
    
    public static <T> T checkNotNull(final T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return t;
    }
    
    public static <T> T checkNotNull(final T t, final Object o) {
        if (t == null) {
            throw new NullPointerException(String.valueOf(o));
        }
        return t;
    }
    
    public static void checkPositionIndex(final int n, final int n2) {
        checkPositionIndex(n, n2, "index");
    }
    
    public static void checkPositionIndex(final int n, final int n2, final String s) {
        checkArgument(n2 >= 0, "negative size: %s", n2);
        if (n < 0) {
            throw new IndexOutOfBoundsException(format("%s (%s) must not be negative", s, n));
        }
        if (n > n2) {
            throw new IndexOutOfBoundsException(format("%s (%s) must not be greater than size (%s)", s, n, n2));
        }
    }
    
    public static void checkPositionIndexes(final int n, final int n2, final int n3) {
        checkPositionIndex(n, n3, "start index");
        checkPositionIndex(n2, n3, "end index");
        if (n2 < n) {
            throw new IndexOutOfBoundsException(format("end index (%s) must not be less than start index (%s)", n2, n));
        }
    }
    
    public static void checkState(final boolean b) {
        if (!b) {
            throw new IllegalStateException();
        }
    }
    
    public static void checkState(final boolean b, final Object o) {
        if (!b) {
            throw new IllegalStateException(String.valueOf(o));
        }
    }
    
    public static void checkState(final boolean b, final String s, final Object... array) {
        if (!b) {
            throw new IllegalStateException(format(s, array));
        }
    }
    
    static String format(final String s, final Object... array) {
        final StringBuilder sb = new StringBuilder(s.length() + array.length * 16);
        int n = 0;
        int i;
        for (i = 0; i < array.length; ++i) {
            final int index = s.indexOf("%s", n);
            if (index == -1) {
                break;
            }
            sb.append(s.substring(n, index));
            sb.append(array[i]);
            n = index + 2;
        }
        sb.append(s.substring(n));
        if (i < array.length) {
            sb.append(" [");
            sb.append(array[i]);
            for (int j = i + 1; j < array.length; ++j) {
                sb.append(", ");
                sb.append(array[j]);
            }
            sb.append("]");
        }
        return sb.toString();
    }
}
