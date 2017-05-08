// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.hls;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import com.google.android.exoplayer.chunk.DataChunk;
import android.util.Log;
import com.google.android.exoplayer.upstream.HttpDataSource;
import com.google.android.exoplayer.chunk.Chunk;
import com.google.android.exoplayer.extractor.ts.TsExtractor;
import com.google.android.exoplayer.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ts.AdtsExtractor;
import com.google.android.exoplayer.util.Util;
import com.google.android.exoplayer.chunk.ChunkOperationHolder;
import android.text.TextUtils;
import java.math.BigInteger;
import java.util.Locale;
import com.google.android.exoplayer.util.UriUtil;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.util.Assertions;
import android.os.SystemClock;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Collection;
import java.util.ArrayList;
import com.google.android.exoplayer.chunk.Format;
import java.util.List;
import com.google.android.exoplayer.extractor.ts.PtsTimestampAdjuster;
import java.io.IOException;
import android.net.Uri;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.BandwidthMeter;

public class HlsChunkSource
{
    private final int adaptiveMaxHeight;
    private final int adaptiveMaxWidth;
    private final int adaptiveMode;
    private final BandwidthMeter bandwidthMeter;
    private final String baseUri;
    private final DataSource dataSource;
    private long durationUs;
    private byte[] encryptionIv;
    private String encryptionIvString;
    private byte[] encryptionKey;
    private Uri encryptionKeyUri;
    private IOException fatalError;
    private boolean live;
    private final long maxBufferDurationToSwitchDownUs;
    private final long minBufferDurationToSwitchUpUs;
    private final HlsPlaylistParser playlistParser;
    private PtsTimestampAdjuster ptsTimestampAdjuster;
    private byte[] scratchSpace;
    private int selectedVariantIndex;
    private final long[] variantBlacklistTimes;
    private final long[] variantLastPlaylistLoadTimesMs;
    private final HlsMediaPlaylist[] variantPlaylists;
    private final Variant[] variants;
    
    public HlsChunkSource(final DataSource dataSource, final String s, final HlsPlaylist hlsPlaylist, final BandwidthMeter bandwidthMeter, final int[] array, final int adaptiveMode, final long n, final long n2) {
        this.dataSource = dataSource;
        this.bandwidthMeter = bandwidthMeter;
        this.adaptiveMode = adaptiveMode;
        this.minBufferDurationToSwitchUpUs = 1000L * n;
        this.maxBufferDurationToSwitchDownUs = 1000L * n2;
        this.baseUri = hlsPlaylist.baseUri;
        this.playlistParser = new HlsPlaylistParser();
        if (hlsPlaylist.type == 1) {
            this.variants = new Variant[] { new Variant(0, s, 0, null, -1, -1) };
            this.variantPlaylists = new HlsMediaPlaylist[1];
            this.variantLastPlaylistLoadTimesMs = new long[1];
            this.variantBlacklistTimes = new long[1];
            this.setMediaPlaylist(0, (HlsMediaPlaylist)hlsPlaylist);
            this.adaptiveMaxWidth = -1;
            this.adaptiveMaxHeight = -1;
            return;
        }
        final List<Variant> variants = ((HlsMasterPlaylist)hlsPlaylist).variants;
        this.variants = buildOrderedVariants(variants, array);
        this.variantPlaylists = new HlsMediaPlaylist[this.variants.length];
        this.variantLastPlaylistLoadTimesMs = new long[this.variants.length];
        this.variantBlacklistTimes = new long[this.variants.length];
        int max = -1;
        int max2 = -1;
        int n3 = Integer.MAX_VALUE;
        int n4;
        for (int i = 0; i < this.variants.length; ++i, n3 = n4) {
            final int index = variants.indexOf(this.variants[i]);
            if (index < (n4 = n3)) {
                n4 = index;
                this.selectedVariantIndex = i;
            }
            final Format format = this.variants[i].format;
            max = Math.max(format.width, max);
            max2 = Math.max(format.height, max2);
        }
        if (this.variants.length <= 1 || adaptiveMode == 0) {
            this.adaptiveMaxWidth = -1;
            this.adaptiveMaxHeight = -1;
            return;
        }
        if (max <= 0) {
            max = 1920;
        }
        this.adaptiveMaxWidth = max;
        if (max2 <= 0) {
            max2 = 1080;
        }
        this.adaptiveMaxHeight = max2;
    }
    
