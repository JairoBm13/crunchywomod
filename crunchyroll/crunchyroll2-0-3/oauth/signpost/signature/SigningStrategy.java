// 
// Decompiled by Procyon v0.5.30
// 

package oauth.signpost.signature;

import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;
import java.io.Serializable;

public interface SigningStrategy extends Serializable
{
    String writeSignature(final String p0, final HttpRequest p1, final HttpParameters p2);
}
