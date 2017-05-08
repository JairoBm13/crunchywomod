// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Collection;
import java.util.Set;

public final class Sets
{
    static boolean equalsImpl(final Set<?> set, final Object o) {
        final boolean b = true;
        boolean b2 = false;
        if (set == o) {
            b2 = true;
        }
        else if (o instanceof Set) {
            final Set set2 = (Set)o;
            try {
                return set.size() == set2.size() && set.containsAll(set2) && b;
            }
            catch (NullPointerException ex) {
                return false;
            }
            catch (ClassCastException ex2) {
                return false;
            }
        }
        return b2;
    }
    
    static int hashCodeImpl(final Set<?> set) {
        int n = 0;
        for (final Object next : set) {
            int hashCode;
            if (next != null) {
                hashCode = next.hashCode();
            }
            else {
                hashCode = 0;
            }
            n += hashCode;
        }
        return n;
    }
    
    public static <E> HashSet<E> newHashSet() {
        return new HashSet<E>();
    }
}
