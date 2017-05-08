// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;

public abstract class DeserializationProblemHandler
{
    public boolean handleUnknownProperty(final DeserializationContext deserializationContext, final JsonParser jsonParser, final JsonDeserializer<?> jsonDeserializer, final Object o, final String s) throws IOException, JsonProcessingException {
        return false;
    }
}
