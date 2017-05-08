// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import org.apache.http.client.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import com.tremorvideo.sdk.android.richmedia.ae;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpVersion;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.BasicHttpParams;
import android.os.AsyncTask;
import java.util.GregorianCalendar;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.BufferedInputStream;
import org.apache.http.HttpEntity;

public class bv implements ax.a, e
{
    int a;
    int b;
    int c;
    String d;
    String e;
    b f;
    a g;
    String h;
    private final String i;
    
    public bv() {
        this.d = "";
        this.e = "";
        this.h = null;
        this.i = "http://data.tmsapi.com/v1/movies/__ID__/showings?startDate=__DATE__&zip=__ZIP__&api_key=dn3gkyv4pzcmfcp5fakh7raz&numDays=1&radius=20&units=mi";
    }
    
    private String a(final HttpEntity httpEntity) throws Exception {
        final BufferedInputStream bufferedInputStream = new BufferedInputStream(httpEntity.getContent());
        final StringBuilder sb = new StringBuilder(Math.max(0, (int)httpEntity.getContentLength()));
        final byte[] array = new byte[1024];
        while (true) {
            final int read = bufferedInputStream.read(array);
            if (read <= 0) {
                break;
            }
            sb.append(new String(array, 0, read));
        }
        return sb.toString();
    }
    
