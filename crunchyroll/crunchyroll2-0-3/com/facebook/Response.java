// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook;

import java.net.MalformedURLException;
import java.net.URL;
import android.content.Context;
import org.json.JSONTokener;
import com.facebook.internal.Logger;
import java.io.InputStream;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONArray;
import com.facebook.internal.Utility;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import com.facebook.model.GraphObjectList;
import com.facebook.model.GraphObject;
import java.net.HttpURLConnection;
import com.facebook.internal.FileLruCache;

public class Response
{
    private static final String BODY_KEY = "body";
    private static final String CODE_KEY = "code";
    private static final int INVALID_SESSION_FACEBOOK_ERROR_CODE = 190;
    public static final String NON_JSON_RESPONSE_PROPERTY = "FACEBOOK_NON_JSON_RESULT";
    private static final String RESPONSE_CACHE_TAG = "ResponseCache";
    private static final String RESPONSE_LOG_TAG = "Response";
    public static final String SUCCESS_KEY = "success";
    private static FileLruCache responseCache;
    private final HttpURLConnection connection;
    private final FacebookRequestError error;
    private final GraphObject graphObject;
    private final GraphObjectList<GraphObject> graphObjectList;
    private final boolean isFromCache;
    private final String rawResponse;
    private final Request request;
    
    Response(final Request request, final HttpURLConnection httpURLConnection, final FacebookRequestError facebookRequestError) {
        this(request, httpURLConnection, null, null, null, false, facebookRequestError);
    }
    
    Response(final Request request, final HttpURLConnection connection, final String rawResponse, final GraphObject graphObject, final GraphObjectList<GraphObject> graphObjectList, final boolean isFromCache, final FacebookRequestError error) {
        this.request = request;
        this.connection = connection;
        this.rawResponse = rawResponse;
        this.graphObject = graphObject;
        this.graphObjectList = graphObjectList;
        this.isFromCache = isFromCache;
        this.error = error;
    }
    
    Response(final Request request, final HttpURLConnection httpURLConnection, final String s, final GraphObject graphObject, final boolean b) {
        this(request, httpURLConnection, s, graphObject, null, b, null);
    }
    
    Response(final Request request, final HttpURLConnection httpURLConnection, final String s, final GraphObjectList<GraphObject> list, final boolean b) {
        this(request, httpURLConnection, s, null, list, b, null);
    }
    
    static List<Response> constructErrorResponses(final List<Request> list, final HttpURLConnection httpURLConnection, final FacebookException ex) {
        final int size = list.size();
        final ArrayList list2 = new ArrayList<Response>(size);
        for (int i = 0; i < size; ++i) {
            list2.add(new Response(list.get(i), httpURLConnection, new FacebookRequestError(httpURLConnection, ex)));
        }
        return (List<Response>)list2;
    }
    
    private static Response createResponseFromObject(final Request request, final HttpURLConnection httpURLConnection, Object stringPropertyAsJSON, final boolean b, final Object o) throws JSONException {
        Object null = stringPropertyAsJSON;
        if (stringPropertyAsJSON instanceof JSONObject) {
            final JSONObject jsonObject = (JSONObject)stringPropertyAsJSON;
            final FacebookRequestError checkResponseAndCreateError = FacebookRequestError.checkResponseAndCreateError(jsonObject, o, httpURLConnection);
            if (checkResponseAndCreateError != null) {
                if (checkResponseAndCreateError.getErrorCode() == 190) {
                    final Session session = request.getSession();
                    if (session != null) {
                        session.closeAndClearTokenInformation();
                    }
                }
                return new Response(request, httpURLConnection, checkResponseAndCreateError);
            }
            stringPropertyAsJSON = Utility.getStringPropertyAsJSON(jsonObject, "body", "FACEBOOK_NON_JSON_RESULT");
            if (stringPropertyAsJSON instanceof JSONObject) {
                return new Response(request, httpURLConnection, stringPropertyAsJSON.toString(), GraphObject.Factory.create((JSONObject)stringPropertyAsJSON), b);
            }
            if (stringPropertyAsJSON instanceof JSONArray) {
                return new Response(request, httpURLConnection, stringPropertyAsJSON.toString(), GraphObject.Factory.createList((JSONArray)stringPropertyAsJSON, GraphObject.class), b);
            }
            null = JSONObject.NULL;
        }
        if (null == JSONObject.NULL) {
            return new Response(request, httpURLConnection, null.toString(), (GraphObject)null, b);
        }
        throw new FacebookException("Got unexpected object type in response, class: " + null.getClass().getSimpleName());
    }
    
