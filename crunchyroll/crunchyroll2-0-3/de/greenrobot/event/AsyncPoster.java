// 
// Decompiled by Procyon v0.5.30
// 

package de.greenrobot.event;

class AsyncPoster implements Runnable
{
    private final EventBus eventBus;
    private final PendingPostQueue queue;
    
    AsyncPoster(final EventBus eventBus) {
        this.eventBus = eventBus;
        this.queue = new PendingPostQueue();
    }
    
    public void enqueue(final Subscription subscription, final Object o) {
        this.queue.enqueue(PendingPost.obtainPendingPost(subscription, o));
        this.eventBus.getExecutorService().execute(this);
    }
    
    @Override
    public void run() {
        final PendingPost poll = this.queue.poll();
        if (poll == null) {
            throw new IllegalStateException("No pending post available");
        }
        this.eventBus.invokeSubscriber(poll);
    }
}
