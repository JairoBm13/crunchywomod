// 
// Decompiled by Procyon v0.5.30
// 

package org.json.simple;

import java.util.List;
import java.util.Map;
import java.io.StringReader;
import org.json.simple.parser.JSONParser;
import java.io.Reader;

public class JSONValue
{
    public static String escape(final String s) {
        if (s == null) {
            return null;
        }
        final StringBuffer sb = new StringBuffer();
        escape(s, sb);
        return sb.toString();
    }
    
    static void escape(final String s, final StringBuffer sb) {
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            switch (char1) {
                default: {
                    if ((char1 >= '\0' && char1 <= '\u001f') || (char1 >= '\u007f' && char1 <= '\u009f') || (char1 >= '\u2000' && char1 <= '\u20ff')) {
                        final String hexString = Integer.toHexString(char1);
                        sb.append("\\u");
                        for (int j = 0; j < 4 - hexString.length(); ++j) {
                            sb.append('0');
                        }
                        sb.append(hexString.toUpperCase());
                        break;
                    }
                    sb.append(char1);
                    break;
                }
                case 34: {
                    sb.append("\\\"");
                    break;
                }
                case 92: {
                    sb.append("\\\\");
                    break;
                }
                case 8: {
                    sb.append("\\b");
                    break;
                }
                case 12: {
                    sb.append("\\f");
                    break;
                }
                case 10: {
                    sb.append("\\n");
                    break;
                }
                case 13: {
                    sb.append("\\r");
                    break;
                }
                case 9: {
                    sb.append("\\t");
                    break;
                }
                case 47: {
                    sb.append("\\/");
                    break;
                }
            }
        }
    }
    
    public static Object parse(final Reader reader) {
        try {
            return new JSONParser().parse(reader);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static Object parse(final String s) {
        return parse(new StringReader(s));
    }
    
    public static String toJSONString(final Object o) {
        if (o == null) {
            return "null";
        }
        if (o instanceof String) {
            return new StringBuffer().append("\"").append(escape((String)o)).append("\"").toString();
        }
        if (o instanceof Double) {
            if (((Double)o).isInfinite() || ((Double)o).isNaN()) {
                return "null";
            }
            return o.toString();
        }
        else if (o instanceof Float) {
            if (((Float)o).isInfinite() || ((Float)o).isNaN()) {
                return "null";
            }
            return o.toString();
        }
        else {
            if (o instanceof Number) {
                return o.toString();
            }
            if (o instanceof Boolean) {
                return o.toString();
            }
            if (o instanceof JSONAware) {
                return ((JSONAware)o).toJSONString();
            }
            if (o instanceof Map) {
                return JSONObject.toJSONString((Map)o);
            }
            if (o instanceof List) {
                return JSONArray.toJSONString((List)o);
            }
            return o.toString();
        }
    }
}
