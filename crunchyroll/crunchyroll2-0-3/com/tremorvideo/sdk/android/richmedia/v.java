// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.HttpEntity;
import java.util.List;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.HttpVersion;
import org.apache.http.params.BasicHttpParams;
import android.os.AsyncTask;
import android.graphics.Paint$Align;
import android.graphics.BitmapFactory;
import android.graphics.Paint$Style;
import android.graphics.Shader;
import android.graphics.LinearGradient;
import android.graphics.Shader$TileMode;
import android.graphics.Color;
import org.json.JSONArray;
import com.tremorvideo.sdk.android.videoad.aw;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import org.json.JSONObject;
import android.graphics.Canvas;
import android.graphics.Paint$FontMetrics;
import android.graphics.Typeface;
import com.tremorvideo.sdk.android.videoad.ac;
import android.graphics.Paint;
import android.graphics.Bitmap;
import com.tremorvideo.sdk.android.videoad.ax;

public class v extends q implements ax.b
{
    String A;
    String B;
    String C;
    String D;
    int E;
    int F;
    Bitmap G;
    boolean H;
    int I;
    int J;
    boolean K;
    String L;
    String M;
    String N;
    String O;
    int P;
    int Q;
    int R;
    int S;
    int T;
    int U;
    int V;
    int W;
    int X;
    int Y;
    int Z;
    int aa;
    int ab;
    int ac;
    Bitmap ad;
    Paint ae;
    int af;
    int ag;
    int ah;
    Paint ai;
    Paint aj;
    int ak;
    int al;
    int am;
    private final String an;
    Paint o;
    Paint p;
    Paint q;
    Paint r;
    Paint s;
    Paint t;
    double u;
    String v;
    int w;
    int x;
    boolean y;
    String z;
    
    public v(final o o) {
        super(o);
        this.u = 0.0;
        this.w = 0;
        this.x = 0;
        this.y = false;
        this.E = 0;
        this.F = 5;
        this.H = true;
        this.I = com.tremorvideo.sdk.android.videoad.ac.K();
        this.O = "your email address";
        this.P = -7829368;
        this.an = "Submitting...";
        this.Q = 0;
        this.R = 0;
        this.S = 0;
        this.T = 0;
        this.U = 0;
        this.V = 0;
        this.W = 0;
        this.X = 0;
        this.Y = 0;
        this.Z = 0;
        this.aa = 0;
        this.ab = 0;
        this.ac = 0;
        this.af = 0;
        this.ag = 0;
        this.ah = 0;
        this.ak = 0;
        this.al = 0;
        this.am = 0;
    }
    
    protected int a(int i, int round, final float n) {
        round = Math.round(n);
        int a;
        Paint paint;
        Paint$FontMetrics fontMetrics;
        for (i = 15; i >= 8; --i) {
            a = com.tremorvideo.sdk.android.videoad.ac.a(i);
            paint = new Paint();
            paint.setTextSize((float)a);
            paint.setTypeface(Typeface.create("helvetica", 0));
            paint.setAntiAlias(true);
            paint.setAlpha(this.w);
            fontMetrics = paint.getFontMetrics();
            this.ac = (int)(fontMetrics.descent - fontMetrics.ascent + fontMetrics.leading);
            if (round >= this.ac * 2 + 10 + (int)fontMetrics.descent) {
                this.I = a;
                this.ah = (round - this.ac * 2 - (int)fontMetrics.descent) / 2;
                break;
            }
        }
        return this.ac;
    }
    
    @Override
    public void a() {
        super.a();
        com.tremorvideo.sdk.android.videoad.ac.e("onRotate  called");
        this.Q = 0;
        this.R = 0;
        this.T = 0;
        this.U = 0;
        this.V = 0;
        this.W = 0;
        this.X = 0;
        this.Y = 0;
        this.Z = 0;
        this.o = null;
        this.p = null;
        this.r = null;
        this.s = null;
        this.t = null;
        this.aa = 0;
        this.ab = 0;
        this.x = 0;
        this.af = 0;
        this.I = com.tremorvideo.sdk.android.videoad.ac.H();
        this.G = null;
        this.ac = 0;
        this.ak = 0;
        this.q = null;
        this.ae = null;
    }
    
