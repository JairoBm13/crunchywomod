// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.hls;

import com.google.android.exoplayer.chunk.BaseChunkSampleSourceEventListener;
import com.google.android.exoplayer.SampleHolder;
import com.google.android.exoplayer.MediaFormatHolder;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.Assertions;
import android.os.SystemClock;
import com.google.android.exoplayer.LoadControl;
import java.util.LinkedList;
import android.os.Handler;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.chunk.Format;
import java.io.IOException;
import com.google.android.exoplayer.chunk.Chunk;
import com.google.android.exoplayer.chunk.ChunkOperationHolder;
import com.google.android.exoplayer.upstream.Loader;
import com.google.android.exoplayer.SampleSource;

public final class HlsSampleSource implements SampleSource, SampleSourceReader, Callback
{
    private final int bufferSizeContribution;
    private final ChunkOperationHolder chunkOperationHolder;
    private final HlsChunkSource chunkSource;
    private long currentLoadStartTimeMs;
    private Chunk currentLoadable;
    private IOException currentLoadableException;
    private int currentLoadableExceptionCount;
    private long currentLoadableExceptionTimestamp;
    private TsChunk currentTsLoadable;
    private Format downstreamFormat;
    private MediaFormat[] downstreamMediaFormats;
    private long downstreamPositionUs;
    private int enabledTrackCount;
    private final Handler eventHandler;
    private final EventListener eventListener;
    private final int eventSourceId;
    private final LinkedList<HlsExtractorWrapper> extractors;
    private long lastSeekPositionUs;
    private final LoadControl loadControl;
    private boolean loadControlRegistered;
    private Loader loader;
    private boolean loadingFinished;
    private final int minLoadableRetryCount;
    private boolean[] pendingDiscontinuities;
    private long pendingResetPositionUs;
    private boolean prepared;
    private TsChunk previousTsLoadable;
    private int remainingReleaseCount;
    private int trackCount;
    private boolean[] trackEnabledStates;
    private MediaFormat[] trackFormat;
    
    public HlsSampleSource(final HlsChunkSource hlsChunkSource, final LoadControl loadControl, final int n) {
        this(hlsChunkSource, loadControl, n, null, null, 0);
    }
    
    public HlsSampleSource(final HlsChunkSource hlsChunkSource, final LoadControl loadControl, final int n, final Handler handler, final EventListener eventListener, final int n2) {
        this(hlsChunkSource, loadControl, n, handler, eventListener, n2, 3);
    }
    
    public HlsSampleSource(final HlsChunkSource chunkSource, final LoadControl loadControl, final int bufferSizeContribution, final Handler eventHandler, final EventListener eventListener, final int eventSourceId, final int minLoadableRetryCount) {
        this.chunkSource = chunkSource;
        this.loadControl = loadControl;
        this.bufferSizeContribution = bufferSizeContribution;
        this.minLoadableRetryCount = minLoadableRetryCount;
        this.eventHandler = eventHandler;
        this.eventListener = eventListener;
        this.eventSourceId = eventSourceId;
        this.pendingResetPositionUs = Long.MIN_VALUE;
        this.extractors = new LinkedList<HlsExtractorWrapper>();
        this.chunkOperationHolder = new ChunkOperationHolder();
    }
    
    private void clearCurrentLoadable() {
        this.currentTsLoadable = null;
        this.currentLoadable = null;
        this.currentLoadableException = null;
        this.currentLoadableExceptionCount = 0;
    }
    
    private void clearState() {
        for (int i = 0; i < this.extractors.size(); ++i) {
            this.extractors.get(i).clear();
        }
        this.extractors.clear();
        this.clearCurrentLoadable();
        this.previousTsLoadable = null;
    }
    
    private void discardSamplesForDisabledTracks(final HlsExtractorWrapper hlsExtractorWrapper, final long n) {
        if (hlsExtractorWrapper.isPrepared()) {
            for (int i = 0; i < this.trackEnabledStates.length; ++i) {
                if (!this.trackEnabledStates[i]) {
                    hlsExtractorWrapper.discardUntil(i, n);
                }
            }
        }
    }
    
