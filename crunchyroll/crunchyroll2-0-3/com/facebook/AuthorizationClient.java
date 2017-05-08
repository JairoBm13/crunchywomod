// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook;

import android.webkit.CookieSyncManager;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.ServerProtocol;
import com.facebook.internal.Utility;
import android.content.ActivityNotFoundException;
import com.facebook.internal.PlatformServiceClient;
import java.util.Iterator;
import android.text.TextUtils;
import com.facebook.widget.WebDialog;
import android.content.Intent;
import android.app.Activity;
import java.util.Collection;
import com.facebook.model.GraphUser;
import com.facebook.android.R;
import android.os.Bundle;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import android.content.Context;
import java.io.Serializable;

class AuthorizationClient implements Serializable
{
    static final String EVENT_EXTRAS_DEFAULT_AUDIENCE = "default_audience";
    static final String EVENT_EXTRAS_IS_LEGACY = "is_legacy";
    static final String EVENT_EXTRAS_LOGIN_BEHAVIOR = "login_behavior";
    static final String EVENT_EXTRAS_MISSING_INTERNET_PERMISSION = "no_internet_permission";
    static final String EVENT_EXTRAS_NEW_PERMISSIONS = "new_permissions";
    static final String EVENT_EXTRAS_NOT_TRIED = "not_tried";
    static final String EVENT_EXTRAS_PERMISSIONS = "permissions";
    static final String EVENT_EXTRAS_REQUEST_CODE = "request_code";
    static final String EVENT_EXTRAS_TRY_LEGACY = "try_legacy";
    static final String EVENT_EXTRAS_TRY_LOGIN_ACTIVITY = "try_login_activity";
    static final String EVENT_NAME_LOGIN_COMPLETE = "fb_mobile_login_complete";
    private static final String EVENT_NAME_LOGIN_METHOD_COMPLETE = "fb_mobile_login_method_complete";
    private static final String EVENT_NAME_LOGIN_METHOD_START = "fb_mobile_login_method_start";
    static final String EVENT_NAME_LOGIN_START = "fb_mobile_login_start";
    static final String EVENT_PARAM_AUTH_LOGGER_ID = "0_auth_logger_id";
    static final String EVENT_PARAM_ERROR_CODE = "4_error_code";
    static final String EVENT_PARAM_ERROR_MESSAGE = "5_error_message";
    static final String EVENT_PARAM_EXTRAS = "6_extras";
    static final String EVENT_PARAM_LOGIN_RESULT = "2_result";
    static final String EVENT_PARAM_METHOD = "3_method";
    private static final String EVENT_PARAM_METHOD_RESULT_SKIPPED = "skipped";
    static final String EVENT_PARAM_TIMESTAMP = "1_timestamp_ms";
    private static final String TAG = "Facebook-AuthorizationClient";
    private static final String WEB_VIEW_AUTH_HANDLER_STORE = "com.facebook.AuthorizationClient.WebViewAuthHandler.TOKEN_STORE_KEY";
    private static final String WEB_VIEW_AUTH_HANDLER_TOKEN_KEY = "TOKEN";
    private static final long serialVersionUID = 1L;
    private transient AppEventsLogger appEventsLogger;
    transient BackgroundProcessingListener backgroundProcessingListener;
    transient boolean checkedInternetPermission;
    transient Context context;
    AuthHandler currentHandler;
    List<AuthHandler> handlersToTry;
    Map<String, String> loggingExtras;
    transient OnCompletedListener onCompletedListener;
    AuthorizationRequest pendingRequest;
    transient StartActivityDelegate startActivityDelegate;
    
    private void addLoggingExtra(final String s, final String s2, final boolean b) {
        if (this.loggingExtras == null) {
            this.loggingExtras = new HashMap<String, String>();
        }
        String string = s2;
        if (this.loggingExtras.containsKey(s)) {
            string = s2;
            if (b) {
                string = this.loggingExtras.get(s) + "," + s2;
            }
        }
        this.loggingExtras.put(s, string);
    }
    
    private void completeWithFailure() {
        this.complete(Result.createErrorResult(this.pendingRequest, "Login attempt failed.", null));
    }
    
    private AppEventsLogger getAppEventsLogger() {
        if (this.appEventsLogger == null || !this.appEventsLogger.getApplicationId().equals(this.pendingRequest.getApplicationId())) {
            this.appEventsLogger = AppEventsLogger.newLogger(this.context, this.pendingRequest.getApplicationId());
        }
        return this.appEventsLogger;
    }
    
