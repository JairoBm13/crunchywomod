// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import java.io.OutputStream;
import java.io.FileOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Iterator;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import java.io.File;
import java.util.Map;

public class ab
{
    private Map<String, a> a;
    private File b;
    private boolean c;
    
    public ab(final Context context) {
        this.c = true;
        this.b = context.getFilesDir();
        this.a = new HashMap<String, a>();
        this.d();
    }
    
    private void c() {
        final ArrayList<a> list = new ArrayList<a>();
        final long timeInMillis = new GregorianCalendar().getTimeInMillis();
        for (final a a : this.a.values()) {
            if (timeInMillis - a.d >= a.c) {
                list.add(a);
            }
        }
        for (final a a2 : list) {
            ac.e("Cleaning cookie: " + a2.toString());
            this.a.remove(a2.a);
        }
    }
    
    private void d() {
        ac.e("Loading cookies...");
        final File absoluteFile = x.a(this.b, "cookies.cookies").getAbsoluteFile();
        while (true) {
            if (!absoluteFile.exists()) {
                break Label_0195;
            }
            final String a = ac.a(absoluteFile);
            try {
                final JSONArray jsonArray = new JSONObject(a).getJSONArray("cookies");
                final HashMap a2 = new HashMap<String, a>(jsonArray.length());
                for (int i = 0; i < jsonArray.length(); ++i) {
                    final JSONObject jsonObject = jsonArray.getJSONObject(i);
                    final a a3 = new a();
                    a3.a = jsonObject.getString("key");
                    a3.b = jsonObject.getString("value");
                    a3.c = jsonObject.getLong("lifeTimeMS");
                    a3.d = jsonObject.getLong("localTimeMS");
                    a3.e = jsonObject.getLong("serverTimeMS");
                    ac.e("Loading cookie: " + a3.toString());
                    a2.put(a3.a, a3);
                }
                this.a = (Map<String, a>)a2;
                ac.e("Cookies loaded.");
            }
            catch (Exception ex) {
                ac.a(ex);
                continue;
            }
            break;
        }
    }
    
    public JSONArray a() {
        synchronized (this) {
            this.c();
            final JSONArray jsonArray = new JSONArray();
            for (final a a : this.a.values()) {
                try {
                    final JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", (Object)a.a);
                    jsonObject.put("value", (Object)a.b);
                    jsonArray.put((Object)jsonObject);
                }
                catch (Exception ex) {
                    ac.a(ex);
                }
            }
        }
        // monitorexit(this)
        return;
    }
    
    public void a(final a a) {
        while (true) {
            Label_0143: {
                synchronized (this) {
                    if (a.c <= 0L) {
                        if (this.a.containsKey(a.a)) {
                            ac.e("Removing cookie: " + a.toString());
                            this.a.remove(a.a);
                            this.c = true;
                        }
                    }
                    else {
                        if (this.a.containsKey(a.a)) {
                            break Label_0143;
                        }
                        ac.e("Adding cookie: " + a.toString());
                        this.a.put(a.a, a);
                        this.c = true;
                    }
                    return;
                }
            }
            final a a2;
            if (this.a.get(a2.a).e < a2.e) {
                ac.e("Updating cookie: " + a2.toString());
                this.a.remove(a2.a);
                this.a.put(a2.a, a2);
                this.c = true;
            }
        }
    }
    
    public void a(final JSONArray jsonArray, final long e) {
        synchronized (this) {
            final long timeInMillis = new GregorianCalendar().getTimeInMillis();
            int i = 0;
            while (i < jsonArray.length()) {
                try {
                    final JSONObject jsonObject = jsonArray.getJSONObject(i);
                    final a a = new a();
                    a.a = jsonObject.getString("name");
                    a.b = jsonObject.getString("value");
                    a.c = jsonObject.getLong("lifetime");
                    a.e = e;
                    a.d = timeInMillis;
                    this.a(a);
                    ++i;
                }
                catch (Exception ex) {
                    ac.a(ex);
                }
            }
        }
    }
    // monitorexit(this)
    
    public void b() {
        while (true) {
            Object o;
            synchronized (this) {
                ac.e("Saving cookies...");
                if (!this.c) {
                    ac.e("No changes in cookies detected, no need to save.");
                    return;
                }
                this.c = false;
                final JSONObject jsonObject = new JSONObject();
                o = new JSONArray();
                for (final a a : this.a.values()) {
                    try {
                        final JSONObject jsonObject2 = new JSONObject();
                        jsonObject2.put("key", (Object)a.a);
                        jsonObject2.put("value", (Object)a.b);
                        jsonObject2.put("localTimeMS", a.d);
                        jsonObject2.put("serverTimeMS", a.e);
                        jsonObject2.put("lifeTimeMS", a.c);
                        ((JSONArray)o).put((Object)jsonObject2);
                    }
                    catch (Exception ex) {
                        ac.a(ex);
                    }
                }
            }
            try {
                final File absoluteFile = x.a(this.b, "cookies.cookies").getAbsoluteFile();
                if (absoluteFile.exists()) {
                    absoluteFile.delete();
                }
                absoluteFile.createNewFile();
                final JSONObject jsonObject3;
                jsonObject3.put("cookies", o);
                final String string = jsonObject3.toString();
                o = new FileOutputStream(absoluteFile);
                ((FileOutputStream)o).write(string.getBytes());
                ((OutputStream)o).flush();
                ((FileOutputStream)o).close();
                ac.e("Cookies saved.");
            }
            catch (Exception ex2) {
                try {
                    ac.a(ex2);
                }
                catch (Exception ex3) {
                    ac.a(ex3);
                }
            }
        }
    }
    
    public class a
    {
        public String a;
        public String b;
        public long c;
        public long d;
        public long e;
        
        @Override
        public String toString() {
            return "\"" + this.a + "\" - \"" + this.b + "\" - " + this.c + "ms, " + this.d + "ms, " + this.e + "ms";
        }
    }
}
