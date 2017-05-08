// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.databind.cfg.PackageVersion;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import java.lang.annotation.Annotation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.databind.annotation.JsonValueInstantiator;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import java.util.List;
import com.fasterxml.jackson.databind.ser.std.RawSerializer;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import java.io.Serializable;
import com.fasterxml.jackson.databind.AnnotationIntrospector;

public class JacksonAnnotationIntrospector extends AnnotationIntrospector implements Serializable
{
    protected StdTypeResolverBuilder _constructNoTypeResolverBuilder() {
        return StdTypeResolverBuilder.noTypeInfoBuilder();
    }
    
    protected StdTypeResolverBuilder _constructStdTypeResolverBuilder() {
        return new StdTypeResolverBuilder();
    }
    
    protected TypeResolverBuilder<?> _findTypeResolver(final MapperConfig<?> mapperConfig, final Annotated annotated, final JavaType javaType) {
        final TypeIdResolver typeIdResolver = null;
        final JsonTypeInfo jsonTypeInfo = annotated.getAnnotation(JsonTypeInfo.class);
        final JsonTypeResolver jsonTypeResolver = annotated.getAnnotation(JsonTypeResolver.class);
        TypeResolverBuilder typeResolverBuilder;
        if (jsonTypeResolver != null) {
            if (jsonTypeInfo == null) {
                return null;
            }
            typeResolverBuilder = mapperConfig.typeResolverBuilderInstance(annotated, jsonTypeResolver.value());
        }
        else {
            if (jsonTypeInfo == null) {
                return null;
            }
            if (jsonTypeInfo.use() == JsonTypeInfo.Id.NONE) {
                return this._constructNoTypeResolverBuilder();
            }
            typeResolverBuilder = this._constructStdTypeResolverBuilder();
        }
        final JsonTypeIdResolver jsonTypeIdResolver = annotated.getAnnotation(JsonTypeIdResolver.class);
        TypeIdResolver typeIdResolverInstance;
        if (jsonTypeIdResolver == null) {
            typeIdResolverInstance = typeIdResolver;
        }
        else {
            typeIdResolverInstance = mapperConfig.typeIdResolverInstance(annotated, jsonTypeIdResolver.value());
        }
        if (typeIdResolverInstance != null) {
            typeIdResolverInstance.init(javaType);
        }
        final TypeResolverBuilder<TypeResolverBuilder> init = typeResolverBuilder.init(jsonTypeInfo.use(), typeIdResolverInstance);
        final JsonTypeInfo.As include = jsonTypeInfo.include();
        Enum<JsonTypeInfo.As> property;
        if ((property = include) == JsonTypeInfo.As.EXTERNAL_PROPERTY) {
            property = include;
            if (annotated instanceof AnnotatedClass) {
                property = JsonTypeInfo.As.PROPERTY;
            }
        }
        final TypeResolverBuilder<TypeResolverBuilder<?>> typeProperty = init.inclusion((JsonTypeInfo.As)property).typeProperty(jsonTypeInfo.property());
        final Class<?> defaultImpl = jsonTypeInfo.defaultImpl();
        TypeResolverBuilder<TypeResolverBuilder<?>> defaultImpl2 = typeProperty;
        if (defaultImpl != JsonTypeInfo.None.class) {
            defaultImpl2 = typeProperty.defaultImpl(defaultImpl);
        }
        return defaultImpl2.typeIdVisibility(jsonTypeInfo.visible());
    }
    
    protected boolean _isIgnorable(final Annotated annotated) {
        final JsonIgnore jsonIgnore = annotated.getAnnotation(JsonIgnore.class);
        return jsonIgnore != null && jsonIgnore.value();
    }
    
    @Override
    public VisibilityChecker<?> findAutoDetectVisibility(final AnnotatedClass annotatedClass, final VisibilityChecker<?> visibilityChecker) {
        final JsonAutoDetect jsonAutoDetect = annotatedClass.getAnnotation(JsonAutoDetect.class);
        if (jsonAutoDetect == null) {
            return visibilityChecker;
        }
        return (VisibilityChecker<?>)visibilityChecker.with(jsonAutoDetect);
    }
    
    @Override
    public Class<? extends JsonDeserializer<?>> findContentDeserializer(final Annotated annotated) {
        final JsonDeserialize jsonDeserialize = annotated.getAnnotation(JsonDeserialize.class);
        if (jsonDeserialize != null) {
            final Class<? extends JsonDeserializer<?>> contentUsing = jsonDeserialize.contentUsing();
            if (contentUsing != JsonDeserializer.None.class) {
                return contentUsing;
            }
        }
        return null;
    }
    
