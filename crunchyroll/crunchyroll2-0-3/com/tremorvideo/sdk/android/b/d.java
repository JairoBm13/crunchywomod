// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.b;

import org.apache.http.client.HttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;
import com.tremorvideo.sdk.android.richmedia.ae;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import android.os.AsyncTask;
import android.net.Uri;
import android.webkit.ConsoleMessage;
import android.util.Log;
import android.graphics.Paint;
import com.tremorvideo.sdk.android.videoad.ac;
import android.webkit.WebSettings;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.webkit.WebSettings$PluginState;
import android.os.Build$VERSION;
import java.util.Iterator;
import java.util.List;
import com.tremorvideo.sdk.android.videoad.aw;
import org.apache.http.NameValuePair;
import java.util.HashMap;
import org.apache.http.client.utils.URLEncodedUtils;
import java.net.URI;
import android.content.Context;
import android.webkit.WebView;

public class d extends WebView
{
    private com.tremorvideo.sdk.android.b.b a;
    private b b;
    private a c;
    private com.tremorvideo.sdk.android.b.a d;
    private com.tremorvideo.sdk.android.b.c e;
    private boolean f;
    
    public d(final Context context, final com.tremorvideo.sdk.android.b.b a) {
        super(context);
        this.a = a;
        this.e = a.g().z();
        this.e();
    }
    
    private void a(final URI uri) {
        final String host = uri.getHost();
        final List parse = URLEncodedUtils.parse(uri, "UTF-8");
        final HashMap<Object, String> hashMap = new HashMap<Object, String>();
        for (final NameValuePair nameValuePair : parse) {
            hashMap.put(nameValuePair.getName(), nameValuePair.getValue());
        }
        if (host.equals("trigger-event")) {
            final aw.b a = aw.b.a(hashMap.get("event_type"));
            if (a == aw.b.ac) {
                final String a2 = hashMap.get("new_zip");
                if (a2 != null && a2.length() > 0 && a2 != this.e.a) {
                    this.e.a = a2;
                    this.d();
                }
                this.d.a(a);
            }
            else if (a == aw.b.aa) {
                this.d.e();
            }
            else if (a == aw.b.ae) {
                this.d.a(a, hashMap.get("longitude"), hashMap.get("latitude"), hashMap.get("theatre"));
            }
            else if (a == aw.b.ad) {
                this.d.a(a, hashMap.get("click_url"));
            }
            else if (a == aw.b.af) {
                this.d.a(a, hashMap.get("click_url"));
            }
            else if (a == aw.b.Z) {
                this.d.f();
            }
            else {
                this.d.a(a);
            }
            this.a("trigger-event");
        }
        else {
            if (host.equals("trigger-error")) {
                this.a((String)hashMap.get("error_code"), (String)hashMap.get("description"));
                return;
            }
            if (host.equals("cancel-auto-skip")) {
                this.d.h();
                this.a("cancel-auto-skip");
            }
        }
    }
    
