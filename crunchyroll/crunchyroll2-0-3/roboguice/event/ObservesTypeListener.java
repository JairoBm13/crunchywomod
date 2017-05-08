// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.event;

import roboguice.event.eventListener.ObserverMethodListener;
import com.google.inject.spi.InjectionListener;
import com.google.inject.TypeLiteral;
import java.lang.annotation.Annotation;
import com.google.inject.spi.TypeEncounter;
import java.lang.reflect.Method;
import roboguice.event.eventListener.factory.EventListenerThreadingDecorator;
import com.google.inject.Provider;
import com.google.inject.spi.TypeListener;

public class ObservesTypeListener implements TypeListener
{
    protected Provider<EventManager> eventManagerProvider;
    protected EventListenerThreadingDecorator observerThreadingDecorator;
    
    public ObservesTypeListener(final Provider<EventManager> eventManagerProvider, final EventListenerThreadingDecorator observerThreadingDecorator) {
        this.eventManagerProvider = eventManagerProvider;
        this.observerThreadingDecorator = observerThreadingDecorator;
    }
    
    protected void checkMethodParameters(final Method method) {
        if (method.getParameterTypes().length > 1) {
            throw new RuntimeException("Annotation @Observes must only annotate one parameter, which must be the only parameter in the listener method.");
        }
    }
    
    protected <I> void findContextObserver(final Method method, final TypeEncounter<I> typeEncounter) {
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; ++i) {
            final Annotation[] array = parameterAnnotations[i];
            final Class<?> clazz = method.getParameterTypes()[i];
            for (int length = array.length, j = 0; j < length; ++j) {
                final Annotation annotation = array[j];
                if (annotation.annotationType().equals(Observes.class)) {
                    this.registerContextObserver(typeEncounter, method, clazz, ((Observes)annotation).value());
                }
            }
        }
    }
    
    @Override
    public <I> void hear(final TypeLiteral<I> typeLiteral, final TypeEncounter<I> typeEncounter) {
        for (Class<? super Object> clazz = (Class<? super Object>)typeLiteral.getRawType(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            final Method[] declaredMethods = clazz.getDeclaredMethods();
            for (int length = declaredMethods.length, i = 0; i < length; ++i) {
                this.findContextObserver(declaredMethods[i], typeEncounter);
            }
            final Class<?>[] interfaces = (Class<?>[])clazz.getInterfaces();
            for (int length2 = interfaces.length, j = 0; j < length2; ++j) {
                final Method[] declaredMethods2 = interfaces[j].getDeclaredMethods();
                for (int length3 = declaredMethods2.length, k = 0; k < length3; ++k) {
                    this.findContextObserver(declaredMethods2[k], typeEncounter);
                }
            }
        }
    }
    
    protected <I, T> void registerContextObserver(final TypeEncounter<I> typeEncounter, final Method method, final Class<T> clazz, final EventThread eventThread) {
        this.checkMethodParameters(method);
        typeEncounter.register(new ContextObserverMethodInjector<Object, Object>(this.eventManagerProvider, this.observerThreadingDecorator, method, clazz, eventThread));
    }
    
    public static class ContextObserverMethodInjector<I, T> implements InjectionListener<I>
    {
        protected Class<T> event;
        protected Provider<EventManager> eventManagerProvider;
        protected Method method;
        protected EventListenerThreadingDecorator observerThreadingDecorator;
        protected EventThread threadType;
        
        public ContextObserverMethodInjector(final Provider<EventManager> eventManagerProvider, final EventListenerThreadingDecorator observerThreadingDecorator, final Method method, final Class<T> event, final EventThread threadType) {
            this.observerThreadingDecorator = observerThreadingDecorator;
            this.eventManagerProvider = eventManagerProvider;
            this.method = method;
            this.event = event;
            this.threadType = threadType;
        }
        
        @Override
        public void afterInjection(final I n) {
            this.eventManagerProvider.get().registerObserver(this.event, this.observerThreadingDecorator.decorate(this.threadType, new ObserverMethodListener<Object>(n, this.method)));
        }
    }
}
