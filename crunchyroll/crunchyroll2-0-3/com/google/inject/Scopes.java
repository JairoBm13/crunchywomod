// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject;

import com.google.inject.internal.CircularDependencyProxy;
import com.google.inject.internal.InternalInjectorCreator;

public class Scopes
{
    public static final Scope NO_SCOPE;
    private static final Object NULL;
    public static final Scope SINGLETON;
    
    static {
        NULL = new Object();
        SINGLETON = new Scope() {
            @Override
            public <T> Provider<T> scope(final Key<T> key, final Provider<T> provider) {
                return new Provider<T>() {
                    private volatile Object instance;
                    
                    @Override
                    public T get() {
                        if (this.instance == null) {
                            while (true) {
                                Object access$000 = null;
                            Label_0081:
                                while (true) {
                                    Label_0078: {
                                        synchronized (InternalInjectorCreator.class) {
                                            if (this.instance != null) {
                                                break;
                                            }
                                            final T value = provider.get();
                                            if (value instanceof CircularDependencyProxy) {
                                                return value;
                                            }
                                            if (value != null) {
                                                break Label_0078;
                                            }
                                            access$000 = Scopes.NULL;
                                            if (this.instance != null && this.instance != access$000) {
                                                throw new ProvisionException("Provider was reentrant while creating a singleton");
                                            }
                                            break Label_0081;
                                        }
                                    }
                                    continue;
                                }
                                this.instance = access$000;
                                break;
                            }
                        }
                        // monitorexit(InternalInjectorCreator.class)
                        Object instance = this.instance;
                        if (instance == Scopes.NULL) {
                            instance = null;
                        }
                        return (T)instance;
                    }
                    
                    @Override
                    public String toString() {
                        return String.format("%s[%s]", provider, Scopes.SINGLETON);
                    }
                };
            }
            
            @Override
            public String toString() {
                return "Scopes.SINGLETON";
            }
        };
        NO_SCOPE = new Scope() {
            @Override
            public <T> Provider<T> scope(final Key<T> key, final Provider<T> provider) {
                return provider;
            }
            
            @Override
            public String toString() {
                return "Scopes.NO_SCOPE";
            }
        };
    }
}
