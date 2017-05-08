// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.vast;

import java.util.Iterator;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public final class Elements
{
    public enum Vast1Element implements VastElement
    {
        AD("Ad"), 
        CLICK_THROUGH("ClickThrough"), 
        CLICK_TRACKING("ClickTracking"), 
        ERROR("Error"), 
        IMPRESSION("Impression"), 
        MEDIA_FILE("MediaFile"), 
        ROOT("VideoAdServingTemplate"), 
        TRACKING("Tracking"), 
        URL("URL"), 
        VAST_AD_TAG_URL("VASTAdTagURL");
        
        private static final Map<String, Vast1Element> reverseMapping;
        private final String tag;
        
        static {
            final HashMap<String, Vast1Element> hashMap = new HashMap<String, Vast1Element>();
            for (final Vast1Element vast1Element : EnumSet.allOf(Vast1Element.class)) {
                hashMap.put(vast1Element.getTag(), vast1Element);
            }
            reverseMapping = Collections.unmodifiableMap((Map<?, ?>)hashMap);
        }
        
        private Vast1Element(final String tag) {
            this.tag = tag;
        }
        
        public static Vast1Element getElement(final String s) {
            return Vast1Element.reverseMapping.get(s);
        }
        
        @Override
        public String getTag() {
            return this.tag;
        }
    }
    
    public enum Vast2Element implements VastElement
    {
        AD("Ad"), 
        CLICK_THROUGH("ClickThrough"), 
        CLICK_TRACKING("ClickTracking"), 
        ERROR("Error"), 
        IMPRESSION("Impression"), 
        MEDIA_FILE("MediaFile"), 
        ROOT("VAST"), 
        TRACKING("Tracking"), 
        VAST_AD_TAG_URI("VASTAdTagURI");
        
        private static final Map<String, Vast2Element> reverseMapping;
        private final String tag;
        
        static {
            final HashMap<String, Vast2Element> hashMap = new HashMap<String, Vast2Element>();
            for (final Vast2Element vast2Element : EnumSet.allOf(Vast2Element.class)) {
                hashMap.put(vast2Element.getTag(), vast2Element);
            }
            reverseMapping = Collections.unmodifiableMap((Map<?, ?>)hashMap);
        }
        
        private Vast2Element(final String tag) {
            this.tag = tag;
        }
        
        public static Vast2Element getElement(final String s) {
            return Vast2Element.reverseMapping.get(s);
        }
        
        @Override
        public String getTag() {
            return this.tag;
        }
    }
    
    public interface VastElement
    {
        String getTag();
    }
}
