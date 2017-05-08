// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b.a;

import java.util.HashMap;
import java.lang.reflect.Field;
import android.util.Log;
import java.util.SortedSet;
import java.util.Map;
import java.util.List;

public class e
{
    public List<Float> adCuePoints;
    public com.google.ads.interactivemedia.v3.b.a.a adData;
    public long adTimeUpdateMs;
    public String adUiStyle;
    public Map<String, c> companions;
    public int errorCode;
    public String errorMessage;
    public String innerError;
    public SortedSet<Float> internalCuePoints;
    public String ln;
    public a logData;
    public String m;
    public String n;
    public String translation;
    public String videoUrl;
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("JavaScriptMsgData[");
        final Field[] fields = e.class.getFields();
        final int length = fields.length;
        int i = 0;
    Label_0074_Outer:
        while (i < length) {
            final Field field = fields[i];
            while (true) {
                try {
                    final Object value = field.get(this);
                    sb.append(field.getName()).append(":");
                    sb.append(value).append(",");
                    ++i;
                    continue Label_0074_Outer;
                }
                catch (IllegalArgumentException ex) {
                    Log.e("IMASDK", "IllegalArgumentException occurred", (Throwable)ex);
                    continue;
                }
                catch (IllegalAccessException ex2) {
                    Log.e("IMASDK", "IllegalAccessException occurred", (Throwable)ex2);
                    continue;
                }
                break;
            }
            break;
        }
        return sb.append("]").toString();
    }
    
    public class a
    {
        public int errorCode;
        public String errorMessage;
        public String innerError;
        public String type;
        
        public Map<String, String> constructMap() {
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("type", this.type);
            hashMap.put("errorCode", String.valueOf(this.errorCode));
            hashMap.put("errorMessage", this.errorMessage);
            if (this.innerError != null) {
                hashMap.put("innerError", this.innerError);
            }
            return hashMap;
        }
        
        @Override
        public String toString() {
            return String.format("Log[type=%s, errorCode=%s, errorMessage=%s, innerError=%s]", this.type, this.errorCode, this.errorMessage, this.innerError);
        }
    }
}
