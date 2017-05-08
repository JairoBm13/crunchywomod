// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.extractor.ts;

import android.util.Log;
import java.io.IOException;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.SeekMap;
import com.google.android.exoplayer.util.ParsableBitArray;
import android.util.SparseArray;
import com.google.android.exoplayer.util.ParsableByteArray;
import android.util.SparseBooleanArray;
import com.google.android.exoplayer.extractor.ExtractorOutput;
import com.google.android.exoplayer.extractor.Extractor;

public final class TsExtractor implements Extractor
{
    Id3Reader id3Reader;
    private final boolean idrKeyframesOnly;
    private ExtractorOutput output;
    private final PtsTimestampAdjuster ptsTimestampAdjuster;
    final SparseBooleanArray streamTypes;
    private final ParsableByteArray tsPacketBuffer;
    final SparseArray<TsPayloadReader> tsPayloadReaders;
    private final ParsableBitArray tsScratch;
    
    public TsExtractor() {
        this(new PtsTimestampAdjuster(0L));
    }
    
    public TsExtractor(final PtsTimestampAdjuster ptsTimestampAdjuster) {
        this(ptsTimestampAdjuster, true);
    }
    
    public TsExtractor(final PtsTimestampAdjuster ptsTimestampAdjuster, final boolean idrKeyframesOnly) {
        this.idrKeyframesOnly = idrKeyframesOnly;
        this.tsScratch = new ParsableBitArray(new byte[3]);
        this.tsPacketBuffer = new ParsableByteArray(188);
        this.streamTypes = new SparseBooleanArray();
        (this.tsPayloadReaders = (SparseArray<TsPayloadReader>)new SparseArray()).put(0, (Object)new PatReader());
        this.ptsTimestampAdjuster = ptsTimestampAdjuster;
    }
    
    @Override
    public void init(final ExtractorOutput output) {
        (this.output = output).seekMap(SeekMap.UNSEEKABLE);
    }
    
    @Override
    public int read(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        final boolean b = false;
        int n;
        if (!extractorInput.readFully(this.tsPacketBuffer.data, 0, 188, true)) {
            n = -1;
        }
        else {
            this.tsPacketBuffer.setPosition(0);
            this.tsPacketBuffer.setLimit(188);
            n = (b ? 1 : 0);
            if (this.tsPacketBuffer.readUnsignedByte() == 71) {
                this.tsPacketBuffer.readBytes(this.tsScratch, 3);
                this.tsScratch.skipBits(1);
                final boolean bit = this.tsScratch.readBit();
                this.tsScratch.skipBits(1);
                final int bits = this.tsScratch.readBits(13);
                this.tsScratch.skipBits(2);
                final boolean bit2 = this.tsScratch.readBit();
                final boolean bit3 = this.tsScratch.readBit();
                if (bit2) {
                    this.tsPacketBuffer.skipBytes(this.tsPacketBuffer.readUnsignedByte());
                }
                n = (b ? 1 : 0);
                if (bit3) {
                    final TsPayloadReader tsPayloadReader = (TsPayloadReader)this.tsPayloadReaders.get(bits);
                    n = (b ? 1 : 0);
                    if (tsPayloadReader != null) {
                        tsPayloadReader.consume(this.tsPacketBuffer, bit, this.output);
                        return 0;
                    }
                }
            }
        }
        return n;
    }
    
    private class PatReader extends TsPayloadReader
    {
        private final ParsableBitArray patScratch;
        
        public PatReader() {
            this.patScratch = new ParsableBitArray(new byte[4]);
        }
        
        @Override
        public void consume(final ParsableByteArray parsableByteArray, final boolean b, final ExtractorOutput extractorOutput) {
            if (b) {
                parsableByteArray.skipBytes(parsableByteArray.readUnsignedByte());
            }
            parsableByteArray.readBytes(this.patScratch, 3);
            this.patScratch.skipBits(12);
            final int bits = this.patScratch.readBits(12);
            parsableByteArray.skipBytes(5);
            for (int n = (bits - 9) / 4, i = 0; i < n; ++i) {
                parsableByteArray.readBytes(this.patScratch, 4);
                this.patScratch.skipBits(19);
                TsExtractor.this.tsPayloadReaders.put(this.patScratch.readBits(13), (Object)new PmtReader());
            }
        }
    }
    
