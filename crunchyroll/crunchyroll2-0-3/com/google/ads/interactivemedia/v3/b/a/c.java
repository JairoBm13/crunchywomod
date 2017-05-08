// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b.a;

public class c
{
    public String clickThroughUrl;
    public String companionId;
    public String size;
    public String src;
    public a type;
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final c c = (c)o;
            if (this.clickThroughUrl == null) {
                if (c.clickThroughUrl != null) {
                    return false;
                }
            }
            else if (!this.clickThroughUrl.equals(c.clickThroughUrl)) {
                return false;
            }
            if (this.companionId != null && c.companionId != null && !this.companionId.equals(c.companionId)) {
                return false;
            }
            if (this.size == null) {
                if (c.size != null) {
                    return false;
                }
            }
            else if (!this.size.equals(c.size)) {
                return false;
            }
            if (this.src == null) {
                if (c.src != null) {
                    return false;
                }
            }
            else if (!this.src.equals(c.src)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "CompanionData [companionId=" + this.companionId + ", size=" + this.size + ", src=" + this.src + ", clickThroughUrl=" + this.clickThroughUrl + "]";
    }
    
    public enum a
    {
        Html, 
        IFrame, 
        Static;
    }
}
