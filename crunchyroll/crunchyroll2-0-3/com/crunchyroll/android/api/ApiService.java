// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api;

import com.fasterxml.jackson.core.JsonGenerator;
import com.crunchyroll.android.api.exceptions.ApiUnknownException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.base.Optional;
import com.crunchyroll.android.api.requests.StartSessionRequest;
import com.crunchyroll.android.api.models.Session;
import com.crunchyroll.android.api.exceptions.ApiNetworkException;
import com.crunchyroll.android.api.exceptions.ApiBadSessionException;
import java.io.IOException;
import com.crunchyroll.android.api.exceptions.ApiBadResponseException;
import com.crunchyroll.android.api.exceptions.ApiErrorException;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.crunchyroll.android.api.exceptions.ApiException;
import java.util.Set;
import java.util.List;
import java.util.Iterator;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import com.google.common.base.Charsets;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.atomic.AtomicBoolean;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.google.common.cache.Cache;
import com.crunchyroll.crunchyroid.app.ApplicationState;
import java.util.Map;

public class ApiService
{
    private static final String HEADER_ANDROID_APPLICATION_VERSION_CODE = "X-Android-Application-Version-Code";
    private static final String HEADER_ANDROID_APPLICATION_VERSION_NAME = "X-Android-Application-Version-Name";
    private static final String HEADER_ANDROID_DEVICE_IS_GOOGLETV = "X-Android-Device-Is-GoogleTV";
    private static final String HEADER_ANDROID_DEVICE_MANUFACTURER = "X-Android-Device-Manufacturer";
    private static final String HEADER_ANDROID_DEVICE_MODEL = "X-Android-Device-Model";
    private static final String HEADER_ANDROID_DEVICE_PRODUCT = "X-Android-Device-Product";
    private static final String HEADER_ANDROID_RELEASE = "X-Android-Release";
    private static final String HEADER_ANDROID_SDK = "X-Android-SDK";
    private static final String HEADER_USING_HLS_PLAYER = "Using-Brightcove-Player";
    private final Map<String, String> mAndroidHeaders;
    private final ApplicationState mApplicationState;
    private final ClientInformation mClientInformation;
    @Deprecated
    private final Cache<Object, TokenBuffer> mHttpCache;
    private AtomicBoolean mIsStartingSession;
    private final ObjectMapper mMapper;
    
    public ApiService(final ApplicationState mApplicationState, final ClientInformation mClientInformation, final ObjectMapper mMapper) {
        this.mIsStartingSession = new AtomicBoolean(false);
        this.mApplicationState = mApplicationState;
        this.mClientInformation = mClientInformation;
        this.mMapper = mMapper;
        this.mHttpCache = CacheBuilder.newBuilder().concurrencyLevel(1).maximumSize(1000L).expireAfterWrite(600L, TimeUnit.SECONDS).build();
        final HashMap<String, String> mAndroidHeaders = new HashMap<String, String>();
        mAndroidHeaders.put("X-Android-Device-Manufacturer", mClientInformation.getAndroidDeviceManufacturer());
        mAndroidHeaders.put("X-Android-Device-Model", mClientInformation.getAndroidDeviceModel());
        mAndroidHeaders.put("X-Android-Device-Product", mClientInformation.getAndroidDeviceProduct());
        String s;
        if (mClientInformation.getAndroidDeviceIsGoogleTV()) {
            s = "1";
        }
        else {
            s = "0";
        }
        mAndroidHeaders.put("X-Android-Device-Is-GoogleTV", s);
        mAndroidHeaders.put("X-Android-SDK", String.valueOf(mClientInformation.getAndroidSDK()));
        mAndroidHeaders.put("X-Android-Release", mClientInformation.getAndroidRelease());
        mAndroidHeaders.put("X-Android-Application-Version-Code", String.valueOf(mClientInformation.getAndroidApplicationVersionCode()));
        mAndroidHeaders.put("X-Android-Application-Version-Name", mClientInformation.getAndroidApplicationVersionName());
        this.mAndroidHeaders = mAndroidHeaders;
    }
    
