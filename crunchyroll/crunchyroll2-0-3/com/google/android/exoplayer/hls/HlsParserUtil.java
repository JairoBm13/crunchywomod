// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.hls;

import java.util.regex.Matcher;
import com.google.android.exoplayer.ParserException;
import java.util.regex.Pattern;

final class HlsParserUtil
{
    public static Pattern compileBooleanAttrPattern(final String s) {
        return Pattern.compile(s + "=(" + "YES" + "|" + "NO" + ")");
    }
    
    public static double parseDoubleAttr(final String s, final Pattern pattern, final String s2) throws ParserException {
        return Double.parseDouble(parseStringAttr(s, pattern, s2));
    }
    
    public static int parseIntAttr(final String s, final Pattern pattern, final String s2) throws ParserException {
        return Integer.parseInt(parseStringAttr(s, pattern, s2));
    }
    
    public static boolean parseOptionalBooleanAttr(final String s, final Pattern pattern) {
        final Matcher matcher = pattern.matcher(s);
        return matcher.find() && "YES".equals(matcher.group(1));
    }
    
    public static String parseOptionalStringAttr(final String s, final Pattern pattern) {
        final Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    
    public static String parseStringAttr(final String s, final Pattern pattern, final String s2) throws ParserException {
        final Matcher matcher = pattern.matcher(s);
        if (matcher.find() && matcher.groupCount() == 1) {
            return matcher.group(1);
        }
        throw new ParserException("Couldn't match " + s2 + " tag in " + s);
    }
}