    private boolean allVariantsBlacklisted() {
        for (int i = 0; i < this.variantBlacklistTimes.length; ++i) {
            if (this.variantBlacklistTimes[i] == 0L) {
                return false;
            }
        }
        return true;
    }
    
    private static Variant[] buildOrderedVariants(final List<Variant> list, final int[] array) {
        final ArrayList<Variant> list2 = new ArrayList<Variant>();
        if (array != null) {
            for (int i = 0; i < array.length; ++i) {
                list2.add(list.get(array[i]));
            }
        }
        else {
            list2.addAll(list);
        }
        ArrayList<Variant> list3 = new ArrayList<Variant>();
        final ArrayList<Variant> list4 = new ArrayList<Variant>();
        for (int j = 0; j < list2.size(); ++j) {
            final Variant variant = list2.get(j);
            if (variant.format.height > 0 || variantHasExplicitCodecWithPrefix(variant, "avc")) {
                list3.add(variant);
            }
            else if (variantHasExplicitCodecWithPrefix(variant, "mp4a")) {
                list4.add(variant);
            }
        }
        if (list3.isEmpty()) {
            list3 = list2;
            if (list4.size() < list2.size()) {
                list2.removeAll(list4);
                list3 = list2;
            }
        }
        final Variant[] array2 = new Variant[list3.size()];
        list3.toArray(array2);
        Arrays.sort(array2, new Comparator<Variant>() {
            private final Comparator<Format> formatComparator = new Format.DecreasingBandwidthComparator();
            
            @Override
            public int compare(final Variant variant, final Variant variant2) {
                return this.formatComparator.compare(variant.format, variant2.format);
            }
        });
        return array2;
    }
    
    private void clearEncryptionData() {
        this.encryptionKeyUri = null;
        this.encryptionKey = null;
        this.encryptionIvString = null;
        this.encryptionIv = null;
    }
    
    private void clearStaleBlacklistedVariants() {
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        for (int i = 0; i < this.variantBlacklistTimes.length; ++i) {
            if (this.variantBlacklistTimes[i] != 0L && elapsedRealtime - this.variantBlacklistTimes[i] > 60000L) {
                this.variantBlacklistTimes[i] = 0L;
            }
        }
    }
    
    private int getLiveStartChunkMediaSequence(int n) {
        final HlsMediaPlaylist hlsMediaPlaylist = this.variantPlaylists[n];
        if (hlsMediaPlaylist.segments.size() > 3) {
            n = hlsMediaPlaylist.segments.size() - 3;
        }
        else {
            n = 0;
        }
        return hlsMediaPlaylist.mediaSequence + n;
    }
    
    private int getNextVariantIndex(final TsChunk tsChunk, long n) {
        this.clearStaleBlacklistedVariants();
        final long bitrateEstimate = this.bandwidthMeter.getBitrateEstimate();
        int variantIndexForBandwidth;
        if (this.variantBlacklistTimes[this.selectedVariantIndex] != 0L) {
            variantIndexForBandwidth = this.getVariantIndexForBandwidth(bitrateEstimate);
        }
        else {
            if (tsChunk == null) {
                return this.selectedVariantIndex;
            }
            if (bitrateEstimate == -1L) {
                return this.selectedVariantIndex;
            }
            final int variantIndexForBandwidth2 = this.getVariantIndexForBandwidth(bitrateEstimate);
            if (variantIndexForBandwidth2 == this.selectedVariantIndex) {
                return this.selectedVariantIndex;
            }
            long n2;
            if (this.adaptiveMode == 1) {
                n2 = tsChunk.startTimeUs;
            }
            else {
                n2 = tsChunk.endTimeUs;
            }
            n = n2 - n;
            variantIndexForBandwidth = variantIndexForBandwidth2;
            if (this.variantBlacklistTimes[this.selectedVariantIndex] == 0L) {
                if (variantIndexForBandwidth2 > this.selectedVariantIndex) {
                    variantIndexForBandwidth = variantIndexForBandwidth2;
                    if (n < this.maxBufferDurationToSwitchDownUs) {
                        return variantIndexForBandwidth;
                    }
                }
                if (variantIndexForBandwidth2 < this.selectedVariantIndex) {
                    variantIndexForBandwidth = variantIndexForBandwidth2;
                    if (n > this.minBufferDurationToSwitchUpUs) {
                        return variantIndexForBandwidth;
                    }
                }
                return this.selectedVariantIndex;
            }
        }
        return variantIndexForBandwidth;
    }
    
