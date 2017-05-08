// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.hls;

import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.extractor.SeekMap;
import java.io.IOException;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.SampleHolder;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.extractor.DefaultTrackOutput;
import android.util.SparseArray;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.extractor.ExtractorOutput;

public final class HlsExtractorWrapper implements ExtractorOutput
{
    private final int adaptiveMaxHeight;
    private final int adaptiveMaxWidth;
    private Allocator allocator;
    private final Extractor extractor;
    public final Format format;
    private boolean prepared;
    private MediaFormat[] sampleQueueFormats;
    private final SparseArray<DefaultTrackOutput> sampleQueues;
    private final boolean shouldSpliceIn;
    private boolean spliceConfigured;
    public final long startTimeUs;
    private volatile boolean tracksBuilt;
    public final int trigger;
    
    public HlsExtractorWrapper(final int trigger, final Format format, final long startTimeUs, final Extractor extractor, final boolean shouldSpliceIn, final int adaptiveMaxWidth, final int adaptiveMaxHeight) {
        this.trigger = trigger;
        this.format = format;
        this.startTimeUs = startTimeUs;
        this.extractor = extractor;
        this.shouldSpliceIn = shouldSpliceIn;
        this.adaptiveMaxWidth = adaptiveMaxWidth;
        this.adaptiveMaxHeight = adaptiveMaxHeight;
        this.sampleQueues = (SparseArray<DefaultTrackOutput>)new SparseArray();
    }
    
    public void clear() {
        for (int i = 0; i < this.sampleQueues.size(); ++i) {
            ((DefaultTrackOutput)this.sampleQueues.valueAt(i)).clear();
        }
    }
    
    public final void configureSpliceTo(final HlsExtractorWrapper hlsExtractorWrapper) {
        Assertions.checkState(this.isPrepared());
        if (this.spliceConfigured || !hlsExtractorWrapper.shouldSpliceIn || !hlsExtractorWrapper.isPrepared()) {
            return;
        }
        boolean spliceConfigured = true;
        for (int trackCount = this.getTrackCount(), i = 0; i < trackCount; ++i) {
            spliceConfigured &= ((DefaultTrackOutput)this.sampleQueues.valueAt(i)).configureSpliceTo((DefaultTrackOutput)hlsExtractorWrapper.sampleQueues.valueAt(i));
        }
        this.spliceConfigured = spliceConfigured;
    }
    
    public void discardUntil(final int n, final long n2) {
        Assertions.checkState(this.isPrepared());
        ((DefaultTrackOutput)this.sampleQueues.valueAt(n)).discardUntil(n2);
    }
    
    @Override
    public void endTracks() {
        this.tracksBuilt = true;
    }
    
    public long getLargestParsedTimestampUs() {
        long max = Long.MIN_VALUE;
        for (int i = 0; i < this.sampleQueues.size(); ++i) {
            max = Math.max(max, ((DefaultTrackOutput)this.sampleQueues.valueAt(i)).getLargestParsedTimestampUs());
        }
        return max;
    }
    
    public MediaFormat getMediaFormat(final int n) {
        Assertions.checkState(this.isPrepared());
        return this.sampleQueueFormats[n];
    }
    
    public boolean getSample(final int n, final SampleHolder sampleHolder) {
        Assertions.checkState(this.isPrepared());
        return ((DefaultTrackOutput)this.sampleQueues.valueAt(n)).getSample(sampleHolder);
    }
    
    public int getTrackCount() {
        Assertions.checkState(this.isPrepared());
        return this.sampleQueues.size();
    }
    
    public boolean hasSamples(final int n) {
        Assertions.checkState(this.isPrepared());
        return !((DefaultTrackOutput)this.sampleQueues.valueAt(n)).isEmpty();
    }
    
    public void init(final Allocator allocator) {
        this.allocator = allocator;
        this.extractor.init(this);
    }
    
    public boolean isPrepared() {
        if (!this.prepared && this.tracksBuilt) {
            for (int i = 0; i < this.sampleQueues.size(); ++i) {
                if (!((DefaultTrackOutput)this.sampleQueues.valueAt(i)).hasFormat()) {
                    return false;
                }
            }
            this.prepared = true;
            this.sampleQueueFormats = new MediaFormat[this.sampleQueues.size()];
            for (int j = 0; j < this.sampleQueueFormats.length; ++j) {
                MediaFormat mediaFormat2;
                final MediaFormat mediaFormat = mediaFormat2 = ((DefaultTrackOutput)this.sampleQueues.valueAt(j)).getFormat();
                Label_0141: {
                    if (MimeTypes.isVideo(mediaFormat.mimeType)) {
                        if (this.adaptiveMaxWidth == -1) {
                            mediaFormat2 = mediaFormat;
                            if (this.adaptiveMaxHeight == -1) {
                                break Label_0141;
                            }
                        }
                        mediaFormat2 = mediaFormat.copyWithMaxVideoDimensions(this.adaptiveMaxWidth, this.adaptiveMaxHeight);
                    }
                }
                this.sampleQueueFormats[j] = mediaFormat2;
            }
        }
        return this.prepared;
    }
    
    public int read(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        boolean b = true;
        final int read = this.extractor.read(extractorInput, null);
        if (read == 1) {
            b = false;
        }
        Assertions.checkState(b);
        return read;
    }
    
    @Override
    public void seekMap(final SeekMap seekMap) {
    }
    
    @Override
    public TrackOutput track(final int n) {
        final DefaultTrackOutput defaultTrackOutput = new DefaultTrackOutput(this.allocator);
        this.sampleQueues.put(n, (Object)defaultTrackOutput);
        return defaultTrackOutput;
    }
}
