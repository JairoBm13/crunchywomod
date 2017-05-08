// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.f;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.ViewGroup$LayoutParams;
import android.widget.RelativeLayout$LayoutParams;
import android.widget.RelativeLayout;
import android.widget.FrameLayout;
import android.webkit.JsResult;
import android.media.MediaPlayer;
import android.webkit.WebChromeClient$CustomViewCallback;
import android.widget.VideoView;
import android.media.MediaPlayer$OnErrorListener;
import android.media.MediaPlayer$OnCompletionListener;
import java.util.ArrayList;
import java.io.IOException;
import java.util.TimerTask;
import java.io.FileWriter;
import com.tremorvideo.sdk.android.richmedia.ae;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import java.util.HashMap;
import org.apache.http.client.utils.URLEncodedUtils;
import java.net.URI;
import android.annotation.SuppressLint;
import android.webkit.WebSettings;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import com.tremorvideo.sdk.android.videoad.ac;
import android.os.Build$VERSION;
import android.view.MotionEvent;
import android.view.View;
import android.view.View$OnTouchListener;
import android.content.Context;
import com.tremorvideo.sdk.android.videoad.g;
import android.app.Activity;
import java.util.Timer;
import com.tremorvideo.sdk.android.videoad.q;
import android.webkit.WebView;

public class s extends WebView
{
    public boolean a;
    public boolean b;
    q c;
    Timer d;
    String e;
    Activity f;
    private com.tremorvideo.sdk.android.f.m g;
    private com.tremorvideo.sdk.android.f.l h;
    private d i;
    private c j;
    private boolean k;
    private final j l;
    private com.tremorvideo.sdk.android.videoad.g m;
    private String n;
    private b o;
    
    public s(final Context context, final a a, final e e, final String n, final q c) {
        super(context);
        this.l = s.j.a;
        this.a = false;
        this.c = c;
        this.a = false;
        this.n = n;
        this.a(a, e);
    }
    
