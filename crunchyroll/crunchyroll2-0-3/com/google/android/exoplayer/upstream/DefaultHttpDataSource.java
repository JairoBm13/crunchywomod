// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.upstream;

import com.google.android.exoplayer.util.Util;
import java.io.InterruptedIOException;
import java.io.EOFException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.net.NoRouteToHostException;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.URL;
import android.util.Log;
import com.google.android.exoplayer.util.Assertions;
import java.util.HashMap;
import java.io.InputStream;
import com.google.android.exoplayer.util.Predicate;
import java.net.HttpURLConnection;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public class DefaultHttpDataSource implements HttpDataSource
{
    private static final Pattern CONTENT_RANGE_HEADER;
    private static final AtomicReference<byte[]> skipBufferReference;
    private final boolean allowCrossProtocolRedirects;
    private long bytesRead;
    private long bytesSkipped;
    private long bytesToRead;
    private long bytesToSkip;
    private final int connectTimeoutMillis;
    private HttpURLConnection connection;
    private final Predicate<String> contentTypePredicate;
    private DataSpec dataSpec;
    private InputStream inputStream;
    private final TransferListener listener;
    private boolean opened;
    private final int readTimeoutMillis;
    private final HashMap<String, String> requestProperties;
    private final String userAgent;
    
    static {
        CONTENT_RANGE_HEADER = Pattern.compile("^bytes (\\d+)-(\\d+)/(\\d+)$");
        skipBufferReference = new AtomicReference<byte[]>();
    }
    
    public DefaultHttpDataSource(final String s, final Predicate<String> contentTypePredicate, final TransferListener listener, final int connectTimeoutMillis, final int readTimeoutMillis, final boolean allowCrossProtocolRedirects) {
        this.userAgent = Assertions.checkNotEmpty(s);
        this.contentTypePredicate = contentTypePredicate;
        this.listener = listener;
        this.requestProperties = new HashMap<String, String>();
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.readTimeoutMillis = readTimeoutMillis;
        this.allowCrossProtocolRedirects = allowCrossProtocolRedirects;
    }
    
    private void closeConnectionQuietly() {
        if (this.connection == null) {
            return;
        }
        while (true) {
            try {
                this.connection.disconnect();
                this.connection = null;
            }
            catch (Exception ex) {
                Log.e("DefaultHttpDataSource", "Unexpected error while disconnecting", (Throwable)ex);
                continue;
            }
            break;
        }
    }
    
    private static long getContentLength(final HttpURLConnection p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: ldc2_w          -1
        //     3: lstore_3       
        //     4: aload_0        
        //     5: ldc             "Content-Length"
        //     7: invokevirtual   java/net/HttpURLConnection.getHeaderField:(Ljava/lang/String;)Ljava/lang/String;
        //    10: astore          7
        //    12: lload_3        
        //    13: lstore_1       
        //    14: aload           7
        //    16: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //    19: ifne            28
        //    22: aload           7
        //    24: invokestatic    java/lang/Long.parseLong:(Ljava/lang/String;)J
        //    27: lstore_1       
        //    28: aload_0        
        //    29: ldc             "Content-Range"
        //    31: invokevirtual   java/net/HttpURLConnection.getHeaderField:(Ljava/lang/String;)Ljava/lang/String;
        //    34: astore_0       
        //    35: lload_1        
        //    36: lstore_3       
        //    37: aload_0        
        //    38: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //    41: ifne            101
        //    44: getstatic       com/google/android/exoplayer/upstream/DefaultHttpDataSource.CONTENT_RANGE_HEADER:Ljava/util/regex/Pattern;
        //    47: aload_0        
        //    48: invokevirtual   java/util/regex/Pattern.matcher:(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
        //    51: astore          8
        //    53: lload_1        
        //    54: lstore_3       
        //    55: aload           8
        //    57: invokevirtual   java/util/regex/Matcher.find:()Z
        //    60: ifeq            101
        //    63: aload           8
        //    65: iconst_2       
        //    66: invokevirtual   java/util/regex/Matcher.group:(I)Ljava/lang/String;
        //    69: invokestatic    java/lang/Long.parseLong:(Ljava/lang/String;)J
        //    72: lstore_3       
        //    73: aload           8
        //    75: iconst_1       
        //    76: invokevirtual   java/util/regex/Matcher.group:(I)Ljava/lang/String;
        //    79: invokestatic    java/lang/Long.parseLong:(Ljava/lang/String;)J
        //    82: lstore          5
        //    84: lload_3        
        //    85: lload           5
        //    87: lsub           
        //    88: lconst_1       
        //    89: ladd           
        //    90: lstore          5
        //    92: lload_1        
        //    93: lconst_0       
        //    94: lcmp           
        //    95: ifge            141
        //    98: lload           5
        //   100: lstore_3       
        //   101: lload_3        
        //   102: lreturn        
        //   103: astore          8
        //   105: ldc             "DefaultHttpDataSource"
        //   107: new             Ljava/lang/StringBuilder;
        //   110: dup            
        //   111: invokespecial   java/lang/StringBuilder.<init>:()V
        //   114: ldc             "Unexpected Content-Length ["
        //   116: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   119: aload           7
        //   121: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   124: ldc             "]"
        //   126: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   129: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   132: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   135: pop            
        //   136: lload_3        
        //   137: lstore_1       
        //   138: goto            28
        //   141: lload_1        
        //   142: lstore_3       
        //   143: lload_1        
        //   144: lload           5
        //   146: lcmp           
        //   147: ifeq            101
        //   150: ldc             "DefaultHttpDataSource"
        //   152: new             Ljava/lang/StringBuilder;
        //   155: dup            
        //   156: invokespecial   java/lang/StringBuilder.<init>:()V
        //   159: ldc             "Inconsistent headers ["
        //   161: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   164: aload           7
        //   166: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   169: ldc             "] ["
        //   171: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   174: aload_0        
        //   175: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   178: ldc             "]"
        //   180: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   183: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   186: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
        //   189: pop            
        //   190: lload_1        
        //   191: lload           5
        //   193: invokestatic    java/lang/Math.max:(JJ)J
        //   196: lstore_3       
        //   197: lload_3        
        //   198: lreturn        
        //   199: astore          7
        //   201: ldc             "DefaultHttpDataSource"
        //   203: new             Ljava/lang/StringBuilder;
        //   206: dup            
        //   207: invokespecial   java/lang/StringBuilder.<init>:()V
        //   210: ldc             "Unexpected Content-Range ["
        //   212: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   215: aload_0        
        //   216: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   219: ldc             "]"
        //   221: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   224: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   227: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   230: pop            
        //   231: lload_1        
        //   232: lreturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  22     28     103    141    Ljava/lang/NumberFormatException;
        //  63     84     199    233    Ljava/lang/NumberFormatException;
        //  150    197    199    233    Ljava/lang/NumberFormatException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0101:
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
    
    private static URL handleRedirect(URL url, String protocol) throws IOException {
        if (protocol == null) {
            throw new ProtocolException("Null location redirect");
        }
        url = new URL(url, protocol);
        protocol = url.getProtocol();
        if (!"https".equals(protocol) && !"http".equals(protocol)) {
            throw new ProtocolException("Unsupported protocol redirect: " + protocol);
        }
        return url;
    }
    
    private HttpURLConnection makeConnection(final DataSpec dataSpec) throws IOException {
        final URL url = new URL(dataSpec.uri.toString());
        byte[] postBody = dataSpec.postBody;
        final long position = dataSpec.position;
        final long length = dataSpec.length;
        final boolean b = (dataSpec.flags & 0x1) != 0x0;
        if (this.allowCrossProtocolRedirects) {
            int n = 0;
            URL handleRedirect = url;
            int n2;
            while (true) {
                n2 = n + 1;
                if (n > 20) {
                    break;
                }
                final HttpURLConnection connection = this.makeConnection(handleRedirect, postBody, position, length, b, false);
                final int responseCode = connection.getResponseCode();
                if (responseCode != 300 && responseCode != 301 && responseCode != 302 && responseCode != 303) {
                    HttpURLConnection connection2 = connection;
                    if (postBody != null) {
                        return connection2;
                    }
                    if (responseCode != 307) {
                        connection2 = connection;
                        if (responseCode != 308) {
                            return connection2;
                        }
                    }
                }
                postBody = null;
                final String headerField = connection.getHeaderField("Location");
                connection.disconnect();
                handleRedirect = handleRedirect(handleRedirect, headerField);
                n = n2;
            }
            throw new NoRouteToHostException("Too many redirects: " + n2);
        }
        return this.makeConnection(url, postBody, position, length, b, true);
    }
    
    private HttpURLConnection makeConnection(final URL url, final byte[] array, final long n, final long n2, final boolean b, final boolean instanceFollowRedirects) throws IOException {
        final HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        httpURLConnection.setConnectTimeout(this.connectTimeoutMillis);
        httpURLConnection.setReadTimeout(this.readTimeoutMillis);
        synchronized (this.requestProperties) {
            for (final Map.Entry<String, String> entry : this.requestProperties.entrySet()) {
                httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        // monitorexit(url)
        if (n != 0L || n2 != -1L) {
            String s = "bytes=" + n + "-";
            if (n2 != -1L) {
                s += n + n2 - 1L;
            }
            httpURLConnection.setRequestProperty("Range", s);
        }
        httpURLConnection.setRequestProperty("User-Agent", this.userAgent);
        if (!b) {
            httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
        }
        httpURLConnection.setInstanceFollowRedirects(instanceFollowRedirects);
        final byte[] array2;
        httpURLConnection.setDoOutput(array2 != null);
        if (array2 != null) {
            httpURLConnection.setFixedLengthStreamingMode(array2.length);
            httpURLConnection.connect();
            final OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(array2);
            outputStream.close();
            return httpURLConnection;
        }
        httpURLConnection.connect();
        return httpURLConnection;
    }
    
    private int readInternal(final byte[] array, int n, int read) throws IOException {
        if (this.bytesToRead != -1L) {
            read = (int)Math.min(read, this.bytesToRead - this.bytesRead);
        }
        if (read == 0) {
            n = -1;
        }
        else {
            read = this.inputStream.read(array, n, read);
            if (read == -1) {
                if (this.bytesToRead != -1L && this.bytesToRead != this.bytesRead) {
                    throw new EOFException();
                }
                return -1;
            }
            else {
                this.bytesRead += read;
                n = read;
                if (this.listener != null) {
                    this.listener.onBytesTransferred(read);
                    return read;
                }
            }
        }
        return n;
    }
    
    private void skipInternal() throws IOException {
        if (this.bytesSkipped == this.bytesToSkip) {
            return;
        }
        byte[] array;
        if ((array = DefaultHttpDataSource.skipBufferReference.getAndSet(null)) == null) {
            array = new byte[4096];
        }
        while (this.bytesSkipped != this.bytesToSkip) {
            final int read = this.inputStream.read(array, 0, (int)Math.min(this.bytesToSkip - this.bytesSkipped, array.length));
            if (Thread.interrupted()) {
                throw new InterruptedIOException();
            }
            if (read == -1) {
                throw new EOFException();
            }
            this.bytesSkipped += read;
            if (this.listener == null) {
                continue;
            }
            this.listener.onBytesTransferred(read);
        }
        DefaultHttpDataSource.skipBufferReference.set(array);
    }
    
    protected final long bytesRemaining() {
        if (this.bytesToRead == -1L) {
            return this.bytesToRead;
        }
        return this.bytesToRead - this.bytesRead;
    }
    
    @Override
    public void close() throws HttpDataSourceException {
        try {
            if (this.inputStream == null) {
                return;
            }
            Util.maybeTerminateInputStream(this.connection, this.bytesRemaining());
            try {
                this.inputStream.close();
            }
            catch (IOException ex) {
                throw new HttpDataSourceException(ex, this.dataSpec);
            }
        }
        finally {
            this.inputStream = null;
            this.closeConnectionQuietly();
            if (this.opened) {
                this.opened = false;
                if (this.listener != null) {
                    this.listener.onTransferEnd();
                }
            }
        }
    }
    
    @Override
    public String getUri() {
        if (this.connection == null) {
            return null;
        }
        return this.connection.getURL().toString();
    }
    
    @Override
    public long open(final DataSpec p0) throws HttpDataSourceException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: lconst_0       
        //     1: lstore          5
        //     3: aload_0        
        //     4: aload_1        
        //     5: putfield        com/google/android/exoplayer/upstream/DefaultHttpDataSource.dataSpec:Lcom/google/android/exoplayer/upstream/DataSpec;
        //     8: aload_0        
        //     9: lconst_0       
        //    10: putfield        com/google/android/exoplayer/upstream/DefaultHttpDataSource.bytesRead:J
        //    13: aload_0        
        //    14: lconst_0       
        //    15: putfield        com/google/android/exoplayer/upstream/DefaultHttpDataSource.bytesSkipped:J
        //    18: aload_0        
        //    19: aload_0        
        //    20: aload_1        
        //    21: invokespecial   com/google/android/exoplayer/upstream/DefaultHttpDataSource.makeConnection:(Lcom/google/android/exoplayer/upstream/DataSpec;)Ljava/net/HttpURLConnection;
        //    24: putfield        com/google/android/exoplayer/upstream/DefaultHttpDataSource.connection:Ljava/net/HttpURLConnection;
        //    27: aload_0        
        //    28: getfield        com/google/android/exoplayer/upstream/DefaultHttpDataSource.connection:Ljava/net/HttpURLConnection;
        //    31: invokevirtual   java/net/HttpURLConnection.getResponseCode:()I
        //    34: istore_2       
        //    35: iload_2        
        //    36: sipush          200
        //    39: if_icmplt       49
        //    42: iload_2        
        //    43: sipush          299
        //    46: if_icmple       156
        //    49: aload_0        
        //    50: getfield        com/google/android/exoplayer/upstream/DefaultHttpDataSource.connection:Ljava/net/HttpURLConnection;
        //    53: invokevirtual   java/net/HttpURLConnection.getHeaderFields:()Ljava/util/Map;
        //    56: astore          7
        //    58: aload_0        
        //    59: invokespecial   com/google/android/exoplayer/upstream/DefaultHttpDataSource.closeConnectionQuietly:()V
        //    62: new             Lcom/google/android/exoplayer/upstream/HttpDataSource$InvalidResponseCodeException;
        //    65: dup            
        //    66: iload_2        
        //    67: aload           7
        //    69: aload_1        
        //    70: invokespecial   com/google/android/exoplayer/upstream/HttpDataSource$InvalidResponseCodeException.<init>:(ILjava/util/Map;Lcom/google/android/exoplayer/upstream/DataSpec;)V
        //    73: athrow         
        //    74: astore          7
        //    76: new             Lcom/google/android/exoplayer/upstream/HttpDataSource$HttpDataSourceException;
        //    79: dup            
        //    80: new             Ljava/lang/StringBuilder;
        //    83: dup            
        //    84: invokespecial   java/lang/StringBuilder.<init>:()V
        //    87: ldc_w           "Unable to connect to "
        //    90: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    93: aload_1        
        //    94: getfield        com/google/android/exoplayer/upstream/DataSpec.uri:Landroid/net/Uri;
        //    97: invokevirtual   android/net/Uri.toString:()Ljava/lang/String;
        //   100: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   103: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   106: aload           7
        //   108: aload_1        
        //   109: invokespecial   com/google/android/exoplayer/upstream/HttpDataSource$HttpDataSourceException.<init>:(Ljava/lang/String;Ljava/io/IOException;Lcom/google/android/exoplayer/upstream/DataSpec;)V
        //   112: athrow         
        //   113: astore          7
        //   115: aload_0        
        //   116: invokespecial   com/google/android/exoplayer/upstream/DefaultHttpDataSource.closeConnectionQuietly:()V
        //   119: new             Lcom/google/android/exoplayer/upstream/HttpDataSource$HttpDataSourceException;
        //   122: dup            
        //   123: new             Ljava/lang/StringBuilder;
        //   126: dup            
        //   127: invokespecial   java/lang/StringBuilder.<init>:()V
        //   130: ldc_w           "Unable to connect to "
        //   133: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   136: aload_1        
        //   137: getfield        com/google/android/exoplayer/upstream/DataSpec.uri:Landroid/net/Uri;
        //   140: invokevirtual   android/net/Uri.toString:()Ljava/lang/String;
        //   143: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   146: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   149: aload           7
        //   151: aload_1        
        //   152: invokespecial   com/google/android/exoplayer/upstream/HttpDataSource$HttpDataSourceException.<init>:(Ljava/lang/String;Ljava/io/IOException;Lcom/google/android/exoplayer/upstream/DataSpec;)V
        //   155: athrow         
        //   156: aload_0        
        //   157: getfield        com/google/android/exoplayer/upstream/DefaultHttpDataSource.connection:Ljava/net/HttpURLConnection;
        //   160: invokevirtual   java/net/HttpURLConnection.getContentType:()Ljava/lang/String;
        //   163: astore          7
        //   165: aload_0        
        //   166: getfield        com/google/android/exoplayer/upstream/DefaultHttpDataSource.contentTypePredicate:Lcom/google/android/exoplayer/util/Predicate;
        //   169: ifnull          201
        //   172: aload_0        
        //   173: getfield        com/google/android/exoplayer/upstream/DefaultHttpDataSource.contentTypePredicate:Lcom/google/android/exoplayer/util/Predicate;
        //   176: aload           7
        //   178: invokeinterface com/google/android/exoplayer/util/Predicate.evaluate:(Ljava/lang/Object;)Z
        //   183: ifne            201
        //   186: aload_0        
        //   187: invokespecial   com/google/android/exoplayer/upstream/DefaultHttpDataSource.closeConnectionQuietly:()V
        //   190: new             Lcom/google/android/exoplayer/upstream/HttpDataSource$InvalidContentTypeException;
        //   193: dup            
        //   194: aload           7
        //   196: aload_1        
        //   197: invokespecial   com/google/android/exoplayer/upstream/HttpDataSource$InvalidContentTypeException.<init>:(Ljava/lang/String;Lcom/google/android/exoplayer/upstream/DataSpec;)V
        //   200: athrow         
        //   201: lload           5
        //   203: lstore_3       
        //   204: iload_2        
        //   205: sipush          200
        //   208: if_icmpne       228
        //   211: lload           5
        //   213: lstore_3       
        //   214: aload_1        
        //   215: getfield        com/google/android/exoplayer/upstream/DataSpec.position:J
        //   218: lconst_0       
        //   219: lcmp           
        //   220: ifeq            228
        //   223: aload_1        
        //   224: getfield        com/google/android/exoplayer/upstream/DataSpec.position:J
        //   227: lstore_3       
        //   228: aload_0        
        //   229: lload_3        
        //   230: putfield        com/google/android/exoplayer/upstream/DefaultHttpDataSource.bytesToSkip:J
        //   233: aload_1        
        //   234: getfield        com/google/android/exoplayer/upstream/DataSpec.flags:I
        //   237: iconst_1       
        //   238: iand           
        //   239: ifne            333
        //   242: aload_0        
        //   243: getfield        com/google/android/exoplayer/upstream/DefaultHttpDataSource.connection:Ljava/net/HttpURLConnection;
        //   246: invokestatic    com/google/android/exoplayer/upstream/DefaultHttpDataSource.getContentLength:(Ljava/net/HttpURLConnection;)J
        //   249: lstore_3       
        //   250: aload_1        
        //   251: getfield        com/google/android/exoplayer/upstream/DataSpec.length:J
        //   254: ldc2_w          -1
        //   257: lcmp           
        //   258: ifeq            308
        //   261: aload_1        
        //   262: getfield        com/google/android/exoplayer/upstream/DataSpec.length:J
        //   265: lstore_3       
        //   266: aload_0        
        //   267: lload_3        
        //   268: putfield        com/google/android/exoplayer/upstream/DefaultHttpDataSource.bytesToRead:J
        //   271: aload_0        
        //   272: aload_0        
        //   273: getfield        com/google/android/exoplayer/upstream/DefaultHttpDataSource.connection:Ljava/net/HttpURLConnection;
        //   276: invokevirtual   java/net/HttpURLConnection.getInputStream:()Ljava/io/InputStream;
        //   279: putfield        com/google/android/exoplayer/upstream/DefaultHttpDataSource.inputStream:Ljava/io/InputStream;
        //   282: aload_0        
        //   283: iconst_1       
        //   284: putfield        com/google/android/exoplayer/upstream/DefaultHttpDataSource.opened:Z
        //   287: aload_0        
        //   288: getfield        com/google/android/exoplayer/upstream/DefaultHttpDataSource.listener:Lcom/google/android/exoplayer/upstream/TransferListener;
        //   291: ifnull          303
        //   294: aload_0        
        //   295: getfield        com/google/android/exoplayer/upstream/DefaultHttpDataSource.listener:Lcom/google/android/exoplayer/upstream/TransferListener;
        //   298: invokeinterface com/google/android/exoplayer/upstream/TransferListener.onTransferStart:()V
        //   303: aload_0        
        //   304: getfield        com/google/android/exoplayer/upstream/DefaultHttpDataSource.bytesToRead:J
        //   307: lreturn        
        //   308: lload_3        
        //   309: ldc2_w          -1
        //   312: lcmp           
        //   313: ifeq            326
        //   316: lload_3        
        //   317: aload_0        
        //   318: getfield        com/google/android/exoplayer/upstream/DefaultHttpDataSource.bytesToSkip:J
        //   321: lsub           
        //   322: lstore_3       
        //   323: goto            266
        //   326: ldc2_w          -1
        //   329: lstore_3       
        //   330: goto            266
        //   333: aload_0        
        //   334: aload_1        
        //   335: getfield        com/google/android/exoplayer/upstream/DataSpec.length:J
        //   338: putfield        com/google/android/exoplayer/upstream/DefaultHttpDataSource.bytesToRead:J
        //   341: goto            271
        //   344: astore          7
        //   346: aload_0        
        //   347: invokespecial   com/google/android/exoplayer/upstream/DefaultHttpDataSource.closeConnectionQuietly:()V
        //   350: new             Lcom/google/android/exoplayer/upstream/HttpDataSource$HttpDataSourceException;
        //   353: dup            
        //   354: aload           7
        //   356: aload_1        
        //   357: invokespecial   com/google/android/exoplayer/upstream/HttpDataSource$HttpDataSourceException.<init>:(Ljava/io/IOException;Lcom/google/android/exoplayer/upstream/DataSpec;)V
        //   360: athrow         
        //    Exceptions:
        //  throws com.google.android.exoplayer.upstream.HttpDataSource.HttpDataSourceException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  18     27     74     113    Ljava/io/IOException;
        //  27     35     113    156    Ljava/io/IOException;
        //  271    282    344    361    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0049:
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
    public int read(final byte[] array, int internal, final int n) throws HttpDataSourceException {
        try {
            this.skipInternal();
            internal = this.readInternal(array, internal, n);
            return internal;
        }
        catch (IOException ex) {
            throw new HttpDataSourceException(ex, this.dataSpec);
        }
    }
}
