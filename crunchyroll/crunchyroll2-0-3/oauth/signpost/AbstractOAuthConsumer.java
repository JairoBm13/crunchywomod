// 
// Decompiled by Procyon v0.5.30
// 

package oauth.signpost;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import java.io.IOException;
import java.util.SortedSet;
import java.util.Map;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.signature.AuthorizationHeaderSigningStrategy;
import oauth.signpost.signature.HmacSha1MessageSigner;
import oauth.signpost.signature.SigningStrategy;
import java.util.Random;
import oauth.signpost.signature.OAuthMessageSigner;
import oauth.signpost.http.HttpParameters;

public abstract class AbstractOAuthConsumer implements OAuthConsumer
{
    private HttpParameters additionalParameters;
    private String consumerKey;
    private String consumerSecret;
    private OAuthMessageSigner messageSigner;
    private final Random random;
    private HttpParameters requestParameters;
    private boolean sendEmptyTokens;
    private SigningStrategy signingStrategy;
    private String token;
    
    public AbstractOAuthConsumer(final String consumerKey, final String consumerSecret) {
        this.random = new Random(System.nanoTime());
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.setMessageSigner(new HmacSha1MessageSigner());
        this.setSigningStrategy(new AuthorizationHeaderSigningStrategy());
    }
    
    protected void collectBodyParameters(final HttpRequest httpRequest, final HttpParameters httpParameters) throws IOException {
        final String contentType = httpRequest.getContentType();
        if (contentType != null && contentType.startsWith("application/x-www-form-urlencoded")) {
            httpParameters.putAll(OAuth.decodeForm(httpRequest.getMessagePayload()), true);
        }
    }
    
    protected void collectHeaderParameters(final HttpRequest httpRequest, final HttpParameters httpParameters) {
        httpParameters.putAll(OAuth.oauthHeaderToParamsMap(httpRequest.getHeader("Authorization")), false);
    }
    
    protected void collectQueryParameters(final HttpRequest httpRequest, final HttpParameters httpParameters) {
        final String requestUrl = httpRequest.getRequestUrl();
        final int index = requestUrl.indexOf(63);
        if (index >= 0) {
            httpParameters.putAll(OAuth.decodeForm(requestUrl.substring(index + 1)), true);
        }
    }
    
    protected void completeOAuthParameters(final HttpParameters httpParameters) {
        if (!httpParameters.containsKey("oauth_consumer_key")) {
            httpParameters.put("oauth_consumer_key", this.consumerKey, true);
        }
        if (!httpParameters.containsKey("oauth_signature_method")) {
            httpParameters.put("oauth_signature_method", this.messageSigner.getSignatureMethod(), true);
        }
        if (!httpParameters.containsKey("oauth_timestamp")) {
            httpParameters.put("oauth_timestamp", this.generateTimestamp(), true);
        }
        if (!httpParameters.containsKey("oauth_nonce")) {
            httpParameters.put("oauth_nonce", this.generateNonce(), true);
        }
        if (!httpParameters.containsKey("oauth_version")) {
            httpParameters.put("oauth_version", "1.0", true);
        }
        if (!httpParameters.containsKey("oauth_token") && ((this.token != null && !this.token.equals("")) || this.sendEmptyTokens)) {
            httpParameters.put("oauth_token", this.token, true);
        }
    }
    
    protected String generateNonce() {
        return Long.toString(this.random.nextLong());
    }
    
    protected String generateTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000L);
    }
    
    public void setMessageSigner(final OAuthMessageSigner messageSigner) {
        (this.messageSigner = messageSigner).setConsumerSecret(this.consumerSecret);
    }
    
    public void setSigningStrategy(final SigningStrategy signingStrategy) {
        this.signingStrategy = signingStrategy;
    }
    
    @Override
    public HttpRequest sign(final Object o) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
        synchronized (this) {
            return this.sign(this.wrap(o));
        }
    }
    
    public HttpRequest sign(final HttpRequest httpRequest) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
        synchronized (this) {
            if (this.consumerKey == null) {
                throw new OAuthExpectationFailedException("consumer key not set");
            }
        }
        if (this.consumerSecret == null) {
            throw new OAuthExpectationFailedException("consumer secret not set");
        }
        this.requestParameters = new HttpParameters();
        try {
            if (this.additionalParameters != null) {
                this.requestParameters.putAll(this.additionalParameters, false);
            }
            final HttpRequest httpRequest2;
            this.collectHeaderParameters(httpRequest2, this.requestParameters);
            this.collectQueryParameters(httpRequest2, this.requestParameters);
            this.collectBodyParameters(httpRequest2, this.requestParameters);
            this.completeOAuthParameters(this.requestParameters);
            this.requestParameters.remove((Object)"oauth_signature");
            final String sign = this.messageSigner.sign(httpRequest2, this.requestParameters);
            OAuth.debugOut("signature", sign);
            this.signingStrategy.writeSignature(sign, httpRequest2, this.requestParameters);
            OAuth.debugOut("Request URL", httpRequest2.getRequestUrl());
            // monitorexit(this)
            return httpRequest2;
        }
        catch (IOException ex) {
            throw new OAuthCommunicationException(ex);
        }
    }
    
    protected abstract HttpRequest wrap(final Object p0);
}
