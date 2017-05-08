// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.databind.PropertyName;
import java.io.Serializable;
import com.fasterxml.jackson.databind.AnnotationIntrospector;

public class POJOPropertyBuilder extends BeanPropertyDefinition implements Comparable<POJOPropertyBuilder>
{
    protected final AnnotationIntrospector _annotationIntrospector;
    protected Linked<AnnotatedParameter> _ctorParameters;
    protected Linked<AnnotatedField> _fields;
    protected final boolean _forSerialization;
    protected Linked<AnnotatedMethod> _getters;
    protected final String _internalName;
    protected final String _name;
    protected Linked<AnnotatedMethod> _setters;
    
    public POJOPropertyBuilder(final POJOPropertyBuilder pojoPropertyBuilder, final String name) {
        this._internalName = pojoPropertyBuilder._internalName;
        this._name = name;
        this._annotationIntrospector = pojoPropertyBuilder._annotationIntrospector;
        this._fields = pojoPropertyBuilder._fields;
        this._ctorParameters = pojoPropertyBuilder._ctorParameters;
        this._getters = pojoPropertyBuilder._getters;
        this._setters = pojoPropertyBuilder._setters;
        this._forSerialization = pojoPropertyBuilder._forSerialization;
    }
    
    public POJOPropertyBuilder(final String s, final AnnotationIntrospector annotationIntrospector, final boolean forSerialization) {
        this._internalName = s;
        this._name = s;
        this._annotationIntrospector = annotationIntrospector;
        this._forSerialization = forSerialization;
    }
    
    private <T> boolean _anyExplicitNames(Linked<T> next) {
        while (next != null) {
            if (next.explicitName != null && next.explicitName.length() > 0) {
                return true;
            }
            next = next.next;
        }
        return false;
    }
    
    private <T> boolean _anyIgnorals(Linked<T> next) {
        while (next != null) {
            if (next.isMarkedIgnored) {
                return true;
            }
            next = next.next;
        }
        return false;
    }
    
    private <T> boolean _anyVisible(Linked<T> next) {
        while (next != null) {
            if (next.isVisible) {
                return true;
            }
            next = next.next;
        }
        return false;
    }
    
    private AnnotationMap _mergeAnnotations(int i, final Linked<? extends AnnotatedMember>... array) {
        final AnnotationMap allAnnotations = ((AnnotatedMember)array[i].value).getAllAnnotations();
        for (++i; i < array.length; ++i) {
            if (array[i] != null) {
                return AnnotationMap.merge(allAnnotations, this._mergeAnnotations(i, array));
            }
        }
        return allAnnotations;
    }
    
    private <T> Linked<T> _removeIgnored(final Linked<T> linked) {
        if (linked == null) {
            return linked;
        }
        return linked.withoutIgnored();
    }
    
    private <T> Linked<T> _removeNonVisible(final Linked<T> linked) {
        if (linked == null) {
            return linked;
        }
        return linked.withoutNonVisible();
    }
    
    private <T> Linked<T> _trimByVisibility(final Linked<T> linked) {
        if (linked == null) {
            return linked;
        }
        return linked.trimByVisibility();
    }
    
    private Linked<? extends AnnotatedMember> findRenamed(Linked<? extends AnnotatedMember> next, Linked<? extends AnnotatedMember> linked) {
        while (next != null) {
            final String explicitName = next.explicitName;
            Linked<? extends AnnotatedMember> linked2;
            if (explicitName == null) {
                linked2 = linked;
            }
            else {
                linked2 = linked;
                if (!explicitName.equals(this._name)) {
                    if (linked == null) {
                        linked2 = next;
                    }
                    else {
                        linked2 = linked;
                        if (!explicitName.equals(linked.explicitName)) {
                            throw new IllegalStateException("Conflicting property name definitions: '" + linked.explicitName + "' (for " + linked.value + ") vs '" + next.explicitName + "' (for " + next.value + ")");
                        }
                    }
                }
            }
            next = next.next;
            linked = linked2;
        }
        return linked;
    }
    
