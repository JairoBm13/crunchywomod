// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.cfg;

public interface ConfigFeature
{
    boolean enabledByDefault();
    
    int getMask();
}
