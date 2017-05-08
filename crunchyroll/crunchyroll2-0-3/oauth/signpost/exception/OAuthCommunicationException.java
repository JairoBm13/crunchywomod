// 
// Decompiled by Procyon v0.5.30
// 

package oauth.signpost.exception;

public class OAuthCommunicationException extends OAuthException
{
    public OAuthCommunicationException(final Exception ex) {
        super("Communication with the service provider failed: " + ex.getLocalizedMessage(), ex);
    }
}
