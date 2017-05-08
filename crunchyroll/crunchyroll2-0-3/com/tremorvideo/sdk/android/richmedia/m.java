// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import android.graphics.Paint$Style;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.graphics.RectF;
import org.json.JSONObject;
import com.tremorvideo.sdk.android.richmedia.a.i;
import com.tremorvideo.sdk.android.videoad.aw;
import java.util.ArrayList;
import android.graphics.Canvas;
import com.tremorvideo.sdk.android.videoad.ac;
import java.util.GregorianCalendar;
import java.util.Iterator;
import android.content.Context;
import com.tremorvideo.sdk.android.videoad.h;

public class m
{
    ad[] a;
    public boolean b;
    private long c;
    private o d;
    private a e;
    private p f;
    private o.b g;
    private int h;
    private boolean i;
    private h j;
    private ScriptInterpreter k;
    private g l;
    private q m;
    private boolean n;
    private boolean o;
    private com.tremorvideo.sdk.android.videoad.o p;
    
    public m(final a e, final com.tremorvideo.sdk.android.richmedia.a a, final Context context, final boolean n) {
        this.h = 1;
        this.i = true;
        this.a = new ad[0];
        this.n = false;
        this.o = false;
        this.b = false;
        this.p = null;
        this.n = n;
        this.e = e;
        this.f = new p();
        this.f.f = a.r().a();
        this.j = null;
        (this.k = new ScriptInterpreter(context)).load(this, a);
        this.p = new com.tremorvideo.sdk.android.videoad.o(context.getResources().getDisplayMetrics());
    }
    
    private float a(final k k, final k i) {
        final float a = k.a;
        final float n = k.f / 2.0f;
        final float b = k.b;
        final float n2 = k.g / 2.0f;
        final float a2 = i.a;
        final float n3 = i.f / 2.0f;
        final float b2 = i.b;
        final float n4 = i.g / 2.0f;
        final float n5 = a + n - (a2 + n3);
        final float n6 = b + n2 - (b2 + n4);
        return (float)Math.sqrt(n5 * n5 + n6 * n6);
    }
    
    private q a(final q q) {
        if (!this.n) {
            return null;
        }
        if (q == null) {
            return null;
        }
        final int a = q.i().a(com.tremorvideo.sdk.android.richmedia.h.c.d).a();
        for (final q q2 : this.d.e()) {
            final com.tremorvideo.sdk.android.richmedia.h.a a2 = q2.i().a(com.tremorvideo.sdk.android.richmedia.h.c.d);
            if (a2 != null && a2.a() == a) {
                return q2;
            }
        }
        return null;
    }
    
    private boolean b(final k k, final k i) {
        return Math.abs(k.a + k.f / 2.0f - (i.a + i.f / 2.0f)) > Math.abs(k.b + k.g / 2.0f - (i.b + i.g / 2.0f));
    }
    
    private q p() {
        k k = null;
        if (!this.n) {
            return null;
        }
        final Iterator<q> iterator = this.d.e().iterator();
        q q = null;
        while (iterator.hasNext()) {
            final q q2 = iterator.next();
            final com.tremorvideo.sdk.android.richmedia.h.a a = q2.i().a(com.tremorvideo.sdk.android.richmedia.h.c.d);
            if (a != null && a.c() != com.tremorvideo.sdk.android.richmedia.h.b.u) {
                final k c = q2.c(this.f, 0L);
                if (c.f == 0.0f || c.g == 0.0f) {
                    continue;
                }
                k i = null;
                q q3 = null;
                Label_0113: {
                    if (q == null) {
                        i = c;
                        q3 = q2;
                    }
                    else {
                        if (c.b == k.b) {
                            if (c.a < k.a) {
                                i = c;
                                q3 = q2;
                                break Label_0113;
                            }
                        }
                        else if (c.b < k.b) {
                            i = c;
                            q3 = q2;
                            break Label_0113;
                        }
                        q3 = q;
                        i = k;
                    }
                }
                k = i;
                q = q3;
            }
        }
        return q;
    }
    
    public k a(final q q, final long n) {
        final k c = q.c(this.f, n);
        c.a += this.f.d() / 2.0f;
        c.b += this.f.e() / 2.0f;
        return c;
    }
    
    public void a() {
        this.k.destroy();
    }
    
    public void a(final int n, final int n2) {
        this.e.a(n, n2);
    }
    
    public void a(final int n, final int n2, final int n3) {
        this.d.g().a(new GregorianCalendar(n, n2, n3));
    }
    
    public void a(final int h, final boolean i) {
        this.h = h;
        this.i = i;
    }
    
