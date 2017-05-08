// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ext;

import org.w3c.dom.Node;
import java.io.Reader;
import org.xml.sax.InputSource;
import java.io.StringReader;
import org.w3c.dom.Document;
import com.fasterxml.jackson.databind.DeserializationContext;
import javax.xml.parsers.DocumentBuilderFactory;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;

public abstract class DOMDeserializer<T> extends FromStringDeserializer<T>
{
    private static final DocumentBuilderFactory _parserFactory;
    
    static {
        (_parserFactory = DocumentBuilderFactory.newInstance()).setNamespaceAware(true);
    }
    
    protected DOMDeserializer(final Class<T> clazz) {
        super(clazz);
    }
    
    public abstract T _deserialize(final String p0, final DeserializationContext p1);
    
    protected final Document parse(final String s) throws IllegalArgumentException {
        try {
            return DOMDeserializer._parserFactory.newDocumentBuilder().parse(new InputSource(new StringReader(s)));
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("Failed to parse JSON String as XML: " + ex.getMessage(), ex);
        }
    }
    
    public static class DocumentDeserializer extends DOMDeserializer<Document>
    {
        public DocumentDeserializer() {
            super(Document.class);
        }
        
        @Override
        public Document _deserialize(final String s, final DeserializationContext deserializationContext) throws IllegalArgumentException {
            return this.parse(s);
        }
    }
    
    public static class NodeDeserializer extends DOMDeserializer<Node>
    {
        public NodeDeserializer() {
            super(Node.class);
        }
        
        @Override
        public Node _deserialize(final String s, final DeserializationContext deserializationContext) throws IllegalArgumentException {
            return this.parse(s);
        }
    }
}
