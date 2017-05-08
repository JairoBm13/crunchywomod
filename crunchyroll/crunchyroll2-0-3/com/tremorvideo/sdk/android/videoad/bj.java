// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.content.Context;
import java.io.File;

public class bj extends bf
{
    n a;
    File b;
    Context c;
    boolean d;
    
    public bj(final a a, final Context c, final n a2) {
        super(a);
        this.a = a2;
        this.c = c;
        this.d = false;
    }
    
    private void h() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: iconst_0       
        //     1: istore_2       
        //     2: aload_0        
        //     3: getfield        com/tremorvideo/sdk/android/videoad/bj.a:Lcom/tremorvideo/sdk/android/videoad/n;
        //     6: invokevirtual   com/tremorvideo/sdk/android/videoad/n.s:()Lcom/tremorvideo/sdk/android/videoad/az;
        //     9: ifnonnull       20
        //    12: aload_0        
        //    13: getstatic       com/tremorvideo/sdk/android/videoad/bf$b.b:Lcom/tremorvideo/sdk/android/videoad/bf$b;
        //    16: invokevirtual   com/tremorvideo/sdk/android/videoad/bj.a:(Lcom/tremorvideo/sdk/android/videoad/bf$b;)V
        //    19: return         
        //    20: aconst_null    
        //    21: astore          7
        //    23: aconst_null    
        //    24: astore          8
        //    26: aconst_null    
        //    27: astore          6
        //    29: aload           6
        //    31: astore          4
        //    33: aload           7
        //    35: astore          5
        //    37: aload           8
        //    39: astore_3       
        //    40: aload_0        
        //    41: aload_0        
        //    42: getfield        com/tremorvideo/sdk/android/videoad/bj.c:Landroid/content/Context;
        //    45: invokevirtual   android/content/Context.getFilesDir:()Ljava/io/File;
        //    48: ldc             "survey-image"
        //    50: invokestatic    com/tremorvideo/sdk/android/videoad/x.a:(Ljava/io/File;Ljava/lang/String;)Ljava/io/File;
        //    53: putfield        com/tremorvideo/sdk/android/videoad/bj.b:Ljava/io/File;
        //    56: aload           6
        //    58: astore          4
        //    60: aload           7
        //    62: astore          5
        //    64: aload           8
        //    66: astore_3       
        //    67: aload_0        
        //    68: getfield        com/tremorvideo/sdk/android/videoad/bj.c:Landroid/content/Context;
        //    71: aload_0        
        //    72: getfield        com/tremorvideo/sdk/android/videoad/bj.b:Ljava/io/File;
        //    75: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //    78: ldc             32769
        //    80: invokevirtual   android/content/Context.openFileOutput:(Ljava/lang/String;I)Ljava/io/FileOutputStream;
        //    83: astore          6
        //    85: aload           6
        //    87: astore          4
        //    89: aload           6
        //    91: astore          5
        //    93: aload           6
        //    95: astore_3       
        //    96: new             Lorg/apache/http/params/BasicHttpParams;
        //    99: dup            
        //   100: invokespecial   org/apache/http/params/BasicHttpParams.<init>:()V
        //   103: astore          7
        //   105: aload           6
        //   107: astore          4
        //   109: aload           6
        //   111: astore          5
        //   113: aload           6
        //   115: astore_3       
        //   116: aload           7
        //   118: sipush          18000
        //   121: invokestatic    org/apache/http/params/HttpConnectionParams.setConnectionTimeout:(Lorg/apache/http/params/HttpParams;I)V
        //   124: aload           6
        //   126: astore          4
        //   128: aload           6
        //   130: astore          5
        //   132: aload           6
        //   134: astore_3       
        //   135: aload           7
        //   137: sipush          18000
        //   140: invokestatic    org/apache/http/params/HttpConnectionParams.setSoTimeout:(Lorg/apache/http/params/HttpParams;I)V
        //   143: aload           6
        //   145: astore          4
        //   147: aload           6
        //   149: astore          5
        //   151: aload           6
        //   153: astore_3       
        //   154: aload           7
        //   156: ldc             "http.protocol.expect-continue"
        //   158: iconst_0       
        //   159: invokeinterface org/apache/http/params/HttpParams.setBooleanParameter:(Ljava/lang/String;Z)Lorg/apache/http/params/HttpParams;
        //   164: pop            
        //   165: aload           6
        //   167: astore          4
        //   169: aload           6
        //   171: astore          5
        //   173: aload           6
        //   175: astore_3       
        //   176: aload           7
        //   178: ldc             "http.protocol.version"
        //   180: getstatic       org/apache/http/HttpVersion.HTTP_1_0:Lorg/apache/http/HttpVersion;
        //   183: invokeinterface org/apache/http/params/HttpParams.setParameter:(Ljava/lang/String;Ljava/lang/Object;)Lorg/apache/http/params/HttpParams;
        //   188: pop            
        //   189: aload           6
        //   191: astore          4
        //   193: aload           6
        //   195: astore          5
        //   197: aload           6
        //   199: astore_3       
        //   200: new             Lorg/apache/http/impl/client/DefaultHttpClient;
        //   203: dup            
        //   204: aload           7
        //   206: invokespecial   org/apache/http/impl/client/DefaultHttpClient.<init>:(Lorg/apache/http/params/HttpParams;)V
        //   209: astore          7
        //   211: aload           6
        //   213: astore          4
        //   215: aload           6
        //   217: astore          5
        //   219: aload           6
        //   221: astore_3       
        //   222: aload_0        
        //   223: getfield        com/tremorvideo/sdk/android/videoad/bj.a:Lcom/tremorvideo/sdk/android/videoad/n;
        //   226: invokevirtual   com/tremorvideo/sdk/android/videoad/n.s:()Lcom/tremorvideo/sdk/android/videoad/az;
        //   229: invokevirtual   com/tremorvideo/sdk/android/videoad/az.c:()Ljava/lang/String;
        //   232: astore          8
        //   234: aload           6
        //   236: astore          4
        //   238: aload           6
        //   240: astore          5
        //   242: aload           6
        //   244: astore_3       
        //   245: new             Lorg/apache/http/client/methods/HttpGet;
        //   248: dup            
        //   249: aload           8
        //   251: invokespecial   org/apache/http/client/methods/HttpGet.<init>:(Ljava/lang/String;)V
        //   254: astore          9
        //   256: aload           6
        //   258: astore          4
        //   260: aload           6
        //   262: astore          5
        //   264: aload           6
        //   266: astore_3       
        //   267: aload           9
        //   269: aload           8
        //   271: invokestatic    com/tremorvideo/sdk/android/richmedia/ae.a:(Lorg/apache/http/client/methods/HttpGet;Ljava/lang/String;)V
        //   274: aload           6
        //   276: astore          4
        //   278: aload           6
        //   280: astore          5
        //   282: aload           6
        //   284: astore_3       
        //   285: aload           7
        //   287: aload           9
        //   289: invokeinterface org/apache/http/client/HttpClient.execute:(Lorg/apache/http/client/methods/HttpUriRequest;)Lorg/apache/http/HttpResponse;
        //   294: invokeinterface org/apache/http/HttpResponse.getEntity:()Lorg/apache/http/HttpEntity;
        //   299: aload           6
        //   301: invokeinterface org/apache/http/HttpEntity.writeTo:(Ljava/io/OutputStream;)V
        //   306: aload           6
        //   308: astore          4
        //   310: aload           6
        //   312: astore          5
        //   314: aload           6
        //   316: astore_3       
        //   317: aload_0        
        //   318: iconst_1       
        //   319: putfield        com/tremorvideo/sdk/android/videoad/bj.d:Z
        //   322: iload_2        
        //   323: istore_1       
        //   324: aload           6
        //   326: ifnull          336
        //   329: aload           6
        //   331: invokevirtual   java/io/FileOutputStream.close:()V
        //   334: iload_2        
        //   335: istore_1       
        //   336: aload_0        
        //   337: getfield        com/tremorvideo/sdk/android/videoad/bj.d:Z
        //   340: ifne            444
        //   343: iload_1        
        //   344: ifeq            436
        //   347: aload_0        
        //   348: getstatic       com/tremorvideo/sdk/android/videoad/bf$b.f:Lcom/tremorvideo/sdk/android/videoad/bf$b;
        //   351: invokevirtual   com/tremorvideo/sdk/android/videoad/bj.a:(Lcom/tremorvideo/sdk/android/videoad/bf$b;)V
        //   354: return         
        //   355: astore          5
        //   357: aload           4
        //   359: astore_3       
        //   360: ldc             "Timeout Downloading Image: "
        //   362: aload           5
        //   364: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   367: aload           4
        //   369: ifnull          377
        //   372: aload           4
        //   374: invokevirtual   java/io/FileOutputStream.close:()V
        //   377: iconst_1       
        //   378: istore_1       
        //   379: goto            336
        //   382: astore_3       
        //   383: iconst_1       
        //   384: istore_1       
        //   385: goto            336
        //   388: astore          4
        //   390: aload           5
        //   392: astore_3       
        //   393: ldc             "Error Downloading Image: "
        //   395: aload           4
        //   397: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   400: iload_2        
        //   401: istore_1       
        //   402: aload           5
        //   404: ifnull          336
        //   407: aload           5
        //   409: invokevirtual   java/io/FileOutputStream.close:()V
        //   412: iload_2        
        //   413: istore_1       
        //   414: goto            336
        //   417: astore_3       
        //   418: iload_2        
        //   419: istore_1       
        //   420: goto            336
        //   423: astore          4
        //   425: aload_3        
        //   426: ifnull          433
        //   429: aload_3        
        //   430: invokevirtual   java/io/FileOutputStream.close:()V
        //   433: aload           4
        //   435: athrow         
        //   436: aload_0        
        //   437: getstatic       com/tremorvideo/sdk/android/videoad/bf$b.c:Lcom/tremorvideo/sdk/android/videoad/bf$b;
        //   440: invokevirtual   com/tremorvideo/sdk/android/videoad/bj.a:(Lcom/tremorvideo/sdk/android/videoad/bf$b;)V
        //   443: return         
        //   444: aload_0        
        //   445: getfield        com/tremorvideo/sdk/android/videoad/bj.a:Lcom/tremorvideo/sdk/android/videoad/n;
        //   448: invokevirtual   com/tremorvideo/sdk/android/videoad/n.s:()Lcom/tremorvideo/sdk/android/videoad/az;
        //   451: aload_0        
        //   452: getfield        com/tremorvideo/sdk/android/videoad/bj.b:Ljava/io/File;
        //   455: invokevirtual   com/tremorvideo/sdk/android/videoad/az.a:(Ljava/io/File;)V
        //   458: aload_0        
        //   459: getstatic       com/tremorvideo/sdk/android/videoad/bf$b.b:Lcom/tremorvideo/sdk/android/videoad/bf$b;
        //   462: invokevirtual   com/tremorvideo/sdk/android/videoad/bj.a:(Lcom/tremorvideo/sdk/android/videoad/bf$b;)V
        //   465: return         
        //   466: astore_3       
        //   467: iload_2        
        //   468: istore_1       
        //   469: goto            336
        //   472: astore_3       
        //   473: goto            433
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  40     56     355    388    Ljava/net/SocketTimeoutException;
        //  40     56     388    423    Ljava/lang/Exception;
        //  40     56     423    436    Any
        //  67     85     355    388    Ljava/net/SocketTimeoutException;
        //  67     85     388    423    Ljava/lang/Exception;
        //  67     85     423    436    Any
        //  96     105    355    388    Ljava/net/SocketTimeoutException;
        //  96     105    388    423    Ljava/lang/Exception;
        //  96     105    423    436    Any
        //  116    124    355    388    Ljava/net/SocketTimeoutException;
        //  116    124    388    423    Ljava/lang/Exception;
        //  116    124    423    436    Any
        //  135    143    355    388    Ljava/net/SocketTimeoutException;
        //  135    143    388    423    Ljava/lang/Exception;
        //  135    143    423    436    Any
        //  154    165    355    388    Ljava/net/SocketTimeoutException;
        //  154    165    388    423    Ljava/lang/Exception;
        //  154    165    423    436    Any
        //  176    189    355    388    Ljava/net/SocketTimeoutException;
        //  176    189    388    423    Ljava/lang/Exception;
        //  176    189    423    436    Any
        //  200    211    355    388    Ljava/net/SocketTimeoutException;
        //  200    211    388    423    Ljava/lang/Exception;
        //  200    211    423    436    Any
        //  222    234    355    388    Ljava/net/SocketTimeoutException;
        //  222    234    388    423    Ljava/lang/Exception;
        //  222    234    423    436    Any
        //  245    256    355    388    Ljava/net/SocketTimeoutException;
        //  245    256    388    423    Ljava/lang/Exception;
        //  245    256    423    436    Any
        //  267    274    355    388    Ljava/net/SocketTimeoutException;
        //  267    274    388    423    Ljava/lang/Exception;
        //  267    274    423    436    Any
        //  285    306    355    388    Ljava/net/SocketTimeoutException;
        //  285    306    388    423    Ljava/lang/Exception;
        //  285    306    423    436    Any
        //  317    322    355    388    Ljava/net/SocketTimeoutException;
        //  317    322    388    423    Ljava/lang/Exception;
        //  317    322    423    436    Any
        //  329    334    466    472    Ljava/lang/Exception;
        //  360    367    423    436    Any
        //  372    377    382    388    Ljava/lang/Exception;
        //  393    400    423    436    Any
        //  407    412    417    423    Ljava/lang/Exception;
        //  429    433    472    476    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 236, Size: 236
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
    
    @Override
    protected void e() {
        this.h();
    }
    
    @Override
    protected void f() {
    }
    
    @Override
    protected void g() {
        this.h();
    }
    
    @Override
    public String toString() {
        return "Download Survey";
    }
}
