// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.matcher;

import java.io.Serializable;

public abstract class AbstractMatcher<T> implements Matcher<T>
{
    private static class AndMatcher<T> extends AbstractMatcher<T> implements Serializable
    {
        private final Matcher<? super T> a;
        private final Matcher<? super T> b;
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof AndMatcher && ((AndMatcher)o).a.equals(this.a) && ((AndMatcher)o).b.equals(this.b);
        }
        
        @Override
        public int hashCode() {
            return (this.a.hashCode() ^ this.b.hashCode()) * 41;
        }
        
        @Override
        public boolean matches(final T t) {
            return this.a.matches((Object)t) && this.b.matches((Object)t);
        }
        
        @Override
        public String toString() {
            return "and(" + this.a + ", " + this.b + ")";
        }
    }
    
    private static class OrMatcher<T> extends AbstractMatcher<T> implements Serializable
    {
        private final Matcher<? super T> a;
        private final Matcher<? super T> b;
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof OrMatcher && ((OrMatcher)o).a.equals(this.a) && ((OrMatcher)o).b.equals(this.b);
        }
        
        @Override
        public int hashCode() {
            return (this.a.hashCode() ^ this.b.hashCode()) * 37;
        }
        
        @Override
        public boolean matches(final T t) {
            return this.a.matches((Object)t) || this.b.matches((Object)t);
        }
        
        @Override
        public String toString() {
            return "or(" + this.a + ", " + this.b + ")";
        }
    }
}
