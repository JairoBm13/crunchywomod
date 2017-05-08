// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.base;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;

public final class Objects
{
    public static boolean equal(final Object o, final Object o2) {
        return o == o2 || (o != null && o.equals(o2));
    }
    
    public static <T> T firstNonNull(final T t, final T t2) {
        if (t != null) {
            return t;
        }
        return Preconditions.checkNotNull(t2);
    }
    
    public static int hashCode(final Object... array) {
        return Arrays.hashCode(array);
    }
    
    private static String simpleName(final Class<?> clazz) {
        final String replaceAll = clazz.getName().replaceAll("\\$[0-9]+", "\\$");
        int n;
        if ((n = replaceAll.lastIndexOf(36)) == -1) {
            n = replaceAll.lastIndexOf(46);
        }
        return replaceAll.substring(n + 1);
    }
    
    public static ToStringHelper toStringHelper(final Object o) {
        return new ToStringHelper(simpleName(o.getClass()));
    }
    
    public static final class ToStringHelper
    {
        private final String className;
        private boolean omitNullValues;
        private final List<ValueHolder> valueHolders;
        
        private ToStringHelper(final String s) {
            this.valueHolders = new LinkedList<ValueHolder>();
            this.omitNullValues = false;
            this.className = Preconditions.checkNotNull(s);
        }
        
        private ValueHolder addHolder() {
            final ValueHolder valueHolder = new ValueHolder();
            this.valueHolders.add(valueHolder);
            return valueHolder;
        }
        
        private ValueHolder addHolder(final Object o) {
            final ValueHolder addHolder = this.addHolder();
            addHolder.isNull = (o == null);
            return addHolder;
        }
        
        private StringBuilder checkNameAndAppend(final String s) {
            Preconditions.checkNotNull(s);
            return this.addHolder().builder.append(s).append('=');
        }
        
        public ToStringHelper add(final String s, final int n) {
            this.checkNameAndAppend(s).append(n);
            return this;
        }
        
        public ToStringHelper add(final String s, final long n) {
            this.checkNameAndAppend(s).append(n);
            return this;
        }
        
        public ToStringHelper add(final String s, final Object o) {
            Preconditions.checkNotNull(s);
            this.addHolder(o).builder.append(s).append('=').append(o);
            return this;
        }
        
        public ToStringHelper addValue(final Object o) {
            this.addHolder(o).builder.append(o);
            return this;
        }
        
        @Override
        public String toString() {
            final boolean omitNullValues = this.omitNullValues;
            int n = 0;
            final StringBuilder append = new StringBuilder(32).append(this.className).append('{');
            for (final ValueHolder valueHolder : this.valueHolders) {
                if (!omitNullValues || !valueHolder.isNull) {
                    if (n != 0) {
                        append.append(", ");
                    }
                    else {
                        n = 1;
                    }
                    append.append((CharSequence)valueHolder.builder);
                }
            }
            return append.append('}').toString();
        }
        
        private static final class ValueHolder
        {
            final StringBuilder builder;
            boolean isNull;
            
            private ValueHolder() {
                this.builder = new StringBuilder();
            }
        }
    }
}
