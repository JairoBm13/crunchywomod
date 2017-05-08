// 
// Decompiled by Procyon v0.5.30
// 

package oauth.signpost.signature;

import java.util.Iterator;
import oauth.signpost.OAuth;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;

public class AuthorizationHeaderSigningStrategy implements SigningStrategy
{
    @Override
    public String writeSignature(String string, final HttpRequest httpRequest, HttpParameters oAuthParameters) {
        final StringBuilder sb = new StringBuilder();
        sb.append("OAuth ");
        if (oAuthParameters.containsKey("realm")) {
            sb.append(oAuthParameters.getAsHeaderElement("realm"));
            sb.append(", ");
        }
        oAuthParameters = oAuthParameters.getOAuthParameters();
        oAuthParameters.put("oauth_signature", string, true);
        final Iterator<String> iterator = oAuthParameters.keySet().iterator();
        while (iterator.hasNext()) {
            sb.append(oAuthParameters.getAsHeaderElement(iterator.next()));
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        string = sb.toString();
        OAuth.debugOut("Auth Header", string);
        httpRequest.setHeader("Authorization", string);
        return string;
    }
}
