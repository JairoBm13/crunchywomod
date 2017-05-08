// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import java.io.Serializable;
import com.google.inject.internal.util.$ObjectArrays;
import java.util.NoSuchElementException;
import java.util.ListIterator;
import com.google.inject.internal.util.$Preconditions;
import com.google.inject.internal.util.$Nullable;
import com.google.inject.internal.util.$UnmodifiableIterator;
import java.util.ArrayList;
import com.google.inject.internal.util.$Lists;
import java.util.Collection;
import java.util.RandomAccess;
import java.util.List;
import com.google.inject.internal.util.$ImmutableCollection;
import java.util.Iterator;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.internal.util.$ImmutableSet;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.internal.util.$ImmutableList;
import com.google.inject.MembersInjector;

final class MembersInjectorImpl<T> implements MembersInjector<T>
{
    private final $ImmutableList<InjectionListener<? super T>> injectionListeners;
    private final InjectorImpl injector;
    private final $ImmutableList<SingleMemberInjector> memberInjectors;
    private final TypeLiteral<T> typeLiteral;
    private final $ImmutableList<MembersInjector<? super T>> userMembersInjectors;
    
    MembersInjectorImpl(final InjectorImpl injector, final TypeLiteral<T> typeLiteral, final EncounterImpl<T> encounterImpl, final $ImmutableList<SingleMemberInjector> memberInjectors) {
        this.injector = injector;
        this.typeLiteral = typeLiteral;
        this.memberInjectors = memberInjectors;
        this.userMembersInjectors = encounterImpl.getMembersInjectors();
        this.injectionListeners = encounterImpl.getInjectionListeners();
    }
    
    public $ImmutableSet<InjectionPoint> getInjectionPoints() {
        final $ImmutableSet.Builder<InjectionPoint> builder = $ImmutableSet.builder();
        final Iterator iterator = this.memberInjectors.iterator();
        while (iterator.hasNext()) {
            builder.add(iterator.next().getInjectionPoint());
        }
        return builder.build();
    }
    
    void injectAndNotify(final T t, final Errors errors, final boolean b) throws ErrorsException {
        if (t != null) {
            this.injector.callInContext((ContextualCallable<Object>)new ContextualCallable<Void>() {
                @Override
                public Void call(final InternalContext internalContext) throws ErrorsException {
                    MembersInjectorImpl.this.injectMembers(t, errors, internalContext, b);
                    return null;
                }
            });
            if (!b) {
                this.notifyListeners(t, errors);
            }
        }
    }
    
    @Override
    public void injectMembers(final T t) {
        final Errors errors = new Errors(this.typeLiteral);
        while (true) {
            try {
                this.injectAndNotify(t, errors, false);
                errors.throwProvisionExceptionIfErrorsExist();
            }
            catch (ErrorsException ex) {
                errors.merge(ex.getErrors());
                continue;
            }
            break;
        }
    }
    
    void injectMembers(final T t, final Errors errors, InternalContext internalContext, final boolean b) {
        for (int i = 0; i < this.memberInjectors.size(); ++i) {
            final SingleMemberInjector singleMemberInjector = this.memberInjectors.get(i);
            if (!b || singleMemberInjector.getInjectionPoint().isToolable()) {
                singleMemberInjector.inject(errors, internalContext, t);
            }
        }
        if (!b) {
            int j = 0;
        Label_0114_Outer:
            while (j < this.userMembersInjectors.size()) {
                internalContext = (InternalContext)this.userMembersInjectors.get(j);
                while (true) {
                    try {
                        ((MembersInjector<T>)internalContext).injectMembers(t);
                        ++j;
                        continue Label_0114_Outer;
                    }
                    catch (RuntimeException ex) {
                        errors.errorInUserInjector((MembersInjector<?>)internalContext, this.typeLiteral, ex);
                        continue;
                    }
                    break;
                }
                break;
            }
        }
    }
    
    void notifyListeners(final T t, final Errors errors) throws ErrorsException {
        final int size = errors.size();
        for (final InjectionListener<T> injectionListener : this.injectionListeners) {
            try {
                injectionListener.afterInjection(t);
            }
            catch (RuntimeException ex) {
                errors.errorNotifyingInjectionListener(injectionListener, this.typeLiteral, ex);
            }
        }
        errors.throwIfNewErrors(size);
    }
    
    @Override
    public String toString() {
        return "MembersInjector<" + this.typeLiteral + ">";
    }
}
