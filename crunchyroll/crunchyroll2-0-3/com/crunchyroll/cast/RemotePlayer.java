// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.cast;

import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.common.api.GoogleApiClient;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.google.common.base.Optional;
import com.crunchyroll.cast.model.CastInfo;
import com.google.android.gms.cast.RemoteMediaPlayer;

class RemotePlayer extends RemoteMediaPlayer
{
    private static final String AD_INFO_TAG = "adInfo";
    private static final String CLICKTHROUGH_URL_TAG = "clickThrough";
    private static final String CONNECTED_SENDER_COUNT = "connectedSenderCount";
    private static final String CONTENT_INFO_TAG = "contentInfo";
    private static final String CURRENT_TIME_TAG = "currentTime";
    private static final String IS_AD_PLAYING_TAG = "isAdPlaying";
    private static final String MEDIA_ID_TAG = "mediaId";
    private static final String STATUS_TAG = "status";
    private static final String SYSTEM_INFO_TAG = "system";
    private static final String TAG;
    private CastInfo castInfo;
    private Optional<String> mClickthroughUrl;
    private double mCurrentTime;
    private final String mDeviceType;
    private boolean mIsOnlyConnectedDevice;
    private boolean mIsPlayingAd;
    private int mLastState;
    private final OnVideoMessageListener mListener;
    private Optional<Long> mMediaId;
    private int mPlayheadThreshold;
    private boolean mWaitingForNewEpisodePlayback;
    
    static {
        TAG = RemotePlayer.class.getName();
    }
    
    public RemotePlayer(final String mDeviceType, final OnVideoMessageListener mListener) {
        this.mLastState = 0;
        this.mMediaId = Optional.absent();
        this.mIsPlayingAd = false;
        this.mCurrentTime = 0.0;
        this.mClickthroughUrl = Optional.absent();
        this.mWaitingForNewEpisodePlayback = false;
        this.castInfo = null;
        this.mDeviceType = mDeviceType;
        this.mListener = mListener;
        this.mPlayheadThreshold = 0;
    }
    
    public static boolean isPlaybackState(final int n) {
        return n == 2 || n == 4 || n == 3;
    }
    
    private void onMediaIdChanged(final long n) {
        Log.i(RemotePlayer.TAG, "New mediaId " + n);
        this.mMediaId = Optional.of(n);
        if (this.mListener != null) {
            this.mListener.onMediaIdChanged(n);
        }
    }
    
    private void onStateChanged(final int mLastState) {
        if (isPlaybackState(mLastState)) {
            if (!isPlaybackState(this.mLastState)) {
                Log.i(RemotePlayer.TAG, "Switch to playback state");
                if (this.mListener != null) {
                    this.mListener.onPlaybackStart();
                }
            }
            else {
                Log.i(RemotePlayer.TAG, "Switch between playback states");
                if (this.mListener != null) {
                    this.mListener.onPlaybackStateChanged(mLastState);
                }
            }
        }
        else if (!isPlaybackState(mLastState) && isPlaybackState(this.mLastState)) {
            Log.i(RemotePlayer.TAG, "Switch to idle state");
            if (this.mListener != null) {
                this.mListener.onPlaybackStop();
            }
            this.mMediaId = Optional.absent();
        }
        this.mLastState = mLastState;
    }
    
    private void processAdState(final JSONObject jsonObject, final boolean mIsPlayingAd, final double mCurrentTime) {
        String tag = "";
        while (true) {
            try {
                final JSONObject jsonObject2 = jsonObject.getJSONObject("adInfo");
                String string = tag;
                if (jsonObject2.has("clickThrough")) {
                    string = jsonObject2.getString("clickThrough");
                }
                if (this.mIsPlayingAd == mIsPlayingAd) {
                    goto Label_0204;
                }
                final boolean b = true;
                if (mCurrentTime >= this.mCurrentTime || (!this.mClickthroughUrl.isPresent() || this.mClickthroughUrl.get().equals(string))) {
                    goto Label_0210;
                }
                final boolean b2 = true;
                this.mIsPlayingAd = mIsPlayingAd;
                this.mCurrentTime = mCurrentTime;
                if (string.length() <= 0) {
                    goto Label_0240;
                }
                this.mClickthroughUrl = Optional.of(string);
                tag = RemotePlayer.TAG;
                final StringBuilder append = new StringBuilder().append("New ads state: ");
                if (!mIsPlayingAd) {
                    goto Label_0250;
                }
                Log.i(tag, append.append("ON").toString());
                if (this.mIsPlayingAd) {
                    if ((b || b2) && this.mListener != null) {
                        this.mListener.onAdStart(this.mClickthroughUrl.orNull());
                    }
                    return;
                }
                goto Label_0256;
            }
            catch (JSONException ex) {
                ex.printStackTrace();
                final String string = tag;
                continue;
            }
            catch (NullPointerException ex2) {
                final String string = tag;
                continue;
            }
            break;
        }
    }
    
