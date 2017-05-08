// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import java.io.InputStream;
import java.io.DataInputStream;
import java.io.ByteArrayInputStream;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import java.util.concurrent.atomic.AtomicReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.nio.charset.Charset;
import java.net.InetAddress;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.Currency;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.UUID;
import java.util.HashSet;

public class JdkDeserializers
{
    private static final HashSet<String> _classNames;
    
    static {
        int i = 0;
        _classNames = new HashSet<String>();
        for (Class[] array = { UUID.class, URL.class, URI.class, File.class, Currency.class, Pattern.class, Locale.class, InetAddress.class, Charset.class, AtomicBoolean.class, Class.class, StackTraceElement.class }; i < array.length; ++i) {
            JdkDeserializers._classNames.add(array[i].getName());
        }
    }
    
    public static JsonDeserializer<?> find(final Class<?> clazz, final String s) {
        if (!JdkDeserializers._classNames.contains(s)) {
            return null;
        }
        if (clazz == URI.class) {
            return URIDeserializer.instance;
        }
        if (clazz == URL.class) {
            return URLDeserializer.instance;
        }
        if (clazz == File.class) {
            return FileDeserializer.instance;
        }
        if (clazz == UUID.class) {
            return UUIDDeserializer.instance;
        }
        if (clazz == Currency.class) {
            return CurrencyDeserializer.instance;
        }
        if (clazz == Pattern.class) {
            return PatternDeserializer.instance;
        }
        if (clazz == Locale.class) {
            return LocaleDeserializer.instance;
        }
        if (clazz == InetAddress.class) {
            return InetAddressDeserializer.instance;
        }
        if (clazz == Charset.class) {
            return CharsetDeserializer.instance;
        }
        if (clazz == Class.class) {
            return ClassDeserializer.instance;
        }
        if (clazz == StackTraceElement.class) {
            return StackTraceElementDeserializer.instance;
        }
        if (clazz == AtomicBoolean.class) {
            return AtomicBooleanDeserializer.instance;
        }
        throw new IllegalArgumentException("Internal error: can't find deserializer for " + s);
    }
    
    public static class AtomicBooleanDeserializer extends StdScalarDeserializer<AtomicBoolean>
    {
        public static final AtomicBooleanDeserializer instance;
        
        static {
            instance = new AtomicBooleanDeserializer();
        }
        
        public AtomicBooleanDeserializer() {
            super(AtomicBoolean.class);
        }
        
