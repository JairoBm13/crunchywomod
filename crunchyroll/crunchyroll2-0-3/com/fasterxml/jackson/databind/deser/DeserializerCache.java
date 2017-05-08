// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.NoClass;
import java.util.HashMap;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import java.util.concurrent.ConcurrentHashMap;
import java.io.Serializable;

public final class DeserializerCache implements Serializable
{
    protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _cachedDeserializers;
    protected final HashMap<JavaType, JsonDeserializer<Object>> _incompleteDeserializers;
    
    public DeserializerCache() {
        this._cachedDeserializers = new ConcurrentHashMap<JavaType, JsonDeserializer<Object>>(64, 0.75f, 2);
        this._incompleteDeserializers = new HashMap<JavaType, JsonDeserializer<Object>>(8);
    }
    
    private Class<?> _verifyAsClass(final Object o, final String s, final Class<?> clazz) {
        Class<?> clazz2;
        if (o == null) {
            clazz2 = null;
        }
        else {
            if (!(o instanceof Class)) {
                throw new IllegalStateException("AnnotationIntrospector." + s + "() returned value of type " + o.getClass().getName() + ": expected type JsonSerializer or Class<JsonSerializer> instead");
            }
            final Class clazz3 = (Class)o;
            if (clazz3 == clazz || (clazz2 = (Class<?>)clazz3) == NoClass.class) {
                return null;
            }
        }
        return clazz2;
    }
    
