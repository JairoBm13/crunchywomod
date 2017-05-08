// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject;

import com.google.inject.internal.util.$Preconditions;
import com.google.inject.internal.Annotations;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import com.google.inject.internal.MoreTypes;

public class Key<T>
{
    private final AnnotationStrategy annotationStrategy;
    private final int hashCode;
    private final TypeLiteral<T> typeLiteral;
    
    protected Key() {
        this.annotationStrategy = (AnnotationStrategy)NullAnnotationStrategy.INSTANCE;
        this.typeLiteral = (TypeLiteral<T>)TypeLiteral.fromSuperclassTypeParameter(this.getClass());
        this.hashCode = this.computeHashCode();
    }
    
    private Key(final TypeLiteral<T> typeLiteral, final AnnotationStrategy annotationStrategy) {
        this.annotationStrategy = annotationStrategy;
        this.typeLiteral = MoreTypes.canonicalizeForKey(typeLiteral);
        this.hashCode = this.computeHashCode();
    }
    
    private Key(final Type type, final AnnotationStrategy annotationStrategy) {
        this.annotationStrategy = annotationStrategy;
        this.typeLiteral = MoreTypes.canonicalizeForKey(TypeLiteral.get(type));
        this.hashCode = this.computeHashCode();
    }
    
    private int computeHashCode() {
        return this.typeLiteral.hashCode() * 31 + this.annotationStrategy.hashCode();
    }
    
    private static void ensureIsBindingAnnotation(final Class<? extends Annotation> clazz) {
        $Preconditions.checkArgument(Annotations.isBindingAnnotation(clazz), "%s is not a binding annotation. Please annotate it with @BindingAnnotation.", clazz.getName());
    }
    
    private static void ensureRetainedAtRuntime(final Class<? extends Annotation> clazz) {
        $Preconditions.checkArgument(Annotations.isRetainedAtRuntime(clazz), "%s is not retained at runtime. Please annotate it with @Retention(RUNTIME).", clazz.getName());
    }
    
    public static <T> Key<T> get(final TypeLiteral<T> typeLiteral) {
        return new Key<T>(typeLiteral, (AnnotationStrategy)NullAnnotationStrategy.INSTANCE);
    }
    
    public static <T> Key<T> get(final TypeLiteral<T> typeLiteral, final Annotation annotation) {
        return new Key<T>(typeLiteral, strategyFor(annotation));
    }
    
    public static <T> Key<T> get(final Class<T> clazz) {
        return new Key<T>(clazz, (AnnotationStrategy)NullAnnotationStrategy.INSTANCE);
    }
    
    public static <T> Key<T> get(final Class<T> clazz, final Class<? extends Annotation> clazz2) {
        return new Key<T>(clazz, strategyFor(clazz2));
    }
    
    public static <T> Key<T> get(final Class<T> clazz, final Annotation annotation) {
        return new Key<T>(clazz, strategyFor(annotation));
    }
    
    public static Key<?> get(final Type type) {
        return new Key<Object>(type, (AnnotationStrategy)NullAnnotationStrategy.INSTANCE);
    }
    
    static AnnotationStrategy strategyFor(final Class<? extends Annotation> clazz) {
        $Preconditions.checkNotNull(clazz, "annotation type");
        ensureRetainedAtRuntime(clazz);
        ensureIsBindingAnnotation(clazz);
        return (AnnotationStrategy)new AnnotationTypeStrategy(Annotations.canonicalizeIfNamed(clazz), null);
    }
    
