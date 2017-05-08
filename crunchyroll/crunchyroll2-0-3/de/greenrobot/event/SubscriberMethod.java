// 
// Decompiled by Procyon v0.5.30
// 

package de.greenrobot.event;

import java.lang.reflect.Method;

final class SubscriberMethod
{
    final Class<?> eventType;
    final Method method;
    String methodString;
    final ThreadMode threadMode;
    
    SubscriberMethod(final Method method, final ThreadMode threadMode, final Class<?> eventType) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = eventType;
    }
    
    private void checkMethodString() {
        synchronized (this) {
            if (this.methodString == null) {
                final StringBuilder sb = new StringBuilder(64);
                sb.append(this.method.getDeclaringClass().getName());
                sb.append('#').append(this.method.getName());
                sb.append('(').append(this.eventType.getName());
                this.methodString = sb.toString();
            }
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof SubscriberMethod) {
            this.checkMethodString();
            final SubscriberMethod subscriberMethod = (SubscriberMethod)o;
            subscriberMethod.checkMethodString();
            return this.methodString.equals(subscriberMethod.methodString);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.method.hashCode();
    }
}
