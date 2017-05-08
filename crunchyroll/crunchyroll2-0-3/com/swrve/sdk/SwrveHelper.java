// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk;

import java.util.Locale;
import android.os.Build$VERSION;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.security.MessageDigest;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import android.util.Base64;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;
import java.util.Calendar;
import java.util.Date;
import org.json.JSONException;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public final class SwrveHelper
{
    public static Map<String, String> JSONToMap(final JSONObject jsonObject) throws JSONException {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        final Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            final String s = keys.next();
            hashMap.put(s, jsonObject.getString(s));
        }
        return hashMap;
    }
    
    public static Date addTimeInterval(final Date time, final int n, final int n2) {
        final Calendar instance = Calendar.getInstance();
        instance.setTime(time);
        instance.add(n2, n);
        return instance.getTime();
    }
    
    public static String createHMACWithMD5(final String s, final String s2) throws NoSuchAlgorithmException, InvalidKeyException {
        final Mac instance = Mac.getInstance("HmacMD5");
        instance.init(new SecretKeySpec(s2.getBytes(), instance.getAlgorithm()));
        return Base64.encodeToString(instance.doFinal(s.getBytes()), 0);
    }
    
    public static String encodeParameters(final Map<String, String> map) throws UnsupportedEncodingException {
        final StringBuilder sb = new StringBuilder();
        final Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        int n = 1;
        while (iterator.hasNext()) {
            final Map.Entry<String, String> entry = iterator.next();
            if (n != 0) {
                n = 0;
            }
            else {
                sb.append("&");
            }
            final StringBuilder append = new StringBuilder().append(URLEncoder.encode(entry.getKey(), "UTF-8")).append("=");
            String encode;
            if (entry.getValue() == null) {
                encode = "null";
            }
            else {
                encode = URLEncoder.encode(entry.getValue(), "UTF-8");
            }
            sb.append(append.append(encode).toString());
        }
        return sb.toString();
    }
    
    public static String generateSessionToken(String md5, final int n, final String s) {
        final String string = Long.toString(new Date().getTime() / 1000L);
        md5 = md5(s + string + md5);
        return n + "=" + s + "=" + string + "=" + md5;
    }
    
    public static boolean isNullOrEmpty(final String s) {
        return s == null || s.length() == 0;
    }
    
    public static void logAndThrowException(final String s) throws IllegalArgumentException {
        Log.e("SwrveSDK", s);
        throw new IllegalArgumentException(s);
    }
    
    public static String md5(String string) {
        if (string == null) {
            return null;
        }
        try {
            final byte[] digest = MessageDigest.getInstance("MD5").digest(string.getBytes());
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digest.length; ++i) {
                if ((digest[i] & 0xFF) < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(digest[i] & 0xFF));
            }
            string = sb.toString();
            return string;
        }
        catch (NoSuchAlgorithmException ex) {
            Log.wtf("SwrveSDK", "Couldn't find MD5 - what a strange JVM", (Throwable)ex);
            return "";
        }
    }
    
    public static String readStringFromInputStream(final InputStream inputStream) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuilder sb = new StringBuilder();
        while (true) {
            final String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            sb.append(line);
        }
        return sb.toString();
    }
    
    public static boolean sdkAvailable() {
        return Build$VERSION.SDK_INT >= 10;
    }
    
    public static String sha1(byte[] digest) {
        if (digest.length == 0) {
            return null;
        }
        try {
            digest = MessageDigest.getInstance("SHA1").digest(digest);
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digest.length; ++i) {
                if ((digest[i] & 0xFF) < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(digest[i] & 0xFF));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException ex) {
            Log.wtf("SwrveSDK", "Couldn't find SHA1 - what a strange JVM", (Throwable)ex);
            return "";
        }
    }
    
    public static boolean successResponseCode(final int n) {
        return n >= 200 && n < 300;
    }
    
    public static String toLanguageTag(final Locale locale) {
        final StringBuilder sb = new StringBuilder();
        sb.append(locale.getLanguage());
        if (!isNullOrEmpty(locale.getCountry())) {
            sb.append('-').append(locale.getCountry());
        }
        if (!isNullOrEmpty(locale.getVariant())) {
            sb.append('-').append(locale.getVariant());
        }
        return sb.toString();
    }
    
    public static boolean userErrorResponseCode(final int n) {
        return n >= 400 && n < 500;
    }
}
