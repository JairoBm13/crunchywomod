// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.tagmanager;

import android.os.Build$VERSION;
import java.util.Set;
import java.util.HashSet;
import android.database.sqlite.SQLiteDatabase$CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.Arrays;
import android.text.TextUtils;
import java.util.Collections;
import java.util.Iterator;
import android.content.ContentValues;
import java.util.List;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import java.util.concurrent.Executors;
import com.google.android.gms.internal.zzld;
import com.google.android.gms.internal.zzlb;
import java.util.concurrent.Executor;
import android.content.Context;

class zzw implements zzc
{
    private static final String zzaLp;
    private final Context mContext;
    private final Executor zzaLq;
    private zza zzaLr;
    private int zzaLs;
    private zzlb zzpw;
    
    static {
        zzaLp = String.format("CREATE TABLE IF NOT EXISTS %s ( '%s' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, '%s' STRING NOT NULL, '%s' BLOB NOT NULL, '%s' INTEGER NOT NULL);", "datalayer", "ID", "key", "value", "expires");
    }
    
    public zzw(final Context context) {
        this(context, zzld.zzoQ(), "google_tagmanager.db", 2000, Executors.newSingleThreadExecutor());
    }
    
    zzw(final Context mContext, final zzlb zzpw, final String s, final int zzaLs, final Executor zzaLq) {
        this.mContext = mContext;
        this.zzpw = zzpw;
        this.zzaLs = zzaLs;
        this.zzaLq = zzaLq;
        this.zzaLr = new zza(this.mContext, s);
    }
    
