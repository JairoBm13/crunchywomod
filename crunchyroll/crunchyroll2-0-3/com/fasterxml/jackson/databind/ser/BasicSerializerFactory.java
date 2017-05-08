// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.MapperFeature;
import java.util.RandomAccess;
import java.nio.charset.Charset;
import com.fasterxml.jackson.databind.ser.std.TimeZoneSerializer;
import java.util.TimeZone;
import com.fasterxml.jackson.databind.ser.std.InetAddressSerializer;
import java.net.InetAddress;
import java.lang.reflect.Method;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import java.lang.reflect.Member;
import com.fasterxml.jackson.databind.ser.std.SerializableSerializer;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.ext.OptionalHandlerFactory;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import java.util.Collection;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.EnumMapSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import java.util.EnumMap;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.introspect.BasicBeanDescription;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.ser.impl.StringCollectionSerializer;
import com.fasterxml.jackson.databind.ser.impl.IndexedStringListSerializer;
import com.fasterxml.jackson.databind.ser.std.StdContainerSerializers;
import java.util.EnumSet;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.ser.std.ObjectArraySerializer;
import com.fasterxml.jackson.databind.ser.std.StdArraySerializers;
import com.fasterxml.jackson.databind.ser.impl.StringArraySerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.SerializationConfig;
import java.util.Iterator;
import com.fasterxml.jackson.databind.ser.std.TokenBufferSerializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.ser.std.StdJdkSerializers;
import com.fasterxml.jackson.databind.ser.std.SqlTimeSerializer;
import java.sql.Time;
import com.fasterxml.jackson.databind.ser.std.SqlDateSerializer;
import java.sql.Timestamp;
import java.util.Date;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.ser.std.CalendarSerializer;
import java.util.Calendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.fasterxml.jackson.databind.ser.std.BooleanSerializer;
import java.util.Map;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.JsonSerializer;
import java.util.HashMap;
import java.io.Serializable;

public abstract class BasicSerializerFactory extends SerializerFactory implements Serializable
{
    protected static final HashMap<String, JsonSerializer<?>> _concrete;
    protected static final HashMap<String, Class<? extends JsonSerializer<?>>> _concreteLazy;
    protected final SerializerFactoryConfig _factoryConfig;
    
    static {
        _concrete = new HashMap<String, JsonSerializer<?>>();
        _concreteLazy = new HashMap<String, Class<? extends JsonSerializer<?>>>();
        BasicSerializerFactory._concrete.put(String.class.getName(), new StringSerializer());
        final ToStringSerializer instance = ToStringSerializer.instance;
        BasicSerializerFactory._concrete.put(StringBuffer.class.getName(), instance);
        BasicSerializerFactory._concrete.put(StringBuilder.class.getName(), instance);
        BasicSerializerFactory._concrete.put(Character.class.getName(), instance);
        BasicSerializerFactory._concrete.put(Character.TYPE.getName(), instance);
        NumberSerializers.addAll(BasicSerializerFactory._concrete);
        BasicSerializerFactory._concrete.put(Boolean.TYPE.getName(), new BooleanSerializer(true));
        BasicSerializerFactory._concrete.put(Boolean.class.getName(), new BooleanSerializer(false));
        final NumberSerializers.NumberSerializer numberSerializer = new NumberSerializers.NumberSerializer();
        BasicSerializerFactory._concrete.put(BigInteger.class.getName(), numberSerializer);
        BasicSerializerFactory._concrete.put(BigDecimal.class.getName(), numberSerializer);
        BasicSerializerFactory._concrete.put(Calendar.class.getName(), CalendarSerializer.instance);
        final DateSerializer instance2 = DateSerializer.instance;
        BasicSerializerFactory._concrete.put(Date.class.getName(), instance2);
        BasicSerializerFactory._concrete.put(Timestamp.class.getName(), instance2);
        BasicSerializerFactory._concreteLazy.put(java.sql.Date.class.getName(), SqlDateSerializer.class);
        BasicSerializerFactory._concreteLazy.put(Time.class.getName(), SqlTimeSerializer.class);
        for (final Map.Entry<Class<?>, Object> entry : StdJdkSerializers.all()) {
            final Class<? extends JsonSerializer<?>> value = entry.getValue();
            if (value instanceof JsonSerializer) {
                BasicSerializerFactory._concrete.put(entry.getKey().getName(), (JsonSerializer<?>)value);
            }
            else {
                if (!(value instanceof Class)) {
                    throw new IllegalStateException("Internal error: unrecognized value of type " + entry.getClass().getName());
                }
                BasicSerializerFactory._concreteLazy.put(entry.getKey().getName(), value);
            }
        }
        BasicSerializerFactory._concreteLazy.put(TokenBuffer.class.getName(), TokenBufferSerializer.class);
    }
    