    private JavaType modifyTypeByAnnotation(final DeserializationContext p0, final Annotated p1, final JavaType p2) throws JsonMappingException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_1        
        //     1: invokevirtual   com/fasterxml/jackson/databind/DeserializationContext.getAnnotationIntrospector:()Lcom/fasterxml/jackson/databind/AnnotationIntrospector;
        //     4: astore          5
        //     6: aload           5
        //     8: aload_2        
        //     9: aload_3        
        //    10: invokevirtual   com/fasterxml/jackson/databind/AnnotationIntrospector.findDeserializationType:(Lcom/fasterxml/jackson/databind/introspect/Annotated;Lcom/fasterxml/jackson/databind/JavaType;)Ljava/lang/Class;
        //    13: astore          6
        //    15: aload           6
        //    17: ifnull          492
        //    20: aload_3        
        //    21: aload           6
        //    23: invokevirtual   com/fasterxml/jackson/databind/JavaType.narrowBy:(Ljava/lang/Class;)Lcom/fasterxml/jackson/databind/JavaType;
        //    26: astore          4
        //    28: aload           4
        //    30: astore_3       
        //    31: aload_3        
        //    32: astore          4
        //    34: aload_3        
        //    35: invokevirtual   com/fasterxml/jackson/databind/JavaType.isContainerType:()Z
        //    38: ifeq            337
        //    41: aload           5
        //    43: aload_2        
        //    44: aload_3        
        //    45: invokevirtual   com/fasterxml/jackson/databind/JavaType.getKeyType:()Lcom/fasterxml/jackson/databind/JavaType;
        //    48: invokevirtual   com/fasterxml/jackson/databind/AnnotationIntrospector.findDeserializationKeyType:(Lcom/fasterxml/jackson/databind/introspect/Annotated;Lcom/fasterxml/jackson/databind/JavaType;)Ljava/lang/Class;
        //    51: astore          6
        //    53: aload           6
        //    55: ifnull          486
        //    58: aload_3        
        //    59: instanceof      Lcom/fasterxml/jackson/databind/type/MapLikeType;
        //    62: ifne            164
        //    65: new             Lcom/fasterxml/jackson/databind/JsonMappingException;
        //    68: dup            
        //    69: new             Ljava/lang/StringBuilder;
        //    72: dup            
        //    73: invokespecial   java/lang/StringBuilder.<init>:()V
        //    76: ldc             "Illegal key-type annotation: type "
        //    78: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    81: aload_3        
        //    82: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    85: ldc             " is not a Map(-like) type"
        //    87: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    90: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    93: invokespecial   com/fasterxml/jackson/databind/JsonMappingException.<init>:(Ljava/lang/String;)V
        //    96: athrow         
        //    97: astore_1       
        //    98: new             Lcom/fasterxml/jackson/databind/JsonMappingException;
        //   101: dup            
        //   102: new             Ljava/lang/StringBuilder;
        //   105: dup            
        //   106: invokespecial   java/lang/StringBuilder.<init>:()V
        //   109: ldc             "Failed to narrow type "
        //   111: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   114: aload_3        
        //   115: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   118: ldc             " with concrete-type annotation (value "
        //   120: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   123: aload           6
        //   125: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //   128: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   131: ldc             "), method '"
        //   133: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   136: aload_2        
        //   137: invokevirtual   com/fasterxml/jackson/databind/introspect/Annotated.getName:()Ljava/lang/String;
        //   140: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   143: ldc             "': "
        //   145: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   148: aload_1        
        //   149: invokevirtual   java/lang/IllegalArgumentException.getMessage:()Ljava/lang/String;
        //   152: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   155: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   158: aconst_null    
        //   159: aload_1        
        //   160: invokespecial   com/fasterxml/jackson/databind/JsonMappingException.<init>:(Ljava/lang/String;Lcom/fasterxml/jackson/core/JsonLocation;Ljava/lang/Throwable;)V
        //   163: athrow         
        //   164: aload_3        
        //   165: checkcast       Lcom/fasterxml/jackson/databind/type/MapLikeType;
        //   168: aload           6
        //   170: invokevirtual   com/fasterxml/jackson/databind/type/MapLikeType.narrowKey:(Ljava/lang/Class;)Lcom/fasterxml/jackson/databind/JavaType;
        //   173: astore          4
        //   175: aload           4
        //   177: invokevirtual   com/fasterxml/jackson/databind/JavaType.getKeyType:()Lcom/fasterxml/jackson/databind/JavaType;
        //   180: astore          6
        //   182: aload           4
        //   184: astore_3       
        //   185: aload           6
        //   187: ifnull          250
        //   190: aload           4
        //   192: astore_3       
        //   193: aload           6
        //   195: invokevirtual   com/fasterxml/jackson/databind/JavaType.getValueHandler:()Ljava/lang/Object;
        //   198: ifnonnull       250
        //   201: aload           5
        //   203: aload_2        
        //   204: invokevirtual   com/fasterxml/jackson/databind/AnnotationIntrospector.findKeyDeserializer:(Lcom/fasterxml/jackson/databind/introspect/Annotated;)Ljava/lang/Object;
        //   207: astore          6
        //   209: aload           4
        //   211: astore_3       
        //   212: aload           6
        //   214: ifnull          250
        //   217: aload_1        
        //   218: aload_2        
        //   219: aload           6
        //   221: invokevirtual   com/fasterxml/jackson/databind/DeserializationContext.keyDeserializerInstance:(Lcom/fasterxml/jackson/databind/introspect/Annotated;Ljava/lang/Object;)Lcom/fasterxml/jackson/databind/KeyDeserializer;
        //   224: astore          6
        //   226: aload           4
        //   228: astore_3       
        //   229: aload           6
        //   231: ifnull          250
        //   234: aload           4
        //   236: checkcast       Lcom/fasterxml/jackson/databind/type/MapLikeType;
        //   239: aload           6
        //   241: invokevirtual   com/fasterxml/jackson/databind/type/MapLikeType.withKeyValueHandler:(Ljava/lang/Object;)Lcom/fasterxml/jackson/databind/type/MapLikeType;
        //   244: astore_3       
        //   245: aload_3        
        //   246: invokevirtual   com/fasterxml/jackson/databind/JavaType.getKeyType:()Lcom/fasterxml/jackson/databind/JavaType;
        //   249: pop            
        //   250: aload           5
        //   252: aload_2        
        //   253: aload_3        
        //   254: invokevirtual   com/fasterxml/jackson/databind/JavaType.getContentType:()Lcom/fasterxml/jackson/databind/JavaType;
        //   257: invokevirtual   com/fasterxml/jackson/databind/AnnotationIntrospector.findDeserializationContentType:(Lcom/fasterxml/jackson/databind/introspect/Annotated;Lcom/fasterxml/jackson/databind/JavaType;)Ljava/lang/Class;
        //   260: astore          6
        //   262: aload           6
        //   264: ifnull          483
        //   267: aload_3        
        //   268: aload           6
        //   270: invokevirtual   com/fasterxml/jackson/databind/JavaType.narrowContentsBy:(Ljava/lang/Class;)Lcom/fasterxml/jackson/databind/JavaType;
        //   273: astore          4
        //   275: aload           4
        //   277: astore_3       
        //   278: aload_3        
        //   279: astore          4
        //   281: aload_3        
        //   282: invokevirtual   com/fasterxml/jackson/databind/JavaType.getContentType:()Lcom/fasterxml/jackson/databind/JavaType;
        //   285: invokevirtual   com/fasterxml/jackson/databind/JavaType.getValueHandler:()Ljava/lang/Object;
        //   288: ifnonnull       337
        //   291: aload           5
        //   293: aload_2        
        //   294: invokevirtual   com/fasterxml/jackson/databind/AnnotationIntrospector.findContentDeserializer:(Lcom/fasterxml/jackson/databind/introspect/Annotated;)Ljava/lang/Object;
        //   297: astore          5
        //   299: aload_3        
        //   300: astore          4
        //   302: aload           5
        //   304: ifnull          337
        //   307: aload           5
        //   309: instanceof      Lcom/fasterxml/jackson/databind/JsonDeserializer;
        //   312: ifeq            450
        //   315: aload           5
        //   317: checkcast       Lcom/fasterxml/jackson/databind/JsonDeserializer;
        //   320: astore_1       
        //   321: aconst_null    
        //   322: astore_1       
        //   323: aload_3        
        //   324: astore          4
        //   326: aload_1        
        //   327: ifnull          337
        //   330: aload_3        
        //   331: aload_1        
        //   332: invokevirtual   com/fasterxml/jackson/databind/JavaType.withContentValueHandler:(Ljava/lang/Object;)Lcom/fasterxml/jackson/databind/JavaType;
        //   335: astore          4
        //   337: aload           4
        //   339: areturn        
        //   340: astore_1       
        //   341: new             Lcom/fasterxml/jackson/databind/JsonMappingException;
        //   344: dup            
        //   345: new             Ljava/lang/StringBuilder;
        //   348: dup            
        //   349: invokespecial   java/lang/StringBuilder.<init>:()V
        //   352: ldc             "Failed to narrow key type "
        //   354: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   357: aload_3        
        //   358: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   361: ldc             " with key-type annotation ("
        //   363: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   366: aload           6
        //   368: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //   371: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   374: ldc             "): "
        //   376: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   379: aload_1        
        //   380: invokevirtual   java/lang/IllegalArgumentException.getMessage:()Ljava/lang/String;
        //   383: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   386: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   389: aconst_null    
        //   390: aload_1        
        //   391: invokespecial   com/fasterxml/jackson/databind/JsonMappingException.<init>:(Ljava/lang/String;Lcom/fasterxml/jackson/core/JsonLocation;Ljava/lang/Throwable;)V
        //   394: athrow         
        //   395: astore_1       
        //   396: new             Lcom/fasterxml/jackson/databind/JsonMappingException;
        //   399: dup            
        //   400: new             Ljava/lang/StringBuilder;
        //   403: dup            
        //   404: invokespecial   java/lang/StringBuilder.<init>:()V
        //   407: ldc             "Failed to narrow content type "
        //   409: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   412: aload_3        
        //   413: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   416: ldc             " with content-type annotation ("
        //   418: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   421: aload           6
        //   423: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //   426: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   429: ldc             "): "
        //   431: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   434: aload_1        
        //   435: invokevirtual   java/lang/IllegalArgumentException.getMessage:()Ljava/lang/String;
        //   438: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   441: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   444: aconst_null    
        //   445: aload_1        
        //   446: invokespecial   com/fasterxml/jackson/databind/JsonMappingException.<init>:(Ljava/lang/String;Lcom/fasterxml/jackson/core/JsonLocation;Ljava/lang/Throwable;)V
        //   449: athrow         
        //   450: aload_0        
        //   451: aload           5
        //   453: ldc             "findContentDeserializer"
        //   455: ldc             Lcom/fasterxml/jackson/databind/JsonDeserializer$None;.class
        //   457: invokespecial   com/fasterxml/jackson/databind/deser/DeserializerCache._verifyAsClass:(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Class;
        //   460: astore          4
        //   462: aload           4
        //   464: ifnull          478
        //   467: aload_1        
        //   468: aload_2        
        //   469: aload           4
        //   471: invokevirtual   com/fasterxml/jackson/databind/DeserializationContext.deserializerInstance:(Lcom/fasterxml/jackson/databind/introspect/Annotated;Ljava/lang/Object;)Lcom/fasterxml/jackson/databind/JsonDeserializer;
        //   474: astore_1       
        //   475: goto            323
        //   478: aconst_null    
        //   479: astore_1       
        //   480: goto            323
        //   483: goto            278
        //   486: aload_3        
        //   487: astore          4
        //   489: goto            175
        //   492: goto            31
        //    Exceptions:
        //  throws com.fasterxml.jackson.databind.JsonMappingException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                
        //  -----  -----  -----  -----  ------------------------------------
        //  20     28     97     164    Ljava/lang/IllegalArgumentException;
        //  164    175    340    395    Ljava/lang/IllegalArgumentException;
        //  267    275    395    450    Ljava/lang/IllegalArgumentException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0278:
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
    