    public void a(long n) {
    Label_0249_Outer:
        while (true) {
            while (true) {
                Label_0332: {
                Label_0313:
                    while (true) {
                        int n2 = 0;
                        Label_0306: {
                            try {
                                if (this.d == null) {
                                    break;
                                }
                                if (this.l != null) {
                                    this.l.a(n);
                                }
                                if (this.g != com.tremorvideo.sdk.android.richmedia.o.b.a || this.b || this.o) {
                                    break;
                                }
                                n *= this.h;
                                n2 = 0;
                                if (n2 < this.d.i().size()) {
                                    final com.tremorvideo.sdk.android.richmedia.h.a a = this.d.i().get(n2);
                                    if (a.b() != com.tremorvideo.sdk.android.richmedia.h.c.p) {
                                        break Label_0306;
                                    }
                                    final int intValue = (int)a.e();
                                    if (this.h == 1) {
                                        if (intValue <= this.c || intValue > this.c + n) {
                                            break Label_0306;
                                        }
                                        this.b = true;
                                        this.c = intValue;
                                    }
                                    else {
                                        if (intValue >= this.c || intValue < this.c + n) {
                                            break Label_0306;
                                        }
                                        this.b = true;
                                        this.c = intValue;
                                    }
                                }
                                if (!this.b) {
                                    this.c += n;
                                }
                                if (this.h == 1) {
                                    if (this.c >= this.d.c()) {
                                        final int d = this.d.d();
                                        this.a(com.tremorvideo.sdk.android.richmedia.h.c.g);
                                        if (this.h == 1 && d == this.d.d()) {
                                            if (!this.i) {
                                                break Label_0313;
                                            }
                                            this.c = 0L;
                                            this.i();
                                        }
                                    }
                                    this.d.a(this, n);
                                    return;
                                }
                                break Label_0332;
                            }
                            catch (Exception ex) {
                                ac.a(ex);
                                return;
                            }
                        }
                        ++n2;
                        continue Label_0249_Outer;
                    }
                    this.c = this.d.c();
                    this.b = true;
                    continue;
                }
                if (this.c > 0L || this.b) {
                    continue;
                }
                if (this.i) {
                    this.c = this.d.c();
                    this.i();
                    continue;
                }
                this.b = true;
                this.c = 0L;
                continue;
            }
        }
    }
    
    public void a(final long c, final boolean b) {
        this.c = c;
        if (c == 0L && this.g == com.tremorvideo.sdk.android.richmedia.o.b.a && !b) {
            this.a(com.tremorvideo.sdk.android.richmedia.h.c.f);
        }
        if (this.c == this.d.c()) {
            this.a(com.tremorvideo.sdk.android.richmedia.h.c.g);
        }
    }
    
    public void a(final Canvas canvas) {
        this.p.a(canvas, this.e.e());
    }
    
    public void a(final af af, final ArrayList<aw> list) {
        this.d.a(this.c, af, this.h, this.i, list);
    }
    
    public void a(final com.tremorvideo.sdk.android.richmedia.h.a a) {
        if (a != null && a.d() != null) {
            this.k.callFunction(a.d());
        }
    }
    
    public void a(final com.tremorvideo.sdk.android.richmedia.h.c c) {
        final com.tremorvideo.sdk.android.richmedia.h.a a = this.d.a(c);
        if (a != null) {
            this.e.a(a);
        }
    }
    
    public void a(final o.b g) {
        this.g = g;
    }
    
    public void a(final o d, final boolean b) {
        this.o = true;
        this.c = 0L;
        this.b = false;
        this.h = 1;
        this.i = true;
        if (this.d != null) {
            this.d.g().w();
            if (this.d.d() != d.d()) {
                this.a(com.tremorvideo.sdk.android.richmedia.h.c.h);
            }
        }
        this.d = d;
        this.g = d.f();
        this.d.b();
        if (d.h() != -1) {
            this.l = new g(this.d.a(d.h()));
        }
        else {
            this.l = null;
        }
        this.m = this.a(this.m);
        final com.tremorvideo.sdk.android.richmedia.b.a j = this.d.j();
        if (j != null && this.m()) {
            j.a(this.f);
        }
        else {
            this.d.g().v();
        }
        final ArrayList<i> k = d.k();
        if (k != null && !k.isEmpty()) {
            for (final i i : k) {
                if (this.m()) {
                    i.a(this.f, i.h());
                }
            }
        }
        else {
            this.d.g().w();
        }
        if (b) {
            this.a(this.d.m(), true);
        }
        else {
            this.a(com.tremorvideo.sdk.android.richmedia.h.c.f);
        }
        this.i();
        this.o = false;
    }
    
    public void a(final h j) {
        this.j = j;
    }
    
    public void a(final String s) {
        this.d.g().b(s);
    }
    
