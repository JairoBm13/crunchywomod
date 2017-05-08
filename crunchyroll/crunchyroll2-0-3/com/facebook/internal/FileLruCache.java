// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.internal;

import org.json.JSONException;
import org.json.JSONTokener;
import java.security.InvalidParameterException;
import java.io.FilenameFilter;
import java.io.OutputStream;
import org.json.JSONObject;
import com.facebook.LoggingBehavior;
import java.util.Date;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.facebook.Settings;
import android.content.Context;
import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

public final class FileLruCache
{
    private static final String HEADER_CACHEKEY_KEY = "key";
    private static final String HEADER_CACHE_CONTENT_TAG_KEY = "tag";
    static final String TAG;
    private static final AtomicLong bufferIndex;
    private final File directory;
    private boolean isTrimInProgress;
    private boolean isTrimPending;
    private AtomicLong lastClearCacheTime;
    private final Limits limits;
    private final Object lock;
    private final String tag;
    
    static {
        TAG = FileLruCache.class.getSimpleName();
        bufferIndex = new AtomicLong();
    }
    
    public FileLruCache(final Context context, final String tag, final Limits limits) {
        this.lastClearCacheTime = new AtomicLong(0L);
        this.tag = tag;
        this.limits = limits;
        this.directory = new File(context.getCacheDir(), tag);
        this.lock = new Object();
        if (this.directory.mkdirs() || this.directory.isDirectory()) {
            BufferFile.deleteAll(this.directory);
        }
    }
    
