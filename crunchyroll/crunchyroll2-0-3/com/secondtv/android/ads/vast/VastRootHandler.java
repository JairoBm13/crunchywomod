// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.vast;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import com.secondtv.android.ads.vast.types.Playlist;
import org.xml.sax.helpers.DefaultHandler;

public class VastRootHandler extends DefaultHandler
{
    private static final VastRootStrategy NULL_STRATEGY;
    private Playlist playlist;
    private VastRootStrategy strategy;
    
    static {
        NULL_STRATEGY = new NullRootStrategy();
    }
    
    public VastRootHandler() {
        this.strategy = VastRootHandler.NULL_STRATEGY;
    }
    
    @Override
    public void characters(final char[] array, final int n, final int n2) throws SAXException {
        this.strategy.characters(array, n, n2);
    }
    
    @Override
    public void endElement(final String s, final String s2, final String s3) throws SAXException {
        this.strategy.endElement(s, s2, s3);
        if (Elements.Vast2Element.ROOT.getTag().equals(s2) || Elements.Vast1Element.ROOT.getTag().equals(s2)) {
            this.playlist = this.strategy.getPlaylist();
            this.strategy = VastRootHandler.NULL_STRATEGY;
        }
    }
    
    public Playlist getPlaylist() {
        return this.playlist;
    }
    
    @Override
    public void startElement(final String s, final String s2, final String s3, final Attributes attributes) throws SAXException {
        if (Elements.Vast2Element.ROOT.getTag().equals(s2)) {
            this.playlist = null;
            this.strategy = new Vast2RootHandler();
        }
        else if (Elements.Vast1Element.ROOT.getTag().equals(s2)) {
            this.playlist = null;
            this.strategy = new Vast1RootHandler();
        }
        this.strategy.startElement(s, s2, s3, attributes);
    }
}
