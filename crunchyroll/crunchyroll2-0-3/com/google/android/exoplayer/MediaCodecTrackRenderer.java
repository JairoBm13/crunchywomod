// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer;

import android.media.MediaCodec$CodecException;
import com.google.android.exoplayer.util.TraceUtil;
import android.view.Surface;
import android.media.MediaCrypto;
import android.os.SystemClock;
import android.media.MediaCodec$CryptoInfo;
import android.media.MediaCodec$CryptoException;
import java.util.ArrayList;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Util;
import android.media.MediaCodec$BufferInfo;
import java.nio.ByteBuffer;
import android.os.Handler;
import com.google.android.exoplayer.drm.DrmSessionManager;
import com.google.android.exoplayer.drm.DrmInitData;
import java.util.List;
import android.media.MediaCodec;
import android.annotation.TargetApi;

@TargetApi(16)
public abstract class MediaCodecTrackRenderer extends SampleSourceTrackRenderer
{
    private static final long MAX_CODEC_HOTSWAP_TIME_MS = 1000L;
    private static final int RECONFIGURATION_STATE_NONE = 0;
    private static final int RECONFIGURATION_STATE_QUEUE_PENDING = 2;
    private static final int RECONFIGURATION_STATE_WRITE_PENDING = 1;
    private static final int REINITIALIZATION_STATE_NONE = 0;
    private static final int REINITIALIZATION_STATE_SIGNAL_END_OF_STREAM = 1;
    private static final int REINITIALIZATION_STATE_WAIT_END_OF_STREAM = 2;
    protected static final int SOURCE_STATE_NOT_READY = 0;
    protected static final int SOURCE_STATE_READY = 1;
    protected static final int SOURCE_STATE_READY_READ_MAY_FAIL = 2;
    private MediaCodec codec;
    public final CodecCounters codecCounters;
    private boolean codecHasQueuedBuffers;
    private long codecHotswapTimeMs;
    private boolean codecIsAdaptive;
    private boolean codecNeedsEosFlushWorkaround;
    private boolean codecNeedsEosPropagationWorkaround;
    private boolean codecReceivedEos;
    private int codecReconfigurationState;
    private boolean codecReconfigured;
    private int codecReinitializationState;
    private final List<Long> decodeOnlyPresentationTimestamps;
    private DrmInitData drmInitData;
    private final DrmSessionManager drmSessionManager;
    protected final Handler eventHandler;
    private final EventListener eventListener;
    private MediaFormat format;
    private final MediaFormatHolder formatHolder;
    private ByteBuffer[] inputBuffers;
    private int inputIndex;
    private boolean inputStreamEnded;
    private boolean openedDrmSession;
    private final MediaCodec$BufferInfo outputBufferInfo;
    private ByteBuffer[] outputBuffers;
    private int outputIndex;
    private boolean outputStreamEnded;
    private final boolean playClearSamplesWithoutKeys;
    private final SampleHolder sampleHolder;
    private int sourceState;
    private boolean waitingForFirstSyncFrame;
    private boolean waitingForKeys;
    
    public MediaCodecTrackRenderer(final SampleSource sampleSource, final DrmSessionManager drmSessionManager, final boolean playClearSamplesWithoutKeys, final Handler eventHandler, final EventListener eventListener) {
        boolean b = true;
        super(new SampleSource[] { sampleSource });
        if (Util.SDK_INT < 16) {
            b = false;
        }
        Assertions.checkState(b);
        this.drmSessionManager = drmSessionManager;
        this.playClearSamplesWithoutKeys = playClearSamplesWithoutKeys;
        this.eventHandler = eventHandler;
        this.eventListener = eventListener;
        this.codecCounters = new CodecCounters();
        this.sampleHolder = new SampleHolder(0);
        this.formatHolder = new MediaFormatHolder();
        this.decodeOnlyPresentationTimestamps = new ArrayList<Long>();
        this.outputBufferInfo = new MediaCodec$BufferInfo();
        this.codecReconfigurationState = 0;
        this.codecReinitializationState = 0;
    }
    
    private void checkForDiscontinuity(final long n) throws ExoPlaybackException {
        if (this.codec != null && this.readSource(n, this.formatHolder, this.sampleHolder, true) == -5) {
            this.flushCodec();
        }
    }
    
    private static boolean codecNeedsEosFlushWorkaround(final String s) {
        return Util.SDK_INT <= 23 && "OMX.google.vorbis.decoder".equals(s);
    }
    
