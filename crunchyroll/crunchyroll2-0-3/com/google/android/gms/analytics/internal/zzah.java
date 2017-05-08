// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import java.net.URLConnection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.HttpURLConnection;
import java.io.IOException;
import com.google.android.gms.common.internal.zzu;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.URL;
import android.os.Build;
import java.util.Locale;
import android.os.Build$VERSION;

class zzah extends zzd
{
    private static final byte[] zzMs;
    private final String zzFP;
    private final zzaj zzMr;
    
    static {
        zzMs = "\n".getBytes();
    }
    
    zzah(final zzf zzf) {
        super(zzf);
        this.zzFP = zza("GoogleAnalytics", zze.VERSION, Build$VERSION.RELEASE, zzam.zza(Locale.getDefault()), Build.MODEL, Build.ID);
        this.zzMr = new zzaj(zzf.zzhP());
    }
    
    private int zza(final URL p0, final byte[] p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aconst_null    
        //     1: astore          6
        //     3: aconst_null    
        //     4: astore          8
        //     6: aconst_null    
        //     7: astore          9
        //     9: aconst_null    
        //    10: astore          7
        //    12: aload_1        
        //    13: invokestatic    com/google/android/gms/common/internal/zzu.zzu:(Ljava/lang/Object;)Ljava/lang/Object;
        //    16: pop            
        //    17: aload_2        
        //    18: invokestatic    com/google/android/gms/common/internal/zzu.zzu:(Ljava/lang/Object;)Ljava/lang/Object;
        //    21: pop            
        //    22: aload_0        
        //    23: ldc             "POST bytes, url"
        //    25: aload_2        
        //    26: arraylength    
        //    27: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    30: aload_1        
        //    31: invokevirtual   com/google/android/gms/analytics/internal/zzah.zzb:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //    34: aload_0        
        //    35: invokevirtual   com/google/android/gms/analytics/internal/zzah.zzhZ:()Z
        //    38: ifeq            55
        //    41: aload_0        
        //    42: ldc             "Post payload\n"
        //    44: new             Ljava/lang/String;
        //    47: dup            
        //    48: aload_2        
        //    49: invokespecial   java/lang/String.<init>:([B)V
        //    52: invokevirtual   com/google/android/gms/analytics/internal/zzah.zza:(Ljava/lang/String;Ljava/lang/Object;)V
        //    55: aload_0        
        //    56: aload_1        
        //    57: invokevirtual   com/google/android/gms/analytics/internal/zzah.zzc:(Ljava/net/URL;)Ljava/net/HttpURLConnection;
        //    60: astore_1       
        //    61: aload_1        
        //    62: astore          6
        //    64: aload           8
        //    66: astore_1       
        //    67: aload           6
        //    69: astore          5
        //    71: aload           9
        //    73: astore          7
        //    75: aload           6
        //    77: iconst_1       
        //    78: invokevirtual   java/net/HttpURLConnection.setDoOutput:(Z)V
        //    81: aload           8
        //    83: astore_1       
        //    84: aload           6
        //    86: astore          5
        //    88: aload           9
        //    90: astore          7
        //    92: aload           6
        //    94: aload_2        
        //    95: arraylength    
        //    96: invokevirtual   java/net/HttpURLConnection.setFixedLengthStreamingMode:(I)V
        //    99: aload           8
        //   101: astore_1       
        //   102: aload           6
        //   104: astore          5
        //   106: aload           9
        //   108: astore          7
        //   110: aload           6
        //   112: invokevirtual   java/net/HttpURLConnection.connect:()V
        //   115: aload           8
        //   117: astore_1       
        //   118: aload           6
        //   120: astore          5
        //   122: aload           9
        //   124: astore          7
        //   126: aload           6
        //   128: invokevirtual   java/net/HttpURLConnection.getOutputStream:()Ljava/io/OutputStream;
        //   131: astore          8
        //   133: aload           8
        //   135: astore_1       
        //   136: aload           6
        //   138: astore          5
        //   140: aload           8
        //   142: astore          7
        //   144: aload           8
        //   146: aload_2        
        //   147: invokevirtual   java/io/OutputStream.write:([B)V
        //   150: aload           8
        //   152: astore_1       
        //   153: aload           6
        //   155: astore          5
        //   157: aload           8
        //   159: astore          7
        //   161: aload_0        
        //   162: aload           6
        //   164: invokespecial   com/google/android/gms/analytics/internal/zzah.zzb:(Ljava/net/HttpURLConnection;)V
        //   167: aload           8
        //   169: astore_1       
        //   170: aload           6
        //   172: astore          5
        //   174: aload           8
        //   176: astore          7
        //   178: aload           6
        //   180: invokevirtual   java/net/HttpURLConnection.getResponseCode:()I
        //   183: istore          4
        //   185: iload           4
        //   187: sipush          200
        //   190: if_icmpne       211
        //   193: aload           8
        //   195: astore_1       
        //   196: aload           6
        //   198: astore          5
        //   200: aload           8
        //   202: astore          7
        //   204: aload_0        
        //   205: invokevirtual   com/google/android/gms/analytics/internal/zzah.zzhl:()Lcom/google/android/gms/analytics/internal/zzb;
        //   208: invokevirtual   com/google/android/gms/analytics/internal/zzb.zzhL:()V
        //   211: aload           8
        //   213: astore_1       
        //   214: aload           6
        //   216: astore          5
        //   218: aload           8
        //   220: astore          7
        //   222: aload_0        
        //   223: ldc             "POST status"
        //   225: iload           4
        //   227: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   230: invokevirtual   com/google/android/gms/analytics/internal/zzah.zzb:(Ljava/lang/String;Ljava/lang/Object;)V
        //   233: aload           8
        //   235: ifnull          243
        //   238: aload           8
        //   240: invokevirtual   java/io/OutputStream.close:()V
        //   243: iload           4
        //   245: istore_3       
        //   246: aload           6
        //   248: ifnull          259
        //   251: aload           6
        //   253: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   256: iload           4
        //   258: istore_3       
        //   259: iload_3        
        //   260: ireturn        
        //   261: astore_1       
        //   262: aload_0        
        //   263: ldc             "Error closing http post connection output stream"
        //   265: aload_1        
        //   266: invokevirtual   com/google/android/gms/analytics/internal/zzah.zze:(Ljava/lang/String;Ljava/lang/Object;)V
        //   269: goto            243
        //   272: astore_2       
        //   273: aconst_null    
        //   274: astore          6
        //   276: aload           7
        //   278: astore_1       
        //   279: aload           6
        //   281: astore          5
        //   283: aload_0        
        //   284: ldc             "Network POST connection error"
        //   286: aload_2        
        //   287: invokevirtual   com/google/android/gms/analytics/internal/zzah.zzd:(Ljava/lang/String;Ljava/lang/Object;)V
        //   290: iconst_0       
        //   291: istore_3       
        //   292: aload           7
        //   294: ifnull          302
        //   297: aload           7
        //   299: invokevirtual   java/io/OutputStream.close:()V
        //   302: aload           6
        //   304: ifnull          259
        //   307: aload           6
        //   309: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   312: iconst_0       
        //   313: ireturn        
        //   314: astore_1       
        //   315: aload_0        
        //   316: ldc             "Error closing http post connection output stream"
        //   318: aload_1        
        //   319: invokevirtual   com/google/android/gms/analytics/internal/zzah.zze:(Ljava/lang/String;Ljava/lang/Object;)V
        //   322: goto            302
        //   325: astore_2       
        //   326: aconst_null    
        //   327: astore          5
        //   329: aload           6
        //   331: astore_1       
        //   332: aload_1        
        //   333: ifnull          340
        //   336: aload_1        
        //   337: invokevirtual   java/io/OutputStream.close:()V
        //   340: aload           5
        //   342: ifnull          350
        //   345: aload           5
        //   347: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   350: aload_2        
        //   351: athrow         
        //   352: astore_1       
        //   353: aload_0        
        //   354: ldc             "Error closing http post connection output stream"
        //   356: aload_1        
        //   357: invokevirtual   com/google/android/gms/analytics/internal/zzah.zze:(Ljava/lang/String;Ljava/lang/Object;)V
        //   360: goto            340
        //   363: astore_2       
        //   364: goto            332
        //   367: astore_2       
        //   368: goto            276
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  55     61     272    276    Ljava/io/IOException;
        //  55     61     325    332    Any
        //  75     81     367    371    Ljava/io/IOException;
        //  75     81     363    367    Any
        //  92     99     367    371    Ljava/io/IOException;
        //  92     99     363    367    Any
        //  110    115    367    371    Ljava/io/IOException;
        //  110    115    363    367    Any
        //  126    133    367    371    Ljava/io/IOException;
        //  126    133    363    367    Any
        //  144    150    367    371    Ljava/io/IOException;
        //  144    150    363    367    Any
        //  161    167    367    371    Ljava/io/IOException;
        //  161    167    363    367    Any
        //  178    185    367    371    Ljava/io/IOException;
        //  178    185    363    367    Any
        //  204    211    367    371    Ljava/io/IOException;
        //  204    211    363    367    Any
        //  222    233    367    371    Ljava/io/IOException;
        //  222    233    363    367    Any
        //  238    243    261    272    Ljava/io/IOException;
        //  283    290    363    367    Any
        //  297    302    314    325    Ljava/io/IOException;
        //  336    340    352    363    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0211:
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
    
    private static String zza(final String s, final String s2, final String s3, final String s4, final String s5, final String s6) {
        return String.format("%s/%s (Linux; U; Android %s; %s; %s Build/%s)", s, s2, s3, s4, s5, s6);
    }
    
    private void zza(final StringBuilder sb, final String s, final String s2) throws UnsupportedEncodingException {
        if (sb.length() != 0) {
            sb.append('&');
        }
        sb.append(URLEncoder.encode(s, "UTF-8"));
        sb.append('=');
        sb.append(URLEncoder.encode(s2, "UTF-8"));
    }
    
    private int zzb(final URL url) {
        zzu.zzu(url);
        this.zzb("GET request", url);
        HttpURLConnection httpURLConnection = null;
        HttpURLConnection zzc = null;
        try {
            final HttpURLConnection httpURLConnection2 = httpURLConnection = (zzc = this.zzc(url));
            httpURLConnection2.connect();
            zzc = httpURLConnection2;
            httpURLConnection = httpURLConnection2;
            this.zzb(httpURLConnection2);
            zzc = httpURLConnection2;
            httpURLConnection = httpURLConnection2;
            final int responseCode = httpURLConnection2.getResponseCode();
            if (responseCode == 200) {
                zzc = httpURLConnection2;
                httpURLConnection = httpURLConnection2;
                this.zzhl().zzhL();
            }
            zzc = httpURLConnection2;
            httpURLConnection = httpURLConnection2;
            this.zzb("GET status", responseCode);
            int n = responseCode;
            if (httpURLConnection2 != null) {
                httpURLConnection2.disconnect();
                n = responseCode;
            }
            return n;
        }
        catch (IOException ex) {
            httpURLConnection = zzc;
            this.zzd("Network GET connection error", ex);
            final int n = 0;
            return 0;
        }
        finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }
    
    private int zzb(final URL p0, final byte[] p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aconst_null    
        //     1: astore          6
        //     3: aconst_null    
        //     4: astore          5
        //     6: aload_1        
        //     7: invokestatic    com/google/android/gms/common/internal/zzu.zzu:(Ljava/lang/Object;)Ljava/lang/Object;
        //    10: pop            
        //    11: aload_2        
        //    12: invokestatic    com/google/android/gms/common/internal/zzu.zzu:(Ljava/lang/Object;)Ljava/lang/Object;
        //    15: pop            
        //    16: aload_2        
        //    17: invokestatic    com/google/android/gms/analytics/internal/zzah.zzg:([B)[B
        //    20: astore          7
        //    22: aload_0        
        //    23: ldc             "POST compressed size, ratio %, url"
        //    25: aload           7
        //    27: arraylength    
        //    28: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    31: ldc2_w          100
        //    34: aload           7
        //    36: arraylength    
        //    37: i2l            
        //    38: lmul           
        //    39: aload_2        
        //    40: arraylength    
        //    41: i2l            
        //    42: ldiv           
        //    43: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //    46: aload_1        
        //    47: invokevirtual   com/google/android/gms/analytics/internal/zzah.zza:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
        //    50: aload           7
        //    52: arraylength    
        //    53: aload_2        
        //    54: arraylength    
        //    55: if_icmple       75
        //    58: aload_0        
        //    59: ldc             "Compressed payload is larger then uncompressed. compressed, uncompressed"
        //    61: aload           7
        //    63: arraylength    
        //    64: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    67: aload_2        
        //    68: arraylength    
        //    69: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    72: invokevirtual   com/google/android/gms/analytics/internal/zzah.zzc:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //    75: aload_0        
        //    76: invokevirtual   com/google/android/gms/analytics/internal/zzah.zzhZ:()Z
        //    79: ifeq            114
        //    82: aload_0        
        //    83: ldc             "Post payload"
        //    85: new             Ljava/lang/StringBuilder;
        //    88: dup            
        //    89: invokespecial   java/lang/StringBuilder.<init>:()V
        //    92: ldc             "\n"
        //    94: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    97: new             Ljava/lang/String;
        //   100: dup            
        //   101: aload_2        
        //   102: invokespecial   java/lang/String.<init>:([B)V
        //   105: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   108: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   111: invokevirtual   com/google/android/gms/analytics/internal/zzah.zza:(Ljava/lang/String;Ljava/lang/Object;)V
        //   114: aload_0        
        //   115: aload_1        
        //   116: invokevirtual   com/google/android/gms/analytics/internal/zzah.zzc:(Ljava/net/URL;)Ljava/net/HttpURLConnection;
        //   119: astore_1       
        //   120: aload_1        
        //   121: iconst_1       
        //   122: invokevirtual   java/net/HttpURLConnection.setDoOutput:(Z)V
        //   125: aload_1        
        //   126: ldc             "Content-Encoding"
        //   128: ldc             "gzip"
        //   130: invokevirtual   java/net/HttpURLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   133: aload_1        
        //   134: aload           7
        //   136: arraylength    
        //   137: invokevirtual   java/net/HttpURLConnection.setFixedLengthStreamingMode:(I)V
        //   140: aload_1        
        //   141: invokevirtual   java/net/HttpURLConnection.connect:()V
        //   144: aload_1        
        //   145: invokevirtual   java/net/HttpURLConnection.getOutputStream:()Ljava/io/OutputStream;
        //   148: astore_2       
        //   149: aload_2        
        //   150: aload           7
        //   152: invokevirtual   java/io/OutputStream.write:([B)V
        //   155: aload_2        
        //   156: invokevirtual   java/io/OutputStream.close:()V
        //   159: aload_0        
        //   160: aload_1        
        //   161: invokespecial   com/google/android/gms/analytics/internal/zzah.zzb:(Ljava/net/HttpURLConnection;)V
        //   164: aload_1        
        //   165: invokevirtual   java/net/HttpURLConnection.getResponseCode:()I
        //   168: istore          4
        //   170: iload           4
        //   172: sipush          200
        //   175: if_icmpne       185
        //   178: aload_0        
        //   179: invokevirtual   com/google/android/gms/analytics/internal/zzah.zzhl:()Lcom/google/android/gms/analytics/internal/zzb;
        //   182: invokevirtual   com/google/android/gms/analytics/internal/zzb.zzhL:()V
        //   185: aload_0        
        //   186: ldc             "POST status"
        //   188: iload           4
        //   190: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   193: invokevirtual   com/google/android/gms/analytics/internal/zzah.zzb:(Ljava/lang/String;Ljava/lang/Object;)V
        //   196: iconst_0       
        //   197: ifeq            208
        //   200: new             Ljava/lang/NullPointerException;
        //   203: dup            
        //   204: invokespecial   java/lang/NullPointerException.<init>:()V
        //   207: athrow         
        //   208: iload           4
        //   210: istore_3       
        //   211: aload_1        
        //   212: ifnull          222
        //   215: aload_1        
        //   216: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   219: iload           4
        //   221: istore_3       
        //   222: iload_3        
        //   223: ireturn        
        //   224: astore_2       
        //   225: aload_0        
        //   226: ldc             "Error closing http compressed post connection output stream"
        //   228: aload_2        
        //   229: invokevirtual   com/google/android/gms/analytics/internal/zzah.zze:(Ljava/lang/String;Ljava/lang/Object;)V
        //   232: goto            208
        //   235: astore_2       
        //   236: aconst_null    
        //   237: astore_1       
        //   238: aload_0        
        //   239: ldc_w           "Network compressed POST connection error"
        //   242: aload_2        
        //   243: invokevirtual   com/google/android/gms/analytics/internal/zzah.zzd:(Ljava/lang/String;Ljava/lang/Object;)V
        //   246: iconst_0       
        //   247: istore_3       
        //   248: aload           5
        //   250: ifnull          258
        //   253: aload           5
        //   255: invokevirtual   java/io/OutputStream.close:()V
        //   258: aload_1        
        //   259: ifnull          222
        //   262: aload_1        
        //   263: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   266: iconst_0       
        //   267: ireturn        
        //   268: astore_2       
        //   269: aload_0        
        //   270: ldc             "Error closing http compressed post connection output stream"
        //   272: aload_2        
        //   273: invokevirtual   com/google/android/gms/analytics/internal/zzah.zze:(Ljava/lang/String;Ljava/lang/Object;)V
        //   276: goto            258
        //   279: astore_2       
        //   280: aconst_null    
        //   281: astore_1       
        //   282: aload           6
        //   284: astore          5
        //   286: aload           5
        //   288: ifnull          296
        //   291: aload           5
        //   293: invokevirtual   java/io/OutputStream.close:()V
        //   296: aload_1        
        //   297: ifnull          304
        //   300: aload_1        
        //   301: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   304: aload_2        
        //   305: athrow         
        //   306: astore          5
        //   308: aload_0        
        //   309: ldc             "Error closing http compressed post connection output stream"
        //   311: aload           5
        //   313: invokevirtual   com/google/android/gms/analytics/internal/zzah.zze:(Ljava/lang/String;Ljava/lang/Object;)V
        //   316: goto            296
        //   319: astore_2       
        //   320: aload           6
        //   322: astore          5
        //   324: goto            286
        //   327: astore          6
        //   329: aload_2        
        //   330: astore          5
        //   332: aload           6
        //   334: astore_2       
        //   335: goto            286
        //   338: astore_2       
        //   339: goto            286
        //   342: astore_2       
        //   343: goto            238
        //   346: astore          6
        //   348: aload_2        
        //   349: astore          5
        //   351: aload           6
        //   353: astore_2       
        //   354: goto            238
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  16     75     235    238    Ljava/io/IOException;
        //  16     75     279    286    Any
        //  75     114    235    238    Ljava/io/IOException;
        //  75     114    279    286    Any
        //  114    120    235    238    Ljava/io/IOException;
        //  114    120    279    286    Any
        //  120    149    342    346    Ljava/io/IOException;
        //  120    149    319    327    Any
        //  149    159    346    357    Ljava/io/IOException;
        //  149    159    327    338    Any
        //  159    170    342    346    Ljava/io/IOException;
        //  159    170    319    327    Any
        //  178    185    342    346    Ljava/io/IOException;
        //  178    185    319    327    Any
        //  185    196    342    346    Ljava/io/IOException;
        //  185    196    319    327    Any
        //  200    208    224    235    Ljava/io/IOException;
        //  238    246    338    342    Any
        //  253    258    268    279    Ljava/io/IOException;
        //  291    296    306    319    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 191, Size: 191
        //     at java.util.ArrayList.rangeCheck(ArrayList.java:653)
        //     at java.util.ArrayList.get(ArrayList.java:429)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3303)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
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
    
    private URL zzb(final zzab zzab, final String s) {
        Label_0059: {
            if (!zzab.zzjY()) {
                break Label_0059;
            }
            String s2 = this.zzhR().zzjk() + this.zzhR().zzjm() + "?" + s;
            try {
                return new URL(s2);
                s2 = this.zzhR().zzjl() + this.zzhR().zzjm() + "?" + s;
                return new URL(s2);
            }
            catch (MalformedURLException ex) {
                this.zze("Error trying to parse the hardcoded host url", ex);
                return null;
            }
        }
    }
    
    private void zzb(final HttpURLConnection httpURLConnection) throws IOException {
        InputStream inputStream = null;
        try {
            final InputStream inputStream2 = inputStream = httpURLConnection.getInputStream();
            do {
                inputStream = inputStream2;
            } while (inputStream2.read(new byte[1024]) > 0);
            if (inputStream2 == null) {
                return;
            }
            try {
                inputStream2.close();
            }
            catch (IOException ex) {
                this.zze("Error closing http connection input stream", ex);
            }
        }
        finally {
            Label_0057: {
                if (inputStream == null) {
                    break Label_0057;
                }
                try {
                    inputStream.close();
                }
                catch (IOException ex2) {
                    this.zze("Error closing http connection input stream", ex2);
                }
            }
        }
    }
    
    private boolean zzg(final zzab zzab) {
        zzu.zzu(zzab);
        final String zza = this.zza(zzab, !zzab.zzjY());
        if (zza == null) {
            this.zzhQ().zza(zzab, "Error formatting hit for upload");
        }
        else if (zza.length() <= this.zzhR().zziZ()) {
            final URL zzb = this.zzb(zzab, zza);
            if (zzb == null) {
                this.zzaX("Failed to build collect GET endpoint url");
                return false;
            }
            if (this.zzb(zzb) != 200) {
                return false;
            }
        }
        else {
            final String zza2 = this.zza(zzab, false);
            if (zza2 == null) {
                this.zzhQ().zza(zzab, "Error formatting hit for POST upload");
                return true;
            }
            final byte[] bytes = zza2.getBytes();
            if (bytes.length > this.zzhR().zzjb()) {
                this.zzhQ().zza(zzab, "Hit payload exceeds size limit");
                return true;
            }
            final URL zzh = this.zzh(zzab);
            if (zzh == null) {
                this.zzaX("Failed to build collect POST endpoint url");
                return false;
            }
            if (this.zza(zzh, bytes) != 200) {
                return false;
            }
        }
        return true;
    }
    
    private static byte[] zzg(final byte[] array) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        gzipOutputStream.write(array);
        gzipOutputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }
    
    private URL zzh(final zzab zzab) {
        Label_0049: {
            if (!zzab.zzjY()) {
                break Label_0049;
            }
            String s = this.zzhR().zzjk() + this.zzhR().zzjm();
            try {
                return new URL(s);
                s = this.zzhR().zzjl() + this.zzhR().zzjm();
                return new URL(s);
            }
            catch (MalformedURLException ex) {
                this.zze("Error trying to parse the hardcoded host url", ex);
                return null;
            }
        }
    }
    
    private String zzi(final zzab zzab) {
        return String.valueOf(zzab.zzjV());
    }
    
    private URL zzkh() {
        final String string = this.zzhR().zzjk() + this.zzhR().zzjn();
        try {
            return new URL(string);
        }
        catch (MalformedURLException ex) {
            this.zze("Error trying to parse the hardcoded host url", ex);
            return null;
        }
    }
    
    String zza(final zzab zzab, final boolean b) {
        zzu.zzu(zzab);
        final StringBuilder sb = new StringBuilder();
        try {
            for (final Map.Entry<String, String> entry : zzab.zzn().entrySet()) {
                final String s = entry.getKey();
                if (!"ht".equals(s) && !"qt".equals(s) && !"AppUID".equals(s) && !"z".equals(s) && !"_gmsv".equals(s)) {
                    this.zza(sb, s, entry.getValue());
                }
            }
        }
        catch (UnsupportedEncodingException ex) {
            this.zze("Failed to encode name or value", ex);
            return null;
        }
        this.zza(sb, "ht", String.valueOf(zzab.zzjW()));
        this.zza(sb, "qt", String.valueOf(this.zzhP().currentTimeMillis() - zzab.zzjW()));
        if (this.zzhR().zziW()) {
            this.zza(sb, "_gmsv", zze.VERSION);
        }
        if (b) {
            final long zzjZ = zzab.zzjZ();
            String s2;
            if (zzjZ != 0L) {
                s2 = String.valueOf(zzjZ);
            }
            else {
                s2 = this.zzi(zzab);
            }
            this.zza(sb, "z", s2);
        }
        return sb.toString();
    }
    
    List<Long> zza(final List<zzab> list, final boolean b) {
        zzu.zzV(!list.isEmpty());
        this.zza("Uploading batched hits. compression, count", b, list.size());
        final zza zza = new zza();
        final ArrayList<Long> list2 = new ArrayList<Long>();
        for (final zzab zzab : list) {
            if (!zza.zzj(zzab)) {
                break;
            }
            list2.add(zzab.zzjV());
        }
        if (zza.zzkj() == 0) {
            return list2;
        }
        final URL zzkh = this.zzkh();
        if (zzkh == null) {
            this.zzaX("Failed to build batching endpoint url");
            return Collections.emptyList();
        }
        int n;
        if (b) {
            n = this.zzb(zzkh, zza.getPayload());
        }
        else {
            n = this.zza(zzkh, zza.getPayload());
        }
        if (200 == n) {
            this.zza("Batched upload completed. Hits batched", zza.zzkj());
            return list2;
        }
        this.zza("Network error uploading hits. status code", n);
        if (this.zzhR().zzjq().contains(n)) {
            this.zzaW("Server instructed the client to stop batching");
            this.zzMr.start();
        }
        return Collections.emptyList();
    }
    
    HttpURLConnection zzc(final URL url) throws IOException {
        final URLConnection openConnection = url.openConnection();
        if (!(openConnection instanceof HttpURLConnection)) {
            throw new IOException("Failed to obtain http connection");
        }
        final HttpURLConnection httpURLConnection = (HttpURLConnection)openConnection;
        httpURLConnection.setDefaultUseCaches(false);
        httpURLConnection.setConnectTimeout(this.zzhR().zzjz());
        httpURLConnection.setReadTimeout(this.zzhR().zzjA());
        httpURLConnection.setInstanceFollowRedirects(false);
        httpURLConnection.setRequestProperty("User-Agent", this.zzFP);
        httpURLConnection.setDoInput(true);
        return httpURLConnection;
    }
    
    public List<Long> zzf(final List<zzab> list) {
        boolean b = true;
        this.zzhO();
        this.zzia();
        zzu.zzu(list);
        int n;
        if (this.zzhR().zzjq().isEmpty() || !this.zzMr.zzv(this.zzhR().zzjj() * 1000L)) {
            b = false;
            n = 0;
        }
        else {
            boolean b2;
            if (this.zzhR().zzjo() != zzm.zzKz) {
                b2 = true;
            }
            else {
                b2 = false;
            }
            n = (b2 ? 1 : 0);
            if (this.zzhR().zzjp() != zzo.zzKL) {
                b = false;
                n = (b2 ? 1 : 0);
            }
        }
        if (n != 0) {
            return this.zza(list, b);
        }
        return this.zzg(list);
    }
    
    List<Long> zzg(final List<zzab> list) {
        final ArrayList<Long> list2 = new ArrayList<Long>(list.size());
        for (final zzab zzab : list) {
            if (!this.zzg(zzab)) {
                break;
            }
            list2.add(zzab.zzjV());
            if (list2.size() >= this.zzhR().zzjh()) {
                return list2;
            }
        }
        return list2;
    }
    
    @Override
    protected void zzhn() {
        this.zza("Network initialized. User agent", this.zzFP);
    }
    
    public boolean zzkg() {
        this.zzhO();
        this.zzia();
        final ConnectivityManager connectivityManager = (ConnectivityManager)this.getContext().getSystemService("connectivity");
        while (true) {
            try {
                final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
                    this.zzaT("No network connectivity");
                    return false;
                }
            }
            catch (SecurityException ex) {
                final NetworkInfo activeNetworkInfo = null;
                continue;
            }
            break;
        }
        return true;
    }
    
