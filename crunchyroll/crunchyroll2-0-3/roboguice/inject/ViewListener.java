// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Iterator;
import java.lang.ref.WeakReference;
import com.google.inject.Provider;
import java.util.ArrayList;
import java.util.WeakHashMap;
import java.lang.reflect.Field;
import com.google.inject.MembersInjector;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import java.lang.reflect.Modifier;
import java.lang.annotation.Annotation;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.TypeLiteral;
import java.lang.reflect.Method;
import javax.inject.Singleton;
import com.google.inject.spi.TypeListener;

@Singleton
public class ViewListener implements TypeListener
{
    protected static Class fragmentClass;
    protected static Method fragmentFindFragmentByIdMethod;
    protected static Method fragmentFindFragmentByTagMethod;
    protected static Method fragmentGetViewMethod;
    protected static Class fragmentManagerClass;
    
    static {
        ViewListener.fragmentClass = null;
        ViewListener.fragmentManagerClass = null;
        ViewListener.fragmentGetViewMethod = null;
        ViewListener.fragmentFindFragmentByIdMethod = null;
        ViewListener.fragmentFindFragmentByTagMethod = null;
        try {
            ViewListener.fragmentClass = Class.forName("android.support.v4.app.Fragment");
            ViewListener.fragmentManagerClass = Class.forName("android.support.v4.app.FragmentManager");
            ViewListener.fragmentGetViewMethod = ViewListener.fragmentClass.getDeclaredMethod("getView", (Class[])new Class[0]);
            ViewListener.fragmentFindFragmentByIdMethod = ViewListener.fragmentManagerClass.getMethod("findFragmentById", Integer.TYPE);
            ViewListener.fragmentFindFragmentByTagMethod = ViewListener.fragmentManagerClass.getMethod("findFragmentByTag", Object.class);
        }
        catch (Throwable t) {}
    }
    
