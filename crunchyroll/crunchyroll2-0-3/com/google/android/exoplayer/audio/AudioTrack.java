// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.audio;

import android.media.AudioTimestamp;
import android.os.SystemClock;
import com.google.android.exoplayer.util.Assertions;
import android.media.MediaFormat;
import com.google.android.exoplayer.util.Ac3Util;
import java.nio.ByteBuffer;
import android.util.Log;
import com.google.android.exoplayer.util.Util;
import android.os.ConditionVariable;
import java.lang.reflect.Method;
import android.annotation.TargetApi;

@TargetApi(16)
public final class AudioTrack
{
    public static boolean enablePreV21AudioSessionWorkaround;
    public static boolean failOnSpuriousAudioTimestamp;
    private final AudioCapabilities audioCapabilities;
    private boolean audioTimestampSet;
    private android.media.AudioTrack audioTrack;
    private final AudioTrackUtil audioTrackUtil;
    private int bufferSize;
    private int channelConfig;
    private int encoding;
    private int frameSize;
    private Method getLatencyMethod;
    private android.media.AudioTrack keepSessionIdAudioTrack;
    private long lastPlayheadSampleTimeUs;
    private long lastTimestampSampleTimeUs;
    private long latencyUs;
    private int minBufferSize;
    private int nextPlayheadOffsetIndex;
    private int passthroughBitrate;
    private int playheadOffsetCount;
    private final long[] playheadOffsets;
    private final ConditionVariable releasingConditionVariable;
    private long resumeSystemTimeUs;
    private int sampleRate;
    private long smoothedPlayheadOffsetUs;
    private int startMediaTimeState;
    private long startMediaTimeUs;
    private final int streamType;
    private long submittedBytes;
    private byte[] temporaryBuffer;
    private int temporaryBufferOffset;
    private int temporaryBufferSize;
    private float volume;
    
    static {
        AudioTrack.enablePreV21AudioSessionWorkaround = false;
        AudioTrack.failOnSpuriousAudioTimestamp = false;
    }
    
    public AudioTrack() {
        this(null, 3);
    }
    
    public AudioTrack(final AudioCapabilities audioCapabilities, final int streamType) {
        this.audioCapabilities = audioCapabilities;
        this.streamType = streamType;
        this.releasingConditionVariable = new ConditionVariable(true);
        while (true) {
            if (Util.SDK_INT < 18) {
                break Label_0049;
            }
            try {
                this.getLatencyMethod = android.media.AudioTrack.class.getMethod("getLatency", (Class<?>[])null);
                if (Util.SDK_INT >= 19) {
                    this.audioTrackUtil = (AudioTrackUtil)new AudioTrackUtilV19();
                }
                else {
                    this.audioTrackUtil = new AudioTrackUtil();
                }
                this.playheadOffsets = new long[10];
                this.volume = 1.0f;
                this.startMediaTimeState = 0;
            }
            catch (NoSuchMethodException ex) {
                continue;
            }
            break;
        }
    }
    
    private long bytesToFrames(final long n) {
        if (!this.isPassthrough()) {
            return n / this.frameSize;
        }
        if (this.passthroughBitrate == 0) {
            return 0L;
        }
        return 8L * n * this.sampleRate / (this.passthroughBitrate * 1000);
    }
    
    private void checkAudioTrackInitialized() throws InitializationException {
        final int state = this.audioTrack.getState();
        if (state == 1) {
            return;
        }
        try {
            this.audioTrack.release();
            this.audioTrack = null;
            throw new InitializationException(state, this.sampleRate, this.channelConfig, this.bufferSize);
        }
        catch (Exception ex) {
            this.audioTrack = null;
            throw new InitializationException(state, this.sampleRate, this.channelConfig, this.bufferSize);
        }
        finally {
            this.audioTrack = null;
        }
    }
    
    private long durationUsToFrames(final long n) {
        return this.sampleRate * n / 1000000L;
    }
    
    private long framesToDurationUs(final long n) {
        return 1000000L * n / this.sampleRate;
    }
    
    private static int getEncodingForMimeType(final String s) {
        if ("audio/ac3".equals(s)) {
            return 5;
        }
        if ("audio/eac3".equals(s)) {
            return 6;
        }
        return 0;
    }
    