    private class zza
    {
        private int zzMt;
        private ByteArrayOutputStream zzMu;
        
        public zza() {
            this.zzMu = new ByteArrayOutputStream();
        }
        
        public byte[] getPayload() {
            return this.zzMu.toByteArray();
        }
        
        public boolean zzj(final zzab zzab) {
            zzu.zzu(zzab);
            if (this.zzMt + 1 > zzah.this.zzhR().zzji()) {
                return false;
            }
            final String zza = zzah.this.zza(zzab, false);
            if (zza == null) {
                zzah.this.zzhQ().zza(zzab, "Error formatting hit");
                return true;
            }
            final byte[] bytes = zza.getBytes();
            final int length = bytes.length;
            if (length > zzah.this.zzhR().zzja()) {
                zzah.this.zzhQ().zza(zzab, "Hit size exceeds the maximum size limit");
                return true;
            }
            int n = length;
            if (this.zzMu.size() > 0) {
                n = length + 1;
            }
            if (n + this.zzMu.size() > zzah.this.zzhR().zzjc()) {
                return false;
            }
            try {
                if (this.zzMu.size() > 0) {
                    this.zzMu.write(zzah.zzMs);
                }
                this.zzMu.write(bytes);
                ++this.zzMt;
                return true;
            }
            catch (IOException ex) {
                zzah.this.zze("Failed to write payload when batching hits", ex);
                return true;
            }
        }
        
        public int zzkj() {
            return this.zzMt;
        }
    }
}
