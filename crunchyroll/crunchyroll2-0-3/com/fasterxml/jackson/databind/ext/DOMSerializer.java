// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ext;

import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.Node;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class DOMSerializer extends StdSerializer<Node>
{
    protected final DOMImplementationLS _domImpl;
    
    public DOMSerializer() {
        super(Node.class);
        try {
            this._domImpl = (DOMImplementationLS)DOMImplementationRegistry.newInstance().getDOMImplementation("LS");
        }
        catch (Exception ex) {
            throw new IllegalStateException("Could not instantiate DOMImplementationRegistry: " + ex.getMessage(), ex);
        }
    }
    
    @Override
    public void serialize(final Node node, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        if (this._domImpl == null) {
            throw new IllegalStateException("Could not find DOM LS");
        }
        jsonGenerator.writeString(this._domImpl.createLSSerializer().writeToString(node));
    }
}
