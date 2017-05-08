// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONArray;
import java.util.ArrayList;
import org.json.JSONObject;
import java.util.GregorianCalendar;
import java.util.List;

public class bs
{
    List<n> a;
    private String b;
    private String c;
    private int d;
    private String e;
    private String f;
    private List<String> g;
    private GregorianCalendar h;
    private String i;
    
    public bs(final String s, final String i, final boolean b, final boolean b2) throws Exception {
        final JSONObject jsonObject = new JSONObject(s);
        this.e = a(jsonObject, "session-id");
        this.f = a(jsonObject, "protocol-version");
        this.b = a(jsonObject, "current-time");
        if (jsonObject.has("session-id")) {
            ac.c(jsonObject.getString("session-id"));
        }
        if (jsonObject.has("pid")) {
            this.d = b(jsonObject, "pid");
        }
        else {
            this.d = 0;
        }
        this.i = i;
        if (jsonObject.has("event-server")) {
            this.c = a(jsonObject, "event-server");
        }
        this.g = a(jsonObject.getJSONObject("compatability"));
        this.h = ac.d(this.b);
        if (jsonObject.has("bluekai") && !ac.b) {
            final JSONObject jsonObject2 = jsonObject.getJSONObject("bluekai");
            if (jsonObject2 != null && jsonObject2.has("enable") && jsonObject2.getBoolean("enable") && jsonObject2.has("template-url")) {
                jsonObject2.put("sdk_udid", (Object)at.a().f);
                final double a = bp.a;
                jsonObject2.put("long", bp.b);
                jsonObject2.put("lat", a);
                if (!ac.b) {
                    ac.a(jsonObject2);
                }
            }
        }
        if (jsonObject.has("cookies")) {
            ac.g().a(jsonObject.getJSONArray("cookies"), this.h.getTimeInMillis());
        }
        final JSONArray jsonArray = jsonObject.getJSONArray("ad");
        this.a = new ArrayList<n>(jsonArray.length());
        for (int j = 0; j < jsonArray.length(); ++j) {
            final JSONObject jsonObject3 = jsonArray.getJSONObject(j);
            boolean b3;
            boolean b4;
            if (n.b.a(jsonObject3.getString("adtype")) == n.b.f) {
                if (jsonObject3.has("url") && jsonObject3.getString("url").endsWith(".zip")) {
                    b3 = false;
                    b4 = true;
                }
                else {
                    b3 = true;
                    b4 = false;
                }
            }
            else {
                b3 = false;
                b4 = false;
            }
            if (jsonObject3.has("buy-now") && this.a("disable-buy-it-now")) {
                ac.e("Buy It Now feature not supported on this device, removing ad");
            }
            else if (b3 && this.a("disable-mraid-url")) {
                ac.e("URL Mraid feature not supported on this device, removing ad");
            }
            else if (b4 && this.a("disable-mraid-zip")) {
                ac.e("Zip Mraid feature not supported on this device, removing ad");
            }
            else {
                final n a2 = n.a(this, jsonObject3, b2);
                if (a2 != null) {
                    this.a.add(a2);
                }
            }
        }
    }
    
    private static String a(final JSONObject jsonObject, String string) {
        try {
            if ((string = jsonObject.getString(string)).equals("null")) {
                string = "";
            }
            return string;
        }
        catch (JSONException ex) {
            ac.a((Throwable)ex);
            return "";
        }
    }
    
    private static List<String> a(final JSONObject jsonObject) {
        final ArrayList<String> list = new ArrayList<String>();
        if (jsonObject != null) {
            try {
                final Iterator keys = jsonObject.keys();
                while (keys.hasNext()) {
                    final String s = keys.next();
                    if (jsonObject.getBoolean(s)) {
                        list.add(s);
                    }
                }
            }
            catch (Exception ex) {
                ac.a(ex);
            }
        }
        return list;
    }
    
    private static int b(final JSONObject jsonObject, final String s) {
        try {
            return jsonObject.getInt(s);
        }
        catch (JSONException ex) {
            ac.a((Throwable)ex);
            return 0;
        }
    }
    
    public String a() {
        return this.b;
    }
    
    public boolean a(final aw.b b) {
        final String string = "ignore-event-" + b.toString().toLowerCase();
        final Iterator<String> iterator = this.g.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(string)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean a(final String s) {
        final Iterator<String> iterator = this.g.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(s)) {
                return true;
            }
        }
        return false;
    }
    
    public List<n> b() {
        return this.a;
    }
    
    public String c() {
        return this.e;
    }
}
