// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer;

import android.media.MediaCodec$BufferInfo;
import java.nio.ByteBuffer;
import com.google.android.exoplayer.util.MimeTypes;
import android.view.Surface;
import android.media.MediaCrypto;
import android.media.MediaCodec;
import com.google.android.exoplayer.audio.AudioCapabilities;
import android.os.Handler;
import com.google.android.exoplayer.drm.DrmSessionManager;
import android.media.MediaFormat;
import com.google.android.exoplayer.audio.AudioTrack;
import android.annotation.TargetApi;

@TargetApi(16)
public class MediaCodecAudioTrackRenderer extends MediaCodecTrackRenderer implements MediaClock
{
    private boolean allowPositionDiscontinuity;
    private int audioSessionId;
    private final AudioTrack audioTrack;
    private long currentPositionUs;
    private final EventListener eventListener;
    private MediaFormat passthroughMediaFormat;
    
    public MediaCodecAudioTrackRenderer(final SampleSource sampleSource) {
        this(sampleSource, null, true);
    }
    
    public MediaCodecAudioTrackRenderer(final SampleSource sampleSource, final DrmSessionManager drmSessionManager, final boolean b) {
        this(sampleSource, drmSessionManager, b, null, (EventListener)null);
    }
    
    public MediaCodecAudioTrackRenderer(final SampleSource sampleSource, final DrmSessionManager drmSessionManager, final boolean b, final Handler handler, final EventListener eventListener) {
        this(sampleSource, drmSessionManager, b, handler, eventListener, null);
    }
    
    public MediaCodecAudioTrackRenderer(final SampleSource sampleSource, final DrmSessionManager drmSessionManager, final boolean b, final Handler handler, final EventListener eventListener, final AudioCapabilities audioCapabilities) {
        this(sampleSource, drmSessionManager, b, handler, eventListener, audioCapabilities, 3);
    }
    
    public MediaCodecAudioTrackRenderer(final SampleSource sampleSource, final DrmSessionManager drmSessionManager, final boolean b, final Handler handler, final EventListener eventListener, final AudioCapabilities audioCapabilities, final int n) {
        super(sampleSource, drmSessionManager, b, handler, (MediaCodecTrackRenderer.EventListener)eventListener);
        this.eventListener = eventListener;
        this.audioSessionId = 0;
        this.audioTrack = new AudioTrack(audioCapabilities, n);
    }
    