    private static <T> Linked<T> merge(final Linked<T> linked, final Linked<T> linked2) {
        if (linked == null) {
            return linked2;
        }
        if (linked2 == null) {
            return linked;
        }
        return (Linked<T>)((Linked<Object>)linked).append((Linked<Object>)linked2);
    }
    
    public void addAll(final POJOPropertyBuilder pojoPropertyBuilder) {
        this._fields = merge(this._fields, pojoPropertyBuilder._fields);
        this._ctorParameters = merge(this._ctorParameters, pojoPropertyBuilder._ctorParameters);
        this._getters = merge(this._getters, pojoPropertyBuilder._getters);
        this._setters = merge(this._setters, pojoPropertyBuilder._setters);
    }
    
    public void addCtor(final AnnotatedParameter annotatedParameter, final String s, final boolean b, final boolean b2) {
        this._ctorParameters = new Linked<AnnotatedParameter>(annotatedParameter, this._ctorParameters, s, b, b2);
    }
    
    public void addField(final AnnotatedField annotatedField, final String s, final boolean b, final boolean b2) {
        this._fields = new Linked<AnnotatedField>(annotatedField, this._fields, s, b, b2);
    }
    
    public void addGetter(final AnnotatedMethod annotatedMethod, final String s, final boolean b, final boolean b2) {
        this._getters = new Linked<AnnotatedMethod>(annotatedMethod, this._getters, s, b, b2);
    }
    
    public void addSetter(final AnnotatedMethod annotatedMethod, final String s, final boolean b, final boolean b2) {
        this._setters = new Linked<AnnotatedMethod>(annotatedMethod, this._setters, s, b, b2);
    }
    
    public boolean anyIgnorals() {
        return this._anyIgnorals(this._fields) || this._anyIgnorals(this._getters) || this._anyIgnorals(this._setters) || this._anyIgnorals(this._ctorParameters);
    }
    
    public boolean anyVisible() {
        return this._anyVisible(this._fields) || this._anyVisible(this._getters) || this._anyVisible(this._setters) || this._anyVisible(this._ctorParameters);
    }
    
    @Override
    public int compareTo(final POJOPropertyBuilder pojoPropertyBuilder) {
        if (this._ctorParameters != null) {
            if (pojoPropertyBuilder._ctorParameters == null) {
                return -1;
            }
        }
        else if (pojoPropertyBuilder._ctorParameters != null) {
            return 1;
        }
        return this.getName().compareTo(pojoPropertyBuilder.getName());
    }
    
    public String findNewName() {
        final Linked<? extends AnnotatedMember> renamed = this.findRenamed(this._ctorParameters, this.findRenamed(this._setters, this.findRenamed(this._getters, this.findRenamed(this._fields, null))));
        if (renamed == null) {
            return null;
        }
        return renamed.explicitName;
    }
    
    @Override
    public AnnotationIntrospector.ReferenceProperty findReferenceType() {
        return this.fromMemberAnnotations((WithMember<AnnotationIntrospector.ReferenceProperty>)new WithMember<AnnotationIntrospector.ReferenceProperty>() {
            public AnnotationIntrospector.ReferenceProperty withMember(final AnnotatedMember annotatedMember) {
                return POJOPropertyBuilder.this._annotationIntrospector.findReferenceType(annotatedMember);
            }
        });
    }
    
    @Override
    public Class<?>[] findViews() {
        return (Class<?>[])this.fromMemberAnnotations((WithMember<Class[]>)new WithMember<Class<?>[]>() {
            public Class<?>[] withMember(final AnnotatedMember annotatedMember) {
                return POJOPropertyBuilder.this._annotationIntrospector.findViews(annotatedMember);
            }
        });
    }
    
    protected <T> T fromMemberAnnotations(final WithMember<T> withMember) {
        T withMember2 = null;
        final T t = null;
        T t2 = null;
        if (this._annotationIntrospector != null) {
            if (this._forSerialization) {
                if (this._getters != null) {
                    t2 = withMember.withMember(this._getters.value);
                }
            }
            else {
                T withMember3 = t;
                if (this._ctorParameters != null) {
                    withMember3 = withMember.withMember(this._ctorParameters.value);
                }
                if ((t2 = withMember3) == null) {
                    t2 = withMember3;
                    if (this._setters != null) {
                        t2 = withMember.withMember(this._setters.value);
                    }
                }
            }
            if ((withMember2 = t2) == null) {
                withMember2 = t2;
                if (this._fields != null) {
                    withMember2 = withMember.withMember(this._fields.value);
                }
            }
        }
        return withMember2;
    }
    
