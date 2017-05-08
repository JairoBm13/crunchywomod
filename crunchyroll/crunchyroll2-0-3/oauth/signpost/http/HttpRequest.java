// 
// Decompiled by Procyon v0.5.30
// 

package oauth.signpost.http;

import java.io.IOException;
import java.io.InputStream;

public interface HttpRequest
{
    String getContentType();
    
    String getHeader(final String p0);
    
    InputStream getMessagePayload() throws IOException;
    
    String getMethod();
    
    String getRequestUrl();
    
    void setHeader(final String p0, final String p1);
}
