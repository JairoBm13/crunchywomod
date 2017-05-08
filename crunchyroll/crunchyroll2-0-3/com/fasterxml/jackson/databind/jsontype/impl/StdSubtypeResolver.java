// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.Annotated;
import java.util.HashMap;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import java.util.LinkedHashSet;
import java.io.Serializable;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

public class StdSubtypeResolver extends SubtypeResolver implements Serializable
{
    protected LinkedHashSet<NamedType> _registeredSubtypes;
    
    protected void _collectAndResolve(final AnnotatedClass annotatedClass, final NamedType namedType, final MapperConfig<?> mapperConfig, final AnnotationIntrospector annotationIntrospector, final HashMap<NamedType, NamedType> hashMap) {
        NamedType namedType2 = namedType;
        if (!namedType.hasName()) {
            final String typeName = annotationIntrospector.findTypeName(annotatedClass);
            namedType2 = namedType;
            if (typeName != null) {
                namedType2 = new NamedType(namedType.getType(), typeName);
            }
        }
        if (hashMap.containsKey(namedType2)) {
            if (namedType2.hasName() && !hashMap.get(namedType2).hasName()) {
                hashMap.put(namedType2, namedType2);
            }
        }
        else {
            hashMap.put(namedType2, namedType2);
            final List<NamedType> subtypes = annotationIntrospector.findSubtypes(annotatedClass);
            if (subtypes != null && !subtypes.isEmpty()) {
                for (NamedType namedType3 : subtypes) {
                    final AnnotatedClass constructWithoutSuperTypes = AnnotatedClass.constructWithoutSuperTypes(namedType3.getType(), annotationIntrospector, mapperConfig);
                    if (!namedType3.hasName()) {
                        namedType3 = new NamedType(namedType3.getType(), annotationIntrospector.findTypeName(constructWithoutSuperTypes));
                    }
                    this._collectAndResolve(constructWithoutSuperTypes, namedType3, mapperConfig, annotationIntrospector, hashMap);
                }
            }
        }
    }
    
    @Override
    public Collection<NamedType> collectAndResolveSubtypes(final AnnotatedClass annotatedClass, final MapperConfig<?> mapperConfig, final AnnotationIntrospector annotationIntrospector) {
        final HashMap<NamedType, NamedType> hashMap = new HashMap<NamedType, NamedType>();
        if (this._registeredSubtypes != null) {
            final Class<?> rawType = annotatedClass.getRawType();
            for (final NamedType namedType : this._registeredSubtypes) {
                if (rawType.isAssignableFrom(namedType.getType())) {
                    this._collectAndResolve(AnnotatedClass.constructWithoutSuperTypes(namedType.getType(), annotationIntrospector, mapperConfig), namedType, mapperConfig, annotationIntrospector, hashMap);
                }
            }
        }
        this._collectAndResolve(annotatedClass, new NamedType(annotatedClass.getRawType(), null), mapperConfig, annotationIntrospector, hashMap);
        return new ArrayList<NamedType>(hashMap.values());
    }
    
    @Override
    public Collection<NamedType> collectAndResolveSubtypes(final AnnotatedMember annotatedMember, final MapperConfig<?> mapperConfig, final AnnotationIntrospector annotationIntrospector, final JavaType javaType) {
        Class<?> clazz;
        if (javaType == null) {
            clazz = annotatedMember.getRawType();
        }
        else {
            clazz = javaType.getRawClass();
        }
        final HashMap<NamedType, NamedType> hashMap = new HashMap<NamedType, NamedType>();
        if (this._registeredSubtypes != null) {
            for (final NamedType namedType : this._registeredSubtypes) {
                if (clazz.isAssignableFrom(namedType.getType())) {
                    this._collectAndResolve(AnnotatedClass.constructWithoutSuperTypes(namedType.getType(), annotationIntrospector, mapperConfig), namedType, mapperConfig, annotationIntrospector, hashMap);
                }
            }
        }
        final List<NamedType> subtypes = annotationIntrospector.findSubtypes(annotatedMember);
        if (subtypes != null) {
            for (final NamedType namedType2 : subtypes) {
                this._collectAndResolve(AnnotatedClass.constructWithoutSuperTypes(namedType2.getType(), annotationIntrospector, mapperConfig), namedType2, mapperConfig, annotationIntrospector, hashMap);
            }
        }
        this._collectAndResolve(AnnotatedClass.constructWithoutSuperTypes(clazz, annotationIntrospector, mapperConfig), new NamedType(clazz, null), mapperConfig, annotationIntrospector, hashMap);
        return new ArrayList<NamedType>(hashMap.values());
    }
}
