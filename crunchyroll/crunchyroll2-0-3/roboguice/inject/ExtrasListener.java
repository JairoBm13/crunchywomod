// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import java.io.Serializable;
import android.os.Bundle;
import roboguice.RoboGuice;
import android.app.Activity;
import com.google.inject.Key;
import com.google.inject.util.Types;
import java.lang.reflect.Type;
import com.google.inject.Injector;
import java.lang.reflect.Field;
import com.google.inject.MembersInjector;
import java.lang.reflect.Modifier;
import java.lang.annotation.Annotation;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.TypeLiteral;
import android.content.Context;
import com.google.inject.Provider;
import com.google.inject.spi.TypeListener;

public class ExtrasListener implements TypeListener
{
    protected Provider<Context> contextProvider;
    
    public ExtrasListener(final Provider<Context> contextProvider) {
        this.contextProvider = contextProvider;
    }
    
    @Override
    public <I> void hear(final TypeLiteral<I> typeLiteral, final TypeEncounter<I> typeEncounter) {
        for (Class<? super Object> clazz = (Class<? super Object>)typeLiteral.getRawType(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            final Field[] declaredFields = clazz.getDeclaredFields();
            for (int length = declaredFields.length, i = 0; i < length; ++i) {
                final Field field = declaredFields[i];
                if (field.isAnnotationPresent(InjectExtra.class)) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        throw new UnsupportedOperationException("Extras may not be statically injected");
                    }
                    typeEncounter.register(new ExtrasMembersInjector<Object>(field, this.contextProvider, field.getAnnotation(InjectExtra.class)));
                }
            }
        }
    }
    
    protected static class ExtrasMembersInjector<T> implements MembersInjector<T>
    {
        protected InjectExtra annotation;
        protected Provider<Context> contextProvider;
        protected Field field;
        
        public ExtrasMembersInjector(final Field field, final Provider<Context> contextProvider, final InjectExtra annotation) {
            this.field = field;
            this.contextProvider = contextProvider;
            this.annotation = annotation;
        }
        
        protected Object convert(final Field field, final Object o, final Injector injector) {
            if (o == null || field.getType().isPrimitive()) {
                return o;
            }
            final Key<?> value = Key.get(Types.newParameterizedType(ExtraConverter.class, o.getClass(), field.getType()));
            Object convert = o;
            if (injector.getBindings().containsKey(value)) {
                convert = injector.getInstance(value).convert(o);
            }
            return convert;
        }
        
        @Override
        public void injectMembers(final T t) {
            final Context context = this.contextProvider.get();
            if (!(context instanceof Activity)) {
                throw new UnsupportedOperationException(String.format("Extras may not be injected into contexts that are not Activities (error in class %s)", this.contextProvider.get().getClass().getSimpleName()));
            }
            final Activity activity = (Activity)context;
            final String value = this.annotation.value();
            final Bundle extras = activity.getIntent().getExtras();
            if (extras == null || !extras.containsKey(value)) {
                if (this.annotation.optional()) {
                    return;
                }
                throw new IllegalStateException(String.format("Can't find the mandatory extra identified by key [%s] on field %s.%s", value, this.field.getDeclaringClass(), this.field.getName()));
            }
            else {
                final Object convert = this.convert(this.field, extras.get(value), RoboGuice.getBaseApplicationInjector(activity.getApplication()));
                if (convert == null && Nullable.notNullable(this.field)) {
                    throw new NullPointerException(String.format("Can't inject null value into %s.%s when field is not @Nullable", this.field.getDeclaringClass(), this.field.getName()));
                }
                this.field.setAccessible(true);
                try {
                    this.field.set(t, convert);
                }
                catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
                catch (IllegalArgumentException ex2) {
                    Serializable class1;
                    if (convert != null) {
                        class1 = convert.getClass();
                    }
                    else {
                        class1 = "(null)";
                    }
                    throw new IllegalArgumentException(String.format("Can't assign %s value %s to %s field %s", class1, convert, this.field.getType(), this.field.getName()));
                }
            }
        }
    }
}
