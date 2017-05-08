// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.matcher;

import java.lang.reflect.Method;
import com.google.inject.internal.util.$Preconditions;
import java.lang.annotation.Annotation;
import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;

public class Matchers
{
    private static final Matcher<Object> ANY;
    
    static {
        ANY = new Any();
    }
    
    public static Matcher<Object> any() {
        return Matchers.ANY;
    }
    
    public static Matcher<Object> identicalTo(final Object o) {
        return new IdenticalTo(o);
    }
    
    public static Matcher<Class> subclassesOf(final Class<?> clazz) {
        return new SubclassesOf(clazz);
    }
    
    private static class AnnotatedWith extends AbstractMatcher<AnnotatedElement> implements Serializable
    {
        private final Annotation annotation;
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof AnnotatedWith && ((AnnotatedWith)o).annotation.equals(this.annotation);
        }
        
        @Override
        public int hashCode() {
            return this.annotation.hashCode() * 37;
        }
        
        @Override
        public boolean matches(final AnnotatedElement annotatedElement) {
            final Annotation annotation = annotatedElement.getAnnotation(this.annotation.annotationType());
            return annotation != null && this.annotation.equals(annotation);
        }
        
        @Override
        public String toString() {
            return "annotatedWith(" + this.annotation + ")";
        }
    }
    
    private static class AnnotatedWithType extends AbstractMatcher<AnnotatedElement> implements Serializable
    {
        private final Class<? extends Annotation> annotationType;
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof AnnotatedWithType && ((AnnotatedWithType)o).annotationType.equals(this.annotationType);
        }
        
        @Override
        public int hashCode() {
            return this.annotationType.hashCode() * 37;
        }
        
        @Override
        public boolean matches(final AnnotatedElement annotatedElement) {
            return annotatedElement.getAnnotation(this.annotationType) != null;
        }
        
        @Override
        public String toString() {
            return "annotatedWith(" + this.annotationType.getSimpleName() + ".class)";
        }
    }
    
    private static class Any extends AbstractMatcher<Object> implements Serializable
    {
        @Override
        public boolean matches(final Object o) {
            return true;
        }
        
        @Override
        public String toString() {
            return "any()";
        }
    }
    
    private static class IdenticalTo extends AbstractMatcher<Object> implements Serializable
    {
        private final Object value;
        
        public IdenticalTo(final Object o) {
            this.value = $Preconditions.checkNotNull(o, "value");
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof IdenticalTo && ((IdenticalTo)o).value == this.value;
        }
        
        @Override
        public int hashCode() {
            return System.identityHashCode(this.value) * 37;
        }
        
        @Override
        public boolean matches(final Object o) {
            return this.value == o;
        }
        
        @Override
        public String toString() {
            return "identicalTo(" + this.value + ")";
        }
    }
    
    private static class InPackage extends AbstractMatcher<Class> implements Serializable
    {
        private final transient Package targetPackage;
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof InPackage && ((InPackage)o).targetPackage.equals(this.targetPackage);
        }
        
        @Override
        public int hashCode() {
            return this.targetPackage.hashCode() * 37;
        }
        
        @Override
        public boolean matches(final Class clazz) {
            return clazz.getPackage().equals(this.targetPackage);
        }
        
        @Override
        public String toString() {
            return "inPackage(" + this.targetPackage.getName() + ")";
        }
    }
    
    private static class InSubpackage extends AbstractMatcher<Class> implements Serializable
    {
        private final String targetPackageName;
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof InSubpackage && ((InSubpackage)o).targetPackageName.equals(this.targetPackageName);
        }
        
        @Override
        public int hashCode() {
            return this.targetPackageName.hashCode() * 37;
        }
        
        @Override
        public boolean matches(final Class clazz) {
            final String name = clazz.getPackage().getName();
            return name.equals(this.targetPackageName) || name.startsWith(this.targetPackageName + ".");
        }
        
        @Override
        public String toString() {
            return "inSubpackage(" + this.targetPackageName + ")";
        }
    }
    
    private static class Not<T> extends AbstractMatcher<T> implements Serializable
    {
        final Matcher<? super T> delegate;
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Not && ((Not)o).delegate.equals(this.delegate);
        }
        
        @Override
        public int hashCode() {
            return -this.delegate.hashCode();
        }
        
        @Override
        public boolean matches(final T t) {
            return !this.delegate.matches((Object)t);
        }
        
        @Override
        public String toString() {
            return "not(" + this.delegate + ")";
        }
    }
    
    private static class Only extends AbstractMatcher<Object> implements Serializable
    {
        private final Object value;
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Only && ((Only)o).value.equals(this.value);
        }
        
        @Override
        public int hashCode() {
            return this.value.hashCode() * 37;
        }
        
        @Override
        public boolean matches(final Object o) {
            return this.value.equals(o);
        }
        
        @Override
        public String toString() {
            return "only(" + this.value + ")";
        }
    }
    
    private static class Returns extends AbstractMatcher<Method> implements Serializable
    {
        private final Matcher<? super Class<?>> returnType;
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Returns && ((Returns)o).returnType.equals(this.returnType);
        }
        
        @Override
        public int hashCode() {
            return this.returnType.hashCode() * 37;
        }
        
        @Override
        public boolean matches(final Method method) {
            return this.returnType.matches(method.getReturnType());
        }
        
        @Override
        public String toString() {
            return "returns(" + this.returnType + ")";
        }
    }
    
    private static class SubclassesOf extends AbstractMatcher<Class> implements Serializable
    {
        private final Class<?> superclass;
        
        public SubclassesOf(final Class<?> clazz) {
            this.superclass = $Preconditions.checkNotNull(clazz, "superclass");
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof SubclassesOf && ((SubclassesOf)o).superclass.equals(this.superclass);
        }
        
        @Override
        public int hashCode() {
            return this.superclass.hashCode() * 37;
        }
        
        @Override
        public boolean matches(final Class clazz) {
            return this.superclass.isAssignableFrom(clazz);
        }
        
        @Override
        public String toString() {
            return "subclassesOf(" + this.superclass.getSimpleName() + ".class)";
        }
    }
}
