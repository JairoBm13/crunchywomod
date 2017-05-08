// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.BeanAsArrayBuilderDeserializer;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import java.io.Serializable;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import java.util.HashSet;
import java.util.Map;
import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

public class BuilderBasedDeserializer extends BeanDeserializerBase
{
    protected final AnnotatedMethod _buildMethod;
    
    public BuilderBasedDeserializer(final BeanDeserializerBuilder beanDeserializerBuilder, final BeanDescription beanDescription, final BeanPropertyMap beanPropertyMap, final Map<String, SettableBeanProperty> map, final HashSet<String> set, final boolean b, final boolean b2) {
        super(beanDeserializerBuilder, beanDescription, beanPropertyMap, map, set, b, b2);
        this._buildMethod = beanDeserializerBuilder.getBuildMethod();
        if (this._objectIdReader != null) {
            throw new IllegalArgumentException("Can not use Object Id with Builder-based deserialization (type " + beanDescription.getType() + ")");
        }
    }
    
    public BuilderBasedDeserializer(final BuilderBasedDeserializer builderBasedDeserializer, final ObjectIdReader objectIdReader) {
        super(builderBasedDeserializer, objectIdReader);
        this._buildMethod = builderBasedDeserializer._buildMethod;
    }
    
    protected BuilderBasedDeserializer(final BuilderBasedDeserializer builderBasedDeserializer, final NameTransformer nameTransformer) {
        super(builderBasedDeserializer, nameTransformer);
        this._buildMethod = builderBasedDeserializer._buildMethod;
    }
    
    public BuilderBasedDeserializer(final BuilderBasedDeserializer builderBasedDeserializer, final HashSet<String> set) {
        super(builderBasedDeserializer, set);
        this._buildMethod = builderBasedDeserializer._buildMethod;
    }
    
