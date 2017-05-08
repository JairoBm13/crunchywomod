// 
// Decompiled by Procyon v0.5.30
// 

package com.google.gson;

import java.util.Iterator;
import java.io.StringReader;
import com.google.gson.internal.Primitives;
import java.io.Reader;
import java.io.EOFException;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import com.google.gson.stream.MalformedJsonException;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.internal.bind.SqlDateTypeAdapter;
import com.google.gson.internal.bind.TimeTypeAdapter;
import com.google.gson.internal.bind.DateTypeAdapter;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Collection;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.Type;
import java.util.Collections;
import com.google.gson.internal.Excluder;
import java.util.List;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import java.util.Map;

public final class Gson
{
    private final ThreadLocal<Map<TypeToken<?>, FutureTypeAdapter<?>>> calls;
    private final ConstructorConstructor constructorConstructor;
    final JsonDeserializationContext deserializationContext;
    private final List<TypeAdapterFactory> factories;
    private final boolean generateNonExecutableJson;
    private final boolean htmlSafe;
    private final boolean prettyPrinting;
    final JsonSerializationContext serializationContext;
    private final boolean serializeNulls;
    private final Map<TypeToken<?>, TypeAdapter<?>> typeTokenCache;
    
    public Gson() {
        this(Excluder.DEFAULT, FieldNamingPolicy.IDENTITY, Collections.emptyMap(), false, false, false, true, false, false, LongSerializationPolicy.DEFAULT, Collections.emptyList());
    }
    
    Gson(final Excluder excluder, final FieldNamingStrategy fieldNamingStrategy, final Map<Type, InstanceCreator<?>> map, final boolean serializeNulls, final boolean b, final boolean generateNonExecutableJson, final boolean htmlSafe, final boolean prettyPrinting, final boolean b2, final LongSerializationPolicy longSerializationPolicy, final List<TypeAdapterFactory> list) {
        this.calls = new ThreadLocal<Map<TypeToken<?>, FutureTypeAdapter<?>>>();
        this.typeTokenCache = Collections.synchronizedMap(new HashMap<TypeToken<?>, TypeAdapter<?>>());
        this.deserializationContext = new JsonDeserializationContext() {
            @Override
            public <T> T deserialize(final JsonElement jsonElement, final Type type) throws JsonParseException {
                return Gson.this.fromJson(jsonElement, type);
            }
        };
        this.serializationContext = new JsonSerializationContext() {};
        this.constructorConstructor = new ConstructorConstructor(map);
        this.serializeNulls = serializeNulls;
        this.generateNonExecutableJson = generateNonExecutableJson;
        this.htmlSafe = htmlSafe;
        this.prettyPrinting = prettyPrinting;
        final ArrayList<TypeAdapterFactory> list2 = new ArrayList<TypeAdapterFactory>();
        list2.add(TypeAdapters.JSON_ELEMENT_FACTORY);
        list2.add(ObjectTypeAdapter.FACTORY);
        list2.add(excluder);
        list2.addAll((Collection<?>)list);
        list2.add(TypeAdapters.STRING_FACTORY);
        list2.add(TypeAdapters.INTEGER_FACTORY);
        list2.add(TypeAdapters.BOOLEAN_FACTORY);
        list2.add(TypeAdapters.BYTE_FACTORY);
        list2.add(TypeAdapters.SHORT_FACTORY);
        list2.add(TypeAdapters.newFactory(Long.TYPE, Long.class, this.longAdapter(longSerializationPolicy)));
        list2.add(TypeAdapters.newFactory(Double.TYPE, Double.class, this.doubleAdapter(b2)));
        list2.add(TypeAdapters.newFactory(Float.TYPE, Float.class, this.floatAdapter(b2)));
        list2.add(TypeAdapters.NUMBER_FACTORY);
        list2.add(TypeAdapters.CHARACTER_FACTORY);
        list2.add(TypeAdapters.STRING_BUILDER_FACTORY);
        list2.add(TypeAdapters.STRING_BUFFER_FACTORY);
        list2.add(TypeAdapters.newFactory(BigDecimal.class, TypeAdapters.BIG_DECIMAL));
        list2.add(TypeAdapters.newFactory(BigInteger.class, TypeAdapters.BIG_INTEGER));
        list2.add(TypeAdapters.URL_FACTORY);
        list2.add(TypeAdapters.URI_FACTORY);
        list2.add(TypeAdapters.UUID_FACTORY);
        list2.add(TypeAdapters.LOCALE_FACTORY);
        list2.add(TypeAdapters.INET_ADDRESS_FACTORY);
        list2.add(TypeAdapters.BIT_SET_FACTORY);
        list2.add(DateTypeAdapter.FACTORY);
        list2.add(TypeAdapters.CALENDAR_FACTORY);
        list2.add(TimeTypeAdapter.FACTORY);
        list2.add(SqlDateTypeAdapter.FACTORY);
        list2.add(TypeAdapters.TIMESTAMP_FACTORY);
        list2.add(ArrayTypeAdapter.FACTORY);
        list2.add(TypeAdapters.ENUM_FACTORY);
        list2.add(TypeAdapters.CLASS_FACTORY);
        list2.add(new CollectionTypeAdapterFactory(this.constructorConstructor));
        list2.add(new MapTypeAdapterFactory(this.constructorConstructor, b));
        list2.add(new ReflectiveTypeAdapterFactory(this.constructorConstructor, fieldNamingStrategy, excluder));
        this.factories = (List<TypeAdapterFactory>)Collections.unmodifiableList((List<?>)list2);
    }
    
