// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class Category implements Serializable
{
    private static final long serialVersionUID = 5164052969647603035L;
    @JsonProperty("label")
    private String label;
    @JsonProperty("tag")
    private String tag;
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final Category category = (Category)o;
            if (this.label == null) {
                if (category.label != null) {
                    return false;
                }
            }
            else if (!this.label.equals(category.label)) {
                return false;
            }
            if (this.tag == null) {
                if (category.tag != null) {
                    return false;
                }
            }
            else if (!this.tag.equals(category.tag)) {
                return false;
            }
        }
        return true;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public String getTag() {
        return this.tag;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.label == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.label.hashCode();
        }
        if (this.tag != null) {
            hashCode = this.tag.hashCode();
        }
        return (hashCode2 + 31) * 31 + hashCode;
    }
    
    public void setLabel(final String label) {
        this.label = label;
    }
    
    public void setTag(final String tag) {
        this.tag = tag;
    }
    
    @Override
    public String toString() {
        return "Category [tag=" + this.tag + ", label=" + this.label + "]";
    }
}
