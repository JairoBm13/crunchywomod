// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer;

import android.media.MediaCodec$BufferInfo;
import java.nio.ByteBuffer;
import com.google.android.exoplayer.util.Util;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.TraceUtil;
import android.media.MediaCrypto;
import android.media.MediaCodec;
import android.annotation.SuppressLint;
import android.media.MediaFormat;
import android.os.SystemClock;
import android.os.Handler;
import com.google.android.exoplayer.drm.DrmSessionManager;
import android.view.Surface;
import android.annotation.TargetApi;

@TargetApi(16)
public class MediaCodecVideoTrackRenderer extends MediaCodecTrackRenderer
{
    private static final String KEY_CROP_BOTTOM = "crop-bottom";
    private static final String KEY_CROP_LEFT = "crop-left";
    private static final String KEY_CROP_RIGHT = "crop-right";
    private static final String KEY_CROP_TOP = "crop-top";
    public static final int MSG_SET_SURFACE = 1;
    private final long allowedJoiningTimeUs;
    private int currentHeight;
    private float currentPixelWidthHeightRatio;
    private int currentUnappliedRotationDegrees;
    private int currentWidth;
    private long droppedFrameAccumulationStartTimeMs;
    private int droppedFrameCount;
    private final EventListener eventListener;
    private final FrameReleaseTimeHelper frameReleaseTimeHelper;
    private long joiningDeadlineUs;
    private int lastReportedHeight;
    private float lastReportedPixelWidthHeightRatio;
    private int lastReportedUnappliedRotationDegrees;
    private int lastReportedWidth;
    private final int maxDroppedFrameCountToNotify;
    private float pendingPixelWidthHeightRatio;
    private int pendingRotationDegrees;
    private boolean renderedFirstFrame;
    private boolean reportedDrawnToSurface;
    private Surface surface;
    private final int videoScalingMode;
    
    public MediaCodecVideoTrackRenderer(final SampleSource sampleSource, final int n) {
        this(sampleSource, null, true, n);
    }
    
    public MediaCodecVideoTrackRenderer(final SampleSource sampleSource, final int n, final long n2) {
        this(sampleSource, null, true, n, n2);
    }
    
    public MediaCodecVideoTrackRenderer(final SampleSource sampleSource, final int n, final long n2, final Handler handler, final EventListener eventListener, final int n3) {
        this(sampleSource, null, true, n, n2, null, handler, eventListener, n3);
    }
    
    public MediaCodecVideoTrackRenderer(final SampleSource sampleSource, final DrmSessionManager drmSessionManager, final boolean b, final int n) {
        this(sampleSource, drmSessionManager, b, n, 0L);
    }
    
    public MediaCodecVideoTrackRenderer(final SampleSource sampleSource, final DrmSessionManager drmSessionManager, final boolean b, final int n, final long n2) {
        this(sampleSource, drmSessionManager, b, n, n2, null, null, null, -1);
    }
    
    public MediaCodecVideoTrackRenderer(final SampleSource sampleSource, final DrmSessionManager drmSessionManager, final boolean b, final int videoScalingMode, final long n, final FrameReleaseTimeHelper frameReleaseTimeHelper, final Handler handler, final EventListener eventListener, final int maxDroppedFrameCountToNotify) {
        super(sampleSource, drmSessionManager, b, handler, (MediaCodecTrackRenderer.EventListener)eventListener);
        this.videoScalingMode = videoScalingMode;
        this.allowedJoiningTimeUs = 1000L * n;
        this.frameReleaseTimeHelper = frameReleaseTimeHelper;
        this.eventListener = eventListener;
        this.maxDroppedFrameCountToNotify = maxDroppedFrameCountToNotify;
        this.joiningDeadlineUs = -1L;
        this.currentWidth = -1;
        this.currentHeight = -1;
        this.currentPixelWidthHeightRatio = -1.0f;
        this.pendingPixelWidthHeightRatio = -1.0f;
        this.lastReportedWidth = -1;
        this.lastReportedHeight = -1;
        this.lastReportedPixelWidthHeightRatio = -1.0f;
    }
    
    private void maybeNotifyDrawnToSurface() {
        if (this.eventHandler == null || this.eventListener == null || this.reportedDrawnToSurface) {
            return;
        }
        this.eventHandler.post((Runnable)new Runnable() {
            final /* synthetic */ Surface val$surface = MediaCodecVideoTrackRenderer.this.surface;
            
            @Override
            public void run() {
                MediaCodecVideoTrackRenderer.this.eventListener.onDrawnToSurface(this.val$surface);
            }
        });
        this.reportedDrawnToSurface = true;
    }
    
