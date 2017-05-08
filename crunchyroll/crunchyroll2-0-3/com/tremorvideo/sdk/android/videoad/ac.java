// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.HttpGet;
import java.net.URLEncoder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.HttpVersion;
import org.apache.http.params.BasicHttpParams;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.content.pm.PackageManager$NameNotFoundException;
import android.content.SharedPreferences;
import java.util.UUID;
import android.provider.Settings$Secure;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.app.ActivityManager$RunningAppProcessInfo;
import android.app.ActivityManager;
import org.json.JSONException;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.ValueCallback;
import android.webkit.WebViewClient;
import java.io.FileInputStream;
import java.util.zip.Checksum;
import android.util.Log;
import android.view.Window;
import android.os.AsyncTask;
import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import android.os.Build$VERSION;
import java.io.File;
import android.os.Environment;
import java.util.Iterator;
import android.content.pm.PackageInfo;
import org.json.JSONObject;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.app.Activity;
import android.view.Display;
import android.util.TypedValue;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import android.content.Context;

public class ac
{
    private static String A;
    private static float B;
    private static float C;
    private static boolean D;
    private static boolean E;
    private static boolean F;
    private static long G;
    private static String H;
    private static String I;
    private static bv J;
    private static long[] K;
    private static String L;
    private static String M;
    private static String N;
    private static String O;
    private static ab P;
    private static bu Q;
    private static bq R;
    private static boolean S;
    private static String T;
    private static String U;
    public static Context a;
    public static boolean b;
    public static String c;
    public static int d;
    public static int e;
    public static long f;
    public static long g;
    public static boolean h;
    public static int i;
    public static int j;
    public static boolean k;
    public static boolean l;
    public static boolean m;
    public static Settings n;
    public static ArrayList<d> o;
    public static String p;
    public static n q;
    public static boolean r;
    public static String s;
    private static String t;
    private static String u;
    private static String v;
    private static long w;
    private static String[] x;
    private static au y;
    private static Settings z;
    
    static {
        boolean f = true;
        ac.t = "TremoPrefs";
        ac.u = "deviceID";
        ac.v = "__DEX_TAG__";
        ac.w = 0L;
        ac.x = new String[0];
        ac.A = "0";
        ac.D = true;
        ac.E = false;
        if (ac.D) {
            f = false;
        }
        ac.F = f;
        ac.G = 0L;
        ac.I = null;
        ac.J = new bv();
        ac.K = new long[ac.f.values().length];
        ac.L = "http://ads.videohub.tv";
        ac.M = null;
        ac.N = null;
        ac.O = null;
        ac.R = null;
        ac.b = false;
        ac.d = 2;
        ac.e = 2000;
        ac.f = 10000L;
        ac.g = 8000L;
        ac.h = false;
        ac.i = 100;
        ac.j = 5;
        ac.k = false;
        ac.l = false;
        ac.m = false;
        ac.n = null;
        ac.o = new ArrayList<d>();
        ac.p = null;
        ac.S = false;
        ac.q = null;
        ac.r = false;
        ac.s = null;
        ac.T = "com.google.market";
        ac.U = "com.android.vending";
    }
    
    public static String A() {
        String s = "3.11.1.59";
        if (!"3.11.1.59".contains(".")) {
            s = "3.11.0.debug";
        }
        return s;
    }
    
    public static String B() {
        synchronized (ac.class) {
            return ac.A;
        }
    }
    
    public static au C() {
        return ac.y;
    }
    
    public static boolean D() {
        return ac.y != null && ac.y.l();
    }
    
    public static void E() {
        ac.H = a(new GregorianCalendar());
        ac.G = G();
    }
    
    public static void F() {
        ac.Q.a();
        if (ac.H != null) {
            com.tremorvideo.sdk.android.videoad.z.a(ac.H, G() - ac.G);
            ac.H = null;
        }
        ac.P.b();
    }
    
    public static long G() {
        return System.nanoTime() / 1000000L - ac.w;
    }
    
    public static int H() {
        final Display defaultDisplay = ((WindowManager)ac.a.getSystemService("window")).getDefaultDisplay();
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return (int)TypedValue.applyDimension(2, 15.0f, displayMetrics);
    }
    
    public static int I() {
        return 25;
    }
    
    public static int J() {
        return 15;
    }
    
    public static int K() {
        final Display defaultDisplay = ((WindowManager)ac.a.getSystemService("window")).getDefaultDisplay();
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return (int)TypedValue.applyDimension(2, 12.0f, displayMetrics);
    }
    
    public static int L() {
        return 20;
    }
    
    public static float M() {
        if (ac.B == 0.0f) {
            ac.B = O();
        }
        return ac.B;
    }
    
    public static float N() {
        if (ac.C == 0.0f) {
            ac.C = U();
        }
        return ac.C;
    }
    
    public static float O() {
        final Display defaultDisplay = ((WindowManager)ac.a.getSystemService("window")).getDefaultDisplay();
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return displayMetrics.density;
    }
    
