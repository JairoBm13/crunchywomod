// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook;

import java.lang.ref.WeakReference;
import android.os.IBinder;
import android.content.ComponentName;
import com.facebook.internal.NativeProtocol;
import android.os.RemoteException;
import android.os.Message;
import android.os.Messenger;
import android.content.ServiceConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.Collections;
import android.content.ActivityNotFoundException;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.util.Log;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.app.Fragment;
import android.app.Activity;
import java.util.Collection;
import com.facebook.internal.SessionAuthorizationType;
import android.text.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Iterator;
import com.facebook.model.GraphObjectList;
import java.util.Map;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphMultiResult;
import android.content.Intent;
import android.os.Looper;
import java.util.ArrayList;
import com.facebook.internal.Validate;
import com.facebook.internal.Utility;
import java.util.HashSet;
import java.util.Date;
import android.os.Handler;
import java.util.List;
import android.os.Bundle;
import android.content.Context;
import java.util.Set;
import java.io.Serializable;

public class Session implements Serializable
{
    public static final String ACTION_ACTIVE_SESSION_CLOSED = "com.facebook.sdk.ACTIVE_SESSION_CLOSED";
    public static final String ACTION_ACTIVE_SESSION_OPENED = "com.facebook.sdk.ACTIVE_SESSION_OPENED";
    public static final String ACTION_ACTIVE_SESSION_SET = "com.facebook.sdk.ACTIVE_SESSION_SET";
    public static final String ACTION_ACTIVE_SESSION_UNSET = "com.facebook.sdk.ACTIVE_SESSION_UNSET";
    private static final String AUTH_BUNDLE_SAVE_KEY = "com.facebook.sdk.Session.authBundleKey";
    public static final int DEFAULT_AUTHORIZE_ACTIVITY_CODE = 64206;
    private static final String MANAGE_PERMISSION_PREFIX = "manage";
    private static final Set<String> OTHER_PUBLISH_PERMISSIONS;
    private static final String PUBLISH_PERMISSION_PREFIX = "publish";
    private static final String SESSION_BUNDLE_SAVE_KEY = "com.facebook.sdk.Session.saveSessionKey";
    private static final Object STATIC_LOCK;
    public static final String TAG;
    private static final int TOKEN_EXTEND_RETRY_SECONDS = 3600;
    private static final int TOKEN_EXTEND_THRESHOLD_SECONDS = 86400;
    public static final String WEB_VIEW_ERROR_CODE_KEY = "com.facebook.sdk.WebViewErrorCode";
    public static final String WEB_VIEW_FAILING_URL_KEY = "com.facebook.sdk.FailingUrl";
    private static Session activeSession;
    private static final long serialVersionUID = 1L;
    private static volatile Context staticContext;
    private AppEventsLogger appEventsLogger;
    private String applicationId;
    private volatile Bundle authorizationBundle;
    private AuthorizationClient authorizationClient;
    private final List<StatusCallback> callbacks;
    private volatile TokenRefreshRequest currentTokenRefreshRequest;
    private Handler handler;
    private Date lastAttemptedTokenExtendDate;
    private final Object lock;
    private AuthorizationRequest pendingAuthorizationRequest;
    private SessionState state;
    private TokenCachingStrategy tokenCachingStrategy;
    private AccessToken tokenInfo;
    
    static {
        TAG = Session.class.getCanonicalName();
        STATIC_LOCK = new Object();
        OTHER_PUBLISH_PERMISSIONS = new HashSet<String>() {
            {
                this.add("ads_management");
                this.add("create_event");
                this.add("rsvp_event");
            }
        };
    }
    
    public Session(final Context context) {
        this(context, null, null, true);
    }
    
    Session(final Context context, final String s, final TokenCachingStrategy tokenCachingStrategy) {
        this(context, s, tokenCachingStrategy, true);
    }
    
    Session(final Context context, final String s, final TokenCachingStrategy tokenCachingStrategy, final boolean b) {
        final Bundle bundle = null;
        this.lastAttemptedTokenExtendDate = new Date(0L);
        this.lock = new Object();
        String metadataApplicationId = s;
        if (context != null && (metadataApplicationId = s) == null) {
            metadataApplicationId = Utility.getMetadataApplicationId(context);
        }
        Validate.notNull(metadataApplicationId, "applicationId");
        initializeStaticContext(context);
        TokenCachingStrategy tokenCachingStrategy2;
        if ((tokenCachingStrategy2 = tokenCachingStrategy) == null) {
            tokenCachingStrategy2 = new SharedPreferencesTokenCachingStrategy(Session.staticContext);
        }
        this.applicationId = metadataApplicationId;
        this.tokenCachingStrategy = tokenCachingStrategy2;
        this.state = SessionState.CREATED;
        this.pendingAuthorizationRequest = null;
        this.callbacks = new ArrayList<StatusCallback>();
        this.handler = new Handler(Looper.getMainLooper());
        Bundle load = bundle;
        if (b) {
            load = tokenCachingStrategy2.load();
        }
        if (!TokenCachingStrategy.hasTokenInformation(load)) {
            this.tokenInfo = AccessToken.createEmptyToken();
            return;
        }
        final Date date = TokenCachingStrategy.getDate(load, "com.facebook.TokenCachingStrategy.ExpirationDate");
        final Date date2 = new Date();
        if (date == null || date.before(date2)) {
            tokenCachingStrategy2.clear();
            this.tokenInfo = AccessToken.createEmptyToken();
            return;
        }
        this.tokenInfo = AccessToken.createFromCache(load);
        this.state = SessionState.CREATED_TOKEN_LOADED;
    }
    
    private Session(final String applicationId, final SessionState state, final AccessToken tokenInfo, final Date lastAttemptedTokenExtendDate, final boolean b, final AuthorizationRequest pendingAuthorizationRequest) {
        this.lastAttemptedTokenExtendDate = new Date(0L);
        this.lock = new Object();
        this.applicationId = applicationId;
        this.state = state;
        this.tokenInfo = tokenInfo;
        this.lastAttemptedTokenExtendDate = lastAttemptedTokenExtendDate;
        this.pendingAuthorizationRequest = pendingAuthorizationRequest;
        this.handler = new Handler(Looper.getMainLooper());
        this.currentTokenRefreshRequest = null;
        this.tokenCachingStrategy = null;
        this.callbacks = new ArrayList<StatusCallback>();
    }
    
    private Session(final String applicationId, final SessionState state, final AccessToken tokenInfo, final Date lastAttemptedTokenExtendDate, final boolean b, final AuthorizationRequest pendingAuthorizationRequest, final Set<String> set) {
        this.lastAttemptedTokenExtendDate = new Date(0L);
        this.lock = new Object();
        this.applicationId = applicationId;
        this.state = state;
        this.tokenInfo = tokenInfo;
        this.lastAttemptedTokenExtendDate = lastAttemptedTokenExtendDate;
        this.pendingAuthorizationRequest = pendingAuthorizationRequest;
        this.handler = new Handler(Looper.getMainLooper());
        this.currentTokenRefreshRequest = null;
        this.tokenCachingStrategy = null;
        this.callbacks = new ArrayList<StatusCallback>();
    }
    
    private static boolean areEqual(final Object o, final Object o2) {
        if (o == null) {
            return o2 == null;
        }
        return o.equals(o2);
    }
    
