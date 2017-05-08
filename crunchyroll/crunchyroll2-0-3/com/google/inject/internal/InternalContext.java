// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.internal.util.$Maps;
import com.google.inject.spi.Dependency;
import java.util.Map;

final class InternalContext
{
    private Map<Object, ConstructionContext<?>> constructionContexts;
    private Dependency dependency;
    
    InternalContext() {
        this.constructionContexts = (Map<Object, ConstructionContext<?>>)$Maps.newHashMap();
    }
    
    public <T> ConstructionContext<T> getConstructionContext(final Object o) {
        ConstructionContext<?> constructionContext;
        if ((constructionContext = this.constructionContexts.get(o)) == null) {
            constructionContext = new ConstructionContext<T>();
            this.constructionContexts.put(o, constructionContext);
        }
        return (ConstructionContext<T>)constructionContext;
    }
    
    public Dependency getDependency() {
        return this.dependency;
    }
    
    public Dependency setDependency(final Dependency dependency) {
        final Dependency dependency2 = this.dependency;
        this.dependency = dependency;
        return dependency2;
    }
}
