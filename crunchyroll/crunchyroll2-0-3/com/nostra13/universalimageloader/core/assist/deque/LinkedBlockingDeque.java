// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.assist.deque;

import com.nostra13.universalimageloader.core.assist.deque.LinkedBlockingDeque$com.nostra13.universalimageloader.core.assist.deque.LinkedBlockingDeque$AbstractItr;
import java.lang.reflect.Array;
import java.util.concurrent.TimeUnit;
import java.util.NoSuchElementException;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.Collection;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.io.Serializable;
import java.util.AbstractQueue;

public class LinkedBlockingDeque<E> extends AbstractQueue<E> implements BlockingDeque<E>, Serializable
{
    private static final long serialVersionUID = -387911632671998426L;
    private final int capacity;
    private transient int count;
    transient Node<E> first;
    transient Node<E> last;
    final ReentrantLock lock;
    private final Condition notEmpty;
    private final Condition notFull;
    
    public LinkedBlockingDeque() {
        this(Integer.MAX_VALUE);
    }
    
    public LinkedBlockingDeque(final int capacity) {
        this.lock = new ReentrantLock();
        this.notEmpty = this.lock.newCondition();
        this.notFull = this.lock.newCondition();
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;
    }
    
    public LinkedBlockingDeque(final Collection<? extends E> collection) {
        while (true) {
            this(Integer.MAX_VALUE);
            final ReentrantLock lock = this.lock;
            lock.lock();
        Label_0082:
            while (true) {
                E next;
                try {
                    final Iterator<? extends E> iterator = collection.iterator();
                    if (!iterator.hasNext()) {
                        break Label_0082;
                    }
                    next = (E)iterator.next();
                    if (next == null) {
                        throw new NullPointerException();
                    }
                }
                finally {
                    lock.unlock();
                }
                if (!this.linkLast(new Node<E>(next))) {
                    throw new IllegalStateException("Deque full");
                }
                continue;
            }
            lock.unlock();
        }
    }
    
    private boolean linkFirst(final Node<E> prev) {
        if (this.count >= this.capacity) {
            return false;
        }
        final Node<E> first = this.first;
        prev.next = first;
        this.first = prev;
        if (this.last == null) {
            this.last = prev;
        }
        else {
            first.prev = prev;
        }
        ++this.count;
        this.notEmpty.signal();
        return true;
    }
    
    private boolean linkLast(final Node<E> next) {
        if (this.count >= this.capacity) {
            return false;
        }
        final Node<E> last = this.last;
        next.prev = last;
        this.last = next;
        if (this.first == null) {
            this.first = next;
        }
        else {
            last.next = next;
        }
        ++this.count;
        this.notEmpty.signal();
        return true;
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.count = 0;
        this.first = null;
        this.last = null;
        while (true) {
            final Object object = objectInputStream.readObject();
            if (object == null) {
                break;
            }
            this.add((E)object);
        }
    }
    
    private E unlinkFirst() {
        final Node<E> first = this.first;
        if (first == null) {
            return null;
        }
        final Node<E> next = first.next;
        final E item = first.item;
        first.item = null;
        first.next = first;
        if ((this.first = next) == null) {
            this.last = null;
        }
        else {
            next.prev = null;
        }
        --this.count;
        this.notFull.signal();
        return item;
    }
    
