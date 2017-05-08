// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core;

import java.io.IOException;

public abstract class ObjectCodec
{
    public JsonFactory getFactory() {
        return this.getJsonFactory();
    }
    
    @Deprecated
    public abstract JsonFactory getJsonFactory();
    
    public abstract <T extends TreeNode> T readTree(final JsonParser p0) throws IOException, JsonProcessingException;
    
    public abstract void writeValue(final JsonGenerator p0, final Object p1) throws IOException, JsonProcessingException;
}