    @Override
    public Class<? extends JsonSerializer<?>> findContentSerializer(final Annotated annotated) {
        final JsonSerialize jsonSerialize = annotated.getAnnotation(JsonSerialize.class);
        if (jsonSerialize != null) {
            final Class<? extends JsonSerializer<?>> contentUsing = jsonSerialize.contentUsing();
            if (contentUsing != JsonSerializer.None.class) {
                return contentUsing;
            }
        }
        return null;
    }
    
    @Override
    public Object findDeserializationContentConverter(final AnnotatedMember annotatedMember) {
        final JsonDeserialize jsonDeserialize = annotatedMember.getAnnotation(JsonDeserialize.class);
        if (jsonDeserialize != null) {
            final Class<? extends Converter<?, ?>> contentConverter = jsonDeserialize.contentConverter();
            if (contentConverter != Converter.None.class) {
                return contentConverter;
            }
        }
        return null;
    }
    
    @Override
    public Class<?> findDeserializationContentType(final Annotated annotated, final JavaType javaType) {
        final JsonDeserialize jsonDeserialize = annotated.getAnnotation(JsonDeserialize.class);
        if (jsonDeserialize != null) {
            final Class<?> contentAs = jsonDeserialize.contentAs();
            if (contentAs != NoClass.class) {
                return contentAs;
            }
        }
        return null;
    }
    
    @Override
    public Object findDeserializationConverter(final Annotated annotated) {
        final JsonDeserialize jsonDeserialize = annotated.getAnnotation(JsonDeserialize.class);
        if (jsonDeserialize != null) {
            final Class<? extends Converter<?, ?>> converter = jsonDeserialize.converter();
            if (converter != Converter.None.class) {
                return converter;
            }
        }
        return null;
    }
    
    @Override
    public Class<?> findDeserializationKeyType(final Annotated annotated, final JavaType javaType) {
        final JsonDeserialize jsonDeserialize = annotated.getAnnotation(JsonDeserialize.class);
        if (jsonDeserialize != null) {
            final Class<?> keyAs = jsonDeserialize.keyAs();
            if (keyAs != NoClass.class) {
                return keyAs;
            }
        }
        return null;
    }
    
    @Override
    public String findDeserializationName(final AnnotatedField annotatedField) {
        final JsonProperty jsonProperty = annotatedField.getAnnotation(JsonProperty.class);
        if (jsonProperty != null) {
            return jsonProperty.value();
        }
        if (annotatedField.hasAnnotation(JsonDeserialize.class) || annotatedField.hasAnnotation(JsonView.class) || annotatedField.hasAnnotation(JsonBackReference.class) || annotatedField.hasAnnotation(JsonManagedReference.class)) {
            return "";
        }
        return null;
    }
    
    @Override
    public String findDeserializationName(final AnnotatedMethod annotatedMethod) {
        final JsonSetter jsonSetter = annotatedMethod.getAnnotation(JsonSetter.class);
        if (jsonSetter != null) {
            return jsonSetter.value();
        }
        final JsonProperty jsonProperty = annotatedMethod.getAnnotation(JsonProperty.class);
        if (jsonProperty != null) {
            return jsonProperty.value();
        }
        if (annotatedMethod.hasAnnotation(JsonDeserialize.class) || annotatedMethod.hasAnnotation(JsonView.class) || annotatedMethod.hasAnnotation(JsonBackReference.class) || annotatedMethod.hasAnnotation(JsonManagedReference.class)) {
            return "";
        }
        return null;
    }
    
    @Override
    public String findDeserializationName(final AnnotatedParameter annotatedParameter) {
        if (annotatedParameter != null) {
            final JsonProperty jsonProperty = annotatedParameter.getAnnotation(JsonProperty.class);
            if (jsonProperty != null) {
                return jsonProperty.value();
            }
        }
        return null;
    }
    
    @Override
    public Class<?> findDeserializationType(final Annotated annotated, final JavaType javaType) {
        final JsonDeserialize jsonDeserialize = annotated.getAnnotation(JsonDeserialize.class);
        if (jsonDeserialize != null) {
            final Class<?> as = jsonDeserialize.as();
            if (as != NoClass.class) {
                return as;
            }
        }
        return null;
    }
    
