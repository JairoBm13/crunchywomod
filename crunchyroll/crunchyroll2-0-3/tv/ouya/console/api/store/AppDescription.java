// 
// Decompiled by Procyon v0.5.30
// 

package tv.ouya.console.api.store;

import java.util.Iterator;
import android.os.Parcel;
import java.util.HashMap;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class AppDescription implements Parcelable
{
    private static final String[] APP_STATUSES;
    public static final Parcelable$Creator<AppDescription> CREATOR;
    private String imageUrl;
    private String title;
    private String uuid;
    private HashMap<String, AppVersion> versions;
    
    static {
        APP_STATUSES = new String[] { "unsubmitted", "submitted", "under_review", "approved", "published", "rejected" };
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<AppDescription>() {
            public AppDescription createFromParcel(final Parcel parcel) {
                final String string = parcel.readString();
                final String string2 = parcel.readString();
                final String string3 = parcel.readString();
                final int int1 = parcel.readInt();
                HashMap<String, AppVersion> hashMap = null;
                if (int1 > -1) {
                    final HashMap<String, AppVersion> hashMap2 = new HashMap<String, AppVersion>();
                    int n = 0;
                    while (true) {
                        hashMap = hashMap2;
                        if (n >= int1) {
                            break;
                        }
                        final String string4 = parcel.readString();
                        final AppVersion appVersion = new AppVersion();
                        appVersion.setUuid(parcel.readString());
                        appVersion.setUploadedAt(parcel.readString());
                        appVersion.setMainImageFullUrl(parcel.readString());
                        hashMap2.put(string4, appVersion);
                        ++n;
                    }
                }
                return new AppDescription(string, string2, string3, hashMap);
            }
            
            public AppDescription[] newArray(final int n) {
                return new AppDescription[n];
            }
        };
    }
    
    public AppDescription() {
    }
    
    public AppDescription(final String uuid, final String title, final String imageUrl, final HashMap<String, AppVersion> versions) {
        this.uuid = uuid;
        this.title = title;
        this.imageUrl = imageUrl;
        this.versions = versions;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (!(o instanceof AppDescription)) {
                return false;
            }
            final AppDescription appDescription = (AppDescription)o;
            Label_0051: {
                if (this.title != null) {
                    if (this.title.equals(appDescription.title)) {
                        break Label_0051;
                    }
                }
                else if (appDescription.title == null) {
                    break Label_0051;
                }
                return false;
            }
            Label_0081: {
                if (this.uuid != null) {
                    if (this.uuid.equals(appDescription.uuid)) {
                        break Label_0081;
                    }
                }
                else if (appDescription.uuid == null) {
                    break Label_0081;
                }
                return false;
            }
            if (this.versions != null) {
                if (this.versions.equals(appDescription.versions)) {
                    return true;
                }
            }
            else if (appDescription.versions == null) {
                return true;
            }
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.uuid != null) {
            hashCode2 = this.uuid.hashCode();
        }
        else {
            hashCode2 = 0;
        }
        int hashCode3;
        if (this.title != null) {
            hashCode3 = this.title.hashCode();
        }
        else {
            hashCode3 = 0;
        }
        if (this.versions != null) {
            hashCode = this.versions.hashCode();
        }
        return (hashCode2 * 31 + hashCode3) * 31 + hashCode;
    }
    
    @Override
    public String toString() {
        return "AppDescription{title='" + this.title + '\'' + ", uuid='" + this.uuid + '\'' + ", versions=" + this.versions + '}';
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.uuid);
        parcel.writeString(this.title);
        parcel.writeString(this.imageUrl);
        if (this.versions == null) {
            parcel.writeInt(-1);
        }
        else {
            parcel.writeInt(this.versions.size());
            for (final String s : this.versions.keySet()) {
                parcel.writeString(s);
                parcel.writeString(this.versions.get(s).getUuid());
                parcel.writeString(this.versions.get(s).getUploadedAt());
                parcel.writeString(this.versions.get(s).getMainImageFullUrl());
            }
        }
    }
    
    public static class AppVersion
    {
        public static final AppVersion EMPTY_VERSION;
        private String mainImageFullUrl;
        private String uploadedAt;
        private String uuid;
        
        static {
            EMPTY_VERSION = new AppVersion();
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this != o) {
                if (o == null || this.getClass() != o.getClass()) {
                    return false;
                }
                final AppVersion appVersion = (AppVersion)o;
                Label_0059: {
                    if (this.uploadedAt != null) {
                        if (this.uploadedAt.equals(appVersion.uploadedAt)) {
                            break Label_0059;
                        }
                    }
                    else if (appVersion.uploadedAt == null) {
                        break Label_0059;
                    }
                    return false;
                }
                Label_0089: {
                    if (this.uuid != null) {
                        if (this.uuid.equals(appVersion.uuid)) {
                            break Label_0089;
                        }
                    }
                    else if (appVersion.uuid == null) {
                        break Label_0089;
                    }
                    return false;
                }
                if (this.mainImageFullUrl != null) {
                    if (this.mainImageFullUrl.equals(appVersion.mainImageFullUrl)) {
                        return true;
                    }
                }
                else if (appVersion.mainImageFullUrl == null) {
                    return true;
                }
                return false;
            }
            return true;
        }
        
        public String getMainImageFullUrl() {
            return this.mainImageFullUrl;
        }
        
        public String getUploadedAt() {
            return this.uploadedAt;
        }
        
        public String getUuid() {
            return this.uuid;
        }
        
        @Override
        public int hashCode() {
            int hashCode = 0;
            int hashCode2;
            if (this.uuid != null) {
                hashCode2 = this.uuid.hashCode();
            }
            else {
                hashCode2 = 0;
            }
            int hashCode3;
            if (this.uploadedAt != null) {
                hashCode3 = this.uploadedAt.hashCode();
            }
            else {
                hashCode3 = 0;
            }
            if (this.mainImageFullUrl != null) {
                hashCode = this.mainImageFullUrl.hashCode();
            }
            return (hashCode2 * 31 + hashCode3) * 31 + hashCode;
        }
        
        public void setMainImageFullUrl(final String mainImageFullUrl) {
            this.mainImageFullUrl = mainImageFullUrl;
        }
        
        public void setUploadedAt(final String uploadedAt) {
            this.uploadedAt = uploadedAt;
        }
        
        public void setUuid(final String uuid) {
            this.uuid = uuid;
        }
        
        @Override
        public String toString() {
            return "AppVersion{uuid='" + this.uuid + '\'' + ", uploadedAt=" + this.uploadedAt + ", mainImageFullUrl='" + this.mainImageFullUrl + '\'' + '}';
        }
    }
}