    private void a(final String s) {
        while (true) {
            final a g;
            Object nextValue;
            synchronized (this) {
                this.f = null;
                g = this.g;
                ac.e(s);
                if (g == null || s == null) {
                    return;
                }
                while (true) {
                    try {
                        nextValue = new JSONTokener(s).nextValue();
                        if (nextValue instanceof JSONObject) {
                            final JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.has("errorMessage")) {
                                ac.e("Error getting TMS data: " + jsonObject.getString("errorMessage"));
                            }
                            else {
                                ac.e("Error getting TMS data.");
                            }
                            return;
                        }
                        break;
                    }
                    catch (Exception ex) {
                        ac.a(ex);
                    }
                    return;
                }
            }
            if (!(nextValue instanceof JSONArray)) {
                return;
            }
            final String s2;
            final JSONArray jsonArray = new JSONArray(s2);
            if (jsonArray.length() > 0) {
                final JSONArray jsonArray2 = new JSONArray();
                for (int i = 0; i < jsonArray.length(); ++i) {
                    final JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    if (jsonObject2.has("showtimes")) {
                        final JSONArray jsonArray3 = jsonObject2.getJSONArray("showtimes");
                        for (int j = 0; j < jsonArray3.length(); ++j) {
                            final JSONObject jsonObject3 = jsonArray3.getJSONObject(j);
                            final String[] split = jsonObject3.getString("dateTime").split("T");
                            final JSONObject jsonObject4 = jsonObject3.getJSONObject("theatre");
                            final JSONObject jsonObject5 = null;
                            int n = 0;
                            JSONObject jsonObject6;
                            while (true) {
                                jsonObject6 = jsonObject5;
                                if (n >= jsonArray2.length()) {
                                    break;
                                }
                                if (jsonArray2.getJSONObject(n).getString("theatreId").equals(jsonObject4.getString("id"))) {
                                    jsonObject6 = jsonArray2.getJSONObject(n);
                                    break;
                                }
                                ++n;
                            }
                            JSONObject jsonObject7;
                            if ((jsonObject7 = jsonObject6) == null) {
                                jsonObject7 = new JSONObject();
                                jsonObject7.put("theatreId", (Object)jsonObject4.getString("id"));
                                jsonObject7.put("theatreName", (Object)jsonObject4.getString("name"));
                                final JSONArray jsonArray4 = new JSONArray();
                                final JSONObject jsonObject8 = new JSONObject();
                                jsonObject8.put("showtimes", (Object)jsonArray4);
                                final JSONArray jsonArray5 = new JSONArray();
                                jsonArray5.put((Object)jsonObject8);
                                final JSONObject jsonObject9 = new JSONObject();
                                jsonObject9.put("day", (Object)split[0]);
                                jsonObject9.put("movies", (Object)jsonArray5);
                                final JSONArray jsonArray6 = new JSONArray();
                                jsonArray6.put((Object)jsonObject9);
                                jsonObject7.put("theatreDays", (Object)jsonArray6);
                                jsonArray2.put((Object)jsonObject7);
                            }
                            final JSONObject jsonObject10 = new JSONObject();
                            jsonObject10.put("datetime", (Object)jsonObject3.getString("dateTime"));
                            if (jsonObject3.has("ticketURI")) {
                                jsonObject10.put("clickURL", (Object)jsonObject3.getString("ticketURI"));
                            }
                            jsonObject7.getJSONArray("theatreDays").getJSONObject(0).getJSONArray("movies").getJSONObject(0).getJSONArray("showtimes").put((Object)jsonObject10);
                        }
                    }
                    final JSONObject jsonObject11 = new JSONObject();
                    nextValue = new JSONObject();
                    ((JSONObject)nextValue).put("theatres", (Object)jsonArray2);
                    jsonObject11.put("theatresAndShowtimesByMovie", nextValue);
                    g.a(jsonObject11);
                }
            }
        }
    }
    
    private String c() {
        return String.format("%04d-%02d-%02d", this.a, this.b, this.c);
    }
    
    public void a() {
        if (this.f == null) {
            (this.f = new b()).execute((Object[])new String[0]);
        }
    }
    
    @Override
    public void a(final aw.b b, final int n, final int n2, final int n3) {
        if (b == aw.b.F) {
            this.a(this.d, n, n2 + 1, n3);
            this.g.a(n, n2, n3);
        }
    }
    
    public void a(final a g) {
        synchronized (this) {
            this.g = g;
        }
    }
    
    public void a(final String d, final int a, final int b, final int c) {
        // monitorenter(this)
        boolean b2 = false;
        try {
            if (this.d != d) {
                this.d = d;
                b2 = true;
            }
            if (this.a != a || this.b != b || this.c != c) {
                this.a = a;
                this.b = b;
                this.c = c;
                b2 = true;
            }
            if (b2) {
                if (this.g != null) {
                    this.g.h();
                }
                this.b();
                this.a();
            }
        }
        finally {
        }
        // monitorexit(this)
    }
    
    public void a(final String d, final String e, final String h, final GregorianCalendar gregorianCalendar) {
        this.d = d;
        this.e = e;
        if (h == null || h.length() == 0) {
            this.h = "http://data.tmsapi.com/v1/movies/__ID__/showings?startDate=__DATE__&zip=__ZIP__&api_key=dn3gkyv4pzcmfcp5fakh7raz&numDays=1&radius=20&units=mi";
        }
        else {
            this.h = h;
        }
        this.a = gregorianCalendar.get(1);
        this.b = gregorianCalendar.get(2) + 1;
        this.c = gregorianCalendar.get(5);
    }
    
    public void b() {
        if (this.f != null) {
            this.f.cancel(true);
            this.f = null;
        }
    }
    
    @Override
    public void f(String string) {
        while (string.length() < 5) {
            string = "0" + string;
        }
        this.a(string, this.a, this.b, this.c);
        this.g.a(string);
    }
    
    public interface a
    {
        void a(final int p0, final int p1, final int p2);
        
        void a(final String p0);
        
        void a(final JSONObject p0);
        
        void h();
    }
    
    class b extends AsyncTask<String, Void, String>
    {
        protected String a(final String... array) {
            final String s = null;
            try {
                final BasicHttpParams basicHttpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout((HttpParams)basicHttpParams, 18000);
                HttpConnectionParams.setSoTimeout((HttpParams)basicHttpParams, 18000);
                ((HttpParams)basicHttpParams).setBooleanParameter("http.protocol.expect-continue", false);
                ((HttpParams)basicHttpParams).setParameter("http.protocol.version", (Object)HttpVersion.HTTP_1_0);
                String s3;
                final String s2 = s3 = bv.this.h.replace("__ZIP__", bv.this.d).replace("__DATE__", bv.this.c());
                if (bv.this.e.length() > 0) {
                    s3 = s2.replace("__ID__", bv.this.e);
                }
                ac.e("TMS Date: " + bv.this.c());
                ac.e("TMS URL: " + s3);
                final DefaultHttpClient defaultHttpClient = new DefaultHttpClient((HttpParams)basicHttpParams);
                final HttpGet httpGet = new HttpGet(s3);
                ae.a(httpGet, s3);
                final HttpResponse execute = ((HttpClient)defaultHttpClient).execute((HttpUriRequest)httpGet);
                String a = s;
                if (!this.isCancelled()) {
                    a = bv.this.a(execute.getEntity());
                }
                return a;
            }
            catch (Exception ex) {
                ac.a("Error Sending Response: Exectuing POST", ex);
                return null;
            }
        }
        
        protected void a(final String s) {
            if (!this.isCancelled() && bv.this.f == this) {
                bv.this.a(s);
            }
        }
    }
}
