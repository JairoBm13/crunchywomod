// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;

public abstract class FilteredBeanPropertyWriter
{
    public static BeanPropertyWriter constructViewBased(final BeanPropertyWriter beanPropertyWriter, final Class<?>[] array) {
        if (array.length == 1) {
            return new SingleView(beanPropertyWriter, array[0]);
        }
        return new MultiView(beanPropertyWriter, array);
    }
    
    private static final class MultiView extends BeanPropertyWriter
    {
        protected final BeanPropertyWriter _delegate;
        protected final Class<?>[] _views;
        
        protected MultiView(final BeanPropertyWriter delegate, final Class<?>[] views) {
            super(delegate);
            this._delegate = delegate;
            this._views = views;
        }
        
        @Override
        public void assignNullSerializer(final JsonSerializer<Object> jsonSerializer) {
            this._delegate.assignNullSerializer(jsonSerializer);
        }
        
        @Override
        public void assignSerializer(final JsonSerializer<Object> jsonSerializer) {
            this._delegate.assignSerializer(jsonSerializer);
        }
        
        @Override
        public MultiView rename(final NameTransformer nameTransformer) {
            return new MultiView(this._delegate.rename(nameTransformer), this._views);
        }
        
        @Override
        public void serializeAsColumn(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws Exception {
            final Class<?> activeView = serializerProvider.getActiveView();
            if (activeView != null) {
                int n;
                int length;
                for (n = 0, length = this._views.length; n < length && !this._views[n].isAssignableFrom(activeView); ++n) {}
                if (n == length) {
                    this._delegate.serializeAsPlaceholder(o, jsonGenerator, serializerProvider);
                    return;
                }
            }
            this._delegate.serializeAsColumn(o, jsonGenerator, serializerProvider);
        }
        
        @Override
        public void serializeAsField(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws Exception {
            final Class<?> activeView = serializerProvider.getActiveView();
            if (activeView != null) {
                int n;
                int length;
                for (n = 0, length = this._views.length; n < length && !this._views[n].isAssignableFrom(activeView); ++n) {}
                if (n == length) {
                    return;
                }
            }
            this._delegate.serializeAsField(o, jsonGenerator, serializerProvider);
        }
    }
    
    private static final class SingleView extends BeanPropertyWriter
    {
        protected final BeanPropertyWriter _delegate;
        protected final Class<?> _view;
        
        protected SingleView(final BeanPropertyWriter delegate, final Class<?> view) {
            super(delegate);
            this._delegate = delegate;
            this._view = view;
        }
        
        @Override
        public void assignNullSerializer(final JsonSerializer<Object> jsonSerializer) {
            this._delegate.assignNullSerializer(jsonSerializer);
        }
        
        @Override
        public void assignSerializer(final JsonSerializer<Object> jsonSerializer) {
            this._delegate.assignSerializer(jsonSerializer);
        }
        
        @Override
        public SingleView rename(final NameTransformer nameTransformer) {
            return new SingleView(this._delegate.rename(nameTransformer), this._view);
        }
        
        @Override
        public void serializeAsColumn(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws Exception {
            final Class<?> activeView = serializerProvider.getActiveView();
            if (activeView == null || this._view.isAssignableFrom(activeView)) {
                this._delegate.serializeAsColumn(o, jsonGenerator, serializerProvider);
                return;
            }
            this._delegate.serializeAsPlaceholder(o, jsonGenerator, serializerProvider);
        }
        
        @Override
        public void serializeAsField(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws Exception {
            final Class<?> activeView = serializerProvider.getActiveView();
            if (activeView == null || this._view.isAssignableFrom(activeView)) {
                this._delegate.serializeAsField(o, jsonGenerator, serializerProvider);
            }
        }
    }
}
