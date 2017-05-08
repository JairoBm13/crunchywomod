// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.cfg;

import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.util.TimeZone;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import java.util.Locale;
import com.fasterxml.jackson.core.Base64Variant;
import java.text.DateFormat;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import java.io.Serializable;

public final class BaseSettings implements Serializable
{
    protected final AnnotationIntrospector _annotationIntrospector;
    protected final ClassIntrospector _classIntrospector;
    protected final DateFormat _dateFormat;
    protected final Base64Variant _defaultBase64;
    protected final HandlerInstantiator _handlerInstantiator;
    protected final Locale _locale;
    protected final PropertyNamingStrategy _propertyNamingStrategy;
    protected final TimeZone _timeZone;
    protected final TypeFactory _typeFactory;
    protected final TypeResolverBuilder<?> _typeResolverBuilder;
    protected final VisibilityChecker<?> _visibilityChecker;
    
    public BaseSettings(final ClassIntrospector classIntrospector, final AnnotationIntrospector annotationIntrospector, final VisibilityChecker<?> visibilityChecker, final PropertyNamingStrategy propertyNamingStrategy, final TypeFactory typeFactory, final TypeResolverBuilder<?> typeResolverBuilder, final DateFormat dateFormat, final HandlerInstantiator handlerInstantiator, final Locale locale, final TimeZone timeZone, final Base64Variant defaultBase64) {
        this._classIntrospector = classIntrospector;
        this._annotationIntrospector = annotationIntrospector;
        this._visibilityChecker = visibilityChecker;
        this._propertyNamingStrategy = propertyNamingStrategy;
        this._typeFactory = typeFactory;
        this._typeResolverBuilder = typeResolverBuilder;
        this._dateFormat = dateFormat;
        this._handlerInstantiator = handlerInstantiator;
        this._locale = locale;
        this._timeZone = timeZone;
        this._defaultBase64 = defaultBase64;
    }
    
    public AnnotationIntrospector getAnnotationIntrospector() {
        return this._annotationIntrospector;
    }
    
    public Base64Variant getBase64Variant() {
        return this._defaultBase64;
    }
    
    public ClassIntrospector getClassIntrospector() {
        return this._classIntrospector;
    }
    
    public DateFormat getDateFormat() {
        return this._dateFormat;
    }
    
    public HandlerInstantiator getHandlerInstantiator() {
        return this._handlerInstantiator;
    }
    
    public Locale getLocale() {
        return this._locale;
    }
    
    public PropertyNamingStrategy getPropertyNamingStrategy() {
        return this._propertyNamingStrategy;
    }
    
    public TimeZone getTimeZone() {
        return this._timeZone;
    }
    
    public TypeFactory getTypeFactory() {
        return this._typeFactory;
    }
    
    public TypeResolverBuilder<?> getTypeResolverBuilder() {
        return this._typeResolverBuilder;
    }
    
    public VisibilityChecker<?> getVisibilityChecker() {
        return this._visibilityChecker;
    }
}
