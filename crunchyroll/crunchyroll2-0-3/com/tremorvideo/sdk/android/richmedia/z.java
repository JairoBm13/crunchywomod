// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.HttpVersion;
import org.apache.http.params.BasicHttpParams;
import android.os.AsyncTask;
import android.graphics.BitmapFactory;
import android.graphics.Paint$Style;
import android.graphics.Shader;
import android.graphics.LinearGradient;
import android.graphics.Shader$TileMode;
import android.graphics.Typeface;
import android.graphics.Color;
import com.tremorvideo.sdk.android.videoad.aw;
import org.json.JSONArray;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import org.json.JSONObject;
import android.graphics.Canvas;
import com.tremorvideo.sdk.android.videoad.ac;
import android.graphics.Paint;
import java.util.ArrayList;
import android.graphics.Bitmap;
import com.tremorvideo.sdk.android.videoad.ax;

public class z extends q implements d, e
{
    int A;
    int B;
    int C;
    int D;
    int E;
    int F;
    int G;
    int H;
    double I;
    int J;
    String K;
    String L;
    String M;
    int N;
    int O;
    int P;
    String Q;
    boolean R;
    String S;
    String T;
    String U;
    String V;
    String W;
    int X;
    int Y;
    Bitmap Z;
    Bitmap aa;
    boolean ab;
    int ac;
    ArrayList<b> ad;
    private final String ae;
    private final String af;
    private final String ag;
    private final String ah;
    private final String ai;
    private final String aj;
    private final String ak;
    private String al;
    private String am;
    Paint o;
    Paint p;
    Paint q;
    Paint r;
    Paint s;
    Paint t;
    int u;
    int v;
    int w;
    int x;
    int y;
    int z;
    
    public z(final o o) {
        super(o);
        this.u = 0;
        this.v = 0;
        this.w = 0;
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.A = 0;
        this.B = 0;
        this.C = 0;
        this.D = 0;
        this.E = 0;
        this.F = 0;
        this.G = 0;
        this.H = 0;
        this.I = 0.0;
        this.J = 0;
        this.O = 0;
        this.P = 0;
        this.ae = "Choose your cable provider  >";
        this.af = "Choose your cable provider";
        this.ag = ">";
        this.ah = "Find Channels";
        this.ai = "Searching...";
        this.aj = "No Provider for Zip Code";
        this.ak = "Enter Zip";
        this.Q = "Enter Zip";
        this.R = false;
        this.al = "Searching...";
        this.am = "Choose your cable provider  >";
        this.X = 0;
        this.ab = true;
        this.ac = com.tremorvideo.sdk.android.videoad.ac.H();
        this.ad = new ArrayList<b>();
    }
    
    @Override
    public void a() {
        super.a();
        this.u = 0;
        this.v = 0;
        this.E = 0;
        this.F = 0;
        this.G = 0;
        this.H = 0;
        this.w = 0;
        this.x = 0;
        this.C = 0;
        this.D = 0;
        this.o = null;
        this.p = null;
        this.r = null;
        this.s = null;
        this.t = null;
        this.J = 250;
        this.P = 0;
        this.ac = com.tremorvideo.sdk.android.videoad.ac.H();
    }
    
    protected void a(final Canvas canvas, final int n, final int n2, final int n3, final int n4) {
        final Paint c = this.c();
        canvas.drawRect((float)n, (float)n2, (float)(n + n3), (float)(n2 + n4), this.b(n2, n2 + n4));
        canvas.drawRect((float)n, (float)n2, (float)(n + n3 - 1), (float)(n2 + n4 - 1), c);
    }
    
