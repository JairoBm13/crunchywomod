// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.internal;

import java.net.URISyntaxException;
import android.net.Uri$Builder;
import java.net.URI;
import android.content.Context;

public class ImageRequest
{
    private static final String HEIGHT_PARAM = "height";
    private static final String MIGRATION_PARAM = "migration_overrides";
    private static final String MIGRATION_VALUE = "{october_2012:true}";
    private static final String PROFILEPIC_URL_FORMAT = "https://graph.facebook.com/%s/picture";
    public static final int UNSPECIFIED_DIMENSION = 0;
    private static final String WIDTH_PARAM = "width";
    private boolean allowCachedRedirects;
    private Callback callback;
    private Object callerTag;
    private Context context;
    private URI imageUri;
    
    private ImageRequest(final Builder builder) {
        this.context = builder.context;
        this.imageUri = builder.imageUrl;
        this.callback = builder.callback;
        this.allowCachedRedirects = builder.allowCachedRedirects;
        Object access$400;
        if (builder.callerTag == null) {
            access$400 = new Object();
        }
        else {
            access$400 = builder.callerTag;
        }
        this.callerTag = access$400;
    }
    
    public static URI getProfilePictureUrl(final String s, int max, int max2) throws URISyntaxException {
        Validate.notNullOrEmpty(s, "userId");
        max = Math.max(max, 0);
        max2 = Math.max(max2, 0);
        if (max == 0 && max2 == 0) {
            throw new IllegalArgumentException("Either width or height must be greater than 0");
        }
        final Uri$Builder encodedPath = new Uri$Builder().encodedPath(String.format("https://graph.facebook.com/%s/picture", s));
        if (max2 != 0) {
            encodedPath.appendQueryParameter("height", String.valueOf(max2));
        }
        if (max != 0) {
            encodedPath.appendQueryParameter("width", String.valueOf(max));
        }
        encodedPath.appendQueryParameter("migration_overrides", "{october_2012:true}");
        return new URI(encodedPath.toString());
    }
    
    public Callback getCallback() {
        return this.callback;
    }
    
    public Object getCallerTag() {
        return this.callerTag;
    }
    
    public Context getContext() {
        return this.context;
    }
    
    public URI getImageUri() {
        return this.imageUri;
    }
    
    public boolean isCachedRedirectAllowed() {
        return this.allowCachedRedirects;
    }
    
    public static class Builder
    {
        private boolean allowCachedRedirects;
        private Callback callback;
        private Object callerTag;
        private Context context;
        private URI imageUrl;
        
        public Builder(final Context context, final URI imageUrl) {
            Validate.notNull(imageUrl, "imageUrl");
            this.context = context;
            this.imageUrl = imageUrl;
        }
        
        public ImageRequest build() {
            return new ImageRequest(this, null);
        }
        
        public Builder setAllowCachedRedirects(final boolean allowCachedRedirects) {
            this.allowCachedRedirects = allowCachedRedirects;
            return this;
        }
        
        public Builder setCallback(final Callback callback) {
            this.callback = callback;
            return this;
        }
        
        public Builder setCallerTag(final Object callerTag) {
            this.callerTag = callerTag;
            return this;
        }
    }
    
    public interface Callback
    {
        void onCompleted(final ImageResponse p0);
    }
}
