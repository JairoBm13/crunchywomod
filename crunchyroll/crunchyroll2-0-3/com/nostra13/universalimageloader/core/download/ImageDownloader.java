// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.download;

import java.util.Locale;
import java.io.IOException;
import java.io.InputStream;

public interface ImageDownloader
{
    InputStream getStream(final String p0, final Object p1) throws IOException;
    
    public enum Scheme
    {
        ASSETS("assets"), 
        CONTENT("content"), 
        DRAWABLE("drawable"), 
        FILE("file"), 
        HTTP("http"), 
        HTTPS("https"), 
        UNKNOWN("");
        
        private String scheme;
        private String uriPrefix;
        
        private Scheme(final String scheme) {
            this.scheme = scheme;
            this.uriPrefix = scheme + "://";
        }
        
        private boolean belongsTo(final String s) {
            return s.toLowerCase(Locale.US).startsWith(this.uriPrefix);
        }
        
        public static Scheme ofUri(final String s) {
            if (s != null) {
                final Scheme[] values = values();
                for (int length = values.length, i = 0; i < length; ++i) {
                    final Scheme scheme = values[i];
                    if (scheme.belongsTo(s)) {
                        return scheme;
                    }
                }
            }
            return Scheme.UNKNOWN;
        }
        
        public String crop(final String s) {
            if (!this.belongsTo(s)) {
                throw new IllegalArgumentException(String.format("URI [%1$s] doesn't have expected scheme [%2$s]", s, this.scheme));
            }
            return s.substring(this.uriPrefix.length());
        }
        
        public String wrap(final String s) {
            return this.uriPrefix + s;
        }
    }
}
