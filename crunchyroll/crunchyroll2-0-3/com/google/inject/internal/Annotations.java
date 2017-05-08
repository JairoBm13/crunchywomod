// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.internal.util.$MapMaker;
import com.google.inject.internal.util.$Function;
import java.util.Map;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import java.lang.reflect.Member;
import com.google.inject.internal.util.$Classes;
import com.google.inject.name.Names;
import java.io.Serializable;
import javax.inject.Named;
import javax.inject.Qualifier;
import com.google.inject.BindingAnnotation;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Arrays;
import javax.inject.Scope;
import com.google.inject.ScopeAnnotation;

public class Annotations
{
    private static final AnnotationChecker bindingAnnotationChecker;
    private static final AnnotationChecker scopeChecker;
    
    static {
        scopeChecker = new AnnotationChecker((Collection<Class<? extends Annotation>>)Arrays.asList(ScopeAnnotation.class, Scope.class));
        bindingAnnotationChecker = new AnnotationChecker((Collection<Class<? extends Annotation>>)Arrays.asList(BindingAnnotation.class, Qualifier.class));
    }
    
    public static Class<? extends Annotation> canonicalizeIfNamed(final Class<? extends Annotation> clazz) {
        Serializable s = clazz;
        if (clazz == Named.class) {
            s = com.google.inject.name.Named.class;
        }
        return (Class<? extends Annotation>)s;
    }
    
    public static Annotation canonicalizeIfNamed(final Annotation annotation) {
        Annotation named = annotation;
        if (annotation instanceof Named) {
            named = Names.named(((Named)annotation).value());
        }
        return named;
    }
    
    public static void checkForMisplacedScopeAnnotations(final Class<?> clazz, final Object o, final Errors errors) {
        if (!$Classes.isConcrete(clazz)) {
            final Class<? extends Annotation> scopeAnnotation = findScopeAnnotation(errors, clazz);
            if (scopeAnnotation != null) {
                errors.withSource(clazz).scopeAnnotationOnAbstractType(scopeAnnotation, clazz, o);
            }
        }
    }
    
    public static Annotation findBindingAnnotation(final Errors errors, final Member member, final Annotation[] array) {
        Annotation annotation = null;
        Annotation annotation3;
        for (int length = array.length, i = 0; i < length; ++i, annotation = annotation3) {
            final Annotation annotation2 = array[i];
            final Class<? extends Annotation> annotationType = annotation2.annotationType();
            annotation3 = annotation;
            if (isBindingAnnotation(annotationType)) {
                if (annotation != null) {
                    errors.duplicateBindingAnnotations(member, annotation.annotationType(), annotationType);
                    annotation3 = annotation;
                }
                else {
                    annotation3 = annotation2;
                }
            }
        }
        return annotation;
    }
    
    public static Class<? extends Annotation> findScopeAnnotation(final Errors errors, final Class<?> clazz) {
        return findScopeAnnotation(errors, clazz.getAnnotations());
    }
    
    public static Class<? extends Annotation> findScopeAnnotation(final Errors errors, final Annotation[] array) {
        Class<? extends Annotation> clazz = null;
        Class<? extends Annotation> clazz2;
        for (int length = array.length, i = 0; i < length; ++i, clazz = clazz2) {
            final Class<? extends Annotation> annotationType = array[i].annotationType();
            clazz2 = clazz;
            if (isScopeAnnotation(annotationType)) {
                if (clazz != null) {
                    errors.duplicateScopeAnnotations(clazz, annotationType);
                    clazz2 = clazz;
                }
                else {
                    clazz2 = annotationType;
                }
            }
        }
        return clazz;
    }
    
    public static Key<?> getKey(final TypeLiteral<?> typeLiteral, final Member member, final Annotation[] array, final Errors errors) throws ErrorsException {
        final int size = errors.size();
        final Annotation bindingAnnotation = findBindingAnnotation(errors, member, array);
        errors.throwIfNewErrors(size);
        if (bindingAnnotation == null) {
            return Key.get(typeLiteral);
        }
        return Key.get(typeLiteral, bindingAnnotation);
    }
    
    public static boolean isBindingAnnotation(final Class<? extends Annotation> clazz) {
        return Annotations.bindingAnnotationChecker.hasAnnotations(clazz);
    }
    
    public static boolean isMarker(final Class<? extends Annotation> clazz) {
        return clazz.getDeclaredMethods().length == 0;
    }
    
    public static boolean isRetainedAtRuntime(final Class<? extends Annotation> clazz) {
        final Retention retention = clazz.getAnnotation(Retention.class);
        return retention != null && retention.value() == RetentionPolicy.RUNTIME;
    }
    
    public static boolean isScopeAnnotation(final Class<? extends Annotation> clazz) {
        return Annotations.scopeChecker.hasAnnotations(clazz);
    }
    
    static class AnnotationChecker
    {
        private final Collection<Class<? extends Annotation>> annotationTypes;
        final Map<Class<? extends Annotation>, Boolean> cache;
        private $Function<Class<? extends Annotation>, Boolean> hasAnnotations;
        
        AnnotationChecker(final Collection<Class<? extends Annotation>> annotationTypes) {
            this.hasAnnotations = new $Function<Class<? extends Annotation>, Boolean>() {
                @Override
                public Boolean apply(final Class<? extends Annotation> clazz) {
                    final Annotation[] annotations = clazz.getAnnotations();
                    for (int length = annotations.length, i = 0; i < length; ++i) {
                        if (AnnotationChecker.this.annotationTypes.contains(annotations[i].annotationType())) {
                            return true;
                        }
                    }
                    return false;
                }
            };
            this.cache = (Map<Class<? extends Annotation>, Boolean>)new $MapMaker().weakKeys().makeComputingMap(($Function<? super Object, ?>)this.hasAnnotations);
            this.annotationTypes = annotationTypes;
        }
        
        boolean hasAnnotations(final Class<? extends Annotation> clazz) {
            return this.cache.get(clazz);
        }
    }
}
