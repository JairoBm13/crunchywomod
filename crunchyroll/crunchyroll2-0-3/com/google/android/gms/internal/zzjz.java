// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import android.text.TextUtils;
import java.util.Calendar;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.android.gms.common.images.WebImage;
import java.util.List;
import com.google.android.gms.cast.internal.zzl;

public class zzjz
{
    private static final zzl zzQW;
    private static final String[] zzVs;
    private static final String zzVt;
    
    static {
        zzQW = new zzl("MetadataUtils");
        zzVs = new String[] { "Z", "+hh", "+hhmm", "+hh:mm" };
        zzVt = "yyyyMMdd'T'HHmmss" + zzjz.zzVs[0];
    }
    
    public static void zza(final List<WebImage> p0, final JSONObject p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: invokeinterface java/util/List.clear:()V
        //     6: aload_1        
        //     7: ldc             "images"
        //     9: invokevirtual   org/json/JSONObject.getJSONArray:(Ljava/lang/String;)Lorg/json/JSONArray;
        //    12: astore_1       
        //    13: aload_1        
        //    14: invokevirtual   org/json/JSONArray.length:()I
        //    17: istore_3       
        //    18: iconst_0       
        //    19: istore_2       
        //    20: iload_2        
        //    21: iload_3        
        //    22: if_icmpge       56
        //    25: aload_1        
        //    26: iload_2        
        //    27: invokevirtual   org/json/JSONArray.getJSONObject:(I)Lorg/json/JSONObject;
        //    30: astore          4
        //    32: aload_0        
        //    33: new             Lcom/google/android/gms/common/images/WebImage;
        //    36: dup            
        //    37: aload           4
        //    39: invokespecial   com/google/android/gms/common/images/WebImage.<init>:(Lorg/json/JSONObject;)V
        //    42: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //    47: pop            
        //    48: iload_2        
        //    49: iconst_1       
        //    50: iadd           
        //    51: istore_2       
        //    52: goto            20
        //    55: astore_0       
        //    56: return         
        //    57: astore          4
        //    59: goto            48
        //    Signature:
        //  (Ljava/util/List<Lcom/google/android/gms/common/images/WebImage;>;Lorg/json/JSONObject;)V
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                
        //  -----  -----  -----  -----  ------------------------------------
        //  0      18     55     56     Lorg/json/JSONException;
        //  25     32     55     56     Lorg/json/JSONException;
        //  32     48     57     62     Ljava/lang/IllegalArgumentException;
        //  32     48     55     56     Lorg/json/JSONException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0048:
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
    
    public static void zza(final JSONObject jsonObject, final List<WebImage> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        final JSONArray jsonArray = new JSONArray();
        final Iterator<WebImage> iterator = list.iterator();
        while (iterator.hasNext()) {
            jsonArray.put((Object)iterator.next().toJson());
        }
        try {
            jsonObject.put("images", (Object)jsonArray);
        }
        catch (JSONException ex) {}
    }
    
    public static Calendar zzbK(String zzVt) {
        if (TextUtils.isEmpty((CharSequence)zzVt)) {
            zzjz.zzQW.zzb("Input string is empty or null", new Object[0]);
            return null;
        }
        final String zzbL = zzbL(zzVt);
        if (TextUtils.isEmpty((CharSequence)zzbL)) {
            zzjz.zzQW.zzb("Invalid date format", new Object[0]);
            return null;
        }
        final String zzbM = zzbM(zzVt);
        zzVt = "yyyyMMdd";
        String string = zzbL;
        Label_0127: {
            if (!TextUtils.isEmpty((CharSequence)zzbM)) {
                string = zzbL + "T" + zzbM;
                if (zzbM.length() != "HHmmss".length()) {
                    break Label_0127;
                }
                zzVt = "yyyyMMdd'T'HHmmss";
            }
            while (true) {
                final Calendar instance = Calendar.getInstance();
                try {
                    instance.setTime(new SimpleDateFormat(zzVt).parse(string));
                    return instance;
                    zzVt = zzjz.zzVt;
                }
                catch (ParseException ex) {
                    zzjz.zzQW.zzb("Error parsing string: %s", ex.getMessage());
                    return null;
                }
            }
        }
    }
    
    private static String zzbL(String substring) {
        if (TextUtils.isEmpty((CharSequence)substring)) {
            zzjz.zzQW.zzb("Input string is empty or null", new Object[0]);
            return null;
        }
        try {
            substring = substring.substring(0, "yyyyMMdd".length());
            return substring;
        }
        catch (IndexOutOfBoundsException ex) {
            zzjz.zzQW.zze("Error extracting the date: %s", ex.getMessage());
            return null;
        }
    }
    
    private static String zzbM(String substring) {
        if (TextUtils.isEmpty((CharSequence)substring)) {
            zzjz.zzQW.zzb("string is empty or null", new Object[0]);
        }
        else {
            final int index = substring.indexOf(84);
            if (index != "yyyyMMdd".length()) {
                zzjz.zzQW.zzb("T delimeter is not found", new Object[0]);
                return null;
            }
            try {
                substring = substring.substring(index + 1);
                if (substring.length() == "HHmmss".length()) {
                    return substring;
                }
            }
            catch (IndexOutOfBoundsException ex) {
                zzjz.zzQW.zzb("Error extracting the time substring: %s", ex.getMessage());
                return null;
            }
            switch (substring.charAt("HHmmss".length())) {
                default: {
                    return null;
                }
                case '+':
                case '-': {
                    if (zzbN(substring)) {
                        return substring.replaceAll("([\\+\\-]\\d\\d):(\\d\\d)", "$1$2");
                    }
                    break;
                }
                case 'Z': {
                    if (substring.length() == "HHmmss".length() + zzjz.zzVs[0].length()) {
                        return substring.substring(0, substring.length() - 1) + "+0000";
                    }
                    break;
                }
            }
        }
        return null;
    }
    
    private static boolean zzbN(final String s) {
        final int length = s.length();
        final int length2 = "HHmmss".length();
        return length == zzjz.zzVs[1].length() + length2 || length == zzjz.zzVs[2].length() + length2 || length == length2 + zzjz.zzVs[3].length();
    }
}
