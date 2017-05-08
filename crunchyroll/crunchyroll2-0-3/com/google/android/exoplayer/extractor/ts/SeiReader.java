// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.extractor.ts;

import com.google.android.exoplayer.text.eia608.Eia608Parser;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.extractor.TrackOutput;

final class SeiReader extends ElementaryStreamReader
{
    public SeiReader(final TrackOutput trackOutput) {
        super(trackOutput);
        trackOutput.format(MediaFormat.createTextFormat(-1, "application/eia-608", -1, -1L, null));
    }
    
    @Override
    public void consume(final ParsableByteArray parsableByteArray, final long n, final boolean b) {
        while (parsableByteArray.bytesLeft() > 1) {
            int n2 = 0;
            int i;
            do {
                i = parsableByteArray.readUnsignedByte();
                n2 += i;
            } while (i == 255);
            int n3 = 0;
            int j;
            int n4;
            do {
                j = parsableByteArray.readUnsignedByte();
                n4 = (n3 += j);
            } while (j == 255);
            if (Eia608Parser.isSeiMessageEia608(n2, n4, parsableByteArray)) {
                this.output.sampleData(parsableByteArray, n4);
                this.output.sampleMetadata(n, 1, n4, 0, null);
            }
            else {
                parsableByteArray.skipBytes(n4);
            }
        }
    }
    
    @Override
    public void packetFinished() {
    }
}
