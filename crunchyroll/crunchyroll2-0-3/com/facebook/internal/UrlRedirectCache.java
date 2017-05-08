// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.internal;

import java.io.OutputStream;
import com.facebook.LoggingBehavior;
import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import android.content.Context;

class UrlRedirectCache
{
    private static final String REDIRECT_CONTENT_TAG;
    static final String TAG;
    private static volatile FileLruCache urlRedirectCache;
    
    static {
        TAG = UrlRedirectCache.class.getSimpleName();
        REDIRECT_CONTENT_TAG = UrlRedirectCache.TAG + "_Redirect";
    }
    
    static void cacheUriRedirect(final Context context, final URI uri, final URI uri2) {
        if (uri == null || uri2 == null) {
            return;
        }
        Closeable openPutStream = null;
        try {
            ((OutputStream)(openPutStream = getCache(context).openPutStream(uri.toString(), UrlRedirectCache.REDIRECT_CONTENT_TAG))).write(uri2.toString().getBytes());
        }
        catch (IOException ex) {}
        finally {
            Utility.closeQuietly(openPutStream);
        }
    }
    
    static void clearCache(final Context context) {
        try {
            getCache(context).clearCache();
        }
        catch (IOException ex) {
            Logger.log(LoggingBehavior.CACHE, 5, UrlRedirectCache.TAG, "clearCache failed " + ex.getMessage());
        }
    }
    
    static FileLruCache getCache(final Context context) throws IOException {
        synchronized (UrlRedirectCache.class) {
            if (UrlRedirectCache.urlRedirectCache == null) {
                UrlRedirectCache.urlRedirectCache = new FileLruCache(context.getApplicationContext(), UrlRedirectCache.TAG, new FileLruCache.Limits());
            }
            return UrlRedirectCache.urlRedirectCache;
        }
    }
    
