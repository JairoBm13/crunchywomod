// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.vast;

import com.secondtv.android.ads.vast.strategies.Vast1SingleAdStrategy;
import com.secondtv.android.ads.vast.strategies.Vast2SingleAdStrategy;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import com.secondtv.android.ads.vast.strategies.NullSingleAdStrategy;
import com.secondtv.android.ads.vast.strategies.SingleAdStrategy;
import org.xml.sax.helpers.DefaultHandler;

public class VastSingleAdHandler extends DefaultHandler
{
    private static final SingleAdStrategy NULL_STRATEGY;
    private LinearAd ad;
    private SingleAdStrategy strategy;
    
    static {
        NULL_STRATEGY = new NullSingleAdStrategy();
    }
    
    public VastSingleAdHandler() {
        this.strategy = VastSingleAdHandler.NULL_STRATEGY;
    }
    
    @Override
    public void characters(final char[] array, final int n, final int n2) throws SAXException {
        this.strategy.characters(array, n, n2);
    }
    
    @Override
    public void endElement(final String s, final String s2, final String s3) throws SAXException {
        this.strategy.endElement(s, s2, s3);
        if (Elements.Vast2Element.ROOT.getTag().equals(s2) || Elements.Vast1Element.ROOT.getTag().equals(s2)) {
            this.strategy = VastSingleAdHandler.NULL_STRATEGY;
        }
    }
    
    public LinearAd getAd() {
        return this.ad;
    }
    
    public void setAd(final LinearAd linearAd) {
        this.ad = linearAd;
        this.strategy.setAd(linearAd);
    }
    
    @Override
    public void startElement(final String s, final String s2, final String s3, final Attributes attributes) throws SAXException {
        if (Elements.Vast2Element.ROOT.getTag().equals(s2)) {
            (this.strategy = new Vast2SingleAdStrategy()).setAd(this.ad);
        }
        else if (Elements.Vast1Element.ROOT.getTag().equals(s2)) {
            (this.strategy = new Vast1SingleAdStrategy()).setAd(this.ad);
        }
        this.strategy.startElement(s, s2, s3, attributes);
    }
}
