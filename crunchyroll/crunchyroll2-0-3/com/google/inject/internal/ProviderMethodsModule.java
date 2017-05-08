// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.Provides;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import com.google.inject.internal.util.$ImmutableSet;
import com.google.inject.spi.Message;
import com.google.inject.spi.Dependency;
import com.google.inject.Provider;
import com.google.inject.Key;
import java.util.logging.Logger;
import java.lang.reflect.Member;
import com.google.inject.internal.util.$Lists;
import java.lang.reflect.Method;
import java.util.Iterator;
import com.google.inject.Binder;
import com.google.inject.util.Modules;
import com.google.inject.internal.util.$Preconditions;
import com.google.inject.TypeLiteral;
import com.google.inject.Module;

public final class ProviderMethodsModule implements Module
{
    private final Object delegate;
    private final TypeLiteral<?> typeLiteral;
    
    private ProviderMethodsModule(final Object o) {
        this.delegate = $Preconditions.checkNotNull(o, "delegate");
        this.typeLiteral = TypeLiteral.get(this.delegate.getClass());
    }
    
    public static Module forModule(final Module module) {
        return forObject(module);
    }
    
    public static Module forObject(final Object o) {
        if (o instanceof ProviderMethodsModule) {
            return Modules.EMPTY_MODULE;
        }
        return new ProviderMethodsModule(o);
    }
    
    @Override
    public void configure(final Binder binder) {
        synchronized (this) {
            final Iterator<ProviderMethod<?>> iterator = this.getProviderMethods(binder).iterator();
            while (iterator.hasNext()) {
                iterator.next().configure(binder);
            }
        }
    }
    // monitorexit(this)
    
     <T> ProviderMethod<T> createProviderMethod(final Binder binder, final Method method) {
        final Binder withSource = binder.withSource(method);
        final Errors errors = new Errors(method);
        final ArrayList<Object> arrayList = $Lists.newArrayList();
        final ArrayList<Object> arrayList2 = $Lists.newArrayList();
        final List<TypeLiteral<?>> parameterTypes = this.typeLiteral.getParameterTypes(method);
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterTypes.size(); ++i) {
            Object o;
            if (((Key)(o = this.getKey(errors, (TypeLiteral<T>)parameterTypes.get(i), method, parameterAnnotations[i]))).equals(Key.get(Logger.class))) {
                o = Key.get(Logger.class, UniqueAnnotations.create());
                withSource.bind((Key<T>)o).toProvider((Provider<? extends T>)new LogProvider(method));
            }
            arrayList.add(Dependency.get((Key<T>)o));
            arrayList2.add(withSource.getProvider((Key<T>)o));
        }
        final Key<?> key = this.getKey(errors, this.typeLiteral.getReturnType(method), method, method.getAnnotations());
        final Class<? extends Annotation> scopeAnnotation = Annotations.findScopeAnnotation(errors, method.getAnnotations());
        final Iterator<Message> iterator = errors.getMessages().iterator();
        while (iterator.hasNext()) {
            withSource.addError(iterator.next());
        }
        return new ProviderMethod<T>((Key<Object>)key, method, this.delegate, $ImmutableSet.copyOf((Iterable<? extends Dependency<?>>)arrayList), (List<Provider<?>>)arrayList2, scopeAnnotation);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof ProviderMethodsModule && ((ProviderMethodsModule)o).delegate == this.delegate;
    }
    
     <T> Key<T> getKey(final Errors errors, final TypeLiteral<T> typeLiteral, final Member member, final Annotation[] array) {
        final Annotation bindingAnnotation = Annotations.findBindingAnnotation(errors, member, array);
        if (bindingAnnotation == null) {
            return Key.get(typeLiteral);
        }
        return Key.get(typeLiteral, bindingAnnotation);
    }
    
    public List<ProviderMethod<?>> getProviderMethods(final Binder binder) {
        final ArrayList<ProviderMethod<?>> arrayList = $Lists.newArrayList();
        for (Class<?> clazz = this.delegate.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            final Method[] declaredMethods = clazz.getDeclaredMethods();
            for (int length = declaredMethods.length, i = 0; i < length; ++i) {
                final Method method = declaredMethods[i];
                if (method.isAnnotationPresent(Provides.class)) {
                    arrayList.add(this.createProviderMethod(binder, method));
                }
            }
        }
        return arrayList;
    }
    
    @Override
    public int hashCode() {
        return this.delegate.hashCode();
    }
    
    private static final class LogProvider implements Provider<Logger>
    {
        private final String name;
        
        public LogProvider(final Method method) {
            this.name = method.getDeclaringClass().getName() + "." + method.getName();
        }
        
        @Override
        public Logger get() {
            return Logger.getLogger(this.name);
        }
    }
}
