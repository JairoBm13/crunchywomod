// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.core.Version;
import java.lang.annotation.Annotation;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import java.io.Serializable;
import com.fasterxml.jackson.core.Versioned;

public abstract class AnnotationIntrospector implements Versioned, Serializable
{
    public static AnnotationIntrospector nopInstance() {
        return NopAnnotationIntrospector.instance;
    }
    
    public VisibilityChecker<?> findAutoDetectVisibility(final AnnotatedClass annotatedClass, final VisibilityChecker<?> visibilityChecker) {
        return visibilityChecker;
    }
    
    public Object findContentDeserializer(final Annotated annotated) {
        return null;
    }
    
    public Object findContentSerializer(final Annotated annotated) {
        return null;
    }
    
    public Object findDeserializationContentConverter(final AnnotatedMember annotatedMember) {
        return null;
    }
    
    public Class<?> findDeserializationContentType(final Annotated annotated, final JavaType javaType) {
        return null;
    }
    
    public Object findDeserializationConverter(final Annotated annotated) {
        return null;
    }
    
    public Class<?> findDeserializationKeyType(final Annotated annotated, final JavaType javaType) {
        return null;
    }
    
    @Deprecated
    public String findDeserializationName(final AnnotatedField annotatedField) {
        return null;
    }
    
    @Deprecated
    public String findDeserializationName(final AnnotatedMethod annotatedMethod) {
        return null;
    }
    
    @Deprecated
    public String findDeserializationName(final AnnotatedParameter annotatedParameter) {
        return null;
    }
    
    public Class<?> findDeserializationType(final Annotated annotated, final JavaType javaType) {
        return null;
    }
    
    public Object findDeserializer(final Annotated annotated) {
        return null;
    }
    
    public String findEnumValue(final Enum<?> enum1) {
        return enum1.name();
    }
    
    public Object findFilterId(final AnnotatedClass annotatedClass) {
        return null;
    }
    
    public JsonFormat.Value findFormat(final Annotated annotated) {
        if (annotated instanceof AnnotatedMember) {
            return this.findFormat((AnnotatedMember)annotated);
        }
        return null;
    }
    
    @Deprecated
    public JsonFormat.Value findFormat(final AnnotatedMember annotatedMember) {
        return null;
    }
    
    public Boolean findIgnoreUnknownProperties(final AnnotatedClass annotatedClass) {
        return null;
    }
    
    public Object findInjectableValueId(final AnnotatedMember annotatedMember) {
        return null;
    }
    
    public Object findKeyDeserializer(final Annotated annotated) {
        return null;
    }
    
    public Object findKeySerializer(final Annotated annotated) {
        return null;
    }
    
    public PropertyName findNameForDeserialization(final Annotated annotated) {
        PropertyName use_DEFAULT = null;
        String s;
        if (annotated instanceof AnnotatedField) {
            s = this.findDeserializationName((AnnotatedField)annotated);
        }
        else if (annotated instanceof AnnotatedMethod) {
            s = this.findDeserializationName((AnnotatedMethod)annotated);
        }
        else if (annotated instanceof AnnotatedParameter) {
            s = this.findDeserializationName((AnnotatedParameter)annotated);
        }
        else {
            s = null;
        }
        if (s != null) {
            if (s.length() != 0) {
                return new PropertyName(s);
            }
            use_DEFAULT = PropertyName.USE_DEFAULT;
        }
        return use_DEFAULT;
    }
    
    public PropertyName findNameForSerialization(final Annotated annotated) {
        PropertyName use_DEFAULT = null;
        String s;
        if (annotated instanceof AnnotatedField) {
            s = this.findSerializationName((AnnotatedField)annotated);
        }
        else if (annotated instanceof AnnotatedMethod) {
            s = this.findSerializationName((AnnotatedMethod)annotated);
        }
        else {
            s = null;
        }
        if (s != null) {
            if (s.length() != 0) {
                return new PropertyName(s);
            }
            use_DEFAULT = PropertyName.USE_DEFAULT;
        }
        return use_DEFAULT;
    }
    
    public Object findNamingStrategy(final AnnotatedClass annotatedClass) {
        return null;
    }
    
    public ObjectIdInfo findObjectIdInfo(final Annotated annotated) {
        return null;
    }
    
    public ObjectIdInfo findObjectReferenceInfo(final Annotated annotated, final ObjectIdInfo objectIdInfo) {
        return objectIdInfo;
    }
    
    public Class<?> findPOJOBuilder(final AnnotatedClass annotatedClass) {
        return null;
    }
    
    public JsonPOJOBuilder.Value findPOJOBuilderConfig(final AnnotatedClass annotatedClass) {
        return null;
    }
    
