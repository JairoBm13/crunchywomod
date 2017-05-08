// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.vast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URL;
import java.io.InputStream;
import javax.xml.parsers.FactoryConfigurationError;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.lang.ref.WeakReference;
import com.secondtv.android.ads.AdTrackListener;
import android.content.Context;
import com.secondtv.android.ads.vast.types.Playlist;
import com.secondtv.android.ads.util.SafeAsyncTask;

public class LoadVastResourceAsyncTask extends SafeAsyncTask<Playlist>
{
    private static final int CONNECTION_TIMEOUT_MS = 5000;
    private static final int SOCKET_TIMEOUT_MS = 10000;
    private Context mContext;
    private AdTrackListener mTrackListener;
    private String url;
    private WeakReference<OnLoadVastListener> vastLoaderRef;
    
    public LoadVastResourceAsyncTask(final Context mContext, final String url, final AdTrackListener mTrackListener) {
        this.url = url;
        this.mContext = mContext;
        this.mTrackListener = mTrackListener;
    }
    
    private SAXParser getParser() throws ParserConfigurationException, SAXException, FactoryConfigurationError {
        return SAXParserFactory.newInstance().newSAXParser();
    }
    
    private InputStream request(final String s) throws MalformedURLException, IOException {
        final URLConnection openConnection = new URL(s).openConnection();
        openConnection.setConnectTimeout(5000);
        openConnection.setReadTimeout(10000);
        return openConnection.getInputStream();
    }
    
