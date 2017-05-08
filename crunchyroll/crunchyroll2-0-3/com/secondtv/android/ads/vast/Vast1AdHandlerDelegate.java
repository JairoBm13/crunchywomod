// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.vast;

import org.xml.sax.Attributes;
import com.secondtv.android.ads.vast.types.MediaFile;
import java.util.Stack;

public class Vast1AdHandlerDelegate
{
    private Stack<StringBuilder> characterDatas;
    private MediaFile currentMediaFile;
    private TrackingType currentTrackingType;
    private LinearAd linearAd;
    private Stack<String> nodes;
    
    public Vast1AdHandlerDelegate() {
        this.nodes = new Stack<String>();
        this.characterDatas = new Stack<StringBuilder>();
    }
    
    private void endMediaFile() {
        this.linearAd.addMediaFile(this.currentMediaFile);
        this.currentMediaFile = null;
    }
    
    private void endTracking() {
        this.currentTrackingType = null;
    }
    
    private void endUrl(final String downstreamAdUrl) {
        final Elements.Vast1Element element = Elements.Vast1Element.getElement(this.nodes.peek());
        if (element == null || downstreamAdUrl == null) {
            return;
        }
        switch (element) {
            default: {}
            case CLICK_THROUGH: {
                this.linearAd.setClickThroughUrl(downstreamAdUrl);
            }
            case CLICK_TRACKING: {
                this.linearAd.addClickTrackingUrl(downstreamAdUrl);
            }
            case ERROR: {
                this.linearAd.addErrorUrl(downstreamAdUrl);
            }
            case IMPRESSION: {
                this.linearAd.addImpressionUrl(downstreamAdUrl);
            }
            case MEDIA_FILE: {
                this.currentMediaFile.setUrl(downstreamAdUrl);
            }
            case TRACKING: {
                this.linearAd.addTrackingUrlWithType(downstreamAdUrl, this.currentTrackingType);
            }
            case VAST_AD_TAG_URL: {
                this.linearAd.setDownstreamAdUrl(downstreamAdUrl);
            }
        }
    }
    
    private void setAdId(final Attributes attributes) {
        final String value = attributes.getValue("", "id");
        if (value != null && value.length() > 0) {
            this.linearAd.setId(value);
        }
    }
    
    private void startMediaFile(final Attributes attributes) {
        (this.currentMediaFile = new MediaFile()).setId(attributes.getValue("", "id"));
        this.currentMediaFile.setMimeType(attributes.getValue("", "type"));
    }
    
    private void startTracking(final Attributes attributes) {
        final String value = attributes.getValue("", "event");
        try {
            this.currentTrackingType = TrackingType.fromVastValue(value);
        }
        catch (IllegalArgumentException ex) {}
    }
    
    public void characters(final char[] array, final int n, final int n2) {
        this.characterDatas.peek().append(array, n, n2);
    }
    
    public void endElement(String trim, final String s, final String s2) {
        this.nodes.pop();
        trim = this.characterDatas.pop().toString().trim();
        if (Elements.Vast1Element.URL.getTag().equals(s) && trim.length() > 0) {
            this.endUrl(trim);
        }
        else {
            if (Elements.Vast1Element.TRACKING.getTag().equals(s)) {
                this.endTracking();
                return;
            }
            if (Elements.Vast1Element.MEDIA_FILE.getTag().equals(s)) {
                this.endMediaFile();
            }
        }
    }
    
    public LinearAd getLinearAd() {
        return this.linearAd;
    }
    
    public void setLinearAd(final LinearAd linearAd) {
        this.linearAd = linearAd;
    }
    
    public void startElement(final String s, final String s2, final String s3, final Attributes adId) {
        this.nodes.push(s2);
        this.characterDatas.push(new StringBuilder());
        if (Elements.Vast1Element.AD.getTag().equals(s2)) {
            this.setAdId(adId);
        }
        else {
            if (Elements.Vast1Element.TRACKING.getTag().equals(s2)) {
                this.startTracking(adId);
                return;
            }
            if (Elements.Vast1Element.MEDIA_FILE.getTag().equals(s2)) {
                this.startMediaFile(adId);
            }
        }
    }
}
