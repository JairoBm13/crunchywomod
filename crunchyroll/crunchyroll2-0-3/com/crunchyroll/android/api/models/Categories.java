// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class Categories implements Serializable
{
    private static final long serialVersionUID = 8941117995340164731L;
    @JsonProperty("genre")
    private ArrayList<Category> genres;
    @JsonProperty("season")
    private ArrayList<Category> seasons;
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final Categories categories = (Categories)o;
            if (this.genres == null) {
                if (categories.genres != null) {
                    return false;
                }
            }
            else if (!this.genres.equals(categories.genres)) {
                return false;
            }
            if (this.seasons == null) {
                if (categories.seasons != null) {
                    return false;
                }
            }
            else if (!this.seasons.equals(categories.seasons)) {
                return false;
            }
        }
        return true;
    }
    
    public ArrayList<Category> getGenres() {
        return this.genres;
    }
    
    public ArrayList<Category> getSeasons() {
        return this.seasons;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.genres == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.genres.hashCode();
        }
        if (this.seasons != null) {
            hashCode = this.seasons.hashCode();
        }
        return (hashCode2 + 31) * 31 + hashCode;
    }
    
    public void setGenres(final ArrayList<Category> genres) {
        this.genres = genres;
    }
    
    public void setSeasons(final ArrayList<Category> seasons) {
        this.seasons = seasons;
    }
    
    @Override
    public String toString() {
        return "Categories [genres=" + this.genres + ", seasons=" + this.seasons + "]";
    }
}
