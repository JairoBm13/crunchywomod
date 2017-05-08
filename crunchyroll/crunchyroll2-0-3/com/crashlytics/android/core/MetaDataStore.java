// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

import org.json.JSONException;
import java.util.Iterator;
import java.util.HashMap;
import org.json.JSONObject;
import java.util.Map;
import java.io.File;
import java.nio.charset.Charset;

class MetaDataStore
{
    private static final Charset UTF_8;
    private final File filesDir;
    
    static {
        UTF_8 = Charset.forName("UTF-8");
    }
    
    public MetaDataStore(final File filesDir) {
        this.filesDir = filesDir;
    }
    
    private File getKeysFileForSession(final String s) {
        return new File(this.filesDir, s + "keys" + ".meta");
    }
    
    private File getUserDataFileForSession(final String s) {
        return new File(this.filesDir, s + "user" + ".meta");
    }
    
    private static Map<String, String> jsonToKeysData(final String s) throws JSONException {
        final JSONObject jsonObject = new JSONObject(s);
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        final Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            final String s2 = keys.next();
            hashMap.put(s2, valueOrNull(jsonObject, s2));
        }
        return hashMap;
    }
    
    private static UserMetaData jsonToUserData(final String s) throws JSONException {
        final JSONObject jsonObject = new JSONObject(s);
        return new UserMetaData(valueOrNull(jsonObject, "userId"), valueOrNull(jsonObject, "userName"), valueOrNull(jsonObject, "userEmail"));
    }
    
    private static String keysDataToJson(final Map<String, String> map) throws JSONException {
        return new JSONObject((Map)map).toString();
    }
    
    private static String valueOrNull(final JSONObject jsonObject, final String s) {
        String optString = null;
        if (!jsonObject.isNull(s)) {
            optString = jsonObject.optString(s, (String)null);
        }
        return optString;
    }
    
    public Map<String, String> readKeyData(final String p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: aload_1        
        //     2: invokespecial   com/crashlytics/android/core/MetaDataStore.getKeysFileForSession:(Ljava/lang/String;)Ljava/io/File;
        //     5: astore_2       
        //     6: aload_2        
        //     7: invokevirtual   java/io/File.exists:()Z
        //    10: ifne            17
        //    13: invokestatic    java/util/Collections.emptyMap:()Ljava/util/Map;
        //    16: areturn        
        //    17: aconst_null    
        //    18: astore_1       
        //    19: aconst_null    
        //    20: astore_3       
        //    21: new             Ljava/io/FileInputStream;
        //    24: dup            
        //    25: aload_2        
        //    26: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //    29: astore_2       
        //    30: aload_2        
        //    31: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.streamToString:(Ljava/io/InputStream;)Ljava/lang/String;
        //    34: invokestatic    com/crashlytics/android/core/MetaDataStore.jsonToKeysData:(Ljava/lang/String;)Ljava/util/Map;
        //    37: astore_1       
        //    38: aload_2        
        //    39: ldc             "Failed to close user metadata file."
        //    41: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //    44: aload_1        
        //    45: areturn        
        //    46: astore_1       
        //    47: aload_3        
        //    48: astore_2       
        //    49: aload_1        
        //    50: astore_3       
        //    51: aload_2        
        //    52: astore_1       
        //    53: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //    56: ldc             "CrashlyticsCore"
        //    58: ldc             "Error deserializing user metadata."
        //    60: aload_3        
        //    61: invokeinterface io/fabric/sdk/android/Logger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //    66: aload_2        
        //    67: ldc             "Failed to close user metadata file."
        //    69: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //    72: invokestatic    java/util/Collections.emptyMap:()Ljava/util/Map;
        //    75: areturn        
        //    76: astore_2       
        //    77: aload_1        
        //    78: ldc             "Failed to close user metadata file."
        //    80: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //    83: aload_2        
        //    84: athrow         
        //    85: astore_3       
        //    86: aload_2        
        //    87: astore_1       
        //    88: aload_3        
        //    89: astore_2       
        //    90: goto            77
        //    93: astore_3       
        //    94: goto            51
        //    Signature:
        //  (Ljava/lang/String;)Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  21     30     46     51     Ljava/lang/Exception;
        //  21     30     76     77     Any
        //  30     38     93     97     Ljava/lang/Exception;
        //  30     38     85     93     Any
        //  53     66     76     77     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0051:
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
    
    public UserMetaData readUserData(final String p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: aload_1        
        //     2: invokespecial   com/crashlytics/android/core/MetaDataStore.getUserDataFileForSession:(Ljava/lang/String;)Ljava/io/File;
        //     5: astore_2       
        //     6: aload_2        
        //     7: invokevirtual   java/io/File.exists:()Z
        //    10: ifne            17
        //    13: getstatic       com/crashlytics/android/core/UserMetaData.EMPTY:Lcom/crashlytics/android/core/UserMetaData;
        //    16: areturn        
        //    17: aconst_null    
        //    18: astore_1       
        //    19: aconst_null    
        //    20: astore_3       
        //    21: new             Ljava/io/FileInputStream;
        //    24: dup            
        //    25: aload_2        
        //    26: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //    29: astore_2       
        //    30: aload_2        
        //    31: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.streamToString:(Ljava/io/InputStream;)Ljava/lang/String;
        //    34: invokestatic    com/crashlytics/android/core/MetaDataStore.jsonToUserData:(Ljava/lang/String;)Lcom/crashlytics/android/core/UserMetaData;
        //    37: astore_1       
        //    38: aload_2        
        //    39: ldc             "Failed to close user metadata file."
        //    41: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //    44: aload_1        
        //    45: areturn        
        //    46: astore_1       
        //    47: aload_3        
        //    48: astore_2       
        //    49: aload_1        
        //    50: astore_3       
        //    51: aload_2        
        //    52: astore_1       
        //    53: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //    56: ldc             "CrashlyticsCore"
        //    58: ldc             "Error deserializing user metadata."
        //    60: aload_3        
        //    61: invokeinterface io/fabric/sdk/android/Logger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //    66: aload_2        
        //    67: ldc             "Failed to close user metadata file."
        //    69: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //    72: getstatic       com/crashlytics/android/core/UserMetaData.EMPTY:Lcom/crashlytics/android/core/UserMetaData;
        //    75: areturn        
        //    76: astore_2       
        //    77: aload_1        
        //    78: ldc             "Failed to close user metadata file."
        //    80: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //    83: aload_2        
        //    84: athrow         
        //    85: astore_3       
        //    86: aload_2        
        //    87: astore_1       
        //    88: aload_3        
        //    89: astore_2       
        //    90: goto            77
        //    93: astore_3       
        //    94: goto            51
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  21     30     46     51     Ljava/lang/Exception;
        //  21     30     76     77     Any
        //  30     38     93     97     Ljava/lang/Exception;
        //  30     38     85     93     Any
        //  53     66     76     77     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0051:
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
    
    public void writeKeyData(final String p0, final Map<String, String> p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: aload_1        
        //     2: invokespecial   com/crashlytics/android/core/MetaDataStore.getKeysFileForSession:(Ljava/lang/String;)Ljava/io/File;
        //     5: astore          5
        //     7: aconst_null    
        //     8: astore          4
        //    10: aconst_null    
        //    11: astore_3       
        //    12: aload           4
        //    14: astore_1       
        //    15: aload_2        
        //    16: invokestatic    com/crashlytics/android/core/MetaDataStore.keysDataToJson:(Ljava/util/Map;)Ljava/lang/String;
        //    19: astore          6
        //    21: aload           4
        //    23: astore_1       
        //    24: new             Ljava/io/BufferedWriter;
        //    27: dup            
        //    28: new             Ljava/io/OutputStreamWriter;
        //    31: dup            
        //    32: new             Ljava/io/FileOutputStream;
        //    35: dup            
        //    36: aload           5
        //    38: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //    41: getstatic       com/crashlytics/android/core/MetaDataStore.UTF_8:Ljava/nio/charset/Charset;
        //    44: invokespecial   java/io/OutputStreamWriter.<init>:(Ljava/io/OutputStream;Ljava/nio/charset/Charset;)V
        //    47: invokespecial   java/io/BufferedWriter.<init>:(Ljava/io/Writer;)V
        //    50: astore_2       
        //    51: aload_2        
        //    52: aload           6
        //    54: invokevirtual   java/io/Writer.write:(Ljava/lang/String;)V
        //    57: aload_2        
        //    58: invokevirtual   java/io/Writer.flush:()V
        //    61: aload_2        
        //    62: ldc             "Failed to close key/value metadata file."
        //    64: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //    67: return         
        //    68: astore_1       
        //    69: aload_3        
        //    70: astore_2       
        //    71: aload_1        
        //    72: astore_3       
        //    73: aload_2        
        //    74: astore_1       
        //    75: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //    78: ldc             "CrashlyticsCore"
        //    80: ldc             "Error serializing key/value metadata."
        //    82: aload_3        
        //    83: invokeinterface io/fabric/sdk/android/Logger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //    88: aload_2        
        //    89: ldc             "Failed to close key/value metadata file."
        //    91: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //    94: return         
        //    95: astore_2       
        //    96: aload_1        
        //    97: ldc             "Failed to close key/value metadata file."
        //    99: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   102: aload_2        
        //   103: athrow         
        //   104: astore_3       
        //   105: aload_2        
        //   106: astore_1       
        //   107: aload_3        
        //   108: astore_2       
        //   109: goto            96
        //   112: astore_3       
        //   113: goto            73
        //    Signature:
        //  (Ljava/lang/String;Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;)V
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  15     21     68     73     Ljava/lang/Exception;
        //  15     21     95     96     Any
        //  24     51     68     73     Ljava/lang/Exception;
        //  24     51     95     96     Any
        //  51     61     112    116    Ljava/lang/Exception;
        //  51     61     104    112    Any
        //  75     88     95     96     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0073:
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
}
