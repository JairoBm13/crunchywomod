// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.vast;

import org.xml.sax.Attributes;
import com.secondtv.android.ads.vast.types.Playlist;

public class NullRootStrategy implements VastRootStrategy
{
    @Override
    public void characters(final char[] array, final int n, final int n2) {
    }
    
    @Override
    public void endElement(final String s, final String s2, final String s3) {
    }
    
    @Override
    public Playlist getPlaylist() {
        return null;
    }
    
    @Override
    public void startElement(final String s, final String s2, final String s3, final Attributes attributes) {
    }
}
