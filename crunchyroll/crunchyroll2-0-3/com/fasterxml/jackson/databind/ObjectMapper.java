// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import java.util.Collection;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import java.io.Closeable;
import com.fasterxml.jackson.databind.cfg.PackageVersion;
import com.fasterxml.jackson.core.Version;
import java.lang.reflect.Type;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import java.util.Map;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import java.text.DateFormat;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.core.Base64Variants;
import java.util.TimeZone;
import java.util.Locale;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BasicClassIntrospector;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.RootNameLookup;
import java.util.concurrent.ConcurrentHashMap;
import com.fasterxml.jackson.databind.type.ClassKey;
import java.util.HashMap;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.cfg.BaseSettings;
import java.io.Serializable;
import com.fasterxml.jackson.core.Versioned;
import com.fasterxml.jackson.core.ObjectCodec;

public class ObjectMapper extends ObjectCodec implements Versioned, Serializable
{
    protected static final AnnotationIntrospector DEFAULT_ANNOTATION_INTROSPECTOR;
    protected static final BaseSettings DEFAULT_BASE;
    protected static final ClassIntrospector DEFAULT_INTROSPECTOR;
    private static final JavaType JSON_NODE_TYPE;
    protected static final VisibilityChecker<?> STD_VISIBILITY_CHECKER;
    protected static final PrettyPrinter _defaultPrettyPrinter;
    protected DeserializationConfig _deserializationConfig;
    protected DefaultDeserializationContext _deserializationContext;
    protected InjectableValues _injectableValues;
    protected final JsonFactory _jsonFactory;
    protected final HashMap<ClassKey, Class<?>> _mixInAnnotations;
    protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _rootDeserializers;
    protected final RootNameLookup _rootNames;
    protected SerializationConfig _serializationConfig;
    protected SerializerFactory _serializerFactory;
    protected DefaultSerializerProvider _serializerProvider;
    protected SubtypeResolver _subtypeResolver;
    protected TypeFactory _typeFactory;
    
    static {
        JSON_NODE_TYPE = SimpleType.constructUnsafe(JsonNode.class);
        DEFAULT_INTROSPECTOR = BasicClassIntrospector.instance;
        DEFAULT_ANNOTATION_INTROSPECTOR = new JacksonAnnotationIntrospector();
        STD_VISIBILITY_CHECKER = VisibilityChecker.Std.defaultInstance();
        _defaultPrettyPrinter = new DefaultPrettyPrinter();
        DEFAULT_BASE = new BaseSettings(ObjectMapper.DEFAULT_INTROSPECTOR, ObjectMapper.DEFAULT_ANNOTATION_INTROSPECTOR, ObjectMapper.STD_VISIBILITY_CHECKER, null, TypeFactory.defaultInstance(), null, StdDateFormat.instance, null, Locale.getDefault(), TimeZone.getTimeZone("GMT"), Base64Variants.getDefaultVariant());
    }
    
    public ObjectMapper() {
        this(null, null, null);
    }
    
    public ObjectMapper(final JsonFactory jsonFactory) {
        this(jsonFactory, null, null);
    }
    
    public ObjectMapper(final JsonFactory jsonFactory, final DefaultSerializerProvider defaultSerializerProvider, final DefaultDeserializationContext defaultDeserializationContext) {
        this._mixInAnnotations = new HashMap<ClassKey, Class<?>>();
        this._rootDeserializers = new ConcurrentHashMap<JavaType, JsonDeserializer<Object>>(64, 0.6f, 2);
        if (jsonFactory == null) {
            this._jsonFactory = new MappingJsonFactory(this);
        }
        else {
            this._jsonFactory = jsonFactory;
            if (jsonFactory.getCodec() == null) {
                this._jsonFactory.setCodec(this);
            }
        }
        this._subtypeResolver = new StdSubtypeResolver();
        this._rootNames = new RootNameLookup();
        this._typeFactory = TypeFactory.defaultInstance();
        this._serializationConfig = new SerializationConfig(ObjectMapper.DEFAULT_BASE, this._subtypeResolver, this._mixInAnnotations);
        this._deserializationConfig = new DeserializationConfig(ObjectMapper.DEFAULT_BASE, this._subtypeResolver, this._mixInAnnotations);
        DefaultSerializerProvider serializerProvider;
        if ((serializerProvider = defaultSerializerProvider) == null) {
            serializerProvider = new DefaultSerializerProvider.Impl();
        }
        this._serializerProvider = serializerProvider;
        DefaultDeserializationContext deserializationContext;
        if ((deserializationContext = defaultDeserializationContext) == null) {
            deserializationContext = new DefaultDeserializationContext.Impl(BeanDeserializerFactory.instance);
        }
        this._deserializationContext = deserializationContext;
        this._serializerFactory = BeanSerializerFactory.instance;
    }
    