    private void notifyAudioTrackInitializationError(final AudioTrack.InitializationException ex) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    MediaCodecAudioTrackRenderer.this.eventListener.onAudioTrackInitializationError(ex);
                }
            });
        }
    }
    
    private void notifyAudioTrackWriteError(final AudioTrack.WriteException ex) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    MediaCodecAudioTrackRenderer.this.eventListener.onAudioTrackWriteError(ex);
                }
            });
        }
    }
    
    private void seekToInternal(final long currentPositionUs) {
        this.audioTrack.reset();
        this.currentPositionUs = currentPositionUs;
        this.allowPositionDiscontinuity = true;
    }
    
    protected boolean allowPassthrough(final String s) {
        return this.audioTrack.isPassthroughSupported(s);
    }
    
    @Override
    protected void configureCodec(final MediaCodec mediaCodec, final String s, final boolean b, final MediaFormat passthroughMediaFormat, final MediaCrypto mediaCrypto) {
        final String string = passthroughMediaFormat.getString("mime");
        if ("OMX.google.raw.decoder".equals(s) && !"audio/raw".equals(string)) {
            passthroughMediaFormat.setString("mime", "audio/raw");
            mediaCodec.configure(passthroughMediaFormat, (Surface)null, mediaCrypto, 0);
            passthroughMediaFormat.setString("mime", string);
            this.passthroughMediaFormat = passthroughMediaFormat;
            return;
        }
        mediaCodec.configure(passthroughMediaFormat, (Surface)null, mediaCrypto, 0);
        this.passthroughMediaFormat = null;
    }
    
    @Override
    protected DecoderInfo getDecoderInfo(final String s, final boolean b) throws MediaCodecUtil.DecoderQueryException {
        if (this.allowPassthrough(s)) {
            return new DecoderInfo("OMX.google.raw.decoder", true);
        }
        return super.getDecoderInfo(s, b);
    }
    
    @Override
    protected MediaClock getMediaClock() {
        return this;
    }
    
    @Override
    public long getPositionUs() {
        long currentPositionUs = this.audioTrack.getCurrentPositionUs(this.isEnded());
        if (currentPositionUs != Long.MIN_VALUE) {
            if (!this.allowPositionDiscontinuity) {
                currentPositionUs = Math.max(this.currentPositionUs, currentPositionUs);
            }
            this.currentPositionUs = currentPositionUs;
            this.allowPositionDiscontinuity = false;
        }
        return this.currentPositionUs;
    }
    
    protected void handleDiscontinuity() {
    }
    
    @Override
    public void handleMessage(final int n, final Object o) throws ExoPlaybackException {
        if (n == 1) {
            this.audioTrack.setVolume((float)o);
            return;
        }
        super.handleMessage(n, o);
    }
    
    @Override
    protected boolean handlesTrack(final com.google.android.exoplayer.MediaFormat mediaFormat) throws MediaCodecUtil.DecoderQueryException {
        final boolean b = false;
        final String mimeType = mediaFormat.mimeType;
        boolean b2 = b;
        if (MimeTypes.isAudio(mimeType)) {
            if (!"audio/x-unknown".equals(mimeType) && !this.allowPassthrough(mimeType)) {
                b2 = b;
                if (MediaCodecUtil.getDecoderInfo(mimeType, false) == null) {
                    return b2;
                }
            }
            b2 = true;
        }
        return b2;
    }
    
    @Override
    protected boolean isEnded() {
        return super.isEnded() && !this.audioTrack.hasPendingData();
    }
    
    @Override
    protected boolean isReady() {
        return this.audioTrack.hasPendingData() || (super.isReady() && this.getSourceState() == 2);
    }
    
    protected void onAudioSessionId(final int n) {
    }
    
    @Override
    protected void onDisabled() throws ExoPlaybackException {
        this.audioSessionId = 0;
        try {
            this.audioTrack.release();
        }
        finally {
            super.onDisabled();
        }
    }
    
    @Override
    protected void onEnabled(final int n, final long n2, final boolean b) throws ExoPlaybackException {
        super.onEnabled(n, n2, b);
        this.seekToInternal(n2);
    }
    
    @Override
    protected void onOutputFormatChanged(MediaFormat passthroughMediaFormat) {
        final boolean b = this.passthroughMediaFormat != null;
        final AudioTrack audioTrack = this.audioTrack;
        if (b) {
            passthroughMediaFormat = this.passthroughMediaFormat;
        }
        audioTrack.reconfigure(passthroughMediaFormat, b);
    }
    
    @Override
    protected void onOutputStreamEnded() {
        this.audioTrack.handleEndOfStream();
    }
    
    @Override
    protected void onStarted() {
        super.onStarted();
        this.audioTrack.play();
    }
    
    @Override
    protected void onStopped() {
        this.audioTrack.pause();
        super.onStopped();
    }
    
    @Override
    protected boolean processOutputBuffer(final long p0, final long p1, final MediaCodec p2, final ByteBuffer p3, final MediaCodec$BufferInfo p4, final int p5, final boolean p6) throws ExoPlaybackException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: iload           9
        //     2: ifeq            40
        //     5: aload           5
        //     7: iload           8
        //     9: iconst_0       
        //    10: invokevirtual   android/media/MediaCodec.releaseOutputBuffer:(IZ)V
        //    13: aload_0        
        //    14: getfield        com/google/android/exoplayer/MediaCodecAudioTrackRenderer.codecCounters:Lcom/google/android/exoplayer/CodecCounters;
        //    17: astore          5
        //    19: aload           5
        //    21: aload           5
        //    23: getfield        com/google/android/exoplayer/CodecCounters.skippedOutputBufferCount:I
        //    26: iconst_1       
        //    27: iadd           
        //    28: putfield        com/google/android/exoplayer/CodecCounters.skippedOutputBufferCount:I
        //    31: aload_0        
        //    32: getfield        com/google/android/exoplayer/MediaCodecAudioTrackRenderer.audioTrack:Lcom/google/android/exoplayer/audio/AudioTrack;
        //    35: invokevirtual   com/google/android/exoplayer/audio/AudioTrack.handleDiscontinuity:()V
        //    38: iconst_1       
        //    39: ireturn        
        //    40: aload_0        
        //    41: getfield        com/google/android/exoplayer/MediaCodecAudioTrackRenderer.audioTrack:Lcom/google/android/exoplayer/audio/AudioTrack;
        //    44: invokevirtual   com/google/android/exoplayer/audio/AudioTrack.isInitialized:()Z
        //    47: ifne            84
        //    50: aload_0        
        //    51: getfield        com/google/android/exoplayer/MediaCodecAudioTrackRenderer.audioSessionId:I
        //    54: ifeq            161
        //    57: aload_0        
        //    58: getfield        com/google/android/exoplayer/MediaCodecAudioTrackRenderer.audioTrack:Lcom/google/android/exoplayer/audio/AudioTrack;
        //    61: aload_0        
        //    62: getfield        com/google/android/exoplayer/MediaCodecAudioTrackRenderer.audioSessionId:I
        //    65: invokevirtual   com/google/android/exoplayer/audio/AudioTrack.initialize:(I)I
        //    68: pop            
        //    69: aload_0        
        //    70: invokevirtual   com/google/android/exoplayer/MediaCodecAudioTrackRenderer.getState:()I
        //    73: iconst_3       
        //    74: if_icmpne       84
        //    77: aload_0        
        //    78: getfield        com/google/android/exoplayer/MediaCodecAudioTrackRenderer.audioTrack:Lcom/google/android/exoplayer/audio/AudioTrack;
        //    81: invokevirtual   com/google/android/exoplayer/audio/AudioTrack.play:()V
        //    84: aload_0        
        //    85: getfield        com/google/android/exoplayer/MediaCodecAudioTrackRenderer.audioTrack:Lcom/google/android/exoplayer/audio/AudioTrack;
        //    88: aload           6
        //    90: aload           7
        //    92: getfield        android/media/MediaCodec$BufferInfo.offset:I
        //    95: aload           7
        //    97: getfield        android/media/MediaCodec$BufferInfo.size:I
        //   100: aload           7
        //   102: getfield        android/media/MediaCodec$BufferInfo.presentationTimeUs:J
        //   105: invokevirtual   com/google/android/exoplayer/audio/AudioTrack.handleBuffer:(Ljava/nio/ByteBuffer;IIJ)I
        //   108: istore          10
        //   110: iload           10
        //   112: iconst_1       
        //   113: iand           
        //   114: ifeq            126
        //   117: aload_0        
        //   118: invokevirtual   com/google/android/exoplayer/MediaCodecAudioTrackRenderer.handleDiscontinuity:()V
        //   121: aload_0        
        //   122: iconst_1       
        //   123: putfield        com/google/android/exoplayer/MediaCodecAudioTrackRenderer.allowPositionDiscontinuity:Z
        //   126: iload           10
        //   128: iconst_2       
        //   129: iand           
        //   130: ifeq            219
        //   133: aload           5
        //   135: iload           8
        //   137: iconst_0       
        //   138: invokevirtual   android/media/MediaCodec.releaseOutputBuffer:(IZ)V
        //   141: aload_0        
        //   142: getfield        com/google/android/exoplayer/MediaCodecAudioTrackRenderer.codecCounters:Lcom/google/android/exoplayer/CodecCounters;
        //   145: astore          5
        //   147: aload           5
        //   149: aload           5
        //   151: getfield        com/google/android/exoplayer/CodecCounters.renderedOutputBufferCount:I
        //   154: iconst_1       
        //   155: iadd           
        //   156: putfield        com/google/android/exoplayer/CodecCounters.renderedOutputBufferCount:I
        //   159: iconst_1       
        //   160: ireturn        
        //   161: aload_0        
        //   162: aload_0        
        //   163: getfield        com/google/android/exoplayer/MediaCodecAudioTrackRenderer.audioTrack:Lcom/google/android/exoplayer/audio/AudioTrack;
        //   166: invokevirtual   com/google/android/exoplayer/audio/AudioTrack.initialize:()I
        //   169: putfield        com/google/android/exoplayer/MediaCodecAudioTrackRenderer.audioSessionId:I
        //   172: aload_0        
        //   173: aload_0        
        //   174: getfield        com/google/android/exoplayer/MediaCodecAudioTrackRenderer.audioSessionId:I
        //   177: invokevirtual   com/google/android/exoplayer/MediaCodecAudioTrackRenderer.onAudioSessionId:(I)V
        //   180: goto            69
        //   183: astore          5
        //   185: aload_0        
        //   186: aload           5
        //   188: invokespecial   com/google/android/exoplayer/MediaCodecAudioTrackRenderer.notifyAudioTrackInitializationError:(Lcom/google/android/exoplayer/audio/AudioTrack$InitializationException;)V
        //   191: new             Lcom/google/android/exoplayer/ExoPlaybackException;
        //   194: dup            
        //   195: aload           5
        //   197: invokespecial   com/google/android/exoplayer/ExoPlaybackException.<init>:(Ljava/lang/Throwable;)V
        //   200: athrow         
        //   201: astore          5
        //   203: aload_0        
        //   204: aload           5
        //   206: invokespecial   com/google/android/exoplayer/MediaCodecAudioTrackRenderer.notifyAudioTrackWriteError:(Lcom/google/android/exoplayer/audio/AudioTrack$WriteException;)V
        //   209: new             Lcom/google/android/exoplayer/ExoPlaybackException;
        //   212: dup            
        //   213: aload           5
        //   215: invokespecial   com/google/android/exoplayer/ExoPlaybackException.<init>:(Ljava/lang/Throwable;)V
        //   218: athrow         
        //   219: iconst_0       
        //   220: ireturn        
        //    Exceptions:
        //  throws com.google.android.exoplayer.ExoPlaybackException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                                   
        //  -----  -----  -----  -----  -----------------------------------------------------------------------
        //  50     69     183    201    Lcom/google/android/exoplayer/audio/AudioTrack$InitializationException;
        //  84     110    201    219    Lcom/google/android/exoplayer/audio/AudioTrack$WriteException;
        //  161    180    183    201    Lcom/google/android/exoplayer/audio/AudioTrack$InitializationException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0084:
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
    protected void seekTo(final long n) throws ExoPlaybackException {
        super.seekTo(n);
        this.seekToInternal(n);
    }
    
    public interface EventListener extends MediaCodecTrackRenderer.EventListener
    {
        void onAudioTrackInitializationError(final AudioTrack.InitializationException p0);
        
        void onAudioTrackWriteError(final AudioTrack.WriteException p0);
    }
}
