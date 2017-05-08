// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

import java.util.Currency;
import java.util.Iterator;
import java.util.HashMap;
import android.os.Parcel;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;
import android.os.Parcelable$Creator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import android.os.Parcelable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MembershipInfoItem implements Parcelable
{
    public static final Parcelable$Creator<MembershipInfoItem> CREATOR;
    @JsonProperty("description")
    String description;
    @JsonProperty("duration")
    Integer duration;
    @JsonProperty("media_type")
    String mediaType;
    @JsonProperty("name")
    String name;
    @JsonProperty("product_price")
    Map<String, String> productPrice;
    @JsonProperty("recur_duration")
    Integer recurringDuration;
    @JsonProperty("recur_price")
    Map<String, String> recurringPrice;
    @JsonProperty("recur_span_type")
    String recurringSpanType;
    @JsonProperty("sku")
    String sku;
    @JsonProperty("span_type")
    String spanType;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<MembershipInfoItem>() {
            public MembershipInfoItem createFromParcel(final Parcel parcel) {
                return new MembershipInfoItem(parcel, null);
            }
            
            public MembershipInfoItem[] newArray(final int n) {
                return new MembershipInfoItem[n];
            }
        };
    }
    
    public MembershipInfoItem() {
    }
    
    private MembershipInfoItem(final Parcel parcel) {
        this.name = parcel.readString();
        this.sku = parcel.readString();
        this.mediaType = parcel.readString();
        this.description = parcel.readString();
        this.duration = parcel.readInt();
        this.spanType = parcel.readString();
        this.recurringDuration = parcel.readInt();
        this.recurringSpanType = parcel.readString();
        final int int1 = parcel.readInt();
        this.productPrice = new HashMap<String, String>(int1);
        for (int i = 0; i < int1; ++i) {
            this.productPrice.put(parcel.readString(), parcel.readString());
        }
        final int int2 = parcel.readInt();
        this.recurringPrice = new HashMap<String, String>(int2);
        for (int j = 0; j < int2; ++j) {
            this.recurringPrice.put(parcel.readString(), parcel.readString());
        }
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final MembershipInfoItem membershipInfoItem = (MembershipInfoItem)o;
            if (this.name == null) {
                if (membershipInfoItem.name != null) {
                    return false;
                }
            }
            else if (!this.name.equals(membershipInfoItem.name)) {
                return false;
            }
            if (this.sku == null) {
                if (membershipInfoItem.sku != null) {
                    return false;
                }
            }
            else if (!this.sku.equals(membershipInfoItem.sku)) {
                return false;
            }
            if (this.mediaType == null) {
                if (membershipInfoItem.mediaType != null) {
                    return false;
                }
            }
            else if (!this.mediaType.equals(membershipInfoItem.mediaType)) {
                return false;
            }
            if (this.description == null) {
                if (membershipInfoItem.description != null) {
                    return false;
                }
            }
            else if (!this.description.equals(membershipInfoItem.description)) {
                return false;
            }
            if (this.duration == null) {
                if (membershipInfoItem.duration != null) {
                    return false;
                }
            }
            else if (!this.duration.equals(membershipInfoItem.duration)) {
                return false;
            }
            if (this.spanType == null) {
                if (membershipInfoItem.spanType != null) {
                    return false;
                }
            }
            else if (!this.spanType.equals(membershipInfoItem.spanType)) {
                return false;
            }
            if (this.productPrice == null) {
                if (membershipInfoItem.productPrice != null) {
                    return false;
                }
            }
            else if (!this.productPrice.equals(membershipInfoItem.productPrice)) {
                return false;
            }
            if (this.recurringPrice == null) {
                if (membershipInfoItem.recurringPrice != null) {
                    return false;
                }
            }
            else if (!this.recurringPrice.equals(membershipInfoItem.recurringPrice)) {
                return false;
            }
            if (this.recurringDuration == null) {
                if (membershipInfoItem.recurringDuration != null) {
                    return false;
                }
            }
            else if (!this.recurringDuration.equals(membershipInfoItem.recurringDuration)) {
                return false;
            }
            if (this.recurringSpanType == null) {
                if (membershipInfoItem.recurringSpanType != null) {
                    return false;
                }
            }
            else if (!this.recurringSpanType.equals(membershipInfoItem.recurringSpanType)) {
                return false;
            }
        }
        return true;
    }
    
    public String getCurrency() {
        final Iterator<String> iterator = this.recurringPrice.keySet().iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public Integer getDuration() {
        return this.duration;
    }
    
    public String getMediaType() {
        return this.mediaType;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getPriceAndSymbol() {
        final Iterator<String> iterator = this.recurringPrice.keySet().iterator();
        if (iterator.hasNext()) {
            final String s = iterator.next();
            return Currency.getInstance(s).getSymbol() + this.recurringPrice.get(s);
        }
        return null;
    }
    
    public String getPriceAndSymbol(final String s) {
        return Currency.getInstance(s).getSymbol() + this.recurringPrice.get(s);
    }
    
    public Map<String, String> getProductPrice() {
        return this.productPrice;
    }
    
    public Integer getRecurringDuration() {
        return this.recurringDuration;
    }
    
    public Map<String, String> getRecurringPrice() {
        return this.recurringPrice;
    }
    
    public String getRecurringSpanType() {
        return this.recurringSpanType;
    }
    
    public String getSku() {
        return this.sku;
    }
    
    public String getSpanType() {
        return this.spanType;
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
        int hashCode3;
        if (this.sku == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = this.sku.hashCode();
        }
        int hashCode4;
        if (this.mediaType == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = this.mediaType.hashCode();
        }
        int hashCode5;
        if (this.description == null) {
            hashCode5 = 0;
        }
        else {
            hashCode5 = this.description.hashCode();
        }
        int hashCode6;
        if (this.duration == null) {
            hashCode6 = 0;
        }
        else {
            hashCode6 = this.duration.hashCode();
        }
        int hashCode7;
        if (this.spanType == null) {
            hashCode7 = 0;
        }
        else {
            hashCode7 = this.spanType.hashCode();
        }
        int hashCode8;
        if (this.productPrice == null) {
            hashCode8 = 0;
        }
        else {
            hashCode8 = this.productPrice.hashCode();
        }
        int hashCode9;
        if (this.recurringPrice == null) {
            hashCode9 = 0;
        }
        else {
            hashCode9 = this.recurringPrice.hashCode();
        }
        int hashCode10;
        if (this.recurringDuration == null) {
            hashCode10 = 0;
        }
        else {
            hashCode10 = this.recurringDuration.hashCode();
        }
        if (this.recurringSpanType != null) {
            hashCode = this.recurringSpanType.hashCode();
        }
        return (((((((((hashCode2 + 31) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + hashCode6) * 31 + hashCode7) * 31 + hashCode8) * 31 + hashCode9) * 31 + hashCode10) * 31 + hashCode;
    }
    
    public boolean isAllAccess() {
        return this.mediaType != null && this.mediaType.contains("anime") && this.mediaType.contains("drama");
    }
    
    public boolean isAnimeOnly() {
        return this.mediaType != null && this.mediaType.contains("anime") && !this.mediaType.contains("drama");
    }
    
    public void setDuration(final Integer duration) {
        this.duration = duration;
    }
    
    public void setMediaType(final String mediaType) {
        this.mediaType = mediaType;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setRecurringDuration(final Integer recurringDuration) {
        this.recurringDuration = recurringDuration;
    }
    
    public void setRecurringPrice(final Map<String, String> recurringPrice) {
        this.recurringPrice = recurringPrice;
    }
    
    public void setSku(final String sku) {
        this.sku = sku;
    }
    
    public void setSpanType(final String spanType) {
        this.spanType = spanType;
    }
    
    @Override
    public String toString() {
        return "MembershipInfoItem [name=" + this.name + ", sku=" + this.sku + ", mediaType=" + this.mediaType + ", description=" + this.description + ", duration=" + this.duration + ", spanType=" + this.spanType + ", productPrice=" + this.productPrice + ", recurringPrice=" + this.recurringPrice + ", recurringDuration=" + this.recurringDuration + ", recurringSpanType=" + this.recurringSpanType + "]";
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.name);
        parcel.writeString(this.sku);
        parcel.writeString(this.mediaType);
        parcel.writeString(this.description);
        parcel.writeInt((int)this.duration);
        parcel.writeString(this.spanType);
        parcel.writeInt((int)this.recurringDuration);
        parcel.writeString(this.recurringSpanType);
        if (this.productPrice == null) {
            parcel.writeInt(0);
        }
        else {
            parcel.writeInt(this.productPrice.size());
            for (final Map.Entry<String, String> entry : this.productPrice.entrySet()) {
                parcel.writeString((String)entry.getKey());
                parcel.writeString((String)entry.getValue());
            }
        }
        if (this.recurringPrice == null) {
            parcel.writeInt(0);
        }
        else {
            parcel.writeInt(this.recurringPrice.size());
            for (final Map.Entry<String, String> entry2 : this.recurringPrice.entrySet()) {
                parcel.writeString((String)entry2.getKey());
                parcel.writeString((String)entry2.getValue());
            }
        }
    }
}
