// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.core.Version;
import java.lang.annotation.Annotation;
import com.fasterxml.jackson.databind.util.NameTransformer;
import java.util.Collection;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.Serializable;
import com.fasterxml.jackson.databind.AnnotationIntrospector;

public class AnnotationIntrospectorPair extends AnnotationIntrospector implements Serializable
{
    protected final AnnotationIntrospector _primary;
    protected final AnnotationIntrospector _secondary;
    
    @Override
    public VisibilityChecker<?> findAutoDetectVisibility(final AnnotatedClass annotatedClass, final VisibilityChecker<?> visibilityChecker) {
        return this._primary.findAutoDetectVisibility(annotatedClass, this._secondary.findAutoDetectVisibility(annotatedClass, visibilityChecker));
    }
    
    @Override
    public Object findContentDeserializer(final Annotated annotated) {
        final Object contentDeserializer = this._primary.findContentDeserializer(annotated);
        Object contentDeserializer2;
        if (contentDeserializer == null || contentDeserializer == JsonDeserializer.None.class || (contentDeserializer2 = contentDeserializer) == NoClass.class) {
            contentDeserializer2 = this._secondary.findContentDeserializer(annotated);
        }
        return contentDeserializer2;
    }
    
    @Override
    public Object findContentSerializer(final Annotated annotated) {
        final Object contentSerializer = this._primary.findContentSerializer(annotated);
        Object contentSerializer2;
        if (contentSerializer == null || contentSerializer == JsonSerializer.None.class || (contentSerializer2 = contentSerializer) == NoClass.class) {
            contentSerializer2 = this._secondary.findContentSerializer(annotated);
        }
        return contentSerializer2;
    }
    
    @Override
    public Object findDeserializationContentConverter(final AnnotatedMember annotatedMember) {
        Object o;
        if ((o = this._primary.findDeserializationContentConverter(annotatedMember)) == null) {
            o = this._secondary.findDeserializationContentConverter(annotatedMember);
        }
        return o;
    }
    
    @Override
    public Class<?> findDeserializationContentType(final Annotated annotated, final JavaType javaType) {
        Class<?> clazz;
        if ((clazz = this._primary.findDeserializationContentType(annotated, javaType)) == null) {
            clazz = this._secondary.findDeserializationContentType(annotated, javaType);
        }
        return clazz;
    }
    
    @Override
    public Object findDeserializationConverter(final Annotated annotated) {
        Object o;
        if ((o = this._primary.findDeserializationConverter(annotated)) == null) {
            o = this._secondary.findDeserializationConverter(annotated);
        }
        return o;
    }
    
    @Override
    public Class<?> findDeserializationKeyType(final Annotated annotated, final JavaType javaType) {
        Class<?> clazz;
        if ((clazz = this._primary.findDeserializationKeyType(annotated, javaType)) == null) {
            clazz = this._secondary.findDeserializationKeyType(annotated, javaType);
        }
        return clazz;
    }
    
    @Deprecated
    @Override
    public String findDeserializationName(final AnnotatedField annotatedField) {
        final String deserializationName = this._primary.findDeserializationName(annotatedField);
        String s;
        if (deserializationName == null) {
            s = this._secondary.findDeserializationName(annotatedField);
        }
        else if (deserializationName.length() != 0 || (s = this._secondary.findDeserializationName(annotatedField)) == null) {
            return deserializationName;
        }
        return s;
    }
    
    @Deprecated
    @Override
    public String findDeserializationName(final AnnotatedMethod annotatedMethod) {
        final String deserializationName = this._primary.findDeserializationName(annotatedMethod);
        String s;
        if (deserializationName == null) {
            s = this._secondary.findDeserializationName(annotatedMethod);
        }
        else if (deserializationName.length() != 0 || (s = this._secondary.findDeserializationName(annotatedMethod)) == null) {
            return deserializationName;
        }
        return s;
    }
    
    @Deprecated
    @Override
    public String findDeserializationName(final AnnotatedParameter annotatedParameter) {
        String s;
        if ((s = this._primary.findDeserializationName(annotatedParameter)) == null) {
            s = this._secondary.findDeserializationName(annotatedParameter);
        }
        return s;
    }
    
