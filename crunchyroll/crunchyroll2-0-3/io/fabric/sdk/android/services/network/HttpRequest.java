// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.io.BufferedOutputStream;
import java.util.concurrent.Callable;
import java.io.Flushable;
import java.util.zip.GZIPInputStream;
import java.io.File;
import java.io.Closeable;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Iterator;
import java.util.Map;
import java.net.MalformedURLException;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;

public class HttpRequest
{
    private static ConnectionFactory CONNECTION_FACTORY;
    private static final String[] EMPTY_STRINGS;
    private int bufferSize;
    private HttpURLConnection connection;
    private String httpProxyHost;
    private int httpProxyPort;
    private boolean ignoreCloseExceptions;
    private boolean multipart;
    private RequestOutputStream output;
    private final String requestMethod;
    private boolean uncompress;
    public final URL url;
    
    static {
        EMPTY_STRINGS = new String[0];
        HttpRequest.CONNECTION_FACTORY = ConnectionFactory.DEFAULT;
    }
    
    public HttpRequest(final CharSequence charSequence, final String requestMethod) throws HttpRequestException {
        this.connection = null;
        this.ignoreCloseExceptions = true;
        this.uncompress = false;
        this.bufferSize = 8192;
        try {
            this.url = new URL(charSequence.toString());
            this.requestMethod = requestMethod;
        }
        catch (MalformedURLException ex) {
            throw new HttpRequestException(ex);
        }
    }
    
    private static StringBuilder addParamPrefix(final String s, final StringBuilder sb) {
        final int index = s.indexOf(63);
        final int n = sb.length() - 1;
        if (index == -1) {
            sb.append('?');
        }
        else if (index < n && s.charAt(n) != '&') {
            sb.append('&');
            return sb;
        }
        return sb;
    }
    
    private static StringBuilder addPathSeparator(final String s, final StringBuilder sb) {
        if (s.indexOf(58) + 2 == s.lastIndexOf(47)) {
            sb.append('/');
        }
        return sb;
    }
    
    public static String append(final CharSequence charSequence, final Map<?, ?> map) {
        final String string = charSequence.toString();
        if (map == null || map.isEmpty()) {
            return string;
        }
        final StringBuilder sb = new StringBuilder(string);
        addPathSeparator(string, sb);
        addParamPrefix(string, sb);
        final Iterator<Map.Entry<Object, ?>> iterator = map.entrySet().iterator();
        final Map.Entry<Object, V> entry = iterator.next();
        sb.append(entry.getKey().toString());
        sb.append('=');
        final V value = entry.getValue();
        if (value != null) {
            sb.append(value);
        }
        while (iterator.hasNext()) {
            sb.append('&');
            final Map.Entry<Object, V> entry2 = iterator.next();
            sb.append(entry2.getKey().toString());
            sb.append('=');
            final V value2 = entry2.getValue();
            if (value2 != null) {
                sb.append(value2);
            }
        }
        return sb.toString();
    }
    
    private HttpURLConnection createConnection() {
        try {
            HttpURLConnection httpURLConnection;
            if (this.httpProxyHost != null) {
                httpURLConnection = HttpRequest.CONNECTION_FACTORY.create(this.url, this.createProxy());
            }
            else {
                httpURLConnection = HttpRequest.CONNECTION_FACTORY.create(this.url);
            }
            httpURLConnection.setRequestMethod(this.requestMethod);
            return httpURLConnection;
        }
        catch (IOException ex) {
            throw new HttpRequestException(ex);
        }
    }
    
