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
public class FreeTrialInformationItem implements Parcelable
{
    public static final Parcelable$Creator<FreeTrialInformationItem> CREATOR;
    @JsonProperty("duration")
    Integer duration;
    @JsonProperty("eligible")
    Boolean eligible;
    @JsonProperty("media_type")
    String mediaType;
    @JsonProperty("name")
    String name;
    @JsonProperty("recur_duration")
    Integer recurringDuration;
    @JsonProperty("recur_price")
    Map<String, String> recurringPrice;
    @JsonProperty("sku")
    String sku;
    @JsonProperty("span_type")
    String spanType;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<FreeTrialInformationItem>() {
            public FreeTrialInformationItem createFromParcel(final Parcel parcel) {
                return new FreeTrialInformationItem(parcel, null);
            }
            
            public FreeTrialInformationItem[] newArray(final int n) {
                return new FreeTrialInformationItem[n];
            }
        };
    }
    
    public FreeTrialInformationItem() {
    }
    
    private FreeTrialInformationItem(final Parcel parcel) {
        this.name = parcel.readString();
        this.sku = parcel.readString();
        this.mediaType = parcel.readString();
        this.duration = parcel.readInt();
        this.recurringDuration = parcel.readInt();
        this.eligible = (parcel.readByte() != 0);
        this.spanType = parcel.readString();
        final int int1 = parcel.readInt();
        this.recurringPrice = new HashMap<String, String>(int1);
        for (int i = 0; i < int1; ++i) {
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
            final FreeTrialInformationItem freeTrialInformationItem = (FreeTrialInformationItem)o;
            if (this.duration == null) {
                if (freeTrialInformationItem.duration != null) {
                    return false;
                }
            }
            else if (!this.duration.equals(freeTrialInformationItem.duration)) {
                return false;
            }
            if (this.eligible == null) {
                if (freeTrialInformationItem.eligible != null) {
                    return false;
                }
            }
            else if (!this.eligible.equals(freeTrialInformationItem.eligible)) {
                return false;
            }
            if (this.mediaType == null) {
                if (freeTrialInformationItem.mediaType != null) {
                    return false;
                }
            }
            else if (!this.mediaType.equals(freeTrialInformationItem.mediaType)) {
                return false;
            }
            if (this.name == null) {
                if (freeTrialInformationItem.name != null) {
                    return false;
                }
            }
            else if (!this.name.equals(freeTrialInformationItem.name)) {
                return false;
            }
            if (this.recurringDuration == null) {
                if (freeTrialInformationItem.recurringDuration != null) {
                    return false;
                }
            }
            else if (!this.recurringDuration.equals(freeTrialInformationItem.recurringDuration)) {
                return false;
            }
            if (this.recurringPrice == null) {
                if (freeTrialInformationItem.recurringPrice != null) {
                    return false;
                }
            }
            else if (!this.recurringPrice.equals(freeTrialInformationItem.recurringPrice)) {
                return false;
            }
            if (this.sku == null) {
                if (freeTrialInformationItem.sku != null) {
                    return false;
                }
            }
            else if (!this.sku.equals(freeTrialInformationItem.sku)) {
                return false;
            }
            if (this.spanType == null) {
                if (freeTrialInformationItem.spanType != null) {
                    return false;
                }
            }
            else if (!this.spanType.equals(freeTrialInformationItem.spanType)) {
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
    
    public Integer getDuration() {
        return this.duration;
    }
    
    public Boolean getEligible() {
        return this.eligible;
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
    
    public String getPriceAndSymbol(final String s) throws NumberFormatException {
        Float.valueOf(this.recurringPrice.get(s));
        return Currency.getInstance(s).getSymbol() + this.recurringPrice.get(s);
    }
    
    public Integer getRecurringDuration() {
        return this.recurringDuration;
    }
    
    public Map<String, String> getRecurringPrice() {
        return this.recurringPrice;
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
        if (this.duration == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.duration.hashCode();
        }
        int hashCode3;
        if (this.eligible == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = this.eligible.hashCode();
        }
        int hashCode4;
        if (this.mediaType == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = this.mediaType.hashCode();
        }
        int hashCode5;
        if (this.name == null) {
            hashCode5 = 0;
        }
        else {
            hashCode5 = this.name.hashCode();
        }
        int hashCode6;
        if (this.recurringDuration == null) {
            hashCode6 = 0;
        }
        else {
            hashCode6 = this.recurringDuration.hashCode();
        }
        int hashCode7;
        if (this.recurringPrice == null) {
            hashCode7 = 0;
        }
        else {
            hashCode7 = this.recurringPrice.hashCode();
        }
        int hashCode8;
        if (this.sku == null) {
            hashCode8 = 0;
        }
        else {
            hashCode8 = this.sku.hashCode();
        }
        if (this.spanType != null) {
            hashCode = this.spanType.hashCode();
        }
        return (((((((hashCode2 + 31) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + hashCode6) * 31 + hashCode7) * 31 + hashCode8) * 31 + hashCode;
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
    
    public void setEligible(final Boolean eligible) {
        this.eligible = eligible;
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
        return "FreeTrialInformationItem [name=" + this.name + ", sku=" + this.sku + ", mediaType=" + this.mediaType + ", duration=" + this.duration + ", recurringPrice=" + this.recurringPrice + ", recurringDuration=" + this.recurringDuration + ", eligible=" + this.eligible + ", spanType=" + this.spanType + "]";
    }
    
    public void writeToParcel(final Parcel parcel, int n) {
        n = 0;
        parcel.writeString(this.name);
        parcel.writeString(this.sku);
        parcel.writeString(this.mediaType);
        parcel.writeInt((int)this.duration);
        parcel.writeInt((int)this.recurringDuration);
        if (this.eligible == null) {
            parcel.writeByte((byte)0);
        }
        else {
            if (this.eligible) {
                n = 1;
            }
            parcel.writeByte((byte)n);
        }
        parcel.writeString(this.spanType);
        parcel.writeInt(this.recurringPrice.size());
        for (final Map.Entry<String, String> entry : this.recurringPrice.entrySet()) {
            parcel.writeString((String)entry.getKey());
            parcel.writeString((String)entry.getValue());
        }
    }
}