    private static String getE2E() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("init", System.currentTimeMillis());
            return jsonObject.toString();
        }
        catch (JSONException ex) {
            return jsonObject.toString();
        }
    }
    
    private List<AuthHandler> getHandlerTypes(final AuthorizationRequest authorizationRequest) {
        final ArrayList<GetTokenAuthHandler> list = (ArrayList<GetTokenAuthHandler>)new ArrayList<AuthHandler>();
        final SessionLoginBehavior loginBehavior = authorizationRequest.getLoginBehavior();
        if (loginBehavior.allowsKatanaAuth()) {
            if (!authorizationRequest.isLegacy()) {
                list.add(new GetTokenAuthHandler());
            }
            list.add((GetTokenAuthHandler)new KatanaProxyAuthHandler());
        }
        if (loginBehavior.allowsWebViewAuth()) {
            list.add((GetTokenAuthHandler)new WebViewAuthHandler());
        }
        return (List<AuthHandler>)list;
    }
    
    private void logAuthorizationMethodComplete(final String s, final Result result, final Map<String, String> map) {
        this.logAuthorizationMethodComplete(s, result.code.getLoggingValue(), result.errorMessage, result.errorCode, map);
    }
    
    private void logAuthorizationMethodComplete(final String s, final String s2, final String s3, final String s4, final Map<String, String> map) {
        Bundle authorizationLoggingBundle;
        if (this.pendingRequest == null) {
            authorizationLoggingBundle = newAuthorizationLoggingBundle("");
            authorizationLoggingBundle.putString("2_result", Code.ERROR.getLoggingValue());
            authorizationLoggingBundle.putString("5_error_message", "Unexpected call to logAuthorizationMethodComplete with null pendingRequest.");
        }
        else {
            final Bundle authorizationLoggingBundle2 = newAuthorizationLoggingBundle(this.pendingRequest.getAuthId());
            if (s2 != null) {
                authorizationLoggingBundle2.putString("2_result", s2);
            }
            if (s3 != null) {
                authorizationLoggingBundle2.putString("5_error_message", s3);
            }
            if (s4 != null) {
                authorizationLoggingBundle2.putString("4_error_code", s4);
            }
            authorizationLoggingBundle = authorizationLoggingBundle2;
            if (map != null) {
                authorizationLoggingBundle = authorizationLoggingBundle2;
                if (!map.isEmpty()) {
                    authorizationLoggingBundle2.putString("6_extras", new JSONObject((Map)map).toString());
                    authorizationLoggingBundle = authorizationLoggingBundle2;
                }
            }
        }
        authorizationLoggingBundle.putString("3_method", s);
        authorizationLoggingBundle.putLong("1_timestamp_ms", System.currentTimeMillis());
        this.getAppEventsLogger().logSdkEvent("fb_mobile_login_method_complete", null, authorizationLoggingBundle);
    }
    
    private void logAuthorizationMethodStart(final String s) {
        final Bundle authorizationLoggingBundle = newAuthorizationLoggingBundle(this.pendingRequest.getAuthId());
        authorizationLoggingBundle.putLong("1_timestamp_ms", System.currentTimeMillis());
        authorizationLoggingBundle.putString("3_method", s);
        this.getAppEventsLogger().logSdkEvent("fb_mobile_login_method_start", null, authorizationLoggingBundle);
    }
    
    private void logWebLoginCompleted(final String s, final String s2) {
        final AppEventsLogger logger = AppEventsLogger.newLogger(this.context, s);
        final Bundle bundle = new Bundle();
        bundle.putString("fb_web_login_e2e", s2);
        bundle.putLong("fb_web_login_switchback_time", System.currentTimeMillis());
        bundle.putString("app_id", s);
        logger.logSdkEvent("fb_dialogs_web_login_dialog_complete", null, bundle);
    }
    
    static Bundle newAuthorizationLoggingBundle(final String s) {
        final Bundle bundle = new Bundle();
        bundle.putLong("1_timestamp_ms", System.currentTimeMillis());
        bundle.putString("0_auth_logger_id", s);
        bundle.putString("3_method", "");
        bundle.putString("2_result", "");
        bundle.putString("5_error_message", "");
        bundle.putString("4_error_code", "");
        bundle.putString("6_extras", "");
        return bundle;
    }
    
    private void notifyBackgroundProcessingStart() {
        if (this.backgroundProcessingListener != null) {
            this.backgroundProcessingListener.onBackgroundProcessingStarted();
        }
    }
    
    private void notifyBackgroundProcessingStop() {
        if (this.backgroundProcessingListener != null) {
            this.backgroundProcessingListener.onBackgroundProcessingStopped();
        }
    }
    
    private void notifyOnCompleteListener(final Result result) {
        if (this.onCompletedListener != null) {
            this.onCompletedListener.onCompleted(result);
        }
    }
    
    void authorize(final AuthorizationRequest pendingRequest) {
        if (pendingRequest != null) {
            if (this.pendingRequest != null) {
                throw new FacebookException("Attempted to authorize while a request is pending.");
            }
            if (!pendingRequest.needsNewTokenValidation() || this.checkInternetPermission()) {
                this.pendingRequest = pendingRequest;
                this.handlersToTry = this.getHandlerTypes(pendingRequest);
                this.tryNextHandler();
            }
        }
    }
    
    void cancelCurrentHandler() {
        if (this.currentHandler != null) {
            this.currentHandler.cancel();
        }
    }
    
    boolean checkInternetPermission() {
        if (this.checkedInternetPermission) {
            return true;
        }
        if (this.checkPermission("android.permission.INTERNET") != 0) {
            this.complete(Result.createErrorResult(this.pendingRequest, this.context.getString(R.string.com_facebook_internet_permission_error_title), this.context.getString(R.string.com_facebook_internet_permission_error_message)));
            return false;
        }
        return this.checkedInternetPermission = true;
    }
    
    int checkPermission(final String s) {
        return this.context.checkCallingOrSelfPermission(s);
    }
    
    void complete(final Result result) {
        if (this.currentHandler != null) {
            this.logAuthorizationMethodComplete(this.currentHandler.getNameForLogging(), result, this.currentHandler.methodLoggingExtras);
        }
        if (this.loggingExtras != null) {
            result.loggingExtras = this.loggingExtras;
        }
        this.handlersToTry = null;
        this.currentHandler = null;
        this.pendingRequest = null;
        this.loggingExtras = null;
        this.notifyOnCompleteListener(result);
    }
    
    void completeAndValidate(final Result result) {
        if (result.token != null && this.pendingRequest.needsNewTokenValidation()) {
            this.validateSameFbidAndFinish(result);
            return;
        }
        this.complete(result);
    }
    
    void continueAuth() {
        if (this.pendingRequest == null || this.currentHandler == null) {
            throw new FacebookException("Attempted to continue authorization without a pending request.");
        }
        if (this.currentHandler.needsRestart()) {
            this.currentHandler.cancel();
            this.tryCurrentHandler();
        }
    }
    
    Request createGetPermissionsRequest(final String s) {
        final Bundle bundle = new Bundle();
        bundle.putString("access_token", s);
        return new Request(null, "me/permissions", bundle, HttpMethod.GET, null);
    }
    
    Request createGetProfileIdRequest(final String s) {
        final Bundle bundle = new Bundle();
        bundle.putString("fields", "id");
        bundle.putString("access_token", s);
        return new Request(null, "me", bundle, HttpMethod.GET, null);
    }
    
    RequestBatch createReauthValidationBatch(final Result result) {
        final ArrayList list = new ArrayList();
        final ArrayList list2 = new ArrayList();
        final ArrayList list3 = new ArrayList();
        final String token = result.token.getToken();
        final Request.Callback callback = new Request.Callback() {
            @Override
            public void onCompleted(final Response response) {
                try {
                    final GraphUser graphUser = response.getGraphObjectAs(GraphUser.class);
                    if (graphUser != null) {
                        list.add(graphUser.getId());
                    }
                }
                catch (Exception ex) {}
            }
        };
        final String previousAccessToken = this.pendingRequest.getPreviousAccessToken();
        final Request getProfileIdRequest = this.createGetProfileIdRequest(previousAccessToken);
        getProfileIdRequest.setCallback((Request.Callback)callback);
        final Request getProfileIdRequest2 = this.createGetProfileIdRequest(token);
        getProfileIdRequest2.setCallback((Request.Callback)callback);
        final Request getPermissionsRequest = this.createGetPermissionsRequest(previousAccessToken);
        getPermissionsRequest.setCallback((Request.Callback)new Request.Callback() {
            @Override
            public void onCompleted(final Response response) {
                try {
                    final Session.PermissionsPair handlePermissionResponse = Session.handlePermissionResponse(response);
                    if (handlePermissionResponse != null) {
                        list2.addAll(handlePermissionResponse.getGrantedPermissions());
                        list3.addAll(handlePermissionResponse.getDeclinedPermissions());
                    }
                }
                catch (Exception ex) {}
            }
        });
        final RequestBatch requestBatch = new RequestBatch(new Request[] { getProfileIdRequest, getProfileIdRequest2, getPermissionsRequest });
        requestBatch.setBatchApplicationId(this.pendingRequest.getApplicationId());
        requestBatch.addCallback((RequestBatch.Callback)new RequestBatch.Callback() {
            @Override
            public void onBatchCompleted(final RequestBatch requestBatch) {
                try {
                    Result result;
                    if (list.size() == 2 && list.get(0) != null && list.get(1) != null && list.get(0).equals(list.get(1))) {
                        result = Result.createTokenResult(AuthorizationClient.this.pendingRequest, AccessToken.createFromTokenWithRefreshedPermissions(result.token, list2, list3));
                    }
                    else {
                        result = Result.createErrorResult(AuthorizationClient.this.pendingRequest, "User logged in as different Facebook user.", null);
                    }
                    AuthorizationClient.this.complete(result);
                }
                catch (Exception ex) {
                    AuthorizationClient.this.complete(Result.createErrorResult(AuthorizationClient.this.pendingRequest, "Caught exception", ex.getMessage()));
                }
                finally {
                    AuthorizationClient.this.notifyBackgroundProcessingStop();
                }
            }
        });
        return requestBatch;
    }
    
    BackgroundProcessingListener getBackgroundProcessingListener() {
        return this.backgroundProcessingListener;
    }
    
    boolean getInProgress() {
        return this.pendingRequest != null && this.currentHandler != null;
    }
    
    OnCompletedListener getOnCompletedListener() {
        return this.onCompletedListener;
    }
    
    StartActivityDelegate getStartActivityDelegate() {
        if (this.startActivityDelegate != null) {
            return this.startActivityDelegate;
        }
        if (this.pendingRequest != null) {
            return (StartActivityDelegate)new StartActivityDelegate() {
                @Override
                public Activity getActivityContext() {
                    return AuthorizationClient.this.pendingRequest.getStartActivityDelegate().getActivityContext();
                }
                
                @Override
                public void startActivityForResult(final Intent intent, final int n) {
                    AuthorizationClient.this.pendingRequest.getStartActivityDelegate().startActivityForResult(intent, n);
                }
            };
        }
        return null;
    }
    
    boolean onActivityResult(final int n, final int n2, final Intent intent) {
        return this.pendingRequest != null && n == this.pendingRequest.getRequestCode() && this.currentHandler.onActivityResult(n, n2, intent);
    }
    
    void setBackgroundProcessingListener(final BackgroundProcessingListener backgroundProcessingListener) {
        this.backgroundProcessingListener = backgroundProcessingListener;
    }
    
    void setContext(final Activity context) {
        this.context = (Context)context;
        this.startActivityDelegate = (StartActivityDelegate)new StartActivityDelegate() {
            @Override
            public Activity getActivityContext() {
                return context;
            }
            
            @Override
            public void startActivityForResult(final Intent intent, final int n) {
                context.startActivityForResult(intent, n);
            }
        };
    }
    
    void setContext(final Context context) {
        this.context = context;
        this.startActivityDelegate = null;
    }
    
    void setOnCompletedListener(final OnCompletedListener onCompletedListener) {
        this.onCompletedListener = onCompletedListener;
    }
    
    void startOrContinueAuth(final AuthorizationRequest authorizationRequest) {
        if (this.getInProgress()) {
            this.continueAuth();
            return;
        }
        this.authorize(authorizationRequest);
    }
    
    boolean tryCurrentHandler() {
        if (this.currentHandler.needsInternetPermission() && !this.checkInternetPermission()) {
            this.addLoggingExtra("no_internet_permission", "1", false);
            return false;
        }
        final boolean tryAuthorize = this.currentHandler.tryAuthorize(this.pendingRequest);
        if (tryAuthorize) {
            this.logAuthorizationMethodStart(this.currentHandler.getNameForLogging());
            return tryAuthorize;
        }
        this.addLoggingExtra("not_tried", this.currentHandler.getNameForLogging(), true);
        return tryAuthorize;
    }
    
    void tryNextHandler() {
        if (this.currentHandler != null) {
            this.logAuthorizationMethodComplete(this.currentHandler.getNameForLogging(), "skipped", null, null, this.currentHandler.methodLoggingExtras);
        }
        while (this.handlersToTry != null && !this.handlersToTry.isEmpty()) {
            this.currentHandler = this.handlersToTry.remove(0);
            if (this.tryCurrentHandler()) {
                return;
            }
        }
        if (this.pendingRequest != null) {
            this.completeWithFailure();
        }
    }
    
    void validateSameFbidAndFinish(final Result result) {
        if (result.token == null) {
            throw new FacebookException("Can't validate without a token");
        }
        final RequestBatch reauthValidationBatch = this.createReauthValidationBatch(result);
        this.notifyBackgroundProcessingStart();
        reauthValidationBatch.executeAsync();
    }
    
    static class AuthDialogBuilder extends Builder
    {
        private static final String OAUTH_DIALOG = "oauth";
        static final String REDIRECT_URI = "fbconnect://success";
        private String e2e;
        private boolean isRerequest;
        
        public AuthDialogBuilder(final Context context, final String s, final Bundle bundle) {
            super(context, s, "oauth", bundle);
        }
        
        @Override
        public WebDialog build() {
            final Bundle parameters = ((WebDialog.BuilderBase)this).getParameters();
            parameters.putString("redirect_uri", "fbconnect://success");
            parameters.putString("client_id", ((WebDialog.BuilderBase)this).getApplicationId());
            parameters.putString("e2e", this.e2e);
            parameters.putString("response_type", "token");
            parameters.putString("return_scopes", "true");
            if (this.isRerequest && !Settings.getPlatformCompatibilityEnabled()) {
                parameters.putString("auth_type", "rerequest");
            }
            return new WebDialog(((WebDialog.BuilderBase)this).getContext(), "oauth", parameters, ((WebDialog.BuilderBase)this).getTheme(), ((WebDialog.BuilderBase)this).getListener());
        }
        
        public AuthDialogBuilder setE2E(final String e2e) {
            this.e2e = e2e;
            return this;
        }
        
        public AuthDialogBuilder setIsRerequest(final boolean isRerequest) {
            this.isRerequest = isRerequest;
            return this;
        }
    }
    
    abstract class AuthHandler implements Serializable
    {
        private static final long serialVersionUID = 1L;
        Map<String, String> methodLoggingExtras;
        
        protected void addLoggingExtra(final String s, final Object o) {
            if (this.methodLoggingExtras == null) {
                this.methodLoggingExtras = new HashMap<String, String>();
            }
            final Map<String, String> methodLoggingExtras = this.methodLoggingExtras;
            String string;
            if (o == null) {
                string = null;
            }
            else {
                string = o.toString();
            }
            methodLoggingExtras.put(s, string);
        }
        
        void cancel() {
        }
        
        abstract String getNameForLogging();
        
        boolean needsInternetPermission() {
            return false;
        }
        
        boolean needsRestart() {
            return false;
        }
        
        boolean onActivityResult(final int n, final int n2, final Intent intent) {
            return false;
        }
        
        abstract boolean tryAuthorize(final AuthorizationRequest p0);
    }
    
    static class AuthorizationRequest implements Serializable
    {
        private static final long serialVersionUID = 1L;
        private final String applicationId;
        private final String authId;
        private final SessionDefaultAudience defaultAudience;
        private boolean isLegacy;
        private boolean isRerequest;
        private final SessionLoginBehavior loginBehavior;
        private List<String> permissions;
        private final String previousAccessToken;
        private final int requestCode;
        private final transient StartActivityDelegate startActivityDelegate;
        
        AuthorizationRequest(final SessionLoginBehavior loginBehavior, final int requestCode, final boolean isLegacy, final List<String> permissions, final SessionDefaultAudience defaultAudience, final String applicationId, final String previousAccessToken, final StartActivityDelegate startActivityDelegate, final String authId) {
            this.isLegacy = false;
            this.isRerequest = false;
            this.loginBehavior = loginBehavior;
            this.requestCode = requestCode;
            this.isLegacy = isLegacy;
            this.permissions = permissions;
            this.defaultAudience = defaultAudience;
            this.applicationId = applicationId;
            this.previousAccessToken = previousAccessToken;
            this.startActivityDelegate = startActivityDelegate;
            this.authId = authId;
        }
        
        String getApplicationId() {
            return this.applicationId;
        }
        
        String getAuthId() {
            return this.authId;
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
        
        String getPreviousAccessToken() {
            return this.previousAccessToken;
        }
        
        int getRequestCode() {
            return this.requestCode;
        }
        
        StartActivityDelegate getStartActivityDelegate() {
            return this.startActivityDelegate;
        }
        
        boolean isLegacy() {
            return this.isLegacy;
        }
        
        boolean isRerequest() {
            return this.isRerequest;
        }
        
        boolean needsNewTokenValidation() {
            return this.previousAccessToken != null && !this.isLegacy;
        }
        
        void setIsLegacy(final boolean isLegacy) {
            this.isLegacy = isLegacy;
        }
        
        void setPermissions(final List<String> permissions) {
            this.permissions = permissions;
        }
        
        void setRerequest(final boolean isRerequest) {
            this.isRerequest = isRerequest;
        }
    }
    
    interface BackgroundProcessingListener
    {
        void onBackgroundProcessingStarted();
        
        void onBackgroundProcessingStopped();
    }
    
    class GetTokenAuthHandler extends AuthHandler
    {
        private static final long serialVersionUID = 1L;
        private transient GetTokenClient getTokenClient;
        
        @Override
        void cancel() {
            if (this.getTokenClient != null) {
                this.getTokenClient.cancel();
                this.getTokenClient = null;
            }
        }
        
        @Override
        String getNameForLogging() {
            return "get_token";
        }
        
        void getTokenCompleted(final AuthorizationRequest authorizationRequest, final Bundle bundle) {
            this.getTokenClient = null;
            AuthorizationClient.this.notifyBackgroundProcessingStop();
            if (bundle != null) {
                final ArrayList stringArrayList = bundle.getStringArrayList("com.facebook.platform.extra.PERMISSIONS");
                final List<String> permissions = authorizationRequest.getPermissions();
                if (stringArrayList != null && (permissions == null || stringArrayList.containsAll(permissions))) {
                    AuthorizationClient.this.completeAndValidate(Result.createTokenResult(AuthorizationClient.this.pendingRequest, AccessToken.createFromNativeLogin(bundle, AccessTokenSource.FACEBOOK_APPLICATION_SERVICE)));
                    return;
                }
                final ArrayList<String> permissions2 = new ArrayList<String>();
                for (final String s : permissions) {
                    if (!stringArrayList.contains(s)) {
                        permissions2.add(s);
                    }
                }
                if (!permissions2.isEmpty()) {
                    ((AuthHandler)this).addLoggingExtra("new_permissions", TextUtils.join((CharSequence)",", (Iterable)permissions2));
                }
                authorizationRequest.setPermissions(permissions2);
            }
            AuthorizationClient.this.tryNextHandler();
        }
        
        @Override
        boolean needsRestart() {
            return this.getTokenClient == null;
        }
        
        @Override
        boolean tryAuthorize(final AuthorizationRequest authorizationRequest) {
            this.getTokenClient = new GetTokenClient(AuthorizationClient.this.context, authorizationRequest.getApplicationId());
            if (!this.getTokenClient.start()) {
                return false;
            }
            AuthorizationClient.this.notifyBackgroundProcessingStart();
            this.getTokenClient.setCompletedListener((PlatformServiceClient.CompletedListener)new PlatformServiceClient.CompletedListener() {
                @Override
                public void completed(final Bundle bundle) {
                    GetTokenAuthHandler.this.getTokenCompleted(authorizationRequest, bundle);
                }
            });
            return true;
        }
    }
    
    abstract class KatanaAuthHandler extends AuthHandler
    {
        private static final long serialVersionUID = 1L;
        
        protected boolean tryIntent(final Intent intent, final int n) {
            if (intent == null) {
                return false;
            }
            try {
                AuthorizationClient.this.getStartActivityDelegate().startActivityForResult(intent, n);
                return true;
            }
            catch (ActivityNotFoundException ex) {
                return false;
            }
        }
    }
    
    class KatanaProxyAuthHandler extends KatanaAuthHandler
    {
        private static final long serialVersionUID = 1L;
        private String applicationId;
        
        private Result handleResultOk(final Intent intent) {
            Serializable tokenResult = null;
            final Bundle extras = intent.getExtras();
            String s;
            if ((s = extras.getString("error")) == null) {
                s = extras.getString("error_type");
            }
            final String string = extras.getString("error_code");
            String s2;
            if ((s2 = extras.getString("error_message")) == null) {
                s2 = extras.getString("error_description");
            }
            final String string2 = extras.getString("e2e");
            if (!Utility.isNullOrEmpty(string2)) {
                AuthorizationClient.this.logWebLoginCompleted(this.applicationId, string2);
            }
            if (s == null && string == null && s2 == null) {
                tokenResult = Result.createTokenResult(AuthorizationClient.this.pendingRequest, AccessToken.createFromWebBundle(AuthorizationClient.this.pendingRequest.getPermissions(), extras, AccessTokenSource.FACEBOOK_APPLICATION_WEB));
            }
            else if (!ServerProtocol.errorsProxyAuthDisabled.contains(s)) {
                if (ServerProtocol.errorsUserCanceled.contains(s)) {
                    return Result.createCancelResult(AuthorizationClient.this.pendingRequest, null);
                }
                return Result.createErrorResult(AuthorizationClient.this.pendingRequest, s, s2, string);
            }
            return (Result)tokenResult;
        }
        
        @Override
        String getNameForLogging() {
            return "katana_proxy_auth";
        }
        
        @Override
        boolean onActivityResult(final int n, final int n2, final Intent intent) {
            Serializable s;
            if (intent == null) {
                s = Result.createCancelResult(AuthorizationClient.this.pendingRequest, "Operation canceled");
            }
            else if (n2 == 0) {
                s = Result.createCancelResult(AuthorizationClient.this.pendingRequest, intent.getStringExtra("error"));
            }
            else if (n2 != -1) {
                s = Result.createErrorResult(AuthorizationClient.this.pendingRequest, "Unexpected resultCode from authorization.", null);
            }
            else {
                s = this.handleResultOk(intent);
            }
            if (s != null) {
                AuthorizationClient.this.completeAndValidate((Result)s);
            }
            else {
                AuthorizationClient.this.tryNextHandler();
            }
            return true;
        }
        
        @Override
        boolean tryAuthorize(final AuthorizationRequest authorizationRequest) {
            this.applicationId = authorizationRequest.getApplicationId();
            final String access$100 = getE2E();
            final Intent proxyAuthIntent = NativeProtocol.createProxyAuthIntent(AuthorizationClient.this.context, authorizationRequest.getApplicationId(), authorizationRequest.getPermissions(), access$100, authorizationRequest.isRerequest(), authorizationRequest.getDefaultAudience());
            ((AuthHandler)this).addLoggingExtra("e2e", access$100);
            return ((KatanaAuthHandler)this).tryIntent(proxyAuthIntent, authorizationRequest.getRequestCode());
        }
    }
    
    interface OnCompletedListener
    {
        void onCompleted(final Result p0);
    }
    
    static class Result implements Serializable
    {
        private static final long serialVersionUID = 1L;
        final Code code;
        final String errorCode;
        final String errorMessage;
        Map<String, String> loggingExtras;
        final AuthorizationRequest request;
        final AccessToken token;
        
        private Result(final AuthorizationRequest request, final Code code, final AccessToken token, final String errorMessage, final String errorCode) {
            this.request = request;
            this.token = token;
            this.errorMessage = errorMessage;
            this.code = code;
            this.errorCode = errorCode;
        }
        
        static Result createCancelResult(final AuthorizationRequest authorizationRequest, final String s) {
            return new Result(authorizationRequest, Code.CANCEL, null, s, null);
        }
        
        static Result createErrorResult(final AuthorizationRequest authorizationRequest, final String s, final String s2) {
            return createErrorResult(authorizationRequest, s, s2, null);
        }
        
        static Result createErrorResult(final AuthorizationRequest authorizationRequest, String join, final String s, final String s2) {
            join = TextUtils.join((CharSequence)": ", (Iterable)Utility.asListNoNulls(join, s));
            return new Result(authorizationRequest, Code.ERROR, null, join, s2);
        }
        
        static Result createTokenResult(final AuthorizationRequest authorizationRequest, final AccessToken accessToken) {
            return new Result(authorizationRequest, Code.SUCCESS, accessToken, null, null);
        }
        
        enum Code
        {
            CANCEL("cancel"), 
            ERROR("error"), 
            SUCCESS("success");
            
            private final String loggingValue;
            
            private Code(final String loggingValue) {
                this.loggingValue = loggingValue;
            }
            
            String getLoggingValue() {
                return this.loggingValue;
            }
        }
    }
    
    interface StartActivityDelegate
    {
        Activity getActivityContext();
        
        void startActivityForResult(final Intent p0, final int p1);
    }
    
    class WebViewAuthHandler extends AuthHandler
    {
        private static final long serialVersionUID = 1L;
        private String applicationId;
        private String e2e;
        private transient WebDialog loginDialog;
        
        private String loadCookieToken() {
            return ((Context)AuthorizationClient.this.getStartActivityDelegate().getActivityContext()).getSharedPreferences("com.facebook.AuthorizationClient.WebViewAuthHandler.TOKEN_STORE_KEY", 0).getString("TOKEN", "");
        }
        
        private void saveCookieToken(final String s) {
            ((Context)AuthorizationClient.this.getStartActivityDelegate().getActivityContext()).getSharedPreferences("com.facebook.AuthorizationClient.WebViewAuthHandler.TOKEN_STORE_KEY", 0).edit().putString("TOKEN", s).apply();
        }
        
        @Override
        void cancel() {
            if (this.loginDialog != null) {
                this.loginDialog.setOnCompleteListener(null);
                this.loginDialog.dismiss();
                this.loginDialog = null;
            }
        }
        
        @Override
        String getNameForLogging() {
            return "web_view";
        }
        
        @Override
        boolean needsInternetPermission() {
            return true;
        }
        
        @Override
        boolean needsRestart() {
            return true;
        }
        
        void onWebDialogComplete(final AuthorizationRequest authorizationRequest, final Bundle bundle, final FacebookException ex) {
            Result result;
            if (bundle != null) {
                if (bundle.containsKey("e2e")) {
                    this.e2e = bundle.getString("e2e");
                }
                final AccessToken fromWebBundle = AccessToken.createFromWebBundle(authorizationRequest.getPermissions(), bundle, AccessTokenSource.WEB_VIEW);
                result = Result.createTokenResult(AuthorizationClient.this.pendingRequest, fromWebBundle);
                CookieSyncManager.createInstance(AuthorizationClient.this.context).sync();
                this.saveCookieToken(fromWebBundle.getToken());
            }
            else if (ex instanceof FacebookOperationCanceledException) {
                result = Result.createCancelResult(AuthorizationClient.this.pendingRequest, "User canceled log in.");
            }
            else {
                this.e2e = null;
                String format = null;
                String s = ex.getMessage();
                if (ex instanceof FacebookServiceException) {
                    final FacebookRequestError requestError = ((FacebookServiceException)ex).getRequestError();
                    format = String.format("%d", requestError.getErrorCode());
                    s = requestError.toString();
                }
                result = Result.createErrorResult(AuthorizationClient.this.pendingRequest, null, s, format);
            }
            if (!Utility.isNullOrEmpty(this.e2e)) {
                AuthorizationClient.this.logWebLoginCompleted(this.applicationId, this.e2e);
            }
            AuthorizationClient.this.completeAndValidate(result);
        }
        
        @Override
        boolean tryAuthorize(final AuthorizationRequest authorizationRequest) {
            this.applicationId = authorizationRequest.getApplicationId();
            final Bundle bundle = new Bundle();
            if (!Utility.isNullOrEmpty(authorizationRequest.getPermissions())) {
                final String join = TextUtils.join((CharSequence)",", (Iterable)authorizationRequest.getPermissions());
                bundle.putString("scope", join);
                ((AuthHandler)this).addLoggingExtra("scope", join);
            }
            bundle.putString("default_audience", authorizationRequest.getDefaultAudience().getNativeProtocolAudience());
            final String previousAccessToken = authorizationRequest.getPreviousAccessToken();
            if (!Utility.isNullOrEmpty(previousAccessToken) && previousAccessToken.equals(this.loadCookieToken())) {
                bundle.putString("access_token", previousAccessToken);
                ((AuthHandler)this).addLoggingExtra("access_token", "1");
            }
            else {
                Utility.clearFacebookCookies(AuthorizationClient.this.context);
                ((AuthHandler)this).addLoggingExtra("access_token", "0");
            }
            final WebDialog.OnCompleteListener onCompleteListener = new WebDialog.OnCompleteListener() {
                @Override
                public void onComplete(final Bundle bundle, final FacebookException ex) {
                    WebViewAuthHandler.this.onWebDialogComplete(authorizationRequest, bundle, ex);
                }
            };
            ((AuthHandler)this).addLoggingExtra("e2e", this.e2e = getE2E());
            (this.loginDialog = ((WebDialog.Builder)new AuthDialogBuilder((Context)AuthorizationClient.this.getStartActivityDelegate().getActivityContext(), this.applicationId, bundle).setE2E(this.e2e).setIsRerequest(authorizationRequest.isRerequest()).setOnCompleteListener(onCompleteListener)).build()).show();
            return true;
        }
    }
}
