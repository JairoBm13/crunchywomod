// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia.a;

import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.scheme.Scheme;
import java.io.IOException;
import org.apache.http.params.HttpParams;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import com.tremorvideo.sdk.android.videoad.ac;
import org.apache.http.client.HttpClient;
import android.os.AsyncTask;

public class g extends AsyncTask<String, Void, String>
{
    String a;
    j b;
    private boolean c;
    private boolean d;
    private boolean e;
    
    public g(final j b) {
        this.c = false;
        this.d = false;
        this.e = false;
        this.b = b;
    }
    
    private void a(final HttpClient httpClient) {
        if (ac.r() < 14) {
            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", (SocketFactory)new LayeredSocketFactory() {
                SSLSocketFactory a = SSLSocketFactory.getSocketFactory();
                
                private void a(final Socket socket, final String s) {
                    try {
                        final Field declaredField = InetAddress.class.getDeclaredField("hostName");
                        declaredField.setAccessible(true);
                        declaredField.set(socket.getInetAddress(), s);
                    }
                    catch (Exception ex) {}
                }
                
                public Socket connectSocket(final Socket socket, final String s, final int n, final InetAddress inetAddress, final int n2, final HttpParams httpParams) throws IOException {
                    return this.a.connectSocket(socket, s, n, inetAddress, n2, httpParams);
                }
                
                public Socket createSocket() throws IOException {
                    return this.a.createSocket();
                }
                
                public Socket createSocket(final Socket socket, final String s, final int n, final boolean b) throws IOException {
                    this.a(socket, s);
                    return this.a.createSocket(socket, s, n, b);
                }
                
                public boolean isSecure(final Socket socket) throws IllegalArgumentException {
                    return this.a.isSecure(socket);
                }
            }, 443));
        }
    }
    
    protected String a(final String... p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: aconst_null    
        //     2: putfield        com/tremorvideo/sdk/android/richmedia/a/g.a:Ljava/lang/String;
        //     5: new             Lorg/apache/http/params/BasicHttpParams;
        //     8: dup            
        //     9: invokespecial   org/apache/http/params/BasicHttpParams.<init>:()V
        //    12: astore_2       
        //    13: aload_2        
        //    14: ldc             "http.protocol.version"
        //    16: getstatic       org/apache/http/HttpVersion.HTTP_1_1:Lorg/apache/http/HttpVersion;
        //    19: invokeinterface org/apache/http/params/HttpParams.setParameter:(Ljava/lang/String;Ljava/lang/Object;)Lorg/apache/http/params/HttpParams;
        //    24: pop            
        //    25: aload_2        
        //    26: iconst_1       
        //    27: invokestatic    org/apache/http/params/HttpConnectionParams.setTcpNoDelay:(Lorg/apache/http/params/HttpParams;Z)V
        //    30: new             Lorg/apache/http/impl/client/DefaultHttpClient;
        //    33: dup            
        //    34: aload_2        
        //    35: invokespecial   org/apache/http/impl/client/DefaultHttpClient.<init>:(Lorg/apache/http/params/HttpParams;)V
        //    38: astore_3       
        //    39: aload_0        
        //    40: aload_3        
        //    41: invokespecial   com/tremorvideo/sdk/android/richmedia/a/g.a:(Lorg/apache/http/client/HttpClient;)V
        //    44: aload_1        
        //    45: iconst_1       
        //    46: aaload         
        //    47: ldc             "POST"
        //    49: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    52: ifeq            239
        //    55: aload_1        
        //    56: iconst_0       
        //    57: aaload         
        //    58: astore_2       
        //    59: new             Lorg/apache/http/client/methods/HttpPost;
        //    62: dup            
        //    63: new             Ljava/net/URI;
        //    66: dup            
        //    67: aload_2        
        //    68: invokespecial   java/net/URI.<init>:(Ljava/lang/String;)V
        //    71: invokespecial   org/apache/http/client/methods/HttpPost.<init>:(Ljava/net/URI;)V
        //    74: astore          4
        //    76: aload           4
        //    78: aload_2        
        //    79: invokestatic    com/tremorvideo/sdk/android/richmedia/ae.a:(Lorg/apache/http/client/methods/HttpPost;Ljava/lang/String;)V
        //    82: new             Lorg/json/JSONObject;
        //    85: dup            
        //    86: aload_1        
        //    87: iconst_2       
        //    88: aaload         
        //    89: invokespecial   org/json/JSONObject.<init>:(Ljava/lang/String;)V
        //    92: astore          5
        //    94: aload           5
        //    96: invokevirtual   org/json/JSONObject.keys:()Ljava/util/Iterator;
        //    99: astore          6
        //   101: aload           6
        //   103: invokeinterface java/util/Iterator.hasNext:()Z
        //   108: ifeq            173
        //   111: aload           6
        //   113: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   118: checkcast       Ljava/lang/String;
        //   121: astore          7
        //   123: aload           5
        //   125: aload           7
        //   127: invokevirtual   org/json/JSONObject.getString:(Ljava/lang/String;)Ljava/lang/String;
        //   130: astore_2       
        //   131: aload_2        
        //   132: ifnull          101
        //   135: aload           4
        //   137: aload           7
        //   139: aload_2        
        //   140: invokevirtual   org/apache/http/client/methods/HttpPost.addHeader:(Ljava/lang/String;Ljava/lang/String;)V
        //   143: goto            101
        //   146: astore_1       
        //   147: aload_1        
        //   148: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Ljava/lang/Throwable;)V
        //   151: aload_0        
        //   152: iconst_1       
        //   153: putfield        com/tremorvideo/sdk/android/richmedia/a/g.d:Z
        //   156: aload_0        
        //   157: iconst_1       
        //   158: putfield        com/tremorvideo/sdk/android/richmedia/a/g.e:Z
        //   161: aconst_null    
        //   162: areturn        
        //   163: astore_2       
        //   164: aload_2        
        //   165: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Ljava/lang/Throwable;)V
        //   168: aconst_null    
        //   169: astore_2       
        //   170: goto            131
        //   173: aload           4
        //   175: new             Lorg/apache/http/entity/StringEntity;
        //   178: dup            
        //   179: aload_1        
        //   180: iconst_3       
        //   181: aaload         
        //   182: ldc             "UTF-8"
        //   184: invokespecial   org/apache/http/entity/StringEntity.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //   187: invokevirtual   org/apache/http/client/methods/HttpPost.setEntity:(Lorg/apache/http/HttpEntity;)V
        //   190: aload_3        
        //   191: aload           4
        //   193: invokeinterface org/apache/http/client/HttpClient.execute:(Lorg/apache/http/client/methods/HttpUriRequest;)Lorg/apache/http/HttpResponse;
        //   198: astore_1       
        //   199: aload_0        
        //   200: aload_1        
        //   201: invokeinterface org/apache/http/HttpResponse.getEntity:()Lorg/apache/http/HttpEntity;
        //   206: invokestatic    org/apache/http/util/EntityUtils.toString:(Lorg/apache/http/HttpEntity;)Ljava/lang/String;
        //   209: putfield        com/tremorvideo/sdk/android/richmedia/a/g.a:Ljava/lang/String;
        //   212: aload_0        
        //   213: getfield        com/tremorvideo/sdk/android/richmedia/a/g.d:Z
        //   216: ifne            224
        //   219: aload_0        
        //   220: iconst_1       
        //   221: putfield        com/tremorvideo/sdk/android/richmedia/a/g.d:Z
        //   224: aload_1        
        //   225: invokeinterface org/apache/http/HttpResponse.getStatusLine:()Lorg/apache/http/StatusLine;
        //   230: invokeinterface org/apache/http/StatusLine.getStatusCode:()I
        //   235: invokestatic    java/lang/Integer.toString:(I)Ljava/lang/String;
        //   238: areturn        
        //   239: aload_1        
        //   240: iconst_0       
        //   241: aaload         
        //   242: astore          4
        //   244: new             Lorg/apache/http/client/methods/HttpGet;
        //   247: dup            
        //   248: new             Ljava/net/URI;
        //   251: dup            
        //   252: aload           4
        //   254: invokespecial   java/net/URI.<init>:(Ljava/lang/String;)V
        //   257: invokespecial   org/apache/http/client/methods/HttpGet.<init>:(Ljava/net/URI;)V
        //   260: astore_2       
        //   261: aload_2        
        //   262: aload           4
        //   264: invokestatic    com/tremorvideo/sdk/android/richmedia/ae.a:(Lorg/apache/http/client/methods/HttpGet;Ljava/lang/String;)V
        //   267: new             Lorg/json/JSONObject;
        //   270: dup            
        //   271: aload_1        
        //   272: iconst_2       
        //   273: aaload         
        //   274: invokespecial   org/json/JSONObject.<init>:(Ljava/lang/String;)V
        //   277: astore          4
        //   279: aload           4
        //   281: invokevirtual   org/json/JSONObject.keys:()Ljava/util/Iterator;
        //   284: astore          5
        //   286: aload           5
        //   288: invokeinterface java/util/Iterator.hasNext:()Z
        //   293: ifeq            340
        //   296: aload           5
        //   298: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   303: checkcast       Ljava/lang/String;
        //   306: astore          6
        //   308: aload           4
        //   310: aload           6
        //   312: invokevirtual   org/json/JSONObject.getString:(Ljava/lang/String;)Ljava/lang/String;
        //   315: astore_1       
        //   316: aload_1        
        //   317: ifnull          286
        //   320: aload_2        
        //   321: aload           6
        //   323: aload_1        
        //   324: invokevirtual   org/apache/http/client/methods/HttpGet.addHeader:(Ljava/lang/String;Ljava/lang/String;)V
        //   327: goto            286
        //   330: astore_1       
        //   331: aload_1        
        //   332: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Ljava/lang/Throwable;)V
        //   335: aconst_null    
        //   336: astore_1       
        //   337: goto            316
        //   340: aload_3        
        //   341: aload_2        
        //   342: invokeinterface org/apache/http/client/HttpClient.execute:(Lorg/apache/http/client/methods/HttpUriRequest;)Lorg/apache/http/HttpResponse;
        //   347: astore_1       
        //   348: aload_0        
        //   349: aload_1        
        //   350: invokeinterface org/apache/http/HttpResponse.getEntity:()Lorg/apache/http/HttpEntity;
        //   355: invokestatic    org/apache/http/util/EntityUtils.toString:(Lorg/apache/http/HttpEntity;)Ljava/lang/String;
        //   358: putfield        com/tremorvideo/sdk/android/richmedia/a/g.a:Ljava/lang/String;
        //   361: aload_0        
        //   362: getfield        com/tremorvideo/sdk/android/richmedia/a/g.d:Z
        //   365: ifne            373
        //   368: aload_0        
        //   369: iconst_1       
        //   370: putfield        com/tremorvideo/sdk/android/richmedia/a/g.d:Z
        //   373: aload_1        
        //   374: invokeinterface org/apache/http/HttpResponse.getStatusLine:()Lorg/apache/http/StatusLine;
        //   379: invokeinterface org/apache/http/StatusLine.getStatusCode:()I
        //   384: invokestatic    java/lang/Integer.toString:(I)Ljava/lang/String;
        //   387: astore_1       
        //   388: aload_1        
        //   389: areturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  0      55     146    163    Ljava/lang/Exception;
        //  59     101    146    163    Ljava/lang/Exception;
        //  101    123    146    163    Ljava/lang/Exception;
        //  123    131    163    173    Ljava/lang/Exception;
        //  135    143    146    163    Ljava/lang/Exception;
        //  164    168    146    163    Ljava/lang/Exception;
        //  173    224    146    163    Ljava/lang/Exception;
        //  224    239    146    163    Ljava/lang/Exception;
        //  244    286    146    163    Ljava/lang/Exception;
        //  286    308    146    163    Ljava/lang/Exception;
        //  308    316    330    340    Ljava/lang/Exception;
        //  320    327    146    163    Ljava/lang/Exception;
        //  331    335    146    163    Ljava/lang/Exception;
        //  340    373    146    163    Ljava/lang/Exception;
        //  373    388    146    163    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
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
    
    public void a() {
        this.d = true;
        this.c = true;
        this.b.a(this);
    }
    
    protected void a(final String s) {
        if (!this.d) {
            this.d = true;
        }
        if (!this.isCancelled() && !this.c && (s == null || Integer.parseInt(s) != 200) && !this.e) {}
        this.b.a(this);
    }
    
    protected void b(final String s) {
        if (!this.d) {
            this.d = true;
        }
    }
}