    public Optional<String> getClickthroughUrl() {
        return this.mClickthroughUrl;
    }
    
    public int getLastState() {
        return this.mLastState;
    }
    
    public Optional<Long> getMediaId() {
        return this.mMediaId;
    }
    
    public boolean isOnlyConnectedDevice() {
        return this.mIsOnlyConnectedDevice;
    }
    
    public boolean isPlayingAd() {
        return this.mIsPlayingAd;
    }
    
    public void load(final GoogleApiClient googleApiClient, final CastInfo castInfo, final long n, final long n2, final String s, final String s2) {
        Log.d(RemotePlayer.TAG, "Load media " + n + " playhead " + n2 + " msec");
        this.castInfo = castInfo;
        if (this.mMediaId.or(Long.valueOf(0L)) != n) {
            Log.d(RemotePlayer.TAG, "Playing new media, blocking metadata until new episode playing");
            this.mWaitingForNewEpisodePlayback = true;
        }
        this.mMediaId = Optional.of(n);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_type", (Object)this.mDeviceType);
            jsonObject.put("media_id", n);
            jsonObject.put("auth", (Object)s2);
            jsonObject.put("locale", (Object)s);
            this.load(googleApiClient, new MediaInfo.Builder(jsonObject.toString()).setContentType("video/mp4").setStreamType(1).build(), true, n2);
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void onMessageReceived(final CastDevice p0, final String p1, final String p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: aload_1        
        //     2: aload_2        
        //     3: aload_3        
        //     4: invokespecial   com/google/android/gms/cast/RemoteMediaPlayer.onMessageReceived:(Lcom/google/android/gms/cast/CastDevice;Ljava/lang/String;Ljava/lang/String;)V
        //     7: iconst_0       
        //     8: istore          14
        //    10: iconst_0       
        //    11: istore          18
        //    13: iconst_0       
        //    14: istore          17
        //    16: dconst_0       
        //    17: dstore          8
        //    19: aload_0        
        //    20: invokevirtual   com/crunchyroll/cast/RemotePlayer.getMediaStatus:()Lcom/google/android/gms/cast/MediaStatus;
        //    23: astore_1       
        //    24: aload_1        
        //    25: ifnull          810
        //    28: aload_1        
        //    29: invokevirtual   com/google/android/gms/cast/MediaStatus.getPlayerState:()I
        //    32: istore          13
        //    34: iload           13
        //    36: istore          12
        //    38: iload           13
        //    40: iconst_4       
        //    41: if_icmpne       47
        //    44: iconst_2       
        //    45: istore          12
        //    47: aload_1        
        //    48: invokevirtual   com/google/android/gms/cast/MediaStatus.getIdleReason:()I
        //    51: istore          13
        //    53: aconst_null    
        //    54: astore          21
        //    56: new             Lorg/json/JSONObject;
        //    59: dup            
        //    60: aload_3        
        //    61: invokespecial   org/json/JSONObject.<init>:(Ljava/lang/String;)V
        //    64: astore_1       
        //    65: aload_1        
        //    66: astore          21
        //    68: invokestatic    com/google/common/base/Optional.absent:()Lcom/google/common/base/Optional;
        //    71: astore_3       
        //    72: aconst_null    
        //    73: astore_1       
        //    74: aconst_null    
        //    75: astore          25
        //    77: aconst_null    
        //    78: astore          22
        //    80: aload_1        
        //    81: astore          23
        //    83: dload           8
        //    85: dstore          10
        //    87: iload           14
        //    89: istore          16
        //    91: aload_3        
        //    92: astore          24
        //    94: aload           21
        //    96: ifnull          579
        //    99: aload_1        
        //   100: astore          23
        //   102: dload           8
        //   104: dstore          10
        //   106: iload           14
        //   108: istore          16
        //   110: aload_3        
        //   111: astore          24
        //   113: aload           25
        //   115: astore_1       
        //   116: dload           8
        //   118: dstore          6
        //   120: iload           18
        //   122: istore          15
        //   124: aload_3        
        //   125: astore_2       
        //   126: aload           21
        //   128: ldc             "status"
        //   130: invokevirtual   org/json/JSONObject.has:(Ljava/lang/String;)Z
        //   133: ifeq            579
        //   136: aload           25
        //   138: astore_1       
        //   139: dload           8
        //   141: dstore          6
        //   143: iload           18
        //   145: istore          15
        //   147: aload_3        
        //   148: astore_2       
        //   149: aload           21
        //   151: ldc             "status"
        //   153: invokevirtual   org/json/JSONObject.getJSONArray:(Ljava/lang/String;)Lorg/json/JSONArray;
        //   156: iconst_0       
        //   157: invokevirtual   org/json/JSONArray.getJSONObject:(I)Lorg/json/JSONObject;
        //   160: astore          23
        //   162: dload           8
        //   164: dstore          4
        //   166: aload           25
        //   168: astore_1       
        //   169: dload           8
        //   171: dstore          6
        //   173: iload           18
        //   175: istore          15
        //   177: aload_3        
        //   178: astore_2       
        //   179: aload           23
        //   181: ldc             "currentTime"
        //   183: invokevirtual   org/json/JSONObject.has:(Ljava/lang/String;)Z
        //   186: ifeq            211
        //   189: aload           25
        //   191: astore_1       
        //   192: dload           8
        //   194: dstore          6
        //   196: iload           18
        //   198: istore          15
        //   200: aload_3        
        //   201: astore_2       
        //   202: aload           23
        //   204: ldc             "currentTime"
        //   206: invokevirtual   org/json/JSONObject.getDouble:(Ljava/lang/String;)D
        //   209: dstore          4
        //   211: aload           22
        //   213: astore          21
        //   215: iload           17
        //   217: istore          14
        //   219: aload_3        
        //   220: astore          22
        //   222: aload           25
        //   224: astore_1       
        //   225: dload           4
        //   227: dstore          6
        //   229: iload           18
        //   231: istore          15
        //   233: aload_3        
        //   234: astore_2       
        //   235: aload           23
        //   237: ldc             "contentInfo"
        //   239: invokevirtual   org/json/JSONObject.has:(Ljava/lang/String;)Z
        //   242: ifeq            415
        //   245: aload           25
        //   247: astore_1       
        //   248: dload           4
        //   250: dstore          6
        //   252: iload           18
        //   254: istore          15
        //   256: aload_3        
        //   257: astore_2       
        //   258: aload           23
        //   260: ldc             "contentInfo"
        //   262: invokevirtual   org/json/JSONObject.getJSONObject:(Ljava/lang/String;)Lorg/json/JSONObject;
        //   265: astore          24
        //   267: aload_3        
        //   268: astore          23
        //   270: aload           24
        //   272: astore_1       
        //   273: dload           4
        //   275: dstore          6
        //   277: iload           18
        //   279: istore          15
        //   281: aload_3        
        //   282: astore_2       
        //   283: aload           24
        //   285: ldc             "mediaId"
        //   287: invokevirtual   org/json/JSONObject.has:(Ljava/lang/String;)Z
        //   290: ifeq            348
        //   293: aload           24
        //   295: astore_1       
        //   296: dload           4
        //   298: dstore          6
        //   300: iload           18
        //   302: istore          15
        //   304: aload_3        
        //   305: astore_2       
        //   306: aload           24
        //   308: ldc             "mediaId"
        //   310: invokevirtual   org/json/JSONObject.getLong:(Ljava/lang/String;)J
        //   313: lstore          19
        //   315: aload_3        
        //   316: astore          23
        //   318: lload           19
        //   320: lconst_0       
        //   321: lcmp           
        //   322: ifle            348
        //   325: aload           24
        //   327: astore_1       
        //   328: dload           4
        //   330: dstore          6
        //   332: iload           18
        //   334: istore          15
        //   336: aload_3        
        //   337: astore_2       
        //   338: lload           19
        //   340: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   343: invokestatic    com/google/common/base/Optional.of:(Ljava/lang/Object;)Lcom/google/common/base/Optional;
        //   346: astore          23
        //   348: aload           24
        //   350: astore          21
        //   352: iload           17
        //   354: istore          14
        //   356: aload           23
        //   358: astore          22
        //   360: aload           24
        //   362: astore_1       
        //   363: dload           4
        //   365: dstore          6
        //   367: iload           18
        //   369: istore          15
        //   371: aload           23
        //   373: astore_2       
        //   374: aload           24
        //   376: ldc             "isAdPlaying"
        //   378: invokevirtual   org/json/JSONObject.has:(Ljava/lang/String;)Z
        //   381: ifeq            415
        //   384: aload           24
        //   386: astore_1       
        //   387: dload           4
        //   389: dstore          6
        //   391: iload           18
        //   393: istore          15
        //   395: aload           23
        //   397: astore_2       
        //   398: aload           24
        //   400: ldc             "isAdPlaying"
        //   402: invokevirtual   org/json/JSONObject.getBoolean:(Ljava/lang/String;)Z
        //   405: istore          14
        //   407: aload           23
        //   409: astore          22
        //   411: aload           24
        //   413: astore          21
        //   415: aload           21
        //   417: astore          23
        //   419: dload           4
        //   421: dstore          10
        //   423: iload           14
        //   425: istore          16
        //   427: aload           22
        //   429: astore          24
        //   431: aload           21
        //   433: astore_1       
        //   434: dload           4
        //   436: dstore          6
        //   438: iload           14
        //   440: istore          15
        //   442: aload           22
        //   444: astore_2       
        //   445: aload           21
        //   447: ldc             "system"
        //   449: invokevirtual   org/json/JSONObject.has:(Ljava/lang/String;)Z
        //   452: ifeq            579
        //   455: aload           21
        //   457: astore_1       
        //   458: dload           4
        //   460: dstore          6
        //   462: iload           14
        //   464: istore          15
        //   466: aload           22
        //   468: astore_2       
        //   469: aload           21
        //   471: ldc             "system"
        //   473: invokevirtual   org/json/JSONObject.getJSONObject:(Ljava/lang/String;)Lorg/json/JSONObject;
        //   476: astore_3       
        //   477: aload           21
        //   479: astore          23
        //   481: dload           4
        //   483: dstore          10
        //   485: iload           14
        //   487: istore          16
        //   489: aload           22
        //   491: astore          24
        //   493: aload           21
        //   495: astore_1       
        //   496: dload           4
        //   498: dstore          6
        //   500: iload           14
        //   502: istore          15
        //   504: aload           22
        //   506: astore_2       
        //   507: aload_3        
        //   508: ldc             "connectedSenderCount"
        //   510: invokevirtual   org/json/JSONObject.has:(Ljava/lang/String;)Z
        //   513: ifeq            579
        //   516: aload           21
        //   518: astore_1       
        //   519: dload           4
        //   521: dstore          6
        //   523: iload           14
        //   525: istore          15
        //   527: aload           22
        //   529: astore_2       
        //   530: aload_3        
        //   531: ldc             "connectedSenderCount"
        //   533: invokevirtual   org/json/JSONObject.getInt:(Ljava/lang/String;)I
        //   536: iconst_1       
        //   537: if_icmpne       835
        //   540: iconst_1       
        //   541: istore          16
        //   543: aload           21
        //   545: astore_1       
        //   546: dload           4
        //   548: dstore          6
        //   550: iload           14
        //   552: istore          15
        //   554: aload           22
        //   556: astore_2       
        //   557: aload_0        
        //   558: iload           16
        //   560: putfield        com/crunchyroll/cast/RemotePlayer.mIsOnlyConnectedDevice:Z
        //   563: aload           22
        //   565: astore          24
        //   567: iload           14
        //   569: istore          16
        //   571: dload           4
        //   573: dstore          10
        //   575: aload           21
        //   577: astore          23
        //   579: iload           13
        //   581: ifeq            612
        //   584: getstatic       com/crunchyroll/cast/RemotePlayer.TAG:Ljava/lang/String;
        //   587: new             Ljava/lang/StringBuilder;
        //   590: dup            
        //   591: invokespecial   java/lang/StringBuilder.<init>:()V
        //   594: ldc_w           "Idle Reason: "
        //   597: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   600: iload           13
        //   602: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   605: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   608: invokestatic    android/util/Log.i:(Ljava/lang/String;Ljava/lang/String;)I
        //   611: pop            
        //   612: aload           24
        //   614: invokevirtual   com/google/common/base/Optional.isPresent:()Z
        //   617: ifeq            710
        //   620: aload           24
        //   622: invokevirtual   com/google/common/base/Optional.get:()Ljava/lang/Object;
        //   625: checkcast       Ljava/lang/Long;
        //   628: invokevirtual   java/lang/Long.longValue:()J
        //   631: lstore          19
        //   633: aload_0        
        //   634: getfield        com/crunchyroll/cast/RemotePlayer.mMediaId:Lcom/google/common/base/Optional;
        //   637: invokevirtual   com/google/common/base/Optional.isPresent:()Z
        //   640: ifeq            664
        //   643: aload_0        
        //   644: getfield        com/crunchyroll/cast/RemotePlayer.mMediaId:Lcom/google/common/base/Optional;
        //   647: invokevirtual   com/google/common/base/Optional.get:()Ljava/lang/Object;
        //   650: checkcast       Ljava/lang/Long;
        //   653: lload           19
        //   655: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   658: invokevirtual   java/lang/Long.equals:(Ljava/lang/Object;)Z
        //   661: ifne            872
        //   664: aload_0        
        //   665: getfield        com/crunchyroll/cast/RemotePlayer.mWaitingForNewEpisodePlayback:Z
        //   668: ifeq            863
        //   671: getstatic       com/crunchyroll/cast/RemotePlayer.TAG:Ljava/lang/String;
        //   674: new             Ljava/lang/StringBuilder;
        //   677: dup            
        //   678: invokespecial   java/lang/StringBuilder.<init>:()V
        //   681: ldc_w           "Ignoring new mediaId, is waiting for new episode "
        //   684: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   687: aload_0        
        //   688: getfield        com/crunchyroll/cast/RemotePlayer.mMediaId:Lcom/google/common/base/Optional;
        //   691: invokevirtual   com/google/common/base/Optional.get:()Ljava/lang/Object;
        //   694: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   697: ldc_w           " to start"
        //   700: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   703: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   706: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   709: pop            
        //   710: aload_0        
        //   711: aload           23
        //   713: iload           16
        //   715: dload           10
        //   717: invokespecial   com/crunchyroll/cast/RemotePlayer.processAdState:(Lorg/json/JSONObject;ZD)V
        //   720: iload           16
        //   722: ifne            810
        //   725: aload_0        
        //   726: getfield        com/crunchyroll/cast/RemotePlayer.mWaitingForNewEpisodePlayback:Z
        //   729: ifne            810
        //   732: iload           12
        //   734: aload_0        
        //   735: getfield        com/crunchyroll/cast/RemotePlayer.mLastState:I
        //   738: if_icmpeq       810
        //   741: iload           12
        //   743: iconst_1       
        //   744: if_icmpne       954
        //   747: aload           24
        //   749: lconst_0       
        //   750: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   753: invokevirtual   com/google/common/base/Optional.or:(Ljava/lang/Object;)Ljava/lang/Object;
        //   756: checkcast       Ljava/lang/Long;
        //   759: invokevirtual   java/lang/Long.longValue:()J
        //   762: lconst_0       
        //   763: lcmp           
        //   764: ifeq            954
        //   767: iload           13
        //   769: iconst_3       
        //   770: if_icmpne       954
        //   773: getstatic       com/crunchyroll/cast/RemotePlayer.TAG:Ljava/lang/String;
        //   776: new             Ljava/lang/StringBuilder;
        //   779: dup            
        //   780: invokespecial   java/lang/StringBuilder.<init>:()V
        //   783: ldc_w           "Ignoring IDLE state while mediaId="
        //   786: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   789: aload           24
        //   791: invokevirtual   com/google/common/base/Optional.get:()Ljava/lang/Object;
        //   794: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   797: ldc_w           "and idleReason = interrupted"
        //   800: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   803: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   806: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   809: pop            
        //   810: aload_0        
        //   811: getfield        com/crunchyroll/cast/RemotePlayer.mListener:Lcom/crunchyroll/cast/RemotePlayer$OnVideoMessageListener;
        //   814: ifnull          826
        //   817: aload_0        
        //   818: getfield        com/crunchyroll/cast/RemotePlayer.mListener:Lcom/crunchyroll/cast/RemotePlayer$OnVideoMessageListener;
        //   821: invokeinterface com/crunchyroll/cast/RemotePlayer$OnVideoMessageListener.onPlaybackStatusUpdate:()V
        //   826: return         
        //   827: astore_1       
        //   828: aload_1        
        //   829: invokevirtual   org/json/JSONException.printStackTrace:()V
        //   832: goto            68
        //   835: iconst_0       
        //   836: istore          16
        //   838: goto            543
        //   841: astore_3       
        //   842: aload_3        
        //   843: invokevirtual   org/json/JSONException.printStackTrace:()V
        //   846: aload_1        
        //   847: astore          23
        //   849: dload           6
        //   851: dstore          10
        //   853: iload           15
        //   855: istore          16
        //   857: aload_2        
        //   858: astore          24
        //   860: goto            579
        //   863: aload_0        
        //   864: lload           19
        //   866: invokespecial   com/crunchyroll/cast/RemotePlayer.onMediaIdChanged:(J)V
        //   869: goto            710
        //   872: aload_0        
        //   873: getfield        com/crunchyroll/cast/RemotePlayer.mWaitingForNewEpisodePlayback:Z
        //   876: ifeq            710
        //   879: aload_0        
        //   880: getfield        com/crunchyroll/cast/RemotePlayer.mMediaId:Lcom/google/common/base/Optional;
        //   883: lconst_0       
        //   884: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   887: invokevirtual   com/google/common/base/Optional.or:(Ljava/lang/Object;)Ljava/lang/Object;
        //   890: checkcast       Ljava/lang/Long;
        //   893: invokevirtual   java/lang/Long.longValue:()J
        //   896: lload           19
        //   898: lcmp           
        //   899: ifne            710
        //   902: aload_0        
        //   903: invokevirtual   com/crunchyroll/cast/RemotePlayer.getMediaStatus:()Lcom/google/android/gms/cast/MediaStatus;
        //   906: invokevirtual   com/google/android/gms/cast/MediaStatus.getPlayerState:()I
        //   909: iconst_1       
        //   910: if_icmpeq       710
        //   913: getstatic       com/crunchyroll/cast/RemotePlayer.TAG:Ljava/lang/String;
        //   916: new             Ljava/lang/StringBuilder;
        //   919: dup            
        //   920: invokespecial   java/lang/StringBuilder.<init>:()V
        //   923: ldc             "New mediaId "
        //   925: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   928: lload           19
        //   930: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   933: ldc_w           " started, unblocking metadata"
        //   936: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   939: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   942: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   945: pop            
        //   946: aload_0        
        //   947: iconst_0       
        //   948: putfield        com/crunchyroll/cast/RemotePlayer.mWaitingForNewEpisodePlayback:Z
        //   951: goto            710
        //   954: aload_0        
        //   955: iload           12
        //   957: invokespecial   com/crunchyroll/cast/RemotePlayer.onStateChanged:(I)V
        //   960: goto            810
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                    
        //  -----  -----  -----  -----  ------------------------
        //  56     65     827    835    Lorg/json/JSONException;
        //  126    136    841    863    Lorg/json/JSONException;
        //  149    162    841    863    Lorg/json/JSONException;
        //  179    189    841    863    Lorg/json/JSONException;
        //  202    211    841    863    Lorg/json/JSONException;
        //  235    245    841    863    Lorg/json/JSONException;
        //  258    267    841    863    Lorg/json/JSONException;
        //  283    293    841    863    Lorg/json/JSONException;
        //  306    315    841    863    Lorg/json/JSONException;
        //  338    348    841    863    Lorg/json/JSONException;
        //  374    384    841    863    Lorg/json/JSONException;
        //  398    407    841    863    Lorg/json/JSONException;
        //  445    455    841    863    Lorg/json/JSONException;
        //  469    477    841    863    Lorg/json/JSONException;
        //  507    516    841    863    Lorg/json/JSONException;
        //  530    540    841    863    Lorg/json/JSONException;
        //  557    563    841    863    Lorg/json/JSONException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0211:
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
    
    public interface OnVideoMessageListener
    {
        void onAdStart(final String p0);
        
        void onAdStop();
        
        void onMediaIdChanged(final long p0);
        
        void onPlaybackStart();
        
        void onPlaybackStateChanged(final int p0);
        
        void onPlaybackStatusUpdate();
        
        void onPlaybackStop();
    }
}