    @Override
    public AnnotatedMember getAccessor() {
        Serializable s;
        if ((s = this.getGetter()) == null) {
            s = this.getField();
        }
        return (AnnotatedMember)s;
    }
    
    @Override
    public AnnotatedParameter getConstructorParameter() {
        if (this._ctorParameters == null) {
            return null;
        }
        Linked<AnnotatedParameter> linked = this._ctorParameters;
        while (!(linked.value.getOwner() instanceof AnnotatedConstructor)) {
            linked = linked.next;
            if (linked == null) {
                return this._ctorParameters.value;
            }
        }
        return linked.value;
    }
    
    @Override
    public AnnotatedField getField() {
        if (this._fields != null) {
            AnnotatedField annotatedField = this._fields.value;
            Linked<AnnotatedField> linked = this._fields.next;
            AnnotatedField annotatedField3;
            while (true) {
                final AnnotatedField annotatedField2 = annotatedField;
                if (linked == null) {
                    return annotatedField2;
                }
                annotatedField3 = linked.value;
                final Class<?> declaringClass = annotatedField.getDeclaringClass();
                final Class<?> declaringClass2 = annotatedField3.getDeclaringClass();
                if (declaringClass == declaringClass2) {
                    break;
                }
                if (declaringClass.isAssignableFrom(declaringClass2)) {
                    annotatedField = annotatedField3;
                }
                else if (!declaringClass2.isAssignableFrom(declaringClass)) {
                    break;
                }
                linked = linked.next;
            }
            throw new IllegalArgumentException("Multiple fields representing property \"" + this.getName() + "\": " + annotatedField.getFullName() + " vs " + annotatedField3.getFullName());
        }
        return null;
    }
    
    @Override
    public AnnotatedMethod getGetter() {
        if (this._getters != null) {
            AnnotatedMethod annotatedMethod = this._getters.value;
            Linked<AnnotatedMethod> linked = this._getters.next;
            AnnotatedMethod annotatedMethod3;
            while (true) {
                final AnnotatedMethod annotatedMethod2 = annotatedMethod;
                if (linked == null) {
                    return annotatedMethod2;
                }
                annotatedMethod3 = linked.value;
                final Class<?> declaringClass = annotatedMethod.getDeclaringClass();
                final Class<?> declaringClass2 = annotatedMethod3.getDeclaringClass();
                if (declaringClass == declaringClass2) {
                    break;
                }
                if (declaringClass.isAssignableFrom(declaringClass2)) {
                    annotatedMethod = annotatedMethod3;
                }
                else if (!declaringClass2.isAssignableFrom(declaringClass)) {
                    break;
                }
                linked = linked.next;
            }
            throw new IllegalArgumentException("Conflicting getter definitions for property \"" + this.getName() + "\": " + annotatedMethod.getFullName() + " vs " + annotatedMethod3.getFullName());
        }
        return null;
    }
    
    public String getInternalName() {
        return this._internalName;
    }
    
    @Override
    public AnnotatedMember getMutator() {
        AnnotatedMember annotatedMember;
        if ((annotatedMember = this.getConstructorParameter()) == null && (annotatedMember = this.getSetter()) == null) {
            annotatedMember = this.getField();
        }
        return annotatedMember;
    }
    
    @Override
    public String getName() {
        return this._name;
    }
    
    @Override
    public AnnotatedMember getPrimaryMember() {
        if (this._forSerialization) {
            return this.getAccessor();
        }
        return this.getMutator();
    }
    
