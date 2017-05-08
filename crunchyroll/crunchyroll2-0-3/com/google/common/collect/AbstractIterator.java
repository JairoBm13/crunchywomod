// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.NoSuchElementException;
import com.google.common.base.Preconditions;

public abstract class AbstractIterator<T> extends UnmodifiableIterator<T>
{
    private T next;
    private State state;
    
    protected AbstractIterator() {
        this.state = State.NOT_READY;
    }
    
    private boolean tryToComputeNext() {
        this.state = State.FAILED;
        this.next = this.computeNext();
        if (this.state != State.DONE) {
            this.state = State.READY;
            return true;
        }
        return false;
    }
    
    protected abstract T computeNext();
    
    protected final T endOfData() {
        this.state = State.DONE;
        return null;
    }
    
    @Override
    public final boolean hasNext() {
        final boolean b = false;
        Preconditions.checkState(this.state != State.FAILED);
        boolean tryToComputeNext = b;
        switch (this.state) {
            default: {
                tryToComputeNext = this.tryToComputeNext();
                return tryToComputeNext;
            }
            case DONE: {
                return tryToComputeNext;
            }
            case READY: {
                return true;
            }
        }
    }
    
    @Override
    public final T next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        this.state = State.NOT_READY;
        return this.next;
    }
    
    private enum State
    {
        DONE, 
        FAILED, 
        NOT_READY, 
        READY;
    }
}
