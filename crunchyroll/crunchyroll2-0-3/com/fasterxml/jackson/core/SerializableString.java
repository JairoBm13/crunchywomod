// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core;

public interface SerializableString
{
    int appendQuotedUTF8(final byte[] p0, final int p1);
    
    char[] asQuotedChars();
    
    byte[] asQuotedUTF8();
    
    byte[] asUnquotedUTF8();
    
    String getValue();
}
