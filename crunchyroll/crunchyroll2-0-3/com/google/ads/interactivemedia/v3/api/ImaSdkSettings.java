// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.api;

public class ImaSdkSettings
{
    private transient String language;
    private String ppid;
    
    public ImaSdkSettings() {
        this.language = "en";
    }
    
    public String getLanguage() {
        return this.language;
    }
    
    @Override
    public String toString() {
        return "ImaSdkSettings [ppid=" + this.ppid + ", language=" + this.language + "]";
    }
}
