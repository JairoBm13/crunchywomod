// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.BeanAsArrayDeserializer;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import java.util.Map;
import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
import com.fasterxml.jackson.databind.BeanDescription;
import java.util.HashSet;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import java.io.Serializable;

public class BeanDeserializer extends BeanDeserializerBase implements Serializable
{
    protected BeanDeserializer(final BeanDeserializerBase beanDeserializerBase) {
        super(beanDeserializerBase, beanDeserializerBase._ignoreAllUnknown);
    }
    
    public BeanDeserializer(final BeanDeserializerBase beanDeserializerBase, final ObjectIdReader objectIdReader) {
        super(beanDeserializerBase, objectIdReader);
    }
    
    protected BeanDeserializer(final BeanDeserializerBase beanDeserializerBase, final NameTransformer nameTransformer) {
        super(beanDeserializerBase, nameTransformer);
    }
    
    public BeanDeserializer(final BeanDeserializerBase beanDeserializerBase, final HashSet<String> set) {
        super(beanDeserializerBase, set);
    }
    
    public BeanDeserializer(final BeanDeserializerBuilder beanDeserializerBuilder, final BeanDescription beanDescription, final BeanPropertyMap beanPropertyMap, final Map<String, SettableBeanProperty> map, final HashSet<String> set, final boolean b, final boolean b2) {
        super(beanDeserializerBuilder, beanDescription, beanPropertyMap, map, set, b, b2);
    }
    
    private final Object _deserializeOther(final JsonParser jsonParser, final DeserializationContext deserializationContext, final JsonToken jsonToken) throws IOException, JsonProcessingException {
        if (jsonToken == null) {
            return this._missingToken(jsonParser, deserializationContext);
        }
        switch (jsonToken) {
            default: {
                throw deserializationContext.mappingException(this.getBeanClass());
            }
            case VALUE_STRING: {
                return this.deserializeFromString(jsonParser, deserializationContext);
            }
            case VALUE_NUMBER_INT: {
                return this.deserializeFromNumber(jsonParser, deserializationContext);
            }
            case VALUE_NUMBER_FLOAT: {
                return this.deserializeFromDouble(jsonParser, deserializationContext);
            }
            case VALUE_EMBEDDED_OBJECT: {
                return jsonParser.getEmbeddedObject();
            }
            case VALUE_TRUE:
            case VALUE_FALSE: {
                return this.deserializeFromBoolean(jsonParser, deserializationContext);
            }
            case START_ARRAY: {
                return this.deserializeFromArray(jsonParser, deserializationContext);
            }
            case FIELD_NAME:
            case END_OBJECT: {
                if (this._vanillaProcessing) {
                    return this.vanillaDeserialize(jsonParser, deserializationContext, jsonToken);
                }
                if (this._objectIdReader != null) {
                    return this.deserializeWithObjectId(jsonParser, deserializationContext);
                }
                return this.deserializeFromObject(jsonParser, deserializationContext);
            }
        }
    }
    