    private static void assertFullConsumption(final Object o, final JsonReader jsonReader) {
        if (o != null) {
            try {
                if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                    throw new JsonIOException("JSON document was not fully consumed.");
                }
            }
            catch (MalformedJsonException ex) {
                throw new JsonSyntaxException(ex);
            }
            catch (IOException ex2) {
                throw new JsonIOException(ex2);
            }
        }
    }
    
    private void checkValidFloatingPoint(final double n) {
        if (Double.isNaN(n) || Double.isInfinite(n)) {
            throw new IllegalArgumentException(n + " is not a valid double value as per JSON specification. To override this" + " behavior, use GsonBuilder.serializeSpecialFloatingPointValues() method.");
        }
    }
    
    private TypeAdapter<Number> doubleAdapter(final boolean b) {
        if (b) {
            return TypeAdapters.DOUBLE;
        }
        return new TypeAdapter<Number>() {
            @Override
            public Double read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return jsonReader.nextDouble();
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final Number n) throws IOException {
                if (n == null) {
                    jsonWriter.nullValue();
                    return;
                }
                Gson.this.checkValidFloatingPoint(n.doubleValue());
                jsonWriter.value(n);
            }
        };
    }
    
    private TypeAdapter<Number> floatAdapter(final boolean b) {
        if (b) {
            return TypeAdapters.FLOAT;
        }
        return new TypeAdapter<Number>() {
            @Override
            public Float read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return (float)jsonReader.nextDouble();
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final Number n) throws IOException {
                if (n == null) {
                    jsonWriter.nullValue();
                    return;
                }
                Gson.this.checkValidFloatingPoint(n.floatValue());
                jsonWriter.value(n);
            }
        };
    }
    
    private TypeAdapter<Number> longAdapter(final LongSerializationPolicy longSerializationPolicy) {
        if (longSerializationPolicy == LongSerializationPolicy.DEFAULT) {
            return TypeAdapters.LONG;
        }
        return new TypeAdapter<Number>() {
            @Override
            public Number read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return jsonReader.nextLong();
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final Number n) throws IOException {
                if (n == null) {
                    jsonWriter.nullValue();
                    return;
                }
                jsonWriter.value(n.toString());
            }
        };
    }
    
    public <T> T fromJson(final JsonElement jsonElement, final Type type) throws JsonSyntaxException {
        if (jsonElement == null) {
            return null;
        }
        return this.fromJson(new JsonTreeReader(jsonElement), type);
    }
    
    public <T> T fromJson(final JsonReader jsonReader, final Type type) throws JsonIOException, JsonSyntaxException {
        boolean b = true;
        jsonReader.isLenient();
        jsonReader.setLenient(true);
        try {
            jsonReader.peek();
            b = false;
            return (T)this.getAdapter(TypeToken.get(type)).read(jsonReader);
        }
        catch (EOFException ex) {
            if (b) {
                return null;
            }
            throw new JsonSyntaxException(ex);
        }
        catch (IllegalStateException ex3) {}
        catch (IOException ex4) {
            final IOException ex2;
            throw new JsonSyntaxException(ex2);
        }
    }
    
    public <T> T fromJson(final Reader reader, final Type type) throws JsonIOException, JsonSyntaxException {
        final JsonReader jsonReader = new JsonReader(reader);
        final Object fromJson = this.fromJson(jsonReader, type);
        assertFullConsumption(fromJson, jsonReader);
        return (T)fromJson;
    }
    
    public <T> T fromJson(final String s, final Class<T> clazz) throws JsonSyntaxException {
        return Primitives.wrap(clazz).cast(this.fromJson(s, (Type)clazz));
    }
    
    public <T> T fromJson(final String s, final Type type) throws JsonSyntaxException {
        if (s == null) {
            return null;
        }
        return this.fromJson(new StringReader(s), type);
    }
    
    public <T> TypeAdapter<T> getAdapter(final TypeToken<T> typeToken) {
        final TypeAdapter<?> typeAdapter = this.typeTokenCache.get(typeToken);
        if (typeAdapter != null) {
            return (TypeAdapter<T>)typeAdapter;
        }
        final Map<TypeToken<?>, FutureTypeAdapter<?>> map = this.calls.get();
        boolean b = false;
        Map<TypeToken<T>, FutureTypeAdapter<?>> map2;
        if ((map2 = (Map<TypeToken<T>, FutureTypeAdapter<?>>)map) == null) {
            map2 = (Map<TypeToken<T>, FutureTypeAdapter<?>>)new HashMap<TypeToken<T>, FutureTypeAdapter<Object>>();
            this.calls.set((Map<TypeToken<?>, FutureTypeAdapter<?>>)map2);
            b = true;
        }
        final FutureTypeAdapter<?> futureTypeAdapter = map2.get(typeToken);
        if (futureTypeAdapter != null) {
            return (TypeAdapter<T>)futureTypeAdapter;
        }
        try {
            final FutureTypeAdapter<Object> futureTypeAdapter2 = new FutureTypeAdapter<Object>();
            map2.put(typeToken, futureTypeAdapter2);
            final Iterator<TypeAdapterFactory> iterator = this.factories.iterator();
            while (iterator.hasNext()) {
                final TypeAdapter<?> create = iterator.next().create(this, (TypeToken<?>)typeToken);
                if (create != null) {
                    futureTypeAdapter2.setDelegate(create);
                    this.typeTokenCache.put(typeToken, create);
                    return (TypeAdapter<T>)create;
                }
            }
            throw new IllegalArgumentException("GSON cannot handle " + typeToken);
        }
        finally {
            map2.remove(typeToken);
            if (b) {
                this.calls.remove();
            }
        }
    }
    
    public <T> TypeAdapter<T> getAdapter(final Class<T> clazz) {
        return this.getAdapter((TypeToken<T>)TypeToken.get((Class<T>)clazz));
    }
    
    public <T> TypeAdapter<T> getDelegateAdapter(final TypeAdapterFactory typeAdapterFactory, final TypeToken<T> typeToken) {
        int n = 0;
        for (final TypeAdapterFactory typeAdapterFactory2 : this.factories) {
            if (n == 0) {
                if (typeAdapterFactory2 != typeAdapterFactory) {
                    continue;
                }
                n = 1;
            }
            else {
                final TypeAdapter<T> create = typeAdapterFactory2.create(this, typeToken);
                if (create != null) {
                    return create;
                }
                continue;
            }
        }
        throw new IllegalArgumentException("GSON cannot serialize " + typeToken);
    }
    
    @Override
    public String toString() {
        return "{serializeNulls:" + this.serializeNulls + "factories:" + this.factories + ",instanceCreators:" + this.constructorConstructor + "}";
    }
    
    static class FutureTypeAdapter<T> extends TypeAdapter<T>
    {
        private TypeAdapter<T> delegate;
        
        @Override
        public T read(final JsonReader jsonReader) throws IOException {
            if (this.delegate == null) {
                throw new IllegalStateException();
            }
            return this.delegate.read(jsonReader);
        }
        
        public void setDelegate(final TypeAdapter<T> delegate) {
            if (this.delegate != null) {
                throw new AssertionError();
            }
            this.delegate = delegate;
        }
        
        @Override
        public void write(final JsonWriter jsonWriter, final T t) throws IOException {
            if (this.delegate == null) {
                throw new IllegalStateException();
            }
            this.delegate.write(jsonWriter, t);
        }
    }
}
