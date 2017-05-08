// 
// Decompiled by Procyon v0.5.30
// 

package com.conviva.utils;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.List;

public class Eventer
{
    private List<Callable<Void>> _handlers;
    
    public Eventer() {
        this._handlers = new ArrayList<Callable<Void>>();
    }
    
    public void AddHandler(final Callable<Void> callable) {
        this._handlers.add(callable);
    }
    
    public void Cleanup() {
        this._handlers.clear();
    }
    
    public void DeleteHandler(final Callable<Void> callable) {
        this._handlers.remove(callable);
    }
    
    public void DispatchEvent() {
        try {
            final Iterator<Callable<Void>> iterator = this._handlers.iterator();
            while (iterator.hasNext()) {
                iterator.next().call();
            }
        }
        catch (Exception ex) {}
    }
}
