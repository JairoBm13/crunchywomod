// 
// Decompiled by Procyon v0.5.30
// 

package oauth.signpost;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpRequest;
import java.io.Serializable;

public interface OAuthConsumer extends Serializable
{
    HttpRequest sign(final Object p0) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException;
}
