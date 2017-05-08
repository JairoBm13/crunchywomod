// 
// Decompiled by Procyon v0.5.30
// 

package oauth.signpost.basic;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import oauth.signpost.http.HttpRequest;

public class HttpURLConnectionRequestAdapter implements HttpRequest
{
    protected HttpURLConnection connection;
    
    public HttpURLConnectionRequestAdapter(final HttpURLConnection connection) {
        this.connection = connection;
    }
    
    @Override
    public String getContentType() {
        return this.connection.getRequestProperty("Content-Type");
    }
    
    @Override
    public String getHeader(final String s) {
        return this.connection.getRequestProperty(s);
    }
    
    @Override
    public InputStream getMessagePayload() throws IOException {
        return null;
    }
    
    @Override
    public String getMethod() {
        return this.connection.getRequestMethod();
    }
    
    @Override
    public String getRequestUrl() {
        return this.connection.getURL().toExternalForm();
    }
    
    @Override
    public void setHeader(final String s, final String s2) {
        this.connection.setRequestProperty(s, s2);
    }
}
