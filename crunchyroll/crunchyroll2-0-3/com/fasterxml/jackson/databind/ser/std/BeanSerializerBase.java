// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.ser.ContainerSerializer;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.impl.PropertyBasedObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.lang.reflect.Type;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import java.util.HashSet;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.AnyGetterWriter;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;

public abstract class BeanSerializerBase extends StdSerializer<Object> implements JsonFormatVisitable, SchemaAware, ContextualSerializer, ResolvableSerializer
{
    protected static final BeanPropertyWriter[] NO_PROPS;
    protected final AnyGetterWriter _anyGetterWriter;
    protected final BeanPropertyWriter[] _filteredProps;
    protected final ObjectIdWriter _objectIdWriter;
    protected final Object _propertyFilterId;
    protected final BeanPropertyWriter[] _props;
    protected final JsonFormat.Shape _serializationShape;
    protected final AnnotatedMember _typeId;
    
    static {
        NO_PROPS = new BeanPropertyWriter[0];
    }
    
    protected BeanSerializerBase(final JavaType javaType, final BeanSerializerBuilder beanSerializerBuilder, final BeanPropertyWriter[] props, final BeanPropertyWriter[] filteredProps) {
        final JsonFormat.Shape shape = null;
        super(javaType);
        this._props = props;
        this._filteredProps = filteredProps;
        if (beanSerializerBuilder == null) {
            this._typeId = null;
            this._anyGetterWriter = null;
            this._propertyFilterId = null;
            this._objectIdWriter = null;
            this._serializationShape = null;
            return;
        }
        this._typeId = beanSerializerBuilder.getTypeId();
        this._anyGetterWriter = beanSerializerBuilder.getAnyGetter();
        this._propertyFilterId = beanSerializerBuilder.getFilterId();
        this._objectIdWriter = beanSerializerBuilder.getObjectIdWriter();
        final JsonFormat.Value expectedFormat = beanSerializerBuilder.getBeanDescription().findExpectedFormat(null);
        JsonFormat.Shape shape2;
        if (expectedFormat == null) {
            shape2 = shape;
        }
        else {
            shape2 = expectedFormat.getShape();
        }
        this._serializationShape = shape2;
    }
    
    protected BeanSerializerBase(final BeanSerializerBase beanSerializerBase, final ObjectIdWriter objectIdWriter) {
        super(beanSerializerBase._handledType);
        this._props = beanSerializerBase._props;
        this._filteredProps = beanSerializerBase._filteredProps;
        this._typeId = beanSerializerBase._typeId;
        this._anyGetterWriter = beanSerializerBase._anyGetterWriter;
        this._objectIdWriter = objectIdWriter;
        this._propertyFilterId = beanSerializerBase._propertyFilterId;
        this._serializationShape = beanSerializerBase._serializationShape;
    }
    
    protected BeanSerializerBase(final BeanSerializerBase beanSerializerBase, final NameTransformer nameTransformer) {
        this(beanSerializerBase, rename(beanSerializerBase._props, nameTransformer), rename(beanSerializerBase._filteredProps, nameTransformer));
    }
    
    public BeanSerializerBase(final BeanSerializerBase beanSerializerBase, final BeanPropertyWriter[] props, final BeanPropertyWriter[] filteredProps) {
        super(beanSerializerBase._handledType);
        this._props = props;
        this._filteredProps = filteredProps;
        this._typeId = beanSerializerBase._typeId;
        this._anyGetterWriter = beanSerializerBase._anyGetterWriter;
        this._objectIdWriter = beanSerializerBase._objectIdWriter;
        this._propertyFilterId = beanSerializerBase._propertyFilterId;
        this._serializationShape = beanSerializerBase._serializationShape;
    }
    
