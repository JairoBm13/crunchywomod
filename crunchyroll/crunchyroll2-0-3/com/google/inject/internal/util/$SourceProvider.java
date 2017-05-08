// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

import java.util.ArrayList;
import java.util.List;

public final class $SourceProvider
{
    public static final $SourceProvider DEFAULT_INSTANCE;
    public static final Object UNKNOWN_SOURCE;
    private final $ImmutableSet<String> classNamesToSkip;
    
    static {
        UNKNOWN_SOURCE = "[unknown source]";
        DEFAULT_INSTANCE = new $SourceProvider($ImmutableSet.of($SourceProvider.class.getName()));
    }
    
    private $SourceProvider(final Iterable<String> iterable) {
        this.classNamesToSkip = $ImmutableSet.copyOf((Iterable<? extends String>)iterable);
    }
    
    private static List<String> asStrings(final Class... array) {
        final ArrayList<String> arrayList = $Lists.newArrayList();
        for (int length = array.length, i = 0; i < length; ++i) {
            arrayList.add(array[i].getName());
        }
        return arrayList;
    }
    
    public StackTraceElement get() {
        final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        for (int length = stackTrace.length, i = 0; i < length; ++i) {
            final StackTraceElement stackTraceElement = stackTrace[i];
            if (!this.classNamesToSkip.contains(stackTraceElement.getClassName())) {
                return stackTraceElement;
            }
        }
        throw new AssertionError();
    }
    
    public $SourceProvider plusSkippedClasses(final Class... array) {
        return new $SourceProvider($Iterables.concat((Iterable<? extends String>)this.classNamesToSkip, (Iterable<? extends String>)asStrings(array)));
    }
}
