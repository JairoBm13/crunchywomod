// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.databind.cfg.PackageVersion;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.core.PrettyPrinter;
import java.io.Serializable;
import com.fasterxml.jackson.core.Versioned;

public class ObjectWriter implements Versioned, Serializable
{
    protected static final PrettyPrinter NULL_PRETTY_PRINTER;
    
    static {
        NULL_PRETTY_PRINTER = new MinimalPrettyPrinter();
    }
    
    @Override
    public Version version() {
        return PackageVersion.VERSION;
    }
}