        @Override
        public AtomicBoolean deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return new AtomicBoolean(this._parseBooleanPrimitive(jsonParser, deserializationContext));
        }
    }
    
    public static class AtomicReferenceDeserializer extends StdScalarDeserializer<AtomicReference<?>> implements ContextualDeserializer
    {
        protected final JavaType _referencedType;
        protected final JsonDeserializer<?> _valueDeserializer;
        
        public AtomicReferenceDeserializer(final JavaType javaType) {
            this(javaType, null);
        }
        
        public AtomicReferenceDeserializer(final JavaType referencedType, final JsonDeserializer<?> valueDeserializer) {
            super(AtomicReference.class);
            this._referencedType = referencedType;
            this._valueDeserializer = valueDeserializer;
        }
        
        @Override
        public JsonDeserializer<?> createContextual(final DeserializationContext deserializationContext, final BeanProperty beanProperty) throws JsonMappingException {
            if (this._valueDeserializer != null) {
                return this;
            }
            return new AtomicReferenceDeserializer(this._referencedType, deserializationContext.findContextualValueDeserializer(this._referencedType, beanProperty));
        }
        
        @Override
        public AtomicReference<?> deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return new AtomicReference<Object>(this._valueDeserializer.deserialize(jsonParser, deserializationContext));
        }
    }
    
    protected static class CharsetDeserializer extends FromStringDeserializer<Charset>
    {
        public static final CharsetDeserializer instance;
        
        static {
            instance = new CharsetDeserializer();
        }
        
        public CharsetDeserializer() {
            super(Charset.class);
        }
        
        @Override
        protected Charset _deserialize(final String s, final DeserializationContext deserializationContext) throws IOException {
            return Charset.forName(s);
        }
    }
    
    public static class CurrencyDeserializer extends FromStringDeserializer<Currency>
    {
        public static final CurrencyDeserializer instance;
        
        static {
            instance = new CurrencyDeserializer();
        }
        
        public CurrencyDeserializer() {
            super(Currency.class);
        }
        
        @Override
        protected Currency _deserialize(final String s, final DeserializationContext deserializationContext) throws IllegalArgumentException {
            return Currency.getInstance(s);
        }
    }
    
    public static class FileDeserializer extends FromStringDeserializer<File>
    {
        public static final FileDeserializer instance;
        
        static {
            instance = new FileDeserializer();
        }
        
        public FileDeserializer() {
            super(File.class);
        }
        
        @Override
        protected File _deserialize(final String s, final DeserializationContext deserializationContext) {
            return new File(s);
        }
    }
    
    protected static class InetAddressDeserializer extends FromStringDeserializer<InetAddress>
    {
        public static final InetAddressDeserializer instance;
        
        static {
            instance = new InetAddressDeserializer();
        }
        
        public InetAddressDeserializer() {
            super(InetAddress.class);
        }
        
        @Override
        protected InetAddress _deserialize(final String s, final DeserializationContext deserializationContext) throws IOException {
            return InetAddress.getByName(s);
        }
    }
    
    protected static class LocaleDeserializer extends FromStringDeserializer<Locale>
    {
        public static final LocaleDeserializer instance;
        
        static {
            instance = new LocaleDeserializer();
        }
        
        public LocaleDeserializer() {
            super(Locale.class);
        }
        
        @Override
        protected Locale _deserialize(String substring, final DeserializationContext deserializationContext) throws IOException {
            final int index = substring.indexOf(95);
            if (index < 0) {
                return new Locale(substring);
            }
            final String substring2 = substring.substring(0, index);
            substring = substring.substring(index + 1);
            final int index2 = substring.indexOf(95);
            if (index2 < 0) {
                return new Locale(substring2, substring);
            }
            return new Locale(substring2, substring.substring(0, index2), substring.substring(index2 + 1));
        }
    }
    
    public static class PatternDeserializer extends FromStringDeserializer<Pattern>
    {
        public static final PatternDeserializer instance;
        
        static {
            instance = new PatternDeserializer();
        }
        
        public PatternDeserializer() {
            super(Pattern.class);
        }
        
        @Override
        protected Pattern _deserialize(final String s, final DeserializationContext deserializationContext) throws IllegalArgumentException {
            return Pattern.compile(s);
        }
    }
    
    public static class StackTraceElementDeserializer extends StdScalarDeserializer<StackTraceElement>
    {
        public static final StackTraceElementDeserializer instance;
        
        static {
            instance = new StackTraceElementDeserializer();
        }
        
        public StackTraceElementDeserializer() {
            super(StackTraceElement.class);
        }
        
        @Override
        public StackTraceElement deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            final JsonToken currentToken = jsonParser.getCurrentToken();
            if (currentToken != JsonToken.START_OBJECT) {
                throw deserializationContext.mappingException(this._valueClass, currentToken);
            }
            String text = "";
            String text2 = "";
            String text3 = "";
            int intValue = -1;
            while (true) {
                final JsonToken nextValue = jsonParser.nextValue();
                if (nextValue == JsonToken.END_OBJECT) {
                    return new StackTraceElement(text, text2, text3, intValue);
                }
                final String currentName = jsonParser.getCurrentName();
                if ("className".equals(currentName)) {
                    text = jsonParser.getText();
                }
                else if ("fileName".equals(currentName)) {
                    text3 = jsonParser.getText();
                }
                else if ("lineNumber".equals(currentName)) {
                    if (!nextValue.isNumeric()) {
                        throw JsonMappingException.from(jsonParser, "Non-numeric token (" + nextValue + ") for property 'lineNumber'");
                    }
                    intValue = jsonParser.getIntValue();
                }
                else if ("methodName".equals(currentName)) {
                    text2 = jsonParser.getText();
                }
                else {
                    if ("nativeMethod".equals(currentName)) {
                        continue;
                    }
                    this.handleUnknownProperty(jsonParser, deserializationContext, this._valueClass, currentName);
                }
            }
        }
    }
    
    public static class URIDeserializer extends FromStringDeserializer<URI>
    {
        public static final URIDeserializer instance;
        
        static {
            instance = new URIDeserializer();
        }
        
        public URIDeserializer() {
            super(URI.class);
        }
        
        @Override
        protected URI _deserialize(final String s, final DeserializationContext deserializationContext) throws IllegalArgumentException {
            return URI.create(s);
        }
    }
    
    public static class URLDeserializer extends FromStringDeserializer<URL>
    {
        public static final URLDeserializer instance;
        
        static {
            instance = new URLDeserializer();
        }
        
        public URLDeserializer() {
            super(URL.class);
        }
        
        @Override
        protected URL _deserialize(final String s, final DeserializationContext deserializationContext) throws IOException {
            return new URL(s);
        }
    }
    
    public static class UUIDDeserializer extends FromStringDeserializer<UUID>
    {
        public static final UUIDDeserializer instance;
        
        static {
            instance = new UUIDDeserializer();
        }
        
        public UUIDDeserializer() {
            super(UUID.class);
        }
        
        @Override
        protected UUID _deserialize(final String s, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return UUID.fromString(s);
        }
        
        @Override
        protected UUID _deserializeEmbedded(final Object o, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            if (o instanceof byte[]) {
                final byte[] array = (byte[])o;
                if (array.length != 16) {
                    deserializationContext.mappingException("Can only construct UUIDs from 16 byte arrays; got " + array.length + " bytes");
                }
                final DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(array));
                return new UUID(dataInputStream.readLong(), dataInputStream.readLong());
            }
            super._deserializeEmbedded(o, deserializationContext);
            return null;
        }
    }
}