    private boolean hasCurrentPositionUs() {
        return this.isInitialized() && this.startMediaTimeState != 0;
    }
    
    private boolean isPassthrough() {
        return this.encoding == 5 || this.encoding == 6;
    }
    
    private void maybeSampleSyncParams() {
        final long playbackHeadPositionUs = this.audioTrackUtil.getPlaybackHeadPositionUs();
        if (playbackHeadPositionUs != 0L) {
            final long n = System.nanoTime() / 1000L;
            if (n - this.lastPlayheadSampleTimeUs >= 30000L) {
                this.playheadOffsets[this.nextPlayheadOffsetIndex] = playbackHeadPositionUs - n;
                this.nextPlayheadOffsetIndex = (this.nextPlayheadOffsetIndex + 1) % 10;
                if (this.playheadOffsetCount < 10) {
                    ++this.playheadOffsetCount;
                }
                this.lastPlayheadSampleTimeUs = n;
                this.smoothedPlayheadOffsetUs = 0L;
                for (int i = 0; i < this.playheadOffsetCount; ++i) {
                    this.smoothedPlayheadOffsetUs += this.playheadOffsets[i] / this.playheadOffsetCount;
                }
            }
            if (!this.isPassthrough() && n - this.lastTimestampSampleTimeUs >= 500000L) {
                this.audioTimestampSet = this.audioTrackUtil.updateTimestamp();
                Label_0323: {
                    if (this.audioTimestampSet) {
                        final long n2 = this.audioTrackUtil.getTimestampNanoTime() / 1000L;
                        final long timestampFramePosition = this.audioTrackUtil.getTimestampFramePosition();
                        if (n2 >= this.resumeSystemTimeUs) {
                            break Label_0323;
                        }
                        this.audioTimestampSet = false;
                    }
                Label_0316_Outer:
                    while (true) {
                    Block_11_Outer:
                        while (true) {
                            if (this.getLatencyMethod == null) {
                                break Label_0316;
                            }
                            try {
                                this.latencyUs = (int)this.getLatencyMethod.invoke(this.audioTrack, (Object[])null) * 1000L - this.framesToDurationUs(this.bytesToFrames(this.bufferSize));
                                this.latencyUs = Math.max(this.latencyUs, 0L);
                                if (this.latencyUs > 5000000L) {
                                    Log.w("AudioTrack", "Ignoring impossibly large audio latency: " + this.latencyUs);
                                    this.latencyUs = 0L;
                                }
                                this.lastTimestampSampleTimeUs = n;
                                return;
                                // iftrue(Label_0514:, !AudioTrack.failOnSpuriousAudioTimestamp)
                                // iftrue(Label_0409:, !AudioTrack.failOnSpuriousAudioTimestamp)
                                // iftrue(Label_0208:, Math.abs(this.framesToDurationUs(timestampFramePosition) - playbackHeadPositionUs) <= 5000000L)
                                while (true) {
                                    while (true) {
                                        final long n2;
                                        final long timestampFramePosition;
                                        final String string = "Spurious audio timestamp (frame position mismatch): " + timestampFramePosition + ", " + n2 + ", " + n + ", " + playbackHeadPositionUs;
                                        throw new InvalidAudioTrackTimestampException(string);
                                        Label_0514: {
                                            Log.w("AudioTrack", string);
                                        }
                                        this.audioTimestampSet = false;
                                        continue Label_0316_Outer;
                                        final String string2;
                                        Label_0409:
                                        Log.w("AudioTrack", string2);
                                        this.audioTimestampSet = false;
                                        continue Label_0316_Outer;
                                        string2 = "Spurious audio timestamp (system clock mismatch): " + timestampFramePosition + ", " + n2 + ", " + n + ", " + playbackHeadPositionUs;
                                        throw new InvalidAudioTrackTimestampException(string2);
                                        Label_0425:
                                        continue Block_11_Outer;
                                    }
                                    continue;
                                }
                            }
                            // iftrue(Label_0425:, Math.abs(n2 - n) <= 5000000L)
                            catch (Exception ex) {
                                this.getLatencyMethod = null;
                                continue;
                            }
                            break;
                        }
                        break;
                    }
                }
            }
        }
    }
    