    @Override
    public Class<? extends JsonDeserializer<?>> findDeserializer(final Annotated annotated) {
        final JsonDeserialize jsonDeserialize = annotated.getAnnotation(JsonDeserialize.class);
        if (jsonDeserialize != null) {
            final Class<? extends JsonDeserializer<?>> using = jsonDeserialize.using();
            if (using != JsonDeserializer.None.class) {
                return using;
            }
        }
        return null;
    }
    
    @Override
    public Object findFilterId(final AnnotatedClass annotatedClass) {
        final JsonFilter jsonFilter = annotatedClass.getAnnotation(JsonFilter.class);
        if (jsonFilter != null) {
            final String value = jsonFilter.value();
            if (value.length() > 0) {
                return value;
            }
        }
        return null;
    }
    
    @Override
    public JsonFormat.Value findFormat(final Annotated annotated) {
        final JsonFormat jsonFormat = annotated.getAnnotation(JsonFormat.class);
        if (jsonFormat == null) {
            return null;
        }
        return new JsonFormat.Value(jsonFormat);
    }
    
    @Override
    public JsonFormat.Value findFormat(final AnnotatedMember annotatedMember) {
        return this.findFormat(annotatedMember);
    }
    
    @Override
    public Boolean findIgnoreUnknownProperties(final AnnotatedClass annotatedClass) {
        final JsonIgnoreProperties jsonIgnoreProperties = annotatedClass.getAnnotation(JsonIgnoreProperties.class);
        if (jsonIgnoreProperties == null) {
            return null;
        }
        return jsonIgnoreProperties.ignoreUnknown();
    }
    
    @Override
    public Object findInjectableValueId(final AnnotatedMember annotatedMember) {
        final JacksonInject jacksonInject = annotatedMember.getAnnotation(JacksonInject.class);
        Object value;
        if (jacksonInject == null) {
            value = null;
        }
        else if (((String)(value = jacksonInject.value())).length() == 0) {
            if (!(annotatedMember instanceof AnnotatedMethod)) {
                return annotatedMember.getRawType().getName();
            }
            final AnnotatedMethod annotatedMethod = (AnnotatedMethod)annotatedMember;
            if (annotatedMethod.getParameterCount() == 0) {
                return annotatedMember.getRawType().getName();
            }
            return annotatedMethod.getRawParameterType(0).getName();
        }
        return value;
    }
    
    @Override
    public Class<? extends KeyDeserializer> findKeyDeserializer(final Annotated annotated) {
        final JsonDeserialize jsonDeserialize = annotated.getAnnotation(JsonDeserialize.class);
        if (jsonDeserialize != null) {
            final Class<? extends KeyDeserializer> keyUsing = jsonDeserialize.keyUsing();
            if (keyUsing != KeyDeserializer.None.class) {
                return keyUsing;
            }
        }
        return null;
    }
    
    @Override
    public Class<? extends JsonSerializer<?>> findKeySerializer(final Annotated annotated) {
        final JsonSerialize jsonSerialize = annotated.getAnnotation(JsonSerialize.class);
        if (jsonSerialize != null) {
            final Class<? extends JsonSerializer<?>> keyUsing = jsonSerialize.keyUsing();
            if (keyUsing != JsonSerializer.None.class) {
                return keyUsing;
            }
        }
        return null;
    }
    
    @Override
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
    
    @Override
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
    
    @Override
    public Object findNamingStrategy(final AnnotatedClass annotatedClass) {
        final JsonNaming jsonNaming = annotatedClass.getAnnotation(JsonNaming.class);
        if (jsonNaming == null) {
            return null;
        }
        return jsonNaming.value();
    }
    
    @Override
    public ObjectIdInfo findObjectIdInfo(final Annotated annotated) {
        final JsonIdentityInfo jsonIdentityInfo = annotated.getAnnotation(JsonIdentityInfo.class);
        if (jsonIdentityInfo == null || jsonIdentityInfo.generator() == ObjectIdGenerators.None.class) {
            return null;
        }
        return new ObjectIdInfo(jsonIdentityInfo.property(), jsonIdentityInfo.scope(), jsonIdentityInfo.generator());
    }
    