    private void maybeNotifyDroppedFrameCount() {
        if (this.eventHandler == null || this.eventListener == null || this.droppedFrameCount == 0) {
            return;
        }
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        this.eventHandler.post((Runnable)new Runnable() {
            final /* synthetic */ int val$countToNotify = MediaCodecVideoTrackRenderer.this.droppedFrameCount;
            final /* synthetic */ long val$elapsedToNotify = elapsedRealtime - MediaCodecVideoTrackRenderer.this.droppedFrameAccumulationStartTimeMs;
            
            @Override
            public void run() {
                MediaCodecVideoTrackRenderer.this.eventListener.onDroppedFrames(this.val$countToNotify, this.val$elapsedToNotify);
            }
        });
        this.droppedFrameCount = 0;
        this.droppedFrameAccumulationStartTimeMs = elapsedRealtime;
    }
    
    private void maybeNotifyVideoSizeChanged() {
        if (this.eventHandler == null || this.eventListener == null || (this.lastReportedWidth == this.currentWidth && this.lastReportedHeight == this.currentHeight && this.lastReportedUnappliedRotationDegrees == this.currentUnappliedRotationDegrees && this.lastReportedPixelWidthHeightRatio == this.currentPixelWidthHeightRatio)) {
            return;
        }
        final int currentWidth = this.currentWidth;
        final int currentHeight = this.currentHeight;
        final int currentUnappliedRotationDegrees = this.currentUnappliedRotationDegrees;
        final float currentPixelWidthHeightRatio = this.currentPixelWidthHeightRatio;
        this.eventHandler.post((Runnable)new Runnable() {
            @Override
            public void run() {
                MediaCodecVideoTrackRenderer.this.eventListener.onVideoSizeChanged(currentWidth, currentHeight, currentUnappliedRotationDegrees, currentPixelWidthHeightRatio);
            }
        });
        this.lastReportedWidth = currentWidth;
        this.lastReportedHeight = currentHeight;
        this.lastReportedUnappliedRotationDegrees = currentUnappliedRotationDegrees;
        this.lastReportedPixelWidthHeightRatio = currentPixelWidthHeightRatio;
    }
    
    @SuppressLint({ "InlinedApi" })
    private void maybeSetMaxInputSize(final MediaFormat mediaFormat, final boolean b) {
        if ("video/avc".equals(mediaFormat.getString("mime")) && !mediaFormat.containsKey("max-input-size")) {
            int n2;
            final int n = n2 = mediaFormat.getInteger("height");
            if (b) {
                n2 = n;
                if (mediaFormat.containsKey("max-height")) {
                    n2 = Math.max(n, mediaFormat.getInteger("max-height"));
                }
            }
            int n3 = mediaFormat.getInteger("width");
            if (b) {
                n3 = n3;
                if (mediaFormat.containsKey("max-width")) {
                    n3 = Math.max(n2, mediaFormat.getInteger("max-width"));
                }
            }
            mediaFormat.setInteger("max-input-size", (n3 + 15) / 16 * ((n2 + 15) / 16) * 192);
        }
    }
    
    private void setSurface(final Surface surface) throws ExoPlaybackException {
        if (this.surface != surface) {
            this.surface = surface;
            this.reportedDrawnToSurface = false;
            final int state = this.getState();
            if (state == 2 || state == 3) {
                this.releaseCodec();
                this.maybeInitCodec();
            }
        }
    }
    
    @Override
    protected boolean canReconfigureCodec(final MediaCodec mediaCodec, final boolean b, final com.google.android.exoplayer.MediaFormat mediaFormat, final com.google.android.exoplayer.MediaFormat mediaFormat2) {
        return mediaFormat2.mimeType.equals(mediaFormat.mimeType) && (b || (mediaFormat.width == mediaFormat2.width && mediaFormat.height == mediaFormat2.height));
    }
    
    @Override
    protected void configureCodec(final MediaCodec mediaCodec, final String s, final boolean b, final MediaFormat mediaFormat, final MediaCrypto mediaCrypto) {
        this.maybeSetMaxInputSize(mediaFormat, b);
        mediaCodec.configure(mediaFormat, this.surface, mediaCrypto, 0);
        mediaCodec.setVideoScalingMode(this.videoScalingMode);
    }
    
    protected void dropOutputBuffer(final MediaCodec mediaCodec, final int n) {
        TraceUtil.beginSection("dropVideoBuffer");
        mediaCodec.releaseOutputBuffer(n, false);
        TraceUtil.endSection();
        final CodecCounters codecCounters = this.codecCounters;
        ++codecCounters.droppedOutputBufferCount;
        ++this.droppedFrameCount;
        if (this.droppedFrameCount == this.maxDroppedFrameCountToNotify) {
            this.maybeNotifyDroppedFrameCount();
        }
    }
    
