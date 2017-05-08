// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import java.io.Serializable;
import android.content.res.Resources;
import java.lang.reflect.Field;
import com.google.inject.MembersInjector;
import java.lang.reflect.Modifier;
import java.lang.annotation.Annotation;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.TypeLiteral;
import android.app.Application;
import com.google.inject.spi.TypeListener;

public class ResourceListener implements TypeListener
{
    protected Application application;
    
    public ResourceListener(final Application application) {
        this.application = application;
    }
    
    @Override
    public <I> void hear(final TypeLiteral<I> typeLiteral, final TypeEncounter<I> typeEncounter) {
        for (Class<? super Object> clazz = (Class<? super Object>)typeLiteral.getRawType(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            final Field[] declaredFields = clazz.getDeclaredFields();
            for (int length = declaredFields.length, i = 0; i < length; ++i) {
                final Field field = declaredFields[i];
                if (field.isAnnotationPresent(InjectResource.class) && !Modifier.isStatic(field.getModifiers())) {
                    typeEncounter.register(new ResourceMembersInjector<Object>(field, this.application, field.getAnnotation(InjectResource.class)));
                }
            }
        }
    }
    
    public void requestStaticInjection(final Class<?>... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            for (Class<?> superclass = array[i]; superclass != Object.class; superclass = superclass.getSuperclass()) {
                final Field[] declaredFields = superclass.getDeclaredFields();
                for (int length2 = declaredFields.length, j = 0; j < length2; ++j) {
                    final Field field = declaredFields[j];
                    if (Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(InjectResource.class)) {
                        new ResourceMembersInjector<Object>(field, this.application, field.getAnnotation(InjectResource.class)).injectMembers(null);
                    }
                }
            }
        }
    }
    
    protected static class ResourceMembersInjector<T> implements MembersInjector<T>
    {
        protected InjectResource annotation;
        protected Application application;
        protected Field field;
        
        public ResourceMembersInjector(final Field field, final Application application, final InjectResource annotation) {
            this.field = field;
            this.application = application;
            this.annotation = annotation;
        }
        
        protected int getId(final Resources resources, final InjectResource injectResource) {
            final int value = injectResource.value();
            if (value >= 0) {
                return value;
            }
            return resources.getIdentifier(injectResource.name(), (String)null, (String)null);
        }
        
        @Override
        public void injectMembers(final T t) {
            final Object o = null;
            Object string = null;
            Object o2 = o;
            try {
                final Resources resources = this.application.getResources();
                o2 = o;
                final int id = this.getId(resources, this.annotation);
                o2 = o;
                final Class<?> type = this.field.getType();
                o2 = o;
                if (!String.class.isAssignableFrom(type)) {
                    goto Label_0138;
                }
                o2 = o;
                string = resources.getString(id);
                if (string != null) {
                    goto Label_0414;
                }
                o2 = string;
                if (Nullable.notNullable(this.field)) {
                    o2 = string;
                    throw new NullPointerException(String.format("Can't inject null value into %s.%s when field is not @Nullable", this.field.getDeclaringClass(), this.field.getName()));
                }
                goto Label_0414;
            }
            catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
            catch (IllegalArgumentException ex2) {
                Serializable class1;
                if (o2 != null) {
                    class1 = o2.getClass();
                }
                else {
                    class1 = "(null)";
                }
                throw new IllegalArgumentException(String.format("Can't assign %s value %s to %s field %s", class1, o2, this.field.getType(), this.field.getName()));
            }
        }
    }
}