    private Proxy createProxy() {
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(this.httpProxyHost, this.httpProxyPort));
    }
    
    public static HttpRequest delete(final CharSequence charSequence) throws HttpRequestException {
        return new HttpRequest(charSequence, "DELETE");
    }
    
    public static String encode(final CharSequence p0) throws HttpRequestException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: new             Ljava/net/URL;
        //     3: dup            
        //     4: aload_0        
        //     5: invokeinterface java/lang/CharSequence.toString:()Ljava/lang/String;
        //    10: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
        //    13: astore_3       
        //    14: aload_3        
        //    15: invokevirtual   java/net/URL.getHost:()Ljava/lang/String;
        //    18: astore_2       
        //    19: aload_3        
        //    20: invokevirtual   java/net/URL.getPort:()I
        //    23: istore_1       
        //    24: aload_2        
        //    25: astore_0       
        //    26: iload_1        
        //    27: iconst_m1      
        //    28: if_icmpeq       58
        //    31: new             Ljava/lang/StringBuilder;
        //    34: dup            
        //    35: invokespecial   java/lang/StringBuilder.<init>:()V
        //    38: aload_2        
        //    39: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    42: bipush          58
        //    44: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //    47: iload_1        
        //    48: invokestatic    java/lang/Integer.toString:(I)Ljava/lang/String;
        //    51: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    54: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    57: astore_0       
        //    58: new             Ljava/net/URI;
        //    61: dup            
        //    62: aload_3        
        //    63: invokevirtual   java/net/URL.getProtocol:()Ljava/lang/String;
        //    66: aload_0        
        //    67: aload_3        
        //    68: invokevirtual   java/net/URL.getPath:()Ljava/lang/String;
        //    71: aload_3        
        //    72: invokevirtual   java/net/URL.getQuery:()Ljava/lang/String;
        //    75: aconst_null    
        //    76: invokespecial   java/net/URI.<init>:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
        //    79: invokevirtual   java/net/URI.toASCIIString:()Ljava/lang/String;
        //    82: astore_2       
        //    83: aload_2        
        //    84: bipush          63
        //    86: invokevirtual   java/lang/String.indexOf:(I)I
        //    89: istore_1       
        //    90: aload_2        
        //    91: astore_0       
        //    92: iload_1        
        //    93: ifle            149
        //    96: aload_2        
        //    97: astore_0       
        //    98: iload_1        
        //    99: iconst_1       
        //   100: iadd           
        //   101: aload_2        
        //   102: invokevirtual   java/lang/String.length:()I
        //   105: if_icmpge       149
        //   108: new             Ljava/lang/StringBuilder;
        //   111: dup            
        //   112: invokespecial   java/lang/StringBuilder.<init>:()V
        //   115: aload_2        
        //   116: iconst_0       
        //   117: iload_1        
        //   118: iconst_1       
        //   119: iadd           
        //   120: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   123: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   126: aload_2        
        //   127: iload_1        
        //   128: iconst_1       
        //   129: iadd           
        //   130: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   133: ldc_w           "+"
        //   136: ldc_w           "%2B"
        //   139: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //   142: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   145: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   148: astore_0       
        //   149: aload_0        
        //   150: areturn        
        //   151: astore_0       
        //   152: new             Lio/fabric/sdk/android/services/network/HttpRequest$HttpRequestException;
        //   155: dup            
        //   156: aload_0        
        //   157: invokespecial   io/fabric/sdk/android/services/network/HttpRequest$HttpRequestException.<init>:(Ljava/io/IOException;)V
        //   160: athrow         
        //   161: astore_0       
        //   162: new             Ljava/io/IOException;
        //   165: dup            
        //   166: ldc_w           "Parsing URI failed"
        //   169: invokespecial   java/io/IOException.<init>:(Ljava/lang/String;)V
        //   172: astore_2       
        //   173: aload_2        
        //   174: aload_0        
        //   175: invokevirtual   java/io/IOException.initCause:(Ljava/lang/Throwable;)Ljava/lang/Throwable;
        //   178: pop            
        //   179: new             Lio/fabric/sdk/android/services/network/HttpRequest$HttpRequestException;
        //   182: dup            
        //   183: aload_2        
        //   184: invokespecial   io/fabric/sdk/android/services/network/HttpRequest$HttpRequestException.<init>:(Ljava/io/IOException;)V
        //   187: athrow         
        //    Exceptions:
        //  throws io.fabric.sdk.android.services.network.HttpRequest.HttpRequestException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                         
        //  -----  -----  -----  -----  -----------------------------
        //  0      14     151    161    Ljava/io/IOException;
        //  58     90     161    188    Ljava/net/URISyntaxException;
        //  98     149    161    188    Ljava/net/URISyntaxException;
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
    
    public static HttpRequest get(final CharSequence charSequence) throws HttpRequestException {
        return new HttpRequest(charSequence, "GET");
    }
    
    public static HttpRequest get(final CharSequence charSequence, final Map<?, ?> map, final boolean b) {
        String s = append(charSequence, map);
        if (b) {
            s = encode(s);
        }
        return get(s);
    }
    
    private static String getValidCharset(final String s) {
        if (s != null && s.length() > 0) {
            return s;
        }
        return "UTF-8";
    }
    
    public static HttpRequest post(final CharSequence charSequence) throws HttpRequestException {
        return new HttpRequest(charSequence, "POST");
    }
    
    public static HttpRequest post(final CharSequence charSequence, final Map<?, ?> map, final boolean b) {
        String s = append(charSequence, map);
        if (b) {
            s = encode(s);
        }
        return post(s);
    }
    
    public static HttpRequest put(final CharSequence charSequence) throws HttpRequestException {
        return new HttpRequest(charSequence, "PUT");
    }
    
    public String body() throws HttpRequestException {
        return this.body(this.charset());
    }
    
    public String body(String string) throws HttpRequestException {
        final ByteArrayOutputStream byteStream = this.byteStream();
        try {
            this.copy(this.buffer(), byteStream);
            string = byteStream.toString(getValidCharset(string));
            return string;
        }
        catch (IOException ex) {
            throw new HttpRequestException(ex);
        }
    }
    
    public BufferedInputStream buffer() throws HttpRequestException {
        return new BufferedInputStream(this.stream(), this.bufferSize);
    }
    
    protected ByteArrayOutputStream byteStream() {
        final int contentLength = this.contentLength();
        if (contentLength > 0) {
            return new ByteArrayOutputStream(contentLength);
        }
        return new ByteArrayOutputStream();
    }
    
    public String charset() {
        return this.parameter("Content-Type", "charset");
    }
    
    protected HttpRequest closeOutput() throws IOException {
        if (this.output == null) {
            return this;
        }
        if (this.multipart) {
            this.output.write("\r\n--00content0boundary00--\r\n");
        }
        Label_0048: {
            if (!this.ignoreCloseExceptions) {
                break Label_0048;
            }
            while (true) {
                while (true) {
                    try {
                        this.output.close();
                        this.output = null;
                        return this;
                        this.output.close();
                        continue;
                    }
                    catch (IOException ex) {
                        continue;
                    }
                    continue;
                }
            }
        }
    }
    
    protected HttpRequest closeOutputQuietly() throws HttpRequestException {
        try {
            return this.closeOutput();
        }
        catch (IOException ex) {
            throw new HttpRequestException(ex);
        }
    }
    
    public int code() throws HttpRequestException {
        try {
            this.closeOutput();
            return this.getConnection().getResponseCode();
        }
        catch (IOException ex) {
            throw new HttpRequestException(ex);
        }
    }
    
    public HttpRequest connectTimeout(final int connectTimeout) {
        this.getConnection().setConnectTimeout(connectTimeout);
        return this;
    }
    
    public String contentEncoding() {
        return this.header("Content-Encoding");
    }
    
    public int contentLength() {
        return this.intHeader("Content-Length");
    }
    
    public HttpRequest contentType(final String s) {
        return this.contentType(s, null);
    }
    
    public HttpRequest contentType(final String s, final String s2) {
        if (s2 != null && s2.length() > 0) {
            return this.header("Content-Type", s + "; charset=" + s2);
        }
        return this.header("Content-Type", s);
    }
    
    protected HttpRequest copy(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        return ((Operation<HttpRequest>)new CloseOperation<HttpRequest>(inputStream, this.ignoreCloseExceptions) {
            public HttpRequest run() throws IOException {
                final byte[] array = new byte[HttpRequest.this.bufferSize];
                while (true) {
                    final int read = inputStream.read(array);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(array, 0, read);
                }
                return HttpRequest.this;
            }
        }).call();
    }
    
    public HttpURLConnection getConnection() {
        if (this.connection == null) {
            this.connection = this.createConnection();
        }
        return this.connection;
    }
    
    protected String getParam(String s, final String s2) {
        if (s == null || s.length() == 0) {
            s = null;
        }
        else {
            final int length = s.length();
            final int n = s.indexOf(59) + 1;
            if (n == 0 || n == length) {
                return null;
            }
            int n3;
            final int n2 = n3 = s.indexOf(59, n);
            int i = n;
            if (n2 == -1) {
                n3 = length;
                i = n;
            }
            while (i < n3) {
                final int index = s.indexOf(61, i);
                if (index != -1 && index < n3 && s2.equals(s.substring(i, index).trim())) {
                    final String trim = s.substring(index + 1, n3).trim();
                    final int length2 = trim.length();
                    if (length2 != 0) {
                        s = trim;
                        if (length2 <= 2) {
                            return s;
                        }
                        s = trim;
                        if ('\"' != trim.charAt(0)) {
                            return s;
                        }
                        s = trim;
                        if ('\"' == trim.charAt(length2 - 1)) {
                            return trim.substring(1, length2 - 1);
                        }
                        return s;
                    }
                }
                final int n4 = n3 + 1;
                final int n5 = n3 = s.indexOf(59, n4);
                i = n4;
                if (n5 == -1) {
                    n3 = length;
                    i = n4;
                }
            }
            return null;
        }
        return s;
    }
    
    public HttpRequest header(final String s, final String s2) {
        this.getConnection().setRequestProperty(s, s2);
        return this;
    }
    
    public HttpRequest header(final Map.Entry<String, String> entry) {
        return this.header(entry.getKey(), entry.getValue());
    }
    
    public String header(final String s) throws HttpRequestException {
        this.closeOutputQuietly();
        return this.getConnection().getHeaderField(s);
    }
    
    public int intHeader(final String s) throws HttpRequestException {
        return this.intHeader(s, -1);
    }
    
    public int intHeader(final String s, final int n) throws HttpRequestException {
        this.closeOutputQuietly();
        return this.getConnection().getHeaderFieldInt(s, n);
    }
    
    public String method() {
        return this.getConnection().getRequestMethod();
    }
    
    public boolean ok() throws HttpRequestException {
        return 200 == this.code();
    }
    
    protected HttpRequest openOutput() throws IOException {
        if (this.output != null) {
            return this;
        }
        this.getConnection().setDoOutput(true);
        this.output = new RequestOutputStream(this.getConnection().getOutputStream(), this.getParam(this.getConnection().getRequestProperty("Content-Type"), "charset"), this.bufferSize);
        return this;
    }
    
    public String parameter(final String s, final String s2) {
        return this.getParam(this.header(s), s2);
    }
    
    public HttpRequest part(final String s, final Number n) throws HttpRequestException {
        return this.part(s, null, n);
    }
    
    public HttpRequest part(final String s, final String s2) {
        return this.part(s, null, s2);
    }
    
    public HttpRequest part(final String s, final String s2, final Number n) throws HttpRequestException {
        String string;
        if (n != null) {
            string = n.toString();
        }
        else {
            string = null;
        }
        return this.part(s, s2, string);
    }
    
    public HttpRequest part(final String s, final String s2, final String s3) throws HttpRequestException {
        return this.part(s, s2, null, s3);
    }
    
    public HttpRequest part(final String p0, final String p1, final String p2, final File p3) throws HttpRequestException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aconst_null    
        //     1: astore          5
        //     3: aconst_null    
        //     4: astore          6
        //     6: new             Ljava/io/BufferedInputStream;
        //     9: dup            
        //    10: new             Ljava/io/FileInputStream;
        //    13: dup            
        //    14: aload           4
        //    16: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //    19: invokespecial   java/io/BufferedInputStream.<init>:(Ljava/io/InputStream;)V
        //    22: astore          4
        //    24: aload_0        
        //    25: aload_1        
        //    26: aload_2        
        //    27: aload_3        
        //    28: aload           4
        //    30: invokevirtual   io/fabric/sdk/android/services/network/HttpRequest.part:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/io/InputStream;)Lio/fabric/sdk/android/services/network/HttpRequest;
        //    33: astore_1       
        //    34: aload           4
        //    36: ifnull          44
        //    39: aload           4
        //    41: invokevirtual   java/io/InputStream.close:()V
        //    44: aload_1        
        //    45: areturn        
        //    46: astore_1       
        //    47: aload           6
        //    49: astore          5
        //    51: new             Lio/fabric/sdk/android/services/network/HttpRequest$HttpRequestException;
        //    54: dup            
        //    55: aload_1        
        //    56: invokespecial   io/fabric/sdk/android/services/network/HttpRequest$HttpRequestException.<init>:(Ljava/io/IOException;)V
        //    59: athrow         
        //    60: astore_1       
        //    61: aload           5
        //    63: ifnull          71
        //    66: aload           5
        //    68: invokevirtual   java/io/InputStream.close:()V
        //    71: aload_1        
        //    72: athrow         
        //    73: astore_2       
        //    74: aload_1        
        //    75: areturn        
        //    76: astore_2       
        //    77: goto            71
        //    80: astore_1       
        //    81: aload           4
        //    83: astore          5
        //    85: goto            61
        //    88: astore_1       
        //    89: aload           4
        //    91: astore          5
        //    93: goto            51
        //    Exceptions:
        //  throws io.fabric.sdk.android.services.network.HttpRequest.HttpRequestException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  6      24     46     51     Ljava/io/IOException;
        //  6      24     60     61     Any
        //  24     34     88     96     Ljava/io/IOException;
        //  24     34     80     88     Any
        //  39     44     73     76     Ljava/io/IOException;
        //  51     60     60     61     Any
        //  66     71     76     80     Ljava/io/IOException;
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
    
    public HttpRequest part(final String s, final String s2, final String s3, final InputStream inputStream) throws HttpRequestException {
        try {
            this.startPart();
            this.writePartHeader(s, s2, s3);
            this.copy(inputStream, this.output);
            return this;
        }
        catch (IOException ex) {
            throw new HttpRequestException(ex);
        }
    }
    
    public HttpRequest part(final String s, final String s2, final String s3, final String s4) throws HttpRequestException {
        try {
            this.startPart();
            this.writePartHeader(s, s2, s3);
            this.output.write(s4);
            return this;
        }
        catch (IOException ex) {
            throw new HttpRequestException(ex);
        }
    }
    
    public HttpRequest partHeader(final String s, final String s2) throws HttpRequestException {
        return this.send(s).send(": ").send(s2).send("\r\n");
    }
    
    public HttpRequest send(final CharSequence charSequence) throws HttpRequestException {
        try {
            this.openOutput();
            this.output.write(charSequence.toString());
            return this;
        }
        catch (IOException ex) {
            throw new HttpRequestException(ex);
        }
    }
    
    protected HttpRequest startPart() throws IOException {
        if (!this.multipart) {
            this.multipart = true;
            this.contentType("multipart/form-data; boundary=00content0boundary00").openOutput();
            this.output.write("--00content0boundary00\r\n");
            return this;
        }
        this.output.write("\r\n--00content0boundary00\r\n");
        return this;
    }
    
    public InputStream stream() throws HttpRequestException {
        Label_0085: {
            InputStream inputStream;
            while (true) {
                Label_0050: {
                    if (this.code() >= 400) {
                        break Label_0050;
                    }
                    try {
                        inputStream = this.getConnection().getInputStream();
                        if (!this.uncompress || !"gzip".equals(this.contentEncoding())) {
                            return inputStream;
                        }
                        break Label_0085;
                    }
                    catch (IOException ex) {
                        throw new HttpRequestException(ex);
                    }
                }
                if ((inputStream = this.getConnection().getErrorStream()) != null) {
                    continue;
                }
                try {
                    inputStream = this.getConnection().getInputStream();
                    continue;
                }
                catch (IOException ex2) {
                    throw new HttpRequestException(ex2);
                }
                break;
            }
            try {
                return new GZIPInputStream(inputStream);
            }
            catch (IOException ex3) {
                throw new HttpRequestException(ex3);
            }
        }
    }
    
    @Override
    public String toString() {
        return this.method() + ' ' + this.url();
    }
    
    public URL url() {
        return this.getConnection().getURL();
    }
    
    public HttpRequest useCaches(final boolean useCaches) {
        this.getConnection().setUseCaches(useCaches);
        return this;
    }
    
    protected HttpRequest writePartHeader(final String s, final String s2, final String s3) throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append("form-data; name=\"").append(s);
        if (s2 != null) {
            sb.append("\"; filename=\"").append(s2);
        }
        sb.append('\"');
        this.partHeader("Content-Disposition", sb.toString());
        if (s3 != null) {
            this.partHeader("Content-Type", s3);
        }
        return this.send("\r\n");
    }
    
    protected abstract static class CloseOperation<V> extends Operation<V>
    {
        private final Closeable closeable;
        private final boolean ignoreCloseExceptions;
        
        protected CloseOperation(final Closeable closeable, final boolean ignoreCloseExceptions) {
            this.closeable = closeable;
            this.ignoreCloseExceptions = ignoreCloseExceptions;
        }
        
        @Override
        protected void done() throws IOException {
            if (this.closeable instanceof Flushable) {
                ((Flushable)this.closeable).flush();
            }
            Label_0039: {
                if (!this.ignoreCloseExceptions) {
                    break Label_0039;
                }
                try {
                    this.closeable.close();
                    return;
                    this.closeable.close();
                }
                catch (IOException ex) {}
            }
        }
    }
    
    public interface ConnectionFactory
    {
        public static final ConnectionFactory DEFAULT = new ConnectionFactory() {
            @Override
            public HttpURLConnection create(final URL url) throws IOException {
                return (HttpURLConnection)url.openConnection();
            }
            
            @Override
            public HttpURLConnection create(final URL url, final Proxy proxy) throws IOException {
                return (HttpURLConnection)url.openConnection(proxy);
            }
        };
        
        HttpURLConnection create(final URL p0) throws IOException;
        
        HttpURLConnection create(final URL p0, final Proxy p1) throws IOException;
    }
    
    public static class HttpRequestException extends RuntimeException
    {
        protected HttpRequestException(final IOException ex) {
            super(ex);
        }
        
        @Override
        public IOException getCause() {
            return (IOException)super.getCause();
        }
    }
    
    protected abstract static class Operation<V> implements Callable<V>
    {
        @Override
        public V call() throws HttpRequestException {
            boolean b = false;
            try {
                final V run = this.run();
                try {
                    this.done();
                    return run;
                }
                catch (IOException ex) {
                    if (!false) {
                        throw new HttpRequestException(ex);
                    }
                    return run;
                }
            }
            catch (HttpRequestException ex2) {
                b = true;
                throw ex2;
            }
            catch (IOException ex4) {}
            finally {
                while (true) {
                    try {
                        this.done();
                    }
                    catch (IOException ex3) {
                        if (!b) {
                            throw new HttpRequestException(ex3);
                        }
                        continue;
                    }
                    break;
                }
            }
        }
        
        protected abstract void done() throws IOException;
        
        protected abstract V run() throws HttpRequestException, IOException;
    }
    
    public static class RequestOutputStream extends BufferedOutputStream
    {
        private final CharsetEncoder encoder;
        
        public RequestOutputStream(final OutputStream outputStream, final String s, final int n) {
            super(outputStream, n);
            this.encoder = Charset.forName(getValidCharset(s)).newEncoder();
        }
        
        public RequestOutputStream write(final String s) throws IOException {
            final ByteBuffer encode = this.encoder.encode(CharBuffer.wrap(s));
            super.write(encode.array(), 0, encode.limit());
            return this;
        }
    }
}
