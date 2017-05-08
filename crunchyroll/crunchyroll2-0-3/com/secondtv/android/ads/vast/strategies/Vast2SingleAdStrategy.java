// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.vast.strategies;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import com.secondtv.android.ads.vast.LinearAd;
import com.secondtv.android.ads.vast.Vast2AdHandlerDelegate;

public class Vast2SingleAdStrategy implements SingleAdStrategy
{
    private Vast2AdHandlerDelegate delegate;
    private int numAds;
    
    public Vast2SingleAdStrategy() {
        this.delegate = new Vast2AdHandlerDelegate();
    }
    
    @Override
    public void characters(final char[] array, final int n, final int n2) {
        this.delegate.characters(array, n, n2);
    }
    
    @Override
    public void endElement(final String s, final String s2, final String s3) {
        if (s2.equals("Ad")) {
            ++this.numAds;
        }
        this.delegate.endElement(s, s2, s3);
    }
    
    @Override
    public LinearAd getAd() {
        return this.delegate.getLinearAd();
    }
    
    @Override
    public void setAd(final LinearAd linearAd) {
        this.delegate.setLinearAd(linearAd);
        this.numAds = 0;
    }
    
    @Override
    public void startElement(final String s, final String s2, final String s3, final Attributes attributes) throws SAXException {
        if (s2.equals("Ad") && this.numAds >= 1) {
            throw new SAXException("Found multiple vast ads.");
        }
        this.delegate.startElement(s, s2, s3, attributes);
    }
}