    @Override
    public void a(final com.tremorvideo.sdk.android.richmedia.e e) {
        super.a(e);
        while (true) {
            try {
                this.K = e.c();
                com.tremorvideo.sdk.android.videoad.ac.e("SceneObjectStationFinder JSON is" + this.K);
                final JSONObject jsonObject = new JSONObject(this.K);
                final JSONObject jsonObject2 = jsonObject.getJSONObject("params");
                this.U = jsonObject.getString("zip_path");
                this.L = jsonObject2.getString("stationID");
                this.M = jsonObject2.getString("stationMessage");
                this.N = Integer.parseInt(jsonObject2.getString("numChannels"));
                this.V = jsonObject2.getString("textColor");
                this.W = jsonObject2.getString("backgroundColor");
                this.X = Integer.parseInt(jsonObject2.getString("backgroundAlpha"));
                this.Y = Integer.parseInt(jsonObject2.getString("cornerStyle"));
                this.h = true;
            }
            catch (Exception ex) {
                com.tremorvideo.sdk.android.videoad.ac.e("SceneObjectStationFinder Exception=" + ex);
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
                    p2.a(b - a4.x, b2 - a4.y, b3, b4, this.l, this.c.c());
                    final float h = p2.h();
                    final float i = p2.i();
                    final float j = p2.j();
                    final float k = p2.k();
                    float n2 = i;
                    float n3 = h;
                    if (this.c.a() != null) {
                        final k c3 = this.c.a().c(p2, n);
                        final float a5 = c3.a;
                        final float b5 = c3.b;
                        final Point a6 = com.tremorvideo.sdk.android.richmedia.b.a(Math.round(c3.f), Math.round(c3.g), this.c.b());
                        n3 = h + a5 + a6.x;
                        n2 = i + b5 + a6.y;
                    }
                    final RectF a7 = this.a(p2, n, new RectF(n3, n2, j + n3, k + n2));
                    final float left = a7.left;
                    final float top = a7.top;
                    final float width = a7.width();
                    final float height = a7.height();
                    this.O = com.tremorvideo.sdk.android.richmedia.ae.a(a2.i, a3.i, a2.j, a3.j, c);
                    final int round = Math.round(height);
                    final int round2 = Math.round(width);
                    final int round3 = Math.round(height);
                    final int c4 = this.c(round2, round3);
                    final int d = this.d(round2, round3);
                    final int l = this.k(round2, round3);
                    final int m = this.l(round2, round3);
                    final int e = this.e(round2, round3);
                    final int f = this.f(round2, round3);
                    final int h2 = this.h(round2, round3);
                    final int i2 = this.i(round2, round3);
                    c2.saveLayerAlpha(new RectF(new Rect(Math.round(left) - 1, Math.round(top), Math.round(a7.right) + 1, Math.round(a7.bottom))), this.O, 1);
                    c2.translate((float)Math.round(left), (float)Math.round(top));
                    this.a(c2, 0, 0, round2, round3);
                    if (this.R) {
                        this.e(c2, f, 0, h2, round);
                        this.f(c2, h2, 0, i2, round);
                    }
                    else {
                        this.c(c2, c4, 0, d, round);
                        this.d(c2, l, 0, m, round);
                        if (e >= 0) {
                            this.g(c2, e, 0, l - 1, round);
                        }
                    }
                    c2.restore();
                }
            }
        }
    }
    
    public void a(final String s) {
        boolean b = false;
        if (s != null) {
            StringBuffer sb = null;
            while (true) {
            Label_0073_Outer:
                while (true) {
                    int n2 = 0;
                Label_0215:
                    while (true) {
                        Label_0157: {
                            try {
                                final JSONArray jsonArray = new JSONArray(s);
                                int n;
                                if (jsonArray.length() > this.N) {
                                    n = this.N;
                                }
                                else {
                                    n = jsonArray.length();
                                }
                                sb = new StringBuffer();
                                n2 = 0;
                                if (n2 >= n) {
                                    break;
                                }
                                if (n2 > 0) {
                                    if (n2 + 1 != n) {
                                        break Label_0157;
                                    }
                                    if (n2 == 1) {
                                        sb.append(" or ");
                                    }
                                    else {
                                        sb.append(", or ");
                                    }
                                }
                                final String string = jsonArray.getJSONObject(n2).getString("channel");
                                if (string != null) {
                                    sb.append(string);
                                    b = true;
                                }
                                break Label_0215;
                            }
                            catch (Exception ex) {
                                this.al = "Not Available";
                                com.tremorvideo.sdk.android.videoad.ac.e("Failed to get Channel " + ex);
                            }
                            return;
                        }
                        sb.append(", ");
                        continue;
                    }
                    ++n2;
                    continue Label_0073_Outer;
                }
            }
            if (b && sb != null && sb.length() > 0) {
                this.al = this.M.replace("%CHANNEL#%", sb.toString());
                return;
            }
            this.al = "Not Available";
        }
    }
    
    public void a(final String s, final String s2) {
        int i = 0;
        if (s != null) {
            try {
                this.ad.clear();
                final JSONArray jsonArray = new JSONArray(s);
                boolean b = false;
                while (i < jsonArray.length()) {
                    final JSONObject jsonObject = jsonArray.getJSONObject(i);
                    final String string = jsonObject.getString("name");
                    final String string2 = jsonObject.getString("lineupId");
                    b = true;
                    this.ad.add(new b(string, string2));
                    ++i;
                }
                if (b) {
                    this.am = "Choose your cable provider  >";
                }
                else {
                    this.am = "No Provider for Zip Code";
                }
                if (s2 != null && s2.equals("launch") && !this.ad.isEmpty()) {
                    this.g.g().c().a(aw.b.at, (ax.e)this, this.ad, this.T);
                }
            }
            catch (Exception ex) {
                com.tremorvideo.sdk.android.videoad.ac.e("Failed to get the Provider " + ex);
                this.am = "No Provider for Zip Code";
            }
        }
    }
    
    @Override
    public boolean a(final int n, int n2) {
        while (true) {
            while (true) {
                Label_0380: {
                    try {
                        if (this.S == null || this.T == null) {
                            final String b = com.tremorvideo.sdk.android.richmedia.ae.b(this.g.g().b() + "/" + this.U + "native_config.txt");
                            if (b != null && b.length() > 0) {
                                final JSONArray jsonArray = new JSONObject(b).getJSONArray("events");
                                n2 = 0;
                                if (n2 < jsonArray.length()) {
                                    final JSONObject jsonObject = jsonArray.getJSONObject(n2);
                                    if (jsonObject.getString("name").equals("Interact_ZipCodeFinder_Click")) {
                                        this.S = jsonObject.getString("tag");
                                        break Label_0380;
                                    }
                                    if (jsonObject.getString("name").equals("Interact_SelectServiceProvider_Click")) {
                                        this.T = jsonObject.getString("tag");
                                    }
                                    break Label_0380;
                                }
                            }
                        }
                    }
                    catch (Exception ex) {}
                    break;
                }
                ++n2;
                continue;
            }
        }
        if (this.R) {
            if (n >= this.y && n < this.y + this.z) {
                this.R = false;
                this.al = "Searching...";
            }
        }
        else {
            if (n >= this.G && n < this.G + this.H) {
                this.g.g().c().a(aw.b.au, (ax.e)this, null, this.S);
                return true;
            }
            if (n >= this.C && n < this.C + this.D && !this.am.equals("No Provider for Zip Code") && !this.Q.equals("Enter Zip")) {
                if (this.ad != null && !this.ad.isEmpty()) {
                    this.g.g().c().a(aw.b.at, (ax.e)this, this.ad, this.T);
                    return true;
                }
                this.am = "Searching...";
                new c().execute((Object[])new String[] { "provider", "launch" });
            }
        }
        return false;
    }
    
    protected Paint b(final int n) {
        if (this.p != null) {
            return this.p;
        }
        (this.p = new Paint()).setTextSize((float)this.ac);
        this.p.setColor(Color.parseColor(this.V));
        this.p.setTypeface(Typeface.create("helvetica", 0));
        this.p.setAntiAlias(true);
        this.p.setAlpha(this.O);
        return this.p;
    }
    
    protected Paint b(final int n, final int n2) {
        if (this.o != null) {
            return this.o;
        }
        final int color = Color.parseColor(this.W);
        final LinearGradient shader = new LinearGradient(0.0f, (float)n, 0.0f, (float)n2, color, color, Shader$TileMode.CLAMP);
        (this.o = new Paint()).setDither(true);
        this.o.setShader((Shader)shader);
        this.o.setAlpha(this.X * 255 / 100);
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
        this.R = false;
        this.al = "Searching...";
        this.am = "Choose your cable provider  >";
        this.ab = true;
        this.Q = "Enter Zip";
        if (this.ad != null) {
            this.ad.clear();
        }
    }
    
    protected void b(final Canvas canvas, final int n, final int n2, final int n3, final int n4) {
        final Paint paint = new Paint();
        paint.setColor(Color.argb(this.O, 255, 255, 255));
        paint.setStyle(Paint$Style.FILL);
        if (this.Y > 0) {
            canvas.drawRoundRect(new RectF((float)n, (float)n2, (float)(n + n3 - 1), (float)(n2 + n4 - 1)), (float)this.Y, (float)this.Y, paint);
            return;
        }
        canvas.drawRect((float)n, (float)n2, (float)(n + n3 - 1), (float)(n2 + n4 - 1), paint);
    }
    
    protected int c(final int n, final int n2) {
        if (this.C > 0) {
            return this.C;
        }
        return this.C = n - this.d(n, n2) - this.P;
    }
    
    protected Paint c() {
        if (this.r != null) {
            return this.r;
        }
        (this.r = new Paint()).setColor(Color.parseColor(this.W));
        this.r.setStyle(Paint$Style.STROKE);
        this.r.setAlpha(this.X * 255 / 100);
        return this.r;
    }
    
    protected Paint c(final int n) {
        if (this.q != null) {
            return this.q;
        }
        (this.q = new Paint()).setTextSize((float)this.ac);
        this.q.setColor(-16777216);
        this.q.setTypeface(Typeface.create("helvetica", 0));
        this.q.setAntiAlias(true);
        this.q.setAlpha(this.O);
        return this.q;
    }
    
    protected void c(final Canvas canvas, final int n, int n2, final int n3, final int n4) {
        final int d = this.d(n3, n4);
        final Paint c = this.c(n4);
        final int round = Math.round(c.measureText(">"));
        n2 += Math.round((n4 - c.getTextSize()) / 2.0f);
        final int ac = this.ac;
        final int n5 = n + Math.round(ac / 2);
        final int n6 = ac * 3;
        this.b(canvas, n, n4 / 2 - n6 / 2, n3, n6);
        if (this.am.equals("Choose your cable provider  >")) {
            canvas.drawText("Choose your cable provider", (float)n5, n2 + c.getTextSize() - c.descent(), c);
            canvas.drawText(">", (float)(n + d - Math.round(ac / 2) - round), n2 + c.getTextSize() - c.descent(), c);
            return;
        }
        canvas.drawText(this.am, (float)n5, n2 + c.getTextSize() - c.descent(), c);
    }
    
    protected int d(final int n, final int n2) {
        if (this.D > 0) {
            return this.D;
        }
        final int n3 = Math.round(this.c(n2).measureText("Choose your cable provider  >")) + this.ac;
        final int n4 = n - n3 - this.l(n, n2) - this.j(n, n2);
        if (this.P == 0) {
            if (n4 - 4 > 0) {
                int ac;
                if ((ac = n4 / 3) > this.ac) {
                    ac = this.ac;
                }
                this.P = ac;
            }
            else {
                int ac2;
                if ((ac2 = (n - n3 - this.l(n, n2)) / 3) > this.ac) {
                    ac2 = this.ac;
                }
                this.P = ac2;
            }
        }
        if (n4 - 4 > 0) {
            this.D = n - n / 3 - this.P * 3 - this.l(n, n2);
        }
        else {
            this.D = n - this.P * 3 - this.l(n, n2);
        }
        return this.D;
    }
    
    protected a d() {
        return new a();
    }
    
    @Override
    public void d(final int n) {
        com.tremorvideo.sdk.android.videoad.ac.e("SceneObjectStationFinder:onProviderSelected at position=" + n);
        this.R = true;
        new c().execute((Object[])new String[] { "channel", null, this.ad.get(n).b });
    }
    
    protected void d(final Canvas canvas, final int n, int round, final int n2, final int n3) {
        final int l = this.l(n2, n3);
        final Paint c = this.c(n3);
        if (this.ab) {
            final String d = this.g.g().a.d;
            this.ab = false;
            if (d != null && d.length() >= 1) {
                this.Q = d;
                this.am = "Searching...";
                new c().execute((Object[])new String[] { "provider", null });
            }
        }
        final int ac = this.ac;
        final int n4 = ac * 3;
        final int n5 = n3 / 2 - n4 / 2;
        final int round2 = Math.round(ac / 2);
        round = Math.round(Math.round((n3 - c.getTextSize()) / 2.0f) + round + c.getTextSize() - c.descent());
        this.b(canvas, n, n5, n2, n4);
        canvas.drawText(this.Q, (float)(n + round2), (float)round, c);
        round = Math.round(ac / 2) + ac;
        if (this.Z == null) {
            this.Z = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(this.g.g().b() + "/comps/station_finder/common/magnifying_glass_large.png"), round, round, true);
        }
        final Paint paint = new Paint();
        paint.setFilterBitmap(true);
        paint.setAlpha(this.O);
        canvas.drawBitmap(this.Z, (float)(n + l - ac / 2 - round), (float)(n5 + n4 / 2 - round / 2), paint);
    }
    
    protected int e(final int n, final int n2) {
        if (n - Math.round(this.b(n2).measureText("Find Channels") + this.ac) - this.d(n, n2) - this.l(n, n2) >= 0 && this.P > 0) {
            return this.P;
        }
        return -1;
    }
    
    protected void e(final Canvas canvas, final int n, int ac, int round, int round2) {
        ac = this.ac;
        round = ac * 2;
        if (this.aa == null) {
            this.aa = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(this.g.g().b() + "/comps/station_finder/common/reset.png"), round, round, true);
        }
        final Paint paint = new Paint();
        paint.setFilterBitmap(true);
        paint.setAlpha(this.O);
        round2 = Math.round(round2 / 2);
        round = Math.round(round / 2);
        canvas.drawBitmap(this.aa, (float)(ac + n), (float)(round2 - round), paint);
    }
    
    protected int f(final int n, final int n2) {
        return 0;
    }
    
    protected void f(final Canvas canvas, final int n, final int n2, int i, int round) {
        i = this.i(i, round);
        final Paint b = this.b(round);
        i = (i - (Math.round(b.measureText(this.al)) + 20)) / 2;
        round = Math.round((round - b.getTextSize()) / 2.0f);
        canvas.drawText(this.al, (float)(i + n), round + n2 + b.getTextSize() - b.descent(), b);
    }
    
    @Override
    public void f(final String q) {
        int n;
        if (this.Q.equals(q) && this.ad != null && !this.ad.isEmpty()) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (n == 0) {
            this.am = "Searching...";
            this.ad.clear();
            this.Q = q;
            com.tremorvideo.sdk.android.videoad.ac.e("SceneObjectStationFinder:onZipChanged zip=" + this.Q);
            new c().execute((Object[])new String[] { "provider", null });
        }
    }
    
    protected int g(final int n, final int n2) {
        if (this.z > 0) {
            return this.z;
        }
        return this.z = this.ac * 4;
    }
    
    protected void g(final Canvas canvas, final int n, final int n2, int j, int round) {
        j = this.j(j, round);
        final Paint b = this.b(round);
        j = (j - Math.round(b.measureText("Find Channels"))) / 2;
        round = Math.round((round - b.getTextSize()) / 2.0f);
        canvas.drawText("Find Channels", (float)(j + n), round + n2 + b.getTextSize() - b.descent(), b);
    }
    
    protected int h(int n, int n2) {
        final int g = this.g(n, n2);
        n2 = (n = (n - g) / 2);
        if (n2 < g) {
            n = this.ac + g;
        }
        return n;
    }
    
    protected int i(final int n, final int n2) {
        if (this.B > 0) {
            return this.B;
        }
        return this.B = Math.round(this.b(n2).measureText(this.al) + 20.0f);
    }
    
    protected int j(final int n, final int n2) {
        if (this.x > 0) {
            return this.x;
        }
        return this.x = n / 3;
    }
    
    protected int k(final int n, final int n2) {
        if (this.G > 0) {
            return this.G;
        }
        if (this.e(n, n2) > 0) {
            this.G = n / 3 + this.P;
        }
        else {
            this.G = this.P;
        }
        return this.G;
    }
    
    protected int l(int ac, final int n) {
        if (this.H > 0) {
            return this.H;
        }
        final Paint c = this.c(n);
        ac = this.ac;
        return this.H = Math.round(c.measureText("Enter Zip") + ac * 3);
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
            catch (Exception ex) {}
        }
    }
    
    public class b
    {
        public String a;
        public String b;
        
        public b(final String a, final String b) {
            this.a = a;
            this.b = b;
        }
        
        @Override
        public String toString() {
            return this.a;
        }
    }
    
    class c extends AsyncTask<String, Void, String>
    {
        String a;
        String b;
        String c;
        String d;
        String e;
        final String f;
        final String g;
        
        public c() {
            this.f = "http://data.tmsapi.com/v1/lineups?postalCode=__ZIP__&api_key=dn3gkyv4pzcmfcp5fakh7raz";
            this.g = "http://data.tmsapi.com/v1/stations/__STATION__?lineupId=__LINEUP__&api_key=dn3gkyv4pzcmfcp5fakh7raz";
        }
        
        protected String a(final String... array) {
            try {
                this.a = null;
                final BasicHttpParams basicHttpParams = new BasicHttpParams();
                ((HttpParams)basicHttpParams).setParameter("http.protocol.version", (Object)HttpVersion.HTTP_1_1);
                HttpConnectionParams.setTcpNoDelay((HttpParams)basicHttpParams, true);
                final DefaultHttpClient defaultHttpClient = new DefaultHttpClient((HttpParams)basicHttpParams);
                this.e = array[0];
                this.b = array[1];
                if (this.e.equals("provider")) {
                    this.d = "http://data.tmsapi.com/v1/lineups?postalCode=__ZIP__&api_key=dn3gkyv4pzcmfcp5fakh7raz".replace("__ZIP__", "" + com.tremorvideo.sdk.android.richmedia.z.this.Q);
                }
                else if (this.e.equals("channel")) {
                    this.c = array[2];
                    this.d = "http://data.tmsapi.com/v1/stations/__STATION__?lineupId=__LINEUP__&api_key=dn3gkyv4pzcmfcp5fakh7raz".replace("__STATION__", "" + com.tremorvideo.sdk.android.richmedia.z.this.L);
                    this.d = this.d.replace("__LINEUP__", "" + this.c);
                }
                final HttpGet httpGet = new HttpGet(this.d);
                com.tremorvideo.sdk.android.richmedia.ae.a(httpGet, this.d);
                return this.a = EntityUtils.toString(((HttpClient)defaultHttpClient).execute((HttpUriRequest)httpGet).getEntity());
            }
            catch (Exception ex) {
                com.tremorvideo.sdk.android.videoad.ac.a(ex);
                return null;
            }
        }
        
        protected void a(final String s) {
            if (this.e.equals("provider")) {
                com.tremorvideo.sdk.android.richmedia.z.this.a(s, this.b);
            }
            else if (this.e.equals("channel")) {
                com.tremorvideo.sdk.android.richmedia.z.this.a(s);
            }
        }
        
        protected void b(final String s) {
        }
    }
}
