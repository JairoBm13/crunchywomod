// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.upstream;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import android.text.TextUtils;
import com.google.android.exoplayer.util.Util;
import com.google.android.exoplayer.util.Predicate;

public interface HttpDataSource extends UriDataSource
{
    public static final Predicate<String> REJECT_PAYWALL_TYPES = new Predicate<String>() {
        @Override
        public boolean evaluate(String lowerInvariant) {
            lowerInvariant = Util.toLowerInvariant(lowerInvariant);
            return !TextUtils.isEmpty((CharSequence)lowerInvariant) && (!lowerInvariant.contains("text") || lowerInvariant.contains("text/vtt")) && !lowerInvariant.contains("html") && !lowerInvariant.contains("xml");
        }
    };
    
    public static class HttpDataSourceException extends IOException
    {
        public final DataSpec dataSpec;
        
        public HttpDataSourceException(final IOException ex, final DataSpec dataSpec) {
            super(ex);
            this.dataSpec = dataSpec;
        }
        
        public HttpDataSourceException(final String s, final DataSpec dataSpec) {
            super(s);
            this.dataSpec = dataSpec;
        }
        
        public HttpDataSourceException(final String s, final IOException ex, final DataSpec dataSpec) {
            super(s, ex);
            this.dataSpec = dataSpec;
        }
    }
    
    public static final class InvalidContentTypeException extends HttpDataSourceException
    {
        public final String contentType;
        
        public InvalidContentTypeException(final String contentType, final DataSpec dataSpec) {
            super("Invalid content type: " + contentType, dataSpec);
            this.contentType = contentType;
        }
    }
    
    public static final class InvalidResponseCodeException extends HttpDataSourceException
    {
        public final Map<String, List<String>> headerFields;
        public final int responseCode;
        
        public InvalidResponseCodeException(final int responseCode, final Map<String, List<String>> headerFields, final DataSpec dataSpec) {
            super("Response code: " + responseCode, dataSpec);
            this.responseCode = responseCode;
            this.headerFields = headerFields;
        }
    }
}
