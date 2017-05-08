// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.cache.disc.naming;

public class HashCodeFileNameGenerator implements FileNameGenerator
{
    @Override
    public String generate(final String s) {
        return String.valueOf(s.hashCode());
    }
}
