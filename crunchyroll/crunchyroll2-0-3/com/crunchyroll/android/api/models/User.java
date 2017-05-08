// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

import com.crunchyroll.android.util.DateFormatter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable
{
    private static final long serialVersionUID = 223157827167849971L;
    @JsonProperty("created")
    private Date created;
    @JsonProperty("email")
    private String email;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("user_id")
    private Long id;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("premium")
    private String premium;
    @JsonProperty("username")
    private String username;
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final User user = (User)o;
            if (this.created == null) {
                if (user.created != null) {
                    return false;
                }
            }
            else if (!this.created.equals(user.created)) {
                return false;
            }
            if (this.email == null) {
                if (user.email != null) {
                    return false;
                }
            }
            else if (!this.email.equals(user.email)) {
                return false;
            }
            if (this.firstName == null) {
                if (user.firstName != null) {
                    return false;
                }
            }
            else if (!this.firstName.equals(user.firstName)) {
                return false;
            }
            if (this.id == null) {
                if (user.id != null) {
                    return false;
                }
            }
            else if (!this.id.equals(user.id)) {
                return false;
            }
            if (this.lastName == null) {
                if (user.lastName != null) {
                    return false;
                }
            }
            else if (!this.lastName.equals(user.lastName)) {
                return false;
            }
            if (this.premium == null) {
                if (user.premium != null) {
                    return false;
                }
            }
            else if (!this.premium.equals(user.premium)) {
                return false;
            }
            if (this.username == null) {
                if (user.username != null) {
                    return false;
                }
            }
            else if (!this.username.equals(user.username)) {
                return false;
            }
        }
        return true;
    }
    
    public Date getCreated() {
        return this.created;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public String getFirstName() {
        return this.firstName;
    }
    
    public Long getId() {
        return this.id;
    }
    
    public String getLastName() {
        return this.lastName;
    }
    
    public String getPremium() {
        return this.premium;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.created == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.created.hashCode();
        }
        int hashCode3;
        if (this.email == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = this.email.hashCode();
        }
        int hashCode4;
        if (this.firstName == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = this.firstName.hashCode();
        }
        int hashCode5;
        if (this.id == null) {
            hashCode5 = 0;
        }
        else {
            hashCode5 = this.id.hashCode();
        }
        int hashCode6;
        if (this.lastName == null) {
            hashCode6 = 0;
        }
        else {
            hashCode6 = this.lastName.hashCode();
        }
        int hashCode7;
        if (this.premium == null) {
            hashCode7 = 0;
        }
        else {
            hashCode7 = this.premium.hashCode();
        }
        if (this.username != null) {
            hashCode = this.username.hashCode();
        }
        return ((((((hashCode2 + 31) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + hashCode6) * 31 + hashCode7) * 31 + hashCode;
    }
    
    public void setCreated(final String s) {
        this.created = DateFormatter.parse(s);
    }
    
    public void setEmail(final String email) {
        this.email = email;
    }
    
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }
    
    public void setPremium(final String premium) {
        this.premium = premium;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    @Override
    public String toString() {
        return "User [id=" + this.id + ", username=" + this.username + ", email=" + this.email + ", firstName=" + this.firstName + ", lastName=" + this.lastName + ", premium=" + this.premium + ", created=" + this.created + "]";
    }
}
