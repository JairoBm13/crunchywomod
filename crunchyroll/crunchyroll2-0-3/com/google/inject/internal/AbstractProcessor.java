// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import java.util.Iterator;
import com.google.inject.spi.ElementVisitor;
import com.google.inject.spi.Element;
import java.util.List;
import com.google.inject.spi.DefaultElementVisitor;

abstract class AbstractProcessor extends DefaultElementVisitor<Boolean>
{
    protected Errors errors;
    protected InjectorImpl injector;
    
    protected AbstractProcessor(final Errors errors) {
        this.errors = errors;
    }
    
    public void process(final InjectorImpl injector, final List<Element> list) {
        final Errors errors = this.errors;
        this.injector = injector;
        try {
            final Iterator<Element> iterator = list.iterator();
            while (iterator.hasNext()) {
                final Element element = iterator.next();
                this.errors = errors.withSource(element.getSource());
                if (element.acceptVisitor((ElementVisitor<Boolean>)this)) {
                    iterator.remove();
                }
            }
        }
        finally {
            this.errors = errors;
            this.injector = null;
        }
        this.errors = errors;
        this.injector = null;
    }
    
    public void process(final Iterable<InjectorShell> iterable) {
        for (final InjectorShell injectorShell : iterable) {
            this.process(injectorShell.getInjector(), injectorShell.getElements());
        }
    }
    
    @Override
    protected Boolean visitOther(final Element element) {
        return false;
    }
}