    @Override
    public void handleMessage(final int n, final Object o) throws ExoPlaybackException {
        if (n == 1) {
            this.setSurface((Surface)o);
            return;
        }
        super.handleMessage(n, o);
    }
    
    @Override
    protected boolean handlesTrack(final com.google.android.exoplayer.MediaFormat mediaFormat) throws MediaCodecUtil.DecoderQueryException {
        final boolean b = false;
        final String mimeType = mediaFormat.mimeType;
        boolean b2 = b;
        if (MimeTypes.isVideo(mimeType)) {
            if (!"video/x-unknown".equals(mimeType)) {
                b2 = b;
                if (MediaCodecUtil.getDecoderInfo(mimeType, false) == null) {
                    return b2;
                }
            }
            b2 = true;
        }
        return b2;
    }
    
    protected final boolean haveRenderedFirstFrame() {
        return this.renderedFirstFrame;
    }
    
    @Override
    protected boolean isReady() {
        if (super.isReady() && (this.renderedFirstFrame || !this.codecInitialized() || this.getSourceState() == 2)) {
            this.joiningDeadlineUs = -1L;
        }
        else {
            if (this.joiningDeadlineUs == -1L) {
                return false;
            }
            if (SystemClock.elapsedRealtime() * 1000L >= this.joiningDeadlineUs) {
                this.joiningDeadlineUs = -1L;
                return false;
            }
        }
        return true;
    }
    
    @Override
    protected void onDisabled() throws ExoPlaybackException {
        this.currentWidth = -1;
        this.currentHeight = -1;
        this.currentPixelWidthHeightRatio = -1.0f;
        this.pendingPixelWidthHeightRatio = -1.0f;
        this.lastReportedWidth = -1;
        this.lastReportedHeight = -1;
        this.lastReportedPixelWidthHeightRatio = -1.0f;
        if (this.frameReleaseTimeHelper != null) {
            this.frameReleaseTimeHelper.disable();
        }
        super.onDisabled();
    }
    
    @Override
    protected void onEnabled(final int n, final long n2, final boolean b) throws ExoPlaybackException {
        super.onEnabled(n, n2, b);
        this.renderedFirstFrame = false;
        if (b && this.allowedJoiningTimeUs > 0L) {
            this.joiningDeadlineUs = SystemClock.elapsedRealtime() * 1000L + this.allowedJoiningTimeUs;
        }
        if (this.frameReleaseTimeHelper != null) {
            this.frameReleaseTimeHelper.enable();
        }
    }
    
    @Override
    protected void onInputFormatChanged(final MediaFormatHolder mediaFormatHolder) throws ExoPlaybackException {
        super.onInputFormatChanged(mediaFormatHolder);
        float pixelWidthHeightRatio;
        if (mediaFormatHolder.format.pixelWidthHeightRatio == -1.0f) {
            pixelWidthHeightRatio = 1.0f;
        }
        else {
            pixelWidthHeightRatio = mediaFormatHolder.format.pixelWidthHeightRatio;
        }
        this.pendingPixelWidthHeightRatio = pixelWidthHeightRatio;
        int rotationDegrees;
        if (mediaFormatHolder.format.rotationDegrees == -1) {
            rotationDegrees = 0;
        }
        else {
            rotationDegrees = mediaFormatHolder.format.rotationDegrees;
        }
        this.pendingRotationDegrees = rotationDegrees;
    }
    
    @Override
    protected void onOutputFormatChanged(final MediaFormat mediaFormat) {
        boolean b;
        if (mediaFormat.containsKey("crop-right") && mediaFormat.containsKey("crop-left") && mediaFormat.containsKey("crop-bottom") && mediaFormat.containsKey("crop-top")) {
            b = true;
        }
        else {
            b = false;
        }
        int integer;
        if (b) {
            integer = mediaFormat.getInteger("crop-right") - mediaFormat.getInteger("crop-left") + 1;
        }
        else {
            integer = mediaFormat.getInteger("width");
        }
        this.currentWidth = integer;
        int integer2;
        if (b) {
            integer2 = mediaFormat.getInteger("crop-bottom") - mediaFormat.getInteger("crop-top") + 1;
        }
        else {
            integer2 = mediaFormat.getInteger("height");
        }
        this.currentHeight = integer2;
        this.currentPixelWidthHeightRatio = this.pendingPixelWidthHeightRatio;
        if (Util.SDK_INT >= 21) {
            if (this.pendingRotationDegrees == 90 || this.pendingRotationDegrees == 270) {
                final int currentWidth = this.currentWidth;
                this.currentWidth = this.currentHeight;
                this.currentHeight = currentWidth;
                this.currentPixelWidthHeightRatio = 1.0f / this.currentPixelWidthHeightRatio;
            }
            return;
        }
        this.currentUnappliedRotationDegrees = this.pendingRotationDegrees;
    }
    
