// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.base;

import java.util.Map;
import java.util.Arrays;
import java.io.IOException;
import java.util.Iterator;

public class Joiner
{
    private final String separator;
    
    private Joiner(final Joiner joiner) {
        this.separator = joiner.separator;
    }
    
    private Joiner(final String s) {
        this.separator = Preconditions.checkNotNull(s);
    }
    
    public static Joiner on(final char c) {
        return new Joiner(String.valueOf(c));
    }
    
    public static Joiner on(final String s) {
        return new Joiner(s);
    }
    
    public <A extends Appendable> A appendTo(final A a, final Iterator<?> iterator) throws IOException {
        Preconditions.checkNotNull(a);
        if (iterator.hasNext()) {
            a.append(this.toString(iterator.next()));
            while (iterator.hasNext()) {
                a.append(this.separator);
                a.append(this.toString(iterator.next()));
            }
        }
        return a;
    }
    
    public final StringBuilder appendTo(final StringBuilder sb, final Iterable<?> iterable) {
        return this.appendTo(sb, iterable.iterator());
    }
    
    public final StringBuilder appendTo(final StringBuilder sb, final Iterator<?> iterator) {
        try {
            this.appendTo(sb, iterator);
            return sb;
        }
        catch (IOException ex) {
            throw new AssertionError((Object)ex);
        }
    }
    
    public final StringBuilder appendTo(final StringBuilder sb, final Object[] array) {
        return this.appendTo(sb, Arrays.asList(array));
    }
    
    public final String join(final Iterable<?> iterable) {
        return this.join(iterable.iterator());
    }
    
    public final String join(final Iterator<?> iterator) {
        return this.appendTo(new StringBuilder(), iterator).toString();
    }
    
    public final String join(final Object[] array) {
        return this.join(Arrays.asList(array));
    }
    
    CharSequence toString(final Object o) {
        Preconditions.checkNotNull(o);
        if (o instanceof CharSequence) {
            return (CharSequence)o;
        }
        return o.toString();
    }
    
    public Joiner useForNull(final String s) {
        Preconditions.checkNotNull(s);
        return new Joiner(this) {
            @Override
            CharSequence toString(final Object o) {
                if (o == null) {
                    return s;
                }
                return Joiner.this.toString(o);
            }
            
            @Override
            public Joiner useForNull(final String s) {
                Preconditions.checkNotNull(s);
                throw new UnsupportedOperationException("already specified useForNull");
            }
        };
    }
    
    public MapJoiner withKeyValueSeparator(final String s) {
        return new MapJoiner(this, s);
    }
    
    public static final class MapJoiner
    {
        private final Joiner joiner;
        private final String keyValueSeparator;
        
        private MapJoiner(final Joiner joiner, final String s) {
            this.joiner = joiner;
            this.keyValueSeparator = Preconditions.checkNotNull(s);
        }
        
        public <A extends Appendable> A appendTo(final A a, final Iterator<? extends Map.Entry<?, ?>> iterator) throws IOException {
            Preconditions.checkNotNull(a);
            if (iterator.hasNext()) {
                final Map.Entry entry = (Map.Entry)iterator.next();
                a.append(this.joiner.toString(entry.getKey()));
                a.append(this.keyValueSeparator);
                a.append(this.joiner.toString(entry.getValue()));
                while (iterator.hasNext()) {
                    a.append(this.joiner.separator);
                    final Map.Entry entry2 = (Map.Entry)iterator.next();
                    a.append(this.joiner.toString(entry2.getKey()));
                    a.append(this.keyValueSeparator);
                    a.append(this.joiner.toString(entry2.getValue()));
                }
            }
            return a;
        }
        
        public StringBuilder appendTo(final StringBuilder sb, final Iterable<? extends Map.Entry<?, ?>> iterable) {
            return this.appendTo(sb, iterable.iterator());
        }
        
        public StringBuilder appendTo(final StringBuilder sb, final Iterator<? extends Map.Entry<?, ?>> iterator) {
            try {
                this.appendTo(sb, iterator);
                return sb;
            }
            catch (IOException ex) {
                throw new AssertionError((Object)ex);
            }
        }
        
        public StringBuilder appendTo(final StringBuilder sb, final Map<?, ?> map) {
            return this.appendTo(sb, (Iterable<? extends Map.Entry<?, ?>>)map.entrySet());
        }
    }
}
