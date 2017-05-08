// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.extractor.ts;

import java.io.IOException;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.SeekMap;
import com.google.android.exoplayer.extractor.ExtractorOutput;
import com.google.android.exoplayer.util.Util;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.extractor.Extractor;

public final class AdtsExtractor implements Extractor
{
    private static final int ID3_TAG;
    private AdtsReader adtsReader;
    private boolean firstPacket;
    private final long firstSampleTimestampUs;
    private final ParsableByteArray packetBuffer;
    
    static {
        ID3_TAG = Util.getIntegerCodeForString("ID3");
    }
    
    public AdtsExtractor() {
        this(0L);
    }
    
    public AdtsExtractor(final long firstSampleTimestampUs) {
        this.firstSampleTimestampUs = firstSampleTimestampUs;
        this.packetBuffer = new ParsableByteArray(200);
        this.firstPacket = true;
    }
    
    @Override
    public void init(final ExtractorOutput extractorOutput) {
        this.adtsReader = new AdtsReader(extractorOutput.track(0));
        extractorOutput.endTracks();
        extractorOutput.seekMap(SeekMap.UNSEEKABLE);
    }
    
    @Override
    public int read(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        final int read = extractorInput.read(this.packetBuffer.data, 0, 200);
        if (read == -1) {
            return -1;
        }
        this.packetBuffer.setPosition(0);
        this.packetBuffer.setLimit(read);
        this.adtsReader.consume(this.packetBuffer, this.firstSampleTimestampUs, this.firstPacket);
        this.firstPacket = false;
        return 0;
    }
}
