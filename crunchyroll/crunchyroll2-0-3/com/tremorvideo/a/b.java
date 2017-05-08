// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.a;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import android.content.ActivityNotFoundException;
import android.content.pm.Signature;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager$NameNotFoundException;
import android.content.Intent;
import android.util.Log;
import android.content.Context;
import android.webkit.CookieSyncManager;
import android.text.TextUtils;
import android.os.Bundle;
import android.app.Activity;

public class b
{
    protected static String a;
    protected static String b;
    protected static String c;
    private String d;
    private long e;
    private String f;
    private Activity g;
    private String[] h;
    private int i;
    private a j;
    
    static {
        com.tremorvideo.a.b.a = "https://m.facebook.com/dialog/";
        com.tremorvideo.a.b.b = "https://graph.facebook.com/";
        com.tremorvideo.a.b.c = "https://api.facebook.com/restserver.php";
    }
    
    public b(final String f) {
        this.d = null;
        this.e = 0L;
        if (f == null) {
            throw new IllegalArgumentException("You must specify your application ID when instantiating a Facebook object. See README for details.");
        }
        this.f = f;
    }
    
    private void a(final Activity activity, final String[] array) {
        final Bundle bundle = new Bundle();
        if (array.length > 0) {
            bundle.putString("scope", TextUtils.join((CharSequence)",", (Object[])array));
        }
        CookieSyncManager.createInstance((Context)activity);
        this.a((Context)activity, "oauth", bundle, (a)new a() {
            @Override
            public void a() {
                Log.d("Facebook-authorize", "Login canceled");
                com.tremorvideo.a.b.this.j.a();
            }
            
            @Override
            public void a(final Bundle bundle) {
                CookieSyncManager.getInstance().sync();
                com.tremorvideo.a.b.this.a(bundle.getString("access_token"));
                com.tremorvideo.a.b.this.b(bundle.getString("expires_in"));
                if (com.tremorvideo.a.b.this.a()) {
                    Log.d("Facebook-authorize", "Login Success! access_token=" + com.tremorvideo.a.b.this.b() + " expires=" + com.tremorvideo.a.b.this.c());
                    com.tremorvideo.a.b.this.j.a(bundle);
                    return;
                }
                com.tremorvideo.a.b.this.j.a(new c("Failed to receive access token."));
            }
            
            @Override
            public void a(final com.tremorvideo.a.a a) {
                Log.d("Facebook-authorize", "Login failed: " + a);
                com.tremorvideo.a.b.this.j.a(a);
            }
            
            @Override
            public void a(final c c) {
                Log.d("Facebook-authorize", "Login failed: " + c);
                com.tremorvideo.a.b.this.j.a(c);
            }
        });
    }
    
    private boolean a(final Activity activity, final Intent intent) {
        final ResolveInfo resolveActivity = activity.getPackageManager().resolveActivity(intent, 0);
        if (resolveActivity != null) {
            final String packageName = resolveActivity.activityInfo.packageName;
            try {
                final Signature[] signatures = activity.getPackageManager().getPackageInfo(packageName, 64).signatures;
                for (int length = signatures.length, i = 0; i < length; ++i) {
                    if (signatures[i].toCharsString().equals("30820268308201d102044a9c4610300d06092a864886f70d0101040500307a310b3009060355040613025553310b3009060355040813024341311230100603550407130950616c6f20416c746f31183016060355040a130f46616365626f6f6b204d6f62696c653111300f060355040b130846616365626f6f6b311d301b0603550403131446616365626f6f6b20436f72706f726174696f6e3020170d3039303833313231353231365a180f32303530303932353231353231365a307a310b3009060355040613025553310b3009060355040813024341311230100603550407130950616c6f20416c746f31183016060355040a130f46616365626f6f6b204d6f62696c653111300f060355040b130846616365626f6f6b311d301b0603550403131446616365626f6f6b20436f72706f726174696f6e30819f300d06092a864886f70d010101050003818d0030818902818100c207d51df8eb8c97d93ba0c8c1002c928fab00dc1b42fca5e66e99cc3023ed2d214d822bc59e8e35ddcf5f44c7ae8ade50d7e0c434f500e6c131f4a2834f987fc46406115de2018ebbb0d5a3c261bd97581ccfef76afc7135a6d59e8855ecd7eacc8f8737e794c60a761c536b72b11fac8e603f5da1a2d54aa103b8a13c0dbc10203010001300d06092a864886f70d0101040500038181005ee9be8bcbb250648d3b741290a82a1c9dc2e76a0af2f2228f1d9f9c4007529c446a70175c5a900d5141812866db46be6559e2141616483998211f4a673149fb2232a10d247663b26a9031e15f84bc1c74d141ff98a02d76f85b2c8ab2571b6469b232d8e768a7f7ca04f7abe4a775615916c07940656b58717457b42bd928a2")) {
                        return true;
                    }
                }
                return false;
            }
            catch (PackageManager$NameNotFoundException ex) {
                return false;
            }
        }
        return false;
    }
    