    private void releaseKeepSessionIdAudioTrack() {
        if (this.keepSessionIdAudioTrack == null) {
            return;
        }
        final android.media.AudioTrack keepSessionIdAudioTrack = this.keepSessionIdAudioTrack;
        this.keepSessionIdAudioTrack = null;
        new Thread() {
            @Override
            public void run() {
                keepSessionIdAudioTrack.release();
            }
        }.start();
    }
    
    private void resetSyncParams() {
        this.smoothedPlayheadOffsetUs = 0L;
        this.playheadOffsetCount = 0;
        this.nextPlayheadOffsetIndex = 0;
        this.lastPlayheadSampleTimeUs = 0L;
        this.audioTimestampSet = false;
        this.lastTimestampSampleTimeUs = 0L;
    }
    
    private void setAudioTrackVolume() {
        if (!this.isInitialized()) {
            return;
        }
        if (Util.SDK_INT >= 21) {
            setAudioTrackVolumeV21(this.audioTrack, this.volume);
            return;
        }
        setAudioTrackVolumeV3(this.audioTrack, this.volume);
    }
    
    @TargetApi(21)
    private static void setAudioTrackVolumeV21(final android.media.AudioTrack audioTrack, final float volume) {
        audioTrack.setVolume(volume);
    }
    
    private static void setAudioTrackVolumeV3(final android.media.AudioTrack audioTrack, final float n) {
        audioTrack.setStereoVolume(n, n);
    }
    
    @TargetApi(21)
    private static int writeNonBlockingV21(final android.media.AudioTrack audioTrack, final ByteBuffer byteBuffer, final int n) {
        return audioTrack.write(byteBuffer, n, 1);
    }
    
    public long getCurrentPositionUs(final boolean b) {
        long n;
        if (!this.hasCurrentPositionUs()) {
            n = Long.MIN_VALUE;
        }
        else {
            if (this.audioTrack.getPlayState() == 3) {
                this.maybeSampleSyncParams();
            }
            final long n2 = System.nanoTime() / 1000L;
            if (this.audioTimestampSet) {
                return this.framesToDurationUs(this.audioTrackUtil.getTimestampFramePosition() + this.durationUsToFrames(n2 - this.audioTrackUtil.getTimestampNanoTime() / 1000L)) + this.startMediaTimeUs;
            }
            long n3;
            if (this.playheadOffsetCount == 0) {
                n3 = this.audioTrackUtil.getPlaybackHeadPositionUs() + this.startMediaTimeUs;
            }
            else {
                n3 = this.smoothedPlayheadOffsetUs + n2 + this.startMediaTimeUs;
            }
            n = n3;
            if (!b) {
                return n3 - this.latencyUs;
            }
        }
        return n;
    }
    
    public int handleBuffer(final ByteBuffer byteBuffer, int n, int write, long n2) throws WriteException {
        if (write == 0) {
            n = 2;
        }
        else {
            if (Util.SDK_INT <= 22 && this.isPassthrough()) {
                if (this.audioTrack.getPlayState() == 2) {
                    return 0;
                }
                if (this.audioTrack.getPlayState() == 1 && this.audioTrackUtil.getPlaybackHeadPosition() != 0L) {
                    return 0;
                }
            }
            int n4;
            final int n3 = n4 = 0;
            if (this.temporaryBufferSize == 0) {
                if (this.isPassthrough() && this.passthroughBitrate == 0) {
                    this.passthroughBitrate = Ac3Util.getBitrate(write, this.sampleRate);
                }
                n2 -= this.framesToDurationUs(this.bytesToFrames(write));
                if (this.startMediaTimeState == 0) {
                    this.startMediaTimeUs = Math.max(0L, n2);
                    this.startMediaTimeState = 1;
                    n4 = n3;
                }
                else {
                    final long n5 = this.startMediaTimeUs + this.framesToDurationUs(this.bytesToFrames(this.submittedBytes));
                    if (this.startMediaTimeState == 1 && Math.abs(n5 - n2) > 200000L) {
                        Log.e("AudioTrack", "Discontinuity detected [expected " + n5 + ", got " + n2 + "]");
                        this.startMediaTimeState = 2;
                    }
                    n4 = n3;
                    if (this.startMediaTimeState == 2) {
                        this.startMediaTimeUs += n2 - n5;
                        this.startMediaTimeState = 1;
                        n4 = ((false | true) ? 1 : 0);
                    }
                }
            }
            if (this.temporaryBufferSize == 0) {
                this.temporaryBufferSize = write;
                byteBuffer.position(n);
                if (Util.SDK_INT < 21) {
                    if (this.temporaryBuffer == null || this.temporaryBuffer.length < write) {
                        this.temporaryBuffer = new byte[write];
                    }
                    byteBuffer.get(this.temporaryBuffer, 0, write);
                    this.temporaryBufferOffset = 0;
                }
            }
            n = 0;
            if (Util.SDK_INT < 21) {
                write = (int)(this.submittedBytes - this.audioTrackUtil.getPlaybackHeadPosition() * this.frameSize);
                write = this.bufferSize - write;
                if (write > 0) {
                    n = Math.min(this.temporaryBufferSize, write);
                    write = this.audioTrack.write(this.temporaryBuffer, this.temporaryBufferOffset, n);
                    if ((n = write) >= 0) {
                        this.temporaryBufferOffset += write;
                        n = write;
                    }
                }
            }
            else {
                n = writeNonBlockingV21(this.audioTrack, byteBuffer, this.temporaryBufferSize);
            }
            if (n < 0) {
                throw new WriteException(n);
            }
            this.temporaryBufferSize -= n;
            this.submittedBytes += n;
            n = n4;
            if (this.temporaryBufferSize == 0) {
                return n4 | 0x2;
            }
        }
        return n;
    }
    
