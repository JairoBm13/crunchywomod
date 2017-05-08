// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.util;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import com.fasterxml.jackson.core.io.NumberInput;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.FieldPosition;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.text.DateFormat;

public class StdDateFormat extends DateFormat
{
    protected static final String[] ALL_FORMATS;
    protected static final DateFormat DATE_FORMAT_ISO8601;
    protected static final DateFormat DATE_FORMAT_ISO8601_Z;
    protected static final DateFormat DATE_FORMAT_PLAIN;
    protected static final DateFormat DATE_FORMAT_RFC1123;
    private static final TimeZone DEFAULT_TIMEZONE;
    public static final StdDateFormat instance;
    protected transient DateFormat _formatISO8601;
    protected transient DateFormat _formatISO8601_z;
    protected transient DateFormat _formatPlain;
    protected transient DateFormat _formatRFC1123;
    protected transient TimeZone _timezone;
    
    static {
        ALL_FORMATS = new String[] { "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "EEE, dd MMM yyyy HH:mm:ss zzz", "yyyy-MM-dd" };
        DEFAULT_TIMEZONE = TimeZone.getTimeZone("GMT");
        (DATE_FORMAT_RFC1123 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US)).setTimeZone(StdDateFormat.DEFAULT_TIMEZONE);
        (DATE_FORMAT_ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")).setTimeZone(StdDateFormat.DEFAULT_TIMEZONE);
        (DATE_FORMAT_ISO8601_Z = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")).setTimeZone(StdDateFormat.DEFAULT_TIMEZONE);
        (DATE_FORMAT_PLAIN = new SimpleDateFormat("yyyy-MM-dd")).setTimeZone(StdDateFormat.DEFAULT_TIMEZONE);
        instance = new StdDateFormat();
    }
    
    public StdDateFormat() {
    }
    
    public StdDateFormat(final TimeZone timezone) {
        this._timezone = timezone;
    }
    
    private final DateFormat _cloneFormat(final DateFormat dateFormat) {
        return _cloneFormat(dateFormat, this._timezone);
    }
    
    private static final DateFormat _cloneFormat(DateFormat dateFormat, final TimeZone timeZone) {
        dateFormat = (DateFormat)dateFormat.clone();
        if (timeZone != null) {
            dateFormat.setTimeZone(timeZone);
        }
        return dateFormat;
    }
    
    public static DateFormat getISO8601Format(final TimeZone timeZone) {
        return _cloneFormat(StdDateFormat.DATE_FORMAT_ISO8601, timeZone);
    }
    
    private static final boolean hasTimeZone(final String s) {
        final int length = s.length();
        if (length >= 6) {
            final char char1 = s.charAt(length - 6);
            if (char1 != '+' && char1 != '-') {
                final char char2 = s.charAt(length - 5);
                if (char2 != '+' && char2 != '-') {
                    final char char3 = s.charAt(length - 3);
                    if (char3 != '+' && char3 != '-') {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public StdDateFormat clone() {
        return new StdDateFormat();
    }
    
    @Override
    public StringBuffer format(final Date date, final StringBuffer sb, final FieldPosition fieldPosition) {
        if (this._formatISO8601 == null) {
            this._formatISO8601 = this._cloneFormat(StdDateFormat.DATE_FORMAT_ISO8601);
        }
        return this._formatISO8601.format(date, sb, fieldPosition);
    }
    
    protected boolean looksLikeISO8601(final String s) {
        boolean b2;
        final boolean b = b2 = false;
        if (s.length() >= 5) {
            b2 = b;
            if (Character.isDigit(s.charAt(0))) {
                b2 = b;
                if (Character.isDigit(s.charAt(3))) {
                    b2 = b;
                    if (s.charAt(4) == '-') {
                        b2 = true;
                    }
                }
            }
        }
        return b2;
    }
    
    @Override
    public Date parse(String trim) throws ParseException {
        trim = trim.trim();
        final ParsePosition parsePosition = new ParsePosition(0);
        final Date parse = this.parse(trim, parsePosition);
        if (parse != null) {
            return parse;
        }
        final StringBuilder sb = new StringBuilder();
        final String[] all_FORMATS = StdDateFormat.ALL_FORMATS;
        for (int length = all_FORMATS.length, i = 0; i < length; ++i) {
            final String s = all_FORMATS[i];
            if (sb.length() > 0) {
                sb.append("\", \"");
            }
            else {
                sb.append('\"');
            }
            sb.append(s);
        }
        sb.append('\"');
        throw new ParseException(String.format("Can not parse date \"%s\": not compatible with any of standard forms (%s)", trim, sb.toString()), parsePosition.getErrorIndex());
    }
    
    @Override
    public Date parse(final String s, final ParsePosition parsePosition) {
        if (this.looksLikeISO8601(s)) {
            return this.parseAsISO8601(s, parsePosition);
        }
        int length = s.length();
        int n;
        while (true) {
            n = length - 1;
            if (n < 0) {
                break;
            }
            final char char1 = s.charAt(n);
            if (char1 >= '0') {
                length = n;
                if (char1 <= '9') {
                    continue;
                }
            }
            if (n > 0) {
                break;
            }
            length = n;
            if (char1 != '-') {
                break;
            }
        }
        if (n < 0 && NumberInput.inLongRange(s, false)) {
            return new Date(Long.parseLong(s));
        }
        return this.parseAsRFC1123(s, parsePosition);
    }
    
    protected Date parseAsISO8601(String s, final ParsePosition parsePosition) {
        final int length = s.length();
        final char char1 = s.charAt(length - 1);
        DateFormat formatISO8601_z;
        String string;
        if (length <= 10 && Character.isDigit(char1)) {
            final DateFormat dateFormat = formatISO8601_z = this._formatPlain;
            string = s;
            if (dateFormat == null) {
                formatISO8601_z = this._cloneFormat(StdDateFormat.DATE_FORMAT_PLAIN);
                this._formatPlain = formatISO8601_z;
                string = s;
            }
        }
        else if (char1 == 'Z') {
            DateFormat formatISO8601_z2;
            if ((formatISO8601_z2 = this._formatISO8601_z) == null) {
                formatISO8601_z2 = this._cloneFormat(StdDateFormat.DATE_FORMAT_ISO8601_Z);
                this._formatISO8601_z = formatISO8601_z2;
            }
            formatISO8601_z = formatISO8601_z2;
            string = s;
            if (s.charAt(length - 4) == ':') {
                final StringBuilder sb = new StringBuilder(s);
                sb.insert(length - 1, ".000");
                string = sb.toString();
                formatISO8601_z = formatISO8601_z2;
            }
        }
        else if (hasTimeZone(s)) {
            final char char2 = s.charAt(length - 3);
            String s2 = null;
            Label_0215: {
                if (char2 == ':') {
                    final StringBuilder sb2 = new StringBuilder(s);
                    sb2.delete(length - 3, length - 2);
                    s2 = sb2.toString();
                }
                else {
                    if (char2 != '+') {
                        s2 = s;
                        if (char2 != '-') {
                            break Label_0215;
                        }
                    }
                    s2 = s + "00";
                }
            }
            final int length2 = s2.length();
            s = s2;
            if (Character.isDigit(s2.charAt(length2 - 9))) {
                final StringBuilder sb3 = new StringBuilder(s2);
                sb3.insert(length2 - 5, ".000");
                s = sb3.toString();
            }
            formatISO8601_z = this._formatISO8601;
            string = s;
            if (this._formatISO8601 == null) {
                formatISO8601_z = this._cloneFormat(StdDateFormat.DATE_FORMAT_ISO8601);
                this._formatISO8601 = formatISO8601_z;
                string = s;
            }
        }
        else {
            final StringBuilder sb4 = new StringBuilder(s);
            if (length - s.lastIndexOf(84) - 1 <= 8) {
                sb4.append(".000");
            }
            sb4.append('Z');
            s = sb4.toString();
            final DateFormat dateFormat2 = formatISO8601_z = this._formatISO8601_z;
            string = s;
            if (dateFormat2 == null) {
                formatISO8601_z = this._cloneFormat(StdDateFormat.DATE_FORMAT_ISO8601_Z);
                this._formatISO8601_z = formatISO8601_z;
                string = s;
            }
        }
        return formatISO8601_z.parse(string, parsePosition);
    }
    
    protected Date parseAsRFC1123(final String s, final ParsePosition parsePosition) {
        if (this._formatRFC1123 == null) {
            this._formatRFC1123 = this._cloneFormat(StdDateFormat.DATE_FORMAT_RFC1123);
        }
        return this._formatRFC1123.parse(s, parsePosition);
    }
    
    @Override
    public void setTimeZone(final TimeZone timezone) {
        if (timezone != this._timezone) {
            this._formatRFC1123 = null;
            this._formatISO8601 = null;
            this._formatISO8601_z = null;
            this._formatPlain = null;
            this._timezone = timezone;
        }
    }
    
    public StdDateFormat withTimeZone(final TimeZone timeZone) {
        TimeZone default_TIMEZONE = timeZone;
        if (timeZone == null) {
            default_TIMEZONE = StdDateFormat.DEFAULT_TIMEZONE;
        }
        return new StdDateFormat(default_TIMEZONE);
    }
}