    @Override
    public <I> void hear(final TypeLiteral<I> typeLiteral, final TypeEncounter<I> typeEncounter) {
        for (Class<? super Object> clazz = (Class<? super Object>)typeLiteral.getRawType(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            final Field[] declaredFields = clazz.getDeclaredFields();
            for (int length = declaredFields.length, i = 0; i < length; ++i) {
                final Field field = declaredFields[i];
                if (field.isAnnotationPresent(InjectView.class)) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        throw new UnsupportedOperationException("Views may not be statically injected");
                    }
                    if (!View.class.isAssignableFrom(field.getType())) {
                        throw new UnsupportedOperationException("You may only use @InjectView on fields descended from type View");
                    }
                    if (Context.class.isAssignableFrom(field.getDeclaringClass()) && !Activity.class.isAssignableFrom(field.getDeclaringClass())) {
                        throw new UnsupportedOperationException("You may only use @InjectView in Activity contexts");
                    }
                    typeEncounter.register(new ViewMembersInjector<Object>(field, field.getAnnotation(InjectView.class), (TypeEncounter<Object>)typeEncounter));
                }
                else if (field.isAnnotationPresent(InjectFragment.class)) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        throw new UnsupportedOperationException("Fragments may not be statically injected");
                    }
                    if (ViewListener.fragmentClass != null && !ViewListener.fragmentClass.isAssignableFrom(field.getType())) {
                        throw new UnsupportedOperationException("You may only use @InjectFragment on fields descended from type Fragment");
                    }
                    if (Context.class.isAssignableFrom(field.getDeclaringClass()) && !Activity.class.isAssignableFrom(field.getDeclaringClass())) {
                        throw new UnsupportedOperationException("You may only use @InjectFragment in Activity contexts");
                    }
                    typeEncounter.register(new ViewMembersInjector<Object>(field, field.getAnnotation(InjectFragment.class), (TypeEncounter<Object>)typeEncounter));
                }
            }
        }
    }
    
    public static class ViewMembersInjector<T> implements MembersInjector<T>
    {
        protected static WeakHashMap<Object, ArrayList<ViewMembersInjector<?>>> viewMembersInjectors;
        protected Provider<Activity> activityProvider;
        protected Annotation annotation;
        protected Field field;
        protected Provider fragmentManagerProvider;
        protected WeakReference<T> instanceRef;
        
        static {
            ViewMembersInjector.viewMembersInjectors = new WeakHashMap<Object, ArrayList<ViewMembersInjector<?>>>();
        }
        
        public ViewMembersInjector(final Field field, final Annotation annotation, final TypeEncounter<T> typeEncounter) {
            this.field = field;
            this.annotation = annotation;
            this.activityProvider = typeEncounter.getProvider(Activity.class);
            if (ViewListener.fragmentManagerClass != null) {
                this.fragmentManagerProvider = typeEncounter.getProvider((Class<Object>)ViewListener.fragmentManagerClass);
            }
        }
        
        protected static void injectViews(final Object o) {
            synchronized (ViewMembersInjector.class) {
                final ArrayList<ViewMembersInjector<?>> list = ViewMembersInjector.viewMembersInjectors.get(o);
                if (list != null) {
                    final Iterator<ViewMembersInjector<?>> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        iterator.next().reallyInjectMembers(o);
                    }
                }
            }
        }
        // monitorexit(ViewMembersInjector.class)
        
        @Override
        public void injectMembers(final T t) {
            while (true) {
                while (true) {
                    Label_0107: {
                        synchronized (ViewMembersInjector.class) {
                            Object o = this.activityProvider.get();
                            if (ViewListener.fragmentClass == null || !ViewListener.fragmentClass.isInstance(t)) {
                                break Label_0107;
                            }
                            o = t;
                            if (o == null) {
                                return;
                            }
                            List<E> list;
                            if ((list = (List<E>)ViewMembersInjector.viewMembersInjectors.get(o)) == null) {
                                list = (List<E>)new ArrayList<ViewMembersInjector>();
                                ViewMembersInjector.viewMembersInjectors.put(o, (ArrayList<ViewMembersInjector<?>>)list);
                            }
                            ((ArrayList<ViewMembersInjector>)list).add(this);
                            this.instanceRef = new WeakReference<T>(t);
                            return;
                        }
                    }
                    continue;
                }
            }
        }
        
        protected void reallyInjectMemberFragments(Object invoke) {
            if (this.instanceRef.get() == null) {
                return;
            }
            if (invoke instanceof Context && !(invoke instanceof Activity)) {
                throw new UnsupportedOperationException("Can't inject fragment into a non-Activity context");
            }
            Object o2;
            final Object o = o2 = null;
            try {
                final InjectFragment injectFragment = (InjectFragment)this.annotation;
                o2 = o;
                final int value = injectFragment.value();
                if (value < 0) {
                    goto Label_0160;
                }
                o2 = o;
                invoke = ViewListener.fragmentFindFragmentByIdMethod.invoke(this.fragmentManagerProvider.get(), value);
                if (invoke != null) {
                    goto Label_0195;
                }
                o2 = invoke;
                if (Nullable.notNullable(this.field)) {
                    o2 = invoke;
                    throw new NullPointerException(String.format("Can't inject null value into %s.%s when field is not @Nullable", this.field.getDeclaringClass(), this.field.getName()));
                }
                goto Label_0195;
            }
            catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
            catch (InvocationTargetException ex2) {
                throw new RuntimeException(ex2);
            }
            catch (IllegalArgumentException ex3) {
                Serializable class1;
                if (o2 != null) {
                    class1 = o2.getClass();
                }
                else {
                    class1 = "(null)";
                }
                throw new IllegalArgumentException(String.format("Can't assign %s value %s to %s field %s", class1, o2, this.field.getType(), this.field.getName()), ex3);
            }
        }
        
        protected void reallyInjectMemberViews(final Object o) {
            Object value;
            if (ViewListener.fragmentClass != null && ViewListener.fragmentClass.isInstance(o)) {
                value = o;
            }
            else {
                value = this.instanceRef.get();
            }
            if (value == null) {
                return;
            }
            if (o instanceof Context && !(o instanceof Activity)) {
                throw new UnsupportedOperationException("Can't inject view into a non-Activity context");
            }
            View view2;
            final View view = view2 = null;
            while (true) {
                try {
                    final InjectView injectView = (InjectView)this.annotation;
                    view2 = view;
                    final int value2 = injectView.value();
                    if (value2 < 0) {
                        goto Label_0215;
                    }
                    view2 = view;
                    if (ViewListener.fragmentClass == null) {
                        goto Label_0200;
                    }
                    view2 = view;
                    if (!ViewListener.fragmentClass.isInstance(o)) {
                        goto Label_0200;
                    }
                    view2 = view;
                    final View viewById = ((View)ViewListener.fragmentGetViewMethod.invoke(o, new Object[0])).findViewById(value2);
                    if (viewById != null) {
                        goto Label_0295;
                    }
                    view2 = viewById;
                    if (Nullable.notNullable(this.field)) {
                        view2 = viewById;
                        throw new NullPointerException(String.format("Can't inject null value into %s.%s when field is not @Nullable", this.field.getDeclaringClass(), this.field.getName()));
                    }
                    goto Label_0295;
                }
                catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
                catch (InvocationTargetException ex2) {
                    throw new RuntimeException(ex2);
                }
                catch (IllegalArgumentException ex3) {
                    Serializable class1;
                    if (view2 != null) {
                        class1 = view2.getClass();
                    }
                    else {
                        class1 = "(null)";
                    }
                    throw new IllegalArgumentException(String.format("Can't assign %s value %s to %s field %s", class1, view2, this.field.getType(), this.field.getName()), ex3);
                }
                continue;
            }
        }
        
        public void reallyInjectMembers(final Object o) {
            if (this.annotation instanceof InjectView) {
                this.reallyInjectMemberViews(o);
                return;
            }
            this.reallyInjectMemberFragments(o);
        }
    }
}