    protected JsonDeserializer<Object> _createAndCache2(final DeserializationContext deserializationContext, final DeserializerFactory deserializerFactory, final JavaType javaType) throws JsonMappingException {
        JsonDeserializer<Object> createDeserializer;
        while (true) {
            try {
                createDeserializer = this._createDeserializer(deserializationContext, deserializerFactory, javaType);
                if (createDeserializer == null) {
                    return null;
                }
            }
            catch (IllegalArgumentException ex) {
                throw new JsonMappingException(ex.getMessage(), null, ex);
            }
            final boolean b = createDeserializer instanceof ResolvableDeserializer;
            final boolean cachable = createDeserializer.isCachable();
            if (b) {
                this._incompleteDeserializers.put(javaType, createDeserializer);
                ((ResolvableDeserializer)createDeserializer).resolve(deserializationContext);
                this._incompleteDeserializers.remove(javaType);
            }
            final JsonDeserializer<Object> jsonDeserializer = createDeserializer;
            if (cachable) {
                break;
            }
            return jsonDeserializer;
        }
        this._cachedDeserializers.put(javaType, createDeserializer);
        return createDeserializer;
    }
    
    protected JsonDeserializer<Object> _createAndCacheValueDeserializer(final DeserializationContext deserializationContext, final DeserializerFactory deserializerFactory, final JavaType javaType) throws JsonMappingException {
        final int size;
        synchronized (this._incompleteDeserializers) {
            final JsonDeserializer<Object> findCachedDeserializer = this._findCachedDeserializer(javaType);
            if (findCachedDeserializer != null) {
                return findCachedDeserializer;
            }
            size = this._incompleteDeserializers.size();
            if (size > 0) {
                final JsonDeserializer<Object> jsonDeserializer = this._incompleteDeserializers.get(javaType);
                if (jsonDeserializer != null) {
                    return jsonDeserializer;
                }
            }
        }
        try {
            final DeserializationContext deserializationContext2;
            final JsonDeserializer<Object> createAndCache2 = this._createAndCache2(deserializationContext2, deserializerFactory, javaType);
            if (size == 0 && this._incompleteDeserializers.size() > 0) {
                this._incompleteDeserializers.clear();
            }
            // monitorexit(hashMap)
            return createAndCache2;
        }
        finally {
            if (size == 0 && this._incompleteDeserializers.size() > 0) {
                this._incompleteDeserializers.clear();
            }
        }
    }
    
