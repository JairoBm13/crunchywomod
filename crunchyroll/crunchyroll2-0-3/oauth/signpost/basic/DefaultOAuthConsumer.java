// 
// Decompiled by Procyon v0.5.30
// 

package oauth.signpost.basic;

import java.net.HttpURLConnection;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.AbstractOAuthConsumer;

public class DefaultOAuthConsumer extends AbstractOAuthConsumer
{
    public DefaultOAuthConsumer(final String s, final String s2) {
        super(s, s2);
    }
    
    @Override
    protected HttpRequest wrap(final Object o) {
        if (!(o instanceof HttpURLConnection)) {
            throw new IllegalArgumentException("The default consumer expects requests of type java.net.HttpURLConnection");
        }
        return new HttpURLConnectionRequestAdapter((HttpURLConnection)o);
    }
}
