// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.event.eventListener;

import java.lang.reflect.InvocationTargetException;
import roboguice.util.Ln;
import roboguice.event.eventListener.javaassist.RuntimeSupport;
import java.lang.reflect.Method;
import roboguice.event.EventListener;

public class ObserverMethodListener<T> implements EventListener<T>
{
    protected String descriptor;
    protected Object instance;
    protected Method method;
    
    public ObserverMethodListener(final Object instance, final Method method) {
        this.instance = instance;
        this.method = method;
        this.descriptor = method.getName() + ':' + RuntimeSupport.makeDescriptor(method);
        method.setAccessible(true);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final ObserverMethodListener observerMethodListener = (ObserverMethodListener)o;
            Label_0059: {
                if (this.descriptor != null) {
                    if (this.descriptor.equals(observerMethodListener.descriptor)) {
                        break Label_0059;
                    }
                }
                else if (observerMethodListener.descriptor == null) {
                    break Label_0059;
                }
                return false;
            }
            if (this.instance != null) {
                if (this.instance.equals(observerMethodListener.instance)) {
                    return true;
                }
            }
            else if (observerMethodListener.instance == null) {
                return true;
            }
            return false;
        }
        return true;
    }
    
    public Object getInstance() {
        return this.instance;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.descriptor != null) {
            hashCode2 = this.descriptor.hashCode();
        }
        else {
            hashCode2 = 0;
        }
        if (this.instance != null) {
            hashCode = this.instance.hashCode();
        }
        return hashCode2 * 31 + hashCode;
    }
    
    @Override
    public void onEvent(final Object o) {
        try {
            this.method.invoke(this.instance, o);
        }
        catch (InvocationTargetException ex) {
            Ln.e(ex);
        }
        catch (IllegalAccessException ex2) {
            throw new RuntimeException(ex2);
        }
    }
}
