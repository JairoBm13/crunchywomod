// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.app.AlertDialog;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemClickListener;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.AlertDialog$Builder;
import org.apache.http.NameValuePair;
import java.util.List;
import org.json.JSONObject;
import com.tremorvideo.sdk.android.richmedia.z;
import android.os.Vibrator;
import android.media.AudioManager;
import java.net.URLEncoder;
import android.content.ActivityNotFoundException;
import com.tremorvideo.a.c;
import android.os.Bundle;
import com.tremorvideo.a.b;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import java.util.Dictionary;
import android.content.Intent;
import android.app.DatePickerDialog;
import android.content.DialogInterface$OnCancelListener;
import java.util.Iterator;
import android.widget.DatePicker;
import android.app.DatePickerDialog$OnDateSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface$OnDismissListener;
import android.content.Context;
import android.app.Dialog;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import android.app.Activity;

public class ax
{
    com.tremorvideo.sdk.android.videoad.a.a a;
    Activity b;
    boolean c;
    bw d;
    boolean e;
    int f;
    int g;
    int h;
    String i;
    ArrayList<a> j;
    ArrayList<e> k;
    c l;
    private int m;
    
    public ax(final Activity b, final com.tremorvideo.sdk.android.videoad.a.a a, final bw d) {
        this.m = -1;
        this.c = false;
        this.e = false;
        this.j = new ArrayList<a>();
        this.k = new ArrayList<e>();
        this.d = d;
        this.b = b;
        this.a = a;
        final GregorianCalendar gregorianCalendar = (GregorianCalendar)Calendar.getInstance();
        this.f = gregorianCalendar.get(1);
        this.g = gregorianCalendar.get(2);
        this.h = gregorianCalendar.get(5);
    }
    