    protected BasicSerializerFactory(final SerializerFactoryConfig serializerFactoryConfig) {
        SerializerFactoryConfig factoryConfig = serializerFactoryConfig;
        if (serializerFactoryConfig == null) {
            factoryConfig = new SerializerFactoryConfig();
        }
        this._factoryConfig = factoryConfig;
    }
    
    protected static <T extends JavaType> T modifySecondaryTypesByAnnotation(final SerializationConfig p0, final Annotated p1, final T p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: invokevirtual   com/fasterxml/jackson/databind/SerializationConfig.getAnnotationIntrospector:()Lcom/fasterxml/jackson/databind/AnnotationIntrospector;
        //     4: astore          4
        //     6: aload_2        
        //     7: astore_3       
        //     8: aload_2        
        //     9: invokevirtual   com/fasterxml/jackson/databind/JavaType.isContainerType:()Z
        //    12: ifeq            103
        //    15: aload           4
        //    17: aload_1        
        //    18: aload_2        
        //    19: invokevirtual   com/fasterxml/jackson/databind/JavaType.getKeyType:()Lcom/fasterxml/jackson/databind/JavaType;
        //    22: invokevirtual   com/fasterxml/jackson/databind/AnnotationIntrospector.findSerializationKeyType:(Lcom/fasterxml/jackson/databind/introspect/Annotated;Lcom/fasterxml/jackson/databind/JavaType;)Ljava/lang/Class;
        //    25: astore_3       
        //    26: aload_2        
        //    27: astore_0       
        //    28: aload_3        
        //    29: ifnull          80
        //    32: aload_2        
        //    33: instanceof      Lcom/fasterxml/jackson/databind/type/MapType;
        //    36: ifne            71
        //    39: new             Ljava/lang/IllegalArgumentException;
        //    42: dup            
        //    43: new             Ljava/lang/StringBuilder;
        //    46: dup            
        //    47: invokespecial   java/lang/StringBuilder.<init>:()V
        //    50: ldc             "Illegal key-type annotation: type "
        //    52: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    55: aload_2        
        //    56: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    59: ldc             " is not a Map type"
        //    61: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    64: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    67: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //    70: athrow         
        //    71: aload_2        
        //    72: checkcast       Lcom/fasterxml/jackson/databind/type/MapType;
        //    75: aload_3        
        //    76: invokevirtual   com/fasterxml/jackson/databind/type/MapType.widenKey:(Ljava/lang/Class;)Lcom/fasterxml/jackson/databind/JavaType;
        //    79: astore_0       
        //    80: aload           4
        //    82: aload_1        
        //    83: aload_0        
        //    84: invokevirtual   com/fasterxml/jackson/databind/JavaType.getContentType:()Lcom/fasterxml/jackson/databind/JavaType;
        //    87: invokevirtual   com/fasterxml/jackson/databind/AnnotationIntrospector.findSerializationContentType:(Lcom/fasterxml/jackson/databind/introspect/Annotated;Lcom/fasterxml/jackson/databind/JavaType;)Ljava/lang/Class;
        //    90: astore_1       
        //    91: aload_0        
        //    92: astore_3       
        //    93: aload_1        
        //    94: ifnull          103
        //    97: aload_0        
        //    98: aload_1        
        //    99: invokevirtual   com/fasterxml/jackson/databind/JavaType.widenContentsBy:(Ljava/lang/Class;)Lcom/fasterxml/jackson/databind/JavaType;
        //   102: astore_3       
        //   103: aload_3        
        //   104: areturn        
        //   105: astore_0       
        //   106: new             Ljava/lang/IllegalArgumentException;
        //   109: dup            
        //   110: new             Ljava/lang/StringBuilder;
        //   113: dup            
        //   114: invokespecial   java/lang/StringBuilder.<init>:()V
        //   117: ldc             "Failed to narrow key type "
        //   119: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   122: aload_2        
        //   123: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   126: ldc             " with key-type annotation ("
        //   128: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   131: aload_3        
        //   132: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //   135: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   138: ldc             "): "
        //   140: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   143: aload_0        
        //   144: invokevirtual   java/lang/IllegalArgumentException.getMessage:()Ljava/lang/String;
        //   147: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   150: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   153: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //   156: athrow         
        //   157: astore_2       
        //   158: new             Ljava/lang/IllegalArgumentException;
        //   161: dup            
        //   162: new             Ljava/lang/StringBuilder;
        //   165: dup            
        //   166: invokespecial   java/lang/StringBuilder.<init>:()V
        //   169: ldc             "Failed to narrow content type "
        //   171: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   174: aload_0        
        //   175: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   178: ldc             " with content-type annotation ("
        //   180: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   183: aload_1        
        //   184: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //   187: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   190: ldc             "): "
        //   192: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   195: aload_2        
        //   196: invokevirtual   java/lang/IllegalArgumentException.getMessage:()Ljava/lang/String;
        //   199: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   202: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   205: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //   208: athrow         
        //    Signature:
        //  <T:Lcom/fasterxml/jackson/databind/JavaType;>(Lcom/fasterxml/jackson/databind/SerializationConfig;Lcom/fasterxml/jackson/databind/introspect/Annotated;TT;)TT;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                
        //  -----  -----  -----  -----  ------------------------------------
        //  71     80     105    157    Ljava/lang/IllegalArgumentException;
        //  97     103    157    209    Ljava/lang/IllegalArgumentException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0103:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    protected JsonSerializer<Object> _findContentSerializer(final SerializerProvider serializerProvider, final Annotated annotated) throws JsonMappingException {
        final Object contentSerializer = serializerProvider.getAnnotationIntrospector().findContentSerializer(annotated);
        if (contentSerializer != null) {
            return serializerProvider.serializerInstance(annotated, contentSerializer);
        }
        return null;
    }
    
