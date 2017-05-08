// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.base;

public final class Preconditions
{
    private static String badElementIndex(final int n, final int n2, final String s) {
        if (n < 0) {
            return format("%s (%s) must not be negative", s, n);
        }
        if (n2 < 0) {
            throw new IllegalArgumentException("negative size: " + n2);
        }
        return format("%s (%s) must be less than size (%s)", s, n, n2);
    }
    
    private static String badPositionIndex(final int n, final int n2, final String s) {
        if (n < 0) {
            return format("%s (%s) must not be negative", s, n);
        }
        if (n2 < 0) {
            throw new IllegalArgumentException("negative size: " + n2);
        }
        return format("%s (%s) must not be greater than size (%s)", s, n, n2);
    }
    
    private static String badPositionIndexes(final int n, final int n2, final int n3) {
        if (n < 0 || n > n3) {
            return badPositionIndex(n, n3, "start index");
        }
        if (n2 < 0 || n2 > n3) {
            return badPositionIndex(n2, n3, "end index");
        }
        return format("end index (%s) must not be less than start index (%s)", n2, n);
    }
    
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
    
    public static int checkElementIndex(final int n, final int n2) {
        return checkElementIndex(n, n2, "index");
    }
    
    public static int checkElementIndex(final int n, final int n2, final String s) {
        if (n < 0 || n >= n2) {
            throw new IndexOutOfBoundsException(badElementIndex(n, n2, s));
        }
        return n;
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
    
    public static <T> T checkNotNull(final T t, final String s, final Object... array) {
        if (t == null) {
            throw new NullPointerException(format(s, array));
        }
        return t;
    }
    
    public static int checkPositionIndex(final int n, final int n2) {
        return checkPositionIndex(n, n2, "index");
    }
    
    public static int checkPositionIndex(final int n, final int n2, final String s) {
        if (n < 0 || n > n2) {
            throw new IndexOutOfBoundsException(badPositionIndex(n, n2, s));
        }
        return n;
    }
    
    public static void checkPositionIndexes(final int n, final int n2, final int n3) {
        if (n < 0 || n2 < n || n2 > n3) {
            throw new IndexOutOfBoundsException(badPositionIndexes(n, n2, n3));
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
    
    static String format(String value, final Object... array) {
        value = String.valueOf(value);
        final StringBuilder sb = new StringBuilder(value.length() + array.length * 16);
        int n = 0;
        int i;
        for (i = 0; i < array.length; ++i) {
            final int index = value.indexOf("%s", n);
            if (index == -1) {
                break;
            }
            sb.append(value.substring(n, index));
            sb.append(array[i]);
            n = index + 2;
        }
        sb.append(value.substring(n));
        if (i < array.length) {
            sb.append(" [");
            sb.append(array[i]);
            for (int j = i + 1; j < array.length; ++j) {
                sb.append(", ");
                sb.append(array[j]);
            }
            sb.append(']');
        }
        return sb.toString();
    }
}