    public void handleDiscontinuity() {
        if (this.startMediaTimeState == 1) {
            this.startMediaTimeState = 2;
        }
    }
    
    public void handleEndOfStream() {
        if (this.isInitialized()) {
            this.audioTrackUtil.handleEndOfStream(this.bytesToFrames(this.submittedBytes));
        }
    }
    
    public boolean hasPendingData() {
        return this.isInitialized() && (this.bytesToFrames(this.submittedBytes) > this.audioTrackUtil.getPlaybackHeadPosition() || this.audioTrackUtil.overrideHasPendingData());
    }
    
    public int initialize() throws InitializationException {
        return this.initialize(0);
    }
    
    public int initialize(int audioSessionId) throws InitializationException {
        this.releasingConditionVariable.block();
        if (audioSessionId == 0) {
            this.audioTrack = new android.media.AudioTrack(this.streamType, this.sampleRate, this.channelConfig, this.encoding, this.bufferSize, 1);
        }
        else {
            this.audioTrack = new android.media.AudioTrack(this.streamType, this.sampleRate, this.channelConfig, this.encoding, this.bufferSize, 1, audioSessionId);
        }
        this.checkAudioTrackInitialized();
        audioSessionId = this.audioTrack.getAudioSessionId();
        if (AudioTrack.enablePreV21AudioSessionWorkaround && Util.SDK_INT < 21) {
            if (this.keepSessionIdAudioTrack != null && audioSessionId != this.keepSessionIdAudioTrack.getAudioSessionId()) {
                this.releaseKeepSessionIdAudioTrack();
            }
            if (this.keepSessionIdAudioTrack == null) {
                this.keepSessionIdAudioTrack = new android.media.AudioTrack(this.streamType, 4000, 4, 2, 2, 0, audioSessionId);
            }
        }
        this.audioTrackUtil.reconfigure(this.audioTrack, this.isPassthrough());
        this.setAudioTrackVolume();
        return audioSessionId;
    }
    
    public boolean isInitialized() {
        return this.audioTrack != null;
    }
    
    public boolean isPassthroughSupported(final String s) {
        return this.audioCapabilities != null && this.audioCapabilities.supportsEncoding(getEncodingForMimeType(s));
    }
    
    public void pause() {
        if (this.isInitialized()) {
            this.resetSyncParams();
            this.audioTrackUtil.pause();
        }
    }
    
    public void play() {
        if (this.isInitialized()) {
            this.resumeSystemTimeUs = System.nanoTime() / 1000L;
            this.audioTrack.play();
        }
    }
    
    public void reconfigure(final MediaFormat mediaFormat, final boolean b) {
        this.reconfigure(mediaFormat, b, 0);
    }
    