    private void finishAuthorization(final AccessToken tokenInfo, final Exception ex) {
        final SessionState state = this.state;
        if (tokenInfo != null) {
            this.saveTokenToCache(this.tokenInfo = tokenInfo);
            this.state = SessionState.OPENED;
        }
        else if (ex != null) {
            this.state = SessionState.CLOSED_LOGIN_FAILED;
        }
        this.pendingAuthorizationRequest = null;
        this.postStateChange(state, this.state, ex);
    }
    
    private void finishReauthorization(final AccessToken tokenInfo, final Exception ex) {
        final SessionState state = this.state;
        if (tokenInfo != null) {
            this.saveTokenToCache(this.tokenInfo = tokenInfo);
            this.state = SessionState.OPENED_TOKEN_UPDATED;
        }
        this.pendingAuthorizationRequest = null;
        this.postStateChange(state, this.state, ex);
    }
    
    public static final Session getActiveSession() {
        synchronized (Session.STATIC_LOCK) {
            return Session.activeSession;
        }
    }
    
    private AppEventsLogger getAppEventsLogger() {
        synchronized (this.lock) {
            if (this.appEventsLogger == null) {
                this.appEventsLogger = AppEventsLogger.newLogger(Session.staticContext, this.applicationId);
            }
            return this.appEventsLogger;
        }
    }
    
    private Intent getLoginActivityIntent(final AuthorizationRequest authorizationRequest) {
        final Intent intent = new Intent();
        intent.setClass(getStaticContext(), (Class)LoginActivity.class);
        intent.setAction(authorizationRequest.getLoginBehavior().toString());
        intent.putExtras(LoginActivity.populateIntentExtras(authorizationRequest.getAuthorizationClientRequest()));
        return intent;
    }
    
    static Context getStaticContext() {
        return Session.staticContext;
    }
    
    private void handleAuthorizationResult(final int n, final AuthorizationClient.Result result) {
        final AccessToken accessToken = null;
        Exception ex = null;
        AccessToken token;
        if (n == -1) {
            if (result.code == AuthorizationClient.Result.Code.SUCCESS) {
                token = result.token;
            }
            else {
                ex = new FacebookAuthorizationException(result.errorMessage);
                token = accessToken;
            }
        }
        else {
            token = accessToken;
            if (n == 0) {
                ex = new FacebookOperationCanceledException(result.errorMessage);
                token = accessToken;
            }
        }
        this.logAuthorizationComplete(result.code, result.loggingExtras, ex);
        this.authorizationClient = null;
        this.finishAuthOrReauth(token, ex);
    }
    
    static PermissionsPair handlePermissionResponse(final Response response) {
        if (response.getError() == null) {
            final GraphMultiResult graphMultiResult = response.getGraphObjectAs(GraphMultiResult.class);
            if (graphMultiResult != null) {
                final GraphObjectList<GraphObject> data = graphMultiResult.getData();
                if (data != null && data.size() != 0) {
                    final ArrayList list = new ArrayList<String>(data.size());
                    final ArrayList list2 = new ArrayList<String>(data.size());
                    final GraphObject graphObject = data.get(0);
                    if (graphObject.getProperty("permission") != null) {
                        for (final GraphObject graphObject2 : data) {
                            final String s = (String)graphObject2.getProperty("permission");
                            if (!s.equals("installed")) {
                                final String s2 = (String)graphObject2.getProperty("status");
                                if (s2.equals("granted")) {
                                    list.add(s);
                                }
                                else {
                                    if (!s2.equals("declined")) {
                                        continue;
                                    }
                                    list2.add(s);
                                }
                            }
                        }
                    }
                    else {
                        for (final Map.Entry<String, Object> entry : graphObject.asMap().entrySet()) {
                            if (!entry.getKey().equals("installed") && entry.getValue() == 1) {
                                list.add(entry.getKey());
                            }
                        }
                    }
                    return new PermissionsPair((List<String>)list, (List<String>)list2);
                }
            }
        }
        return null;
    }
    
    static void initializeStaticContext(Context staticContext) {
        if (staticContext != null && Session.staticContext == null) {
            final Context applicationContext = staticContext.getApplicationContext();
            if (applicationContext != null) {
                staticContext = applicationContext;
            }
            Session.staticContext = staticContext;
        }
    }
    
    public static boolean isPublishPermission(final String s) {
        return s != null && (s.startsWith("publish") || s.startsWith("manage") || Session.OTHER_PUBLISH_PERMISSIONS.contains(s));
    }
    
    private void logAuthorizationComplete(final AuthorizationClient.Result.Code code, final Map<String, String> map, Exception o) {
        Bundle authorizationLoggingBundle;
        if (this.pendingAuthorizationRequest == null) {
            authorizationLoggingBundle = AuthorizationClient.newAuthorizationLoggingBundle("");
            authorizationLoggingBundle.putString("2_result", AuthorizationClient.Result.Code.ERROR.getLoggingValue());
            authorizationLoggingBundle.putString("5_error_message", "Unexpected call to logAuthorizationComplete with null pendingAuthorizationRequest.");
        }
        else {
            final Bundle authorizationLoggingBundle2 = AuthorizationClient.newAuthorizationLoggingBundle(this.pendingAuthorizationRequest.getAuthId());
            if (code != null) {
                authorizationLoggingBundle2.putString("2_result", code.getLoggingValue());
            }
            if (o != null && ((Throwable)o).getMessage() != null) {
                authorizationLoggingBundle2.putString("5_error_message", ((Throwable)o).getMessage());
            }
            JSONObject jsonObject = null;
            if (!this.pendingAuthorizationRequest.loggingExtras.isEmpty()) {
                jsonObject = new JSONObject(this.pendingAuthorizationRequest.loggingExtras);
            }
            Object o2 = jsonObject;
            if (map != null) {
                if ((o = jsonObject) == null) {
                    o = new JSONObject();
                }
                try {
                    final Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
                    while (true) {
                        o2 = o;
                        if (!iterator.hasNext()) {
                            break;
                        }
                        final Map.Entry<String, String> entry = iterator.next();
                        ((JSONObject)o).put((String)entry.getKey(), (Object)entry.getValue());
                    }
                }
                catch (JSONException ex) {
                    o2 = o;
                }
            }
            authorizationLoggingBundle = authorizationLoggingBundle2;
            if (o2 != null) {
                authorizationLoggingBundle2.putString("6_extras", ((JSONObject)o2).toString());
                authorizationLoggingBundle = authorizationLoggingBundle2;
            }
        }
        authorizationLoggingBundle.putLong("1_timestamp_ms", System.currentTimeMillis());
        this.getAppEventsLogger().logSdkEvent("fb_mobile_login_complete", null, authorizationLoggingBundle);
    }
    
    private void logAuthorizationStart() {
        final Bundle authorizationLoggingBundle = AuthorizationClient.newAuthorizationLoggingBundle(this.pendingAuthorizationRequest.getAuthId());
        authorizationLoggingBundle.putLong("1_timestamp_ms", System.currentTimeMillis());
        while (true) {
            try {
                final JSONObject jsonObject = new JSONObject();
                jsonObject.put("login_behavior", (Object)this.pendingAuthorizationRequest.loginBehavior.toString());
                jsonObject.put("request_code", this.pendingAuthorizationRequest.requestCode);
                jsonObject.put("is_legacy", this.pendingAuthorizationRequest.isLegacy);
                jsonObject.put("permissions", (Object)TextUtils.join((CharSequence)",", (Iterable)this.pendingAuthorizationRequest.permissions));
                jsonObject.put("default_audience", (Object)this.pendingAuthorizationRequest.defaultAudience.toString());
                authorizationLoggingBundle.putString("6_extras", jsonObject.toString());
                this.getAppEventsLogger().logSdkEvent("fb_mobile_login_start", null, authorizationLoggingBundle);
            }
            catch (JSONException ex) {
                continue;
            }
            break;
        }
    }
    
