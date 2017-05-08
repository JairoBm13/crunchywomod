// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia.a;

import android.webkit.ValueCallback;
import android.webkit.ConsoleMessage;
import android.view.GestureDetector$SimpleOnGestureListener;
import android.os.AsyncTask;
import android.view.ViewConfiguration;
import android.view.MotionEvent;
import android.view.ViewGroup$LayoutParams;
import android.widget.RelativeLayout$LayoutParams;
import android.webkit.JavascriptInterface;
import android.graphics.RectF;
import android.graphics.Canvas;
import com.tremorvideo.sdk.android.videoad.bp;
import com.tremorvideo.sdk.android.videoad.at;
import android.annotation.SuppressLint;
import android.webkit.WebSettings;
import java.util.TimerTask;
import android.view.GestureDetector$OnGestureListener;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.os.Build$VERSION;
import android.webkit.WebSettings$PluginState;
import android.webkit.WebSettings$LayoutAlgorithm;
import android.graphics.Paint;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.scheme.Scheme;
import java.io.IOException;
import org.apache.http.params.HttpParams;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import java.util.Iterator;
import com.tremorvideo.sdk.android.videoad.Playvideo;
import com.tremorvideo.sdk.android.videoad.bx;
import android.content.Intent;
import android.net.Uri;
import java.net.URLEncoder;
import org.json.JSONArray;
import com.tremorvideo.sdk.android.videoad.ac;
import java.net.URLDecoder;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import java.util.Map;
import org.apache.http.NameValuePair;
import java.util.HashMap;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.client.HttpClient;
import java.net.URI;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicBoolean;
import android.view.GestureDetector;
import java.util.ArrayList;
import org.json.JSONObject;
import java.util.Timer;
import android.app.Activity;
import android.content.Context;
import com.tremorvideo.sdk.android.videoad.ax;
import android.webkit.WebView;

public class h extends WebView implements ax.e
{
    a a;
    com.tremorvideo.sdk.android.richmedia.a.e b;
    public float c;
    public boolean d;
    int e;
    Context f;
    Activity g;
    Timer h;
    JSONObject i;
    private boolean j;
    private JSONObject k;
    private ArrayList<b> l;
    private String m;
    private GestureDetector n;
    private AtomicBoolean o;
    private AtomicLong p;
    
    public h(final Context f, final int e, final String m, final JSONObject k, final JSONObject i) {
        super(f);
        this.c = 255.0f;
        this.d = false;
        this.e = -1;
        this.o = new AtomicBoolean(false);
        this.p = new AtomicLong(0L);
        this.f = f;
        this.g = (Activity)f;
        this.clearCache(true);
        this.e = e;
        this.m = m;
        this.k = k;
        this.i = i;
        this.d();
    }
    