    private E unlinkLast() {
        final Node<E> last = this.last;
        if (last == null) {
            return null;
        }
        final Node<E> prev = last.prev;
        final E item = last.item;
        last.item = null;
        last.prev = last;
        if ((this.last = prev) == null) {
            this.first = null;
        }
        else {
            prev.next = null;
        }
        --this.count;
        this.notFull.signal();
        return item;
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            objectOutputStream.defaultWriteObject();
            for (Node<E> node = this.first; node != null; node = node.next) {
                objectOutputStream.writeObject(node.item);
            }
            objectOutputStream.writeObject(null);
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public boolean add(final E e) {
        this.addLast(e);
        return true;
    }
    
    @Override
    public void addFirst(final E e) {
        if (!this.offerFirst(e)) {
            throw new IllegalStateException("Deque full");
        }
    }
    
    @Override
    public void addLast(final E e) {
        if (!this.offerLast(e)) {
            throw new IllegalStateException("Deque full");
        }
    }
    
    @Override
    public void clear() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Node<E> next;
            for (Node<E> first = this.first; first != null; first = next) {
                first.item = null;
                next = first.next;
                first.prev = null;
                first.next = null;
            }
            this.last = null;
            this.first = null;
            this.count = 0;
            this.notFull.signalAll();
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public boolean contains(final Object o) {
        if (o == null) {
            return false;
        }
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            for (Node<E> node = this.first; node != null; node = node.next) {
                if (o.equals(node.item)) {
                    return true;
                }
            }
            return false;
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public Iterator<E> descendingIterator() {
        return (Iterator<E>)new DescendingItr();
    }
    
    @Override
    public int drainTo(final Collection<? super E> collection) {
        return this.drainTo(collection, Integer.MAX_VALUE);
    }
    
    @Override
    public int drainTo(final Collection<? super E> collection, int i) {
        if (collection == null) {
            throw new NullPointerException();
        }
        if (collection == this) {
            throw new IllegalArgumentException();
        }
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            int min;
            for (min = Math.min(i, this.count), i = 0; i < min; ++i) {
                collection.add((Object)this.first.item);
                this.unlinkFirst();
            }
            return min;
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public E element() {
        return this.getFirst();
    }
    
    @Override
    public E getFirst() {
        final E peekFirst = this.peekFirst();
        if (peekFirst == null) {
            throw new NoSuchElementException();
        }
        return peekFirst;
    }
    
    @Override
    public E getLast() {
        final E peekLast = this.peekLast();
        if (peekLast == null) {
            throw new NoSuchElementException();
        }
        return peekLast;
    }
    
    @Override
    public Iterator<E> iterator() {
        return (Iterator<E>)new Itr();
    }
    
    @Override
    public boolean offer(final E e) {
        return this.offerLast(e);
    }
    
    @Override
    public boolean offer(final E e, final long n, final TimeUnit timeUnit) throws InterruptedException {
        return this.offerLast(e, n, timeUnit);
    }
    
    @Override
    public boolean offerFirst(E lock) {
        if (lock == null) {
            throw new NullPointerException();
        }
        final Node<E> node = new Node<E>(lock);
        lock = (E)this.lock;
        ((ReentrantLock)lock).lock();
        try {
            return this.linkFirst(node);
        }
        finally {
            ((ReentrantLock)lock).unlock();
        }
    }
    
    @Override
    public boolean offerFirst(final E e, long n, TimeUnit lock) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        final Node<E> node = new Node<E>(e);
        n = lock.toNanos(n);
        lock = (TimeUnit)this.lock;
        ((ReentrantLock)lock).lockInterruptibly();
        try {
            while (!this.linkFirst(node)) {
                if (n <= 0L) {
                    return false;
                }
                n = this.notFull.awaitNanos(n);
            }
            return true;
        }
        finally {
            ((ReentrantLock)lock).unlock();
        }
    }
    
    @Override
    public boolean offerLast(E lock) {
        if (lock == null) {
            throw new NullPointerException();
        }
        final Node<E> node = new Node<E>(lock);
        lock = (E)this.lock;
        ((ReentrantLock)lock).lock();
        try {
            return this.linkLast(node);
        }
        finally {
            ((ReentrantLock)lock).unlock();
        }
    }
    
    @Override
    public boolean offerLast(final E e, long n, TimeUnit lock) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        final Node<E> node = new Node<E>(e);
        n = lock.toNanos(n);
        lock = (TimeUnit)this.lock;
        ((ReentrantLock)lock).lockInterruptibly();
        try {
            while (!this.linkLast(node)) {
                if (n <= 0L) {
                    return false;
                }
                n = this.notFull.awaitNanos(n);
            }
            return true;
        }
        finally {
            ((ReentrantLock)lock).unlock();
        }
    }
    
    @Override
    public E peek() {
        return this.peekFirst();
    }
    
    @Override
    public E peekFirst() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            E item;
            if (this.first == null) {
                item = null;
            }
            else {
                item = this.first.item;
            }
            return item;
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public E peekLast() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            E item;
            if (this.last == null) {
                item = null;
            }
            else {
                item = this.last.item;
            }
            return item;
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public E poll() {
        return this.pollFirst();
    }
    
    @Override
    public E poll(final long n, final TimeUnit timeUnit) throws InterruptedException {
        return this.pollFirst(n, timeUnit);
    }
    
    @Override
    public E pollFirst() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return this.unlinkFirst();
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public E pollFirst(long n, TimeUnit lock) throws InterruptedException {
        n = lock.toNanos(n);
        lock = (TimeUnit)this.lock;
        ((ReentrantLock)lock).lockInterruptibly();
        try {
            while (true) {
                final E unlinkFirst = this.unlinkFirst();
                if (unlinkFirst != null) {
                    return unlinkFirst;
                }
                if (n <= 0L) {
                    return null;
                }
                n = this.notEmpty.awaitNanos(n);
            }
        }
        finally {
            ((ReentrantLock)lock).unlock();
        }
    }
    
    @Override
    public E pollLast() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return this.unlinkLast();
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public E pollLast(long n, TimeUnit lock) throws InterruptedException {
        n = lock.toNanos(n);
        lock = (TimeUnit)this.lock;
        ((ReentrantLock)lock).lockInterruptibly();
        try {
            while (true) {
                final E unlinkLast = this.unlinkLast();
                if (unlinkLast != null) {
                    return unlinkLast;
                }
                if (n <= 0L) {
                    return null;
                }
                n = this.notEmpty.awaitNanos(n);
            }
        }
        finally {
            ((ReentrantLock)lock).unlock();
        }
    }
    
    @Override
    public E pop() {
        return this.removeFirst();
    }
    
    @Override
    public void push(final E e) {
        this.addFirst(e);
    }
    
    @Override
    public void put(final E e) throws InterruptedException {
        this.putLast(e);
    }
    
    @Override
    public void putFirst(E lock) throws InterruptedException {
        if (lock == null) {
            throw new NullPointerException();
        }
        final Node<E> node = new Node<E>(lock);
        lock = (E)this.lock;
        ((ReentrantLock)lock).lock();
        try {
            while (!this.linkFirst(node)) {
                this.notFull.await();
            }
        }
        finally {
            ((ReentrantLock)lock).unlock();
        }
        ((ReentrantLock)lock).unlock();
    }
    
    @Override
    public void putLast(E lock) throws InterruptedException {
        if (lock == null) {
            throw new NullPointerException();
        }
        final Node<E> node = new Node<E>(lock);
        lock = (E)this.lock;
        ((ReentrantLock)lock).lock();
        try {
            while (!this.linkLast(node)) {
                this.notFull.await();
            }
        }
        finally {
            ((ReentrantLock)lock).unlock();
        }
        ((ReentrantLock)lock).unlock();
    }
    
    @Override
    public int remainingCapacity() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            final int capacity = this.capacity;
            final int count = this.count;
            lock.unlock();
            return capacity - count;
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public E remove() {
        return this.removeFirst();
    }
    
    @Override
    public boolean remove(final Object o) {
        return this.removeFirstOccurrence(o);
    }
    
    @Override
    public E removeFirst() {
        final E pollFirst = this.pollFirst();
        if (pollFirst == null) {
            throw new NoSuchElementException();
        }
        return pollFirst;
    }
    
    @Override
    public boolean removeFirstOccurrence(final Object o) {
        if (o == null) {
            return false;
        }
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            for (Node<E> node = this.first; node != null; node = node.next) {
                if (o.equals(node.item)) {
                    this.unlink(node);
                    return true;
                }
            }
            return false;
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public E removeLast() {
        final E pollLast = this.pollLast();
        if (pollLast == null) {
            throw new NoSuchElementException();
        }
        return pollLast;
    }
    
    @Override
    public boolean removeLastOccurrence(final Object o) {
        if (o == null) {
            return false;
        }
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            for (Node<E> node = this.last; node != null; node = node.prev) {
                if (o.equals(node.item)) {
                    this.unlink(node);
                    return true;
                }
            }
            return false;
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public int size() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return this.count;
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public E take() throws InterruptedException {
        return this.takeFirst();
    }
    
    @Override
    public E takeFirst() throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            while (this.unlinkFirst() == null) {
                this.notEmpty.await();
            }
        }
        finally {
            lock.unlock();
        }
        lock.unlock();
        return;
    }
    
    @Override
    public E takeLast() throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            while (this.unlinkLast() == null) {
                this.notEmpty.await();
            }
        }
        finally {
            lock.unlock();
        }
        lock.unlock();
        return;
    }
    
    @Override
    public Object[] toArray() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            final Object[] array = new Object[this.count];
            Node<E> node = this.first;
            for (int n = 0; node != null; node = node.next, ++n) {
                array[n] = node.item;
            }
            return array;
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public <T> T[] toArray(final T[] array) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        Object[] array2 = array;
        try {
            if (array.length < this.count) {
                array2 = (Object[])Array.newInstance(array.getClass().getComponentType(), this.count);
            }
            Node<E> node;
            int n;
            for (node = this.first, n = 0; node != null; node = node.next, ++n) {
                array2[n] = node.item;
            }
            if (array2.length > n) {
                array2[n] = null;
            }
            return (T[])array2;
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public String toString() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Node<E> node = this.first;
            if (node == null) {
                return "[]";
            }
            final StringBuilder sb = new StringBuilder();
            sb.append('[');
            while (true) {
                Object item;
                if ((item = node.item) == this) {
                    item = "(this Collection)";
                }
                sb.append(item);
                node = node.next;
                if (node == null) {
                    break;
                }
                sb.append(',').append(' ');
            }
            return sb.append(']').toString();
        }
        finally {
            lock.unlock();
        }
    }
    
    void unlink(final Node<E> node) {
        final Node<E> prev = node.prev;
        final Node<E> next = node.next;
        if (prev == null) {
            this.unlinkFirst();
            return;
        }
        if (next == null) {
            this.unlinkLast();
            return;
        }
        prev.next = next;
        next.prev = prev;
        node.item = null;
        --this.count;
        this.notFull.signal();
    }
    
    private abstract class AbstractItr implements Iterator<E>
    {
        private Node<E> lastRet;
        Node<E> next;
        E nextItem;
        
        AbstractItr() {
            final ReentrantLock lock = LinkedBlockingDeque.this.lock;
            lock.lock();
            try {
                this.next = this.firstNode();
                E item;
                if (this.next == null) {
                    item = null;
                }
                else {
                    item = this.next.item;
                }
                this.nextItem = item;
            }
            finally {
                lock.unlock();
            }
        }
        
        private Node<E> succ(Node<E> node) {
            Node<E> node2;
            while (true) {
                final Node<E> nextNode = this.nextNode(node);
                if (nextNode == null) {
                    node2 = null;
                    break;
                }
                node2 = nextNode;
                if (nextNode.item != null) {
                    break;
                }
                if (nextNode == node) {
                    return this.firstNode();
                }
                node = nextNode;
            }
            return node2;
        }
        
        void advance() {
            final ReentrantLock lock = LinkedBlockingDeque.this.lock;
            lock.lock();
            try {
                this.next = this.succ(this.next);
                E item;
                if (this.next == null) {
                    item = null;
                }
                else {
                    item = this.next.item;
                }
                this.nextItem = item;
            }
            finally {
                lock.unlock();
            }
        }
        
        abstract Node<E> firstNode();
        
        @Override
        public boolean hasNext() {
            return this.next != null;
        }
        
        @Override
        public E next() {
            if (this.next == null) {
                throw new NoSuchElementException();
            }
            this.lastRet = this.next;
            final E nextItem = this.nextItem;
            this.advance();
            return nextItem;
        }
        
        abstract Node<E> nextNode(final Node<E> p0);
        
        @Override
        public void remove() {
            final Node<E> lastRet = this.lastRet;
            if (lastRet == null) {
                throw new IllegalStateException();
            }
            this.lastRet = null;
            final ReentrantLock lock = LinkedBlockingDeque.this.lock;
            lock.lock();
            try {
                if (lastRet.item != null) {
                    LinkedBlockingDeque.this.unlink(lastRet);
                }
            }
            finally {
                lock.unlock();
            }
        }
    }
    
    private class DescendingItr extends LinkedBlockingDeque$AbstractItr
    {
        Node<E> firstNode() {
            return LinkedBlockingDeque.this.last;
        }
        
        Node<E> nextNode(final Node<E> node) {
            return node.prev;
        }
    }
    
    private class Itr extends LinkedBlockingDeque$AbstractItr
    {
        Node<E> firstNode() {
            return LinkedBlockingDeque.this.first;
        }
        
        Node<E> nextNode(final Node<E> node) {
            return node.next;
        }
    }
    
    static final class Node<E>
    {
        E item;
        Node<E> next;
        Node<E> prev;
        
        Node(final E item) {
            this.item = item;
        }
    }
}