    private static List<Response> createResponsesFromObject(final HttpURLConnection httpURLConnection, final List<Request> list, final Object o, final boolean b) throws FacebookException, JSONException {
        assert !(!b);
        final int size = list.size();
        final ArrayList list2 = new ArrayList<Response>(size);
        Object o2 = o;
        while (true) {
            if (size == 1) {
                final Request request = list.get(0);
                try {
                    final JSONObject jsonObject = new JSONObject();
                    jsonObject.put("body", o);
                    int responseCode;
                    if (httpURLConnection != null) {
                        responseCode = httpURLConnection.getResponseCode();
                    }
                    else {
                        responseCode = 200;
                    }
                    jsonObject.put("code", responseCode);
                    o2 = new JSONArray();
                    ((JSONArray)o2).put((Object)jsonObject);
                    if (!(o2 instanceof JSONArray) || ((JSONArray)o2).length() != size) {
                        throw new FacebookException("Unexpected number of results");
                    }
                }
                catch (JSONException ex) {
                    list2.add(new Response(request, httpURLConnection, new FacebookRequestError(httpURLConnection, (Exception)ex)));
                    o2 = o;
                    continue;
                }
                catch (IOException ex2) {
                    list2.add(new Response(request, httpURLConnection, new FacebookRequestError(httpURLConnection, ex2)));
                    o2 = o;
                    continue;
                }
                final JSONArray jsonArray = (JSONArray)o2;
                int i = 0;
            Label_0284_Outer:
                while (i < jsonArray.length()) {
                    final Request request2 = list.get(i);
                    while (true) {
                        try {
                            list2.add(createResponseFromObject(request2, httpURLConnection, jsonArray.get(i), b, o));
                            ++i;
                            continue Label_0284_Outer;
                        }
                        catch (JSONException ex3) {
                            list2.add(new Response(request2, httpURLConnection, new FacebookRequestError(httpURLConnection, (Exception)ex3)));
                            continue;
                        }
                        catch (FacebookException ex4) {
                            list2.add(new Response(request2, httpURLConnection, new FacebookRequestError(httpURLConnection, ex4)));
                            continue;
                        }
                        break;
                    }
                    break;
                }
                return (List<Response>)list2;
            }
            continue;
        }
    }
    
    static List<Response> createResponsesFromStream(final InputStream inputStream, final HttpURLConnection httpURLConnection, final RequestBatch requestBatch, final boolean b) throws FacebookException, JSONException, IOException {
        final String streamToString = Utility.readStreamToString(inputStream);
        Logger.log(LoggingBehavior.INCLUDE_RAW_RESPONSES, "Response", "Response (raw)\n  Size: %d\n  Response:\n%s\n", streamToString.length(), streamToString);
        return createResponsesFromString(streamToString, httpURLConnection, requestBatch, b);
    }
    
    static List<Response> createResponsesFromString(final String s, final HttpURLConnection httpURLConnection, final RequestBatch requestBatch, final boolean b) throws FacebookException, JSONException, IOException {
        final List<Response> responsesFromObject = createResponsesFromObject(httpURLConnection, requestBatch, new JSONTokener(s).nextValue(), b);
        Logger.log(LoggingBehavior.REQUESTS, "Response", "Response\n  Id: %s\n  Size: %d\n  Responses:\n%s\n", requestBatch.getId(), s.length(), responsesFromObject);
        return responsesFromObject;
    }
    
