// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import android.annotation.SuppressLint;
import com.facebook.internal.Validate;
import android.content.Intent;
import com.facebook.internal.Utility;
import android.os.Bundle;
import android.text.TextUtils;
import java.util.Collections;
import java.util.List;
import java.util.Date;
import java.io.Serializable;

public final class AccessToken implements Serializable
{
    static final String ACCESS_TOKEN_KEY = "access_token";
    private static final Date ALREADY_EXPIRED_EXPIRATION_TIME;
    private static final AccessTokenSource DEFAULT_ACCESS_TOKEN_SOURCE;
    private static final Date DEFAULT_EXPIRATION_TIME;
    private static final Date DEFAULT_LAST_REFRESH_TIME;
    static final String EXPIRES_IN_KEY = "expires_in";
    private static final Date MAX_DATE;
    private static final Date MIN_DATE;
    private static final long serialVersionUID = 1L;
    private final List<String> declinedPermissions;
    private final Date expires;
    private final Date lastRefresh;
    private final List<String> permissions;
    private final AccessTokenSource source;
    private final String token;
    
    static {
        MIN_DATE = new Date(Long.MIN_VALUE);
        MAX_DATE = new Date(Long.MAX_VALUE);
        DEFAULT_EXPIRATION_TIME = AccessToken.MAX_DATE;
        DEFAULT_LAST_REFRESH_TIME = new Date();
        DEFAULT_ACCESS_TOKEN_SOURCE = AccessTokenSource.FACEBOOK_APPLICATION_WEB;
        ALREADY_EXPIRED_EXPIRATION_TIME = AccessToken.MIN_DATE;
    }
    
    AccessToken(final String token, final Date expires, List<String> emptyList, final List<String> list, final AccessTokenSource source, final Date lastRefresh) {
        List<? extends String> emptyList2 = emptyList;
        if (emptyList == null) {
            emptyList2 = Collections.emptyList();
        }
        if ((emptyList = list) == null) {
            emptyList = Collections.emptyList();
        }
        this.expires = expires;
        this.permissions = (List<String>)Collections.unmodifiableList((List<?>)emptyList2);
        this.declinedPermissions = (List<String>)Collections.unmodifiableList((List<?>)emptyList);
        this.token = token;
        this.source = source;
        this.lastRefresh = lastRefresh;
    }
    
    private void appendPermissions(final StringBuilder sb) {
        sb.append(" permissions:");
        if (this.permissions == null) {
            sb.append("null");
            return;
        }
        sb.append("[");
        sb.append(TextUtils.join((CharSequence)", ", (Iterable)this.permissions));
        sb.append("]");
    }
    
    static AccessToken createEmptyToken() {
        return new AccessToken("", AccessToken.ALREADY_EXPIRED_EXPIRATION_TIME, null, null, AccessTokenSource.NONE, AccessToken.DEFAULT_LAST_REFRESH_TIME);
    }
    
    private static AccessToken createFromBundle(final List<String> list, final Bundle bundle, final AccessTokenSource accessTokenSource, final Date date) {
        final String string = bundle.getString("access_token");
        final Date bundleLongAsDate = getBundleLongAsDate(bundle, "expires_in", date);
        if (Utility.isNullOrEmpty(string) || bundleLongAsDate == null) {
            return null;
        }
        return new AccessToken(string, bundleLongAsDate, list, null, accessTokenSource, new Date());
    }
    
    static AccessToken createFromCache(final Bundle bundle) {
        return new AccessToken(bundle.getString("com.facebook.TokenCachingStrategy.Token"), TokenCachingStrategy.getDate(bundle, "com.facebook.TokenCachingStrategy.ExpirationDate"), getPermissionsFromBundle(bundle, "com.facebook.TokenCachingStrategy.Permissions"), getPermissionsFromBundle(bundle, "com.facebook.TokenCachingStrategy.DeclinedPermissions"), TokenCachingStrategy.getSource(bundle), TokenCachingStrategy.getDate(bundle, "com.facebook.TokenCachingStrategy.LastRefreshDate"));
    }
    
