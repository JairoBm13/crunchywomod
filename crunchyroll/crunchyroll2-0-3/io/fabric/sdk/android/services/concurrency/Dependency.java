// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.concurrency;

import java.util.Collection;

public interface Dependency<T>
{
    void addDependency(final T p0);
    
    boolean areDependenciesMet();
    
    Collection<T> getDependencies();
}