    protected JsonSerializer<Object> _findKeySerializer(final SerializerProvider serializerProvider, final Annotated annotated) throws JsonMappingException {
        final Object keySerializer = serializerProvider.getAnnotationIntrospector().findKeySerializer(annotated);
        if (keySerializer != null) {
            return serializerProvider.serializerInstance(annotated, keySerializer);
        }
        return null;
    }
    
    protected JsonSerializer<?> buildArraySerializer(final SerializationConfig serializationConfig, final ArrayType arrayType, final BeanDescription beanDescription, final boolean b, final TypeSerializer typeSerializer, final JsonSerializer<Object> jsonSerializer) throws JsonMappingException {
        JsonSerializer<?> jsonSerializer2 = null;
        final Iterator<Serializers> iterator = this.customSerializers().iterator();
        while (iterator.hasNext()) {
            final JsonSerializer<?> arraySerializer = iterator.next().findArraySerializer(serializationConfig, arrayType, beanDescription, typeSerializer, jsonSerializer);
            if ((jsonSerializer2 = arraySerializer) != null) {
                jsonSerializer2 = arraySerializer;
                break;
            }
        }
        JsonSerializer<?> jsonSerializer3;
        if ((jsonSerializer3 = jsonSerializer2) == null) {
            final Class<?> rawClass = arrayType.getRawClass();
            if (jsonSerializer == null || ClassUtil.isJacksonStdImpl(jsonSerializer)) {
                if (String[].class == rawClass) {
                    jsonSerializer2 = StringArraySerializer.instance;
                }
                else {
                    jsonSerializer2 = StdArraySerializers.findStandardImpl(rawClass);
                }
            }
            if ((jsonSerializer3 = jsonSerializer2) == null) {
                jsonSerializer3 = new ObjectArraySerializer(arrayType.getContentType(), b, typeSerializer, jsonSerializer);
            }
        }
        JsonSerializer<?> jsonSerializer4;
        if (this._factoryConfig.hasSerializerModifiers()) {
            final Iterator<BeanSerializerModifier> iterator2 = this._factoryConfig.serializerModifiers().iterator();
            JsonSerializer<?> modifyArraySerializer = jsonSerializer3;
            while (true) {
                jsonSerializer4 = modifyArraySerializer;
                if (!iterator2.hasNext()) {
                    break;
                }
                modifyArraySerializer = iterator2.next().modifyArraySerializer(serializationConfig, arrayType, beanDescription, modifyArraySerializer);
            }
        }
        else {
            jsonSerializer4 = jsonSerializer3;
        }
        return jsonSerializer4;
    }
    
