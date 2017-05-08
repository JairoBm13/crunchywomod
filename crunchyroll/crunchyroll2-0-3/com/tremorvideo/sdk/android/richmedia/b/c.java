// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia.b;

import com.tremorvideo.sdk.android.videoad.aw;
import android.widget.RelativeLayout$LayoutParams;
import com.tremorvideo.sdk.android.videoad.h;

public class c implements b
{
    private h a;
    private d b;
    private String c;
    
    public c(final h a, final d b, final String c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    @Override
    public void a() {
        this.a.r();
    }
    
    @Override
    public void a(final float n) {
        if (this.b != null) {
            this.b.a(n);
        }
    }
    
    @Override
    public void a(final RelativeLayout$LayoutParams relativeLayout$LayoutParams) {
        this.a.a(relativeLayout$LayoutParams);
    }
    
    @Override
    public void a(final aw.b b, final String s) {
        this.a.a(b, s);
    }
    
    @Override
    public void a(final String s) {
        this.a.b(s);
    }
    
    @Override
    public void a(final String p0, final String p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: getfield        com/tremorvideo/sdk/android/richmedia/b/c.b:Lcom/tremorvideo/sdk/android/richmedia/b/d;
        //     4: invokevirtual   com/tremorvideo/sdk/android/richmedia/b/d.clearView:()V
        //     7: new             Lorg/json/JSONObject;
        //    10: dup            
        //    11: aload_2        
        //    12: invokespecial   org/json/JSONObject.<init>:(Ljava/lang/String;)V
        //    15: astore_2       
        //    16: aload_2        
        //    17: ldc             "query_url"
        //    19: aload_0        
        //    20: getfield        com/tremorvideo/sdk/android/richmedia/b/c.c:Ljava/lang/String;
        //    23: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //    26: pop            
        //    27: aload_0        
        //    28: getfield        com/tremorvideo/sdk/android/richmedia/b/c.b:Lcom/tremorvideo/sdk/android/richmedia/b/d;
        //    31: aload_1        
        //    32: aload_2        
        //    33: invokevirtual   com/tremorvideo/sdk/android/richmedia/b/d.a:(Ljava/lang/String;Lorg/json/JSONObject;)V
        //    36: return         
        //    37: astore_3       
        //    38: aconst_null    
        //    39: astore_2       
        //    40: aload_3        
        //    41: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Ljava/lang/Throwable;)V
        //    44: goto            27
        //    47: astore_3       
        //    48: goto            40
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  7      16     37     40     Ljava/lang/Exception;
        //  16     27     47     51     Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0027:
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
    public void b() {
        this.a.s();
    }
    
    public void c() {
        if (this.b != null) {
            this.b.destroy();
            this.b = null;
            System.gc();
        }
    }
}
