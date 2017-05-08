// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import java.util.ArrayList;
import java.lang.reflect.Field;
import com.google.inject.internal.util.$Lists;
import java.util.Iterator;
import com.google.inject.ConfigurationException;
import java.util.Set;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.InjectionPoint;
import java.util.List;
import com.google.inject.spi.TypeListenerBinding;
import com.google.inject.internal.util.$ImmutableList;
import com.google.inject.TypeLiteral;

final class MembersInjectorStore
{
    private final FailableCache<TypeLiteral<?>, MembersInjectorImpl<?>> cache;
    private final InjectorImpl injector;
    private final $ImmutableList<TypeListenerBinding> typeListenerBindings;
    
    MembersInjectorStore(final InjectorImpl injector, final List<TypeListenerBinding> list) {
        this.cache = new FailableCache<TypeLiteral<?>, MembersInjectorImpl<?>>() {
            @Override
            protected MembersInjectorImpl<?> create(final TypeLiteral<?> typeLiteral, final Errors errors) throws ErrorsException {
                return MembersInjectorStore.this.createWithListeners(typeLiteral, errors);
            }
        };
        this.injector = injector;
        this.typeListenerBindings = $ImmutableList.copyOf((Iterable<? extends TypeListenerBinding>)list);
    }
    
    private <T> MembersInjectorImpl<T> createWithListeners(final TypeLiteral<T> typeLiteral, final Errors errors) throws ErrorsException {
        final int size = errors.size();
        $ImmutableList<SingleMemberInjector> injectors;
        EncounterImpl<T> encounterImpl;
        while (true) {
            try {
                final Set<InjectionPoint> forInstanceMethodsAndFields = InjectionPoint.forInstanceMethodsAndFields(typeLiteral);
                injectors = this.getInjectors(forInstanceMethodsAndFields, errors);
                errors.throwIfNewErrors(size);
                encounterImpl = new EncounterImpl<T>(errors, this.injector.lookups);
                for (final TypeListenerBinding typeListenerBinding : this.typeListenerBindings) {
                    if (typeListenerBinding.getTypeMatcher().matches(typeLiteral)) {
                        try {
                            typeListenerBinding.getListener().hear(typeLiteral, encounterImpl);
                        }
                        catch (RuntimeException ex) {
                            errors.errorNotifyingTypeListener(typeListenerBinding, typeLiteral, ex);
                        }
                    }
                }
            }
            catch (ConfigurationException ex2) {
                errors.merge(ex2.getErrorMessages());
                final Set<InjectionPoint> forInstanceMethodsAndFields = ex2.getPartialValue();
                continue;
            }
            break;
        }
        encounterImpl.invalidate();
        errors.throwIfNewErrors(size);
        return new MembersInjectorImpl<T>(this.injector, (TypeLiteral<Object>)typeLiteral, (EncounterImpl<Object>)encounterImpl, injectors);
    }
    
    public <T> MembersInjectorImpl<T> get(final TypeLiteral<T> typeLiteral, final Errors errors) throws ErrorsException {
        return (MembersInjectorImpl<T>)this.cache.get(typeLiteral, errors);
    }
    
    $ImmutableList<SingleMemberInjector> getInjectors(final Set<InjectionPoint> set, final Errors errors) {
        final ArrayList<Object> arrayList = $Lists.newArrayList();
        final Iterator<InjectionPoint> iterator = set.iterator();
        while (true) {
            Label_0117: {
                if (!iterator.hasNext()) {
                    break Label_0117;
                }
                final InjectionPoint injectionPoint = iterator.next();
                try {
                    Errors withSource;
                    if (injectionPoint.isOptional()) {
                        withSource = new Errors(injectionPoint);
                    }
                    else {
                        withSource = errors.withSource(injectionPoint);
                    }
                    SingleMemberInjector singleMemberInjector;
                    if (injectionPoint.getMember() instanceof Field) {
                        singleMemberInjector = new SingleFieldInjector(this.injector, injectionPoint, withSource);
                    }
                    else {
                        singleMemberInjector = new SingleMethodInjector(this.injector, injectionPoint, withSource);
                    }
                    arrayList.add(singleMemberInjector);
                    continue;
                    return $ImmutableList.copyOf((Iterable<? extends SingleMemberInjector>)arrayList);
                }
                catch (ErrorsException ex) {}
            }
        }
    }
    
    public boolean hasTypeListeners() {
        return !this.typeListenerBindings.isEmpty();
    }
    
    boolean remove(final TypeLiteral<?> typeLiteral) {
        return this.cache.remove(typeLiteral);
    }
}