    protected void a(final Canvas canvas, final int n, final int n2, final int n3, final int n4) {
        final Paint o = this.o();
        canvas.drawRect((float)n, (float)n2, (float)(n + n3), (float)(n2 + n4), this.b(n2, n2 + n4));
        canvas.drawRect((float)n, (float)n2, (float)(n + n3 - 1), (float)(n2 + n4 - 1), o);
    }
    
    protected void a(final Canvas canvas, int round, int round2, final int n, final int n2, final int n3) {
        this.b(canvas, round, round2, n, n2);
        Paint paint;
        if (this.P == -7829368) {
            paint = this.e(n2);
        }
        else {
            paint = this.f(n2);
        }
        round2 = Math.round(n3 / 2 + paint.descent());
        round = Math.round((n - Math.round(this.b(n2).measureText(this.O))) / 2 + round);
        canvas.drawText(this.O, (float)round, (float)round2, paint);
    }
    
    @Override
    public void a(final com.tremorvideo.sdk.android.richmedia.e e) {
        super.a(e);
        while (true) {
            try {
                this.v = e.c();
                com.tremorvideo.sdk.android.videoad.ac.e("SceneObjectGoWatchIt JSON is" + this.v);
                final JSONObject jsonObject = new JSONObject(this.v);
                this.J = jsonObject.getInt("component_type");
                this.K = jsonObject.getBoolean("pause_creative");
                this.D = jsonObject.getString("zip_path");
                final JSONObject jsonObject2 = jsonObject.getJSONObject("params");
                this.L = jsonObject2.getString("endpoint");
                this.M = jsonObject2.getString("movieId");
                this.N = jsonObject2.getString("clickUrl");
                this.h = true;
            }
            catch (Exception ex) {
                com.tremorvideo.sdk.android.videoad.ac.e("SceneObjectGoWatchIt Exception =" + ex);
                continue;
            }
            break;
        }
    }
    
    @Override
    public void a(final p p2, final long n) {
        if (this.n()) {
            final q.a a = this.a(n);
            if (a != null) {
                final a a2 = (a)a.a;
                final a a3 = (a)a.b;
                final float c = a.c;
                this.k = a2.k;
                if (this.d(a2.k)) {
                    final float b = com.tremorvideo.sdk.android.richmedia.ae.b(a2.a, a3.a, a2.b, a3.b, c);
                    final float b2 = com.tremorvideo.sdk.android.richmedia.ae.b(a2.c, a3.c, a2.d, a3.d, c);
                    final float b3 = com.tremorvideo.sdk.android.richmedia.ae.b(a2.e, a3.e, a2.f, a3.f, c);
                    final float b4 = com.tremorvideo.sdk.android.richmedia.ae.b(a2.g, a3.g, a2.h, a3.h, c);
                    final Canvas c2 = p2.c();
                    final PointF a4 = com.tremorvideo.sdk.android.richmedia.b.a(b3, b4, this.c.c());
                    final float n2 = b - a4.x;
                    final float n3 = b2 - a4.y;
                    p2.a(n2, n3, b3, 60.0f, this.l, this.c.c());
                    final float k = p2.k();
                    p2.a(n2, n3, b3, b4, this.l, this.c.c());
                    final float h = p2.h();
                    final float i = p2.i();
                    final float j = p2.j();
                    final float l = p2.k();
                    float n4 = i;
                    float n5 = h;
                    if (this.c.a() != null) {
                        final k c3 = this.c.a().c(p2, n);
                        final float a5 = c3.a;
                        final float b5 = c3.b;
                        final Point a6 = com.tremorvideo.sdk.android.richmedia.b.a(Math.round(c3.f), Math.round(c3.g), this.c.b());
                        n5 = h + a5 + a6.x;
                        n4 = i + b5 + a6.y;
                    }
                    final RectF a7 = this.a(p2, n, new RectF(n5, n4, j + n5, l + n4));
                    final float left = a7.left;
                    final float top = a7.top;
                    final float width = a7.width();
                    final float height = a7.height();
                    this.w = com.tremorvideo.sdk.android.richmedia.ae.a(a2.i, a3.i, a2.j, a3.j, c);
                    final int ac = this.ac;
                    final int round = Math.round(height);
                    final int round2 = Math.round(width);
                    final int round3 = Math.round(height);
                    this.al = round3;
                    this.ac = this.a(round2, round3, k);
                    this.x = (round3 - this.ac * 2 - this.d()) / 2;
                    this.x = this.ah;
                    final int round4 = Math.round((round3 - k) / 2.0f);
                    if (round4 > 0) {
                        this.x += round4;
                    }
                    this.af = this.ac / 2;
                    this.Q = this.af;
                    final int c4 = this.c(round2, round3);
                    final int e = this.e(round2, round3);
                    this.d(round2, round3);
                    final int h2 = this.h(round2, round3);
                    final int m = this.i(round2, round3);
                    final int ab = round - this.x * 2;
                    this.ab = ab;
                    this.aa = round2 - this.af * 2 - this.ab;
                    final int f = this.f(round2, round3);
                    final int g = this.g(round2, round3);
                    c2.saveLayerAlpha(new RectF(new Rect(Math.round(left), Math.round(top), Math.round(a7.right), Math.round(a7.bottom))), this.w, 1);
                    c2.translate((float)Math.round(left), (float)Math.round(top));
                    this.a(c2, 0, 0, round2, round3);
                    this.f(c2, this.Q, this.x, c4, ab);
                    if (this.y) {
                        this.c(c2, this.aa, this.x, this.ab, ab);
                        this.e(c2, f, this.x + this.c(), g, ab);
                    }
                    else {
                        this.a(c2, h2 + m + this.af, this.x, e, ab, round3);
                        this.d(c2, h2, this.x + this.c(), m, ab);
                    }
                    c2.restore();
                }
            }
        }
    }
    