    private static String buildQueryString(final Map<String, String> map) {
        final String s = "";
        try {
            final StringBuilder sb = new StringBuilder(1024);
            final Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
            String s2 = s;
            while (iterator.hasNext()) {
                final Map.Entry<String, String> entry = iterator.next();
                sb.append(s2);
                sb.append(URLEncoder.encode(entry.getKey(), Charsets.UTF_8.name()));
                sb.append("=");
                sb.append(URLEncoder.encode(entry.getValue(), Charsets.UTF_8.name()));
                s2 = "&";
            }
            return sb.toString();
        }
        catch (UnsupportedEncodingException ex) {
            return "";
        }
    }
    
    private ApiResponse dispatchBatch(final List<ApiRequest> list, final Set<String> set, final ApiRequest.RequestMethod requestMethod) throws ApiException {
        return this.dispatchRequestAndRetry(new ApiBatchRequestWrapper(list, requestMethod, this.mApplicationState.getSession().getId(), set, this.mMapper, this.mApplicationState.getCustomLocale()), 0);
    }
    
    private ApiResponse dispatchRequest(final AbstractApiRequest abstractApiRequest) throws ApiException {
        TokenBuffer tokenBuffer = this.mHttpCache.getIfPresent(abstractApiRequest.getKey());
        while (true) {
            Label_0131: {
                if (tokenBuffer != null) {
                    break Label_0131;
                }
                try {
                    tokenBuffer = this.mMapper.readValue(this.fetchRequest(abstractApiRequest), TokenBuffer.class);
                    final JsonNode jsonNode = tokenBuffer.asParser(this.mMapper).readValueAsTree();
                    if (!(jsonNode.path("error") instanceof MissingNode) && jsonNode.path("error").asBoolean(true)) {
                        throw ApiErrorException.withErrorCode(ApiErrorException.ApiErrorCode.getErrorStatus(jsonNode.path("code").asText())).message(jsonNode.path("message").asText()).build();
                    }
                    return new ApiResponse(abstractApiRequest.getKey(), tokenBuffer, this.mHttpCache);
                }
                catch (IOException ex) {
                    throw new ApiBadResponseException(ex);
                }
                break Label_0131;
            }
            continue;
        }
    }
    
    private ApiResponse dispatchRequestAndRetry(final AbstractApiRequest abstractApiRequest, final int n) throws ApiException {
        if ("start_session".equals(abstractApiRequest.getApiMethod())) {
            if (this.mIsStartingSession.get()) {
                while (this.mIsStartingSession.get()) {
                    try {
                        Thread.sleep(500L);
                    }
                    catch (InterruptedException ex) {}
                }
            }
            abstractApiRequest.setSessionId(this.mApplicationState.getSession().getId());
        }
        try {
            return this.dispatchRequest(abstractApiRequest);
        }
        catch (ApiBadSessionException ex2) {
            final int intValue = (int)(Object)Double.valueOf(Math.pow(2.0, n));
            if (n > 3) {
                throw new ApiException("Open and use new session failed during 4 attemps, aborting.");
            }
            final long n2 = intValue * 1000;
            try {
                Thread.sleep(n2);
                if (abstractApiRequest.getSessionId().isPresent() && abstractApiRequest.getSessionId().get().equals(this.mApplicationState.getSession().getId()) && this.mIsStartingSession.compareAndSet(false, true)) {
                    this.openSession();
                }
                return this.dispatchRequestAndRetry(abstractApiRequest, n + 1);
            }
            catch (InterruptedException ex3) {}
        }
    }
    