    private static boolean codecNeedsEosPropagationWorkaround(final String s) {
        return Util.SDK_INT <= 17 && "OMX.rk.video_decoder.avc".equals(s) && ("ht7s3".equals(Util.DEVICE) || "rk30sdk".equals(Util.DEVICE) || "rk31sdk".equals(Util.DEVICE));
    }
    
    private boolean drainOutputBuffer(final long n, final long n2) throws ExoPlaybackException {
        if (this.outputStreamEnded) {
            return false;
        }
        if (this.outputIndex < 0) {
            this.outputIndex = this.codec.dequeueOutputBuffer(this.outputBufferInfo, this.getDequeueOutputBufferTimeoutUs());
        }
        if (this.outputIndex == -2) {
            this.onOutputFormatChanged(this.codec.getOutputFormat());
            final CodecCounters codecCounters = this.codecCounters;
            ++codecCounters.outputFormatChangedCount;
            return true;
        }
        if (this.outputIndex == -3) {
            this.outputBuffers = this.codec.getOutputBuffers();
            final CodecCounters codecCounters2 = this.codecCounters;
            ++codecCounters2.outputBuffersChangedCount;
            return true;
        }
        if (this.outputIndex < 0) {
            if (this.codecNeedsEosPropagationWorkaround && (this.inputStreamEnded || this.codecReinitializationState == 2)) {
                this.processEndOfStream();
                return true;
            }
            return false;
        }
        else {
            if ((this.outputBufferInfo.flags & 0x4) != 0x0) {
                this.processEndOfStream();
                return false;
            }
            final int decodeOnlyIndex = this.getDecodeOnlyIndex(this.outputBufferInfo.presentationTimeUs);
            if (this.processOutputBuffer(n, n2, this.codec, this.outputBuffers[this.outputIndex], this.outputBufferInfo, this.outputIndex, decodeOnlyIndex != -1)) {
                if (decodeOnlyIndex != -1) {
                    this.decodeOnlyPresentationTimestamps.remove(decodeOnlyIndex);
                }
                this.outputIndex = -1;
                return true;
            }
            return false;
        }
    }
    
    private boolean feedInputBuffer(long timeUs, final boolean b) throws ExoPlaybackException {
        if (this.inputStreamEnded || this.codecReinitializationState == 2) {
            return false;
        }
        if (this.inputIndex < 0) {
            this.inputIndex = this.codec.dequeueInputBuffer(0L);
            if (this.inputIndex < 0) {
                return false;
            }
            this.sampleHolder.data = this.inputBuffers[this.inputIndex];
            this.sampleHolder.clearData();
        }
        if (this.codecReinitializationState == 1) {
            if (!this.codecNeedsEosPropagationWorkaround) {
                this.codecReceivedEos = true;
                this.codec.queueInputBuffer(this.inputIndex, 0, 0, 0L, 4);
                this.inputIndex = -1;
            }
            this.codecReinitializationState = 2;
            return false;
        }
        int source;
        if (this.waitingForKeys) {
            source = -3;
        }
        else {
            if (this.codecReconfigurationState == 1) {
                for (int i = 0; i < this.format.initializationData.size(); ++i) {
                    this.sampleHolder.data.put(this.format.initializationData.get(i));
                }
                this.codecReconfigurationState = 2;
            }
            final int n = source = this.readSource(timeUs, this.formatHolder, this.sampleHolder, 0 != 0);
            if (b) {
                source = n;
                if (this.sourceState == 1 && (source = n) == -2) {
                    this.sourceState = 2;
                    source = n;
                }
            }
        }
        if (source == -2) {
            return false;
        }
        if (source == -5) {
            this.flushCodec();
            return true;
        }
        if (source == -4) {
            if (this.codecReconfigurationState == 2) {
                this.sampleHolder.clearData();
                this.codecReconfigurationState = 1;
            }
            this.onInputFormatChanged(this.formatHolder);
            return true;
        }
        if (source == -1) {
            if (this.codecReconfigurationState == 2) {
                this.sampleHolder.clearData();
                this.codecReconfigurationState = 1;
            }
            this.inputStreamEnded = true;
            if (!this.codecHasQueuedBuffers) {
                this.processEndOfStream();
                return false;
            }
            try {
                if (this.codecNeedsEosPropagationWorkaround) {
                    return false;
                }
                this.codecReceivedEos = true;
                this.codec.queueInputBuffer(this.inputIndex, 0, 0, 0L, 4);
                this.inputIndex = -1;
                return false;
            }
            catch (MediaCodec$CryptoException ex) {
                this.notifyCryptoError(ex);
                throw new ExoPlaybackException((Throwable)ex);
            }
        }
        if (this.waitingForFirstSyncFrame) {
            if (!this.sampleHolder.isSyncFrame()) {
                this.sampleHolder.clearData();
                if (this.codecReconfigurationState == 2) {
                    this.codecReconfigurationState = 1;
                }
                return true;
            }
            this.waitingForFirstSyncFrame = false;
        }
        final boolean encrypted = this.sampleHolder.isEncrypted();
        this.waitingForKeys = this.shouldWaitForKeys(encrypted);
        if (this.waitingForKeys) {
            return false;
        }
        try {
            final int position = this.sampleHolder.data.position();
            final int size = this.sampleHolder.size;
            timeUs = this.sampleHolder.timeUs;
            if (this.sampleHolder.isDecodeOnly()) {
                this.decodeOnlyPresentationTimestamps.add(timeUs);
            }
            if (encrypted) {
                this.codec.queueSecureInputBuffer(this.inputIndex, 0, getFrameworkCryptoInfo(this.sampleHolder, position - size), timeUs, 0);
            }
            else {
                this.codec.queueInputBuffer(this.inputIndex, 0, position, timeUs, 0);
            }
            this.inputIndex = -1;
            this.codecHasQueuedBuffers = true;
            this.codecReconfigurationState = 0;
            return true;
        }
        catch (MediaCodec$CryptoException ex2) {
            this.notifyCryptoError(ex2);
            throw new ExoPlaybackException((Throwable)ex2);
        }
        return false;
    }
    