    private final void _writeCloseableValue(final JsonGenerator p0, final Object p1, final SerializationConfig p2) throws IOException, JsonGenerationException, JsonMappingException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_2        
        //     1: checkcast       Ljava/io/Closeable;
        //     4: astore          4
        //     6: aload_0        
        //     7: aload_3        
        //     8: invokevirtual   com/fasterxml/jackson/databind/ObjectMapper._serializerProvider:(Lcom/fasterxml/jackson/databind/SerializationConfig;)Lcom/fasterxml/jackson/databind/ser/DefaultSerializerProvider;
        //    11: aload_1        
        //    12: aload_2        
        //    13: invokevirtual   com/fasterxml/jackson/databind/ser/DefaultSerializerProvider.serializeValue:(Lcom/fasterxml/jackson/core/JsonGenerator;Ljava/lang/Object;)V
        //    16: aload_3        
        //    17: getstatic       com/fasterxml/jackson/databind/SerializationFeature.FLUSH_AFTER_WRITE_VALUE:Lcom/fasterxml/jackson/databind/SerializationFeature;
        //    20: invokevirtual   com/fasterxml/jackson/databind/SerializationConfig.isEnabled:(Lcom/fasterxml/jackson/databind/SerializationFeature;)Z
        //    23: ifeq            30
        //    26: aload_1        
        //    27: invokevirtual   com/fasterxml/jackson/core/JsonGenerator.flush:()V
        //    30: aconst_null    
        //    31: astore_3       
        //    32: aload           4
        //    34: invokeinterface java/io/Closeable.close:()V
        //    39: iconst_0       
        //    40: ifeq            51
        //    43: new             Ljava/lang/NullPointerException;
        //    46: dup            
        //    47: invokespecial   java/lang/NullPointerException.<init>:()V
        //    50: athrow         
        //    51: return         
        //    52: astore_2       
        //    53: aload           4
        //    55: astore_1       
        //    56: aload_1        
        //    57: astore_3       
        //    58: aload_2        
        //    59: astore_1       
        //    60: aload_3        
        //    61: ifnull          70
        //    64: aload_3        
        //    65: invokeinterface java/io/Closeable.close:()V
        //    70: aload_1        
        //    71: athrow         
        //    72: astore_1       
        //    73: return         
        //    74: astore_2       
        //    75: goto            70
        //    78: astore_1       
        //    79: goto            60
        //    Exceptions:
        //  throws java.io.IOException
        //  throws com.fasterxml.jackson.core.JsonGenerationException
        //  throws com.fasterxml.jackson.databind.JsonMappingException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  6      30     52     60     Any
        //  32     39     78     82     Any
        //  43     51     72     74     Ljava/io/IOException;
        //  64     70     74     78     Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0051:
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
    
    protected JsonDeserializer<Object> _findRootDeserializer(final DeserializationContext deserializationContext, final JavaType javaType) throws JsonMappingException {
        final JsonDeserializer<Object> jsonDeserializer = this._rootDeserializers.get(javaType);
        if (jsonDeserializer != null) {
            return jsonDeserializer;
        }
        final JsonDeserializer<Object> rootValueDeserializer = deserializationContext.findRootValueDeserializer(javaType);
        if (rootValueDeserializer == null) {
            throw new JsonMappingException("Can not find a deserializer for type " + javaType);
        }
        this._rootDeserializers.put(javaType, rootValueDeserializer);
        return rootValueDeserializer;
    }
    
    protected JsonToken _initForReading(final JsonParser jsonParser) throws IOException, JsonParseException, JsonMappingException {
        JsonToken jsonToken;
        if ((jsonToken = jsonParser.getCurrentToken()) == null && (jsonToken = jsonParser.nextToken()) == null) {
            throw JsonMappingException.from(jsonParser, "No content to map due to end-of-input");
        }
        return jsonToken;
    }
    
