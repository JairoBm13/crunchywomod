// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import java.util.TimeZone;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import com.tremorvideo.sdk.android.richmedia.ae;
import java.net.URL;
import java.net.HttpURLConnection;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpVersion;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.BasicHttpParams;
import android.os.AsyncTask;
import android.content.SharedPreferences;
import java.net.HttpCookie;
import java.net.URI;
import java.security.MessageDigest;
import org.apache.http.NameValuePair;
import java.util.List;
import com.tremorvideo.sdk.android.logger.TestAppLogger;
import java.util.Map;
import java.util.Calendar;
import java.util.GregorianCalendar;
import android.app.Activity;
import java.net.CookieHandler;
import java.net.CookieStore;
import java.net.CookiePolicy;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.CookieManager;

public class z
{
    private static int a;
    private static CookieManager b;
    private static String c;
    private static String d;
    
    static {
        z.a = 1;
        z.b = null;
        z.c = "TremoPrefs";
        z.d = "TremorBis";
    }
    
    public static JSONObject a(final Settings ex) {
        JSONArray jsonArray;
        while (true) {
            try {
                bp.a(ac.x(), true);
                try {
                    jsonArray = new JSONArray();
                    final Iterator<String> iterator = ((Settings)ex).userInterests.iterator();
                    while (iterator.hasNext()) {
                        jsonArray.put((Object)iterator.next());
                    }
                }
                catch (Exception ex) {
                    ac.a("Error creating JSON user data", ex);
                    return null;
                }
            }
            catch (Exception ex2) {
                ac.e("Communication: Unable to get location: " + ex2.toString());
                continue;
            }
            break;
        }
        final JSONObject jsonObject = new JSONObject();
        for (final String s : ((Settings)ex).misc.keySet()) {
            jsonObject.put(s, (Object)((Settings)ex).misc.get(s));
        }
        final JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("age", ((Settings)ex).userAge);
        jsonObject2.put("gender", ((Settings)ex).userGender.ordinal());
        jsonObject2.put("locale", (Object)((Settings)ex).userCountry);
        jsonObject2.put("lang", (Object)((Settings)ex).userLanguage);
        jsonObject2.put("zip", (Object)((Settings)ex).userZip);
        jsonObject2.put("income", ((Settings)ex).userIncomeRange.ordinal());
        jsonObject2.put("education", ((Settings)ex).userEducation.ordinal());
        jsonObject2.put("race", ((Settings)ex).userRace.ordinal());
        jsonObject2.put("interests", (Object)jsonArray);
        jsonObject2.put("misc", (Object)jsonObject);
        final double a = bp.a;
        final double b = bp.b;
        if (a == 0.0 && b == 0.0) {
            jsonObject2.put("long", ((Settings)ex).userLongitude);
            jsonObject2.put("lat", ((Settings)ex).userLatitude);
            return jsonObject2;
        }
        jsonObject2.put("long", b);
        jsonObject2.put("lat", a);
        return jsonObject2;
    }
    