    @Override
    public Playlist call() throws Exception {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: aload_0        
        //     2: getfield        com/secondtv/android/ads/vast/LoadVastResourceAsyncTask.url:Ljava/lang/String;
        //     5: invokespecial   com/secondtv/android/ads/vast/LoadVastResourceAsyncTask.request:(Ljava/lang/String;)Ljava/io/InputStream;
        //     8: astore_2       
        //     9: aload_0        
        //    10: invokespecial   com/secondtv/android/ads/vast/LoadVastResourceAsyncTask.getParser:()Ljavax/xml/parsers/SAXParser;
        //    13: astore_1       
        //    14: new             Lcom/secondtv/android/ads/vast/VastRootHandler;
        //    17: dup            
        //    18: invokespecial   com/secondtv/android/ads/vast/VastRootHandler.<init>:()V
        //    21: astore_3       
        //    22: aload_0        
        //    23: getfield        com/secondtv/android/ads/vast/LoadVastResourceAsyncTask.mTrackListener:Lcom/secondtv/android/ads/AdTrackListener;
        //    26: ifnull          40
        //    29: aload_0        
        //    30: getfield        com/secondtv/android/ads/vast/LoadVastResourceAsyncTask.mTrackListener:Lcom/secondtv/android/ads/AdTrackListener;
        //    33: aload_0        
        //    34: getfield        com/secondtv/android/ads/vast/LoadVastResourceAsyncTask.mContext:Landroid/content/Context;
        //    37: invokevirtual   com/secondtv/android/ads/AdTrackListener.onVastAdParseStart:(Landroid/content/Context;)V
        //    40: aload_1        
        //    41: aload_2        
        //    42: aload_3        
        //    43: invokevirtual   javax/xml/parsers/SAXParser.parse:(Ljava/io/InputStream;Lorg/xml/sax/helpers/DefaultHandler;)V
        //    46: aload_2        
        //    47: invokevirtual   java/io/InputStream.close:()V
        //    50: aload_3        
        //    51: invokevirtual   com/secondtv/android/ads/vast/VastRootHandler.getPlaylist:()Lcom/secondtv/android/ads/vast/types/Playlist;
        //    54: astore_3       
        //    55: new             Lcom/secondtv/android/ads/vast/VastSingleAdHandler;
        //    58: dup            
        //    59: invokespecial   com/secondtv/android/ads/vast/VastSingleAdHandler.<init>:()V
        //    62: astore          4
        //    64: aload_3        
        //    65: invokevirtual   com/secondtv/android/ads/vast/types/Playlist.getLinearAds:()Ljava/util/List;
        //    68: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //    73: astore          5
        //    75: aload           5
        //    77: invokeinterface java/util/Iterator.hasNext:()Z
        //    82: ifeq            219
        //    85: aload           5
        //    87: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    92: checkcast       Lcom/secondtv/android/ads/vast/LinearAd;
        //    95: astore          6
        //    97: aload           6
        //    99: invokevirtual   com/secondtv/android/ads/vast/LinearAd.hasDownstreamAdUrl:()Z
        //   102: ifeq            75
        //   105: aload_0        
        //   106: aload           6
        //   108: invokevirtual   com/secondtv/android/ads/vast/LinearAd.getDownstreamAdUrl:()Ljava/lang/String;
        //   111: invokespecial   com/secondtv/android/ads/vast/LoadVastResourceAsyncTask.request:(Ljava/lang/String;)Ljava/io/InputStream;
        //   114: astore_2       
        //   115: aload           6
        //   117: invokevirtual   com/secondtv/android/ads/vast/LinearAd.clearDownstreamAdUrl:()V
        //   120: aload           4
        //   122: aload           6
        //   124: invokevirtual   com/secondtv/android/ads/vast/VastSingleAdHandler.setAd:(Lcom/secondtv/android/ads/vast/LinearAd;)V
        //   127: aload_1        
        //   128: aload_2        
        //   129: aload           4
        //   131: invokevirtual   javax/xml/parsers/SAXParser.parse:(Ljava/io/InputStream;Lorg/xml/sax/helpers/DefaultHandler;)V
        //   134: aload_2        
        //   135: invokevirtual   java/io/InputStream.close:()V
        //   138: aload           4
        //   140: aconst_null    
        //   141: invokevirtual   com/secondtv/android/ads/vast/VastSingleAdHandler.setAd:(Lcom/secondtv/android/ads/vast/LinearAd;)V
        //   144: goto            97
        //   147: astore          4
        //   149: aload_0        
        //   150: getfield        com/secondtv/android/ads/vast/LoadVastResourceAsyncTask.mTrackListener:Lcom/secondtv/android/ads/AdTrackListener;
        //   153: ifnull          169
        //   156: aload_0        
        //   157: getfield        com/secondtv/android/ads/vast/LoadVastResourceAsyncTask.mTrackListener:Lcom/secondtv/android/ads/AdTrackListener;
        //   160: aload_0        
        //   161: getfield        com/secondtv/android/ads/vast/LoadVastResourceAsyncTask.mContext:Landroid/content/Context;
        //   164: ldc             "vast"
        //   166: invokevirtual   com/secondtv/android/ads/AdTrackListener.onAdUnfulfilled:(Landroid/content/Context;Ljava/lang/String;)V
        //   169: aload_2        
        //   170: invokevirtual   java/io/InputStream.close:()V
        //   173: goto            50
        //   176: astore_1       
        //   177: aload_2        
        //   178: invokevirtual   java/io/InputStream.close:()V
        //   181: aload_1        
        //   182: athrow         
        //   183: astore          7
        //   185: aload_0        
        //   186: getfield        com/secondtv/android/ads/vast/LoadVastResourceAsyncTask.mTrackListener:Lcom/secondtv/android/ads/AdTrackListener;
        //   189: ifnull          205
        //   192: aload_0        
        //   193: getfield        com/secondtv/android/ads/vast/LoadVastResourceAsyncTask.mTrackListener:Lcom/secondtv/android/ads/AdTrackListener;
        //   196: aload_0        
        //   197: getfield        com/secondtv/android/ads/vast/LoadVastResourceAsyncTask.mContext:Landroid/content/Context;
        //   200: ldc             "vast"
        //   202: invokevirtual   com/secondtv/android/ads/AdTrackListener.onAdUnfulfilled:(Landroid/content/Context;Ljava/lang/String;)V
        //   205: aload_2        
        //   206: invokevirtual   java/io/InputStream.close:()V
        //   209: goto            138
        //   212: astore_1       
        //   213: aload_2        
        //   214: invokevirtual   java/io/InputStream.close:()V
        //   217: aload_1        
        //   218: athrow         
        //   219: aload_0        
        //   220: getfield        com/secondtv/android/ads/vast/LoadVastResourceAsyncTask.mTrackListener:Lcom/secondtv/android/ads/AdTrackListener;
        //   223: ifnull          237
        //   226: aload_0        
        //   227: getfield        com/secondtv/android/ads/vast/LoadVastResourceAsyncTask.mTrackListener:Lcom/secondtv/android/ads/AdTrackListener;
        //   230: aload_0        
        //   231: getfield        com/secondtv/android/ads/vast/LoadVastResourceAsyncTask.mContext:Landroid/content/Context;
        //   234: invokevirtual   com/secondtv/android/ads/AdTrackListener.onVastAdParseComplete:(Landroid/content/Context;)V
        //   237: aload_3        
        //   238: areturn        
        //    Exceptions:
        //  throws java.lang.Exception
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  40     46     147    176    Ljava/lang/Exception;
        //  40     46     176    183    Any
        //  127    134    183    212    Ljava/lang/Exception;
        //  127    134    212    219    Any
        //  149    169    176    183    Any
        //  185    205    212    219    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0138:
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
    
    public OnLoadVastListener getVastLoader() {
        if (this.vastLoaderRef == null) {
            return null;
        }
        return this.vastLoaderRef.get();
    }
    
    @Override
    protected void onException(final Exception ex) throws RuntimeException {
        final OnLoadVastListener vastLoader = this.getVastLoader();
        if (vastLoader == null) {
            return;
        }
        vastLoader.onPlaylistLoadException(ex);
    }
    
    @Override
    protected void onSuccess(final Playlist playlist) throws Exception {
        super.onSuccess(playlist);
        final OnLoadVastListener vastLoader = this.getVastLoader();
        if (vastLoader == null) {
            return;
        }
        vastLoader.onPlaylistLoadSuccess(playlist);
    }
    
    public void setOnVastLoaderListener(final OnLoadVastListener onLoadVastListener) {
        this.vastLoaderRef = new WeakReference<OnLoadVastListener>(onLoadVastListener);
    }
    
    public interface OnLoadVastListener
    {
        void onPlaylistLoadException(final Exception p0);
        
        void onPlaylistLoadSuccess(final Playlist p0);
    }
}
