// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.c;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.net.URL;
import java.net.HttpURLConnection;
import android.net.Uri;
import com.tremorvideo.sdk.android.videoad.ac;
import java.net.URLEncoder;
import java.security.Key;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Collections;
import java.util.Comparator;
import java.util.Collection;
import java.util.List;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.Random;
import java.util.ArrayList;

public class a
{
    private ArrayList<String[]> a;
    private String b;
    private String c;
    private String d;
    private String e;
    private String f;
    private String g;
    
    public a(final String c, final String d, final String b, final String e) {
        this.f = null;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = null;
        this.a = new ArrayList<String[]>();
    }
    
    public a(final String c, final String d, final String f, final String b, final String e) {
        this.f = null;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.a = new ArrayList<String[]>();
    }
    
    private static String a(final long n) {
        return String.valueOf(n) + String.valueOf(new Random().nextInt());
    }
    
    private static String a(final InputStream inputStream) {
        try {
            final char[] array = new char[65536];
            final StringBuilder sb = new StringBuilder();
            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            int i;
            do {
                i = inputStreamReader.read(array, 0, array.length);
                if (i > 0) {
                    sb.append(array, 0, i);
                }
            } while (i >= 0);
            inputStreamReader.close();
            return sb.toString();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
    
    private static String a(final List<String[]> list) {
        final ArrayList<Object> list2 = new ArrayList<Object>(list);
        Collections.sort(list2, (Comparator<? super Object>)new Comparator<String[]>() {
            public int a(final String[] array, final String[] array2) {
                int n;
                if ((n = array[0].compareTo(array2[0])) == 0) {
                    n = array[1].compareTo(array2[1]);
                }
                return n;
            }
        });
        int i = 0;
        String s = "";
        while (i < list2.size()) {
            final String[] array = list2.get(i);
            final String s2 = s = s + array[0] + (char)61 + array[1];
            if (i != list2.size() - 1) {
                s = s2 + '&';
            }
            ++i;
        }
        return s;
    }
    
    public static String a(final byte[] array) {
        final int n = 0;
        final StringBuilder sb = new StringBuilder();
        final int n2 = (3 - array.length % 3) % 3;
        final byte[] array2 = new byte[array.length + n2];
        System.arraycopy(array, 0, array2, 0, array.length);
        int n3 = 0;
        int i;
        while (true) {
            i = n;
            if (n3 >= array2.length) {
                break;
            }
            final int n4 = ((array2[n3] & 0xFF) << 16) + ((array2[n3 + 1] & 0xFF) << 8) + (array2[n3 + 2] & 0xFF);
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(n4 >> 18 & 0x3F));
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(n4 >> 12 & 0x3F));
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(n4 >> 6 & 0x3F));
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(n4 >> 0 & 0x3F));
            n3 += 3;
        }
        while (i < n2) {
            sb.setCharAt(sb.length() - n2 + i, '=');
            ++i;
        }
        return sb.toString();
    }
    
    public static String b(String a, final String s) {
        try {
            final SecretKeySpec secretKeySpec = new SecretKeySpec(s.getBytes("UTF-8"), "HmacSHA1");
            final Mac instance = Mac.getInstance("HmacSHA1");
            instance.init(secretKeySpec);
            a = a(instance.doFinal(a.getBytes("UTF-8")));
            return a;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    private String d() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.a.size(); ++i) {
            final String[] array = this.a.get(i);
            sb.append(array[0] + "=" + array[1]);
            if (i < this.a.size() - 1) {
                sb.append('&');
            }
        }
        return sb.toString();
    }
    
    private static String e() {
        return "HMAC-SHA1";
    }
    
    private static long f() {
        return System.currentTimeMillis() / 1000L;
    }
    
    public void a(final String s, String replace) {
        try {
            replace = URLEncoder.encode(replace, "UTF-8").replace("*", "%2A").replace("+", "%20").replace("%7E", "~");
            this.a.add(new String[] { s, replace });
        }
        catch (Exception ex) {
            ac.a(ex);
        }
    }
    
    public boolean a() {
        long f;
        String a;
        ArrayList<String[]> list;
        try {
            f = f();
            a = a(f);
            list = new ArrayList<String[]>();
            list.add(new String[] { "oauth_consumer_key", this.c });
            list.add(new String[] { "oauth_signature_method", e() });
            list.add(new String[] { "oauth_timestamp", Long.toString(f) });
            list.add(new String[] { "oauth_nonce", a });
            list.add(new String[] { "oauth_version", "1.0" });
            if (this.f != null) {
                list.add(new String[] { "oauth_token", this.f });
            }
            final Iterator<String[]> iterator = this.a.iterator();
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
        }
        catch (Exception ex) {
            ac.a("Error sending OAuth request", ex);
            this.g = "";
            return false;
        }
        String s = "OAuth " + "oauth_nonce=\"" + Uri.encode(a) + "\", " + "oauth_signature_method=\"HMAC-SHA1\", " + "oauth_timestamp=\"" + f + "\", " + "oauth_consumer_key=\"" + this.c + "\", " + "oauth_signature=\"" + Uri.encode(b("" + this.b + "&" + Uri.encode(this.e) + "&" + Uri.encode(a(list)), this.d)) + "\", ";
        if (this.f != null) {
            s = s + "oauth_token=\"" + this.f + "\", ";
        }
        final String string = s + "oauth_version=\"1.0\"";
        final String d = this.d();
        final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(this.e).openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod(this.b);
        httpURLConnection.setReadTimeout(18000);
        httpURLConnection.setConnectTimeout(18000);
        httpURLConnection.setRequestProperty("Authorization", string);
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(d.getBytes().length));
        httpURLConnection.getOutputStream().write(d.getBytes());
        final int responseCode = httpURLConnection.getResponseCode();
        if (responseCode >= 200 && responseCode < 300) {
            this.g = a(httpURLConnection.getInputStream());
            return true;
        }
        this.g = a(httpURLConnection.getErrorStream());
        return false;
    }
    
    public HashMap<String, String> b() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        final String[] split = this.g.split("&");
        for (int length = split.length, i = 0; i < length; ++i) {
            final String[] split2 = split[i].split("=");
            if (split2.length == 2) {
                hashMap.put(split2[0], split2[1]);
            }
        }
        return hashMap;
    }
    
    public String c() {
        return this.g;
    }
}
