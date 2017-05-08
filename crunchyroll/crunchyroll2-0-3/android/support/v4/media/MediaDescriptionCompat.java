// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.media;

import android.text.TextUtils;
import android.os.Build$VERSION;
import android.os.Parcel;
import android.net.Uri;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public final class MediaDescriptionCompat implements Parcelable
{
    public static final Parcelable$Creator<MediaDescriptionCompat> CREATOR;
    private final CharSequence mDescription;
    private Object mDescriptionObj;
    private final Bundle mExtras;
    private final Bitmap mIcon;
    private final Uri mIconUri;
    private final String mMediaId;
    private final CharSequence mSubtitle;
    private final CharSequence mTitle;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<MediaDescriptionCompat>() {
            public MediaDescriptionCompat createFromParcel(final Parcel parcel) {
                if (Build$VERSION.SDK_INT < 21) {
                    return new MediaDescriptionCompat(parcel, null);
                }
                return MediaDescriptionCompat.fromMediaDescription(MediaDescriptionCompatApi21.fromParcel(parcel));
            }
            
            public MediaDescriptionCompat[] newArray(final int n) {
                return new MediaDescriptionCompat[n];
            }
        };
    }
    
    private MediaDescriptionCompat(final Parcel parcel) {
        this.mMediaId = parcel.readString();
        this.mTitle = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.mSubtitle = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.mDescription = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.mIcon = (Bitmap)parcel.readParcelable((ClassLoader)null);
        this.mIconUri = (Uri)parcel.readParcelable((ClassLoader)null);
        this.mExtras = parcel.readBundle();
    }
    
    private MediaDescriptionCompat(final String mMediaId, final CharSequence mTitle, final CharSequence mSubtitle, final CharSequence mDescription, final Bitmap mIcon, final Uri mIconUri, final Bundle mExtras) {
        this.mMediaId = mMediaId;
        this.mTitle = mTitle;
        this.mSubtitle = mSubtitle;
        this.mDescription = mDescription;
        this.mIcon = mIcon;
        this.mIconUri = mIconUri;
        this.mExtras = mExtras;
    }
    
    public static MediaDescriptionCompat fromMediaDescription(final Object mDescriptionObj) {
        if (mDescriptionObj == null || Build$VERSION.SDK_INT < 21) {
            return null;
        }
        final Builder builder = new Builder();
        builder.setMediaId(MediaDescriptionCompatApi21.getMediaId(mDescriptionObj));
        builder.setTitle(MediaDescriptionCompatApi21.getTitle(mDescriptionObj));
        builder.setSubtitle(MediaDescriptionCompatApi21.getSubtitle(mDescriptionObj));
        builder.setDescription(MediaDescriptionCompatApi21.getDescription(mDescriptionObj));
        builder.setIconBitmap(MediaDescriptionCompatApi21.getIconBitmap(mDescriptionObj));
        builder.setIconUri(MediaDescriptionCompatApi21.getIconUri(mDescriptionObj));
        builder.setExtras(MediaDescriptionCompatApi21.getExtras(mDescriptionObj));
        final MediaDescriptionCompat build = builder.build();
        build.mDescriptionObj = mDescriptionObj;
        return build;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public CharSequence getDescription() {
        return this.mDescription;
    }
    
    public Bundle getExtras() {
        return this.mExtras;
    }
    
    public Bitmap getIconBitmap() {
        return this.mIcon;
    }
    
    public Uri getIconUri() {
        return this.mIconUri;
    }
    
    public Object getMediaDescription() {
        if (this.mDescriptionObj != null || Build$VERSION.SDK_INT < 21) {
            return this.mDescriptionObj;
        }
        final Object instance = MediaDescriptionCompatApi21.Builder.newInstance();
        MediaDescriptionCompatApi21.Builder.setMediaId(instance, this.mMediaId);
        MediaDescriptionCompatApi21.Builder.setTitle(instance, this.mTitle);
        MediaDescriptionCompatApi21.Builder.setSubtitle(instance, this.mSubtitle);
        MediaDescriptionCompatApi21.Builder.setDescription(instance, this.mDescription);
        MediaDescriptionCompatApi21.Builder.setIconBitmap(instance, this.mIcon);
        MediaDescriptionCompatApi21.Builder.setIconUri(instance, this.mIconUri);
        MediaDescriptionCompatApi21.Builder.setExtras(instance, this.mExtras);
        return this.mDescriptionObj = MediaDescriptionCompatApi21.Builder.build(instance);
    }
    
    public String getMediaId() {
        return this.mMediaId;
    }
    
    public CharSequence getSubtitle() {
        return this.mSubtitle;
    }
    
    public CharSequence getTitle() {
        return this.mTitle;
    }
    
    @Override
    public String toString() {
        return (Object)this.mTitle + ", " + (Object)this.mSubtitle + ", " + (Object)this.mDescription;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        if (Build$VERSION.SDK_INT < 21) {
            parcel.writeString(this.mMediaId);
            TextUtils.writeToParcel(this.mTitle, parcel, n);
            TextUtils.writeToParcel(this.mSubtitle, parcel, n);
            TextUtils.writeToParcel(this.mDescription, parcel, n);
            parcel.writeParcelable((Parcelable)this.mIcon, n);
            parcel.writeParcelable((Parcelable)this.mIconUri, n);
            parcel.writeBundle(this.mExtras);
            return;
        }
        MediaDescriptionCompatApi21.writeToParcel(this.getMediaDescription(), parcel, n);
    }
    
    public static final class Builder
    {
        private CharSequence mDescription;
        private Bundle mExtras;
        private Bitmap mIcon;
        private Uri mIconUri;
        private String mMediaId;
        private CharSequence mSubtitle;
        private CharSequence mTitle;
        
        public MediaDescriptionCompat build() {
            return new MediaDescriptionCompat(this.mMediaId, this.mTitle, this.mSubtitle, this.mDescription, this.mIcon, this.mIconUri, this.mExtras, null);
        }
        
        public Builder setDescription(final CharSequence mDescription) {
            this.mDescription = mDescription;
            return this;
        }
        
        public Builder setExtras(final Bundle mExtras) {
            this.mExtras = mExtras;
            return this;
        }
        
        public Builder setIconBitmap(final Bitmap mIcon) {
            this.mIcon = mIcon;
            return this;
        }
        
        public Builder setIconUri(final Uri mIconUri) {
            this.mIconUri = mIconUri;
            return this;
        }
        
        public Builder setMediaId(final String mMediaId) {
            this.mMediaId = mMediaId;
            return this;
        }
        
        public Builder setSubtitle(final CharSequence mSubtitle) {
            this.mSubtitle = mSubtitle;
            return this;
        }
        
        public Builder setTitle(final CharSequence mTitle) {
            this.mTitle = mTitle;
            return this;
        }
    }
}