    private void open(final OpenRequest openRequest, final SessionAuthorizationType sessionAuthorizationType) {
        SessionState state = null;
        SessionState state2;
        while (true) {
            this.validatePermissions((AuthorizationRequest)openRequest, sessionAuthorizationType);
            this.validateLoginBehavior((AuthorizationRequest)openRequest);
            Label_0172: {
            Label_0108:
                while (true) {
                    Label_0237: {
                        synchronized (this.lock) {
                            if (this.pendingAuthorizationRequest != null) {
                                this.postStateChange(this.state, this.state, new UnsupportedOperationException("Session: an attempt was made to open a session that has a pending request."));
                                return;
                            }
                            state = this.state;
                            switch (this.state) {
                                case OPENING: {
                                    throw new UnsupportedOperationException("Session: an attempt was made to open an already opened session.");
                                }
                                case CREATED: {
                                    break;
                                }
                                case CREATED_TOKEN_LOADED: {
                                    break Label_0172;
                                }
                                default: {
                                    break Label_0237;
                                }
                            }
                        }
                        break Label_0108;
                    }
                    continue;
                }
                state2 = SessionState.OPENING;
                this.state = state2;
                if (openRequest == null) {
                    throw new IllegalArgumentException("openRequest cannot be null when opening a new Session");
                }
                this.pendingAuthorizationRequest = (AuthorizationRequest)openRequest;
                break;
            }
            if (openRequest != null && !Utility.isNullOrEmpty(((AuthorizationRequest)openRequest).getPermissions()) && !Utility.isSubset(((AuthorizationRequest)openRequest).getPermissions(), this.getPermissions())) {
                this.pendingAuthorizationRequest = (AuthorizationRequest)openRequest;
            }
            if (this.pendingAuthorizationRequest == null) {
                state2 = SessionState.OPENED;
                this.state = state2;
                break;
            }
            state2 = SessionState.OPENING;
            this.state = state2;
            break;
        }
        if (openRequest != null) {
            this.addCallback(((AuthorizationRequest)openRequest).getCallback());
        }
        this.postStateChange(state, state2, null);
        // monitorexit(o)
        if (state2 == SessionState.OPENING) {
            this.authorize((AuthorizationRequest)openRequest);
        }
    }
    
    public static Session openActiveSession(final Activity activity, final boolean b, final StatusCallback callback) {
        return openActiveSession((Context)activity, b, new OpenRequest(activity).setCallback(callback));
    }
    
    public static Session openActiveSession(final Activity activity, final boolean b, final List<String> permissions, final StatusCallback callback) {
        return openActiveSession((Context)activity, b, new OpenRequest(activity).setCallback(callback).setPermissions(permissions));
    }
    
    public static Session openActiveSession(final Context context, final Fragment fragment, final boolean b, final StatusCallback callback) {
        return openActiveSession(context, b, new OpenRequest(fragment).setCallback(callback));
    }
    
    public static Session openActiveSession(final Context context, final Fragment fragment, final boolean b, final List<String> permissions, final StatusCallback callback) {
        return openActiveSession(context, b, new OpenRequest(fragment).setCallback(callback).setPermissions(permissions));
    }
    
    private static Session openActiveSession(final Context context, final boolean b, final OpenRequest openRequest) {
        final Session build = new Builder(context).build();
        if (SessionState.CREATED_TOKEN_LOADED.equals(build.getState()) || b) {
            setActiveSession(build);
            build.openForRead(openRequest);
            return build;
        }
        return null;
    }
    
    public static Session openActiveSessionFromCache(final Context context) {
        return openActiveSession(context, false, null);
    }
    
    public static Session openActiveSessionWithAccessToken(final Context context, final AccessToken accessToken, final StatusCallback statusCallback) {
        final Session activeSession = new Session(context, null, null, false);
        setActiveSession(activeSession);
        activeSession.open(accessToken, statusCallback);
        return activeSession;
    }
    
    static void postActiveSessionAction(final String s) {
        LocalBroadcastManager.getInstance(getStaticContext()).sendBroadcast(new Intent(s));
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Cannot readObject, serialization proxy required");
    }
    
    private void requestNewPermissions(final NewPermissionsRequest newPermissionsRequest, final SessionAuthorizationType sessionAuthorizationType) {
        this.validatePermissions((AuthorizationRequest)newPermissionsRequest, sessionAuthorizationType);
        this.validateLoginBehavior((AuthorizationRequest)newPermissionsRequest);
        if (newPermissionsRequest != null) {
            synchronized (this.lock) {
                if (this.pendingAuthorizationRequest != null) {
                    throw new UnsupportedOperationException("Session: an attempt was made to request new permissions for a session that has a pending request.");
                }
            }
            if (this.state.isOpened()) {
                final AuthorizationRequest pendingAuthorizationRequest;
                this.pendingAuthorizationRequest = pendingAuthorizationRequest;
                // monitorexit(sessionAuthorizationType)
                pendingAuthorizationRequest.setValidateSameFbidAsToken(this.getAccessToken());
                this.addCallback(pendingAuthorizationRequest.getCallback());
                this.authorize(pendingAuthorizationRequest);
            }
            else {
                if (this.state.isClosed()) {
                    throw new UnsupportedOperationException("Session: an attempt was made to request new permissions for a session that has been closed.");
                }
                throw new UnsupportedOperationException("Session: an attempt was made to request new permissions for a session that is not currently open.");
            }
        }
    }
    
    private boolean resolveIntent(final Intent intent) {
        return getStaticContext().getPackageManager().resolveActivity(intent, 0) != null;
    }
    
    public static final Session restoreSession(final Context context, final TokenCachingStrategy tokenCachingStrategy, final StatusCallback statusCallback, final Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        final byte[] byteArray = bundle.getByteArray("com.facebook.sdk.Session.saveSessionKey");
        if (byteArray == null) {
            goto Label_0097;
        }
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        try {
            final Session session = (Session)new ObjectInputStream(byteArrayInputStream).readObject();
            initializeStaticContext(context);
            if (tokenCachingStrategy != null) {
                session.tokenCachingStrategy = tokenCachingStrategy;
                if (statusCallback != null) {
                    session.addCallback(statusCallback);
                }
                session.authorizationBundle = bundle.getBundle("com.facebook.sdk.Session.authBundleKey");
                return session;
            }
            goto Label_0099;
        }
        catch (ClassNotFoundException ex) {
            Log.w(Session.TAG, "Unable to restore session", (Throwable)ex);
        }
        catch (IOException ex2) {
            Log.w(Session.TAG, "Unable to restore session.", (Throwable)ex2);
            goto Label_0097;
        }
    }
    
    private static void runWithHandlerOrExecutor(final Handler handler, final Runnable runnable) {
        if (handler != null) {
            handler.post(runnable);
            return;
        }
        Settings.getExecutor().execute(runnable);
    }
    
    public static final void saveSession(final Session session, final Bundle bundle) {
        if (bundle == null || session == null || bundle.containsKey("com.facebook.sdk.Session.saveSessionKey")) {
            return;
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(byteArrayOutputStream).writeObject(session);
            bundle.putByteArray("com.facebook.sdk.Session.saveSessionKey", byteArrayOutputStream.toByteArray());
            bundle.putBundle("com.facebook.sdk.Session.authBundleKey", session.authorizationBundle);
        }
        catch (IOException ex) {
            throw new FacebookException("Unable to save session.", ex);
        }
    }
    
