// 
// Decompiled by Procyon v0.5.30
// 

package com.google.gson.internal;

import com.google.gson.FieldAttributes;
import com.google.gson.annotations.Expose;
import java.lang.reflect.Field;
import java.util.Iterator;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import com.google.gson.stream.JsonReader;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.annotations.Until;
import com.google.gson.annotations.Since;
import java.util.Collections;
import com.google.gson.ExclusionStrategy;
import java.util.List;
import com.google.gson.TypeAdapterFactory;

public final class Excluder implements TypeAdapterFactory, Cloneable
{
    public static final Excluder DEFAULT;
    private List<ExclusionStrategy> deserializationStrategies;
    private int modifiers;
    private boolean requireExpose;
    private List<ExclusionStrategy> serializationStrategies;
    private boolean serializeInnerClasses;
    private double version;
    
    static {
        DEFAULT = new Excluder();
    }
    
    public Excluder() {
        this.version = -1.0;
        this.modifiers = 136;
        this.serializeInnerClasses = true;
        this.serializationStrategies = Collections.emptyList();
        this.deserializationStrategies = Collections.emptyList();
    }
    
    private boolean isAnonymousOrLocal(final Class<?> clazz) {
        return !Enum.class.isAssignableFrom(clazz) && (clazz.isAnonymousClass() || clazz.isLocalClass());
    }
    
    private boolean isInnerClass(final Class<?> clazz) {
        return clazz.isMemberClass() && !this.isStatic(clazz);
    }
    
    private boolean isStatic(final Class<?> clazz) {
        return (clazz.getModifiers() & 0x8) != 0x0;
    }
    
    private boolean isValidSince(final Since since) {
        return since == null || since.value() <= this.version;
    }
    
    private boolean isValidUntil(final Until until) {
        return until == null || until.value() > this.version;
    }
    
    private boolean isValidVersion(final Since since, final Until until) {
        return this.isValidSince(since) && this.isValidUntil(until);
    }
    
    @Override
    protected Excluder clone() {
        try {
            return (Excluder)super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }
    
    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
        final Class<? super T> rawType = typeToken.getRawType();
        final boolean excludeClass = this.excludeClass(rawType, true);
        final boolean excludeClass2 = this.excludeClass(rawType, false);
        if (!excludeClass && !excludeClass2) {
            return null;
        }
        return new TypeAdapter<T>() {
            private TypeAdapter<T> delegate;
            
            private TypeAdapter<T> delegate() {
                final TypeAdapter<T> delegate = this.delegate;
                if (delegate != null) {
                    return delegate;
                }
                return this.delegate = gson.getDelegateAdapter(Excluder.this, typeToken);
            }
            
            @Override
            public T read(final JsonReader jsonReader) throws IOException {
                if (excludeClass2) {
                    jsonReader.skipValue();
                    return null;
                }
                return this.delegate().read(jsonReader);
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final T t) throws IOException {
                if (excludeClass) {
                    jsonWriter.nullValue();
                    return;
                }
                this.delegate().write(jsonWriter, t);
            }
        };
    }
    
    public boolean excludeClass(final Class<?> clazz, final boolean b) {
        if (this.version != -1.0 && !this.isValidVersion(clazz.getAnnotation(Since.class), clazz.getAnnotation(Until.class))) {
            return true;
        }
        if (!this.serializeInnerClasses && this.isInnerClass(clazz)) {
            return true;
        }
        if (this.isAnonymousOrLocal(clazz)) {
            return true;
        }
        List<ExclusionStrategy> list;
        if (b) {
            list = this.serializationStrategies;
        }
        else {
            list = this.deserializationStrategies;
        }
        final Iterator<ExclusionStrategy> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().shouldSkipClass(clazz)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean excludeField(final Field field, final boolean b) {
        if ((this.modifiers & field.getModifiers()) != 0x0) {
            return true;
        }
        if (this.version != -1.0 && !this.isValidVersion(field.getAnnotation(Since.class), field.getAnnotation(Until.class))) {
            return true;
        }
        if (field.isSynthetic()) {
            return true;
        }
        Label_0106: {
            if (this.requireExpose) {
                final Expose expose = field.getAnnotation(Expose.class);
                if (expose != null) {
                    if (b) {
                        if (expose.serialize()) {
                            break Label_0106;
                        }
                    }
                    else if (expose.deserialize()) {
                        break Label_0106;
                    }
                }
                return true;
            }
        }
        if (!this.serializeInnerClasses && this.isInnerClass(field.getType())) {
            return true;
        }
        if (this.isAnonymousOrLocal(field.getType())) {
            return true;
        }
        List<ExclusionStrategy> list;
        if (b) {
            list = this.serializationStrategies;
        }
        else {
            list = this.deserializationStrategies;
        }
        if (!list.isEmpty()) {
            final FieldAttributes fieldAttributes = new FieldAttributes(field);
            final Iterator<ExclusionStrategy> iterator = list.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().shouldSkipField(fieldAttributes)) {
                    return true;
                }
            }
        }
        return false;
    }
}
