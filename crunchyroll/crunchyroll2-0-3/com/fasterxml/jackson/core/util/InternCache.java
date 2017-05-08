// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.util;

import java.util.Map;
import java.util.LinkedHashMap;

public final class InternCache extends LinkedHashMap<String, String>
{
    public static final InternCache instance;
    
    static {
        instance = new InternCache();
    }
    
    private InternCache() {
        super(100, 0.8f, true);
    }
    
    public String intern(final String s) {
        synchronized (this) {
            String intern;
            if ((intern = ((LinkedHashMap<K, String>)this).get(s)) == null) {
                intern = s.intern();
                this.put(intern, intern);
            }
            return intern;
        }
    }
    
    @Override
    protected boolean removeEldestEntry(final Map.Entry<String, String> entry) {
        return this.size() > 100;
    }
}
