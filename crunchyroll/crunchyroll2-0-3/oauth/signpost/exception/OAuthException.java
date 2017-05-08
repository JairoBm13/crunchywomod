// 
// Decompiled by Procyon v0.5.30
// 

package oauth.signpost.exception;

public abstract class OAuthException extends Exception
{
    public OAuthException(final String s) {
        super(s);
    }
    
    public OAuthException(final String s, final Throwable t) {
        super(s, t);
    }
    
    public OAuthException(final Throwable t) {
        super(t);
    }
}