    private void a(final URI uri) {
        final String host = uri.getHost();
        final List parse = URLEncodedUtils.parse(uri, "UTF-8");
        final HashMap<Object, String> hashMap = new HashMap<Object, String>();
        for (final NameValuePair nameValuePair : parse) {
            hashMap.put(nameValuePair.getName(), nameValuePair.getValue());
        }
        if (host.equals("url-request")) {
            ((Activity)this.getContext()).runOnUiThread((Runnable)new e((Map<String, String>)hashMap));
            this.a("url-request");
        }
        else {
            if (host.equals("fire-tracking")) {
                final String s = hashMap.get("event_tag");
                if (s != null) {
                    final ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
                    for (final Map.Entry<Object, Object> entry : hashMap.entrySet()) {
                        if (!entry.getKey().equals("event_tag")) {
                            list.add(new BasicNameValuePair((String)entry.getKey(), (String)entry.getValue()));
                        }
                    }
                    this.b.a(s, list);
                }
                this.a("fire-tracking");
                return;
            }
            if (host.equals("open-map-view")) {
                while (true) {
                    while (true) {
                        try {
                            if (hashMap.get("locations") != null) {
                                final String decode = URLDecoder.decode(hashMap.get("locations"), "UTF-8");
                                ac.e("locations:" + decode);
                                if (decode == null || decode.length() <= 0) {
                                    break;
                                }
                                final JSONObject jsonObject = new JSONArray(decode).getJSONObject(0);
                                if (jsonObject.has("long") && jsonObject.has("lat")) {
                                    final double double1 = jsonObject.getDouble("long");
                                    final double double2 = jsonObject.getDouble("lat");
                                    final StringBuilder sb = new StringBuilder("geo:");
                                    sb.append(double2);
                                    sb.append(",");
                                    sb.append(double1);
                                    sb.append("?q=");
                                    sb.append(double2);
                                    sb.append(",");
                                    sb.append(double1);
                                    sb.append("(");
                                    if (jsonObject.has("name")) {
                                        sb.append(URLEncoder.encode(jsonObject.getString("name"), "UTF-8"));
                                    }
                                    sb.append("+");
                                    if (jsonObject.has("address")) {
                                        sb.append(URLEncoder.encode(jsonObject.getString("address"), "UTF-8"));
                                    }
                                    sb.append(")");
                                    sb.append("&z=16");
                                    this.a("open-map-view");
                                    this.g.startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse(sb.toString())), 12);
                                    bx.c();
                                }
                                return;
                            }
                        }
                        catch (Exception ex) {
                            this.a("open-map-view");
                            return;
                        }
                        final String decode = null;
                        continue;
                    }
                }
                this.a("open-map-view");
                return;
            }
            if (host.equals("open-web-view")) {
                this.a("open-web-view");
                try {
                    final String s2 = hashMap.get("url");
                    if (s2 != null && s2.length() > 0) {
                        final Intent intent = new Intent((Context)this.g, (Class)Playvideo.class);
                        intent.putExtra("tremorVideoType", "webview");
                        intent.putExtra("tremorVideoURL", URLDecoder.decode(s2, "UTF-8"));
                        intent.putExtra("curEventID", -1);
                        this.g.startActivityForResult(intent, 12);
                    }
                    return;
                }
                catch (Exception ex2) {
                    return;
                }
            }
            if (host.equals("open-url")) {
                this.a("open-url");
                try {
                    final String s3 = hashMap.get("url");
                    if (s3 != null && s3.length() > 0) {
                        String s5;
                        final String s4 = s5 = URLDecoder.decode(s3, "UTF-8");
                        if (!s4.startsWith("http")) {
                            s5 = "http://" + s4;
                        }
                        final Intent intent2 = new Intent("android.intent.action.VIEW");
                        intent2.setData(Uri.parse(s5));
                        this.g.startActivityForResult(intent2, 12);
                        bx.c();
                    }
                    return;
                }
                catch (Exception ex3) {
                    return;
                }
            }
            if (host.equals("show-zip-picker")) {
                this.a("show-zip-picker");
                this.b.a(this);
                return;
            }
            if (host.equals("trigger-pause-ad")) {
                this.a("trigger-pause-ad");
                return;
            }
            if (host.equals("trigger-resume-ad")) {
                this.a("trigger-resume-ad");
                return;
            }
            if (host.equals("cancel-request")) {
                if (this.l != null) {
                    for (final b b : this.l) {
                        b.cancel(true);
                        b.a();
                    }
                }
                this.a("cancel-request");
                return;
            }
            this.a(host);
        }
    }
    
    private void a(final HttpClient httpClient) {
        if (ac.r() < 14) {
            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", (SocketFactory)new LayeredSocketFactory() {
                SSLSocketFactory a = SSLSocketFactory.getSocketFactory();
                
                private void a(final Socket socket, final String s) {
                    try {
                        final Field declaredField = InetAddress.class.getDeclaredField("hostName");
                        declaredField.setAccessible(true);
                        declaredField.set(socket.getInetAddress(), s);
                    }
                    catch (Exception ex) {}
                }
                
                public Socket connectSocket(final Socket socket, final String s, final int n, final InetAddress inetAddress, final int n2, final HttpParams httpParams) throws IOException {
                    return this.a.connectSocket(socket, s, n, inetAddress, n2, httpParams);
                }
                
                public Socket createSocket() throws IOException {
                    return this.a.createSocket();
                }
                
                public Socket createSocket(final Socket socket, final String s, final int n, final boolean b) throws IOException {
                    this.a(socket, s);
                    return this.a.createSocket(socket, s, n, b);
                }
                
                public boolean isSecure(final Socket socket) throws IllegalArgumentException {
                    return this.a.isSecure(socket);
                }
            }, 443));
        }
    }
    
    @SuppressLint({ "NewApi" })
    private void d() {
        this.setScrollContainer(false);
        this.setBackgroundColor(0);
        this.setHorizontalScrollBarEnabled(false);
        this.setHorizontalScrollbarOverlay(false);
        this.setVerticalScrollBarEnabled(false);
        this.setVerticalScrollbarOverlay(false);
        this.requestFocus(130);
        this.setBackgroundColor(0);
        if (ac.r() > 10) {
            this.setLayerType(1, (Paint)null);
        }
        final WebSettings settings = this.getSettings();
        settings.setSupportZoom(false);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(2);
        this.getSettings().setLayoutAlgorithm(WebSettings$LayoutAlgorithm.NORMAL);
        if (ac.r() >= 8) {
            settings.setPluginState(WebSettings$PluginState.ON);
        }
        settings.setAllowFileAccess(true);
        if (Build$VERSION.SDK_INT >= 11) {
            settings.setAllowContentAccess(true);
        }
        if (ac.r() >= 16) {
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        this.setWebViewClient((WebViewClient)(this.a = new a()));
        this.setWebChromeClient((WebChromeClient)new d());
        this.l = new ArrayList<b>();
        this.j = false;
        this.n = new GestureDetector(this.f, (GestureDetector$OnGestureListener)new c());
        this.addJavascriptInterface((Object)this, "AndroidDevice");
        final Iterator<b> iterator = this.l.iterator();
        while (iterator.hasNext()) {
            iterator.next().cancel(true);
        }
        this.l.clear();
        this.j = false;
        (this.h = new Timer()).schedule(new g(this.m), 100L);
    }
    
    public int a() {
        return this.e;
    }
    
    @SuppressLint({ "NewApi" })
    public void a(final float c) {
        if (ac.r() > 10 && c != this.c) {
            this.setAlpha(c / 255.0f);
        }
        this.c = c;
    }
    
    @SuppressLint({ "NewApi" })
    public void a(final int n, final int n2) {
        if (Build$VERSION.SDK_INT < 11) {
            this.offsetLeftAndRight(n);
            this.offsetTopAndBottom(n2);
            return;
        }
        this.setX((float)n);
        this.setY((float)n2);
    }
    
    public void a(final com.tremorvideo.sdk.android.richmedia.a.e b) {
        this.b = b;
    }
    
    public void a(final b b) {
        if (this.l != null && this.l.contains(b)) {
            this.l.remove(b);
        }
    }
    
    protected void a(final String s) {
        this.e("TMWI.nativeCallComplete('" + s + "');");
    }
    
    @SuppressLint({ "NewApi" })
    public void b() {
        this.setBackgroundColor(0);
        if (ac.r() > 10) {
            this.setLayerType(1, (Paint)null);
        }
        this.onSizeChanged(this.getWidth(), this.getHeight(), 0, 0);
    }
    
    protected void b(final String s) {
        this.e("TMWI.urlRequestDidFinishLoading(" + s + ");");
    }
    
    protected void c() {
        while (true) {
            Label_0294: {
                try {
                    this.k.put("AppID", (Object)ac.c);
                    final Context x = ac.x();
                    final at a = at.a();
                    this.k.put("OS", (Object)a.d);
                    this.k.put("OS_version", (Object)a.e);
                    this.k.put("carrier", (Object)a.m);
                    this.k.put("connection", (Object)a.i);
                    this.k.put("make", (Object)a.b);
                    this.k.put("sdk_version", (Object)a.a);
                    this.k.put("udid", (Object)a.f);
                    this.k.put("opt-out", a.g);
                    this.k.put("androidID", (Object)a.h);
                    this.k.put("AndroidVersion", ac.r());
                    this.k.put("AndroidTargetVersion", ac.s());
                    this.k.put("canCall", ac.t());
                    if (this.i != null) {
                        final Iterator keys = this.i.keys();
                        while (keys.hasNext()) {
                            final String s = keys.next();
                            this.k.put(s, this.i.get(s));
                        }
                    }
                    break Label_0294;
                }
                catch (Exception ex2) {
                    ac.e("Failed to add additional parameters");
                }
                this.e("TMWI.fireEvent('init'," + this.k + ");");
                return;
                try {
                    final Context x;
                    bp.a(x, true);
                    this.k.put("Lat", bp.a);
                    this.k.put("Long", bp.b);
                }
                catch (Exception ex) {
                    ac.a(ex);
                }
            }
            continue;
        }
    }
    
    protected void c(final String s) {
        this.e("TMWI.urlRequestDidFailWithError(" + s + ");");
    }
    
    protected void d(final String s) {
        this.e("TMWI.fireEvent('frameChange'," + s + ");");
    }
    
    public void destroy() {
        this.d = true;
        final Iterator<b> iterator = this.l.iterator();
        while (iterator.hasNext()) {
            iterator.next().cancel(true);
        }
        this.l.clear();
        this.l = null;
        this.k = null;
        this.j = false;
        super.destroyDrawingCache();
        super.destroy();
    }
    
    public void draw(final Canvas canvas) {
        if (ac.r() < 11) {
            canvas.saveLayerAlpha((RectF)null, Math.round(this.c), 31);
            super.draw(canvas);
            canvas.restore();
            return;
        }
        super.draw(canvas);
    }
    
    protected void e(final String s) {
        if (s != null) {
            ((Activity)this.getContext()).runOnUiThread((Runnable)new f(s));
        }
    }
    
    @JavascriptInterface
    public void executeSDKCall(final String s) {
        this.a(URI.create(s));
    }
    
    public void f(final String s) {
        ac.e("GenericWebView:onZipChanged zip=" + s);
        this.e("TMWI.fireEvent('onZipPicked','" + s + "');");
    }
    
    public void loadUrl(final String s) {
        if (!this.d) {
            super.loadUrl(s);
        }
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        Label_0131: {
            if (!this.j) {
                break Label_0131;
            }
            final JSONObject jsonObject = new JSONObject();
            while (true) {
                try {
                    final ViewGroup$LayoutParams layoutParams = this.getLayoutParams();
                    if (layoutParams instanceof RelativeLayout$LayoutParams) {
                        jsonObject.put("x", ((RelativeLayout$LayoutParams)layoutParams).leftMargin);
                        jsonObject.put("y", ((RelativeLayout$LayoutParams)layoutParams).topMargin);
                        jsonObject.put("width", ((RelativeLayout$LayoutParams)layoutParams).width);
                        jsonObject.put("height", ((RelativeLayout$LayoutParams)layoutParams).height);
                    }
                    jsonObject.put("AndroidTargetVersion", ac.s());
                    jsonObject.put("AndroidVersion", ac.r());
                    this.d(jsonObject.toString());
                    super.onSizeChanged(n, n2, n3, n4);
                }
                catch (Exception ex) {
                    ac.a(ex);
                    continue;
                }
                break;
            }
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final boolean b = true;
        this.requestFocus(130);
        boolean onTouchEvent = b;
        if (motionEvent.getPointerId((motionEvent.getAction() & 0xFF00) >> 8) == 0) {
            this.n.onTouchEvent(motionEvent);
            if (this.o.get()) {
                onTouchEvent = b;
                if (System.currentTimeMillis() - this.p.get() <= ViewConfiguration.getDoubleTapTimeout()) {
                    return onTouchEvent;
                }
                this.o.set(false);
            }
            onTouchEvent = super.onTouchEvent(motionEvent);
        }
        return onTouchEvent;
    }
    
    private class a extends WebViewClient
    {
        public void onPageFinished(WebView webView, final String s) {
            if (!s.equals(com.tremorvideo.sdk.android.richmedia.a.h.this.m) || com.tremorvideo.sdk.android.richmedia.a.h.this.j) {
                return;
            }
            webView = (WebView)new JSONObject();
            while (true) {
                try {
                    final ViewGroup$LayoutParams layoutParams = com.tremorvideo.sdk.android.richmedia.a.h.this.getLayoutParams();
                    if (layoutParams instanceof RelativeLayout$LayoutParams) {
                        ((JSONObject)webView).put("x", ((RelativeLayout$LayoutParams)layoutParams).leftMargin);
                        ((JSONObject)webView).put("y", ((RelativeLayout$LayoutParams)layoutParams).topMargin);
                        ((JSONObject)webView).put("width", layoutParams.width);
                        ((JSONObject)webView).put("height", layoutParams.height);
                        ((JSONObject)webView).put("AndroidVersion", ac.r());
                        ((JSONObject)webView).put("AndroidTargetVersion", ac.s());
                        ((JSONObject)webView).put("canCall", ac.t());
                    }
                    com.tremorvideo.sdk.android.richmedia.a.h.this.d(((JSONObject)webView).toString());
                    com.tremorvideo.sdk.android.richmedia.a.h.this.j = true;
                    com.tremorvideo.sdk.android.richmedia.a.h.this.b();
                    new Thread() {
                        @Override
                        public void run() {
                            com.tremorvideo.sdk.android.richmedia.a.h.this.c();
                        }
                    }.start();
                }
                catch (Exception ex) {
                    ac.a(ex);
                    continue;
                }
                break;
            }
        }
        
        public void onReceivedError(final WebView webView, final int n, final String s, final String s2) {
            super.onReceivedError(webView, n, s, s2);
        }
        
        public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
            if (s.indexOf("tel:") > -1) {
                com.tremorvideo.sdk.android.richmedia.a.h.this.g.startActivity(new Intent("android.intent.action.DIAL", Uri.parse(s)));
                return true;
            }
            if (Uri.parse(s).getScheme().equals("tmwi")) {
                com.tremorvideo.sdk.android.richmedia.a.h.this.a(URI.create(s));
                return true;
            }
            return false;
        }
    }
    
    class b extends AsyncTask<String, Void, String>
    {
        String a;
        private boolean c;
        private boolean d;
        private boolean e;
        
        b() {
            this.c = false;
            this.d = false;
            this.e = false;
        }
        
        protected String a(final String... p0) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: aload_0        
            //     1: aconst_null    
            //     2: putfield        com/tremorvideo/sdk/android/richmedia/a/h$b.a:Ljava/lang/String;
            //     5: new             Lorg/apache/http/params/BasicHttpParams;
            //     8: dup            
            //     9: invokespecial   org/apache/http/params/BasicHttpParams.<init>:()V
            //    12: astore_2       
            //    13: aload_2        
            //    14: ldc             "http.protocol.version"
            //    16: getstatic       org/apache/http/HttpVersion.HTTP_1_1:Lorg/apache/http/HttpVersion;
            //    19: invokeinterface org/apache/http/params/HttpParams.setParameter:(Ljava/lang/String;Ljava/lang/Object;)Lorg/apache/http/params/HttpParams;
            //    24: pop            
            //    25: aload_2        
            //    26: iconst_1       
            //    27: invokestatic    org/apache/http/params/HttpConnectionParams.setTcpNoDelay:(Lorg/apache/http/params/HttpParams;Z)V
            //    30: new             Lorg/apache/http/impl/client/DefaultHttpClient;
            //    33: dup            
            //    34: aload_2        
            //    35: invokespecial   org/apache/http/impl/client/DefaultHttpClient.<init>:(Lorg/apache/http/params/HttpParams;)V
            //    38: astore_3       
            //    39: aload_0        
            //    40: getfield        com/tremorvideo/sdk/android/richmedia/a/h$b.b:Lcom/tremorvideo/sdk/android/richmedia/a/h;
            //    43: aload_3        
            //    44: invokestatic    com/tremorvideo/sdk/android/richmedia/a/h.a:(Lcom/tremorvideo/sdk/android/richmedia/a/h;Lorg/apache/http/client/HttpClient;)V
            //    47: aload_1        
            //    48: iconst_1       
            //    49: aaload         
            //    50: ldc             "POST"
            //    52: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
            //    55: ifeq            242
            //    58: aload_1        
            //    59: iconst_0       
            //    60: aaload         
            //    61: astore_2       
            //    62: new             Lorg/apache/http/client/methods/HttpPost;
            //    65: dup            
            //    66: new             Ljava/net/URI;
            //    69: dup            
            //    70: aload_2        
            //    71: invokespecial   java/net/URI.<init>:(Ljava/lang/String;)V
            //    74: invokespecial   org/apache/http/client/methods/HttpPost.<init>:(Ljava/net/URI;)V
            //    77: astore          4
            //    79: aload           4
            //    81: aload_2        
            //    82: invokestatic    com/tremorvideo/sdk/android/richmedia/ae.a:(Lorg/apache/http/client/methods/HttpPost;Ljava/lang/String;)V
            //    85: new             Lorg/json/JSONObject;
            //    88: dup            
            //    89: aload_1        
            //    90: iconst_2       
            //    91: aaload         
            //    92: invokespecial   org/json/JSONObject.<init>:(Ljava/lang/String;)V
            //    95: astore          5
            //    97: aload           5
            //    99: invokevirtual   org/json/JSONObject.keys:()Ljava/util/Iterator;
            //   102: astore          6
            //   104: aload           6
            //   106: invokeinterface java/util/Iterator.hasNext:()Z
            //   111: ifeq            176
            //   114: aload           6
            //   116: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
            //   121: checkcast       Ljava/lang/String;
            //   124: astore          7
            //   126: aload           5
            //   128: aload           7
            //   130: invokevirtual   org/json/JSONObject.getString:(Ljava/lang/String;)Ljava/lang/String;
            //   133: astore_2       
            //   134: aload_2        
            //   135: ifnull          104
            //   138: aload           4
            //   140: aload           7
            //   142: aload_2        
            //   143: invokevirtual   org/apache/http/client/methods/HttpPost.addHeader:(Ljava/lang/String;Ljava/lang/String;)V
            //   146: goto            104
            //   149: astore_1       
            //   150: aload_1        
            //   151: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Ljava/lang/Throwable;)V
            //   154: aload_0        
            //   155: iconst_1       
            //   156: putfield        com/tremorvideo/sdk/android/richmedia/a/h$b.d:Z
            //   159: aload_0        
            //   160: iconst_1       
            //   161: putfield        com/tremorvideo/sdk/android/richmedia/a/h$b.e:Z
            //   164: aconst_null    
            //   165: areturn        
            //   166: astore_2       
            //   167: aload_2        
            //   168: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Ljava/lang/Throwable;)V
            //   171: aconst_null    
            //   172: astore_2       
            //   173: goto            134
            //   176: aload           4
            //   178: new             Lorg/apache/http/entity/StringEntity;
            //   181: dup            
            //   182: aload_1        
            //   183: iconst_3       
            //   184: aaload         
            //   185: ldc             "UTF-8"
            //   187: invokespecial   org/apache/http/entity/StringEntity.<init>:(Ljava/lang/String;Ljava/lang/String;)V
            //   190: invokevirtual   org/apache/http/client/methods/HttpPost.setEntity:(Lorg/apache/http/HttpEntity;)V
            //   193: aload_3        
            //   194: aload           4
            //   196: invokeinterface org/apache/http/client/HttpClient.execute:(Lorg/apache/http/client/methods/HttpUriRequest;)Lorg/apache/http/HttpResponse;
            //   201: astore_1       
            //   202: aload_0        
            //   203: aload_1        
            //   204: invokeinterface org/apache/http/HttpResponse.getEntity:()Lorg/apache/http/HttpEntity;
            //   209: invokestatic    org/apache/http/util/EntityUtils.toString:(Lorg/apache/http/HttpEntity;)Ljava/lang/String;
            //   212: putfield        com/tremorvideo/sdk/android/richmedia/a/h$b.a:Ljava/lang/String;
            //   215: aload_0        
            //   216: getfield        com/tremorvideo/sdk/android/richmedia/a/h$b.d:Z
            //   219: ifne            227
            //   222: aload_0        
            //   223: iconst_1       
            //   224: putfield        com/tremorvideo/sdk/android/richmedia/a/h$b.d:Z
            //   227: aload_1        
            //   228: invokeinterface org/apache/http/HttpResponse.getStatusLine:()Lorg/apache/http/StatusLine;
            //   233: invokeinterface org/apache/http/StatusLine.getStatusCode:()I
            //   238: invokestatic    java/lang/Integer.toString:(I)Ljava/lang/String;
            //   241: areturn        
            //   242: aload_1        
            //   243: iconst_0       
            //   244: aaload         
            //   245: astore          4
            //   247: new             Lorg/apache/http/client/methods/HttpGet;
            //   250: dup            
            //   251: new             Ljava/net/URI;
            //   254: dup            
            //   255: aload           4
            //   257: invokespecial   java/net/URI.<init>:(Ljava/lang/String;)V
            //   260: invokespecial   org/apache/http/client/methods/HttpGet.<init>:(Ljava/net/URI;)V
            //   263: astore_2       
            //   264: aload_2        
            //   265: aload           4
            //   267: invokestatic    com/tremorvideo/sdk/android/richmedia/ae.a:(Lorg/apache/http/client/methods/HttpGet;Ljava/lang/String;)V
            //   270: new             Lorg/json/JSONObject;
            //   273: dup            
            //   274: aload_1        
            //   275: iconst_2       
            //   276: aaload         
            //   277: invokespecial   org/json/JSONObject.<init>:(Ljava/lang/String;)V
            //   280: astore          4
            //   282: aload           4
            //   284: invokevirtual   org/json/JSONObject.keys:()Ljava/util/Iterator;
            //   287: astore          5
            //   289: aload           5
            //   291: invokeinterface java/util/Iterator.hasNext:()Z
            //   296: ifeq            343
            //   299: aload           5
            //   301: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
            //   306: checkcast       Ljava/lang/String;
            //   309: astore          6
            //   311: aload           4
            //   313: aload           6
            //   315: invokevirtual   org/json/JSONObject.getString:(Ljava/lang/String;)Ljava/lang/String;
            //   318: astore_1       
            //   319: aload_1        
            //   320: ifnull          289
            //   323: aload_2        
            //   324: aload           6
            //   326: aload_1        
            //   327: invokevirtual   org/apache/http/client/methods/HttpGet.addHeader:(Ljava/lang/String;Ljava/lang/String;)V
            //   330: goto            289
            //   333: astore_1       
            //   334: aload_1        
            //   335: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Ljava/lang/Throwable;)V
            //   338: aconst_null    
            //   339: astore_1       
            //   340: goto            319
            //   343: aload_3        
            //   344: aload_2        
            //   345: invokeinterface org/apache/http/client/HttpClient.execute:(Lorg/apache/http/client/methods/HttpUriRequest;)Lorg/apache/http/HttpResponse;
            //   350: astore_1       
            //   351: aload_0        
            //   352: aload_1        
            //   353: invokeinterface org/apache/http/HttpResponse.getEntity:()Lorg/apache/http/HttpEntity;
            //   358: invokestatic    org/apache/http/util/EntityUtils.toString:(Lorg/apache/http/HttpEntity;)Ljava/lang/String;
            //   361: putfield        com/tremorvideo/sdk/android/richmedia/a/h$b.a:Ljava/lang/String;
            //   364: aload_0        
            //   365: getfield        com/tremorvideo/sdk/android/richmedia/a/h$b.d:Z
            //   368: ifne            376
            //   371: aload_0        
            //   372: iconst_1       
            //   373: putfield        com/tremorvideo/sdk/android/richmedia/a/h$b.d:Z
            //   376: aload_1        
            //   377: invokeinterface org/apache/http/HttpResponse.getStatusLine:()Lorg/apache/http/StatusLine;
            //   382: invokeinterface org/apache/http/StatusLine.getStatusCode:()I
            //   387: invokestatic    java/lang/Integer.toString:(I)Ljava/lang/String;
            //   390: astore_1       
            //   391: aload_1        
            //   392: areturn        
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  0      58     149    166    Ljava/lang/Exception;
            //  62     104    149    166    Ljava/lang/Exception;
            //  104    126    149    166    Ljava/lang/Exception;
            //  126    134    166    176    Ljava/lang/Exception;
            //  138    146    149    166    Ljava/lang/Exception;
            //  167    171    149    166    Ljava/lang/Exception;
            //  176    227    149    166    Ljava/lang/Exception;
            //  227    242    149    166    Ljava/lang/Exception;
            //  247    289    149    166    Ljava/lang/Exception;
            //  289    311    149    166    Ljava/lang/Exception;
            //  311    319    333    343    Ljava/lang/Exception;
            //  323    330    149    166    Ljava/lang/Exception;
            //  334    338    149    166    Ljava/lang/Exception;
            //  343    376    149    166    Ljava/lang/Exception;
            //  376    391    149    166    Ljava/lang/Exception;
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
            //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
            //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
            //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:556)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        public void a() {
            this.d = true;
            this.c = true;
            com.tremorvideo.sdk.android.richmedia.a.h.this.a(this);
        }
        
        protected void a(final String s) {
            if (!this.d) {
                this.d = true;
            }
            if (!this.isCancelled() && !this.c) {
                if (s != null && Integer.parseInt(s) == 200) {
                    com.tremorvideo.sdk.android.richmedia.a.h.this.b(this.a);
                }
                else if (!this.e) {
                    com.tremorvideo.sdk.android.richmedia.a.h.this.c(this.a);
                }
            }
            com.tremorvideo.sdk.android.richmedia.a.h.this.a(this);
        }
        
        protected void b(final String s) {
            if (!this.d) {
                this.d = true;
            }
            com.tremorvideo.sdk.android.richmedia.a.h.this.a(this);
        }
    }
    
    private class c extends GestureDetector$SimpleOnGestureListener
    {
        public boolean onDoubleTap(final MotionEvent motionEvent) {
            com.tremorvideo.sdk.android.richmedia.a.h.this.o.set(true);
            com.tremorvideo.sdk.android.richmedia.a.h.this.p.set(System.currentTimeMillis());
            return true;
        }
        
        public boolean onDoubleTapEvent(final MotionEvent motionEvent) {
            com.tremorvideo.sdk.android.richmedia.a.h.this.o.set(true);
            com.tremorvideo.sdk.android.richmedia.a.h.this.p.set(System.currentTimeMillis());
            return true;
        }
    }
    
    private class d extends WebChromeClient
    {
        public void onConsoleMessage(final String s, final int n, final String s2) {
            ac.e("GenericWebview Console: " + s);
        }
        
        public boolean onConsoleMessage(final ConsoleMessage consoleMessage) {
            ac.e("GenericWebview Console: " + consoleMessage.message());
            return false;
        }
    }
    
    public class e implements Runnable
    {
        Map<String, String> a;
        
        public e(final Map<String, String> a) {
            this.a = a;
        }
        
        @Override
        public void run() {
            final b b = new b();
            com.tremorvideo.sdk.android.richmedia.a.h.this.l.add(b);
            if (this.a.containsKey("data")) {
                b.execute((Object[])new String[] { this.a.get("url"), this.a.get("type"), this.a.get("header"), this.a.get("data") });
                return;
            }
            b.execute((Object[])new String[] { this.a.get("url"), this.a.get("type"), this.a.get("header") });
        }
    }
    
    public class f implements Runnable
    {
        String a;
        
        public f(final String a) {
            this.a = a;
        }
        
        @SuppressLint({ "NewApi" })
        @Override
        public void run() {
            if (ac.r() < 19) {
                com.tremorvideo.sdk.android.richmedia.a.h.this.loadUrl("javascript:" + this.a);
                return;
            }
            com.tremorvideo.sdk.android.richmedia.a.h.this.evaluateJavascript(this.a, (ValueCallback)null);
        }
    }
    
    class g extends TimerTask
    {
        String a;
        
        public g(final String a) {
            this.a = a;
        }
        
        @Override
        public void run() {
            com.tremorvideo.sdk.android.richmedia.a.h.this.h.purge();
            com.tremorvideo.sdk.android.richmedia.a.h.this.h.cancel();
            com.tremorvideo.sdk.android.richmedia.a.h.this.h = null;
            com.tremorvideo.sdk.android.richmedia.a.h.this.g.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    if (!com.tremorvideo.sdk.android.richmedia.a.h.this.d) {
                        com.tremorvideo.sdk.android.richmedia.a.h.this.loadUrl(g.this.a);
                    }
                }
            });
        }
    }
}
