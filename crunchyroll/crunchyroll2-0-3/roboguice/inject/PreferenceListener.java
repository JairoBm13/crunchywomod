// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import java.io.Serializable;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import com.google.inject.MembersInjector;
import java.lang.reflect.Modifier;
import java.lang.annotation.Annotation;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.TypeLiteral;
import java.util.ArrayList;
import android.content.Context;
import com.google.inject.Provider;
import android.app.Application;
import com.google.inject.spi.TypeListener;

public class PreferenceListener implements TypeListener
{
    protected Application application;
    protected Provider<Context> contextProvider;
    protected ArrayList<PreferenceMembersInjector<?>> preferencesForInjection;
    protected ContextScope scope;
    
    public PreferenceListener(final Provider<Context> contextProvider, final Application application, final ContextScope scope) {
        this.preferencesForInjection = new ArrayList<PreferenceMembersInjector<?>>();
        this.contextProvider = contextProvider;
        this.application = application;
        this.scope = scope;
    }
    
    @Override
    public <I> void hear(final TypeLiteral<I> typeLiteral, final TypeEncounter<I> typeEncounter) {
        for (Class<? super Object> clazz = (Class<? super Object>)typeLiteral.getRawType(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            final Field[] declaredFields = clazz.getDeclaredFields();
            for (int length = declaredFields.length, i = 0; i < length; ++i) {
                final Field field = declaredFields[i];
                if (field.isAnnotationPresent(InjectPreference.class)) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        throw new UnsupportedOperationException("Preferences may not be statically injected");
                    }
                    typeEncounter.register(new PreferenceMembersInjector<Object>(field, this.contextProvider, field.getAnnotation(InjectPreference.class), this.scope));
                }
            }
        }
    }
    
    public void injectPreferenceViews() {
        for (int i = this.preferencesForInjection.size() - 1; i >= 0; --i) {
            this.preferencesForInjection.remove(i).reallyInjectMembers();
        }
    }
    
    public void registerPreferenceForInjection(final PreferenceMembersInjector<?> preferenceMembersInjector) {
        this.preferencesForInjection.add(preferenceMembersInjector);
    }
    
    class PreferenceMembersInjector<T> implements MembersInjector<T>
    {
        protected InjectPreference annotation;
        protected Provider<Context> contextProvider;
        protected Field field;
        protected WeakReference<T> instanceRef;
        protected ContextScope scope;
        
        public PreferenceMembersInjector(final Field field, final Provider<Context> contextProvider, final InjectPreference annotation, final ContextScope scope) {
            this.field = field;
            this.annotation = annotation;
            this.contextProvider = contextProvider;
            this.scope = scope;
        }
        
        @Override
        public void injectMembers(final T t) {
            this.instanceRef = new WeakReference<T>(t);
            PreferenceListener.this.registerPreferenceForInjection(this);
        }
        
        public void reallyInjectMembers() {
            if (this.instanceRef.get() == null) {
                return;
            }
            Preference preference = null;
            try {
                final Preference preference2 = ((PreferenceActivity)this.contextProvider.get()).findPreference((CharSequence)this.annotation.value());
                if (preference2 != null) {
                    goto Label_0105;
                }
                preference = preference2;
                if (Nullable.notNullable(this.field)) {
                    preference = preference2;
                    throw new NullPointerException(String.format("Can't inject null value into %s.%s when field is not @Nullable", this.field.getDeclaringClass(), this.field.getName()));
                }
                goto Label_0105;
            }
            catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
            catch (IllegalArgumentException ex2) {
                Serializable class1;
                if (preference != null) {
                    class1 = preference.getClass();
                }
                else {
                    class1 = "(null)";
                }
                throw new IllegalArgumentException(String.format("Can't assign %s value %s to %s field %s", class1, preference, this.field.getType(), this.field.getName()));
            }
        }
    }
}
