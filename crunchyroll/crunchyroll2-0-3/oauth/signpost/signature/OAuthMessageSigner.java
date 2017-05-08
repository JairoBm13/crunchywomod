// 
// Decompiled by Procyon v0.5.30
// 

package oauth.signpost.signature;

import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;
import org.apache.commons.codec.binary.Base64;
import java.io.Serializable;

public abstract class OAuthMessageSigner implements Serializable
{
    private transient Base64 base64;
    private String consumerSecret;
    private String tokenSecret;
    
    public OAuthMessageSigner() {
        this.base64 = new Base64();
    }
    
    protected String base64Encode(final byte[] array) {
        return new String(this.base64.encode(array));
    }
    
    public String getConsumerSecret() {
        return this.consumerSecret;
    }
    
    public abstract String getSignatureMethod();
    
    public String getTokenSecret() {
        return this.tokenSecret;
    }
    
    public void setConsumerSecret(final String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }
    
    public abstract String sign(final HttpRequest p0, final HttpParameters p1) throws OAuthMessageSignerException;
}