    static AnnotationStrategy strategyFor(final Annotation annotation) {
        $Preconditions.checkNotNull(annotation, "annotation");
        final Class<? extends Annotation> annotationType = annotation.annotationType();
        ensureRetainedAtRuntime(annotationType);
        ensureIsBindingAnnotation(annotationType);
        if (Annotations.isMarker(annotationType)) {
            return (AnnotationStrategy)new AnnotationTypeStrategy(annotationType, annotation);
        }
        return (AnnotationStrategy)new AnnotationInstanceStrategy(Annotations.canonicalizeIfNamed(annotation));
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o != this) {
            if (!(o instanceof Key)) {
                return false;
            }
            final Key key = (Key)o;
            if (!this.annotationStrategy.equals(key.annotationStrategy) || !this.typeLiteral.equals(key.typeLiteral)) {
                return false;
            }
        }
        return true;
    }
    
    public final Annotation getAnnotation() {
        return this.annotationStrategy.getAnnotation();
    }
    
    public final Class<? extends Annotation> getAnnotationType() {
        return this.annotationStrategy.getAnnotationType();
    }
    
    public final TypeLiteral<T> getTypeLiteral() {
        return this.typeLiteral;
    }
    
    public boolean hasAttributes() {
        return this.annotationStrategy.hasAttributes();
    }
    
    @Override
    public final int hashCode() {
        return this.hashCode;
    }
    
    public <T> Key<T> ofType(final TypeLiteral<T> typeLiteral) {
        return new Key<T>(typeLiteral, this.annotationStrategy);
    }
    
    public Key<?> ofType(final Type type) {
        return new Key<Object>(type, this.annotationStrategy);
    }
    
    @Override
    public final String toString() {
        return "Key[type=" + this.typeLiteral + ", annotation=" + this.annotationStrategy + "]";
    }
    
    public Key<T> withoutAttributes() {
        return new Key<T>(this.typeLiteral, this.annotationStrategy.withoutAttributes());
    }
    
    static class AnnotationInstanceStrategy implements AnnotationStrategy
    {
        final Annotation annotation;
        
        AnnotationInstanceStrategy(final Annotation annotation) {
            this.annotation = $Preconditions.checkNotNull(annotation, "annotation");
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof AnnotationInstanceStrategy && this.annotation.equals(((AnnotationInstanceStrategy)o).annotation);
        }
        
        @Override
        public Annotation getAnnotation() {
            return this.annotation;
        }
        
        @Override
        public Class<? extends Annotation> getAnnotationType() {
            return this.annotation.annotationType();
        }
        
        @Override
        public boolean hasAttributes() {
            return true;
        }
        
        @Override
        public int hashCode() {
            return this.annotation.hashCode();
        }
        
        @Override
        public String toString() {
            return this.annotation.toString();
        }
        
        @Override
        public AnnotationStrategy withoutAttributes() {
            return new AnnotationTypeStrategy(this.getAnnotationType(), this.annotation);
        }
    }
    
    interface AnnotationStrategy
    {
        Annotation getAnnotation();
        
        Class<? extends Annotation> getAnnotationType();
        
        boolean hasAttributes();
        
        AnnotationStrategy withoutAttributes();
    }
    
    static class AnnotationTypeStrategy implements AnnotationStrategy
    {
        final Annotation annotation;
        final Class<? extends Annotation> annotationType;
        
        AnnotationTypeStrategy(final Class<? extends Annotation> clazz, final Annotation annotation) {
            this.annotationType = $Preconditions.checkNotNull(clazz, "annotation type");
            this.annotation = annotation;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof AnnotationTypeStrategy && this.annotationType.equals(((AnnotationTypeStrategy)o).annotationType);
        }
        
        @Override
        public Annotation getAnnotation() {
            return this.annotation;
        }
        
        @Override
        public Class<? extends Annotation> getAnnotationType() {
            return this.annotationType;
        }
        
        @Override
        public boolean hasAttributes() {
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.annotationType.hashCode();
        }
        
        @Override
        public String toString() {
            return "@" + this.annotationType.getName();
        }
        
        @Override
        public AnnotationStrategy withoutAttributes() {
            throw new UnsupportedOperationException("Key already has no attributes.");
        }
    }
    
    enum NullAnnotationStrategy implements AnnotationStrategy
    {
        INSTANCE;
        
        @Override
        public Annotation getAnnotation() {
            return null;
        }
        
        @Override
        public Class<? extends Annotation> getAnnotationType() {
            return null;
        }
        
        @Override
        public boolean hasAttributes() {
            return false;
        }
        
        @Override
        public String toString() {
            return "[none]";
        }
        
        @Override
        public AnnotationStrategy withoutAttributes() {
            throw new UnsupportedOperationException("Key already has no attributes.");
        }
    }
}
