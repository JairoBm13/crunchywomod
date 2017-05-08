// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.os.Build;
import android.os.StatFs;
import android.os.Environment;
import java.util.List;

public class at
{
    private static at F;
    public String A;
    public String B;
    public String C;
    public String D;
    public int E;
    public String a;
    public String b;
    public String c;
    public String d;
    public String e;
    public String f;
    public boolean g;
    public String h;
    public String i;
    public int j;
    public int k;
    public int l;
    public String m;
    public int n;
    public long o;
    public long p;
    public int q;
    public boolean r;
    public boolean s;
    public boolean t;
    public List<String> u;
    public String v;
    public String w;
    public String x;
    public boolean y;
    public String z;
    
    static {
        at.F = new at();
    }
    
    private at() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: invokespecial   java/lang/Object.<init>:()V
        //     4: aload_0        
        //     5: ldc             ""
        //     7: putfield        com/tremorvideo/sdk/android/videoad/at.f:Ljava/lang/String;
        //    10: aload_0        
        //    11: iconst_0       
        //    12: putfield        com/tremorvideo/sdk/android/videoad/at.g:Z
        //    15: aload_0        
        //    16: ldc             ""
        //    18: putfield        com/tremorvideo/sdk/android/videoad/at.i:Ljava/lang/String;
        //    21: aload_0        
        //    22: iconst_0       
        //    23: putfield        com/tremorvideo/sdk/android/videoad/at.j:I
        //    26: aload_0        
        //    27: ldc             ""
        //    29: putfield        com/tremorvideo/sdk/android/videoad/at.x:Ljava/lang/String;
        //    32: aload_0        
        //    33: ldc             ""
        //    35: putfield        com/tremorvideo/sdk/android/videoad/at.A:Ljava/lang/String;
        //    38: aload_0        
        //    39: ldc             ""
        //    41: putfield        com/tremorvideo/sdk/android/videoad/at.B:Ljava/lang/String;
        //    44: aload_0        
        //    45: ldc             ""
        //    47: putfield        com/tremorvideo/sdk/android/videoad/at.D:Ljava/lang/String;
        //    50: aload_0        
        //    51: iconst_0       
        //    52: putfield        com/tremorvideo/sdk/android/videoad/at.E:I
        //    55: invokestatic    com/tremorvideo/sdk/android/videoad/ac.x:()Landroid/content/Context;
        //    58: astore_2       
        //    59: invokestatic    java/util/concurrent/Executors.newSingleThreadExecutor:()Ljava/util/concurrent/ExecutorService;
        //    62: astore_3       
        //    63: aload_3        
        //    64: new             Lcom/tremorvideo/sdk/android/videoad/at$1;
        //    67: dup            
        //    68: aload_0        
        //    69: invokespecial   com/tremorvideo/sdk/android/videoad/at$1.<init>:(Lcom/tremorvideo/sdk/android/videoad/at;)V
        //    72: invokeinterface java/util/concurrent/ExecutorService.submit:(Ljava/util/concurrent/Callable;)Ljava/util/concurrent/Future;
        //    77: astore          4
        //    79: aload           4
        //    81: ldc2_w          2500
        //    84: getstatic       java/util/concurrent/TimeUnit.MILLISECONDS:Ljava/util/concurrent/TimeUnit;
        //    87: invokeinterface java/util/concurrent/Future.get:(JLjava/util/concurrent/TimeUnit;)Ljava/lang/Object;
        //    92: checkcast       Ljava/util/Map;
        //    95: astore          4
        //    97: aload           4
        //    99: ifnull          137
        //   102: aload_0        
        //   103: aload           4
        //   105: ldc             "udid"
        //   107: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   112: checkcast       Ljava/lang/String;
        //   115: putfield        com/tremorvideo/sdk/android/videoad/at.f:Ljava/lang/String;
        //   118: aload_0        
        //   119: aload           4
        //   121: ldc             "optOut_Ad"
        //   123: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   128: checkcast       Ljava/lang/Boolean;
        //   131: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //   134: putfield        com/tremorvideo/sdk/android/videoad/at.g:Z
        //   137: aload_3        
        //   138: invokeinterface java/util/concurrent/ExecutorService.shutdown:()V
        //   143: aload_0        
        //   144: invokestatic    com/tremorvideo/sdk/android/videoad/ac.q:()Ljava/lang/String;
        //   147: putfield        com/tremorvideo/sdk/android/videoad/at.h:Ljava/lang/String;
        //   150: aload_0        
        //   151: invokestatic    com/tremorvideo/sdk/android/videoad/ac.A:()Ljava/lang/String;
        //   154: putfield        com/tremorvideo/sdk/android/videoad/at.a:Ljava/lang/String;
        //   157: invokestatic    com/tremorvideo/sdk/android/videoad/ac.r:()I
        //   160: iconst_4       
        //   161: if_icmplt       703
        //   164: aload_0        
        //   165: new             Lcom/tremorvideo/sdk/android/videoad/at$a;
        //   168: dup            
        //   169: aload_0        
        //   170: invokespecial   com/tremorvideo/sdk/android/videoad/at$a.<init>:(Lcom/tremorvideo/sdk/android/videoad/at;)V
        //   173: invokevirtual   com/tremorvideo/sdk/android/videoad/at$a.a:()Ljava/lang/String;
        //   176: putfield        com/tremorvideo/sdk/android/videoad/at.b:Ljava/lang/String;
        //   179: aload_0        
        //   180: invokestatic    java/util/TimeZone.getDefault:()Ljava/util/TimeZone;
        //   183: invokevirtual   java/util/TimeZone.getID:()Ljava/lang/String;
        //   186: putfield        com/tremorvideo/sdk/android/videoad/at.z:Ljava/lang/String;
        //   189: aload_2        
        //   190: ldc             "phone"
        //   192: invokevirtual   android/content/Context.getSystemService:(Ljava/lang/String;)Ljava/lang/Object;
        //   195: checkcast       Landroid/telephony/TelephonyManager;
        //   198: astore_3       
        //   199: aload_0        
        //   200: aload_3        
        //   201: invokevirtual   android/telephony/TelephonyManager.getSimCountryIso:()Ljava/lang/String;
        //   204: putfield        com/tremorvideo/sdk/android/videoad/at.v:Ljava/lang/String;
        //   207: aload_0        
        //   208: getfield        com/tremorvideo/sdk/android/videoad/at.v:Ljava/lang/String;
        //   211: ifnonnull       220
        //   214: aload_0        
        //   215: ldc             ""
        //   217: putfield        com/tremorvideo/sdk/android/videoad/at.v:Ljava/lang/String;
        //   220: aload_0        
        //   221: aload_3        
        //   222: invokevirtual   android/telephony/TelephonyManager.getNetworkCountryIso:()Ljava/lang/String;
        //   225: putfield        com/tremorvideo/sdk/android/videoad/at.w:Ljava/lang/String;
        //   228: aload_0        
        //   229: getfield        com/tremorvideo/sdk/android/videoad/at.w:Ljava/lang/String;
        //   232: ifnonnull       241
        //   235: aload_0        
        //   236: ldc             ""
        //   238: putfield        com/tremorvideo/sdk/android/videoad/at.w:Ljava/lang/String;
        //   241: aload_0        
        //   242: getfield        com/tremorvideo/sdk/android/videoad/at.v:Ljava/lang/String;
        //   245: ifnull          713
        //   248: aload_0        
        //   249: getfield        com/tremorvideo/sdk/android/videoad/at.v:Ljava/lang/String;
        //   252: invokevirtual   java/lang/String.length:()I
        //   255: ifle            713
        //   258: aload_0        
        //   259: aload_0        
        //   260: getfield        com/tremorvideo/sdk/android/videoad/at.v:Ljava/lang/String;
        //   263: putfield        com/tremorvideo/sdk/android/videoad/at.x:Ljava/lang/String;
        //   266: aload_3        
        //   267: invokevirtual   android/telephony/TelephonyManager.getSimState:()I
        //   270: iconst_5       
        //   271: if_icmpne       282
        //   274: aload_0        
        //   275: aload_3        
        //   276: invokevirtual   android/telephony/TelephonyManager.getSimOperatorName:()Ljava/lang/String;
        //   279: putfield        com/tremorvideo/sdk/android/videoad/at.m:Ljava/lang/String;
        //   282: aload_0        
        //   283: getfield        com/tremorvideo/sdk/android/videoad/at.m:Ljava/lang/String;
        //   286: ifnull          299
        //   289: aload_0        
        //   290: getfield        com/tremorvideo/sdk/android/videoad/at.m:Ljava/lang/String;
        //   293: invokevirtual   java/lang/String.length:()I
        //   296: ifne            320
        //   299: aload_0        
        //   300: aload_3        
        //   301: invokevirtual   android/telephony/TelephonyManager.getNetworkOperatorName:()Ljava/lang/String;
        //   304: putfield        com/tremorvideo/sdk/android/videoad/at.m:Ljava/lang/String;
        //   307: aload_0        
        //   308: getfield        com/tremorvideo/sdk/android/videoad/at.m:Ljava/lang/String;
        //   311: ifnonnull       320
        //   314: aload_0        
        //   315: ldc             ""
        //   317: putfield        com/tremorvideo/sdk/android/videoad/at.m:Ljava/lang/String;
        //   320: aload_3        
        //   321: invokevirtual   android/telephony/TelephonyManager.getSimOperator:()Ljava/lang/String;
        //   324: astore          4
        //   326: aload           4
        //   328: ifnull          741
        //   331: aload           4
        //   333: invokevirtual   java/lang/String.length:()I
        //   336: ifle            741
        //   339: aload_0        
        //   340: aload           4
        //   342: iconst_0       
        //   343: iconst_3       
        //   344: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   347: putfield        com/tremorvideo/sdk/android/videoad/at.A:Ljava/lang/String;
        //   350: aload_0        
        //   351: aload           4
        //   353: iconst_3       
        //   354: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   357: putfield        com/tremorvideo/sdk/android/videoad/at.B:Ljava/lang/String;
        //   360: aload_3        
        //   361: invokevirtual   android/telephony/TelephonyManager.getPhoneType:()I
        //   364: ifne            813
        //   367: iconst_0       
        //   368: istore_1       
        //   369: aload_0        
        //   370: iload_1        
        //   371: putfield        com/tremorvideo/sdk/android/videoad/at.y:Z
        //   374: aload_0        
        //   375: getstatic       com/tremorvideo/sdk/android/videoad/ac.p:Ljava/lang/String;
        //   378: putfield        com/tremorvideo/sdk/android/videoad/at.C:Ljava/lang/String;
        //   381: aload_0        
        //   382: getfield        com/tremorvideo/sdk/android/videoad/at.C:Ljava/lang/String;
        //   385: ifnonnull       394
        //   388: aload_0        
        //   389: ldc             ""
        //   391: putfield        com/tremorvideo/sdk/android/videoad/at.C:Ljava/lang/String;
        //   394: aload_0        
        //   395: getstatic       android/os/Build.MODEL:Ljava/lang/String;
        //   398: putfield        com/tremorvideo/sdk/android/videoad/at.c:Ljava/lang/String;
        //   401: aload_0        
        //   402: getfield        com/tremorvideo/sdk/android/videoad/at.c:Ljava/lang/String;
        //   405: ifnonnull       414
        //   408: aload_0        
        //   409: ldc             ""
        //   411: putfield        com/tremorvideo/sdk/android/videoad/at.c:Ljava/lang/String;
        //   414: aload_0        
        //   415: ldc             "Android OS"
        //   417: putfield        com/tremorvideo/sdk/android/videoad/at.d:Ljava/lang/String;
        //   420: aload_0        
        //   421: getstatic       android/os/Build$VERSION.RELEASE:Ljava/lang/String;
        //   424: putfield        com/tremorvideo/sdk/android/videoad/at.e:Ljava/lang/String;
        //   427: aload_0        
        //   428: getfield        com/tremorvideo/sdk/android/videoad/at.e:Ljava/lang/String;
        //   431: ifnonnull       440
        //   434: aload_0        
        //   435: ldc             ""
        //   437: putfield        com/tremorvideo/sdk/android/videoad/at.e:Ljava/lang/String;
        //   440: aload_2        
        //   441: ldc             "connectivity"
        //   443: invokevirtual   android/content/Context.getSystemService:(Ljava/lang/String;)Ljava/lang/Object;
        //   446: checkcast       Landroid/net/ConnectivityManager;
        //   449: invokevirtual   android/net/ConnectivityManager.getActiveNetworkInfo:()Landroid/net/NetworkInfo;
        //   452: astore_3       
        //   453: aload_3        
        //   454: ifnull          487
        //   457: aload_3        
        //   458: invokevirtual   android/net/NetworkInfo.getTypeName:()Ljava/lang/String;
        //   461: ldc             "WIFI"
        //   463: invokevirtual   java/lang/String.compareToIgnoreCase:(Ljava/lang/String;)I
        //   466: ifne            818
        //   469: aload_0        
        //   470: ldc_w           "wifi"
        //   473: putfield        com/tremorvideo/sdk/android/videoad/at.i:Ljava/lang/String;
        //   476: aload_0        
        //   477: ldc             ""
        //   479: putfield        com/tremorvideo/sdk/android/videoad/at.D:Ljava/lang/String;
        //   482: aload_0        
        //   483: iconst_2       
        //   484: putfield        com/tremorvideo/sdk/android/videoad/at.j:I
        //   487: aload_2        
        //   488: ldc_w           "window"
        //   491: invokevirtual   android/content/Context.getSystemService:(Ljava/lang/String;)Ljava/lang/Object;
        //   494: checkcast       Landroid/view/WindowManager;
        //   497: invokeinterface android/view/WindowManager.getDefaultDisplay:()Landroid/view/Display;
        //   502: astore_2       
        //   503: new             Landroid/util/DisplayMetrics;
        //   506: dup            
        //   507: invokespecial   android/util/DisplayMetrics.<init>:()V
        //   510: astore_3       
        //   511: aload_2        
        //   512: aload_3        
        //   513: invokevirtual   android/view/Display.getMetrics:(Landroid/util/DisplayMetrics;)V
        //   516: aload_0        
        //   517: aload_3        
        //   518: getfield        android/util/DisplayMetrics.widthPixels:I
        //   521: i2f            
        //   522: invokestatic    java/lang/Math.round:(F)I
        //   525: putfield        com/tremorvideo/sdk/android/videoad/at.k:I
        //   528: aload_0        
        //   529: aload_3        
        //   530: getfield        android/util/DisplayMetrics.heightPixels:I
        //   533: i2f            
        //   534: invokestatic    java/lang/Math.round:(F)I
        //   537: putfield        com/tremorvideo/sdk/android/videoad/at.l:I
        //   540: new             Landroid/graphics/PixelFormat;
        //   543: dup            
        //   544: invokespecial   android/graphics/PixelFormat.<init>:()V
        //   547: astore_3       
        //   548: aload_2        
        //   549: invokevirtual   android/view/Display.getPixelFormat:()I
        //   552: aload_3        
        //   553: invokestatic    android/graphics/PixelFormat.getPixelFormatInfo:(ILandroid/graphics/PixelFormat;)V
        //   556: aload_0        
        //   557: aload_3        
        //   558: getfield        android/graphics/PixelFormat.bitsPerPixel:I
        //   561: putfield        com/tremorvideo/sdk/android/videoad/at.n:I
        //   564: aload_0        
        //   565: invokestatic    java/lang/Runtime.getRuntime:()Ljava/lang/Runtime;
        //   568: invokevirtual   java/lang/Runtime.freeMemory:()J
        //   571: putfield        com/tremorvideo/sdk/android/videoad/at.o:J
        //   574: aload_0        
        //   575: aload_0        
        //   576: invokespecial   com/tremorvideo/sdk/android/videoad/at.d:()J
        //   579: putfield        com/tremorvideo/sdk/android/videoad/at.p:J
        //   582: aload_0        
        //   583: iconst_0       
        //   584: putfield        com/tremorvideo/sdk/android/videoad/at.q:I
        //   587: aload_0        
        //   588: aload_0        
        //   589: invokespecial   com/tremorvideo/sdk/android/videoad/at.c:()Z
        //   592: putfield        com/tremorvideo/sdk/android/videoad/at.r:Z
        //   595: aload_0        
        //   596: aload_0        
        //   597: invokespecial   com/tremorvideo/sdk/android/videoad/at.e:()Z
        //   600: putfield        com/tremorvideo/sdk/android/videoad/at.s:Z
        //   603: aload_0        
        //   604: invokestatic    com/tremorvideo/sdk/android/videoad/bp.a:()I
        //   607: putfield        com/tremorvideo/sdk/android/videoad/at.E:I
        //   610: aload_0        
        //   611: iconst_0       
        //   612: putfield        com/tremorvideo/sdk/android/videoad/at.t:Z
        //   615: aload_0        
        //   616: new             Ljava/util/ArrayList;
        //   619: dup            
        //   620: invokespecial   java/util/ArrayList.<init>:()V
        //   623: putfield        com/tremorvideo/sdk/android/videoad/at.u:Ljava/util/List;
        //   626: return         
        //   627: astore          4
        //   629: aload           4
        //   631: invokevirtual   java/util/concurrent/TimeoutException.printStackTrace:()V
        //   634: aload_3        
        //   635: invokeinterface java/util/concurrent/ExecutorService.shutdown:()V
        //   640: getstatic       com/tremorvideo/sdk/android/videoad/at.F:Lcom/tremorvideo/sdk/android/videoad/at;
        //   643: ifnonnull       660
        //   646: aload_0        
        //   647: ldc             ""
        //   649: putfield        com/tremorvideo/sdk/android/videoad/at.f:Ljava/lang/String;
        //   652: aload_0        
        //   653: iconst_0       
        //   654: putfield        com/tremorvideo/sdk/android/videoad/at.g:Z
        //   657: goto            137
        //   660: aload_0        
        //   661: getstatic       com/tremorvideo/sdk/android/videoad/at.F:Lcom/tremorvideo/sdk/android/videoad/at;
        //   664: getfield        com/tremorvideo/sdk/android/videoad/at.f:Ljava/lang/String;
        //   667: putfield        com/tremorvideo/sdk/android/videoad/at.f:Ljava/lang/String;
        //   670: aload_0        
        //   671: getstatic       com/tremorvideo/sdk/android/videoad/at.F:Lcom/tremorvideo/sdk/android/videoad/at;
        //   674: getfield        com/tremorvideo/sdk/android/videoad/at.g:Z
        //   677: putfield        com/tremorvideo/sdk/android/videoad/at.g:Z
        //   680: goto            137
        //   683: astore          4
        //   685: aload           4
        //   687: invokevirtual   java/lang/InterruptedException.printStackTrace:()V
        //   690: goto            137
        //   693: astore          4
        //   695: aload           4
        //   697: invokevirtual   java/util/concurrent/ExecutionException.printStackTrace:()V
        //   700: goto            137
        //   703: aload_0        
        //   704: ldc_w           "Android"
        //   707: putfield        com/tremorvideo/sdk/android/videoad/at.b:Ljava/lang/String;
        //   710: goto            179
        //   713: aload_0        
        //   714: getfield        com/tremorvideo/sdk/android/videoad/at.w:Ljava/lang/String;
        //   717: ifnull          266
        //   720: aload_0        
        //   721: getfield        com/tremorvideo/sdk/android/videoad/at.w:Ljava/lang/String;
        //   724: invokevirtual   java/lang/String.length:()I
        //   727: ifle            266
        //   730: aload_0        
        //   731: aload_0        
        //   732: getfield        com/tremorvideo/sdk/android/videoad/at.w:Ljava/lang/String;
        //   735: putfield        com/tremorvideo/sdk/android/videoad/at.x:Ljava/lang/String;
        //   738: goto            266
        //   741: aload_3        
        //   742: invokevirtual   android/telephony/TelephonyManager.getNetworkOperator:()Ljava/lang/String;
        //   745: astore          4
        //   747: aload           4
        //   749: ifnull          360
        //   752: aload           4
        //   754: invokevirtual   java/lang/String.length:()I
        //   757: ifle            360
        //   760: aload_0        
        //   761: aload           4
        //   763: iconst_0       
        //   764: iconst_3       
        //   765: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   768: putfield        com/tremorvideo/sdk/android/videoad/at.A:Ljava/lang/String;
        //   771: aload_0        
        //   772: aload           4
        //   774: iconst_3       
        //   775: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   778: putfield        com/tremorvideo/sdk/android/videoad/at.B:Ljava/lang/String;
        //   781: goto            360
        //   784: astore          4
        //   786: new             Ljava/lang/StringBuilder;
        //   789: dup            
        //   790: invokespecial   java/lang/StringBuilder.<init>:()V
        //   793: ldc_w           "Exception fetch mcc,mnc:"
        //   796: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   799: aload           4
        //   801: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   804: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   807: invokestatic    com/tremorvideo/sdk/android/videoad/ac.e:(Ljava/lang/String;)V
        //   810: goto            360
        //   813: iconst_1       
        //   814: istore_1       
        //   815: goto            369
        //   818: aload_3        
        //   819: invokevirtual   android/net/NetworkInfo.getTypeName:()Ljava/lang/String;
        //   822: ldc_w           "MOBILE"
        //   825: invokevirtual   java/lang/String.compareToIgnoreCase:(Ljava/lang/String;)I
        //   828: ifne            976
        //   831: aload_0        
        //   832: ldc_w           "cellular"
        //   835: putfield        com/tremorvideo/sdk/android/videoad/at.i:Ljava/lang/String;
        //   838: aload_3        
        //   839: invokevirtual   android/net/NetworkInfo.getSubtype:()I
        //   842: tableswitch {
        //                2: 930
        //                3: 930
        //                4: 945
        //                5: 930
        //                6: 945
        //                7: 945
        //                8: 930
        //                9: 945
        //               10: 945
        //               11: 945
        //               12: 930
        //               13: 945
        //               14: 960
        //               15: 945
        //               16: 945
        //          default: 916
        //        }
        //   916: aload_0        
        //   917: ldc             ""
        //   919: putfield        com/tremorvideo/sdk/android/videoad/at.D:Ljava/lang/String;
        //   922: aload_0        
        //   923: iconst_3       
        //   924: putfield        com/tremorvideo/sdk/android/videoad/at.j:I
        //   927: goto            487
        //   930: aload_0        
        //   931: ldc_w           "2g"
        //   934: putfield        com/tremorvideo/sdk/android/videoad/at.D:Ljava/lang/String;
        //   937: aload_0        
        //   938: iconst_4       
        //   939: putfield        com/tremorvideo/sdk/android/videoad/at.j:I
        //   942: goto            487
        //   945: aload_0        
        //   946: ldc_w           "3g"
        //   949: putfield        com/tremorvideo/sdk/android/videoad/at.D:Ljava/lang/String;
        //   952: aload_0        
        //   953: iconst_5       
        //   954: putfield        com/tremorvideo/sdk/android/videoad/at.j:I
        //   957: goto            487
        //   960: aload_0        
        //   961: ldc_w           "4g"
        //   964: putfield        com/tremorvideo/sdk/android/videoad/at.D:Ljava/lang/String;
        //   967: aload_0        
        //   968: bipush          6
        //   970: putfield        com/tremorvideo/sdk/android/videoad/at.j:I
        //   973: goto            487
        //   976: aload_3        
        //   977: invokevirtual   android/net/NetworkInfo.getTypeName:()Ljava/lang/String;
        //   980: ldc_w           "ETHERNET"
        //   983: invokevirtual   java/lang/String.compareToIgnoreCase:(Ljava/lang/String;)I
        //   986: ifne            1010
        //   989: aload_0        
        //   990: ldc_w           "ethernet"
        //   993: putfield        com/tremorvideo/sdk/android/videoad/at.i:Ljava/lang/String;
        //   996: aload_0        
        //   997: ldc             ""
        //   999: putfield        com/tremorvideo/sdk/android/videoad/at.D:Ljava/lang/String;
        //  1002: aload_0        
        //  1003: iconst_1       
        //  1004: putfield        com/tremorvideo/sdk/android/videoad/at.j:I
        //  1007: goto            487
        //  1010: aload_3        
        //  1011: invokevirtual   android/net/NetworkInfo.getTypeName:()Ljava/lang/String;
        //  1014: ifnull          487
        //  1017: aload_0        
        //  1018: aload_3        
        //  1019: invokevirtual   android/net/NetworkInfo.getTypeName:()Ljava/lang/String;
        //  1022: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //  1025: putfield        com/tremorvideo/sdk/android/videoad/at.i:Ljava/lang/String;
        //  1028: aload_0        
        //  1029: ldc             ""
        //  1031: putfield        com/tremorvideo/sdk/android/videoad/at.D:Ljava/lang/String;
        //  1034: aload_0        
        //  1035: iconst_0       
        //  1036: putfield        com/tremorvideo/sdk/android/videoad/at.j:I
        //  1039: goto            487
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  79     97     627    683    Ljava/util/concurrent/TimeoutException;
        //  79     97     683    693    Ljava/lang/InterruptedException;
        //  79     97     693    703    Ljava/util/concurrent/ExecutionException;
        //  102    137    627    683    Ljava/util/concurrent/TimeoutException;
        //  102    137    683    693    Ljava/lang/InterruptedException;
        //  102    137    693    703    Ljava/util/concurrent/ExecutionException;
        //  320    326    784    813    Ljava/lang/Exception;
        //  331    360    784    813    Ljava/lang/Exception;
        //  741    747    784    813    Ljava/lang/Exception;
        //  752    781    784    813    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0320:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:692)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:529)
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
    
    public static at a() {
        while (true) {
            Label_0032: {
                if (at.F != null) {
                    break Label_0032;
                }
                synchronized (at.class) {
                    at.F = new at();
                    return at.F;
                }
            }
            at.F.f();
            continue;
        }
    }
    
    public static at b() {
        return at.F;
    }
    
    private boolean c() {
        boolean b = false;
        try {
            if (ac.x().getSystemService("sensor") != null) {
                b = true;
            }
            return b;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    private long d() {
        final StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        return Math.abs(statFs.getFreeBlocks() * statFs.getBlockSize());
    }
    
    private boolean e() {
        boolean b = false;
        try {
            if (ac.x().getSystemService("location") != null) {
                b = true;
            }
            return b;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    private void f() {
        this.E = bp.a();
    }
    
    class a
    {
        public String a() {
            return Build.MANUFACTURER;
        }
    }
}
