// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.base;

import java.util.Iterator;

public final class Splitter
{
    private final int limit;
    private final boolean omitEmptyStrings;
    private final Strategy strategy;
    private final CharMatcher trimmer;
    
    private Splitter(final Strategy strategy) {
        this(strategy, false, CharMatcher.NONE, Integer.MAX_VALUE);
    }
    
    private Splitter(final Strategy strategy, final boolean omitEmptyStrings, final CharMatcher trimmer, final int limit) {
        this.strategy = strategy;
        this.omitEmptyStrings = omitEmptyStrings;
        this.trimmer = trimmer;
        this.limit = limit;
    }
    
    public static Splitter on(final String s) {
        Preconditions.checkArgument(s.length() != 0, (Object)"The separator may not be the empty string.");
        return new Splitter((Strategy)new Strategy() {
            public SplittingIterator iterator(final Splitter splitter, final CharSequence charSequence) {
                return new SplittingIterator(splitter, charSequence) {
                    public int separatorEnd(final int n) {
                        return s.length() + n;
                    }
                    
                    public int separatorStart(int i) {
                        final int length = s.length();
                        final int length2 = this.toSplit.length();
                    Label_0023:
                        while (i <= length2 - length) {
                            int n = 0;
                            int n2;
                            while (true) {
                                n2 = i;
                                if (n >= length) {
                                    break;
                                }
                                if (this.toSplit.charAt(n + i) != s.charAt(n)) {
                                    ++i;
                                    continue Label_0023;
                                }
                                ++n;
                            }
                            return n2;
                        }
                        return -1;
                    }
                };
            }
        });
    }
    
    private Iterator<String> spliterator(final CharSequence charSequence) {
        return this.strategy.iterator(this, charSequence);
    }
    
    public Iterable<String> split(final CharSequence charSequence) {
        Preconditions.checkNotNull(charSequence);
        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return Splitter.this.spliterator(charSequence);
            }
            
            @Override
            public String toString() {
                return Joiner.on(", ").appendTo(new StringBuilder().append('['), this).append(']').toString();
            }
        };
    }
    
    private abstract static class SplittingIterator extends AbstractIterator<String>
    {
        int limit;
        int offset;
        final boolean omitEmptyStrings;
        final CharSequence toSplit;
        final CharMatcher trimmer;
        
        protected SplittingIterator(final Splitter splitter, final CharSequence toSplit) {
            this.offset = 0;
            this.trimmer = splitter.trimmer;
            this.omitEmptyStrings = splitter.omitEmptyStrings;
            this.limit = splitter.limit;
            this.toSplit = toSplit;
        }
        
        @Override
        protected String computeNext() {
            int n = this.offset;
            while (this.offset != -1) {
                int i = n;
                final int separatorStart = this.separatorStart(this.offset);
                int length;
                if (separatorStart == -1) {
                    length = this.toSplit.length();
                    this.offset = -1;
                }
                else {
                    length = separatorStart;
                    this.offset = this.separatorEnd(separatorStart);
                }
                if (this.offset == n) {
                    ++this.offset;
                    if (this.offset < this.toSplit.length()) {
                        continue;
                    }
                    this.offset = -1;
                }
                else {
                    int n2;
                    while (i < (n2 = length)) {
                        n2 = length;
                        if (!this.trimmer.matches(this.toSplit.charAt(i))) {
                            break;
                        }
                        ++i;
                    }
                    while (n2 > i && this.trimmer.matches(this.toSplit.charAt(n2 - 1))) {
                        --n2;
                    }
                    if (!this.omitEmptyStrings || i != n2) {
                        int n3;
                        if (this.limit == 1) {
                            int length2 = this.toSplit.length();
                            this.offset = -1;
                            while (true) {
                                n3 = length2;
                                if (length2 <= i) {
                                    break;
                                }
                                n3 = length2;
                                if (!this.trimmer.matches(this.toSplit.charAt(length2 - 1))) {
                                    break;
                                }
                                --length2;
                            }
                        }
                        else {
                            --this.limit;
                            n3 = n2;
                        }
                        return this.toSplit.subSequence(i, n3).toString();
                    }
                    n = this.offset;
                }
            }
            return this.endOfData();
        }
        
        abstract int separatorEnd(final int p0);
        
        abstract int separatorStart(final int p0);
    }
    
    private interface Strategy
    {
        Iterator<String> iterator(final Splitter p0, final CharSequence p1);
    }
}
