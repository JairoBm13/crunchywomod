// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.spi.InjectionPoint;
import java.lang.reflect.Field;
import com.google.inject.spi.Dependency;

final class SingleFieldInjector implements SingleMemberInjector
{
    final Dependency<?> dependency;
    final InternalFactory<?> factory;
    final Field field;
    final InjectionPoint injectionPoint;
    
    public SingleFieldInjector(final InjectorImpl injectorImpl, final InjectionPoint injectionPoint, final Errors errors) throws ErrorsException {
        this.injectionPoint = injectionPoint;
        this.field = (Field)injectionPoint.getMember();
        this.dependency = injectionPoint.getDependencies().get(0);
        this.field.setAccessible(true);
        this.factory = injectorImpl.getInternalFactory(this.dependency.getKey(), errors, InjectorImpl.JitLimitation.NO_JIT);
    }
    
    @Override
    public InjectionPoint getInjectionPoint() {
        return this.injectionPoint;
    }
    
    @Override
    public void inject(Errors setDependency, final InternalContext internalContext, final Object o) {
        final Errors withSource = setDependency.withSource(this.dependency);
        setDependency = (Errors)internalContext.setDependency(this.dependency);
        try {
            this.field.set(o, this.factory.get(withSource, internalContext, this.dependency, false));
        }
        catch (ErrorsException ex) {
            withSource.withSource(this.injectionPoint).merge(ex.getErrors());
        }
        catch (IllegalAccessException ex2) {
            throw new AssertionError((Object)ex2);
        }
        finally {
            internalContext.setDependency((Dependency)setDependency);
        }
    }
}