    protected BeanSerializerBase(final BeanSerializerBase beanSerializerBase, final String[] array) {
        final BeanPropertyWriter[] array2 = null;
        super(beanSerializerBase._handledType);
        final HashSet<String> arrayToSet = ArrayBuilders.arrayToSet(array);
        final BeanPropertyWriter[] props = beanSerializerBase._props;
        final BeanPropertyWriter[] filteredProps = beanSerializerBase._filteredProps;
        final int length = props.length;
        final ArrayList list = new ArrayList<BeanPropertyWriter>(length);
        ArrayList<BeanPropertyWriter> list2;
        if (filteredProps == null) {
            list2 = null;
        }
        else {
            list2 = new ArrayList<BeanPropertyWriter>(length);
        }
        for (int i = 0; i < length; ++i) {
            final BeanPropertyWriter beanPropertyWriter = props[i];
            if (!arrayToSet.contains(beanPropertyWriter.getName())) {
                list.add(beanPropertyWriter);
                if (filteredProps != null) {
                    list2.add(filteredProps[i]);
                }
            }
        }
        this._props = list.toArray(new BeanPropertyWriter[list.size()]);
        BeanPropertyWriter[] filteredProps2;
        if (list2 == null) {
            filteredProps2 = array2;
        }
        else {
            filteredProps2 = list2.toArray(new BeanPropertyWriter[list2.size()]);
        }
        this._filteredProps = filteredProps2;
        this._typeId = beanSerializerBase._typeId;
        this._anyGetterWriter = beanSerializerBase._anyGetterWriter;
        this._objectIdWriter = beanSerializerBase._objectIdWriter;
        this._propertyFilterId = beanSerializerBase._propertyFilterId;
        this._serializationShape = beanSerializerBase._serializationShape;
    }
    
    private final String _customTypeId(Object value) {
        value = this._typeId.getValue(value);
        if (value == null) {
            return "";
        }
        if (value instanceof String) {
            return (String)value;
        }
        return value.toString();
    }
    
    private static final BeanPropertyWriter[] rename(final BeanPropertyWriter[] array, final NameTransformer nameTransformer) {
        if (array == null || array.length == 0 || nameTransformer == null || nameTransformer == NameTransformer.NOP) {
            return array;
        }
        final int length = array.length;
        final BeanPropertyWriter[] array2 = new BeanPropertyWriter[length];
        for (int i = 0; i < length; ++i) {
            final BeanPropertyWriter beanPropertyWriter = array[i];
            if (beanPropertyWriter != null) {
                array2[i] = beanPropertyWriter.rename(nameTransformer);
            }
        }
        return array2;
    }
    
    protected final void _serializeWithObjectId(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonGenerationException {
        final ObjectIdWriter objectIdWriter = this._objectIdWriter;
        final WritableObjectId objectId = serializerProvider.findObjectId(o, objectIdWriter.generator);
        if (objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter)) {
            return;
        }
        final Object generateId = objectId.generateId(o);
        if (objectIdWriter.alwaysAsId) {
            objectIdWriter.serializer.serialize(generateId, jsonGenerator, serializerProvider);
            return;
        }
        String customTypeId;
        if (this._typeId == null) {
            customTypeId = null;
        }
        else {
            customTypeId = this._customTypeId(o);
        }
        if (customTypeId == null) {
            typeSerializer.writeTypePrefixForObject(o, jsonGenerator);
        }
        else {
            typeSerializer.writeCustomTypePrefixForObject(o, jsonGenerator, customTypeId);
        }
        objectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        if (this._propertyFilterId != null) {
            this.serializeFieldsFiltered(o, jsonGenerator, serializerProvider);
        }
        else {
            this.serializeFields(o, jsonGenerator, serializerProvider);
        }
        if (customTypeId == null) {
            typeSerializer.writeTypeSuffixForObject(o, jsonGenerator);
            return;
        }
        typeSerializer.writeCustomTypeSuffixForObject(o, jsonGenerator, customTypeId);
    }
    