    static URI getRedirectedUri(final Context p0, final URI p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_1        
        //     1: ifnonnull       6
        //     4: aconst_null    
        //     5: areturn        
        //     6: aload_1        
        //     7: invokevirtual   java/net/URI.toString:()Ljava/lang/String;
        //    10: astore_1       
        //    11: aconst_null    
        //    12: astore          5
        //    14: aconst_null    
        //    15: astore          6
        //    17: aconst_null    
        //    18: astore          4
        //    20: aload_0        
        //    21: invokestatic    com/facebook/internal/UrlRedirectCache.getCache:(Landroid/content/Context;)Lcom/facebook/internal/FileLruCache;
        //    24: astore          7
        //    26: iconst_0       
        //    27: istore_2       
        //    28: aconst_null    
        //    29: astore_0       
        //    30: aload           7
        //    32: aload_1        
        //    33: getstatic       com/facebook/internal/UrlRedirectCache.REDIRECT_CONTENT_TAG:Ljava/lang/String;
        //    36: invokevirtual   com/facebook/internal/FileLruCache.get:(Ljava/lang/String;Ljava/lang/String;)Ljava/io/InputStream;
        //    39: astore          4
        //    41: aload           4
        //    43: ifnull          181
        //    46: iconst_1       
        //    47: istore_2       
        //    48: new             Ljava/io/InputStreamReader;
        //    51: dup            
        //    52: aload           4
        //    54: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
        //    57: astore_1       
        //    58: aload_1        
        //    59: astore          4
        //    61: aload_1        
        //    62: astore          5
        //    64: aload_1        
        //    65: astore          6
        //    67: sipush          128
        //    70: newarray        C
        //    72: astore_0       
        //    73: aload_1        
        //    74: astore          4
        //    76: aload_1        
        //    77: astore          5
        //    79: aload_1        
        //    80: astore          6
        //    82: new             Ljava/lang/StringBuilder;
        //    85: dup            
        //    86: invokespecial   java/lang/StringBuilder.<init>:()V
        //    89: astore          8
        //    91: aload_1        
        //    92: astore          4
        //    94: aload_1        
        //    95: astore          5
        //    97: aload_1        
        //    98: astore          6
        //   100: aload_1        
        //   101: aload_0        
        //   102: iconst_0       
        //   103: aload_0        
        //   104: arraylength    
        //   105: invokevirtual   java/io/InputStreamReader.read:([CII)I
        //   108: istore_3       
        //   109: iload_3        
        //   110: ifle            142
        //   113: aload_1        
        //   114: astore          4
        //   116: aload_1        
        //   117: astore          5
        //   119: aload_1        
        //   120: astore          6
        //   122: aload           8
        //   124: aload_0        
        //   125: iconst_0       
        //   126: iload_3        
        //   127: invokevirtual   java/lang/StringBuilder.append:([CII)Ljava/lang/StringBuilder;
        //   130: pop            
        //   131: goto            91
        //   134: astore_0       
        //   135: aload           4
        //   137: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
        //   140: aconst_null    
        //   141: areturn        
        //   142: aload_1        
        //   143: astore          4
        //   145: aload_1        
        //   146: astore          5
        //   148: aload_1        
        //   149: astore          6
        //   151: aload_1        
        //   152: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
        //   155: aload_1        
        //   156: astore          4
        //   158: aload_1        
        //   159: astore          5
        //   161: aload_1        
        //   162: astore          6
        //   164: aload           8
        //   166: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   169: astore_0       
        //   170: aload_0        
        //   171: astore          4
        //   173: aload_1        
        //   174: astore_0       
        //   175: aload           4
        //   177: astore_1       
        //   178: goto            30
        //   181: iload_2        
        //   182: ifeq            200
        //   185: new             Ljava/net/URI;
        //   188: dup            
        //   189: aload_1        
        //   190: invokespecial   java/net/URI.<init>:(Ljava/lang/String;)V
        //   193: astore_1       
        //   194: aload_0        
        //   195: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
        //   198: aload_1        
        //   199: areturn        
        //   200: aload_0        
        //   201: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
        //   204: aconst_null    
        //   205: areturn        
        //   206: astore_0       
        //   207: aload           5
        //   209: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
        //   212: aconst_null    
        //   213: areturn        
        //   214: astore_0       
        //   215: aload           6
        //   217: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
        //   220: aload_0        
        //   221: athrow         
        //   222: astore_1       
        //   223: aload_0        
        //   224: astore          6
        //   226: aload_1        
        //   227: astore_0       
        //   228: goto            215
        //   231: astore_1       
        //   232: aload_0        
        //   233: astore          5
        //   235: goto            207
        //   238: astore_1       
        //   239: aload_0        
        //   240: astore          4
        //   242: goto            135
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                         
        //  -----  -----  -----  -----  -----------------------------
        //  20     26     134    135    Ljava/net/URISyntaxException;
        //  20     26     206    207    Ljava/io/IOException;
        //  20     26     214    215    Any
        //  30     41     238    245    Ljava/net/URISyntaxException;
        //  30     41     231    238    Ljava/io/IOException;
        //  30     41     222    231    Any
        //  48     58     238    245    Ljava/net/URISyntaxException;
        //  48     58     231    238    Ljava/io/IOException;
        //  48     58     222    231    Any
        //  67     73     134    135    Ljava/net/URISyntaxException;
        //  67     73     206    207    Ljava/io/IOException;
        //  67     73     214    215    Any
        //  82     91     134    135    Ljava/net/URISyntaxException;
        //  82     91     206    207    Ljava/io/IOException;
        //  82     91     214    215    Any
        //  100    109    134    135    Ljava/net/URISyntaxException;
        //  100    109    206    207    Ljava/io/IOException;
        //  100    109    214    215    Any
        //  122    131    134    135    Ljava/net/URISyntaxException;
        //  122    131    206    207    Ljava/io/IOException;
        //  122    131    214    215    Any
        //  151    155    134    135    Ljava/net/URISyntaxException;
        //  151    155    206    207    Ljava/io/IOException;
        //  151    155    214    215    Any
        //  164    170    134    135    Ljava/net/URISyntaxException;
        //  164    170    206    207    Ljava/io/IOException;
        //  164    170    214    215    Any
        //  185    194    238    245    Ljava/net/URISyntaxException;
        //  185    194    231    238    Ljava/io/IOException;
        //  185    194    222    231    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0030:
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
