// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.util;

import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Date;
import java.util.TimeZone;

public class ISO8601Utils
{
    private static final TimeZone TIMEZONE_GMT;
    
    static {
        TIMEZONE_GMT = TimeZone.getTimeZone("GMT");
    }
    
    private static void checkOffset(final String s, final int n, final char c) throws IndexOutOfBoundsException {
        final char char1 = s.charAt(n);
        if (char1 != c) {
            throw new IndexOutOfBoundsException("Expected '" + c + "' character but found '" + char1 + "'");
        }
    }
    
    public static String format(final Date date) {
        return format(date, false, ISO8601Utils.TIMEZONE_GMT);
    }
    
    public static String format(final Date time, final boolean b, final TimeZone timeZone) {
        final GregorianCalendar gregorianCalendar = new GregorianCalendar(timeZone, Locale.US);
        gregorianCalendar.setTime(time);
        final int length = "yyyy-MM-ddThh:mm:ss".length();
        int length2;
        if (b) {
            length2 = ".sss".length();
        }
        else {
            length2 = 0;
        }
        int n;
        if (timeZone.getRawOffset() == 0) {
            n = "Z".length();
        }
        else {
            n = "+hh:mm".length();
        }
        final StringBuilder sb = new StringBuilder(n + (length + length2));
        padInt(sb, gregorianCalendar.get(1), "yyyy".length());
        sb.append('-');
        padInt(sb, gregorianCalendar.get(2) + 1, "MM".length());
        sb.append('-');
        padInt(sb, gregorianCalendar.get(5), "dd".length());
        sb.append('T');
        padInt(sb, gregorianCalendar.get(11), "hh".length());
        sb.append(':');
        padInt(sb, gregorianCalendar.get(12), "mm".length());
        sb.append(':');
        padInt(sb, gregorianCalendar.get(13), "ss".length());
        if (b) {
            sb.append('.');
            padInt(sb, gregorianCalendar.get(14), "sss".length());
        }
        final int offset = timeZone.getOffset(gregorianCalendar.getTimeInMillis());
        if (offset != 0) {
            final int abs = Math.abs(offset / 60000 / 60);
            final int abs2 = Math.abs(offset / 60000 % 60);
            char c;
            if (offset < 0) {
                c = '-';
            }
            else {
                c = '+';
            }
            sb.append(c);
            padInt(sb, abs, "hh".length());
            sb.append(':');
            padInt(sb, abs2, "mm".length());
        }
        else {
            sb.append('Z');
        }
        return sb.toString();
    }
    
    private static void padInt(final StringBuilder sb, int i, final int n) {
        final String string = Integer.toString(i);
        for (i = n - string.length(); i > 0; --i) {
            sb.append('0');
        }
        sb.append(string);
    }
    
    public static Date parse(final String s) {
        while (true) {
            while (true) {
                try {
                    parseInt(s, 0, 4);
                    checkOffset(s, 4, '-');
                    parseInt(s, 5, 7);
                    checkOffset(s, 7, '-');
                    parseInt(s, 8, 10);
                    checkOffset(s, 10, 'T');
                    parseInt(s, 11, 13);
                    checkOffset(s, 13, ':');
                    parseInt(s, 14, 16);
                    checkOffset(s, 16, ':');
                    parseInt(s, 17, 19);
                    if (s.charAt(19) == '.') {
                        checkOffset(s, 19, '.');
                        parseInt(s, 20, 23);
                        final int n = 23;
                        final char char1 = s.charAt(n);
                        if (char1 != '+' && char1 != '-') {
                            goto Label_0229;
                        }
                        final String string = "GMT" + s.substring(n);
                        if (!TimeZone.getTimeZone(string).getID().equals(string)) {
                            throw new IndexOutOfBoundsException();
                        }
                        goto Label_0300;
                    }
                }
                catch (IndexOutOfBoundsException ex) {
                    throw new IllegalArgumentException("Failed to parse date " + s, ex);
                }
                catch (NumberFormatException ex2) {
                    throw new IllegalArgumentException("Failed to parse date " + s, ex2);
                }
                catch (IllegalArgumentException ex3) {
                    throw new IllegalArgumentException("Failed to parse date " + s, ex3);
                }
                final int n = 19;
                continue;
            }
        }
    }
    
    private static int parseInt(final String s, int digit, final int n) throws NumberFormatException {
        if (digit < 0 || n > s.length() || digit > n) {
            throw new NumberFormatException(s);
        }
        int n2 = 0;
        int i;
        if ((i = digit) < n) {
            final int digit2 = Character.digit(s.charAt(digit), 10);
            if (digit2 < 0) {
                throw new NumberFormatException("Invalid number: " + s);
            }
            n2 = -digit2;
            i = digit + 1;
        }
        while (i < n) {
            digit = Character.digit(s.charAt(i), 10);
            if (digit < 0) {
                throw new NumberFormatException("Invalid number: " + s);
            }
            n2 = n2 * 10 - digit;
            ++i;
        }
        return -n2;
    }
}
