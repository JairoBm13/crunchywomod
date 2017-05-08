// 
// Decompiled by Procyon v0.5.30
// 

package com.google.gson;

import java.util.Map;
import java.util.Set;
import com.google.gson.internal.LinkedTreeMap;

public final class JsonObject extends JsonElement
{
    private final LinkedTreeMap<String, JsonElement> members;
    
    public JsonObject() {
        this.members = new LinkedTreeMap<String, JsonElement>();
    }
    
    public void add(final String s, final JsonElement jsonElement) {
        JsonElement instance = jsonElement;
        if (jsonElement == null) {
            instance = JsonNull.INSTANCE;
        }
        this.members.put(s, instance);
    }
    
    public Set<Map.Entry<String, JsonElement>> entrySet() {
        return this.members.entrySet();
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof JsonObject && ((JsonObject)o).members.equals(this.members));
    }
    
    public JsonElement get(final String s) {
        return this.members.get(s);
    }
    
    public boolean has(final String s) {
        return this.members.containsKey(s);
    }
    
    @Override
    public int hashCode() {
        return this.members.hashCode();
    }
}