    private void saveTokenToCache(final AccessToken accessToken) {
        if (accessToken != null && this.tokenCachingStrategy != null) {
            this.tokenCachingStrategy.save(accessToken.toCacheBundle());
        }
    }
    
    public static final void setActiveSession(final Session activeSession) {
        synchronized (Session.STATIC_LOCK) {
            if (activeSession != Session.activeSession) {
                final Session activeSession2 = Session.activeSession;
                if (activeSession2 != null) {
                    activeSession2.close();
                }
                Session.activeSession = activeSession;
                if (activeSession2 != null) {
                    postActiveSessionAction("com.facebook.sdk.ACTIVE_SESSION_UNSET");
                }
                if (activeSession != null) {
                    postActiveSessionAction("com.facebook.sdk.ACTIVE_SESSION_SET");
                    if (activeSession.isOpened()) {
                        postActiveSessionAction("com.facebook.sdk.ACTIVE_SESSION_OPENED");
                    }
                }
            }
        }
    }
    
    private void tryLegacyAuth(final AuthorizationRequest authorizationRequest) {
        (this.authorizationClient = new AuthorizationClient()).setOnCompletedListener((AuthorizationClient.OnCompletedListener)new AuthorizationClient.OnCompletedListener() {
            @Override
            public void onCompleted(final Result result) {
                int n;
                if (result.code == Code.CANCEL) {
                    n = 0;
                }
                else {
                    n = -1;
                }
                Session.this.handleAuthorizationResult(n, result);
            }
        });
        this.authorizationClient.setContext(getStaticContext());
        this.authorizationClient.startOrContinueAuth(authorizationRequest.getAuthorizationClientRequest());
    }
    
    private boolean tryLoginActivity(final AuthorizationRequest authorizationRequest) {
        final Intent loginActivityIntent = this.getLoginActivityIntent(authorizationRequest);
        if (!this.resolveIntent(loginActivityIntent)) {
            return false;
        }
        try {
            authorizationRequest.getStartActivityDelegate().startActivityForResult(loginActivityIntent, authorizationRequest.getRequestCode());
            return true;
        }
        catch (ActivityNotFoundException ex) {
            return false;
        }
    }
    
    private void validateLoginBehavior(final AuthorizationRequest authorizationRequest) {
        if (authorizationRequest != null && !authorizationRequest.isLegacy) {
            final Intent intent = new Intent();
            intent.setClass(getStaticContext(), (Class)LoginActivity.class);
            if (!this.resolveIntent(intent)) {
                throw new FacebookException(String.format("Cannot use SessionLoginBehavior %s when %s is not declared as an activity in AndroidManifest.xml", authorizationRequest.getLoginBehavior(), LoginActivity.class.getName()));
            }
        }
    }
    
    private void validatePermissions(final AuthorizationRequest authorizationRequest, final SessionAuthorizationType sessionAuthorizationType) {
        if (authorizationRequest == null || Utility.isNullOrEmpty(authorizationRequest.getPermissions())) {
            if (SessionAuthorizationType.PUBLISH.equals(sessionAuthorizationType)) {
                throw new FacebookException("Cannot request publish or manage authorization with no permissions.");
            }
        }
        else {
            for (final String s : authorizationRequest.getPermissions()) {
                if (isPublishPermission(s)) {
                    if (SessionAuthorizationType.READ.equals(sessionAuthorizationType)) {
                        throw new FacebookException(String.format("Cannot pass a publish or manage permission (%s) to a request for read authorization", s));
                    }
                    continue;
                }
                else {
                    if (!SessionAuthorizationType.PUBLISH.equals(sessionAuthorizationType)) {
                        continue;
                    }
                    Log.w(Session.TAG, String.format("Should not pass a read permission (%s) to a request for publish or manage authorization", s));
                }
            }
        }
    }
    
    private Object writeReplace() {
        return new SerializationProxyV1(this.applicationId, this.state, this.tokenInfo, this.lastAttemptedTokenExtendDate, false, this.pendingAuthorizationRequest);
    }
    
    public final void addCallback(final StatusCallback statusCallback) {
        final List<StatusCallback> callbacks = this.callbacks;
        // monitorenter(callbacks)
        if (statusCallback == null) {
            return;
        }
        try {
            if (!this.callbacks.contains(statusCallback)) {
                this.callbacks.add(statusCallback);
            }
        }
        finally {
        }
        // monitorexit(callbacks)
    }
    
    void authorize(final AuthorizationRequest authorizationRequest) {
        authorizationRequest.setApplicationId(this.applicationId);
        this.logAuthorizationStart();
        final boolean tryLoginActivity = this.tryLoginActivity(authorizationRequest);
        Object access$500 = this.pendingAuthorizationRequest.loggingExtras;
        while (true) {
            final String s;
            Label_0036: {
                if (tryLoginActivity) {
                    s = "1";
                    break Label_0036;
                }
                Label_0184: {
                    break Label_0184;
                    while (true) {
                        while (true) {
                            Label_0202: {
                                synchronized (this.lock) {
                                    final SessionState state = this.state;
                                    switch (this.state) {
                                        case CLOSED:
                                        case CLOSED_LOGIN_FAILED: {
                                            return;
                                        }
                                        default: {
                                            break Label_0202;
                                        }
                                    }
                                    return;
                                    this.state = SessionState.CLOSED_LOGIN_FAILED;
                                    access$500 = new FacebookException("Log in attempt failed: LoginActivity could not be started, and not legacy request");
                                    this.logAuthorizationComplete(AuthorizationClient.Result.Code.ERROR, null, (Exception)access$500);
                                    this.postStateChange(state, this.state, (Exception)access$500);
                                    return;
                                    break;
                                }
                            }
                            continue;
                        }
                    }
                }
            }
            ((Map<String, String>)access$500).put("try_login_activity", s);
            boolean b = tryLoginActivity;
            if (!tryLoginActivity) {
                b = tryLoginActivity;
                if (authorizationRequest.isLegacy) {
                    this.pendingAuthorizationRequest.loggingExtras.put("try_legacy", "1");
                    this.tryLegacyAuth(authorizationRequest);
                    b = true;
                }
            }
            if (!b) {
                continue;
            }
            break;
        }
    }
    
    public final void close() {
        while (true) {
            final SessionState state;
            synchronized (this.lock) {
                state = this.state;
                switch (this.state) {
                    case CREATED:
                    case OPENING: {
                        this.postStateChange(state, this.state = SessionState.CLOSED_LOGIN_FAILED, new FacebookException("Log in attempt aborted."));
                        return;
                    }
                    case CREATED_TOKEN_LOADED:
                    case OPENED:
                    case OPENED_TOKEN_UPDATED: {
                        break;
                    }
                    default: {
                        return;
                    }
                }
            }
            this.postStateChange(state, this.state = SessionState.CLOSED, null);
        }
    }
    