    public void reconfigure(final MediaFormat mediaFormat, final boolean b, int n) {
        final int integer = mediaFormat.getInteger("channel-count");
        int channelConfig = 0;
        switch (integer) {
            default: {
                throw new IllegalArgumentException("Unsupported channel count: " + integer);
            }
            case 1: {
                channelConfig = 4;
                break;
            }
            case 2: {
                channelConfig = 12;
                break;
            }
            case 6: {
                channelConfig = 252;
                break;
            }
            case 8: {
                channelConfig = 1020;
                break;
            }
        }
        final int integer2 = mediaFormat.getInteger("sample-rate");
        final String string = mediaFormat.getString("mime");
        int encodingForMimeType;
        if (b) {
            encodingForMimeType = getEncodingForMimeType(string);
        }
        else {
            encodingForMimeType = 2;
        }
        if (this.isInitialized() && this.sampleRate == integer2 && this.channelConfig == channelConfig && this.encoding == encodingForMimeType) {
            return;
        }
        this.reset();
        this.encoding = encodingForMimeType;
        this.sampleRate = integer2;
        this.channelConfig = channelConfig;
        this.passthroughBitrate = 0;
        this.frameSize = integer * 2;
        this.minBufferSize = android.media.AudioTrack.getMinBufferSize(integer2, channelConfig, encodingForMimeType);
        Assertions.checkState(this.minBufferSize != -2);
        if (n != 0) {
            this.bufferSize = n;
            return;
        }
        n = this.minBufferSize * 4;
        final int n2 = (int)this.durationUsToFrames(250000L) * this.frameSize;
        final int n3 = (int)Math.max(this.minBufferSize, this.durationUsToFrames(750000L) * this.frameSize);
        if (n < n2) {
            n = n2;
        }
        else if (n > n3) {
            n = n3;
        }
        this.bufferSize = n;
    }
    
    public void release() {
        this.reset();
        this.releaseKeepSessionIdAudioTrack();
    }
    
    public void reset() {
        if (this.isInitialized()) {
            this.submittedBytes = 0L;
            this.temporaryBufferSize = 0;
            this.startMediaTimeState = 0;
            this.latencyUs = 0L;
            this.resetSyncParams();
            if (this.audioTrack.getPlayState() == 3) {
                this.audioTrack.pause();
            }
            final android.media.AudioTrack audioTrack = this.audioTrack;
            this.audioTrack = null;
            this.audioTrackUtil.reconfigure(null, false);
            this.releasingConditionVariable.close();
            new Thread() {
                @Override
                public void run() {
                    try {
                        audioTrack.flush();
                        audioTrack.release();
                    }
                    finally {
                        AudioTrack.this.releasingConditionVariable.open();
                    }
                }
            }.start();
        }
    }
    
    public void setVolume(final float volume) {
        if (this.volume != volume) {
            this.volume = volume;
            this.setAudioTrackVolume();
        }
    }
    
    private static class AudioTrackUtil
    {
        protected android.media.AudioTrack audioTrack;
        private long endPlaybackHeadPosition;
        private boolean isPassthrough;
        private long lastRawPlaybackHeadPosition;
        private long passthroughWorkaroundPauseOffset;
        private long rawPlaybackHeadWrapCount;
        private int sampleRate;
        private long stopPlaybackHeadPosition;
        private long stopTimestampUs;
        
        public long getPlaybackHeadPosition() {
            long min = 0L;
            if (this.stopTimestampUs != -1L) {
                min = Math.min(this.endPlaybackHeadPosition, this.stopPlaybackHeadPosition + this.sampleRate * (SystemClock.elapsedRealtime() * 1000L - this.stopTimestampUs) / 1000000L);
            }
            else {
                final int playState = this.audioTrack.getPlayState();
                if (playState != 1) {
                    long lastRawPlaybackHeadPosition;
                    final long n = lastRawPlaybackHeadPosition = (0xFFFFFFFFL & this.audioTrack.getPlaybackHeadPosition());
                    if (Util.SDK_INT <= 22) {
                        lastRawPlaybackHeadPosition = n;
                        if (this.isPassthrough) {
                            if (playState == 2 && n == 0L) {
                                this.passthroughWorkaroundPauseOffset = this.lastRawPlaybackHeadPosition;
                            }
                            lastRawPlaybackHeadPosition = n + this.passthroughWorkaroundPauseOffset;
                        }
                    }
                    if (this.lastRawPlaybackHeadPosition > lastRawPlaybackHeadPosition) {
                        ++this.rawPlaybackHeadWrapCount;
                    }
                    this.lastRawPlaybackHeadPosition = lastRawPlaybackHeadPosition;
                    return (this.rawPlaybackHeadWrapCount << 32) + lastRawPlaybackHeadPosition;
                }
            }
            return min;
        }
        
