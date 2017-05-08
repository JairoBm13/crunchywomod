// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.regex.Matcher;
import android.text.TextUtils;
import java.util.regex.Pattern;

public final class zzlh
{
    private static final Pattern zzacL;
    private static final Pattern zzacM;
    
    static {
        zzacL = Pattern.compile("\\\\.");
        zzacM = Pattern.compile("[\\\\\"/\b\f\n\r\t]");
    }
    
    public static String zzcr(final String s) {
        if (!TextUtils.isEmpty((CharSequence)s)) {
            final Matcher matcher = zzlh.zzacM.matcher(s);
            StringBuffer sb = null;
            while (matcher.find()) {
                StringBuffer sb2;
                if ((sb2 = sb) == null) {
                    sb2 = new StringBuffer();
                }
                switch (matcher.group().charAt(0)) {
                    default: {
                        sb = sb2;
                        continue;
                    }
                    case '\b': {
                        matcher.appendReplacement(sb2, "\\\\b");
                        sb = sb2;
                        continue;
                    }
                    case '\"': {
                        matcher.appendReplacement(sb2, "\\\\\\\"");
                        sb = sb2;
                        continue;
                    }
                    case '\\': {
                        matcher.appendReplacement(sb2, "\\\\\\\\");
                        sb = sb2;
                        continue;
                    }
                    case '/': {
                        matcher.appendReplacement(sb2, "\\\\/");
                        sb = sb2;
                        continue;
                    }
                    case '\f': {
                        matcher.appendReplacement(sb2, "\\\\f");
                        sb = sb2;
                        continue;
                    }
                    case '\n': {
                        matcher.appendReplacement(sb2, "\\\\n");
                        sb = sb2;
                        continue;
                    }
                    case '\r': {
                        matcher.appendReplacement(sb2, "\\\\r");
                        sb = sb2;
                        continue;
                    }
                    case '\t': {
                        matcher.appendReplacement(sb2, "\\\\t");
                        sb = sb2;
                        continue;
                    }
                }
            }
            if (sb != null) {
                matcher.appendTail(sb);
                return sb.toString();
            }
        }
        return s;
    }
    
    public static boolean zzd(final Object o, final Object o2) {
        final boolean b = false;
        boolean b2;
        if (o == null && o2 == null) {
            b2 = true;
        }
        else {
            b2 = b;
            if (o != null) {
                b2 = b;
                if (o2 != null) {
                    Label_0131: {
                        if (!(o instanceof JSONObject) || !(o2 instanceof JSONObject)) {
                            break Label_0131;
                        }
                        final JSONObject jsonObject = (JSONObject)o;
                        final JSONObject jsonObject2 = (JSONObject)o2;
                        b2 = b;
                        if (jsonObject.length() != jsonObject2.length()) {
                            return b2;
                        }
                        final Iterator keys = jsonObject.keys();
                    Block_15_Outer:
                        while (true) {
                            if (!keys.hasNext()) {
                                return true;
                            }
                            final String s = keys.next();
                            b2 = b;
                            if (!jsonObject2.has(s)) {
                                return b2;
                            }
                            try {
                                if (!zzd(jsonObject.get(s), jsonObject2.get(s))) {
                                    return false;
                                }
                                continue Block_15_Outer;
                                while (true) {
                                    Label_0171: {
                                        try {
                                            final JSONArray jsonArray;
                                            final int n;
                                            final JSONArray jsonArray2;
                                            final boolean zzd = zzd(jsonArray.get(n), jsonArray2.get(n));
                                            b2 = b;
                                            if (zzd) {
                                                ++n;
                                                break Label_0171;
                                            }
                                            return b2;
                                            Label_0209: {
                                                return true;
                                            }
                                            Label_0211:
                                            return o.equals(o2);
                                        }
                                        catch (JSONException ex) {
                                            return false;
                                        }
                                        Block_13: {
                                            break Block_13;
                                        }
                                        final JSONArray jsonArray = (JSONArray)o;
                                        final JSONArray jsonArray2 = (JSONArray)o2;
                                        b2 = b;
                                        int n = 0;
                                    }
                                    continue;
                                }
                            }
                            // iftrue(Label_0211:, !o instanceof JSONArray || !o2 instanceof JSONArray)
                            // iftrue(Label_0013:, jsonArray.length() != jsonArray2.length())
                            // iftrue(Label_0209:, n >= jsonArray.length())
                            catch (JSONException ex2) {
                                return false;
                            }
                            break;
                        }
                    }
                }
            }
        }
        Label_0013: {
            return b2;
        }
    }
}