    protected Object _readMapAndClose(final JsonParser p0, final JavaType p1) throws IOException, JsonParseException, JsonMappingException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: aload_1        
        //     2: invokevirtual   com/fasterxml/jackson/databind/ObjectMapper._initForReading:(Lcom/fasterxml/jackson/core/JsonParser;)Lcom/fasterxml/jackson/core/JsonToken;
        //     5: astore_3       
        //     6: aload_3        
        //     7: getstatic       com/fasterxml/jackson/core/JsonToken.VALUE_NULL:Lcom/fasterxml/jackson/core/JsonToken;
        //    10: if_acmpne       41
        //    13: aload_0        
        //    14: aload_0        
        //    15: aload_1        
        //    16: aload_0        
        //    17: invokevirtual   com/fasterxml/jackson/databind/ObjectMapper.getDeserializationConfig:()Lcom/fasterxml/jackson/databind/DeserializationConfig;
        //    20: invokevirtual   com/fasterxml/jackson/databind/ObjectMapper.createDeserializationContext:(Lcom/fasterxml/jackson/core/JsonParser;Lcom/fasterxml/jackson/databind/DeserializationConfig;)Lcom/fasterxml/jackson/databind/deser/DefaultDeserializationContext;
        //    23: aload_2        
        //    24: invokevirtual   com/fasterxml/jackson/databind/ObjectMapper._findRootDeserializer:(Lcom/fasterxml/jackson/databind/DeserializationContext;Lcom/fasterxml/jackson/databind/JavaType;)Lcom/fasterxml/jackson/databind/JsonDeserializer;
        //    27: invokevirtual   com/fasterxml/jackson/databind/JsonDeserializer.getNullValue:()Ljava/lang/Object;
        //    30: astore_2       
        //    31: aload_1        
        //    32: invokevirtual   com/fasterxml/jackson/core/JsonParser.clearCurrentToken:()V
        //    35: aload_1        
        //    36: invokevirtual   com/fasterxml/jackson/core/JsonParser.close:()V
        //    39: aload_2        
        //    40: areturn        
        //    41: aload_3        
        //    42: getstatic       com/fasterxml/jackson/core/JsonToken.END_ARRAY:Lcom/fasterxml/jackson/core/JsonToken;
        //    45: if_acmpeq       128
        //    48: aload_3        
        //    49: getstatic       com/fasterxml/jackson/core/JsonToken.END_OBJECT:Lcom/fasterxml/jackson/core/JsonToken;
        //    52: if_acmpne       58
        //    55: goto            128
        //    58: aload_0        
        //    59: invokevirtual   com/fasterxml/jackson/databind/ObjectMapper.getDeserializationConfig:()Lcom/fasterxml/jackson/databind/DeserializationConfig;
        //    62: astore_3       
        //    63: aload_0        
        //    64: aload_1        
        //    65: aload_3        
        //    66: invokevirtual   com/fasterxml/jackson/databind/ObjectMapper.createDeserializationContext:(Lcom/fasterxml/jackson/core/JsonParser;Lcom/fasterxml/jackson/databind/DeserializationConfig;)Lcom/fasterxml/jackson/databind/deser/DefaultDeserializationContext;
        //    69: astore          4
        //    71: aload_0        
        //    72: aload           4
        //    74: aload_2        
        //    75: invokevirtual   com/fasterxml/jackson/databind/ObjectMapper._findRootDeserializer:(Lcom/fasterxml/jackson/databind/DeserializationContext;Lcom/fasterxml/jackson/databind/JavaType;)Lcom/fasterxml/jackson/databind/JsonDeserializer;
        //    78: astore          5
        //    80: aload_3        
        //    81: invokevirtual   com/fasterxml/jackson/databind/DeserializationConfig.useRootWrapping:()Z
        //    84: ifeq            102
        //    87: aload_0        
        //    88: aload_1        
        //    89: aload           4
        //    91: aload_3        
        //    92: aload_2        
        //    93: aload           5
        //    95: invokevirtual   com/fasterxml/jackson/databind/ObjectMapper._unwrapAndDeserialize:(Lcom/fasterxml/jackson/core/JsonParser;Lcom/fasterxml/jackson/databind/DeserializationContext;Lcom/fasterxml/jackson/databind/DeserializationConfig;Lcom/fasterxml/jackson/databind/JavaType;Lcom/fasterxml/jackson/databind/JsonDeserializer;)Ljava/lang/Object;
        //    98: astore_2       
        //    99: goto            31
        //   102: aload           5
        //   104: aload_1        
        //   105: aload           4
        //   107: invokevirtual   com/fasterxml/jackson/databind/JsonDeserializer.deserialize:(Lcom/fasterxml/jackson/core/JsonParser;Lcom/fasterxml/jackson/databind/DeserializationContext;)Ljava/lang/Object;
        //   110: astore_2       
        //   111: goto            31
        //   114: astore_2       
        //   115: aload_1        
        //   116: invokevirtual   com/fasterxml/jackson/core/JsonParser.close:()V
        //   119: aload_2        
        //   120: athrow         
        //   121: astore_1       
        //   122: aload_2        
        //   123: areturn        
        //   124: astore_1       
        //   125: goto            119
        //   128: aconst_null    
        //   129: astore_2       
        //   130: goto            31
        //    Exceptions:
        //  throws java.io.IOException
        //  throws com.fasterxml.jackson.core.JsonParseException
        //  throws com.fasterxml.jackson.databind.JsonMappingException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  0      31     114    121    Any
        //  31     35     114    121    Any
        //  35     39     121    124    Ljava/io/IOException;
        //  41     55     114    121    Any
        //  58     99     114    121    Any
        //  102    111    114    121    Any
        //  115    119    124    128    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 74, Size: 74
        //     at java.util.ArrayList.rangeCheck(ArrayList.java:653)
        //     at java.util.ArrayList.get(ArrayList.java:429)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3303)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
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
    
