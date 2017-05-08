// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core;

public class JsonParseException extends JsonProcessingException
{
    public JsonParseException(final String s, final JsonLocation jsonLocation) {
        super(s, jsonLocation);
    }
    
    public JsonParseException(final String s, final JsonLocation jsonLocation, final Throwable t) {
        super(s, jsonLocation, t);
    }
}
