// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.a;

import java.io.File;
import dalvik.system.DexClassLoader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import android.content.Context;
import java.lang.reflect.Method;

public abstract class e extends d
{
    static boolean d;
    private static Method e;
    private static Method f;
    private static Method g;
    private static Method h;
    private static Method i;
    private static Method j;
    private static Method k;
    private static Method l;
    private static String m;
    private static long n;
    private static k o;
    
    static {
        com.google.android.a.e.n = 0L;
        com.google.android.a.e.d = false;
    }
    
    protected e(final Context context, final i i, final j j) {
        super(context, i, j);
    }
    
    static String a() throws a {
        if (com.google.android.a.e.m == null) {
            throw new a();
        }
        return com.google.android.a.e.m;
    }
    
    static String a(final Context context, final i i) throws a {
        if (com.google.android.a.e.h == null) {
            throw new a();
        }
        try {
            if (com.google.android.a.e.h.invoke(null, context) == null) {
                throw new a();
            }
            goto Label_0055;
        }
        catch (IllegalAccessException ex) {
            throw new a(ex);
        }
        catch (InvocationTargetException ex2) {
            throw new a(ex2);
        }
    }
    
    static ArrayList<Long> a(final MotionEvent motionEvent, final DisplayMetrics displayMetrics) throws a {
        if (com.google.android.a.e.i == null || motionEvent == null) {
            throw new a();
        }
        try {
            return (ArrayList<Long>)com.google.android.a.e.i.invoke(null, motionEvent, displayMetrics);
        }
        catch (IllegalAccessException ex) {
            throw new a(ex);
        }
        catch (InvocationTargetException ex2) {
            throw new a(ex2);
        }
    }
    
