// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.internal.util.$Lists;
import com.google.inject.internal.util.$Preconditions;
import com.google.inject.Provider;
import com.google.inject.Key;
import com.google.inject.internal.util.$ImmutableList;
import com.google.inject.MembersInjector;
import com.google.inject.spi.InjectionListener;
import java.util.List;
import com.google.inject.spi.TypeEncounter;

final class EncounterImpl<T> implements TypeEncounter<T>
{
    private final Errors errors;
    private List<InjectionListener<? super T>> injectionListeners;
    private final Lookups lookups;
    private List<MembersInjector<? super T>> membersInjectors;
    private boolean valid;
    
    EncounterImpl(final Errors errors, final Lookups lookups) {
        this.valid = true;
        this.errors = errors;
        this.lookups = lookups;
    }
    
    $ImmutableList<InjectionListener<? super T>> getInjectionListeners() {
        if (this.injectionListeners == null) {
            return $ImmutableList.of();
        }
        return $ImmutableList.copyOf((Iterable<? extends InjectionListener<? super T>>)this.injectionListeners);
    }
    
    $ImmutableList<MembersInjector<? super T>> getMembersInjectors() {
        if (this.membersInjectors == null) {
            return $ImmutableList.of();
        }
        return $ImmutableList.copyOf((Iterable<? extends MembersInjector<? super T>>)this.membersInjectors);
    }
    
    public <T> Provider<T> getProvider(final Key<T> key) {
        $Preconditions.checkState(this.valid, (Object)"Encounters may not be used after hear() returns.");
        return this.lookups.getProvider(key);
    }
    
    @Override
    public <T> Provider<T> getProvider(final Class<T> clazz) {
        return this.getProvider((Key<T>)Key.get((Class<T>)clazz));
    }
    
    void invalidate() {
        this.valid = false;
    }
    
    @Override
    public void register(final MembersInjector<? super T> membersInjector) {
        $Preconditions.checkState(this.valid, (Object)"Encounters may not be used after hear() returns.");
        if (this.membersInjectors == null) {
            this.membersInjectors = (List<MembersInjector<? super T>>)$Lists.newArrayList();
        }
        this.membersInjectors.add(membersInjector);
    }
    
    @Override
    public void register(final InjectionListener<? super T> injectionListener) {
        $Preconditions.checkState(this.valid, (Object)"Encounters may not be used after hear() returns.");
        if (this.injectionListeners == null) {
            this.injectionListeners = (List<InjectionListener<? super T>>)$Lists.newArrayList();
        }
        this.injectionListeners.add(injectionListener);
    }
}
