// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

import com.crunchyroll.android.util.DateFormatter;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticateItem implements Serializable
{
    private static final long serialVersionUID = 445231392171829007L;
    @JsonProperty("auth")
    private String auth;
    @JsonProperty("expires")
    private Date expires;
    @JsonProperty("user")
    private User user;
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final AuthenticateItem authenticateItem = (AuthenticateItem)o;
            if (this.auth == null) {
                if (authenticateItem.auth != null) {
                    return false;
                }
            }
            else if (!this.auth.equals(authenticateItem.auth)) {
                return false;
            }
            if (this.expires == null) {
                if (authenticateItem.expires != null) {
                    return false;
                }
            }
            else if (!this.expires.equals(authenticateItem.expires)) {
                return false;
            }
            if (this.user == null) {
                if (authenticateItem.user != null) {
                    return false;
                }
            }
            else if (!this.user.equals(authenticateItem.user)) {
                return false;
            }
        }
        return true;
    }
    
    public String getAuth() {
        return this.auth;
    }
    
    public Date getExpires() {
        return this.expires;
    }
    
    public User getUser() {
        return this.user;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.auth == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.auth.hashCode();
        }
        int hashCode3;
        if (this.expires == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = this.expires.hashCode();
        }
        if (this.user != null) {
            hashCode = this.user.hashCode();
        }
        return ((hashCode2 + 31) * 31 + hashCode3) * 31 + hashCode;
    }
    
    public void setAuth(final String auth) {
        this.auth = auth;
    }
    
    public void setExpires(final String s) {
        this.expires = DateFormatter.parse(s);
    }
    
    public void setUser(final User user) {
        this.user = user;
    }
    
    @Override
    public String toString() {
        return "Login [user=" + this.user + ", auth=" + this.auth + ", expires=" + this.expires + "]";
    }
}
