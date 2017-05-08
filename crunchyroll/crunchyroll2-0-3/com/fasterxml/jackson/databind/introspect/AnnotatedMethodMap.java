// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.introspect;

import java.util.Collections;
import java.util.Iterator;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;

public final class AnnotatedMethodMap implements Iterable<AnnotatedMethod>
{
    protected LinkedHashMap<MemberKey, AnnotatedMethod> _methods;
    
    public void add(final AnnotatedMethod annotatedMethod) {
        if (this._methods == null) {
            this._methods = new LinkedHashMap<MemberKey, AnnotatedMethod>();
        }
        this._methods.put(new MemberKey(annotatedMethod.getAnnotated()), annotatedMethod);
    }
    
    public AnnotatedMethod find(final String s, final Class<?>[] array) {
        if (this._methods == null) {
            return null;
        }
        return this._methods.get(new MemberKey(s, array));
    }
    
    public AnnotatedMethod find(final Method method) {
        if (this._methods == null) {
            return null;
        }
        return this._methods.get(new MemberKey(method));
    }
    
    public boolean isEmpty() {
        return this._methods == null || this._methods.size() == 0;
    }
    
    @Override
    public Iterator<AnnotatedMethod> iterator() {
        if (this._methods != null) {
            return this._methods.values().iterator();
        }
        return Collections.emptyList().iterator();
    }
    
    public AnnotatedMethod remove(final Method method) {
        if (this._methods != null) {
            return this._methods.remove(new MemberKey(method));
        }
        return null;
    }
}