    @SuppressLint({ "NewApi" })
    private void a(final a a, final e e) {
        this.setScrollContainer(false);
        this.setBackgroundColor(0);
        this.setHorizontalScrollBarEnabled(false);
        this.setHorizontalScrollbarOverlay(false);
        this.setVerticalScrollBarEnabled(false);
        this.setVerticalScrollbarOverlay(false);
        final WebSettings settings = this.getSettings();
        settings.setSupportZoom(false);
        this.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 0:
                    case 1: {
                        if (!view.hasFocus()) {
                            view.requestFocus();
                            break;
                        }
                        break;
                    }
                }
                return false;
            }
        });
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setCacheMode(2);
        if (Build$VERSION.SDK_INT >= 11) {
            settings.setAllowContentAccess(true);
        }
        if (ac.r() < 19) {
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
        }
        if (ac.r() >= 16) {
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        if (ac.r() >= 17) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }
        this.g = new com.tremorvideo.sdk.android.f.m(this);
        this.h = new com.tremorvideo.sdk.android.f.l(this, a, e);
        this.setWebViewClient((WebViewClient)(this.i = new d()));
        this.setWebChromeClient((WebChromeClient)(this.j = new c(this)));
        this.o = new b();
    }
    
    private boolean a(final URI uri) {
        ac.e("MRAID tryCommand=" + uri.toString());
        final String host = uri.getHost();
        final List parse = URLEncodedUtils.parse(uri, "UTF-8");
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        for (final NameValuePair nameValuePair : parse) {
            hashMap.put(nameValuePair.getName(), nameValuePair.getValue());
        }
        final com.tremorvideo.sdk.android.f.d a = com.tremorvideo.sdk.android.f.h.a(host, hashMap, this);
        if (a == null) {
            this.b(host);
            return false;
        }
        a.a();
        this.b(host);
        return true;
    }
    
    private void q() {
        if (this.d != null) {
            this.d.purge();
            this.d.cancel();
            this.d = null;
        }
    }
    
    public void a() {
        if (this.m != null) {
            this.m.e();
        }
    }
    
    protected void a(final o o) {
        this.a("window.mraidbridge.fireChangeEvent(" + ("{" + o.toString() + "}") + ");");
    }
    
    public void a(final f f) {
        this.o.c = f;
    }
    
    public void a(final g g) {
        this.o.a = g;
    }
    
    public void a(final i i) {
        this.o.b = i;
    }
    
    public void a(final com.tremorvideo.sdk.android.videoad.g m) {
        this.m = m;
    }
    
    public void a(final File file, final Activity f) {
        this.f = f;
        String s = ae.b(file);
        boolean b;
        if (s.indexOf("<html>") == -1) {
            s = "<html><head><meta name='viewport' content='user-scalable=no, initial-scale=1.0'/></head><body style='margin:0;padding:0;overflow:hidden;'>" + s + "</body></html>";
            b = true;
        }
        else {
            b = false;
        }
        final String j = this.c.J();
        int n = b ? 1 : 0;
        String s2 = s;
        while (true) {
            Label_0408: {
                if (j != null) {
                    String string = j;
                    if (j.indexOf("mraid.js") == -1) {
                        string = j + "mraid.js";
                    }
                    n = (b ? 1 : 0);
                    s2 = s;
                    if (s.indexOf("<head><script type='text/javascript' src='" + string + "'></script>") == -1) {
                        final String replace = s.replace("mraid.js", "");
                        if (replace.indexOf("<head>") != -1) {
                            break Label_0408;
                        }
                        s2 = replace.replace("<html>", "<html><head><script type='text/javascript' src='" + string + "'></script><meta name='viewport' content='user-scalable=no, initial-scale=1.0'/></head>");
                        n = 1;
                    }
                }
            Label_0326_Outer:
                while (true) {
                    String s3 = Build$VERSION.RELEASE;
                    if (s3.length() >= 3) {
                        s3 = s3.substring(0, 3);
                    }
                    Label_0448: {
                        if (s3.equals("4.4")) {
                            break Label_0448;
                        }
                        int n2 = n;
                        String s4 = s2;
                        if (this.c != null) {
                            n2 = n;
                            s4 = s2;
                            if (this.c.f().a("disable-video-tag-autoplay")) {
                                n2 = n;
                                s4 = s2;
                                if (s2.indexOf("<video") != -1) {
                                    s4 = s2.replace("autoplay", "").replace("<video", "<video preload='none'");
                                    n2 = 1;
                                }
                            }
                        }
                    Label_0351_Outer:
                        while (true) {
                            while (true) {
                                if (n2 != 1) {
                                    break Label_0351;
                                }
                                try {
                                    final FileWriter fileWriter = new FileWriter(file);
                                    fileWriter.write(s4);
                                    fileWriter.close();
                                    this.e = "file://" + file.getAbsolutePath();
                                    (this.d = new Timer()).schedule(new l(), 1500L);
                                    return;
                                    final String string;
                                    final String replace;
                                    s2 = replace.replace("<head>", "<head><script type='text/javascript' src='" + string + "'></script>");
                                    n = 1;
                                    continue Label_0326_Outer;
                                    // iftrue(Label_0514:, this.c == null || !this.c.f().a("disable-video-tag-autoplay"))
                                    // iftrue(Label_0508:, s2.indexOf("<video") == -1)
                                    String replace2 = null;
                                Block_15:
                                    while (true) {
                                        replace2 = s2.replace("autoplay", "").replace("<video", "<video preload='none' ");
                                        break Block_15;
                                        s4 = s2;
                                        continue;
                                    }
                                    s4 = replace2;
                                    // iftrue(Label_0508:, replace2.indexOf("<body") == -1)
                                    Label_0508: {
                                        break Label_0508;
                                        Label_0514: {
                                            s4 = replace2;
                                        }
                                        s4 = replace2.replace("<body", "<body onload=\"document.getElementsByTagName('video')[0].play()\"");
                                    }
                                    n2 = 1;
                                    continue Label_0351_Outer;
                                }
                                catch (IOException ex) {
                                    ac.a(ex);
                                    continue;
                                }
                                break;
                            }
                            break;
                        }
                    }
                    break;
                }
            }
            continue;
        }
    }
    
    protected void a(final String s) {
        if (s != null && this.a) {
            super.loadUrl("javascript:" + s);
        }
    }
    
    protected void a(final String s, final String s2) {
        this.a("window.mraidbridge.fireErrorEvent('" + s + "', '" + s2 + "');");
    }
    
    protected void a(final ArrayList<o> list) {
        final String string = list.toString();
        if (string.length() < 2) {
            return;
        }
        this.a("window.mraidbridge.fireChangeEvent(" + ("{" + string.substring(1, string.length() - 1) + "}") + ");");
    }
    
    @SuppressLint({ "NewApi" })
    public void a(final boolean b) {
        if (ac.r() >= 11) {
            super.onResume();
        }
        if (b) {
            this.n();
        }
        if (this.j != null) {
            this.j.d();
        }
    }
    
    public void a(final boolean b, final String s) {
        this.m.a(b, s);
    }
    
    protected void b(final String s) {
        this.a("window.mraidbridge.nativeCallComplete('" + s + "');");
    }
    
    public boolean b() {
        this.l();
        return this.j.b();
    }
    
    public void c() {
        this.m.f();
    }
    
    public void d() {
        this.m.h();
    }
    
    public void destroy() {
        if (this.h != null) {
            this.h.b();
        }
        super.destroy();
    }
    
    public void e() {
        this.q();
        this.clearCache(true);
        this.j.a();
        this.h.b();
        this.h = null;
        this.clearView();
        this.j = null;
        this.i = null;
    }
    
    protected com.tremorvideo.sdk.android.f.m f() {
        return this.g;
    }
    
    protected com.tremorvideo.sdk.android.f.l g() {
        return this.h;
    }
    
    public g h() {
        return this.o.a;
    }
    
    public i i() {
        return this.o.b;
    }
    
    public f j() {
        return this.o.c;
    }
    
    public h k() {
        return this.o.d;
    }
    
    public void l() {
        this.a("window.mraidbridge.fireTremorVideoEndEvent();");
    }
    
    public void m() {
        this.a(t.a(false));
    }
    
    public void n() {
        this.a(t.a(true));
    }
    
    protected void o() {
        this.a("window.mraidbridge.fireReadyEvent();");
    }
    
    @SuppressLint({ "NewApi" })
    public void onPause() {
        if (ac.r() >= 11) {
            super.onPause();
        }
        this.m();
        if (this.j != null) {
            this.j.c();
        }
    }
    
    public void p() {
        try {
            this.a(new URI("mraid://close"));
        }
        catch (Exception ex) {
            ac.a(ex);
        }
    }
    
    public enum a
    {
        a, 
        b;
    }
    
    static class b
    {
        private g a;
        private i b;
        private f c;
        private h d;
    }
    
    private class c extends WebChromeClient implements MediaPlayer$OnCompletionListener, MediaPlayer$OnErrorListener
    {
        private s b;
        private VideoView c;
        private WebChromeClient$CustomViewCallback d;
        private int e;
        
        public c(final s b) {
            this.e = 0;
            this.b = b;
        }
        
        public void a() {
            if (this.c != null) {
                this.c.stopPlayback();
                if (this.d != null) {
                    this.d.onCustomViewHidden();
                }
                this.c = null;
                this.d = null;
            }
        }
        
        public boolean b() {
            if (this.c != null) {
                this.c.stopPlayback();
                this.d.onCustomViewHidden();
                this.onHideCustomView();
                this.b.a();
                this.b.d();
                this.c = null;
                this.d = null;
                this.b.l();
                return true;
            }
            return false;
        }
        
        public void c() {
            if (this.c != null) {
                this.e = this.c.getCurrentPosition();
                this.c.pause();
            }
        }
        
        public void d() {
            if (this.c != null) {
                this.c.forceLayout();
                this.c.requestLayout();
                if (!this.c.isPlaying()) {
                    this.c.start();
                    if (this.c.isPlaying() && this.e > 0) {
                        this.c.seekTo(this.e);
                    }
                }
                this.e = 0;
            }
        }
        
        public void onCompletion(final MediaPlayer mediaPlayer) {
            this.c.stopPlayback();
            this.d.onCustomViewHidden();
            this.onHideCustomView();
            this.b.a();
            this.b.d();
            this.c = null;
            this.d = null;
            this.b.l();
        }
        
        public boolean onError(final MediaPlayer mediaPlayer, final int n, final int n2) {
            this.c.stopPlayback();
            this.d.onCustomViewHidden();
            this.onHideCustomView();
            this.b.a();
            this.b.d();
            this.c = null;
            this.d = null;
            return true;
        }
        
        public boolean onJsAlert(final WebView webView, final String s, final String s2, final JsResult jsResult) {
            return false;
        }
        
        public void onShowCustomView(final View view, final WebChromeClient$CustomViewCallback d) {
            super.onShowCustomView(view, d);
            if (!(view instanceof FrameLayout)) {
                return;
            }
            final FrameLayout frameLayout = (FrameLayout)view;
            if (!(frameLayout.getFocusedChild() instanceof VideoView)) {
                return;
            }
            try {
                final VideoView c = (VideoView)frameLayout.getFocusedChild();
                this.c = c;
                this.d = d;
                frameLayout.removeView((View)c);
                final Activity activity = (Activity)s.this.getContext();
                final RelativeLayout contentView = new RelativeLayout(s.this.getContext());
                final RelativeLayout$LayoutParams layoutParams = new RelativeLayout$LayoutParams(-1, -1);
                layoutParams.addRule(14);
                layoutParams.addRule(15);
                c.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                contentView.addView((View)c);
                activity.setContentView((View)contentView);
                c.setOnCompletionListener((MediaPlayer$OnCompletionListener)this);
                c.setOnErrorListener((MediaPlayer$OnErrorListener)this);
                c.start();
                this.b.c();
            }
            catch (Exception ex) {
                ac.a(ex);
            }
        }
    }
    
    private class d extends WebViewClient
    {
        public void onPageFinished(final WebView webView, final String s) {
            if (!s.this.k && s.this.h != null && !s.this.b) {
                s.this.q();
                s.this.a = true;
                s.this.h.c();
                s.this.a(com.tremorvideo.sdk.android.f.n.a(s.this.l));
                s.this.o();
                if (s.this.i() != null) {
                    s.this.i().a(s.this);
                }
                s.this.k = true;
            }
        }
        
        public void onReceivedError(final WebView webView, final int n, final String s, final String s2) {
            super.onReceivedError(webView, n, s, s2);
        }
        
        public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
            if (Uri.parse(s).getScheme().equals("mraid")) {
                s.this.a(URI.create(s));
                return true;
            }
            final Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.parse(s));
            intent.addFlags(268435456);
            try {
                s.this.getContext().startActivity(intent);
                return true;
            }
            catch (ActivityNotFoundException ex) {
                ac.a((Throwable)ex);
                return false;
            }
        }
    }
    
    public enum e
    {
        a, 
        b, 
        c;
    }
    
    public interface f
    {
        void a(final s p0, final boolean p1);
    }
    
    public interface g
    {
        void a(final s p0, final k p1);
    }
    
    public interface h
    {
        void a(final s p0);
    }
    
    public interface i
    {
        void a(final s p0);
    }
    
    public enum j
    {
        a;
    }
    
    public enum k
    {
        a, 
        b, 
        c, 
        d;
    }
    
    class l extends TimerTask
    {
        @Override
        public void run() {
            s.this.q();
            s.this.b = false;
            s.this.f.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    ac.e("MraidView _URL" + s.this.e);
                    (s.this.d = new Timer()).schedule(new m(), ac.g);
                    s.this.loadUrl(s.this.e);
                }
            });
        }
    }
    
    class m extends TimerTask
    {
        @Override
        public void run() {
            s.this.q();
            s.this.b = true;
            s.this.f.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    s.this.m.a(true);
                }
            });
        }
    }
}
