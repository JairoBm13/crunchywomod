// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.extractor.mp3;

import com.google.android.exoplayer.extractor.PositionHolder;
import java.io.EOFException;
import com.google.android.exoplayer.ParserException;
import java.util.List;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.extractor.SeekMap;
import java.io.IOException;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.util.Util;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.MpegAudioHeader;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.extractor.ExtractorOutput;
import com.google.android.exoplayer.extractor.Extractor;

public final class Mp3Extractor implements Extractor
{
    private static final int ID3_TAG;
    private static final int INFO_HEADER;
    private static final int VBRI_HEADER;
    private static final int XING_HEADER;
    private long basisTimeUs;
    private ExtractorOutput extractorOutput;
    private final long forcedFirstSampleTimestampUs;
    private final BufferingInput inputBuffer;
    private int sampleBytesRemaining;
    private int samplesRead;
    private final ParsableByteArray scratch;
    private Seeker seeker;
    private final MpegAudioHeader synchronizedHeader;
    private int synchronizedHeaderData;
    private TrackOutput trackOutput;
    
    static {
        ID3_TAG = Util.getIntegerCodeForString("ID3");
        XING_HEADER = Util.getIntegerCodeForString("Xing");
        INFO_HEADER = Util.getIntegerCodeForString("Info");
        VBRI_HEADER = Util.getIntegerCodeForString("VBRI");
    }
    
    public Mp3Extractor() {
        this(-1L);
    }
    
    public Mp3Extractor(final long forcedFirstSampleTimestampUs) {
        this.forcedFirstSampleTimestampUs = forcedFirstSampleTimestampUs;
        this.inputBuffer = new BufferingInput(12288);
        this.scratch = new ParsableByteArray(4);
        this.synchronizedHeader = new MpegAudioHeader();
        this.basisTimeUs = -1L;
    }
    
    private static long getPosition(final ExtractorInput extractorInput, final BufferingInput bufferingInput) {
        return extractorInput.getPosition() - bufferingInput.getAvailableByteCount();
    }
    
    private long maybeResynchronize(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        this.inputBuffer.mark();
        if (!this.inputBuffer.readAllowingEndOfInput(extractorInput, this.scratch.data, 0, 4)) {
            return -1L;
        }
        this.inputBuffer.returnToMark();
        this.scratch.setPosition(0);
        final int int1 = this.scratch.readInt();
        if ((int1 & 0xFFFE0C00) == (this.synchronizedHeaderData & 0xFFFE0C00) && MpegAudioHeader.getFrameSize(int1) != -1) {
            MpegAudioHeader.populateHeader(int1, this.synchronizedHeader);
            return 0L;
        }
        this.synchronizedHeaderData = 0;
        this.inputBuffer.skip(extractorInput, 1);
        return this.synchronizeCatchingEndOfInput(extractorInput);
    }
    
    private boolean parseSeekerFrame(final ExtractorInput extractorInput, final long n, final long n2) throws IOException, InterruptedException {
        this.inputBuffer.mark();
        this.seeker = null;
        final ParsableByteArray parsableByteArray = this.inputBuffer.getParsableByteArray(extractorInput, this.synchronizedHeader.frameSize);
        int n3;
        if ((this.synchronizedHeader.version & 0x1) == 0x1) {
            if (this.synchronizedHeader.channels != 1) {
                n3 = 32;
            }
            else {
                n3 = 17;
            }
        }
        else if (this.synchronizedHeader.channels != 1) {
            n3 = 17;
        }
        else {
            n3 = 9;
        }
        parsableByteArray.setPosition(n3 + 4);
        final int int1 = parsableByteArray.readInt();
        if (int1 == Mp3Extractor.XING_HEADER || int1 == Mp3Extractor.INFO_HEADER) {
            this.seeker = (Seeker)XingSeeker.create(this.synchronizedHeader, parsableByteArray, n, n2);
            return true;
        }
        parsableByteArray.setPosition(36);
        if (parsableByteArray.readInt() == Mp3Extractor.VBRI_HEADER) {
            this.seeker = (Seeker)VbriSeeker.create(this.synchronizedHeader, parsableByteArray, n);
            return true;
        }
        return false;
    }
    
    private int readSample(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (this.sampleBytesRemaining == 0) {
            if (this.maybeResynchronize(extractorInput) == -1L) {
                return -1;
            }
            if (this.basisTimeUs == -1L) {
                this.basisTimeUs = this.seeker.getTimeUs(getPosition(extractorInput, this.inputBuffer));
                if (this.forcedFirstSampleTimestampUs != -1L) {
                    this.basisTimeUs += this.forcedFirstSampleTimestampUs - this.seeker.getTimeUs(0L);
                }
            }
            this.sampleBytesRemaining = this.synchronizedHeader.frameSize;
        }
        final long basisTimeUs = this.basisTimeUs;
        final long n = this.samplesRead * 1000000L / this.synchronizedHeader.sampleRate;
        this.sampleBytesRemaining -= this.inputBuffer.drainToOutput(this.trackOutput, this.sampleBytesRemaining);
        if (this.sampleBytesRemaining > 0) {
            this.inputBuffer.mark();
            final int sampleData = this.trackOutput.sampleData(extractorInput, this.sampleBytesRemaining, true);
            if (sampleData == -1) {
                return -1;
            }
            this.sampleBytesRemaining -= sampleData;
            if (this.sampleBytesRemaining > 0) {
                return 0;
            }
        }
        this.trackOutput.sampleMetadata(basisTimeUs + n, 1, this.synchronizedHeader.frameSize, 0, null);
        this.samplesRead += this.synchronizedHeader.samplesPerFrame;
        return this.sampleBytesRemaining = 0;
    }
    
