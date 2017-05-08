// 
// Decompiled by Procyon v0.5.30
// 

package de.greenrobot.event;

import java.util.ArrayList;
import java.util.List;

final class PendingPost
{
    private static final List<PendingPost> pendingPostPool;
    Object event;
    PendingPost next;
    Subscription subscription;
    
    static {
        pendingPostPool = new ArrayList<PendingPost>();
    }
    
    private PendingPost(final Object event, final Subscription subscription) {
        this.event = event;
        this.subscription = subscription;
    }
    
    static PendingPost obtainPendingPost(final Subscription subscription, final Object event) {
        synchronized (PendingPost.pendingPostPool) {
            final int size = PendingPost.pendingPostPool.size();
            if (size > 0) {
                final PendingPost pendingPost = PendingPost.pendingPostPool.remove(size - 1);
                pendingPost.event = event;
                pendingPost.subscription = subscription;
                pendingPost.next = null;
                return pendingPost;
            }
            // monitorexit(PendingPost.pendingPostPool)
            return new PendingPost(event, subscription);
        }
    }
    
    static void releasePendingPost(final PendingPost pendingPost) {
        pendingPost.event = null;
        pendingPost.subscription = null;
        pendingPost.next = null;
        synchronized (PendingPost.pendingPostPool) {
            if (PendingPost.pendingPostPool.size() < 10000) {
                PendingPost.pendingPostPool.add(pendingPost);
            }
        }
    }
}