    protected JsonSerializer<?> buildCollectionSerializer(final SerializationConfig serializationConfig, final CollectionType collectionType, final BeanDescription beanDescription, final boolean b, final TypeSerializer typeSerializer, final JsonSerializer<Object> jsonSerializer) throws JsonMappingException {
        final JavaType javaType = null;
        final Iterator<Serializers> iterator = this.customSerializers().iterator();
        JsonSerializer<?> jsonSerializer2 = null;
        while (iterator.hasNext()) {
            final JsonSerializer<?> collectionSerializer = iterator.next().findCollectionSerializer(serializationConfig, collectionType, beanDescription, typeSerializer, jsonSerializer);
            if ((jsonSerializer2 = collectionSerializer) != null) {
                jsonSerializer2 = collectionSerializer;
                break;
            }
        }
        JsonSerializer<?> jsonSerializer3;
        if ((jsonSerializer3 = jsonSerializer2) == null) {
            final JsonFormat.Value expectedFormat = beanDescription.findExpectedFormat(null);
            if (expectedFormat != null && expectedFormat.getShape() == JsonFormat.Shape.OBJECT) {
                return null;
            }
            final Class<?> rawClass = collectionType.getRawClass();
            if (EnumSet.class.isAssignableFrom(rawClass)) {
                JavaType contentType = collectionType.getContentType();
                if (!contentType.isEnumType()) {
                    contentType = javaType;
                }
                jsonSerializer3 = StdContainerSerializers.enumSetSerializer(contentType);
            }
            else {
                final Class<?> rawClass2 = collectionType.getContentType().getRawClass();
                JsonSerializer<?> jsonSerializer4 = null;
                Label_0252: {
                    if (this.isIndexedList(rawClass)) {
                        if (rawClass2 == String.class) {
                            if (jsonSerializer != null) {
                                jsonSerializer4 = jsonSerializer2;
                                if (!ClassUtil.isJacksonStdImpl(jsonSerializer)) {
                                    break Label_0252;
                                }
                            }
                            jsonSerializer4 = IndexedStringListSerializer.instance;
                        }
                        else {
                            jsonSerializer4 = StdContainerSerializers.indexedListSerializer(collectionType.getContentType(), b, typeSerializer, jsonSerializer);
                        }
                    }
                    else {
                        jsonSerializer4 = jsonSerializer2;
                        if (rawClass2 == String.class) {
                            if (jsonSerializer != null) {
                                jsonSerializer4 = jsonSerializer2;
                                if (!ClassUtil.isJacksonStdImpl(jsonSerializer)) {
                                    break Label_0252;
                                }
                            }
                            jsonSerializer4 = StringCollectionSerializer.instance;
                        }
                    }
                }
                if ((jsonSerializer3 = jsonSerializer4) == null) {
                    jsonSerializer3 = StdContainerSerializers.collectionSerializer(collectionType.getContentType(), b, typeSerializer, jsonSerializer);
                }
            }
        }
        JsonSerializer<?> jsonSerializer5;
        if (this._factoryConfig.hasSerializerModifiers()) {
            final Iterator<BeanSerializerModifier> iterator2 = this._factoryConfig.serializerModifiers().iterator();
            JsonSerializer<?> modifyCollectionSerializer = jsonSerializer3;
            while (true) {
                jsonSerializer5 = modifyCollectionSerializer;
                if (!iterator2.hasNext()) {
                    break;
                }
                modifyCollectionSerializer = iterator2.next().modifyCollectionSerializer(serializationConfig, collectionType, beanDescription, modifyCollectionSerializer);
            }
        }
        else {
            jsonSerializer5 = jsonSerializer3;
        }
        return jsonSerializer5;
    }
    
