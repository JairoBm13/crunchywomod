// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer;

import java.util.Arrays;
import java.io.IOException;

public abstract class SampleSourceTrackRenderer extends TrackRenderer
{
    private long durationUs;
    private SampleSource.SampleSourceReader enabledSource;
    private int enabledSourceTrackIndex;
    private int[] handledSourceIndices;
    private int[] handledSourceTrackIndices;
    private final SampleSource.SampleSourceReader[] sources;
    
    public SampleSourceTrackRenderer(final SampleSource... array) {
        this.sources = new SampleSource.SampleSourceReader[array.length];
        for (int i = 0; i < array.length; ++i) {
            this.sources[i] = array[i].register();
        }
    }
    
    private void maybeThrowError(final SampleSource.SampleSourceReader sampleSourceReader) throws ExoPlaybackException {
        try {
            sampleSourceReader.maybeThrowError();
        }
        catch (IOException ex) {
            throw new ExoPlaybackException(ex);
        }
    }
    
    protected final boolean continueBufferingSource(final long n) {
        return this.enabledSource.continueBuffering(this.enabledSourceTrackIndex, n);
    }
    
    @Override
    protected boolean doPrepare(long max) throws ExoPlaybackException {
        boolean b = true;
        for (int i = 0; i < this.sources.length; ++i) {
            b &= this.sources[i].prepare(max);
        }
        if (!b) {
            return false;
        }
        int n = 0;
        for (int j = 0; j < this.sources.length; ++j) {
            n += this.sources[j].getTrackCount();
        }
        long durationUs = 0L;
        int n2 = 0;
        final int[] array = new int[n];
        final int[] array2 = new int[n];
        for (int length = this.sources.length, k = 0; k < length; ++k) {
            final SampleSource.SampleSourceReader sampleSourceReader = this.sources[k];
            int n3;
            for (int trackCount = sampleSourceReader.getTrackCount(), l = 0; l < trackCount; ++l, durationUs = max, n2 = n3) {
                while (true) {
                    final MediaFormat format = sampleSourceReader.getFormat(l);
                    while (true) {
                        int n4 = 0;
                        Label_0236: {
                            try {
                                final boolean handlesTrack = this.handlesTrack(format);
                                max = durationUs;
                                n3 = n2;
                                if (handlesTrack) {
                                    array[n2] = k;
                                    array2[n2] = l;
                                    n4 = n2 + 1;
                                    if (durationUs != -1L) {
                                        break Label_0236;
                                    }
                                    n3 = n4;
                                    max = durationUs;
                                }
                                break;
                            }
                            catch (MediaCodecUtil.DecoderQueryException ex) {
                                throw new ExoPlaybackException(ex);
                            }
                        }
                        final long durationUs2 = format.durationUs;
                        if (durationUs2 == -1L) {
                            max = -1L;
                            n3 = n4;
                            continue;
                        }
                        max = durationUs;
                        n3 = n4;
                        if (durationUs2 != -2L) {
                            max = Math.max(durationUs, durationUs2);
                            n3 = n4;
                            continue;
                        }
                        continue;
                    }
                }
            }
        }
        this.durationUs = durationUs;
        this.handledSourceIndices = Arrays.copyOf(array, n2);
        this.handledSourceTrackIndices = Arrays.copyOf(array2, n2);
        return true;
    }
    
    @Override
    protected long getBufferedPositionUs() {
        return this.enabledSource.getBufferedPositionUs();
    }
    
    @Override
    protected long getDurationUs() {
        return this.durationUs;
    }
    
    @Override
    protected final MediaFormat getFormat(final int n) {
        return this.sources[this.handledSourceIndices[n]].getFormat(this.handledSourceTrackIndices[n]);
    }
    
    @Override
    protected final int getTrackCount() {
        return this.handledSourceTrackIndices.length;
    }
    
    protected abstract boolean handlesTrack(final MediaFormat p0) throws MediaCodecUtil.DecoderQueryException;
    
    @Override
    protected void maybeThrowError() throws ExoPlaybackException {
        if (this.enabledSource != null) {
            this.maybeThrowError(this.enabledSource);
        }
        else {
            for (int length = this.sources.length, i = 0; i < length; ++i) {
                this.maybeThrowError(this.sources[i]);
            }
        }
    }
    
    @Override
    protected void onDisabled() throws ExoPlaybackException {
        this.enabledSource.disable(this.enabledSourceTrackIndex);
        this.enabledSource = null;
    }
    
    @Override
    protected void onEnabled(final int n, final long n2, final boolean b) throws ExoPlaybackException {
        this.enabledSource = this.sources[this.handledSourceIndices[n]];
        this.enabledSourceTrackIndex = this.handledSourceTrackIndices[n];
        this.enabledSource.enable(this.enabledSourceTrackIndex, n2);
    }
    
    @Override
    protected void onReleased() throws ExoPlaybackException {
        for (int length = this.sources.length, i = 0; i < length; ++i) {
            this.sources[i].release();
        }
    }
    
    protected final int readSource(final long n, final MediaFormatHolder mediaFormatHolder, final SampleHolder sampleHolder, final boolean b) {
        return this.enabledSource.readData(this.enabledSourceTrackIndex, n, mediaFormatHolder, sampleHolder, b);
    }
    
    @Override
    protected void seekTo(final long n) throws ExoPlaybackException {
        this.enabledSource.seekToUs(n);
    }
}
