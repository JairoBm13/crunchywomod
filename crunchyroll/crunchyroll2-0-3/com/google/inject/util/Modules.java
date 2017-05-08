// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.util;

import com.google.inject.internal.util.$ImmutableSet;
import java.util.Iterator;
import com.google.inject.spi.ElementVisitor;
import com.google.inject.spi.Element;
import com.google.inject.spi.DefaultElementVisitor;
import com.google.inject.Binder;
import com.google.inject.Module;

public final class Modules
{
    public static final Module EMPTY_MODULE;
    
    static {
        EMPTY_MODULE = new Module() {
            @Override
            public void configure(final Binder binder) {
            }
        };
    }
    
    private static class ModuleWriter extends DefaultElementVisitor<Void>
    {
        protected final Binder binder;
        
        ModuleWriter(final Binder binder) {
            this.binder = binder;
        }
        
        @Override
        protected Void visitOther(final Element element) {
            element.applyTo(this.binder);
            return null;
        }
        
        void writeAll(final Iterable<? extends Element> iterable) {
            final Iterator<? extends Element> iterator = iterable.iterator();
            while (iterator.hasNext()) {
                ((Element)iterator.next()).acceptVisitor((ElementVisitor<Object>)this);
            }
        }
    }
    
    public interface OverriddenModuleBuilder
    {
    }
    
    private static final class RealOverriddenModuleBuilder implements OverriddenModuleBuilder
    {
        private final $ImmutableSet<Module> baseModules;
    }
}