    private String fetchRequest(final ApiRequest p0) throws ApiBadResponseException, ApiNetworkException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_1        
        //     1: invokeinterface com/crunchyroll/android/api/ApiRequest.getMethod:()Lcom/crunchyroll/android/api/ApiRequest$RequestMethod;
        //     6: astore_3       
        //     7: new             Ljava/net/URL;
        //    10: dup            
        //    11: aload_1        
        //    12: invokeinterface com/crunchyroll/android/api/ApiRequest.getUrl:()Ljava/lang/String;
        //    17: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
        //    20: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
        //    23: astore          5
        //    25: aload           5
        //    27: sipush          15000
        //    30: invokevirtual   java/net/URLConnection.setConnectTimeout:(I)V
        //    33: aload           5
        //    35: sipush          15000
        //    38: invokevirtual   java/net/URLConnection.setReadTimeout:(I)V
        //    41: getstatic       com/crunchyroll/android/api/ApiRequest$RequestMethod.POST:Lcom/crunchyroll/android/api/ApiRequest$RequestMethod;
        //    44: aload_3        
        //    45: invokevirtual   com/crunchyroll/android/api/ApiRequest$RequestMethod.equals:(Ljava/lang/Object;)Z
        //    48: ifeq            57
        //    51: aload           5
        //    53: iconst_1       
        //    54: invokevirtual   java/net/URLConnection.setDoOutput:(Z)V
        //    57: aload_0        
        //    58: invokevirtual   com/crunchyroll/android/api/ApiService.getAndroidHeaders:()Ljava/util/Map;
        //    61: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
        //    66: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //    71: astore          4
        //    73: aload           4
        //    75: invokeinterface java/util/Iterator.hasNext:()Z
        //    80: ifeq            136
        //    83: aload           4
        //    85: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    90: checkcast       Ljava/util/Map$Entry;
        //    93: astore          6
        //    95: aload           5
        //    97: aload           6
        //    99: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
        //   104: checkcast       Ljava/lang/String;
        //   107: aload           6
        //   109: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //   114: checkcast       Ljava/lang/String;
        //   117: invokevirtual   java/net/URLConnection.setRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   120: goto            73
        //   123: astore_1       
        //   124: new             Lcom/crunchyroll/android/api/exceptions/ApiNetworkException;
        //   127: dup            
        //   128: ldc_w           "Network host did not resolve"
        //   131: aload_1        
        //   132: invokespecial   com/crunchyroll/android/api/exceptions/ApiNetworkException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   135: athrow         
        //   136: aload_1        
        //   137: invokeinterface com/crunchyroll/android/api/ApiRequest.requiresOauth:()Z
        //   142: ifeq            166
        //   145: new             Loauth/signpost/basic/DefaultOAuthConsumer;
        //   148: dup            
        //   149: ldc_w           "com.crunchyroll.crunchyroid"
        //   152: ldc_w           "pGp7amzPhUCJ9Zu"
        //   155: invokespecial   oauth/signpost/basic/DefaultOAuthConsumer.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //   158: aload           5
        //   160: invokeinterface oauth/signpost/OAuthConsumer.sign:(Ljava/lang/Object;)Loauth/signpost/http/HttpRequest;
        //   165: pop            
        //   166: getstatic       com/crunchyroll/android/api/ApiRequest$RequestMethod.POST:Lcom/crunchyroll/android/api/ApiRequest$RequestMethod;
        //   169: aload_3        
        //   170: invokevirtual   com/crunchyroll/android/api/ApiRequest$RequestMethod.equals:(Ljava/lang/Object;)Z
        //   173: istore_2       
        //   174: iload_2        
        //   175: ifeq            229
        //   178: aconst_null    
        //   179: astore          4
        //   181: aload           4
        //   183: astore_3       
        //   184: aload_1        
        //   185: invokeinterface com/crunchyroll/android/api/ApiRequest.getParams:()Ljava/util/Map;
        //   190: invokestatic    com/crunchyroll/android/api/ApiService.buildQueryString:(Ljava/util/Map;)Ljava/lang/String;
        //   193: astore          6
        //   195: aload           4
        //   197: astore_3       
        //   198: aload           5
        //   200: invokevirtual   java/net/URLConnection.getOutputStream:()Ljava/io/OutputStream;
        //   203: astore_1       
        //   204: aload_1        
        //   205: astore_3       
        //   206: aload_1        
        //   207: aload           6
        //   209: getstatic       com/google/common/base/Charsets.UTF_8:Ljava/nio/charset/Charset;
        //   212: invokevirtual   java/nio/charset/Charset.name:()Ljava/lang/String;
        //   215: invokevirtual   java/lang/String.getBytes:(Ljava/lang/String;)[B
        //   218: invokevirtual   java/io/OutputStream.write:([B)V
        //   221: aload_1        
        //   222: ifnull          229
        //   225: aload_1        
        //   226: invokevirtual   java/io/OutputStream.close:()V
        //   229: new             Ljava/io/InputStreamReader;
        //   232: dup            
        //   233: aload           5
        //   235: invokevirtual   java/net/URLConnection.getInputStream:()Ljava/io/InputStream;
        //   238: getstatic       com/google/common/base/Charsets.UTF_8:Ljava/nio/charset/Charset;
        //   241: invokevirtual   java/nio/charset/Charset.name:()Ljava/lang/String;
        //   244: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;Ljava/lang/String;)V
        //   247: astore_1       
        //   248: aload_1        
        //   249: invokestatic    com/google/common/io/CharStreams.toString:(Ljava/lang/Readable;)Ljava/lang/String;
        //   252: astore_3       
        //   253: aload_1        
        //   254: invokevirtual   java/io/InputStreamReader.close:()V
        //   257: aload_3        
        //   258: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //   261: ifeq            395
        //   264: new             Lcom/crunchyroll/android/api/exceptions/ApiBadResponseException;
        //   267: dup            
        //   268: new             Ljava/lang/StringBuilder;
        //   271: dup            
        //   272: invokespecial   java/lang/StringBuilder.<init>:()V
        //   275: ldc_w           "Request '"
        //   278: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   281: aload_3        
        //   282: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   285: ldc_w           "' is null or empty"
        //   288: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   291: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   294: invokespecial   com/crunchyroll/android/api/exceptions/ApiBadResponseException.<init>:(Ljava/lang/String;)V
        //   297: athrow         
        //   298: astore_1       
        //   299: new             Lcom/crunchyroll/android/api/exceptions/ApiNetworkException;
        //   302: dup            
        //   303: ldc_w           "Network unreachable"
        //   306: aload_1        
        //   307: invokespecial   com/crunchyroll/android/api/exceptions/ApiNetworkException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   310: athrow         
        //   311: astore_1       
        //   312: aload_3        
        //   313: ifnull          320
        //   316: aload_3        
        //   317: invokevirtual   java/io/OutputStream.close:()V
        //   320: aload_1        
        //   321: athrow         
        //   322: astore_1       
        //   323: new             Lcom/crunchyroll/android/api/exceptions/ApiNetworkException;
        //   326: dup            
        //   327: ldc_w           "Network connection timeout"
        //   330: aload_1        
        //   331: invokespecial   com/crunchyroll/android/api/exceptions/ApiNetworkException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   334: athrow         
        //   335: astore_1       
        //   336: new             Lcom/crunchyroll/android/api/exceptions/ApiBadResponseException;
        //   339: dup            
        //   340: ldc_w           "IOException in fetchRequest"
        //   343: aload_1        
        //   344: invokespecial   com/crunchyroll/android/api/exceptions/ApiBadResponseException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   347: athrow         
        //   348: astore_1       
        //   349: new             Lcom/crunchyroll/android/api/exceptions/ApiBadResponseException;
        //   352: dup            
        //   353: ldc_w           "Oauth failed"
        //   356: aload_1        
        //   357: invokespecial   com/crunchyroll/android/api/exceptions/ApiBadResponseException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   360: athrow         
        //   361: astore_1       
        //   362: new             Lcom/crunchyroll/android/api/exceptions/ApiBadResponseException;
        //   365: dup            
        //   366: ldc_w           "Oauth comminication failed"
        //   369: aload_1        
        //   370: invokespecial   com/crunchyroll/android/api/exceptions/ApiBadResponseException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   373: athrow         
        //   374: astore_1       
        //   375: new             Lcom/crunchyroll/android/api/exceptions/ApiBadResponseException;
        //   378: dup            
        //   379: ldc_w           "Oauth signing failed"
        //   382: aload_1        
        //   383: invokespecial   com/crunchyroll/android/api/exceptions/ApiBadResponseException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   386: athrow         
        //   387: astore_1       
        //   388: goto            229
        //   391: astore_3       
        //   392: goto            320
        //   395: aload_3        
        //   396: areturn        
        //    Exceptions:
        //  throws com.crunchyroll.android.api.exceptions.ApiBadResponseException
        //  throws com.crunchyroll.android.api.exceptions.ApiNetworkException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                      
        //  -----  -----  -----  -----  ----------------------------------------------------------
        //  0      57     123    136    Ljava/net/UnknownHostException;
        //  0      57     298    311    Ljava/net/SocketException;
        //  0      57     322    335    Ljava/net/SocketTimeoutException;
        //  0      57     335    348    Ljava/io/IOException;
        //  0      57     348    361    Loauth/signpost/exception/OAuthExpectationFailedException;
        //  0      57     361    374    Loauth/signpost/exception/OAuthCommunicationException;
        //  0      57     374    387    Loauth/signpost/exception/OAuthMessageSignerException;
        //  57     73     123    136    Ljava/net/UnknownHostException;
        //  57     73     298    311    Ljava/net/SocketException;
        //  57     73     322    335    Ljava/net/SocketTimeoutException;
        //  57     73     335    348    Ljava/io/IOException;
        //  57     73     348    361    Loauth/signpost/exception/OAuthExpectationFailedException;
        //  57     73     361    374    Loauth/signpost/exception/OAuthCommunicationException;
        //  57     73     374    387    Loauth/signpost/exception/OAuthMessageSignerException;
        //  73     120    123    136    Ljava/net/UnknownHostException;
        //  73     120    298    311    Ljava/net/SocketException;
        //  73     120    322    335    Ljava/net/SocketTimeoutException;
        //  73     120    335    348    Ljava/io/IOException;
        //  73     120    348    361    Loauth/signpost/exception/OAuthExpectationFailedException;
        //  73     120    361    374    Loauth/signpost/exception/OAuthCommunicationException;
        //  73     120    374    387    Loauth/signpost/exception/OAuthMessageSignerException;
        //  136    166    123    136    Ljava/net/UnknownHostException;
        //  136    166    298    311    Ljava/net/SocketException;
        //  136    166    322    335    Ljava/net/SocketTimeoutException;
        //  136    166    335    348    Ljava/io/IOException;
        //  136    166    348    361    Loauth/signpost/exception/OAuthExpectationFailedException;
        //  136    166    361    374    Loauth/signpost/exception/OAuthCommunicationException;
        //  136    166    374    387    Loauth/signpost/exception/OAuthMessageSignerException;
        //  166    174    123    136    Ljava/net/UnknownHostException;
        //  166    174    298    311    Ljava/net/SocketException;
        //  166    174    322    335    Ljava/net/SocketTimeoutException;
        //  166    174    335    348    Ljava/io/IOException;
        //  166    174    348    361    Loauth/signpost/exception/OAuthExpectationFailedException;
        //  166    174    361    374    Loauth/signpost/exception/OAuthCommunicationException;
        //  166    174    374    387    Loauth/signpost/exception/OAuthMessageSignerException;
        //  184    195    311    322    Any
        //  198    204    311    322    Any
        //  206    221    311    322    Any
        //  225    229    387    391    Ljava/io/IOException;
        //  225    229    123    136    Ljava/net/UnknownHostException;
        //  225    229    298    311    Ljava/net/SocketException;
        //  225    229    322    335    Ljava/net/SocketTimeoutException;
        //  225    229    348    361    Loauth/signpost/exception/OAuthExpectationFailedException;
        //  225    229    361    374    Loauth/signpost/exception/OAuthCommunicationException;
        //  225    229    374    387    Loauth/signpost/exception/OAuthMessageSignerException;
        //  229    298    123    136    Ljava/net/UnknownHostException;
        //  229    298    298    311    Ljava/net/SocketException;
        //  229    298    322    335    Ljava/net/SocketTimeoutException;
        //  229    298    335    348    Ljava/io/IOException;
        //  229    298    348    361    Loauth/signpost/exception/OAuthExpectationFailedException;
        //  229    298    361    374    Loauth/signpost/exception/OAuthCommunicationException;
        //  229    298    374    387    Loauth/signpost/exception/OAuthMessageSignerException;
        //  316    320    391    395    Ljava/io/IOException;
        //  316    320    123    136    Ljava/net/UnknownHostException;
        //  316    320    298    311    Ljava/net/SocketException;
        //  316    320    322    335    Ljava/net/SocketTimeoutException;
        //  316    320    348    361    Loauth/signpost/exception/OAuthExpectationFailedException;
        //  316    320    361    374    Loauth/signpost/exception/OAuthCommunicationException;
        //  316    320    374    387    Loauth/signpost/exception/OAuthMessageSignerException;
        //  320    322    123    136    Ljava/net/UnknownHostException;
        //  320    322    298    311    Ljava/net/SocketException;
        //  320    322    322    335    Ljava/net/SocketTimeoutException;
        //  320    322    335    348    Ljava/io/IOException;
        //  320    322    348    361    Loauth/signpost/exception/OAuthExpectationFailedException;
        //  320    322    361    374    Loauth/signpost/exception/OAuthCommunicationException;
        //  320    322    374    387    Loauth/signpost/exception/OAuthMessageSignerException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 178, Size: 178
        //     at java.util.ArrayList.rangeCheck(ArrayList.java:653)
        //     at java.util.ArrayList.get(ArrayList.java:429)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3303)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
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
    