    public void a(final JSONObject p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_1        
        //     1: ifnonnull       16
        //     4: aload_0        
        //     5: getfield        com/tremorvideo/sdk/android/richmedia/m.d:Lcom/tremorvideo/sdk/android/richmedia/o;
        //     8: invokevirtual   com/tremorvideo/sdk/android/richmedia/o.g:()Lcom/tremorvideo/sdk/android/richmedia/a;
        //    11: aconst_null    
        //    12: invokevirtual   com/tremorvideo/sdk/android/richmedia/a.a:([Lcom/tremorvideo/sdk/android/richmedia/ad;)V
        //    15: return         
        //    16: new             Ljava/util/ArrayList;
        //    19: dup            
        //    20: invokespecial   java/util/ArrayList.<init>:()V
        //    23: astore          6
        //    25: new             Ljava/text/SimpleDateFormat;
        //    28: dup            
        //    29: ldc_w           "h:mma"
        //    32: invokespecial   java/text/SimpleDateFormat.<init>:(Ljava/lang/String;)V
        //    35: astore          7
        //    37: new             Ljava/text/SimpleDateFormat;
        //    40: dup            
        //    41: ldc_w           "hh:mm"
        //    44: invokespecial   java/text/SimpleDateFormat.<init>:(Ljava/lang/String;)V
        //    47: astore          8
        //    49: aload_1        
        //    50: ldc_w           "theatresAndShowtimesByMovie"
        //    53: invokevirtual   org/json/JSONObject.getJSONObject:(Ljava/lang/String;)Lorg/json/JSONObject;
        //    56: ldc_w           "theatres"
        //    59: invokevirtual   org/json/JSONObject.getJSONArray:(Ljava/lang/String;)Lorg/json/JSONArray;
        //    62: astore          9
        //    64: new             Ljava/lang/StringBuilder;
        //    67: dup            
        //    68: invokespecial   java/lang/StringBuilder.<init>:()V
        //    71: astore          10
        //    73: new             Ljava/util/ArrayList;
        //    76: dup            
        //    77: invokespecial   java/util/ArrayList.<init>:()V
        //    80: astore          11
        //    82: iconst_0       
        //    83: istore_2       
        //    84: aconst_null    
        //    85: astore_1       
        //    86: iload_2        
        //    87: aload           9
        //    89: invokevirtual   org/json/JSONArray.length:()I
        //    92: if_icmpge       622
        //    95: aload           11
        //    97: invokevirtual   java/util/ArrayList.clear:()V
        //   100: aload_1        
        //   101: astore          4
        //   103: aload           9
        //   105: iload_2        
        //   106: invokevirtual   org/json/JSONArray.getJSONObject:(I)Lorg/json/JSONObject;
        //   109: astore          5
        //   111: aload_1        
        //   112: astore          4
        //   114: aload           10
        //   116: iconst_0       
        //   117: invokevirtual   java/lang/StringBuilder.setLength:(I)V
        //   120: aload_1        
        //   121: astore          4
        //   123: aload           10
        //   125: aload           5
        //   127: ldc_w           "theatreName"
        //   130: invokevirtual   org/json/JSONObject.getString:(Ljava/lang/String;)Ljava/lang/String;
        //   133: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   136: pop            
        //   137: aload_1        
        //   138: astore          4
        //   140: aload           10
        //   142: ldc_w           ":  "
        //   145: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   148: pop            
        //   149: aload_1        
        //   150: astore          4
        //   152: aload           11
        //   154: new             Lcom/tremorvideo/sdk/android/richmedia/ad;
        //   157: dup            
        //   158: aload           10
        //   160: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   163: getstatic       com/tremorvideo/sdk/android/richmedia/ad$a.b:Lcom/tremorvideo/sdk/android/richmedia/ad$a;
        //   166: invokespecial   com/tremorvideo/sdk/android/richmedia/ad.<init>:(Ljava/lang/String;Lcom/tremorvideo/sdk/android/richmedia/ad$a;)V
        //   169: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   172: pop            
        //   173: aload_1        
        //   174: astore          4
        //   176: aload           5
        //   178: ldc_w           "theatreDays"
        //   181: invokevirtual   org/json/JSONObject.getJSONArray:(Ljava/lang/String;)Lorg/json/JSONArray;
        //   184: astore          12
        //   186: aload_1        
        //   187: astore          4
        //   189: aload           12
        //   191: iconst_0       
        //   192: invokevirtual   org/json/JSONArray.getJSONObject:(I)Lorg/json/JSONObject;
        //   195: ldc_w           "movies"
        //   198: invokevirtual   org/json/JSONObject.getJSONArray:(Ljava/lang/String;)Lorg/json/JSONArray;
        //   201: iconst_0       
        //   202: invokevirtual   org/json/JSONArray.getJSONObject:(I)Lorg/json/JSONObject;
        //   205: ldc_w           "showtimes"
        //   208: invokevirtual   org/json/JSONObject.getJSONArray:(Ljava/lang/String;)Lorg/json/JSONArray;
        //   211: astore          5
        //   213: aload_1        
        //   214: astore          4
        //   216: aload_1        
        //   217: ifnonnull       351
        //   220: aload_1        
        //   221: astore          4
        //   223: aload           12
        //   225: iconst_0       
        //   226: invokevirtual   org/json/JSONArray.getJSONObject:(I)Lorg/json/JSONObject;
        //   229: ldc_w           "day"
        //   232: invokevirtual   org/json/JSONObject.getString:(Ljava/lang/String;)Ljava/lang/String;
        //   235: ldc_w           "-"
        //   238: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //   241: astore          12
        //   243: aload_1        
        //   244: astore          4
        //   246: new             Ljava/util/GregorianCalendar;
        //   249: dup            
        //   250: aload           12
        //   252: iconst_0       
        //   253: aaload         
        //   254: invokestatic    java/lang/Integer.valueOf:(Ljava/lang/String;)Ljava/lang/Integer;
        //   257: invokevirtual   java/lang/Integer.intValue:()I
        //   260: aload           12
        //   262: iconst_1       
        //   263: aaload         
        //   264: invokestatic    java/lang/Integer.valueOf:(Ljava/lang/String;)Ljava/lang/Integer;
        //   267: invokevirtual   java/lang/Integer.intValue:()I
        //   270: iconst_1       
        //   271: isub           
        //   272: aload           12
        //   274: iconst_2       
        //   275: aaload         
        //   276: invokestatic    java/lang/Integer.valueOf:(Ljava/lang/String;)Ljava/lang/Integer;
        //   279: invokevirtual   java/lang/Integer.intValue:()I
        //   282: invokespecial   java/util/GregorianCalendar.<init>:(III)V
        //   285: astore          12
        //   287: aload_1        
        //   288: astore          4
        //   290: new             Ljava/text/SimpleDateFormat;
        //   293: dup            
        //   294: ldc_w           "EEEE, MMMM d"
        //   297: invokespecial   java/text/SimpleDateFormat.<init>:(Ljava/lang/String;)V
        //   300: aload           12
        //   302: invokevirtual   java/util/GregorianCalendar.getTime:()Ljava/util/Date;
        //   305: invokevirtual   java/text/SimpleDateFormat.format:(Ljava/util/Date;)Ljava/lang/String;
        //   308: astore_1       
        //   309: aload_1        
        //   310: astore          4
        //   312: aload           11
        //   314: new             Lcom/tremorvideo/sdk/android/richmedia/ad;
        //   317: dup            
        //   318: new             Ljava/lang/StringBuilder;
        //   321: dup            
        //   322: invokespecial   java/lang/StringBuilder.<init>:()V
        //   325: aload_1        
        //   326: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   329: ldc_w           "  ||  "
        //   332: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   335: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   338: getstatic       com/tremorvideo/sdk/android/richmedia/ad$a.a:Lcom/tremorvideo/sdk/android/richmedia/ad$a;
        //   341: invokespecial   com/tremorvideo/sdk/android/richmedia/ad.<init>:(Ljava/lang/String;Lcom/tremorvideo/sdk/android/richmedia/ad$a;)V
        //   344: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   347: pop            
        //   348: aload_1        
        //   349: astore          4
        //   351: aload           4
        //   353: astore_1       
        //   354: iconst_0       
        //   355: istore_3       
        //   356: iload_3        
        //   357: aload           5
        //   359: invokevirtual   org/json/JSONArray.length:()I
        //   362: if_icmpge       553
        //   365: aload           5
        //   367: iload_3        
        //   368: invokevirtual   org/json/JSONArray.getJSONObject:(I)Lorg/json/JSONObject;
        //   371: ldc_w           "datetime"
        //   374: invokevirtual   org/json/JSONObject.getString:(Ljava/lang/String;)Ljava/lang/String;
        //   377: ldc_w           "T"
        //   380: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //   383: iconst_1       
        //   384: aaload         
        //   385: astore          12
        //   387: aload           8
        //   389: aload           12
        //   391: invokevirtual   java/text/DateFormat.parse:(Ljava/lang/String;)Ljava/util/Date;
        //   394: astore          4
        //   396: aload           10
        //   398: iconst_0       
        //   399: invokevirtual   java/lang/StringBuilder.setLength:(I)V
        //   402: aload           10
        //   404: aload           7
        //   406: aload           4
        //   408: invokevirtual   java/text/DateFormat.format:(Ljava/util/Date;)Ljava/lang/String;
        //   411: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   414: pop            
        //   415: iload_3        
        //   416: aload           5
        //   418: invokevirtual   org/json/JSONArray.length:()I
        //   421: iconst_1       
        //   422: isub           
        //   423: if_icmpge       521
        //   426: aload           10
        //   428: ldc_w           ", "
        //   431: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   434: pop            
        //   435: aconst_null    
        //   436: astore          4
        //   438: aload           5
        //   440: iload_3        
        //   441: invokevirtual   org/json/JSONArray.getJSONObject:(I)Lorg/json/JSONObject;
        //   444: ldc_w           "clickURL"
        //   447: invokevirtual   org/json/JSONObject.has:(Ljava/lang/String;)Z
        //   450: ifeq            491
        //   453: new             Ljava/lang/StringBuilder;
        //   456: dup            
        //   457: invokespecial   java/lang/StringBuilder.<init>:()V
        //   460: aload           5
        //   462: iload_3        
        //   463: invokevirtual   org/json/JSONArray.getJSONObject:(I)Lorg/json/JSONObject;
        //   466: ldc_w           "clickURL"
        //   469: invokevirtual   org/json/JSONObject.getString:(Ljava/lang/String;)Ljava/lang/String;
        //   472: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   475: ldc_w           "+"
        //   478: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   481: aload           12
        //   483: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   486: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   489: astore          4
        //   491: aload           11
        //   493: new             Lcom/tremorvideo/sdk/android/richmedia/ad;
        //   496: dup            
        //   497: aload           10
        //   499: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   502: aload           4
        //   504: getstatic       com/tremorvideo/sdk/android/richmedia/ad$a.c:Lcom/tremorvideo/sdk/android/richmedia/ad$a;
        //   507: invokespecial   com/tremorvideo/sdk/android/richmedia/ad.<init>:(Ljava/lang/String;Ljava/lang/String;Lcom/tremorvideo/sdk/android/richmedia/ad$a;)V
        //   510: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   513: pop            
        //   514: iload_3        
        //   515: iconst_1       
        //   516: iadd           
        //   517: istore_3       
        //   518: goto            356
        //   521: aload           10
        //   523: ldc_w           " "
        //   526: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   529: pop            
        //   530: goto            435
        //   533: astore          4
        //   535: aload           4
        //   537: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Ljava/lang/Throwable;)V
        //   540: aload_1        
        //   541: astore          4
        //   543: iload_2        
        //   544: iconst_1       
        //   545: iadd           
        //   546: istore_2       
        //   547: aload           4
        //   549: astore_1       
        //   550: goto            86
        //   553: iload_2        
        //   554: aload           9
        //   556: invokevirtual   org/json/JSONArray.length:()I
        //   559: iconst_1       
        //   560: isub           
        //   561: if_icmpge       583
        //   564: aload           11
        //   566: new             Lcom/tremorvideo/sdk/android/richmedia/ad;
        //   569: dup            
        //   570: ldc_w           "  |  "
        //   573: getstatic       com/tremorvideo/sdk/android/richmedia/ad$a.a:Lcom/tremorvideo/sdk/android/richmedia/ad$a;
        //   576: invokespecial   com/tremorvideo/sdk/android/richmedia/ad.<init>:(Ljava/lang/String;Lcom/tremorvideo/sdk/android/richmedia/ad$a;)V
        //   579: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   582: pop            
        //   583: aload           11
        //   585: invokevirtual   java/util/ArrayList.iterator:()Ljava/util/Iterator;
        //   588: astore          5
        //   590: aload_1        
        //   591: astore          4
        //   593: aload           5
        //   595: invokeinterface java/util/Iterator.hasNext:()Z
        //   600: ifeq            543
        //   603: aload           6
        //   605: aload           5
        //   607: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   612: checkcast       Lcom/tremorvideo/sdk/android/richmedia/ad;
        //   615: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   618: pop            
        //   619: goto            590
        //   622: aload_0        
        //   623: aload           6
        //   625: invokevirtual   java/util/ArrayList.size:()I
        //   628: anewarray       Lcom/tremorvideo/sdk/android/richmedia/ad;
        //   631: putfield        com/tremorvideo/sdk/android/richmedia/m.a:[Lcom/tremorvideo/sdk/android/richmedia/ad;
        //   634: iconst_0       
        //   635: istore_2       
        //   636: iload_2        
        //   637: aload_0        
        //   638: getfield        com/tremorvideo/sdk/android/richmedia/m.a:[Lcom/tremorvideo/sdk/android/richmedia/ad;
        //   641: arraylength    
        //   642: if_icmpge       667
        //   645: aload_0        
        //   646: getfield        com/tremorvideo/sdk/android/richmedia/m.a:[Lcom/tremorvideo/sdk/android/richmedia/ad;
        //   649: iload_2        
        //   650: aload           6
        //   652: iload_2        
        //   653: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //   656: checkcast       Lcom/tremorvideo/sdk/android/richmedia/ad;
        //   659: aastore        
        //   660: iload_2        
        //   661: iconst_1       
        //   662: iadd           
        //   663: istore_2       
        //   664: goto            636
        //   667: aload_0        
        //   668: getfield        com/tremorvideo/sdk/android/richmedia/m.d:Lcom/tremorvideo/sdk/android/richmedia/o;
        //   671: invokevirtual   com/tremorvideo/sdk/android/richmedia/o.g:()Lcom/tremorvideo/sdk/android/richmedia/a;
        //   674: aload_0        
        //   675: getfield        com/tremorvideo/sdk/android/richmedia/m.a:[Lcom/tremorvideo/sdk/android/richmedia/ad;
        //   678: invokevirtual   com/tremorvideo/sdk/android/richmedia/a.a:([Lcom/tremorvideo/sdk/android/richmedia/ad;)V
        //   681: return         
        //   682: astore_1       
        //   683: aload_1        
        //   684: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Ljava/lang/Throwable;)V
        //   687: return         
        //   688: astore          5
        //   690: aload           4
        //   692: astore_1       
        //   693: aload           5
        //   695: astore          4
        //   697: goto            535
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  25     82     682    688    Ljava/lang/Exception;
        //  86     100    682    688    Ljava/lang/Exception;
        //  103    111    688    700    Ljava/lang/Exception;
        //  114    120    688    700    Ljava/lang/Exception;
        //  123    137    688    700    Ljava/lang/Exception;
        //  140    149    688    700    Ljava/lang/Exception;
        //  152    173    688    700    Ljava/lang/Exception;
        //  176    186    688    700    Ljava/lang/Exception;
        //  189    213    688    700    Ljava/lang/Exception;
        //  223    243    688    700    Ljava/lang/Exception;
        //  246    287    688    700    Ljava/lang/Exception;
        //  290    309    688    700    Ljava/lang/Exception;
        //  312    348    688    700    Ljava/lang/Exception;
        //  356    435    533    535    Ljava/lang/Exception;
        //  438    491    533    535    Ljava/lang/Exception;
        //  491    514    533    535    Ljava/lang/Exception;
        //  521    530    533    535    Ljava/lang/Exception;
        //  535    540    682    688    Ljava/lang/Exception;
        //  553    583    533    535    Ljava/lang/Exception;
        //  583    590    533    535    Ljava/lang/Exception;
        //  593    619    533    535    Ljava/lang/Exception;
        //  622    634    682    688    Ljava/lang/Exception;
        //  636    660    682    688    Ljava/lang/Exception;
        //  667    681    682    688    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0351:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
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
    
    public RectF b(int n, int n2) {
        int n3 = 2;
        final int n4 = 0;
        final com.tremorvideo.sdk.android.videoad.o.a e = this.e.e();
        final Bitmap d = this.d.g().d();
        final int width = d.getWidth();
        final int height = d.getHeight();
        int n5;
        int n6;
        int n7;
        if (e == com.tremorvideo.sdk.android.videoad.o.a.a) {
            n5 = n / 2 - (width + 4);
            n = -4;
            n6 = n2 / -2 + 2;
            n7 = -10;
            n2 = n;
            n = 14;
        }
        else if (e == com.tremorvideo.sdk.android.videoad.o.a.b) {
            n5 = n / -2 + 4;
            n = -14;
            n6 = n2 / -2 + 2;
            n7 = -10;
            n2 = n;
            n = 4;
        }
        else if (e == com.tremorvideo.sdk.android.videoad.o.a.d) {
            n5 = n / -2 + 4;
            n = -14;
            n6 = n2 / 2 - height + 2;
            n7 = -2;
            n3 = 12;
            n2 = n;
            n = 4;
        }
        else if (e == com.tremorvideo.sdk.android.videoad.o.a.c) {
            n5 = n / 2 - (width + 4);
            n = -4;
            n6 = n2 / 2 - (height + 2);
            n7 = -2;
            n3 = 12;
            n2 = n;
            n = 14;
        }
        else {
            n3 = 0;
            n2 = 0;
            final int n8 = 0;
            n6 = 0;
            n5 = 0;
            n = n4;
            n7 = n8;
        }
        final RectF rectF = new RectF();
        rectF.left = n2 + n5;
        rectF.top = n6 + n7;
        rectF.right = n + (n5 + width);
        rectF.bottom = n3 + (n6 + height);
        rectF.left -= 15.0f;
        rectF.right += 15.0f;
        rectF.top -= 15.0f;
        rectF.bottom += 15.0f;
        return rectF;
    }
    
    public o b() {
        return this.d;
    }
    
    public void b(final long n) {
        this.a(n, false);
    }
    
    public void b(final Canvas canvas) {
        if (this.d != null) {
            canvas.save();
            this.f.a(this.e, canvas, this.d.g().l(), this.d.g().m());
            this.f.g = this.e.c();
            this.f.h = this.e.d();
            this.d.c(this.f, this.c, this.l);
            this.a(canvas);
            if (this.l != null) {
                this.l.a(this.f);
            }
            if (this.m != null) {
                final k c = this.m.c(this.f, this.c);
                final RectF rectF = new RectF();
                rectF.left = c.a - 10.0f;
                rectF.right = c.a + c.f + 10.0f;
                rectF.top = c.b - 10.0f;
                rectF.bottom = c.g + c.b + 10.0f;
                final Paint paint = new Paint();
                paint.setColor(Color.argb(128, 20, 20, 200));
                paint.setStrokeWidth(5.0f);
                paint.setStyle(Paint$Style.STROKE);
                canvas.drawRoundRect(rectF, 5.0f, 5.0f, paint);
                paint.setColor(Color.argb(128, 200, 200, 255));
                paint.setStrokeWidth(1.0f);
                paint.setStyle(Paint$Style.STROKE);
                canvas.drawRoundRect(rectF, 5.0f, 5.0f, paint);
            }
            canvas.restore();
        }
    }
    
    public void b(final com.tremorvideo.sdk.android.richmedia.h.a a) {
        if (a != null) {
            this.e.a(a);
        }
    }
    
    public void b(final String s) {
        this.e.a(s);
    }
    
    public void c() {
        final Iterator<q> iterator = this.d.e().iterator();
        while (iterator.hasNext()) {
            final com.tremorvideo.sdk.android.richmedia.h.a a = iterator.next().i().a(com.tremorvideo.sdk.android.richmedia.h.c.d);
            if (a != null && a.c() == com.tremorvideo.sdk.android.richmedia.h.b.u) {
                this.e.a(a);
            }
        }
    }
    
    public void c(final int n, final int n2) {
        final boolean b = false;
        final com.tremorvideo.sdk.android.videoad.o.a e = this.e.e();
        boolean b2 = b;
        if (e != com.tremorvideo.sdk.android.videoad.o.a.e) {
            b2 = b;
            if (e != com.tremorvideo.sdk.android.videoad.o.a.f) {
                b2 = b;
                if (this.b((int)this.f.d(), (int)this.f.e()).contains((float)n, (float)n2)) {
                    this.e.a("adchoices");
                    b2 = true;
                }
            }
        }
        if (!b2) {
            final com.tremorvideo.sdk.android.richmedia.h.a a = this.d.a(this.f, n, n2, this.c);
            if (a != null) {
                this.e.a(a);
            }
            if (this.l != null && this.l.c(this.f, n, n2)) {
                this.l.a(this.f, n, n2, this.c);
                this.l.a = true;
            }
        }
    }
    
    public void c(final Canvas canvas) {
        if (this.d != null) {
            canvas.save();
            this.f.a(this.e, canvas, this.d.g().l(), this.d.g().m());
            this.f.g = this.e.c();
            this.f.h = this.e.d();
            this.d.b(this.f, this.c, this.l);
            canvas.restore();
        }
    }
    
    public void d() {
        if (this.m == null) {
            return;
        }
        final com.tremorvideo.sdk.android.richmedia.h.a m = this.m.m();
        if (m != null) {
            this.e.a(m);
            return;
        }
        this.e.a(this.m.a(true));
    }
    
    public void d(final int n, final int n2) {
        if (this.l != null && this.l.a) {
            this.l.a(this.f, n, n2);
            final com.tremorvideo.sdk.android.richmedia.h.a b = this.d.b(this.f, n, n2, this.c);
            if (b != null) {
                this.e.a(b);
            }
        }
    }
    
    public void e() {
        if (this.m == null) {
            this.m = this.p();
        }
        else {
            final k c = this.m.c(this.f, this.c);
            q m = null;
            float n = Float.MAX_VALUE;
        Label_0167_Outer:
            for (final q q : this.d.e()) {
                if (q.k() && q != this.m) {
                    final k c2 = q.c(this.f, 0L);
                    if (c2.f != 0.0f && c2.g != 0.0f) {
                        if (c2.a >= c.a || !this.b(c2, c)) {
                            continue Label_0167_Outer;
                        }
                        final float a = this.a(c, c2);
                        if (m == null) {
                            m = q;
                            n = a;
                        }
                        else {
                            if (a >= n) {
                                continue Label_0167_Outer;
                            }
                            m = q;
                            n = a;
                        }
                        while (true) {
                            continue Label_0167_Outer;
                            continue;
                        }
                    }
                    continue;
                }
            }
            if (m != null) {
                this.m = m;
            }
        }
    }
    
    public void e(final int n, final int n2) {
        final com.tremorvideo.sdk.android.richmedia.h.a d = this.d.d(this.f, n, n2, this.c);
        if (d != null) {
            this.e.a(d);
        }
        if (this.l != null && this.l.a) {
            this.l.b(this.f, n, n2);
            this.l.a = false;
            if (d == null) {
                final com.tremorvideo.sdk.android.richmedia.h.a c = this.d.c(this.f, n, n2, this.c);
                if (c != null) {
                    this.e.a(c);
                }
            }
        }
    }
    
    public void f() {
        if (this.m == null) {
            this.m = this.p();
        }
        else {
            final k c = this.m.c(this.f, this.c);
            q m = null;
            float n = Float.MAX_VALUE;
        Label_0167_Outer:
            for (final q q : this.d.e()) {
                if (q.k() && q != this.m) {
                    final k c2 = q.c(this.f, 0L);
                    if (c2.f != 0.0f && c2.g != 0.0f) {
                        if (c2.a <= c.a || !this.b(c2, c)) {
                            continue Label_0167_Outer;
                        }
                        final float a = this.a(c, c2);
                        if (m == null) {
                            m = q;
                            n = a;
                        }
                        else {
                            if (a >= n) {
                                continue Label_0167_Outer;
                            }
                            m = q;
                            n = a;
                        }
                        while (true) {
                            continue Label_0167_Outer;
                            continue;
                        }
                    }
                    continue;
                }
            }
            if (m != null) {
                this.m = m;
            }
        }
    }
    
    public void g() {
        if (this.m == null) {
            this.m = this.p();
        }
        else {
            final k c = this.m.c(this.f, this.c);
            q m = null;
            float n = Float.MAX_VALUE;
        Label_0167_Outer:
            for (final q q : this.d.e()) {
                if (q.k() && q != this.m) {
                    final k c2 = q.c(this.f, 0L);
                    if (c2.f != 0.0f && c2.g != 0.0f) {
                        if (c2.b >= c.b || this.b(c2, c)) {
                            continue Label_0167_Outer;
                        }
                        final float a = this.a(c, c2);
                        if (m == null) {
                            m = q;
                            n = a;
                        }
                        else {
                            if (a >= n) {
                                continue Label_0167_Outer;
                            }
                            m = q;
                            n = a;
                        }
                        while (true) {
                            continue Label_0167_Outer;
                            continue;
                        }
                    }
                    continue;
                }
            }
            if (m != null) {
                this.m = m;
            }
        }
    }
    
    public void h() {
        if (this.m == null) {
            this.m = this.p();
        }
        else {
            final k c = this.m.c(this.f, this.c);
            q m = null;
            float n = Float.MAX_VALUE;
        Label_0167_Outer:
            for (final q q : this.d.e()) {
                if (q.k() && q != this.m) {
                    final k c2 = q.c(this.f, 0L);
                    if (c2.f != 0.0f && c2.g != 0.0f) {
                        if (c2.b <= c.b || this.b(c2, c)) {
                            continue Label_0167_Outer;
                        }
                        final float a = this.a(c, c2);
                        if (m == null) {
                            m = q;
                            n = a;
                        }
                        else {
                            if (a >= n) {
                                continue Label_0167_Outer;
                            }
                            m = q;
                            n = a;
                        }
                        while (true) {
                            continue Label_0167_Outer;
                            continue;
                        }
                    }
                    continue;
                }
            }
            if (m != null) {
                this.m = m;
            }
        }
    }
    
    public void i() {
        if (this.d != null && this.g == com.tremorvideo.sdk.android.richmedia.o.b.a && !this.b) {
            for (int i = 0; i < this.d.i().size(); ++i) {
                final com.tremorvideo.sdk.android.richmedia.h.a a = this.d.i().get(i);
                if (a.b() == com.tremorvideo.sdk.android.richmedia.h.c.p) {
                    final int intValue = (int)a.e();
                    if (intValue == this.c) {
                        this.b = true;
                        this.c = intValue;
                    }
                }
            }
        }
    }
    
    public long j() {
        return this.c;
    }
    
    public void k() {
        try {
            if (this.l != null) {
                this.l.a();
            }
            if (this.d != null) {
                final Iterator<q> iterator = this.d.e().iterator();
                while (iterator.hasNext()) {
                    iterator.next().a();
                }
            }
        }
        catch (Exception ex) {
            ac.e("Exception Player onRotate=" + ex);
        }
    }
    
    public void l() {
        final com.tremorvideo.sdk.android.richmedia.h.a a = this.d.a(com.tremorvideo.sdk.android.richmedia.h.c.i);
        if (a != null) {
            this.e.a(a);
        }
    }
    
    public boolean m() {
        return this.k.scriptLoaded;
    }
    
    public void n() {
        if (this.j != null) {
            this.j.i();
        }
    }
    
    public boolean o() {
        return this.f != null && this.f.k;
    }
    
    public interface a
    {
        int a();
        
        void a(final int p0, final int p1);
        
        void a(final com.tremorvideo.sdk.android.richmedia.h.a p0);
        
        void a(final String p0);
        
        int b();
        
        boolean c();
        
        boolean d();
        
        com.tremorvideo.sdk.android.videoad.o.a e();
    }
}
