// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;

public abstract class KeyDeserializer
{
    public abstract Object deserializeKey(final String p0, final DeserializationContext p1) throws IOException, JsonProcessingException;
    
    public abstract static class None extends KeyDeserializer
    {
    }
}
