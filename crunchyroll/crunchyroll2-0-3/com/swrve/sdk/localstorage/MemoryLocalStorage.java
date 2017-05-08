// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.localstorage;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class MemoryLocalStorage implements IFlushableLocalStorage, ILocalStorage
{
    private List<StoredEvent> events;
    private Map<String, StoredCacheEntry> serverCache;
    
    public MemoryLocalStorage() {
        this.events = new ArrayList<StoredEvent>();
        this.serverCache = new HashMap<String, StoredCacheEntry>();
    }
    
    @Override
    public void addEvent(final String event) throws Exception {
        synchronized (this) {
            if (this.events.size() < 2000) {
                final StoredEvent storedEvent = new StoredEvent();
                storedEvent.event = event;
                this.events.add(storedEvent);
            }
        }
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public void flushCache(final IFastInsertLocalStorage fastInsertLocalStorage) {
        final ArrayList<SimpleEntry<String, SimpleEntry<String, String>>> multipleCacheEntries;
        synchronized (this) {
            final Iterator<String> iterator = this.serverCache.keySet().iterator();
            multipleCacheEntries = new ArrayList<SimpleEntry<String, SimpleEntry<String, String>>>();
            while (iterator.hasNext()) {
                final StoredCacheEntry storedCacheEntry = this.serverCache.get(iterator.next());
                multipleCacheEntries.add(new SimpleEntry<String, SimpleEntry<String, String>>(storedCacheEntry.userId, new SimpleEntry<String, String>(storedCacheEntry.category, storedCacheEntry.rawData)));
            }
        }
        final IFastInsertLocalStorage fastInsertLocalStorage2;
        fastInsertLocalStorage2.setMultipleCacheEntries((List<Map.Entry<String, Map.Entry<String, String>>>)multipleCacheEntries);
        this.serverCache.clear();
    }
    // monitorexit(this)
    
    @Override
    public void flushEvents(final IFastInsertLocalStorage fastInsertLocalStorage) {
        final ArrayList<String> list;
        synchronized (this) {
            final Iterator<StoredEvent> iterator = this.events.iterator();
            list = new ArrayList<String>();
            while (iterator.hasNext()) {
                list.add(iterator.next().event);
            }
        }
        final IFastInsertLocalStorage fastInsertLocalStorage2;
        fastInsertLocalStorage2.addMultipleEvent(list);
        this.events.clear();
    }
    // monitorexit(this)
    
    @Override
    public String getCacheEntryForUser(String s, final String s2) {
        synchronized (this) {
            s = s + "##" + s2;
            final StoredCacheEntry storedCacheEntry = this.serverCache.get(s);
            if (storedCacheEntry != null) {
                s = storedCacheEntry.rawData;
            }
            else {
                s = null;
            }
            return s;
        }
    }
    
    @Override
    public LinkedHashMap<Long, String> getFirstNEvents(final Integer n) {
        synchronized (this) {
            final LinkedHashMap<Long, String> linkedHashMap = new LinkedHashMap<Long, String>();
            int intValue = n;
            for (Iterator<StoredEvent> iterator = this.events.iterator(); iterator.hasNext() && intValue > 0; --intValue) {
                final StoredEvent storedEvent = iterator.next();
                linkedHashMap.put(storedEvent.id, storedEvent.event);
            }
            return linkedHashMap;
        }
    }
    
    @Override
    public void removeEventsById(final Collection<Long> collection) {
        synchronized (this) {
            final Iterator<StoredEvent> iterator = this.events.iterator();
            while (iterator.hasNext()) {
                if (collection.contains(iterator.next().id)) {
                    iterator.remove();
                }
            }
        }
    }
    // monitorexit(this)
    
    @Override
    public void setCacheEntryForUser(final String userId, final String category, final String rawData) {
        synchronized (this) {
            final String string = userId + "##" + category;
            StoredCacheEntry storedCacheEntry2;
            final StoredCacheEntry storedCacheEntry = storedCacheEntry2 = this.serverCache.get(string);
            if (storedCacheEntry == null) {
                storedCacheEntry2 = storedCacheEntry;
                if (this.serverCache.size() < 2000) {
                    storedCacheEntry2 = new StoredCacheEntry();
                    this.serverCache.put(string, storedCacheEntry2);
                }
            }
            if (storedCacheEntry2 != null) {
                storedCacheEntry2.userId = userId;
                storedCacheEntry2.category = category;
                storedCacheEntry2.rawData = rawData;
            }
        }
    }
    
    @Override
    public void setSecureCacheEntryForUser(final String s, final String s2, final String s3, final String s4) {
        synchronized (this) {
            this.setCacheEntryForUser(s, s2, s3);
            this.setCacheEntryForUser(s, s2 + "_SGT", s4);
        }
    }
    
    private static class StoredCacheEntry
    {
        public String category;
        public String rawData;
        public String userId;
    }
    
    private static class StoredEvent
    {
        public static long eventCount;
        public String event;
        public long id;
        
        static {
            StoredEvent.eventCount = 0L;
        }
        
        public StoredEvent() {
            final long eventCount = StoredEvent.eventCount;
            StoredEvent.eventCount = 1L + eventCount;
            this.id = eventCount;
        }
    }
}
