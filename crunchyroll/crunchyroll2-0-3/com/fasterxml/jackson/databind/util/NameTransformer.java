// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.util;

public abstract class NameTransformer
{
    public static final NameTransformer NOP;
    
    static {
        NOP = new NameTransformer() {
            @Override
            public String transform(final String s) {
                return s;
            }
        };
    }
    
    public static NameTransformer chainedTransformer(final NameTransformer nameTransformer, final NameTransformer nameTransformer2) {
        return new Chained(nameTransformer, nameTransformer2);
    }
    
    public static NameTransformer simpleTransformer(final String s, final String s2) {
        boolean b = true;
        int n;
        if (s != null && s.length() > 0) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (s2 == null || s2.length() <= 0) {
            b = false;
        }
        if (n != 0) {
            if (b) {
                return new NameTransformer() {
                    @Override
                    public String toString() {
                        return "[PreAndSuffixTransformer('" + s + "','" + s2 + "')]";
                    }
                    
                    @Override
                    public String transform(final String s) {
                        return s + s + s2;
                    }
                };
            }
            return new NameTransformer() {
                @Override
                public String toString() {
                    return "[PrefixTransformer('" + s + "')]";
                }
                
                @Override
                public String transform(final String s) {
                    return s + s;
                }
            };
        }
        else {
            if (b) {
                return new NameTransformer() {
                    @Override
                    public String toString() {
                        return "[SuffixTransformer('" + s2 + "')]";
                    }
                    
                    @Override
                    public String transform(final String s) {
                        return s + s2;
                    }
                };
            }
            return NameTransformer.NOP;
        }
    }
    
    public abstract String transform(final String p0);
    
    public static class Chained extends NameTransformer
    {
        protected final NameTransformer _t1;
        protected final NameTransformer _t2;
        
        public Chained(final NameTransformer t1, final NameTransformer t2) {
            this._t1 = t1;
            this._t2 = t2;
        }
        
        @Override
        public String toString() {
            return "[ChainedTransformer(" + this._t1 + ", " + this._t2 + ")]";
        }
        
        @Override
        public String transform(final String s) {
            return this._t1.transform(this._t2.transform(s));
        }
    }
}
