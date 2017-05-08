// 
// Decompiled by Procyon v0.5.30
// 

package com.conviva.utils;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.ProtocolVersion;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.HttpVersion;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import com.conviva.platforms.ConvivaSSLSocketFactory;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;
import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import org.apache.http.util.EncodingUtils;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.HttpClient;
import android.util.Log;
import java.util.List;
import android.app.ActivityManager$RunningAppProcessInfo;
import android.app.ActivityManager;
import android.os.Process;
import android.os.PowerManager;
import android.os.Build;
import java.io.IOException;
import java.util.HashMap;
import java.io.File;
import android.os.Environment;
import android.os.Build$VERSION;
import java.util.Map;
import android.content.Context;

public class Utils extends PlatformUtils
{
    private static Context _appContext;
    private static final String _clientIdKey = "clId";
    private static final String _convivaJsonFilename = "conviva.json";
    private static Map<String, String> _platformMetadata;
    private static boolean _traceOverride;
    
    static {
        Utils._platformMetadata = null;
        Utils._traceOverride = false;
        Utils._appContext = null;
    }
    
    private Utils(final Settings settings, final Context appContext) throws Exception {
        super(settings);
        Utils._appContext = appContext;
        this._PLATFORM_VER = Build$VERSION.RELEASE;
    }
    
    public static PlatformUtils CreateUtils(final Map<String, Object> map, final Context context) throws Exception {
        if (Utils._instance == null) {
            final Settings settings = new Settings();
            settings.changeSettings(map);
            Utils._instance = new Utils(settings, context);
            Utils._traceOverride = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/conviva_debug.txt").exists();
            return Utils._instance;
        }
        Utils._instance.getSettings().changeSettings(map);
        final PlatformUtils instance = Utils._instance;
        ++instance._referenceCount;
        return Utils._instance;
    }
    
