// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.vast;

import android.text.TextUtils;
import org.xml.sax.Attributes;
import com.secondtv.android.ads.vast.types.MediaFile;
import java.util.Stack;

public class Vast2AdHandlerDelegate
{
    private Stack<StringBuilder> characterDatas;
    private MediaFile currentMediaFile;
    private TrackingType currentTrackingType;
    private LinearAd linearAd;
    private Stack<String> nodes;
    
    public Vast2AdHandlerDelegate() {
        this.nodes = new Stack<String>();
        this.characterDatas = new Stack<StringBuilder>();
    }
    
    private void setAdId(final Attributes attributes) {
        final String value = attributes.getValue("", "id");
        if (value != null && value.length() > 0) {
            this.linearAd.setId(value);
        }
    }
    
    private void setCurrentMediaFile(final Attributes attributes) {
        (this.currentMediaFile = new MediaFile()).setId(attributes.getValue("", "id"));
        this.currentMediaFile.setMimeType(attributes.getValue("", "type"));
    }
    
    private void setCurrentTrackingType(final Attributes attributes) {
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
        final Elements.Vast2Element element = Elements.Vast2Element.getElement(s);
        if (element != null) {
            switch (element) {
                default: {}
                case CLICK_THROUGH: {
                    if (trim != null) {
                        this.linearAd.setClickThroughUrl(trim);
                        return;
                    }
                    break;
                }
                case CLICK_TRACKING: {
                    if (trim != null && !TextUtils.isEmpty((CharSequence)trim)) {
                        this.linearAd.addClickTrackingUrl(trim);
                        return;
                    }
                    break;
                }
                case ERROR: {
                    if (trim != null && !TextUtils.isEmpty((CharSequence)trim)) {
                        this.linearAd.addErrorUrl(trim);
                        return;
                    }
                    break;
                }
                case IMPRESSION: {
                    if (trim != null && !TextUtils.isEmpty((CharSequence)trim)) {
                        this.linearAd.addImpressionUrl(trim);
                        return;
                    }
                    break;
                }
                case MEDIA_FILE: {
                    if (trim != null) {
                        this.currentMediaFile.setUrl(trim);
                        this.linearAd.addMediaFile(this.currentMediaFile);
                    }
                    this.currentMediaFile = null;
                }
                case TRACKING: {
                    if (trim != null && !TextUtils.isEmpty((CharSequence)trim)) {
                        this.linearAd.addTrackingUrlWithType(trim, this.currentTrackingType);
                    }
                    this.currentTrackingType = null;
                }
                case VAST_AD_TAG_URI: {
                    if (trim != null && !TextUtils.isEmpty((CharSequence)trim)) {
                        this.linearAd.setDownstreamAdUrl(trim);
                        return;
                    }
                    break;
                }
            }
        }
    }
    
    public LinearAd getLinearAd() {
        return this.linearAd;
    }
    
    public void setLinearAd(final LinearAd linearAd) {
        this.linearAd = linearAd;
    }
    
    public void startElement(final String s, final String s2, final String s3, final Attributes currentMediaFile) {
        this.nodes.push(s2);
        this.characterDatas.push(new StringBuilder());
        if (Elements.Vast2Element.AD.getTag().equals(s2)) {
            this.setAdId(currentMediaFile);
        }
        else {
            if (Elements.Vast2Element.TRACKING.getTag().equals(s2)) {
                this.setCurrentTrackingType(currentMediaFile);
                return;
            }
            if (Elements.Vast2Element.MEDIA_FILE.getTag().equals(s2)) {
                this.setCurrentMediaFile(currentMediaFile);
            }
        }
    }
}
