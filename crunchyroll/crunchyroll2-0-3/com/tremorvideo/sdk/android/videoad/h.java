// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.media.MediaMetadataRetriever;
import android.os.Process;
import android.app.ActivityManager$RunningAppProcessInfo;
import android.app.ActivityManager;
import com.tremorvideo.sdk.android.richmedia.z;
import android.app.KeyguardManager;
import java.util.Iterator;
import org.apache.http.NameValuePair;
import java.util.List;
import android.view.KeyEvent;
import org.json.JSONObject;
import android.content.Intent;
import com.tremorvideo.sdk.android.richmedia.m;
import java.util.ArrayList;
import android.content.res.Configuration;
import java.util.Date;
import com.tremorvideo.sdk.android.richmedia.a.e;
import com.tremorvideo.sdk.android.richmedia.a.f;
import com.tremorvideo.sdk.android.richmedia.b.b;
import com.tremorvideo.sdk.android.richmedia.b.c;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.widget.RelativeLayout$LayoutParams;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import android.content.Context;
import android.app.Activity;
import com.tremorvideo.sdk.android.richmedia.l;
import android.widget.RelativeLayout;
import com.tremorvideo.sdk.android.richmedia.d;
import com.tremorvideo.sdk.android.richmedia.n;

public class h extends com.tremorvideo.sdk.android.videoad.a implements n.a, ax.a, ax.c, bv.a
{
    r b;
    n e;
    com.tremorvideo.sdk.android.richmedia.d f;
    RelativeLayout g;
    com.tremorvideo.sdk.android.richmedia.b.d h;
    ax i;
    boolean j;
    int k;
    int l;
    boolean m;
    boolean n;
    l[] o;
    
    public h(com.tremorvideo.sdk.android.videoad.a.a a, Activity a2, r b) {
        int i = 0;
        super(a, a2);
        this.j = false;
        this.k = 0;
        this.l = -1;
        this.m = false;
        this.n = false;
        this.b = b;
        this.o = this.b.K();
        String a3;
        for (b = (r)(Object)this.o; i < b.length; ++i) {
            a3 = b[i];
            if (!((l)a3).f()) {
                ((l)a3).a(com.tremorvideo.sdk.android.videoad.x.a(a2.getFilesDir(), ((l)a3).e()).getAbsolutePath());
            }
        }
        this.f = new com.tremorvideo.sdk.android.richmedia.d((Context)this.c, this.b.J());
        (this.e = new n((Context)this.c, this.f, this.b.J(), this.b.H(), this.b.I(), this.o, this.b.D())).a((n.a)this);
        this.f.a(this.e.f());
        (this.i = new ax(a2, a, this.b.q())).a((ax.c)this);
        this.i.a((ax.a)this);
        this.b.J().a(this.i);
        a = (com.tremorvideo.sdk.android.videoad.a.a)this.b.a(aw.b.I);
        Label_0444: {
            if (a == null) {
                break Label_0444;
            }
            a2 = (Activity)((aw)a).a("zip", "");
            b = (r)((aw)a).a("movie", "");
            a3 = ((aw)a).a("movie-date", "");
            this.b.J().s();
            this.b.J().b((String)a2);
            final GregorianCalendar gregorianCalendar = new GregorianCalendar();
        Label_0377:
            while (true) {
                if (a3 == null || a3.length() <= 0) {
                    break Label_0377;
                }
                while (true) {
                    while (true) {
                        try {
                            final Date parse = new SimpleDateFormat("yyyy-MM-dd").parse(a3);
                            if (parse.after(gregorianCalendar.getTime())) {
                                gregorianCalendar.setTime(parse);
                            }
                            this.b.J().a(gregorianCalendar);
                            this.i.a((String)a2);
                            this.i.a((ax.a)ac.l());
                            this.i.a((ax.e)ac.l());
                            ac.l().a((String)a2, (String)b, ((aw)a).j(), gregorianCalendar);
                            ac.l().a((bv.a)this);
                            ac.l().a();
                            a = (com.tremorvideo.sdk.android.videoad.a.a)new RelativeLayout$LayoutParams(-1, -1);
                            (this.g = new RelativeLayout((Context)this.c)).setBackgroundColor(-16777216);
                            this.g.addView((View)this.f, (ViewGroup$LayoutParams)a);
                            this.g.addView(this.e.l(), (ViewGroup$LayoutParams)a);
                            this.g.addView((View)this.e, (ViewGroup$LayoutParams)a);
                            a = (com.tremorvideo.sdk.android.videoad.a.a)this.b.a(aw.b.am);
                            if (a != null) {
                                this.h = new com.tremorvideo.sdk.android.richmedia.b.d((Context)this.c);
                                a = (com.tremorvideo.sdk.android.videoad.a.a)((aw)a).a("query-url", "");
                                this.b.t = new com.tremorvideo.sdk.android.richmedia.b.c(this, this.h, (String)a);
                                this.b.J().a(this.b.t);
                                this.h.a(this.b.t);
                            }
                            this.b.u = new f(this.c, this);
                            this.b.J().a(this.b.u);
                            if (this.e.r()) {
                                this.e();
                                a = (com.tremorvideo.sdk.android.videoad.a.a)this.b.B();
                                if (a != null) {
                                    this.i.a((aw)a);
                                }
                                this.c.requestWindowFeature(1);
                                this.c.getWindow().setFlags(1024, 1024);
                                ac.a(this.c.getWindow());
                                this.c.setContentView((View)this.g);
                                return;
                            }
                        }
                        catch (Exception ex) {
                            ac.e("Invalid TMS date: " + a3);
                            continue Label_0377;
                        }
                        this.e();
                        this.e.a(this);
                        this.n = true;
                        continue;
                    }
                }
                break;
            }
        }
    }
    
