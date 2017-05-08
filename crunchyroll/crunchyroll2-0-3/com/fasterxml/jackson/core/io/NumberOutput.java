// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.io;

public final class NumberOutput
{
    private static int BILLION;
    static final char[] FULL_TRIPLETS;
    static final byte[] FULL_TRIPLETS_B;
    static final char[] LEADING_TRIPLETS;
    private static long MAX_INT_AS_LONG;
    private static int MILLION;
    private static long MIN_INT_AS_LONG;
    static final String SMALLEST_LONG;
    private static long TEN_BILLION_L;
    private static long THOUSAND_L;
    static final String[] sSmallIntStrs;
    static final String[] sSmallIntStrs2;
    
    static {
        NumberOutput.MILLION = 1000000;
        NumberOutput.BILLION = 1000000000;
        NumberOutput.TEN_BILLION_L = 10000000000L;
        NumberOutput.THOUSAND_L = 1000L;
        NumberOutput.MIN_INT_AS_LONG = -2147483648L;
        NumberOutput.MAX_INT_AS_LONG = 2147483647L;
        SMALLEST_LONG = String.valueOf(Long.MIN_VALUE);
        LEADING_TRIPLETS = new char[4000];
        FULL_TRIPLETS = new char[4000];
        int i = 0;
        int n = 0;
        while (i < 10) {
            final char c = (char)(i + 48);
            char c2;
            if (i == 0) {
                c2 = '\0';
            }
            else {
                c2 = c;
            }
            for (int j = 0; j < 10; ++j) {
                final char c3 = (char)(j + 48);
                char c4;
                if (i == 0 && j == 0) {
                    c4 = '\0';
                }
                else {
                    c4 = c3;
                }
                for (int k = 0; k < 10; ++k) {
                    final char c5 = (char)(k + 48);
                    NumberOutput.LEADING_TRIPLETS[n] = c2;
                    NumberOutput.LEADING_TRIPLETS[n + 1] = c4;
                    NumberOutput.LEADING_TRIPLETS[n + 2] = c5;
                    NumberOutput.FULL_TRIPLETS[n] = c;
                    NumberOutput.FULL_TRIPLETS[n + 1] = c3;
                    NumberOutput.FULL_TRIPLETS[n + 2] = c5;
                    n += 4;
                }
            }
            ++i;
        }
        FULL_TRIPLETS_B = new byte[4000];
        for (int l = 0; l < 4000; ++l) {
            NumberOutput.FULL_TRIPLETS_B[l] = (byte)NumberOutput.FULL_TRIPLETS[l];
        }
        sSmallIntStrs = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
        sSmallIntStrs2 = new String[] { "-1", "-2", "-3", "-4", "-5", "-6", "-7", "-8", "-9", "-10" };
    }
    
    private static int calcLongStrLength(final long n) {
        int n2 = 10;
        for (long ten_BILLION_L = NumberOutput.TEN_BILLION_L; n >= ten_BILLION_L && n2 != 19; ++n2, ten_BILLION_L = (ten_BILLION_L << 1) + (ten_BILLION_L << 3)) {}
        return n2;
    }
    
    private static int outputFullTriplet(int n, final byte[] array, int n2) {
        final int n3 = n << 2;
        n = n2 + 1;
        final byte[] full_TRIPLETS_B = NumberOutput.FULL_TRIPLETS_B;
        final int n4 = n3 + 1;
        array[n2] = full_TRIPLETS_B[n3];
        n2 = n + 1;
        array[n] = NumberOutput.FULL_TRIPLETS_B[n4];
        array[n2] = NumberOutput.FULL_TRIPLETS_B[n4 + 1];
        return n2 + 1;
    }
    
    private static int outputFullTriplet(int n, final char[] array, int n2) {
        final int n3 = n << 2;
        n = n2 + 1;
        final char[] full_TRIPLETS = NumberOutput.FULL_TRIPLETS;
        final int n4 = n3 + 1;
        array[n2] = full_TRIPLETS[n3];
        n2 = n + 1;
        array[n] = NumberOutput.FULL_TRIPLETS[n4];
        array[n2] = NumberOutput.FULL_TRIPLETS[n4 + 1];
        return n2 + 1;
    }
    
