// 
// Decompiled by Procyon v0.5.30
// 

package oauth.signpost.signature;

import java.net.URISyntaxException;
import java.net.URI;
import java.io.IOException;
import java.util.Iterator;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.OAuth;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;

public class SignatureBaseString
{
    private HttpRequest request;
    private HttpParameters requestParameters;
    
    public SignatureBaseString(final HttpRequest request, final HttpParameters requestParameters) {
        this.request = request;
        this.requestParameters = requestParameters;
    }
    
    public String generate() throws OAuthMessageSignerException {
        try {
            return this.request.getMethod() + '&' + OAuth.percentEncode(this.normalizeRequestUrl()) + '&' + OAuth.percentEncode(this.normalizeRequestParameters());
        }
        catch (Exception ex) {
            throw new OAuthMessageSignerException(ex);
        }
    }
    
    public String normalizeRequestParameters() throws IOException {
        if (this.requestParameters == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        final Iterator<String> iterator = this.requestParameters.keySet().iterator();
        int n = 0;
        while (iterator.hasNext()) {
            final String s = iterator.next();
            if (!"oauth_signature".equals(s) && !"realm".equals(s)) {
                if (n > 0) {
                    sb.append("&");
                }
                sb.append(this.requestParameters.getAsQueryString(s, false));
            }
            ++n;
        }
        return sb.toString();
    }
    
    public String normalizeRequestUrl() throws URISyntaxException {
        final URI uri = new URI(this.request.getRequestUrl());
        final String lowerCase = uri.getScheme().toLowerCase();
        final String lowerCase2 = uri.getAuthority().toLowerCase();
        int n;
        if ((lowerCase.equals("http") && uri.getPort() == 80) || (lowerCase.equals("https") && uri.getPort() == 443)) {
            n = 1;
        }
        else {
            n = 0;
        }
        String substring = lowerCase2;
        if (n != 0) {
            final int lastIndex = lowerCase2.lastIndexOf(":");
            substring = lowerCase2;
            if (lastIndex >= 0) {
                substring = lowerCase2.substring(0, lastIndex);
            }
        }
        final String rawPath = uri.getRawPath();
        if (rawPath != null) {
            final String s = rawPath;
            if (rawPath.length() > 0) {
                return lowerCase + "://" + substring + s;
            }
        }
        final String s = "/";
        return lowerCase + "://" + substring + s;
    }
}
