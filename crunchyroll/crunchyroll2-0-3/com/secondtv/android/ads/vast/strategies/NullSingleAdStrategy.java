// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.vast.strategies;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import com.secondtv.android.ads.vast.LinearAd;

public class NullSingleAdStrategy implements SingleAdStrategy
{
    @Override
    public void characters(final char[] array, final int n, final int n2) {
    }
    
    @Override
    public void endElement(final String s, final String s2, final String s3) {
    }
    
    @Override
    public LinearAd getAd() {
        return null;
    }
    
    @Override
    public void setAd(final LinearAd linearAd) {
    }
    
    @Override
    public void startElement(final String s, final String s2, final String s3, final Attributes attributes) throws SAXException {
    }
}
