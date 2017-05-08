// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.images;

import android.os.Parcel;
import com.google.android.gms.common.internal.zzt;
import org.json.JSONException;
import org.json.JSONObject;
import android.net.Uri;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class WebImage implements SafeParcelable
{
    public static final Parcelable$Creator<WebImage> CREATOR;
    private final int zzCY;
    private final Uri zzZn;
    private final int zznM;
    private final int zznN;
    
    static {
        CREATOR = (Parcelable$Creator)new zzb();
    }
    
    WebImage(final int zzCY, final Uri zzZn, final int zznM, final int zznN) {
        this.zzCY = zzCY;
        this.zzZn = zzZn;
        this.zznM = zznM;
        this.zznN = zznN;
    }
    
    public WebImage(final Uri uri, final int n, final int n2) throws IllegalArgumentException {
        this(1, uri, n, n2);
        if (uri == null) {
            throw new IllegalArgumentException("url cannot be null");
        }
        if (n < 0 || n2 < 0) {
            throw new IllegalArgumentException("width and height must not be negative");
        }
    }
    
    public WebImage(final JSONObject jsonObject) throws IllegalArgumentException {
        this(zzi(jsonObject), jsonObject.optInt("width", 0), jsonObject.optInt("height", 0));
    }
    
    private static Uri zzi(final JSONObject jsonObject) {
        Uri parse = null;
        if (!jsonObject.has("url")) {
            return parse;
        }
        try {
            parse = Uri.parse(jsonObject.getString("url"));
            return parse;
        }
        catch (JSONException ex) {
            return null;
        }
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null || !(o instanceof WebImage)) {
                return false;
            }
            final WebImage webImage = (WebImage)o;
            if (!zzt.equal(this.zzZn, webImage.zzZn) || this.zznM != webImage.zznM || this.zznN != webImage.zznN) {
                return false;
            }
        }
        return true;
    }
    
    public int getHeight() {
        return this.zznN;
    }
    
    public Uri getUrl() {
        return this.zzZn;
    }
    
    int getVersionCode() {
        return this.zzCY;
    }
    
    public int getWidth() {
        return this.zznM;
    }
    
    @Override
    public int hashCode() {
        return zzt.hashCode(this.zzZn, this.zznM, this.zznN);
    }
    
    public JSONObject toJson() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("url", (Object)this.zzZn.toString());
            jsonObject.put("width", this.zznM);
            jsonObject.put("height", this.zznN);
            return jsonObject;
        }
        catch (JSONException ex) {
            return jsonObject;
        }
    }
    
    @Override
    public String toString() {
        return String.format("Image %dx%d %s", this.zznM, this.zznN, this.zzZn.toString());
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzb.zza(this, parcel, n);
    }
}