    @Override
    public ObjectIdInfo findObjectReferenceInfo(final Annotated annotated, final ObjectIdInfo objectIdInfo) {
        final JsonIdentityReference jsonIdentityReference = annotated.getAnnotation(JsonIdentityReference.class);
        ObjectIdInfo withAlwaysAsId = objectIdInfo;
        if (jsonIdentityReference != null) {
            withAlwaysAsId = objectIdInfo.withAlwaysAsId(jsonIdentityReference.alwaysAsId());
        }
        return withAlwaysAsId;
    }
    
    @Override
    public Class<?> findPOJOBuilder(final AnnotatedClass annotatedClass) {
        final JsonDeserialize jsonDeserialize = annotatedClass.getAnnotation(JsonDeserialize.class);
        if (jsonDeserialize == null || jsonDeserialize.builder() == NoClass.class) {
            return null;
        }
        return jsonDeserialize.builder();
    }
    
    @Override
    public JsonPOJOBuilder.Value findPOJOBuilderConfig(final AnnotatedClass annotatedClass) {
        final JsonPOJOBuilder jsonPOJOBuilder = annotatedClass.getAnnotation(JsonPOJOBuilder.class);
        if (jsonPOJOBuilder == null) {
            return null;
        }
        return new JsonPOJOBuilder.Value(jsonPOJOBuilder);
    }
    
    @Override
    public String[] findPropertiesToIgnore(final Annotated annotated) {
        final JsonIgnoreProperties jsonIgnoreProperties = annotated.getAnnotation(JsonIgnoreProperties.class);
        if (jsonIgnoreProperties == null) {
            return null;
        }
        return jsonIgnoreProperties.value();
    }
    
    @Override
    public TypeResolverBuilder<?> findPropertyContentTypeResolver(final MapperConfig<?> mapperConfig, final AnnotatedMember annotatedMember, final JavaType javaType) {
        if (!javaType.isContainerType()) {
            throw new IllegalArgumentException("Must call method with a container type (got " + javaType + ")");
        }
        return this._findTypeResolver(mapperConfig, annotatedMember, javaType);
    }
    
    @Override
    public TypeResolverBuilder<?> findPropertyTypeResolver(final MapperConfig<?> mapperConfig, final AnnotatedMember annotatedMember, final JavaType javaType) {
        if (javaType.isContainerType()) {
            return null;
        }
        return this._findTypeResolver(mapperConfig, annotatedMember, javaType);
    }
    
    @Override
    public ReferenceProperty findReferenceType(final AnnotatedMember annotatedMember) {
        final JsonManagedReference jsonManagedReference = annotatedMember.getAnnotation(JsonManagedReference.class);
        if (jsonManagedReference != null) {
            return ReferenceProperty.managed(jsonManagedReference.value());
        }
        final JsonBackReference jsonBackReference = annotatedMember.getAnnotation(JsonBackReference.class);
        if (jsonBackReference != null) {
            return ReferenceProperty.back(jsonBackReference.value());
        }
        return null;
    }
    
    @Override
    public PropertyName findRootName(final AnnotatedClass annotatedClass) {
        final JsonRootName jsonRootName = annotatedClass.getAnnotation(JsonRootName.class);
        if (jsonRootName == null) {
            return null;
        }
        return new PropertyName(jsonRootName.value());
    }
    
    @Override
    public Object findSerializationContentConverter(final AnnotatedMember annotatedMember) {
        final JsonSerialize jsonSerialize = annotatedMember.getAnnotation(JsonSerialize.class);
        if (jsonSerialize != null) {
            final Class<? extends Converter<?, ?>> contentConverter = jsonSerialize.contentConverter();
            if (contentConverter != Converter.None.class) {
                return contentConverter;
            }
        }
        return null;
    }
    
    @Override
    public Class<?> findSerializationContentType(final Annotated annotated, final JavaType javaType) {
        final JsonSerialize jsonSerialize = annotated.getAnnotation(JsonSerialize.class);
        if (jsonSerialize != null) {
            final Class<?> contentAs = jsonSerialize.contentAs();
            if (contentAs != NoClass.class) {
                return contentAs;
            }
        }
        return null;
    }
    
    @Override
    public Object findSerializationConverter(final Annotated annotated) {
        final JsonSerialize jsonSerialize = annotated.getAnnotation(JsonSerialize.class);
        if (jsonSerialize != null) {
            final Class<? extends Converter<?, ?>> converter = jsonSerialize.converter();
            if (converter != Converter.None.class) {
                return converter;
            }
        }
        return null;
    }
    
