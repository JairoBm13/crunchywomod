// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import java.util.Iterator;
import com.google.inject.internal.util.$Lists;
import java.util.List;

class ProcessedBindingData
{
    private final List<CreationListener> creationListeners;
    private final List<Runnable> uninitializedBindings;
    
    ProcessedBindingData() {
        this.creationListeners = (List<CreationListener>)$Lists.newArrayList();
        this.uninitializedBindings = (List<Runnable>)$Lists.newArrayList();
    }
    
    void addCreationListener(final CreationListener creationListener) {
        this.creationListeners.add(creationListener);
    }
    
    void addUninitializedBinding(final Runnable runnable) {
        this.uninitializedBindings.add(runnable);
    }
    
    void initializeBindings() {
        final Iterator<Runnable> iterator = this.uninitializedBindings.iterator();
        while (iterator.hasNext()) {
            iterator.next().run();
        }
    }
    
    void runCreationListeners(final Errors errors) {
        final Iterator<CreationListener> iterator = this.creationListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().notify(errors);
        }
    }
}
