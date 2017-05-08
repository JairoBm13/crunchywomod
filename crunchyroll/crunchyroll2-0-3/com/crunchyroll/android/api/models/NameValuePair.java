// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class NameValuePair implements Serializable
{
    @JsonProperty("name")
    private String name;
    @JsonProperty("value")
    private JsonNode value;
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final NameValuePair nameValuePair = (NameValuePair)o;
            if (this.name == null) {
                if (nameValuePair.name != null) {
                    return false;
                }
            }
            else if (!this.name.equals(nameValuePair.name)) {
                return false;
            }
            if (this.value == null) {
                if (nameValuePair.value != null) {
                    return false;
                }
            }
            else if (!this.value.toString().equals(nameValuePair.value.toString())) {
                return false;
            }
        }
        return true;
    }
    
    public String getName() {
        return this.name;
    }
    
    public JsonNode getValue() {
        return this.value;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.name == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.name.hashCode();
        }
        if (this.value != null) {
            hashCode = this.value.toString().hashCode();
        }
        return (hashCode2 + 31) * 31 + hashCode;
    }
    
    @Override
    public String toString() {
        return "NameValuePair [name=" + this.name + ", value=" + this.value.toString() + "]";
    }
}
