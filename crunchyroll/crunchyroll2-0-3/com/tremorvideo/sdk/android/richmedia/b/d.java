// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia.b;

import android.webkit.ConsoleMessage;
import android.os.AsyncTask;
import android.net.Uri;
import android.graphics.RectF;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.widget.RelativeLayout$LayoutParams;
import android.graphics.Paint;
import android.webkit.WebSettings;
import android.webkit.WebSettings$LayoutAlgorithm;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.webkit.WebSettings$PluginState;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.scheme.Scheme;
import java.io.IOException;
import org.apache.http.params.HttpParams;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import com.tremorvideo.sdk.android.videoad.ac;
import java.util.Iterator;
import java.util.List;
import com.tremorvideo.sdk.android.videoad.aw;
import java.util.Map;
import android.app.Activity;
import org.apache.http.NameValuePair;
import java.util.HashMap;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.client.HttpClient;
import java.net.URI;
import android.content.Context;
import java.util.ArrayList;
import org.json.JSONObject;
import android.webkit.WebView;

public class d extends WebView
{
    a a;
    com.tremorvideo.sdk.android.richmedia.b.b b;
    public float c;
    public boolean d;
    private boolean e;
    private JSONObject f;
    private ArrayList<b> g;
    private String h;
    
    public d(final Context context) {
        super(context);
        this.c = 255.0f;
        this.d = false;
        this.clearCache(true);
        this.c();
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
        }
        else {
            if (host.equals("trigger-event")) {
                final aw.b a = aw.b.a(hashMap.get("event_type"));
                if (a == aw.b.ao) {
                    this.b.a(a, hashMap.get("url"));
                }
                if (a == aw.b.an) {
                    this.b.a(a, hashMap.get("url"));
                }
                if (a == aw.b.ap && hashMap.containsKey("id")) {
                    this.b.a((String)hashMap.get("id"));
                }
                this.a("trigger-event");
                return;
            }
            if (host.equals("cancel-request") && this.g != null) {
                for (final b b : this.g) {
                    b.cancel(true);
                    b.a();
                }
            }
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
    
    private void c() {
        this.setScrollContainer(false);
        this.setBackgroundColor(0);
        this.setHorizontalScrollBarEnabled(false);
        this.setHorizontalScrollbarOverlay(false);
        this.setVerticalScrollBarEnabled(false);
        this.setVerticalScrollbarOverlay(false);
        final WebSettings settings = this.getSettings();
        settings.setSupportZoom(false);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setCacheMode(2);
        if (ac.r() >= 8) {
            settings.setPluginState(WebSettings$PluginState.ON);
        }
        settings.setAllowFileAccess(true);
        if (ac.r() >= 16) {
            settings.setAllowFileAccessFromFileURLs(true);
        }
        this.setWebViewClient((WebViewClient)(this.a = new a()));
        this.setWebChromeClient((WebChromeClient)new c());
        this.getSettings().setLayoutAlgorithm(WebSettings$LayoutAlgorithm.NORMAL);
        this.g = new ArrayList<b>();
        this.e = false;
    }
    
    public void a() {
        this.setBackgroundColor(0);
        if (ac.r() > 10) {
            this.setLayerType(1, (Paint)null);
        }
        this.onSizeChanged(this.getWidth(), this.getHeight(), 0, 0);
    }
    
    public void a(final float c) {
        if (ac.r() > 10 && c != this.c) {
            this.setAlpha(c / 255.0f);
        }
        this.c = c;
    }
    
    public void a(final com.tremorvideo.sdk.android.richmedia.b.b b) {
        this.b = b;
    }
    
    public void a(final b b) {
        if (this.g != null && this.g.contains(b)) {
            this.g.remove(b);
        }
    }
    
    protected void a(final String s) {
        this.e("tremorcore.nativeCallComplete('" + s + "');");
    }
    
    public void a(final String h, final JSONObject f) {
        final Iterator<b> iterator = this.g.iterator();
        while (iterator.hasNext()) {
            iterator.next().cancel(true);
        }
        this.g.clear();
        this.e = false;
        this.h = h;
        this.f = f;
        this.loadUrl(h);
    }
    
    protected void b() {
        final JSONObject jsonObject = new JSONObject();
        while (true) {
            try {
                final ViewGroup$LayoutParams layoutParams = this.getLayoutParams();
                if (layoutParams instanceof RelativeLayout$LayoutParams) {
                    jsonObject.put("x", ((RelativeLayout$LayoutParams)layoutParams).leftMargin);
                    jsonObject.put("y", ((RelativeLayout$LayoutParams)layoutParams).topMargin);
                    jsonObject.put("width", layoutParams.width);
                    jsonObject.put("height", layoutParams.height);
                    if (ac.r() < 14) {
                        jsonObject.put("Android2Fix", 1);
                    }
                }
                this.d(jsonObject.toString());
                this.e("tremorcore.fireEvent('init'," + this.f + ");");
            }
            catch (Exception ex) {
                ac.a(ex);
                continue;
            }
            break;
        }
    }
    
    protected void b(final String s) {
        this.e("tremorcore.urlRequestDidFinishLoading(" + s + ");");
    }
    
    protected void c(final String s) {
        this.e("tremorcore.urlRequestDidFailWithError(" + s + ");");
    }
    
    protected void d(final String s) {
        this.e("tremorcore.fireEvent('frameChange'," + s + ");");
    }
    
    public void destroy() {
        this.d = true;
        final Iterator<b> iterator = this.g.iterator();
        while (iterator.hasNext()) {
            iterator.next().cancel(true);
        }
        this.g.clear();
        this.g = null;
        this.f = null;
        this.e = false;
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
            ((Activity)this.getContext()).runOnUiThread((Runnable)new d(s));
        }
    }
    
