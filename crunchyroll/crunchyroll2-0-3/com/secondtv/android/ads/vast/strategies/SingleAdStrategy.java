// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.vast.strategies;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import com.secondtv.android.ads.vast.LinearAd;

public interface SingleAdStrategy
{
    void characters(final char[] p0, final int p1, final int p2);
    
    void endElement(final String p0, final String p1, final String p2);
    
    LinearAd getAd();
    
    void setAd(final LinearAd p0);
    
    void startElement(final String p0, final String p1, final String p2, final Attributes p3) throws SAXException;
}
