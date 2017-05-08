// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core;

public enum JsonEncoding
{
    UTF16_BE("UTF-16BE", true), 
    UTF16_LE("UTF-16LE", false), 
    UTF32_BE("UTF-32BE", true), 
    UTF32_LE("UTF-32LE", false), 
    UTF8("UTF-8", false);
    
    protected final boolean _bigEndian;
    protected final String _javaName;
    
    private JsonEncoding(final String javaName, final boolean bigEndian) {
        this._javaName = javaName;
        this._bigEndian = bigEndian;
    }
    
    public String getJavaName() {
        return this._javaName;
    }
}
