// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.concurrency;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.Collection;
import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class DependencyPriorityBlockingQueue<E extends Dependency> extends PriorityBlockingQueue<E>
{
    final Queue<E> blockedQueue;
    private final ReentrantLock lock;
    
    public DependencyPriorityBlockingQueue() {
        this.blockedQueue = new LinkedList<E>();
        this.lock = new ReentrantLock();
    }
    
    boolean canProcess(final E e) {
        return ((io.fabric.sdk.android.services.concurrency.Dependency)e).areDependenciesMet();
    }
    
    @Override
    public void clear() {
        try {
            this.lock.lock();
            this.blockedQueue.clear();
            super.clear();
        }
        finally {
            this.lock.unlock();
        }
    }
    
     <T> T[] concatenate(final T[] array, final T[] array2) {
        final int length = array.length;
        final int length2 = array2.length;
        final Object[] array3 = (Object[])Array.newInstance(array.getClass().getComponentType(), length + length2);
        System.arraycopy(array, 0, array3, 0, length);
        System.arraycopy(array2, 0, array3, length, length2);
        return (T[])array3;
    }
    
    @Override
    public boolean contains(final Object o) {
        try {
            this.lock.lock();
            return super.contains(o) || this.blockedQueue.contains(o);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public int drainTo(final Collection<? super E> collection) {
        int drainTo;
        int size;
        try {
            this.lock.lock();
            drainTo = super.drainTo(collection);
            size = this.blockedQueue.size();
            while (!this.blockedQueue.isEmpty()) {
                collection.add(this.blockedQueue.poll());
            }
        }
        finally {
            this.lock.unlock();
        }
        this.lock.unlock();
        return drainTo + size;
    }
    
    @Override
    public int drainTo(final Collection<? super E> collection, final int n) {
        try {
            this.lock.lock();
            int drainTo;
            for (drainTo = super.drainTo(collection, n); !this.blockedQueue.isEmpty() && drainTo <= n; ++drainTo) {
                collection.add(this.blockedQueue.poll());
            }
            return drainTo;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    E get(final int p0, final Long p1, final TimeUnit p2) throws InterruptedException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: iload_1        
        //     2: aload_2        
        //     3: aload_3        
        //     4: invokevirtual   io/fabric/sdk/android/services/concurrency/DependencyPriorityBlockingQueue.performOperation:(ILjava/lang/Long;Ljava/util/concurrent/TimeUnit;)Lio/fabric/sdk/android/services/concurrency/Dependency;
        //     7: astore          4
        //     9: aload           4
        //    11: ifnull          23
        //    14: aload_0        
        //    15: aload           4
        //    17: invokevirtual   io/fabric/sdk/android/services/concurrency/DependencyPriorityBlockingQueue.canProcess:(Lio/fabric/sdk/android/services/concurrency/Dependency;)Z
        //    20: ifeq            26
        //    23: aload           4
        //    25: areturn        
        //    26: aload_0        
        //    27: iload_1        
        //    28: aload           4
        //    30: invokevirtual   io/fabric/sdk/android/services/concurrency/DependencyPriorityBlockingQueue.offerBlockedResult:(ILio/fabric/sdk/android/services/concurrency/Dependency;)Z
        //    33: pop            
        //    34: goto            0
        //    Exceptions:
        //  throws java.lang.InterruptedException
        //    Signature:
        //  (ILjava/lang/Long;Ljava/util/concurrent/TimeUnit;)TE;
        // 
        // The error that occurred was:
        // 
        // java.lang.StackOverflowError
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1242)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    boolean offerBlockedResult(final int n, final E e) {
        try {
            this.lock.lock();
            if (n == 1) {
                super.remove(e);
            }
            return this.blockedQueue.offer(e);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public E peek() {
        try {
            return this.get(1, null, null);
        }
        catch (InterruptedException ex) {
            return null;
        }
    }
    
    E performOperation(final int n, final Long n2, final TimeUnit timeUnit) throws InterruptedException {
        switch (n) {
            default: {
                return null;
            }
            case 0: {
                return super.take();
            }
            case 1: {
                return super.peek();
            }
            case 2: {
                return super.poll();
            }
            case 3: {
                return super.poll(n2, timeUnit);
            }
        }
    }
    
    @Override
    public E poll() {
        try {
            return this.get(2, null, null);
        }
        catch (InterruptedException ex) {
            return null;
        }
    }
    
    @Override
    public E poll(final long n, final TimeUnit timeUnit) throws InterruptedException {
        return this.get(3, n, timeUnit);
    }
    
    public void recycleBlockedQueue() {
        try {
            this.lock.lock();
            final Iterator<Dependency> iterator = (Iterator<Dependency>)this.blockedQueue.iterator();
            while (iterator.hasNext()) {
                final Dependency dependency = iterator.next();
                if (this.canProcess((E)dependency)) {
                    super.offer((E)dependency);
                    iterator.remove();
                }
            }
        }
        finally {
            this.lock.unlock();
        }
        this.lock.unlock();
    }
    
    @Override
    public boolean remove(final Object o) {
        try {
            this.lock.lock();
            return super.remove(o) || this.blockedQueue.remove(o);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public boolean removeAll(final Collection<?> collection) {
        try {
            this.lock.lock();
            final boolean removeAll = super.removeAll(collection);
            final boolean removeAll2 = this.blockedQueue.removeAll(collection);
            this.lock.unlock();
            return removeAll | removeAll2;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public int size() {
        try {
            this.lock.lock();
            final int size = this.blockedQueue.size();
            final int size2 = super.size();
            this.lock.unlock();
            return size + size2;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public E take() throws InterruptedException {
        return this.get(0, null, null);
    }
    
    @Override
    public Object[] toArray() {
        try {
            this.lock.lock();
            return this.concatenate(super.toArray(), this.blockedQueue.toArray());
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public <T> T[] toArray(final T[] array) {
        try {
            this.lock.lock();
            return this.concatenate((T[])super.toArray((T[])array), (T[])this.blockedQueue.toArray((T[])array));
        }
        finally {
            this.lock.unlock();
        }
    }
}