    public void clearCache() {
        this.mHttpCache.invalidateAll();
    }
    
    public Map<String, String> getAndroidHeaders() {
        final Map<String, String> mAndroidHeaders = this.mAndroidHeaders;
        if (this.mApplicationState.getUseHls()) {
            mAndroidHeaders.put("Using-Brightcove-Player", "1");
        }
        return mAndroidHeaders;
    }
    
    public void invalidate(final CacheKey cacheKey) {
        if (this.mHttpCache.getIfPresent(cacheKey) != null) {}
        this.mHttpCache.invalidate(cacheKey);
    }
    
    public void openSession() {
        try {
            this.mApplicationState.setSession(this.mMapper.readValue(this.runSessionless(new StartSessionRequest(this.mApplicationState.getAuth().orNull(), null), this.mClientInformation).body.asParser(this.mMapper).readValueAsTree().path("data").traverse(), Session.class));
        }
        catch (IOException ex) {
            throw new ApiBadResponseException(ex);
        }
        finally {
            this.mIsStartingSession.set(false);
        }
    }
    
    public ApiResponse postBatch(final List<ApiRequest> list, final Set<String> set) throws ApiException {
        return this.dispatchBatch(list, set, ApiRequest.RequestMethod.POST);
    }
    
    public ApiResponse run(final ApiRequest apiRequest) throws ApiException {
        return this.run(apiRequest, null);
    }
    