    private class PesReader extends TsPayloadReader
    {
        private boolean bodyStarted;
        private int bytesRead;
        private boolean dtsFlag;
        private int extendedHeaderLength;
        private int payloadSize;
        private final ElementaryStreamReader pesPayloadReader;
        private final ParsableBitArray pesScratch;
        private boolean ptsFlag;
        private boolean seenFirstDts;
        private int state;
        private long timeUs;
        
        public PesReader(final ElementaryStreamReader pesPayloadReader) {
            this.pesPayloadReader = pesPayloadReader;
            this.pesScratch = new ParsableBitArray(new byte[10]);
            this.state = 0;
        }
        
        private boolean continueRead(final ParsableByteArray parsableByteArray, final byte[] array, final int n) {
            final int min = Math.min(parsableByteArray.bytesLeft(), n - this.bytesRead);
            if (min > 0) {
                if (array == null) {
                    parsableByteArray.skipBytes(min);
                }
                else {
                    parsableByteArray.readBytes(array, this.bytesRead, min);
                }
                this.bytesRead += min;
                if (this.bytesRead != n) {
                    return false;
                }
            }
            return true;
        }
        
        private boolean parseHeader() {
            this.pesScratch.setPosition(0);
            final int bits = this.pesScratch.readBits(24);
            if (bits != 1) {
                Log.w("TsExtractor", "Unexpected start code prefix: " + bits);
                this.payloadSize = -1;
                return false;
            }
            this.pesScratch.skipBits(8);
            final int bits2 = this.pesScratch.readBits(16);
            this.pesScratch.skipBits(8);
            this.ptsFlag = this.pesScratch.readBit();
            this.dtsFlag = this.pesScratch.readBit();
            this.pesScratch.skipBits(6);
            this.extendedHeaderLength = this.pesScratch.readBits(8);
            if (bits2 == 0) {
                this.payloadSize = -1;
            }
            else {
                this.payloadSize = bits2 + 6 - 9 - this.extendedHeaderLength;
            }
            return true;
        }
        
        private void parseHeaderExtension() {
            this.pesScratch.setPosition(0);
            this.timeUs = 0L;
            if (this.ptsFlag) {
                this.pesScratch.skipBits(4);
                final long n = this.pesScratch.readBits(3);
                this.pesScratch.skipBits(1);
                final long n2 = this.pesScratch.readBits(15) << 15;
                this.pesScratch.skipBits(1);
                final long n3 = this.pesScratch.readBits(15);
                this.pesScratch.skipBits(1);
                if (!this.seenFirstDts && this.dtsFlag) {
                    this.pesScratch.skipBits(4);
                    final long n4 = this.pesScratch.readBits(3);
                    this.pesScratch.skipBits(1);
                    final long n5 = this.pesScratch.readBits(15) << 15;
                    this.pesScratch.skipBits(1);
                    final long n6 = this.pesScratch.readBits(15);
                    this.pesScratch.skipBits(1);
                    TsExtractor.this.ptsTimestampAdjuster.adjustTimestamp(n4 << 30 | n5 | n6);
                    this.seenFirstDts = true;
                }
                this.timeUs = TsExtractor.this.ptsTimestampAdjuster.adjustTimestamp(n << 30 | n2 | n3);
            }
        }
        
        private void setState(final int state) {
            this.state = state;
            this.bytesRead = 0;
        }
        