    @Override
    public Class<?> findDeserializationType(final Annotated annotated, final JavaType javaType) {
        Class<?> clazz;
        if ((clazz = this._primary.findDeserializationType(annotated, javaType)) == null) {
            clazz = this._secondary.findDeserializationType(annotated, javaType);
        }
        return clazz;
    }
    
    @Override
    public Object findDeserializer(final Annotated annotated) {
        Object o;
        if ((o = this._primary.findDeserializer(annotated)) == null) {
            o = this._secondary.findDeserializer(annotated);
        }
        return o;
    }
    
    @Override
    public String findEnumValue(final Enum<?> enum1) {
        String s;
        if ((s = this._primary.findEnumValue(enum1)) == null) {
            s = this._secondary.findEnumValue(enum1);
        }
        return s;
    }
    
    @Override
    public Object findFilterId(final AnnotatedClass annotatedClass) {
        Object o;
        if ((o = this._primary.findFilterId(annotatedClass)) == null) {
            o = this._secondary.findFilterId(annotatedClass);
        }
        return o;
    }
    
    @Override
    public JsonFormat.Value findFormat(final Annotated annotated) {
        JsonFormat.Value value;
        if ((value = this._primary.findFormat(annotated)) == null) {
            value = this._secondary.findFormat(annotated);
        }
        return value;
    }
    
    @Override
    public Boolean findIgnoreUnknownProperties(final AnnotatedClass annotatedClass) {
        Boolean b;
        if ((b = this._primary.findIgnoreUnknownProperties(annotatedClass)) == null) {
            b = this._secondary.findIgnoreUnknownProperties(annotatedClass);
        }
        return b;
    }
    
    @Override
    public Object findInjectableValueId(final AnnotatedMember annotatedMember) {
        Object o;
        if ((o = this._primary.findInjectableValueId(annotatedMember)) == null) {
            o = this._secondary.findInjectableValueId(annotatedMember);
        }
        return o;
    }
    
    @Override
    public Object findKeyDeserializer(final Annotated annotated) {
        final Object keyDeserializer = this._primary.findKeyDeserializer(annotated);
        Object keyDeserializer2;
        if (keyDeserializer == null || keyDeserializer == KeyDeserializer.None.class || (keyDeserializer2 = keyDeserializer) == NoClass.class) {
            keyDeserializer2 = this._secondary.findKeyDeserializer(annotated);
        }
        return keyDeserializer2;
    }
    
    @Override
    public Object findKeySerializer(final Annotated annotated) {
        final Object keySerializer = this._primary.findKeySerializer(annotated);
        Object keySerializer2;
        if (keySerializer == null || keySerializer == JsonSerializer.None.class || (keySerializer2 = keySerializer) == NoClass.class) {
            keySerializer2 = this._secondary.findKeySerializer(annotated);
        }
        return keySerializer2;
    }
    
    @Override
    public PropertyName findNameForDeserialization(final Annotated annotated) {
        final PropertyName nameForDeserialization = this._primary.findNameForDeserialization(annotated);
        PropertyName propertyName;
        if (nameForDeserialization == null) {
            propertyName = this._secondary.findNameForDeserialization(annotated);
        }
        else if (nameForDeserialization != PropertyName.USE_DEFAULT || (propertyName = this._secondary.findNameForDeserialization(annotated)) == null) {
            return nameForDeserialization;
        }
        return propertyName;
    }
    
    @Override
    public PropertyName findNameForSerialization(final Annotated annotated) {
        final PropertyName nameForSerialization = this._primary.findNameForSerialization(annotated);
        PropertyName propertyName;
        if (nameForSerialization == null) {
            propertyName = this._secondary.findNameForSerialization(annotated);
        }
        else if (nameForSerialization != PropertyName.USE_DEFAULT || (propertyName = this._secondary.findNameForSerialization(annotated)) == null) {
            return nameForSerialization;
        }
        return propertyName;
    }
    
    @Override
    public Object findNamingStrategy(final AnnotatedClass annotatedClass) {
        Object o;
        if ((o = this._primary.findNamingStrategy(annotatedClass)) == null) {
            o = this._secondary.findNamingStrategy(annotatedClass);
        }
        return o;
    }
    
    @Override
    public ObjectIdInfo findObjectIdInfo(final Annotated annotated) {
        ObjectIdInfo objectIdInfo;
        if ((objectIdInfo = this._primary.findObjectIdInfo(annotated)) == null) {
            objectIdInfo = this._secondary.findObjectIdInfo(annotated);
        }
        return objectIdInfo;
    }
    
