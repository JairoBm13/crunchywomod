// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.assist.deque;

import java.util.concurrent.TimeUnit;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

public interface BlockingDeque<E> extends Deque<E>, BlockingQueue<E>
{
    boolean add(final E p0);
    
    void addFirst(final E p0);
    
    void addLast(final E p0);
    
    boolean contains(final Object p0);
    
    E element();
    
    Iterator<E> iterator();
    
    boolean offer(final E p0);
    
    boolean offer(final E p0, final long p1, final TimeUnit p2) throws InterruptedException;
    
    boolean offerFirst(final E p0);
    
    boolean offerFirst(final E p0, final long p1, final TimeUnit p2) throws InterruptedException;
    
    boolean offerLast(final E p0);
    
    boolean offerLast(final E p0, final long p1, final TimeUnit p2) throws InterruptedException;
    
    E peek();
    
    E poll();
    
    E poll(final long p0, final TimeUnit p1) throws InterruptedException;
    
    E pollFirst(final long p0, final TimeUnit p1) throws InterruptedException;
    
    E pollLast(final long p0, final TimeUnit p1) throws InterruptedException;
    
    void push(final E p0);
    
    void put(final E p0) throws InterruptedException;
    
    void putFirst(final E p0) throws InterruptedException;
    
    void putLast(final E p0) throws InterruptedException;
    
    E remove();
    
    boolean remove(final Object p0);
    
    boolean removeFirstOccurrence(final Object p0);
    
    boolean removeLastOccurrence(final Object p0);
    
    int size();
    
    E take() throws InterruptedException;
    
    E takeFirst() throws InterruptedException;
    
    E takeLast() throws InterruptedException;
}
