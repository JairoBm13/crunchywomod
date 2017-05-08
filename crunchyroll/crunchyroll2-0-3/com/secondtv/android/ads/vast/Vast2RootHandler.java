// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.vast;

import org.xml.sax.Attributes;
import com.secondtv.android.ads.vast.types.Playlist;

public class Vast2RootHandler implements VastRootStrategy
{
    private Vast2AdHandlerDelegate delegate;
    private Playlist playlist;
    
    @Override
    public void characters(final char[] array, final int n, final int n2) {
        if (this.delegate != null) {
            this.delegate.characters(array, n, n2);
        }
    }
    
    @Override
    public void endElement(final String s, final String s2, final String s3) {
        if (Elements.Vast2Element.AD.getTag().equals(s2)) {
            this.delegate.endElement(s, s2, s3);
            final LinearAd linearAd = this.delegate.getLinearAd();
            this.delegate.setLinearAd(null);
            this.delegate = null;
            if (linearAd != null) {
                this.playlist.addLinearAd(linearAd);
            }
        }
        else if (this.delegate != null) {
            this.delegate.endElement(s, s2, s3);
        }
    }
    
    @Override
    public Playlist getPlaylist() {
        return this.playlist;
    }
    
    @Override
    public void startElement(final String s, final String s2, final String s3, final Attributes attributes) {
        if (Elements.Vast2Element.ROOT.getTag().equals(s2)) {
            this.playlist = new Playlist();
        }
        else {
            if (Elements.Vast2Element.AD.getTag().equals(s2)) {
                (this.delegate = new Vast2AdHandlerDelegate()).setLinearAd(new LinearAd());
                this.delegate.startElement(s, s2, s3, attributes);
                return;
            }
            if (this.delegate != null) {
                this.delegate.startElement(s, s2, s3, attributes);
            }
        }
    }
}