    public static int outputInt(int n, final byte[] array, int n2) {
        int n3 = n;
        int n4 = n2;
        if (n < 0) {
            if (n == Integer.MIN_VALUE) {
                return outputLong(n, array, n2);
            }
            array[n2] = 45;
            n3 = -n;
            n4 = n2 + 1;
        }
        if (n3 >= NumberOutput.MILLION) {
            boolean b;
            if (n3 >= NumberOutput.BILLION) {
                b = true;
            }
            else {
                b = false;
            }
            n = n3;
            n2 = n4;
            if (b) {
                n = n3 - NumberOutput.BILLION;
                if (n >= NumberOutput.BILLION) {
                    n -= NumberOutput.BILLION;
                    array[n4] = 50;
                    n2 = n4 + 1;
                }
                else {
                    array[n4] = 49;
                    n2 = n4 + 1;
                }
            }
            final int n5 = n / 1000;
            final int n6 = n5 / 1000;
            if (b) {
                n2 = outputFullTriplet(n6, array, n2);
            }
            else {
                n2 = outputLeadingTriplet(n6, array, n2);
            }
            return outputFullTriplet(n - n5 * 1000, array, outputFullTriplet(n5 - n6 * 1000, array, n2));
        }
        if (n3 >= 1000) {
            n = n3 / 1000;
            return outputFullTriplet(n3 - n * 1000, array, outputLeadingTriplet(n, array, n4));
        }
        if (n3 < 10) {
            array[n4] = (byte)(n3 + 48);
            return n4 + 1;
        }
        return outputLeadingTriplet(n3, array, n4);
    }
    
    public static int outputInt(int n, final char[] array, int n2) {
        int n3 = n;
        int n4 = n2;
        if (n < 0) {
            if (n == Integer.MIN_VALUE) {
                return outputLong(n, array, n2);
            }
            array[n2] = '-';
            n3 = -n;
            n4 = n2 + 1;
        }
        if (n3 >= NumberOutput.MILLION) {
            boolean b;
            if (n3 >= NumberOutput.BILLION) {
                b = true;
            }
            else {
                b = false;
            }
            n = n3;
            n2 = n4;
            if (b) {
                n = n3 - NumberOutput.BILLION;
                if (n >= NumberOutput.BILLION) {
                    n -= NumberOutput.BILLION;
                    array[n4] = '2';
                    n2 = n4 + 1;
                }
                else {
                    array[n4] = '1';
                    n2 = n4 + 1;
                }
            }
            final int n5 = n / 1000;
            final int n6 = n5 / 1000;
            if (b) {
                n2 = outputFullTriplet(n6, array, n2);
            }
            else {
                n2 = outputLeadingTriplet(n6, array, n2);
            }
            return outputFullTriplet(n - n5 * 1000, array, outputFullTriplet(n5 - n6 * 1000, array, n2));
        }
        if (n3 >= 1000) {
            n = n3 / 1000;
            return outputFullTriplet(n3 - n * 1000, array, outputLeadingTriplet(n, array, n4));
        }
        if (n3 < 10) {
            array[n4] = (char)(n3 + 48);
            return n4 + 1;
        }
        return outputLeadingTriplet(n3, array, n4);
    }
    
    private static int outputLeadingTriplet(int n, final byte[] array, int n2) {
        n <<= 2;
        final char[] leading_TRIPLETS = NumberOutput.LEADING_TRIPLETS;
        final int n3 = n + 1;
        final char c = leading_TRIPLETS[n];
        n = n2;
        if (c != '\0') {
            array[n2] = (byte)c;
            n = n2 + 1;
        }
        final char c2 = NumberOutput.LEADING_TRIPLETS[n3];
        n2 = n;
        if (c2 != '\0') {
            array[n] = (byte)c2;
            n2 = n + 1;
        }
        array[n2] = (byte)NumberOutput.LEADING_TRIPLETS[n3 + 1];
        return n2 + 1;
    }
    