    private boolean a(final Activity g, final String s, final String[] h, final int i) {
        boolean b = true;
        final Intent intent = new Intent();
        intent.setClassName("com.facebook.katana", "com.facebook.katana.ProxyAuth");
        intent.putExtra("client_id", s);
        if (h.length > 0) {
            intent.putExtra("scope", TextUtils.join((CharSequence)",", (Object[])h));
        }
        if (!this.a(g, intent)) {
            return false;
        }
        this.g = g;
        this.h = h;
        this.i = i;
        try {
            g.startActivityForResult(intent, i);
            return b;
        }
        catch (ActivityNotFoundException ex) {
            b = false;
            return b;
        }
    }
    
    public String a(final Context context) throws MalformedURLException, IOException {
        com.tremorvideo.a.g.a(context);
        final Bundle bundle = new Bundle();
        bundle.putString("method", "auth.expireSession");
        final String a = this.a(bundle);
        this.a((String)null);
        this.a(0L);
        return a;
    }
    
    public String a(final Bundle bundle) throws MalformedURLException, IOException {
        if (!bundle.containsKey("method")) {
            throw new IllegalArgumentException("API method must be specified. (parameters must contain key \"method\" and value). See http://developers.facebook.com/docs/reference/rest/");
        }
        return this.a(null, bundle, "GET");
    }
    
    public String a(String s, final Bundle bundle, final String s2) throws FileNotFoundException, MalformedURLException, IOException {
        bundle.putString("format", "json");
        if (this.a()) {
            bundle.putString("access_token", this.b());
        }
        if (s != null) {
            s = com.tremorvideo.a.b.b + s;
        }
        else {
            s = com.tremorvideo.a.b.c;
        }
        return com.tremorvideo.a.g.a(s, s2, bundle);
    }
    
    public void a(final long e) {
        this.e = e;
    }
    
    public void a(final Activity activity, final String[] array, final int n, final a j) {
        boolean a = false;
        this.j = j;
        if (n >= 0) {
            a = this.a(activity, this.f, array, n);
        }
        if (!a) {
            this.a(activity, array);
        }
    }
    
    public void a(final Context context, String string, final Bundle bundle, final a a) {
        final String string2 = com.tremorvideo.a.b.a + string;
        bundle.putString("display", "touch");
        bundle.putString("redirect_uri", "fbconnect://success");
        if (string.equals("oauth")) {
            bundle.putString("type", "user_agent");
            bundle.putString("client_id", this.f);
        }
        else {
            bundle.putString("app_id", this.f);
        }
        if (this.a()) {
            bundle.putString("access_token", this.b());
        }
        string = string2 + "?" + com.tremorvideo.a.g.a(bundle);
        if (context.checkCallingOrSelfPermission("android.permission.INTERNET") != 0) {
            com.tremorvideo.a.g.a(context, "Error", "Application requires permission to access the Internet");
            return;
        }
        com.tremorvideo.a.d.a(context, string, a).a();
    }
    
    public void a(final String d) {
        this.d = d;
    }
    
    public boolean a() {
        return this.b() != null && (this.c() == 0L || System.currentTimeMillis() < this.c());
    }
    
    public String b() {
        return this.d;
    }
    
    public void b(final String s) {
        if (s != null && !s.equals("0")) {
            this.a(System.currentTimeMillis() + Integer.parseInt(s) * 1000);
        }
    }
    
    public long c() {
        return this.e;
    }
    
    public interface a
    {
        void a();
        
        void a(final Bundle p0);
        
        void a(final com.tremorvideo.a.a p0);
        
        void a(final c p0);
    }
}
