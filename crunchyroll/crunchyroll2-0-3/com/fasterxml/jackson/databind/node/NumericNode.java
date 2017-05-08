// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.node;

public abstract class NumericNode extends ValueNode
{
    @Override
    public final int asInt() {
        return this.intValue();
    }
    
    @Override
    public final int asInt(final int n) {
        return this.intValue();
    }
    
    @Override
    public final JsonNodeType getNodeType() {
        return JsonNodeType.NUMBER;
    }
    
    @Override
    public abstract int intValue();
}
