// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.rest;

import java.io.UnsupportedEncodingException;
import com.swrve.sdk.SwrveHelper;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class RESTClient implements IRESTClient
{
    private static List<String> metrics;
    
    static {
        RESTClient.metrics = new ArrayList<String>();
    }
    
    private boolean addMetric(final List<String> list, final long n, final List<String> list2, final String s, final boolean b) {
        if (n > 0L) {
            final Iterator<String> iterator = list2.iterator();
            while (iterator.hasNext()) {
                list.add(String.format(iterator.next(), n));
            }
        }
        else {
            list.add(s);
            if (b) {
                final Iterator<String> iterator2 = list2.iterator();
                while (iterator2.hasNext()) {
                    list.add(String.format(iterator2.next(), 15000));
                }
            }
            synchronized (RESTClient.metrics) {
                RESTClient.metrics.add(this.printList(list, ", "));
                return false;
            }
        }
        return true;
    }
    
    private HttpURLConnection addMetricsHeader(final HttpURLConnection httpURLConnection) {
        final ArrayList<Object> list = new ArrayList<Object>();
        synchronized (RESTClient.metrics) {
            list.addAll(RESTClient.metrics);
            RESTClient.metrics.clear();
            // monitorexit(RESTClient.metrics)
            if (!list.isEmpty()) {
                httpURLConnection.addRequestProperty("Swrve-Latency-Metrics", this.printList((List<String>)list, "; "));
            }
            return httpURLConnection;
        }
    }
    
    private String getUrlWithoutPathOrQuery(final String s) throws MalformedURLException {
        final URL url = new URL(s);
        return String.format("%s://%s", url.getProtocol(), url.getAuthority());
    }
    
    private long milisecondsFrom(final long n) {
        return TimeUnit.MILLISECONDS.convert(System.nanoTime() - n, TimeUnit.NANOSECONDS);
    }
    
    private String printList(final List<String> list, final String s) {
        final StringBuffer sb = new StringBuffer();
        int size = list.size();
        final Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next());
            final int n = size - 1;
            if ((size = n) > 0) {
                sb.append(s);
                size = n;
            }
        }
        return sb.toString();
    }
    
    private void recordGetMetrics(final String s, final long n, final long n2, final long n3, final boolean b) {
        this.recordMetrics(false, s, n, n2, n2, n3, b);
    }
    
    private void recordMetrics(final boolean b, final String s, final long n, final long n2, final long n3, final long n4, final boolean b2) {
        ArrayList<String> list;
        ArrayList<String> list2;
        while (true) {
            list = new ArrayList<String>();
            try {
                list.add(String.format("u=%s", this.getUrlWithoutPathOrQuery(s)));
                list2 = new ArrayList<String>();
                list2.add("c=%d");
                if (!this.addMetric(list, n, list2, "c_error=1", b2)) {
                    return;
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            list2.clear();
            list2.add("sh=%d");
            list2.add("sb=%d");
            if (b) {
                if (!this.addMetric(list, n2, list2, "sb_error=1", b2)) {
                    return;
                }
                list2.clear();
            }
            list2.add("rh=%d");
            if (!this.addMetric(list, n3, list2, "rh_error=1", b2)) {
                return;
            }
            list2.clear();
            list2.add("rb=%d");
            if (this.addMetric(list, n4, list2, "rb_error=1", b2)) {
                break;
            }
            return;
        }
        list2.clear();
        synchronized (RESTClient.metrics) {
            RESTClient.metrics.add(this.printList(list, ", "));
        }
    }
    
    private void recordPostMetrics(final String s, final long n, final long n2, final long n3, final long n4, final boolean b) {
        this.recordMetrics(true, s, n, n2, n3, n4, b);
    }
    
    public void get(final String p0, final IRESTResponseListener p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aconst_null    
        //     1: astore          24
        //     3: aconst_null    
        //     4: astore          25
        //     6: aconst_null    
        //     7: astore          30
        //     9: aconst_null    
        //    10: astore          28
        //    12: sipush          503
        //    15: istore          4
        //    17: lconst_0       
        //    18: lstore          11
        //    20: lconst_0       
        //    21: lstore          9
        //    23: iconst_0       
        //    24: istore          21
        //    26: iconst_0       
        //    27: istore          22
        //    29: iconst_0       
        //    30: istore          20
        //    32: aconst_null    
        //    33: astore          31
        //    35: aconst_null    
        //    36: astore          29
        //    38: lload           11
        //    40: lstore          13
        //    42: lload           9
        //    44: lstore          15
        //    46: iload           4
        //    48: istore_3       
        //    49: lload           11
        //    51: lstore          5
        //    53: lload           9
        //    55: lstore          7
        //    57: iload           21
        //    59: istore          19
        //    61: aload           31
        //    63: astore          26
        //    65: new             Ljava/net/URL;
        //    68: dup            
        //    69: aload_1        
        //    70: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
        //    73: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
        //    76: checkcast       Ljava/net/HttpURLConnection;
        //    79: astore          23
        //    81: lload           11
        //    83: lstore          13
        //    85: lload           9
        //    87: lstore          15
        //    89: iload           4
        //    91: istore_3       
        //    92: aload           23
        //    94: astore          25
        //    96: lload           11
        //    98: lstore          5
        //   100: lload           9
        //   102: lstore          7
        //   104: iload           21
        //   106: istore          19
        //   108: aload           23
        //   110: astore          24
        //   112: aload           31
        //   114: astore          26
        //   116: aload           23
        //   118: sipush          15000
        //   121: invokevirtual   java/net/HttpURLConnection.setReadTimeout:(I)V
        //   124: lload           11
        //   126: lstore          13
        //   128: lload           9
        //   130: lstore          15
        //   132: iload           4
        //   134: istore_3       
        //   135: aload           23
        //   137: astore          25
        //   139: lload           11
        //   141: lstore          5
        //   143: lload           9
        //   145: lstore          7
        //   147: iload           21
        //   149: istore          19
        //   151: aload           23
        //   153: astore          24
        //   155: aload           31
        //   157: astore          26
        //   159: aload           23
        //   161: sipush          15000
        //   164: invokevirtual   java/net/HttpURLConnection.setConnectTimeout:(I)V
        //   167: lload           11
        //   169: lstore          13
        //   171: lload           9
        //   173: lstore          15
        //   175: iload           4
        //   177: istore_3       
        //   178: aload           23
        //   180: astore          25
        //   182: lload           11
        //   184: lstore          5
        //   186: lload           9
        //   188: lstore          7
        //   190: iload           21
        //   192: istore          19
        //   194: aload           23
        //   196: astore          24
        //   198: aload           31
        //   200: astore          26
        //   202: aload           23
        //   204: ldc             "GET"
        //   206: invokevirtual   java/net/HttpURLConnection.setRequestMethod:(Ljava/lang/String;)V
        //   209: lload           11
        //   211: lstore          13
        //   213: lload           9
        //   215: lstore          15
        //   217: iload           4
        //   219: istore_3       
        //   220: aload           23
        //   222: astore          25
        //   224: lload           11
        //   226: lstore          5
        //   228: lload           9
        //   230: lstore          7
        //   232: iload           21
        //   234: istore          19
        //   236: aload           23
        //   238: astore          24
        //   240: aload           31
        //   242: astore          26
        //   244: aload           23
        //   246: ldc             "Accept-Charset"
        //   248: ldc             "UTF-8"
        //   250: invokevirtual   java/net/HttpURLConnection.setRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   253: lload           11
        //   255: lstore          13
        //   257: lload           9
        //   259: lstore          15
        //   261: iload           4
        //   263: istore_3       
        //   264: aload           23
        //   266: astore          25
        //   268: lload           11
        //   270: lstore          5
        //   272: lload           9
        //   274: lstore          7
        //   276: iload           21
        //   278: istore          19
        //   280: aload           23
        //   282: astore          24
        //   284: aload           31
        //   286: astore          26
        //   288: aload           23
        //   290: ldc             "Connection"
        //   292: ldc             "close"
        //   294: invokevirtual   java/net/HttpURLConnection.setRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   297: lload           11
        //   299: lstore          13
        //   301: lload           9
        //   303: lstore          15
        //   305: iload           4
        //   307: istore_3       
        //   308: aload           23
        //   310: astore          25
        //   312: lload           11
        //   314: lstore          5
        //   316: lload           9
        //   318: lstore          7
        //   320: iload           21
        //   322: istore          19
        //   324: aload           23
        //   326: astore          24
        //   328: aload           31
        //   330: astore          26
        //   332: aload           23
        //   334: ldc             "Accept-Encoding"
        //   336: ldc             "gzip"
        //   338: invokevirtual   java/net/HttpURLConnection.setRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   341: lload           11
        //   343: lstore          13
        //   345: lload           9
        //   347: lstore          15
        //   349: iload           4
        //   351: istore_3       
        //   352: aload           23
        //   354: astore          25
        //   356: lload           11
        //   358: lstore          5
        //   360: lload           9
        //   362: lstore          7
        //   364: iload           21
        //   366: istore          19
        //   368: aload           23
        //   370: astore          24
        //   372: aload           31
        //   374: astore          26
        //   376: aload_0        
        //   377: aload           23
        //   379: invokespecial   com/swrve/sdk/rest/RESTClient.addMetricsHeader:(Ljava/net/HttpURLConnection;)Ljava/net/HttpURLConnection;
        //   382: astore          23
        //   384: lload           11
        //   386: lstore          13
        //   388: lload           9
        //   390: lstore          15
        //   392: iload           4
        //   394: istore_3       
        //   395: aload           23
        //   397: astore          25
        //   399: lload           11
        //   401: lstore          5
        //   403: lload           9
        //   405: lstore          7
        //   407: iload           21
        //   409: istore          19
        //   411: aload           23
        //   413: astore          24
        //   415: aload           31
        //   417: astore          26
        //   419: invokestatic    java/lang/System.nanoTime:()J
        //   422: lstore          17
        //   424: lload           11
        //   426: lstore          13
        //   428: lload           9
        //   430: lstore          15
        //   432: iload           4
        //   434: istore_3       
        //   435: aload           23
        //   437: astore          25
        //   439: lload           11
        //   441: lstore          5
        //   443: lload           9
        //   445: lstore          7
        //   447: iload           21
        //   449: istore          19
        //   451: aload           23
        //   453: astore          24
        //   455: aload           31
        //   457: astore          26
        //   459: aload           23
        //   461: invokevirtual   java/net/HttpURLConnection.connect:()V
        //   464: lload           11
        //   466: lstore          13
        //   468: lload           9
        //   470: lstore          15
        //   472: iload           4
        //   474: istore_3       
        //   475: aload           23
        //   477: astore          25
        //   479: lload           11
        //   481: lstore          5
        //   483: lload           9
        //   485: lstore          7
        //   487: iload           21
        //   489: istore          19
        //   491: aload           23
        //   493: astore          24
        //   495: aload           31
        //   497: astore          26
        //   499: aload_0        
        //   500: lload           17
        //   502: invokespecial   com/swrve/sdk/rest/RESTClient.milisecondsFrom:(J)J
        //   505: lstore          11
        //   507: lload           11
        //   509: lstore          13
        //   511: lload           9
        //   513: lstore          15
        //   515: iload           4
        //   517: istore_3       
        //   518: aload           23
        //   520: astore          25
        //   522: lload           11
        //   524: lstore          5
        //   526: lload           9
        //   528: lstore          7
        //   530: iload           21
        //   532: istore          19
        //   534: aload           23
        //   536: astore          24
        //   538: aload           31
        //   540: astore          26
        //   542: aload           23
        //   544: invokevirtual   java/net/HttpURLConnection.getResponseCode:()I
        //   547: istore          4
        //   549: lload           11
        //   551: lstore          13
        //   553: lload           9
        //   555: lstore          15
        //   557: iload           4
        //   559: istore_3       
        //   560: aload           23
        //   562: astore          25
        //   564: lload           11
        //   566: lstore          5
        //   568: lload           9
        //   570: lstore          7
        //   572: iload           21
        //   574: istore          19
        //   576: aload           23
        //   578: astore          24
        //   580: aload           31
        //   582: astore          26
        //   584: aload_0        
        //   585: lload           17
        //   587: invokespecial   com/swrve/sdk/rest/RESTClient.milisecondsFrom:(J)J
        //   590: lstore          9
        //   592: lload           11
        //   594: lstore          13
        //   596: lload           9
        //   598: lstore          15
        //   600: iload           4
        //   602: istore_3       
        //   603: aload           23
        //   605: astore          25
        //   607: lload           11
        //   609: lstore          5
        //   611: lload           9
        //   613: lstore          7
        //   615: iload           21
        //   617: istore          19
        //   619: aload           23
        //   621: astore          24
        //   623: aload           31
        //   625: astore          26
        //   627: aload           23
        //   629: invokevirtual   java/net/HttpURLConnection.getErrorStream:()Ljava/io/InputStream;
        //   632: astore          27
        //   634: aload           27
        //   636: ifnonnull       1002
        //   639: lload           11
        //   641: lstore          13
        //   643: lload           9
        //   645: lstore          15
        //   647: iload           4
        //   649: istore_3       
        //   650: aload           23
        //   652: astore          25
        //   654: lload           11
        //   656: lstore          5
        //   658: lload           9
        //   660: lstore          7
        //   662: iload           21
        //   664: istore          19
        //   666: aload           23
        //   668: astore          24
        //   670: aload           31
        //   672: astore          26
        //   674: aload           23
        //   676: invokevirtual   java/net/HttpURLConnection.getInputStream:()Ljava/io/InputStream;
        //   679: astore          27
        //   681: lload           11
        //   683: lstore          13
        //   685: lload           9
        //   687: lstore          15
        //   689: iload           4
        //   691: istore_3       
        //   692: aload           23
        //   694: astore          25
        //   696: lload           11
        //   698: lstore          5
        //   700: lload           9
        //   702: lstore          7
        //   704: iload           21
        //   706: istore          19
        //   708: aload           23
        //   710: astore          24
        //   712: aload           31
        //   714: astore          26
        //   716: aload           23
        //   718: invokevirtual   java/net/HttpURLConnection.getContentEncoding:()Ljava/lang/String;
        //   721: astore          32
        //   723: aload           32
        //   725: ifnull          953
        //   728: lload           11
        //   730: lstore          13
        //   732: lload           9
        //   734: lstore          15
        //   736: iload           4
        //   738: istore_3       
        //   739: aload           23
        //   741: astore          25
        //   743: lload           11
        //   745: lstore          5
        //   747: lload           9
        //   749: lstore          7
        //   751: iload           21
        //   753: istore          19
        //   755: aload           23
        //   757: astore          24
        //   759: aload           31
        //   761: astore          26
        //   763: aload           32
        //   765: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //   768: ldc             "gzip"
        //   770: invokevirtual   java/lang/String.indexOf:(Ljava/lang/String;)I
        //   773: iconst_m1      
        //   774: if_icmpeq       953
        //   777: lload           11
        //   779: lstore          13
        //   781: lload           9
        //   783: lstore          15
        //   785: iload           4
        //   787: istore_3       
        //   788: aload           23
        //   790: astore          25
        //   792: lload           11
        //   794: lstore          5
        //   796: lload           9
        //   798: lstore          7
        //   800: iload           21
        //   802: istore          19
        //   804: aload           23
        //   806: astore          24
        //   808: aload           31
        //   810: astore          26
        //   812: new             Ljava/util/zip/GZIPInputStream;
        //   815: dup            
        //   816: aload           27
        //   818: invokespecial   java/util/zip/GZIPInputStream.<init>:(Ljava/io/InputStream;)V
        //   821: astore          27
        //   823: lload           11
        //   825: lstore          13
        //   827: lload           9
        //   829: lstore          15
        //   831: iload           4
        //   833: istore_3       
        //   834: aload           23
        //   836: astore          25
        //   838: lload           11
        //   840: lstore          5
        //   842: lload           9
        //   844: lstore          7
        //   846: iload           21
        //   848: istore          19
        //   850: aload           23
        //   852: astore          24
        //   854: aload           31
        //   856: astore          26
        //   858: new             Lcom/swrve/sdk/rest/SwrveFilterInputStream;
        //   861: dup            
        //   862: aload           27
        //   864: invokespecial   com/swrve/sdk/rest/SwrveFilterInputStream.<init>:(Ljava/io/InputStream;)V
        //   867: astore          27
        //   869: aload           30
        //   871: astore          25
        //   873: aload           27
        //   875: invokestatic    com/swrve/sdk/SwrveHelper.readStringFromInputStream:(Ljava/io/InputStream;)Ljava/lang/String;
        //   878: astore          24
        //   880: aload           24
        //   882: astore          25
        //   884: aload_0        
        //   885: lload           17
        //   887: invokespecial   com/swrve/sdk/rest/RESTClient.milisecondsFrom:(J)J
        //   890: lstore          5
        //   892: aload           23
        //   894: ifnull          902
        //   897: aload           23
        //   899: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   902: aload           27
        //   904: ifnull          912
        //   907: aload           27
        //   909: invokevirtual   java/io/InputStream.close:()V
        //   912: aload_0        
        //   913: aload_1        
        //   914: lload           11
        //   916: lload           9
        //   918: lload           5
        //   920: iconst_0       
        //   921: invokespecial   com/swrve/sdk/rest/RESTClient.recordGetMetrics:(Ljava/lang/String;JJJZ)V
        //   924: iload           4
        //   926: istore_3       
        //   927: aload_2        
        //   928: ifnull          952
        //   931: aload_2        
        //   932: new             Lcom/swrve/sdk/rest/RESTResponse;
        //   935: dup            
        //   936: iload_3        
        //   937: aload           24
        //   939: aload           23
        //   941: invokevirtual   java/net/HttpURLConnection.getHeaderFields:()Ljava/util/Map;
        //   944: invokespecial   com/swrve/sdk/rest/RESTResponse.<init>:(ILjava/lang/String;Ljava/util/Map;)V
        //   947: invokeinterface com/swrve/sdk/rest/IRESTResponseListener.onResponse:(Lcom/swrve/sdk/rest/RESTResponse;)V
        //   952: return         
        //   953: lload           11
        //   955: lstore          13
        //   957: lload           9
        //   959: lstore          15
        //   961: iload           4
        //   963: istore_3       
        //   964: aload           23
        //   966: astore          25
        //   968: lload           11
        //   970: lstore          5
        //   972: lload           9
        //   974: lstore          7
        //   976: iload           21
        //   978: istore          19
        //   980: aload           23
        //   982: astore          24
        //   984: aload           31
        //   986: astore          26
        //   988: new             Ljava/io/BufferedInputStream;
        //   991: dup            
        //   992: aload           27
        //   994: invokespecial   java/io/BufferedInputStream.<init>:(Ljava/io/InputStream;)V
        //   997: astore          27
        //   999: goto            823
        //  1002: lload           11
        //  1004: lstore          13
        //  1006: lload           9
        //  1008: lstore          15
        //  1010: iload           4
        //  1012: istore_3       
        //  1013: aload           23
        //  1015: astore          25
        //  1017: lload           11
        //  1019: lstore          5
        //  1021: lload           9
        //  1023: lstore          7
        //  1025: iload           21
        //  1027: istore          19
        //  1029: aload           23
        //  1031: astore          24
        //  1033: aload           31
        //  1035: astore          26
        //  1037: new             Ljava/io/BufferedInputStream;
        //  1040: dup            
        //  1041: aload           27
        //  1043: invokespecial   java/io/BufferedInputStream.<init>:(Ljava/io/InputStream;)V
        //  1046: astore          27
        //  1048: goto            823
        //  1051: astore          25
        //  1053: aload           25
        //  1055: invokevirtual   java/io/IOException.printStackTrace:()V
        //  1058: goto            912
        //  1061: astore          24
        //  1063: aload           29
        //  1065: astore          27
        //  1067: aload           25
        //  1069: astore          23
        //  1071: aload           28
        //  1073: astore          25
        //  1075: aload           24
        //  1077: astore          28
        //  1079: lload           15
        //  1081: lstore          9
        //  1083: lload           13
        //  1085: lstore          11
        //  1087: lload           11
        //  1089: lstore          5
        //  1091: lload           9
        //  1093: lstore          7
        //  1095: iload           21
        //  1097: istore          19
        //  1099: aload           23
        //  1101: astore          24
        //  1103: aload           27
        //  1105: astore          26
        //  1107: aload           28
        //  1109: invokevirtual   java/lang/Exception.printStackTrace:()V
        //  1112: lload           11
        //  1114: lstore          5
        //  1116: lload           9
        //  1118: lstore          7
        //  1120: iload           21
        //  1122: istore          19
        //  1124: aload           23
        //  1126: astore          24
        //  1128: aload           27
        //  1130: astore          26
        //  1132: aload           28
        //  1134: instanceof      Ljava/net/SocketTimeoutException;
        //  1137: ifeq            1143
        //  1140: iconst_1       
        //  1141: istore          20
        //  1143: aload_2        
        //  1144: ifnull          1175
        //  1147: lload           11
        //  1149: lstore          5
        //  1151: lload           9
        //  1153: lstore          7
        //  1155: iload           20
        //  1157: istore          19
        //  1159: aload           23
        //  1161: astore          24
        //  1163: aload           27
        //  1165: astore          26
        //  1167: aload_2        
        //  1168: aload           28
        //  1170: invokeinterface com/swrve/sdk/rest/IRESTResponseListener.onException:(Ljava/lang/Exception;)V
        //  1175: aload           23
        //  1177: ifnull          1185
        //  1180: aload           23
        //  1182: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //  1185: aload           27
        //  1187: ifnull          1195
        //  1190: aload           27
        //  1192: invokevirtual   java/io/InputStream.close:()V
        //  1195: aload_0        
        //  1196: aload_1        
        //  1197: lload           11
        //  1199: lload           9
        //  1201: lconst_0       
        //  1202: iload           20
        //  1204: invokespecial   com/swrve/sdk/rest/RESTClient.recordGetMetrics:(Ljava/lang/String;JJJZ)V
        //  1207: aload           25
        //  1209: astore          24
        //  1211: goto            927
        //  1214: astore          24
        //  1216: aload           24
        //  1218: invokevirtual   java/io/IOException.printStackTrace:()V
        //  1221: goto            1195
        //  1224: astore_2       
        //  1225: aload           24
        //  1227: astore          23
        //  1229: aload           23
        //  1231: ifnull          1239
        //  1234: aload           23
        //  1236: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //  1239: aload           26
        //  1241: ifnull          1249
        //  1244: aload           26
        //  1246: invokevirtual   java/io/InputStream.close:()V
        //  1249: aload_0        
        //  1250: aload_1        
        //  1251: lload           5
        //  1253: lload           7
        //  1255: lconst_0       
        //  1256: iload           19
        //  1258: invokespecial   com/swrve/sdk/rest/RESTClient.recordGetMetrics:(Ljava/lang/String;JJJZ)V
        //  1261: aload_2        
        //  1262: athrow         
        //  1263: astore          23
        //  1265: aload           23
        //  1267: invokevirtual   java/io/IOException.printStackTrace:()V
        //  1270: goto            1249
        //  1273: astore_2       
        //  1274: aload           27
        //  1276: astore          26
        //  1278: lload           11
        //  1280: lstore          5
        //  1282: lload           9
        //  1284: lstore          7
        //  1286: iload           22
        //  1288: istore          19
        //  1290: goto            1229
        //  1293: astore          28
        //  1295: iload           4
        //  1297: istore_3       
        //  1298: goto            1087
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  65     81     1061   1087   Ljava/lang/Exception;
        //  65     81     1224   1229   Any
        //  116    124    1061   1087   Ljava/lang/Exception;
        //  116    124    1224   1229   Any
        //  159    167    1061   1087   Ljava/lang/Exception;
        //  159    167    1224   1229   Any
        //  202    209    1061   1087   Ljava/lang/Exception;
        //  202    209    1224   1229   Any
        //  244    253    1061   1087   Ljava/lang/Exception;
        //  244    253    1224   1229   Any
        //  288    297    1061   1087   Ljava/lang/Exception;
        //  288    297    1224   1229   Any
        //  332    341    1061   1087   Ljava/lang/Exception;
        //  332    341    1224   1229   Any
        //  376    384    1061   1087   Ljava/lang/Exception;
        //  376    384    1224   1229   Any
        //  419    424    1061   1087   Ljava/lang/Exception;
        //  419    424    1224   1229   Any
        //  459    464    1061   1087   Ljava/lang/Exception;
        //  459    464    1224   1229   Any
        //  499    507    1061   1087   Ljava/lang/Exception;
        //  499    507    1224   1229   Any
        //  542    549    1061   1087   Ljava/lang/Exception;
        //  542    549    1224   1229   Any
        //  584    592    1061   1087   Ljava/lang/Exception;
        //  584    592    1224   1229   Any
        //  627    634    1061   1087   Ljava/lang/Exception;
        //  627    634    1224   1229   Any
        //  674    681    1061   1087   Ljava/lang/Exception;
        //  674    681    1224   1229   Any
        //  716    723    1061   1087   Ljava/lang/Exception;
        //  716    723    1224   1229   Any
        //  763    777    1061   1087   Ljava/lang/Exception;
        //  763    777    1224   1229   Any
        //  812    823    1061   1087   Ljava/lang/Exception;
        //  812    823    1224   1229   Any
        //  858    869    1061   1087   Ljava/lang/Exception;
        //  858    869    1224   1229   Any
        //  873    880    1293   1301   Ljava/lang/Exception;
        //  873    880    1273   1293   Any
        //  884    892    1293   1301   Ljava/lang/Exception;
        //  884    892    1273   1293   Any
        //  907    912    1051   1061   Ljava/io/IOException;
        //  988    999    1061   1087   Ljava/lang/Exception;
        //  988    999    1224   1229   Any
        //  1037   1048   1061   1087   Ljava/lang/Exception;
        //  1037   1048   1224   1229   Any
        //  1107   1112   1224   1229   Any
        //  1132   1140   1224   1229   Any
        //  1167   1175   1224   1229   Any
        //  1190   1195   1214   1224   Ljava/io/IOException;
        //  1244   1249   1263   1273   Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        //     at java.util.ArrayList.rangeCheck(ArrayList.java:653)
        //     at java.util.ArrayList.get(ArrayList.java:429)
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:3035)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
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
    
    @Override
    public void get(final String s, final Map<String, String> map, final IRESTResponseListener irestResponseListener) throws UnsupportedEncodingException {
        this.get(s + "?" + SwrveHelper.encodeParameters(map), irestResponseListener);
    }
    
    @Override
    public void post(final String s, final String s2, final IRESTResponseListener irestResponseListener) {
        this.post(s, s2, irestResponseListener, "application/json");
    }
    
    public void post(final String p0, final String p1, final IRESTResponseListener p2, final String p3) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aconst_null    
        //     1: astore          39
        //     3: aconst_null    
        //     4: astore          38
        //     6: aconst_null    
        //     7: astore          37
        //     9: aconst_null    
        //    10: astore          35
        //    12: sipush          503
        //    15: istore          6
        //    17: lconst_0       
        //    18: lstore          17
        //    20: lconst_0       
        //    21: lstore          15
        //    23: lconst_0       
        //    24: lstore          13
        //    26: iconst_0       
        //    27: istore          29
        //    29: iconst_0       
        //    30: istore          30
        //    32: iconst_0       
        //    33: istore          28
        //    35: aconst_null    
        //    36: astore          34
        //    38: aconst_null    
        //    39: astore          36
        //    41: lload           17
        //    43: lstore          19
        //    45: lload           15
        //    47: lstore          21
        //    49: lload           13
        //    51: lstore          23
        //    53: iload           6
        //    55: istore          5
        //    57: aload           38
        //    59: astore          32
        //    61: lload           17
        //    63: lstore          7
        //    65: lload           15
        //    67: lstore          9
        //    69: lload           13
        //    71: lstore          11
        //    73: iload           29
        //    75: istore          27
        //    77: aload           39
        //    79: astore          31
        //    81: aload           34
        //    83: astore          33
        //    85: aload_2        
        //    86: ldc             "UTF-8"
        //    88: invokevirtual   java/lang/String.getBytes:(Ljava/lang/String;)[B
        //    91: astore          40
        //    93: lload           17
        //    95: lstore          19
        //    97: lload           15
        //    99: lstore          21
        //   101: lload           13
        //   103: lstore          23
        //   105: iload           6
        //   107: istore          5
        //   109: aload           38
        //   111: astore          32
        //   113: lload           17
        //   115: lstore          7
        //   117: lload           15
        //   119: lstore          9
        //   121: lload           13
        //   123: lstore          11
        //   125: iload           29
        //   127: istore          27
        //   129: aload           39
        //   131: astore          31
        //   133: aload           34
        //   135: astore          33
        //   137: new             Ljava/net/URL;
        //   140: dup            
        //   141: aload_1        
        //   142: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
        //   145: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
        //   148: checkcast       Ljava/net/HttpURLConnection;
        //   151: astore_2       
        //   152: lload           17
        //   154: lstore          19
        //   156: lload           15
        //   158: lstore          21
        //   160: lload           13
        //   162: lstore          23
        //   164: iload           6
        //   166: istore          5
        //   168: aload_2        
        //   169: astore          32
        //   171: lload           17
        //   173: lstore          7
        //   175: lload           15
        //   177: lstore          9
        //   179: lload           13
        //   181: lstore          11
        //   183: iload           29
        //   185: istore          27
        //   187: aload_2        
        //   188: astore          31
        //   190: aload           34
        //   192: astore          33
        //   194: aload_2        
        //   195: sipush          15000
        //   198: invokevirtual   java/net/HttpURLConnection.setReadTimeout:(I)V
        //   201: lload           17
        //   203: lstore          19
        //   205: lload           15
        //   207: lstore          21
        //   209: lload           13
        //   211: lstore          23
        //   213: iload           6
        //   215: istore          5
        //   217: aload_2        
        //   218: astore          32
        //   220: lload           17
        //   222: lstore          7
        //   224: lload           15
        //   226: lstore          9
        //   228: lload           13
        //   230: lstore          11
        //   232: iload           29
        //   234: istore          27
        //   236: aload_2        
        //   237: astore          31
        //   239: aload           34
        //   241: astore          33
        //   243: aload_2        
        //   244: sipush          15000
        //   247: invokevirtual   java/net/HttpURLConnection.setConnectTimeout:(I)V
        //   250: lload           17
        //   252: lstore          19
        //   254: lload           15
        //   256: lstore          21
        //   258: lload           13
        //   260: lstore          23
        //   262: iload           6
        //   264: istore          5
        //   266: aload_2        
        //   267: astore          32
        //   269: lload           17
        //   271: lstore          7
        //   273: lload           15
        //   275: lstore          9
        //   277: lload           13
        //   279: lstore          11
        //   281: iload           29
        //   283: istore          27
        //   285: aload_2        
        //   286: astore          31
        //   288: aload           34
        //   290: astore          33
        //   292: aload_2        
        //   293: ldc_w           "POST"
        //   296: invokevirtual   java/net/HttpURLConnection.setRequestMethod:(Ljava/lang/String;)V
        //   299: lload           17
        //   301: lstore          19
        //   303: lload           15
        //   305: lstore          21
        //   307: lload           13
        //   309: lstore          23
        //   311: iload           6
        //   313: istore          5
        //   315: aload_2        
        //   316: astore          32
        //   318: lload           17
        //   320: lstore          7
        //   322: lload           15
        //   324: lstore          9
        //   326: lload           13
        //   328: lstore          11
        //   330: iload           29
        //   332: istore          27
        //   334: aload_2        
        //   335: astore          31
        //   337: aload           34
        //   339: astore          33
        //   341: aload_2        
        //   342: ldc_w           "Content-Type"
        //   345: aload           4
        //   347: invokevirtual   java/net/HttpURLConnection.setRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   350: lload           17
        //   352: lstore          19
        //   354: lload           15
        //   356: lstore          21
        //   358: lload           13
        //   360: lstore          23
        //   362: iload           6
        //   364: istore          5
        //   366: aload_2        
        //   367: astore          32
        //   369: lload           17
        //   371: lstore          7
        //   373: lload           15
        //   375: lstore          9
        //   377: lload           13
        //   379: lstore          11
        //   381: iload           29
        //   383: istore          27
        //   385: aload_2        
        //   386: astore          31
        //   388: aload           34
        //   390: astore          33
        //   392: aload_2        
        //   393: ldc             "Accept-Charset"
        //   395: ldc             "UTF-8"
        //   397: invokevirtual   java/net/HttpURLConnection.setRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   400: lload           17
        //   402: lstore          19
        //   404: lload           15
        //   406: lstore          21
        //   408: lload           13
        //   410: lstore          23
        //   412: iload           6
        //   414: istore          5
        //   416: aload_2        
        //   417: astore          32
        //   419: lload           17
        //   421: lstore          7
        //   423: lload           15
        //   425: lstore          9
        //   427: lload           13
        //   429: lstore          11
        //   431: iload           29
        //   433: istore          27
        //   435: aload_2        
        //   436: astore          31
        //   438: aload           34
        //   440: astore          33
        //   442: aload_2        
        //   443: ldc             "Connection"
        //   445: ldc             "close"
        //   447: invokevirtual   java/net/HttpURLConnection.setRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   450: lload           17
        //   452: lstore          19
        //   454: lload           15
        //   456: lstore          21
        //   458: lload           13
        //   460: lstore          23
        //   462: iload           6
        //   464: istore          5
        //   466: aload_2        
        //   467: astore          32
        //   469: lload           17
        //   471: lstore          7
        //   473: lload           15
        //   475: lstore          9
        //   477: lload           13
        //   479: lstore          11
        //   481: iload           29
        //   483: istore          27
        //   485: aload_2        
        //   486: astore          31
        //   488: aload           34
        //   490: astore          33
        //   492: aload_2        
        //   493: aload           40
        //   495: arraylength    
        //   496: invokevirtual   java/net/HttpURLConnection.setFixedLengthStreamingMode:(I)V
        //   499: lload           17
        //   501: lstore          19
        //   503: lload           15
        //   505: lstore          21
        //   507: lload           13
        //   509: lstore          23
        //   511: iload           6
        //   513: istore          5
        //   515: aload_2        
        //   516: astore          32
        //   518: lload           17
        //   520: lstore          7
        //   522: lload           15
        //   524: lstore          9
        //   526: lload           13
        //   528: lstore          11
        //   530: iload           29
        //   532: istore          27
        //   534: aload_2        
        //   535: astore          31
        //   537: aload           34
        //   539: astore          33
        //   541: aload_2        
        //   542: iconst_1       
        //   543: invokevirtual   java/net/HttpURLConnection.setDoOutput:(Z)V
        //   546: lload           17
        //   548: lstore          19
        //   550: lload           15
        //   552: lstore          21
        //   554: lload           13
        //   556: lstore          23
        //   558: iload           6
        //   560: istore          5
        //   562: aload_2        
        //   563: astore          32
        //   565: lload           17
        //   567: lstore          7
        //   569: lload           15
        //   571: lstore          9
        //   573: lload           13
        //   575: lstore          11
        //   577: iload           29
        //   579: istore          27
        //   581: aload_2        
        //   582: astore          31
        //   584: aload           34
        //   586: astore          33
        //   588: aload_2        
        //   589: iconst_1       
        //   590: invokevirtual   java/net/HttpURLConnection.setDoInput:(Z)V
        //   593: lload           17
        //   595: lstore          19
        //   597: lload           15
        //   599: lstore          21
        //   601: lload           13
        //   603: lstore          23
        //   605: iload           6
        //   607: istore          5
        //   609: aload_2        
        //   610: astore          32
        //   612: lload           17
        //   614: lstore          7
        //   616: lload           15
        //   618: lstore          9
        //   620: lload           13
        //   622: lstore          11
        //   624: iload           29
        //   626: istore          27
        //   628: aload_2        
        //   629: astore          31
        //   631: aload           34
        //   633: astore          33
        //   635: aload_2        
        //   636: iconst_0       
        //   637: invokevirtual   java/net/HttpURLConnection.setUseCaches:(Z)V
        //   640: lload           17
        //   642: lstore          19
        //   644: lload           15
        //   646: lstore          21
        //   648: lload           13
        //   650: lstore          23
        //   652: iload           6
        //   654: istore          5
        //   656: aload_2        
        //   657: astore          32
        //   659: lload           17
        //   661: lstore          7
        //   663: lload           15
        //   665: lstore          9
        //   667: lload           13
        //   669: lstore          11
        //   671: iload           29
        //   673: istore          27
        //   675: aload_2        
        //   676: astore          31
        //   678: aload           34
        //   680: astore          33
        //   682: aload_0        
        //   683: aload_2        
        //   684: invokespecial   com/swrve/sdk/rest/RESTClient.addMetricsHeader:(Ljava/net/HttpURLConnection;)Ljava/net/HttpURLConnection;
        //   687: astore_2       
        //   688: lload           17
        //   690: lstore          19
        //   692: lload           15
        //   694: lstore          21
        //   696: lload           13
        //   698: lstore          23
        //   700: iload           6
        //   702: istore          5
        //   704: aload_2        
        //   705: astore          32
        //   707: lload           17
        //   709: lstore          7
        //   711: lload           15
        //   713: lstore          9
        //   715: lload           13
        //   717: lstore          11
        //   719: iload           29
        //   721: istore          27
        //   723: aload_2        
        //   724: astore          31
        //   726: aload           34
        //   728: astore          33
        //   730: invokestatic    java/lang/System.nanoTime:()J
        //   733: lstore          25
        //   735: lload           17
        //   737: lstore          19
        //   739: lload           15
        //   741: lstore          21
        //   743: lload           13
        //   745: lstore          23
        //   747: iload           6
        //   749: istore          5
        //   751: aload_2        
        //   752: astore          32
        //   754: lload           17
        //   756: lstore          7
        //   758: lload           15
        //   760: lstore          9
        //   762: lload           13
        //   764: lstore          11
        //   766: iload           29
        //   768: istore          27
        //   770: aload_2        
        //   771: astore          31
        //   773: aload           34
        //   775: astore          33
        //   777: aload_2        
        //   778: invokevirtual   java/net/HttpURLConnection.connect:()V
        //   781: lload           17
        //   783: lstore          19
        //   785: lload           15
        //   787: lstore          21
        //   789: lload           13
        //   791: lstore          23
        //   793: iload           6
        //   795: istore          5
        //   797: aload_2        
        //   798: astore          32
        //   800: lload           17
        //   802: lstore          7
        //   804: lload           15
        //   806: lstore          9
        //   808: lload           13
        //   810: lstore          11
        //   812: iload           29
        //   814: istore          27
        //   816: aload_2        
        //   817: astore          31
        //   819: aload           34
        //   821: astore          33
        //   823: aload_0        
        //   824: lload           25
        //   826: invokespecial   com/swrve/sdk/rest/RESTClient.milisecondsFrom:(J)J
        //   829: lstore          17
        //   831: lload           17
        //   833: lstore          19
        //   835: lload           15
        //   837: lstore          21
        //   839: lload           13
        //   841: lstore          23
        //   843: iload           6
        //   845: istore          5
        //   847: aload_2        
        //   848: astore          32
        //   850: lload           17
        //   852: lstore          7
        //   854: lload           15
        //   856: lstore          9
        //   858: lload           13
        //   860: lstore          11
        //   862: iload           29
        //   864: istore          27
        //   866: aload_2        
        //   867: astore          31
        //   869: aload           34
        //   871: astore          33
        //   873: aload_2        
        //   874: invokevirtual   java/net/HttpURLConnection.getOutputStream:()Ljava/io/OutputStream;
        //   877: astore          4
        //   879: lload           17
        //   881: lstore          19
        //   883: lload           15
        //   885: lstore          21
        //   887: lload           13
        //   889: lstore          23
        //   891: iload           6
        //   893: istore          5
        //   895: aload_2        
        //   896: astore          32
        //   898: lload           17
        //   900: lstore          7
        //   902: lload           15
        //   904: lstore          9
        //   906: lload           13
        //   908: lstore          11
        //   910: iload           29
        //   912: istore          27
        //   914: aload_2        
        //   915: astore          31
        //   917: aload           34
        //   919: astore          33
        //   921: aload           4
        //   923: aload           40
        //   925: invokevirtual   java/io/OutputStream.write:([B)V
        //   928: lload           17
        //   930: lstore          19
        //   932: lload           15
        //   934: lstore          21
        //   936: lload           13
        //   938: lstore          23
        //   940: iload           6
        //   942: istore          5
        //   944: aload_2        
        //   945: astore          32
        //   947: lload           17
        //   949: lstore          7
        //   951: lload           15
        //   953: lstore          9
        //   955: lload           13
        //   957: lstore          11
        //   959: iload           29
        //   961: istore          27
        //   963: aload_2        
        //   964: astore          31
        //   966: aload           34
        //   968: astore          33
        //   970: aload           4
        //   972: invokevirtual   java/io/OutputStream.close:()V
        //   975: lload           17
        //   977: lstore          19
        //   979: lload           15
        //   981: lstore          21
        //   983: lload           13
        //   985: lstore          23
        //   987: iload           6
        //   989: istore          5
        //   991: aload_2        
        //   992: astore          32
        //   994: lload           17
        //   996: lstore          7
        //   998: lload           15
        //  1000: lstore          9
        //  1002: lload           13
        //  1004: lstore          11
        //  1006: iload           29
        //  1008: istore          27
        //  1010: aload_2        
        //  1011: astore          31
        //  1013: aload           34
        //  1015: astore          33
        //  1017: aload_0        
        //  1018: lload           25
        //  1020: invokespecial   com/swrve/sdk/rest/RESTClient.milisecondsFrom:(J)J
        //  1023: lstore          15
        //  1025: lload           17
        //  1027: lstore          19
        //  1029: lload           15
        //  1031: lstore          21
        //  1033: lload           13
        //  1035: lstore          23
        //  1037: iload           6
        //  1039: istore          5
        //  1041: aload_2        
        //  1042: astore          32
        //  1044: lload           17
        //  1046: lstore          7
        //  1048: lload           15
        //  1050: lstore          9
        //  1052: lload           13
        //  1054: lstore          11
        //  1056: iload           29
        //  1058: istore          27
        //  1060: aload_2        
        //  1061: astore          31
        //  1063: aload           34
        //  1065: astore          33
        //  1067: aload_2        
        //  1068: invokevirtual   java/net/HttpURLConnection.getResponseCode:()I
        //  1071: istore          6
        //  1073: lload           17
        //  1075: lstore          19
        //  1077: lload           15
        //  1079: lstore          21
        //  1081: lload           13
        //  1083: lstore          23
        //  1085: iload           6
        //  1087: istore          5
        //  1089: aload_2        
        //  1090: astore          32
        //  1092: lload           17
        //  1094: lstore          7
        //  1096: lload           15
        //  1098: lstore          9
        //  1100: lload           13
        //  1102: lstore          11
        //  1104: iload           29
        //  1106: istore          27
        //  1108: aload_2        
        //  1109: astore          31
        //  1111: aload           34
        //  1113: astore          33
        //  1115: aload_0        
        //  1116: lload           25
        //  1118: invokespecial   com/swrve/sdk/rest/RESTClient.milisecondsFrom:(J)J
        //  1121: lstore          13
        //  1123: lload           17
        //  1125: lstore          19
        //  1127: lload           15
        //  1129: lstore          21
        //  1131: lload           13
        //  1133: lstore          23
        //  1135: iload           6
        //  1137: istore          5
        //  1139: aload_2        
        //  1140: astore          32
        //  1142: lload           17
        //  1144: lstore          7
        //  1146: lload           15
        //  1148: lstore          9
        //  1150: lload           13
        //  1152: lstore          11
        //  1154: iload           29
        //  1156: istore          27
        //  1158: aload_2        
        //  1159: astore          31
        //  1161: aload           34
        //  1163: astore          33
        //  1165: aload_2        
        //  1166: invokevirtual   java/net/HttpURLConnection.getErrorStream:()Ljava/io/InputStream;
        //  1169: astore          4
        //  1171: aload           4
        //  1173: ifnonnull       1373
        //  1176: lload           17
        //  1178: lstore          19
        //  1180: lload           15
        //  1182: lstore          21
        //  1184: lload           13
        //  1186: lstore          23
        //  1188: iload           6
        //  1190: istore          5
        //  1192: aload_2        
        //  1193: astore          32
        //  1195: lload           17
        //  1197: lstore          7
        //  1199: lload           15
        //  1201: lstore          9
        //  1203: lload           13
        //  1205: lstore          11
        //  1207: iload           29
        //  1209: istore          27
        //  1211: aload_2        
        //  1212: astore          31
        //  1214: aload           34
        //  1216: astore          33
        //  1218: new             Ljava/io/BufferedInputStream;
        //  1221: dup            
        //  1222: aload_2        
        //  1223: invokevirtual   java/net/HttpURLConnection.getInputStream:()Ljava/io/InputStream;
        //  1226: invokespecial   java/io/BufferedInputStream.<init>:(Ljava/io/InputStream;)V
        //  1229: astore          4
        //  1231: lload           17
        //  1233: lstore          19
        //  1235: lload           15
        //  1237: lstore          21
        //  1239: lload           13
        //  1241: lstore          23
        //  1243: iload           6
        //  1245: istore          5
        //  1247: aload_2        
        //  1248: astore          32
        //  1250: lload           17
        //  1252: lstore          7
        //  1254: lload           15
        //  1256: lstore          9
        //  1258: lload           13
        //  1260: lstore          11
        //  1262: iload           29
        //  1264: istore          27
        //  1266: aload_2        
        //  1267: astore          31
        //  1269: aload           34
        //  1271: astore          33
        //  1273: new             Lcom/swrve/sdk/rest/SwrveFilterInputStream;
        //  1276: dup            
        //  1277: aload           4
        //  1279: invokespecial   com/swrve/sdk/rest/SwrveFilterInputStream.<init>:(Ljava/io/InputStream;)V
        //  1282: astore          34
        //  1284: aload           37
        //  1286: astore          4
        //  1288: aload           34
        //  1290: invokestatic    com/swrve/sdk/SwrveHelper.readStringFromInputStream:(Ljava/io/InputStream;)Ljava/lang/String;
        //  1293: astore          31
        //  1295: aload           31
        //  1297: astore          4
        //  1299: aload_0        
        //  1300: lload           25
        //  1302: invokespecial   com/swrve/sdk/rest/RESTClient.milisecondsFrom:(J)J
        //  1305: lstore          7
        //  1307: aload_2        
        //  1308: ifnull          1315
        //  1311: aload_2        
        //  1312: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //  1315: aload           34
        //  1317: ifnull          1325
        //  1320: aload           34
        //  1322: invokevirtual   java/io/InputStream.close:()V
        //  1325: aload_0        
        //  1326: aload_1        
        //  1327: lload           17
        //  1329: lload           15
        //  1331: lload           13
        //  1333: lload           7
        //  1335: iconst_0       
        //  1336: invokespecial   com/swrve/sdk/rest/RESTClient.recordPostMetrics:(Ljava/lang/String;JJJJZ)V
        //  1339: iload           6
        //  1341: istore          5
        //  1343: aload           31
        //  1345: astore          4
        //  1347: aload_3        
        //  1348: ifnull          1372
        //  1351: aload_3        
        //  1352: new             Lcom/swrve/sdk/rest/RESTResponse;
        //  1355: dup            
        //  1356: iload           5
        //  1358: aload           4
        //  1360: aload_2        
        //  1361: invokevirtual   java/net/HttpURLConnection.getHeaderFields:()Ljava/util/Map;
        //  1364: invokespecial   com/swrve/sdk/rest/RESTResponse.<init>:(ILjava/lang/String;Ljava/util/Map;)V
        //  1367: invokeinterface com/swrve/sdk/rest/IRESTResponseListener.onResponse:(Lcom/swrve/sdk/rest/RESTResponse;)V
        //  1372: return         
        //  1373: lload           17
        //  1375: lstore          19
        //  1377: lload           15
        //  1379: lstore          21
        //  1381: lload           13
        //  1383: lstore          23
        //  1385: iload           6
        //  1387: istore          5
        //  1389: aload_2        
        //  1390: astore          32
        //  1392: lload           17
        //  1394: lstore          7
        //  1396: lload           15
        //  1398: lstore          9
        //  1400: lload           13
        //  1402: lstore          11
        //  1404: iload           29
        //  1406: istore          27
        //  1408: aload_2        
        //  1409: astore          31
        //  1411: aload           34
        //  1413: astore          33
        //  1415: new             Ljava/io/BufferedInputStream;
        //  1418: dup            
        //  1419: aload           4
        //  1421: invokespecial   java/io/BufferedInputStream.<init>:(Ljava/io/InputStream;)V
        //  1424: astore          4
        //  1426: goto            1231
        //  1429: astore          4
        //  1431: aload           4
        //  1433: invokevirtual   java/io/IOException.printStackTrace:()V
        //  1436: goto            1325
        //  1439: astore          31
        //  1441: aload           36
        //  1443: astore          34
        //  1445: aload           32
        //  1447: astore_2       
        //  1448: aload           35
        //  1450: astore          4
        //  1452: aload           31
        //  1454: astore          32
        //  1456: lload           23
        //  1458: lstore          13
        //  1460: lload           21
        //  1462: lstore          15
        //  1464: lload           19
        //  1466: lstore          17
        //  1468: lload           17
        //  1470: lstore          7
        //  1472: lload           15
        //  1474: lstore          9
        //  1476: lload           13
        //  1478: lstore          11
        //  1480: iload           29
        //  1482: istore          27
        //  1484: aload_2        
        //  1485: astore          31
        //  1487: aload           34
        //  1489: astore          33
        //  1491: aload           32
        //  1493: invokevirtual   java/lang/Exception.printStackTrace:()V
        //  1496: lload           17
        //  1498: lstore          7
        //  1500: lload           15
        //  1502: lstore          9
        //  1504: lload           13
        //  1506: lstore          11
        //  1508: iload           29
        //  1510: istore          27
        //  1512: aload_2        
        //  1513: astore          31
        //  1515: aload           34
        //  1517: astore          33
        //  1519: aload           32
        //  1521: instanceof      Ljava/net/SocketTimeoutException;
        //  1524: ifeq            1530
        //  1527: iconst_1       
        //  1528: istore          28
        //  1530: aload_3        
        //  1531: ifnull          1565
        //  1534: lload           17
        //  1536: lstore          7
        //  1538: lload           15
        //  1540: lstore          9
        //  1542: lload           13
        //  1544: lstore          11
        //  1546: iload           28
        //  1548: istore          27
        //  1550: aload_2        
        //  1551: astore          31
        //  1553: aload           34
        //  1555: astore          33
        //  1557: aload_3        
        //  1558: aload           32
        //  1560: invokeinterface com/swrve/sdk/rest/IRESTResponseListener.onException:(Ljava/lang/Exception;)V
        //  1565: aload_2        
        //  1566: ifnull          1573
        //  1569: aload_2        
        //  1570: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //  1573: aload           34
        //  1575: ifnull          1583
        //  1578: aload           34
        //  1580: invokevirtual   java/io/InputStream.close:()V
        //  1583: aload_0        
        //  1584: aload_1        
        //  1585: lload           17
        //  1587: lload           15
        //  1589: lload           13
        //  1591: lconst_0       
        //  1592: iload           28
        //  1594: invokespecial   com/swrve/sdk/rest/RESTClient.recordPostMetrics:(Ljava/lang/String;JJJJZ)V
        //  1597: goto            1347
        //  1600: astore          31
        //  1602: aload           31
        //  1604: invokevirtual   java/io/IOException.printStackTrace:()V
        //  1607: goto            1583
        //  1610: astore_2       
        //  1611: aload           31
        //  1613: ifnull          1621
        //  1616: aload           31
        //  1618: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //  1621: aload           33
        //  1623: ifnull          1631
        //  1626: aload           33
        //  1628: invokevirtual   java/io/InputStream.close:()V
        //  1631: aload_0        
        //  1632: aload_1        
        //  1633: lload           7
        //  1635: lload           9
        //  1637: lload           11
        //  1639: lconst_0       
        //  1640: iload           27
        //  1642: invokespecial   com/swrve/sdk/rest/RESTClient.recordPostMetrics:(Ljava/lang/String;JJJJZ)V
        //  1645: aload_2        
        //  1646: athrow         
        //  1647: astore_3       
        //  1648: aload_3        
        //  1649: invokevirtual   java/io/IOException.printStackTrace:()V
        //  1652: goto            1631
        //  1655: astore_3       
        //  1656: aload           34
        //  1658: astore          33
        //  1660: lload           17
        //  1662: lstore          7
        //  1664: lload           15
        //  1666: lstore          9
        //  1668: lload           13
        //  1670: lstore          11
        //  1672: iload           30
        //  1674: istore          27
        //  1676: aload_2        
        //  1677: astore          31
        //  1679: aload_3        
        //  1680: astore_2       
        //  1681: goto            1611
        //  1684: astore          32
        //  1686: iload           6
        //  1688: istore          5
        //  1690: goto            1468
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  85     93     1439   1468   Ljava/lang/Exception;
        //  85     93     1610   1611   Any
        //  137    152    1439   1468   Ljava/lang/Exception;
        //  137    152    1610   1611   Any
        //  194    201    1439   1468   Ljava/lang/Exception;
        //  194    201    1610   1611   Any
        //  243    250    1439   1468   Ljava/lang/Exception;
        //  243    250    1610   1611   Any
        //  292    299    1439   1468   Ljava/lang/Exception;
        //  292    299    1610   1611   Any
        //  341    350    1439   1468   Ljava/lang/Exception;
        //  341    350    1610   1611   Any
        //  392    400    1439   1468   Ljava/lang/Exception;
        //  392    400    1610   1611   Any
        //  442    450    1439   1468   Ljava/lang/Exception;
        //  442    450    1610   1611   Any
        //  492    499    1439   1468   Ljava/lang/Exception;
        //  492    499    1610   1611   Any
        //  541    546    1439   1468   Ljava/lang/Exception;
        //  541    546    1610   1611   Any
        //  588    593    1439   1468   Ljava/lang/Exception;
        //  588    593    1610   1611   Any
        //  635    640    1439   1468   Ljava/lang/Exception;
        //  635    640    1610   1611   Any
        //  682    688    1439   1468   Ljava/lang/Exception;
        //  682    688    1610   1611   Any
        //  730    735    1439   1468   Ljava/lang/Exception;
        //  730    735    1610   1611   Any
        //  777    781    1439   1468   Ljava/lang/Exception;
        //  777    781    1610   1611   Any
        //  823    831    1439   1468   Ljava/lang/Exception;
        //  823    831    1610   1611   Any
        //  873    879    1439   1468   Ljava/lang/Exception;
        //  873    879    1610   1611   Any
        //  921    928    1439   1468   Ljava/lang/Exception;
        //  921    928    1610   1611   Any
        //  970    975    1439   1468   Ljava/lang/Exception;
        //  970    975    1610   1611   Any
        //  1017   1025   1439   1468   Ljava/lang/Exception;
        //  1017   1025   1610   1611   Any
        //  1067   1073   1439   1468   Ljava/lang/Exception;
        //  1067   1073   1610   1611   Any
        //  1115   1123   1439   1468   Ljava/lang/Exception;
        //  1115   1123   1610   1611   Any
        //  1165   1171   1439   1468   Ljava/lang/Exception;
        //  1165   1171   1610   1611   Any
        //  1218   1231   1439   1468   Ljava/lang/Exception;
        //  1218   1231   1610   1611   Any
        //  1273   1284   1439   1468   Ljava/lang/Exception;
        //  1273   1284   1610   1611   Any
        //  1288   1295   1684   1693   Ljava/lang/Exception;
        //  1288   1295   1655   1684   Any
        //  1299   1307   1684   1693   Ljava/lang/Exception;
        //  1299   1307   1655   1684   Any
        //  1320   1325   1429   1439   Ljava/io/IOException;
        //  1415   1426   1439   1468   Ljava/lang/Exception;
        //  1415   1426   1610   1611   Any
        //  1491   1496   1610   1611   Any
        //  1519   1527   1610   1611   Any
        //  1557   1565   1610   1611   Any
        //  1578   1583   1600   1610   Ljava/io/IOException;
        //  1626   1631   1647   1655   Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_1315:
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