    private void writeClientId() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("clId", this.clientId);
        final String jsonEncode = this.jsonEncode((Map<String, Object>)hashMap);
        if (jsonEncode == null) {
            this.log("failed to json encode client id");
        }
        else {
            Object openFileOutput = null;
            Object o;
            try {
                Object o3;
                final Object o2;
                o = (o2 = (o3 = (openFileOutput = Utils._appContext.openFileOutput("conviva.json", 0))));
                final String s = jsonEncode;
                final byte[] array = s.getBytes();
                ((FileOutputStream)o2).write(array);
                o3 = o;
                final Object o4 = o;
                ((OutputStream)o4).flush();
                o3 = o;
                final Object o5 = o;
                ((FileOutputStream)o5).close();
                final Object o6 = o;
                if (o6 == null) {
                    return;
                }
                try {
                    final Object o7 = o;
                    ((FileOutputStream)o7).close();
                    return;
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                    return;
                }
            }
            catch (Exception o3) {
                o3 = openFileOutput;
                this.log("Cannot save data to persistent storage");
                if (openFileOutput == null) {
                    return;
                }
                try {
                    final Object o8 = openFileOutput;
                    ((FileOutputStream)o8).close();
                    return;
                }
                catch (IOException ex2) {
                    ex2.printStackTrace();
                    return;
                }
            }
            finally {
                final Object o10;
                final Object o9 = o10;
                final Object o3 = null;
            }
            while (true) {
                try {
                    final Object o2 = o;
                    final String s = jsonEncode;
                    final byte[] array = s.getBytes();
                    ((FileOutputStream)o2).write(array);
                    Object o3 = o;
                    final Object o4 = o;
                    ((OutputStream)o4).flush();
                    o3 = o;
                    final Object o5 = o;
                    ((FileOutputStream)o5).close();
                    final Object o6 = o;
                    if (o6 != null) {
                        final Object o7 = o;
                        ((FileOutputStream)o7).close();
                        return;
                    }
                    return;
                    final Object o8 = openFileOutput;
                    ((FileOutputStream)o8).close();
                    return;
                    while (true) {
                        try {
                            ((FileOutputStream)o3).close();
                            Label_0131: {
                                throw;
                            }
                        }
                        catch (IOException o3) {
                            ((Throwable)o3).printStackTrace();
                        }
                        continue;
                    }
                }
                // iftrue(Label_0131:, o3 == null)
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    @Override
    public void deleteLocalData() {
    }
    
    @Override
    public Map<String, String> getPlatformMetadata() {
        Label_0110: {
            if (Utils._platformMetadata != null) {
                break Label_0110;
            }
            (Utils._platformMetadata = new HashMap<String, String>()).put("sch", "and1");
            try {
                Utils._platformMetadata.put("dv", "android");
                Utils._platformMetadata.put("dvt", "Mobile");
                Utils._platformMetadata.put("os", "AND");
                Utils._platformMetadata.put("osv", Build$VERSION.RELEASE);
                Utils._platformMetadata.put("manu", Build.MANUFACTURER);
                Utils._platformMetadata.put("mod", Build.MODEL);
                return Utils._platformMetadata;
            }
            catch (Exception ex) {
                return Utils._platformMetadata;
            }
        }
    }
    
    @Override
    public void httpRequest(final boolean b, final String s, final String s2, final String s3, final CallableWithParameters.With1<String> with1) {
        final HTTPTask httpTask = new HTTPTask();
        httpTask.setState(b, s, s2, s3, with1);
        final Thread thread = new Thread(httpTask);
        if (thread != null) {
            thread.start();
        }
    }
    
    @Override
    public boolean inSleepingMode() {
        return !((PowerManager)Utils._appContext.getSystemService("power")).isScreenOn();
    }
    
    @Override
    public boolean isVisible() {
        final int myPid = Process.myPid();
        final List runningAppProcesses = ((ActivityManager)Utils._appContext.getSystemService("activity")).getRunningAppProcesses();
        if (runningAppProcesses != null) {
            int i = 0;
            while (i < runningAppProcesses.size()) {
                if (runningAppProcesses.get(i).pid == myPid) {
                    if (runningAppProcesses.get(i).importance <= 200) {
                        return true;
                    }
                    return false;
                }
                else {
                    ++i;
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void logConsole(final String s) {
        if (this._settings.enableLogging || Utils._traceOverride) {
            if (!s.contains("ERROR:")) {
                Log.d("CONVIVA", s);
                return;
            }
            Log.e("CONVIVA", s);
        }
    }
    
    @Override
    public void setClientIdFromServer(final String clientId) {
        if (!clientId.equals(this.clientId)) {
            this.clientId = clientId;
            this.log("Setting the client id to " + clientId + " (from server)");
            this.writeClientId();
        }
    }
    
    @Override
    public void startFetchClientId() {
        final Thread thread = new Thread(new LoadDataTask());
        if (thread != null) {
            thread.start();
        }
    }
    
    class LoadDataTask implements Runnable
    {
        @Override
        public void run() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: aconst_null    
            //     1: astore_3       
            //     2: aconst_null    
            //     3: astore          5
            //     5: invokestatic    com/conviva/utils/Utils.access$000:()Landroid/content/Context;
            //     8: ldc             "conviva.json"
            //    10: invokevirtual   android/content/Context.getFileStreamPath:(Ljava/lang/String;)Ljava/io/File;
            //    13: invokevirtual   java/io/File.exists:()Z
            //    16: ifeq            312
            //    19: new             Ljava/lang/StringBuffer;
            //    22: dup            
            //    23: ldc             ""
            //    25: invokespecial   java/lang/StringBuffer.<init>:(Ljava/lang/String;)V
            //    28: astore          4
            //    30: invokestatic    com/conviva/utils/Utils.access$000:()Landroid/content/Context;
            //    33: ldc             "conviva.json"
            //    35: invokevirtual   android/content/Context.openFileInput:(Ljava/lang/String;)Ljava/io/FileInputStream;
            //    38: astore_2       
            //    39: aload_2        
            //    40: astore_3       
            //    41: aload_3        
            //    42: astore_2       
            //    43: sipush          1024
            //    46: newarray        B
            //    48: astore          6
            //    50: aload_3        
            //    51: astore_2       
            //    52: aload_3        
            //    53: aload           6
            //    55: invokevirtual   java/io/FileInputStream.read:([B)I
            //    58: istore_1       
            //    59: iload_1        
            //    60: iconst_m1      
            //    61: if_icmpeq       210
            //    64: aload_3        
            //    65: astore_2       
            //    66: aload           4
            //    68: new             Ljava/lang/String;
            //    71: dup            
            //    72: aload           6
            //    74: iconst_0       
            //    75: iload_1        
            //    76: invokespecial   java/lang/String.<init>:([BII)V
            //    79: invokevirtual   java/lang/StringBuffer.append:(Ljava/lang/String;)Ljava/lang/StringBuffer;
            //    82: pop            
            //    83: goto            50
            //    86: astore_2       
            //    87: aload_3        
            //    88: astore_2       
            //    89: aload_0        
            //    90: getfield        com/conviva/utils/Utils$1LoadDataTask.this$0:Lcom/conviva/utils/Utils;
            //    93: ldc             "Failed to load data from persistent storage"
            //    95: invokevirtual   com/conviva/utils/Utils.err:(Ljava/lang/String;)V
            //    98: aload_3        
            //    99: ifnull          106
            //   102: aload_3        
            //   103: invokevirtual   java/io/FileInputStream.close:()V
            //   106: aconst_null    
            //   107: astore_2       
            //   108: aload           5
            //   110: astore_3       
            //   111: aload_2        
            //   112: ifnull          141
            //   115: aload           5
            //   117: astore_3       
            //   118: aload_2        
            //   119: ldc             "clId"
            //   121: invokeinterface java/util/Map.containsKey:(Ljava/lang/Object;)Z
            //   126: ifeq            141
            //   129: aload_2        
            //   130: ldc             "clId"
            //   132: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
            //   137: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
            //   140: astore_3       
            //   141: aload_3        
            //   142: ifnull          286
            //   145: aload_3        
            //   146: ldc             "0"
            //   148: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
            //   151: ifne            286
            //   154: aload_3        
            //   155: ldc             "null"
            //   157: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
            //   160: ifne            286
            //   163: aload_3        
            //   164: invokevirtual   java/lang/String.length:()I
            //   167: ifle            286
            //   170: aload_0        
            //   171: getfield        com/conviva/utils/Utils$1LoadDataTask.this$0:Lcom/conviva/utils/Utils;
            //   174: aload_3        
            //   175: putfield        com/conviva/utils/Utils.clientId:Ljava/lang/String;
            //   178: aload_0        
            //   179: getfield        com/conviva/utils/Utils$1LoadDataTask.this$0:Lcom/conviva/utils/Utils;
            //   182: new             Ljava/lang/StringBuilder;
            //   185: dup            
            //   186: invokespecial   java/lang/StringBuilder.<init>:()V
            //   189: ldc             "Setting the client id to "
            //   191: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   194: aload_3        
            //   195: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   198: ldc             " (from local storage)"
            //   200: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   203: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   206: invokevirtual   com/conviva/utils/Utils.log:(Ljava/lang/String;)V
            //   209: return         
            //   210: aload_3        
            //   211: astore_2       
            //   212: aload_3        
            //   213: invokevirtual   java/io/FileInputStream.close:()V
            //   216: aload_3        
            //   217: astore_2       
            //   218: aload_0        
            //   219: getfield        com/conviva/utils/Utils$1LoadDataTask.this$0:Lcom/conviva/utils/Utils;
            //   222: aload           4
            //   224: invokevirtual   java/lang/StringBuffer.toString:()Ljava/lang/String;
            //   227: invokevirtual   com/conviva/utils/Utils.jsonDecode:(Ljava/lang/String;)Ljava/util/Map;
            //   230: astore          4
            //   232: aload_3        
            //   233: ifnull          240
            //   236: aload_3        
            //   237: invokevirtual   java/io/FileInputStream.close:()V
            //   240: aload           4
            //   242: astore_2       
            //   243: goto            108
            //   246: astore_2       
            //   247: aload_2        
            //   248: invokevirtual   java/io/IOException.printStackTrace:()V
            //   251: aload           4
            //   253: astore_2       
            //   254: goto            108
            //   257: astore_2       
            //   258: aload_2        
            //   259: invokevirtual   java/io/IOException.printStackTrace:()V
            //   262: aconst_null    
            //   263: astore_2       
            //   264: goto            108
            //   267: astore_2       
            //   268: aload_3        
            //   269: ifnull          276
            //   272: aload_3        
            //   273: invokevirtual   java/io/FileInputStream.close:()V
            //   276: aload_2        
            //   277: athrow         
            //   278: astore_3       
            //   279: aload_3        
            //   280: invokevirtual   java/io/IOException.printStackTrace:()V
            //   283: goto            276
            //   286: aload_0        
            //   287: getfield        com/conviva/utils/Utils$1LoadDataTask.this$0:Lcom/conviva/utils/Utils;
            //   290: ldc             "Failed to load the client id from local storage"
            //   292: invokevirtual   com/conviva/utils/Utils.log:(Ljava/lang/String;)V
            //   295: return         
            //   296: astore          4
            //   298: aload_2        
            //   299: astore_3       
            //   300: aload           4
            //   302: astore_2       
            //   303: goto            268
            //   306: astore_2       
            //   307: aconst_null    
            //   308: astore_3       
            //   309: goto            87
            //   312: aconst_null    
            //   313: astore_2       
            //   314: goto            108
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  30     39     306    312    Ljava/lang/Exception;
            //  30     39     267    268    Any
            //  43     50     86     87     Ljava/lang/Exception;
            //  43     50     296    306    Any
            //  52     59     86     87     Ljava/lang/Exception;
            //  52     59     296    306    Any
            //  66     83     86     87     Ljava/lang/Exception;
            //  66     83     296    306    Any
            //  89     98     296    306    Any
            //  102    106    257    267    Ljava/io/IOException;
            //  212    216    86     87     Ljava/lang/Exception;
            //  212    216    296    306    Any
            //  218    232    86     87     Ljava/lang/Exception;
            //  218    232    296    306    Any
            //  236    240    246    257    Ljava/io/IOException;
            //  272    276    278    286    Ljava/io/IOException;
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0276:
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
    }
    
    private class HTTPTask implements Runnable
    {
        private CallableWithParameters.With1<String> _callback;
        private HttpClient _client;
        private String _contentT;
        private String _data;
        private boolean _isPost;
        private HttpUriRequest _method;
        private ResponseHandler<Void> _responseHandler;
        private String _url;
        
        private HTTPTask() {
            this._callback = null;
            this._client = null;
            this._method = null;
        }
        
        private void doneHandler(final Exception ex, final byte[] array) {
            final CallableWithParameters.With1<String> callback = this._callback;
            this._callback = null;
            if (ex != null) {
                Utils.this.err("Failed to send heartbeat: " + ex.toString());
            }
            String string;
            if (array == null) {
                string = null;
            }
            else {
                string = EncodingUtils.getString(array, "UTF-8");
            }
            if (callback != null) {
                if (ex != null) {
                    string = null;
                }
                callback.exec(string);
            }
        }
        
        private boolean shouldUseSsl(final String s) {
            return s.contains("https://");
        }
        
        private HttpClient sslClient(final HttpClient httpClient) {
            try {
                final X509TrustManager x509TrustManager = new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(final X509Certificate[] array, final String s) throws CertificateException {
                    }
                    
                    @Override
                    public void checkServerTrusted(final X509Certificate[] array, final String s) throws CertificateException {
                    }
                    
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                };
                final SSLContext instance = SSLContext.getInstance("TLS");
                instance.init(null, new TrustManager[] { x509TrustManager }, null);
                final ConvivaSSLSocketFactory convivaSSLSocketFactory = new ConvivaSSLSocketFactory(instance);
                convivaSSLSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                final ClientConnectionManager connectionManager = httpClient.getConnectionManager();
                connectionManager.getSchemeRegistry().register(new Scheme("https", (SocketFactory)convivaSSLSocketFactory, 443));
                return (HttpClient)new DefaultHttpClient(connectionManager, httpClient.getParams());
            }
            catch (Exception ex) {
                Utils.this.log("Error in creating SSL client");
                return null;
            }
        }
        
        @Override
        public void run() {
            final BasicHttpParams basicHttpParams = new BasicHttpParams();
            HttpProtocolParams.setVersion((HttpParams)basicHttpParams, (ProtocolVersion)HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset((HttpParams)basicHttpParams, "UTF-8");
            HttpProtocolParams.setUseExpectContinue((HttpParams)basicHttpParams, false);
            HttpConnectionParams.setConnectionTimeout((HttpParams)basicHttpParams, 60000);
            final DefaultHttpClient client = new DefaultHttpClient((HttpParams)basicHttpParams);
            Label_0159: {
                if (!this.shouldUseSsl(this._url)) {
                    break Label_0159;
                }
                this._client = this.sslClient((HttpClient)client);
            Label_0115_Outer:
                while (true) {
                    while (true) {
                        Label_0193: {
                            try {
                                if (this._isPost) {
                                    final HttpPost method = new HttpPost(this._url);
                                    method.setEntity((HttpEntity)new ByteArrayEntity(this._data.getBytes("UTF-8")));
                                    this._method = (HttpUriRequest)method;
                                }
                                else {
                                    this._method = (HttpUriRequest)new HttpGet(this._url);
                                }
                                if (this._contentT == null) {
                                    final String contentT = "application/json";
                                    this._method.setHeader("Content-Type", contentT);
                                    this._responseHandler = (ResponseHandler<Void>)new HttpResponseHandler();
                                    this._client.execute(this._method, (ResponseHandler)this._responseHandler);
                                    return;
                                }
                                break Label_0193;
                                this._client = (HttpClient)client;
                                continue Label_0115_Outer;
                            }
                            catch (Exception ex) {
                                this.doneHandler(ex, null);
                                return;
                            }
                        }
                        final String contentT = this._contentT;
                        continue;
                    }
                }
            }
        }
        
        public void setState(final boolean isPost, final String url, final String data, final String contentT, final CallableWithParameters.With1<String> callback) {
            this._isPost = isPost;
            this._url = url;
            this._data = data;
            this._contentT = contentT;
            this._callback = callback;
        }
        
        private class HttpResponseHandler implements ResponseHandler<Void>
        {
            public Void handleResponse(final HttpResponse httpResponse) {
                int statusCode = 0;
                Label_0095: {
                    ByteArrayOutputStream byteArrayOutputStream;
                    try {
                        statusCode = httpResponse.getStatusLine().getStatusCode();
                        if (statusCode != 200) {
                            break Label_0095;
                        }
                        final InputStream content = httpResponse.getEntity().getContent();
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        final byte[] array = new byte[1024];
                        while (true) {
                            final int read = content.read(array);
                            if (read == -1) {
                                break;
                            }
                            byteArrayOutputStream.write(array, 0, read);
                        }
                    }
                    catch (Exception ex) {
                        HTTPTask.this.doneHandler(ex, null);
                        return null;
                    }
                    HTTPTask.this.doneHandler(null, byteArrayOutputStream.toByteArray());
                    return null;
                }
                HTTPTask.this.doneHandler(new Exception("Status code in HTTP response is not OK: " + statusCode), null);
                return null;
            }
        }
    }
}
