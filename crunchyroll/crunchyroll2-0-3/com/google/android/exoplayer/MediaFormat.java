// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer;

import android.annotation.SuppressLint;
import java.nio.ByteBuffer;
import java.util.Arrays;
import com.google.android.exoplayer.util.Util;
import android.annotation.TargetApi;
import java.util.Collections;
import com.google.android.exoplayer.util.Assertions;
import java.util.List;

public final class MediaFormat
{
    public final boolean adaptive;
    public final int bitrate;
    public final int channelCount;
    public final long durationUs;
    private android.media.MediaFormat frameworkMediaFormat;
    private int hashCode;
    public final int height;
    public final List<byte[]> initializationData;
    public final String language;
    public final int maxHeight;
    public final int maxInputSize;
    public final int maxWidth;
    public final String mimeType;
    public final float pixelWidthHeightRatio;
    public final int rotationDegrees;
    public final int sampleRate;
    public final long subsampleOffsetUs;
    public final int trackId;
    public final int width;
    
    MediaFormat(final int trackId, final String s, final int bitrate, final int maxInputSize, final long durationUs, final int width, final int height, final int rotationDegrees, final float pixelWidthHeightRatio, final int channelCount, final int sampleRate, final String language, final long subsampleOffsetUs, final List<byte[]> list, final boolean adaptive, final int maxWidth, final int maxHeight) {
        this.trackId = trackId;
        this.mimeType = Assertions.checkNotEmpty(s);
        this.bitrate = bitrate;
        this.maxInputSize = maxInputSize;
        this.durationUs = durationUs;
        this.width = width;
        this.height = height;
        this.rotationDegrees = rotationDegrees;
        this.pixelWidthHeightRatio = pixelWidthHeightRatio;
        this.channelCount = channelCount;
        this.sampleRate = sampleRate;
        this.language = language;
        this.subsampleOffsetUs = subsampleOffsetUs;
        List<byte[]> emptyList = list;
        if (list == null) {
            emptyList = Collections.emptyList();
        }
        this.initializationData = emptyList;
        this.adaptive = adaptive;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }
    
    public static MediaFormat createAudioFormat(final int n, final String s, final int n2, final int n3, final long n4, final int n5, final int n6, final List<byte[]> list, final String s2) {
        return new MediaFormat(n, s, n2, n3, n4, -1, -1, -1, -1.0f, n5, n6, s2, Long.MAX_VALUE, list, false, -1, -1);
    }
    
    public static MediaFormat createFormatForMimeType(final int n, final String s, final int n2, final long n3) {
        return new MediaFormat(n, s, n2, -1, n3, -1, -1, -1, -1.0f, -1, -1, null, Long.MAX_VALUE, null, false, -1, -1);
    }
    
    public static MediaFormat createTextFormat(final int n, final String s, final int n2, final long n3, final String s2) {
        return createTextFormat(n, s, n2, n3, s2, Long.MAX_VALUE);
    }
    
    public static MediaFormat createTextFormat(final int n, final String s, final int n2, final long n3, final String s2, final long n4) {
        return new MediaFormat(n, s, n2, -1, n3, -1, -1, -1, -1.0f, -1, -1, s2, n4, null, false, -1, -1);
    }
    
    public static MediaFormat createVideoFormat(final int n, final String s, final int n2, final int n3, final long n4, final int n5, final int n6, final List<byte[]> list, final int n7, final float n8) {
        return new MediaFormat(n, s, n2, n3, n4, n5, n6, n7, n8, -1, -1, null, Long.MAX_VALUE, list, false, -1, -1);
    }
    
    @TargetApi(16)
    private static final void maybeSetIntegerV16(final android.media.MediaFormat mediaFormat, final String s, final int n) {
        if (n != -1) {
            mediaFormat.setInteger(s, n);
        }
    }
    
    @TargetApi(16)
    private static final void maybeSetStringV16(final android.media.MediaFormat mediaFormat, final String s, final String s2) {
        if (s2 != null) {
            mediaFormat.setString(s, s2);
        }
    }
    
    public MediaFormat copyAsAdaptive() {
        return new MediaFormat(this.trackId, this.mimeType, -1, -1, this.durationUs, -1, -1, -1, -1.0f, -1, -1, null, Long.MAX_VALUE, null, true, this.maxWidth, this.maxHeight);
    }
    
    public MediaFormat copyWithDurationUs(final long n) {
        return new MediaFormat(this.trackId, this.mimeType, this.bitrate, this.maxInputSize, n, this.width, this.height, this.rotationDegrees, this.pixelWidthHeightRatio, this.channelCount, this.sampleRate, this.language, this.subsampleOffsetUs, this.initializationData, this.adaptive, this.maxWidth, this.maxHeight);
    }
    