    public ApiResponse run(final ApiRequest apiRequest, final Set<String> set) throws ApiException {
        return this.dispatchRequestAndRetry(new ApiSingleRequestWrapper(apiRequest, this.mApplicationState.getSession().getId(), set, this.mApplicationState.getCustomLocale()), 0);
    }
    
    public ApiResponse runSessionless(final AbstractApiRequest abstractApiRequest, final ClientInformation clientInformation) throws ApiException {
        return this.dispatchRequest(new ApiSingleRequestWrapper(abstractApiRequest, clientInformation, null, this.mApplicationState.getCustomLocale()));
    }
    
    private static class ApiBatchRequestWrapper extends AbstractApiRequest
    {
        private static final long serialVersionUID = -172665767012508694L;
        private final Optional<ClientInformation> mClientInformation;
        private final Optional<Set<String>> mFields;
        private final String mLocale;
        private final RequestMethod mMethod;
        private final ObjectMapper mObjectMapper;
        private final List<ApiRequest> mRequests;
        
        public ApiBatchRequestWrapper(final List<ApiRequest> mRequests, final RequestMethod mMethod, final String sessionId, final Set<String> set, final ObjectMapper mObjectMapper, final String mLocale) {
            if (sessionId == null) {
                throw new IllegalArgumentException("sessionId must not be null");
            }
            this.setSessionId(sessionId);
            this.mRequests = mRequests;
            this.mMethod = mMethod;
            this.mClientInformation = Optional.absent();
            this.mFields = Optional.fromNullable(set);
            this.mObjectMapper = mObjectMapper;
            this.mLocale = mLocale;
        }
        
