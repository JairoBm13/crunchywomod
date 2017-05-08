// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.databind.cfg.PackageVersion;
import com.fasterxml.jackson.core.Version;
import java.io.Serializable;
import com.fasterxml.jackson.databind.AnnotationIntrospector;

public abstract class NopAnnotationIntrospector extends AnnotationIntrospector implements Serializable
{
    public static final NopAnnotationIntrospector instance;
    
    static {
        instance = new NopAnnotationIntrospector() {
            @Override
            public Version version() {
                return PackageVersion.VERSION;
            }
        };
    }
    
    @Override
    public Version version() {
        return Version.unknownVersion();
    }
}