    public static JSONObject a(final at at) {
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray();
            final Iterator<String> iterator = at.u.iterator();
            while (iterator.hasNext()) {
                jsonArray.put((Object)iterator.next());
            }
        }
        catch (Exception ex) {
            ac.a("Error creating JSON device info", ex);
            return null;
        }
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("sdk_version", (Object)at.a);
        jsonObject.put("timezone", (Object)at.z);
        jsonObject.put("make", (Object)at.b);
        jsonObject.put("width", at.k);
        jsonObject.put("height", at.l);
        jsonObject.put("carrier", (Object)at.m);
        jsonObject.put("mcc", (Object)at.A);
        jsonObject.put("mnc", (Object)at.B);
        jsonObject.put("color_depth", at.n);
        jsonObject.put("model", (Object)at.c);
        jsonObject.put("OS", (Object)at.d);
        jsonObject.put("OS_version", (Object)at.e);
        jsonObject.put("udid", (Object)at.f);
        jsonObject.put("opt-out", at.g);
        jsonObject.put("androidID", (Object)at.h);
        jsonObject.put("connection", (Object)at.i);
        jsonObject.put("connectType", at.j);
        jsonObject.put("disk_space", at.p);
        jsonObject.put("heap", at.o);
        jsonObject.put("bandwith", at.q);
        jsonObject.put("accelerometer", at.r);
        jsonObject.put("gps", at.s);
        jsonObject.put("gyroscope", at.t);
        jsonObject.put("apps", (Object)jsonArray);
        jsonObject.put("networkISO", (Object)at.w);
        jsonObject.put("simISO", (Object)at.v);
        jsonObject.put("carrierCountryISO", (Object)at.x);
        jsonObject.put("user_agent", (Object)at.C);
        jsonObject.put("location_access", at.E);
        return jsonObject;
    }
    
    public static JSONObject a(final at at, final Settings settings, final y y, final TremorVideo.a a, final String s, final boolean b) {
        JSONObject jsonObject;
        JSONObject jsonObject2;
        JSONArray jsonArray;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("version", (Object)"3.11.0.0");
            jsonObject.put("adtype", (Object)"video");
            jsonObject.put("mode", 311);
            jsonObject.put("placement", a.ordinal());
            jsonObject.put("previous_session_id", (Object)ac.B());
            jsonObject.put("appId", 0);
            jsonObject.put("device_info", (Object)a(at));
            jsonObject.put("user_info", (Object)a(settings));
            jsonObject2 = new JSONObject();
            jsonArray = new JSONArray();
            final Iterator<String> iterator = settings.category.iterator();
            while (iterator.hasNext()) {
                jsonArray.put((Object)iterator.next());
            }
        }
        catch (Exception ex) {
            ac.a("Error creating JSON request", ex);
            return null;
        }
        final JSONArray jsonArray2 = new JSONArray();
        final Iterator<String> iterator2 = settings.adBlocks.iterator();
        while (iterator2.hasNext()) {
            jsonArray2.put((Object)iterator2.next());
        }
        jsonObject2.put("cid", 0);
        jsonObject2.put("pid", 0);
        jsonObject2.put("cch", (Object)s);
        jsonObject2.put("cp", (Object)"");
        jsonObject2.put("cat", (Object)jsonArray);
        jsonObject2.put("preferred_orientation", settings.preferredOrientation.ordinal());
        jsonObject2.put("adBlocks", (Object)jsonArray2);
        jsonObject2.put("policyId", (Object)settings.policyID);
        jsonObject2.put("maxAdTimeSeconds", settings.maxAdTimeSeconds);
        jsonObject2.put("contentID", (Object)settings.contentID);
        jsonObject2.put("contentDescription", (Object)settings.contentDescription);
        jsonObject2.put("contentTitle", (Object)settings.contentTitle);
        jsonObject2.put("bundleid", (Object)ac.j());
        jsonObject2.put("appversion", (Object)ac.k());
        jsonObject2.put("returnStreamingAd", b);
        jsonObject.put("contextual_info", (Object)jsonObject2);
        ac.a(ac.c.c, jsonObject.toString(2));
        return jsonObject;
    }
    
    public static void a() {
        if (z.b == null) {
            CookieHandler.setDefault(z.b = new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        }
        c();
    }
    
    public static void a(final Activity activity, final bs bs) {
        try {
            final Settings v = ac.v();
            final JSONObject jsonObject = new JSONObject();
            final JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("protocol_version", (Object)"3.11.0.0");
            jsonObject2.put("pid", 0);
            jsonObject2.put("cch", (Object)ac.p());
            jsonObject2.put("appId", 0);
            jsonObject2.put("policyId", (Object)v.policyID);
            jsonObject2.put("init_tapResponse_delta", ac.G());
            if (bs == null) {
                jsonObject2.put("session_id", 0);
                jsonObject2.put("tap_response_timestamp", (Object)"");
            }
            else {
                jsonObject2.put("session_id", (Object)bs.c());
                jsonObject2.put("tap_response_timestamp", (Object)bs.a());
            }
            jsonObject.put("avail", (Object)jsonObject2);
            ac.a(ac.c.c, jsonObject.toString(2));
            a(activity, jsonObject.toString(), ac.f());
        }
        catch (Exception ex) {
            ac.a("Error Sending Avail: ", ex);
        }
    }
    
    public static void a(final Activity activity, final String s) {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("protocol_version", (Object)"3.11.0.0");
            jsonObject.put("cch", (Object)ac.p());
            jsonObject.put("state", (Object)s);
            jsonObject.put("timestamp", (Object)ac.a(new GregorianCalendar()));
            jsonObject.put("app", (Object)ac.o());
            jsonObject.put("app_version", (Object)ac.k());
            jsonObject.put("device_info", (Object)a(at.a()));
            final JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("appAnalyticsStateChange", (Object)jsonObject);
            ac.a(ac.c.c, jsonObject2.toString(2));
            a(activity, jsonObject2.toString(), ac.i());
        }
        catch (Exception ex) {
            ac.a("Error Sending Custom Event: ", ex);
        }
    }
    
    private static void a(final Activity activity, final String s, final String s2) {
        if (s2 == null || s2.equalsIgnoreCase("")) {
            return;
        }
        if (activity != null) {
            activity.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    try {
                        new c().execute((Object[])new String[0]);
                    }
                    catch (Exception ex) {
                        ac.a("Error sending response: ", ex);
                    }
                }
            });
            return;
        }
        try {
            new c().execute((Object[])new String[0]);
        }
        catch (Exception ex) {
            ac.a("Error sending response: ", ex);
        }
    }
    
    public static void a(final Activity activity, final String s, final Map<String, String> map) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject();
            for (final String s2 : map.keySet()) {
                jsonObject.put(s2, (Object)map.get(s2));
            }
        }
        catch (Exception ex) {
            ac.a("Error Sending Custom Event: ", ex);
            return;
        }
        final JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("protocol_version", (Object)"3.11.0.0");
        jsonObject2.put("cch", (Object)ac.p());
        jsonObject2.put("event", (Object)s);
        jsonObject2.put("timestamp", (Object)ac.a(new GregorianCalendar()));
        jsonObject2.put("app", (Object)ac.o());
        jsonObject2.put("app_version", (Object)ac.k());
        jsonObject2.put("parameters", (Object)jsonObject);
        jsonObject2.put("device_info", (Object)a(at.a()));
        final JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("appAnalyticsCustomEvent", (Object)jsonObject2);
        ac.a(ac.c.c, jsonObject3.toString(2));
        a(activity, jsonObject3.toString(), ac.i());
    }
    
    public static void a(String value, final int n, String string) {
    Label_0065_Outer:
        while (true) {
            while (true) {
            Label_0151:
                while (true) {
                    Label_0144: {
                        try {
                            if (ac.r) {
                                final TestAppLogger instance = TestAppLogger.getInstance();
                                final StringBuilder append = new StringBuilder().append("event=");
                                if (value == null) {
                                    break Label_0144;
                                }
                                final String string2 = append.append(value).toString();
                                final StringBuilder append2 = new StringBuilder().append("response_code:");
                                if (n != -1) {
                                    value = (String)n;
                                    string = append2.append((Object)value).append(", tracking_url:").append(string).toString();
                                    if (n >= 200 && n < 400) {
                                        value = "pass";
                                    }
                                    else {
                                        value = "fail";
                                    }
                                    instance.logtracking(string2, string, value);
                                    return;
                                }
                                break Label_0151;
                            }
                        }
                        catch (Exception ex) {
                            ac.e("Error logTracking" + ex);
                        }
                        break;
                    }
                    value = "";
                    continue Label_0065_Outer;
                }
                value = "";
                continue;
            }
        }
    }
    
    public static void a(final String s, final long n) {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("protocol_version", (Object)"3.11.0.0");
            jsonObject.put("cch", (Object)ac.p());
            jsonObject.put("timeDeltaMS", (Object)String.valueOf(n));
            jsonObject.put("startTime", (Object)s);
            jsonObject.put("app", (Object)ac.o());
            jsonObject.put("app_version", (Object)ac.k());
            final at b = at.b();
            if (b != null) {
                jsonObject.put("device_info", (Object)a(b));
            }
            jsonObject.put("cookies", (Object)ac.g().a());
            final JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("appSessionEnd", (Object)jsonObject);
            ac.a(ac.c.c, jsonObject2.toString(2));
            b(jsonObject2.toString(), ac.i());
        }
        catch (Exception ex) {
            ac.a(ex);
        }
    }
    
    public static void a(final String s, final String s2) {
        new a().execute((Object[])new String[0]);
    }
    
    public static void a(final String s, final List<NameValuePair> list, final boolean b, final int n, final String s2) {
        ac.e("Firing Tracking Pixel: " + s);
        new b().execute((Object[])new String[0]);
    }
    
    private static String b(String lowerCase) {
        try {
            final byte[] digest = MessageDigest.getInstance("MD5").digest(lowerCase.getBytes());
            final StringBuffer sb = new StringBuffer(digest.length * 2);
            for (int i = 0; i < digest.length; ++i) {
                final int n = digest[i] & 0xFF;
                if (n < 16) {
                    sb.append('0');
                }
                sb.append(Integer.toHexString(n));
            }
            lowerCase = sb.toString().toLowerCase();
            return lowerCase;
        }
        catch (Exception ex) {
            ac.a(ac.c.b, "Error encoding mac.", ex);
            return "";
        }
    }
    
    public static void b() {
        SharedPreferences sharedPreferences = null;
        JSONArray jsonArray = null;
        Label_0282: {
            try {
                sharedPreferences = ac.x().getSharedPreferences(z.c, 0);
                jsonArray = new JSONArray();
                for (final URI uri : z.b.getCookieStore().getURIs()) {
                    for (final HttpCookie httpCookie : z.b.getCookieStore().get(uri)) {
                        final JSONObject jsonObject = new JSONObject();
                        jsonObject.put("uri", (Object)uri.toString());
                        jsonObject.put("name", (Object)httpCookie.getName());
                        jsonObject.put("value", (Object)httpCookie.getValue());
                        jsonObject.put("domain", (Object)httpCookie.getDomain());
                        jsonObject.put("path", (Object)httpCookie.getPath());
                        jsonObject.put("version", httpCookie.getVersion());
                        jsonObject.put("secure", httpCookie.getSecure());
                        jsonObject.put("maxAge", httpCookie.getMaxAge());
                        jsonObject.put("discard", httpCookie.getDiscard());
                        jsonObject.put("portList", (Object)httpCookie.getPortlist());
                        jsonArray.put((Object)jsonObject);
                    }
                }
                break Label_0282;
            }
            catch (Exception ex) {
                ac.e("Saving cookies Exception : " + ex.getMessage());
            }
            return;
        }
        if (jsonArray.length() > 0) {
            sharedPreferences.edit().putString(z.d, jsonArray.toString()).commit();
        }
    }
    
    private static void b(final String s, final String s2) {
        a(null, s, s2);
    }
    
    public static void c() {
        final String string = ac.x().getSharedPreferences(z.c, 0).getString(z.d, (String)null);
        if (string != null && string.length() > 0) {
            try {
                final JSONArray jsonArray = new JSONArray(string);
                int i = 0;
                while (i < jsonArray.length()) {
                    try {
                        final JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.has("name") && jsonObject.has("value")) {
                            final HttpCookie httpCookie = new HttpCookie(jsonObject.getString("name"), jsonObject.getString("value"));
                            if (jsonObject.has("domain")) {
                                httpCookie.setDomain(jsonObject.getString("domain"));
                            }
                            if (jsonObject.has("path")) {
                                httpCookie.setPath(jsonObject.getString("path"));
                            }
                            if (jsonObject.has("version")) {
                                httpCookie.setVersion(jsonObject.getInt("version"));
                            }
                            if (jsonObject.has("secure")) {
                                httpCookie.setSecure(jsonObject.getBoolean("secure"));
                            }
                            if (jsonObject.has("maxAge")) {
                                httpCookie.setMaxAge(jsonObject.getLong("maxAge"));
                            }
                            if (jsonObject.has("discard")) {
                                httpCookie.setDiscard(jsonObject.getBoolean("discard"));
                            }
                            if (jsonObject.has("portList")) {
                                httpCookie.setPortlist(jsonObject.getString("portList"));
                            }
                            URI uri;
                            if (jsonObject.has("uri")) {
                                uri = new URI(jsonObject.getString("uri"));
                            }
                            else {
                                uri = null;
                            }
                            z.b.getCookieStore().add(uri, httpCookie);
                        }
                        ++i;
                    }
                    catch (Exception ex) {
                        ac.e("Error loading cookie : " + ex.getMessage());
                    }
                }
            }
            catch (Exception ex2) {
                ac.e("Loading cookies exception : " + ex2.getMessage());
            }
        }
    }
    
    class a extends AsyncTask<String, Void, Void>
    {
        final /* synthetic */ String a;
        final /* synthetic */ String b;
        
        a(final String a, final String b) {
            this.a = a;
            this.b = b;
        }
        
        protected Void a(final String... array) {
            while (true) {
                while (true) {
                    Label_0274: {
                        try {
                            final BasicHttpParams basicHttpParams = new BasicHttpParams();
                            HttpConnectionParams.setConnectionTimeout((HttpParams)basicHttpParams, 18000);
                            HttpConnectionParams.setSoTimeout((HttpParams)basicHttpParams, 18000);
                            ((HttpParams)basicHttpParams).setBooleanParameter("http.protocol.expect-continue", false);
                            ((HttpParams)basicHttpParams).setParameter("http.protocol.version", (Object)HttpVersion.HTTP_1_0);
                            final DefaultHttpClient defaultHttpClient = new DefaultHttpClient((HttpParams)basicHttpParams);
                            final StringBuilder sb = new StringBuilder(this.a);
                            sb.append("&deviceId=");
                            if (this.b == null || this.b.length() <= 0) {
                                break Label_0274;
                            }
                            final String b = this.b;
                            sb.append(b);
                            final String string = sb.toString();
                            ac.e("TremorLog_info::ConversionsTracking::Firing Tracking Pixel url=" + string);
                            final HttpGet httpGet = new HttpGet(string);
                            httpGet.setHeader("User-Agent", ac.z());
                            final StatusLine statusLine = defaultHttpClient.execute((HttpUriRequest)httpGet).getStatusLine();
                            if (statusLine != null) {
                                final int statusCode = statusLine.getStatusCode();
                                if (statusCode >= 200 && statusCode < 400) {
                                    ac.e("TremorLog_info::ConversionsTracking::Conversion tracking fired successfully.");
                                    ac.x().getSharedPreferences("Conversion", 0).edit().putBoolean("convFired", true).commit();
                                }
                                else {
                                    ac.e("TremorLog_error::ConversionsTracking::Conversion tracking failed with StatusCode=" + statusCode);
                                }
                            }
                        }
                        catch (Exception ex) {
                            ac.e("TremorLog_error::ConversionsTracking::" + ex.getMessage());
                        }
                        break;
                    }
                    final String b = "";
                    continue;
                }
            }
            return null;
        }
    }
    
    class b extends AsyncTask<String, Void, Void>
    {
        final /* synthetic */ String a;
        final /* synthetic */ boolean b;
        final /* synthetic */ int c;
        final /* synthetic */ List d;
        final /* synthetic */ String e;
        
        b(final String a, final boolean b, final int c, final List d, final String e) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
        }
        
        protected Void a(final String... array) {
            try {
                final int d = ++z.a;
                ac.e("Sending Communication: " + d + this.a);
                final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(this.a).openConnection();
                httpURLConnection.setReadTimeout(18000);
                httpURLConnection.setConnectTimeout(18000);
                httpURLConnection.setRequestProperty("Content-Type", "text/plain; charset=ISO-8859-1");
                httpURLConnection.setDoInput(true);
                final String a = ae.a(this.a, this.b, this.c);
                if (a != null) {
                    httpURLConnection.setRequestProperty("User-Agent", a);
                }
                if (this.b && this.d != null) {
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));
                    bufferedWriter.write(ae.a(this.d));
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
                else {
                    httpURLConnection.setRequestMethod("GET");
                }
                final int responseCode = httpURLConnection.getResponseCode();
                final String responseMessage = httpURLConnection.getResponseMessage();
                if (responseMessage != null) {
                    ac.e("Status: " + d + " : " + responseMessage);
                    z.a(this.e, responseCode, this.a);
                }
                z.b();
            }
            catch (Exception ex) {
                ac.e("Error Firing Tracking Pixel: " + this.a + " :: " + ex.getMessage());
                ac.a(ex);
                z.a(this.e, -1, this.a);
            }
            return null;
        }
    }
    
    class c extends AsyncTask<String, Void, Void>
    {
        final /* synthetic */ String a;
        final /* synthetic */ String b;
        
        c(final String a, final String b) {
            this.a = a;
            this.b = b;
        }
        
        protected Void a(final String... array) {
            try {
                final int d = ++z.a;
                ac.e("Sending Communication: " + d);
                final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(this.a).openConnection();
                httpURLConnection.setReadTimeout(18000);
                httpURLConnection.setConnectTimeout(18000);
                httpURLConnection.setRequestProperty("Content-Type", "text/plain; charset=ISO-8859-1");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                final String c = ae.c(this.a);
                if (c != null) {
                    httpURLConnection.setRequestProperty("User-Agent", c);
                }
                final String string = "" + new GregorianCalendar(TimeZone.getTimeZone("GMT")).getTimeInMillis() / 1000L;
                httpURLConnection.setRequestProperty("mac", b("a5e8fa6812cd60cfff77db9728ccfa70" + this.b + string));
                httpURLConnection.setRequestProperty("rts", string);
                final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));
                bufferedWriter.write(this.b);
                bufferedWriter.flush();
                bufferedWriter.close();
                httpURLConnection.getResponseCode();
                final String responseMessage = httpURLConnection.getResponseMessage();
                z.b();
                if (responseMessage != null) {
                    ac.e("Status: " + d + " : " + responseMessage);
                }
                return null;
            }
            catch (Exception ex) {
                ac.a("Error Sending Response: Executing POST", ex);
                return null;
            }
        }
    }
}
