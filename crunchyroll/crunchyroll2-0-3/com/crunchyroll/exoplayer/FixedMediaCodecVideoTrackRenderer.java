// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.exoplayer;

import android.media.MediaFormat;
import com.google.android.exoplayer.drm.DrmSessionManager;
import android.os.Handler;
import com.google.android.exoplayer.SampleSource;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;

public class FixedMediaCodecVideoTrackRenderer extends MediaCodecVideoTrackRenderer
{
    private float mLastAspectRatio;
    private int mLastHeight;
    private int mLastWidth;
    private VideoSizeChangeListener mSizeChangeListener;
    
    public FixedMediaCodecVideoTrackRenderer(final SampleSource sampleSource, final int n) {
        super(sampleSource, n);
        this.mLastWidth = -1;
        this.mLastHeight = -1;
        this.mLastAspectRatio = -1.0f;
    }
    
    public FixedMediaCodecVideoTrackRenderer(final SampleSource sampleSource, final int n, final long n2, final Handler handler, final EventListener eventListener, final int n3) {
        super(sampleSource, n, n2, handler, eventListener, n3);
        this.mLastWidth = -1;
        this.mLastHeight = -1;
        this.mLastAspectRatio = -1.0f;
    }
    
    public FixedMediaCodecVideoTrackRenderer(final SampleSource sampleSource, final int n, final long n2, final VideoSizeChangeListener mSizeChangeListener) {
        super(sampleSource, n, n2);
        this.mLastWidth = -1;
        this.mLastHeight = -1;
        this.mLastAspectRatio = -1.0f;
        this.mSizeChangeListener = mSizeChangeListener;
    }
    
    public FixedMediaCodecVideoTrackRenderer(final SampleSource sampleSource, final DrmSessionManager drmSessionManager, final boolean b, final int n) {
        super(sampleSource, drmSessionManager, b, n);
        this.mLastWidth = -1;
        this.mLastHeight = -1;
        this.mLastAspectRatio = -1.0f;
    }
    
    public FixedMediaCodecVideoTrackRenderer(final SampleSource sampleSource, final DrmSessionManager drmSessionManager, final boolean b, final int n, final long n2) {
        super(sampleSource, drmSessionManager, b, n, n2);
        this.mLastWidth = -1;
        this.mLastHeight = -1;
        this.mLastAspectRatio = -1.0f;
    }
    
    public FixedMediaCodecVideoTrackRenderer(final SampleSource sampleSource, final DrmSessionManager drmSessionManager, final boolean b, final int n, final long n2, final FrameReleaseTimeHelper frameReleaseTimeHelper, final Handler handler, final EventListener eventListener, final int n3) {
        super(sampleSource, drmSessionManager, b, n, n2, frameReleaseTimeHelper, handler, eventListener, n3);
        this.mLastWidth = -1;
        this.mLastHeight = -1;
        this.mLastAspectRatio = -1.0f;
    }
    
    @Override
    protected void onOutputFormatChanged(final MediaFormat mediaFormat) {
        super.onOutputFormatChanged(mediaFormat);
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
        int integer2;
        if (b) {
            integer2 = mediaFormat.getInteger("crop-bottom") - mediaFormat.getInteger("crop-top") + 1;
        }
        else {
            integer2 = mediaFormat.getInteger("height");
        }
        final float mLastAspectRatio = integer / integer2;
        if (this.mSizeChangeListener != null && (this.mLastWidth != integer || this.mLastHeight != integer2 || this.mLastAspectRatio != mLastAspectRatio)) {
            this.mLastWidth = integer;
            this.mLastHeight = integer2;
            this.mLastAspectRatio = mLastAspectRatio;
            this.mSizeChangeListener.onVideoSizeChanged(integer, integer2, mLastAspectRatio);
        }
    }
    
    public interface VideoSizeChangeListener
    {
        void onVideoSizeChanged(final int p0, final int p1, final float p2);
    }
}