    public static AccessToken createFromExistingAccessToken(final String s, Date default_LAST_REFRESH_TIME, final Date date, final AccessTokenSource accessTokenSource, final List<String> list) {
        Date default_EXPIRATION_TIME = default_LAST_REFRESH_TIME;
        if (default_LAST_REFRESH_TIME == null) {
            default_EXPIRATION_TIME = AccessToken.DEFAULT_EXPIRATION_TIME;
        }
        if ((default_LAST_REFRESH_TIME = date) == null) {
            default_LAST_REFRESH_TIME = AccessToken.DEFAULT_LAST_REFRESH_TIME;
        }
        AccessTokenSource default_ACCESS_TOKEN_SOURCE;
        if ((default_ACCESS_TOKEN_SOURCE = accessTokenSource) == null) {
            default_ACCESS_TOKEN_SOURCE = AccessToken.DEFAULT_ACCESS_TOKEN_SOURCE;
        }
        return new AccessToken(s, default_EXPIRATION_TIME, list, null, default_ACCESS_TOKEN_SOURCE, default_LAST_REFRESH_TIME);
    }
    
    public static AccessToken createFromNativeLinkingIntent(final Intent intent) {
        Validate.notNull(intent, "intent");
        if (intent.getExtras() == null) {
            return null;
        }
        return createFromBundle(null, intent.getExtras(), AccessTokenSource.FACEBOOK_APPLICATION_WEB, new Date());
    }
    
    static AccessToken createFromNativeLogin(final Bundle bundle, final AccessTokenSource accessTokenSource) {
        return createNew(bundle.getStringArrayList("com.facebook.platform.extra.PERMISSIONS"), null, bundle.getString("com.facebook.platform.extra.ACCESS_TOKEN"), getBundleLongAsDate(bundle, "com.facebook.platform.extra.EXPIRES_SECONDS_SINCE_EPOCH", new Date(0L)), accessTokenSource);
    }
    
    @SuppressLint({ "FieldGetter" })
    static AccessToken createFromRefresh(final AccessToken accessToken, final Bundle bundle) {
        if (accessToken.source != AccessTokenSource.FACEBOOK_APPLICATION_WEB && accessToken.source != AccessTokenSource.FACEBOOK_APPLICATION_NATIVE && accessToken.source != AccessTokenSource.FACEBOOK_APPLICATION_SERVICE) {
            throw new FacebookException("Invalid token source: " + accessToken.source);
        }
        return createNew(accessToken.getPermissions(), accessToken.getDeclinedPermissions(), bundle.getString("access_token"), getBundleLongAsDate(bundle, "expires_in", new Date(0L)), accessToken.source);
    }
    
    static AccessToken createFromString(final String s, final List<String> list, final AccessTokenSource accessTokenSource) {
        return new AccessToken(s, AccessToken.DEFAULT_EXPIRATION_TIME, list, null, accessTokenSource, AccessToken.DEFAULT_LAST_REFRESH_TIME);
    }
    
    static AccessToken createFromTokenWithRefreshedPermissions(final AccessToken accessToken, final List<String> list, final List<String> list2) {
        return new AccessToken(accessToken.token, accessToken.expires, list, list2, accessToken.source, accessToken.lastRefresh);
    }
    
    static AccessToken createFromWebBundle(List<String> list, final Bundle bundle, final AccessTokenSource accessTokenSource) {
        final Date bundleLongAsDate = getBundleLongAsDate(bundle, "expires_in", new Date());
        final String string = bundle.getString("access_token");
        final String string2 = bundle.getString("granted_scopes");
        if (!Utility.isNullOrEmpty(string2)) {
            list = new ArrayList<String>(Arrays.asList(string2.split(",")));
        }
        final String string3 = bundle.getString("denied_scopes");
        ArrayList list2 = null;
        if (!Utility.isNullOrEmpty(string3)) {
            list2 = new ArrayList<String>(Arrays.asList(string3.split(",")));
        }
        return createNew((List<String>)list, (List<String>)list2, string, bundleLongAsDate, accessTokenSource);
    }
    
    private static AccessToken createNew(final List<String> list, final List<String> list2, final String s, final Date date, final AccessTokenSource accessTokenSource) {
        if (Utility.isNullOrEmpty(s) || date == null) {
            return createEmptyToken();
        }
        return new AccessToken(s, date, list, list2, accessTokenSource, new Date());
    }
    
    private static Date getBundleLongAsDate(final Bundle bundle, final String s, final Date date) {
        if (bundle != null) {
            final Object value = bundle.get(s);
            long n = 0L;
            Label_0027: {
                if (value instanceof Long) {
                    n = (long)value;
                }
                else {
                    if (value instanceof String) {
                        try {
                            n = Long.parseLong((String)value);
                            break Label_0027;
                        }
                        catch (NumberFormatException ex) {
                            return null;
                        }
                        return new Date(date.getTime() + 1000L * n);
                    }
                    return null;
                }
            }
            if (n == 0L) {
                return new Date(Long.MAX_VALUE);
            }
            return new Date(date.getTime() + 1000L * n);
        }
        return null;
    }
    