    private HlsExtractorWrapper getCurrentExtractor() {
        HlsExtractorWrapper hlsExtractorWrapper;
        for (hlsExtractorWrapper = this.extractors.getFirst(); this.extractors.size() > 1 && !this.haveSamplesForEnabledTracks(hlsExtractorWrapper); hlsExtractorWrapper = this.extractors.getFirst()) {
            this.extractors.removeFirst().clear();
        }
        return hlsExtractorWrapper;
    }
    
    private long getNextLoadPositionUs() {
        if (this.isPendingReset()) {
            return this.pendingResetPositionUs;
        }
        if (this.loadingFinished) {
            return -1L;
        }
        if (this.currentTsLoadable != null) {
            return this.currentTsLoadable.endTimeUs;
        }
        return this.previousTsLoadable.endTimeUs;
    }
    
    private long getRetryDelayMillis(final long n) {
        return Math.min((n - 1L) * 1000L, 5000L);
    }
    
    private boolean haveSamplesForEnabledTracks(final HlsExtractorWrapper hlsExtractorWrapper) {
        if (hlsExtractorWrapper.isPrepared()) {
            for (int i = 0; i < this.trackEnabledStates.length; ++i) {
                if (this.trackEnabledStates[i] && hlsExtractorWrapper.hasSamples(i)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isPendingReset() {
        return this.pendingResetPositionUs != Long.MIN_VALUE;
    }
    
    private boolean isTsChunk(final Chunk chunk) {
        return chunk instanceof TsChunk;
    }
    
    private void maybeStartLoading() {
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        final long nextLoadPositionUs = this.getNextLoadPositionUs();
        boolean b;
        if (this.currentLoadableException != null) {
            b = true;
        }
        else {
            b = false;
        }
        final boolean update = this.loadControl.update(this, this.downstreamPositionUs, nextLoadPositionUs, this.loader.isLoading() || b);
        if (b) {
            if (elapsedRealtime - this.currentLoadableExceptionTimestamp >= this.getRetryDelayMillis(this.currentLoadableExceptionCount)) {
                this.currentLoadableException = null;
                this.loader.startLoading((Loader.Loadable)this.currentLoadable, (Loader.Callback)this);
            }
        }
        else if (!this.loader.isLoading() && update) {
            this.chunkSource.getChunkOperation(this.previousTsLoadable, this.pendingResetPositionUs, this.downstreamPositionUs, this.chunkOperationHolder);
            final boolean endOfStream = this.chunkOperationHolder.endOfStream;
            final Chunk chunk = this.chunkOperationHolder.chunk;
            this.chunkOperationHolder.clear();
            if (endOfStream) {
                this.loadingFinished = true;
                return;
            }
            if (chunk != null) {
                this.currentLoadStartTimeMs = elapsedRealtime;
                this.currentLoadable = chunk;
                if (this.isTsChunk(this.currentLoadable)) {
                    final TsChunk currentTsLoadable = (TsChunk)this.currentLoadable;
                    if (this.isPendingReset()) {
                        this.pendingResetPositionUs = Long.MIN_VALUE;
                    }
                    final HlsExtractorWrapper extractorWrapper = currentTsLoadable.extractorWrapper;
                    if (this.extractors.isEmpty() || this.extractors.getLast() != extractorWrapper) {
                        extractorWrapper.init(this.loadControl.getAllocator());
                        this.extractors.addLast(extractorWrapper);
                    }
                    this.notifyLoadStarted(currentTsLoadable.dataSpec.length, currentTsLoadable.type, currentTsLoadable.trigger, currentTsLoadable.format, currentTsLoadable.startTimeUs, currentTsLoadable.endTimeUs);
                    this.currentTsLoadable = currentTsLoadable;
                }
                else {
                    this.notifyLoadStarted(this.currentLoadable.dataSpec.length, this.currentLoadable.type, this.currentLoadable.trigger, this.currentLoadable.format, -1L, -1L);
                }
                this.loader.startLoading((Loader.Loadable)this.currentLoadable, (Loader.Callback)this);
            }
        }
    }
    
    private void notifyDownstreamFormatChanged(final Format format, final int n, final long n2) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    HlsSampleSource.this.eventListener.onDownstreamFormatChanged(HlsSampleSource.this.eventSourceId, format, n, HlsSampleSource.this.usToMs(n2));
                }
            });
        }
    }
    
    private void notifyLoadCanceled(final long n) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    HlsSampleSource.this.eventListener.onLoadCanceled(HlsSampleSource.this.eventSourceId, n);
                }
            });
        }
    }
    
    private void notifyLoadCompleted(final long n, final int n2, final int n3, final Format format, final long n4, final long n5, final long n6, final long n7) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    HlsSampleSource.this.eventListener.onLoadCompleted(HlsSampleSource.this.eventSourceId, n, n2, n3, format, HlsSampleSource.this.usToMs(n4), HlsSampleSource.this.usToMs(n5), n6, n7);
                }
            });
        }
    }
    
    private void notifyLoadError(final IOException ex) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    HlsSampleSource.this.eventListener.onLoadError(HlsSampleSource.this.eventSourceId, ex);
                }
            });
        }
    }
    
    private void notifyLoadStarted(final long n, final int n2, final int n3, final Format format, final long n4, final long n5) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    HlsSampleSource.this.eventListener.onLoadStarted(HlsSampleSource.this.eventSourceId, n, n2, n3, format, HlsSampleSource.this.usToMs(n4), HlsSampleSource.this.usToMs(n5));
                }
            });
        }
    }
    
    private void restartFrom(final long pendingResetPositionUs) {
        this.pendingResetPositionUs = pendingResetPositionUs;
        this.loadingFinished = false;
        if (this.loader.isLoading()) {
            this.loader.cancelLoading();
            return;
        }
        this.clearState();
        this.maybeStartLoading();
    }
    
    @Override
    public boolean continueBuffering(final int n, final long downstreamPositionUs) {
        Assertions.checkState(this.prepared);
        Assertions.checkState(this.trackEnabledStates[n]);
        this.downstreamPositionUs = downstreamPositionUs;
        if (!this.extractors.isEmpty()) {
            this.discardSamplesForDisabledTracks(this.getCurrentExtractor(), this.downstreamPositionUs);
        }
        if (!this.loadingFinished) {
            this.maybeStartLoading();
            if (this.isPendingReset() || this.extractors.isEmpty()) {
                return false;
            }
            for (int i = 0; i < this.extractors.size(); ++i) {
                final HlsExtractorWrapper hlsExtractorWrapper = this.extractors.get(i);
                if (!hlsExtractorWrapper.isPrepared()) {
                    break;
                }
                if (hlsExtractorWrapper.hasSamples(n)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    @Override
    public void disable(final int n) {
        Assertions.checkState(this.prepared);
        Assertions.checkState(this.trackEnabledStates[n]);
        --this.enabledTrackCount;
        this.trackEnabledStates[n] = false;
        if (this.enabledTrackCount == 0) {
            this.chunkSource.reset();
            this.downstreamPositionUs = Long.MIN_VALUE;
            if (this.loadControlRegistered) {
                this.loadControl.unregister(this);
                this.loadControlRegistered = false;
            }
            if (!this.loader.isLoading()) {
                this.clearState();
                this.loadControl.trimAllocator();
                return;
            }
            this.loader.cancelLoading();
        }
    }
    
    @Override
    public void enable(final int n, final long n2) {
        Assertions.checkState(this.prepared);
        Assertions.checkState(!this.trackEnabledStates[n]);
        ++this.enabledTrackCount;
        this.trackEnabledStates[n] = true;
        this.downstreamMediaFormats[n] = null;
        this.pendingDiscontinuities[n] = false;
        this.downstreamFormat = null;
        final boolean loadControlRegistered = this.loadControlRegistered;
        if (!this.loadControlRegistered) {
            this.loadControl.register(this, this.bufferSizeContribution);
            this.loadControlRegistered = true;
        }
        if (this.enabledTrackCount == 1) {
            this.lastSeekPositionUs = n2;
            if (!loadControlRegistered || this.downstreamPositionUs != n2) {
                this.restartFrom(this.downstreamPositionUs = n2);
                return;
            }
            this.maybeStartLoading();
        }
    }
    
    @Override
    public long getBufferedPositionUs() {
        Assertions.checkState(this.prepared);
        Assertions.checkState(this.enabledTrackCount > 0);
        long pendingResetPositionUs;
        if (this.isPendingReset()) {
            pendingResetPositionUs = this.pendingResetPositionUs;
        }
        else {
            if (this.loadingFinished) {
                return -3L;
            }
            long n2;
            final long n = n2 = this.extractors.getLast().getLargestParsedTimestampUs();
            if (this.extractors.size() > 1) {
                n2 = Math.max(n, this.extractors.get(this.extractors.size() - 2).getLargestParsedTimestampUs());
            }
            pendingResetPositionUs = n2;
            if (n2 == Long.MIN_VALUE) {
                return this.downstreamPositionUs;
            }
        }
        return pendingResetPositionUs;
    }
    
    @Override
    public MediaFormat getFormat(final int n) {
        Assertions.checkState(this.prepared);
        return this.trackFormat[n];
    }
    
    @Override
    public int getTrackCount() {
        Assertions.checkState(this.prepared);
        return this.trackCount;
    }
    
    @Override
    public void maybeThrowError() throws IOException {
        if (this.currentLoadableException != null && this.currentLoadableExceptionCount > this.minLoadableRetryCount) {
            throw this.currentLoadableException;
        }
        if (this.currentLoadable == null) {
            this.chunkSource.maybeThrowError();
        }
    }
    
    @Override
    public void onLoadCanceled(final Loadable loadable) {
        this.notifyLoadCanceled(this.currentLoadable.bytesLoaded());
        if (this.enabledTrackCount > 0) {
            this.restartFrom(this.pendingResetPositionUs);
            return;
        }
        this.clearState();
        this.loadControl.trimAllocator();
    }
    
    @Override
    public void onLoadCompleted(final Loadable loadable) {
        final boolean b = true;
        Assertions.checkState(loadable == this.currentLoadable);
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        final long n = elapsedRealtime - this.currentLoadStartTimeMs;
        this.chunkSource.onChunkLoadCompleted(this.currentLoadable);
        if (this.isTsChunk(this.currentLoadable)) {
            Assertions.checkState(this.currentLoadable == this.currentTsLoadable && b);
            this.previousTsLoadable = this.currentTsLoadable;
            this.notifyLoadCompleted(this.currentLoadable.bytesLoaded(), this.currentTsLoadable.type, this.currentTsLoadable.trigger, this.currentTsLoadable.format, this.currentTsLoadable.startTimeUs, this.currentTsLoadable.endTimeUs, elapsedRealtime, n);
        }
        else {
            this.notifyLoadCompleted(this.currentLoadable.bytesLoaded(), this.currentLoadable.type, this.currentLoadable.trigger, this.currentLoadable.format, -1L, -1L, elapsedRealtime, n);
        }
        this.clearCurrentLoadable();
        if (this.enabledTrackCount > 0 || !this.prepared) {
            this.maybeStartLoading();
        }
    }
    
    @Override
    public void onLoadError(final Loadable loadable, final IOException currentLoadableException) {
        if (this.chunkSource.onChunkLoadError(this.currentLoadable, currentLoadableException)) {
            if (this.previousTsLoadable == null && !this.isPendingReset()) {
                this.pendingResetPositionUs = this.lastSeekPositionUs;
            }
            this.clearCurrentLoadable();
        }
        else {
            this.currentLoadableException = currentLoadableException;
            ++this.currentLoadableExceptionCount;
            this.currentLoadableExceptionTimestamp = SystemClock.elapsedRealtime();
        }
        this.notifyLoadError(currentLoadableException);
        this.maybeStartLoading();
    }
    
    @Override
    public boolean prepare(long durationUs) {
        if (this.prepared) {
            return true;
        }
        if (!this.extractors.isEmpty()) {
            while (true) {
                final HlsExtractorWrapper hlsExtractorWrapper = this.extractors.getFirst();
                if (hlsExtractorWrapper.isPrepared()) {
                    this.trackCount = hlsExtractorWrapper.getTrackCount();
                    this.trackEnabledStates = new boolean[this.trackCount];
                    this.pendingDiscontinuities = new boolean[this.trackCount];
                    this.downstreamMediaFormats = new MediaFormat[this.trackCount];
                    this.trackFormat = new MediaFormat[this.trackCount];
                    durationUs = this.chunkSource.getDurationUs();
                    for (int i = 0; i < this.trackCount; ++i) {
                        MediaFormat mediaFormat2;
                        final MediaFormat mediaFormat = mediaFormat2 = hlsExtractorWrapper.getMediaFormat(i).copyWithDurationUs(durationUs);
                        if (MimeTypes.isVideo(mediaFormat.mimeType)) {
                            mediaFormat2 = mediaFormat.copyAsAdaptive();
                        }
                        this.trackFormat[i] = mediaFormat2;
                    }
                    return this.prepared = true;
                }
                if (this.extractors.size() <= 1) {
                    break;
                }
                this.extractors.removeFirst().clear();
            }
        }
        if (this.loader == null) {
            this.loader = new Loader("Loader:HLS");
        }
        if (!this.loadControlRegistered) {
            this.loadControl.register(this, this.bufferSizeContribution);
            this.loadControlRegistered = true;
        }
        if (!this.loader.isLoading()) {
            this.pendingResetPositionUs = durationUs;
            this.downstreamPositionUs = durationUs;
        }
        this.maybeStartLoading();
        return false;
    }
    
    @Override
    public int readData(int n, final long downstreamPositionUs, final MediaFormatHolder mediaFormatHolder, final SampleHolder sampleHolder, final boolean b) {
        Assertions.checkState(this.prepared);
        this.downstreamPositionUs = downstreamPositionUs;
        if (this.pendingDiscontinuities[n]) {
            this.pendingDiscontinuities[n] = false;
            return -5;
        }
        if (b) {
            return -2;
        }
        if (this.isPendingReset()) {
            return -2;
        }
        HlsExtractorWrapper currentExtractor = this.getCurrentExtractor();
        if (!currentExtractor.isPrepared()) {
            return -2;
        }
        if (this.downstreamFormat == null || !this.downstreamFormat.equals(currentExtractor.format)) {
            this.notifyDownstreamFormatChanged(currentExtractor.format, currentExtractor.trigger, currentExtractor.startTimeUs);
            this.downstreamFormat = currentExtractor.format;
        }
        if (this.extractors.size() > 1) {
            currentExtractor.configureSpliceTo(this.extractors.get(1));
        }
        int n2 = 0;
        while (this.extractors.size() > n2 + 1 && !currentExtractor.hasSamples(n)) {
            final LinkedList<HlsExtractorWrapper> extractors = this.extractors;
            ++n2;
            if (!(currentExtractor = extractors.get(n2)).isPrepared()) {
                return -2;
            }
        }
        final MediaFormat mediaFormat = currentExtractor.getMediaFormat(n);
        if (mediaFormat != null && !mediaFormat.equals(this.downstreamMediaFormats[n])) {
            mediaFormatHolder.format = mediaFormat;
            this.downstreamMediaFormats[n] = mediaFormat;
            return -4;
        }
        if (currentExtractor.getSample(n, sampleHolder)) {
            if (sampleHolder.timeUs < this.lastSeekPositionUs) {
                n = 1;
            }
            else {
                n = 0;
            }
            final int flags = sampleHolder.flags;
            if (n != 0) {
                n = 134217728;
            }
            else {
                n = 0;
            }
            sampleHolder.flags = (n | flags);
            return -3;
        }
        if (this.loadingFinished) {
            return -1;
        }
        return -2;
    }
    
    @Override
    public SampleSourceReader register() {
        ++this.remainingReleaseCount;
        return this;
    }
    
    @Override
    public void release() {
        Assertions.checkState(this.remainingReleaseCount > 0);
        final int remainingReleaseCount = this.remainingReleaseCount - 1;
        this.remainingReleaseCount = remainingReleaseCount;
        if (remainingReleaseCount == 0 && this.loader != null) {
            this.loader.release();
            this.loader = null;
        }
    }
    
    @Override
    public void seekToUs(final long downstreamPositionUs) {
        Assertions.checkState(this.prepared);
        Assertions.checkState(this.enabledTrackCount > 0);
        long n;
        if (this.isPendingReset()) {
            n = this.pendingResetPositionUs;
        }
        else {
            n = this.downstreamPositionUs;
        }
        this.downstreamPositionUs = downstreamPositionUs;
        this.lastSeekPositionUs = downstreamPositionUs;
        if (n == downstreamPositionUs) {
            return;
        }
        this.downstreamPositionUs = downstreamPositionUs;
        for (int i = 0; i < this.pendingDiscontinuities.length; ++i) {
            this.pendingDiscontinuities[i] = true;
        }
        this.restartFrom(downstreamPositionUs);
    }
    
    long usToMs(final long n) {
        return n / 1000L;
    }
    
    public interface EventListener extends BaseChunkSampleSourceEventListener
    {
    }
}