    private void setupSeeker(final ExtractorInput extractorInput, final long n) throws IOException, InterruptedException {
        long n2 = n;
        if (this.parseSeekerFrame(extractorInput, n, extractorInput.getLength())) {
            this.inputBuffer.mark();
            if (this.seeker != null) {
                return;
            }
            this.inputBuffer.read(extractorInput, this.scratch.data, 0, 4);
            this.scratch.setPosition(0);
            n2 = n + this.synchronizedHeader.frameSize;
            MpegAudioHeader.populateHeader(this.scratch.readInt(), this.synchronizedHeader);
        }
        this.inputBuffer.returnToMark();
        this.seeker = (Seeker)new ConstantBitrateSeeker(n2, this.synchronizedHeader.bitrate * 1000, extractorInput.getLength());
    }
    
    private long synchronize(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (extractorInput.getPosition() == 0L) {
            this.inputBuffer.reset();
        }
        else {
            this.inputBuffer.returnToMark();
        }
        long n2;
        final long n = n2 = getPosition(extractorInput, this.inputBuffer);
        if (n == 0L) {
            n2 = n;
            while (true) {
                this.inputBuffer.read(extractorInput, this.scratch.data, 0, 3);
                this.scratch.setPosition(0);
                if (this.scratch.readUnsignedInt24() != Mp3Extractor.ID3_TAG) {
                    break;
                }
                extractorInput.skipFully(3);
                extractorInput.readFully(this.scratch.data, 0, 4);
                extractorInput.skipFully((this.scratch.data[0] & 0x7F) << 21 | (this.scratch.data[1] & 0x7F) << 14 | (this.scratch.data[2] & 0x7F) << 7 | (this.scratch.data[3] & 0x7F));
                this.inputBuffer.reset();
                n2 = getPosition(extractorInput, this.inputBuffer);
            }
            this.inputBuffer.returnToMark();
        }
        this.inputBuffer.mark();
        long n3 = n2;
        int n4 = 0;
        int synchronizedHeaderData = 0;
        while (n3 - n2 < 131072L) {
            if (this.inputBuffer.readAllowingEndOfInput(extractorInput, this.scratch.data, 0, 4)) {
                this.scratch.setPosition(0);
                final int int1 = this.scratch.readInt();
                if (synchronizedHeaderData == 0 || (0xFFFE0C00 & int1) == (0xFFFE0C00 & synchronizedHeaderData)) {
                    final int frameSize = MpegAudioHeader.getFrameSize(int1);
                    if (frameSize != -1) {
                        if (n4 == 0) {
                            MpegAudioHeader.populateHeader(int1, this.synchronizedHeader);
                            synchronizedHeaderData = int1;
                        }
                        ++n4;
                        if (n4 != 4) {
                            this.inputBuffer.skip(extractorInput, frameSize - 4);
                            continue;
                        }
                        this.inputBuffer.returnToMark();
                        this.synchronizedHeaderData = synchronizedHeaderData;
                        final long n5 = n3;
                        if (this.seeker == null) {
                            this.setupSeeker(extractorInput, n3);
                            this.extractorOutput.seekMap(this.seeker);
                            this.trackOutput.format(MediaFormat.createAudioFormat(-1, this.synchronizedHeader.mimeType, -1, 4096, this.seeker.getDurationUs(), this.synchronizedHeader.channels, this.synchronizedHeader.sampleRate, null, null));
                            return n3;
                        }
                        return n5;
                    }
                }
                n4 = 0;
                synchronizedHeaderData = 0;
                this.inputBuffer.returnToMark();
                this.inputBuffer.skip(extractorInput, 1);
                this.inputBuffer.mark();
                ++n3;
                continue;
            }
            return -1L;
        }
        throw new ParserException("Searched too many bytes while resynchronizing.");
    }
    
    private long synchronizeCatchingEndOfInput(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        try {
            return this.synchronize(extractorInput);
        }
        catch (EOFException ex) {
            return -1L;
        }
    }
    
    @Override
    public void init(final ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
        this.trackOutput = extractorOutput.track(0);
        extractorOutput.endTracks();
    }
    
    @Override
    public int read(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        if (this.synchronizedHeaderData == 0 && this.synchronizeCatchingEndOfInput(extractorInput) == -1L) {
            return -1;
        }
        return this.readSample(extractorInput);
    }
    
    interface Seeker extends SeekMap
    {
        long getDurationUs();
        
        long getTimeUs(final long p0);
    }
}
