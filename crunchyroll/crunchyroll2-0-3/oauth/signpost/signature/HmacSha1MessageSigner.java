// 
// Decompiled by Procyon v0.5.30
// 

package oauth.signpost.signature;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import oauth.signpost.exception.OAuthMessageSignerException;
import java.security.Key;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import oauth.signpost.OAuth;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;

public class HmacSha1MessageSigner extends OAuthMessageSigner
{
    @Override
    public String getSignatureMethod() {
        return "HMAC-SHA1";
    }
    
    @Override
    public String sign(final HttpRequest httpRequest, final HttpParameters httpParameters) throws OAuthMessageSignerException {
        try {
            final SecretKeySpec secretKeySpec = new SecretKeySpec((OAuth.percentEncode(this.getConsumerSecret()) + '&' + OAuth.percentEncode(this.getTokenSecret())).getBytes("UTF-8"), "HmacSHA1");
            final Mac instance = Mac.getInstance("HmacSHA1");
            instance.init(secretKeySpec);
            final String generate = new SignatureBaseString(httpRequest, httpParameters).generate();
            OAuth.debugOut("SBS", generate);
            return this.base64Encode(instance.doFinal(generate.getBytes("UTF-8"))).trim();
        }
        catch (GeneralSecurityException ex) {
            throw new OAuthMessageSignerException(ex);
        }
        catch (UnsupportedEncodingException ex2) {
            throw new OAuthMessageSignerException(ex2);
        }
    }
}