    protected Object _readValue(final DeserializationConfig deserializationConfig, final JsonParser jsonParser, final JavaType javaType) throws IOException, JsonParseException, JsonMappingException {
        final JsonToken initForReading = this._initForReading(jsonParser);
        Object o;
        if (initForReading == JsonToken.VALUE_NULL) {
            o = this._findRootDeserializer(this.createDeserializationContext(jsonParser, deserializationConfig), javaType).getNullValue();
        }
        else if (initForReading == JsonToken.END_ARRAY || initForReading == JsonToken.END_OBJECT) {
            o = null;
        }
        else {
            final DefaultDeserializationContext deserializationContext = this.createDeserializationContext(jsonParser, deserializationConfig);
            final JsonDeserializer<Object> findRootDeserializer = this._findRootDeserializer(deserializationContext, javaType);
            if (deserializationConfig.useRootWrapping()) {
                o = this._unwrapAndDeserialize(jsonParser, deserializationContext, deserializationConfig, javaType, findRootDeserializer);
            }
            else {
                o = findRootDeserializer.deserialize(jsonParser, deserializationContext);
            }
        }
        jsonParser.clearCurrentToken();
        return o;
    }
    
    protected DefaultSerializerProvider _serializerProvider(final SerializationConfig serializationConfig) {
        return this._serializerProvider.createInstance(serializationConfig, this._serializerFactory);
    }
    
