// 
// Decompiled by Procyon v0.5.30
// 

package de.greenrobot.event;

final class PendingPostQueue
{
    private PendingPost head;
    private PendingPost tail;
    
    void enqueue(final PendingPost pendingPost) {
        // monitorenter(this)
        if (pendingPost == null) {
            try {
                throw new NullPointerException("null cannot be enqueued");
            }
            finally {
            }
            // monitorexit(this)
        }
        if (this.tail != null) {
            this.tail.next = pendingPost;
            this.tail = pendingPost;
        }
        else {
            if (this.head != null) {
                throw new IllegalStateException("Head present, but no tail");
            }
            this.tail = pendingPost;
            this.head = pendingPost;
        }
        this.notifyAll();
    }
    // monitorexit(this)
    
    PendingPost poll() {
        synchronized (this) {
            final PendingPost head = this.head;
            if (this.head != null) {
                this.head = this.head.next;
                if (this.head == null) {
                    this.tail = null;
                }
            }
            return head;
        }
    }
    
    PendingPost poll(final int n) throws InterruptedException {
        synchronized (this) {
            if (this.head == null) {
                this.wait(n);
            }
            return this.poll();
        }
    }
}