    @Override
    public JsonInclude.Include findSerializationInclusion(final Annotated annotated, final JsonInclude.Include include) {
        final JsonInclude jsonInclude = annotated.getAnnotation(JsonInclude.class);
        Enum<JsonInclude.Include> value;
        if (jsonInclude != null) {
            value = jsonInclude.value();
        }
        else {
            final JsonSerialize jsonSerialize = annotated.getAnnotation(JsonSerialize.class);
            value = include;
            if (jsonSerialize != null) {
                switch (jsonSerialize.include()) {
                    default: {
                        return include;
                    }
                    case ALWAYS: {
                        return JsonInclude.Include.ALWAYS;
                    }
                    case NON_NULL: {
                        return JsonInclude.Include.NON_NULL;
                    }
                    case NON_DEFAULT: {
                        return JsonInclude.Include.NON_DEFAULT;
                    }
                    case NON_EMPTY: {
                        return JsonInclude.Include.NON_EMPTY;
                    }
                }
            }
        }
        return (JsonInclude.Include)value;
    }
    
    @Override
    public Class<?> findSerializationKeyType(final Annotated annotated, final JavaType javaType) {
        final JsonSerialize jsonSerialize = annotated.getAnnotation(JsonSerialize.class);
        if (jsonSerialize != null) {
            final Class<?> keyAs = jsonSerialize.keyAs();
            if (keyAs != NoClass.class) {
                return keyAs;
            }
        }
        return null;
    }
    
    @Override
    public String findSerializationName(final AnnotatedField annotatedField) {
        final JsonProperty jsonProperty = annotatedField.getAnnotation(JsonProperty.class);
        if (jsonProperty != null) {
            return jsonProperty.value();
        }
        if (annotatedField.hasAnnotation(JsonSerialize.class) || annotatedField.hasAnnotation(JsonView.class)) {
            return "";
        }
        return null;
    }
    
    @Override
    public String findSerializationName(final AnnotatedMethod annotatedMethod) {
        final JsonGetter jsonGetter = annotatedMethod.getAnnotation(JsonGetter.class);
        if (jsonGetter != null) {
            return jsonGetter.value();
        }
        final JsonProperty jsonProperty = annotatedMethod.getAnnotation(JsonProperty.class);
        if (jsonProperty != null) {
            return jsonProperty.value();
        }
        if (annotatedMethod.hasAnnotation(JsonSerialize.class) || annotatedMethod.hasAnnotation(JsonView.class)) {
            return "";
        }
        return null;
    }
    
    @Override
    public String[] findSerializationPropertyOrder(final AnnotatedClass annotatedClass) {
        final JsonPropertyOrder jsonPropertyOrder = annotatedClass.getAnnotation(JsonPropertyOrder.class);
        if (jsonPropertyOrder == null) {
            return null;
        }
        return jsonPropertyOrder.value();
    }
    
    @Override
    public Boolean findSerializationSortAlphabetically(final AnnotatedClass annotatedClass) {
        final JsonPropertyOrder jsonPropertyOrder = annotatedClass.getAnnotation(JsonPropertyOrder.class);
        if (jsonPropertyOrder == null) {
            return null;
        }
        return jsonPropertyOrder.alphabetic();
    }
    
    @Override
    public Class<?> findSerializationType(final Annotated annotated) {
        final JsonSerialize jsonSerialize = annotated.getAnnotation(JsonSerialize.class);
        if (jsonSerialize != null) {
            final Class<?> as = jsonSerialize.as();
            if (as != NoClass.class) {
                return as;
            }
        }
        return null;
    }
    
    @Override
    public JsonSerialize.Typing findSerializationTyping(final Annotated annotated) {
        final JsonSerialize jsonSerialize = annotated.getAnnotation(JsonSerialize.class);
        if (jsonSerialize == null) {
            return null;
        }
        return jsonSerialize.typing();
    }
    
    @Override
    public Object findSerializer(final Annotated annotated) {
        final JsonSerialize jsonSerialize = annotated.getAnnotation(JsonSerialize.class);
        if (jsonSerialize != null) {
            final Class<? extends JsonSerializer<?>> using = jsonSerialize.using();
            if (using != JsonSerializer.None.class) {
                return using;
            }
        }
        final JsonRawValue jsonRawValue = annotated.getAnnotation(JsonRawValue.class);
        if (jsonRawValue != null && jsonRawValue.value()) {
            return new RawSerializer(annotated.getRawType());
        }
        return null;
    }
    
