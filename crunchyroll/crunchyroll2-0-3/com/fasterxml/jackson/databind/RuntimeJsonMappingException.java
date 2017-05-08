// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

public class RuntimeJsonMappingException extends RuntimeException
{
    public RuntimeJsonMappingException(final String s, final JsonMappingException ex) {
        super(s, ex);
    }
}
