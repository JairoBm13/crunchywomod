// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonFactory;

public class MappingJsonFactory extends JsonFactory
{
    public MappingJsonFactory() {
        this(null);
    }
    
    public MappingJsonFactory(final ObjectMapper objectMapper) {
        super(objectMapper);
        if (objectMapper == null) {
            this.setCodec(new ObjectMapper(this));
        }
    }
    
    @Override
    public final ObjectMapper getCodec() {
        return (ObjectMapper)this._objectCodec;
    }
    
    @Override
    public String getFormatName() {
        return "JSON";
    }
}