    @Override
    public AnnotatedMethod getSetter() {
        if (this._setters != null) {
            AnnotatedMethod annotatedMethod = this._setters.value;
            Linked<AnnotatedMethod> linked = this._setters.next;
            AnnotatedMethod annotatedMethod3;
            while (true) {
                final AnnotatedMethod annotatedMethod2 = annotatedMethod;
                if (linked == null) {
                    return annotatedMethod2;
                }
                annotatedMethod3 = linked.value;
                final Class<?> declaringClass = annotatedMethod.getDeclaringClass();
                final Class<?> declaringClass2 = annotatedMethod3.getDeclaringClass();
                if (declaringClass == declaringClass2) {
                    break;
                }
                if (declaringClass.isAssignableFrom(declaringClass2)) {
                    annotatedMethod = annotatedMethod3;
                }
                else if (!declaringClass2.isAssignableFrom(declaringClass)) {
                    break;
                }
                linked = linked.next;
            }
            throw new IllegalArgumentException("Conflicting setter definitions for property \"" + this.getName() + "\": " + annotatedMethod.getFullName() + " vs " + annotatedMethod3.getFullName());
        }
        return null;
    }
    
    @Override
    public PropertyName getWrapperName() {
        final AnnotatedMember primaryMember = this.getPrimaryMember();
        if (primaryMember == null || this._annotationIntrospector == null) {
            return null;
        }
        return this._annotationIntrospector.findWrapperName(primaryMember);
    }
    
    @Override
    public boolean hasConstructorParameter() {
        return this._ctorParameters != null;
    }
    
    @Override
    public boolean hasField() {
        return this._fields != null;
    }
    
    @Override
    public boolean hasGetter() {
        return this._getters != null;
    }
    
    @Override
    public boolean hasSetter() {
        return this._setters != null;
    }
    
    @Override
    public boolean isExplicitlyIncluded() {
        return this._anyExplicitNames(this._fields) || this._anyExplicitNames(this._getters) || this._anyExplicitNames(this._setters) || this._anyExplicitNames(this._ctorParameters);
    }
    
    @Override
    public boolean isRequired() {
        final Boolean b = this.fromMemberAnnotations((WithMember<Boolean>)new WithMember<Boolean>() {
            public Boolean withMember(final AnnotatedMember annotatedMember) {
                return POJOPropertyBuilder.this._annotationIntrospector.hasRequiredMarker(annotatedMember);
            }
        });
        return b != null && b;
    }
    
    @Override
    public boolean isTypeId() {
        final Boolean b = this.fromMemberAnnotations((WithMember<Boolean>)new WithMember<Boolean>() {
            public Boolean withMember(final AnnotatedMember annotatedMember) {
                return POJOPropertyBuilder.this._annotationIntrospector.isTypeId(annotatedMember);
            }
        });
        return b != null && b;
    }
    
    public void mergeAnnotations(final boolean b) {
        if (b) {
            if (this._getters != null) {
                this._getters = this._getters.withValue(this._getters.value.withAnnotations(this._mergeAnnotations(0, this._getters, this._fields, this._ctorParameters, this._setters)));
            }
            else if (this._fields != null) {
                this._fields = this._fields.withValue(this._fields.value.withAnnotations(this._mergeAnnotations(0, this._fields, this._ctorParameters, this._setters)));
            }
        }
        else {
            if (this._ctorParameters != null) {
                this._ctorParameters = this._ctorParameters.withValue(this._ctorParameters.value.withAnnotations(this._mergeAnnotations(0, this._ctorParameters, this._setters, this._fields, this._getters)));
                return;
            }
            if (this._setters != null) {
                this._setters = this._setters.withValue(this._setters.value.withAnnotations(this._mergeAnnotations(0, this._setters, this._fields, this._getters)));
                return;
            }
            if (this._fields != null) {
                this._fields = this._fields.withValue(this._fields.value.withAnnotations(this._mergeAnnotations(0, this._fields, this._getters)));
            }
        }
    }
    
    public void removeIgnored() {
        this._fields = this._removeIgnored(this._fields);
        this._getters = this._removeIgnored(this._getters);
        this._setters = this._removeIgnored(this._setters);
        this._ctorParameters = this._removeIgnored(this._ctorParameters);
    }
    
