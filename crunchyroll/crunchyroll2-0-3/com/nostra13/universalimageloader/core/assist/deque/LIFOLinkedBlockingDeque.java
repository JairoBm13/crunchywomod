// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.assist.deque;

public class LIFOLinkedBlockingDeque<T> extends LinkedBlockingDeque<T>
{
    private static final long serialVersionUID = -4114786347960826192L;
    
    @Override
    public boolean offer(final T t) {
        return super.offerFirst(t);
    }
    
    @Override
    public T remove() {
        return super.removeFirst();
    }
}