    private int getVariantIndex(final Format format) {
        for (int i = 0; i < this.variants.length; ++i) {
            if (this.variants[i].format.equals(format)) {
                return i;
            }
        }
        throw new IllegalStateException("Invalid format: " + format);
    }
    
    private int getVariantIndexForBandwidth(final long n) {
        long n2 = n;
        if (n == -1L) {
            n2 = 0L;
        }
        final int n3 = (int)(n2 * 0.8f);
        int n4 = -1;
        for (int i = 0; i < this.variants.length; ++i) {
            if (this.variantBlacklistTimes[i] == 0L) {
                if (this.variants[i].format.bitrate <= n3) {
                    return i;
                }
                n4 = i;
            }
        }
        Assertions.checkState(n4 != -1);
        return n4;
    }
    
    private EncryptionKeyChunk newEncryptionKeyChunk(final Uri uri, final String s, final int n) {
        return new EncryptionKeyChunk(this.dataSource, new DataSpec(uri, 0L, -1L, null, 1), this.scratchSpace, s, n);
    }
    
    private MediaPlaylistChunk newMediaPlaylistChunk(final int n) {
        final Uri resolveToUri = UriUtil.resolveToUri(this.baseUri, this.variants[n].url);
        return new MediaPlaylistChunk(this.dataSource, new DataSpec(resolveToUri, 0L, -1L, null, 1), this.scratchSpace, this.playlistParser, n, resolveToUri.toString());
    }
    
    private void setEncryptionData(final Uri encryptionKeyUri, final String encryptionIvString, final byte[] encryptionKey) {
        String substring;
        if (encryptionIvString.toLowerCase(Locale.getDefault()).startsWith("0x")) {
            substring = encryptionIvString.substring(2);
        }
        else {
            substring = encryptionIvString;
        }
        final byte[] byteArray = new BigInteger(substring, 16).toByteArray();
        final byte[] encryptionIv = new byte[16];
        int n;
        if (byteArray.length > 16) {
            n = byteArray.length - 16;
        }
        else {
            n = 0;
        }
        System.arraycopy(byteArray, n, encryptionIv, encryptionIv.length - byteArray.length + n, byteArray.length - n);
        this.encryptionKeyUri = encryptionKeyUri;
        this.encryptionKey = encryptionKey;
        this.encryptionIvString = encryptionIvString;
        this.encryptionIv = encryptionIv;
    }
    
    private void setMediaPlaylist(final int n, final HlsMediaPlaylist hlsMediaPlaylist) {
        this.variantLastPlaylistLoadTimesMs[n] = SystemClock.elapsedRealtime();
        this.variantPlaylists[n] = hlsMediaPlaylist;
        this.live |= hlsMediaPlaylist.live;
        long durationUs;
        if (this.live) {
            durationUs = -1L;
        }
        else {
            durationUs = hlsMediaPlaylist.durationUs;
        }
        this.durationUs = durationUs;
    }
    
    private boolean shouldRerequestLiveMediaPlaylist(final int n) {
        return SystemClock.elapsedRealtime() - this.variantLastPlaylistLoadTimesMs[n] >= this.variantPlaylists[n].targetDurationSecs * 1000 / 2;
    }
    