    private void postTrim() {
        synchronized (this.lock) {
            if (!this.isTrimPending) {
                this.isTrimPending = true;
                Settings.getExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        FileLruCache.this.trim();
                    }
                });
            }
        }
    }
    
    private void renameToTargetAndTrim(final String s, final File file) {
        if (!file.renameTo(new File(this.directory, Utility.md5hash(s)))) {
            file.delete();
        }
        this.postTrim();
    }
    
    private void trim() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: getfield        com/facebook/internal/FileLruCache.lock:Ljava/lang/Object;
        //     4: astore          11
        //     6: aload           11
        //     8: monitorenter   
        //     9: aload_0        
        //    10: iconst_0       
        //    11: putfield        com/facebook/internal/FileLruCache.isTrimPending:Z
        //    14: aload_0        
        //    15: iconst_1       
        //    16: putfield        com/facebook/internal/FileLruCache.isTrimInProgress:Z
        //    19: aload           11
        //    21: monitorexit    
        //    22: getstatic       com/facebook/LoggingBehavior.CACHE:Lcom/facebook/LoggingBehavior;
        //    25: getstatic       com/facebook/internal/FileLruCache.TAG:Ljava/lang/String;
        //    28: ldc             "trim started"
        //    30: invokestatic    com/facebook/internal/Logger.log:(Lcom/facebook/LoggingBehavior;Ljava/lang/String;Ljava/lang/String;)V
        //    33: new             Ljava/util/PriorityQueue;
        //    36: dup            
        //    37: invokespecial   java/util/PriorityQueue.<init>:()V
        //    40: astore          11
        //    42: lconst_0       
        //    43: lstore          7
        //    45: lconst_0       
        //    46: lstore          5
        //    48: aload_0        
        //    49: getfield        com/facebook/internal/FileLruCache.directory:Ljava/io/File;
        //    52: invokestatic    com/facebook/internal/FileLruCache$BufferFile.excludeBufferFiles:()Ljava/io/FilenameFilter;
        //    55: invokevirtual   java/io/File.listFiles:(Ljava/io/FilenameFilter;)[Ljava/io/File;
        //    58: astore          12
        //    60: lload           5
        //    62: lstore_3       
        //    63: lload           7
        //    65: lstore          9
        //    67: aload           12
        //    69: ifnull          199
        //    72: aload           12
        //    74: arraylength    
        //    75: istore_2       
        //    76: iconst_0       
        //    77: istore_1       
        //    78: lload           5
        //    80: lstore_3       
        //    81: lload           7
        //    83: lstore          9
        //    85: iload_1        
        //    86: iload_2        
        //    87: if_icmpge       199
        //    90: aload           12
        //    92: iload_1        
        //    93: aaload         
        //    94: astore          13
        //    96: new             Lcom/facebook/internal/FileLruCache$ModifiedFile;
        //    99: dup            
        //   100: aload           13
        //   102: invokespecial   com/facebook/internal/FileLruCache$ModifiedFile.<init>:(Ljava/io/File;)V
        //   105: astore          14
        //   107: aload           11
        //   109: aload           14
        //   111: invokevirtual   java/util/PriorityQueue.add:(Ljava/lang/Object;)Z
        //   114: pop            
        //   115: getstatic       com/facebook/LoggingBehavior.CACHE:Lcom/facebook/LoggingBehavior;
        //   118: getstatic       com/facebook/internal/FileLruCache.TAG:Ljava/lang/String;
        //   121: new             Ljava/lang/StringBuilder;
        //   124: dup            
        //   125: invokespecial   java/lang/StringBuilder.<init>:()V
        //   128: ldc             "  trim considering time="
        //   130: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   133: aload           14
        //   135: invokevirtual   com/facebook/internal/FileLruCache$ModifiedFile.getModified:()J
        //   138: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   141: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   144: ldc             " name="
        //   146: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   149: aload           14
        //   151: invokevirtual   com/facebook/internal/FileLruCache$ModifiedFile.getFile:()Ljava/io/File;
        //   154: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //   157: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   160: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   163: invokestatic    com/facebook/internal/Logger.log:(Lcom/facebook/LoggingBehavior;Ljava/lang/String;Ljava/lang/String;)V
        //   166: aload           13
        //   168: invokevirtual   java/io/File.length:()J
        //   171: lstore_3       
        //   172: lload           7
        //   174: lload_3        
        //   175: ladd           
        //   176: lstore          7
        //   178: lload           5
        //   180: lconst_1       
        //   181: ladd           
        //   182: lstore          5
        //   184: iload_1        
        //   185: iconst_1       
        //   186: iadd           
        //   187: istore_1       
        //   188: goto            78
        //   191: astore          12
        //   193: aload           11
        //   195: monitorexit    
        //   196: aload           12
        //   198: athrow         
        //   199: lload           9
        //   201: aload_0        
        //   202: getfield        com/facebook/internal/FileLruCache.limits:Lcom/facebook/internal/FileLruCache$Limits;
        //   205: invokevirtual   com/facebook/internal/FileLruCache$Limits.getByteCount:()I
        //   208: i2l            
        //   209: lcmp           
        //   210: ifgt            226
        //   213: lload_3        
        //   214: aload_0        
        //   215: getfield        com/facebook/internal/FileLruCache.limits:Lcom/facebook/internal/FileLruCache$Limits;
        //   218: invokevirtual   com/facebook/internal/FileLruCache$Limits.getFileCount:()I
        //   221: i2l            
        //   222: lcmp           
        //   223: ifle            323
        //   226: aload           11
        //   228: invokevirtual   java/util/PriorityQueue.remove:()Ljava/lang/Object;
        //   231: checkcast       Lcom/facebook/internal/FileLruCache$ModifiedFile;
        //   234: invokevirtual   com/facebook/internal/FileLruCache$ModifiedFile.getFile:()Ljava/io/File;
        //   237: astore          12
        //   239: getstatic       com/facebook/LoggingBehavior.CACHE:Lcom/facebook/LoggingBehavior;
        //   242: getstatic       com/facebook/internal/FileLruCache.TAG:Ljava/lang/String;
        //   245: new             Ljava/lang/StringBuilder;
        //   248: dup            
        //   249: invokespecial   java/lang/StringBuilder.<init>:()V
        //   252: ldc             "  trim removing "
        //   254: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   257: aload           12
        //   259: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //   262: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   265: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   268: invokestatic    com/facebook/internal/Logger.log:(Lcom/facebook/LoggingBehavior;Ljava/lang/String;Ljava/lang/String;)V
        //   271: lload           9
        //   273: aload           12
        //   275: invokevirtual   java/io/File.length:()J
        //   278: lsub           
        //   279: lstore          9
        //   281: lload_3        
        //   282: lconst_1       
        //   283: lsub           
        //   284: lstore_3       
        //   285: aload           12
        //   287: invokevirtual   java/io/File.delete:()Z
        //   290: pop            
        //   291: goto            199
        //   294: astore          12
        //   296: aload_0        
        //   297: getfield        com/facebook/internal/FileLruCache.lock:Ljava/lang/Object;
        //   300: astore          11
        //   302: aload           11
        //   304: monitorenter   
        //   305: aload_0        
        //   306: iconst_0       
        //   307: putfield        com/facebook/internal/FileLruCache.isTrimInProgress:Z
        //   310: aload_0        
        //   311: getfield        com/facebook/internal/FileLruCache.lock:Ljava/lang/Object;
        //   314: invokevirtual   java/lang/Object.notifyAll:()V
        //   317: aload           11
        //   319: monitorexit    
        //   320: aload           12
        //   322: athrow         
        //   323: aload_0        
        //   324: getfield        com/facebook/internal/FileLruCache.lock:Ljava/lang/Object;
        //   327: astore          11
        //   329: aload           11
        //   331: monitorenter   
        //   332: aload_0        
        //   333: iconst_0       
        //   334: putfield        com/facebook/internal/FileLruCache.isTrimInProgress:Z
        //   337: aload_0        
        //   338: getfield        com/facebook/internal/FileLruCache.lock:Ljava/lang/Object;
        //   341: invokevirtual   java/lang/Object.notifyAll:()V
        //   344: aload           11
        //   346: monitorexit    
        //   347: return         
        //   348: astore          12
        //   350: aload           11
        //   352: monitorexit    
        //   353: aload           12
        //   355: athrow         
        //   356: astore          12
        //   358: aload           11
        //   360: monitorexit    
        //   361: aload           12
        //   363: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  9      22     191    199    Any
        //  22     42     294    364    Any
        //  48     60     294    364    Any
        //  72     76     294    364    Any
        //  96     172    294    364    Any
        //  193    196    191    199    Any
        //  199    226    294    364    Any
        //  226    281    294    364    Any
        //  285    291    294    364    Any
        //  305    320    356    364    Any
        //  332    347    348    356    Any
        //  350    353    348    356    Any
        //  358    361    356    364    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0078:
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
    
    public void clearCache() {
        final File[] listFiles = this.directory.listFiles(BufferFile.excludeBufferFiles());
        this.lastClearCacheTime.set(System.currentTimeMillis());
        if (listFiles != null) {
            Settings.getExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    final File[] val$filesToDelete = listFiles;
                    for (int length = val$filesToDelete.length, i = 0; i < length; ++i) {
                        val$filesToDelete[i].delete();
                    }
                }
            });
        }
    }
    
    public InputStream get(final String s) throws IOException {
        return this.get(s, null);
    }
    
    public InputStream get(String optString, final String s) throws IOException {
        final File file = new File(this.directory, Utility.md5hash(optString));
        Label_0068: {
            InputStream inputStream;
            try {
                inputStream = new FileInputStream(file);
                final InputStream inputStream2;
                inputStream = (inputStream2 = new BufferedInputStream(inputStream, 8192));
                final JSONObject jsonObject = StreamHeader.readHeader(inputStream2);
                final JSONObject jsonObject3;
                final JSONObject jsonObject2 = jsonObject3 = jsonObject;
                if (jsonObject3 == null) {
                    return null;
                }
                break Label_0068;
            }
            catch (IOException ex) {
                return null;
            }
            try {
                final InputStream inputStream2 = inputStream;
                final JSONObject jsonObject = StreamHeader.readHeader(inputStream2);
                final JSONObject jsonObject3;
                final JSONObject jsonObject2 = jsonObject3 = jsonObject;
                if (jsonObject3 == null) {
                    return null;
                }
                final String optString2 = jsonObject2.optString("key");
                if (optString2 == null || !optString2.equals(optString)) {
                    return null;
                }
                optString = jsonObject2.optString("tag", (String)null);
                if ((s == null && optString != null) || (s != null && !s.equals(optString))) {
                    return null;
                }
                final long time = new Date().getTime();
                Logger.log(LoggingBehavior.CACHE, FileLruCache.TAG, "Setting lastModified to " + (Object)time + " for " + file.getName());
                file.setLastModified(time);
                if (!true) {
                    ((BufferedInputStream)inputStream).close();
                }
                return inputStream;
            }
            finally {
                if (!false) {
                    ((BufferedInputStream)inputStream).close();
                }
            }
        }
    }
    
    public InputStream interceptAndPut(final String s, final InputStream inputStream) throws IOException {
        return new CopyingInputStream(inputStream, this.openPutStream(s));
    }
    
    OutputStream openPutStream(final String s) throws IOException {
        return this.openPutStream(s, null);
    }
    
    public OutputStream openPutStream(final String p0, final String p1) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: getfield        com/facebook/internal/FileLruCache.directory:Ljava/io/File;
        //     4: invokestatic    com/facebook/internal/FileLruCache$BufferFile.newFile:(Ljava/io/File;)Ljava/io/File;
        //     7: astore_3       
        //     8: aload_3        
        //     9: invokevirtual   java/io/File.delete:()Z
        //    12: pop            
        //    13: aload_3        
        //    14: invokevirtual   java/io/File.createNewFile:()Z
        //    17: ifne            51
        //    20: new             Ljava/io/IOException;
        //    23: dup            
        //    24: new             Ljava/lang/StringBuilder;
        //    27: dup            
        //    28: invokespecial   java/lang/StringBuilder.<init>:()V
        //    31: ldc_w           "Could not create file at "
        //    34: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    37: aload_3        
        //    38: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //    41: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    44: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    47: invokespecial   java/io/IOException.<init>:(Ljava/lang/String;)V
        //    50: athrow         
        //    51: new             Ljava/io/FileOutputStream;
        //    54: dup            
        //    55: aload_3        
        //    56: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //    59: astore          4
        //    61: new             Ljava/io/BufferedOutputStream;
        //    64: dup            
        //    65: new             Lcom/facebook/internal/FileLruCache$CloseCallbackOutputStream;
        //    68: dup            
        //    69: aload           4
        //    71: new             Lcom/facebook/internal/FileLruCache$1;
        //    74: dup            
        //    75: aload_0        
        //    76: invokestatic    java/lang/System.currentTimeMillis:()J
        //    79: aload_3        
        //    80: aload_1        
        //    81: invokespecial   com/facebook/internal/FileLruCache$1.<init>:(Lcom/facebook/internal/FileLruCache;JLjava/io/File;Ljava/lang/String;)V
        //    84: invokespecial   com/facebook/internal/FileLruCache$CloseCallbackOutputStream.<init>:(Ljava/io/OutputStream;Lcom/facebook/internal/FileLruCache$StreamCloseCallback;)V
        //    87: sipush          8192
        //    90: invokespecial   java/io/BufferedOutputStream.<init>:(Ljava/io/OutputStream;I)V
        //    93: astore_3       
        //    94: new             Lorg/json/JSONObject;
        //    97: dup            
        //    98: invokespecial   org/json/JSONObject.<init>:()V
        //   101: astore          4
        //   103: aload           4
        //   105: ldc             "key"
        //   107: aload_1        
        //   108: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   111: pop            
        //   112: aload_2        
        //   113: invokestatic    com/facebook/internal/Utility.isNullOrEmpty:(Ljava/lang/String;)Z
        //   116: ifne            128
        //   119: aload           4
        //   121: ldc             "tag"
        //   123: aload_2        
        //   124: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   127: pop            
        //   128: aload_3        
        //   129: aload           4
        //   131: invokestatic    com/facebook/internal/FileLruCache$StreamHeader.writeHeader:(Ljava/io/OutputStream;Lorg/json/JSONObject;)V
        //   134: iconst_1       
        //   135: ifne            142
        //   138: aload_3        
        //   139: invokevirtual   java/io/BufferedOutputStream.close:()V
        //   142: aload_3        
        //   143: areturn        
        //   144: astore_1       
        //   145: getstatic       com/facebook/LoggingBehavior.CACHE:Lcom/facebook/LoggingBehavior;
        //   148: iconst_5       
        //   149: getstatic       com/facebook/internal/FileLruCache.TAG:Ljava/lang/String;
        //   152: new             Ljava/lang/StringBuilder;
        //   155: dup            
        //   156: invokespecial   java/lang/StringBuilder.<init>:()V
        //   159: ldc_w           "Error creating buffer output stream: "
        //   162: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   165: aload_1        
        //   166: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   169: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   172: invokestatic    com/facebook/internal/Logger.log:(Lcom/facebook/LoggingBehavior;ILjava/lang/String;Ljava/lang/String;)V
        //   175: new             Ljava/io/IOException;
        //   178: dup            
        //   179: aload_1        
        //   180: invokevirtual   java/io/FileNotFoundException.getMessage:()Ljava/lang/String;
        //   183: invokespecial   java/io/IOException.<init>:(Ljava/lang/String;)V
        //   186: athrow         
        //   187: astore_1       
        //   188: getstatic       com/facebook/LoggingBehavior.CACHE:Lcom/facebook/LoggingBehavior;
        //   191: iconst_5       
        //   192: getstatic       com/facebook/internal/FileLruCache.TAG:Ljava/lang/String;
        //   195: new             Ljava/lang/StringBuilder;
        //   198: dup            
        //   199: invokespecial   java/lang/StringBuilder.<init>:()V
        //   202: ldc_w           "Error creating JSON header for cache file: "
        //   205: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   208: aload_1        
        //   209: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   212: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   215: invokestatic    com/facebook/internal/Logger.log:(Lcom/facebook/LoggingBehavior;ILjava/lang/String;Ljava/lang/String;)V
        //   218: new             Ljava/io/IOException;
        //   221: dup            
        //   222: aload_1        
        //   223: invokevirtual   org/json/JSONException.getMessage:()Ljava/lang/String;
        //   226: invokespecial   java/io/IOException.<init>:(Ljava/lang/String;)V
        //   229: athrow         
        //   230: astore_1       
        //   231: iconst_0       
        //   232: ifne            239
        //   235: aload_3        
        //   236: invokevirtual   java/io/BufferedOutputStream.close:()V
        //   239: aload_1        
        //   240: athrow         
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                           
        //  -----  -----  -----  -----  -------------------------------
        //  51     61     144    187    Ljava/io/FileNotFoundException;
        //  94     128    187    230    Lorg/json/JSONException;
        //  94     128    230    241    Any
        //  128    134    187    230    Lorg/json/JSONException;
        //  128    134    230    241    Any
        //  188    230    230    241    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0128:
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
    
    long sizeInBytesForTest() {
        Object o = this.lock;
        long n2;
        synchronized (o) {
            while (true) {
                if (!this.isTrimPending) {
                    if (!this.isTrimInProgress) {
                        break;
                    }
                }
                try {
                    this.lock.wait();
                }
                catch (InterruptedException ex) {}
            }
            // monitorexit(o)
            o = this.directory.listFiles();
            long n = n2 = 0L;
            if (o != null) {
                final int length = o.length;
                int n3 = 0;
                while (true) {
                    n2 = n;
                    if (n3 >= length) {
                        break;
                    }
                    n += o[n3].length();
                    ++n3;
                }
            }
        }
        return n2;
    }
    
    @Override
    public String toString() {
        return "{FileLruCache: tag:" + this.tag + " file:" + this.directory.getName() + "}";
    }
    
    private static class BufferFile
    {
        private static final String FILE_NAME_PREFIX = "buffer";
        private static final FilenameFilter filterExcludeBufferFiles;
        private static final FilenameFilter filterExcludeNonBufferFiles;
        
        static {
            filterExcludeBufferFiles = new FilenameFilter() {
                @Override
                public boolean accept(final File file, final String s) {
                    return !s.startsWith("buffer");
                }
            };
            filterExcludeNonBufferFiles = new FilenameFilter() {
                @Override
                public boolean accept(final File file, final String s) {
                    return s.startsWith("buffer");
                }
            };
        }
        
        static void deleteAll(final File file) {
            final File[] listFiles = file.listFiles(excludeNonBufferFiles());
            if (listFiles != null) {
                for (int length = listFiles.length, i = 0; i < length; ++i) {
                    listFiles[i].delete();
                }
            }
        }
        
        static FilenameFilter excludeBufferFiles() {
            return BufferFile.filterExcludeBufferFiles;
        }
        
        static FilenameFilter excludeNonBufferFiles() {
            return BufferFile.filterExcludeNonBufferFiles;
        }
        
        static File newFile(final File file) {
            return new File(file, "buffer" + Long.valueOf(FileLruCache.bufferIndex.incrementAndGet()).toString());
        }
    }
    
    private static class CloseCallbackOutputStream extends OutputStream
    {
        final StreamCloseCallback callback;
        final OutputStream innerStream;
        
        CloseCallbackOutputStream(final OutputStream innerStream, final StreamCloseCallback callback) {
            this.innerStream = innerStream;
            this.callback = callback;
        }
        
        @Override
        public void close() throws IOException {
            try {
                this.innerStream.close();
            }
            finally {
                this.callback.onClose();
            }
        }
        
        @Override
        public void flush() throws IOException {
            this.innerStream.flush();
        }
        
        @Override
        public void write(final int n) throws IOException {
            this.innerStream.write(n);
        }
        
        @Override
        public void write(final byte[] array) throws IOException {
            this.innerStream.write(array);
        }
        
        @Override
        public void write(final byte[] array, final int n, final int n2) throws IOException {
            this.innerStream.write(array, n, n2);
        }
    }
    
    private static final class CopyingInputStream extends InputStream
    {
        final InputStream input;
        final OutputStream output;
        
        CopyingInputStream(final InputStream input, final OutputStream output) {
            this.input = input;
            this.output = output;
        }
        
        @Override
        public int available() throws IOException {
            return this.input.available();
        }
        
        @Override
        public void close() throws IOException {
            try {
                this.input.close();
            }
            finally {
                this.output.close();
            }
        }
        
        @Override
        public void mark(final int n) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean markSupported() {
            return false;
        }
        
        @Override
        public int read() throws IOException {
            final int read = this.input.read();
            if (read >= 0) {
                this.output.write(read);
            }
            return read;
        }
        
        @Override
        public int read(final byte[] array) throws IOException {
            final int read = this.input.read(array);
            if (read > 0) {
                this.output.write(array, 0, read);
            }
            return read;
        }
        
        @Override
        public int read(final byte[] array, final int n, int read) throws IOException {
            read = this.input.read(array, n, read);
            if (read > 0) {
                this.output.write(array, n, read);
            }
            return read;
        }
        
        @Override
        public void reset() {
            synchronized (this) {
                throw new UnsupportedOperationException();
            }
        }
        
        @Override
        public long skip(final long n) throws IOException {
            final byte[] array = new byte[1024];
            long n2;
            int read;
            for (n2 = 0L; n2 < n; n2 += read) {
                read = this.read(array, 0, (int)Math.min(n - n2, array.length));
                if (read < 0) {
                    break;
                }
            }
            return n2;
        }
    }
    
    public static final class Limits
    {
        private int byteCount;
        private int fileCount;
        
        public Limits() {
            this.fileCount = 1024;
            this.byteCount = 1048576;
        }
        
        int getByteCount() {
            return this.byteCount;
        }
        
        int getFileCount() {
            return this.fileCount;
        }
        
        void setByteCount(final int byteCount) {
            if (byteCount < 0) {
                throw new InvalidParameterException("Cache byte-count limit must be >= 0");
            }
            this.byteCount = byteCount;
        }
        
        void setFileCount(final int fileCount) {
            if (fileCount < 0) {
                throw new InvalidParameterException("Cache file count limit must be >= 0");
            }
            this.fileCount = fileCount;
        }
    }
    
    private static final class ModifiedFile implements Comparable<ModifiedFile>
    {
        private static final int HASH_MULTIPLIER = 37;
        private static final int HASH_SEED = 29;
        private final File file;
        private final long modified;
        
        ModifiedFile(final File file) {
            this.file = file;
            this.modified = file.lastModified();
        }
        
        @Override
        public int compareTo(final ModifiedFile modifiedFile) {
            if (this.getModified() < modifiedFile.getModified()) {
                return -1;
            }
            if (this.getModified() > modifiedFile.getModified()) {
                return 1;
            }
            return this.getFile().compareTo(modifiedFile.getFile());
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof ModifiedFile && this.compareTo((ModifiedFile)o) == 0;
        }
        
        File getFile() {
            return this.file;
        }
        
        long getModified() {
            return this.modified;
        }
        
        @Override
        public int hashCode() {
            return (this.file.hashCode() + 1073) * 37 + (int)(this.modified % 2147483647L);
        }
    }
    
    private interface StreamCloseCallback
    {
        void onClose();
    }
    
    private static final class StreamHeader
    {
        private static final int HEADER_VERSION = 0;
        
        static JSONObject readHeader(final InputStream inputStream) throws IOException {
            if (inputStream.read() != 0) {
                return null;
            }
            int n = 0;
            for (int i = 0; i < 3; ++i) {
                final int read = inputStream.read();
                if (read == -1) {
                    Logger.log(LoggingBehavior.CACHE, FileLruCache.TAG, "readHeader: stream.read returned -1 while reading header size");
                    return null;
                }
                n = (n << 8) + (read & 0xFF);
            }
            final byte[] array = new byte[n];
            int read2;
            for (int j = 0; j < array.length; j += read2) {
                read2 = inputStream.read(array, j, array.length - j);
                if (read2 < 1) {
                    Logger.log(LoggingBehavior.CACHE, FileLruCache.TAG, "readHeader: stream.read stopped at " + (Object)j + " when expected " + array.length);
                    return null;
                }
            }
            final JSONTokener jsonTokener = new JSONTokener(new String(array));
            try {
                final Object nextValue = jsonTokener.nextValue();
                if (!(nextValue instanceof JSONObject)) {
                    Logger.log(LoggingBehavior.CACHE, FileLruCache.TAG, "readHeader: expected JSONObject, got " + ((JSONObject)nextValue).getClass().getCanonicalName());
                    return null;
                }
                return (JSONObject)nextValue;
            }
            catch (JSONException ex) {
                throw new IOException(ex.getMessage());
            }
        }
        
        static void writeHeader(final OutputStream outputStream, final JSONObject jsonObject) throws IOException {
            final byte[] bytes = jsonObject.toString().getBytes();
            outputStream.write(0);
            outputStream.write(bytes.length >> 16 & 0xFF);
            outputStream.write(bytes.length >> 8 & 0xFF);
            outputStream.write(bytes.length >> 0 & 0xFF);
            outputStream.write(bytes);
        }
    }
}