    static List<String> getPermissionsFromBundle(final Bundle bundle, final String s) {
        final ArrayList stringArrayList = bundle.getStringArrayList(s);
        if (stringArrayList == null) {
            return Collections.emptyList();
        }
        return (List<String>)Collections.unmodifiableList((List<?>)new ArrayList<Object>(stringArrayList));
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Cannot readObject, serialization proxy required");
    }
    
    private String tokenToString() {
        if (this.token == null) {
            return "null";
        }
        if (Settings.isLoggingBehaviorEnabled(LoggingBehavior.INCLUDE_ACCESS_TOKENS)) {
            return this.token;
        }
        return "ACCESS_TOKEN_REMOVED";
    }
    
    private Object writeReplace() {
        return new SerializationProxyV2(this.token, this.expires, (List)this.permissions, (List)this.declinedPermissions, this.source, this.lastRefresh);
    }
    
    public List<String> getDeclinedPermissions() {
        return this.declinedPermissions;
    }
    
    public Date getExpires() {
        return this.expires;
    }
    
    public Date getLastRefresh() {
        return this.lastRefresh;
    }
    
    public List<String> getPermissions() {
        return this.permissions;
    }
    
    public AccessTokenSource getSource() {
        return this.source;
    }
    
    public String getToken() {
        return this.token;
    }
    
    boolean isInvalid() {
        return Utility.isNullOrEmpty(this.token) || new Date().after(this.expires);
    }
    
    Bundle toCacheBundle() {
        final Bundle bundle = new Bundle();
        bundle.putString("com.facebook.TokenCachingStrategy.Token", this.token);
        TokenCachingStrategy.putDate(bundle, "com.facebook.TokenCachingStrategy.ExpirationDate", this.expires);
        bundle.putStringArrayList("com.facebook.TokenCachingStrategy.Permissions", new ArrayList((Collection<? extends E>)this.permissions));
        bundle.putStringArrayList("com.facebook.TokenCachingStrategy.DeclinedPermissions", new ArrayList((Collection<? extends E>)this.declinedPermissions));
        bundle.putSerializable("com.facebook.TokenCachingStrategy.AccessTokenSource", (Serializable)this.source);
        TokenCachingStrategy.putDate(bundle, "com.facebook.TokenCachingStrategy.LastRefreshDate", this.lastRefresh);
        return bundle;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{AccessToken");
        sb.append(" token:").append(this.tokenToString());
        this.appendPermissions(sb);
        sb.append("}");
        return sb.toString();
    }
    
    private static class SerializationProxyV1 implements Serializable
    {
        private static final long serialVersionUID = -2488473066578201069L;
        private final Date expires;
        private final Date lastRefresh;
        private final List<String> permissions;
        private final AccessTokenSource source;
        private final String token;
        
        private SerializationProxyV1(final String token, final Date expires, final List<String> permissions, final AccessTokenSource source, final Date lastRefresh) {
            this.expires = expires;
            this.permissions = permissions;
            this.token = token;
            this.source = source;
            this.lastRefresh = lastRefresh;
        }
        
        private Object readResolve() {
            return new AccessToken(this.token, this.expires, this.permissions, null, this.source, this.lastRefresh);
        }
    }
    
    private static class SerializationProxyV2 implements Serializable
    {
        private static final long serialVersionUID = -2488473066578201068L;
        private final List<String> declinedPermissions;
        private final Date expires;
        private final Date lastRefresh;
        private final List<String> permissions;
        private final AccessTokenSource source;
        private final String token;
        
        private SerializationProxyV2(final String token, final Date expires, final List<String> permissions, final List<String> declinedPermissions, final AccessTokenSource source, final Date lastRefresh) {
            this.expires = expires;
            this.permissions = permissions;
            this.declinedPermissions = declinedPermissions;
            this.token = token;
            this.source = source;
            this.lastRefresh = lastRefresh;
        }
        
        private Object readResolve() {
            return new AccessToken(this.token, this.expires, this.permissions, this.declinedPermissions, this.source, this.lastRefresh);
        }
    }
}
