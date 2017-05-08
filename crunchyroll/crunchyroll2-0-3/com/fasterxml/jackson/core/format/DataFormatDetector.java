// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.format;

import com.fasterxml.jackson.core.JsonFactory;

public class DataFormatDetector
{
    protected final JsonFactory[] _detectors;
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        final int length = this._detectors.length;
        if (length > 0) {
            sb.append(this._detectors[0].getFormatName());
            for (int i = 1; i < length; ++i) {
                sb.append(", ");
                sb.append(this._detectors[i].getFormatName());
            }
        }
        sb.append(']');
        return sb.toString();
    }
}