    private static int outputLeadingTriplet(int n, final char[] array, int n2) {
        n <<= 2;
        final char[] leading_TRIPLETS = NumberOutput.LEADING_TRIPLETS;
        final int n3 = n + 1;
        final char c = leading_TRIPLETS[n];
        n = n2;
        if (c != '\0') {
            array[n2] = c;
            n = n2 + 1;
        }
        final char c2 = NumberOutput.LEADING_TRIPLETS[n3];
        n2 = n;
        if (c2 != '\0') {
            array[n] = c2;
            n2 = n + 1;
        }
        array[n2] = NumberOutput.LEADING_TRIPLETS[n3 + 1];
        return n2 + 1;
    }
    
    public static int outputLong(long n, final byte[] array, int i) {
        long n2 = 0L;
        int n3 = 0;
        Label_0094: {
            if (n < 0L) {
                int outputInt;
                if (n > NumberOutput.MIN_INT_AS_LONG) {
                    outputInt = outputInt((int)n, array, i);
                }
                else {
                    if (n != Long.MIN_VALUE) {
                        array[i] = 45;
                        n2 = -n;
                        n3 = i + 1;
                        break Label_0094;
                    }
                    final int length = NumberOutput.SMALLEST_LONG.length();
                    int n4 = 0;
                    while (true) {
                        outputInt = i;
                        if (n4 >= length) {
                            break;
                        }
                        array[i] = (byte)NumberOutput.SMALLEST_LONG.charAt(n4);
                        ++n4;
                        ++i;
                    }
                }
                return outputInt;
            }
            n2 = n;
            n3 = i;
            if (n <= NumberOutput.MAX_INT_AS_LONG) {
                return outputInt((int)n, array, i);
            }
        }
        final int n5 = i = n3 + calcLongStrLength(n2);
        while (n2 > NumberOutput.MAX_INT_AS_LONG) {
            i -= 3;
            n = n2 / NumberOutput.THOUSAND_L;
            outputFullTriplet((int)(n2 - NumberOutput.THOUSAND_L * n), array, i);
            n2 = n;
        }
        final int n6 = (int)n2;
        int n7 = i;
        int n8;
        for (i = n6; i >= 1000; i = n8) {
            n7 -= 3;
            n8 = i / 1000;
            outputFullTriplet(i - n8 * 1000, array, n7);
        }
        outputLeadingTriplet(i, array, n3);
        return n5;
    }
    
    public static int outputLong(long n, final char[] array, int i) {
        long n2;
        int n3;
        if (n < 0L) {
            if (n > NumberOutput.MIN_INT_AS_LONG) {
                return outputInt((int)n, array, i);
            }
            if (n == Long.MIN_VALUE) {
                final int length = NumberOutput.SMALLEST_LONG.length();
                NumberOutput.SMALLEST_LONG.getChars(0, length, array, i);
                return i + length;
            }
            array[i] = '-';
            n2 = -n;
            n3 = i + 1;
        }
        else {
            n2 = n;
            n3 = i;
            if (n <= NumberOutput.MAX_INT_AS_LONG) {
                return outputInt((int)n, array, i);
            }
        }
        final int n4 = i = n3 + calcLongStrLength(n2);
        while (n2 > NumberOutput.MAX_INT_AS_LONG) {
            i -= 3;
            n = n2 / NumberOutput.THOUSAND_L;
            outputFullTriplet((int)(n2 - NumberOutput.THOUSAND_L * n), array, i);
            n2 = n;
        }
        final int n5 = (int)n2;
        int n6 = i;
        int n7;
        for (i = n5; i >= 1000; i = n7) {
            n6 -= 3;
            n7 = i / 1000;
            outputFullTriplet(i - n7 * 1000, array, n6);
        }
        outputLeadingTriplet(i, array, n3);
        return n4;
    }
    
    public static String toString(final double n) {
        return Double.toString(n);
    }
    
    public static String toString(final int n) {
        if (n < NumberOutput.sSmallIntStrs.length) {
            if (n >= 0) {
                return NumberOutput.sSmallIntStrs[n];
            }
            final int n2 = -n - 1;
            if (n2 < NumberOutput.sSmallIntStrs2.length) {
                return NumberOutput.sSmallIntStrs2[n2];
            }
        }
        return Integer.toString(n);
    }
    
    public static String toString(final long n) {
        if (n <= 2147483647L && n >= -2147483648L) {
            return toString((int)n);
        }
        return Long.toString(n);
    }
}
