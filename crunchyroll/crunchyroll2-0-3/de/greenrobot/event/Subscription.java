// 
// Decompiled by Procyon v0.5.30
// 

package de.greenrobot.event;

final class Subscription
{
    volatile boolean active;
    final int priority;
    final Object subscriber;
    final SubscriberMethod subscriberMethod;
    
    Subscription(final Object subscriber, final SubscriberMethod subscriberMethod, final int priority) {
        this.subscriber = subscriber;
        this.subscriberMethod = subscriberMethod;
        this.priority = priority;
        this.active = true;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b2;
        final boolean b = b2 = false;
        if (o instanceof Subscription) {
            final Subscription subscription = (Subscription)o;
            b2 = b;
            if (this.subscriber == subscription.subscriber) {
                b2 = b;
                if (this.subscriberMethod.equals(subscription.subscriberMethod)) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    @Override
    public int hashCode() {
        return this.subscriber.hashCode() + this.subscriberMethod.methodString.hashCode();
    }
}