    protected JsonDeserializer<Object> _createDeserializer(final DeserializationContext deserializationContext, final DeserializerFactory deserializerFactory, final JavaType javaType) throws JsonMappingException {
        final DeserializationConfig config = deserializationContext.getConfig();
        JavaType mapAbstractType = null;
        Label_0039: {
            if (!javaType.isAbstract() && !javaType.isMapLikeType()) {
                mapAbstractType = javaType;
                if (!javaType.isCollectionLikeType()) {
                    break Label_0039;
                }
            }
            mapAbstractType = deserializerFactory.mapAbstractType(config, javaType);
        }
        BeanDescription beanDescription = config.introspect(mapAbstractType);
        final JsonDeserializer<Object> deserializerFromAnnotation = this.findDeserializerFromAnnotation(deserializationContext, beanDescription.getClassInfo());
        if (deserializerFromAnnotation != null) {
            return deserializerFromAnnotation;
        }
        final JavaType modifyTypeByAnnotation = this.modifyTypeByAnnotation(deserializationContext, beanDescription.getClassInfo(), mapAbstractType);
        JavaType javaType2;
        if (modifyTypeByAnnotation != (javaType2 = mapAbstractType)) {
            beanDescription = config.introspect(modifyTypeByAnnotation);
            javaType2 = modifyTypeByAnnotation;
        }
        final Class<?> pojoBuilder = beanDescription.findPOJOBuilder();
        if (pojoBuilder != null) {
            return deserializerFactory.createBuilderBasedDeserializer(deserializationContext, javaType2, beanDescription, pojoBuilder);
        }
        final Converter<Object, Object> deserializationConverter = beanDescription.findDeserializationConverter();
        if (deserializationConverter == null) {
            return (JsonDeserializer<Object>)this._createDeserializer2(deserializationContext, deserializerFactory, javaType2, beanDescription);
        }
        final JavaType inputType = deserializationConverter.getInputType(deserializationContext.getTypeFactory());
        if (!inputType.hasRawClass(javaType2.getRawClass())) {
            beanDescription = config.introspect(inputType);
        }
        return new StdDelegatingDeserializer<Object>(deserializationConverter, inputType, this._createDeserializer2(deserializationContext, deserializerFactory, inputType, beanDescription));
    }
    
