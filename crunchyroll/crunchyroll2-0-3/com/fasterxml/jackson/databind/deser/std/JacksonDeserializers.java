// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JacksonDeserializers
{
    public static JsonDeserializer<?> find(final Class<?> clazz) {
        if (clazz == TokenBuffer.class) {
            return TokenBufferDeserializer.instance;
        }
        if (JavaType.class.isAssignableFrom(clazz)) {
            return JavaTypeDeserializer.instance;
        }
        return null;
    }
    
    public static ValueInstantiator findValueInstantiator(final DeserializationConfig deserializationConfig, final BeanDescription beanDescription) {
        if (beanDescription.getBeanClass() == JsonLocation.class) {
            return JsonLocationInstantiator.instance;
        }
        return null;
    }
    
    public static class JavaTypeDeserializer extends StdScalarDeserializer<JavaType>
    {
        public static final JavaTypeDeserializer instance;
        
        static {
            instance = new JavaTypeDeserializer();
        }
        
        public JavaTypeDeserializer() {
            super(JavaType.class);
        }
        
        @Override
        public JavaType deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            final JsonToken currentToken = jsonParser.getCurrentToken();
            if (currentToken == JsonToken.VALUE_STRING) {
                final String trim = jsonParser.getText().trim();
                if (trim.length() == 0) {
                    return this.getEmptyValue();
                }
                return deserializationContext.getTypeFactory().constructFromCanonical(trim);
            }
            else {
                if (currentToken == JsonToken.VALUE_EMBEDDED_OBJECT) {
                    return (JavaType)jsonParser.getEmbeddedObject();
                }
                throw deserializationContext.mappingException(this._valueClass);
            }
        }
    }
    
    public static class JsonLocationInstantiator extends ValueInstantiator
    {
        public static final JsonLocationInstantiator instance;
        
        static {
            instance = new JsonLocationInstantiator();
        }
        
        private static final int _int(final Object o) {
            if (o == null) {
                return 0;
            }
            return ((Number)o).intValue();
        }
        
        private static final long _long(final Object o) {
            if (o == null) {
                return 0L;
            }
            return ((Number)o).longValue();
        }
        
        private static CreatorProperty creatorProp(final String s, final JavaType javaType, final int n) {
            return new CreatorProperty(s, javaType, null, null, null, null, n, null, true);
        }
        
        @Override
        public boolean canCreateFromObjectWith() {
            return true;
        }
        
        @Override
        public Object createFromObjectWith(final DeserializationContext deserializationContext, final Object[] array) {
            return new JsonLocation(array[0], _long(array[1]), _long(array[2]), _int(array[3]), _int(array[4]));
        }
        
        @Override
        public CreatorProperty[] getFromObjectArguments(final DeserializationConfig deserializationConfig) {
            final JavaType constructType = deserializationConfig.constructType(Integer.TYPE);
            final JavaType constructType2 = deserializationConfig.constructType(Long.TYPE);
            return new CreatorProperty[] { creatorProp("sourceRef", deserializationConfig.constructType(Object.class), 0), creatorProp("byteOffset", constructType2, 1), creatorProp("charOffset", constructType2, 2), creatorProp("lineNr", constructType, 3), creatorProp("columnNr", constructType, 4) };
        }
        
        @Override
        public String getValueTypeDesc() {
            return JsonLocation.class.getName();
        }
    }
    
    @JacksonStdImpl
    public static class TokenBufferDeserializer extends StdScalarDeserializer<TokenBuffer>
    {
        public static final TokenBufferDeserializer instance;
        
        static {
            instance = new TokenBufferDeserializer();
        }
        
        public TokenBufferDeserializer() {
            super(TokenBuffer.class);
        }
        
        @Override
        public TokenBuffer deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            final TokenBuffer tokenBuffer = new TokenBuffer(jsonParser.getCodec());
            tokenBuffer.copyCurrentStructure(jsonParser);
            return tokenBuffer;
        }
    }
}
