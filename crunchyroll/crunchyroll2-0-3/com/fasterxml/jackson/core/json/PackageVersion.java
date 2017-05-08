// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.Versioned;

public final class PackageVersion implements Versioned
{
    public static final Version VERSION;
    
    static {
        VERSION = VersionUtil.parseVersion("2.2.3", "com.fasterxml.jackson.core", "jackson-core");
    }
    
    @Override
    public Version version() {
        return PackageVersion.VERSION;
    }
}
