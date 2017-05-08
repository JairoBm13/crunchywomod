// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.exoplayer;

import android.app.Activity;
import java.io.IOException;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.util.Util;
import java.util.Iterator;
import com.google.android.exoplayer.upstream.UriLoadable;
import com.google.android.exoplayer.upstream.UriDataSource;
import com.google.android.exoplayer.hls.HlsPlaylistParser;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import android.util.AttributeSet;
import java.util.ArrayList;
import android.content.Context;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import android.os.Handler;
import java.util.List;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.AspectRatioFrameLayout;
import com.google.android.exoplayer.hls.HlsPlaylist;
import com.google.android.exoplayer.util.ManifestFetcher;
import com.google.android.exoplayer.ExoPlayer;
import android.view.SurfaceView;

public class ExoPlayerView extends SurfaceView implements VideoSizeChangeListener, VideoControllerListener, VideoPlayer, Listener, ManifestCallback<HlsPlaylist>
{
    private static final int BUFFER_SEGMENTS = 64;
    private static final int BUFFER_SEGMENT_SIZE = 262144;
    public static final int DEFAULT_MAX_BUFFER_TO_SWITCH_DOWN_MS = 5000;
    public static final int DEFAULT_MIN_BUFFER_MS = 1000;
    public static final int DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS = 1000;
    public static final int DEFAULT_MIN_REBUFFER_MS = 5000;
    private static final int PLAYER_UPDATE_INTERVAL = 500;
    private AspectRatioFrameLayout mAspectFrameLayout;
    private MediaCodecAudioTrackRenderer mAudioRenderer;
    private VideoController mController;
    private int mCurrentVideoIndex;
    private ErrorListener mErrorListener;
    private List<PlayerEventListener> mEventListeners;
    private ExoPlayer mExoPlayer;
    private Handler mHandler;
    private int mLastReportedPlaybackState;
    private int mMinBufferMs;
    private int mMinBufferToSwitchDownMs;
    private int mMinBufferToSwitchUpMs;
    private int mMinRebufferMs;
    private long mPlayerPosition;
    private StateChangeListener mStateListener;
    private boolean mStopped;
    private String mUserAgent;
    private List<String> mVideoListQueue;
    private VideoQuality mVideoQuality;
    private MediaCodecVideoTrackRenderer mVideoRenderer;
    private ManifestFetcher<HlsPlaylist> playlistFetcher;
    
    public ExoPlayerView(final Context context) {
        super(context);
        this.mVideoListQueue = new ArrayList<String>();
        this.mCurrentVideoIndex = 0;
        this.mEventListeners = new ArrayList<PlayerEventListener>();
        this.mPlayerPosition = 0L;
        this.mLastReportedPlaybackState = 1;
        this.mMinBufferMs = 1000;
        this.mMinRebufferMs = 5000;
        this.mMinBufferToSwitchUpMs = 1000;
        this.mMinBufferToSwitchDownMs = 5000;
        this.mVideoQuality = VideoQuality.QUALITY_ADAPTIVE;
        this.mStopped = false;
    }
    
