// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import java.util.Map;
import android.content.Context;
import com.tremorvideo.sdk.android.videoad.a.a;

public class bk extends bf
{
    String a;
    boolean b;
    com.tremorvideo.sdk.android.videoad.a.a c;
    boolean d;
    
    public bk(final a a, final Context context, final Map<String, Object> map) {
        super(a);
        this.d = false;
        this.b = false;
        this.a = map.get("url");
        this.c = null;
    }
    
    private void h() {
        this.d = false;
        this.i();
        if (this.c != null) {
            this.a(bf.b.b);
            return;
        }
        if (this.d) {
            this.a(bf.b.f);
            return;
        }
        this.a(bf.b.c);
    }
    
    private void i() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: getfield        com/tremorvideo/sdk/android/videoad/bk.a:Ljava/lang/String;
        //     4: ldc             "|"
        //     6: ldc             "%7C"
        //     8: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //    11: astore          5
        //    13: aload           5
        //    15: astore          4
        //    17: aload           5
        //    19: astore_3       
        //    20: aload           5
        //    22: ldc             " "
        //    24: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //    27: ifeq            44
        //    30: aload           5
        //    32: astore_3       
        //    33: aload           5
        //    35: ldc             " "
        //    37: ldc             "%20"
        //    39: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //    42: astore          4
        //    44: aload           4
        //    46: astore_3       
        //    47: aload           4
        //    49: aload           4
        //    51: invokestatic    com/tremorvideo/sdk/android/richmedia/ae.c:(Ljava/lang/String;)Ljava/lang/String;
        //    54: invokestatic    com/tremorvideo/sdk/android/videoad/bb.a:(Ljava/lang/String;Ljava/lang/String;)Lcom/tremorvideo/sdk/android/videoad/bb;
        //    57: astore          5
        //    59: aload           4
        //    61: astore_3       
        //    62: aload           5
        //    64: invokevirtual   com/tremorvideo/sdk/android/videoad/bb.a:()V
        //    67: aload           4
        //    69: astore_3       
        //    70: aload           5
        //    72: invokevirtual   com/tremorvideo/sdk/android/videoad/bb.b:()Ljava/lang/String;
        //    75: astore          5
        //    77: aload           4
        //    79: astore_3       
        //    80: aload_0        
        //    81: new             Lcom/tremorvideo/sdk/android/videoad/a/a;
        //    84: dup            
        //    85: aload           5
        //    87: invokespecial   com/tremorvideo/sdk/android/videoad/a/a.<init>:(Ljava/lang/String;)V
        //    90: putfield        com/tremorvideo/sdk/android/videoad/bk.c:Lcom/tremorvideo/sdk/android/videoad/a/a;
        //    93: aload           5
        //    95: ldc             ""
        //    97: if_acmpeq       229
        //   100: getstatic       com/tremorvideo/sdk/android/videoad/ac.r:Z
        //   103: ifeq            136
        //   106: invokestatic    com/tremorvideo/sdk/android/logger/TestAppLogger.getInstance:()Lcom/tremorvideo/sdk/android/logger/TestAppLogger;
        //   109: ldc             "VastTag download and parse"
        //   111: new             Ljava/lang/StringBuilder;
        //   114: dup            
        //   115: invokespecial   java/lang/StringBuilder.<init>:()V
        //   118: ldc             "vasttag_url="
        //   120: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   123: aload           4
        //   125: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   128: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   131: ldc             "pass"
        //   133: invokevirtual   com/tremorvideo/sdk/android/logger/TestAppLogger.logVastTag:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
        //   136: aload           4
        //   138: astore_3       
        //   139: aload           5
        //   141: ldc             "\n"
        //   143: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //   146: astore          5
        //   148: aload           4
        //   150: astore_3       
        //   151: aload           5
        //   153: arraylength    
        //   154: istore_2       
        //   155: iconst_0       
        //   156: istore_1       
        //   157: iload_1        
        //   158: iload_2        
        //   159: if_icmpge       229
        //   162: aload           5
        //   164: iload_1        
        //   165: aaload         
        //   166: astore          6
        //   168: aload           4
        //   170: astore_3       
        //   171: getstatic       com/tremorvideo/sdk/android/videoad/ac$c.c:Lcom/tremorvideo/sdk/android/videoad/ac$c;
        //   174: aload           6
        //   176: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Lcom/tremorvideo/sdk/android/videoad/ac$c;Ljava/lang/String;)V
        //   179: iload_1        
        //   180: iconst_1       
        //   181: iadd           
        //   182: istore_1       
        //   183: goto            157
        //   186: astore          6
        //   188: aload           4
        //   190: astore_3       
        //   191: new             Ljava/lang/StringBuilder;
        //   194: dup            
        //   195: invokespecial   java/lang/StringBuilder.<init>:()V
        //   198: ldc             "Error logVastTag "
        //   200: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   203: aload           6
        //   205: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   208: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   211: invokestatic    com/tremorvideo/sdk/android/videoad/ac.e:(Ljava/lang/String;)V
        //   214: goto            136
        //   217: astore_3       
        //   218: aload_0        
        //   219: iconst_1       
        //   220: putfield        com/tremorvideo/sdk/android/videoad/bk.d:Z
        //   223: ldc             "Timeout Downloading VAST tag: "
        //   225: aload_3        
        //   226: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   229: return         
        //   230: astore          4
        //   232: aconst_null    
        //   233: astore_3       
        //   234: aload_0        
        //   235: aconst_null    
        //   236: putfield        com/tremorvideo/sdk/android/videoad/bk.c:Lcom/tremorvideo/sdk/android/videoad/a/a;
        //   239: aload           4
        //   241: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Ljava/lang/Throwable;)V
        //   244: getstatic       com/tremorvideo/sdk/android/videoad/ac.r:Z
        //   247: ifeq            229
        //   250: invokestatic    com/tremorvideo/sdk/android/logger/TestAppLogger.getInstance:()Lcom/tremorvideo/sdk/android/logger/TestAppLogger;
        //   253: ldc             "VastTag download and parse"
        //   255: new             Ljava/lang/StringBuilder;
        //   258: dup            
        //   259: invokespecial   java/lang/StringBuilder.<init>:()V
        //   262: ldc             "vasttag_url="
        //   264: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   267: aload_3        
        //   268: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   271: ldc             ", Exception="
        //   273: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   276: aload           4
        //   278: invokevirtual   java/lang/Exception.getMessage:()Ljava/lang/String;
        //   281: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   284: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   287: ldc             "fail"
        //   289: invokevirtual   com/tremorvideo/sdk/android/logger/TestAppLogger.logVastTag:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
        //   292: return         
        //   293: astore_3       
        //   294: new             Ljava/lang/StringBuilder;
        //   297: dup            
        //   298: invokespecial   java/lang/StringBuilder.<init>:()V
        //   301: ldc             "Error logVastTag "
        //   303: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   306: aload_3        
        //   307: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   310: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   313: invokestatic    com/tremorvideo/sdk/android/videoad/ac.e:(Ljava/lang/String;)V
        //   316: return         
        //   317: astore          4
        //   319: goto            234
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  0      13     217    229    Ljava/net/SocketTimeoutException;
        //  0      13     230    234    Ljava/lang/Exception;
        //  20     30     217    229    Ljava/net/SocketTimeoutException;
        //  20     30     317    322    Ljava/lang/Exception;
        //  33     44     217    229    Ljava/net/SocketTimeoutException;
        //  33     44     317    322    Ljava/lang/Exception;
        //  47     59     217    229    Ljava/net/SocketTimeoutException;
        //  47     59     317    322    Ljava/lang/Exception;
        //  62     67     217    229    Ljava/net/SocketTimeoutException;
        //  62     67     317    322    Ljava/lang/Exception;
        //  70     77     217    229    Ljava/net/SocketTimeoutException;
        //  70     77     317    322    Ljava/lang/Exception;
        //  80     93     217    229    Ljava/net/SocketTimeoutException;
        //  80     93     317    322    Ljava/lang/Exception;
        //  100    136    186    217    Ljava/lang/Exception;
        //  100    136    217    229    Ljava/net/SocketTimeoutException;
        //  139    148    217    229    Ljava/net/SocketTimeoutException;
        //  139    148    317    322    Ljava/lang/Exception;
        //  151    155    217    229    Ljava/net/SocketTimeoutException;
        //  151    155    317    322    Ljava/lang/Exception;
        //  171    179    217    229    Ljava/net/SocketTimeoutException;
        //  171    179    317    322    Ljava/lang/Exception;
        //  191    214    217    229    Ljava/net/SocketTimeoutException;
        //  191    214    317    322    Ljava/lang/Exception;
        //  244    292    293    317    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0044:
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
    
    @Override
    public void a(final String s, final n n) throws Exception {
        n.a(s, this.c);
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
        return "Download VAST";
    }
}
