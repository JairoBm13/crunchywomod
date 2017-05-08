// 
// Decompiled by Procyon v0.5.30
// 

package tv.ouya.console.api.store;

import java.util.ArrayList;
import android.os.Parcel;
import java.util.List;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public final class AppDetails implements Parcelable
{
    public static final Parcelable$Creator<AppDetails> CREATOR;
    public Long apkFileSize;
    public String description;
    public String developer;
    public boolean founder;
    public List<String> imageUrls;
    public String latestVersion;
    public Long likeCount;
    public String mainImageFullUrl;
    public String overview;
    public String publishedAt;
    public String rating;
    public String title;
    public String uuid;
    public String versionNumber;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<AppDetails>() {
            public AppDetails createFromParcel(final Parcel parcel) {
                final String string = parcel.readString();
                final String string2 = parcel.readString();
                final String string3 = parcel.readString();
                final String string4 = parcel.readString();
                final long long1 = parcel.readLong();
                final String string5 = parcel.readString();
                final String string6 = parcel.readString();
                final ArrayList<String> list = new ArrayList<String>();
                parcel.readStringList((List)list);
                return new AppDetails(string, string2, string3, string4, long1, string5, string6, list, parcel.readLong(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readByte() == 1, parcel.readString());
            }
            
            public AppDetails[] newArray(final int n) {
                return new AppDetails[n];
            }
        };
    }
    
    public AppDetails() {
        this.imageUrls = new ArrayList<String>();
    }
    
    public AppDetails(final String title, final String description, final String mainImageFullUrl, final String uuid, final Long apkFileSize, final String versionNumber, final String publishedAt, final List<String> imageUrls, final Long likeCount, final String overview, final String rating, final String latestVersion, final Boolean b, final String developer) {
        this.title = title;
        this.description = description;
        this.mainImageFullUrl = mainImageFullUrl;
        this.uuid = uuid;
        this.apkFileSize = apkFileSize;
        this.versionNumber = versionNumber;
        this.publishedAt = publishedAt;
        this.imageUrls = imageUrls;
        this.likeCount = likeCount;
        this.overview = overview;
        this.rating = rating;
        this.latestVersion = latestVersion;
        this.founder = b;
        this.developer = developer;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (!(o instanceof AppDetails)) {
                return false;
            }
            final AppDetails appDetails = (AppDetails)o;
            if (!this.description.equals(appDetails.description)) {
                return false;
            }
            if (!this.mainImageFullUrl.equals(appDetails.mainImageFullUrl)) {
                return false;
            }
            if (!this.title.equals(appDetails.title)) {
                return false;
            }
            if (!this.uuid.equals(appDetails.uuid)) {
                return false;
            }
            if (!this.apkFileSize.equals(appDetails.apkFileSize)) {
                return false;
            }
            if (!this.versionNumber.equals(appDetails.versionNumber)) {
                return false;
            }
            if (!this.publishedAt.equals(appDetails.publishedAt)) {
                return false;
            }
            if (!this.likeCount.equals(appDetails.likeCount)) {
                return false;
            }
            if (!this.overview.equals(appDetails.overview)) {
                return false;
            }
            if (!this.rating.equals(appDetails.rating)) {
                return false;
            }
            if (!this.latestVersion.equals(appDetails.latestVersion)) {
                return false;
            }
            if (this.founder != appDetails.founder) {
                return false;
            }
            if (!this.developer.equals(appDetails.developer)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return ((this.title.hashCode() * 31 + this.description.hashCode()) * 31 + this.mainImageFullUrl.hashCode()) * 31 + this.uuid.hashCode();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.title);
        parcel.writeString(this.description);
        parcel.writeString(this.mainImageFullUrl);
        parcel.writeString(this.uuid);
        parcel.writeLong((long)this.apkFileSize);
        parcel.writeString(this.versionNumber);
        parcel.writeString(this.publishedAt);
        parcel.writeStringList((List)this.imageUrls);
        parcel.writeLong((long)this.likeCount);
        parcel.writeString(this.overview);
        parcel.writeString(this.rating);
        parcel.writeString(this.latestVersion);
        byte b;
        if (this.founder) {
            b = 1;
        }
        else {
            b = 0;
        }
        parcel.writeByte(b);
        parcel.writeString(this.developer);
    }
}