    protected JsonDeserializer<?> _createDeserializer2(final DeserializationContext deserializationContext, final DeserializerFactory deserializerFactory, final JavaType javaType, final BeanDescription beanDescription) throws JsonMappingException {
        final DeserializationConfig config = deserializationContext.getConfig();
        if (javaType.isEnumType()) {
            return deserializerFactory.createEnumDeserializer(deserializationContext, javaType, beanDescription);
        }
        if (javaType.isContainerType()) {
            if (javaType.isArrayType()) {
                return deserializerFactory.createArrayDeserializer(deserializationContext, (ArrayType)javaType, beanDescription);
            }
            if (javaType.isMapLikeType()) {
                final MapLikeType mapLikeType = (MapLikeType)javaType;
                if (mapLikeType.isTrueMapType()) {
                    return deserializerFactory.createMapDeserializer(deserializationContext, (MapType)mapLikeType, beanDescription);
                }
                return deserializerFactory.createMapLikeDeserializer(deserializationContext, mapLikeType, beanDescription);
            }
            else if (javaType.isCollectionLikeType()) {
                final JsonFormat.Value expectedFormat = beanDescription.findExpectedFormat(null);
                if (expectedFormat == null || expectedFormat.getShape() != JsonFormat.Shape.OBJECT) {
                    final CollectionLikeType collectionLikeType = (CollectionLikeType)javaType;
                    if (collectionLikeType.isTrueCollectionType()) {
                        return deserializerFactory.createCollectionDeserializer(deserializationContext, (CollectionType)collectionLikeType, beanDescription);
                    }
                    return deserializerFactory.createCollectionLikeDeserializer(deserializationContext, collectionLikeType, beanDescription);
                }
            }
        }
        if (JsonNode.class.isAssignableFrom(javaType.getRawClass())) {
            return deserializerFactory.createTreeDeserializer(config, javaType, beanDescription);
        }
        return deserializerFactory.createBeanDeserializer(deserializationContext, javaType, beanDescription);
    }
    