    private void flushCodec() throws ExoPlaybackException {
        this.codecHotswapTimeMs = -1L;
        this.inputIndex = -1;
        this.outputIndex = -1;
        this.waitingForFirstSyncFrame = true;
        this.waitingForKeys = false;
        this.decodeOnlyPresentationTimestamps.clear();
        if (Util.SDK_INT < 18 || (this.codecNeedsEosFlushWorkaround && this.codecReceivedEos)) {
            this.releaseCodec();
            this.maybeInitCodec();
        }
        else if (this.codecReinitializationState != 0) {
            this.releaseCodec();
            this.maybeInitCodec();
        }
        else {
            this.codec.flush();
            this.codecHasQueuedBuffers = false;
        }
        if (this.codecReconfigured && this.format != null) {
            this.codecReconfigurationState = 1;
        }
    }
    
    private int getDecodeOnlyIndex(final long n) {
        for (int size = this.decodeOnlyPresentationTimestamps.size(), i = 0; i < size; ++i) {
            if (this.decodeOnlyPresentationTimestamps.get(i) == n) {
                return i;
            }
        }
        return -1;
    }
    
    private static MediaCodec$CryptoInfo getFrameworkCryptoInfo(final SampleHolder sampleHolder, final int n) {
        final MediaCodec$CryptoInfo frameworkCryptoInfoV16 = sampleHolder.cryptoInfo.getFrameworkCryptoInfoV16();
        if (n == 0) {
            return frameworkCryptoInfoV16;
        }
        if (frameworkCryptoInfoV16.numBytesOfClearData == null) {
            frameworkCryptoInfoV16.numBytesOfClearData = new int[1];
        }
        final int[] numBytesOfClearData = frameworkCryptoInfoV16.numBytesOfClearData;
        numBytesOfClearData[0] += n;
        return frameworkCryptoInfoV16;
    }
    
    private boolean isWithinHotswapPeriod() {
        return SystemClock.elapsedRealtime() < this.codecHotswapTimeMs + 1000L;
    }
    
    private void notifyAndThrowDecoderInitError(final DecoderInitializationException ex) throws ExoPlaybackException {
        this.notifyDecoderInitializationError(ex);
        throw new ExoPlaybackException(ex);
    }
    
