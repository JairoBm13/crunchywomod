// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

import android.os.Build$VERSION;
import java.util.HashSet;
import java.util.Set;
import android.database.sqlite.SQLiteDatabase$CursorFactory;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import android.content.ContentValues;
import java.net.URISyntaxException;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import java.net.URI;
import java.util.HashMap;
import android.text.TextUtils;
import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import java.util.Iterator;
import android.net.Uri$Builder;
import com.google.android.gms.common.internal.zzu;
import java.util.Map;
import java.io.Closeable;

class zzj extends zzd implements Closeable
{
    private static final String zzKg;
    private static final String zzKh;
    private final zza zzKi;
    private final zzaj zzKj;
    private final zzaj zzKk;
    
    static {
        zzKg = String.format("CREATE TABLE IF NOT EXISTS %s ( '%s' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, '%s' INTEGER NOT NULL, '%s' TEXT NOT NULL, '%s' TEXT NOT NULL, '%s' INTEGER);", "hits2", "hit_id", "hit_time", "hit_url", "hit_string", "hit_app_id");
        zzKh = String.format("SELECT MAX(%s) FROM %s WHERE 1;", "hit_time", "hits2");
    }
    
    zzj(final zzf zzf) {
        super(zzf);
        this.zzKj = new zzaj(this.zzhP());
        this.zzKk = new zzaj(this.zzhP());
        this.zzKi = new zza(zzf.getContext(), this.zziv());
    }
    