        public long getPlaybackHeadPositionUs() {
            return this.getPlaybackHeadPosition() * 1000000L / this.sampleRate;
        }
        
        public long getTimestampFramePosition() {
            throw new UnsupportedOperationException();
        }
        
        public long getTimestampNanoTime() {
            throw new UnsupportedOperationException();
        }
        
        public void handleEndOfStream(final long endPlaybackHeadPosition) {
            this.stopPlaybackHeadPosition = this.getPlaybackHeadPosition();
            this.stopTimestampUs = SystemClock.elapsedRealtime() * 1000L;
            this.endPlaybackHeadPosition = endPlaybackHeadPosition;
            this.audioTrack.stop();
        }
        
        public boolean overrideHasPendingData() {
            return Util.SDK_INT <= 22 && this.isPassthrough && this.audioTrack.getPlayState() == 2 && this.audioTrack.getPlaybackHeadPosition() == 0;
        }
        
        public void pause() {
            if (this.stopTimestampUs != -1L) {
                return;
            }
            this.audioTrack.pause();
        }
        
        public void reconfigure(final android.media.AudioTrack audioTrack, final boolean isPassthrough) {
            this.audioTrack = audioTrack;
            this.isPassthrough = isPassthrough;
            this.stopTimestampUs = -1L;
            this.lastRawPlaybackHeadPosition = 0L;
            this.rawPlaybackHeadWrapCount = 0L;
            this.passthroughWorkaroundPauseOffset = 0L;
            if (audioTrack != null) {
                this.sampleRate = audioTrack.getSampleRate();
            }
        }
        
        public boolean updateTimestamp() {
            return false;
        }
    }
    
    @TargetApi(19)
    private static class AudioTrackUtilV19 extends AudioTrackUtil
    {
        private final AudioTimestamp audioTimestamp;
        private long lastRawTimestampFramePosition;
        private long lastTimestampFramePosition;
        private long rawTimestampFramePositionWrapCount;
        
        public AudioTrackUtilV19() {
            this.audioTimestamp = new AudioTimestamp();
        }
        
        @Override
        public long getTimestampFramePosition() {
            return this.lastTimestampFramePosition;
        }
        
        @Override
        public long getTimestampNanoTime() {
            return this.audioTimestamp.nanoTime;
        }
        
        @Override
        public void reconfigure(final android.media.AudioTrack audioTrack, final boolean b) {
            super.reconfigure(audioTrack, b);
            this.rawTimestampFramePositionWrapCount = 0L;
            this.lastRawTimestampFramePosition = 0L;
            this.lastTimestampFramePosition = 0L;
        }
        
        @Override
        public boolean updateTimestamp() {
            final boolean timestamp = this.audioTrack.getTimestamp(this.audioTimestamp);
            if (timestamp) {
                final long framePosition = this.audioTimestamp.framePosition;
                if (this.lastRawTimestampFramePosition > framePosition) {
                    ++this.rawTimestampFramePositionWrapCount;
                }
                this.lastRawTimestampFramePosition = framePosition;
                this.lastTimestampFramePosition = (this.rawTimestampFramePositionWrapCount << 32) + framePosition;
            }
            return timestamp;
        }
    }
    
    public static final class InitializationException extends Exception
    {
        public final int audioTrackState;
        
        public InitializationException(final int audioTrackState, final int n, final int n2, final int n3) {
            super("AudioTrack init failed: " + audioTrackState + ", Config(" + n + ", " + n2 + ", " + n3 + ")");
            this.audioTrackState = audioTrackState;
        }
    }
    
    public static final class InvalidAudioTrackTimestampException extends RuntimeException
    {
        public InvalidAudioTrackTimestampException(final String s) {
            super(s);
        }
    }
    
    public static final class WriteException extends Exception
    {
        public final int errorCode;
        
        public WriteException(final int errorCode) {
            super("AudioTrack write failed: " + errorCode);
            this.errorCode = errorCode;
        }
    }
}