    public void removeNonVisible(final boolean b) {
        this._getters = this._removeNonVisible(this._getters);
        this._ctorParameters = this._removeNonVisible(this._ctorParameters);
        if (b || this._getters == null) {
            this._fields = this._removeNonVisible(this._fields);
            this._setters = this._removeNonVisible(this._setters);
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[Property '").append(this._name).append("'; ctors: ").append(this._ctorParameters).append(", field(s): ").append(this._fields).append(", getter(s): ").append(this._getters).append(", setter(s): ").append(this._setters);
        sb.append("]");
        return sb.toString();
    }
    
    public void trimByVisibility() {
        this._fields = this._trimByVisibility(this._fields);
        this._getters = this._trimByVisibility(this._getters);
        this._setters = this._trimByVisibility(this._setters);
        this._ctorParameters = this._trimByVisibility(this._ctorParameters);
    }
    
    public POJOPropertyBuilder withName(final String s) {
        return new POJOPropertyBuilder(this, s);
    }
    
    private static final class Linked<T>
    {
        public final String explicitName;
        public final boolean isMarkedIgnored;
        public final boolean isVisible;
        public final Linked<T> next;
        public final T value;
        
        public Linked(final T value, final Linked<T> next, final String s, final boolean isVisible, final boolean isMarkedIgnored) {
            this.value = value;
            this.next = next;
            if (s == null) {
                this.explicitName = null;
            }
            else {
                String explicitName = s;
                if (s.length() == 0) {
                    explicitName = null;
                }
                this.explicitName = explicitName;
            }
            this.isVisible = isVisible;
            this.isMarkedIgnored = isMarkedIgnored;
        }
        
        private Linked<T> append(final Linked<T> linked) {
            if (this.next == null) {
                return this.withNext(linked);
            }
            return this.withNext(this.next.append(linked));
        }
        
        @Override
        public String toString() {
            String s = this.value.toString() + "[visible=" + this.isVisible + "]";
            if (this.next != null) {
                s = s + ", " + this.next.toString();
            }
            return s;
        }
        
        public Linked<T> trimByVisibility() {
            Linked linked;
            if (this.next == null) {
                linked = this;
            }
            else {
                final Linked<T> trimByVisibility = this.next.trimByVisibility();
                if (this.explicitName != null) {
                    if (trimByVisibility.explicitName == null) {
                        return this.withNext(null);
                    }
                    return this.withNext(trimByVisibility);
                }
                else {
                    linked = trimByVisibility;
                    if (trimByVisibility.explicitName == null) {
                        if (this.isVisible == trimByVisibility.isVisible) {
                            return this.withNext(trimByVisibility);
                        }
                        linked = trimByVisibility;
                        if (this.isVisible) {
                            return this.withNext(null);
                        }
                    }
                }
            }
            return linked;
        }
        
        public Linked<T> withNext(final Linked<T> linked) {
            if (linked == this.next) {
                return this;
            }
            return new Linked<T>(this.value, linked, this.explicitName, this.isVisible, this.isMarkedIgnored);
        }
        
        public Linked<T> withValue(final T t) {
            if (t == this.value) {
                return this;
            }
            return new Linked<T>(t, this.next, this.explicitName, this.isVisible, this.isMarkedIgnored);
        }
        
        public Linked<T> withoutIgnored() {
            if (!this.isMarkedIgnored) {
                if (this.next != null) {
                    final Linked<T> withoutIgnored = this.next.withoutIgnored();
                    if (withoutIgnored != this.next) {
                        return this.withNext(withoutIgnored);
                    }
                }
                return this;
            }
            if (this.next == null) {
                return null;
            }
            return this.next.withoutIgnored();
        }
        
        public Linked<T> withoutNonVisible() {
            Linked<T> withoutNonVisible;
            if (this.next == null) {
                withoutNonVisible = null;
            }
            else {
                withoutNonVisible = this.next.withoutNonVisible();
            }
            Linked<T> withNext = withoutNonVisible;
            if (this.isVisible) {
                withNext = this.withNext(withoutNonVisible);
            }
            return withNext;
        }
    }
    
    private interface WithMember<T>
    {
        T withMember(final AnnotatedMember p0);
    }
}
