// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.localstorage;

import java.util.Collection;
import java.util.LinkedHashMap;

public interface ILocalStorage
{
    void addEvent(final String p0) throws Exception;
    
    void close();
    
    String getCacheEntryForUser(final String p0, final String p1);
    
    LinkedHashMap<Long, String> getFirstNEvents(final Integer p0);
    
    void removeEventsById(final Collection<Long> p0);
    
    void setCacheEntryForUser(final String p0, final String p1, final String p2);
    
    void setSecureCacheEntryForUser(final String p0, final String p1, final String p2, final String p3);
}