    public void a(final String s) {
        try {
            if (new JSONObject(s).has("upgrade_path")) {
                this.y = true;
                this.g.g().c().a(aw.b.ax, (ax.b)this, this.A, null, null);
                return;
            }
            this.g.g().c().a(aw.b.ay, (ax.b)this, this.B, null, null);
            this.O = "email invalid";
            this.P = -65536;
        }
        catch (Exception ex) {
            com.tremorvideo.sdk.android.videoad.ac.e("Exception parsing Quick Alert GoWatchit response " + ex);
            this.O = "email invalid";
            this.P = -65536;
        }
    }
    
    @Override
    public boolean a(final int n, final int n2) {
        boolean b;
        while (true) {
            b = false;
            while (true) {
                int n3 = 0;
                Label_0496: {
                    JSONObject jsonObject = null;
                    Label_0291: {
                        try {
                            if (this.z == null || this.A == null || this.B == null || this.C == null) {
                                final String b2 = com.tremorvideo.sdk.android.richmedia.ae.b(this.g.g().b() + "/" + this.D + "native_config.txt");
                                if (b2 != null && b2.length() > 0) {
                                    final JSONArray jsonArray = new JSONObject(b2).getJSONArray("events");
                                    n3 = 0;
                                    if (n3 < jsonArray.length()) {
                                        jsonObject = jsonArray.getJSONObject(n3);
                                        if (jsonObject.getString("name").equals("Interact_EmailField_Click")) {
                                            this.z = jsonObject.getString("tag");
                                            break Label_0496;
                                        }
                                        if (jsonObject.getString("name").equals("Interact_SubmitEmailSuccess_Click")) {
                                            this.A = jsonObject.getString("tag");
                                            break Label_0496;
                                        }
                                        break Label_0291;
                                    }
                                }
                            }
                        }
                        catch (Exception ex) {
                            com.tremorvideo.sdk.android.videoad.ac.e("SceneObjectGoWatchIt Touch Exception =" + ex);
                        }
                        break;
                    }
                    if (jsonObject.getString("name").equals("Interact_SubmitEmailFail_Click")) {
                        this.B = jsonObject.getString("tag");
                    }
                    else if (jsonObject.getString("name").equals("Click_GoWatchIt")) {
                        this.C = jsonObject.getString("tag");
                    }
                }
                ++n3;
                continue;
            }
        }
        boolean b3;
        if (n >= this.Q && n < this.Q + this.ak && n2 >= this.x && n2 < this.al - this.x) {
            this.g.g().c().a(aw.b.b, (ax.b)this, this.C, null, this.N);
            b3 = true;
        }
        else {
            b3 = b;
            if (!this.y) {
                b3 = b;
                if (n >= this.S) {
                    b3 = b;
                    if (n < this.S + this.T) {
                        b3 = b;
                        if (!this.O.equals("Submitting...")) {
                            b3 = b;
                            if (n2 >= this.x) {
                                b3 = b;
                                if (n2 < this.al - this.x) {
                                    this.O = "your email address";
                                    this.P = -7829368;
                                    final ax c = this.g.g().c();
                                    c.a(aw.b.az, (ax.b)this, this.z, null, null);
                                    c.a(aw.b.aw, (ax.b)this, this.z, null, null);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return b3;
    }
    
    protected Paint b(final int n) {
        (this.p = new Paint()).setTextSize((float)this.ac);
        this.p.setColor(-1);
        this.p.setTypeface(Typeface.create("helvetica", 0));
        this.p.setAntiAlias(true);
        this.p.setAlpha(this.w);
        return this.p;
    }
    
    protected Paint b(final int n, final int n2) {
        if (this.o != null) {
            return this.o;
        }
        final LinearGradient shader = new LinearGradient(0.0f, (float)n, 0.0f, (float)n2, new int[] { Color.argb(255, 54, 54, 54), Color.argb(255, 35, 35, 35), Color.argb(255, 35, 35, 35) }, new float[] { 0.0f, 0.75f, 1.0f }, Shader$TileMode.CLAMP);
        (this.o = new Paint()).setDither(true);
        this.o.setShader((Shader)shader);
        return this.o;
    }
    
    public k b(final p p2, final long n) {
        final q.a a = this.a(n);
        if (a != null) {
            final a a2 = (a)a.a;
            final a a3 = (a)a.b;
            final float c = a.c;
            final float b = com.tremorvideo.sdk.android.richmedia.ae.b(a2.a, a3.a, a2.b, a3.b, c);
            final float b2 = com.tremorvideo.sdk.android.richmedia.ae.b(a2.c, a3.c, a2.d, a3.d, c);
            final float b3 = com.tremorvideo.sdk.android.richmedia.ae.b(a2.e, a3.e, a2.f, a3.f, c);
            final float b4 = com.tremorvideo.sdk.android.richmedia.ae.b(a2.g, a3.g, a2.h, a3.h, c);
            final PointF a4 = com.tremorvideo.sdk.android.richmedia.b.a(b3, b4, this.c.c());
            p2.a(b - a4.x, b2 - a4.y, b3, b4, this.l, this.c.c());
            final float h = p2.h();
            final float i = p2.i();
            final float j = p2.j();
            final float k = p2.k();
            float n2 = i;
            float n3 = h;
            if (this.c.a() != null) {
                final k c2 = this.c.a().c(p2, n);
                final float a5 = c2.a;
                final float b5 = c2.b;
                final Point a6 = com.tremorvideo.sdk.android.richmedia.b.a(Math.round(c2.f), Math.round(c2.g), this.c.b());
                n3 = h + a5 + a6.x;
                n2 = i + b5 + a6.y;
            }
            final RectF a7 = this.a(p2, n, new RectF(n3, n2, j + n3, k + n2));
            return new k(a7.left, a7.top, a7.right - a7.left, a7.bottom - a7.top, 1.0f, 1.0f, 0.0f);
        }
        return k.h;
    }
    
    @Override
    public void b() {
        super.b();
        this.y = false;
        this.O = "your email address";
        this.P = -7829368;
        this.H = true;
    }
    
    protected void b(final Canvas canvas, final int n, final int n2, final int n3, final int n4) {
        final Paint paint = new Paint();
        paint.setColor(Color.argb(this.w, 255, 255, 255));
        paint.setStyle(Paint$Style.FILL);
        canvas.drawRoundRect(new RectF((float)n, (float)n2, (float)(n + n3 - 1), (float)(n2 + n4 - 1)), (float)this.F, (float)this.F, paint);
    }
    
    @Override
    public void b(final String s) {
        this.O = "Submitting...";
        this.P = -7829368;
        new b().execute((Object[])new String[] { s });
    }
    
    protected int c() {
        final Paint paint = new Paint();
        paint.setTextSize((float)this.ac);
        paint.setTypeface(Typeface.create("helvetica", 0));
        paint.setAntiAlias(true);
        paint.setAlpha(this.w);
        return (int)Math.abs(paint.getFontMetrics().ascent);
    }
    
    protected int c(final int n, final int n2) {
        return this.R = Math.round(n * 21 / 100);
    }
    
    protected Paint c(final int n) {
        (this.ai = new Paint()).setTextSize((float)this.ac);
        this.ai.setColor(-1);
        this.ai.setTypeface(Typeface.create("helvetica", 0));
        this.ai.setAntiAlias(true);
        this.ai.setAlpha(this.w);
        return this.ai;
    }
    
    protected void c(final Canvas canvas, final int aa, final int n, final int ab, final int n2) {
        this.aa = aa;
        this.ab = ab;
        if (this.ad == null) {
            this.ad = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(this.g.g().b() + "/comps/gowatchit/common/imgs/check_mark.png"), ab, n2, true);
        }
        final Paint paint = new Paint();
        paint.setFilterBitmap(true);
        paint.setAlpha(this.w);
        canvas.drawBitmap(this.ad, (float)aa, (float)n, paint);
    }
    
    protected int d() {
        final Paint paint = new Paint();
        paint.setTextSize((float)this.ac);
        paint.setTypeface(Typeface.create("helvetica", 0));
        paint.setAntiAlias(true);
        paint.setAlpha(this.w);
        return (int)paint.getFontMetrics().descent;
    }
    
    protected int d(final int n, final int n2) {
        if (this.S > 0) {
            return this.S;
        }
        return this.S = n - this.e(n, n2) - this.af;
    }
    
    protected Paint d(final int n) {
        (this.aj = new Paint()).setTextSize((float)this.ac);
        this.aj.setColor(-65536);
        this.aj.setTypeface(Typeface.create("helvetica", 0));
        this.aj.setAntiAlias(true);
        this.aj.setAlpha(this.w);
        return this.aj;
    }
    
    protected void d(final Canvas canvas, final int n, final int n2, final int n3, final int n4) {
        final Paint b = this.b(n4);
        b.setTextAlign(Paint$Align.RIGHT);
        canvas.drawText("Get alerts when this movie arrives in theaters", (float)(n + n3), (float)n2, b);
        canvas.drawText("and on other viewing services", (float)(n + n3), (float)(this.ac + n2 + this.d()), b);
    }
    
    protected int e(final int n, final int n2) {
        return this.T = Math.round(this.b(n2).measureText("your email address")) + this.af * 4;
    }
    
    protected Paint e(final int n) {
        (this.q = new Paint()).setTextSize((float)this.ac);
        this.q.setColor(-7829368);
        this.q.setTypeface(Typeface.create("helvetica", 0));
        this.q.setAntiAlias(true);
        this.q.setAlpha(this.w);
        return this.q;
    }
    
    protected void e(final Canvas canvas, final int n, final int n2, final int n3, final int n4) {
        final Paint b = this.b(n4);
        b.setTextAlign(Paint$Align.RIGHT);
        canvas.drawText("You've successfully signed up for this quick alert.", (float)(n + n3), (float)n2, b);
        canvas.drawText("Expect an email from GoWatchIt soon!", (float)(n + n3), (float)(this.ac + n2 + this.d()), b);
    }
    
    protected int f(final int n, final int n2) {
        if (this.W > 0) {
            return this.W;
        }
        this.W = n - this.ab - this.af * 4 - this.g(n, n2);
        return this.U;
    }
    
    protected Paint f(final int n) {
        (this.ae = new Paint()).setTextSize((float)this.ac);
        this.ae.setColor(-65536);
        this.ae.setTypeface(Typeface.create("helvetica", 0));
        this.ae.setAntiAlias(true);
        this.ae.setAlpha(this.w);
        return this.ae;
    }
    
    protected void f(final Canvas canvas, final int n, final int n2, final int ag, final int n3) {
        final Paint c = this.c(n3);
        final Paint d = this.d(n3);
        if (this.G == null || ag != this.ag) {
            this.ag = ag;
            final Bitmap decodeFile = BitmapFactory.decodeFile(this.g.g().b() + "/comps/gowatchit/common/imgs/gowatchit_icon.png");
            this.am = decodeFile.getWidth() / decodeFile.getHeight() * n3;
            this.G = Bitmap.createScaledBitmap(decodeFile, this.am, n3, true);
            this.ak = this.am + Math.round(c.measureText(" Powered by GoWatchIt"));
        }
        final Paint paint = new Paint();
        paint.setFilterBitmap(true);
        paint.setAlpha(this.w);
        canvas.drawBitmap(this.G, (float)n, (float)n2, paint);
        canvas.drawText(" QUICK ALERT", (float)(this.am + n), (float)(this.c() + n2), c);
        canvas.drawText(" Powered by ", (float)(this.am + n), (float)(this.c() + n2 + this.ac + this.d()), c);
        canvas.drawText("GoWatchIt", (float)(Math.round(c.measureText(" Powered by ")) + (this.am + n)), (float)(this.c() + n2 + this.ac + this.d()), d);
    }
    
    protected int g(final int n, final int n2) {
        if (this.X > 0) {
            return this.X;
        }
        return this.X = Math.round(this.b(n2).measureText("You've successfully signed up for this quick alert."));
    }
    
    protected int h(final int n, final int n2) {
        return this.U = n - this.e(n, n2) - this.af * 2 - this.i(n, n2);
    }
    
    protected int i(final int n, final int n2) {
        return this.V = Math.round(this.b(n2).measureText("Get alerts when this movie arrives in theaters"));
    }
    
    protected Paint o() {
        if (this.r != null) {
            return this.r;
        }
        (this.r = new Paint()).setColor(Color.argb(this.w, 73, 73, 73));
        this.r.setStyle(Paint$Style.STROKE);
        return this.r;
    }
    
    protected a p() {
        return new a();
    }
    
    protected class a extends j
    {
        public float a;
        public int b;
        public float c;
        public int d;
        public float e;
        public int f;
        public float g;
        public int h;
        public float i;
        public int j;
        public boolean k;
        
        @Override
        public void a(final com.tremorvideo.sdk.android.richmedia.e e) {
            super.a(e);
            try {
                this.a = e.d();
                this.b = e.b();
                this.c = e.d();
                this.d = e.b();
                this.e = e.d();
                this.f = e.b();
                this.g = e.d();
                this.h = e.b();
                this.i = e.d();
                this.j = e.b();
                this.k = e.f();
            }
            catch (Exception ex) {
                com.tremorvideo.sdk.android.videoad.ac.e("SceneObjectGoWatchIt load Exception " + ex);
            }
        }
    }
    
    class b extends AsyncTask<String, Void, String>
    {
        String a;
        String b;
        
        protected String a(final String... array) {
            try {
                this.a = null;
                final BasicHttpParams basicHttpParams = new BasicHttpParams();
                ((HttpParams)basicHttpParams).setParameter("http.protocol.version", (Object)HttpVersion.HTTP_1_1);
                HttpConnectionParams.setConnectionTimeout((HttpParams)basicHttpParams, 10000);
                HttpConnectionParams.setSoTimeout((HttpParams)basicHttpParams, 10000);
                HttpConnectionParams.setTcpNoDelay((HttpParams)basicHttpParams, true);
                final DefaultHttpClient defaultHttpClient = new DefaultHttpClient((HttpParams)basicHttpParams);
                this.b = array[0];
                final HttpPost httpPost = new HttpPost(com.tremorvideo.sdk.android.richmedia.v.this.L);
                final ArrayList<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>(2);
                list.add(new BasicNameValuePair("email", this.b));
                list.add(new BasicNameValuePair("movieId", com.tremorvideo.sdk.android.richmedia.v.this.M));
                httpPost.setEntity((HttpEntity)new UrlEncodedFormEntity((List)list, "utf-8"));
                return this.a = EntityUtils.toString(((HttpClient)defaultHttpClient).execute((HttpUriRequest)httpPost).getEntity());
            }
            catch (Exception ex) {
                com.tremorvideo.sdk.android.videoad.ac.e("Exception GoWatchit call: " + ex);
                return null;
            }
        }
        
        protected void a(final String s) {
            Label_0026: {
                if (s == null) {
                    break Label_0026;
                }
                while (true) {
                    while (true) {
                        try {
                            if (s.length() > 0) {
                                com.tremorvideo.sdk.android.richmedia.v.this.a(s);
                            }
                            else {
                                com.tremorvideo.sdk.android.richmedia.v.this.O = "your email address";
                                com.tremorvideo.sdk.android.richmedia.v.this.P = -7829368;
                            }
                            this.cancel(true);
                            return;
                        }
                        catch (Exception ex) {
                            com.tremorvideo.sdk.android.videoad.ac.e("Exception " + ex);
                            continue;
                        }
                        continue;
                    }
                }
            }
        }
        
        protected void b(final String s) {
        }
    }
}
