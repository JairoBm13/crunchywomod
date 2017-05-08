// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.util;

import java.util.regex.Pattern;
import java.text.Normalizer;
import com.google.common.base.Joiner;

public class UrlUtils
{
    public static String getEventUri(String urlEncode, String urlEncode2) {
        urlEncode = urlEncode(urlEncode);
        urlEncode2 = urlEncode(urlEncode2);
        return urlEncode + "-episode-" + urlEncode2;
    }
    
    public static String getEventUriFromComponents(final String[] array) {
        for (int i = 0; i < array.length; ++i) {
            array[i] = urlEncode(array[i]);
        }
        return Joiner.on('-').join(array);
    }
    
    public static String getScreenUriFromComponents(final String[] array) {
        for (int i = 0; i < array.length; ++i) {
            array[i] = urlEncode(array[i]);
        }
        return Joiner.on('/').join(array);
    }
    
    public static String urlEncode(String normalize) {
        if (normalize == null) {
            throw new NullPointerException("Cannot url-encode a null string");
        }
        normalize = Normalizer.normalize(normalize.toLowerCase(), Normalizer.Form.NFD);
        return Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(normalize).replaceAll("").replaceAll(" ", "-").replaceAll("[^a-z0-9-]+", "").replaceAll("[-]+", "-");
    }
}
