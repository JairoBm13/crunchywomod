// 
// Decompiled by Procyon v0.5.30
// 

package org.json.simple;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

public class JSONObject extends HashMap implements Map, JSONAware
{
    private static String toJSONString(final String s, final Object o, final StringBuffer sb) {
        sb.append('\"');
        if (s == null) {
            sb.append("null");
        }
        else {
            JSONValue.escape(s, sb);
        }
        sb.append('\"').append(':');
        sb.append(JSONValue.toJSONString(o));
        return sb.toString();
    }
    
    public static String toJSONString(final Map map) {
        if (map == null) {
            return "null";
        }
        final StringBuffer sb = new StringBuffer();
        int n = 1;
        final Iterator<Entry<Object, V>> iterator = map.entrySet().iterator();
        sb.append('{');
        while (iterator.hasNext()) {
            if (n != 0) {
                n = 0;
            }
            else {
                sb.append(',');
            }
            final Entry<Object, V> entry = iterator.next();
            toJSONString(String.valueOf(entry.getKey()), entry.getValue(), sb);
        }
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public String toJSONString() {
        return toJSONString(this);
    }
    
    @Override
    public String toString() {
        return this.toJSONString();
    }
}
