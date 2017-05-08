// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.base;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public final class Predicates
{
    private static final Joiner COMMA_JOINER;
    
    static {
        COMMA_JOINER = Joiner.on(",");
    }
    
    public static <T> Predicate<T> and(final Predicate<? super T> predicate, final Predicate<? super T> predicate2) {
        return new AndPredicate<T>((List)asList(Preconditions.checkNotNull(predicate), Preconditions.checkNotNull(predicate2)));
    }
    
    private static <T> List<Predicate<? super T>> asList(final Predicate<? super T> predicate, final Predicate<? super T> predicate2) {
        return Arrays.asList(predicate, predicate2);
    }
    
    public static <T> Predicate<T> equalTo(final T t) {
        if (t == null) {
            return isNull();
        }
        return new IsEqualToPredicate<T>((Object)t);
    }
    
    public static <T> Predicate<T> isNull() {
        return ObjectPredicate.IS_NULL.withNarrowedType();
    }
    
    public static <T> Predicate<T> not(final Predicate<T> predicate) {
        return new NotPredicate<T>(predicate);
    }
    
    private static class AndPredicate<T> implements Predicate<T>, Serializable
    {
        private final List<? extends Predicate<? super T>> components;
        
        private AndPredicate(final List<? extends Predicate<? super T>> components) {
            this.components = components;
        }
        
        @Override
        public boolean apply(final T t) {
            for (int i = 0; i < this.components.size(); ++i) {
                if (!((Predicate)this.components.get(i)).apply(t)) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof AndPredicate && this.components.equals(((AndPredicate)o).components);
        }
        
        @Override
        public int hashCode() {
            return this.components.hashCode() + 306654252;
        }
        
        @Override
        public String toString() {
            return "And(" + Predicates.COMMA_JOINER.join(this.components) + ")";
        }
    }
    
    private static class IsEqualToPredicate<T> implements Predicate<T>, Serializable
    {
        private final T target;
        
        private IsEqualToPredicate(final T target) {
            this.target = target;
        }
        
        @Override
        public boolean apply(final T t) {
            return this.target.equals(t);
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof IsEqualToPredicate && this.target.equals(((IsEqualToPredicate)o).target);
        }
        
        @Override
        public int hashCode() {
            return this.target.hashCode();
        }
        
        @Override
        public String toString() {
            return "IsEqualTo(" + this.target + ")";
        }
    }
    
    private static class NotPredicate<T> implements Predicate<T>, Serializable
    {
        final Predicate<T> predicate;
        
        NotPredicate(final Predicate<T> predicate) {
            this.predicate = Preconditions.checkNotNull(predicate);
        }
        
        @Override
        public boolean apply(final T t) {
            return !this.predicate.apply(t);
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof NotPredicate && this.predicate.equals(((NotPredicate)o).predicate);
        }
        
        @Override
        public int hashCode() {
            return ~this.predicate.hashCode();
        }
        
        @Override
        public String toString() {
            return "Not(" + this.predicate.toString() + ")";
        }
    }
    
    enum ObjectPredicate implements Predicate<Object>
    {
        ALWAYS_FALSE {
            @Override
            public boolean apply(final Object o) {
                return false;
            }
        }, 
        ALWAYS_TRUE {
            @Override
            public boolean apply(final Object o) {
                return true;
            }
        }, 
        IS_NULL {
            @Override
            public boolean apply(final Object o) {
                return o == null;
            }
        }, 
        NOT_NULL {
            @Override
            public boolean apply(final Object o) {
                return o != null;
            }
        };
        
         <T> Predicate<T> withNarrowedType() {
            return (Predicate<T>)this;
        }
    }
}
