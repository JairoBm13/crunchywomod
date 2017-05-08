// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.hls;

import java.util.List;

public final class HlsMediaPlaylist extends HlsPlaylist
{
    public final long durationUs;
    public final boolean live;
    public final int mediaSequence;
    public final List<Segment> segments;
    public final int targetDurationSecs;
    public final int version;
    
    public HlsMediaPlaylist(final String s, final int mediaSequence, final int targetDurationSecs, final int version, final boolean live, final List<Segment> segments) {
        super(s, 1);
        this.mediaSequence = mediaSequence;
        this.targetDurationSecs = targetDurationSecs;
        this.version = version;
        this.live = live;
        this.segments = segments;
        if (!segments.isEmpty()) {
            final Segment segment = segments.get(segments.size() - 1);
            this.durationUs = segment.startTimeUs + (long)(segment.durationSecs * 1000000.0);
            return;
        }
        this.durationUs = 0L;
    }
    
    public static final class Segment implements Comparable<Long>
    {
        public final int byterangeLength;
        public final int byterangeOffset;
        public final boolean discontinuity;
        public final double durationSecs;
        public final String encryptionIV;
        public final String encryptionKeyUri;
        public final boolean isEncrypted;
        public final long startTimeUs;
        public final String url;
        
        public Segment(final String url, final double durationSecs, final boolean discontinuity, final long startTimeUs, final boolean isEncrypted, final String encryptionKeyUri, final String encryptionIV, final int byterangeOffset, final int byterangeLength) {
            this.url = url;
            this.durationSecs = durationSecs;
            this.discontinuity = discontinuity;
            this.startTimeUs = startTimeUs;
            this.isEncrypted = isEncrypted;
            this.encryptionKeyUri = encryptionKeyUri;
            this.encryptionIV = encryptionIV;
            this.byterangeOffset = byterangeOffset;
            this.byterangeLength = byterangeLength;
        }
        
        @Override
        public int compareTo(final Long n) {
            if (this.startTimeUs > n) {
                return 1;
            }
            if (this.startTimeUs < n) {
                return -1;
            }
            return 0;
        }
    }
}