    @Override
    public ObjectIdInfo findObjectReferenceInfo(final Annotated annotated, ObjectIdInfo objectReferenceInfo) {
        objectReferenceInfo = this._secondary.findObjectReferenceInfo(annotated, objectReferenceInfo);
        return this._primary.findObjectReferenceInfo(annotated, objectReferenceInfo);
    }
    
    @Override
    public Class<?> findPOJOBuilder(final AnnotatedClass annotatedClass) {
        Class<?> clazz;
        if ((clazz = this._primary.findPOJOBuilder(annotatedClass)) == null) {
            clazz = this._secondary.findPOJOBuilder(annotatedClass);
        }
        return clazz;
    }
    
    @Override
    public JsonPOJOBuilder.Value findPOJOBuilderConfig(final AnnotatedClass annotatedClass) {
        JsonPOJOBuilder.Value value;
        if ((value = this._primary.findPOJOBuilderConfig(annotatedClass)) == null) {
            value = this._secondary.findPOJOBuilderConfig(annotatedClass);
        }
        return value;
    }
    
    @Override
    public String[] findPropertiesToIgnore(final Annotated annotated) {
        String[] array;
        if ((array = this._primary.findPropertiesToIgnore(annotated)) == null) {
            array = this._secondary.findPropertiesToIgnore(annotated);
        }
        return array;
    }
    
    @Override
    public TypeResolverBuilder<?> findPropertyContentTypeResolver(final MapperConfig<?> mapperConfig, final AnnotatedMember annotatedMember, final JavaType javaType) {
        TypeResolverBuilder<?> typeResolverBuilder;
        if ((typeResolverBuilder = this._primary.findPropertyContentTypeResolver(mapperConfig, annotatedMember, javaType)) == null) {
            typeResolverBuilder = this._secondary.findPropertyContentTypeResolver(mapperConfig, annotatedMember, javaType);
        }
        return typeResolverBuilder;
    }
    
    @Override
    public TypeResolverBuilder<?> findPropertyTypeResolver(final MapperConfig<?> mapperConfig, final AnnotatedMember annotatedMember, final JavaType javaType) {
        TypeResolverBuilder<?> typeResolverBuilder;
        if ((typeResolverBuilder = this._primary.findPropertyTypeResolver(mapperConfig, annotatedMember, javaType)) == null) {
            typeResolverBuilder = this._secondary.findPropertyTypeResolver(mapperConfig, annotatedMember, javaType);
        }
        return typeResolverBuilder;
    }
    
    @Override
    public ReferenceProperty findReferenceType(final AnnotatedMember annotatedMember) {
        ReferenceProperty referenceProperty;
        if ((referenceProperty = this._primary.findReferenceType(annotatedMember)) == null) {
            referenceProperty = this._secondary.findReferenceType(annotatedMember);
        }
        return referenceProperty;
    }
    
    @Override
    public PropertyName findRootName(final AnnotatedClass annotatedClass) {
        final PropertyName rootName = this._primary.findRootName(annotatedClass);
        PropertyName rootName2;
        if (rootName == null) {
            rootName2 = this._secondary.findRootName(annotatedClass);
        }
        else {
            rootName2 = rootName;
            if (!rootName.hasSimpleName()) {
                final PropertyName rootName3 = this._secondary.findRootName(annotatedClass);
                rootName2 = rootName;
                if (rootName3 != null) {
                    return rootName3;
                }
            }
        }
        return rootName2;
    }
    
    @Override
    public Object findSerializationContentConverter(final AnnotatedMember annotatedMember) {
        Object o;
        if ((o = this._primary.findSerializationContentConverter(annotatedMember)) == null) {
            o = this._secondary.findSerializationContentConverter(annotatedMember);
        }
        return o;
    }
    
    @Override
    public Class<?> findSerializationContentType(final Annotated annotated, final JavaType javaType) {
        Class<?> clazz;
        if ((clazz = this._primary.findSerializationContentType(annotated, javaType)) == null) {
            clazz = this._secondary.findSerializationContentType(annotated, javaType);
        }
        return clazz;
    }
    