    protected JsonSerializer<?> buildContainerSerializer(final SerializerProvider serializerProvider, final JavaType javaType, final BeanDescription beanDescription, final boolean b) throws JsonMappingException {
        final SerializationConfig config = serializerProvider.getConfig();
        boolean b2 = b;
        Label_0052: {
            if (!b) {
                b2 = b;
                if (javaType.useStaticType()) {
                    if (javaType.isContainerType()) {
                        b2 = b;
                        if (javaType.getContentType().getRawClass() == Object.class) {
                            break Label_0052;
                        }
                    }
                    b2 = true;
                }
            }
        }
        final TypeSerializer typeSerializer = this.createTypeSerializer(config, javaType.getContentType());
        final boolean b3 = typeSerializer == null && b2;
        final JsonSerializer<Object> findContentSerializer = this._findContentSerializer(serializerProvider, beanDescription.getClassInfo());
        JsonSerializer<?> buildMapSerializer;
        if (javaType.isMapLikeType()) {
            final MapLikeType mapLikeType = (MapLikeType)javaType;
            final JsonSerializer<Object> findKeySerializer = this._findKeySerializer(serializerProvider, beanDescription.getClassInfo());
            if (!mapLikeType.isTrueMapType()) {
                for (final Serializers serializers : this.customSerializers()) {
                    final MapLikeType mapLikeType2 = (MapLikeType)javaType;
                    final JsonSerializer<?> mapLikeSerializer = serializers.findMapLikeSerializer(config, mapLikeType2, beanDescription, findKeySerializer, typeSerializer, findContentSerializer);
                    if (mapLikeSerializer != null) {
                        final JsonSerializer<?> jsonSerializer = mapLikeSerializer;
                        if (!this._factoryConfig.hasSerializerModifiers()) {
                            return jsonSerializer;
                        }
                        final Iterator<BeanSerializerModifier> iterator2 = this._factoryConfig.serializerModifiers().iterator();
                        JsonSerializer<?> modifyMapLikeSerializer = mapLikeSerializer;
                        while (true) {
                            buildMapSerializer = modifyMapLikeSerializer;
                            if (!iterator2.hasNext()) {
                                return buildMapSerializer;
                            }
                            modifyMapLikeSerializer = iterator2.next().modifyMapLikeSerializer(config, mapLikeType2, beanDescription, modifyMapLikeSerializer);
                        }
                    }
                }
                return null;
            }
            buildMapSerializer = this.buildMapSerializer(config, (MapType)mapLikeType, beanDescription, b3, findKeySerializer, typeSerializer, findContentSerializer);
        }
        else if (javaType.isCollectionLikeType()) {
            final CollectionLikeType collectionLikeType = (CollectionLikeType)javaType;
            if (collectionLikeType.isTrueCollectionType()) {
                return this.buildCollectionSerializer(config, (CollectionType)collectionLikeType, beanDescription, b3, typeSerializer, findContentSerializer);
            }
            final CollectionLikeType collectionLikeType2 = (CollectionLikeType)javaType;
            final Iterator<Serializers> iterator3 = this.customSerializers().iterator();
            while (iterator3.hasNext()) {
                final JsonSerializer<?> collectionLikeSerializer = iterator3.next().findCollectionLikeSerializer(config, collectionLikeType2, beanDescription, typeSerializer, findContentSerializer);
                if (collectionLikeSerializer != null) {
                    final JsonSerializer<?> jsonSerializer = collectionLikeSerializer;
                    if (!this._factoryConfig.hasSerializerModifiers()) {
                        return jsonSerializer;
                    }
                    final Iterator<BeanSerializerModifier> iterator4 = this._factoryConfig.serializerModifiers().iterator();
                    JsonSerializer<?> modifyCollectionLikeSerializer = collectionLikeSerializer;
                    while (true) {
                        buildMapSerializer = modifyCollectionLikeSerializer;
                        if (!iterator4.hasNext()) {
                            return buildMapSerializer;
                        }
                        modifyCollectionLikeSerializer = iterator4.next().modifyCollectionLikeSerializer(config, collectionLikeType2, beanDescription, modifyCollectionLikeSerializer);
                    }
                }
            }
            return null;
        }
        else {
            if (javaType.isArrayType()) {
                return this.buildArraySerializer(config, (ArrayType)javaType, beanDescription, b3, typeSerializer, findContentSerializer);
            }
            return null;
        }
        return buildMapSerializer;
    }
    
    protected JsonSerializer<?> buildEnumSerializer(final SerializationConfig serializationConfig, final JavaType javaType, final BeanDescription beanDescription) throws JsonMappingException {
        JsonSerializer<?> jsonSerializer = null;
        final JsonFormat.Value expectedFormat = beanDescription.findExpectedFormat(null);
        if (expectedFormat != null && expectedFormat.getShape() == JsonFormat.Shape.OBJECT) {
            ((BasicBeanDescription)beanDescription).removeProperty("declaringClass");
        }
        else {
            JsonSerializer<?> jsonSerializer2 = EnumSerializer.construct((Class<Enum<?>>)javaType.getRawClass(), serializationConfig, beanDescription, expectedFormat);
            if (!this._factoryConfig.hasSerializerModifiers()) {
                return jsonSerializer2;
            }
            final Iterator<BeanSerializerModifier> iterator = this._factoryConfig.serializerModifiers().iterator();
            while (true) {
                jsonSerializer = jsonSerializer2;
                if (!iterator.hasNext()) {
                    break;
                }
                jsonSerializer2 = iterator.next().modifyEnumSerializer(serializationConfig, javaType, beanDescription, jsonSerializer2);
            }
        }
        return jsonSerializer;
    }
    
