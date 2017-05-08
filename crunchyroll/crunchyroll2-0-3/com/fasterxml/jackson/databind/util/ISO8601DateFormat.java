// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.util;

import java.text.ParsePosition;
import java.text.FieldPosition;
import java.util.Date;
import java.text.DecimalFormat;
import java.util.GregorianCalendar;
import java.text.NumberFormat;
import java.util.Calendar;
import java.text.DateFormat;

public class ISO8601DateFormat extends DateFormat
{
    private static Calendar CALENDAR;
    private static NumberFormat NUMBER_FORMAT;
    
    static {
        ISO8601DateFormat.CALENDAR = new GregorianCalendar();
        ISO8601DateFormat.NUMBER_FORMAT = new DecimalFormat();
    }
    
    public ISO8601DateFormat() {
        this.numberFormat = ISO8601DateFormat.NUMBER_FORMAT;
        this.calendar = ISO8601DateFormat.CALENDAR;
    }
    
    @Override
    public Object clone() {
        return this;
    }
    
    @Override
    public StringBuffer format(final Date date, final StringBuffer sb, final FieldPosition fieldPosition) {
        sb.append(ISO8601Utils.format(date));
        return sb;
    }
    
    @Override
    public Date parse(final String s, final ParsePosition parsePosition) {
        parsePosition.setIndex(s.length());
        return ISO8601Utils.parse(s);
    }
}