    private void a(final aw aw, final b b, final String s) {
        final Dialog a = am.a((Context)this.b, (am.a)new am.a() {
            @Override
            public void a(final Dialog dialog) {
                dialog.dismiss();
            }
            
            @Override
            public void a(final Dialog dialog, final String i) {
                ax.this.i = i;
                ax.this.a(true);
                b.b(i);
                dialog.dismiss();
            }
        }, this.i, "Please enter your email address", this.d, true, 33, 254, new String[] { "Submit", "Cancel" });
        a.setOnDismissListener((DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener() {
            public void onDismiss(final DialogInterface dialogInterface) {
                ax.this.a(true);
            }
        });
        this.i();
        a.setCanceledOnTouchOutside(false);
        a.show();
    }
    
    private void a(final aw aw, final e e, final String s) {
        final Dialog a = am.a((Context)this.b, (am.a)new am.a() {
            @Override
            public void a(final Dialog dialog) {
                dialog.dismiss();
            }
            
            @Override
            public void a(final Dialog dialog, final String i) {
                ax.this.i = i;
                e.f(i);
                if (s != null) {
                    final aw c = ax.this.a.g().c(s);
                    if (c != null) {
                        ax.this.a(c);
                        ax.this.b(c);
                    }
                }
                dialog.dismiss();
            }
        }, this.i, "Please enter a zip code", this.d, true, 2, 5, null);
        a.setOnDismissListener((DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener() {
            public void onDismiss(final DialogInterface dialogInterface) {
                ax.this.a(true);
            }
        });
        this.a(e);
        a.setCanceledOnTouchOutside(false);
        this.i();
        a.show();
    }
    
    private void a(final aw aw, final String s) {
        aw.b b;
        if (aw == null) {
            b = com.tremorvideo.sdk.android.videoad.aw.b.F;
        }
        else {
            b = aw.a();
        }
        final DatePickerDialog a = ag.a((Context)this.b, (DatePickerDialog$OnDateSetListener)new DatePickerDialog$OnDateSetListener() {
            public void onDateSet(final DatePicker datePicker, final int f, final int g, final int h) {
                if (ax.this.f != f || ax.this.g != g || ax.this.h != h) {
                    ax.this.f = f;
                    ax.this.g = g;
                    ax.this.h = h;
                    final Iterator<a> iterator = ax.this.j.iterator();
                    while (iterator.hasNext()) {
                        ((a)iterator.next()).a(b, ax.this.f, ax.this.g, ax.this.h);
                    }
                    if (s != null) {
                        final aw c = ax.this.a.g().c(s);
                        if (c != null) {
                            ax.this.a(c);
                            ax.this.b(c);
                        }
                    }
                    ax.this.a(true);
                }
            }
        }, this.f, this.g, this.h);
        a.setOnCancelListener((DialogInterface$OnCancelListener)new DialogInterface$OnCancelListener() {
            public void onCancel(final DialogInterface dialogInterface) {
                ax.this.a(true);
            }
        });
        a.setOnDismissListener((DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener() {
            public void onDismiss(final DialogInterface dialogInterface) {
                ax.this.a(true);
            }
        });
        a.setCanceledOnTouchOutside(false);
        a.setTitle((CharSequence)"Please select a date.");
        this.i();
        a.show();
    }
    
    private void a(final boolean b) {
        this.c = false;
        if (b) {
            this.b((aw)null);
        }
    }
    
    private void b(final aw aw, final e e, final String s) {
        final Dialog a = am.a((Context)this.b, (am.a)new am.a() {
            @Override
            public void a(final Dialog dialog) {
                dialog.dismiss();
            }
            
            @Override
            public void a(final Dialog dialog, final String i) {
                ax.this.i = i;
                final Iterator<e> iterator = ax.this.k.iterator();
                while (iterator.hasNext()) {
                    ((e)iterator.next()).f(i);
                }
                if (s != null) {
                    final aw c = ax.this.a.g().c(s);
                    if (c != null) {
                        ax.this.a(c);
                        ax.this.b(c);
                    }
                }
                dialog.dismiss();
            }
        }, this.i, "Please enter a zip code", this.d, false, 2, 5, null);
        a.setOnDismissListener((DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener() {
            public void onDismiss(final DialogInterface dialogInterface) {
                ax.this.a(true);
            }
        });
        if (e != null) {
            this.a(e);
        }
        a.setCanceledOnTouchOutside(false);
        this.i();
        a.show();
    }
    
    private void d(final aw aw) throws Exception {
        final Dictionary<String, String> f = aw.f();
        if (f.get("subject") == null && f.get("message") == null) {
            throw new Exception("Error launching share, no data supplied");
        }
        final Intent intent = new Intent("android.intent.action.SEND");
        if (f.get("subject") != null) {
            intent.putExtra("android.intent.extra.SUBJECT", (String)f.get("subject"));
        }
        if (f.get("message") != null) {
            intent.putExtra("android.intent.extra.TEXT", (String)f.get("message"));
        }
        intent.setType("text/plain");
        this.b.startActivityForResult(Intent.createChooser(intent, (CharSequence)"Share"), 11);
    }
    
    private void d(final String s) throws Exception {
        ResolveInfo resolveInfo = null;
        String s2;
        if (s.startsWith("http://www.youtube.com/watch?v=")) {
            s2 = s.replace("http://www.youtube.com/watch?v=", "");
        }
        else if (s.startsWith("http://www.youtube.com/v/")) {
            s2 = s.replace("http://www.youtube.com/v/", "");
        }
        else {
            s2 = null;
        }
        Intent intent = null;
        Label_0042: {
            if (s2 == null) {
                intent = new Intent("android.intent.action.VIEW", Uri.parse(s));
            }
            else {
                final Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse(s));
                final Iterator iterator = this.b.getPackageManager().queryIntentActivities(intent2, 65536).iterator();
                ResolveInfo resolveInfo2 = null;
                while (iterator.hasNext()) {
                    final ResolveInfo resolveInfo3 = iterator.next();
                    ResolveInfo resolveInfo4;
                    ResolveInfo resolveInfo5;
                    if (resolveInfo3.activityInfo.packageName.toLowerCase().contains("browser")) {
                        resolveInfo4 = resolveInfo;
                        resolveInfo5 = resolveInfo3;
                    }
                    else if (resolveInfo3.activityInfo.packageName.toLowerCase().contains("youtube")) {
                        resolveInfo5 = resolveInfo2;
                        resolveInfo4 = resolveInfo3;
                    }
                    else {
                        final ResolveInfo resolveInfo6 = resolveInfo2;
                        resolveInfo4 = resolveInfo;
                        resolveInfo5 = resolveInfo6;
                    }
                    final ResolveInfo resolveInfo7 = resolveInfo5;
                    resolveInfo = resolveInfo4;
                    resolveInfo2 = resolveInfo7;
                }
                if (ac.r() >= 11) {
                    if (resolveInfo != null) {
                        intent2.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
                        intent = intent2;
                        break Label_0042;
                    }
                    if (resolveInfo2 != null) {
                        intent2.setClassName(resolveInfo2.activityInfo.packageName, resolveInfo2.activityInfo.name);
                        intent = intent2;
                        break Label_0042;
                    }
                }
                else {
                    if (resolveInfo2 != null) {
                        final Intent intent3 = new Intent("android.intent.action.VIEW", Uri.parse("http://www.youtube.com/watch?v=" + s2));
                        intent3.setClassName(resolveInfo2.activityInfo.packageName, resolveInfo2.activityInfo.name);
                        intent = intent3;
                        break Label_0042;
                    }
                    if (resolveInfo != null) {
                        intent2.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
                    }
                }
                intent = intent2;
            }
        }
        this.b.startActivityForResult(intent, 11);
        bx.c();
        this.e = true;
    }
    
    private void e(final aw aw) {
        final com.tremorvideo.a.b.a a = new com.tremorvideo.a.b.a() {
            @Override
            public void a() {
                ax.this.a(true);
            }
            
            @Override
            public void a(final Bundle bundle) {
                ax.this.b.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        final aa aa = new aa((Context)ax.this.b, ax.this.d, com.tremorvideo.sdk.android.videoad.aa.a.a, (aa.b)new aa.b() {
                            @Override
                            public void a(final boolean b) {
                                ax.this.a(true);
                            }
                        });
                        aa.setCanceledOnTouchOutside(false);
                        aa.setTitle("Facebook");
                        aa.a("Posted to Facebook successfully.");
                        aa.a("Ok", "");
                        aa.show();
                    }
                });
            }
            
            @Override
            public void a(final com.tremorvideo.a.a a) {
                ax.this.b.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        final aa aa = new aa((Context)ax.this.b, ax.this.d, com.tremorvideo.sdk.android.videoad.aa.a.a, (aa.b)new aa.b() {
                            @Override
                            public void a(final boolean b) {
                                ax.this.a(true);
                            }
                        });
                        aa.setCanceledOnTouchOutside(false);
                        aa.setTitle("Facebook");
                        aa.a("There was an error posting to your Facebook page.");
                        aa.a("Ok", "");
                        aa.show();
                    }
                });
            }
            
            @Override
            public void a(final com.tremorvideo.a.c c) {
                ax.this.b.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        final aa aa = new aa((Context)ax.this.b, ax.this.d, com.tremorvideo.sdk.android.videoad.aa.a.a, (aa.b)new aa.b() {
                            @Override
                            public void a(final boolean b) {
                                ax.this.a(true);
                            }
                        });
                        aa.setCanceledOnTouchOutside(false);
                        aa.setTitle("Facebook");
                        aa.a("There was an error posting to your Facebook page.");
                        aa.a("Ok", "");
                        aa.show();
                    }
                });
            }
        };
        this.i();
        final aa aa = new aa((Context)this.b, this.d, com.tremorvideo.sdk.android.videoad.aa.a.b, (aa.b)new aa.b() {
            @Override
            public void a(final boolean b) {
                if (b) {
                    ax.this.a(true);
                    return;
                }
                ba.a(ax.this.b, aw, a);
            }
        });
        aa.setCanceledOnTouchOutside(false);
        aa.setTitle("Facebook");
        aa.a("Would you like to share this on your Facebook page?");
        aa.a("Yes", "No");
        aa.show();
    }
    
    private void e(final String s) throws Exception {
        this.b.startActivityForResult(new Intent("android.intent.action.DIAL", Uri.parse(s)), 11);
        this.e = true;
        Thread.sleep(500L);
    }
    
    private void f(final aw aw) {
        final DatePickerDialog a = ag.a((Context)this.b, (DatePickerDialog$OnDateSetListener)new DatePickerDialog$OnDateSetListener() {
            public void onDateSet(final DatePicker datePicker, final int f, final int g, final int h) {
                ax.this.f = f;
                ax.this.g = g;
                ax.this.h = h;
                final Iterator<a> iterator = ax.this.j.iterator();
                while (iterator.hasNext()) {
                    ((a)iterator.next()).a(aw.a(), ax.this.f, ax.this.g, ax.this.h);
                }
                ax.this.a(true);
            }
        }, this.f, this.g, this.h);
        a.setOnCancelListener((DialogInterface$OnCancelListener)new DialogInterface$OnCancelListener() {
            public void onCancel(final DialogInterface dialogInterface) {
                ax.this.a(true);
            }
        });
        a.setOnDismissListener((DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener() {
            public void onDismiss(final DialogInterface dialogInterface) {
                ax.this.a(true);
            }
        });
        a.setTitle((CharSequence)"Please enter your birthday.");
        this.i();
        a.show();
    }
    
    private void f(final String s) throws ActivityNotFoundException {
        this.e = true;
        this.b.startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse(s)), 11);
        bx.c();
    }
    
    private void g() throws Exception {
        com.tremorvideo.sdk.android.a.b.a = this.a.g();
        com.tremorvideo.sdk.android.a.b.b = this.a;
        try {
            final Intent intent = new Intent((Context)this.b, (Class)Playvideo.class);
            intent.putExtra("tremorVideoType", "buyitnow");
            this.b.startActivityForResult(intent, 3232);
        }
        catch (Exception ex) {
            com.tremorvideo.sdk.android.a.b.a = null;
            com.tremorvideo.sdk.android.a.b.b = null;
            throw ex;
        }
    }
    
    private boolean g(final aw aw) throws Exception {
        final String j = aw.j();
        Label_0053: {
            if (!j.startsWith("market://") && !j.startsWith("https://play.google.com")) {
                if (!j.startsWith("http://play.google.com")) {
                    break Label_0053;
                }
            }
            try {
                this.f(j);
                bx.c();
                return true;
            }
            catch (Exception ex) {
                ac.a(ex);
                return true;
            }
        }
        final Intent intent = new Intent((Context)this.b, (Class)Playvideo.class);
        intent.putExtra("tremorVideoType", "webview");
        intent.putExtra("tremorVideoURL", j);
        intent.putExtra("curEventID", this.m);
        this.b.startActivityForResult(intent, 3232);
        return false;
    }
    
    private void h() throws Exception {
        com.tremorvideo.sdk.android.b.b.a = this.a.g();
        com.tremorvideo.sdk.android.b.b.b = this.a;
        try {
            final Intent intent = new Intent((Context)this.b, (Class)Playvideo.class);
            intent.putExtra("tremorVideoType", "movieboard");
            this.b.startActivityForResult(intent, 3232);
        }
        catch (Exception ex) {
            com.tremorvideo.sdk.android.a.b.a = null;
            com.tremorvideo.sdk.android.a.b.b = null;
            throw ex;
        }
    }
    
    private void h(final aw aw) throws Exception {
        com.tremorvideo.sdk.android.videoad.k.a = this.a.g();
        com.tremorvideo.sdk.android.videoad.k.b = this.a;
        try {
            final Intent intent = new Intent((Context)this.b, (Class)Playvideo.class);
            intent.putExtra("tremorVideoType", "coupon");
            intent.putExtra("curEventID", this.m);
            this.b.startActivityForResult(intent, 3232);
        }
        catch (Exception ex) {
            com.tremorvideo.sdk.android.videoad.k.a = null;
            com.tremorvideo.sdk.android.videoad.k.b = null;
            throw ex;
        }
    }
    
    private void i() {
        this.c = true;
    }
    
    private void i(final aw aw) throws Exception {
        Object f;
        int int1;
        double n = 0.0;
        double n2 = 0.0;
        int int2 = 0;
        String s = null;
        String s2;
        int n3;
        String s3 = null;
        ArrayList<String> list;
        int n4;
        String s4;
        double n5;
        double b;
        double n6;
        double double1;
        double n7;
        double n8;
        String s5;
        String s6;
        StringBuilder sb;
        r.b r;
        String string;
        Intent intent;
        Uri parse;
        Label_0088_Outer:Label_0111_Outer:Label_0140_Outer:Label_0342_Outer:
        while (true) {
            f = aw.f();
        Label_1079:
            while (true) {
            Label_1054:
                while (true) {
                Label_1001:
                    while (true) {
                    Label_0965:
                        while (true) {
                            while (true) {
                                try {
                                    bp.a(ac.x(), true);
                                    int1 = Integer.parseInt(((Dictionary<K, String>)f).get((Object)"map-type"));
                                    n = 0.0;
                                    n2 = 0.0;
                                    int2 = -1;
                                    s = ((Dictionary<K, String>)f).get("zoom");
                                    if (s != null) {
                                        int2 = Integer.parseInt(s);
                                    }
                                    if (int1 == 1) {
                                        f = ((Dictionary<K, String>)f).get("query");
                                        n = 0.0;
                                        s2 = null;
                                        n3 = 1;
                                        if (n3 == 0) {
                                            break;
                                        }
                                        if (n == 0.0 && n2 == 0.0) {
                                            s3 = "geo:0,0";
                                            list = new ArrayList<String>();
                                            if (int2 >= 0) {
                                                if (!"0".equalsIgnoreCase(s)) {
                                                    break Label_1001;
                                                }
                                                n4 = 16;
                                                list.add("z=" + n4);
                                            }
                                            if (f != null && f != "") {
                                                list.add("q=" + URLEncoder.encode((String)f, "UTF-8"));
                                            }
                                            if (s2 != null && s2.length() > 0) {
                                                f = new StringBuilder("q=");
                                                ((StringBuilder)f).append(n);
                                                ((StringBuilder)f).append(",");
                                                ((StringBuilder)f).append(n2);
                                                ((StringBuilder)f).append("(");
                                                ((StringBuilder)f).append(URLEncoder.encode(s2, "UTF-8"));
                                                ((StringBuilder)f).append(")");
                                                list.add(((StringBuilder)f).toString());
                                            }
                                            for (int i = 0; i < list.size(); ++i) {
                                                if (i != 0) {
                                                    break Label_1054;
                                                }
                                                s4 = s3 + "?";
                                                s3 = s4 + list.get(i);
                                            }
                                            break Label_1079;
                                        }
                                        break Label_0965;
                                    }
                                }
                                catch (Exception ex) {
                                    ac.e("EventHandler: Unable to get location: " + ex.toString());
                                    continue Label_0088_Outer;
                                }
                                break;
                            }
                            if (int1 == 2) {
                                n5 = bp.a;
                                b = bp.b;
                                n6 = n5;
                                while (true) {
                                    Label_1165: {
                                        if (n5 != 0.0) {
                                            break Label_1165;
                                        }
                                        n6 = n5;
                                        if (b != 0.0) {
                                            break Label_1165;
                                        }
                                        if (((Dictionary<K, String>)f).get("latitude") != null) {
                                            n5 = Double.parseDouble(((Dictionary<K, String>)f).get((Object)"latitude"));
                                        }
                                        n6 = n5;
                                        if (((Dictionary<K, String>)f).get("longitude") == null) {
                                            break Label_1165;
                                        }
                                        double1 = Double.parseDouble(((Dictionary<K, String>)f).get((Object)"longitude"));
                                        n6 = n5;
                                        n7 = double1;
                                        f = ((Dictionary<K, String>)f).get("query");
                                        n8 = n6;
                                        s2 = null;
                                        n3 = 1;
                                        n2 = n7;
                                        n = n8;
                                        continue Label_0111_Outer;
                                    }
                                    n7 = b;
                                    continue;
                                }
                            }
                            if (int1 == 3) {
                                if (((Dictionary<K, String>)f).get("latitude") != null) {
                                    n = Double.parseDouble(((Dictionary<K, String>)f).get((Object)"latitude"));
                                }
                                if (((Dictionary<K, String>)f).get("longitude") != null) {
                                    n2 = Double.parseDouble(((Dictionary<K, String>)f).get((Object)"longitude"));
                                }
                                s2 = ((Dictionary<K, String>)f).get("pinpoint-name");
                                n3 = 1;
                                f = null;
                                continue Label_0111_Outer;
                            }
                            if (int1 == 4 || int1 == 5) {
                                n = bp.a;
                                n2 = bp.b;
                                s5 = ((Dictionary<K, String>)f).get("retailigence-endpoint");
                                s6 = ((Dictionary<K, String>)f).get("html-template-path");
                                sb = new StringBuilder("file://" + ((r)this.a.g()).G() + "/comps/retailigence_map/index.html");
                                sb.append("?api=");
                                sb.append(URLEncoder.encode(s5, "UTF-8"));
                                sb.append("&type=");
                                sb.append(int1);
                                if (int2 >= 0) {
                                    sb.append("&zoom=");
                                    sb.append(int2);
                                }
                                if (n == 0.0 && n2 == 0.0) {
                                    r = this.a.g().r();
                                    if (r != null) {
                                        sb.append("&zipcode=");
                                        sb.append(r.d);
                                    }
                                }
                                else {
                                    sb.append("&lat=");
                                    sb.append(n);
                                    sb.append("&lng=");
                                    sb.append(n2);
                                }
                                string = sb.toString();
                                ac.e("WebView URL=" + string);
                                intent = new Intent((Context)this.b, (Class)Playvideo.class);
                                intent.putExtra("tremorVideoType", "webview");
                                intent.putExtra("tremorVideoURL", string);
                                intent.putExtra("curEventID", this.m);
                                this.b.startActivityForResult(intent, 3232);
                                s2 = null;
                                n3 = 0;
                                f = null;
                                continue Label_0111_Outer;
                            }
                            n3 = 1;
                            n = 0.0;
                            s2 = null;
                            f = null;
                            continue Label_0111_Outer;
                        }
                        s3 = "geo:" + n + "," + n2;
                        continue Label_0140_Outer;
                    }
                    if ("1".equalsIgnoreCase(s)) {
                        n4 = 12;
                        continue Label_0342_Outer;
                    }
                    if ("2".equalsIgnoreCase(s)) {
                        n4 = 6;
                        continue Label_0342_Outer;
                    }
                    if ("3".equalsIgnoreCase(s)) {
                        n4 = 4;
                        continue Label_0342_Outer;
                    }
                    n4 = int2;
                    continue Label_0342_Outer;
                }
                s4 = s3 + "&";
                continue;
            }
            this.e = true;
            parse = Uri.parse(s3);
            ac.a(ac.c.b, "Map URL: " + parse.toString());
            this.b.startActivityForResult(new Intent("android.intent.action.VIEW", parse), 11);
            bx.c();
            break;
        }
    }
    
    private void j(final aw aw) throws Exception {
        this.i();
        ca.a(this.b, aw, (ca.e)new ca.e() {
            @Override
            public void a(final int n, final String s) {
                if (n == -1) {
                    ax.this.a(true);
                    return;
                }
                final aa aa = new aa((Context)ax.this.b, ax.this.d, com.tremorvideo.sdk.android.videoad.aa.a.a, (aa.b)new aa.b() {
                    @Override
                    public void a(final boolean b) {
                        ax.this.a(true);
                    }
                });
                if (n == 0) {
                    aa.setTitle("Error");
                    if (s.compareTo("") == 0) {
                        aa.a("There was an error sending your Tweet.");
                    }
                }
                else {
                    aa.setTitle("Success");
                    if (s.compareTo("") == 0) {
                        aa.a("Your tweet was sent successfully.");
                    }
                }
                if (s.compareTo("") != 0) {
                    aa.a(s);
                }
                aa.setCanceledOnTouchOutside(false);
                aa.a("OK", "");
                aa.show();
            }
        });
    }
    
    private void k(final aw aw) {
        try {
            final AudioManager audioManager = (AudioManager)this.a.h().getSystemService("audio");
            final boolean shouldVibrate = audioManager.shouldVibrate(1);
            final boolean shouldVibrate2 = audioManager.shouldVibrate(0);
            if (shouldVibrate && shouldVibrate2) {
                final int min = Math.min(30, Integer.parseInt(aw.f().get("duration")));
                if (min > 0) {
                    ((Vibrator)this.b.getSystemService("vibrator")).vibrate((long)(min * 1000));
                }
            }
        }
        catch (Exception ex) {
            ac.a(ex);
        }
    }
    
    public void a() {
        this.j = new ArrayList<a>();
        this.k = new ArrayList<e>();
    }
    
    public void a(final aw.b b) {
        this.a(b, null, null, null);
    }
    
    public void a(final aw.b b, final b b2, final String s, final String s2, final String s3) {
        if (!this.c) {
            try {
                if (this.l != null) {
                    this.l.a(null);
                }
                if (b != aw.b.I) {
                    this.m = Integer.MAX_VALUE;
                }
                if (b == aw.b.b) {
                    if (s != null) {
                        final aw c = this.a.g().c(s);
                        if (c != null) {
                            this.a(c);
                            this.b(c);
                        }
                    }
                    final Intent intent = new Intent((Context)this.b, (Class)Playvideo.class);
                    intent.putExtra("tremorVideoType", "webview");
                    intent.putExtra("tremorVideoURL", s3);
                    intent.putExtra("curEventID", -1);
                    this.b.startActivityForResult(intent, 3232);
                    return;
                }
                if (b == aw.b.aw) {
                    this.a(null, b2, s2);
                    return;
                }
            }
            catch (Exception ex) {
                ac.e("Exception doEventAction" + ex);
                this.b((aw)null);
                return;
            }
            if (b != aw.b.ax && b != aw.b.ay && b != aw.b.az) {
                throw new Exception();
            }
            if (s != null) {
                final aw c2 = this.a.g().c(s);
                if (c2 != null) {
                    this.a(c2);
                    this.b(c2);
                }
            }
        }
    }
    
    public void a(final aw.b b, final e e, final ArrayList<z.b> list, final String s) {
        final aw aw = null;
        aw aw2 = null;
        if (!this.c) {
            aw aw3 = aw;
            String d = null;
            Label_0291: {
                try {
                    if (b == com.tremorvideo.sdk.android.videoad.aw.b.I) {
                        aw3 = aw;
                        final aw a = this.a.g().a(b);
                        if ((aw2 = a) != null) {
                            aw3 = a;
                            this.a(a);
                            aw2 = a;
                        }
                    }
                    aw3 = aw2;
                    aw a2 = null;
                    Label_0143: {
                        if (b != com.tremorvideo.sdk.android.videoad.aw.b.J) {
                            a2 = aw2;
                            d = s;
                            aw3 = aw2;
                            if (b != com.tremorvideo.sdk.android.videoad.aw.b.K) {
                                break Label_0143;
                            }
                        }
                        aw3 = aw2;
                        final aw aw4 = a2 = this.a.g().a(b);
                        d = s;
                        if (aw4 != null) {
                            aw3 = aw4;
                            d = aw4.d();
                            a2 = aw4;
                        }
                    }
                    aw3 = a2;
                    if (this.l != null) {
                        aw3 = a2;
                        this.l.a(a2);
                    }
                    aw3 = a2;
                    if (b != com.tremorvideo.sdk.android.videoad.aw.b.I) {
                        aw3 = a2;
                        this.m = Integer.MAX_VALUE;
                    }
                    aw3 = a2;
                    if (b != com.tremorvideo.sdk.android.videoad.aw.b.F) {
                        aw3 = a2;
                        if (b != com.tremorvideo.sdk.android.videoad.aw.b.K) {
                            aw3 = a2;
                            if (b != com.tremorvideo.sdk.android.videoad.aw.b.G) {
                                aw3 = a2;
                                if (b != com.tremorvideo.sdk.android.videoad.aw.b.J) {
                                    break Label_0291;
                                }
                            }
                            aw3 = a2;
                            this.b(null, e, d);
                            return;
                        }
                    }
                    aw3 = a2;
                    this.a(null, d);
                    return;
                }
                catch (Exception ex) {
                    ac.e("Exception doEventAction" + ex);
                    this.b(aw3);
                    return;
                }
            }
            if (b == com.tremorvideo.sdk.android.videoad.aw.b.au) {
                this.a(null, e, d);
                return;
            }
            if (b == com.tremorvideo.sdk.android.videoad.aw.b.at) {
                this.a((d)e, list, d);
                return;
            }
            if (b != com.tremorvideo.sdk.android.videoad.aw.b.I) {
                throw new Exception();
            }
        }
    }
    
    public void a(final aw aw) {
        this.a(aw, -1);
    }
    
    public void a(final aw aw, final int n) {
        this.m = this.a.a(aw, n);
    }
    
    public void a(final aw aw, final int n, String string) {
        final boolean b = false;
        if (aw != null) {
            int n2 = 0;
            Label_0033: {
                if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.x) {
                    this.a(aw, n);
                    this.k(aw);
                    n2 = (b ? 1 : 0);
                }
                else if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.ar) {
                    this.a(aw, n);
                    n2 = (b ? 1 : 0);
                }
                else {
                    n2 = (b ? 1 : 0);
                    if (!this.c) {
                        this.a(aw, n);
                        if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.j) {
                            this.m = -1;
                            n2 = (b ? 1 : 0);
                        }
                        else {
                            while (true) {
                                while (true) {
                                    Label_0839: {
                                        try {
                                            if (this.l != null) {
                                                this.l.a(aw);
                                            }
                                            if (aw.a() != com.tremorvideo.sdk.android.videoad.aw.b.p) {
                                                break;
                                            }
                                            this.j(aw);
                                            n2 = (b ? 1 : 0);
                                        }
                                        catch (Exception ex) {
                                            final Throwable cause = ex.getCause();
                                            if (cause == null) {
                                                break Label_0839;
                                            }
                                            ac.e("Unable to launch URL: " + aw.j() + ". " + ex.getMessage() + " " + cause.getMessage());
                                            n2 = 1;
                                        }
                                        break Label_0033;
                                    }
                                    final Exception ex;
                                    ac.e("Unable to launch URL: " + aw.j() + ". " + ex.getMessage());
                                    continue;
                                }
                            }
                            if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.r) {
                                this.d(aw);
                                n2 = 1;
                            }
                            else if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.q) {
                                this.i(aw);
                                n2 = (b ? 1 : 0);
                            }
                            else if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.u) {
                                this.e(aw);
                                n2 = (b ? 1 : 0);
                            }
                            else if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.B) {
                                this.h(aw);
                                n2 = (b ? 1 : 0);
                            }
                            else if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.b) {
                                n2 = (this.g(aw) ? 1 : 0);
                            }
                            else if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.h) {
                                n2 = (this.g(aw) ? 1 : 0);
                            }
                            else if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.g) {
                                n2 = (this.g(aw) ? 1 : 0);
                            }
                            else if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.e) {
                                n2 = (this.g(aw) ? 1 : 0);
                            }
                            else if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.d) {
                                n2 = (this.g(aw) ? 1 : 0);
                            }
                            else if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.i) {
                                this.d(aw.j());
                                n2 = 1;
                            }
                            else if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.c) {
                                this.e(aw.j());
                                n2 = 1;
                            }
                            else if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.F) {
                                this.a(aw, null);
                                n2 = (b ? 1 : 0);
                            }
                            else if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.H) {
                                this.b(aw.j());
                                n2 = (b ? 1 : 0);
                            }
                            else if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.L) {
                                this.f(aw);
                                n2 = (b ? 1 : 0);
                            }
                            else if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.S) {
                                this.g();
                                n2 = 1;
                            }
                            else if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.X) {
                                this.h();
                                n2 = 1;
                            }
                            else if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.as) {
                                n2 = (b ? 1 : 0);
                                if (string != null) {
                                    final JSONObject jsonObject = new JSONObject(string);
                                    n2 = (b ? 1 : 0);
                                    if (jsonObject.has("component_type")) {
                                        if (jsonObject.getInt("component_type") == 5) {
                                            string = jsonObject.getJSONObject("params").getString("pinEndpoint");
                                            if (string != null && string.length() > 0) {
                                                this.f(string);
                                            }
                                            n2 = 1;
                                        }
                                        else {
                                            com.tremorvideo.sdk.android.richmedia.a.b.a = this.a.g();
                                            com.tremorvideo.sdk.android.richmedia.a.b.b = this.a;
                                            final Intent intent = new Intent((Context)this.b, (Class)Playvideo.class);
                                            intent.putExtra("tremorVideoType", "genericwebview");
                                            intent.putExtra("curEventID", this.m);
                                            if (jsonObject.has("zip_path") && jsonObject.has("index_file")) {
                                                intent.putExtra("path", jsonObject.getString("zip_path") + jsonObject.getString("index_file"));
                                            }
                                            if (jsonObject.has("params")) {
                                                intent.putExtra("params", jsonObject.getString("params"));
                                            }
                                            this.b.startActivityForResult(intent, 3232);
                                            n2 = (b ? 1 : 0);
                                        }
                                    }
                                }
                            }
                            else {
                                n2 = (b ? 1 : 0);
                                if (aw.a() != com.tremorvideo.sdk.android.videoad.aw.b.ar) {
                                    this.f(aw.j());
                                    n2 = 1;
                                }
                            }
                        }
                    }
                }
            }
            if (n2 == 1) {
                this.b(aw);
            }
        }
    }
    
    public void a(final aw aw, final int n, final List<NameValuePair> list) {
        this.m = this.a.a(aw, n, list);
    }
    
    public void a(final a a) {
        this.j.add(a);
    }
    
    public void a(final c l) {
        this.l = l;
    }
    
    public void a(final d d, final ArrayList<z.b> list, final String s) {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this.b);
        alertDialog$Builder.setTitle((CharSequence)"Select Cable Provider");
        final ListView view = new ListView((Context)this.b);
        view.setItemsCanFocus(false);
        view.setAdapter((ListAdapter)new ArrayAdapter((Context)this.b, 17367043, 16908308, (List)list));
        alertDialog$Builder.setView((View)view);
        final AlertDialog create = alertDialog$Builder.create();
        ((Dialog)create).setOnCancelListener((DialogInterface$OnCancelListener)new DialogInterface$OnCancelListener() {
            public void onCancel(final DialogInterface dialogInterface) {
                ax.this.a(true);
            }
        });
        ((Dialog)create).setOnDismissListener((DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener() {
            public void onDismiss(final DialogInterface dialogInterface) {
                ax.this.a(true);
            }
        });
        this.i();
        ((Dialog)create).show();
        view.setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                ac.e("Position setOnItemClickListener" + n);
                d.d(n);
                if (s != null) {
                    final aw c = ax.this.a.g().c(s);
                    if (c != null) {
                        ax.this.a(c);
                        ax.this.b(c);
                    }
                }
                ax.this.a(true);
                ((Dialog)create).dismiss();
            }
        });
    }
    
    public void a(final e e) {
        this.k.add(e);
    }
    
    public void a(final String i) {
        this.i = i;
    }
    
    public void a(final String s, final aw aw) throws Exception {
        if (this.l != null) {
            this.l.a(aw);
        }
        this.i();
        ca.a(this.b, null, (ca.e)new ca.e() {
            @Override
            public void a(final int n, final String s) {
                if (n == -1) {
                    if (ax.this.l != null) {
                        ax.this.l.b(aw);
                    }
                    ax.this.a(true);
                    return;
                }
                final aa aa = new aa((Context)ax.this.b, ax.this.d, com.tremorvideo.sdk.android.videoad.aa.a.a, (aa.b)new aa.b() {
                    @Override
                    public void a(final boolean b) {
                        if (ax.this.l != null) {
                            ax.this.l.b(aw);
                        }
                        ax.this.a(true);
                    }
                });
                if (n == 0) {
                    aa.setTitle("Error");
                    if (s.compareTo("") == 0) {
                        aa.a("There was an error sending your Tweet.");
                    }
                }
                else {
                    aa.setTitle("Success");
                    if (s.compareTo("") == 0) {
                        aa.a("Your tweet was sent successfully.");
                    }
                }
                if (s.compareTo("") != 0) {
                    aa.a(s);
                }
                aa.setCanceledOnTouchOutside(false);
                aa.a("OK", "");
                aa.show();
            }
        }, 1, s);
    }
    
    public void b(final aw aw) {
        if (this.m != -1) {
            if (this.l != null) {
                this.l.b(aw);
            }
            if (this.m != Integer.MAX_VALUE) {
                this.a.a(this.m);
            }
            this.m = -1;
        }
    }
    
    public void b(final String s) {
        com.tremorvideo.sdk.android.d.b.a = this.a.g();
        com.tremorvideo.sdk.android.d.b.b = this.a;
        ac.e("TremorLog_info::AdChoice::Web-URL=" + s);
        try {
            final Intent intent = new Intent((Context)this.b, (Class)Playvideo.class);
            intent.putExtra("tremorVideoType", "adchoices");
            intent.putExtra("tremorVideoURL", s);
            this.b.startActivityForResult(intent, 3232);
        }
        catch (Exception ex) {
            com.tremorvideo.sdk.android.a.b.a = null;
            com.tremorvideo.sdk.android.a.b.b = null;
        }
    }
    
    public boolean b() {
        return this.e;
    }
    
    public int c() {
        return this.m;
    }
    
    public void c(final aw aw) {
        this.a(aw, -1, (String)null);
    }
    
    public void c(final String s) {
        final Intent intent = new Intent((Context)this.b, (Class)Playvideo.class);
        intent.putExtra("tremorVideoType", "webview");
        intent.putExtra("tremorVideoURL", s);
        intent.putExtra("curEventID", this.m);
        this.b.startActivityForResult(intent, 3232);
    }
    
    public boolean d() {
        return this.c;
    }
    
    public void e() {
        this.e = false;
    }
    
    public boolean f() {
        return this.c;
    }
    
    public interface a
    {
        void a(final aw.b p0, final int p1, final int p2, final int p3);
    }
    
    public interface b
    {
        void b(final String p0);
    }
    
    public interface c
    {
        void a(final aw p0);
        
        void b(final aw p0);
    }
    
    public interface d
    {
        void d(final int p0);
    }
    
    public interface e
    {
        void f(final String p0);
    }
}