    public void loadUrl(final String s) {
        if (!this.d) {
            super.loadUrl(s);
        }
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        Label_0111: {
            if (!this.e) {
                break Label_0111;
            }
            final JSONObject jsonObject = new JSONObject();
            while (true) {
                try {
                    final ViewGroup$LayoutParams layoutParams = this.getLayoutParams();
                    if (layoutParams instanceof RelativeLayout$LayoutParams) {
                        jsonObject.put("x", ((RelativeLayout$LayoutParams)layoutParams).leftMargin);
                        jsonObject.put("y", ((RelativeLayout$LayoutParams)layoutParams).topMargin);
                    }
                    jsonObject.put("width", n);
                    jsonObject.put("height", n2);
                    if (ac.r() < 14) {
                        jsonObject.put("Android2Fix", 1);
                    }
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
    
    private class a extends WebViewClient
    {
        public void onPageFinished(final WebView webView, final String s) {
            if (s.equals(com.tremorvideo.sdk.android.richmedia.b.d.this.h) && !com.tremorvideo.sdk.android.richmedia.b.d.this.e) {
                com.tremorvideo.sdk.android.richmedia.b.d.this.b();
                com.tremorvideo.sdk.android.richmedia.b.d.this.e = true;
                com.tremorvideo.sdk.android.richmedia.b.d.this.a();
            }
        }
        
        public void onReceivedError(final WebView webView, final int n, final String s, final String s2) {
            super.onReceivedError(webView, n, s, s2);
        }
        
        public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
            if (Uri.parse(s).getScheme().equals("tremorvideo")) {
                com.tremorvideo.sdk.android.richmedia.b.d.this.a(URI.create(s));
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
            //     2: putfield        com/tremorvideo/sdk/android/richmedia/b/d$b.a:Ljava/lang/String;
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
            //    40: getfield        com/tremorvideo/sdk/android/richmedia/b/d$b.b:Lcom/tremorvideo/sdk/android/richmedia/b/d;
            //    43: aload_3        
            //    44: invokestatic    com/tremorvideo/sdk/android/richmedia/b/d.a:(Lcom/tremorvideo/sdk/android/richmedia/b/d;Lorg/apache/http/client/HttpClient;)V
            //    47: aload_1        
            //    48: iconst_1       
            //    49: aaload         
            //    50: ldc             "POST"
            //    52: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
            //    55: ifeq            269
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
            //   111: ifeq            194
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
            //   156: putfield        com/tremorvideo/sdk/android/richmedia/b/d$b.d:Z
            //   159: aload_0        
            //   160: iconst_1       
            //   161: putfield        com/tremorvideo/sdk/android/richmedia/b/d$b.e:Z
            //   164: aload_0        
            //   165: getfield        com/tremorvideo/sdk/android/richmedia/b/d$b.b:Lcom/tremorvideo/sdk/android/richmedia/b/d;
            //   168: ldc             "url-request"
            //   170: invokevirtual   com/tremorvideo/sdk/android/richmedia/b/d.a:(Ljava/lang/String;)V
            //   173: aload_0        
            //   174: getfield        com/tremorvideo/sdk/android/richmedia/b/d$b.b:Lcom/tremorvideo/sdk/android/richmedia/b/d;
            //   177: ldc             ""
            //   179: invokevirtual   com/tremorvideo/sdk/android/richmedia/b/d.c:(Ljava/lang/String;)V
            //   182: aconst_null    
            //   183: areturn        
            //   184: astore_2       
            //   185: aload_2        
            //   186: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Ljava/lang/Throwable;)V
            //   189: aconst_null    
            //   190: astore_2       
            //   191: goto            134
            //   194: aload           4
            //   196: new             Lorg/apache/http/entity/StringEntity;
            //   199: dup            
            //   200: aload_1        
            //   201: iconst_3       
            //   202: aaload         
            //   203: ldc             "UTF-8"
            //   205: invokespecial   org/apache/http/entity/StringEntity.<init>:(Ljava/lang/String;Ljava/lang/String;)V
            //   208: invokevirtual   org/apache/http/client/methods/HttpPost.setEntity:(Lorg/apache/http/HttpEntity;)V
            //   211: aload_3        
            //   212: aload           4
            //   214: invokeinterface org/apache/http/client/HttpClient.execute:(Lorg/apache/http/client/methods/HttpUriRequest;)Lorg/apache/http/HttpResponse;
            //   219: astore_1       
            //   220: aload_0        
            //   221: aload_1        
            //   222: invokeinterface org/apache/http/HttpResponse.getEntity:()Lorg/apache/http/HttpEntity;
            //   227: invokestatic    org/apache/http/util/EntityUtils.toString:(Lorg/apache/http/HttpEntity;)Ljava/lang/String;
            //   230: putfield        com/tremorvideo/sdk/android/richmedia/b/d$b.a:Ljava/lang/String;
            //   233: aload_0        
            //   234: getfield        com/tremorvideo/sdk/android/richmedia/b/d$b.d:Z
            //   237: ifne            254
            //   240: aload_0        
            //   241: getfield        com/tremorvideo/sdk/android/richmedia/b/d$b.b:Lcom/tremorvideo/sdk/android/richmedia/b/d;
            //   244: ldc             "url-request"
            //   246: invokevirtual   com/tremorvideo/sdk/android/richmedia/b/d.a:(Ljava/lang/String;)V
            //   249: aload_0        
            //   250: iconst_1       
            //   251: putfield        com/tremorvideo/sdk/android/richmedia/b/d$b.d:Z
            //   254: aload_1        
            //   255: invokeinterface org/apache/http/HttpResponse.getStatusLine:()Lorg/apache/http/StatusLine;
            //   260: invokeinterface org/apache/http/StatusLine.getStatusCode:()I
            //   265: invokestatic    java/lang/Integer.toString:(I)Ljava/lang/String;
            //   268: areturn        
            //   269: new             Lorg/apache/http/client/methods/HttpGet;
            //   272: dup            
            //   273: new             Ljava/net/URI;
            //   276: dup            
            //   277: aload_1        
            //   278: iconst_0       
            //   279: aaload         
            //   280: invokespecial   java/net/URI.<init>:(Ljava/lang/String;)V
            //   283: invokespecial   org/apache/http/client/methods/HttpGet.<init>:(Ljava/net/URI;)V
            //   286: astore_2       
            //   287: new             Lorg/json/JSONObject;
            //   290: dup            
            //   291: aload_1        
            //   292: iconst_2       
            //   293: aaload         
            //   294: invokespecial   org/json/JSONObject.<init>:(Ljava/lang/String;)V
            //   297: astore          4
            //   299: aload           4
            //   301: invokevirtual   org/json/JSONObject.keys:()Ljava/util/Iterator;
            //   304: astore          5
            //   306: aload           5
            //   308: invokeinterface java/util/Iterator.hasNext:()Z
            //   313: ifeq            360
            //   316: aload           5
            //   318: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
            //   323: checkcast       Ljava/lang/String;
            //   326: astore          6
            //   328: aload           4
            //   330: aload           6
            //   332: invokevirtual   org/json/JSONObject.getString:(Ljava/lang/String;)Ljava/lang/String;
            //   335: astore_1       
            //   336: aload_1        
            //   337: ifnull          306
            //   340: aload_2        
            //   341: aload           6
            //   343: aload_1        
            //   344: invokevirtual   org/apache/http/client/methods/HttpGet.addHeader:(Ljava/lang/String;Ljava/lang/String;)V
            //   347: goto            306
            //   350: astore_1       
            //   351: aload_1        
            //   352: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Ljava/lang/Throwable;)V
            //   355: aconst_null    
            //   356: astore_1       
            //   357: goto            336
            //   360: aload_3        
            //   361: aload_2        
            //   362: invokeinterface org/apache/http/client/HttpClient.execute:(Lorg/apache/http/client/methods/HttpUriRequest;)Lorg/apache/http/HttpResponse;
            //   367: astore_1       
            //   368: aload_0        
            //   369: aload_1        
            //   370: invokeinterface org/apache/http/HttpResponse.getEntity:()Lorg/apache/http/HttpEntity;
            //   375: invokestatic    org/apache/http/util/EntityUtils.toString:(Lorg/apache/http/HttpEntity;)Ljava/lang/String;
            //   378: putfield        com/tremorvideo/sdk/android/richmedia/b/d$b.a:Ljava/lang/String;
            //   381: aload_0        
            //   382: getfield        com/tremorvideo/sdk/android/richmedia/b/d$b.d:Z
            //   385: ifne            402
            //   388: aload_0        
            //   389: getfield        com/tremorvideo/sdk/android/richmedia/b/d$b.b:Lcom/tremorvideo/sdk/android/richmedia/b/d;
            //   392: ldc             "url-request"
            //   394: invokevirtual   com/tremorvideo/sdk/android/richmedia/b/d.a:(Ljava/lang/String;)V
            //   397: aload_0        
            //   398: iconst_1       
            //   399: putfield        com/tremorvideo/sdk/android/richmedia/b/d$b.d:Z
            //   402: aload_1        
            //   403: invokeinterface org/apache/http/HttpResponse.getStatusLine:()Lorg/apache/http/StatusLine;
            //   408: invokeinterface org/apache/http/StatusLine.getStatusCode:()I
            //   413: invokestatic    java/lang/Integer.toString:(I)Ljava/lang/String;
            //   416: astore_1       
            //   417: aload_1        
            //   418: areturn        
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  0      58     149    184    Ljava/lang/Exception;
            //  62     104    149    184    Ljava/lang/Exception;
            //  104    126    149    184    Ljava/lang/Exception;
            //  126    134    184    194    Ljava/lang/Exception;
            //  138    146    149    184    Ljava/lang/Exception;
            //  185    189    149    184    Ljava/lang/Exception;
            //  194    254    149    184    Ljava/lang/Exception;
            //  254    269    149    184    Ljava/lang/Exception;
            //  269    306    149    184    Ljava/lang/Exception;
            //  306    328    149    184    Ljava/lang/Exception;
            //  328    336    350    360    Ljava/lang/Exception;
            //  340    347    149    184    Ljava/lang/Exception;
            //  351    355    149    184    Ljava/lang/Exception;
            //  360    402    149    184    Ljava/lang/Exception;
            //  402    417    149    184    Ljava/lang/Exception;
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
            com.tremorvideo.sdk.android.richmedia.b.d.this.a("url-request");
            this.c = true;
            com.tremorvideo.sdk.android.richmedia.b.d.this.a(this);
        }
        
        protected void a(final String s) {
            if (!this.d) {
                com.tremorvideo.sdk.android.richmedia.b.d.this.a("url-request");
                this.d = true;
            }
            if (!this.isCancelled() && !this.c) {
                if (s != null && Integer.parseInt(s) == 200) {
                    com.tremorvideo.sdk.android.richmedia.b.d.this.b(this.a);
                }
                else if (!this.e) {
                    com.tremorvideo.sdk.android.richmedia.b.d.this.c(this.a);
                }
            }
            com.tremorvideo.sdk.android.richmedia.b.d.this.a(this);
        }
        
        protected void b(final String s) {
            if (!this.d) {
                com.tremorvideo.sdk.android.richmedia.b.d.this.a("url-request");
                this.d = true;
            }
            com.tremorvideo.sdk.android.richmedia.b.d.this.a(this);
        }
    }
    
    private class c extends WebChromeClient
    {
        public void onConsoleMessage(final String s, final int n, final String s2) {
        }
        
        public boolean onConsoleMessage(final ConsoleMessage consoleMessage) {
            return false;
        }
    }
    
    public class d implements Runnable
    {
        String a;
        
        public d(final String a) {
            this.a = a;
        }
        
        @Override
        public void run() {
            com.tremorvideo.sdk.android.richmedia.b.d.this.loadUrl("javascript:" + this.a);
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
            com.tremorvideo.sdk.android.richmedia.b.d.this.g.add(b);
            if (this.a.containsKey("data")) {
                b.execute((Object[])new String[] { this.a.get("url"), this.a.get("type"), this.a.get("header"), this.a.get("data") });
                return;
            }
            b.execute((Object[])new String[] { this.a.get("url"), this.a.get("type"), this.a.get("header") });
        }
    }
}