        @Override
        public String getApiMethod() {
            return "batch";
        }
        
        @Override
        public RequestMethod getMethod() {
            return this.mMethod;
        }
        
        @Override
        public ImmutableMap<String, String> getParams() {
            final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
            builder.put("locale", this.mLocale);
            if (this.mFields.isPresent() && this.mFields.get().size() > 0) {
                builder.put("fields", Joiner.on(',').join(this.mFields.get()));
            }
            ByteArrayOutputStream byteArrayOutputStream;
            JsonGenerator generator;
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                generator = this.mObjectMapper.getFactory().createGenerator(byteArrayOutputStream);
                generator.writeStartArray();
                for (final ApiRequest apiRequest : this.mRequests) {
                    final ImmutableMap.Builder<String, String> builder2 = ImmutableMap.builder();
                    final Map<String, String> params = apiRequest.getParams();
                    builder2.putAll(params);
                    if (params.get("locale") == null) {
                        builder2.put("locale", this.mLocale);
                    }
                    generator.writeStartObject();
                    generator.writeStringField("method", apiRequest.getMethod().name());
                    generator.writeStringField("api_method", apiRequest.getApiMethod());
                    generator.writeNumberField("method_version", apiRequest.getVersion());
                    generator.writeObjectField("params", builder2.build());
                    generator.writeEndObject();
                }
            }
            catch (IOException ex) {
                throw new ApiUnknownException("Problem encoding batch request", ex);
            }
            generator.writeEndArray();
            generator.close();
            final String string = byteArrayOutputStream.toString();
            byteArrayOutputStream.close();
            builder.put("requests", string);
            if (this.sessionId.isPresent()) {
                builder.put("session_id", this.sessionId.get());
            }
            else if (this.mClientInformation.isPresent()) {
                builder.put("device_id", this.mClientInformation.get().getDeviceId());
                builder.put("device_type", this.mClientInformation.get().getDeviceType());
                builder.put("access_token", "Scwg9PRRZ19iVwD");
                builder.put("version", String.valueOf(this.mClientInformation.get().getAndroidApplicationVersionCode()));
            }
            return builder.build();
        }
        
