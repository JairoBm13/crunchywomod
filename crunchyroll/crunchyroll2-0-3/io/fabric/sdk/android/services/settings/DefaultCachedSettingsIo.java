// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.settings;

import org.json.JSONObject;
import io.fabric.sdk.android.Kit;

class DefaultCachedSettingsIo implements CachedSettingsIo
{
    private final Kit kit;
    
    public DefaultCachedSettingsIo(final Kit kit) {
        this.kit = kit;
    }
    
    @Override
    public JSONObject readCachedSettings() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //     3: ldc             "Fabric"
        //     5: ldc             "Reading cached settings..."
        //     7: invokeinterface io/fabric/sdk/android/Logger.d:(Ljava/lang/String;Ljava/lang/String;)V
        //    12: aconst_null    
        //    13: astore_3       
        //    14: aconst_null    
        //    15: astore          5
        //    17: aconst_null    
        //    18: astore_2       
        //    19: aconst_null    
        //    20: astore          4
        //    22: aload           5
        //    24: astore_1       
        //    25: new             Ljava/io/File;
        //    28: dup            
        //    29: new             Lio/fabric/sdk/android/services/persistence/FileStoreImpl;
        //    32: dup            
        //    33: aload_0        
        //    34: getfield        io/fabric/sdk/android/services/settings/DefaultCachedSettingsIo.kit:Lio/fabric/sdk/android/Kit;
        //    37: invokespecial   io/fabric/sdk/android/services/persistence/FileStoreImpl.<init>:(Lio/fabric/sdk/android/Kit;)V
        //    40: invokevirtual   io/fabric/sdk/android/services/persistence/FileStoreImpl.getFilesDir:()Ljava/io/File;
        //    43: ldc             "com.crashlytics.settings.json"
        //    45: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    48: astore          6
        //    50: aload           5
        //    52: astore_1       
        //    53: aload           6
        //    55: invokevirtual   java/io/File.exists:()Z
        //    58: ifeq            94
        //    61: aload           5
        //    63: astore_1       
        //    64: new             Ljava/io/FileInputStream;
        //    67: dup            
        //    68: aload           6
        //    70: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //    73: astore_2       
        //    74: new             Lorg/json/JSONObject;
        //    77: dup            
        //    78: aload_2        
        //    79: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.streamToString:(Ljava/io/InputStream;)Ljava/lang/String;
        //    82: invokespecial   org/json/JSONObject.<init>:(Ljava/lang/String;)V
        //    85: astore_1       
        //    86: aload_2        
        //    87: ldc             "Error while closing settings cache file."
        //    89: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //    92: aload_1        
        //    93: areturn        
        //    94: aload           5
        //    96: astore_1       
        //    97: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   100: ldc             "Fabric"
        //   102: ldc             "No cached settings found."
        //   104: invokeinterface io/fabric/sdk/android/Logger.d:(Ljava/lang/String;Ljava/lang/String;)V
        //   109: aload           4
        //   111: astore_1       
        //   112: goto            86
        //   115: astore_1       
        //   116: aload_3        
        //   117: astore_2       
        //   118: aload_1        
        //   119: astore_3       
        //   120: aload_2        
        //   121: astore_1       
        //   122: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   125: ldc             "Fabric"
        //   127: ldc             "Failed to fetch cached settings"
        //   129: aload_3        
        //   130: invokeinterface io/fabric/sdk/android/Logger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   135: aload_2        
        //   136: ldc             "Error while closing settings cache file."
        //   138: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   141: aconst_null    
        //   142: areturn        
        //   143: astore_2       
        //   144: aload_1        
        //   145: ldc             "Error while closing settings cache file."
        //   147: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   150: aload_2        
        //   151: athrow         
        //   152: astore_3       
        //   153: aload_2        
        //   154: astore_1       
        //   155: aload_3        
        //   156: astore_2       
        //   157: goto            144
        //   160: astore_3       
        //   161: goto            120
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  25     50     115    120    Ljava/lang/Exception;
        //  25     50     143    144    Any
        //  53     61     115    120    Ljava/lang/Exception;
        //  53     61     143    144    Any
        //  64     74     115    120    Ljava/lang/Exception;
        //  64     74     143    144    Any
        //  74     86     160    164    Ljava/lang/Exception;
        //  74     86     152    160    Any
        //  97     109    115    120    Ljava/lang/Exception;
        //  97     109    143    144    Any
        //  122    135    143    144    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0086:
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
    public void writeCachedSettings(final long p0, final JSONObject p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //     3: ldc             "Fabric"
        //     5: ldc             "Writing settings to cache file..."
        //     7: invokeinterface io/fabric/sdk/android/Logger.d:(Ljava/lang/String;Ljava/lang/String;)V
        //    12: aload_3        
        //    13: ifnull          91
        //    16: aconst_null    
        //    17: astore          5
        //    19: aconst_null    
        //    20: astore          6
        //    22: aload           5
        //    24: astore          4
        //    26: aload_3        
        //    27: ldc             "expires_at"
        //    29: lload_1        
        //    30: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;J)Lorg/json/JSONObject;
        //    33: pop            
        //    34: aload           5
        //    36: astore          4
        //    38: new             Ljava/io/FileWriter;
        //    41: dup            
        //    42: new             Ljava/io/File;
        //    45: dup            
        //    46: new             Lio/fabric/sdk/android/services/persistence/FileStoreImpl;
        //    49: dup            
        //    50: aload_0        
        //    51: getfield        io/fabric/sdk/android/services/settings/DefaultCachedSettingsIo.kit:Lio/fabric/sdk/android/Kit;
        //    54: invokespecial   io/fabric/sdk/android/services/persistence/FileStoreImpl.<init>:(Lio/fabric/sdk/android/Kit;)V
        //    57: invokevirtual   io/fabric/sdk/android/services/persistence/FileStoreImpl.getFilesDir:()Ljava/io/File;
        //    60: ldc             "com.crashlytics.settings.json"
        //    62: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    65: invokespecial   java/io/FileWriter.<init>:(Ljava/io/File;)V
        //    68: astore          5
        //    70: aload           5
        //    72: aload_3        
        //    73: invokevirtual   org/json/JSONObject.toString:()Ljava/lang/String;
        //    76: invokevirtual   java/io/FileWriter.write:(Ljava/lang/String;)V
        //    79: aload           5
        //    81: invokevirtual   java/io/FileWriter.flush:()V
        //    84: aload           5
        //    86: ldc             "Failed to close settings writer."
        //    88: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //    91: return         
        //    92: astore          5
        //    94: aload           6
        //    96: astore_3       
        //    97: aload_3        
        //    98: astore          4
        //   100: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   103: ldc             "Fabric"
        //   105: ldc             "Failed to cache settings"
        //   107: aload           5
        //   109: invokeinterface io/fabric/sdk/android/Logger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   114: aload_3        
        //   115: ldc             "Failed to close settings writer."
        //   117: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   120: return         
        //   121: astore_3       
        //   122: aload           4
        //   124: ldc             "Failed to close settings writer."
        //   126: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   129: aload_3        
        //   130: athrow         
        //   131: astore_3       
        //   132: aload           5
        //   134: astore          4
        //   136: goto            122
        //   139: astore          4
        //   141: aload           5
        //   143: astore_3       
        //   144: aload           4
        //   146: astore          5
        //   148: goto            97
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  26     34     92     97     Ljava/lang/Exception;
        //  26     34     121    122    Any
        //  38     70     92     97     Ljava/lang/Exception;
        //  38     70     121    122    Any
        //  70     84     139    151    Ljava/lang/Exception;
        //  70     84     131    139    Any
        //  100    114    121    122    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0091:
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