    protected JsonDeserializer<Object> _findCachedDeserializer(final JavaType javaType) {
        if (javaType == null) {
            throw new IllegalArgumentException("Null JavaType passed");
        }
        return this._cachedDeserializers.get(javaType);
    }
    
    protected KeyDeserializer _handleUnknownKeyDeserializer(final JavaType javaType) throws JsonMappingException {
        throw new JsonMappingException("Can not find a (Map) Key deserializer for type " + javaType);
    }
    
    protected JsonDeserializer<Object> _handleUnknownValueDeserializer(final JavaType javaType) throws JsonMappingException {
        if (!ClassUtil.isConcrete(javaType.getRawClass())) {
            throw new JsonMappingException("Can not find a Value deserializer for abstract type " + javaType);
        }
        throw new JsonMappingException("Can not find a Value deserializer for type " + javaType);
    }
    
    protected Converter<Object, Object> findConverter(final DeserializationContext deserializationContext, final Annotated annotated) throws JsonMappingException {
        final Object deserializationConverter = deserializationContext.getAnnotationIntrospector().findDeserializationConverter(annotated);
        if (deserializationConverter == null) {
            return null;
        }
        return deserializationContext.converterInstance(annotated, deserializationConverter);
    }
    
    protected JsonDeserializer<Object> findConvertingDeserializer(final DeserializationContext deserializationContext, final Annotated annotated, final JsonDeserializer<Object> jsonDeserializer) throws JsonMappingException {
        final Converter<Object, Object> converter = this.findConverter(deserializationContext, annotated);
        if (converter == null) {
            return jsonDeserializer;
        }
        return new StdDelegatingDeserializer<Object>(converter, converter.getInputType(deserializationContext.getTypeFactory()), jsonDeserializer);
    }
    
    protected JsonDeserializer<Object> findDeserializerFromAnnotation(final DeserializationContext deserializationContext, final Annotated annotated) throws JsonMappingException {
        final Object deserializer = deserializationContext.getAnnotationIntrospector().findDeserializer(annotated);
        if (deserializer == null) {
            return null;
        }
        return this.findConvertingDeserializer(deserializationContext, annotated, deserializationContext.deserializerInstance(annotated, deserializer));
    }
    
    public KeyDeserializer findKeyDeserializer(final DeserializationContext deserializationContext, final DeserializerFactory deserializerFactory, final JavaType javaType) throws JsonMappingException {
        final KeyDeserializer keyDeserializer = deserializerFactory.createKeyDeserializer(deserializationContext, javaType);
        KeyDeserializer handleUnknownKeyDeserializer;
        if (keyDeserializer == null) {
            handleUnknownKeyDeserializer = this._handleUnknownKeyDeserializer(javaType);
        }
        else {
            handleUnknownKeyDeserializer = keyDeserializer;
            if (keyDeserializer instanceof ResolvableDeserializer) {
                ((ResolvableDeserializer)keyDeserializer).resolve(deserializationContext);
                return keyDeserializer;
            }
        }
        return handleUnknownKeyDeserializer;
    }
    
    public JsonDeserializer<Object> findValueDeserializer(final DeserializationContext deserializationContext, final DeserializerFactory deserializerFactory, final JavaType javaType) throws JsonMappingException {
        final JsonDeserializer<Object> findCachedDeserializer = this._findCachedDeserializer(javaType);
        JsonDeserializer<Object> createAndCacheValueDeserializer;
        if (findCachedDeserializer != null) {
            createAndCacheValueDeserializer = findCachedDeserializer;
        }
        else if ((createAndCacheValueDeserializer = this._createAndCacheValueDeserializer(deserializationContext, deserializerFactory, javaType)) == null) {
            return this._handleUnknownValueDeserializer(javaType);
        }
        return createAndCacheValueDeserializer;
    }
}