    private byte[] zzA(final Object p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: new             Ljava/io/ByteArrayOutputStream;
        //     3: dup            
        //     4: invokespecial   java/io/ByteArrayOutputStream.<init>:()V
        //     7: astore_3       
        //     8: new             Ljava/io/ObjectOutputStream;
        //    11: dup            
        //    12: aload_3        
        //    13: invokespecial   java/io/ObjectOutputStream.<init>:(Ljava/io/OutputStream;)V
        //    16: astore_2       
        //    17: aload_2        
        //    18: aload_1        
        //    19: invokevirtual   java/io/ObjectOutputStream.writeObject:(Ljava/lang/Object;)V
        //    22: aload_3        
        //    23: invokevirtual   java/io/ByteArrayOutputStream.toByteArray:()[B
        //    26: astore_1       
        //    27: aload_2        
        //    28: ifnull          35
        //    31: aload_2        
        //    32: invokevirtual   java/io/ObjectOutputStream.close:()V
        //    35: aload_3        
        //    36: invokevirtual   java/io/ByteArrayOutputStream.close:()V
        //    39: aload_1        
        //    40: areturn        
        //    41: astore_1       
        //    42: aconst_null    
        //    43: astore_2       
        //    44: aload_2        
        //    45: ifnull          52
        //    48: aload_2        
        //    49: invokevirtual   java/io/ObjectOutputStream.close:()V
        //    52: aload_3        
        //    53: invokevirtual   java/io/ByteArrayOutputStream.close:()V
        //    56: aconst_null    
        //    57: areturn        
        //    58: astore_1       
        //    59: aconst_null    
        //    60: areturn        
        //    61: astore_1       
        //    62: aconst_null    
        //    63: astore_2       
        //    64: aload_2        
        //    65: ifnull          72
        //    68: aload_2        
        //    69: invokevirtual   java/io/ObjectOutputStream.close:()V
        //    72: aload_3        
        //    73: invokevirtual   java/io/ByteArrayOutputStream.close:()V
        //    76: aload_1        
        //    77: athrow         
        //    78: astore_2       
        //    79: goto            76
        //    82: astore_1       
        //    83: goto            64
        //    86: astore_1       
        //    87: goto            44
        //    90: astore_2       
        //    91: aload_1        
        //    92: areturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  8      17     41     44     Ljava/io/IOException;
        //  8      17     61     64     Any
        //  17     27     86     90     Ljava/io/IOException;
        //  17     27     82     86     Any
        //  31     35     90     93     Ljava/io/IOException;
        //  35     39     90     93     Ljava/io/IOException;
        //  48     52     58     61     Ljava/io/IOException;
        //  52     56     58     61     Ljava/io/IOException;
        //  68     72     78     82     Ljava/io/IOException;
        //  72     76     78     82     Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 57, Size: 57
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
    
    private void zzS(final long n) {
        final SQLiteDatabase zzer = this.zzer("Error opening database for deleteOlderThan.");
        if (zzer == null) {
            return;
        }
        try {
            zzbg.zzaB("Deleted " + zzer.delete("datalayer", "expires <= ?", new String[] { Long.toString(n) }) + " expired items");
        }
        catch (SQLiteException ex) {
            zzbg.zzaC("Error deleting old entries.");
        }
    }
    
    private void zzb(final List<zzb> list, final long n) {
        // monitorenter(this)
        try {
            final long currentTimeMillis = this.zzpw.currentTimeMillis();
            this.zzS(currentTimeMillis);
            this.zziS(list.size());
            this.zzc(list, currentTimeMillis + n);
            return;
        }
        finally {
            this.zzyF();
        }
        try {}
        finally {
        }
        // monitorexit(this)
    }
    
    private void zzc(final List<zzb> list, final long n) {
        final SQLiteDatabase zzer = this.zzer("Error opening database for writeEntryToDatabase.");
        if (zzer != null) {
            for (final zzb zzb : list) {
                final ContentValues contentValues = new ContentValues();
                contentValues.put("expires", n);
                contentValues.put("key", zzb.zztw);
                contentValues.put("value", zzb.zzaLy);
                zzer.insert("datalayer", (String)null, contentValues);
            }
        }
    }
    
    private void zze(final String[] array) {
        if (array != null && array.length != 0) {
            final SQLiteDatabase zzer = this.zzer("Error opening database for deleteEntries.");
            if (zzer != null) {
                final String format = String.format("%s in (%s)", "ID", TextUtils.join((CharSequence)",", (Iterable)Collections.nCopies(array.length, "?")));
                try {
                    zzer.delete("datalayer", format, array);
                }
                catch (SQLiteException ex) {
                    zzbg.zzaC("Error deleting entries " + Arrays.toString(array));
                }
            }
        }
    }
    
    private SQLiteDatabase zzer(final String s) {
        try {
            return this.zzaLr.getWritableDatabase();
        }
        catch (SQLiteException ex) {
            zzbg.zzaC(s);
            return null;
        }
    }
    
    private void zziS(int n) {
        n += this.zzyE() - this.zzaLs;
        if (n > 0) {
            final List<String> zziT = this.zziT(n);
            zzbg.zzaA("DataLayer store full, deleting " + zziT.size() + " entries to make room.");
            this.zze(zziT.toArray(new String[0]));
        }
    }
    
    private List<String> zziT(final int p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: new             Ljava/util/ArrayList;
        //     3: dup            
        //     4: invokespecial   java/util/ArrayList.<init>:()V
        //     7: astore          6
        //     9: iload_1        
        //    10: ifgt            22
        //    13: ldc_w           "Invalid maxEntries specified. Skipping."
        //    16: invokestatic    com/google/android/gms/tagmanager/zzbg.zzaC:(Ljava/lang/String;)V
        //    19: aload           6
        //    21: areturn        
        //    22: aload_0        
        //    23: ldc_w           "Error opening database for peekEntryIds."
        //    26: invokespecial   com/google/android/gms/tagmanager/zzw.zzer:(Ljava/lang/String;)Landroid/database/sqlite/SQLiteDatabase;
        //    29: astore_3       
        //    30: aload_3        
        //    31: ifnonnull       37
        //    34: aload           6
        //    36: areturn        
        //    37: ldc_w           "%s ASC"
        //    40: iconst_1       
        //    41: anewarray       Ljava/lang/Object;
        //    44: dup            
        //    45: iconst_0       
        //    46: ldc             "ID"
        //    48: aastore        
        //    49: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //    52: astore          4
        //    54: iload_1        
        //    55: invokestatic    java/lang/Integer.toString:(I)Ljava/lang/String;
        //    58: astore          5
        //    60: aload_3        
        //    61: ldc             "datalayer"
        //    63: iconst_1       
        //    64: anewarray       Ljava/lang/String;
        //    67: dup            
        //    68: iconst_0       
        //    69: ldc             "ID"
        //    71: aastore        
        //    72: aconst_null    
        //    73: aconst_null    
        //    74: aconst_null    
        //    75: aconst_null    
        //    76: aload           4
        //    78: aload           5
        //    80: invokevirtual   android/database/sqlite/SQLiteDatabase.query:(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //    83: astore          4
        //    85: aload           4
        //    87: astore_3       
        //    88: aload           4
        //    90: invokeinterface android/database/Cursor.moveToFirst:()Z
        //    95: ifeq            135
        //    98: aload           4
        //   100: astore_3       
        //   101: aload           6
        //   103: aload           4
        //   105: iconst_0       
        //   106: invokeinterface android/database/Cursor.getLong:(I)J
        //   111: invokestatic    java/lang/String.valueOf:(J)Ljava/lang/String;
        //   114: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   119: pop            
        //   120: aload           4
        //   122: astore_3       
        //   123: aload           4
        //   125: invokeinterface android/database/Cursor.moveToNext:()Z
        //   130: istore_2       
        //   131: iload_2        
        //   132: ifne            98
        //   135: aload           4
        //   137: ifnull          147
        //   140: aload           4
        //   142: invokeinterface android/database/Cursor.close:()V
        //   147: aload           6
        //   149: areturn        
        //   150: astore          5
        //   152: aconst_null    
        //   153: astore          4
        //   155: aload           4
        //   157: astore_3       
        //   158: new             Ljava/lang/StringBuilder;
        //   161: dup            
        //   162: invokespecial   java/lang/StringBuilder.<init>:()V
        //   165: ldc_w           "Error in peekEntries fetching entryIds: "
        //   168: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   171: aload           5
        //   173: invokevirtual   android/database/sqlite/SQLiteException.getMessage:()Ljava/lang/String;
        //   176: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   179: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   182: invokestatic    com/google/android/gms/tagmanager/zzbg.zzaC:(Ljava/lang/String;)V
        //   185: aload           4
        //   187: ifnull          147
        //   190: aload           4
        //   192: invokeinterface android/database/Cursor.close:()V
        //   197: goto            147
        //   200: astore          4
        //   202: aconst_null    
        //   203: astore_3       
        //   204: aload_3        
        //   205: ifnull          214
        //   208: aload_3        
        //   209: invokeinterface android/database/Cursor.close:()V
        //   214: aload           4
        //   216: athrow         
        //   217: astore          4
        //   219: goto            204
        //   222: astore          5
        //   224: goto            155
        //    Signature:
        //  (I)Ljava/util/List<Ljava/lang/String;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  37     85     150    155    Landroid/database/sqlite/SQLiteException;
        //  37     85     200    204    Any
        //  88     98     222    227    Landroid/database/sqlite/SQLiteException;
        //  88     98     217    222    Any
        //  101    120    222    227    Landroid/database/sqlite/SQLiteException;
        //  101    120    217    222    Any
        //  123    131    222    227    Landroid/database/sqlite/SQLiteException;
        //  123    131    217    222    Any
        //  158    185    217    222    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0098:
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
    
    private List<DataLayer.zza> zzp(final List<zzb> list) {
        final ArrayList<DataLayer.zza> list2 = new ArrayList<DataLayer.zza>();
        for (final zzb zzb : list) {
            list2.add(new DataLayer.zza(zzb.zztw, this.zzq(zzb.zzaLy)));
        }
        return list2;
    }
    
    private Object zzq(final byte[] p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: new             Ljava/io/ByteArrayInputStream;
        //     3: dup            
        //     4: aload_1        
        //     5: invokespecial   java/io/ByteArrayInputStream.<init>:([B)V
        //     8: astore_3       
        //     9: new             Ljava/io/ObjectInputStream;
        //    12: dup            
        //    13: aload_3        
        //    14: invokespecial   java/io/ObjectInputStream.<init>:(Ljava/io/InputStream;)V
        //    17: astore_1       
        //    18: aload_1        
        //    19: invokevirtual   java/io/ObjectInputStream.readObject:()Ljava/lang/Object;
        //    22: astore_2       
        //    23: aload_1        
        //    24: ifnull          31
        //    27: aload_1        
        //    28: invokevirtual   java/io/ObjectInputStream.close:()V
        //    31: aload_3        
        //    32: invokevirtual   java/io/ByteArrayInputStream.close:()V
        //    35: aload_2        
        //    36: areturn        
        //    37: astore_1       
        //    38: aconst_null    
        //    39: astore_1       
        //    40: aload_1        
        //    41: ifnull          48
        //    44: aload_1        
        //    45: invokevirtual   java/io/ObjectInputStream.close:()V
        //    48: aload_3        
        //    49: invokevirtual   java/io/ByteArrayInputStream.close:()V
        //    52: aconst_null    
        //    53: areturn        
        //    54: astore_1       
        //    55: aconst_null    
        //    56: areturn        
        //    57: astore_1       
        //    58: aconst_null    
        //    59: astore_1       
        //    60: aload_1        
        //    61: ifnull          68
        //    64: aload_1        
        //    65: invokevirtual   java/io/ObjectInputStream.close:()V
        //    68: aload_3        
        //    69: invokevirtual   java/io/ByteArrayInputStream.close:()V
        //    72: aconst_null    
        //    73: areturn        
        //    74: astore_1       
        //    75: aconst_null    
        //    76: areturn        
        //    77: astore_2       
        //    78: aconst_null    
        //    79: astore_1       
        //    80: aload_1        
        //    81: ifnull          88
        //    84: aload_1        
        //    85: invokevirtual   java/io/ObjectInputStream.close:()V
        //    88: aload_3        
        //    89: invokevirtual   java/io/ByteArrayInputStream.close:()V
        //    92: aload_2        
        //    93: athrow         
        //    94: astore_1       
        //    95: goto            92
        //    98: astore_2       
        //    99: goto            80
        //   102: astore_2       
        //   103: goto            60
        //   106: astore_2       
        //   107: goto            40
        //   110: astore_1       
        //   111: aload_2        
        //   112: areturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                              
        //  -----  -----  -----  -----  ----------------------------------
        //  9      18     37     40     Ljava/io/IOException;
        //  9      18     57     60     Ljava/lang/ClassNotFoundException;
        //  9      18     77     80     Any
        //  18     23     106    110    Ljava/io/IOException;
        //  18     23     102    106    Ljava/lang/ClassNotFoundException;
        //  18     23     98     102    Any
        //  27     31     110    113    Ljava/io/IOException;
        //  31     35     110    113    Ljava/io/IOException;
        //  44     48     54     57     Ljava/io/IOException;
        //  48     52     54     57     Ljava/io/IOException;
        //  64     68     74     77     Ljava/io/IOException;
        //  68     72     74     77     Ljava/io/IOException;
        //  84     88     94     98     Ljava/io/IOException;
        //  88     92     94     98     Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 71, Size: 71
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
    
    private List<zzb> zzq(final List<DataLayer.zza> list) {
        final ArrayList<zzb> list2 = new ArrayList<zzb>();
        for (final DataLayer.zza zza : list) {
            list2.add(new zzb(zza.zztw, this.zzA(zza.zzGK)));
        }
        return list2;
    }
    
    private List<DataLayer.zza> zzyC() {
        try {
            this.zzS(this.zzpw.currentTimeMillis());
            return this.zzp(this.zzyD());
        }
        finally {
            this.zzyF();
        }
    }
    
    private List<zzb> zzyD() {
        final SQLiteDatabase zzer = this.zzer("Error opening database for loadSerialized.");
        final ArrayList<zzb> list = new ArrayList<zzb>();
        if (zzer == null) {
            return list;
        }
        final Cursor query = zzer.query("datalayer", new String[] { "key", "value" }, (String)null, (String[])null, (String)null, (String)null, "ID", (String)null);
        try {
            while (query.moveToNext()) {
                list.add(new zzb(query.getString(0), query.getBlob(1)));
            }
        }
        finally {
            query.close();
        }
        query.close();
        return;
    }
    
    private int zzyE() {
        Cursor cursor = null;
        Cursor rawQuery = null;
        int n = 0;
        int n2 = 0;
        final SQLiteDatabase zzer = this.zzer("Error opening database for getNumStoredEntries.");
        if (zzer == null) {
            return n2;
        }
        try {
            final Cursor cursor2 = cursor = (rawQuery = zzer.rawQuery("SELECT COUNT(*) from datalayer", (String[])null));
            if (cursor2.moveToFirst()) {
                rawQuery = cursor2;
                cursor = cursor2;
                n = (int)cursor2.getLong(0);
            }
            n2 = n;
            return n;
        }
        catch (SQLiteException cursor) {
            cursor = rawQuery;
            zzbg.zzaC("Error getting numStoredEntries");
            return 0;
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    
    private void zzyF() {
        try {
            this.zzaLr.close();
        }
        catch (SQLiteException ex) {}
    }
    
    @Override
    public void zza(final zzc.zza zza) {
        this.zzaLq.execute(new Runnable() {
            @Override
            public void run() {
                zza.zzo(zzw.this.zzyC());
            }
        });
    }
    
    @Override
    public void zza(final List<DataLayer.zza> list, final long n) {
        this.zzaLq.execute(new Runnable() {
            final /* synthetic */ List zzaLt = zzw.this.zzq(list);
            
            @Override
            public void run() {
                zzw.this.zzb(this.zzaLt, n);
            }
        });
    }
    
    class zza extends SQLiteOpenHelper
    {
        zza(final Context context, final String s) {
            super(context, s, (SQLiteDatabase$CursorFactory)null, 1);
        }
        
        private boolean zza(final String p0, final SQLiteDatabase p1) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: aconst_null    
            //     1: astore          4
            //     3: aload_2        
            //     4: ldc             "SQLITE_MASTER"
            //     6: iconst_1       
            //     7: anewarray       Ljava/lang/String;
            //    10: dup            
            //    11: iconst_0       
            //    12: ldc             "name"
            //    14: aastore        
            //    15: ldc             "name=?"
            //    17: iconst_1       
            //    18: anewarray       Ljava/lang/String;
            //    21: dup            
            //    22: iconst_0       
            //    23: aload_1        
            //    24: aastore        
            //    25: aconst_null    
            //    26: aconst_null    
            //    27: aconst_null    
            //    28: invokevirtual   android/database/sqlite/SQLiteDatabase.query:(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
            //    31: astore_2       
            //    32: aload_2        
            //    33: invokeinterface android/database/Cursor.moveToFirst:()Z
            //    38: istore_3       
            //    39: aload_2        
            //    40: ifnull          49
            //    43: aload_2        
            //    44: invokeinterface android/database/Cursor.close:()V
            //    49: iload_3        
            //    50: ireturn        
            //    51: astore_2       
            //    52: aconst_null    
            //    53: astore_2       
            //    54: new             Ljava/lang/StringBuilder;
            //    57: dup            
            //    58: invokespecial   java/lang/StringBuilder.<init>:()V
            //    61: ldc             "Error querying for table "
            //    63: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    66: aload_1        
            //    67: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    70: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //    73: invokestatic    com/google/android/gms/tagmanager/zzbg.zzaC:(Ljava/lang/String;)V
            //    76: aload_2        
            //    77: ifnull          86
            //    80: aload_2        
            //    81: invokeinterface android/database/Cursor.close:()V
            //    86: iconst_0       
            //    87: ireturn        
            //    88: astore_1       
            //    89: aload           4
            //    91: astore_2       
            //    92: aload_2        
            //    93: ifnull          102
            //    96: aload_2        
            //    97: invokeinterface android/database/Cursor.close:()V
            //   102: aload_1        
            //   103: athrow         
            //   104: astore_1       
            //   105: goto            92
            //   108: astore_1       
            //   109: goto            92
            //   112: astore          4
            //   114: goto            54
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                                     
            //  -----  -----  -----  -----  -----------------------------------------
            //  3      32     51     54     Landroid/database/sqlite/SQLiteException;
            //  3      32     88     92     Any
            //  32     39     112    117    Landroid/database/sqlite/SQLiteException;
            //  32     39     104    108    Any
            //  54     76     108    112    Any
            // 
            // The error that occurred was:
            // 
            // java.lang.IndexOutOfBoundsException: Index: 64, Size: 64
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:556)
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
        
        private void zzc(SQLiteDatabase rawQuery) {
            rawQuery = (SQLiteDatabase)rawQuery.rawQuery("SELECT * FROM datalayer WHERE 0", (String[])null);
            final HashSet<String> set = new HashSet<String>();
            try {
                final String[] columnNames = ((Cursor)rawQuery).getColumnNames();
                for (int i = 0; i < columnNames.length; ++i) {
                    set.add(columnNames[i]);
                }
                ((Cursor)rawQuery).close();
                if (!set.remove("key") || !set.remove("value") || !set.remove("ID") || !set.remove("expires")) {
                    throw new SQLiteException("Database column missing");
                }
            }
            finally {
                ((Cursor)rawQuery).close();
            }
            final Set set2;
            if (!set2.isEmpty()) {
                throw new SQLiteException("Database has extra columns");
            }
        }
        
        public SQLiteDatabase getWritableDatabase() {
            SQLiteDatabase writableDatabase = null;
            while (true) {
                try {
                    writableDatabase = super.getWritableDatabase();
                    SQLiteDatabase writableDatabase2 = writableDatabase;
                    if (writableDatabase == null) {
                        writableDatabase2 = super.getWritableDatabase();
                    }
                    return writableDatabase2;
                }
                catch (SQLiteException ex) {
                    zzw.this.mContext.getDatabasePath("google_tagmanager.db").delete();
                    continue;
                }
                break;
            }
        }
        
        public void onCreate(final SQLiteDatabase sqLiteDatabase) {
            zzal.zzbe(sqLiteDatabase.getPath());
        }
        
        public void onOpen(final SQLiteDatabase sqLiteDatabase) {
            while (true) {
                if (Build$VERSION.SDK_INT < 15) {
                    final Cursor rawQuery = sqLiteDatabase.rawQuery("PRAGMA journal_mode=memory", (String[])null);
                    try {
                        rawQuery.moveToFirst();
                        rawQuery.close();
                        if (!this.zza("datalayer", sqLiteDatabase)) {
                            sqLiteDatabase.execSQL(zzw.zzaLp);
                            return;
                        }
                    }
                    finally {
                        rawQuery.close();
                    }
                    final SQLiteDatabase sqLiteDatabase2;
                    this.zzc(sqLiteDatabase2);
                    return;
                }
                continue;
            }
        }
        
        public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int n, final int n2) {
        }
    }
    
    private static class zzb
    {
        final byte[] zzaLy;
        final String zztw;
        
        zzb(final String zztw, final byte[] zzaLy) {
            this.zztw = zztw;
            this.zzaLy = zzaLy;
        }
        
        @Override
        public String toString() {
            return "KeyAndSerialized: key = " + this.zztw + " serialized hash = " + Arrays.hashCode(this.zzaLy);
        }
    }
}