    @Override
    public Object findSerializationConverter(final Annotated annotated) {
        Object o;
        if ((o = this._primary.findSerializationConverter(annotated)) == null) {
            o = this._secondary.findSerializationConverter(annotated);
        }
        return o;
    }
    
    @Override
    public JsonInclude.Include findSerializationInclusion(final Annotated annotated, JsonInclude.Include serializationInclusion) {
        serializationInclusion = this._secondary.findSerializationInclusion(annotated, serializationInclusion);
        return this._primary.findSerializationInclusion(annotated, serializationInclusion);
    }
    
    @Override
    public Class<?> findSerializationKeyType(final Annotated annotated, final JavaType javaType) {
        Class<?> clazz;
        if ((clazz = this._primary.findSerializationKeyType(annotated, javaType)) == null) {
            clazz = this._secondary.findSerializationKeyType(annotated, javaType);
        }
        return clazz;
    }
    
    @Deprecated
    @Override
    public String findSerializationName(final AnnotatedField annotatedField) {
        final String serializationName = this._primary.findSerializationName(annotatedField);
        String s;
        if (serializationName == null) {
            s = this._secondary.findSerializationName(annotatedField);
        }
        else if (serializationName.length() != 0 || (s = this._secondary.findSerializationName(annotatedField)) == null) {
            return serializationName;
        }
        return s;
    }
    
    @Deprecated
    @Override
    public String findSerializationName(final AnnotatedMethod annotatedMethod) {
        final String serializationName = this._primary.findSerializationName(annotatedMethod);
        String s;
        if (serializationName == null) {
            s = this._secondary.findSerializationName(annotatedMethod);
        }
        else if (serializationName.length() != 0 || (s = this._secondary.findSerializationName(annotatedMethod)) == null) {
            return serializationName;
        }
        return s;
    }
    
    @Override
    public String[] findSerializationPropertyOrder(final AnnotatedClass annotatedClass) {
        String[] array;
        if ((array = this._primary.findSerializationPropertyOrder(annotatedClass)) == null) {
            array = this._secondary.findSerializationPropertyOrder(annotatedClass);
        }
        return array;
    }
    
    @Override
    public Boolean findSerializationSortAlphabetically(final AnnotatedClass annotatedClass) {
        Boolean b;
        if ((b = this._primary.findSerializationSortAlphabetically(annotatedClass)) == null) {
            b = this._secondary.findSerializationSortAlphabetically(annotatedClass);
        }
        return b;
    }
    
    @Override
    public Class<?> findSerializationType(final Annotated annotated) {
        Class<?> clazz;
        if ((clazz = this._primary.findSerializationType(annotated)) == null) {
            clazz = this._secondary.findSerializationType(annotated);
        }
        return clazz;
    }
    
    @Override
    public JsonSerialize.Typing findSerializationTyping(final Annotated annotated) {
        JsonSerialize.Typing typing;
        if ((typing = this._primary.findSerializationTyping(annotated)) == null) {
            typing = this._secondary.findSerializationTyping(annotated);
        }
        return typing;
    }
    
    @Override
    public Object findSerializer(final Annotated annotated) {
        Object o;
        if ((o = this._primary.findSerializer(annotated)) == null) {
            o = this._secondary.findSerializer(annotated);
        }
        return o;
    }
    
    @Override
    public List<NamedType> findSubtypes(final Annotated annotated) {
        final List<NamedType> subtypes = this._primary.findSubtypes(annotated);
        final List<NamedType> subtypes2 = this._secondary.findSubtypes(annotated);
        if (subtypes == null || subtypes.isEmpty()) {
            return subtypes2;
        }
        if (subtypes2 == null || subtypes2.isEmpty()) {
            return subtypes;
        }
        final ArrayList list = new ArrayList<Object>(subtypes.size() + subtypes2.size());
        list.addAll(subtypes);
        list.addAll(subtypes2);
        return (List<NamedType>)list;
    }
    
    @Override
    public String findTypeName(final AnnotatedClass annotatedClass) {
        final String typeName = this._primary.findTypeName(annotatedClass);
        if (typeName != null) {
            final String typeName2 = typeName;
            if (typeName.length() != 0) {
                return typeName2;
            }
        }
        return this._secondary.findTypeName(annotatedClass);
    }
    
