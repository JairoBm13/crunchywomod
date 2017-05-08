// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import com.google.inject.internal.util.$Objects;
import java.util.Iterator;
import java.util.ArrayList;
import com.google.inject.internal.util.$ImmutableSet;
import java.util.Collection;
import com.google.inject.internal.util.$Lists;
import java.util.Set;
import com.google.inject.internal.util.$Preconditions;
import com.google.inject.Key;

public final class Dependency<T>
{
    private final InjectionPoint injectionPoint;
    private final Key<T> key;
    private final boolean nullable;
    private final int parameterIndex;
    
    Dependency(final InjectionPoint injectionPoint, final Key<T> key, final boolean nullable, final int parameterIndex) {
        this.injectionPoint = injectionPoint;
        this.key = $Preconditions.checkNotNull(key, "key");
        this.nullable = nullable;
        this.parameterIndex = parameterIndex;
    }
    
    public static Set<Dependency<?>> forInjectionPoints(final Set<InjectionPoint> set) {
        final ArrayList<Object> arrayList = $Lists.newArrayList();
        final Iterator<InjectionPoint> iterator = set.iterator();
        while (iterator.hasNext()) {
            arrayList.addAll(iterator.next().getDependencies());
        }
        return (Set<Dependency<?>>)$ImmutableSet.copyOf((Iterable<?>)arrayList);
    }
    
    public static <T> Dependency<T> get(final Key<T> key) {
        return new Dependency<T>(null, key, true, -1);
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b2;
        final boolean b = b2 = false;
        if (o instanceof Dependency) {
            final Dependency dependency = (Dependency)o;
            b2 = b;
            if ($Objects.equal(this.injectionPoint, dependency.injectionPoint)) {
                b2 = b;
                if ($Objects.equal(this.parameterIndex, dependency.parameterIndex)) {
                    b2 = b;
                    if ($Objects.equal(this.key, dependency.key)) {
                        b2 = true;
                    }
                }
            }
        }
        return b2;
    }
    
    public InjectionPoint getInjectionPoint() {
        return this.injectionPoint;
    }
    
    public Key<T> getKey() {
        return this.key;
    }
    
    public int getParameterIndex() {
        return this.parameterIndex;
    }
    
    @Override
    public int hashCode() {
        return $Objects.hashCode(this.injectionPoint, this.parameterIndex, this.key);
    }
    
    public boolean isNullable() {
        return this.nullable;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.key);
        if (this.injectionPoint != null) {
            sb.append("@").append(this.injectionPoint);
            if (this.parameterIndex != -1) {
                sb.append("[").append(this.parameterIndex).append("]");
            }
        }
        return sb.toString();
    }
}
