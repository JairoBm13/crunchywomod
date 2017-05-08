// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.localstorage;

import java.util.Map;
import java.util.List;

public interface IFastInsertLocalStorage
{
    void addMultipleEvent(final List<String> p0);
    
    void setMultipleCacheEntries(final List<Map.Entry<String, Map.Entry<String, String>>> p0);
}