    private void e() {
        this.setScrollContainer(false);
        this.setBackgroundColor(0);
        this.setHorizontalScrollBarEnabled(false);
        this.setHorizontalScrollbarOverlay(false);
        this.setVerticalScrollBarEnabled(false);
        this.setVerticalScrollbarOverlay(false);
        final WebSettings settings = this.getSettings();
        settings.setSupportZoom(false);
        settings.setJavaScriptEnabled(true);
        if (Build$VERSION.SDK_INT >= 8) {
            settings.setPluginState(WebSettings$PluginState.ON);
        }
        settings.setAllowFileAccess(true);
        if (Build$VERSION.SDK_INT >= 11) {
            settings.setAllowContentAccess(true);
        }
        if (Build$VERSION.SDK_INT >= 16) {
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        this.d = this.a.k();
        this.setWebViewClient((WebViewClient)(this.b = new b()));
        this.setWebChromeClient((WebChromeClient)(this.c = new a()));
        this.f = false;
    }
    
    protected void a() {
        this.c("tremorcore.fireEvent('viewableChange','true');");
    }
    
    protected void a(final String s) {
        this.c("tremorcore.nativeCallComplete('" + s + "');");
    }
    
    public void a(final String s, final String s2) {
        ac.e("EmbedPlayer Error - " + s + ": " + s2);
        this.a("trigger-error");
    }
    
    protected void b() {
        this.c("tremorcore.fireEvent('zipChange','" + this.e.a + "');");
    }
    
    protected void b(final String s) {
        this.d.g();
        this.c("tremorcore.fireEvent('tmsDataChange'," + s + ");");
    }
    
    public void c() {
        this.setBackgroundColor(0);
        if (ac.r() > 10) {
            this.setLayerType(1, (Paint)null);
        }
        this.f = true;
    }
    
    protected void c(final String s) {
        if (s != null) {
            super.loadUrl("javascript:" + s);
        }
    }
    
    public void d() {
        synchronized (this) {
            new c().execute((Object[])new String[] { this.e.b().replace("__ZIP__", this.e.a).replace("__DATE__", this.e.b) });
        }
    }
    
    public void destroy() {
        this.clearCache(false);
        this.destroyDrawingCache();
    }
    
    private class a extends WebChromeClient
    {
        public void onConsoleMessage(final String s, final int n, final String s2) {
            Log.d("KK", "MSG: " + s + "; Line: " + n + "; Source: " + s2);
        }
        
        public boolean onConsoleMessage(final ConsoleMessage consoleMessage) {
            Log.d("KK", "MSG: " + consoleMessage.message() + "; Line: " + consoleMessage.lineNumber() + "; Source: " + consoleMessage.sourceId());
            return true;
        }
    }
    
    private class b extends WebViewClient
    {
        public void onPageFinished(final WebView webView, final String s) {
            if (!com.tremorvideo.sdk.android.b.d.this.f) {
                com.tremorvideo.sdk.android.b.d.this.c();
                com.tremorvideo.sdk.android.b.d.this.a();
                com.tremorvideo.sdk.android.b.d.this.b();
                com.tremorvideo.sdk.android.b.d.this.d();
            }
        }
        
        public void onReceivedError(final WebView webView, final int n, final String s, final String s2) {
            super.onReceivedError(webView, n, s, s2);
        }
        
        public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
            if (Uri.parse(s).getScheme().equals("tremorvideo")) {
                com.tremorvideo.sdk.android.b.d.this.a(URI.create(s));
                return true;
            }
            return false;
        }
    }
    
    class c extends AsyncTask<String, Void, String>
    {
        private String a(final InputStream ex) {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((InputStream)ex));
            final StringBuilder sb = new StringBuilder();
            try {
                Label_0077: {
                    try {
                        while (true) {
                            final String line = bufferedReader.readLine();
                            if (line == null) {
                                break Label_0077;
                            }
                            sb.append(line + "\n");
                        }
                    }
                    catch (IOException ex2) {
                        ex2.printStackTrace();
                        final IOException ex3 = ex;
                        ((InputStream)ex3).close();
                    }
                    try {
                        final IOException ex3 = ex;
                        ((InputStream)ex3).close();
                        return sb.toString();
                        try {
                            ((InputStream)ex).close();
                            return sb.toString();
                        }
                        catch (IOException ex) {
                            ex.printStackTrace();
                            return sb.toString();
                        }
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            finally {
                try {
                    ((InputStream)ex).close();
                }
                catch (IOException ex4) {
                    ex4.printStackTrace();
                }
            }
        }
        
        protected String a(final String... array) {
            final DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            try {
                final HttpGet httpGet = new HttpGet(new URI(array[0]));
                ae.a(httpGet, array[0]);
                final HttpEntity entity = ((HttpClient)defaultHttpClient).execute((HttpUriRequest)httpGet).getEntity();
                if (entity != null) {
                    final InputStream content = entity.getContent();
                    final String a = this.a(content);
                    content.close();
                    return a;
                }
            }
            catch (Exception ex) {
                ac.a(ex);
                return null;
            }
            return null;
        }
        
        protected void a(final String s) {
            com.tremorvideo.sdk.android.b.d.this.b(s);
        }
    }
}