    private final Object vanillaDeserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext, JsonToken jsonToken) throws IOException, JsonProcessingException {
        jsonToken = (JsonToken)this._valueInstantiator.createUsingDefault(deserializationContext);
    Label_0059_Outer:
        while (jsonParser.getCurrentToken() != JsonToken.END_OBJECT) {
            final String currentName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            final SettableBeanProperty find = this._beanProperties.find(currentName);
            while (true) {
                Label_0082: {
                    if (find == null) {
                        break Label_0082;
                    }
                    try {
                        jsonToken = (JsonToken)find.deserializeSetAndReturn(jsonParser, deserializationContext, jsonToken);
                        jsonParser.nextToken();
                        continue Label_0059_Outer;
                    }
                    catch (Exception ex) {
                        this.wrapAndThrow(ex, jsonToken, currentName, deserializationContext);
                        continue;
                    }
                }
                this.handleUnknownVanilla(jsonParser, deserializationContext, jsonToken, currentName);
                continue;
            }
        }
        return jsonToken;
    }
    
    protected final Object _deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext, Object deserializeWithUnwrapped) throws IOException, JsonProcessingException {
        if (this._injectables != null) {
            this.injectValues(deserializationContext, deserializeWithUnwrapped);
        }
        if (this._unwrappedPropertyHandler != null) {
            deserializeWithUnwrapped = this.deserializeWithUnwrapped(jsonParser, deserializationContext, deserializeWithUnwrapped);
        }
        else {
            if (this._externalTypeIdHandler != null) {
                return this.deserializeWithExternalTypeId(jsonParser, deserializationContext, deserializeWithUnwrapped);
            }
            if (this._needViewProcesing) {
                final Class<?> activeView = deserializationContext.getActiveView();
                if (activeView != null) {
                    return this.deserializeWithView(jsonParser, deserializationContext, deserializeWithUnwrapped, activeView);
                }
            }
            Serializable s;
            final JsonToken jsonToken = (JsonToken)(s = jsonParser.getCurrentToken());
            Object deserializeSetAndReturn = deserializeWithUnwrapped;
            if (jsonToken == JsonToken.START_OBJECT) {
                s = jsonParser.nextToken();
                deserializeSetAndReturn = deserializeWithUnwrapped;
            }
        Label_0151_Outer:
            while (true) {
                deserializeWithUnwrapped = deserializeSetAndReturn;
                if (s != JsonToken.FIELD_NAME) {
                    break;
                }
                s = jsonParser.getCurrentName();
                jsonParser.nextToken();
                final SettableBeanProperty find = this._beanProperties.find((String)s);
                while (true) {
                    Label_0174: {
                        if (find == null) {
                            break Label_0174;
                        }
                        try {
                            deserializeWithUnwrapped = (deserializeSetAndReturn = find.deserializeSetAndReturn(jsonParser, deserializationContext, deserializeSetAndReturn));
                            s = jsonParser.nextToken();
                            continue Label_0151_Outer;
                        }
                        catch (Exception ex) {
                            this.wrapAndThrow(ex, deserializeSetAndReturn, (String)s, deserializationContext);
                            continue;
                        }
                    }
                    if (this._ignorableProps != null && this._ignorableProps.contains(s)) {
                        jsonParser.skipChildren();
                        continue;
                    }
                    if (this._anySetter != null) {
                        this._anySetter.deserializeAndSet(jsonParser, deserializationContext, deserializeSetAndReturn, (String)s);
                        continue;
                    }
                    this.handleUnknownProperty(jsonParser, deserializationContext, deserializeSetAndReturn, (String)s);
                    continue;
                }
            }
        }
        return deserializeWithUnwrapped;
    }
    
    @Override
    protected final Object _deserializeUsingPropertyBased(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final PropertyBasedCreator propertyBasedCreator = this._propertyBasedCreator;
        final PropertyValueBuffer startBuilding = propertyBasedCreator.startBuilding(jsonParser, deserializationContext, this._objectIdReader);
        JsonToken currentToken = jsonParser.getCurrentToken();
        TokenBuffer tokenBuffer = null;
        while (currentToken == JsonToken.FIELD_NAME) {
            final String currentName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            final SettableBeanProperty creatorProperty = propertyBasedCreator.findCreatorProperty(currentName);
            TokenBuffer tokenBuffer2 = null;
            Label_0148: {
                if (creatorProperty != null) {
                    final Object deserialize = creatorProperty.deserialize(jsonParser, deserializationContext);
                    tokenBuffer2 = tokenBuffer;
                    if (startBuilding.assignParameter(creatorProperty.getCreatorIndex(), deserialize)) {
                        jsonParser.nextToken();
                        Object build = null;
                        Label_0164: {
                            try {
                                build = propertyBasedCreator.build(deserializationContext, startBuilding);
                                if (build.getClass() != this._beanType.getRawClass()) {
                                    return this.handlePolymorphic(jsonParser, deserializationContext, build, tokenBuffer);
                                }
                                break Label_0164;
                            }
                            catch (Exception ex) {
                                this.wrapAndThrow(ex, this._beanType.getRawClass(), currentName, deserializationContext);
                                tokenBuffer2 = tokenBuffer;
                            }
                            break Label_0148;
                        }
                        Object handleUnknownProperties;
                        if (tokenBuffer != null) {
                            handleUnknownProperties = this.handleUnknownProperties(deserializationContext, build, tokenBuffer);
                        }
                        else {
                            handleUnknownProperties = build;
                        }
                        return this._deserialize(jsonParser, deserializationContext, handleUnknownProperties);
                    }
                }
                else {
                    tokenBuffer2 = tokenBuffer;
                    if (!startBuilding.readIdProperty(currentName)) {
                        final SettableBeanProperty find = this._beanProperties.find(currentName);
                        if (find != null) {
                            startBuilding.bufferProperty(find, find.deserialize(jsonParser, deserializationContext));
                            tokenBuffer2 = tokenBuffer;
                        }
                        else if (this._ignorableProps != null && this._ignorableProps.contains(currentName)) {
                            jsonParser.skipChildren();
                            tokenBuffer2 = tokenBuffer;
                        }
                        else if (this._anySetter != null) {
                            startBuilding.bufferAnyProperty(this._anySetter, currentName, this._anySetter.deserialize(jsonParser, deserializationContext));
                            tokenBuffer2 = tokenBuffer;
                        }
                        else {
                            if ((tokenBuffer2 = tokenBuffer) == null) {
                                tokenBuffer2 = new TokenBuffer(jsonParser.getCodec());
                            }
                            tokenBuffer2.writeFieldName(currentName);
                            tokenBuffer2.copyCurrentStructure(jsonParser);
                        }
                    }
                }
            }
            final JsonToken nextToken = jsonParser.nextToken();
            tokenBuffer = tokenBuffer2;
            currentToken = nextToken;
        }
        Object build2;
        try {
            build2 = propertyBasedCreator.build(deserializationContext, startBuilding);
            if (tokenBuffer == null) {
                return build2;
            }
            if (build2.getClass() != this._beanType.getRawClass()) {
                return this.handlePolymorphic(null, deserializationContext, build2, tokenBuffer);
            }
        }
        catch (Exception ex2) {
            this.wrapInstantiationProblem(ex2, deserializationContext);
            return null;
        }
        return this.handleUnknownProperties(deserializationContext, build2, tokenBuffer);
    }
    
    @Override
    protected BeanAsArrayBuilderDeserializer asArrayDeserializer() {
        return new BeanAsArrayBuilderDeserializer(this, this._beanProperties.getPropertiesInInsertionOrder(), this._buildMethod);
    }
    
    @Override
    public final Object deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final JsonToken currentToken = jsonParser.getCurrentToken();
        if (currentToken == JsonToken.START_OBJECT) {
            final JsonToken nextToken = jsonParser.nextToken();
            if (this._vanillaProcessing) {
                return this.finishBuild(deserializationContext, this.vanillaDeserialize(jsonParser, deserializationContext, nextToken));
            }
            return this.finishBuild(deserializationContext, this.deserializeFromObject(jsonParser, deserializationContext));
        }
        else {
            switch (currentToken) {
                default: {
                    throw deserializationContext.mappingException(this.getBeanClass());
                }
                case VALUE_STRING: {
                    return this.finishBuild(deserializationContext, this.deserializeFromString(jsonParser, deserializationContext));
                }
                case VALUE_NUMBER_INT: {
                    return this.finishBuild(deserializationContext, this.deserializeFromNumber(jsonParser, deserializationContext));
                }
                case VALUE_NUMBER_FLOAT: {
                    return this.finishBuild(deserializationContext, this.deserializeFromDouble(jsonParser, deserializationContext));
                }
                case VALUE_EMBEDDED_OBJECT: {
                    return jsonParser.getEmbeddedObject();
                }
                case VALUE_TRUE:
                case VALUE_FALSE: {
                    return this.finishBuild(deserializationContext, this.deserializeFromBoolean(jsonParser, deserializationContext));
                }
                case START_ARRAY: {
                    return this.finishBuild(deserializationContext, this.deserializeFromArray(jsonParser, deserializationContext));
                }
                case FIELD_NAME:
                case END_OBJECT: {
                    return this.finishBuild(deserializationContext, this.deserializeFromObject(jsonParser, deserializationContext));
                }
            }
        }
    }
    
    @Override
    public Object deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Object o) throws IOException, JsonProcessingException {
        return this.finishBuild(deserializationContext, this._deserialize(jsonParser, deserializationContext, o));
    }
    
    @Override
    public Object deserializeFromObject(final JsonParser p0, final DeserializationContext p1) throws IOException, JsonProcessingException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: getfield        com/fasterxml/jackson/databind/deser/BuilderBasedDeserializer._nonStandardCreation:Z
        //     4: ifeq            46
        //     7: aload_0        
        //     8: getfield        com/fasterxml/jackson/databind/deser/BuilderBasedDeserializer._unwrappedPropertyHandler:Lcom/fasterxml/jackson/databind/deser/impl/UnwrappedPropertyHandler;
        //    11: ifnull          25
        //    14: aload_0        
        //    15: aload_1        
        //    16: aload_2        
        //    17: invokevirtual   com/fasterxml/jackson/databind/deser/BuilderBasedDeserializer.deserializeWithUnwrapped:(Lcom/fasterxml/jackson/core/JsonParser;Lcom/fasterxml/jackson/databind/DeserializationContext;)Ljava/lang/Object;
        //    20: astore          4
        //    22: aload           4
        //    24: areturn        
        //    25: aload_0        
        //    26: getfield        com/fasterxml/jackson/databind/deser/BuilderBasedDeserializer._externalTypeIdHandler:Lcom/fasterxml/jackson/databind/deser/impl/ExternalTypeHandler;
        //    29: ifnull          39
        //    32: aload_0        
        //    33: aload_1        
        //    34: aload_2        
        //    35: invokevirtual   com/fasterxml/jackson/databind/deser/BuilderBasedDeserializer.deserializeWithExternalTypeId:(Lcom/fasterxml/jackson/core/JsonParser;Lcom/fasterxml/jackson/databind/DeserializationContext;)Ljava/lang/Object;
        //    38: areturn        
        //    39: aload_0        
        //    40: aload_1        
        //    41: aload_2        
        //    42: invokevirtual   com/fasterxml/jackson/databind/deser/BuilderBasedDeserializer.deserializeFromObjectUsingNonDefault:(Lcom/fasterxml/jackson/core/JsonParser;Lcom/fasterxml/jackson/databind/DeserializationContext;)Ljava/lang/Object;
        //    45: areturn        
        //    46: aload_0        
        //    47: getfield        com/fasterxml/jackson/databind/deser/BuilderBasedDeserializer._valueInstantiator:Lcom/fasterxml/jackson/databind/deser/ValueInstantiator;
        //    50: aload_2        
        //    51: invokevirtual   com/fasterxml/jackson/databind/deser/ValueInstantiator.createUsingDefault:(Lcom/fasterxml/jackson/databind/DeserializationContext;)Ljava/lang/Object;
        //    54: astore          4
        //    56: aload_0        
        //    57: getfield        com/fasterxml/jackson/databind/deser/BuilderBasedDeserializer._injectables:[Lcom/fasterxml/jackson/databind/deser/impl/ValueInjector;
        //    60: ifnull          70
        //    63: aload_0        
        //    64: aload_2        
        //    65: aload           4
        //    67: invokevirtual   com/fasterxml/jackson/databind/deser/BuilderBasedDeserializer.injectValues:(Lcom/fasterxml/jackson/databind/DeserializationContext;Ljava/lang/Object;)V
        //    70: aload           4
        //    72: astore_3       
        //    73: aload_0        
        //    74: getfield        com/fasterxml/jackson/databind/deser/BuilderBasedDeserializer._needViewProcesing:Z
        //    77: ifeq            122
        //    80: aload_2        
        //    81: invokevirtual   com/fasterxml/jackson/databind/DeserializationContext.getActiveView:()Ljava/lang/Class;
        //    84: astore          5
        //    86: aload           4
        //    88: astore_3       
        //    89: aload           5
        //    91: ifnull          122
        //    94: aload_0        
        //    95: aload_1        
        //    96: aload_2        
        //    97: aload           4
        //    99: aload           5
        //   101: invokevirtual   com/fasterxml/jackson/databind/deser/BuilderBasedDeserializer.deserializeWithView:(Lcom/fasterxml/jackson/core/JsonParser;Lcom/fasterxml/jackson/databind/DeserializationContext;Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;
        //   104: areturn        
        //   105: astore          4
        //   107: aload_0        
        //   108: aload           4
        //   110: aload_3        
        //   111: aload           5
        //   113: aload_2        
        //   114: invokevirtual   com/fasterxml/jackson/databind/deser/BuilderBasedDeserializer.wrapAndThrow:(Ljava/lang/Throwable;Ljava/lang/Object;Ljava/lang/String;Lcom/fasterxml/jackson/databind/DeserializationContext;)V
        //   117: aload_1        
        //   118: invokevirtual   com/fasterxml/jackson/core/JsonParser.nextToken:()Lcom/fasterxml/jackson/core/JsonToken;
        //   121: pop            
        //   122: aload_3        
        //   123: astore          4
        //   125: aload_1        
        //   126: invokevirtual   com/fasterxml/jackson/core/JsonParser.getCurrentToken:()Lcom/fasterxml/jackson/core/JsonToken;
        //   129: getstatic       com/fasterxml/jackson/core/JsonToken.END_OBJECT:Lcom/fasterxml/jackson/core/JsonToken;
        //   132: if_acmpeq       22
        //   135: aload_1        
        //   136: invokevirtual   com/fasterxml/jackson/core/JsonParser.getCurrentName:()Ljava/lang/String;
        //   139: astore          5
        //   141: aload_1        
        //   142: invokevirtual   com/fasterxml/jackson/core/JsonParser.nextToken:()Lcom/fasterxml/jackson/core/JsonToken;
        //   145: pop            
        //   146: aload_0        
        //   147: getfield        com/fasterxml/jackson/databind/deser/BuilderBasedDeserializer._beanProperties:Lcom/fasterxml/jackson/databind/deser/impl/BeanPropertyMap;
        //   150: aload           5
        //   152: invokevirtual   com/fasterxml/jackson/databind/deser/impl/BeanPropertyMap.find:(Ljava/lang/String;)Lcom/fasterxml/jackson/databind/deser/SettableBeanProperty;
        //   155: astore          4
        //   157: aload           4
        //   159: ifnull          178
        //   162: aload           4
        //   164: aload_1        
        //   165: aload_2        
        //   166: aload_3        
        //   167: invokevirtual   com/fasterxml/jackson/databind/deser/SettableBeanProperty.deserializeSetAndReturn:(Lcom/fasterxml/jackson/core/JsonParser;Lcom/fasterxml/jackson/databind/DeserializationContext;Ljava/lang/Object;)Ljava/lang/Object;
        //   170: astore          4
        //   172: aload           4
        //   174: astore_3       
        //   175: goto            117
        //   178: aload_0        
        //   179: getfield        com/fasterxml/jackson/databind/deser/BuilderBasedDeserializer._ignorableProps:Ljava/util/HashSet;
        //   182: ifnull          205
        //   185: aload_0        
        //   186: getfield        com/fasterxml/jackson/databind/deser/BuilderBasedDeserializer._ignorableProps:Ljava/util/HashSet;
        //   189: aload           5
        //   191: invokevirtual   java/util/HashSet.contains:(Ljava/lang/Object;)Z
        //   194: ifeq            205
        //   197: aload_1        
        //   198: invokevirtual   com/fasterxml/jackson/core/JsonParser.skipChildren:()Lcom/fasterxml/jackson/core/JsonParser;
        //   201: pop            
        //   202: goto            117
        //   205: aload_0        
        //   206: getfield        com/fasterxml/jackson/databind/deser/BuilderBasedDeserializer._anySetter:Lcom/fasterxml/jackson/databind/deser/SettableAnyProperty;
        //   209: ifnull          242
        //   212: aload_0        
        //   213: getfield        com/fasterxml/jackson/databind/deser/BuilderBasedDeserializer._anySetter:Lcom/fasterxml/jackson/databind/deser/SettableAnyProperty;
        //   216: aload_1        
        //   217: aload_2        
        //   218: aload_3        
        //   219: aload           5
        //   221: invokevirtual   com/fasterxml/jackson/databind/deser/SettableAnyProperty.deserializeAndSet:(Lcom/fasterxml/jackson/core/JsonParser;Lcom/fasterxml/jackson/databind/DeserializationContext;Ljava/lang/Object;Ljava/lang/String;)V
        //   224: goto            117
        //   227: astore          4
        //   229: aload_0        
        //   230: aload           4
        //   232: aload_3        
        //   233: aload           5
        //   235: aload_2        
        //   236: invokevirtual   com/fasterxml/jackson/databind/deser/BuilderBasedDeserializer.wrapAndThrow:(Ljava/lang/Throwable;Ljava/lang/Object;Ljava/lang/String;Lcom/fasterxml/jackson/databind/DeserializationContext;)V
        //   239: goto            117
        //   242: aload_0        
        //   243: aload_1        
        //   244: aload_2        
        //   245: aload_3        
        //   246: aload           5
        //   248: invokevirtual   com/fasterxml/jackson/databind/deser/BuilderBasedDeserializer.handleUnknownProperty:(Lcom/fasterxml/jackson/core/JsonParser;Lcom/fasterxml/jackson/databind/DeserializationContext;Ljava/lang/Object;Ljava/lang/String;)V
        //   251: goto            117
        //    Exceptions:
        //  throws java.io.IOException
        //  throws com.fasterxml.jackson.core.JsonProcessingException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  162    172    105    117    Ljava/lang/Exception;
        //  212    224    227    242    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        //     at java.util.ArrayList.rangeCheck(ArrayList.java:653)
        //     at java.util.ArrayList.get(ArrayList.java:429)
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:3035)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
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
    
    protected Object deserializeUsingPropertyBasedWithExternalTypeId(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        throw new IllegalStateException("Deserialization with Builder, External type id, @JsonCreator not yet implemented");
    }
    
    protected Object deserializeUsingPropertyBasedWithUnwrapped(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final PropertyBasedCreator propertyBasedCreator = this._propertyBasedCreator;
        final PropertyValueBuffer startBuilding = propertyBasedCreator.startBuilding(jsonParser, deserializationContext, this._objectIdReader);
        final TokenBuffer tokenBuffer = new TokenBuffer(jsonParser.getCodec());
        tokenBuffer.writeStartObject();
        for (JsonToken jsonToken = jsonParser.getCurrentToken(); jsonToken == JsonToken.FIELD_NAME; jsonToken = jsonParser.nextToken()) {
            final String currentName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            final SettableBeanProperty creatorProperty = propertyBasedCreator.findCreatorProperty(currentName);
            if (creatorProperty != null) {
                if (startBuilding.assignParameter(creatorProperty.getCreatorIndex(), creatorProperty.deserialize(jsonParser, deserializationContext))) {
                    JsonToken jsonToken2 = jsonParser.nextToken();
                    Object build = null;
                    Label_0159: {
                        try {
                            build = propertyBasedCreator.build(deserializationContext, startBuilding);
                            while (jsonToken2 == JsonToken.FIELD_NAME) {
                                jsonParser.nextToken();
                                tokenBuffer.copyCurrentStructure(jsonParser);
                                jsonToken2 = jsonParser.nextToken();
                            }
                            break Label_0159;
                        }
                        catch (Exception ex) {
                            this.wrapAndThrow(ex, this._beanType.getRawClass(), currentName, deserializationContext);
                        }
                        continue;
                    }
                    tokenBuffer.writeEndObject();
                    if (build.getClass() != this._beanType.getRawClass()) {
                        throw deserializationContext.mappingException("Can not create polymorphic instances with unwrapped values");
                    }
                    return this._unwrappedPropertyHandler.processUnwrapped(jsonParser, deserializationContext, build, tokenBuffer);
                }
            }
            else if (!startBuilding.readIdProperty(currentName)) {
                final SettableBeanProperty find = this._beanProperties.find(currentName);
                if (find != null) {
                    startBuilding.bufferProperty(find, find.deserialize(jsonParser, deserializationContext));
                }
                else if (this._ignorableProps != null && this._ignorableProps.contains(currentName)) {
                    jsonParser.skipChildren();
                }
                else {
                    tokenBuffer.writeFieldName(currentName);
                    tokenBuffer.copyCurrentStructure(jsonParser);
                    if (this._anySetter != null) {
                        startBuilding.bufferAnyProperty(this._anySetter, currentName, this._anySetter.deserialize(jsonParser, deserializationContext));
                    }
                }
            }
        }
        try {
            return this._unwrappedPropertyHandler.processUnwrapped(jsonParser, deserializationContext, propertyBasedCreator.build(deserializationContext, startBuilding), tokenBuffer);
        }
        catch (Exception ex2) {
            this.wrapInstantiationProblem(ex2, deserializationContext);
            return null;
        }
    }
    
    protected Object deserializeWithExternalTypeId(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (this._propertyBasedCreator != null) {
            return this.deserializeUsingPropertyBasedWithExternalTypeId(jsonParser, deserializationContext);
        }
        return this.deserializeWithExternalTypeId(jsonParser, deserializationContext, this._valueInstantiator.createUsingDefault(deserializationContext));
    }
    
    protected Object deserializeWithExternalTypeId(final JsonParser jsonParser, final DeserializationContext deserializationContext, Object o) throws IOException, JsonProcessingException {
        Class<?> activeView;
        if (this._needViewProcesing) {
            activeView = deserializationContext.getActiveView();
        }
        else {
            activeView = null;
        }
        final ExternalTypeHandler start = this._externalTypeIdHandler.start();
        while (jsonParser.getCurrentToken() != JsonToken.END_OBJECT) {
            final String currentName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            final SettableBeanProperty find = this._beanProperties.find(currentName);
            Object deserializeSetAndReturn;
            if (find != null) {
                if (activeView != null && !find.visibleInView(activeView)) {
                    jsonParser.skipChildren();
                    deserializeSetAndReturn = o;
                }
                else {
                    try {
                        deserializeSetAndReturn = find.deserializeSetAndReturn(jsonParser, deserializationContext, o);
                    }
                    catch (Exception ex) {
                        this.wrapAndThrow(ex, o, currentName, deserializationContext);
                        deserializeSetAndReturn = o;
                    }
                }
            }
            else if (this._ignorableProps != null && this._ignorableProps.contains(currentName)) {
                jsonParser.skipChildren();
                deserializeSetAndReturn = o;
            }
            else {
                deserializeSetAndReturn = o;
                if (!start.handlePropertyValue(jsonParser, deserializationContext, currentName, o)) {
                    if (this._anySetter != null) {
                        try {
                            this._anySetter.deserializeAndSet(jsonParser, deserializationContext, o, currentName);
                            deserializeSetAndReturn = o;
                        }
                        catch (Exception ex2) {
                            this.wrapAndThrow(ex2, o, currentName, deserializationContext);
                            deserializeSetAndReturn = o;
                        }
                    }
                    else {
                        this.handleUnknownProperty(jsonParser, deserializationContext, o, currentName);
                        deserializeSetAndReturn = o;
                    }
                }
            }
            jsonParser.nextToken();
            o = deserializeSetAndReturn;
        }
        return start.complete(jsonParser, deserializationContext, o);
    }
    
    protected Object deserializeWithUnwrapped(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (this._delegateDeserializer != null) {
            return this._valueInstantiator.createUsingDelegate(deserializationContext, this._delegateDeserializer.deserialize(jsonParser, deserializationContext));
        }
        if (this._propertyBasedCreator != null) {
            return this.deserializeUsingPropertyBasedWithUnwrapped(jsonParser, deserializationContext);
        }
        final TokenBuffer tokenBuffer = new TokenBuffer(jsonParser.getCodec());
        tokenBuffer.writeStartObject();
        Object usingDefault = this._valueInstantiator.createUsingDefault(deserializationContext);
        if (this._injectables != null) {
            this.injectValues(deserializationContext, usingDefault);
        }
        Class<?> activeView;
        if (this._needViewProcesing) {
            activeView = deserializationContext.getActiveView();
        }
        else {
            activeView = null;
        }
        while (jsonParser.getCurrentToken() != JsonToken.END_OBJECT) {
            final String currentName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            final SettableBeanProperty find = this._beanProperties.find(currentName);
            Object deserializeSetAndReturn;
            if (find != null) {
                if (activeView != null && !find.visibleInView(activeView)) {
                    jsonParser.skipChildren();
                    deserializeSetAndReturn = usingDefault;
                }
                else {
                    try {
                        deserializeSetAndReturn = find.deserializeSetAndReturn(jsonParser, deserializationContext, usingDefault);
                    }
                    catch (Exception ex) {
                        this.wrapAndThrow(ex, usingDefault, currentName, deserializationContext);
                        deserializeSetAndReturn = usingDefault;
                    }
                }
            }
            else if (this._ignorableProps != null && this._ignorableProps.contains(currentName)) {
                jsonParser.skipChildren();
                deserializeSetAndReturn = usingDefault;
            }
            else {
                tokenBuffer.writeFieldName(currentName);
                tokenBuffer.copyCurrentStructure(jsonParser);
                deserializeSetAndReturn = usingDefault;
                if (this._anySetter != null) {
                    try {
                        this._anySetter.deserializeAndSet(jsonParser, deserializationContext, usingDefault, currentName);
                        deserializeSetAndReturn = usingDefault;
                    }
                    catch (Exception ex2) {
                        this.wrapAndThrow(ex2, usingDefault, currentName, deserializationContext);
                        deserializeSetAndReturn = usingDefault;
                    }
                }
            }
            jsonParser.nextToken();
            usingDefault = deserializeSetAndReturn;
        }
        tokenBuffer.writeEndObject();
        this._unwrappedPropertyHandler.processUnwrapped(jsonParser, deserializationContext, usingDefault, tokenBuffer);
        return usingDefault;
    }
    
    protected Object deserializeWithUnwrapped(final JsonParser jsonParser, final DeserializationContext deserializationContext, Object o) throws IOException, JsonProcessingException {
        JsonToken jsonToken;
        if ((jsonToken = jsonParser.getCurrentToken()) == JsonToken.START_OBJECT) {
            jsonToken = jsonParser.nextToken();
        }
        final TokenBuffer tokenBuffer = new TokenBuffer(jsonParser.getCodec());
        tokenBuffer.writeStartObject();
        Class<?> activeView;
        if (this._needViewProcesing) {
            activeView = deserializationContext.getActiveView();
        }
        else {
            activeView = null;
        }
        while (jsonToken == JsonToken.FIELD_NAME) {
            final String currentName = jsonParser.getCurrentName();
            final SettableBeanProperty find = this._beanProperties.find(currentName);
            jsonParser.nextToken();
            Object deserializeSetAndReturn;
            if (find != null) {
                if (activeView != null && !find.visibleInView(activeView)) {
                    jsonParser.skipChildren();
                    deserializeSetAndReturn = o;
                }
                else {
                    try {
                        deserializeSetAndReturn = find.deserializeSetAndReturn(jsonParser, deserializationContext, o);
                    }
                    catch (Exception ex) {
                        this.wrapAndThrow(ex, o, currentName, deserializationContext);
                        deserializeSetAndReturn = o;
                    }
                }
            }
            else if (this._ignorableProps != null && this._ignorableProps.contains(currentName)) {
                jsonParser.skipChildren();
                deserializeSetAndReturn = o;
            }
            else {
                tokenBuffer.writeFieldName(currentName);
                tokenBuffer.copyCurrentStructure(jsonParser);
                deserializeSetAndReturn = o;
                if (this._anySetter != null) {
                    this._anySetter.deserializeAndSet(jsonParser, deserializationContext, o, currentName);
                    deserializeSetAndReturn = o;
                }
            }
            jsonToken = jsonParser.nextToken();
            o = deserializeSetAndReturn;
        }
        tokenBuffer.writeEndObject();
        this._unwrappedPropertyHandler.processUnwrapped(jsonParser, deserializationContext, o, tokenBuffer);
        return o;
    }
    
    protected final Object deserializeWithView(final JsonParser jsonParser, final DeserializationContext deserializationContext, Object deserializeSetAndReturn, final Class<?> clazz) throws IOException, JsonProcessingException {
        for (JsonToken jsonToken = jsonParser.getCurrentToken(); jsonToken == JsonToken.FIELD_NAME; jsonToken = jsonParser.nextToken()) {
            final String currentName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            final SettableBeanProperty find = this._beanProperties.find(currentName);
            if (find != null) {
                if (!find.visibleInView(clazz)) {
                    jsonParser.skipChildren();
                }
                else {
                    try {
                        deserializeSetAndReturn = find.deserializeSetAndReturn(jsonParser, deserializationContext, deserializeSetAndReturn);
                    }
                    catch (Exception ex) {
                        this.wrapAndThrow(ex, deserializeSetAndReturn, currentName, deserializationContext);
                    }
                }
            }
            else if (this._ignorableProps != null && this._ignorableProps.contains(currentName)) {
                jsonParser.skipChildren();
            }
            else if (this._anySetter != null) {
                this._anySetter.deserializeAndSet(jsonParser, deserializationContext, deserializeSetAndReturn, currentName);
            }
            else {
                this.handleUnknownProperty(jsonParser, deserializationContext, deserializeSetAndReturn, currentName);
            }
        }
        return deserializeSetAndReturn;
    }
    
    protected final Object finishBuild(final DeserializationContext deserializationContext, Object invoke) throws IOException {
        try {
            invoke = this._buildMethod.getMember().invoke(invoke, new Object[0]);
            return invoke;
        }
        catch (Exception ex) {
            this.wrapInstantiationProblem(ex, deserializationContext);
            return null;
        }
    }
    
    @Override
    public JsonDeserializer<Object> unwrappingDeserializer(final NameTransformer nameTransformer) {
        return new BuilderBasedDeserializer(this, nameTransformer);
    }
    
    @Override
    public BuilderBasedDeserializer withIgnorableProperties(final HashSet<String> set) {
        return new BuilderBasedDeserializer(this, set);
    }
    
    @Override
    public BuilderBasedDeserializer withObjectIdReader(final ObjectIdReader objectIdReader) {
        return new BuilderBasedDeserializer(this, objectIdReader);
    }
}
