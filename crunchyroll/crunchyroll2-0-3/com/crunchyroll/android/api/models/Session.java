// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

import com.fasterxml.jackson.databind.node.MissingNode;
import com.google.common.base.Optional;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Session implements Serializable
{
    private static final long serialVersionUID = -5599965227334293877L;
    @JsonProperty("auth")
    private String auth;
    @JsonProperty("country_code")
    private String countryCode;
    @JsonProperty("session_id")
    private String id;
    @JsonProperty("ops")
    private JsonNode ops;
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
            final Session session = (Session)o;
            if (this.auth == null) {
                if (session.auth != null) {
                    return false;
                }
            }
            else if (!this.auth.equals(session.auth)) {
                return false;
            }
            if (this.countryCode == null) {
                if (session.countryCode != null) {
                    return false;
                }
            }
            else if (!this.countryCode.equals(session.countryCode)) {
                return false;
            }
            if (this.id == null) {
                if (session.id != null) {
                    return false;
                }
            }
            else if (!this.id.equals(session.id)) {
                return false;
            }
            if (this.ops == null) {
                if (session.ops != null) {
                    return false;
                }
            }
            else if (!this.ops.equals(session.ops)) {
                return false;
            }
            if (this.user == null) {
                if (session.user != null) {
                    return false;
                }
            }
            else if (!this.user.equals(session.user)) {
                return false;
            }
        }
        return true;
    }
    
    public Optional<String> getAuth() {
        return Optional.fromNullable(this.auth);
    }
    
    public String getCountryCode() {
        return this.countryCode;
    }
    
    public String getId() {
        return this.id;
    }
    
    public JsonNode getOps() {
        if (this.ops == null) {
            return MissingNode.getInstance();
        }
        return this.ops;
    }
    
    public Optional<User> getUser() {
        return Optional.fromNullable(this.user);
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
        if (this.countryCode == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = this.countryCode.hashCode();
        }
        int hashCode4;
        if (this.id == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = this.id.hashCode();
        }
        int hashCode5;
        if (this.ops == null) {
            hashCode5 = 0;
        }
        else {
            hashCode5 = this.ops.hashCode();
        }
        if (this.user != null) {
            hashCode = this.user.hashCode();
        }
        return ((((hashCode2 + 31) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + hashCode;
    }
    
    public void setAuth(final String auth) {
        this.auth = auth;
    }
    
    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setOps(final JsonNode ops) {
        this.ops = ops;
    }
    
    public void setUser(final User user) {
        this.user = user;
    }
    
    @Override
    public String toString() {
        return "Session [id=" + this.id + ", countryCode=" + this.countryCode + ", auth=" + this.auth + ", user=" + this.user + ", ops=" + this.ops + "]";
    }
}
