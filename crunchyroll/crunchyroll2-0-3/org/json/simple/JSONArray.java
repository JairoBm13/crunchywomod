// 
// Decompiled by Procyon v0.5.30
// 

package org.json.simple;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class JSONArray extends ArrayList implements List, JSONAware
{
    public static String toJSONString(final List list) {
        if (list == null) {
            return "null";
        }
        int n = 1;
        final StringBuffer sb = new StringBuffer();
        final Iterator<Object> iterator = list.iterator();
        sb.append('[');
        while (iterator.hasNext()) {
            if (n != 0) {
                n = 0;
            }
            else {
                sb.append(',');
            }
            final Object next = iterator.next();
            if (next == null) {
                sb.append("null");
            }
            else {
                sb.append(JSONValue.toJSONString(next));
            }
        }
        sb.append(']');
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