    public ExoPlayerView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mVideoListQueue = new ArrayList<String>();
        this.mCurrentVideoIndex = 0;
        this.mEventListeners = new ArrayList<PlayerEventListener>();
        this.mPlayerPosition = 0L;
        this.mLastReportedPlaybackState = 1;
        this.mMinBufferMs = 1000;
        this.mMinRebufferMs = 5000;
        this.mMinBufferToSwitchUpMs = 1000;
        this.mMinBufferToSwitchDownMs = 5000;
        this.mVideoQuality = VideoQuality.QUALITY_ADAPTIVE;
        this.mStopped = false;
    }
    
    public ExoPlayerView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mVideoListQueue = new ArrayList<String>();
        this.mCurrentVideoIndex = 0;
        this.mEventListeners = new ArrayList<PlayerEventListener>();
        this.mPlayerPosition = 0L;
        this.mLastReportedPlaybackState = 1;
        this.mMinBufferMs = 1000;
        this.mMinRebufferMs = 5000;
        this.mMinBufferToSwitchUpMs = 1000;
        this.mMinBufferToSwitchDownMs = 5000;
        this.mVideoQuality = VideoQuality.QUALITY_ADAPTIVE;
        this.mStopped = false;
    }
    
    private String getCurrentVideoUrl() {
        if (this.mVideoListQueue.size() == 0) {
            throw new IllegalStateException("You are trying to access the video queue without having added any videos!");
        }
        return this.mVideoListQueue.get(this.mCurrentVideoIndex);
    }
    
    private PlayerState getPlayerState(final int n) {
        switch (n) {
            default: {
                return PlayerState.STATE_IDLE;
            }
            case 5: {
                return PlayerState.STATE_ENDED;
            }
            case 4: {
                return PlayerState.STATE_READY;
            }
            case 3: {
                return PlayerState.STATE_BUFFERING;
            }
            case 1: {
                return PlayerState.STATE_IDLE;
            }
            case 2: {
                return PlayerState.STATE_PREPARING;
            }
        }
    }
    
    private void maybeReportPlayerState() {
        final int playbackState = this.mExoPlayer.getPlaybackState();
        if (this.mLastReportedPlaybackState != playbackState) {
            if (this.mStateListener != null) {
                this.mStateListener.onStateChange(this.getPlayerState(playbackState));
            }
            this.mLastReportedPlaybackState = playbackState;
        }
    }
    
    private void resetPlayer() {
        (this.playlistFetcher = new ManifestFetcher<HlsPlaylist>(this.getCurrentVideoUrl(), new DefaultUriDataSource(this.getContext(), this.mUserAgent), new HlsPlaylistParser())).singleLoad(this.mHandler.getLooper(), (ManifestFetcher.ManifestCallback<HlsPlaylist>)this);
    }
    
    private void updateVideoProgress() {
        if (this.mController != null && this.mExoPlayer != null) {
            this.mController.setSeekPosition(this.mExoPlayer.getCurrentPosition());
            this.mController.setVideoDuration(this.mExoPlayer.getDuration());
        }
        if (this.mHandler != null) {
            this.mHandler.postDelayed((Runnable)new Runnable() {
                @Override
                public void run() {
                    ExoPlayerView.this.updateVideoProgress();
                }
            }, 500L);
        }
    }
    
    public void addPlayerEventListener(final PlayerEventListener playerEventListener) {
        if (!this.mEventListeners.contains(playerEventListener)) {
            this.mEventListeners.add(playerEventListener);
        }
    }
    
    public void addVideo(final String s) {
        this.mVideoListQueue.add(s);
    }
    
    public void addVideos(final List<String> list) {
        final Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.mVideoListQueue.add(iterator.next());
        }
    }
    
    public AspectRatioFrameLayout getAspectRatioFrameLayout() {
        return this.mAspectFrameLayout;
    }
    
    public int getDuration() {
        if (this.mExoPlayer != null) {
            return (int)this.mExoPlayer.getDuration();
        }
        return 0;
    }
    
    public long getVideoPosition() {
        if (this.mExoPlayer != null) {
            return this.mExoPlayer.getCurrentPosition();
        }
        return 0L;
    }
    
    public void init() {
        this.mStopped = false;
        if (this.mVideoListQueue.size() == 0) {
            return;
        }
        if (this.mController != null) {
            this.mController.addVideoControllerListener(this);
        }
        this.mAspectFrameLayout = (AspectRatioFrameLayout)this.getParent();
        this.mHandler = new Handler();
        this.mUserAgent = Util.getUserAgent(this.getContext(), "CX Video Player");
        this.resetPlayer();
    }
    
    public boolean isPlaying() {
        return this.mExoPlayer != null && this.mExoPlayer.getPlayWhenReady();
    }
    
    public boolean isPlayingLastVideo() {
        return this.mCurrentVideoIndex == this.mVideoListQueue.size() - 1;
    }
    
    public void onFastForwardPressed(final int n) {
        this.seekTo(this.getVideoPosition() + n);
        final Iterator<PlayerEventListener> iterator = this.mEventListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onFastForward();
        }
    }
    
    public void onNextPressed() {
        this.playNext();
    }
    
    public void onPlayPausePressed() {
        if (this.isPlaying()) {
            this.pause();
            return;
        }
        this.play();
    }
    
    public void onPlayWhenReadyCommitted() {
    }
    
    public void onPlayerError(final ExoPlaybackException ex) {
        if (this.mErrorListener != null) {
            this.mErrorListener.onError(ex);
        }
    }
    
    public void onPlayerStateChanged(final boolean b, final int n) {
        this.maybeReportPlayerState();
    }
    
    public void onRewindPressed(final int n) {
        this.seekTo(this.getVideoPosition() - n);
        final Iterator<PlayerEventListener> iterator = this.mEventListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onRewind();
        }
    }
    
    public void onSeekProgressChanged(final int n) {
    }
    
    public void onSingleManifest(final HlsPlaylist p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: getfield        com/crunchyroll/exoplayer/ExoPlayerView.mStopped:Z
        //     4: ifeq            8
        //     7: return         
        //     8: new             Lcom/google/android/exoplayer/DefaultLoadControl;
        //    11: dup            
        //    12: new             Lcom/google/android/exoplayer/upstream/DefaultAllocator;
        //    15: dup            
        //    16: ldc             262144
        //    18: invokespecial   com/google/android/exoplayer/upstream/DefaultAllocator.<init>:(I)V
        //    21: invokespecial   com/google/android/exoplayer/DefaultLoadControl.<init>:(Lcom/google/android/exoplayer/upstream/Allocator;)V
        //    24: astore          10
        //    26: new             Lcom/google/android/exoplayer/upstream/DefaultBandwidthMeter;
        //    29: dup            
        //    30: invokespecial   com/google/android/exoplayer/upstream/DefaultBandwidthMeter.<init>:()V
        //    33: astore          11
        //    35: aconst_null    
        //    36: astore          9
        //    38: aload_1        
        //    39: instanceof      Lcom/google/android/exoplayer/hls/HlsMasterPlaylist;
        //    42: ifeq            96
        //    45: aload_1        
        //    46: checkcast       Lcom/google/android/exoplayer/hls/HlsMasterPlaylist;
        //    49: astore          8
        //    51: aload_0        
        //    52: getfield        com/crunchyroll/exoplayer/ExoPlayerView.mVideoQuality:Lcom/crunchyroll/exoplayer/VideoPlayer$VideoQuality;
        //    55: getstatic       com/crunchyroll/exoplayer/VideoPlayer$VideoQuality.QUALITY_ADAPTIVE:Lcom/crunchyroll/exoplayer/VideoPlayer$VideoQuality;
        //    58: if_acmpne       260
        //    61: aload_0        
        //    62: invokevirtual   com/crunchyroll/exoplayer/ExoPlayerView.getContext:()Landroid/content/Context;
        //    65: aload           8
        //    67: getfield        com/google/android/exoplayer/hls/HlsMasterPlaylist.variants:Ljava/util/List;
        //    70: aconst_null    
        //    71: iconst_0       
        //    72: invokestatic    com/google/android/exoplayer/chunk/VideoFormatSelectorUtil.selectVideoFormatsForDefaultDisplay:(Landroid/content/Context;Ljava/util/List;[Ljava/lang/String;Z)[I
        //    75: astore          8
        //    77: aload           8
        //    79: astore          9
        //    81: aload           8
        //    83: ifnull          96
        //    86: aload           8
        //    88: arraylength    
        //    89: ifeq            7
        //    92: aload           8
        //    94: astore          9
        //    96: new             Lcom/google/android/exoplayer/hls/HlsChunkSource;
        //    99: dup            
        //   100: new             Lcom/google/android/exoplayer/upstream/DefaultUriDataSource;
        //   103: dup            
        //   104: aload_0        
        //   105: invokevirtual   com/crunchyroll/exoplayer/ExoPlayerView.getContext:()Landroid/content/Context;
        //   108: aload           11
        //   110: aload_0        
        //   111: getfield        com/crunchyroll/exoplayer/ExoPlayerView.mUserAgent:Ljava/lang/String;
        //   114: invokespecial   com/google/android/exoplayer/upstream/DefaultUriDataSource.<init>:(Landroid/content/Context;Lcom/google/android/exoplayer/upstream/TransferListener;Ljava/lang/String;)V
        //   117: aload_0        
        //   118: invokespecial   com/crunchyroll/exoplayer/ExoPlayerView.getCurrentVideoUrl:()Ljava/lang/String;
        //   121: aload_1        
        //   122: aload           11
        //   124: aload           9
        //   126: iconst_1       
        //   127: aload_0        
        //   128: getfield        com/crunchyroll/exoplayer/ExoPlayerView.mMinBufferToSwitchUpMs:I
        //   131: i2l            
        //   132: aload_0        
        //   133: getfield        com/crunchyroll/exoplayer/ExoPlayerView.mMinBufferToSwitchDownMs:I
        //   136: i2l            
        //   137: invokespecial   com/google/android/exoplayer/hls/HlsChunkSource.<init>:(Lcom/google/android/exoplayer/upstream/DataSource;Ljava/lang/String;Lcom/google/android/exoplayer/hls/HlsPlaylist;Lcom/google/android/exoplayer/upstream/BandwidthMeter;[IIJJ)V
        //   140: astore_1       
        //   141: aload           9
        //   143: ifnull          405
        //   146: ldc_w           Lcom/google/android/exoplayer/hls/HlsChunkSource;.class
        //   149: ldc_w           "variants"
        //   152: invokevirtual   java/lang/Class.getDeclaredField:(Ljava/lang/String;)Ljava/lang/reflect/Field;
        //   155: astore          8
        //   157: aload           8
        //   159: iconst_1       
        //   160: invokevirtual   java/lang/reflect/Field.setAccessible:(Z)V
        //   163: aload           8
        //   165: aload_1        
        //   166: invokevirtual   java/lang/reflect/Field.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   169: checkcast       [Lcom/google/android/exoplayer/hls/Variant;
        //   172: checkcast       [Lcom/google/android/exoplayer/hls/Variant;
        //   175: astore          8
        //   177: iconst_0       
        //   178: istore_3       
        //   179: ldc_w           2147483647
        //   182: istore          4
        //   184: iconst_0       
        //   185: istore_2       
        //   186: iload_2        
        //   187: aload           8
        //   189: arraylength    
        //   190: if_icmpge       378
        //   193: aload           8
        //   195: iload_2        
        //   196: aaload         
        //   197: invokevirtual   com/google/android/exoplayer/hls/Variant.getFormat:()Lcom/google/android/exoplayer/chunk/Format;
        //   200: getfield        com/google/android/exoplayer/chunk/Format.height:I
        //   203: istore          5
        //   205: iload           5
        //   207: sipush          480
        //   210: isub           
        //   211: istore          7
        //   213: iload           4
        //   215: istore          6
        //   217: iload_3        
        //   218: istore          5
        //   220: iload           7
        //   222: iflt            246
        //   225: iload           4
        //   227: istore          6
        //   229: iload_3        
        //   230: istore          5
        //   232: iload           7
        //   234: iload           4
        //   236: if_icmpge       246
        //   239: iload           7
        //   241: istore          6
        //   243: iload_2        
        //   244: istore          5
        //   246: iload_2        
        //   247: iconst_1       
        //   248: iadd           
        //   249: istore_2       
        //   250: iload           6
        //   252: istore          4
        //   254: iload           5
        //   256: istore_3       
        //   257: goto            186
        //   260: aload_0        
        //   261: getfield        com/crunchyroll/exoplayer/ExoPlayerView.mVideoQuality:Lcom/crunchyroll/exoplayer/VideoPlayer$VideoQuality;
        //   264: getstatic       com/crunchyroll/exoplayer/VideoPlayer$VideoQuality.QUALITY_HIGH:Lcom/crunchyroll/exoplayer/VideoPlayer$VideoQuality;
        //   267: if_acmpne       353
        //   270: aload_0        
        //   271: invokevirtual   com/crunchyroll/exoplayer/ExoPlayerView.getContext:()Landroid/content/Context;
        //   274: aload           8
        //   276: getfield        com/google/android/exoplayer/hls/HlsMasterPlaylist.variants:Ljava/util/List;
        //   279: aconst_null    
        //   280: iconst_1       
        //   281: invokestatic    com/google/android/exoplayer/chunk/VideoFormatSelectorUtil.selectVideoFormatsForDefaultDisplay:(Landroid/content/Context;Ljava/util/List;[Ljava/lang/String;Z)[I
        //   284: astore          9
        //   286: new             Ljava/util/ArrayList;
        //   289: dup            
        //   290: invokespecial   java/util/ArrayList.<init>:()V
        //   293: astore          12
        //   295: iconst_0       
        //   296: istore_2       
        //   297: iload_2        
        //   298: aload           8
        //   300: getfield        com/google/android/exoplayer/hls/HlsMasterPlaylist.variants:Ljava/util/List;
        //   303: invokeinterface java/util/List.size:()I
        //   308: if_icmpge       343
        //   311: iconst_0       
        //   312: istore_3       
        //   313: iload_3        
        //   314: aload           9
        //   316: arraylength    
        //   317: if_icmpge       581
        //   320: iload_2        
        //   321: aload           9
        //   323: iload_3        
        //   324: iaload         
        //   325: if_icmpne       588
        //   328: aload           12
        //   330: iload_2        
        //   331: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   334: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   339: pop            
        //   340: goto            581
        //   343: aload           12
        //   345: invokestatic    com/google/android/exoplayer/util/Util.toArray:(Ljava/util/List;)[I
        //   348: astore          8
        //   350: goto            77
        //   353: aload_0        
        //   354: invokevirtual   com/crunchyroll/exoplayer/ExoPlayerView.getContext:()Landroid/content/Context;
        //   357: aload           8
        //   359: getfield        com/google/android/exoplayer/hls/HlsMasterPlaylist.variants:Ljava/util/List;
        //   362: aconst_null    
        //   363: iconst_1       
        //   364: invokestatic    com/google/android/exoplayer/chunk/VideoFormatSelectorUtil.selectVideoFormatsForDefaultDisplay:(Landroid/content/Context;Ljava/util/List;[Ljava/lang/String;Z)[I
        //   367: astore          8
        //   369: goto            77
        //   372: astore_1       
        //   373: aload_1        
        //   374: invokevirtual   com/google/android/exoplayer/MediaCodecUtil$DecoderQueryException.printStackTrace:()V
        //   377: return         
        //   378: ldc_w           Lcom/google/android/exoplayer/hls/HlsChunkSource;.class
        //   381: ldc_w           "selectedVariantIndex"
        //   384: invokevirtual   java/lang/Class.getDeclaredField:(Ljava/lang/String;)Ljava/lang/reflect/Field;
        //   387: astore          8
        //   389: aload           8
        //   391: iconst_1       
        //   392: invokevirtual   java/lang/reflect/Field.setAccessible:(Z)V
        //   395: aload           8
        //   397: aload_1        
        //   398: iload_3        
        //   399: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   402: invokevirtual   java/lang/reflect/Field.set:(Ljava/lang/Object;Ljava/lang/Object;)V
        //   405: new             Lcom/google/android/exoplayer/hls/HlsSampleSource;
        //   408: dup            
        //   409: aload_1        
        //   410: aload           10
        //   412: ldc_w           16777216
        //   415: invokespecial   com/google/android/exoplayer/hls/HlsSampleSource.<init>:(Lcom/google/android/exoplayer/hls/HlsChunkSource;Lcom/google/android/exoplayer/LoadControl;I)V
        //   418: astore_1       
        //   419: aload_0        
        //   420: new             Lcom/crunchyroll/exoplayer/FixedMediaCodecVideoTrackRenderer;
        //   423: dup            
        //   424: aload_1        
        //   425: iconst_1       
        //   426: ldc2_w          50
        //   429: aload_0        
        //   430: invokespecial   com/crunchyroll/exoplayer/FixedMediaCodecVideoTrackRenderer.<init>:(Lcom/google/android/exoplayer/SampleSource;IJLcom/crunchyroll/exoplayer/FixedMediaCodecVideoTrackRenderer$VideoSizeChangeListener;)V
        //   433: putfield        com/crunchyroll/exoplayer/ExoPlayerView.mVideoRenderer:Lcom/google/android/exoplayer/MediaCodecVideoTrackRenderer;
        //   436: aload_0        
        //   437: new             Lcom/google/android/exoplayer/MediaCodecAudioTrackRenderer;
        //   440: dup            
        //   441: aload_1        
        //   442: invokespecial   com/google/android/exoplayer/MediaCodecAudioTrackRenderer.<init>:(Lcom/google/android/exoplayer/SampleSource;)V
        //   445: putfield        com/crunchyroll/exoplayer/ExoPlayerView.mAudioRenderer:Lcom/google/android/exoplayer/MediaCodecAudioTrackRenderer;
        //   448: aload_0        
        //   449: getfield        com/crunchyroll/exoplayer/ExoPlayerView.mExoPlayer:Lcom/google/android/exoplayer/ExoPlayer;
        //   452: ifnonnull       481
        //   455: aload_0        
        //   456: iconst_2       
        //   457: aload_0        
        //   458: getfield        com/crunchyroll/exoplayer/ExoPlayerView.mMinBufferMs:I
        //   461: aload_0        
        //   462: getfield        com/crunchyroll/exoplayer/ExoPlayerView.mMinRebufferMs:I
        //   465: invokestatic    com/google/android/exoplayer/ExoPlayer$Factory.newInstance:(III)Lcom/google/android/exoplayer/ExoPlayer;
        //   468: putfield        com/crunchyroll/exoplayer/ExoPlayerView.mExoPlayer:Lcom/google/android/exoplayer/ExoPlayer;
        //   471: aload_0        
        //   472: getfield        com/crunchyroll/exoplayer/ExoPlayerView.mExoPlayer:Lcom/google/android/exoplayer/ExoPlayer;
        //   475: aload_0        
        //   476: invokeinterface com/google/android/exoplayer/ExoPlayer.addListener:(Lcom/google/android/exoplayer/ExoPlayer$Listener;)V
        //   481: aload_0        
        //   482: getfield        com/crunchyroll/exoplayer/ExoPlayerView.mExoPlayer:Lcom/google/android/exoplayer/ExoPlayer;
        //   485: iconst_2       
        //   486: anewarray       Lcom/google/android/exoplayer/TrackRenderer;
        //   489: dup            
        //   490: iconst_0       
        //   491: aload_0        
        //   492: getfield        com/crunchyroll/exoplayer/ExoPlayerView.mVideoRenderer:Lcom/google/android/exoplayer/MediaCodecVideoTrackRenderer;
        //   495: aastore        
        //   496: dup            
        //   497: iconst_1       
        //   498: aload_0        
        //   499: getfield        com/crunchyroll/exoplayer/ExoPlayerView.mAudioRenderer:Lcom/google/android/exoplayer/MediaCodecAudioTrackRenderer;
        //   502: aastore        
        //   503: invokeinterface com/google/android/exoplayer/ExoPlayer.prepare:([Lcom/google/android/exoplayer/TrackRenderer;)V
        //   508: aload_0        
        //   509: aload_0        
        //   510: getfield        com/crunchyroll/exoplayer/ExoPlayerView.mPlayerPosition:J
        //   513: invokevirtual   com/crunchyroll/exoplayer/ExoPlayerView.seekTo:(J)V
        //   516: aload_0        
        //   517: getfield        com/crunchyroll/exoplayer/ExoPlayerView.mController:Lcom/crunchyroll/exoplayer/VideoController;
        //   520: ifnull          527
        //   523: aload_0        
        //   524: invokespecial   com/crunchyroll/exoplayer/ExoPlayerView.updateVideoProgress:()V
        //   527: aload_0        
        //   528: getfield        com/crunchyroll/exoplayer/ExoPlayerView.mExoPlayer:Lcom/google/android/exoplayer/ExoPlayer;
        //   531: aload_0        
        //   532: getfield        com/crunchyroll/exoplayer/ExoPlayerView.mVideoRenderer:Lcom/google/android/exoplayer/MediaCodecVideoTrackRenderer;
        //   535: iconst_1       
        //   536: aload_0        
        //   537: invokevirtual   com/crunchyroll/exoplayer/ExoPlayerView.getHolder:()Landroid/view/SurfaceHolder;
        //   540: invokeinterface android/view/SurfaceHolder.getSurface:()Landroid/view/Surface;
        //   545: invokeinterface com/google/android/exoplayer/ExoPlayer.sendMessage:(Lcom/google/android/exoplayer/ExoPlayer$ExoPlayerComponent;ILjava/lang/Object;)V
        //   550: aload_0        
        //   551: getfield        com/crunchyroll/exoplayer/ExoPlayerView.mExoPlayer:Lcom/google/android/exoplayer/ExoPlayer;
        //   554: iconst_1       
        //   555: invokeinterface com/google/android/exoplayer/ExoPlayer.setPlayWhenReady:(Z)V
        //   560: return         
        //   561: astore          8
        //   563: aload           8
        //   565: invokevirtual   java/lang/NoSuchFieldException.printStackTrace:()V
        //   568: goto            405
        //   571: astore          8
        //   573: aload           8
        //   575: invokevirtual   java/lang/IllegalAccessException.printStackTrace:()V
        //   578: goto            405
        //   581: iload_2        
        //   582: iconst_1       
        //   583: iadd           
        //   584: istore_2       
        //   585: goto            297
        //   588: iload_3        
        //   589: iconst_1       
        //   590: iadd           
        //   591: istore_3       
        //   592: goto            313
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                               
        //  -----  -----  -----  -----  -------------------------------------------------------------------
        //  51     77     372    378    Lcom/google/android/exoplayer/MediaCodecUtil$DecoderQueryException;
        //  146    177    561    571    Ljava/lang/NoSuchFieldException;
        //  146    177    571    581    Ljava/lang/IllegalAccessException;
        //  186    205    561    571    Ljava/lang/NoSuchFieldException;
        //  186    205    571    581    Ljava/lang/IllegalAccessException;
        //  260    295    372    378    Lcom/google/android/exoplayer/MediaCodecUtil$DecoderQueryException;
        //  297    311    372    378    Lcom/google/android/exoplayer/MediaCodecUtil$DecoderQueryException;
        //  313    320    372    378    Lcom/google/android/exoplayer/MediaCodecUtil$DecoderQueryException;
        //  328    340    372    378    Lcom/google/android/exoplayer/MediaCodecUtil$DecoderQueryException;
        //  343    350    372    378    Lcom/google/android/exoplayer/MediaCodecUtil$DecoderQueryException;
        //  353    369    372    378    Lcom/google/android/exoplayer/MediaCodecUtil$DecoderQueryException;
        //  378    405    561    571    Ljava/lang/NoSuchFieldException;
        //  378    405    571    581    Ljava/lang/IllegalAccessException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0186:
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
    
    public void onSingleManifestError(final IOException ex) {
        if (this.mErrorListener != null) {
            this.mErrorListener.onError(ex);
        }
    }
    
    public void onStartSeekScrub() {
    }
    
    public void onStopPressed() {
        this.stop();
    }
    
    public void onStopSeekScrub(final int n) {
        this.seekTo(n);
    }
    
    public void onVideoSizeChanged(final int n, final int n2, final float n3) {
        if (this.getContext() != null) {
            ((Activity)this.getContext()).runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    ExoPlayerView.this.mAspectFrameLayout.setAspectRatio(n3);
                    final Iterator<PlayerEventListener> iterator = ExoPlayerView.this.mEventListeners.iterator();
                    while (iterator.hasNext()) {
                        iterator.next().onVideoSizeChanged(n, n2);
                    }
                }
            });
        }
    }
    
    public void pause() {
        if (this.mExoPlayer != null) {
            this.mExoPlayer.setPlayWhenReady(false);
        }
        if (this.mController != null) {
            this.mController.showPlayButton();
        }
        final Iterator<PlayerEventListener> iterator = this.mEventListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onPause();
        }
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object)null);
        }
    }
    
    public void play() {
        if (this.mExoPlayer != null) {
            this.mExoPlayer.setPlayWhenReady(true);
        }
        if (this.mController != null) {
            this.mController.showPauseButton();
        }
        final Iterator<PlayerEventListener> iterator = this.mEventListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onPlay();
        }
        this.updateVideoProgress();
    }
    
    public void playNext() {
        if (!this.isPlayingLastVideo()) {
            ++this.mCurrentVideoIndex;
            this.mPlayerPosition = 0L;
            this.resetPlayer();
        }
        final Iterator<PlayerEventListener> iterator = this.mEventListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onPlayNext();
        }
    }
    
    public void release() {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object)null);
        }
        if (this.mExoPlayer != null) {
            this.mPlayerPosition = this.mExoPlayer.getCurrentPosition();
            this.mExoPlayer.removeListener((ExoPlayer.Listener)this);
            this.mExoPlayer.release();
        }
        this.mHandler = null;
        this.mExoPlayer = null;
    }
    
    public void seekTo(final long n) {
        if (this.mExoPlayer != null) {
            this.mExoPlayer.seekTo(n);
        }
    }
    
    public void setBufferLengths(final int mMinBufferMs, final int mMinRebufferMs, final int n, final int mMinBufferToSwitchDownMs) {
        this.mMinBufferMs = mMinBufferMs;
        this.mMinRebufferMs = mMinRebufferMs;
        this.mMinBufferToSwitchDownMs = mMinBufferToSwitchDownMs;
    }
    
    public void setErrorListener(final ErrorListener mErrorListener) {
        this.mErrorListener = mErrorListener;
    }
    
    public void setPlayhead(final long mPlayerPosition) {
        this.mPlayerPosition = mPlayerPosition;
    }
    
    public void setStateChangeListener(final StateChangeListener mStateListener) {
        this.mStateListener = mStateListener;
    }
    
    public void setVideoController(final VideoController mController) {
        (this.mController = mController).addVideoControllerListener(this);
    }
    
    public void setVideoQuality(final VideoQuality mVideoQuality) {
        this.mVideoQuality = mVideoQuality;
    }
    
    public void stop() {
        this.mStopped = true;
        if (this.mExoPlayer != null) {
            this.mExoPlayer.stop();
            this.release();
            this.mPlayerPosition = 0L;
        }
        if (this.mController != null) {
            this.mController.removeListeners();
        }
        final Iterator<PlayerEventListener> iterator = this.mEventListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onStop();
        }
        this.mEventListeners.clear();
    }
}
