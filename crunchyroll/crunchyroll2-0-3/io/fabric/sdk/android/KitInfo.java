// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android;

public class KitInfo
{
    private final String buildType;
    private final String identifier;
    private final String version;
    
    public KitInfo(final String identifier, final String version, final String buildType) {
        this.identifier = identifier;
        this.version = version;
        this.buildType = buildType;
    }
    
    public String getBuildType() {
        return this.buildType;
    }
    
    public String getIdentifier() {
        return this.identifier;
    }
    
    public String getVersion() {
        return this.version;
    }
}