    public String[] findPropertiesToIgnore(final Annotated annotated) {
        return null;
    }
    
    public TypeResolverBuilder<?> findPropertyContentTypeResolver(final MapperConfig<?> mapperConfig, final AnnotatedMember annotatedMember, final JavaType javaType) {
        return null;
    }
    
    public TypeResolverBuilder<?> findPropertyTypeResolver(final MapperConfig<?> mapperConfig, final AnnotatedMember annotatedMember, final JavaType javaType) {
        return null;
    }
    
    public ReferenceProperty findReferenceType(final AnnotatedMember annotatedMember) {
        return null;
    }
    
    public PropertyName findRootName(final AnnotatedClass annotatedClass) {
        return null;
    }
    
    public Object findSerializationContentConverter(final AnnotatedMember annotatedMember) {
        return null;
    }
    
    public Class<?> findSerializationContentType(final Annotated annotated, final JavaType javaType) {
        return null;
    }
    
    public Object findSerializationConverter(final Annotated annotated) {
        return null;
    }
    
    public JsonInclude.Include findSerializationInclusion(final Annotated annotated, final JsonInclude.Include include) {
        return include;
    }
    
    public Class<?> findSerializationKeyType(final Annotated annotated, final JavaType javaType) {
        return null;
    }
    
    @Deprecated
    public String findSerializationName(final AnnotatedField annotatedField) {
        return null;
    }
    
    @Deprecated
    public String findSerializationName(final AnnotatedMethod annotatedMethod) {
        return null;
    }
    
    public String[] findSerializationPropertyOrder(final AnnotatedClass annotatedClass) {
        return null;
    }
    
    public Boolean findSerializationSortAlphabetically(final AnnotatedClass annotatedClass) {
        return null;
    }
    
    public Class<?> findSerializationType(final Annotated annotated) {
        return null;
    }
    
    public JsonSerialize.Typing findSerializationTyping(final Annotated annotated) {
        return null;
    }
    
    public Object findSerializer(final Annotated annotated) {
        return null;
    }
    
    public List<NamedType> findSubtypes(final Annotated annotated) {
        return null;
    }
    
    public String findTypeName(final AnnotatedClass annotatedClass) {
        return null;
    }
    
    public TypeResolverBuilder<?> findTypeResolver(final MapperConfig<?> mapperConfig, final AnnotatedClass annotatedClass, final JavaType javaType) {
        return null;
    }
    
    public NameTransformer findUnwrappingNameTransformer(final AnnotatedMember annotatedMember) {
        return null;
    }
    
    public Object findValueInstantiator(final AnnotatedClass annotatedClass) {
        return null;
    }
    
    public Class<?>[] findViews(final Annotated annotated) {
        return null;
    }
    
    public PropertyName findWrapperName(final Annotated annotated) {
        return null;
    }
    
    public boolean hasAnyGetterAnnotation(final AnnotatedMethod annotatedMethod) {
        return false;
    }
    
    public boolean hasAnySetterAnnotation(final AnnotatedMethod annotatedMethod) {
        return false;
    }
    
    public boolean hasAsValueAnnotation(final AnnotatedMethod annotatedMethod) {
        return false;
    }
    
    public boolean hasCreatorAnnotation(final Annotated annotated) {
        return false;
    }
    
    public boolean hasIgnoreMarker(final AnnotatedMember annotatedMember) {
        return false;
    }
    
    public Boolean hasRequiredMarker(final AnnotatedMember annotatedMember) {
        return null;
    }
    
    public boolean isAnnotationBundle(final Annotation annotation) {
        return false;
    }
    
    public Boolean isIgnorableType(final AnnotatedClass annotatedClass) {
        return null;
    }
    
    public Boolean isTypeId(final AnnotatedMember annotatedMember) {
        return null;
    }
    
    @Override
    public abstract Version version();
    
    @Deprecated
    public static class Pair extends AnnotationIntrospectorPair
    {
    }
    
    public static class ReferenceProperty
    {
        private final String _name;
        private final Type _type;
        
        public ReferenceProperty(final Type type, final String name) {
            this._type = type;
            this._name = name;
        }
        
        public static ReferenceProperty back(final String s) {
            return new ReferenceProperty(Type.BACK_REFERENCE, s);
        }
        
        public static ReferenceProperty managed(final String s) {
            return new ReferenceProperty(Type.MANAGED_REFERENCE, s);
        }
        
        public String getName() {
            return this._name;
        }
        
        public boolean isBackReference() {
            return this._type == Type.BACK_REFERENCE;
        }
        
        public boolean isManagedReference() {
            return this._type == Type.MANAGED_REFERENCE;
        }
        
        public enum Type
        {
            BACK_REFERENCE, 
            MANAGED_REFERENCE;
        }
    }
}
