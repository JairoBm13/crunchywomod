// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import com.fasterxml.jackson.core.util.InternCache;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.impl.FailingDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.util.ViewMatcher;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.impl.NullProvider;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.Serializable;
import com.fasterxml.jackson.databind.BeanProperty;

public abstract class SettableBeanProperty implements BeanProperty, Serializable
{
    protected static final JsonDeserializer<Object> MISSING_VALUE_DESERIALIZER;
    protected final transient Annotations _contextAnnotations;
    protected final boolean _isRequired;
    protected String _managedReferenceName;
    protected final NullProvider _nullProvider;
    protected final String _propName;
    protected int _propertyIndex;
    protected final JavaType _type;
    protected JsonDeserializer<Object> _valueDeserializer;
    protected final TypeDeserializer _valueTypeDeserializer;
    protected ViewMatcher _viewMatcher;
    protected final PropertyName _wrapperName;
    
    static {
        MISSING_VALUE_DESERIALIZER = new FailingDeserializer("No _valueDeserializer assigned");
    }
    
    protected SettableBeanProperty(final SettableBeanProperty settableBeanProperty) {
        this._propertyIndex = -1;
        this._propName = settableBeanProperty._propName;
        this._type = settableBeanProperty._type;
        this._wrapperName = settableBeanProperty._wrapperName;
        this._isRequired = settableBeanProperty._isRequired;
        this._contextAnnotations = settableBeanProperty._contextAnnotations;
        this._valueDeserializer = settableBeanProperty._valueDeserializer;
        this._valueTypeDeserializer = settableBeanProperty._valueTypeDeserializer;
        this._nullProvider = settableBeanProperty._nullProvider;
        this._managedReferenceName = settableBeanProperty._managedReferenceName;
        this._propertyIndex = settableBeanProperty._propertyIndex;
        this._viewMatcher = settableBeanProperty._viewMatcher;
    }
    
    protected SettableBeanProperty(final SettableBeanProperty settableBeanProperty, final JsonDeserializer<?> valueDeserializer) {
        NullProvider nullProvider = null;
        this._propertyIndex = -1;
        this._propName = settableBeanProperty._propName;
        this._type = settableBeanProperty._type;
        this._wrapperName = settableBeanProperty._wrapperName;
        this._isRequired = settableBeanProperty._isRequired;
        this._contextAnnotations = settableBeanProperty._contextAnnotations;
        this._valueTypeDeserializer = settableBeanProperty._valueTypeDeserializer;
        this._managedReferenceName = settableBeanProperty._managedReferenceName;
        this._propertyIndex = settableBeanProperty._propertyIndex;
        if (valueDeserializer == null) {
            this._nullProvider = null;
            this._valueDeserializer = SettableBeanProperty.MISSING_VALUE_DESERIALIZER;
        }
        else {
            final Object nullValue = valueDeserializer.getNullValue();
            if (nullValue != null) {
                nullProvider = new NullProvider(this._type, nullValue);
            }
            this._nullProvider = nullProvider;
            this._valueDeserializer = (JsonDeserializer<Object>)valueDeserializer;
        }
        this._viewMatcher = settableBeanProperty._viewMatcher;
    }
    
    protected SettableBeanProperty(final SettableBeanProperty settableBeanProperty, final String propName) {
        this._propertyIndex = -1;
        this._propName = propName;
        this._type = settableBeanProperty._type;
        this._wrapperName = settableBeanProperty._wrapperName;
        this._isRequired = settableBeanProperty._isRequired;
        this._contextAnnotations = settableBeanProperty._contextAnnotations;
        this._valueDeserializer = settableBeanProperty._valueDeserializer;
        this._valueTypeDeserializer = settableBeanProperty._valueTypeDeserializer;
        this._nullProvider = settableBeanProperty._nullProvider;
        this._managedReferenceName = settableBeanProperty._managedReferenceName;
        this._propertyIndex = settableBeanProperty._propertyIndex;
        this._viewMatcher = settableBeanProperty._viewMatcher;
    }
    
    protected SettableBeanProperty(final BeanPropertyDefinition beanPropertyDefinition, final JavaType javaType, final TypeDeserializer typeDeserializer, final Annotations annotations) {
        this(beanPropertyDefinition.getName(), javaType, beanPropertyDefinition.getWrapperName(), typeDeserializer, annotations, beanPropertyDefinition.isRequired());
    }
    
    protected SettableBeanProperty(final String s, final JavaType type, final PropertyName wrapperName, final TypeDeserializer typeDeserializer, final Annotations contextAnnotations, final boolean isRequired) {
        this._propertyIndex = -1;
        if (s == null || s.length() == 0) {
            this._propName = "";
        }
        else {
            this._propName = InternCache.instance.intern(s);
        }
        this._type = type;
        this._wrapperName = wrapperName;
        this._isRequired = isRequired;
        this._contextAnnotations = contextAnnotations;
        this._viewMatcher = null;
        this._nullProvider = null;
        TypeDeserializer forProperty = typeDeserializer;
        if (typeDeserializer != null) {
            forProperty = typeDeserializer.forProperty(this);
        }
        this._valueTypeDeserializer = forProperty;
        this._valueDeserializer = SettableBeanProperty.MISSING_VALUE_DESERIALIZER;
    }
    
