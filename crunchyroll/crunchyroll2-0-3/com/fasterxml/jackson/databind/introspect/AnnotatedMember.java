// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.introspect;

import java.lang.reflect.Member;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.lang.annotation.Annotation;
import java.io.Serializable;

public abstract class AnnotatedMember extends Annotated implements Serializable
{
    protected final transient AnnotationMap _annotations;
    
    protected AnnotatedMember(final AnnotationMap annotations) {
        this._annotations = annotations;
    }
    
    public final void addIfNotPresent(final Annotation annotation) {
        this._annotations.addIfNotPresent(annotation);
    }
    
    public final void addOrOverride(final Annotation annotation) {
        this._annotations.add(annotation);
    }
    
    public final void fixAccess() {
        ClassUtil.checkAndFixAccess(this.getMember());
    }
    
    @Override
    protected AnnotationMap getAllAnnotations() {
        return this._annotations;
    }
    
    public abstract Class<?> getDeclaringClass();
    
    public abstract Member getMember();
    
    public abstract Object getValue(final Object p0) throws UnsupportedOperationException, IllegalArgumentException;
    
    public abstract void setValue(final Object p0, final Object p1) throws UnsupportedOperationException, IllegalArgumentException;
}
