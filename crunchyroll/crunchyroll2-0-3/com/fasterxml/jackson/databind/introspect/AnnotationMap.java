// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.introspect;

import java.util.Iterator;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import com.fasterxml.jackson.databind.util.Annotations;

public final class AnnotationMap implements Annotations
{
    protected HashMap<Class<? extends Annotation>, Annotation> _annotations;
    
    public AnnotationMap() {
    }
    
    private AnnotationMap(final HashMap<Class<? extends Annotation>, Annotation> annotations) {
        this._annotations = annotations;
    }
    
    public static AnnotationMap merge(final AnnotationMap annotationMap, final AnnotationMap annotationMap2) {
        AnnotationMap annotationMap3;
        if (annotationMap == null || annotationMap._annotations == null || annotationMap._annotations.isEmpty()) {
            annotationMap3 = annotationMap2;
        }
        else {
            annotationMap3 = annotationMap;
            if (annotationMap2 != null) {
                annotationMap3 = annotationMap;
                if (annotationMap2._annotations != null) {
                    annotationMap3 = annotationMap;
                    if (!annotationMap2._annotations.isEmpty()) {
                        final HashMap<Class<? extends Annotation>, Annotation> hashMap = new HashMap<Class<? extends Annotation>, Annotation>();
                        for (final Annotation annotation : annotationMap2._annotations.values()) {
                            hashMap.put(annotation.annotationType(), annotation);
                        }
                        for (final Annotation annotation2 : annotationMap._annotations.values()) {
                            hashMap.put(annotation2.annotationType(), annotation2);
                        }
                        return new AnnotationMap(hashMap);
                    }
                }
            }
        }
        return annotationMap3;
    }
    
    protected final void _add(final Annotation annotation) {
        if (this._annotations == null) {
            this._annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        }
        this._annotations.put(annotation.annotationType(), annotation);
    }
    
    public void add(final Annotation annotation) {
        this._add(annotation);
    }
    
    public void addIfNotPresent(final Annotation annotation) {
        if (this._annotations == null || !this._annotations.containsKey(annotation.annotationType())) {
            this._add(annotation);
        }
    }
    
    public <A extends Annotation> A get(final Class<A> clazz) {
        if (this._annotations == null) {
            return null;
        }
        return (A)this._annotations.get(clazz);
    }
    
    public int size() {
        if (this._annotations == null) {
            return 0;
        }
        return this._annotations.size();
    }
    
    @Override
    public String toString() {
        if (this._annotations == null) {
            return "[null]";
        }
        return this._annotations.toString();
    }
}