    private void e() {
        int i = this.d.i();
        if (this.b.J().i()) {
            if (i == 0) {
                if (this.b.f().a("lock-orientation")) {
                    ac.c(this.c);
                    this.l = 0;
                }
                if (this.b.J().j() != null) {
                    this.e.a(this.b.J().j());
                    return;
                }
                this.e.a(this.b.J().k());
            }
            else {
                if (this.b.f().a("lock-orientation")) {
                    ac.b(this.c);
                    this.l = 1;
                }
                if (this.b.J().k() != null) {
                    this.e.a(this.b.J().k());
                    return;
                }
                this.e.a(this.b.J().j());
            }
        }
        else {
            if (i == 0) {
                if (this.b.J().j() == null) {
                    i = 1;
                }
            }
            else if (this.b.J().k() == null) {
                i = 0;
            }
            if (i == 0) {
                this.l = 0;
                ac.c(this.c);
                this.b.J().j();
                this.e.a(this.b.J().j());
                this.d.b(this.l);
                return;
            }
            this.l = 1;
            ac.b(this.c);
            this.e.a(this.b.J().k());
            this.d.b(this.l);
        }
    }
    
    @Override
    public void a() {
        this.j = false;
        if (this.i.b()) {
            this.i.e();
            this.i.b((aw)null);
        }
    }
    
    @Override
    public void a(final int n, final int n2, final int n3) {
        this.e.f().a(n, n2, n3);
    }
    
    @Override
    public void a(final Configuration configuration) {
        if (this.l == -1) {
            this.e.a(false);
            return;
        }
        this.e.a(true);
    }
    
    public void a(final RelativeLayout$LayoutParams relativeLayout$LayoutParams) {
        this.c.runOnUiThread((Runnable)new c(1, relativeLayout$LayoutParams));
    }
    
    public void a(final RelativeLayout$LayoutParams relativeLayout$LayoutParams, final com.tremorvideo.sdk.android.richmedia.a.h h) {
        this.c.runOnUiThread((Runnable)new a(1, relativeLayout$LayoutParams, h, null));
    }
    
    public void a(final com.tremorvideo.sdk.android.richmedia.a.h h) {
        this.c.runOnUiThread((Runnable)new a(3, null, h, null));
    }
    
    @Override
    public void a(final m m, final boolean i) {
        ac.l().a((bv.a)null);
        ac.l().b();
        this.i.a();
        this.b.J().a((ax)null);
        if (!(this.m = i)) {
            final aw c = this.b.C();
            if (c != null) {
                this.i.a(c);
                this.i.b(c);
            }
            this.e.h();
        }
        else {
            this.d.l();
        }
        this.c.runOnUiThread((Runnable)new b(this));
    }
    