    private final Object vanillaDeserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext, JsonToken usingDefault) throws IOException, JsonProcessingException {
        usingDefault = (JsonToken)this._valueInstantiator.createUsingDefault(deserializationContext);
    Label_0054_Outer:
        while (jsonParser.getCurrentToken() != JsonToken.END_OBJECT) {
            final String currentName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            final SettableBeanProperty find = this._beanProperties.find(currentName);
            while (true) {
                Label_0077: {
                    if (find == null) {
                        break Label_0077;
                    }
                    try {
                        find.deserializeAndSet(jsonParser, deserializationContext, usingDefault);
                        jsonParser.nextToken();
                        continue Label_0054_Outer;
                    }
                    catch (Exception ex) {
                        this.wrapAndThrow(ex, usingDefault, currentName, deserializationContext);
                        continue;
                    }
                }
                this.handleUnknownVanilla(jsonParser, deserializationContext, usingDefault, currentName);
                continue;
            }
        }
        return usingDefault;
    }
    
    @Override
    protected Object _deserializeUsingPropertyBased(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final Object o = null;
        final PropertyBasedCreator propertyBasedCreator = this._propertyBasedCreator;
        final PropertyValueBuffer startBuilding = propertyBasedCreator.startBuilding(jsonParser, deserializationContext, this._objectIdReader);
        JsonToken currentToken = jsonParser.getCurrentToken();
        TokenBuffer tokenBuffer = null;
        while (currentToken == JsonToken.FIELD_NAME) {
            final String currentName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            final SettableBeanProperty creatorProperty = propertyBasedCreator.findCreatorProperty(currentName);
            TokenBuffer tokenBuffer2;
            if (creatorProperty != null) {
                final Object deserialize = creatorProperty.deserialize(jsonParser, deserializationContext);
                tokenBuffer2 = tokenBuffer;
                if (startBuilding.assignParameter(creatorProperty.getCreatorIndex(), deserialize)) {
                    jsonParser.nextToken();
                    Object build;
                    while (true) {
                        try {
                            build = propertyBasedCreator.build(deserializationContext, startBuilding);
                            if (build.getClass() != this._beanType.getRawClass()) {
                                return this.handlePolymorphic(jsonParser, deserializationContext, build, tokenBuffer);
                            }
                        }
                        catch (Exception ex) {
                            this.wrapAndThrow(ex, this._beanType.getRawClass(), currentName, deserializationContext);
                            build = o;
                            continue;
                        }
                        break;
                    }
                    Object handleUnknownProperties = build;
                    if (tokenBuffer != null) {
                        handleUnknownProperties = this.handleUnknownProperties(deserializationContext, build, tokenBuffer);
                    }
                    return this.deserialize(jsonParser, deserializationContext, handleUnknownProperties);
                }
            }
            else if (startBuilding.readIdProperty(currentName)) {
                tokenBuffer2 = tokenBuffer;
            }
            else {
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
            final JsonToken nextToken = jsonParser.nextToken();
            tokenBuffer = tokenBuffer2;
            currentToken = nextToken;
        }
        Object build2;
        while (true) {
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
                build2 = null;
                continue;
            }
            break;
        }
        return this.handleUnknownProperties(deserializationContext, build2, tokenBuffer);
    }
    
    protected Object _missingToken(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws JsonProcessingException {
        throw deserializationContext.endOfInputException(this.getBeanClass());
    }
    
    @Override
    protected BeanDeserializerBase asArrayDeserializer() {
        return new BeanAsArrayDeserializer(this, this._beanProperties.getPropertiesInInsertionOrder());
    }
    
    @Override
    public final Object deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final JsonToken currentToken = jsonParser.getCurrentToken();
        if (currentToken != JsonToken.START_OBJECT) {
            return this._deserializeOther(jsonParser, deserializationContext, currentToken);
        }
        if (this._vanillaProcessing) {
            return this.vanillaDeserialize(jsonParser, deserializationContext, jsonParser.nextToken());
        }
        jsonParser.nextToken();
        if (this._objectIdReader != null) {
            return this.deserializeWithObjectId(jsonParser, deserializationContext);
        }
        return this.deserializeFromObject(jsonParser, deserializationContext);
    }
    
    @Override
    public Object deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Object o) throws IOException, JsonProcessingException {
        if (this._injectables != null) {
            this.injectValues(deserializationContext, o);
        }
        Object deserializeWithUnwrapped = null;
        if (this._unwrappedPropertyHandler != null) {
            deserializeWithUnwrapped = this.deserializeWithUnwrapped(jsonParser, deserializationContext, o);
        }
        else {
            if (this._externalTypeIdHandler != null) {
                return this.deserializeWithExternalTypeId(jsonParser, deserializationContext, o);
            }
            JsonToken jsonToken;
            if ((jsonToken = jsonParser.getCurrentToken()) == JsonToken.START_OBJECT) {
                jsonToken = jsonParser.nextToken();
            }
            JsonToken nextToken = jsonToken;
            if (this._needViewProcesing) {
                final Class<?> activeView = deserializationContext.getActiveView();
                nextToken = jsonToken;
                if (activeView != null) {
                    return this.deserializeWithView(jsonParser, deserializationContext, o, activeView);
                }
            }
        Label_0153_Outer:
            while (true) {
                deserializeWithUnwrapped = o;
                if (nextToken != JsonToken.FIELD_NAME) {
                    break;
                }
                final String currentName = jsonParser.getCurrentName();
                jsonParser.nextToken();
                final SettableBeanProperty find = this._beanProperties.find(currentName);
                while (true) {
                    Label_0177: {
                        if (find == null) {
                            break Label_0177;
                        }
                        try {
                            find.deserializeAndSet(jsonParser, deserializationContext, o);
                            nextToken = jsonParser.nextToken();
                            continue Label_0153_Outer;
                        }
                        catch (Exception ex) {
                            this.wrapAndThrow(ex, o, currentName, deserializationContext);
                            continue;
                        }
                    }
                    if (this._ignorableProps != null && this._ignorableProps.contains(currentName)) {
                        jsonParser.skipChildren();
                        continue;
                    }
                    if (this._anySetter != null) {
                        this._anySetter.deserializeAndSet(jsonParser, deserializationContext, o, currentName);
                        continue;
                    }
                    this.handleUnknownProperty(jsonParser, deserializationContext, o, currentName);
                    continue;
                }
            }
        }
        return deserializeWithUnwrapped;
    }
    
    @Override
    public Object deserializeFromObject(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Object deserializeWithUnwrapped = null;
        if (this._nonStandardCreation) {
            if (this._unwrappedPropertyHandler != null) {
                deserializeWithUnwrapped = this.deserializeWithUnwrapped(jsonParser, deserializationContext);
            }
            else {
                if (this._externalTypeIdHandler != null) {
                    return this.deserializeWithExternalTypeId(jsonParser, deserializationContext);
                }
                return this.deserializeFromObjectUsingNonDefault(jsonParser, deserializationContext);
            }
        }
        else {
            final Object usingDefault = this._valueInstantiator.createUsingDefault(deserializationContext);
            if (this._injectables != null) {
                this.injectValues(deserializationContext, usingDefault);
            }
            if (this._needViewProcesing) {
                final Class<?> activeView = deserializationContext.getActiveView();
                if (activeView != null) {
                    return this.deserializeWithView(jsonParser, deserializationContext, usingDefault, activeView);
                }
            }
        Label_0141_Outer:
            while (true) {
                deserializeWithUnwrapped = usingDefault;
                if (jsonParser.getCurrentToken() == JsonToken.END_OBJECT) {
                    break;
                }
                final String currentName = jsonParser.getCurrentName();
                jsonParser.nextToken();
                final SettableBeanProperty find = this._beanProperties.find(currentName);
                while (true) {
                    Label_0164: {
                        if (find == null) {
                            break Label_0164;
                        }
                        try {
                            find.deserializeAndSet(jsonParser, deserializationContext, usingDefault);
                            jsonParser.nextToken();
                            continue Label_0141_Outer;
                        }
                        catch (Exception ex) {
                            this.wrapAndThrow(ex, usingDefault, currentName, deserializationContext);
                            continue;
                        }
                    }
                    if (this._ignorableProps != null && this._ignorableProps.contains(currentName)) {
                        jsonParser.skipChildren();
                        continue;
                    }
                    if (this._anySetter != null) {
                        try {
                            this._anySetter.deserializeAndSet(jsonParser, deserializationContext, usingDefault, currentName);
                            continue;
                        }
                        catch (Exception ex2) {
                            this.wrapAndThrow(ex2, usingDefault, currentName, deserializationContext);
                            continue;
                        }
                        continue;
                    }
                    this.handleUnknownProperty(jsonParser, deserializationContext, usingDefault, currentName);
                    continue;
                }
            }
        }
        return deserializeWithUnwrapped;
    }
    
    protected Object deserializeUsingPropertyBasedWithExternalTypeId(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final ExternalTypeHandler start = this._externalTypeIdHandler.start();
        final PropertyBasedCreator propertyBasedCreator = this._propertyBasedCreator;
        final PropertyValueBuffer startBuilding = propertyBasedCreator.startBuilding(jsonParser, deserializationContext, this._objectIdReader);
        final TokenBuffer tokenBuffer = new TokenBuffer(jsonParser.getCodec());
        tokenBuffer.writeStartObject();
        for (JsonToken jsonToken = jsonParser.getCurrentToken(); jsonToken == JsonToken.FIELD_NAME; jsonToken = jsonParser.nextToken()) {
            final String currentName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            final SettableBeanProperty creatorProperty = propertyBasedCreator.findCreatorProperty(currentName);
            if (creatorProperty != null) {
                if (!start.handlePropertyValue(jsonParser, deserializationContext, currentName, startBuilding) && startBuilding.assignParameter(creatorProperty.getCreatorIndex(), creatorProperty.deserialize(jsonParser, deserializationContext))) {
                    JsonToken jsonToken2 = jsonParser.nextToken();
                    Object build;
                    try {
                        build = propertyBasedCreator.build(deserializationContext, startBuilding);
                        while (jsonToken2 == JsonToken.FIELD_NAME) {
                            jsonParser.nextToken();
                            tokenBuffer.copyCurrentStructure(jsonParser);
                            jsonToken2 = jsonParser.nextToken();
                        }
                    }
                    catch (Exception ex) {
                        this.wrapAndThrow(ex, this._beanType.getRawClass(), currentName, deserializationContext);
                        continue;
                    }
                    if (build.getClass() != this._beanType.getRawClass()) {
                        throw deserializationContext.mappingException("Can not create polymorphic instances with unwrapped values");
                    }
                    return start.complete(jsonParser, deserializationContext, build);
                }
            }
            else if (!startBuilding.readIdProperty(currentName)) {
                final SettableBeanProperty find = this._beanProperties.find(currentName);
                if (find != null) {
                    startBuilding.bufferProperty(find, find.deserialize(jsonParser, deserializationContext));
                }
                else if (!start.handlePropertyValue(jsonParser, deserializationContext, currentName, null)) {
                    if (this._ignorableProps != null && this._ignorableProps.contains(currentName)) {
                        jsonParser.skipChildren();
                    }
                    else if (this._anySetter != null) {
                        startBuilding.bufferAnyProperty(this._anySetter, currentName, this._anySetter.deserialize(jsonParser, deserializationContext));
                    }
                }
            }
        }
        try {
            return start.complete(jsonParser, deserializationContext, startBuilding, propertyBasedCreator);
        }
        catch (Exception ex2) {
            this.wrapInstantiationProblem(ex2, deserializationContext);
            return null;
        }
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
                        tokenBuffer.close();
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
    
    protected Object deserializeWithExternalTypeId(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Object o) throws IOException, JsonProcessingException {
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
            if (find != null) {
                if (jsonParser.getCurrentToken().isScalarValue()) {
                    start.handleTypePropertyValue(jsonParser, deserializationContext, currentName, o);
                }
                if (activeView != null && !find.visibleInView(activeView)) {
                    jsonParser.skipChildren();
                }
                else {
                    try {
                        find.deserializeAndSet(jsonParser, deserializationContext, o);
                    }
                    catch (Exception ex) {
                        this.wrapAndThrow(ex, o, currentName, deserializationContext);
                    }
                }
            }
            else if (this._ignorableProps != null && this._ignorableProps.contains(currentName)) {
                jsonParser.skipChildren();
            }
            else if (!start.handlePropertyValue(jsonParser, deserializationContext, currentName, o)) {
                if (this._anySetter != null) {
                    try {
                        this._anySetter.deserializeAndSet(jsonParser, deserializationContext, o, currentName);
                    }
                    catch (Exception ex2) {
                        this.wrapAndThrow(ex2, o, currentName, deserializationContext);
                    }
                }
                else {
                    this.handleUnknownProperty(jsonParser, deserializationContext, o, currentName);
                }
            }
            jsonParser.nextToken();
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
        final Object usingDefault = this._valueInstantiator.createUsingDefault(deserializationContext);
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
            if (find != null) {
                if (activeView != null && !find.visibleInView(activeView)) {
                    jsonParser.skipChildren();
                }
                else {
                    try {
                        find.deserializeAndSet(jsonParser, deserializationContext, usingDefault);
                    }
                    catch (Exception ex) {
                        this.wrapAndThrow(ex, usingDefault, currentName, deserializationContext);
                    }
                }
            }
            else if (this._ignorableProps != null && this._ignorableProps.contains(currentName)) {
                jsonParser.skipChildren();
            }
            else {
                tokenBuffer.writeFieldName(currentName);
                tokenBuffer.copyCurrentStructure(jsonParser);
                if (this._anySetter != null) {
                    try {
                        this._anySetter.deserializeAndSet(jsonParser, deserializationContext, usingDefault, currentName);
                    }
                    catch (Exception ex2) {
                        this.wrapAndThrow(ex2, usingDefault, currentName, deserializationContext);
                    }
                }
            }
            jsonParser.nextToken();
        }
        tokenBuffer.writeEndObject();
        this._unwrappedPropertyHandler.processUnwrapped(jsonParser, deserializationContext, usingDefault, tokenBuffer);
        return usingDefault;
    }
    
    protected Object deserializeWithUnwrapped(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Object o) throws IOException, JsonProcessingException {
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
            if (find != null) {
                if (activeView != null && !find.visibleInView(activeView)) {
                    jsonParser.skipChildren();
                }
                else {
                    try {
                        find.deserializeAndSet(jsonParser, deserializationContext, o);
                    }
                    catch (Exception ex) {
                        this.wrapAndThrow(ex, o, currentName, deserializationContext);
                    }
                }
            }
            else if (this._ignorableProps != null && this._ignorableProps.contains(currentName)) {
                jsonParser.skipChildren();
            }
            else {
                tokenBuffer.writeFieldName(currentName);
                tokenBuffer.copyCurrentStructure(jsonParser);
                if (this._anySetter != null) {
                    this._anySetter.deserializeAndSet(jsonParser, deserializationContext, o, currentName);
                }
            }
            jsonToken = jsonParser.nextToken();
        }
        tokenBuffer.writeEndObject();
        this._unwrappedPropertyHandler.processUnwrapped(jsonParser, deserializationContext, o, tokenBuffer);
        return o;
    }
    
    protected final Object deserializeWithView(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Object o, final Class<?> clazz) throws IOException, JsonProcessingException {
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
                        find.deserializeAndSet(jsonParser, deserializationContext, o);
                    }
                    catch (Exception ex) {
                        this.wrapAndThrow(ex, o, currentName, deserializationContext);
                    }
                }
            }
            else if (this._ignorableProps != null && this._ignorableProps.contains(currentName)) {
                jsonParser.skipChildren();
            }
            else if (this._anySetter != null) {
                this._anySetter.deserializeAndSet(jsonParser, deserializationContext, o, currentName);
            }
            else {
                this.handleUnknownProperty(jsonParser, deserializationContext, o, currentName);
            }
        }
        return o;
    }
    
    @Override
    public JsonDeserializer<Object> unwrappingDeserializer(final NameTransformer nameTransformer) {
        if (this.getClass() != BeanDeserializer.class) {
            return this;
        }
        return new BeanDeserializer(this, nameTransformer);
    }
    
    @Override
    public BeanDeserializer withIgnorableProperties(final HashSet<String> set) {
        return new BeanDeserializer(this, set);
    }
    
    @Override
    public BeanDeserializer withObjectIdReader(final ObjectIdReader objectIdReader) {
        return new BeanDeserializer(this, objectIdReader);
    }
}