        @Override
        public void consume(final ParsableByteArray parsableByteArray, final boolean b, final ExtractorOutput extractorOutput) {
            if (b) {
                switch (this.state) {
                    case 2: {
                        Log.w("TsExtractor", "Unexpected start indicator reading extended header");
                        break;
                    }
                    case 3: {
                        if (this.payloadSize != -1) {
                            Log.w("TsExtractor", "Unexpected start indicator: expected " + this.payloadSize + " more bytes");
                        }
                        if (this.bodyStarted) {
                            this.pesPayloadReader.packetFinished();
                            break;
                        }
                        break;
                    }
                }
                this.setState(1);
            }
            while (parsableByteArray.bytesLeft() > 0) {
                switch (this.state) {
                    default: {
                        continue;
                    }
                    case 0: {
                        parsableByteArray.skipBytes(parsableByteArray.bytesLeft());
                        continue;
                    }
                    case 1: {
                        if (this.continueRead(parsableByteArray, this.pesScratch.data, 9)) {
                            int state;
                            if (this.parseHeader()) {
                                state = 2;
                            }
                            else {
                                state = 0;
                            }
                            this.setState(state);
                            continue;
                        }
                        continue;
                    }
                    case 2: {
                        if (this.continueRead(parsableByteArray, this.pesScratch.data, Math.min(10, this.extendedHeaderLength)) && this.continueRead(parsableByteArray, null, this.extendedHeaderLength)) {
                            this.parseHeaderExtension();
                            this.bodyStarted = false;
                            this.setState(3);
                            continue;
                        }
                        continue;
                    }
                    case 3: {
                        final int bytesLeft = parsableByteArray.bytesLeft();
                        int n;
                        if (this.payloadSize == -1) {
                            n = 0;
                        }
                        else {
                            n = bytesLeft - this.payloadSize;
                        }
                        int n2 = bytesLeft;
                        if (n > 0) {
                            n2 = bytesLeft - n;
                            parsableByteArray.setLimit(parsableByteArray.getPosition() + n2);
                        }
                        this.pesPayloadReader.consume(parsableByteArray, this.timeUs, !this.bodyStarted);
                        this.bodyStarted = true;
                        if (this.payloadSize == -1) {
                            continue;
                        }
                        this.payloadSize -= n2;
                        if (this.payloadSize == 0) {
                            this.pesPayloadReader.packetFinished();
                            this.setState(1);
                            continue;
                        }
                        continue;
                    }
                }
            }
        }
    }
    
    private class PmtReader extends TsPayloadReader
    {
        private final ParsableBitArray pmtScratch;
        
        public PmtReader() {
            this.pmtScratch = new ParsableBitArray(new byte[5]);
        }
        
        @Override
        public void consume(final ParsableByteArray parsableByteArray, final boolean b, final ExtractorOutput extractorOutput) {
            if (b) {
                parsableByteArray.skipBytes(parsableByteArray.readUnsignedByte());
            }
            parsableByteArray.readBytes(this.pmtScratch, 3);
            this.pmtScratch.skipBits(12);
            final int bits = this.pmtScratch.readBits(12);
            parsableByteArray.skipBytes(7);
            parsableByteArray.readBytes(this.pmtScratch, 2);
            this.pmtScratch.skipBits(4);
            final int bits2 = this.pmtScratch.readBits(12);
            parsableByteArray.skipBytes(bits2);
            if (TsExtractor.this.id3Reader == null) {
                TsExtractor.this.id3Reader = new Id3Reader(extractorOutput.track(21));
            }
            int i = bits - 9 - bits2 - 4;
            while (i > 0) {
                parsableByteArray.readBytes(this.pmtScratch, 5);
                final int bits3 = this.pmtScratch.readBits(8);
                this.pmtScratch.skipBits(3);
                final int bits4 = this.pmtScratch.readBits(13);
                this.pmtScratch.skipBits(4);
                final int bits5 = this.pmtScratch.readBits(12);
                parsableByteArray.skipBytes(bits5);
                final int n = i -= bits5 + 5;
                if (!TsExtractor.this.streamTypes.get(bits3)) {
                    ElementaryStreamReader id3Reader = null;
                    switch (bits3) {
                        case 3: {
                            id3Reader = new MpegAudioReader(extractorOutput.track(3));
                            break;
                        }
                        case 4: {
                            id3Reader = new MpegAudioReader(extractorOutput.track(4));
                            break;
                        }
                        case 15: {
                            id3Reader = new AdtsReader(extractorOutput.track(15));
                            break;
                        }
                        case 129:
                        case 135: {
                            id3Reader = new Ac3Reader(extractorOutput.track(bits3));
                            break;
                        }
                        case 27: {
                            id3Reader = new H264Reader(extractorOutput.track(27), new SeiReader(extractorOutput.track(256)), TsExtractor.this.idrKeyframesOnly);
                            break;
                        }
                        case 36: {
                            id3Reader = new H265Reader(extractorOutput.track(36), new SeiReader(extractorOutput.track(256)));
                            break;
                        }
                        case 21: {
                            id3Reader = TsExtractor.this.id3Reader;
                            break;
                        }
                    }
                    i = n;
                    if (id3Reader == null) {
                        continue;
                    }
                    TsExtractor.this.streamTypes.put(bits3, true);
                    TsExtractor.this.tsPayloadReaders.put(bits4, (Object)new PesReader(id3Reader));
                    i = n;
                }
            }
            extractorOutput.endTracks();
        }
    }
    
    private abstract static class TsPayloadReader
    {
        public abstract void consume(final ParsableByteArray p0, final boolean p1, final ExtractorOutput p2);
    }
}