    protected final void _serializeWithObjectId(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final boolean b) throws IOException, JsonGenerationException {
        final ObjectIdWriter objectIdWriter = this._objectIdWriter;
        final WritableObjectId objectId = serializerProvider.findObjectId(o, objectIdWriter.generator);
        if (!objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter)) {
            final Object generateId = objectId.generateId(o);
            if (objectIdWriter.alwaysAsId) {
                objectIdWriter.serializer.serialize(generateId, jsonGenerator, serializerProvider);
                return;
            }
            if (b) {
                jsonGenerator.writeStartObject();
            }
            objectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
            if (this._propertyFilterId != null) {
                this.serializeFieldsFiltered(o, jsonGenerator, serializerProvider);
            }
            else {
                this.serializeFields(o, jsonGenerator, serializerProvider);
            }
            if (b) {
                jsonGenerator.writeEndObject();
            }
        }
    }
    
    protected abstract BeanSerializerBase asArraySerializer();
    
    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider, final BeanProperty beanProperty) throws JsonMappingException {
        final JsonFormat.Shape shape = null;
        ObjectIdWriter objectIdWriter = this._objectIdWriter;
        final AnnotationIntrospector annotationIntrospector = serializerProvider.getAnnotationIntrospector();
        Annotated member;
        if (beanProperty == null || annotationIntrospector == null) {
            member = null;
        }
        else {
            member = beanProperty.getMember();
        }
        String[] propertiesToIgnore = null;
        Label_0094: {
            if (member != null) {
                propertiesToIgnore = annotationIntrospector.findPropertiesToIgnore(member);
                final ObjectIdInfo objectIdInfo = annotationIntrospector.findObjectIdInfo(member);
                if (objectIdInfo == null) {
                    if (objectIdWriter != null) {
                        objectIdWriter = this._objectIdWriter.withAlwaysAsId(annotationIntrospector.findObjectReferenceInfo(member, new ObjectIdInfo("", null, null)).getAlwaysAsId());
                    }
                }
                else {
                    final ObjectIdInfo objectReferenceInfo = annotationIntrospector.findObjectReferenceInfo(member, objectIdInfo);
                    final Class<? extends ObjectIdGenerator<?>> generatorType = objectReferenceInfo.getGeneratorType();
                    final JavaType javaType = serializerProvider.getTypeFactory().findTypeParameters(serializerProvider.constructType(generatorType), ObjectIdGenerator.class)[0];
                    if (generatorType == ObjectIdGenerators.PropertyGenerator.class) {
                        final String propertyName = objectReferenceInfo.getPropertyName();
                        for (int length = this._props.length, i = 0; i != length; ++i) {
                            final BeanPropertyWriter beanPropertyWriter = this._props[i];
                            if (propertyName.equals(beanPropertyWriter.getName())) {
                                if (i > 0) {
                                    System.arraycopy(this._props, 0, this._props, 1, i);
                                    this._props[0] = beanPropertyWriter;
                                    if (this._filteredProps != null) {
                                        final BeanPropertyWriter beanPropertyWriter2 = this._filteredProps[i];
                                        System.arraycopy(this._filteredProps, 0, this._filteredProps, 1, i);
                                        this._filteredProps[0] = beanPropertyWriter2;
                                    }
                                }
                                objectIdWriter = ObjectIdWriter.construct(beanPropertyWriter.getType(), null, new PropertyBasedObjectIdGenerator(objectReferenceInfo, beanPropertyWriter), objectReferenceInfo.getAlwaysAsId());
                                break Label_0094;
                            }
                        }
                        throw new IllegalArgumentException("Invalid Object Id definition for " + this._handledType.getName() + ": can not find property with name '" + propertyName + "'");
                    }
                    objectIdWriter = ObjectIdWriter.construct(javaType, objectReferenceInfo.getPropertyName(), serializerProvider.objectIdGeneratorInstance(member, objectReferenceInfo), objectReferenceInfo.getAlwaysAsId());
                }
            }
            else {
                propertiesToIgnore = null;
            }
        }
        while (true) {
            Label_0497: {
                if (objectIdWriter == null) {
                    break Label_0497;
                }
                final ObjectIdWriter withSerializer = objectIdWriter.withSerializer(serializerProvider.findValueSerializer(objectIdWriter.idType, beanProperty));
                if (withSerializer == this._objectIdWriter) {
                    break Label_0497;
                }
                final BeanSerializerBase withObjectIdWriter = this.withObjectIdWriter(withSerializer);
                BeanSerializerBase withIgnorals = withObjectIdWriter;
                if (propertiesToIgnore != null) {
                    withIgnorals = withObjectIdWriter;
                    if (propertiesToIgnore.length != 0) {
                        withIgnorals = withObjectIdWriter.withIgnorals(propertiesToIgnore);
                    }
                }
                JsonFormat.Shape shape2 = shape;
                if (member != null) {
                    final JsonFormat.Value format = annotationIntrospector.findFormat(member);
                    shape2 = shape;
                    if (format != null) {
                        shape2 = format.getShape();
                    }
                }
                JsonFormat.Shape serializationShape;
                if ((serializationShape = shape2) == null) {
                    serializationShape = this._serializationShape;
                }
                if (serializationShape == JsonFormat.Shape.ARRAY) {
                    return withIgnorals.asArraySerializer();
                }
                return withIgnorals;
            }
            final BeanSerializerBase withObjectIdWriter = this;
            continue;
        }
    }
    
    protected JsonSerializer<Object> findConvertingSerializer(final SerializerProvider serializerProvider, final BeanPropertyWriter beanPropertyWriter) throws JsonMappingException {
        final AnnotationIntrospector annotationIntrospector = serializerProvider.getAnnotationIntrospector();
        if (annotationIntrospector != null) {
            final Object serializationConverter = annotationIntrospector.findSerializationConverter(beanPropertyWriter.getMember());
            if (serializationConverter != null) {
                final Converter<Object, Object> converterInstance = serializerProvider.converterInstance(beanPropertyWriter.getMember(), serializationConverter);
                final JavaType outputType = converterInstance.getOutputType(serializerProvider.getTypeFactory());
                return new StdDelegatingSerializer(converterInstance, outputType, serializerProvider.findValueSerializer(outputType, beanPropertyWriter));
            }
        }
        return null;
    }
    
    protected BeanPropertyFilter findFilter(final SerializerProvider serializerProvider) throws JsonMappingException {
        final Object propertyFilterId = this._propertyFilterId;
        final FilterProvider filterProvider = serializerProvider.getFilterProvider();
        if (filterProvider == null) {
            throw new JsonMappingException("Can not resolve BeanPropertyFilter with id '" + propertyFilterId + "'; no FilterProvider configured");
        }
        return filterProvider.findFilter(propertyFilterId);
    }
    
    @Override
    public void resolve(final SerializerProvider serializerProvider) throws JsonMappingException {
        int length;
        if (this._filteredProps == null) {
            length = 0;
        }
        else {
            length = this._filteredProps.length;
        }
        for (int length2 = this._props.length, i = 0; i < length2; ++i) {
            final BeanPropertyWriter beanPropertyWriter = this._props[i];
            if (!beanPropertyWriter.willSuppressNulls() && !beanPropertyWriter.hasNullSerializer()) {
                final JsonSerializer<Object> nullValueSerializer = serializerProvider.findNullValueSerializer(beanPropertyWriter);
                if (nullValueSerializer != null) {
                    beanPropertyWriter.assignNullSerializer(nullValueSerializer);
                    if (i < length) {
                        final BeanPropertyWriter beanPropertyWriter2 = this._filteredProps[i];
                        if (beanPropertyWriter2 != null) {
                            beanPropertyWriter2.assignNullSerializer(nullValueSerializer);
                        }
                    }
                }
            }
            if (!beanPropertyWriter.hasSerializer()) {
                JsonFormatVisitable jsonFormatVisitable;
                if ((jsonFormatVisitable = this.findConvertingSerializer(serializerProvider, beanPropertyWriter)) == null) {
                    JavaType javaType;
                    if ((javaType = beanPropertyWriter.getSerializationType()) == null) {
                        final JavaType nonTrivialBaseType = javaType = serializerProvider.constructType(beanPropertyWriter.getGenericPropertyType());
                        if (!nonTrivialBaseType.isFinal()) {
                            if (nonTrivialBaseType.isContainerType() || nonTrivialBaseType.containedTypeCount() > 0) {
                                beanPropertyWriter.setNonTrivialBaseType(nonTrivialBaseType);
                            }
                            continue;
                        }
                    }
                    final JsonSerializer<Object> jsonSerializer = (JsonSerializer<Object>)(jsonFormatVisitable = serializerProvider.findValueSerializer(javaType, beanPropertyWriter));
                    if (javaType.isContainerType()) {
                        final TypeSerializer typeSerializer = javaType.getContentType().getTypeHandler();
                        jsonFormatVisitable = jsonSerializer;
                        if (typeSerializer != null) {
                            jsonFormatVisitable = jsonSerializer;
                            if (jsonSerializer instanceof ContainerSerializer) {
                                jsonFormatVisitable = ((ContainerSerializer<Object>)jsonSerializer).withValueTypeSerializer(typeSerializer);
                            }
                        }
                    }
                }
                beanPropertyWriter.assignSerializer((JsonSerializer<Object>)jsonFormatVisitable);
                if (i < length) {
                    final BeanPropertyWriter beanPropertyWriter3 = this._filteredProps[i];
                    if (beanPropertyWriter3 != null) {
                        beanPropertyWriter3.assignSerializer((JsonSerializer<Object>)jsonFormatVisitable);
                    }
                }
            }
        }
        if (this._anyGetterWriter != null) {
            this._anyGetterWriter.resolve(serializerProvider);
        }
    }
    
    protected void serializeFields(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        Label_0078: {
            if (this._filteredProps == null || serializerProvider.getActiveView() == null) {
                break Label_0078;
            }
            BeanPropertyWriter[] array = this._filteredProps;
            while (true) {
                int n = 0;
                int n2 = 0;
                int i = 0;
                try {
                    while (i < array.length) {
                        final BeanPropertyWriter beanPropertyWriter = array[i];
                        if (beanPropertyWriter != null) {
                            n = i;
                            n2 = i;
                            beanPropertyWriter.serializeAsField(o, jsonGenerator, serializerProvider);
                        }
                        ++i;
                    }
                    n = i;
                    n2 = i;
                    if (this._anyGetterWriter != null) {
                        n = i;
                        n2 = i;
                        this._anyGetterWriter.getAndSerialize(o, jsonGenerator, serializerProvider);
                    }
                    return;
                    array = this._props;
                }
                catch (Exception ex) {
                    String name;
                    if (n == array.length) {
                        name = "[anySetter]";
                    }
                    else {
                        name = array[n].getName();
                    }
                    this.wrapAndThrow(serializerProvider, ex, o, name);
                }
                catch (StackOverflowError stackOverflowError) {
                    final JsonMappingException ex2 = new JsonMappingException("Infinite recursion (StackOverflowError)", stackOverflowError);
                    String name2;
                    if (n2 == array.length) {
                        name2 = "[anySetter]";
                    }
                    else {
                        name2 = array[n2].getName();
                    }
                    ex2.prependPath(new JsonMappingException.Reference(o, name2));
                    throw ex2;
                }
            }
        }
    }
    
    protected void serializeFieldsFiltered(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        BeanPropertyWriter[] array;
        if (this._filteredProps != null && serializerProvider.getActiveView() != null) {
            array = this._filteredProps;
        }
        else {
            array = this._props;
        }
        final BeanPropertyFilter filter = this.findFilter(serializerProvider);
        if (filter == null) {
            this.serializeFields(o, jsonGenerator, serializerProvider);
        }
        else {
            while (true) {
                int n = 0;
                int n2 = 0;
                int n3 = 0;
                while (true) {
                    try {
                        final int length = array.length;
                        if (n3 < length) {
                            final BeanPropertyWriter beanPropertyWriter = array[n3];
                            if (beanPropertyWriter != null) {
                                n = n3;
                                n2 = n3;
                                filter.serializeAsField(o, jsonGenerator, serializerProvider, beanPropertyWriter);
                            }
                        }
                        else {
                            n = n3;
                            n2 = n3;
                            if (this._anyGetterWriter != null) {
                                n = n3;
                                n2 = n3;
                                this._anyGetterWriter.getAndSerialize(o, jsonGenerator, serializerProvider);
                                return;
                            }
                            break;
                        }
                    }
                    catch (Exception ex) {
                        String name;
                        if (n == array.length) {
                            name = "[anySetter]";
                        }
                        else {
                            name = array[n].getName();
                        }
                        this.wrapAndThrow(serializerProvider, ex, o, name);
                        return;
                    }
                    catch (StackOverflowError stackOverflowError) {
                        final JsonMappingException ex2 = new JsonMappingException("Infinite recursion (StackOverflowError)", stackOverflowError);
                        String name2;
                        if (n2 == array.length) {
                            name2 = "[anySetter]";
                        }
                        else {
                            name2 = array[n2].getName();
                        }
                        ex2.prependPath(new JsonMappingException.Reference(o, name2));
                        throw ex2;
                    }
                    ++n3;
                    continue;
                }
            }
        }
    }
    
    @Override
    public void serializeWithType(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonGenerationException {
        if (this._objectIdWriter != null) {
            this._serializeWithObjectId(o, jsonGenerator, serializerProvider, typeSerializer);
            return;
        }
        String customTypeId;
        if (this._typeId == null) {
            customTypeId = null;
        }
        else {
            customTypeId = this._customTypeId(o);
        }
        if (customTypeId == null) {
            typeSerializer.writeTypePrefixForObject(o, jsonGenerator);
        }
        else {
            typeSerializer.writeCustomTypePrefixForObject(o, jsonGenerator, customTypeId);
        }
        if (this._propertyFilterId != null) {
            this.serializeFieldsFiltered(o, jsonGenerator, serializerProvider);
        }
        else {
            this.serializeFields(o, jsonGenerator, serializerProvider);
        }
        if (customTypeId == null) {
            typeSerializer.writeTypeSuffixForObject(o, jsonGenerator);
            return;
        }
        typeSerializer.writeCustomTypeSuffixForObject(o, jsonGenerator, customTypeId);
    }
    
    @Override
    public boolean usesObjectId() {
        return this._objectIdWriter != null;
    }
    
    protected abstract BeanSerializerBase withIgnorals(final String[] p0);
    
    public abstract BeanSerializerBase withObjectIdWriter(final ObjectIdWriter p0);
}