    protected static void a(final String p0, final Context p1, final i p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: ldc             Lcom/google/android/a/e;.class
        //     2: monitorenter   
        //     3: getstatic       com/google/android/a/e.d:Z
        //     6: istore_3       
        //     7: iload_3        
        //     8: ifne            44
        //    11: new             Lcom/google/android/a/k;
        //    14: dup            
        //    15: aload_2        
        //    16: aconst_null    
        //    17: invokespecial   com/google/android/a/k.<init>:(Lcom/google/android/a/i;Ljava/security/SecureRandom;)V
        //    20: putstatic       com/google/android/a/e.o:Lcom/google/android/a/k;
        //    23: aload_0        
        //    24: putstatic       com/google/android/a/e.m:Ljava/lang/String;
        //    27: aload_1        
        //    28: invokestatic    com/google/android/a/e.f:(Landroid/content/Context;)V
        //    31: invokestatic    com/google/android/a/e.b:()Ljava/lang/Long;
        //    34: invokevirtual   java/lang/Long.longValue:()J
        //    37: putstatic       com/google/android/a/e.n:J
        //    40: iconst_1       
        //    41: putstatic       com/google/android/a/e.d:Z
        //    44: ldc             Lcom/google/android/a/e;.class
        //    46: monitorexit    
        //    47: return         
        //    48: astore_0       
        //    49: ldc             Lcom/google/android/a/e;.class
        //    51: monitorexit    
        //    52: aload_0        
        //    53: athrow         
        //    54: astore_0       
        //    55: goto            44
        //    58: astore_0       
        //    59: goto            44
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  3      7      48     54     Any
        //  11     44     58     62     Lcom/google/android/a/e$a;
        //  11     44     54     58     Ljava/lang/UnsupportedOperationException;
        //  11     44     48     54     Any
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
    
    static Long b() throws a {
        if (com.google.android.a.e.e == null) {
            throw new a();
        }
        try {
            return (Long)com.google.android.a.e.e.invoke(null, new Object[0]);
        }
        catch (IllegalAccessException ex) {
            throw new a(ex);
        }
        catch (InvocationTargetException ex2) {
            throw new a(ex2);
        }
    }
    
    static String b(final Context context, final i i) throws a {
        if (com.google.android.a.e.k == null) {
            throw new a();
        }
        try {
            if (com.google.android.a.e.k.invoke(null, context) == null) {
                throw new a();
            }
            goto Label_0055;
        }
        catch (IllegalAccessException ex) {
            throw new a(ex);
        }
        catch (InvocationTargetException ex2) {
            throw new a(ex2);
        }
    }
    
    private static String b(final byte[] array, final String s) throws a {
        try {
            return new String(com.google.android.a.e.o.a(array, s), "UTF-8");
        }
        catch (k.a a) {
            throw new a(a);
        }
        catch (UnsupportedEncodingException ex) {
            throw new a(ex);
        }
    }
    
    static String c() throws a {
        if (com.google.android.a.e.g == null) {
            throw new a();
        }
        try {
            return (String)com.google.android.a.e.g.invoke(null, new Object[0]);
        }
        catch (IllegalAccessException ex) {
            throw new a(ex);
        }
        catch (InvocationTargetException ex2) {
            throw new a(ex2);
        }
    }
    
    static Long d() throws a {
        if (com.google.android.a.e.f == null) {
            throw new a();
        }
        try {
            return (Long)com.google.android.a.e.f.invoke(null, new Object[0]);
        }
        catch (IllegalAccessException ex) {
            throw new a(ex);
        }
        catch (InvocationTargetException ex2) {
            throw new a(ex2);
        }
    }
    
    static String d(final Context context) throws a {
        if (com.google.android.a.e.j == null) {
            throw new a();
        }
        String s;
        try {
            s = (String)com.google.android.a.e.j.invoke(null, context);
            if (s == null) {
                throw new a();
            }
        }
        catch (IllegalAccessException ex) {
            throw new a(ex);
        }
        catch (InvocationTargetException ex2) {
            throw new a(ex2);
        }
        return s;
    }
    
    static ArrayList<Long> e(final Context context) throws a {
        if (com.google.android.a.e.l == null) {
            throw new a();
        }
        ArrayList list;
        try {
            list = (ArrayList)com.google.android.a.e.l.invoke(null, context);
            if (list == null || list.size() != 2) {
                throw new a();
            }
        }
        catch (IllegalAccessException ex) {
            throw new a(ex);
        }
        catch (InvocationTargetException ex2) {
            throw new a(ex2);
        }
        return (ArrayList<Long>)list;
    }
    
    private static void f(final Context context) throws a {
        byte[] a;
        File file;
        try {
            a = com.google.android.a.e.o.a(com.google.android.a.m.a());
            com.google.android.a.e.o.a(a, com.google.android.a.m.b());
            if ((file = context.getCacheDir()) == null && (file = context.getDir("dex", 0)) == null) {
                throw new a();
            }
            goto Label_0065;
        }
        catch (FileNotFoundException ex) {
            throw new a(ex);
        }
        catch (IOException ex2) {
            throw new a(ex2);
        }
        catch (ClassNotFoundException ex3) {
            throw new a(ex3);
        }
        catch (k.a a2) {
            throw new a(a2);
        }
        catch (NoSuchMethodException ex4) {
            throw new a(ex4);
        }
        catch (NullPointerException ex5) {
            throw new a(ex5);
        }
        try {
            final File file2;
            final DexClassLoader dexClassLoader = new DexClassLoader(file2.getAbsolutePath(), file.getAbsolutePath(), (String)null, context.getClassLoader());
            final Class loadClass = dexClassLoader.loadClass(b(a, com.google.android.a.m.c()));
            final Class loadClass2 = dexClassLoader.loadClass(b(a, com.google.android.a.m.o()));
            final Class loadClass3 = dexClassLoader.loadClass(b(a, com.google.android.a.m.i()));
            final Class loadClass4 = dexClassLoader.loadClass(b(a, com.google.android.a.m.g()));
            final Class loadClass5 = dexClassLoader.loadClass(b(a, com.google.android.a.m.q()));
            final Class loadClass6 = dexClassLoader.loadClass(b(a, com.google.android.a.m.e()));
            final Class loadClass7 = dexClassLoader.loadClass(b(a, com.google.android.a.m.m()));
            final Class loadClass8 = dexClassLoader.loadClass(b(a, com.google.android.a.m.k()));
            com.google.android.a.e.e = loadClass.getMethod(b(a, com.google.android.a.m.d()), (Class[])new Class[0]);
            com.google.android.a.e.f = loadClass2.getMethod(b(a, com.google.android.a.m.p()), (Class[])new Class[0]);
            com.google.android.a.e.g = loadClass3.getMethod(b(a, com.google.android.a.m.j()), (Class[])new Class[0]);
            com.google.android.a.e.h = loadClass4.getMethod(b(a, com.google.android.a.m.h()), Context.class);
            com.google.android.a.e.i = loadClass5.getMethod(b(a, com.google.android.a.m.r()), MotionEvent.class, DisplayMetrics.class);
            com.google.android.a.e.j = loadClass6.getMethod(b(a, com.google.android.a.m.f()), Context.class);
            com.google.android.a.e.k = loadClass7.getMethod(b(a, com.google.android.a.m.n()), Context.class);
            com.google.android.a.e.l = loadClass8.getMethod(b(a, com.google.android.a.m.l()), Context.class);
        }
        finally {}
    }
    
    @Override
    protected void b(final Context p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: iconst_1       
        //     2: invokestatic    com/google/android/a/e.c:()Ljava/lang/String;
        //     5: invokevirtual   com/google/android/a/e.a:(ILjava/lang/String;)V
        //     8: aload_0        
        //     9: iconst_2       
        //    10: invokestatic    com/google/android/a/e.a:()Ljava/lang/String;
        //    13: invokevirtual   com/google/android/a/e.a:(ILjava/lang/String;)V
        //    16: aload_0        
        //    17: bipush          25
        //    19: invokestatic    com/google/android/a/e.b:()Ljava/lang/Long;
        //    22: invokevirtual   java/lang/Long.longValue:()J
        //    25: invokevirtual   com/google/android/a/e.a:(IJ)V
        //    28: aload_0        
        //    29: bipush          24
        //    31: aload_1        
        //    32: invokestatic    com/google/android/a/e.d:(Landroid/content/Context;)Ljava/lang/String;
        //    35: invokevirtual   com/google/android/a/e.a:(ILjava/lang/String;)V
        //    38: aload_1        
        //    39: invokestatic    com/google/android/a/e.e:(Landroid/content/Context;)Ljava/util/ArrayList;
        //    42: astore_1       
        //    43: aload_0        
        //    44: bipush          31
        //    46: aload_1        
        //    47: iconst_0       
        //    48: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //    51: checkcast       Ljava/lang/Long;
        //    54: invokevirtual   java/lang/Long.longValue:()J
        //    57: invokevirtual   com/google/android/a/e.a:(IJ)V
        //    60: aload_0        
        //    61: bipush          32
        //    63: aload_1        
        //    64: iconst_1       
        //    65: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //    68: checkcast       Ljava/lang/Long;
        //    71: invokevirtual   java/lang/Long.longValue:()J
        //    74: invokevirtual   com/google/android/a/e.a:(IJ)V
        //    77: aload_0        
        //    78: bipush          33
        //    80: invokestatic    com/google/android/a/e.d:()Ljava/lang/Long;
        //    83: invokevirtual   java/lang/Long.longValue:()J
        //    86: invokevirtual   com/google/android/a/e.a:(IJ)V
        //    89: return         
        //    90: astore_1       
        //    91: return         
        //    92: astore_1       
        //    93: return         
        //    94: astore_1       
        //    95: goto            77
        //    98: astore_2       
        //    99: goto            38
        //   102: astore_2       
        //   103: goto            28
        //   106: astore_2       
        //   107: goto            16
        //   110: astore_2       
        //   111: goto            8
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                      
        //  -----  -----  -----  -----  --------------------------
        //  0      8      110    114    Lcom/google/android/a/e$a;
        //  0      8      90     92     Ljava/io/IOException;
        //  8      16     106    110    Lcom/google/android/a/e$a;
        //  8      16     90     92     Ljava/io/IOException;
        //  16     28     102    106    Lcom/google/android/a/e$a;
        //  16     28     90     92     Ljava/io/IOException;
        //  28     38     98     102    Lcom/google/android/a/e$a;
        //  28     38     90     92     Ljava/io/IOException;
        //  38     77     94     98     Lcom/google/android/a/e$a;
        //  38     77     90     92     Ljava/io/IOException;
        //  77     89     92     94     Lcom/google/android/a/e$a;
        //  77     89     90     92     Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0008:
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
    protected void c(final Context p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: iconst_2       
        //     2: invokestatic    com/google/android/a/e.a:()Ljava/lang/String;
        //     5: invokevirtual   com/google/android/a/e.a:(ILjava/lang/String;)V
        //     8: aload_0        
        //     9: iconst_1       
        //    10: invokestatic    com/google/android/a/e.c:()Ljava/lang/String;
        //    13: invokevirtual   com/google/android/a/e.a:(ILjava/lang/String;)V
        //    16: invokestatic    com/google/android/a/e.b:()Ljava/lang/Long;
        //    19: invokevirtual   java/lang/Long.longValue:()J
        //    22: lstore_2       
        //    23: aload_0        
        //    24: bipush          25
        //    26: lload_2        
        //    27: invokevirtual   com/google/android/a/e.a:(IJ)V
        //    30: getstatic       com/google/android/a/e.n:J
        //    33: lconst_0       
        //    34: lcmp           
        //    35: ifeq            58
        //    38: aload_0        
        //    39: bipush          17
        //    41: lload_2        
        //    42: getstatic       com/google/android/a/e.n:J
        //    45: lsub           
        //    46: invokevirtual   com/google/android/a/e.a:(IJ)V
        //    49: aload_0        
        //    50: bipush          23
        //    52: getstatic       com/google/android/a/e.n:J
        //    55: invokevirtual   com/google/android/a/e.a:(IJ)V
        //    58: aload_0        
        //    59: getfield        com/google/android/a/e.a:Landroid/view/MotionEvent;
        //    62: aload_0        
        //    63: getfield        com/google/android/a/e.b:Landroid/util/DisplayMetrics;
        //    66: invokestatic    com/google/android/a/e.a:(Landroid/view/MotionEvent;Landroid/util/DisplayMetrics;)Ljava/util/ArrayList;
        //    69: astore          4
        //    71: aload_0        
        //    72: bipush          14
        //    74: aload           4
        //    76: iconst_0       
        //    77: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //    80: checkcast       Ljava/lang/Long;
        //    83: invokevirtual   java/lang/Long.longValue:()J
        //    86: invokevirtual   com/google/android/a/e.a:(IJ)V
        //    89: aload_0        
        //    90: bipush          15
        //    92: aload           4
        //    94: iconst_1       
        //    95: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //    98: checkcast       Ljava/lang/Long;
        //   101: invokevirtual   java/lang/Long.longValue:()J
        //   104: invokevirtual   com/google/android/a/e.a:(IJ)V
        //   107: aload           4
        //   109: invokevirtual   java/util/ArrayList.size:()I
        //   112: iconst_3       
        //   113: if_icmplt       134
        //   116: aload_0        
        //   117: bipush          16
        //   119: aload           4
        //   121: iconst_2       
        //   122: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //   125: checkcast       Ljava/lang/Long;
        //   128: invokevirtual   java/lang/Long.longValue:()J
        //   131: invokevirtual   com/google/android/a/e.a:(IJ)V
        //   134: aload_0        
        //   135: bipush          27
        //   137: aload_1        
        //   138: aload_0        
        //   139: getfield        com/google/android/a/e.c:Lcom/google/android/a/i;
        //   142: invokestatic    com/google/android/a/e.a:(Landroid/content/Context;Lcom/google/android/a/i;)Ljava/lang/String;
        //   145: invokevirtual   com/google/android/a/e.a:(ILjava/lang/String;)V
        //   148: aload_0        
        //   149: bipush          29
        //   151: aload_1        
        //   152: aload_0        
        //   153: getfield        com/google/android/a/e.c:Lcom/google/android/a/i;
        //   156: invokestatic    com/google/android/a/e.b:(Landroid/content/Context;Lcom/google/android/a/i;)Ljava/lang/String;
        //   159: invokevirtual   com/google/android/a/e.a:(ILjava/lang/String;)V
        //   162: return         
        //   163: astore_1       
        //   164: return         
        //   165: astore_1       
        //   166: return         
        //   167: astore          4
        //   169: goto            148
        //   172: astore          4
        //   174: goto            134
        //   177: astore          4
        //   179: goto            58
        //   182: astore          4
        //   184: goto            16
        //   187: astore          4
        //   189: goto            8
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                      
        //  -----  -----  -----  -----  --------------------------
        //  0      8      187    192    Lcom/google/android/a/e$a;
        //  0      8      163    165    Ljava/io/IOException;
        //  8      16     182    187    Lcom/google/android/a/e$a;
        //  8      16     163    165    Ljava/io/IOException;
        //  16     58     177    182    Lcom/google/android/a/e$a;
        //  16     58     163    165    Ljava/io/IOException;
        //  58     134    172    177    Lcom/google/android/a/e$a;
        //  58     134    163    165    Ljava/io/IOException;
        //  134    148    167    172    Lcom/google/android/a/e$a;
        //  134    148    163    165    Ljava/io/IOException;
        //  148    162    165    167    Lcom/google/android/a/e$a;
        //  148    162    163    165    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0008:
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
    
    static class a extends Exception
    {
        public a() {
        }
        
        public a(final Throwable t) {
            super(t);
        }
    }
}
