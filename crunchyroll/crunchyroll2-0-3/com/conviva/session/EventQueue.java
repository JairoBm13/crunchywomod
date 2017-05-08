// 
// Decompiled by Procyon v0.5.30
// 

package com.conviva.session;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class EventQueue
{
    private List<Map<String, Object>> _events;
    private int _nextSeqNumber;
    
    public EventQueue() {
        this._events = null;
        this._nextSeqNumber = 0;
        this._events = new ArrayList<Map<String, Object>>();
    }
    
    public void enqueueEvent(final String s, final Map<String, Object> map, final int n) {
        map.put("t", s);
        map.put("st", n);
        map.put("seq", this._nextSeqNumber);
        ++this._nextSeqNumber;
        this._events.add(map);
    }
    
    public List<Map<String, Object>> flushEvents() {
        final List<Map<String, Object>> events = this._events;
        this._events = new ArrayList<Map<String, Object>>();
        return events;
    }
    
    public int size() {
        return this._events.size();
    }
}
