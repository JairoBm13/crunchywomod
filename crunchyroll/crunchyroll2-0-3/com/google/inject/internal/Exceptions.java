// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import java.lang.reflect.InvocationTargetException;

class Exceptions
{
    public static RuntimeException throwCleanly(final InvocationTargetException ex) {
        Throwable cause = ex;
        if (ex.getCause() != null) {
            cause = ex.getCause();
        }
        if (cause instanceof RuntimeException) {
            throw (RuntimeException)cause;
        }
        if (cause instanceof Error) {
            throw (Error)cause;
        }
        throw new UnhandledCheckedUserException(cause);
    }
    
    static class UnhandledCheckedUserException extends RuntimeException
    {
        public UnhandledCheckedUserException(final Throwable t) {
            super(t);
        }
    }
}