    public final void closeAndClearTokenInformation() {
        if (this.tokenCachingStrategy != null) {
            this.tokenCachingStrategy.clear();
        }
        Utility.clearFacebookCookies(Session.staticContext);
        Utility.clearCaches(Session.staticContext);
        this.close();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof Session) {
            final Session session = (Session)o;
            if (areEqual(session.applicationId, this.applicationId) && areEqual(session.authorizationBundle, this.authorizationBundle) && areEqual(session.state, this.state) && areEqual(session.getExpirationDate(), this.getExpirationDate())) {
                return true;
            }
        }
        return false;
    }
    
    void extendAccessToken() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_1       
        //     2: aload_0        
        //     3: getfield        com/facebook/Session.lock:Ljava/lang/Object;
        //     6: astore_2       
        //     7: aload_2        
        //     8: monitorenter   
        //     9: aload_0        
        //    10: getfield        com/facebook/Session.currentTokenRefreshRequest:Lcom/facebook/Session$TokenRefreshRequest;
        //    13: ifnonnull       30
        //    16: new             Lcom/facebook/Session$TokenRefreshRequest;
        //    19: dup            
        //    20: aload_0        
        //    21: invokespecial   com/facebook/Session$TokenRefreshRequest.<init>:(Lcom/facebook/Session;)V
        //    24: astore_1       
        //    25: aload_0        
        //    26: aload_1        
        //    27: putfield        com/facebook/Session.currentTokenRefreshRequest:Lcom/facebook/Session$TokenRefreshRequest;
        //    30: aload_2        
        //    31: monitorexit    
        //    32: aload_1        
        //    33: ifnull          40
        //    36: aload_1        
        //    37: invokevirtual   com/facebook/Session$TokenRefreshRequest.bind:()V
        //    40: return         
        //    41: astore_1       
        //    42: aload_2        
        //    43: monitorexit    
        //    44: aload_1        
        //    45: athrow         
        //    46: astore_1       
        //    47: goto            42
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  9      25     41     42     Any
        //  25     30     46     50     Any
        //  30     32     41     42     Any
        //  42     44     41     42     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0030:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    void extendAccessTokenIfNeeded() {
        if (this.shouldExtendAccessToken()) {
            this.extendAccessToken();
        }
    }
    
    void extendTokenCompleted(final Bundle bundle) {
        while (true) {
            while (true) {
                Label_0135: {
                    synchronized (this.lock) {
                        final SessionState state = this.state;
                        switch (this.state) {
                            case OPENED: {
                                this.postStateChange(state, this.state = SessionState.OPENED_TOKEN_UPDATED, null);
                            }
                            case OPENED_TOKEN_UPDATED: {
                                this.tokenInfo = AccessToken.createFromRefresh(this.tokenInfo, bundle);
                                if (this.tokenCachingStrategy != null) {
                                    this.tokenCachingStrategy.save(this.tokenInfo.toCacheBundle());
                                }
                                return;
                            }
                            default: {
                                break Label_0135;
                            }
                        }
                        Log.d(Session.TAG, "refreshToken ignored in state " + this.state);
                        return;
                    }
                }
                continue;
            }
        }
    }
    
    void finishAuthOrReauth(final AccessToken accessToken, final Exception ex) {
        AccessToken accessToken2 = accessToken;
        Exception ex2 = ex;
        if (accessToken != null) {
            accessToken2 = accessToken;
            ex2 = ex;
            if (accessToken.isInvalid()) {
                accessToken2 = null;
                ex2 = new FacebookException("Invalid access token.");
            }
        }
        while (true) {
            Label_0124: {
                synchronized (this.lock) {
                    switch (this.state) {
                        case OPENING: {
                            this.finishAuthorization(accessToken2, ex2);
                            return;
                        }
                        case OPENED:
                        case OPENED_TOKEN_UPDATED: {
                            break;
                        }
                        case CREATED:
                        case CREATED_TOKEN_LOADED:
                        case CLOSED:
                        case CLOSED_LOGIN_FAILED: {
                            break Label_0124;
                        }
                        default: {
                            return;
                        }
                    }
                }
                this.finishReauthorization(accessToken2, ex2);
                return;
            }
            Log.d(Session.TAG, "Unexpected call to finishAuthOrReauth in state " + this.state);
        }
    }
    
    public final String getAccessToken() {
        synchronized (this.lock) {
            String token;
            if (this.tokenInfo == null) {
                token = null;
            }
            else {
                token = this.tokenInfo.getToken();
            }
            return token;
        }
    }
    
    public final String getApplicationId() {
        return this.applicationId;
    }
    
    public final Bundle getAuthorizationBundle() {
        synchronized (this.lock) {
            return this.authorizationBundle;
        }
    }
    
    public final List<String> getDeclinedPermissions() {
        synchronized (this.lock) {
            List<String> declinedPermissions;
            if (this.tokenInfo == null) {
                declinedPermissions = null;
            }
            else {
                declinedPermissions = this.tokenInfo.getDeclinedPermissions();
            }
            return declinedPermissions;
        }
    }
    
    public final Date getExpirationDate() {
        synchronized (this.lock) {
            Date expires;
            if (this.tokenInfo == null) {
                expires = null;
            }
            else {
                expires = this.tokenInfo.getExpires();
            }
            return expires;
        }
    }
    
    Date getLastAttemptedTokenExtendDate() {
        return this.lastAttemptedTokenExtendDate;
    }
    
    public final List<String> getPermissions() {
        synchronized (this.lock) {
            List<String> permissions;
            if (this.tokenInfo == null) {
                permissions = null;
            }
            else {
                permissions = this.tokenInfo.getPermissions();
            }
            return permissions;
        }
    }
    
    public final SessionState getState() {
        synchronized (this.lock) {
            return this.state;
        }
    }
    
    AccessToken getTokenInfo() {
        return this.tokenInfo;
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    public final boolean isClosed() {
        synchronized (this.lock) {
            return this.state.isClosed();
        }
    }
    
    public final boolean isOpened() {
        synchronized (this.lock) {
            return this.state.isOpened();
        }
    }
    
    public boolean isPermissionGranted(final String s) {
        final List<String> permissions = this.getPermissions();
        return permissions != null && permissions.contains(s);
    }
    
    public final boolean onActivityResult(Activity activity, final int n, final int n2, final Intent intent) {
        Validate.notNull(activity, "currentActivity");
        initializeStaticContext((Context)activity);
        AuthorizationClient.Result.Code code = null;
        Label_0129: {
            Label_0109: {
                synchronized (this.lock) {
                    if (this.pendingAuthorizationRequest == null || n != this.pendingAuthorizationRequest.getRequestCode()) {
                        return false;
                    }
                    // monitorexit(this.lock)
                    activity = null;
                    code = AuthorizationClient.Result.Code.ERROR;
                    if (intent == null) {
                        break Label_0109;
                    }
                    final AuthorizationClient.Result result = (AuthorizationClient.Result)intent.getSerializableExtra("com.facebook.LoginActivity:Result");
                    if (result != null) {
                        this.handleAuthorizationResult(n2, result);
                        return true;
                    }
                }
                if (this.authorizationClient != null) {
                    final Intent intent2;
                    this.authorizationClient.onActivityResult(n, n2, intent2);
                    return true;
                }
                break Label_0129;
            }
            if (n2 == 0) {
                activity = (Activity)new FacebookOperationCanceledException("User canceled operation.");
                code = AuthorizationClient.Result.Code.CANCEL;
            }
        }
        Object o;
        if ((o = activity) == null) {
            o = new FacebookException("Unexpected call to Session.onActivityResult");
        }
        this.logAuthorizationComplete(code, null, (Exception)o);
        this.finishAuthOrReauth(null, (Exception)o);
        return true;
    }
    
    public final void open(final AccessToken accessToken, final StatusCallback statusCallback) {
        synchronized (this.lock) {
            if (this.pendingAuthorizationRequest != null) {
                throw new UnsupportedOperationException("Session: an attempt was made to open a session that has a pending request.");
            }
        }
        if (this.state.isClosed()) {
            throw new UnsupportedOperationException("Session: an attempt was made to open a previously-closed session.");
        }
        if (this.state != SessionState.CREATED && this.state != SessionState.CREATED_TOKEN_LOADED) {
            throw new UnsupportedOperationException("Session: an attempt was made to open an already opened session.");
        }
        if (statusCallback != null) {
            this.addCallback(statusCallback);
        }
        final AccessToken tokenInfo;
        this.tokenInfo = tokenInfo;
        if (this.tokenCachingStrategy != null) {
            this.tokenCachingStrategy.save(tokenInfo.toCacheBundle());
        }
        this.postStateChange(this.state, this.state = SessionState.OPENED, null);
    }
    // monitorexit(o)
    
    public final void openForPublish(final OpenRequest openRequest) {
        this.open(openRequest, SessionAuthorizationType.PUBLISH);
    }
    
    public final void openForRead(final OpenRequest openRequest) {
        this.open(openRequest, SessionAuthorizationType.READ);
    }
    
    void postStateChange(final SessionState sessionState, final SessionState sessionState2, final Exception ex) {
        if (sessionState != sessionState2 || sessionState == SessionState.OPENED_TOKEN_UPDATED || ex != null) {
            if (sessionState2.isClosed()) {
                this.tokenInfo = AccessToken.createEmptyToken();
            }
            runWithHandlerOrExecutor(this.handler, new Runnable() {
                @Override
                public void run() {
                    synchronized (Session.this.callbacks) {
                        final Iterator<StatusCallback> iterator = Session.this.callbacks.iterator();
                        while (iterator.hasNext()) {
                            runWithHandlerOrExecutor(Session.this.handler, new Runnable() {
                                final /* synthetic */ StatusCallback val$callback = iterator.next();
                                
                                @Override
                                public void run() {
                                    this.val$callback.call(Session.this, sessionState2, ex);
                                }
                            });
                        }
                    }
                }
                // monitorexit(list)
            });
            if (this == Session.activeSession && sessionState.isOpened() != sessionState2.isOpened()) {
                if (sessionState2.isOpened()) {
                    postActiveSessionAction("com.facebook.sdk.ACTIVE_SESSION_OPENED");
                    return;
                }
                postActiveSessionAction("com.facebook.sdk.ACTIVE_SESSION_CLOSED");
            }
        }
    }
    
    public final void refreshPermissions() {
        final Request request = new Request(this, "me/permissions");
        request.setCallback((Request.Callback)new Request.Callback() {
            @Override
            public void onCompleted(final Response response) {
                final PermissionsPair handlePermissionResponse = Session.handlePermissionResponse(response);
                if (handlePermissionResponse != null) {
                    synchronized (Session.this.lock) {
                        Session.this.tokenInfo = AccessToken.createFromTokenWithRefreshedPermissions(Session.this.tokenInfo, handlePermissionResponse.getGrantedPermissions(), handlePermissionResponse.getDeclinedPermissions());
                        Session.this.postStateChange(Session.this.state, SessionState.OPENED_TOKEN_UPDATED, null);
                    }
                }
            }
        });
        request.executeAsync();
    }
    
    public final void removeCallback(final StatusCallback statusCallback) {
        synchronized (this.callbacks) {
            this.callbacks.remove(statusCallback);
        }
    }
    
    public final void requestNewPublishPermissions(final NewPermissionsRequest newPermissionsRequest) {
        this.requestNewPermissions(newPermissionsRequest, SessionAuthorizationType.PUBLISH);
    }
    
    public final void requestNewReadPermissions(final NewPermissionsRequest newPermissionsRequest) {
        this.requestNewPermissions(newPermissionsRequest, SessionAuthorizationType.READ);
    }
    
    void setCurrentTokenRefreshRequest(final TokenRefreshRequest currentTokenRefreshRequest) {
        this.currentTokenRefreshRequest = currentTokenRefreshRequest;
    }
    
    void setLastAttemptedTokenExtendDate(final Date lastAttemptedTokenExtendDate) {
        this.lastAttemptedTokenExtendDate = lastAttemptedTokenExtendDate;
    }
    
    void setTokenInfo(final AccessToken tokenInfo) {
        this.tokenInfo = tokenInfo;
    }
    
    boolean shouldExtendAccessToken() {
        if (this.currentTokenRefreshRequest == null) {
            final Date date = new Date();
            if (this.state.isOpened() && this.tokenInfo.getSource().canExtendToken() && date.getTime() - this.lastAttemptedTokenExtendDate.getTime() > 3600000L && date.getTime() - this.tokenInfo.getLastRefresh().getTime() > 86400000L) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        final StringBuilder append = new StringBuilder().append("{Session").append(" state:").append(this.state).append(", token:");
        Serializable tokenInfo;
        if (this.tokenInfo == null) {
            tokenInfo = "null";
        }
        else {
            tokenInfo = this.tokenInfo;
        }
        final StringBuilder append2 = append.append(tokenInfo).append(", appId:");
        String applicationId;
        if (this.applicationId == null) {
            applicationId = "null";
        }
        else {
            applicationId = this.applicationId;
        }
        return append2.append(applicationId).append("}").toString();
    }
    
    public static class AuthorizationRequest implements Serializable
    {
        private static final long serialVersionUID = 1L;
        private String applicationId;
        private final String authId;
        private SessionDefaultAudience defaultAudience;
        private boolean isLegacy;
        private final Map<String, String> loggingExtras;
        private SessionLoginBehavior loginBehavior;
        private List<String> permissions;
        private int requestCode;
        private final StartActivityDelegate startActivityDelegate;
        private StatusCallback statusCallback;
        private String validateSameFbidAsToken;
        
        AuthorizationRequest(final Activity activity) {
            this.loginBehavior = SessionLoginBehavior.SSO_WITH_FALLBACK;
            this.requestCode = 64206;
            this.isLegacy = false;
            this.permissions = Collections.emptyList();
            this.defaultAudience = SessionDefaultAudience.FRIENDS;
            this.authId = UUID.randomUUID().toString();
            this.loggingExtras = new HashMap<String, String>();
            this.startActivityDelegate = new StartActivityDelegate() {
                @Override
                public Activity getActivityContext() {
                    return activity;
                }
                
                @Override
                public void startActivityForResult(final Intent intent, final int n) {
                    activity.startActivityForResult(intent, n);
                }
            };
        }
        
        AuthorizationRequest(final Fragment fragment) {
            this.loginBehavior = SessionLoginBehavior.SSO_WITH_FALLBACK;
            this.requestCode = 64206;
            this.isLegacy = false;
            this.permissions = Collections.emptyList();
            this.defaultAudience = SessionDefaultAudience.FRIENDS;
            this.authId = UUID.randomUUID().toString();
            this.loggingExtras = new HashMap<String, String>();
            this.startActivityDelegate = new StartActivityDelegate() {
                @Override
                public Activity getActivityContext() {
                    return fragment.getActivity();
                }
                
                @Override
                public void startActivityForResult(final Intent intent, final int n) {
                    fragment.startActivityForResult(intent, n);
                }
            };
        }
        
        private AuthorizationRequest(final SessionLoginBehavior loginBehavior, final int requestCode, final List<String> permissions, final String s, final boolean isLegacy, final String applicationId, final String validateSameFbidAsToken) {
            this.loginBehavior = SessionLoginBehavior.SSO_WITH_FALLBACK;
            this.requestCode = 64206;
            this.isLegacy = false;
            this.permissions = Collections.emptyList();
            this.defaultAudience = SessionDefaultAudience.FRIENDS;
            this.authId = UUID.randomUUID().toString();
            this.loggingExtras = new HashMap<String, String>();
            this.startActivityDelegate = new StartActivityDelegate() {
                @Override
                public Activity getActivityContext() {
                    throw new UnsupportedOperationException("Cannot create an AuthorizationRequest without a valid Activity or Fragment");
                }
                
                @Override
                public void startActivityForResult(final Intent intent, final int n) {
                    throw new UnsupportedOperationException("Cannot create an AuthorizationRequest without a valid Activity or Fragment");
                }
            };
            this.loginBehavior = loginBehavior;
            this.requestCode = requestCode;
            this.permissions = permissions;
            this.defaultAudience = SessionDefaultAudience.valueOf(s);
            this.isLegacy = isLegacy;
            this.applicationId = applicationId;
            this.validateSameFbidAsToken = validateSameFbidAsToken;
        }
        
        private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
            throw new InvalidObjectException("Cannot readObject, serialization proxy required");
        }
        
        String getApplicationId() {
            return this.applicationId;
        }
        
        String getAuthId() {
            return this.authId;
        }
        
        AuthorizationClient.AuthorizationRequest getAuthorizationClientRequest() {
            return new AuthorizationClient.AuthorizationRequest(this.loginBehavior, this.requestCode, this.isLegacy, this.permissions, this.defaultAudience, this.applicationId, this.validateSameFbidAsToken, new AuthorizationClient.StartActivityDelegate() {
                @Override
                public Activity getActivityContext() {
                    return Session.AuthorizationRequest.this.startActivityDelegate.getActivityContext();
                }
                
                @Override
                public void startActivityForResult(final Intent intent, final int n) {
                    Session.AuthorizationRequest.this.startActivityDelegate.startActivityForResult(intent, n);
                }
            }, this.authId);
        }
        
        StatusCallback getCallback() {
            return this.statusCallback;
        }
        
        SessionDefaultAudience getDefaultAudience() {
            return this.defaultAudience;
        }
        
        SessionLoginBehavior getLoginBehavior() {
            return this.loginBehavior;
        }
        
        List<String> getPermissions() {
            return this.permissions;
        }
        
        int getRequestCode() {
            return this.requestCode;
        }
        
        StartActivityDelegate getStartActivityDelegate() {
            return this.startActivityDelegate;
        }
        
        String getValidateSameFbidAsToken() {
            return this.validateSameFbidAsToken;
        }
        
        boolean isLegacy() {
            return this.isLegacy;
        }
        
        void setApplicationId(final String applicationId) {
            this.applicationId = applicationId;
        }
        
        AuthorizationRequest setCallback(final StatusCallback statusCallback) {
            this.statusCallback = statusCallback;
            return this;
        }
        
        AuthorizationRequest setDefaultAudience(final SessionDefaultAudience defaultAudience) {
            if (defaultAudience != null) {
                this.defaultAudience = defaultAudience;
            }
            return this;
        }
        
        public void setIsLegacy(final boolean isLegacy) {
            this.isLegacy = isLegacy;
        }
        
        AuthorizationRequest setLoginBehavior(final SessionLoginBehavior loginBehavior) {
            if (loginBehavior != null) {
                this.loginBehavior = loginBehavior;
            }
            return this;
        }
        
        AuthorizationRequest setPermissions(final List<String> permissions) {
            if (permissions != null) {
                this.permissions = permissions;
            }
            return this;
        }
        
        AuthorizationRequest setPermissions(final String... array) {
            return this.setPermissions(Arrays.asList(array));
        }
        
        AuthorizationRequest setRequestCode(final int requestCode) {
            if (requestCode >= 0) {
                this.requestCode = requestCode;
            }
            return this;
        }
        
        void setValidateSameFbidAsToken(final String validateSameFbidAsToken) {
            this.validateSameFbidAsToken = validateSameFbidAsToken;
        }
        
        Object writeReplace() {
            return new AuthRequestSerializationProxyV1(this.loginBehavior, this.requestCode, (List)this.permissions, this.defaultAudience.name(), this.isLegacy, this.applicationId, this.validateSameFbidAsToken);
        }
        
        private static class AuthRequestSerializationProxyV1 implements Serializable
        {
            private static final long serialVersionUID = -8748347685113614927L;
            private final String applicationId;
            private final String defaultAudience;
            private boolean isLegacy;
            private final SessionLoginBehavior loginBehavior;
            private final List<String> permissions;
            private final int requestCode;
            private final String validateSameFbidAsToken;
            
            private AuthRequestSerializationProxyV1(final SessionLoginBehavior loginBehavior, final int requestCode, final List<String> permissions, final String defaultAudience, final boolean isLegacy, final String applicationId, final String validateSameFbidAsToken) {
                this.loginBehavior = loginBehavior;
                this.requestCode = requestCode;
                this.permissions = permissions;
                this.defaultAudience = defaultAudience;
                this.isLegacy = isLegacy;
                this.applicationId = applicationId;
                this.validateSameFbidAsToken = validateSameFbidAsToken;
            }
            
            private Object readResolve() {
                return new AuthorizationRequest(this.loginBehavior, this.requestCode, (List)this.permissions, this.defaultAudience, this.isLegacy, this.applicationId, this.validateSameFbidAsToken);
            }
        }
    }
    
    public static final class Builder
    {
        private String applicationId;
        private final Context context;
        private TokenCachingStrategy tokenCachingStrategy;
        
        public Builder(final Context context) {
            this.context = context;
        }
        
        public Session build() {
            return new Session(this.context, this.applicationId, this.tokenCachingStrategy);
        }
        
        public Builder setApplicationId(final String applicationId) {
            this.applicationId = applicationId;
            return this;
        }
        
        public Builder setTokenCachingStrategy(final TokenCachingStrategy tokenCachingStrategy) {
            this.tokenCachingStrategy = tokenCachingStrategy;
            return this;
        }
    }
    
    public static final class NewPermissionsRequest extends AuthorizationRequest
    {
        private static final long serialVersionUID = 1L;
        
        public NewPermissionsRequest(final Activity activity, final List<String> permissions) {
            super(activity);
            ((AuthorizationRequest)this).setPermissions(permissions);
        }
        
        public NewPermissionsRequest(final Activity activity, final String... permissions) {
            super(activity);
            ((AuthorizationRequest)this).setPermissions(permissions);
        }
        
        public NewPermissionsRequest(final Fragment fragment, final List<String> permissions) {
            super(fragment);
            ((AuthorizationRequest)this).setPermissions(permissions);
        }
        
        public NewPermissionsRequest(final Fragment fragment, final String... permissions) {
            super(fragment);
            ((AuthorizationRequest)this).setPermissions(permissions);
        }
        
        @Override
        AuthorizationClient.AuthorizationRequest getAuthorizationClientRequest() {
            final AuthorizationClient.AuthorizationRequest authorizationClientRequest = super.getAuthorizationClientRequest();
            authorizationClientRequest.setRerequest(true);
            return authorizationClientRequest;
        }
        
        public final NewPermissionsRequest setCallback(final StatusCallback callback) {
            super.setCallback(callback);
            return this;
        }
        
        public final NewPermissionsRequest setDefaultAudience(final SessionDefaultAudience defaultAudience) {
            super.setDefaultAudience(defaultAudience);
            return this;
        }
        
        public final NewPermissionsRequest setLoginBehavior(final SessionLoginBehavior loginBehavior) {
            super.setLoginBehavior(loginBehavior);
            return this;
        }
        
        public final NewPermissionsRequest setRequestCode(final int requestCode) {
            super.setRequestCode(requestCode);
            return this;
        }
    }
    
    public static final class OpenRequest extends AuthorizationRequest
    {
        private static final long serialVersionUID = 1L;
        
        public OpenRequest(final Activity activity) {
            super(activity);
        }
        
        public OpenRequest(final Fragment fragment) {
            super(fragment);
        }
        
        public final OpenRequest setCallback(final StatusCallback callback) {
            super.setCallback(callback);
            return this;
        }
        
        public final OpenRequest setDefaultAudience(final SessionDefaultAudience defaultAudience) {
            super.setDefaultAudience(defaultAudience);
            return this;
        }
        
        public final OpenRequest setLoginBehavior(final SessionLoginBehavior loginBehavior) {
            super.setLoginBehavior(loginBehavior);
            return this;
        }
        
        public final OpenRequest setPermissions(final List<String> permissions) {
            super.setPermissions(permissions);
            return this;
        }
        
        public final OpenRequest setPermissions(final String... permissions) {
            super.setPermissions(permissions);
            return this;
        }
        
        public final OpenRequest setRequestCode(final int requestCode) {
            super.setRequestCode(requestCode);
            return this;
        }
    }
    
    static class PermissionsPair
    {
        List<String> declinedPermissions;
        List<String> grantedPermissions;
        
        public PermissionsPair(final List<String> grantedPermissions, final List<String> declinedPermissions) {
            this.grantedPermissions = grantedPermissions;
            this.declinedPermissions = declinedPermissions;
        }
        
        public List<String> getDeclinedPermissions() {
            return this.declinedPermissions;
        }
        
        public List<String> getGrantedPermissions() {
            return this.grantedPermissions;
        }
    }
    
    private static class SerializationProxyV1 implements Serializable
    {
        private static final long serialVersionUID = 7663436173185080063L;
        private final String applicationId;
        private final Date lastAttemptedTokenExtendDate;
        private final AuthorizationRequest pendingAuthorizationRequest;
        private final boolean shouldAutoPublish;
        private final SessionState state;
        private final AccessToken tokenInfo;
        
        SerializationProxyV1(final String applicationId, final SessionState state, final AccessToken tokenInfo, final Date lastAttemptedTokenExtendDate, final boolean shouldAutoPublish, final AuthorizationRequest pendingAuthorizationRequest) {
            this.applicationId = applicationId;
            this.state = state;
            this.tokenInfo = tokenInfo;
            this.lastAttemptedTokenExtendDate = lastAttemptedTokenExtendDate;
            this.shouldAutoPublish = shouldAutoPublish;
            this.pendingAuthorizationRequest = pendingAuthorizationRequest;
        }
        
        private Object readResolve() {
            return new Session(this.applicationId, this.state, this.tokenInfo, this.lastAttemptedTokenExtendDate, this.shouldAutoPublish, this.pendingAuthorizationRequest, null);
        }
    }
    
    private static class SerializationProxyV2 implements Serializable
    {
        private static final long serialVersionUID = 7663436173185080064L;
        private final String applicationId;
        private final Date lastAttemptedTokenExtendDate;
        private final AuthorizationRequest pendingAuthorizationRequest;
        private final Set<String> requestedPermissions;
        private final boolean shouldAutoPublish;
        private final SessionState state;
        private final AccessToken tokenInfo;
        
        SerializationProxyV2(final String applicationId, final SessionState state, final AccessToken tokenInfo, final Date lastAttemptedTokenExtendDate, final boolean shouldAutoPublish, final AuthorizationRequest pendingAuthorizationRequest, final Set<String> requestedPermissions) {
            this.applicationId = applicationId;
            this.state = state;
            this.tokenInfo = tokenInfo;
            this.lastAttemptedTokenExtendDate = lastAttemptedTokenExtendDate;
            this.shouldAutoPublish = shouldAutoPublish;
            this.pendingAuthorizationRequest = pendingAuthorizationRequest;
            this.requestedPermissions = requestedPermissions;
        }
        
        private Object readResolve() {
            return new Session(this.applicationId, this.state, this.tokenInfo, this.lastAttemptedTokenExtendDate, this.shouldAutoPublish, this.pendingAuthorizationRequest, this.requestedPermissions, null);
        }
    }
    
    interface StartActivityDelegate
    {
        Activity getActivityContext();
        
        void startActivityForResult(final Intent p0, final int p1);
    }
    
    public interface StatusCallback
    {
        void call(final Session p0, final SessionState p1, final Exception p2);
    }
    
    class TokenRefreshRequest implements ServiceConnection
    {
        final Messenger messageReceiver;
        Messenger messageSender;
        
        TokenRefreshRequest() {
            this.messageReceiver = new Messenger((Handler)new TokenRefreshRequestHandler(Session.this, this));
            this.messageSender = null;
        }
        
        private void cleanup() {
            if (Session.this.currentTokenRefreshRequest == this) {
                Session.this.currentTokenRefreshRequest = null;
            }
        }
        
        private void refreshToken() {
            final Bundle data = new Bundle();
            data.putString("access_token", Session.this.getTokenInfo().getToken());
            final Message obtain = Message.obtain();
            obtain.setData(data);
            obtain.replyTo = this.messageReceiver;
            try {
                this.messageSender.send(obtain);
            }
            catch (RemoteException ex) {
                this.cleanup();
            }
        }
        
        public void bind() {
            final Intent tokenRefreshIntent = NativeProtocol.createTokenRefreshIntent(Session.getStaticContext());
            if (tokenRefreshIntent != null && Session.staticContext.bindService(tokenRefreshIntent, (ServiceConnection)this, 1)) {
                Session.this.setLastAttemptedTokenExtendDate(new Date());
                return;
            }
            this.cleanup();
        }
        
        public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
            this.messageSender = new Messenger(binder);
            this.refreshToken();
        }
        
        public void onServiceDisconnected(final ComponentName componentName) {
            this.cleanup();
            try {
                Session.staticContext.unbindService((ServiceConnection)this);
            }
            catch (IllegalArgumentException ex) {}
        }
    }
    
    static class TokenRefreshRequestHandler extends Handler
    {
        private WeakReference<TokenRefreshRequest> refreshRequestWeakReference;
        private WeakReference<Session> sessionWeakReference;
        
        TokenRefreshRequestHandler(final Session session, final TokenRefreshRequest tokenRefreshRequest) {
            super(Looper.getMainLooper());
            this.sessionWeakReference = new WeakReference<Session>(session);
            this.refreshRequestWeakReference = new WeakReference<TokenRefreshRequest>(tokenRefreshRequest);
        }
        
        public void handleMessage(final Message message) {
            final String string = message.getData().getString("access_token");
            final Session session = this.sessionWeakReference.get();
            if (session != null && string != null) {
                session.extendTokenCompleted(message.getData());
            }
            final TokenRefreshRequest tokenRefreshRequest = this.refreshRequestWeakReference.get();
            if (tokenRefreshRequest != null) {
                Session.staticContext.unbindService((ServiceConnection)tokenRefreshRequest);
                tokenRefreshRequest.cleanup();
            }
        }
    }
}