    protected IOException _throwAsIOE(final Exception ex) throws IOException {
        if (ex instanceof IOException) {
            throw (IOException)ex;
        }
        Throwable cause = ex;
        if (ex instanceof RuntimeException) {
            throw (RuntimeException)ex;
        }
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        throw new JsonMappingException(cause.getMessage(), null, cause);
    }
    
    protected void _throwAsIOE(final Exception ex, final Object o) throws IOException {
        if (ex instanceof IllegalArgumentException) {
            String name;
            if (o == null) {
                name = "[NULL]";
            }
            else {
                name = o.getClass().getName();
            }
            final StringBuilder append = new StringBuilder("Problem deserializing property '").append(this.getName());
            append.append("' (expected type: ").append(this.getType());
            append.append("; actual type: ").append(name).append(")");
            final String message = ex.getMessage();
            if (message != null) {
                append.append(", problem: ").append(message);
            }
            else {
                append.append(" (no error message provided)");
            }
            throw new JsonMappingException(append.toString(), null, ex);
        }
        this._throwAsIOE(ex);
    }
    
    public void assignIndex(final int propertyIndex) {
        if (this._propertyIndex != -1) {
            throw new IllegalStateException("Property '" + this.getName() + "' already had index (" + this._propertyIndex + "), trying to assign " + propertyIndex);
        }
        this._propertyIndex = propertyIndex;
    }
    
    public final Object deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (jsonParser.getCurrentToken() == JsonToken.VALUE_NULL) {
            if (this._nullProvider == null) {
                return null;
            }
            return this._nullProvider.nullValue(deserializationContext);
        }
        else {
            if (this._valueTypeDeserializer != null) {
                return this._valueDeserializer.deserializeWithType(jsonParser, deserializationContext, this._valueTypeDeserializer);
            }
            return this._valueDeserializer.deserialize(jsonParser, deserializationContext);
        }
    }
    
    public abstract void deserializeAndSet(final JsonParser p0, final DeserializationContext p1, final Object p2) throws IOException, JsonProcessingException;
    
    public abstract Object deserializeSetAndReturn(final JsonParser p0, final DeserializationContext p1, final Object p2) throws IOException, JsonProcessingException;
    
    public int getCreatorIndex() {
        return -1;
    }
    
    public Object getInjectableValueId() {
        return null;
    }
    
    public String getManagedReferenceName() {
        return this._managedReferenceName;
    }
    
    @Override
    public abstract AnnotatedMember getMember();
    
    @Override
    public final String getName() {
        return this._propName;
    }
    
    @Override
    public JavaType getType() {
        return this._type;
    }
    
    public JsonDeserializer<Object> getValueDeserializer() {
        JsonDeserializer<Object> valueDeserializer;
        if ((valueDeserializer = this._valueDeserializer) == SettableBeanProperty.MISSING_VALUE_DESERIALIZER) {
            valueDeserializer = null;
        }
        return valueDeserializer;
    }
    
    public TypeDeserializer getValueTypeDeserializer() {
        return this._valueTypeDeserializer;
    }
    
    public PropertyName getWrapperName() {
        return this._wrapperName;
    }
    
    public boolean hasValueDeserializer() {
        return this._valueDeserializer != null && this._valueDeserializer != SettableBeanProperty.MISSING_VALUE_DESERIALIZER;
    }
    
    public boolean hasValueTypeDeserializer() {
        return this._valueTypeDeserializer != null;
    }
    
    public boolean hasViews() {
        return this._viewMatcher != null;
    }
    
    public boolean isRequired() {
        return this._isRequired;
    }
    
    public abstract void set(final Object p0, final Object p1) throws IOException;
    
    public abstract Object setAndReturn(final Object p0, final Object p1) throws IOException;
    
    public void setManagedReferenceName(final String managedReferenceName) {
        this._managedReferenceName = managedReferenceName;
    }
    
    public void setViews(final Class<?>[] array) {
        if (array == null) {
            this._viewMatcher = null;
            return;
        }
        this._viewMatcher = ViewMatcher.construct(array);
    }
    
    @Override
    public String toString() {
        return "[property '" + this.getName() + "']";
    }
    
    public boolean visibleInView(final Class<?> clazz) {
        return this._viewMatcher == null || this._viewMatcher.isVisibleForView(clazz);
    }
    
    public abstract SettableBeanProperty withName(final String p0);
    
    public abstract SettableBeanProperty withValueDeserializer(final JsonDeserializer<?> p0);
}
