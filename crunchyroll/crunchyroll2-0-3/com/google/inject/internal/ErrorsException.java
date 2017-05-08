// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

public class ErrorsException extends Exception
{
    private final Errors errors;
    
    public ErrorsException(final Errors errors) {
        this.errors = errors;
    }
    
    public Errors getErrors() {
        return this.errors;
    }
}
