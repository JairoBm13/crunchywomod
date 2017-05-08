// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.name;

public class Names
{
    public static Named named(final String s) {
        return new NamedImpl(s);
    }
}