    private static boolean variantHasExplicitCodecWithPrefix(final Variant variant, final String s) {
        final String codecs = variant.format.codecs;
        if (!TextUtils.isEmpty((CharSequence)codecs)) {
            final String[] split = codecs.split("(\\s*,\\s*)|(\\s*$)");
            for (int i = 0; i < split.length; ++i) {
                if (split[i].startsWith(s)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void getChunkOperation(final TsChunk tsChunk, long n, long n2, final ChunkOperationHolder chunkOperationHolder) {
        int selectedVariantIndex;
        boolean b;
        if (this.adaptiveMode == 0) {
            selectedVariantIndex = this.selectedVariantIndex;
            b = false;
        }
        else {
            selectedVariantIndex = this.getNextVariantIndex(tsChunk, n2);
            b = (tsChunk != null && !this.variants[selectedVariantIndex].format.equals(tsChunk.format) && this.adaptiveMode == 1);
        }
        final HlsMediaPlaylist hlsMediaPlaylist = this.variantPlaylists[selectedVariantIndex];
        if (hlsMediaPlaylist == null) {
            chunkOperationHolder.chunk = this.newMediaPlaylistChunk(selectedVariantIndex);
        }
        else {
            this.selectedVariantIndex = selectedVariantIndex;
            boolean b2 = false;
            int n3;
            if (this.live) {
                if (tsChunk == null) {
                    n3 = this.getLiveStartChunkMediaSequence(selectedVariantIndex);
                }
                else {
                    int chunkIndex;
                    if (b) {
                        chunkIndex = tsChunk.chunkIndex;
                    }
                    else {
                        chunkIndex = tsChunk.chunkIndex + 1;
                    }
                    n3 = chunkIndex;
                    if (chunkIndex < hlsMediaPlaylist.mediaSequence) {
                        n3 = this.getLiveStartChunkMediaSequence(selectedVariantIndex);
                        b2 = true;
                    }
                }
            }
            else if (tsChunk == null) {
                n3 = Util.binarySearchFloor(hlsMediaPlaylist.segments, Long.valueOf(n), true, true) + hlsMediaPlaylist.mediaSequence;
            }
            else if (b) {
                n3 = tsChunk.chunkIndex;
            }
            else {
                n3 = tsChunk.chunkIndex + 1;
            }
            final int n4 = n3 - hlsMediaPlaylist.mediaSequence;
            if (n4 < hlsMediaPlaylist.segments.size()) {
                final HlsMediaPlaylist.Segment segment = hlsMediaPlaylist.segments.get(n4);
                final Uri resolveToUri = UriUtil.resolveToUri(hlsMediaPlaylist.baseUri, segment.url);
                if (segment.isEncrypted) {
                    final Uri resolveToUri2 = UriUtil.resolveToUri(hlsMediaPlaylist.baseUri, segment.encryptionKeyUri);
                    if (!resolveToUri2.equals((Object)this.encryptionKeyUri)) {
                        chunkOperationHolder.chunk = this.newEncryptionKeyChunk(resolveToUri2, segment.encryptionIV, this.selectedVariantIndex);
                        return;
                    }
                    if (!Util.areEqual(segment.encryptionIV, this.encryptionIvString)) {
                        this.setEncryptionData(resolveToUri2, segment.encryptionIV, this.encryptionKey);
                    }
                }
                else {
                    this.clearEncryptionData();
                }
                final DataSpec dataSpec = new DataSpec(resolveToUri, segment.byterangeOffset, segment.byterangeLength, null);
                if (this.live) {
                    if (tsChunk == null) {
                        n = 0L;
                    }
                    else if (b) {
                        n = tsChunk.startTimeUs;
                    }
                    else {
                        n = tsChunk.endTimeUs;
                    }
                }
                else {
                    n = segment.startTimeUs;
                }
                n2 = (long)(segment.durationSecs * 1000000.0);
                final Format format = this.variants[this.selectedVariantIndex].format;
                HlsExtractorWrapper extractorWrapper;
                if (resolveToUri.getLastPathSegment().endsWith(".aac")) {
                    extractorWrapper = new HlsExtractorWrapper(0, format, n, new AdtsExtractor(n), b, this.adaptiveMaxWidth, this.adaptiveMaxHeight);
                }
                else if (resolveToUri.getLastPathSegment().endsWith(".mp3")) {
                    extractorWrapper = new HlsExtractorWrapper(0, format, n, new Mp3Extractor(n), b, this.adaptiveMaxWidth, this.adaptiveMaxHeight);
                }
                else if (tsChunk == null || segment.discontinuity || b2 || !format.equals(tsChunk.format)) {
                    if (tsChunk == null || segment.discontinuity || b2 || this.ptsTimestampAdjuster == null) {
                        this.ptsTimestampAdjuster = new PtsTimestampAdjuster(n);
                    }
                    extractorWrapper = new HlsExtractorWrapper(0, format, n, new TsExtractor(this.ptsTimestampAdjuster), b, this.adaptiveMaxWidth, this.adaptiveMaxHeight);
                }
                else {
                    extractorWrapper = tsChunk.extractorWrapper;
                }
                chunkOperationHolder.chunk = new TsChunk(this.dataSource, dataSpec, 0, format, n, n + n2, n3, extractorWrapper, this.encryptionKey, this.encryptionIv);
                return;
            }
            if (!hlsMediaPlaylist.live) {
                chunkOperationHolder.endOfStream = true;
                return;
            }
            if (this.shouldRerequestLiveMediaPlaylist(selectedVariantIndex)) {
                chunkOperationHolder.chunk = this.newMediaPlaylistChunk(selectedVariantIndex);
            }
        }
    }
    
    public long getDurationUs() {
        return this.durationUs;
    }
    
    public void maybeThrowError() throws IOException {
        if (this.fatalError != null) {
            throw this.fatalError;
        }
    }
    
    public void onChunkLoadCompleted(final Chunk chunk) {
        if (chunk instanceof MediaPlaylistChunk) {
            final MediaPlaylistChunk mediaPlaylistChunk = (MediaPlaylistChunk)chunk;
            this.scratchSpace = mediaPlaylistChunk.getDataHolder();
            this.setMediaPlaylist(mediaPlaylistChunk.variantIndex, mediaPlaylistChunk.getResult());
        }
        else if (chunk instanceof EncryptionKeyChunk) {
            final EncryptionKeyChunk encryptionKeyChunk = (EncryptionKeyChunk)chunk;
            this.scratchSpace = encryptionKeyChunk.getDataHolder();
            this.setEncryptionData(encryptionKeyChunk.dataSpec.uri, encryptionKeyChunk.iv, encryptionKeyChunk.getResult());
        }
    }
    
    public boolean onChunkLoadError(final Chunk chunk, final IOException ex) {
        if (chunk.bytesLoaded() == 0L && (chunk instanceof TsChunk || chunk instanceof MediaPlaylistChunk || chunk instanceof EncryptionKeyChunk) && ex instanceof HttpDataSource.InvalidResponseCodeException) {
            final int responseCode = ((HttpDataSource.InvalidResponseCodeException)ex).responseCode;
            if (responseCode == 404 || responseCode == 410) {
                int n;
                if (chunk instanceof TsChunk) {
                    n = this.getVariantIndex(((TsChunk)chunk).format);
                }
                else if (chunk instanceof MediaPlaylistChunk) {
                    n = ((MediaPlaylistChunk)chunk).variantIndex;
                }
                else {
                    n = ((EncryptionKeyChunk)chunk).variantIndex;
                }
                int n2;
                if (this.variantBlacklistTimes[n] != 0L) {
                    n2 = 1;
                }
                else {
                    n2 = 0;
                }
                this.variantBlacklistTimes[n] = SystemClock.elapsedRealtime();
                if (n2 != 0) {
                    Log.w("HlsChunkSource", "Already blacklisted variant (" + responseCode + "): " + chunk.dataSpec.uri);
                    return false;
                }
                if (!this.allVariantsBlacklisted()) {
                    Log.w("HlsChunkSource", "Blacklisted variant (" + responseCode + "): " + chunk.dataSpec.uri);
                    return true;
                }
                Log.w("HlsChunkSource", "Final variant not blacklisted (" + responseCode + "): " + chunk.dataSpec.uri);
                this.variantBlacklistTimes[n] = 0L;
                return false;
            }
        }
        return false;
    }
    
    public void reset() {
        this.fatalError = null;
    }
    
    private static class EncryptionKeyChunk extends DataChunk
    {
        public final String iv;
        private byte[] result;
        public final int variantIndex;
        
        public EncryptionKeyChunk(final DataSource dataSource, final DataSpec dataSpec, final byte[] array, final String iv, final int variantIndex) {
            super(dataSource, dataSpec, 3, 0, null, -1, array);
            this.iv = iv;
            this.variantIndex = variantIndex;
        }
        
        @Override
        protected void consume(final byte[] array, final int n) throws IOException {
            this.result = Arrays.copyOf(array, n);
        }
        
        public byte[] getResult() {
            return this.result;
        }
    }
    
    private static class MediaPlaylistChunk extends DataChunk
    {
        private final HlsPlaylistParser playlistParser;
        private final String playlistUrl;
        private HlsMediaPlaylist result;
        public final int variantIndex;
        
        public MediaPlaylistChunk(final DataSource dataSource, final DataSpec dataSpec, final byte[] array, final HlsPlaylistParser playlistParser, final int variantIndex, final String playlistUrl) {
            super(dataSource, dataSpec, 4, 0, null, -1, array);
            this.variantIndex = variantIndex;
            this.playlistParser = playlistParser;
            this.playlistUrl = playlistUrl;
        }
        
        @Override
        protected void consume(final byte[] array, final int n) throws IOException {
            this.result = (HlsMediaPlaylist)this.playlistParser.parse(this.playlistUrl, (InputStream)new ByteArrayInputStream(array, 0, n));
        }
        
        public HlsMediaPlaylist getResult() {
            return this.result;
        }
    }
}
