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

public class bu
{
    private Map<String, a> a;
    private File b;
    private boolean c;
    private n d;
    
    public bu(final Context context) {
        this.c = true;
        this.b = context.getFilesDir();
        this.a = new HashMap<String, a>();
        this.c();
        this.b();
    }
    
    private void b() {
        final ArrayList<a> list = new ArrayList<a>();
        final long timeInMillis = new GregorianCalendar().getTimeInMillis();
        for (final a a : this.a.values()) {
            if (timeInMillis >= a.c) {
                list.add(a);
            }
        }
        for (final a a2 : list) {
            ac.e("Cleaning data: " + a2.toString());
            this.a.remove(a2.a);
        }
    }
    
    private void c() {
        ac.e("Loading data...");
        final File absoluteFile = x.a(this.b, "savedata.savedata").getAbsoluteFile();
        while (true) {
            if (!absoluteFile.exists()) {
                break Label_0171;
            }
            final String a = ac.a(absoluteFile);
            try {
                final JSONArray jsonArray = new JSONObject(a).getJSONArray("savedata");
                final HashMap a2 = new HashMap<String, a>(jsonArray.length());
                for (int i = 0; i < jsonArray.length(); ++i) {
                    final JSONObject jsonObject = jsonArray.getJSONObject(i);
                    final a a3 = new a();
                    a3.a = jsonObject.getString("key");
                    a3.b = jsonObject.getString("value");
                    a3.c = jsonObject.getLong("expirationDate");
                    ac.e("Loading data: " + a3.toString());
                    a2.put(a3.a, a3);
                }
                this.a = (Map<String, a>)a2;
                ac.e("Data loaded.");
            }
            catch (Exception ex) {
                ac.a(ex);
                continue;
            }
            break;
        }
    }
    
    public String a(String s) {
        synchronized (this) {
            this.d = ac.C().h();
            if (this.d == null) {
                return null;
            }
            s += this.d.i();
            if (!this.a.containsKey(s)) {
                return null;
            }
            s = this.a.get(s).b;
            return s;
            s = null;
            return s;
        }
    }
    
    public void a() {
        while (true) {
            Object o;
            synchronized (this) {
                ac.e("Saving data...");
                if (!this.c) {
                    ac.e("No changes in save data detected, no need to save.");
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
                        jsonObject2.put("expirationDate", a.c);
                        ((JSONArray)o).put((Object)jsonObject2);
                    }
                    catch (Exception ex) {
                        ac.a(ex);
                    }
                }
            }
            try {
                final File absoluteFile = x.a(this.b, "savedata.savedata").getAbsoluteFile();
                if (absoluteFile.exists()) {
                    absoluteFile.delete();
                }
                absoluteFile.createNewFile();
                final JSONObject jsonObject3;
                jsonObject3.put("savedata", o);
                final String string = jsonObject3.toString();
                o = new FileOutputStream(absoluteFile);
                ((FileOutputStream)o).write(string.getBytes());
                ((OutputStream)o).flush();
                ((FileOutputStream)o).close();
                ac.e("Data saved.");
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
    
    public void a(String string, final String s) {
        synchronized (this) {
            this.d = ac.C().h();
            if (this.d != null) {
                final long timeInMillis = this.d.e().getTimeInMillis();
                string += this.d.i();
                if (!this.a.containsKey(string)) {
                    final a a = new a();
                    a.a = string;
                    a.b = s;
                    a.c = timeInMillis;
                    this.a.put(string, a);
                    this.c = true;
                }
                final a a2 = this.a.get(string);
                if (a2.b != s || a2.c != timeInMillis) {
                    a2.c = timeInMillis;
                    a2.b = s;
                    this.c = true;
                }
            }
        }
    }
    
    public class a
    {
        public String a;
        public String b;
        public long c;
        
        @Override
        public String toString() {
            return "\"" + this.a + "\" - \"" + this.b + "\" - " + this.c + "ms";
        }
    }
}