    protected JsonSerializer<?> buildIterableSerializer(final SerializationConfig serializationConfig, JavaType javaType, final BeanDescription beanDescription, final boolean b) throws JsonMappingException {
        if ((javaType = javaType.containedType(0)) == null) {
            javaType = TypeFactory.unknownType();
        }
        final TypeSerializer typeSerializer = this.createTypeSerializer(serializationConfig, javaType);
        return StdContainerSerializers.iterableSerializer(javaType, this.usesStaticTyping(serializationConfig, beanDescription, typeSerializer), typeSerializer);
    }
    
    protected JsonSerializer<?> buildIteratorSerializer(final SerializationConfig serializationConfig, JavaType javaType, final BeanDescription beanDescription, final boolean b) throws JsonMappingException {
        if ((javaType = javaType.containedType(0)) == null) {
            javaType = TypeFactory.unknownType();
        }
        final TypeSerializer typeSerializer = this.createTypeSerializer(serializationConfig, javaType);
        return StdContainerSerializers.iteratorSerializer(javaType, this.usesStaticTyping(serializationConfig, beanDescription, typeSerializer), typeSerializer);
    }
    
    protected JsonSerializer<?> buildMapSerializer(final SerializationConfig serializationConfig, final MapType mapType, final BeanDescription beanDescription, final boolean b, final JsonSerializer<Object> jsonSerializer, final TypeSerializer typeSerializer, final JsonSerializer<Object> jsonSerializer2) throws JsonMappingException {
        JsonSerializer<?> jsonSerializer3 = null;
        final Iterator<Serializers> iterator = this.customSerializers().iterator();
        JsonSerializer<?> mapSerializer;
        do {
            mapSerializer = jsonSerializer3;
            if (!iterator.hasNext()) {
                break;
            }
            mapSerializer = iterator.next().findMapSerializer(serializationConfig, mapType, beanDescription, jsonSerializer, typeSerializer, jsonSerializer2);
        } while ((jsonSerializer3 = mapSerializer) == null);
        JsonSerializer<?> construct;
        if ((construct = mapSerializer) == null) {
            if (EnumMap.class.isAssignableFrom(mapType.getRawClass())) {
                final JavaType keyType = mapType.getKeyType();
                EnumValues construct2 = null;
                if (keyType.isEnumType()) {
                    construct2 = EnumValues.construct((Class<Enum<?>>)keyType.getRawClass(), serializationConfig.getAnnotationIntrospector());
                }
                construct = new EnumMapSerializer(mapType.getContentType(), b, construct2, typeSerializer, jsonSerializer2);
            }
            else {
                construct = MapSerializer.construct(serializationConfig.getAnnotationIntrospector().findPropertiesToIgnore(beanDescription.getClassInfo()), mapType, b, typeSerializer, jsonSerializer, jsonSerializer2);
            }
        }
        JsonSerializer<?> jsonSerializer4;
        if (this._factoryConfig.hasSerializerModifiers()) {
            final Iterator<BeanSerializerModifier> iterator2 = this._factoryConfig.serializerModifiers().iterator();
            JsonSerializer<?> modifyMapSerializer = construct;
            while (true) {
                jsonSerializer4 = modifyMapSerializer;
                if (!iterator2.hasNext()) {
                    break;
                }
                modifyMapSerializer = iterator2.next().modifyMapSerializer(serializationConfig, mapType, beanDescription, modifyMapSerializer);
            }
        }
        else {
            jsonSerializer4 = construct;
        }
        return jsonSerializer4;
    }
    
    @Override
    public JsonSerializer<Object> createKeySerializer(final SerializationConfig serializationConfig, final JavaType javaType, JsonSerializer<Object> jsonSerializer) {
        final BeanDescription introspectClassAnnotations = serializationConfig.introspectClassAnnotations(javaType.getRawClass());
        JsonSerializer<?> jsonSerializer2 = null;
        final JsonSerializer<Object> jsonSerializer3 = null;
        if (this._factoryConfig.hasKeySerializers()) {
            final Iterator<Serializers> iterator = this._factoryConfig.keySerializers().iterator();
            jsonSerializer2 = jsonSerializer3;
            while (iterator.hasNext()) {
                final JsonSerializer<?> serializer = iterator.next().findSerializer(serializationConfig, javaType, introspectClassAnnotations);
                if ((jsonSerializer2 = serializer) != null) {
                    jsonSerializer2 = serializer;
                    break;
                }
            }
        }
        if (jsonSerializer2 == null) {
            if ((jsonSerializer2 = jsonSerializer) == null) {
                jsonSerializer2 = StdKeySerializers.getStdKeySerializer(javaType);
            }
        }
        jsonSerializer = jsonSerializer2;
        if (this._factoryConfig.hasSerializerModifiers()) {
            final Iterator<BeanSerializerModifier> iterator2 = this._factoryConfig.serializerModifiers().iterator();
            while (true) {
                jsonSerializer = jsonSerializer2;
                if (!iterator2.hasNext()) {
                    break;
                }
                jsonSerializer2 = iterator2.next().modifyKeySerializer(serializationConfig, javaType, introspectClassAnnotations, jsonSerializer2);
            }
        }
        return (JsonSerializer<Object>)jsonSerializer;
    }
    