    private void notifyCryptoError(final MediaCodec$CryptoException ex) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    MediaCodecTrackRenderer.this.eventListener.onCryptoError(ex);
                }
            });
        }
    }
    
    private void notifyDecoderInitializationError(final DecoderInitializationException ex) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    MediaCodecTrackRenderer.this.eventListener.onDecoderInitializationError(ex);
                }
            });
        }
    }
    
    private void notifyDecoderInitialized(final String s, final long n, final long n2) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    MediaCodecTrackRenderer.this.eventListener.onDecoderInitialized(s, n, n2);
                }
            });
        }
    }
    
    private void processEndOfStream() throws ExoPlaybackException {
        if (this.codecReinitializationState == 2) {
            this.releaseCodec();
            this.maybeInitCodec();
            return;
        }
        this.outputStreamEnded = true;
        this.onOutputStreamEnded();
    }
    
    private void readFormat(final long n) throws ExoPlaybackException {
        if (this.readSource(n, this.formatHolder, this.sampleHolder, false) == -4) {
            this.onInputFormatChanged(this.formatHolder);
        }
    }
    
    private void seekToInternal() {
        this.sourceState = 0;
        this.inputStreamEnded = false;
        this.outputStreamEnded = false;
    }
    
    private boolean shouldWaitForKeys(final boolean b) throws ExoPlaybackException {
        if (this.openedDrmSession) {
            final int state = this.drmSessionManager.getState();
            if (state == 0) {
                throw new ExoPlaybackException(this.drmSessionManager.getError());
            }
            if (state != 4 && (b || !this.playClearSamplesWithoutKeys)) {
                return true;
            }
        }
        return false;
    }
    
    protected boolean canReconfigureCodec(final MediaCodec mediaCodec, final boolean b, final MediaFormat mediaFormat, final MediaFormat mediaFormat2) {
        return false;
    }
    
    protected final boolean codecInitialized() {
        return this.codec != null;
    }
    
    protected void configureCodec(final MediaCodec mediaCodec, final String s, final boolean b, final android.media.MediaFormat mediaFormat, final MediaCrypto mediaCrypto) {
        mediaCodec.configure(mediaFormat, (Surface)null, mediaCrypto, 0);
    }
    
    @Override
    protected void doSomeWork(final long n, final long n2) throws ExoPlaybackException {
        int sourceState;
        if (this.continueBufferingSource(n)) {
            if (this.sourceState == 0) {
                sourceState = 1;
            }
            else {
                sourceState = this.sourceState;
            }
        }
        else {
            sourceState = 0;
        }
        this.sourceState = sourceState;
        this.checkForDiscontinuity(n);
        if (this.format == null) {
            this.readFormat(n);
        }
        if (this.codec == null && this.shouldInitCodec()) {
            this.maybeInitCodec();
        }
        if (this.codec != null) {
            TraceUtil.beginSection("drainAndFeed");
            while (this.drainOutputBuffer(n, n2)) {}
            if (this.feedInputBuffer(n, true)) {
                while (this.feedInputBuffer(n, false)) {}
            }
            TraceUtil.endSection();
        }
        this.codecCounters.ensureUpdated();
    }
    
    protected DecoderInfo getDecoderInfo(final String s, final boolean b) throws MediaCodecUtil.DecoderQueryException {
        return MediaCodecUtil.getDecoderInfo(s, b);
    }
    
    protected long getDequeueOutputBufferTimeoutUs() {
        return 0L;
    }
    
    protected final int getSourceState() {
        return this.sourceState;
    }
    
    protected final boolean haveFormat() {
        return this.format != null;
    }
    
    @Override
    protected boolean isEnded() {
        return this.outputStreamEnded;
    }
    
    @Override
    protected boolean isReady() {
        return this.format != null && !this.waitingForKeys && (this.sourceState != 0 || this.outputIndex >= 0 || this.isWithinHotswapPeriod());
    }
    
    protected final void maybeInitCodec() throws ExoPlaybackException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: invokevirtual   com/google/android/exoplayer/MediaCodecTrackRenderer.shouldInitCodec:()Z
        //     4: ifne            8
        //     7: return         
        //     8: aload_0        
        //     9: getfield        com/google/android/exoplayer/MediaCodecTrackRenderer.format:Lcom/google/android/exoplayer/MediaFormat;
        //    12: getfield        com/google/android/exoplayer/MediaFormat.mimeType:Ljava/lang/String;
        //    15: astore          9
        //    17: aconst_null    
        //    18: astore          7
        //    20: iconst_0       
        //    21: istore_2       
        //    22: aload_0        
        //    23: getfield        com/google/android/exoplayer/MediaCodecTrackRenderer.drmInitData:Lcom/google/android/exoplayer/drm/DrmInitData;
        //    26: ifnull          136
        //    29: aload_0        
        //    30: getfield        com/google/android/exoplayer/MediaCodecTrackRenderer.drmSessionManager:Lcom/google/android/exoplayer/drm/DrmSessionManager;
        //    33: ifnonnull       47
        //    36: new             Lcom/google/android/exoplayer/ExoPlaybackException;
        //    39: dup            
        //    40: ldc_w           "Media requires a DrmSessionManager"
        //    43: invokespecial   com/google/android/exoplayer/ExoPlaybackException.<init>:(Ljava/lang/String;)V
        //    46: athrow         
        //    47: aload_0        
        //    48: getfield        com/google/android/exoplayer/MediaCodecTrackRenderer.openedDrmSession:Z
        //    51: ifne            72
        //    54: aload_0        
        //    55: getfield        com/google/android/exoplayer/MediaCodecTrackRenderer.drmSessionManager:Lcom/google/android/exoplayer/drm/DrmSessionManager;
        //    58: aload_0        
        //    59: getfield        com/google/android/exoplayer/MediaCodecTrackRenderer.drmInitData:Lcom/google/android/exoplayer/drm/DrmInitData;
        //    62: invokeinterface com/google/android/exoplayer/drm/DrmSessionManager.open:(Lcom/google/android/exoplayer/drm/DrmInitData;)V
        //    67: aload_0        
        //    68: iconst_1       
        //    69: putfield        com/google/android/exoplayer/MediaCodecTrackRenderer.openedDrmSession:Z
        //    72: aload_0        
        //    73: getfield        com/google/android/exoplayer/MediaCodecTrackRenderer.drmSessionManager:Lcom/google/android/exoplayer/drm/DrmSessionManager;
        //    76: invokeinterface com/google/android/exoplayer/drm/DrmSessionManager.getState:()I
        //    81: istore_1       
        //    82: iload_1        
        //    83: ifne            103
        //    86: new             Lcom/google/android/exoplayer/ExoPlaybackException;
        //    89: dup            
        //    90: aload_0        
        //    91: getfield        com/google/android/exoplayer/MediaCodecTrackRenderer.drmSessionManager:Lcom/google/android/exoplayer/drm/DrmSessionManager;
        //    94: invokeinterface com/google/android/exoplayer/drm/DrmSessionManager.getError:()Ljava/lang/Exception;
        //    99: invokespecial   com/google/android/exoplayer/ExoPlaybackException.<init>:(Ljava/lang/Throwable;)V
        //   102: athrow         
        //   103: iload_1        
        //   104: iconst_3       
        //   105: if_icmpeq       113
        //   108: iload_1        
        //   109: iconst_4       
        //   110: if_icmpne       7
        //   113: aload_0        
        //   114: getfield        com/google/android/exoplayer/MediaCodecTrackRenderer.drmSessionManager:Lcom/google/android/exoplayer/drm/DrmSessionManager;
        //   117: invokeinterface com/google/android/exoplayer/drm/DrmSessionManager.getMediaCrypto:()Landroid/media/MediaCrypto;
        //   122: astore          7
        //   124: aload_0        
        //   125: getfield        com/google/android/exoplayer/MediaCodecTrackRenderer.drmSessionManager:Lcom/google/android/exoplayer/drm/DrmSessionManager;
        //   128: aload           9
        //   130: invokeinterface com/google/android/exoplayer/drm/DrmSessionManager.requiresSecureDecoderComponent:(Ljava/lang/String;)Z
        //   135: istore_2       
        //   136: aconst_null    
        //   137: astore          8
        //   139: aload_0        
        //   140: aload           9
        //   142: iload_2        
        //   143: invokevirtual   com/google/android/exoplayer/MediaCodecTrackRenderer.getDecoderInfo:(Ljava/lang/String;Z)Lcom/google/android/exoplayer/DecoderInfo;
        //   146: astore          9
        //   148: aload           9
        //   150: astore          8
        //   152: aload           8
        //   154: ifnonnull       176
        //   157: aload_0        
        //   158: new             Lcom/google/android/exoplayer/MediaCodecTrackRenderer$DecoderInitializationException;
        //   161: dup            
        //   162: aload_0        
        //   163: getfield        com/google/android/exoplayer/MediaCodecTrackRenderer.format:Lcom/google/android/exoplayer/MediaFormat;
        //   166: aconst_null    
        //   167: ldc_w           -49999
        //   170: invokespecial   com/google/android/exoplayer/MediaCodecTrackRenderer$DecoderInitializationException.<init>:(Lcom/google/android/exoplayer/MediaFormat;Ljava/lang/Throwable;I)V
        //   173: invokespecial   com/google/android/exoplayer/MediaCodecTrackRenderer.notifyAndThrowDecoderInitError:(Lcom/google/android/exoplayer/MediaCodecTrackRenderer$DecoderInitializationException;)V
        //   176: aload           8
        //   178: getfield        com/google/android/exoplayer/DecoderInfo.name:Ljava/lang/String;
        //   181: astore          9
        //   183: aload_0        
        //   184: aload           8
        //   186: getfield        com/google/android/exoplayer/DecoderInfo.adaptive:Z
        //   189: putfield        com/google/android/exoplayer/MediaCodecTrackRenderer.codecIsAdaptive:Z
        //   192: aload_0        
        //   193: aload           9
        //   195: invokestatic    com/google/android/exoplayer/MediaCodecTrackRenderer.codecNeedsEosPropagationWorkaround:(Ljava/lang/String;)Z
        //   198: putfield        com/google/android/exoplayer/MediaCodecTrackRenderer.codecNeedsEosPropagationWorkaround:Z
        //   201: aload_0        
        //   202: aload           9
        //   204: invokestatic    com/google/android/exoplayer/MediaCodecTrackRenderer.codecNeedsEosFlushWorkaround:(Ljava/lang/String;)Z
        //   207: putfield        com/google/android/exoplayer/MediaCodecTrackRenderer.codecNeedsEosFlushWorkaround:Z
        //   210: invokestatic    android/os/SystemClock.elapsedRealtime:()J
        //   213: lstore_3       
        //   214: new             Ljava/lang/StringBuilder;
        //   217: dup            
        //   218: invokespecial   java/lang/StringBuilder.<init>:()V
        //   221: ldc_w           "createByCodecName("
        //   224: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   227: aload           9
        //   229: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   232: ldc_w           ")"
        //   235: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   238: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   241: invokestatic    com/google/android/exoplayer/util/TraceUtil.beginSection:(Ljava/lang/String;)V
        //   244: aload_0        
        //   245: aload           9
        //   247: invokestatic    android/media/MediaCodec.createByCodecName:(Ljava/lang/String;)Landroid/media/MediaCodec;
        //   250: putfield        com/google/android/exoplayer/MediaCodecTrackRenderer.codec:Landroid/media/MediaCodec;
        //   253: invokestatic    com/google/android/exoplayer/util/TraceUtil.endSection:()V
        //   256: ldc_w           "configureCodec"
        //   259: invokestatic    com/google/android/exoplayer/util/TraceUtil.beginSection:(Ljava/lang/String;)V
        //   262: aload_0        
        //   263: aload_0        
        //   264: getfield        com/google/android/exoplayer/MediaCodecTrackRenderer.codec:Landroid/media/MediaCodec;
        //   267: aload           9
        //   269: aload_0        
        //   270: getfield        com/google/android/exoplayer/MediaCodecTrackRenderer.codecIsAdaptive:Z
        //   273: aload_0        
        //   274: getfield        com/google/android/exoplayer/MediaCodecTrackRenderer.format:Lcom/google/android/exoplayer/MediaFormat;
        //   277: invokevirtual   com/google/android/exoplayer/MediaFormat.getFrameworkMediaFormatV16:()Landroid/media/MediaFormat;
        //   280: aload           7
        //   282: invokevirtual   com/google/android/exoplayer/MediaCodecTrackRenderer.configureCodec:(Landroid/media/MediaCodec;Ljava/lang/String;ZLandroid/media/MediaFormat;Landroid/media/MediaCrypto;)V
        //   285: invokestatic    com/google/android/exoplayer/util/TraceUtil.endSection:()V
        //   288: ldc_w           "codec.start()"
        //   291: invokestatic    com/google/android/exoplayer/util/TraceUtil.beginSection:(Ljava/lang/String;)V
        //   294: aload_0        
        //   295: getfield        com/google/android/exoplayer/MediaCodecTrackRenderer.codec:Landroid/media/MediaCodec;
        //   298: invokevirtual   android/media/MediaCodec.start:()V
        //   301: invokestatic    com/google/android/exoplayer/util/TraceUtil.endSection:()V
        //   304: invokestatic    android/os/SystemClock.elapsedRealtime:()J
        //   307: lstore          5
        //   309: aload_0        
        //   310: aload           9
        //   312: lload           5
        //   314: lload           5
        //   316: lload_3        
        //   317: lsub           
        //   318: invokespecial   com/google/android/exoplayer/MediaCodecTrackRenderer.notifyDecoderInitialized:(Ljava/lang/String;JJ)V
        //   321: aload_0        
        //   322: aload_0        
        //   323: getfield        com/google/android/exoplayer/MediaCodecTrackRenderer.codec:Landroid/media/MediaCodec;
        //   326: invokevirtual   android/media/MediaCodec.getInputBuffers:()[Ljava/nio/ByteBuffer;
        //   329: putfield        com/google/android/exoplayer/MediaCodecTrackRenderer.inputBuffers:[Ljava/nio/ByteBuffer;
        //   332: aload_0        
        //   333: aload_0        
        //   334: getfield        com/google/android/exoplayer/MediaCodecTrackRenderer.codec:Landroid/media/MediaCodec;
        //   337: invokevirtual   android/media/MediaCodec.getOutputBuffers:()[Ljava/nio/ByteBuffer;
        //   340: putfield        com/google/android/exoplayer/MediaCodecTrackRenderer.outputBuffers:[Ljava/nio/ByteBuffer;
        //   343: aload_0        
        //   344: invokevirtual   com/google/android/exoplayer/MediaCodecTrackRenderer.getState:()I
        //   347: iconst_3       
        //   348: if_icmpne       443
        //   351: invokestatic    android/os/SystemClock.elapsedRealtime:()J
        //   354: lstore_3       
        //   355: aload_0        
        //   356: lload_3        
        //   357: putfield        com/google/android/exoplayer/MediaCodecTrackRenderer.codecHotswapTimeMs:J
        //   360: aload_0        
        //   361: iconst_m1      
        //   362: putfield        com/google/android/exoplayer/MediaCodecTrackRenderer.inputIndex:I
        //   365: aload_0        
        //   366: iconst_m1      
        //   367: putfield        com/google/android/exoplayer/MediaCodecTrackRenderer.outputIndex:I
        //   370: aload_0        
        //   371: iconst_1       
        //   372: putfield        com/google/android/exoplayer/MediaCodecTrackRenderer.waitingForFirstSyncFrame:Z
        //   375: aload_0        
        //   376: getfield        com/google/android/exoplayer/MediaCodecTrackRenderer.codecCounters:Lcom/google/android/exoplayer/CodecCounters;
        //   379: astore          7
        //   381: aload           7
        //   383: aload           7
        //   385: getfield        com/google/android/exoplayer/CodecCounters.codecInitCount:I
        //   388: iconst_1       
        //   389: iadd           
        //   390: putfield        com/google/android/exoplayer/CodecCounters.codecInitCount:I
        //   393: return         
        //   394: astore          9
        //   396: aload_0        
        //   397: new             Lcom/google/android/exoplayer/MediaCodecTrackRenderer$DecoderInitializationException;
        //   400: dup            
        //   401: aload_0        
        //   402: getfield        com/google/android/exoplayer/MediaCodecTrackRenderer.format:Lcom/google/android/exoplayer/MediaFormat;
        //   405: aload           9
        //   407: ldc_w           -49998
        //   410: invokespecial   com/google/android/exoplayer/MediaCodecTrackRenderer$DecoderInitializationException.<init>:(Lcom/google/android/exoplayer/MediaFormat;Ljava/lang/Throwable;I)V
        //   413: invokespecial   com/google/android/exoplayer/MediaCodecTrackRenderer.notifyAndThrowDecoderInitError:(Lcom/google/android/exoplayer/MediaCodecTrackRenderer$DecoderInitializationException;)V
        //   416: goto            152
        //   419: astore          7
        //   421: aload_0        
        //   422: new             Lcom/google/android/exoplayer/MediaCodecTrackRenderer$DecoderInitializationException;
        //   425: dup            
        //   426: aload_0        
        //   427: getfield        com/google/android/exoplayer/MediaCodecTrackRenderer.format:Lcom/google/android/exoplayer/MediaFormat;
        //   430: aload           7
        //   432: aload           9
        //   434: invokespecial   com/google/android/exoplayer/MediaCodecTrackRenderer$DecoderInitializationException.<init>:(Lcom/google/android/exoplayer/MediaFormat;Ljava/lang/Throwable;Ljava/lang/String;)V
        //   437: invokespecial   com/google/android/exoplayer/MediaCodecTrackRenderer.notifyAndThrowDecoderInitError:(Lcom/google/android/exoplayer/MediaCodecTrackRenderer$DecoderInitializationException;)V
        //   440: goto            343
        //   443: ldc2_w          -1
        //   446: lstore_3       
        //   447: goto            355
        //    Exceptions:
        //  throws com.google.android.exoplayer.ExoPlaybackException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                               
        //  -----  -----  -----  -----  -------------------------------------------------------------------
        //  139    148    394    419    Lcom/google/android/exoplayer/MediaCodecUtil$DecoderQueryException;
        //  210    343    419    443    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0343:
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
    protected void onDisabled() throws ExoPlaybackException {
        this.format = null;
        this.drmInitData = null;
        try {
            this.releaseCodec();
            try {
                if (this.openedDrmSession) {
                    this.drmSessionManager.close();
                    this.openedDrmSession = false;
                }
            }
            finally {
                super.onDisabled();
            }
        }
        finally {
            try {
                if (this.openedDrmSession) {
                    this.drmSessionManager.close();
                    this.openedDrmSession = false;
                }
                super.onDisabled();
            }
            finally {
                super.onDisabled();
            }
        }
    }
    
    @Override
    protected void onEnabled(final int n, final long n2, final boolean b) throws ExoPlaybackException {
        super.onEnabled(n, n2, b);
        this.seekToInternal();
    }
    
    protected void onInputFormatChanged(final MediaFormatHolder mediaFormatHolder) throws ExoPlaybackException {
        final MediaFormat format = this.format;
        this.format = mediaFormatHolder.format;
        this.drmInitData = mediaFormatHolder.drmInitData;
        if (this.codec != null && this.canReconfigureCodec(this.codec, this.codecIsAdaptive, format, this.format)) {
            this.codecReconfigured = true;
            this.codecReconfigurationState = 1;
            return;
        }
        if (this.codecHasQueuedBuffers) {
            this.codecReinitializationState = 1;
            return;
        }
        this.releaseCodec();
        this.maybeInitCodec();
    }
    
    protected void onOutputFormatChanged(final android.media.MediaFormat mediaFormat) {
    }
    
    protected void onOutputStreamEnded() {
    }
    
    @Override
    protected void onStarted() {
    }
    
    @Override
    protected void onStopped() {
    }
    
    protected abstract boolean processOutputBuffer(final long p0, final long p1, final MediaCodec p2, final ByteBuffer p3, final MediaCodec$BufferInfo p4, final int p5, final boolean p6) throws ExoPlaybackException;
    
    protected void releaseCodec() {
        if (this.codec == null) {
            return;
        }
        this.codecHotswapTimeMs = -1L;
        this.inputIndex = -1;
        this.outputIndex = -1;
        this.waitingForKeys = false;
        this.decodeOnlyPresentationTimestamps.clear();
        this.inputBuffers = null;
        this.outputBuffers = null;
        this.codecReconfigured = false;
        this.codecHasQueuedBuffers = false;
        this.codecIsAdaptive = false;
        this.codecNeedsEosPropagationWorkaround = false;
        this.codecNeedsEosFlushWorkaround = false;
        this.codecReceivedEos = false;
        this.codecReconfigurationState = 0;
        this.codecReinitializationState = 0;
        final CodecCounters codecCounters = this.codecCounters;
        ++codecCounters.codecReleaseCount;
        try {
            this.codec.stop();
            try {
                this.codec.release();
            }
            finally {
                this.codec = null;
            }
        }
        finally {
            try {
                this.codec.release();
                this.codec = null;
            }
            finally {
                this.codec = null;
            }
        }
    }
    
    @Override
    protected void seekTo(final long n) throws ExoPlaybackException {
        super.seekTo(n);
        this.seekToInternal();
    }
    
    protected boolean shouldInitCodec() {
        return this.codec == null && this.format != null;
    }
    
    public static class DecoderInitializationException extends Exception
    {
        public final String decoderName;
        public final String diagnosticInfo;
        
        public DecoderInitializationException(final MediaFormat mediaFormat, final Throwable t, final int n) {
            super("Decoder init failed: [" + n + "], " + mediaFormat, t);
            this.decoderName = null;
            this.diagnosticInfo = buildCustomDiagnosticInfo(n);
        }
        
        public DecoderInitializationException(final MediaFormat mediaFormat, final Throwable t, final String decoderName) {
            super("Decoder init failed: " + decoderName + ", " + mediaFormat, t);
            this.decoderName = decoderName;
            String diagnosticInfoV21;
            if (Util.SDK_INT >= 21) {
                diagnosticInfoV21 = getDiagnosticInfoV21(t);
            }
            else {
                diagnosticInfoV21 = null;
            }
            this.diagnosticInfo = diagnosticInfoV21;
        }
        
        private static String buildCustomDiagnosticInfo(final int n) {
            String s;
            if (n < 0) {
                s = "neg_";
            }
            else {
                s = "";
            }
            return "com.google.android.exoplayer.MediaCodecTrackRenderer_" + s + Math.abs(n);
        }
        
        @TargetApi(21)
        private static String getDiagnosticInfoV21(final Throwable t) {
            if (t instanceof MediaCodec$CodecException) {
                return ((MediaCodec$CodecException)t).getDiagnosticInfo();
            }
            return null;
        }
    }
    
    public interface EventListener
    {
        void onCryptoError(final MediaCodec$CryptoException p0);
        
        void onDecoderInitializationError(final DecoderInitializationException p0);
        
        void onDecoderInitialized(final String p0, final long p1, final long p2);
    }
}