    @Override
    protected void onStarted() {
        super.onStarted();
        this.droppedFrameCount = 0;
        this.droppedFrameAccumulationStartTimeMs = SystemClock.elapsedRealtime();
    }
    
    @Override
    protected void onStopped() {
        this.joiningDeadlineUs = -1L;
        this.maybeNotifyDroppedFrameCount();
        super.onStopped();
    }
    
    @Override
    protected boolean processOutputBuffer(long adjustReleaseTime, long n, final MediaCodec mediaCodec, final ByteBuffer byteBuffer, final MediaCodec$BufferInfo mediaCodec$BufferInfo, final int n2, final boolean b) {
        if (b) {
            this.skipOutputBuffer(mediaCodec, n2);
            return true;
        }
        if (!this.renderedFirstFrame) {
            if (Util.SDK_INT >= 21) {
                this.renderOutputBufferV21(mediaCodec, n2, System.nanoTime());
            }
            else {
                this.renderOutputBuffer(mediaCodec, n2);
            }
            return true;
        }
        if (this.getState() != 3) {
            return false;
        }
        n = mediaCodec$BufferInfo.presentationTimeUs - adjustReleaseTime - (SystemClock.elapsedRealtime() * 1000L - n);
        final long nanoTime = System.nanoTime();
        adjustReleaseTime = nanoTime + 1000L * n;
        if (this.frameReleaseTimeHelper != null) {
            adjustReleaseTime = this.frameReleaseTimeHelper.adjustReleaseTime(mediaCodec$BufferInfo.presentationTimeUs, adjustReleaseTime);
            n = (adjustReleaseTime - nanoTime) / 1000L;
        }
        if (n < -30000L) {
            this.dropOutputBuffer(mediaCodec, n2);
            return true;
        }
        if (Util.SDK_INT >= 21) {
            if (n < 50000L) {
                this.renderOutputBufferV21(mediaCodec, n2, adjustReleaseTime);
                return true;
            }
        }
        else if (n < 30000L) {
            while (true) {
                if (n <= 11000L) {
                    break Label_0208;
                }
                try {
                    Thread.sleep((n - 10000L) / 1000L);
                    this.renderOutputBuffer(mediaCodec, n2);
                    return true;
                }
                catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    continue;
                }
                break;
            }
        }
        return false;
    }
    
    protected void renderOutputBuffer(final MediaCodec mediaCodec, final int n) {
        this.maybeNotifyVideoSizeChanged();
        TraceUtil.beginSection("releaseOutputBuffer");
        mediaCodec.releaseOutputBuffer(n, true);
        TraceUtil.endSection();
        final CodecCounters codecCounters = this.codecCounters;
        ++codecCounters.renderedOutputBufferCount;
        this.renderedFirstFrame = true;
        this.maybeNotifyDrawnToSurface();
    }
    
    @TargetApi(21)
    protected void renderOutputBufferV21(final MediaCodec mediaCodec, final int n, final long n2) {
        this.maybeNotifyVideoSizeChanged();
        TraceUtil.beginSection("releaseOutputBuffer");
        mediaCodec.releaseOutputBuffer(n, n2);
        TraceUtil.endSection();
        final CodecCounters codecCounters = this.codecCounters;
        ++codecCounters.renderedOutputBufferCount;
        this.renderedFirstFrame = true;
        this.maybeNotifyDrawnToSurface();
    }
    
    @Override
    protected void seekTo(final long n) throws ExoPlaybackException {
        super.seekTo(n);
        this.renderedFirstFrame = false;
        this.joiningDeadlineUs = -1L;
    }
    
    @Override
    protected boolean shouldInitCodec() {
        return super.shouldInitCodec() && this.surface != null && this.surface.isValid();
    }
    
    protected void skipOutputBuffer(final MediaCodec mediaCodec, final int n) {
        TraceUtil.beginSection("skipVideoBuffer");
        mediaCodec.releaseOutputBuffer(n, false);
        TraceUtil.endSection();
        final CodecCounters codecCounters = this.codecCounters;
        ++codecCounters.skippedOutputBufferCount;
    }
    
    public interface EventListener extends MediaCodecTrackRenderer.EventListener
    {
        void onDrawnToSurface(final Surface p0);
        
        void onDroppedFrames(final int p0, final long p1);
        
        void onVideoSizeChanged(final int p0, final int p1, final int p2, final float p3);
    }
    
    public interface FrameReleaseTimeHelper
    {
        long adjustReleaseTime(final long p0, final long p1);
        
        void disable();
        
        void enable();
    }
}