    @Override
    public TypeSerializer createTypeSerializer(final SerializationConfig serializationConfig, final JavaType javaType) {
        final AnnotatedClass classInfo = serializationConfig.introspectClassAnnotations(javaType.getRawClass()).getClassInfo();
        final AnnotationIntrospector annotationIntrospector = serializationConfig.getAnnotationIntrospector();
        TypeResolverBuilder<?> typeResolverBuilder = annotationIntrospector.findTypeResolver(serializationConfig, classInfo, javaType);
        Collection<NamedType> collectAndResolveSubtypes;
        if (typeResolverBuilder == null) {
            typeResolverBuilder = serializationConfig.getDefaultTyper(javaType);
            collectAndResolveSubtypes = null;
        }
        else {
            collectAndResolveSubtypes = serializationConfig.getSubtypeResolver().collectAndResolveSubtypes(classInfo, serializationConfig, annotationIntrospector);
        }
        if (typeResolverBuilder == null) {
            return null;
        }
        return typeResolverBuilder.buildTypeSerializer(serializationConfig, javaType, collectAndResolveSubtypes);
    }
    
    protected abstract Iterable<Serializers> customSerializers();
    
    protected Converter<Object, Object> findConverter(final SerializerProvider serializerProvider, final Annotated annotated) throws JsonMappingException {
        final Object serializationConverter = serializerProvider.getAnnotationIntrospector().findSerializationConverter(annotated);
        if (serializationConverter == null) {
            return null;
        }
        return serializerProvider.converterInstance(annotated, serializationConverter);
    }
    
    protected JsonSerializer<?> findConvertingSerializer(final SerializerProvider serializerProvider, final Annotated annotated, final JsonSerializer<?> jsonSerializer) throws JsonMappingException {
        final Converter<Object, Object> converter = this.findConverter(serializerProvider, annotated);
        if (converter == null) {
            return jsonSerializer;
        }
        return new StdDelegatingSerializer(converter, converter.getOutputType(serializerProvider.getTypeFactory()), jsonSerializer);
    }
    
    protected JsonSerializer<?> findOptionalStdSerializer(final SerializerProvider serializerProvider, final JavaType javaType, final BeanDescription beanDescription, final boolean b) throws JsonMappingException {
        return OptionalHandlerFactory.instance.findSerializer(serializerProvider.getConfig(), javaType, beanDescription);
    }
    
    protected final JsonSerializer<?> findSerializerByAddonType(final SerializationConfig serializationConfig, final JavaType javaType, final BeanDescription beanDescription, final boolean b) throws JsonMappingException {
        final Class<?> rawClass = javaType.getRawClass();
        if (Iterator.class.isAssignableFrom(rawClass)) {
            return this.buildIteratorSerializer(serializationConfig, javaType, beanDescription, b);
        }
        if (Iterable.class.isAssignableFrom(rawClass)) {
            return this.buildIterableSerializer(serializationConfig, javaType, beanDescription, b);
        }
        if (CharSequence.class.isAssignableFrom(rawClass)) {
            return ToStringSerializer.instance;
        }
        return null;
    }
    
    protected final JsonSerializer<?> findSerializerByAnnotations(final SerializerProvider serializerProvider, final JavaType javaType, final BeanDescription beanDescription) throws JsonMappingException {
        if (JsonSerializable.class.isAssignableFrom(javaType.getRawClass())) {
            return SerializableSerializer.instance;
        }
        final AnnotatedMethod jsonValueMethod = beanDescription.findJsonValueMethod();
        if (jsonValueMethod != null) {
            final Method annotated = jsonValueMethod.getAnnotated();
            if (serializerProvider.canOverrideAccessModifiers()) {
                ClassUtil.checkAndFixAccess(annotated);
            }
            return new JsonValueSerializer(annotated, this.findSerializerFromAnnotation(serializerProvider, jsonValueMethod));
        }
        return null;
    }
    
