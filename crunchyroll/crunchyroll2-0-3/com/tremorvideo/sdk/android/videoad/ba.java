// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import com.tremorvideo.a.c;
import com.tremorvideo.a.a;
import android.os.Bundle;
import android.content.Context;
import android.app.Activity;
import com.tremorvideo.a.b;

public class ba
{
    private static b a;
    
    public static void a(final Activity activity, final aw aw, final b.a a) {
        final b a2 = new b("503779313037583");
        b((Context)activity, ba.a = a2);
        a2.a(activity, new String[] { "publish_actions" }, -1, (b.a)new b.a() {
            @Override
            public void a() {
                b((Context)activity, a2);
                a.a();
                ba.a = null;
            }
            
            @Override
            public void a(final Bundle bundle) {
                final an a = an.a((Context)activity);
                a.setIndeterminate(true);
                a.setMessage((CharSequence)"Please Wait...");
                a.setCancelable(false);
                a.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 
                        // This method could not be decompiled.
                        // 
                        // Original Bytecode:
                        // 
                        //     0: aload_0        
                        //     1: getfield        com/tremorvideo/sdk/android/videoad/ba$1$1.c:Lcom/tremorvideo/sdk/android/videoad/ba$1;
                        //     4: getfield        com/tremorvideo/sdk/android/videoad/ba$1.d:Lcom/tremorvideo/sdk/android/videoad/aw;
                        //     7: invokevirtual   com/tremorvideo/sdk/android/videoad/aw.f:()Ljava/util/Dictionary;
                        //    10: astore_3       
                        //    11: new             Landroid/os/Bundle;
                        //    14: dup            
                        //    15: invokespecial   android/os/Bundle.<init>:()V
                        //    18: astore          4
                        //    20: aload_3        
                        //    21: ldc             "link"
                        //    23: invokevirtual   java/util/Dictionary.get:(Ljava/lang/Object;)Ljava/lang/Object;
                        //    26: ifnull          45
                        //    29: aload           4
                        //    31: ldc             "link"
                        //    33: aload_3        
                        //    34: ldc             "link"
                        //    36: invokevirtual   java/util/Dictionary.get:(Ljava/lang/Object;)Ljava/lang/Object;
                        //    39: checkcast       Ljava/lang/String;
                        //    42: invokevirtual   android/os/Bundle.putString:(Ljava/lang/String;Ljava/lang/String;)V
                        //    45: aload_3        
                        //    46: ldc             "description"
                        //    48: invokevirtual   java/util/Dictionary.get:(Ljava/lang/Object;)Ljava/lang/Object;
                        //    51: ifnull          70
                        //    54: aload           4
                        //    56: ldc             "description"
                        //    58: aload_3        
                        //    59: ldc             "description"
                        //    61: invokevirtual   java/util/Dictionary.get:(Ljava/lang/Object;)Ljava/lang/Object;
                        //    64: checkcast       Ljava/lang/String;
                        //    67: invokevirtual   android/os/Bundle.putString:(Ljava/lang/String;Ljava/lang/String;)V
                        //    70: aload_3        
                        //    71: ldc             "name"
                        //    73: invokevirtual   java/util/Dictionary.get:(Ljava/lang/Object;)Ljava/lang/Object;
                        //    76: ifnull          95
                        //    79: aload           4
                        //    81: ldc             "name"
                        //    83: aload_3        
                        //    84: ldc             "name"
                        //    86: invokevirtual   java/util/Dictionary.get:(Ljava/lang/Object;)Ljava/lang/Object;
                        //    89: checkcast       Ljava/lang/String;
                        //    92: invokevirtual   android/os/Bundle.putString:(Ljava/lang/String;Ljava/lang/String;)V
                        //    95: aload_3        
                        //    96: ldc             "message"
                        //    98: invokevirtual   java/util/Dictionary.get:(Ljava/lang/Object;)Ljava/lang/Object;
                        //   101: ifnull          120
                        //   104: aload           4
                        //   106: ldc             "message"
                        //   108: aload_3        
                        //   109: ldc             "message"
                        //   111: invokevirtual   java/util/Dictionary.get:(Ljava/lang/Object;)Ljava/lang/Object;
                        //   114: checkcast       Ljava/lang/String;
                        //   117: invokevirtual   android/os/Bundle.putString:(Ljava/lang/String;Ljava/lang/String;)V
                        //   120: aload_3        
                        //   121: ldc             "image"
                        //   123: invokevirtual   java/util/Dictionary.get:(Ljava/lang/Object;)Ljava/lang/Object;
                        //   126: ifnull          145
                        //   129: aload           4
                        //   131: ldc             "picture"
                        //   133: aload_3        
                        //   134: ldc             "image"
                        //   136: invokevirtual   java/util/Dictionary.get:(Ljava/lang/Object;)Ljava/lang/Object;
                        //   139: checkcast       Ljava/lang/String;
                        //   142: invokevirtual   android/os/Bundle.putString:(Ljava/lang/String;Ljava/lang/String;)V
                        //   145: aload_3        
                        //   146: ldc             "video"
                        //   148: invokevirtual   java/util/Dictionary.get:(Ljava/lang/Object;)Ljava/lang/Object;
                        //   151: ifnull          170
                        //   154: aload           4
                        //   156: ldc             "source"
                        //   158: aload_3        
                        //   159: ldc             "video"
                        //   161: invokevirtual   java/util/Dictionary.get:(Ljava/lang/Object;)Ljava/lang/Object;
                        //   164: checkcast       Ljava/lang/String;
                        //   167: invokevirtual   android/os/Bundle.putString:(Ljava/lang/String;Ljava/lang/String;)V
                        //   170: aload           4
                        //   172: ldc             "access_token"
                        //   174: aload_0        
                        //   175: getfield        com/tremorvideo/sdk/android/videoad/ba$1$1.c:Lcom/tremorvideo/sdk/android/videoad/ba$1;
                        //   178: getfield        com/tremorvideo/sdk/android/videoad/ba$1.b:Lcom/tremorvideo/a/b;
                        //   181: invokevirtual   com/tremorvideo/a/b.b:()Ljava/lang/String;
                        //   184: invokevirtual   android/os/Bundle.putSerializable:(Ljava/lang/String;Ljava/io/Serializable;)V
                        //   187: aload_0        
                        //   188: getfield        com/tremorvideo/sdk/android/videoad/ba$1$1.c:Lcom/tremorvideo/sdk/android/videoad/ba$1;
                        //   191: getfield        com/tremorvideo/sdk/android/videoad/ba$1.b:Lcom/tremorvideo/a/b;
                        //   194: ldc             "feed"
                        //   196: aload           4
                        //   198: ldc             "POST"
                        //   200: invokevirtual   com/tremorvideo/a/b.a:(Ljava/lang/String;Landroid/os/Bundle;Ljava/lang/String;)Ljava/lang/String;
                        //   203: pop            
                        //   204: iconst_1       
                        //   205: istore_2       
                        //   206: iconst_1       
                        //   207: istore_1       
                        //   208: aload_0        
                        //   209: getfield        com/tremorvideo/sdk/android/videoad/ba$1$1.c:Lcom/tremorvideo/sdk/android/videoad/ba$1;
                        //   212: getfield        com/tremorvideo/sdk/android/videoad/ba$1.b:Lcom/tremorvideo/a/b;
                        //   215: aload_0        
                        //   216: getfield        com/tremorvideo/sdk/android/videoad/ba$1$1.c:Lcom/tremorvideo/sdk/android/videoad/ba$1;
                        //   219: getfield        com/tremorvideo/sdk/android/videoad/ba$1.a:Landroid/app/Activity;
                        //   222: invokevirtual   com/tremorvideo/a/b.a:(Landroid/content/Context;)Ljava/lang/String;
                        //   225: pop            
                        //   226: aload_0        
                        //   227: getfield        com/tremorvideo/sdk/android/videoad/ba$1$1.a:Lcom/tremorvideo/sdk/android/videoad/an;
                        //   230: invokevirtual   com/tremorvideo/sdk/android/videoad/an.dismiss:()V
                        //   233: iload_1        
                        //   234: ifeq            269
                        //   237: aload_0        
                        //   238: getfield        com/tremorvideo/sdk/android/videoad/ba$1$1.c:Lcom/tremorvideo/sdk/android/videoad/ba$1;
                        //   241: getfield        com/tremorvideo/sdk/android/videoad/ba$1.c:Lcom/tremorvideo/a/b$a;
                        //   244: aload_0        
                        //   245: getfield        com/tremorvideo/sdk/android/videoad/ba$1$1.b:Landroid/os/Bundle;
                        //   248: invokeinterface com/tremorvideo/a/b$a.a:(Landroid/os/Bundle;)V
                        //   253: aconst_null    
                        //   254: invokestatic    com/tremorvideo/sdk/android/videoad/ba.a:(Lcom/tremorvideo/a/b;)Lcom/tremorvideo/a/b;
                        //   257: pop            
                        //   258: return         
                        //   259: astore_3       
                        //   260: iconst_0       
                        //   261: istore_1       
                        //   262: aload_3        
                        //   263: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Ljava/lang/Throwable;)V
                        //   266: goto            226
                        //   269: aload_0        
                        //   270: getfield        com/tremorvideo/sdk/android/videoad/ba$1$1.c:Lcom/tremorvideo/sdk/android/videoad/ba$1;
                        //   273: getfield        com/tremorvideo/sdk/android/videoad/ba$1.c:Lcom/tremorvideo/a/b$a;
                        //   276: new             Lcom/tremorvideo/a/c;
                        //   279: dup            
                        //   280: ldc             "A connection error occured."
                        //   282: invokespecial   com/tremorvideo/a/c.<init>:(Ljava/lang/String;)V
                        //   285: invokeinterface com/tremorvideo/a/b$a.a:(Lcom/tremorvideo/a/c;)V
                        //   290: goto            253
                        //   293: astore_3       
                        //   294: iload_2        
                        //   295: istore_1       
                        //   296: goto            262
                        //    Exceptions:
                        //  Try           Handler
                        //  Start  End    Start  End    Type                 
                        //  -----  -----  -----  -----  ---------------------
                        //  0      45     259    262    Ljava/lang/Exception;
                        //  45     70     259    262    Ljava/lang/Exception;
                        //  70     95     259    262    Ljava/lang/Exception;
                        //  95     120    259    262    Ljava/lang/Exception;
                        //  120    145    259    262    Ljava/lang/Exception;
                        //  145    170    259    262    Ljava/lang/Exception;
                        //  170    204    259    262    Ljava/lang/Exception;
                        //  208    226    293    299    Ljava/lang/Exception;
                        // 
                        // The error that occurred was:
                        // 
                        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0226:
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
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1163)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1010)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1163)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1010)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
                }).start();
            }
            
            @Override
            public void a(final com.tremorvideo.a.a a) {
                if (ba.a != null) {
                    b((Context)activity, a2);
                    a.a(a);
                    ba.a = null;
                }
            }
            
            @Override
            public void a(final c c) {
                b((Context)activity, a2);
                a.a(c);
                ba.a = null;
            }
        });
    }
    
    private static void b(final Context context, final b b) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    b.a(context);
                }
                catch (Exception ex) {
                    ac.a(ex);
                }
            }
        }).start();
    }
}
