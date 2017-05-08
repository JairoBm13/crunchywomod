// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;

public class DateFormatter
{
    public static String format(final Date date) {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.US).format(date);
    }
    
    public static Date parse(final String s) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.US);
        try {
            return simpleDateFormat.parse(s);
        }
        catch (ParseException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
