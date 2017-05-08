// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.Stage;
import com.google.inject.internal.util.$Objects;
import com.google.inject.Provider;
import com.google.inject.Key;
import java.lang.annotation.Annotation;
import com.google.inject.Singleton;
import com.google.inject.Scopes;
import com.google.inject.Scope;
import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.spi.BindingScopingVisitor;

public abstract class Scoping
{
    public static final Scoping EAGER_SINGLETON;
    public static final Scoping SINGLETON_ANNOTATION;
    public static final Scoping SINGLETON_INSTANCE;
    public static final Scoping UNSCOPED;
    
    static {
        UNSCOPED = new Scoping() {
            @Override
            public <V> V acceptVisitor(final BindingScopingVisitor<V> bindingScopingVisitor) {
                return bindingScopingVisitor.visitNoScoping();
            }
            
            @Override
            public void applyTo(final ScopedBindingBuilder scopedBindingBuilder) {
            }
            
            @Override
            public Scope getScopeInstance() {
                return Scopes.NO_SCOPE;
            }
            
            @Override
            public String toString() {
                return Scopes.NO_SCOPE.toString();
            }
        };
        SINGLETON_ANNOTATION = new Scoping() {
            @Override
            public <V> V acceptVisitor(final BindingScopingVisitor<V> bindingScopingVisitor) {
                return bindingScopingVisitor.visitScopeAnnotation(Singleton.class);
            }
            
            @Override
            public void applyTo(final ScopedBindingBuilder scopedBindingBuilder) {
                scopedBindingBuilder.in(Singleton.class);
            }
            
            @Override
            public Class<? extends Annotation> getScopeAnnotation() {
                return Singleton.class;
            }
            
            @Override
            public String toString() {
                return Singleton.class.getName();
            }
        };
        SINGLETON_INSTANCE = new Scoping() {
            @Override
            public <V> V acceptVisitor(final BindingScopingVisitor<V> bindingScopingVisitor) {
                return bindingScopingVisitor.visitScope(Scopes.SINGLETON);
            }
            
            @Override
            public void applyTo(final ScopedBindingBuilder scopedBindingBuilder) {
                scopedBindingBuilder.in(Scopes.SINGLETON);
            }
            
            @Override
            public Scope getScopeInstance() {
                return Scopes.SINGLETON;
            }
            
            @Override
            public String toString() {
                return Scopes.SINGLETON.toString();
            }
        };
        EAGER_SINGLETON = new Scoping() {
            @Override
            public <V> V acceptVisitor(final BindingScopingVisitor<V> bindingScopingVisitor) {
                return bindingScopingVisitor.visitEagerSingleton();
            }
            
            @Override
            public void applyTo(final ScopedBindingBuilder scopedBindingBuilder) {
                scopedBindingBuilder.asEagerSingleton();
            }
            
            @Override
            public Scope getScopeInstance() {
                return Scopes.SINGLETON;
            }
            
            @Override
            public String toString() {
                return "eager singleton";
            }
        };
    }
    
    public static Scoping forAnnotation(final Class<? extends Annotation> clazz) {
        if (clazz == Singleton.class || clazz == javax.inject.Singleton.class) {
            return Scoping.SINGLETON_ANNOTATION;
        }
        return new Scoping() {
            @Override
            public <V> V acceptVisitor(final BindingScopingVisitor<V> bindingScopingVisitor) {
                return bindingScopingVisitor.visitScopeAnnotation(clazz);
            }
            
            @Override
            public void applyTo(final ScopedBindingBuilder scopedBindingBuilder) {
                scopedBindingBuilder.in(clazz);
            }
            
            @Override
            public Class<? extends Annotation> getScopeAnnotation() {
                return clazz;
            }
            
            @Override
            public String toString() {
                return clazz.getName();
            }
        };
    }
    
    public static Scoping forInstance(final Scope scope) {
        if (scope == Scopes.SINGLETON) {
            return Scoping.SINGLETON_INSTANCE;
        }
        return new Scoping() {
            @Override
            public <V> V acceptVisitor(final BindingScopingVisitor<V> bindingScopingVisitor) {
                return bindingScopingVisitor.visitScope(scope);
            }
            
            @Override
            public void applyTo(final ScopedBindingBuilder scopedBindingBuilder) {
                scopedBindingBuilder.in(scope);
            }
            
            @Override
            public Scope getScopeInstance() {
                return scope;
            }
            
            @Override
            public String toString() {
                return scope.toString();
            }
        };
    }
    
    static Scoping makeInjectable(final Scoping scoping, final InjectorImpl injectorImpl, final Errors errors) {
        final Class<? extends Annotation> scopeAnnotation = scoping.getScopeAnnotation();
        if (scopeAnnotation == null) {
            return scoping;
        }
        final Scope scope = injectorImpl.state.getScope(scopeAnnotation);
        if (scope != null) {
            return forInstance(scope);
        }
        errors.scopeNotFound(scopeAnnotation);
        return Scoping.UNSCOPED;
    }
    
    static <T> InternalFactory<? extends T> scope(final Key<T> key, final InjectorImpl injectorImpl, final InternalFactory<? extends T> internalFactory, final Object o, final Scoping scoping) {
        if (scoping.isNoScope()) {
            return internalFactory;
        }
        return (InternalFactory<? extends T>)new InternalFactoryToProviderAdapter<T>((Initializable<Provider<? extends T>>)Initializables.of((Provider<? extends T>)scoping.getScopeInstance().scope(key, new ProviderToInternalFactoryAdapter<T>(injectorImpl, internalFactory))), o);
    }
    
    public abstract <V> V acceptVisitor(final BindingScopingVisitor<V> p0);
    
    public abstract void applyTo(final ScopedBindingBuilder p0);
    
    @Override
    public boolean equals(final Object o) {
        boolean b2;
        final boolean b = b2 = false;
        if (o instanceof Scoping) {
            final Scoping scoping = (Scoping)o;
            b2 = b;
            if ($Objects.equal(this.getScopeAnnotation(), scoping.getScopeAnnotation())) {
                b2 = b;
                if ($Objects.equal(this.getScopeInstance(), scoping.getScopeInstance())) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    public Class<? extends Annotation> getScopeAnnotation() {
        return null;
    }
    
    public Scope getScopeInstance() {
        return null;
    }
    
    @Override
    public int hashCode() {
        return $Objects.hashCode(this.getScopeAnnotation(), this.getScopeInstance());
    }
    
    public boolean isEagerSingleton(final Stage stage) {
        final boolean b = false;
        boolean b2;
        if (this == Scoping.EAGER_SINGLETON) {
            b2 = true;
        }
        else {
            b2 = b;
            if (stage == Stage.PRODUCTION) {
                if (this != Scoping.SINGLETON_ANNOTATION) {
                    b2 = b;
                    if (this != Scoping.SINGLETON_INSTANCE) {
                        return b2;
                    }
                }
                return true;
            }
        }
        return b2;
    }
    
    public boolean isExplicitlyScoped() {
        return this != Scoping.UNSCOPED;
    }
    
    public boolean isNoScope() {
        return this.getScopeInstance() == Scopes.NO_SCOPE;
    }
}