    public MediaFormat copyWithMaxVideoDimensions(final int n, final int n2) {
        return new MediaFormat(this.trackId, this.mimeType, this.bitrate, this.maxInputSize, this.durationUs, this.width, this.height, this.rotationDegrees, this.pixelWidthHeightRatio, this.channelCount, this.sampleRate, this.language, this.subsampleOffsetUs, this.initializationData, this.adaptive, n, n2);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final MediaFormat mediaFormat = (MediaFormat)o;
        if (this.adaptive != mediaFormat.adaptive || this.bitrate != mediaFormat.bitrate || this.maxInputSize != mediaFormat.maxInputSize || this.width != mediaFormat.width || this.height != mediaFormat.height || this.rotationDegrees != mediaFormat.rotationDegrees || this.pixelWidthHeightRatio != mediaFormat.pixelWidthHeightRatio || this.maxWidth != mediaFormat.maxWidth || this.maxHeight != mediaFormat.maxHeight || this.channelCount != mediaFormat.channelCount || this.sampleRate != mediaFormat.sampleRate || this.trackId != mediaFormat.trackId || !Util.areEqual(this.language, mediaFormat.language) || !Util.areEqual(this.mimeType, mediaFormat.mimeType) || this.initializationData.size() != mediaFormat.initializationData.size()) {
            return false;
        }
        for (int i = 0; i < this.initializationData.size(); ++i) {
            if (!Arrays.equals(this.initializationData.get(i), mediaFormat.initializationData.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    @SuppressLint({ "InlinedApi" })
    @TargetApi(16)
    public final android.media.MediaFormat getFrameworkMediaFormatV16() {
        if (this.frameworkMediaFormat == null) {
            final android.media.MediaFormat frameworkMediaFormat = new android.media.MediaFormat();
            frameworkMediaFormat.setString("mime", this.mimeType);
            maybeSetStringV16(frameworkMediaFormat, "language", this.language);
            maybeSetIntegerV16(frameworkMediaFormat, "max-input-size", this.maxInputSize);
            maybeSetIntegerV16(frameworkMediaFormat, "width", this.width);
            maybeSetIntegerV16(frameworkMediaFormat, "height", this.height);
            maybeSetIntegerV16(frameworkMediaFormat, "rotation-degrees", this.rotationDegrees);
            maybeSetIntegerV16(frameworkMediaFormat, "max-width", this.maxWidth);
            maybeSetIntegerV16(frameworkMediaFormat, "max-height", this.maxHeight);
            maybeSetIntegerV16(frameworkMediaFormat, "channel-count", this.channelCount);
            maybeSetIntegerV16(frameworkMediaFormat, "sample-rate", this.sampleRate);
            for (int i = 0; i < this.initializationData.size(); ++i) {
                frameworkMediaFormat.setByteBuffer("csd-" + i, ByteBuffer.wrap(this.initializationData.get(i)));
            }
            if (this.durationUs != -1L) {
                frameworkMediaFormat.setLong("durationUs", this.durationUs);
            }
            this.frameworkMediaFormat = frameworkMediaFormat;
        }
        return this.frameworkMediaFormat;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        if (this.hashCode == 0) {
            final int trackId = this.trackId;
            int hashCode2;
            if (this.mimeType == null) {
                hashCode2 = 0;
            }
            else {
                hashCode2 = this.mimeType.hashCode();
            }
            final int bitrate = this.bitrate;
            final int maxInputSize = this.maxInputSize;
            final int width = this.width;
            final int height = this.height;
            final int rotationDegrees = this.rotationDegrees;
            final int floatToRawIntBits = Float.floatToRawIntBits(this.pixelWidthHeightRatio);
            final int n = (int)this.durationUs;
            int n2;
            if (this.adaptive) {
                n2 = 1231;
            }
            else {
                n2 = 1237;
            }
            final int maxWidth = this.maxWidth;
            final int maxHeight = this.maxHeight;
            final int channelCount = this.channelCount;
            final int sampleRate = this.sampleRate;
            if (this.language != null) {
                hashCode = this.language.hashCode();
            }
            int hashCode3 = ((((((((((((((trackId + 527) * 31 + hashCode2) * 31 + bitrate) * 31 + maxInputSize) * 31 + width) * 31 + height) * 31 + rotationDegrees) * 31 + floatToRawIntBits) * 31 + n) * 31 + n2) * 31 + maxWidth) * 31 + maxHeight) * 31 + channelCount) * 31 + sampleRate) * 31 + hashCode;
            for (int i = 0; i < this.initializationData.size(); ++i) {
                hashCode3 = hashCode3 * 31 + Arrays.hashCode(this.initializationData.get(i));
            }
            this.hashCode = hashCode3;
        }
        return this.hashCode;
    }
    
    @Override
    public String toString() {
        return "MediaFormat(" + this.trackId + ", " + this.mimeType + ", " + this.bitrate + ", " + this.maxInputSize + ", " + this.width + ", " + this.height + ", " + this.rotationDegrees + ", " + this.pixelWidthHeightRatio + ", " + this.channelCount + ", " + this.sampleRate + ", " + this.language + ", " + this.durationUs + ", " + this.adaptive + ", " + this.maxWidth + ", " + this.maxHeight + ")";
    }
}
