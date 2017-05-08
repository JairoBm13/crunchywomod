// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.jsonschema;

import com.fasterxml.jackson.databind.node.ObjectNode;

@Deprecated
public class JsonSchema
{
    private final ObjectNode schema;
    
    @Override
    public boolean equals(final Object o) {
        if (o != this) {
            if (o == null) {
                return false;
            }
            if (!(o instanceof JsonSchema)) {
                return false;
            }
            final JsonSchema jsonSchema = (JsonSchema)o;
            if (this.schema != null) {
                return this.schema.equals(jsonSchema.schema);
            }
            if (jsonSchema.schema != null) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return this.schema.hashCode();
    }
    
    @Override
    public String toString() {
        return this.schema.toString();
    }
}