    static List<Response> fromHttpConnection(final HttpURLConnection p0, final RequestBatch p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aconst_null    
        //     1: astore          11
        //     3: aconst_null    
        //     4: astore          5
        //     6: aconst_null    
        //     7: astore          6
        //     9: aconst_null    
        //    10: astore          7
        //    12: aconst_null    
        //    13: astore          4
        //    15: aconst_null    
        //    16: astore          9
        //    18: aconst_null    
        //    19: astore          10
        //    21: aload           4
        //    23: astore_2       
        //    24: aload_1        
        //    25: instanceof      Lcom/facebook/internal/CacheableRequestBatch;
        //    28: ifeq            194
        //    31: aload_1        
        //    32: checkcast       Lcom/facebook/internal/CacheableRequestBatch;
        //    35: astore          12
        //    37: invokestatic    com/facebook/Response.getResponseCache:()Lcom/facebook/internal/FileLruCache;
        //    40: astore          8
        //    42: aload           12
        //    44: invokevirtual   com/facebook/internal/CacheableRequestBatch.getCacheKeyOverride:()Ljava/lang/String;
        //    47: astore_2       
        //    48: aload_2        
        //    49: astore_3       
        //    50: aload_2        
        //    51: invokestatic    com/facebook/internal/Utility.isNullOrEmpty:(Ljava/lang/String;)Z
        //    54: ifeq            74
        //    57: aload_1        
        //    58: invokevirtual   com/facebook/RequestBatch.size:()I
        //    61: iconst_1       
        //    62: if_icmpne       167
        //    65: aload_1        
        //    66: iconst_0       
        //    67: invokevirtual   com/facebook/RequestBatch.get:(I)Lcom/facebook/Request;
        //    70: invokevirtual   com/facebook/Request.getUrlForSingleRequest:()Ljava/lang/String;
        //    73: astore_3       
        //    74: aload           8
        //    76: astore          9
        //    78: aload_3        
        //    79: astore          10
        //    81: aload           4
        //    83: astore_2       
        //    84: aload           12
        //    86: invokevirtual   com/facebook/internal/CacheableRequestBatch.getForceRoundTrip:()Z
        //    89: ifne            194
        //    92: aload           8
        //    94: astore          9
        //    96: aload_3        
        //    97: astore          10
        //    99: aload           4
        //   101: astore_2       
        //   102: aload           8
        //   104: ifnull          194
        //   107: aload           8
        //   109: astore          9
        //   111: aload_3        
        //   112: astore          10
        //   114: aload           4
        //   116: astore_2       
        //   117: aload_3        
        //   118: invokestatic    com/facebook/internal/Utility.isNullOrEmpty:(Ljava/lang/String;)Z
        //   121: ifne            194
        //   124: aload           11
        //   126: astore          4
        //   128: aload           8
        //   130: aload_3        
        //   131: invokevirtual   com/facebook/internal/FileLruCache.get:(Ljava/lang/String;)Ljava/io/InputStream;
        //   134: astore_2       
        //   135: aload_2        
        //   136: ifnull          183
        //   139: aload_2        
        //   140: astore          4
        //   142: aload_2        
        //   143: astore          5
        //   145: aload_2        
        //   146: astore          6
        //   148: aload_2        
        //   149: astore          7
        //   151: aload_2        
        //   152: aconst_null    
        //   153: aload_1        
        //   154: iconst_1       
        //   155: invokestatic    com/facebook/Response.createResponsesFromStream:(Ljava/io/InputStream;Ljava/net/HttpURLConnection;Lcom/facebook/RequestBatch;Z)Ljava/util/List;
        //   158: astore          9
        //   160: aload_2        
        //   161: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
        //   164: aload           9
        //   166: areturn        
        //   167: getstatic       com/facebook/LoggingBehavior.REQUESTS:Lcom/facebook/LoggingBehavior;
        //   170: ldc             "ResponseCache"
        //   172: ldc_w           "Not using cache for cacheable request because no key was specified"
        //   175: invokestatic    com/facebook/internal/Logger.log:(Lcom/facebook/LoggingBehavior;Ljava/lang/String;Ljava/lang/String;)V
        //   178: aload_2        
        //   179: astore_3       
        //   180: goto            74
        //   183: aload_2        
        //   184: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
        //   187: aload_3        
        //   188: astore          10
        //   190: aload           8
        //   192: astore          9
        //   194: aload_2        
        //   195: astore          5
        //   197: aload_2        
        //   198: astore          6
        //   200: aload_2        
        //   201: astore          7
        //   203: aload_2        
        //   204: astore          8
        //   206: aload_2        
        //   207: astore_3       
        //   208: aload_0        
        //   209: invokevirtual   java/net/HttpURLConnection.getResponseCode:()I
        //   212: sipush          400
        //   215: if_icmplt       332
        //   218: aload_2        
        //   219: astore          5
        //   221: aload_2        
        //   222: astore          6
        //   224: aload_2        
        //   225: astore          7
        //   227: aload_2        
        //   228: astore          8
        //   230: aload_2        
        //   231: astore_3       
        //   232: aload_0        
        //   233: invokevirtual   java/net/HttpURLConnection.getErrorStream:()Ljava/io/InputStream;
        //   236: astore_2       
        //   237: aload_2        
        //   238: astore          5
        //   240: aload_2        
        //   241: astore          6
        //   243: aload_2        
        //   244: astore          7
        //   246: aload_2        
        //   247: astore          8
        //   249: aload_2        
        //   250: astore_3       
        //   251: aload_2        
        //   252: aload_0        
        //   253: aload_1        
        //   254: iconst_0       
        //   255: invokestatic    com/facebook/Response.createResponsesFromStream:(Ljava/io/InputStream;Ljava/net/HttpURLConnection;Lcom/facebook/RequestBatch;Z)Ljava/util/List;
        //   258: astore          4
        //   260: aload_2        
        //   261: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
        //   264: aload           4
        //   266: areturn        
        //   267: astore_2       
        //   268: aload           4
        //   270: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
        //   273: aload           8
        //   275: astore          9
        //   277: aload_3        
        //   278: astore          10
        //   280: aload           4
        //   282: astore_2       
        //   283: goto            194
        //   286: astore_2       
        //   287: aload           5
        //   289: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
        //   292: aload           8
        //   294: astore          9
        //   296: aload_3        
        //   297: astore          10
        //   299: aload           5
        //   301: astore_2       
        //   302: goto            194
        //   305: astore_2       
        //   306: aload           6
        //   308: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
        //   311: aload           8
        //   313: astore          9
        //   315: aload_3        
        //   316: astore          10
        //   318: aload           6
        //   320: astore_2       
        //   321: goto            194
        //   324: astore_0       
        //   325: aload           7
        //   327: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
        //   330: aload_0        
        //   331: athrow         
        //   332: aload_2        
        //   333: astore          5
        //   335: aload_2        
        //   336: astore          6
        //   338: aload_2        
        //   339: astore          7
        //   341: aload_2        
        //   342: astore          8
        //   344: aload_2        
        //   345: astore_3       
        //   346: aload_0        
        //   347: invokevirtual   java/net/HttpURLConnection.getInputStream:()Ljava/io/InputStream;
        //   350: astore          4
        //   352: aload           4
        //   354: astore_2       
        //   355: aload           9
        //   357: ifnull          237
        //   360: aload           4
        //   362: astore_2       
        //   363: aload           10
        //   365: ifnull          237
        //   368: aload           4
        //   370: astore_2       
        //   371: aload           4
        //   373: ifnull          237
        //   376: aload           4
        //   378: astore          5
        //   380: aload           4
        //   382: astore          6
        //   384: aload           4
        //   386: astore          7
        //   388: aload           4
        //   390: astore          8
        //   392: aload           4
        //   394: astore_3       
        //   395: aload           9
        //   397: aload           10
        //   399: aload           4
        //   401: invokevirtual   com/facebook/internal/FileLruCache.interceptAndPut:(Ljava/lang/String;Ljava/io/InputStream;)Ljava/io/InputStream;
        //   404: astore          9
        //   406: aload           4
        //   408: astore_2       
        //   409: aload           9
        //   411: ifnull          237
        //   414: aload           9
        //   416: astore_2       
        //   417: goto            237
        //   420: astore_2       
        //   421: aload           5
        //   423: astore_3       
        //   424: getstatic       com/facebook/LoggingBehavior.REQUESTS:Lcom/facebook/LoggingBehavior;
        //   427: ldc             "Response"
        //   429: ldc_w           "Response <Error>: %s"
        //   432: iconst_1       
        //   433: anewarray       Ljava/lang/Object;
        //   436: dup            
        //   437: iconst_0       
        //   438: aload_2        
        //   439: aastore        
        //   440: invokestatic    com/facebook/internal/Logger.log:(Lcom/facebook/LoggingBehavior;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
        //   443: aload           5
        //   445: astore_3       
        //   446: aload_1        
        //   447: aload_0        
        //   448: aload_2        
        //   449: invokestatic    com/facebook/Response.constructErrorResponses:(Ljava/util/List;Ljava/net/HttpURLConnection;Lcom/facebook/FacebookException;)Ljava/util/List;
        //   452: astore_0       
        //   453: aload           5
        //   455: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
        //   458: aload_0        
        //   459: areturn        
        //   460: astore_2       
        //   461: aload           6
        //   463: astore_3       
        //   464: getstatic       com/facebook/LoggingBehavior.REQUESTS:Lcom/facebook/LoggingBehavior;
        //   467: ldc             "Response"
        //   469: ldc_w           "Response <Error>: %s"
        //   472: iconst_1       
        //   473: anewarray       Ljava/lang/Object;
        //   476: dup            
        //   477: iconst_0       
        //   478: aload_2        
        //   479: aastore        
        //   480: invokestatic    com/facebook/internal/Logger.log:(Lcom/facebook/LoggingBehavior;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
        //   483: aload           6
        //   485: astore_3       
        //   486: aload_1        
        //   487: aload_0        
        //   488: new             Lcom/facebook/FacebookException;
        //   491: dup            
        //   492: aload_2        
        //   493: invokespecial   com/facebook/FacebookException.<init>:(Ljava/lang/Throwable;)V
        //   496: invokestatic    com/facebook/Response.constructErrorResponses:(Ljava/util/List;Ljava/net/HttpURLConnection;Lcom/facebook/FacebookException;)Ljava/util/List;
        //   499: astore_0       
        //   500: aload           6
        //   502: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
        //   505: aload_0        
        //   506: areturn        
        //   507: astore_2       
        //   508: aload           7
        //   510: astore_3       
        //   511: getstatic       com/facebook/LoggingBehavior.REQUESTS:Lcom/facebook/LoggingBehavior;
        //   514: ldc             "Response"
        //   516: ldc_w           "Response <Error>: %s"
        //   519: iconst_1       
        //   520: anewarray       Ljava/lang/Object;
        //   523: dup            
        //   524: iconst_0       
        //   525: aload_2        
        //   526: aastore        
        //   527: invokestatic    com/facebook/internal/Logger.log:(Lcom/facebook/LoggingBehavior;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
        //   530: aload           7
        //   532: astore_3       
        //   533: aload_1        
        //   534: aload_0        
        //   535: new             Lcom/facebook/FacebookException;
        //   538: dup            
        //   539: aload_2        
        //   540: invokespecial   com/facebook/FacebookException.<init>:(Ljava/lang/Throwable;)V
        //   543: invokestatic    com/facebook/Response.constructErrorResponses:(Ljava/util/List;Ljava/net/HttpURLConnection;Lcom/facebook/FacebookException;)Ljava/util/List;
        //   546: astore_0       
        //   547: aload           7
        //   549: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
        //   552: aload_0        
        //   553: areturn        
        //   554: astore_2       
        //   555: aload           8
        //   557: astore_3       
        //   558: getstatic       com/facebook/LoggingBehavior.REQUESTS:Lcom/facebook/LoggingBehavior;
        //   561: ldc             "Response"
        //   563: ldc_w           "Response <Error>: %s"
        //   566: iconst_1       
        //   567: anewarray       Ljava/lang/Object;
        //   570: dup            
        //   571: iconst_0       
        //   572: aload_2        
        //   573: aastore        
        //   574: invokestatic    com/facebook/internal/Logger.log:(Lcom/facebook/LoggingBehavior;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
        //   577: aload           8
        //   579: astore_3       
        //   580: aload_1        
        //   581: aload_0        
        //   582: new             Lcom/facebook/FacebookException;
        //   585: dup            
        //   586: aload_2        
        //   587: invokespecial   com/facebook/FacebookException.<init>:(Ljava/lang/Throwable;)V
        //   590: invokestatic    com/facebook/Response.constructErrorResponses:(Ljava/util/List;Ljava/net/HttpURLConnection;Lcom/facebook/FacebookException;)Ljava/util/List;
        //   593: astore_0       
        //   594: aload           8
        //   596: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
        //   599: aload_0        
        //   600: areturn        
        //   601: astore_0       
        //   602: aload_3        
        //   603: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
        //   606: aload_0        
        //   607: athrow         
        //    Signature:
        //  (Ljava/net/HttpURLConnection;Lcom/facebook/RequestBatch;)Ljava/util/List<Lcom/facebook/Response;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  128    135    267    286    Lcom/facebook/FacebookException;
        //  128    135    286    305    Lorg/json/JSONException;
        //  128    135    305    324    Ljava/io/IOException;
        //  128    135    324    332    Any
        //  151    160    267    286    Lcom/facebook/FacebookException;
        //  151    160    286    305    Lorg/json/JSONException;
        //  151    160    305    324    Ljava/io/IOException;
        //  151    160    324    332    Any
        //  208    218    420    460    Lcom/facebook/FacebookException;
        //  208    218    460    507    Lorg/json/JSONException;
        //  208    218    507    554    Ljava/io/IOException;
        //  208    218    554    601    Ljava/lang/SecurityException;
        //  208    218    601    608    Any
        //  232    237    420    460    Lcom/facebook/FacebookException;
        //  232    237    460    507    Lorg/json/JSONException;
        //  232    237    507    554    Ljava/io/IOException;
        //  232    237    554    601    Ljava/lang/SecurityException;
        //  232    237    601    608    Any
        //  251    260    420    460    Lcom/facebook/FacebookException;
        //  251    260    460    507    Lorg/json/JSONException;
        //  251    260    507    554    Ljava/io/IOException;
        //  251    260    554    601    Ljava/lang/SecurityException;
        //  251    260    601    608    Any
        //  346    352    420    460    Lcom/facebook/FacebookException;
        //  346    352    460    507    Lorg/json/JSONException;
        //  346    352    507    554    Ljava/io/IOException;
        //  346    352    554    601    Ljava/lang/SecurityException;
        //  346    352    601    608    Any
        //  395    406    420    460    Lcom/facebook/FacebookException;
        //  395    406    460    507    Lorg/json/JSONException;
        //  395    406    507    554    Ljava/io/IOException;
        //  395    406    554    601    Ljava/lang/SecurityException;
        //  395    406    601    608    Any
        //  424    443    601    608    Any
        //  446    453    601    608    Any
        //  464    483    601    608    Any
        //  486    500    601    608    Any
        //  511    530    601    608    Any
        //  533    547    601    608    Any
        //  558    577    601    608    Any
        //  580    594    601    608    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0237:
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
    
    static FileLruCache getResponseCache() {
        if (Response.responseCache == null) {
            final Context staticContext = Session.getStaticContext();
            if (staticContext != null) {
                Response.responseCache = new FileLruCache(staticContext, "ResponseCache", new FileLruCache.Limits());
            }
        }
        return Response.responseCache;
    }
    
    public final HttpURLConnection getConnection() {
        return this.connection;
    }
    
    public final FacebookRequestError getError() {
        return this.error;
    }
    
    public final GraphObject getGraphObject() {
        return this.graphObject;
    }
    
    public final <T extends GraphObject> T getGraphObjectAs(final Class<T> clazz) {
        if (this.graphObject == null) {
            return null;
        }
        if (clazz == null) {
            throw new NullPointerException("Must pass in a valid interface that extends GraphObject");
        }
        return this.graphObject.cast(clazz);
    }
    
    public final GraphObjectList<GraphObject> getGraphObjectList() {
        return this.graphObjectList;
    }
    
    public final <T extends GraphObject> GraphObjectList<T> getGraphObjectListAs(final Class<T> clazz) {
        if (this.graphObjectList == null) {
            return null;
        }
        return this.graphObjectList.castToListOf(clazz);
    }
    
    public final boolean getIsFromCache() {
        return this.isFromCache;
    }
    
    public String getRawResponse() {
        return this.rawResponse;
    }
    
    public Request getRequest() {
        return this.request;
    }
    
    public Request getRequestForPagedResults(final PagingDirection pagingDirection) {
        String s2;
        final String s = s2 = null;
        if (this.graphObject != null) {
            final PagingInfo paging = this.graphObject.cast(PagedResults.class).getPaging();
            s2 = s;
            if (paging != null) {
                if (pagingDirection == PagingDirection.NEXT) {
                    s2 = paging.getNext();
                }
                else {
                    s2 = paging.getPrevious();
                }
            }
        }
        if (Utility.isNullOrEmpty(s2)) {
            return null;
        }
        if (s2 != null && s2.equals(this.request.getUrlForSingleRequest())) {
            return null;
        }
        try {
            return new Request(this.request.getSession(), new URL(s2));
        }
        catch (MalformedURLException ex) {
            return null;
        }
    }
    
    @Override
    public String toString() {
        try {
            int responseCode;
            if (this.connection != null) {
                responseCode = this.connection.getResponseCode();
            }
            else {
                responseCode = 200;
            }
            final String format = String.format("%d", responseCode);
            return "{Response: " + " responseCode: " + format + ", graphObject: " + this.graphObject + ", error: " + this.error + ", isFromCache:" + this.isFromCache + "}";
        }
        catch (IOException ex) {
            final String format = "unknown";
            return "{Response: " + " responseCode: " + format + ", graphObject: " + this.graphObject + ", error: " + this.error + ", isFromCache:" + this.isFromCache + "}";
        }
    }
    
    interface PagedResults extends GraphObject
    {
        GraphObjectList<GraphObject> getData();
        
        PagingInfo getPaging();
    }
    
    public enum PagingDirection
    {
        NEXT, 
        PREVIOUS;
    }
    
    interface PagingInfo extends GraphObject
    {
        String getNext();
        
        String getPrevious();
    }
}
