// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.a;

import java.net.URLConnection;
import android.app.AlertDialog$Builder;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.content.Context;
import java.net.MalformedURLException;
import java.io.FileNotFoundException;
import java.io.BufferedOutputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import android.util.Log;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.Iterator;
import java.net.URLEncoder;
import java.net.URLDecoder;
import android.os.Bundle;

public final class g
{
    public static Bundle a(final String s) {
        final Bundle bundle = new Bundle();
        if (s != null) {
            final String[] split = s.split("&");
            for (int length = split.length, i = 0; i < length; ++i) {
                final String[] split2 = split[i].split("=");
                bundle.putString(URLDecoder.decode(split2[0]), URLDecoder.decode(split2[1]));
            }
        }
        return bundle;
    }
    
    public static String a(final Bundle bundle) {
        if (bundle == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        final Iterator<String> iterator = (Iterator<String>)bundle.keySet().iterator();
        int n = 1;
        while (iterator.hasNext()) {
            final String s = iterator.next();
            if (n != 0) {
                n = 0;
            }
            else {
                sb.append("&");
            }
            sb.append(URLEncoder.encode(s) + "=" + URLEncoder.encode(bundle.getString(s)));
        }
        return sb.toString();
    }
    
    public static String a(final Bundle bundle, final String s) {
        if (bundle == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for (final String s2 : bundle.keySet()) {
            if (bundle.getByteArray(s2) == null) {
                sb.append("Content-Disposition: form-data; name=\"" + s2 + "\"\r\n\r\n" + bundle.getString(s2));
                sb.append("\r\n--" + s + "\r\n");
            }
        }
        return sb.toString();
    }
    
    private static String a(final InputStream inputStream) throws IOException {
        final StringBuilder sb = new StringBuilder();
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 1000);
        for (String s = bufferedReader.readLine(); s != null; s = bufferedReader.readLine()) {
            sb.append(s);
        }
        inputStream.close();
        return sb.toString();
    }
    
    public static String a(String s, String a, final Bundle bundle) throws MalformedURLException, IOException {
        String string = s;
        if (a.equals("GET")) {
            string = s + "?" + a(bundle);
        }
        Log.d("Facebook-Util", a + " URL: " + string);
        s = (String)new URL(string).openConnection();
        ((URLConnection)s).setRequestProperty("User-Agent", System.getProperties().getProperty("http.agent") + " FacebookAndroidSDK");
        ((URLConnection)s).setConnectTimeout(3000);
        ((URLConnection)s).setReadTimeout(3000);
        if (!a.equals("GET")) {
            final Bundle bundle2 = new Bundle();
            for (final String s2 : bundle.keySet()) {
                if (bundle.getByteArray(s2) != null) {
                    bundle2.putByteArray(s2, bundle.getByteArray(s2));
                }
            }
            if (!bundle.containsKey("method")) {
                bundle.putString("method", a);
            }
            if (bundle.containsKey("access_token")) {
                bundle.putString("access_token", URLDecoder.decode(bundle.getString("access_token")));
            }
            ((HttpURLConnection)s).setRequestMethod("POST");
            ((URLConnection)s).setRequestProperty("Content-Type", "multipart/form-data;boundary=" + "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f");
            ((URLConnection)s).setDoOutput(true);
            ((URLConnection)s).setDoInput(true);
            ((URLConnection)s).setRequestProperty("Connection", "Keep-Alive");
            ((URLConnection)s).connect();
            final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(((URLConnection)s).getOutputStream());
            bufferedOutputStream.write(("--" + "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f" + "\r\n").getBytes());
            bufferedOutputStream.write(a(bundle, "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f").getBytes());
            bufferedOutputStream.write(("\r\n" + "--" + "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f" + "\r\n").getBytes());
            if (!bundle2.isEmpty()) {
                for (final String s3 : bundle2.keySet()) {
                    bufferedOutputStream.write(("Content-Disposition: form-data; filename=\"" + s3 + "\"" + "\r\n").getBytes());
                    bufferedOutputStream.write(("Content-Type: content/unknown" + "\r\n" + "\r\n").getBytes());
                    bufferedOutputStream.write(bundle2.getByteArray(s3));
                    bufferedOutputStream.write(("\r\n" + "--" + "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f" + "\r\n").getBytes());
                }
            }
            bufferedOutputStream.flush();
        }
        try {
            a = a(((URLConnection)s).getInputStream());
            return a;
        }
        catch (FileNotFoundException ex) {
            return a(((HttpURLConnection)s).getErrorStream());
        }
    }
    
    public static void a(final Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager.getInstance().removeAllCookie();
    }
    
    public static void a(final Context context, final String title, final String message) {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder(context);
        alertDialog$Builder.setTitle((CharSequence)title);
        alertDialog$Builder.setMessage((CharSequence)message);
        alertDialog$Builder.create().show();
    }
    
    public static Bundle b(String replace) {
        replace = replace.replace("fbconnect", "http");
        try {
            final URL url = new URL(replace);
            final Bundle a = a(url.getQuery());
            a.putAll(a(url.getRef()));
            return a;
        }
        catch (MalformedURLException ex) {
            return new Bundle();
        }
    }
}