    public static void P() {
        if (r() < 17) {
            ((Activity)ac.a).runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    ac.p = new WebView(ac.a).getSettings().getUserAgentString();
                }
            });
            return;
        }
        ac.p = WebSettings.getDefaultUserAgent(ac.a);
    }
    
    public static void Q() {
        final String string = ac.a.getSharedPreferences("PreConfig", 0).getString("preConfigJSON", (String)null);
        e("Load pre config from cache:" + string);
        if (string == null || string.length() <= 0) {
            return;
        }
        try {
            final JSONObject jsonObject = new JSONObject(string);
            ac.M = jsonObject.getJSONObject("urls").getString("ads");
            if (jsonObject.has("adrequest_timeout")) {
                ac.e = jsonObject.getInt("adrequest_timeout");
            }
            if (jsonObject.has("adstart_timeout")) {
                ac.f = jsonObject.getLong("adstart_timeout");
            }
            if (jsonObject.has("max_buffer_time")) {
                ac.g = jsonObject.getLong("max_buffer_time");
            }
            if (jsonObject.has("disable_vast_hls")) {
                ac.h = jsonObject.getBoolean("disable_vast_hls");
            }
            if (jsonObject.has("throttle")) {
                ac.i = jsonObject.getInt("throttle");
                if (ac.i < 0 || ac.i > 100) {
                    ac.i = 100;
                }
            }
            if (jsonObject.has("maxRequestPerMinute")) {
                ac.j = jsonObject.getInt("maxRequestPerMinute");
            }
        }
        catch (Exception ex) {}
    }
    
    public static boolean R() {
        for (final PackageInfo packageInfo : ac.a.getPackageManager().getInstalledPackages(8192)) {
            if (packageInfo.packageName.equals(ac.T) || packageInfo.packageName.equals(ac.U)) {
                return true;
            }
        }
        return false;
    }
    
    private static float U() {
        final Display defaultDisplay = ((WindowManager)ac.a.getSystemService("window")).getDefaultDisplay();
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return displayMetrics.scaledDensity;
    }
    
    private static boolean V() {
        if (!ac.F) {
            try {
                final String externalStorageState = Environment.getExternalStorageState();
                if ((!externalStorageState.equals("mounted") && !externalStorageState.equals("mounted_ro")) || !new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separatorChar + "d89f24dc727d476db670624a16933ebd.debug").exists()) {
                    return false;
                }
            }
            catch (Exception ex) {
                a(ex);
                return false;
            }
        }
        return true;
    }
    
    public static int a(final int n) {
        final Display defaultDisplay = ((WindowManager)ac.a.getSystemService("window")).getDefaultDisplay();
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return (int)TypedValue.applyDimension(2, (float)n, displayMetrics);
    }
    
    public static aq a(final Context context) {
        if (Integer.parseInt(Build$VERSION.SDK) >= 7) {
            return new as(context);
        }
        return new ar(context);
    }
    
    public static String a(final File file) {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            final char[] array = new char[(int)file.length()];
            bufferedReader.read(array);
            bufferedReader.close();
            return String.valueOf(array);
        }
        catch (IOException ex) {
            a(ex);
            return "";
        }
    }
    
    public static String a(final InputStream inputStream) {
        StringBuilder sb;
        try {
            sb = new StringBuilder();
            final byte[] array = new byte[1024];
            while (true) {
                final int read = inputStream.read(array);
                if (read <= 0) {
                    break;
                }
                sb.append(new String(array, 0, read));
            }
        }
        catch (Exception ex) {
            a(ex);
            return "";
        }
        return sb.toString();
    }
    
    public static String a(final Calendar calendar) {
        final StringBuilder sb = new StringBuilder();
        sb.append(calendar.get(1));
        sb.append("-");
        sb.append(calendar.get(2) + 1);
        sb.append("-");
        sb.append(calendar.get(7));
        sb.append(" ");
        sb.append(calendar.get(11));
        sb.append(":");
        sb.append(calendar.get(12));
        sb.append(":");
        sb.append(calendar.get(13));
        return sb.toString();
    }
    
    public static void a() {
        ac.r = true;
    }
    
    public static void a(final Activity activity, final int n) {
        if (n == 0 || n == 6) {
            c(activity);
            return;
        }
        if (n == 1 || n == 7) {
            b(activity);
            return;
        }
        activity.setRequestedOrientation(-1);
    }
    
    public static void a(final Activity activity, final String s) {
        final long g = G();
        final int ordinal = ac.f.a.ordinal();
        if (g - ac.K[ordinal] >= 2000L) {
            e("Sending Custom Event: " + s);
            ac.K[ordinal] = g;
            com.tremorvideo.sdk.android.videoad.z.a(activity, s, new HashMap<String, String>());
            return;
        }
        e("Can't send a new event so soon: " + s);
    }
    
    public static void a(final Activity activity, final String s, final Map<String, String> map) {
        final long g = G();
        final int ordinal = ac.f.b.ordinal();
        if (g - ac.K[ordinal] >= 2000L) {
            e("Sending Custom Event with Parameters: " + s);
            ac.K[ordinal] = g;
            com.tremorvideo.sdk.android.videoad.z.a(activity, s, map);
            return;
        }
        e("Can't send a new event so soon: " + s);
    }
    
    public static void a(final Context a, final String[] array) {
        ac.w = System.nanoTime() / 1000000L;
        ac.a = a;
        ac.z = new Settings();
        ac.B = 0.0f;
        ac.C = 0.0f;
        ac.P = new ab(a);
        com.tremorvideo.sdk.android.videoad.z.a();
        ac.Q = new bu(a);
        ac.x = array.clone();
        P();
        for (int i = 0; i < ac.K.length; ++i) {
            ac.K[i] = 0L;
        }
        e e;
        while (true) {
            try {
                bp.a(a, true);
                ac.F = V();
                e = new e();
                if (Build$VERSION.SDK_INT >= 11) {
                    e.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new String[0]);
                    return;
                }
            }
            catch (Exception ex) {
                e("Core: Unable to get location: " + ex.toString());
                continue;
            }
            break;
        }
        e.execute((Object[])new String[0]);
    }
    
    public static void a(final Window window) {
        if (Integer.parseInt(Build$VERSION.SDK) >= 11) {
            new a().a();
        }
    }
    
    public static void a(final Settings settings) {
        synchronized (ac.class) {
            ac.z = new Settings(settings);
        }
    }
    
    public static void a(final c c, final String s) {
        a(c, s, null);
    }
    
    public static void a(final c c, final String s, final Throwable t) {
        final int n = 0;
        if (ac.F) {
            if (s != null) {
                final String[] split = s.split("\n");
                for (int length = split.length, i = 0; i < length; ++i) {
                    Log.v("tremor", split[i]);
                }
            }
            if (t != null) {
                final String message = t.getMessage();
                if (message != null) {
                    final String[] split2 = message.split("\n");
                    for (int length2 = split2.length, j = n; j < length2; ++j) {
                        Log.v("tremor", split2[j]);
                    }
                }
                else {
                    Log.v("tremor", t.toString());
                }
                a(t.getStackTrace());
            }
        }
    }
    
    public static void a(final String s) {
        ac.s = s;
    }
    
    public static void a(final String s, final String s2) {
        if (ac.y == null) {
            ac.o.add(new d(s, s2));
            return;
        }
        ac.y.a(s, s2);
    }
    
    public static void a(final String s, final Throwable t) {
        a(ac.c.a, s, t);
    }
    
    public static void a(final String s, final String[][] array, final String[][] array2) {
        Log.v("tremorQA", "[");
        Log.v("tremorQA", "    {");
        Log.v("tremorQA", "        \"event_type\": \"" + s + "\"");
        Log.v("tremorQA", "        \"event_data\":{");
        for (int i = 0; i < array.length; ++i) {
            Log.v("tremorQA", "            \"" + array[i][0] + "\": \"" + array[i][1] + "\"");
        }
        Log.v("tremorQA", "            \"parameters\":{");
        for (int j = 0; j < array2.length; ++j) {
            Log.v("tremorQA", "                \"" + array2[j][0] + "\": \"" + array2[j][1] + "\"");
        }
        Log.v("tremorQA", "                }");
        Log.v("tremorQA", "        }");
        Log.v("tremorQA", "    }");
        Log.v("tremorQA", "],");
    }
    
    public static void a(final Throwable t) {
        a(ac.c.a, null, t);
    }
    
    public static void a(final Checksum checksum, final File file) {
        checksum.reset();
        FileInputStream fileInputStream = null;
        Label_0062: {
            try {
                if (file.exists()) {
                    final byte[] array = new byte[1024];
                    fileInputStream = new FileInputStream(file);
                    while (true) {
                        final int read = fileInputStream.read(array);
                        if (read <= 0) {
                            break Label_0062;
                        }
                        checksum.update(array, 0, read);
                    }
                }
            }
            catch (Exception ex) {
                a(ex);
                checksum.reset();
            }
            return;
        }
        fileInputStream.close();
    }
    
    public static void a(final JSONObject jsonObject) {
        synchronized (ac.class) {
            while (true) {
                try {
                    ac.c = jsonObject.getJSONObject("parameters").getString("AppID");
                    ((Activity)ac.a).runOnUiThread((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            ac.e("Init BlueKai");
                            final WebView webView = new WebView(ac.a);
                            webView.getSettings().setJavaScriptEnabled(true);
                            webView.setWebViewClient((WebViewClient)new WebViewClient() {
                                public void onPageFinished(final WebView webView, final String s) {
                                    ac.e("BlueKai Page Loaded");
                                    if (!ac.S && !ac.b) {
                                        try {
                                            final String string = "init(" + jsonObject + ")";
                                            ac.S = true;
                                            if (ac.r() < 19) {
                                                webView.loadUrl("javascript:" + string);
                                            }
                                            else {
                                                webView.evaluateJavascript(string, (ValueCallback)null);
                                            }
                                            ac.b = true;
                                        }
                                        catch (Exception ex) {
                                            ac.e("Error firing BK: " + ex.toString());
                                        }
                                    }
                                }
                            });
                            webView.setWebChromeClient((WebChromeClient)new WebChromeClient() {
                                public void onConsoleMessage(final String s, final int n, final String s2) {
                                    ac.e("BluKai Console: " + s);
                                }
                                
                                public boolean onConsoleMessage(final ConsoleMessage consoleMessage) {
                                    ac.e("BluKai Console: " + consoleMessage.message());
                                    return false;
                                }
                            });
                            try {
                                ac.e("Loading URL");
                                webView.loadUrl(jsonObject.getString("template-url"));
                            }
                            catch (Exception ex) {
                                ac.e("enableBlueKaiJS loadurl Exception:" + ex);
                            }
                        }
                    });
                }
                catch (JSONException ex) {
                    ac.c = "";
                    continue;
                }
                break;
            }
        }
    }
    
    public static void a(final StackTraceElement[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            e("  " + array[i].toString());
        }
    }
    
    public static boolean a(final Activity activity) {
        return Integer.parseInt(Build$VERSION.SDK) >= 11 && new b().a();
    }
    
    public static boolean a(final aw.b b) {
        bs i = null;
        if (ac.y != null) {
            i = ac.y.i();
        }
        return i == null || !i.a(b);
    }
    
    public static boolean a(final Checksum checksum, final File file, final long n) {
        if (n == 0L) {
            e("No CRC to check.");
        }
        else {
            e("Calculating CRC...");
            a(checksum, file);
            final long value = checksum.getValue();
            if (value != n) {
                e("CRC is invalid. Expected: " + n + ". Got: " + value);
                return false;
            }
            e("CRC is good: " + value);
        }
        return true;
    }
    
    public static void b() {
        final Runtime runtime = Runtime.getRuntime();
        e("Free Memory: " + (runtime.maxMemory() - runtime.totalMemory() + runtime.freeMemory()) / 1024L / 1024L + "MB");
    }
    
    public static void b(final Activity activity) {
        if (Integer.parseInt(Build$VERSION.SDK) >= 9) {
            activity.setRequestedOrientation(7);
            return;
        }
        activity.setRequestedOrientation(1);
    }
    
    public static void b(final Activity activity, final String s) {
        final long g = G();
        final int ordinal = ac.f.c.ordinal();
        if (g - ac.K[ordinal] >= 2000L) {
            e("Sending State Change: " + s);
            ac.K[ordinal] = g;
            com.tremorvideo.sdk.android.videoad.z.a(activity, s);
            return;
        }
        e("Can't send a new event so soon: " + s);
    }
    
    public static boolean b(final String s) {
        return ac.a.checkCallingOrSelfPermission(s) == 0;
    }
    
    public static void c() {
        a(ac.c.b, "Core - Destroy");
        ac.w = 0L;
        ac.z = new Settings();
        ac.a = null;
        if (ac.R != null) {
            ac.R.a();
            ac.R = null;
        }
        ac.y.c();
        ac.y = null;
        ac.N = null;
        ac.O = null;
        for (int i = 0; i < ac.K.length; ++i) {
            ac.K[i] = 0L;
        }
    }
    
    public static void c(final Activity activity) {
        if (Integer.parseInt(Build$VERSION.SDK) >= 9) {
            activity.setRequestedOrientation(6);
            return;
        }
        activity.setRequestedOrientation(0);
    }
    
    public static void c(final String a) {
        synchronized (ac.class) {
            ac.A = a;
        }
    }
    
    public static String d() {
        if (false) {
            return null;
        }
        if (ac.N != null) {
            return ac.N;
        }
        if (ac.D) {
            if (ac.M != null && ac.M.length() > 0) {
                if (ac.s != null) {
                    return ac.M + "?ssF=default&ssCI=" + ac.s;
                }
                return ac.M;
            }
            else {
                if (ac.s != null) {
                    return ac.L + "/tap/ad/Ad" + "?ssF=default&ssCI=" + ac.s;
                }
                return ac.L + "/tap/ad/Ad";
            }
        }
        else if (ac.M != null && ac.M.length() > 0) {
            if (ac.s != null) {
                return ac.M + "?ssF=default&ssCI=" + ac.s;
            }
            return ac.M;
        }
        else {
            if (ac.s != null) {
                return ac.L + "/tap/ad/Ad" + "?ssF=default&ssCI=" + ac.s;
            }
            return ac.L + "/tap/ad/Ad";
        }
    }
    
    public static GregorianCalendar d(final String s) {
        final String[] split = s.split(" ");
        final String[] split2 = split[0].split("-");
        final String[] split3 = split[1].split(":");
        return new GregorianCalendar(Integer.parseInt(split2[0]), Integer.parseInt(split2[1]), Integer.parseInt(split2[2]), Integer.parseInt(split3[0]), Integer.parseInt(split3[1]), Integer.parseInt(split3[2]));
    }
    
    public static void e(final String s) {
        a(ac.c.a, s, null);
    }
    
    public static boolean e() {
        return ac.F;
    }
    
    public static String f() {
        if (ac.D) {
            return "http://l0.videohub.tv/ssframework/tap/avail/Avail";
        }
        return "http://l0.videohub/ssframework/tap/avail/Avail";
    }
    
    public static void f(final String s) {
        Log.v("tremorQA", s);
    }
    
    public static ab g() {
        return ac.P;
    }
    
    public static void g(final String p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aconst_null    
        //     1: astore_2       
        //     2: ldc             Lcom/tremorvideo/sdk/android/videoad/ac;.class
        //     4: monitorenter   
        //     5: aload_0        
        //     6: ifnull          424
        //     9: aload_0        
        //    10: invokevirtual   java/lang/String.length:()I
        //    13: istore_1       
        //    14: iload_1        
        //    15: ifle            424
        //    18: new             Ljava/lang/StringBuilder;
        //    21: dup            
        //    22: invokespecial   java/lang/StringBuilder.<init>:()V
        //    25: ldc_w           "pre config response new :"
        //    28: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    31: aload_0        
        //    32: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    35: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    38: invokestatic    com/tremorvideo/sdk/android/videoad/ac.e:(Ljava/lang/String;)V
        //    41: new             Lorg/json/JSONObject;
        //    44: dup            
        //    45: aload_0        
        //    46: invokespecial   org/json/JSONObject.<init>:(Ljava/lang/String;)V
        //    49: astore_3       
        //    50: aload_3        
        //    51: ldc_w           "urls"
        //    54: invokevirtual   org/json/JSONObject.has:(Ljava/lang/String;)Z
        //    57: ifeq            284
        //    60: aload_3        
        //    61: ldc_w           "urls"
        //    64: invokevirtual   org/json/JSONObject.getJSONObject:(Ljava/lang/String;)Lorg/json/JSONObject;
        //    67: astore          4
        //    69: aload           4
        //    71: ldc_w           "ads"
        //    74: invokevirtual   org/json/JSONObject.has:(Ljava/lang/String;)Z
        //    77: ifeq            89
        //    80: aload           4
        //    82: ldc_w           "ads"
        //    85: invokevirtual   org/json/JSONObject.getString:(Ljava/lang/String;)Ljava/lang/String;
        //    88: astore_2       
        //    89: aload_2        
        //    90: ifnull          388
        //    93: aload_2        
        //    94: invokevirtual   java/lang/String.length:()I
        //    97: ifle            388
        //   100: aload_2        
        //   101: invokestatic    android/webkit/URLUtil.isValidUrl:(Ljava/lang/String;)Z
        //   104: ifeq            388
        //   107: aload_2        
        //   108: putstatic       com/tremorvideo/sdk/android/videoad/ac.M:Ljava/lang/String;
        //   111: aload_3        
        //   112: ldc_w           "adrequest_timeout"
        //   115: invokevirtual   org/json/JSONObject.has:(Ljava/lang/String;)Z
        //   118: ifeq            131
        //   121: aload_3        
        //   122: ldc_w           "adrequest_timeout"
        //   125: invokevirtual   org/json/JSONObject.getInt:(Ljava/lang/String;)I
        //   128: putstatic       com/tremorvideo/sdk/android/videoad/ac.e:I
        //   131: aload_3        
        //   132: ldc_w           "adstart_timeout"
        //   135: invokevirtual   org/json/JSONObject.has:(Ljava/lang/String;)Z
        //   138: ifeq            151
        //   141: aload_3        
        //   142: ldc_w           "adstart_timeout"
        //   145: invokevirtual   org/json/JSONObject.getLong:(Ljava/lang/String;)J
        //   148: putstatic       com/tremorvideo/sdk/android/videoad/ac.f:J
        //   151: aload_3        
        //   152: ldc_w           "max_buffer_time"
        //   155: invokevirtual   org/json/JSONObject.has:(Ljava/lang/String;)Z
        //   158: ifeq            171
        //   161: aload_3        
        //   162: ldc_w           "max_buffer_time"
        //   165: invokevirtual   org/json/JSONObject.getLong:(Ljava/lang/String;)J
        //   168: putstatic       com/tremorvideo/sdk/android/videoad/ac.g:J
        //   171: aload_3        
        //   172: ldc_w           "disable_vast_hls"
        //   175: invokevirtual   org/json/JSONObject.has:(Ljava/lang/String;)Z
        //   178: ifeq            191
        //   181: aload_3        
        //   182: ldc_w           "disable_vast_hls"
        //   185: invokevirtual   org/json/JSONObject.getBoolean:(Ljava/lang/String;)Z
        //   188: putstatic       com/tremorvideo/sdk/android/videoad/ac.h:Z
        //   191: aload_3        
        //   192: ldc_w           "throttle"
        //   195: invokevirtual   org/json/JSONObject.has:(Ljava/lang/String;)Z
        //   198: ifeq            230
        //   201: aload_3        
        //   202: ldc_w           "throttle"
        //   205: invokevirtual   org/json/JSONObject.getInt:(Ljava/lang/String;)I
        //   208: putstatic       com/tremorvideo/sdk/android/videoad/ac.i:I
        //   211: getstatic       com/tremorvideo/sdk/android/videoad/ac.i:I
        //   214: iflt            225
        //   217: getstatic       com/tremorvideo/sdk/android/videoad/ac.i:I
        //   220: bipush          100
        //   222: if_icmple       230
        //   225: bipush          100
        //   227: putstatic       com/tremorvideo/sdk/android/videoad/ac.i:I
        //   230: aload_3        
        //   231: ldc_w           "maxRequestPerMinute"
        //   234: invokevirtual   org/json/JSONObject.has:(Ljava/lang/String;)Z
        //   237: ifeq            250
        //   240: aload_3        
        //   241: ldc_w           "maxRequestPerMinute"
        //   244: invokevirtual   org/json/JSONObject.getInt:(Ljava/lang/String;)I
        //   247: putstatic       com/tremorvideo/sdk/android/videoad/ac.j:I
        //   250: getstatic       com/tremorvideo/sdk/android/videoad/ac.a:Landroid/content/Context;
        //   253: ldc_w           "PreConfig"
        //   256: iconst_0       
        //   257: invokevirtual   android/content/Context.getSharedPreferences:(Ljava/lang/String;I)Landroid/content/SharedPreferences;
        //   260: invokeinterface android/content/SharedPreferences.edit:()Landroid/content/SharedPreferences$Editor;
        //   265: astore_2       
        //   266: aload_2        
        //   267: ldc_w           "preConfigJSON"
        //   270: aload_0        
        //   271: invokeinterface android/content/SharedPreferences$Editor.putString:(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;
        //   276: pop            
        //   277: aload_2        
        //   278: invokeinterface android/content/SharedPreferences$Editor.commit:()Z
        //   283: pop            
        //   284: new             Ljava/lang/StringBuilder;
        //   287: dup            
        //   288: invokespecial   java/lang/StringBuilder.<init>:()V
        //   291: ldc_w           "FlowType: "
        //   294: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   297: getstatic       com/tremorvideo/sdk/android/videoad/ac.d:I
        //   300: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   303: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   306: invokestatic    com/tremorvideo/sdk/android/videoad/ac.e:(Ljava/lang/String;)V
        //   309: new             Lcom/tremorvideo/sdk/android/videoad/au;
        //   312: dup            
        //   313: getstatic       com/tremorvideo/sdk/android/videoad/ac.a:Landroid/content/Context;
        //   316: invokestatic    com/tremorvideo/sdk/android/videoad/ac.d:()Ljava/lang/String;
        //   319: invokespecial   com/tremorvideo/sdk/android/videoad/au.<init>:(Landroid/content/Context;Ljava/lang/String;)V
        //   322: putstatic       com/tremorvideo/sdk/android/videoad/ac.y:Lcom/tremorvideo/sdk/android/videoad/au;
        //   325: iconst_1       
        //   326: putstatic       com/tremorvideo/sdk/android/videoad/ac.k:Z
        //   329: iconst_1       
        //   330: putstatic       com/tremorvideo/sdk/android/videoad/TremorVideo._Initialized:I
        //   333: getstatic       com/tremorvideo/sdk/android/videoad/ac.o:Ljava/util/ArrayList;
        //   336: invokevirtual   java/util/ArrayList.size:()I
        //   339: ifle            436
        //   342: getstatic       com/tremorvideo/sdk/android/videoad/ac.o:Ljava/util/ArrayList;
        //   345: invokevirtual   java/util/ArrayList.iterator:()Ljava/util/Iterator;
        //   348: astore_0       
        //   349: aload_0        
        //   350: invokeinterface java/util/Iterator.hasNext:()Z
        //   355: ifeq            430
        //   358: aload_0        
        //   359: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   364: checkcast       Lcom/tremorvideo/sdk/android/videoad/ac$d;
        //   367: astore_2       
        //   368: aload_2        
        //   369: getfield        com/tremorvideo/sdk/android/videoad/ac$d.a:Ljava/lang/String;
        //   372: aload_2        
        //   373: getfield        com/tremorvideo/sdk/android/videoad/ac$d.b:Ljava/lang/String;
        //   376: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Ljava/lang/String;Ljava/lang/String;)V
        //   379: goto            349
        //   382: astore_0       
        //   383: ldc             Lcom/tremorvideo/sdk/android/videoad/ac;.class
        //   385: monitorexit    
        //   386: aload_0        
        //   387: athrow         
        //   388: invokestatic    com/tremorvideo/sdk/android/videoad/ac.Q:()V
        //   391: goto            284
        //   394: astore_0       
        //   395: new             Ljava/lang/StringBuilder;
        //   398: dup            
        //   399: invokespecial   java/lang/StringBuilder.<init>:()V
        //   402: ldc_w           "Exception pre config JSON processing "
        //   405: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   408: aload_0        
        //   409: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   412: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   415: invokestatic    com/tremorvideo/sdk/android/videoad/ac.e:(Ljava/lang/String;)V
        //   418: invokestatic    com/tremorvideo/sdk/android/videoad/ac.Q:()V
        //   421: goto            284
        //   424: invokestatic    com/tremorvideo/sdk/android/videoad/ac.Q:()V
        //   427: goto            284
        //   430: getstatic       com/tremorvideo/sdk/android/videoad/ac.o:Ljava/util/ArrayList;
        //   433: invokevirtual   java/util/ArrayList.clear:()V
        //   436: getstatic       com/tremorvideo/sdk/android/videoad/ac.n:Lcom/tremorvideo/sdk/android/videoad/Settings;
        //   439: ifnull          458
        //   442: ldc_w           "Delayed set Settings"
        //   445: invokestatic    com/tremorvideo/sdk/android/videoad/ac.e:(Ljava/lang/String;)V
        //   448: getstatic       com/tremorvideo/sdk/android/videoad/ac.n:Lcom/tremorvideo/sdk/android/videoad/Settings;
        //   451: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Lcom/tremorvideo/sdk/android/videoad/Settings;)V
        //   454: aconst_null    
        //   455: putstatic       com/tremorvideo/sdk/android/videoad/ac.n:Lcom/tremorvideo/sdk/android/videoad/Settings;
        //   458: getstatic       com/tremorvideo/sdk/android/videoad/ac.d:I
        //   461: ifne            481
        //   464: getstatic       com/tremorvideo/sdk/android/videoad/ac.l:Z
        //   467: ifeq            481
        //   470: iconst_0       
        //   471: putstatic       com/tremorvideo/sdk/android/videoad/ac.l:Z
        //   474: invokestatic    com/tremorvideo/sdk/android/videoad/ac.C:()Lcom/tremorvideo/sdk/android/videoad/au;
        //   477: iconst_0       
        //   478: invokevirtual   com/tremorvideo/sdk/android/videoad/au.a:(Z)V
        //   481: getstatic       com/tremorvideo/sdk/android/videoad/ac.d:I
        //   484: iconst_2       
        //   485: if_icmpne       505
        //   488: getstatic       com/tremorvideo/sdk/android/videoad/ac.m:Z
        //   491: ifeq            505
        //   494: iconst_0       
        //   495: putstatic       com/tremorvideo/sdk/android/videoad/ac.m:Z
        //   498: invokestatic    com/tremorvideo/sdk/android/videoad/ac.C:()Lcom/tremorvideo/sdk/android/videoad/au;
        //   501: iconst_0       
        //   502: invokevirtual   com/tremorvideo/sdk/android/videoad/au.a:(Z)V
        //   505: ldc             Lcom/tremorvideo/sdk/android/videoad/ac;.class
        //   507: monitorexit    
        //   508: return         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  9      14     382    388    Any
        //  18     69     394    424    Ljava/lang/Exception;
        //  18     69     382    388    Any
        //  69     89     394    424    Ljava/lang/Exception;
        //  69     89     382    388    Any
        //  93     131    394    424    Ljava/lang/Exception;
        //  93     131    382    388    Any
        //  131    151    394    424    Ljava/lang/Exception;
        //  131    151    382    388    Any
        //  151    171    394    424    Ljava/lang/Exception;
        //  151    171    382    388    Any
        //  171    191    394    424    Ljava/lang/Exception;
        //  171    191    382    388    Any
        //  191    225    394    424    Ljava/lang/Exception;
        //  191    225    382    388    Any
        //  225    230    394    424    Ljava/lang/Exception;
        //  225    230    382    388    Any
        //  230    250    394    424    Ljava/lang/Exception;
        //  230    250    382    388    Any
        //  250    284    394    424    Ljava/lang/Exception;
        //  250    284    382    388    Any
        //  284    349    382    388    Any
        //  349    379    382    388    Any
        //  388    391    394    424    Ljava/lang/Exception;
        //  388    391    382    388    Any
        //  395    421    382    388    Any
        //  424    427    382    388    Any
        //  430    436    382    388    Any
        //  436    458    382    388    Any
        //  458    481    382    388    Any
        //  481    505    382    388    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0089:
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
    
    public static bu h() {
        return ac.Q;
    }
    
    public static String i() {
        if (ac.D) {
            return "http://l0.videohub.tv/ssframework/tap/ad/Session";
        }
        return "http://l0.videohub.tv/ssframework/tap/ad/Session";
    }
    
    public static String j() {
        try {
            return ac.a.getPackageName();
        }
        catch (Exception ex) {
            return "unknown";
        }
    }
    
    public static String k() {
        try {
            return ac.a.getPackageManager().getPackageInfo(ac.a.getPackageName(), 0).versionName;
        }
        catch (Exception ex) {
            return "unknown";
        }
    }
    
    public static bv l() {
        return ac.J;
    }
    
    public static int m() {
        final Display defaultDisplay = ((WindowManager)ac.a.getSystemService("window")).getDefaultDisplay();
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
    
    public static int n() {
        final Display defaultDisplay = ((WindowManager)ac.a.getSystemService("window")).getDefaultDisplay();
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
    
    public static String o() {
        try {
            final ApplicationInfo applicationInfo = ac.a.getPackageManager().getPackageInfo(ac.a.getPackageName(), 0).applicationInfo;
            final Iterator<ActivityManager$RunningAppProcessInfo> iterator = ((ActivityManager)ac.a.getSystemService("activity")).getRunningAppProcesses().iterator();
            final PackageManager packageManager = ac.a.getPackageManager();
            while (iterator.hasNext()) {
                final ActivityManager$RunningAppProcessInfo activityManager$RunningAppProcessInfo = iterator.next();
                if (activityManager$RunningAppProcessInfo.processName.compareTo(applicationInfo.processName) == 0) {
                    return packageManager.getApplicationLabel(packageManager.getApplicationInfo(activityManager$RunningAppProcessInfo.processName, 128)).toString();
                }
            }
            return "unknown";
        }
        catch (Exception ex) {
            a(ex);
        }
        return "unknown";
    }
    
    public static String p() {
        return ac.x[0];
    }
    
    public static String q() {
        final SharedPreferences sharedPreferences = ac.a.getSharedPreferences(ac.t, 0);
        String i = sharedPreferences.getString(ac.u, (String)null);
        if (i == null || i.length() == 0) {
            e("DeviceID: No stored ID found");
            final String string = Settings$Secure.getString(ac.a.getContentResolver(), "android_id");
            e("DeviceID: Android ID = " + string);
            Label_0134: {
                if (string != null && string.length() != 0) {
                    i = string;
                    if (!string.equals("9774d56d682e549c")) {
                        break Label_0134;
                    }
                }
                e("DeviceID: falling back to randomUUID.");
                i = UUID.randomUUID().toString();
                e("DeviceID: udid = " + i);
            }
            e("DeviceID: Save ID: " + i);
            sharedPreferences.edit().putString(ac.u, i).commit();
        }
        else {
            e("DeviceID: Stored ID found");
        }
        if (i == null) {
            ac.I = "";
        }
        else {
            ac.I = i;
        }
        return ac.I;
    }
    
    public static int r() {
        return Integer.parseInt(Build$VERSION.SDK);
    }
    
    public static int s() {
        try {
            return ac.a.getPackageManager().getPackageInfo(j(), 0).applicationInfo.targetSdkVersion;
        }
        catch (PackageManager$NameNotFoundException ex) {
            a((Throwable)ex);
            return 0;
        }
    }
    
    public static boolean t() {
        final TelephonyManager telephonyManager = (TelephonyManager)ac.a.getSystemService("phone");
        final int phoneType = telephonyManager.getPhoneType();
        boolean b;
        if (x().checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == 0) {
            b = true;
        }
        else {
            b = false;
        }
        return b && phoneType != 0 && telephonyManager.getLine1Number() != null;
    }
    
    public static String[] u() {
        synchronized (ac.class) {
            return ac.x;
        }
    }
    
    public static Settings v() {
        synchronized (ac.class) {
            return ac.z;
        }
    }
    
    public static boolean w() {
        return ac.E;
    }
    
    public static Context x() {
        return ac.a;
    }
    
    public static String y() {
        return ac.p;
    }
    
    public static String z() {
        return "TransperaSDK v" + A() + " : " + Build.BRAND + " : " + Build.MODEL + " : Android OS : " + Build$VERSION.RELEASE;
    }
    
    class a
    {
        final /* synthetic */ Window a;
        
        a(final Window a) {
            this.a = a;
        }
        
        public void a() {
            this.a.setFlags(16777216, 16777216);
        }
    }
    
    class b
    {
        final /* synthetic */ Activity a;
        
        b(final Activity a) {
            this.a = a;
        }
        
        public boolean a() {
            boolean b = false;
            try {
                if ((this.a.getPackageManager().getActivityInfo(this.a.getComponentName(), 0).flags & 0x200) == 0x200) {
                    b = true;
                }
                return b;
            }
            catch (Exception ex) {
                ac.a(ex);
                return false;
            }
        }
    }
    
    public enum c
    {
        a, 
        b, 
        c;
    }
    
    public static class d
    {
        public String a;
        public String b;
        
        public d(final String a, final String b) {
            this.a = a;
            this.b = b;
        }
    }
    
    static class e extends AsyncTask<String, Void, String>
    {
        String a;
        
        protected String a(final String... array) {
            while (true) {
                while (true) {
                    Label_0455: {
                        try {
                            this.a = null;
                            final BasicHttpParams basicHttpParams = new BasicHttpParams();
                            ((HttpParams)basicHttpParams).setParameter("http.protocol.version", (Object)HttpVersion.HTTP_1_1);
                            HttpConnectionParams.setConnectionTimeout((HttpParams)basicHttpParams, 8000);
                            HttpConnectionParams.setSoTimeout((HttpParams)basicHttpParams, 8000);
                            HttpConnectionParams.setTcpNoDelay((HttpParams)basicHttpParams, true);
                            final DefaultHttpClient defaultHttpClient = new DefaultHttpClient((HttpParams)basicHttpParams);
                            final at a = at.a();
                            String k = ac.k();
                            if (k == null) {
                                k = "";
                                String t = "http://config.tremorhub.com/config";
                                if (ac.O != null) {
                                    t = ac.O;
                                }
                                StringBuilder sb;
                                if ((sb = new StringBuilder(t)).toString().startsWith("__")) {
                                    sb = new StringBuilder("http://config.tremorhub.com/config");
                                }
                                sb.append("?cch=");
                                sb.append(ac.p());
                                sb.append("&v=");
                                sb.append(a.a);
                                sb.append("&a=");
                                sb.append(ac.j());
                                sb.append("&av=");
                                sb.append(URLEncoder.encode(k, "UTF-8"));
                                sb.append("&p=android");
                                sb.append("&di=");
                                sb.append(a.f);
                                sb.append("&lt=");
                                sb.append(a.g);
                                sb.append("&dm=");
                                sb.append(URLEncoder.encode(a.c, "UTF-8"));
                                sb.append("&nt=");
                                sb.append(a.i);
                                sb.append("&nst=");
                                sb.append(a.D);
                                sb.append("&osv=");
                                sb.append(URLEncoder.encode(a.e, "UTF-8"));
                                sb.append("&mnc=");
                                sb.append(a.B);
                                sb.append("&mcc=");
                                sb.append(a.A);
                                sb.append("&ua=");
                                sb.append(URLEncoder.encode(a.C, "UTF-8"));
                                ac.e("Debug: pre config URL: " + sb.toString());
                                final HttpGet httpGet = new HttpGet(sb.toString());
                                httpGet.setHeader("User-Agent", ac.z());
                                return this.a = EntityUtils.toString(((HttpClient)defaultHttpClient).execute((HttpUriRequest)httpGet).getEntity());
                            }
                            break Label_0455;
                        }
                        catch (Exception ex) {
                            ac.e("Exception pre config:" + ex);
                            return null;
                        }
                    }
                    continue;
                }
            }
        }
        
        protected void a(final String s) {
            while (true) {
                try {
                    ac.g(this.a);
                    this.cancel(true);
                }
                catch (Exception ex) {
                    ac.a(ex);
                    continue;
                }
                break;
            }
        }
        
        protected void b(final String s) {
            Log.d("KK", "This should not happen!");
        }
    }
    
    private enum f
    {
        a, 
        b, 
        c;
    }
}