    private static String zzA(final Map<String, String> map) {
        zzu.zzu(map);
        final Uri$Builder uri$Builder = new Uri$Builder();
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            uri$Builder.appendQueryParameter((String)entry.getKey(), (String)entry.getValue());
        }
        String encodedQuery;
        if ((encodedQuery = uri$Builder.build().getEncodedQuery()) == null) {
            encodedQuery = "";
        }
        return encodedQuery;
    }
    
    private long zza(final String s, final String[] array, long n) {
        final SQLiteDatabase writableDatabase = this.getWritableDatabase();
        Cursor cursor = null;
        Cursor rawQuery = null;
        try {
            final Cursor cursor2 = cursor = (rawQuery = writableDatabase.rawQuery(s, array));
            if (cursor2.moveToFirst()) {
                rawQuery = cursor2;
                cursor = cursor2;
                long long1;
                n = (long1 = cursor2.getLong(0));
                if (cursor2 != null) {
                    cursor2.close();
                    long1 = n;
                }
                return long1;
            }
            long long1 = n;
            return n;
        }
        catch (SQLiteException ex) {
            cursor = rawQuery;
            this.zzd("Database error", s, ex);
            cursor = rawQuery;
            throw ex;
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    
    private long zzb(final String s, final String[] array) {
        final SQLiteDatabase writableDatabase = this.getWritableDatabase();
        Cursor cursor = null;
        Cursor rawQuery = null;
        try {
            final Cursor cursor2 = cursor = (rawQuery = writableDatabase.rawQuery(s, array));
            if (cursor2.moveToFirst()) {
                rawQuery = cursor2;
                cursor = cursor2;
                return cursor2.getLong(0);
            }
            rawQuery = cursor2;
            cursor = cursor2;
            throw new SQLiteException("Database returned empty set");
        }
        catch (SQLiteException ex) {
            cursor = rawQuery;
            this.zzd("Database error", s, ex);
            cursor = rawQuery;
            throw ex;
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    
    private String zzd(final zzab zzab) {
        if (zzab.zzjY()) {
            return this.zzhR().zzjk();
        }
        return this.zzhR().zzjl();
    }
    
    private static String zze(final zzab zzab) {
        zzu.zzu(zzab);
        final Uri$Builder uri$Builder = new Uri$Builder();
        for (final Map.Entry<String, String> entry : zzab.zzn().entrySet()) {
            final String s = entry.getKey();
            if (!"ht".equals(s) && !"qt".equals(s) && !"AppUID".equals(s)) {
                uri$Builder.appendQueryParameter(s, (String)entry.getValue());
            }
        }
        String encodedQuery;
        if ((encodedQuery = uri$Builder.build().getEncodedQuery()) == null) {
            encodedQuery = "";
        }
        return encodedQuery;
    }
    
    private void zziu() {
        final int zzju = this.zzhR().zzju();
        final long zzil = this.zzil();
        if (zzil > zzju - 1) {
            final List<Long> zzo = this.zzo(zzil - zzju + 1L);
            this.zzd("Store full, deleting hits to make room, count", zzo.size());
            this.zzd(zzo);
        }
    }
    
    private String zziv() {
        if (!this.zzhR().zziW()) {
            return this.zzhR().zzjw();
        }
        if (this.zzhR().zziX()) {
            return this.zzhR().zzjw();
        }
        return this.zzhR().zzjx();
    }
    
    public void beginTransaction() {
        this.zzia();
        this.getWritableDatabase().beginTransaction();
    }
    
    @Override
    public void close() {
        try {
            this.zzKi.close();
        }
        catch (SQLiteException ex) {
            this.zze("Sql error closing database", ex);
        }
        catch (IllegalStateException ex2) {
            this.zze("Error closing database", ex2);
        }
    }
    
    public void endTransaction() {
        this.zzia();
        this.getWritableDatabase().endTransaction();
    }
    
    SQLiteDatabase getWritableDatabase() {
        try {
            return this.zzKi.getWritableDatabase();
        }
        catch (SQLiteException ex) {
            this.zzd("Error opening database", ex);
            throw ex;
        }
    }
    
    boolean isEmpty() {
        return this.zzil() == 0L;
    }
    
    public void setTransactionSuccessful() {
        this.zzia();
        this.getWritableDatabase().setTransactionSuccessful();
    }
    
    public long zza(final long n, final String s, final String s2) {
        zzu.zzcj(s);
        zzu.zzcj(s2);
        this.zzia();
        this.zzhO();
        return this.zza("SELECT hits_count FROM properties WHERE app_uid=? AND cid=? AND tid=?", new String[] { String.valueOf(n), s, s2 }, 0L);
    }
    
    public void zza(final long n, final String s) {
        zzu.zzcj(s);
        this.zzia();
        this.zzhO();
        final int delete = this.getWritableDatabase().delete("properties", "app_uid=? AND cid<>?", new String[] { String.valueOf(n), s });
        if (delete > 0) {
            this.zza("Deleted property records", delete);
        }
    }
    
    Map<String, String> zzaY(String string) {
        if (TextUtils.isEmpty((CharSequence)string)) {
            return new HashMap<String, String>(0);
        }
        HashMap hashMap;
        while (true) {
            while (true) {
                try {
                    if (string.startsWith("?")) {
                        final List parse = URLEncodedUtils.parse(new URI(string), "UTF-8");
                        hashMap = new HashMap<String, String>(parse.size());
                        for (final NameValuePair nameValuePair : parse) {
                            hashMap.put(nameValuePair.getName(), nameValuePair.getValue());
                        }
                        break;
                    }
                }
                catch (URISyntaxException ex) {
                    this.zze("Error parsing hit parameters", ex);
                    return new HashMap<String, String>(0);
                }
                string = "?" + string;
                continue;
            }
        }
        return (Map<String, String>)hashMap;
    }
    
    Map<String, String> zzaZ(final String s) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            return new HashMap<String, String>(0);
        }
        HashMap hashMap;
        try {
            final List parse = URLEncodedUtils.parse(new URI("?" + s), "UTF-8");
            hashMap = new HashMap<String, String>(parse.size());
            for (final NameValuePair nameValuePair : parse) {
                hashMap.put(nameValuePair.getName(), nameValuePair.getValue());
            }
        }
        catch (URISyntaxException ex) {
            this.zze("Error parsing property parameters", ex);
            return new HashMap<String, String>(0);
        }
        return (Map<String, String>)hashMap;
    }
    
    public void zzb(final zzh zzh) {
        zzu.zzu(zzh);
        this.zzia();
        this.zzhO();
        final SQLiteDatabase writableDatabase = this.getWritableDatabase();
        final String zzA = zzA(zzh.zzn());
        final ContentValues contentValues = new ContentValues();
        contentValues.put("app_uid", zzh.zzii());
        contentValues.put("cid", zzh.getClientId());
        contentValues.put("tid", zzh.zzij());
        Label_0147: {
            if (!zzh.zzik()) {
                break Label_0147;
            }
            int n = 1;
            while (true) {
                contentValues.put("adid", n);
                contentValues.put("hits_count", zzh.zzil());
                contentValues.put("params", zzA);
                try {
                    if (writableDatabase.insertWithOnConflict("properties", (String)null, contentValues, 5) == -1L) {
                        this.zzaX("Failed to insert/update a property (got -1)");
                    }
                    return;
                    n = 0;
                }
                catch (SQLiteException ex) {
                    this.zze("Error storing a property", ex);
                }
            }
        }
    }
    
    public void zzc(final zzab zzab) {
        zzu.zzu(zzab);
        this.zzhO();
        this.zzia();
        final String zze = zze(zzab);
        if (zze.length() > 8192) {
            this.zzhQ().zza(zzab, "Hit length exceeds the maximum allowed size");
            return;
        }
        this.zziu();
        final SQLiteDatabase writableDatabase = this.getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put("hit_string", zze);
        contentValues.put("hit_time", zzab.zzjW());
        contentValues.put("hit_app_id", zzab.zzjU());
        contentValues.put("hit_url", this.zzd(zzab));
        long insert;
        try {
            insert = writableDatabase.insert("hits2", (String)null, contentValues);
            if (insert == -1L) {
                this.zzaX("Failed to insert a hit (got -1)");
                return;
            }
        }
        catch (SQLiteException ex) {
            this.zze("Error storing a hit", ex);
            return;
        }
        this.zzb("Hit saved to database. db-id, hit", insert, zzab);
    }
    
    public void zzd(final List<Long> list) {
        zzu.zzu(list);
        this.zzhO();
        this.zzia();
        if (!list.isEmpty()) {
            final StringBuilder sb = new StringBuilder("hit_id");
            sb.append(" in (");
            for (int i = 0; i < list.size(); ++i) {
                final Long n = list.get(i);
                if (n == null || n == 0L) {
                    throw new SQLiteException("Invalid hit id");
                }
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(n);
            }
            sb.append(")");
            final String string = sb.toString();
            try {
                final SQLiteDatabase writableDatabase = this.getWritableDatabase();
                this.zza("Deleting dispatched hits. count", list.size());
                final int delete = writableDatabase.delete("hits2", string, (String[])null);
                if (delete != list.size()) {
                    this.zzb("Deleted fewer hits then expected", list.size(), delete, string);
                }
            }
            catch (SQLiteException ex) {
                this.zze("Error deleting hits", ex);
                throw ex;
            }
        }
    }
    
    @Override
    protected void zzhn() {
    }
    
    public long zzil() {
        this.zzhO();
        this.zzia();
        return this.zzb("SELECT COUNT(*) FROM hits2", null);
    }
    
    public int zzis() {
        this.zzhO();
        this.zzia();
        if (!this.zzKj.zzv(86400000L)) {
            return 0;
        }
        this.zzKj.start();
        this.zzaT("Deleting stale hits (if any)");
        final int delete = this.getWritableDatabase().delete("hits2", "hit_time < ?", new String[] { Long.toString(this.zzhP().currentTimeMillis() - 2592000000L) });
        this.zza("Deleted stale hits, count", delete);
        return delete;
    }
    
    public long zzit() {
        this.zzhO();
        this.zzia();
        return this.zza(zzj.zzKh, null, 0L);
    }
    
    public List<Long> zzo(final long p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aconst_null    
        //     1: astore          5
        //     3: aload_0        
        //     4: invokevirtual   com/google/android/gms/analytics/internal/zzj.zzhO:()V
        //     7: aload_0        
        //     8: invokevirtual   com/google/android/gms/analytics/internal/zzj.zzia:()V
        //    11: lload_1        
        //    12: lconst_0       
        //    13: lcmp           
        //    14: ifgt            21
        //    17: invokestatic    java/util/Collections.emptyList:()Ljava/util/List;
        //    20: areturn        
        //    21: aload_0        
        //    22: invokevirtual   com/google/android/gms/analytics/internal/zzj.getWritableDatabase:()Landroid/database/sqlite/SQLiteDatabase;
        //    25: astore          4
        //    27: new             Ljava/util/ArrayList;
        //    30: dup            
        //    31: invokespecial   java/util/ArrayList.<init>:()V
        //    34: astore          7
        //    36: ldc_w           "%s ASC"
        //    39: iconst_1       
        //    40: anewarray       Ljava/lang/Object;
        //    43: dup            
        //    44: iconst_0       
        //    45: ldc             "hit_id"
        //    47: aastore        
        //    48: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //    51: astore          6
        //    53: lload_1        
        //    54: invokestatic    java/lang/Long.toString:(J)Ljava/lang/String;
        //    57: astore          8
        //    59: aload           4
        //    61: ldc             "hits2"
        //    63: iconst_1       
        //    64: anewarray       Ljava/lang/String;
        //    67: dup            
        //    68: iconst_0       
        //    69: ldc             "hit_id"
        //    71: aastore        
        //    72: aconst_null    
        //    73: aconst_null    
        //    74: aconst_null    
        //    75: aconst_null    
        //    76: aload           6
        //    78: aload           8
        //    80: invokevirtual   android/database/sqlite/SQLiteDatabase.query:(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //    83: astore          4
        //    85: aload           4
        //    87: astore          5
        //    89: aload           5
        //    91: astore          4
        //    93: aload           5
        //    95: invokeinterface android/database/Cursor.moveToFirst:()Z
        //   100: ifeq            142
        //   103: aload           5
        //   105: astore          4
        //   107: aload           7
        //   109: aload           5
        //   111: iconst_0       
        //   112: invokeinterface android/database/Cursor.getLong:(I)J
        //   117: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   120: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   125: pop            
        //   126: aload           5
        //   128: astore          4
        //   130: aload           5
        //   132: invokeinterface android/database/Cursor.moveToNext:()Z
        //   137: istore_3       
        //   138: iload_3        
        //   139: ifne            103
        //   142: aload           5
        //   144: ifnull          154
        //   147: aload           5
        //   149: invokeinterface android/database/Cursor.close:()V
        //   154: aload           7
        //   156: areturn        
        //   157: astore          6
        //   159: aconst_null    
        //   160: astore          5
        //   162: aload           5
        //   164: astore          4
        //   166: aload_0        
        //   167: ldc_w           "Error selecting hit ids"
        //   170: aload           6
        //   172: invokevirtual   com/google/android/gms/analytics/internal/zzj.zzd:(Ljava/lang/String;Ljava/lang/Object;)V
        //   175: aload           5
        //   177: ifnull          154
        //   180: aload           5
        //   182: invokeinterface android/database/Cursor.close:()V
        //   187: goto            154
        //   190: astore          4
        //   192: aload           5
        //   194: ifnull          204
        //   197: aload           5
        //   199: invokeinterface android/database/Cursor.close:()V
        //   204: aload           4
        //   206: athrow         
        //   207: astore          6
        //   209: aload           4
        //   211: astore          5
        //   213: aload           6
        //   215: astore          4
        //   217: goto            192
        //   220: astore          6
        //   222: goto            162
        //    Signature:
        //  (J)Ljava/util/List<Ljava/lang/Long;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  36     85     157    162    Landroid/database/sqlite/SQLiteException;
        //  36     85     190    192    Any
        //  93     103    220    225    Landroid/database/sqlite/SQLiteException;
        //  93     103    207    220    Any
        //  107    126    220    225    Landroid/database/sqlite/SQLiteException;
        //  107    126    207    220    Any
        //  130    138    220    225    Landroid/database/sqlite/SQLiteException;
        //  130    138    207    220    Any
        //  166    175    207    220    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0103:
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
    
    public List<zzab> zzp(final long p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: iconst_1       
        //     1: istore          6
        //     3: aconst_null    
        //     4: astore          8
        //     6: lload_1        
        //     7: lconst_0       
        //     8: lcmp           
        //     9: iflt            275
        //    12: iload           6
        //    14: invokestatic    com/google/android/gms/common/internal/zzu.zzV:(Z)V
        //    17: aload_0        
        //    18: invokevirtual   com/google/android/gms/analytics/internal/zzj.zzhO:()V
        //    21: aload_0        
        //    22: invokevirtual   com/google/android/gms/analytics/internal/zzj.zzia:()V
        //    25: aload_0        
        //    26: invokevirtual   com/google/android/gms/analytics/internal/zzj.getWritableDatabase:()Landroid/database/sqlite/SQLiteDatabase;
        //    29: astore          9
        //    31: aload           8
        //    33: astore          7
        //    35: ldc_w           "%s ASC"
        //    38: iconst_1       
        //    39: anewarray       Ljava/lang/Object;
        //    42: dup            
        //    43: iconst_0       
        //    44: ldc             "hit_id"
        //    46: aastore        
        //    47: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //    50: astore          10
        //    52: aload           8
        //    54: astore          7
        //    56: lload_1        
        //    57: invokestatic    java/lang/Long.toString:(J)Ljava/lang/String;
        //    60: astore          11
        //    62: aload           8
        //    64: astore          7
        //    66: aload           9
        //    68: ldc             "hits2"
        //    70: iconst_5       
        //    71: anewarray       Ljava/lang/String;
        //    74: dup            
        //    75: iconst_0       
        //    76: ldc             "hit_id"
        //    78: aastore        
        //    79: dup            
        //    80: iconst_1       
        //    81: ldc             "hit_time"
        //    83: aastore        
        //    84: dup            
        //    85: iconst_2       
        //    86: ldc             "hit_string"
        //    88: aastore        
        //    89: dup            
        //    90: iconst_3       
        //    91: ldc             "hit_url"
        //    93: aastore        
        //    94: dup            
        //    95: iconst_4       
        //    96: ldc             "hit_app_id"
        //    98: aastore        
        //    99: aconst_null    
        //   100: aconst_null    
        //   101: aconst_null    
        //   102: aconst_null    
        //   103: aload           10
        //   105: aload           11
        //   107: invokevirtual   android/database/sqlite/SQLiteDatabase.query:(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //   110: astore          8
        //   112: aload           8
        //   114: astore          7
        //   116: new             Ljava/util/ArrayList;
        //   119: dup            
        //   120: invokespecial   java/util/ArrayList.<init>:()V
        //   123: astore          9
        //   125: aload           8
        //   127: astore          7
        //   129: aload           8
        //   131: invokeinterface android/database/Cursor.moveToFirst:()Z
        //   136: ifeq            260
        //   139: aload           8
        //   141: astore          7
        //   143: aload           8
        //   145: iconst_0       
        //   146: invokeinterface android/database/Cursor.getLong:(I)J
        //   151: lstore_1       
        //   152: aload           8
        //   154: astore          7
        //   156: aload           8
        //   158: iconst_1       
        //   159: invokeinterface android/database/Cursor.getLong:(I)J
        //   164: lstore          4
        //   166: aload           8
        //   168: astore          7
        //   170: aload           8
        //   172: iconst_2       
        //   173: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   178: astore          10
        //   180: aload           8
        //   182: astore          7
        //   184: aload           8
        //   186: iconst_3       
        //   187: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   192: astore          11
        //   194: aload           8
        //   196: astore          7
        //   198: aload           8
        //   200: iconst_4       
        //   201: invokeinterface android/database/Cursor.getInt:(I)I
        //   206: istore_3       
        //   207: aload           8
        //   209: astore          7
        //   211: aload           9
        //   213: new             Lcom/google/android/gms/analytics/internal/zzab;
        //   216: dup            
        //   217: aload_0        
        //   218: aload_0        
        //   219: aload           10
        //   221: invokevirtual   com/google/android/gms/analytics/internal/zzj.zzaY:(Ljava/lang/String;)Ljava/util/Map;
        //   224: lload           4
        //   226: aload           11
        //   228: invokestatic    com/google/android/gms/analytics/internal/zzam.zzbn:(Ljava/lang/String;)Z
        //   231: lload_1        
        //   232: iload_3        
        //   233: invokespecial   com/google/android/gms/analytics/internal/zzab.<init>:(Lcom/google/android/gms/analytics/internal/zzc;Ljava/util/Map;JZJI)V
        //   236: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   241: pop            
        //   242: aload           8
        //   244: astore          7
        //   246: aload           8
        //   248: invokeinterface android/database/Cursor.moveToNext:()Z
        //   253: istore          6
        //   255: iload           6
        //   257: ifne            139
        //   260: aload           8
        //   262: ifnull          272
        //   265: aload           8
        //   267: invokeinterface android/database/Cursor.close:()V
        //   272: aload           9
        //   274: areturn        
        //   275: iconst_0       
        //   276: istore          6
        //   278: goto            12
        //   281: astore          8
        //   283: aconst_null    
        //   284: astore          7
        //   286: aload_0        
        //   287: ldc_w           "Error loading hits from the database"
        //   290: aload           8
        //   292: invokevirtual   com/google/android/gms/analytics/internal/zzj.zze:(Ljava/lang/String;Ljava/lang/Object;)V
        //   295: aload           8
        //   297: athrow         
        //   298: astore          8
        //   300: aload           7
        //   302: ifnull          312
        //   305: aload           7
        //   307: invokeinterface android/database/Cursor.close:()V
        //   312: aload           8
        //   314: athrow         
        //   315: astore          8
        //   317: goto            300
        //   320: astore          9
        //   322: aload           8
        //   324: astore          7
        //   326: aload           9
        //   328: astore          8
        //   330: goto            286
        //    Signature:
        //  (J)Ljava/util/List<Lcom/google/android/gms/analytics/internal/zzab;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  35     52     281    286    Landroid/database/sqlite/SQLiteException;
        //  35     52     315    320    Any
        //  56     62     281    286    Landroid/database/sqlite/SQLiteException;
        //  56     62     315    320    Any
        //  66     112    281    286    Landroid/database/sqlite/SQLiteException;
        //  66     112    315    320    Any
        //  116    125    320    333    Landroid/database/sqlite/SQLiteException;
        //  116    125    315    320    Any
        //  129    139    320    333    Landroid/database/sqlite/SQLiteException;
        //  129    139    315    320    Any
        //  143    152    320    333    Landroid/database/sqlite/SQLiteException;
        //  143    152    315    320    Any
        //  156    166    320    333    Landroid/database/sqlite/SQLiteException;
        //  156    166    315    320    Any
        //  170    180    320    333    Landroid/database/sqlite/SQLiteException;
        //  170    180    315    320    Any
        //  184    194    320    333    Landroid/database/sqlite/SQLiteException;
        //  184    194    315    320    Any
        //  198    207    320    333    Landroid/database/sqlite/SQLiteException;
        //  198    207    315    320    Any
        //  211    242    320    333    Landroid/database/sqlite/SQLiteException;
        //  211    242    315    320    Any
        //  246    255    320    333    Landroid/database/sqlite/SQLiteException;
        //  246    255    315    320    Any
        //  286    298    298    300    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0139:
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
    
    public void zzq(final long n) {
        this.zzhO();
        this.zzia();
        final ArrayList<Long> list = new ArrayList<Long>(1);
        list.add(n);
        this.zza("Deleting hit, id", n);
        this.zzd(list);
    }
    
    public List<zzh> zzr(final long n) {
        this.zzia();
        this.zzhO();
        final SQLiteDatabase writableDatabase = this.getWritableDatabase();
        Cursor query;
        final Cursor cursor = query = null;
        while (true) {
            try {
                try {
                    final int zzjv = this.zzhR().zzjv();
                    query = cursor;
                    final Cursor cursor2 = query = writableDatabase.query("properties", new String[] { "cid", "tid", "adid", "hits_count", "params" }, "app_uid=?", new String[] { String.valueOf(n) }, (String)null, (String)null, (String)null, String.valueOf(zzjv));
                    SQLiteException ex;
                    try {
                        final ArrayList<zzh> list = new ArrayList<zzh>();
                        query = cursor2;
                        if (cursor2.moveToFirst()) {
                            do {
                                query = cursor2;
                                final String string = cursor2.getString(0);
                                query = cursor2;
                                final String string2 = cursor2.getString(1);
                                query = cursor2;
                                final boolean b = cursor2.getInt(2) != 0;
                                query = cursor2;
                                final long n2 = cursor2.getInt(3);
                                query = cursor2;
                                final Map<String, String> zzaZ = this.zzaZ(cursor2.getString(4));
                                query = cursor2;
                                Label_0243: {
                                    if (!TextUtils.isEmpty((CharSequence)string)) {
                                        query = cursor2;
                                        if (!TextUtils.isEmpty((CharSequence)string2)) {
                                            query = cursor2;
                                            list.add(new zzh(n, string, string2, b, n2, zzaZ));
                                            break Label_0243;
                                        }
                                    }
                                    query = cursor2;
                                    this.zzc("Read property with empty client id or tracker id", string, string2);
                                }
                                query = cursor2;
                            } while (cursor2.moveToNext());
                        }
                        query = cursor2;
                        if (list.size() >= zzjv) {
                            query = cursor2;
                            this.zzaW("Sending hits to too many properties. Campaign report might be incorrect");
                        }
                        if (cursor2 != null) {
                            cursor2.close();
                        }
                        return list;
                    }
                    catch (SQLiteException ex2) {
                        query = cursor2;
                        ex = ex2;
                    }
                    try {
                        this.zze("Error loading hits from the database", ex);
                        throw ex;
                    }
                    finally {}
                    if (query != null) {
                        query.close();
                    }
                    throw;
                }
                finally {}
            }
            catch (SQLiteException ex) {
                query = null;
                continue;
            }
            break;
        }
    }
    
    class zza extends SQLiteOpenHelper
    {
        zza(final Context context, final String s) {
            super(context, s, (SQLiteDatabase$CursorFactory)null, 1);
        }
        
        private void zza(final SQLiteDatabase sqLiteDatabase) {
            final boolean b = true;
            final Set<String> zzb = this.zzb(sqLiteDatabase, "hits2");
            final String[] array = { "hit_id", "hit_string", "hit_time", "hit_url" };
            for (int length = array.length, i = 0; i < length; ++i) {
                final String s = array[i];
                if (!zzb.remove(s)) {
                    throw new SQLiteException("Database hits2 is missing required column: " + s);
                }
            }
            final boolean b2 = !zzb.remove("hit_app_id") && b;
            if (!zzb.isEmpty()) {
                throw new SQLiteException("Database hits2 has extra columns");
            }
            if (b2) {
                sqLiteDatabase.execSQL("ALTER TABLE hits2 ADD COLUMN hit_app_id INTEGER");
            }
        }
        
        private boolean zza(final SQLiteDatabase p0, final String p1) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: aconst_null    
            //     1: astore          4
            //     3: aload_1        
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
            //    23: aload_2        
            //    24: aastore        
            //    25: aconst_null    
            //    26: aconst_null    
            //    27: aconst_null    
            //    28: invokevirtual   android/database/sqlite/SQLiteDatabase.query:(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
            //    31: astore_1       
            //    32: aload_1        
            //    33: astore          4
            //    35: aload           4
            //    37: astore_1       
            //    38: aload           4
            //    40: invokeinterface android/database/Cursor.moveToFirst:()Z
            //    45: istore_3       
            //    46: aload           4
            //    48: ifnull          58
            //    51: aload           4
            //    53: invokeinterface android/database/Cursor.close:()V
            //    58: iload_3        
            //    59: ireturn        
            //    60: astore          5
            //    62: aconst_null    
            //    63: astore          4
            //    65: aload           4
            //    67: astore_1       
            //    68: aload_0        
            //    69: getfield        com/google/android/gms/analytics/internal/zzj$zza.zzKl:Lcom/google/android/gms/analytics/internal/zzj;
            //    72: ldc             "Error querying for table"
            //    74: aload_2        
            //    75: aload           5
            //    77: invokevirtual   com/google/android/gms/analytics/internal/zzj.zzc:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
            //    80: aload           4
            //    82: ifnull          92
            //    85: aload           4
            //    87: invokeinterface android/database/Cursor.close:()V
            //    92: iconst_0       
            //    93: ireturn        
            //    94: astore_1       
            //    95: aload           4
            //    97: astore_2       
            //    98: aload_2        
            //    99: ifnull          108
            //   102: aload_2        
            //   103: invokeinterface android/database/Cursor.close:()V
            //   108: aload_1        
            //   109: athrow         
            //   110: astore          4
            //   112: aload_1        
            //   113: astore_2       
            //   114: aload           4
            //   116: astore_1       
            //   117: goto            98
            //   120: astore          5
            //   122: goto            65
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                                     
            //  -----  -----  -----  -----  -----------------------------------------
            //  3      32     60     65     Landroid/database/sqlite/SQLiteException;
            //  3      32     94     98     Any
            //  38     46     120    125    Landroid/database/sqlite/SQLiteException;
            //  38     46     110    120    Any
            //  68     80     110    120    Any
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0058:
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
        
        private Set<String> zzb(SQLiteDatabase rawQuery, final String s) {
            final HashSet<String> set = new HashSet<String>();
            rawQuery = (SQLiteDatabase)rawQuery.rawQuery("SELECT * FROM " + s + " LIMIT 0", (String[])null);
            try {
                final String[] columnNames = ((Cursor)rawQuery).getColumnNames();
                for (int i = 0; i < columnNames.length; ++i) {
                    set.add(columnNames[i]);
                }
                return set;
            }
            finally {
                ((Cursor)rawQuery).close();
            }
        }
        
        private void zzb(final SQLiteDatabase sqLiteDatabase) {
            int i = 0;
            final Set<String> zzb = this.zzb(sqLiteDatabase, "properties");
            for (String[] array = { "app_uid", "cid", "tid", "params", "adid", "hits_count" }; i < array.length; ++i) {
                final String s = array[i];
                if (!zzb.remove(s)) {
                    throw new SQLiteException("Database properties is missing required column: " + s);
                }
            }
            if (!zzb.isEmpty()) {
                throw new SQLiteException("Database properties table has extra columns");
            }
        }
        
        public SQLiteDatabase getWritableDatabase() {
            if (!zzj.this.zzKk.zzv(3600000L)) {
                throw new SQLiteException("Database open failed");
            }
            try {
                return super.getWritableDatabase();
            }
            catch (SQLiteException ex2) {
                zzj.this.zzKk.start();
                zzj.this.zzaX("Opening the database failed, dropping the table and recreating it");
                zzj.this.getContext().getDatabasePath(zzj.this.zziv()).delete();
                try {
                    final SQLiteDatabase writableDatabase = super.getWritableDatabase();
                    zzj.this.zzKk.clear();
                    return writableDatabase;
                }
                catch (SQLiteException ex) {
                    zzj.this.zze("Failed to open freshly created database", ex);
                    throw ex;
                }
            }
        }
        
        public void onCreate(final SQLiteDatabase sqLiteDatabase) {
            zzx.zzbe(sqLiteDatabase.getPath());
        }
        
        public void onOpen(final SQLiteDatabase sqLiteDatabase) {
            while (true) {
                if (Build$VERSION.SDK_INT < 15) {
                    while (true) {
                        final Cursor rawQuery = sqLiteDatabase.rawQuery("PRAGMA journal_mode=memory", (String[])null);
                        while (true) {
                            try {
                                rawQuery.moveToFirst();
                                rawQuery.close();
                                if (!this.zza(sqLiteDatabase, "hits2")) {
                                    sqLiteDatabase.execSQL(zzj.zzKg);
                                    if (!this.zza(sqLiteDatabase, "properties")) {
                                        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS properties ( app_uid INTEGER NOT NULL, cid TEXT NOT NULL, tid TEXT NOT NULL, params TEXT NOT NULL, adid INTEGER NOT NULL, hits_count INTEGER NOT NULL, PRIMARY KEY (app_uid, cid, tid)) ;");
                                        return;
                                    }
                                    break;
                                }
                            }
                            finally {
                                rawQuery.close();
                            }
                            this.zza(sqLiteDatabase);
                            continue;
                        }
                    }
                    this.zzb(sqLiteDatabase);
                    return;
                }
                continue;
            }
        }
        
        public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int n, final int n2) {
        }
    }
}