    protected final JsonSerializer<?> findSerializerByLookup(final JavaType javaType, final SerializationConfig serializationConfig, BeanDescription name, final boolean b) {
        name = (BeanDescription)javaType.getRawClass().getName();
        JsonSerializer<?> jsonSerializer2;
        final JsonSerializer<?> jsonSerializer = jsonSerializer2 = BasicSerializerFactory._concrete.get(name);
        if (jsonSerializer != null) {
            return jsonSerializer2;
        }
        name = (BeanDescription)BasicSerializerFactory._concreteLazy.get(name);
        jsonSerializer2 = jsonSerializer;
        if (name == null) {
            return jsonSerializer2;
        }
        try {
            jsonSerializer2 = ((Class<JsonSerializer<?>>)name).newInstance();
            return jsonSerializer2;
        }
        catch (Exception ex) {
            throw new IllegalStateException("Failed to instantiate standard serializer (of type " + ((Class)name).getName() + "): " + ex.getMessage(), ex);
        }
    }
    
    protected final JsonSerializer<?> findSerializerByPrimaryType(final SerializerProvider serializerProvider, final JavaType javaType, final BeanDescription beanDescription, final boolean b) throws JsonMappingException {
        final Class<?> rawClass = javaType.getRawClass();
        JsonSerializer<?> jsonSerializer;
        if (InetAddress.class.isAssignableFrom(rawClass)) {
            jsonSerializer = InetAddressSerializer.instance;
        }
        else {
            if (TimeZone.class.isAssignableFrom(rawClass)) {
                return TimeZoneSerializer.instance;
            }
            if (Charset.class.isAssignableFrom(rawClass)) {
                return ToStringSerializer.instance;
            }
            if ((jsonSerializer = this.findOptionalStdSerializer(serializerProvider, javaType, beanDescription, b)) == null) {
                if (Number.class.isAssignableFrom(rawClass)) {
                    return NumberSerializers.NumberSerializer.instance;
                }
                if (Enum.class.isAssignableFrom(rawClass)) {
                    return this.buildEnumSerializer(serializerProvider.getConfig(), javaType, beanDescription);
                }
                if (Calendar.class.isAssignableFrom(rawClass)) {
                    return CalendarSerializer.instance;
                }
                if (Date.class.isAssignableFrom(rawClass)) {
                    return DateSerializer.instance;
                }
                return null;
            }
        }
        return jsonSerializer;
    }
    
    protected JsonSerializer<Object> findSerializerFromAnnotation(final SerializerProvider serializerProvider, final Annotated annotated) throws JsonMappingException {
        final Object serializer = serializerProvider.getAnnotationIntrospector().findSerializer(annotated);
        if (serializer == null) {
            return null;
        }
        return (JsonSerializer<Object>)this.findConvertingSerializer(serializerProvider, annotated, serializerProvider.serializerInstance(annotated, serializer));
    }
    
    protected boolean isIndexedList(final Class<?> clazz) {
        return RandomAccess.class.isAssignableFrom(clazz);
    }
    
    protected <T extends JavaType> T modifyTypeByAnnotation(final SerializationConfig serializationConfig, final Annotated annotated, final T t) {
        final Class<?> serializationType = serializationConfig.getAnnotationIntrospector().findSerializationType(annotated);
        JavaType widenBy = t;
        Label_0026: {
            if (serializationType == null) {
                break Label_0026;
            }
            try {
                widenBy = t.widenBy(serializationType);
                return (T)modifySecondaryTypesByAnnotation(serializationConfig, annotated, widenBy);
            }
            catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Failed to widen type " + t + " with concrete-type annotation (value " + serializationType.getName() + "), method '" + annotated.getName() + "': " + ex.getMessage());
            }
        }
    }
    
    protected boolean usesStaticTyping(final SerializationConfig serializationConfig, final BeanDescription beanDescription, final TypeSerializer typeSerializer) {
        if (typeSerializer == null) {
            final JsonSerialize.Typing serializationTyping = serializationConfig.getAnnotationIntrospector().findSerializationTyping(beanDescription.getClassInfo());
            if (serializationTyping == null) {
                return serializationConfig.isEnabled(MapperFeature.USE_STATIC_TYPING);
            }
            if (serializationTyping == JsonSerialize.Typing.STATIC) {
                return true;
            }
        }
        return false;
    }
}