    @Override
    public void a(final aw.b b, final int n, final int n2, final int n3) {
        if (b == aw.b.L) {
            final aw a = this.b.a(b);
            if (a != null) {
                final int a2 = a.a("age", 0);
                final GregorianCalendar gregorianCalendar = new GregorianCalendar();
                final GregorianCalendar gregorianCalendar2 = new GregorianCalendar(n, n2, n3);
                gregorianCalendar2.roll(1, a2);
                if (!gregorianCalendar2.before(gregorianCalendar)) {
                    this.e.f().a(com.tremorvideo.sdk.android.richmedia.h.c.o);
                    return;
                }
                this.e.f().a(com.tremorvideo.sdk.android.richmedia.h.c.n);
            }
        }
    }
    
    public void a(final aw.b b, final String s) {
        synchronized (this) {
            final aw a = this.b.a(b);
            if (a != null) {
                this.d.a(this.d.a(a));
            }
            final Intent intent = new Intent((Context)this.c, (Class)Playvideo.class);
            intent.putExtra("tremorVideoType", "webview");
            intent.putExtra("tremorVideoURL", s);
            this.c.startActivityForResult(intent, 3232);
        }
    }
    
    @Override
    public void a(final aw aw) {
        if (aw != null) {
            if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.w) {
                ++this.k;
            }
            else if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.v) {
                this.e.h();
                this.d.a(this);
            }
        }
        if (aw == null || !aw.p()) {
            this.e.i();
        }
    }
    
    @Override
    public void a(final aw aw, final int n) {
        this.a(aw, n, null);
    }
    
    public void a(final aw aw, final int n, final String s) {
        if (aw != null) {
            if (!aw.m()) {
                final aa aa = new aa((Context)this.c, this.b.q(), com.tremorvideo.sdk.android.videoad.aa.a.a, (aa.b)new aa.b() {
                    @Override
                    public void a(final boolean b) {
                        h.this.e.j();
                    }
                });
                aa.setCanceledOnTouchOutside(false);
                aa.setTitle("Unsupported Feature");
                aa.a("Sorry, that feature is not supported on your device.");
                aa.a("OK", "");
                aa.show();
                this.e.i();
                return;
            }
            if (aw.a() != com.tremorvideo.sdk.android.videoad.aw.b.as || !this.j) {
                this.i.a(aw, n, s);
            }
        }
    }
    
    @Override
    public void a(final String s) {
        this.e.f().a(s);
    }
    
    @Override
    public void a(final String s, final int n) {
        aw aw;
        if (s == "adchoices") {
            aw = this.b.a(com.tremorvideo.sdk.android.videoad.aw.b.H);
        }
        else {
            aw = this.b.c(s);
        }
        if (aw != null) {
            this.a(aw, n, null);
        }
    }
    
    @Override
    public void a(final String s, final aw.b b, final int n, final String s2) {
        aw c;
        if (s != null) {
            c = this.b.c(s);
        }
        else {
            c = null;
        }
        aw aw = c;
        if (c == null) {
            aw = c;
            if (b == com.tremorvideo.sdk.android.videoad.aw.b.as) {
                aw = new aw(com.tremorvideo.sdk.android.videoad.aw.b.as, null, null);
            }
        }
        if (aw != null) {
            this.a(aw, n, s2);
        }
    }
    
    public void a(final ArrayList<com.tremorvideo.sdk.android.richmedia.a.h> list) {
        this.c.runOnUiThread((Runnable)new a(2, null, null, list));
    }
    
    @Override
    public void a(final JSONObject jsonObject) {
        this.e.f().a(jsonObject);
    }
    
    @Override
    public boolean a(final int n, final KeyEvent keyEvent) {
        return n == 4 || super.a(n, keyEvent);
    }
    
    @Override
    public void b() {
        this.j = true;
        this.e.g();
    }
    
    public void b(final RelativeLayout$LayoutParams relativeLayout$LayoutParams, final com.tremorvideo.sdk.android.richmedia.a.h h) {
        if (h != null) {
            h.setVisibility(0);
            int n;
            if (this.g == h.getParent()) {
                n = 1;
            }
            else {
                n = 0;
            }
            if (n == 0) {
                final RelativeLayout$LayoutParams relativeLayout$LayoutParams2 = new RelativeLayout$LayoutParams(relativeLayout$LayoutParams.width, relativeLayout$LayoutParams.height);
                relativeLayout$LayoutParams2.leftMargin = 0;
                relativeLayout$LayoutParams2.topMargin = 0;
                this.g.addView((View)h, (ViewGroup$LayoutParams)relativeLayout$LayoutParams2);
                h.a(relativeLayout$LayoutParams.leftMargin, relativeLayout$LayoutParams.topMargin);
            }
            else {
                final RelativeLayout$LayoutParams layoutParams = (RelativeLayout$LayoutParams)h.getLayoutParams();
                if (layoutParams.width != relativeLayout$LayoutParams.width || layoutParams.height != relativeLayout$LayoutParams.height) {
                    layoutParams.width = relativeLayout$LayoutParams.width;
                    layoutParams.height = relativeLayout$LayoutParams.height;
                    h.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                }
                h.a(relativeLayout$LayoutParams.leftMargin, relativeLayout$LayoutParams.topMargin);
            }
            this.g.requestLayout();
        }
    }
    
    public void b(final com.tremorvideo.sdk.android.richmedia.a.h h) {
        if (h != null) {
            h.setVisibility(4);
        }
    }
    
    @Override
    public void b(final aw aw) {
        if (aw == null || !aw.p()) {
            this.e.j();
        }
    }
    
    public void b(final String s) {
        final aw a = this.b.a(aw.b.ap);
        if (a != null) {
            this.d.a(a);
        }
        try {
            this.i.a(s, a);
        }
        catch (Exception ex) {}
    }
    
    public void b(final String s, final List<NameValuePair> list) {
        synchronized (this) {
            final aw c = this.b.c(s);
            if (c != null) {
                this.i.a(c, -1, list);
                this.i.b(c);
            }
        }
    }
    
    public void b(final ArrayList<com.tremorvideo.sdk.android.richmedia.a.h> list) {
        for (final com.tremorvideo.sdk.android.richmedia.a.h h : list) {
            if (h != null) {
                if (h.getParent() == this.g) {
                    this.g.removeView((View)h);
                }
                h.clearView();
            }
        }
        list.clear();
    }
    
    @Override
    public void c() {
        this.j = false;
        if (!((KeyguardManager)this.c.getSystemService("keyguard")).inKeyguardRestrictedInputMode()) {
            this.e.b(this.i.f());
        }
    }
    
    public void c(final com.tremorvideo.sdk.android.richmedia.a.h h) {
        this.i.a(aw.b.au, (ax.e)h, null, null);
    }
    
    @Override
    public void d() {
        while (true) {
            super.d();
            while (true) {
                Label_0082: {
                    try {
                        Block_4: {
                            for (final ActivityManager$RunningAppProcessInfo activityManager$RunningAppProcessInfo : ((ActivityManager)this.c.getSystemService("activity")).getRunningAppProcesses()) {
                                if (activityManager$RunningAppProcessInfo.pid == Process.myPid()) {
                                    break Block_4;
                                }
                            }
                            break Label_0082;
                        }
                        final ActivityManager$RunningAppProcessInfo activityManager$RunningAppProcessInfo;
                        if (activityManager$RunningAppProcessInfo.importance == 100) {
                            final int n = 1;
                            if (n != 0) {
                                this.c();
                            }
                            return;
                        }
                    }
                    catch (Exception ex) {
                        ac.a(ex);
                    }
                }
                final int n = 0;
                continue;
            }
        }
    }
    
    @Override
    public o.a g() {
        final aw a = this.b.a(aw.b.H);
        if (a == null) {
            return com.tremorvideo.sdk.android.videoad.o.a.f;
        }
        final int a2 = a.a("location", -1);
        if (a2 == -1) {
            return com.tremorvideo.sdk.android.videoad.o.a.f;
        }
        return com.tremorvideo.sdk.android.videoad.o.a.values()[a2];
    }
    
    @Override
    public void h() {
        this.e.f().a((JSONObject)null);
    }
    
    public void i() {
        if (this.n) {
            this.n = false;
            this.e();
        }
    }
    
    @Override
    public boolean m() {
        return !this.i.d();
    }
    
    @Override
    public com.tremorvideo.sdk.android.videoad.a.b n() {
        if (this.m) {
            return com.tremorvideo.sdk.android.videoad.a.b.d;
        }
        return com.tremorvideo.sdk.android.videoad.a.b.b;
    }
    
    @Override
    public void o() {
        if (this.b != null) {
            this.b.d();
        }
    }
    
    public void r() {
        this.c.runOnUiThread((Runnable)new c(2, null));
    }
    
    public void s() {
        if (this.h != null) {
            this.h.setVisibility(4);
        }
    }
    
    public r t() {
        return this.b;
    }
    
    public int u() {
        if (this.e != null) {
            return this.e.s();
        }
        return -1;
    }
    
    @SuppressLint({ "NewApi" })
    public int v() {
        final int n = -1;
        if (this.o != null && this.o.length > 0) {
            final l[] o = this.o;
            final int length = o.length;
            int n2 = 0;
            int int1;
            while (true) {
                int1 = n;
                if (n2 >= length) {
                    break;
                }
                final l l = o[n2];
                if (l.c().equals("video-1")) {
                    if (ac.r() >= 10) {
                        final MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                        mediaMetadataRetriever.setDataSource(l.e());
                        final String metadata = mediaMetadataRetriever.extractMetadata(9);
                        int1 = n;
                        if (metadata != null) {
                            int1 = Integer.parseInt(metadata);
                            break;
                        }
                        break;
                    }
                    else {
                        final MediaPlayer mediaPlayer = new MediaPlayer();
                        try {
                            mediaPlayer.setDataSource(l.e());
                            mediaPlayer.prepare();
                            return mediaPlayer.getDuration();
                        }
                        catch (Exception ex) {
                            ac.a(ex);
                            return -1;
                        }
                        finally {
                            mediaPlayer.release();
                        }
                    }
                }
                ++n2;
            }
            return int1;
        }
        return this.b.A();
    }
    
    private class a implements Runnable
    {
        ArrayList<com.tremorvideo.sdk.android.richmedia.a.h> a;
        private int c;
        private RelativeLayout$LayoutParams d;
        private com.tremorvideo.sdk.android.richmedia.a.h e;
        
        public a(final int c, final RelativeLayout$LayoutParams d, final com.tremorvideo.sdk.android.richmedia.a.h e, final ArrayList<com.tremorvideo.sdk.android.richmedia.a.h> a) {
            this.c = c;
            this.d = d;
            this.e = e;
            this.a = a;
        }
        
        @Override
        public void run() {
            if (this.c == 1) {
                if (this.d != null && this.e != null) {
                    com.tremorvideo.sdk.android.videoad.h.this.b(this.d, this.e);
                }
            }
            else if (this.c == 2) {
                if (this.a != null) {
                    com.tremorvideo.sdk.android.videoad.h.this.b(this.a);
                }
            }
            else if (this.c == 3 && this.e != null) {
                com.tremorvideo.sdk.android.videoad.h.this.b(this.e);
            }
        }
    }
    
    private class b implements Runnable
    {
        private com.tremorvideo.sdk.android.videoad.a b;
        
        public b(final com.tremorvideo.sdk.android.videoad.a b) {
            this.b = b;
        }
        
        @Override
        public void run() {
            com.tremorvideo.sdk.android.videoad.h.this.d.a(this.b);
        }
    }
    
    private class c implements Runnable
    {
        private int b;
        private RelativeLayout$LayoutParams c;
        
        public c(final int b, final RelativeLayout$LayoutParams c) {
            this.b = b;
            this.c = c;
        }
        
        private void a() {
            if (com.tremorvideo.sdk.android.videoad.h.this.h != null) {
                if (com.tremorvideo.sdk.android.videoad.h.this.h.getParent() == com.tremorvideo.sdk.android.videoad.h.this.g) {
                    com.tremorvideo.sdk.android.videoad.h.this.g.removeView((View)com.tremorvideo.sdk.android.videoad.h.this.h);
                }
                com.tremorvideo.sdk.android.videoad.h.this.h.clearView();
            }
        }
        
        private void a(final RelativeLayout$LayoutParams layoutParams) {
            boolean b = false;
            if (com.tremorvideo.sdk.android.videoad.h.this.h != null) {
                com.tremorvideo.sdk.android.videoad.h.this.h.setVisibility(0);
                if (com.tremorvideo.sdk.android.videoad.h.this.g == com.tremorvideo.sdk.android.videoad.h.this.h.getParent()) {
                    b = true;
                }
                if (!b) {
                    com.tremorvideo.sdk.android.videoad.h.this.g.addView((View)com.tremorvideo.sdk.android.videoad.h.this.h, (ViewGroup$LayoutParams)layoutParams);
                }
                else {
                    com.tremorvideo.sdk.android.videoad.h.this.h.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                    com.tremorvideo.sdk.android.videoad.h.this.h.requestLayout();
                }
                com.tremorvideo.sdk.android.videoad.h.this.g.requestLayout();
            }
        }
        
        @Override
        public void run() {
            if (this.b == 1) {
                this.a(this.c);
            }
            else if (this.b == 2) {
                this.a();
            }
        }
    }
}