    protected Object _unwrapAndDeserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext, final DeserializationConfig deserializationConfig, final JavaType javaType, final JsonDeserializer<Object> jsonDeserializer) throws IOException, JsonParseException, JsonMappingException {
        String s;
        if ((s = deserializationConfig.getRootName()) == null) {
            s = this._rootNames.findRootName(javaType, deserializationConfig).getValue();
        }
        if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            throw JsonMappingException.from(jsonParser, "Current token not START_OBJECT (needed to unwrap root name '" + s + "'), but " + jsonParser.getCurrentToken());
        }
        if (jsonParser.nextToken() != JsonToken.FIELD_NAME) {
            throw JsonMappingException.from(jsonParser, "Current token not FIELD_NAME (to contain expected root name '" + s + "'), but " + jsonParser.getCurrentToken());
        }
        final String currentName = jsonParser.getCurrentName();
        if (!s.equals(currentName)) {
            throw JsonMappingException.from(jsonParser, "Root name '" + currentName + "' does not match expected ('" + s + "') for type " + javaType);
        }
        jsonParser.nextToken();
        final Object deserialize = jsonDeserializer.deserialize(jsonParser, deserializationContext);
        if (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            throw JsonMappingException.from(jsonParser, "Current token not END_OBJECT (to match wrapper object with root name '" + s + "'), but " + jsonParser.getCurrentToken());
        }
        return deserialize;
    }
    
    protected DefaultDeserializationContext createDeserializationContext(final JsonParser jsonParser, final DeserializationConfig deserializationConfig) {
        return this._deserializationContext.createInstance(deserializationConfig, jsonParser, this._injectableValues);
    }
    
    public DeserializationConfig getDeserializationConfig() {
        return this._deserializationConfig;
    }
    
    @Override
    public JsonFactory getFactory() {
        return this._jsonFactory;
    }
    
    @Deprecated
    @Override
    public JsonFactory getJsonFactory() {
        return this._jsonFactory;
    }
    
    public JsonNodeFactory getNodeFactory() {
        return this._deserializationConfig.getNodeFactory();
    }
    
    public SerializationConfig getSerializationConfig() {
        return this._serializationConfig;
    }
    
    @Override
    public <T extends TreeNode> T readTree(final JsonParser jsonParser) throws IOException, JsonProcessingException {
        final DeserializationConfig deserializationConfig = this.getDeserializationConfig();
        TreeNode treeNode;
        if (jsonParser.getCurrentToken() == null && jsonParser.nextToken() == null) {
            treeNode = null;
        }
        else if ((treeNode = (JsonNode)this._readValue(deserializationConfig, jsonParser, ObjectMapper.JSON_NODE_TYPE)) == null) {
            return (T)this.getNodeFactory().nullNode();
        }
        return (T)treeNode;
    }
    
    public <T> T readValue(final JsonParser jsonParser, final TypeReference<?> typeReference) throws IOException, JsonParseException, JsonMappingException {
        return (T)this._readValue(this.getDeserializationConfig(), jsonParser, this._typeFactory.constructType(typeReference));
    }
    
    public <T> T readValue(final JsonParser jsonParser, final Class<T> clazz) throws IOException, JsonParseException, JsonMappingException {
        return (T)this._readValue(this.getDeserializationConfig(), jsonParser, this._typeFactory.constructType(clazz));
    }
    
    public <T> T readValue(final String s, final TypeReference typeReference) throws IOException, JsonParseException, JsonMappingException {
        return (T)this._readMapAndClose(this._jsonFactory.createParser(s), this._typeFactory.constructType(typeReference));
    }
    
    public <T> T readValue(final String s, final Class<T> clazz) throws IOException, JsonParseException, JsonMappingException {
        return (T)this._readMapAndClose(this._jsonFactory.createParser(s), this._typeFactory.constructType(clazz));
    }
    
    @Override
    public Version version() {
        return PackageVersion.VERSION;
    }
    
    @Override
    public void writeValue(final JsonGenerator jsonGenerator, final Object o) throws IOException, JsonGenerationException, JsonMappingException {
        final SerializationConfig serializationConfig = this.getSerializationConfig();
        if (serializationConfig.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
            jsonGenerator.useDefaultPrettyPrinter();
        }
        if (serializationConfig.isEnabled(SerializationFeature.CLOSE_CLOSEABLE) && o instanceof Closeable) {
            this._writeCloseableValue(jsonGenerator, o, serializationConfig);
        }
        else {
            this._serializerProvider(serializationConfig).serializeValue(jsonGenerator, o);
            if (serializationConfig.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
                jsonGenerator.flush();
            }
        }
    }
    
    public static class DefaultTypeResolverBuilder extends StdTypeResolverBuilder implements Serializable
    {
        protected final DefaultTyping _appliesFor;
        
        @Override
        public TypeDeserializer buildTypeDeserializer(final DeserializationConfig deserializationConfig, final JavaType javaType, final Collection<NamedType> collection) {
            if (this.useForType(javaType)) {
                return super.buildTypeDeserializer(deserializationConfig, javaType, collection);
            }
            return null;
        }
        
        @Override
        public TypeSerializer buildTypeSerializer(final SerializationConfig serializationConfig, final JavaType javaType, final Collection<NamedType> collection) {
            if (this.useForType(javaType)) {
                return super.buildTypeSerializer(serializationConfig, javaType, collection);
            }
            return null;
        }
        
        public boolean useForType(final JavaType javaType) {
            boolean b = false;
            JavaType contentType = javaType;
            JavaType javaType2 = javaType;
            JavaType contentType2 = javaType;
            Label_0077: {
                switch (this._appliesFor) {
                    default: {
                        if (javaType.getRawClass() == Object.class) {
                            break;
                        }
                        return false;
                    }
                    case NON_CONCRETE_AND_ARRAYS: {
                        while (true) {
                            javaType2 = contentType;
                            if (!contentType.isArrayType()) {
                                break Label_0077;
                            }
                            contentType = contentType.getContentType();
                        }
                        break;
                    }
                    case OBJECT_AND_NON_CONCRETE: {
                        if (javaType2.getRawClass() == Object.class || !javaType2.isConcrete()) {
                            b = true;
                        }
                        return b;
                    }
                    case NON_FINAL: {
                        while (contentType2.isArrayType()) {
                            contentType2 = contentType2.getContentType();
                        }
                        if (contentType2.isFinal()) {
                            return false;
                        }
                        break;
                    }
                }
            }
            return true;
        }
    }
    
    public enum DefaultTyping
    {
        JAVA_LANG_OBJECT, 
        NON_CONCRETE_AND_ARRAYS, 
        NON_FINAL, 
        OBJECT_AND_NON_CONCRETE;
    }
}