    @Override
    public TypeResolverBuilder<?> findTypeResolver(final MapperConfig<?> mapperConfig, final AnnotatedClass annotatedClass, final JavaType javaType) {
        TypeResolverBuilder<?> typeResolverBuilder;
        if ((typeResolverBuilder = this._primary.findTypeResolver(mapperConfig, annotatedClass, javaType)) == null) {
            typeResolverBuilder = this._secondary.findTypeResolver(mapperConfig, annotatedClass, javaType);
        }
        return typeResolverBuilder;
    }
    
    @Override
    public NameTransformer findUnwrappingNameTransformer(final AnnotatedMember annotatedMember) {
        NameTransformer nameTransformer;
        if ((nameTransformer = this._primary.findUnwrappingNameTransformer(annotatedMember)) == null) {
            nameTransformer = this._secondary.findUnwrappingNameTransformer(annotatedMember);
        }
        return nameTransformer;
    }
    
    @Override
    public Object findValueInstantiator(final AnnotatedClass annotatedClass) {
        Object o;
        if ((o = this._primary.findValueInstantiator(annotatedClass)) == null) {
            o = this._secondary.findValueInstantiator(annotatedClass);
        }
        return o;
    }
    
    @Override
    public Class<?>[] findViews(final Annotated annotated) {
        Class<?>[] array;
        if ((array = this._primary.findViews(annotated)) == null) {
            array = this._secondary.findViews(annotated);
        }
        return array;
    }
    
    @Override
    public PropertyName findWrapperName(final Annotated annotated) {
        final PropertyName wrapperName = this._primary.findWrapperName(annotated);
        PropertyName propertyName;
        if (wrapperName == null) {
            propertyName = this._secondary.findWrapperName(annotated);
        }
        else if (wrapperName != PropertyName.USE_DEFAULT || (propertyName = this._secondary.findWrapperName(annotated)) == null) {
            return wrapperName;
        }
        return propertyName;
    }
    
    @Override
    public boolean hasAnyGetterAnnotation(final AnnotatedMethod annotatedMethod) {
        return this._primary.hasAnyGetterAnnotation(annotatedMethod) || this._secondary.hasAnyGetterAnnotation(annotatedMethod);
    }
    
    @Override
    public boolean hasAnySetterAnnotation(final AnnotatedMethod annotatedMethod) {
        return this._primary.hasAnySetterAnnotation(annotatedMethod) || this._secondary.hasAnySetterAnnotation(annotatedMethod);
    }
    
    @Override
    public boolean hasAsValueAnnotation(final AnnotatedMethod annotatedMethod) {
        return this._primary.hasAsValueAnnotation(annotatedMethod) || this._secondary.hasAsValueAnnotation(annotatedMethod);
    }
    
    @Override
    public boolean hasCreatorAnnotation(final Annotated annotated) {
        return this._primary.hasCreatorAnnotation(annotated) || this._secondary.hasCreatorAnnotation(annotated);
    }
    
    @Override
    public boolean hasIgnoreMarker(final AnnotatedMember annotatedMember) {
        return this._primary.hasIgnoreMarker(annotatedMember) || this._secondary.hasIgnoreMarker(annotatedMember);
    }
    
    @Override
    public Boolean hasRequiredMarker(final AnnotatedMember annotatedMember) {
        Boolean b;
        if ((b = this._primary.hasRequiredMarker(annotatedMember)) == null) {
            b = this._secondary.hasRequiredMarker(annotatedMember);
        }
        return b;
    }
    
    @Override
    public boolean isAnnotationBundle(final Annotation annotation) {
        return this._primary.isAnnotationBundle(annotation) || this._secondary.isAnnotationBundle(annotation);
    }
    
    @Override
    public Boolean isIgnorableType(final AnnotatedClass annotatedClass) {
        Boolean b;
        if ((b = this._primary.isIgnorableType(annotatedClass)) == null) {
            b = this._secondary.isIgnorableType(annotatedClass);
        }
        return b;
    }
    
    @Override
    public Boolean isTypeId(final AnnotatedMember annotatedMember) {
        Boolean b;
        if ((b = this._primary.isTypeId(annotatedMember)) == null) {
            b = this._secondary.isTypeId(annotatedMember);
        }
        return b;
    }
    
    @Override
    public Version version() {
        return this._primary.version();
    }
}
