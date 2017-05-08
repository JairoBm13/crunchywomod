// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.core.format.InputAccessor;
import com.fasterxml.jackson.databind.ObjectReader;

public class DataFormatReaders
{
    protected final ObjectReader[] _readers;
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        final int length = this._readers.length;
        if (length > 0) {
            sb.append(this._readers[0].getFactory().getFormatName());
            for (int i = 1; i < length; ++i) {
                sb.append(", ");
                sb.append(this._readers[i].getFactory().getFormatName());
            }
        }
        sb.append(']');
        return sb.toString();
    }
    
    protected class AccessorForReader extends Std
    {
    }
    
    public static class Match
    {
    }
}
