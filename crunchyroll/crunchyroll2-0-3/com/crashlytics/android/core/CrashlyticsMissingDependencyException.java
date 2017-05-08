// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

public class CrashlyticsMissingDependencyException extends RuntimeException
{
    CrashlyticsMissingDependencyException(final String s) {
        super(buildExceptionMessage(s));
    }
    
    private static String buildExceptionMessage(final String s) {
        return "\n" + s + "\n";
    }
}
