// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject;

import java.util.Arrays;
import com.google.inject.internal.InternalInjectorCreator;

public final class Guice
{
    public static Injector createInjector(final Stage stage, final Iterable<? extends Module> iterable) {
        return new InternalInjectorCreator().stage(stage).addModules(iterable).build();
    }
    
    public static Injector createInjector(final Stage stage, final Module... array) {
        return createInjector(stage, Arrays.asList(array));
    }
}
