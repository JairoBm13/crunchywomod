// 
// Decompiled by Procyon v0.5.30
// 

package com.conviva.utils;

import java.util.LinkedList;
import java.util.List;

public class SlidingWindow<T>
{
    private int _capacity;
    private List<T> _slots;
    
    public SlidingWindow(final int capacity) {
        this._capacity = 0;
        this._slots = null;
        if (capacity > 0) {
            this._capacity = capacity;
            this._slots = new LinkedList<T>();
        }
    }
    
    public void add(final T t) {
        this._slots.add(0, t);
        if (this._slots.size() > this._capacity) {
            this._slots.remove(this._slots.size() - 1);
        }
    }
    
    public void clear() {
        this._slots = new LinkedList<T>();
    }
    
    public List<T> getSlots() {
        return this._slots;
    }
    
    public int size() {
        return this._slots.size();
    }
}
