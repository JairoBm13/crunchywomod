// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;

public class ReadableObjectId
{
    public final Object id;
    public Object item;
    
    public ReadableObjectId(final Object id) {
        this.id = id;
    }
    
    public void bindItem(final Object item) throws IOException {
        if (this.item != null) {
            throw new IllegalStateException("Already had POJO for id (" + this.id.getClass().getName() + ") [" + this.id + "]");
        }
        this.item = item;
    }
}