        @Override
        public String getUrl() {
            return "https://api.crunchyroll.com/" + this.getApiMethod() + "." + this.getVersion() + "." + "json";
        }
        
        @Override
        public int getVersion() {
            return 0;
        }
        
        @Override
        public String toString() {
            return "ApiBatchRequestWrapper [getUrl()=" + this.getUrl() + ", getParams()=" + this.getParams() + "]";
        }
    }
    
    private static class ApiSingleRequestWrapper extends AbstractApiRequest
    {
        private static final long serialVersionUID = -6779261247174816636L;
        private final Optional<ClientInformation> clientInformation;
        private final Optional<Set<String>> fields;
        private final String locale;
        private final ApiRequest request;
        
        public ApiSingleRequestWrapper(final ApiRequest request, final ClientInformation clientInformation, final Set<String> set, final String locale) {
            if (clientInformation == null) {
                throw new IllegalArgumentException("clientInformation must not be null");
            }
            this.request = request;
            this.sessionId = Optional.absent();
            this.clientInformation = Optional.of(clientInformation);
            this.fields = Optional.fromNullable(set);
            this.locale = locale;
        }
        
        public ApiSingleRequestWrapper(final ApiRequest request, final String s, final Set<String> set, final String locale) {
            if (s == null) {
                throw new ApiBadResponseException("sessionId must not be null");
            }
            this.request = request;
            this.sessionId = Optional.of(s);
            this.clientInformation = Optional.absent();
            this.fields = Optional.fromNullable(set);
            this.locale = locale;
        }
        
        @Override
        public String getApiMethod() {
            return this.request.getApiMethod();
        }
        
        @Override
        public Object getKey() {
            return this.request.getKey();
        }
        
        @Override
        public RequestMethod getMethod() {
            return this.request.getMethod();
        }
        
        @Override
        public ImmutableMap<String, String> getParams() {
            final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
            if (this.fields.isPresent() && this.fields.get().size() > 0) {
                builder.put("fields", Joiner.on(',').join(this.fields.get()));
            }
            final Map<String, String> params = this.request.getParams();
            builder.putAll(params);
            if (params.get("locale") == null) {
                builder.put("locale", this.locale);
            }
            if (this.sessionId.isPresent()) {
                builder.put("session_id", this.sessionId.get());
            }
            else if (this.clientInformation.isPresent()) {
                builder.put("device_id", this.clientInformation.get().getDeviceId());
                builder.put("device_type", this.clientInformation.get().getDeviceType());
                builder.put("access_token", "Scwg9PRRZ19iVwD");
                builder.put("version", String.valueOf(this.clientInformation.get().getAndroidApplicationVersionCode()));
            }
            return builder.build();
        }
        
        @Override
        public String getUrl() {
            if (RequestMethod.POST.equals(this.getMethod())) {
                return this.request.getUrl();
            }
            if (RequestMethod.GET.equals(this.getMethod())) {
                return this.request.getUrl() + '?' + buildQueryString(this.getParams());
            }
            return this.request.getUrl();
        }
        
        @Override
        public int getVersion() {
            return this.request.getVersion();
        }
        
        @Override
        public boolean requiresOauth() {
            return this.request.requiresOauth();
        }
        
        @Override
        public String toString() {
            return "ApiSingleRequestWrapper [getUrl()=" + this.getUrl() + ", getParams()=" + this.getParams() + "]";
        }
    }
}
