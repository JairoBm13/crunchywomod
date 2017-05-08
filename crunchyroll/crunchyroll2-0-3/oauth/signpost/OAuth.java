// 
// Decompiled by Procyon v0.5.30
// 

package oauth.signpost;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import oauth.signpost.http.HttpParameters;
import java.io.InputStream;
import com.google.gdata.util.common.base.PercentEscaper;

public class OAuth
{
    private static final PercentEscaper percentEncoder;
    
    static {
        percentEncoder = new PercentEscaper("-._~", false);
    }
    
    public static void debugOut(final String s, final String s2) {
        if (System.getProperty("debug") != null) {
            System.out.println("[SIGNPOST] " + s + ": " + s2);
        }
    }
    
    public static HttpParameters decodeForm(final InputStream inputStream) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuilder sb = new StringBuilder();
        for (String s = bufferedReader.readLine(); s != null; s = bufferedReader.readLine()) {
            sb.append(s);
        }
        return decodeForm(sb.toString());
    }
    
    public static HttpParameters decodeForm(String s) {
        final HttpParameters httpParameters = new HttpParameters();
        if (!isEmpty(s)) {
            final String[] split = s.split("\\&");
            for (int length = split.length, i = 0; i < length; ++i) {
                final String s2 = split[i];
                final int index = s2.indexOf(61);
                String percentDecode;
                if (index < 0) {
                    s = percentDecode(s2);
                    percentDecode = null;
                }
                else {
                    s = percentDecode(s2.substring(0, index));
                    percentDecode = percentDecode(s2.substring(index + 1));
                }
                httpParameters.put(s, percentDecode);
            }
        }
        return httpParameters;
    }
    
    public static boolean isEmpty(final String s) {
        return s == null || s.length() == 0;
    }
    
    public static HttpParameters oauthHeaderToParamsMap(final String s) {
        final HttpParameters httpParameters = new HttpParameters();
        if (s != null && s.startsWith("OAuth ")) {
            final String[] split = s.substring("OAuth ".length()).split(",");
            for (int length = split.length, i = 0; i < length; ++i) {
                final String[] split2 = split[i].split("=");
                httpParameters.put(split2[0].trim(), split2[1].replace("\"", "").trim());
            }
        }
        return httpParameters;
    }
    
    public static String percentDecode(String decode) {
        if (decode == null) {
            return "";
        }
        try {
            decode = URLDecoder.decode(decode, "UTF-8");
            return decode;
        }
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
    
    public static String percentEncode(final String s) {
        if (s == null) {
            return "";
        }
        return OAuth.percentEncoder.escape(s);
    }
}
