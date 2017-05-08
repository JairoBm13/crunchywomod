// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RelatedApp implements Serializable
{
    private static final long serialVersionUID = -1341681260586175012L;
    @JsonProperty("app_scheme")
    private String appScheme;
    @JsonProperty("description")
    private String description;
    @JsonProperty("id")
    private String id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("url")
    private String url;
    
    public String getAppScheme() {
        return this.appScheme;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getUrl() {
        return this.url;
    }
}