    @Override
    public List<NamedType> findSubtypes(final Annotated annotated) {
        final JsonSubTypes jsonSubTypes = annotated.getAnnotation(JsonSubTypes.class);
        List<NamedType> list;
        if (jsonSubTypes == null) {
            list = null;
        }
        else {
            final JsonSubTypes.Type[] value = jsonSubTypes.value();
            final ArrayList list2 = new ArrayList<NamedType>(value.length);
            final int length = value.length;
            int n = 0;
            while (true) {
                list = (List<NamedType>)list2;
                if (n >= length) {
                    break;
                }
                final JsonSubTypes.Type type = value[n];
                list2.add(new NamedType(type.value(), type.name()));
                ++n;
            }
        }
        return list;
    }
    
    @Override
    public String findTypeName(final AnnotatedClass annotatedClass) {
        final JsonTypeName jsonTypeName = annotatedClass.getAnnotation(JsonTypeName.class);
        if (jsonTypeName == null) {
            return null;
        }
        return jsonTypeName.value();
    }
    
    @Override
    public TypeResolverBuilder<?> findTypeResolver(final MapperConfig<?> mapperConfig, final AnnotatedClass annotatedClass, final JavaType javaType) {
        return this._findTypeResolver(mapperConfig, annotatedClass, javaType);
    }
    
    @Override
    public NameTransformer findUnwrappingNameTransformer(final AnnotatedMember annotatedMember) {
        final JsonUnwrapped jsonUnwrapped = annotatedMember.getAnnotation(JsonUnwrapped.class);
        if (jsonUnwrapped == null || !jsonUnwrapped.enabled()) {
            return null;
        }
        return NameTransformer.simpleTransformer(jsonUnwrapped.prefix(), jsonUnwrapped.suffix());
    }
    
    @Override
    public Object findValueInstantiator(final AnnotatedClass annotatedClass) {
        final JsonValueInstantiator jsonValueInstantiator = annotatedClass.getAnnotation(JsonValueInstantiator.class);
        if (jsonValueInstantiator == null) {
            return null;
        }
        return jsonValueInstantiator.value();
    }
    
    @Override
    public Class<?>[] findViews(final Annotated annotated) {
        final JsonView jsonView = annotated.getAnnotation(JsonView.class);
        if (jsonView == null) {
            return null;
        }
        return jsonView.value();
    }
    
    @Override
    public boolean hasAnyGetterAnnotation(final AnnotatedMethod annotatedMethod) {
        return annotatedMethod.hasAnnotation(JsonAnyGetter.class);
    }
    
    @Override
    public boolean hasAnySetterAnnotation(final AnnotatedMethod annotatedMethod) {
        return annotatedMethod.hasAnnotation(JsonAnySetter.class);
    }
    
    @Override
    public boolean hasAsValueAnnotation(final AnnotatedMethod annotatedMethod) {
        final JsonValue jsonValue = annotatedMethod.getAnnotation(JsonValue.class);
        return jsonValue != null && jsonValue.value();
    }
    
    @Override
    public boolean hasCreatorAnnotation(final Annotated annotated) {
        return annotated.hasAnnotation(JsonCreator.class);
    }
    
    @Override
    public boolean hasIgnoreMarker(final AnnotatedMember annotatedMember) {
        return this._isIgnorable(annotatedMember);
    }
    
    @Override
    public Boolean hasRequiredMarker(final AnnotatedMember annotatedMember) {
        final JsonProperty jsonProperty = annotatedMember.getAnnotation(JsonProperty.class);
        if (jsonProperty != null) {
            return jsonProperty.required();
        }
        return null;
    }
    
    @Override
    public boolean isAnnotationBundle(final Annotation annotation) {
        return annotation.annotationType().getAnnotation(JacksonAnnotationsInside.class) != null;
    }
    
    @Override
    public Boolean isIgnorableType(final AnnotatedClass annotatedClass) {
        final JsonIgnoreType jsonIgnoreType = annotatedClass.getAnnotation(JsonIgnoreType.class);
        if (jsonIgnoreType == null) {
            return null;
        }
        return jsonIgnoreType.value();
    }
    
    @Override
    public Boolean isTypeId(final AnnotatedMember annotatedMember) {
        return annotatedMember.hasAnnotation(JsonTypeId.class);
    }
    
    @Override
    public Version version() {
        return PackageVersion.VERSION;
    }
}
