// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.widget;

import android.os.Bundle;
import com.facebook.AppEventsLogger;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.app.AlertDialog$Builder;
import android.util.Log;
import java.util.Collection;
import java.util.Collections;
import com.facebook.internal.SessionAuthorizationType;
import com.facebook.SessionState;
import java.util.Arrays;
import android.graphics.Canvas;
import android.app.Activity;
import android.content.Intent;
import com.facebook.FacebookException;
import java.util.List;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionDefaultAudience;
import android.content.res.TypedArray;
import com.facebook.Response;
import com.facebook.Request;
import android.view.View;
import android.os.AsyncTask;
import com.facebook.internal.Utility;
import android.graphics.Typeface;
import com.facebook.android.R;
import android.util.AttributeSet;
import android.content.Context;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.internal.SessionTracker;
import android.support.v4.app.Fragment;
import android.view.View$OnClickListener;
import android.widget.Button;

public class LoginButton extends Button
{
    private static final String TAG;
    private String applicationId;
    private boolean confirmLogout;
    private boolean fetchUserInfo;
    private View$OnClickListener listenerCallback;
    private String loginLogoutEventName;
    private String loginText;
    private String logoutText;
    private boolean nuxChecked;
    private long nuxDisplayTime;
    private ToolTipMode nuxMode;
    private ToolTipPopup nuxPopup;
    private ToolTipPopup.Style nuxStyle;
    private Fragment parentFragment;
    private LoginButtonProperties properties;
    private SessionTracker sessionTracker;
    private GraphUser user;
    private UserInfoChangedCallback userInfoChangedCallback;
    private Session userInfoSession;
    
    static {
        TAG = LoginButton.class.getName();
    }
    
    public LoginButton(final Context context) {
        super(context);
        this.applicationId = null;
        this.user = null;
        this.userInfoSession = null;
        this.properties = new LoginButtonProperties();
        this.loginLogoutEventName = "fb_login_view_usage";
        this.nuxStyle = ToolTipPopup.Style.BLUE;
        this.nuxMode = ToolTipMode.DEFAULT;
        this.nuxDisplayTime = 6000L;
        this.initializeActiveSessionWithCachedToken(context);
        this.finishInit();
    }
    
    public LoginButton(final Context context, final AttributeSet set) {
        super(context, set);
        this.applicationId = null;
        this.user = null;
        this.userInfoSession = null;
        this.properties = new LoginButtonProperties();
        this.loginLogoutEventName = "fb_login_view_usage";
        this.nuxStyle = ToolTipPopup.Style.BLUE;
        this.nuxMode = ToolTipMode.DEFAULT;
        this.nuxDisplayTime = 6000L;
        if (set.getStyleAttribute() == 0) {
            this.setGravity(17);
            this.setTextColor(this.getResources().getColor(R.color.com_facebook_loginview_text_color));
            this.setTextSize(0, this.getResources().getDimension(R.dimen.com_facebook_loginview_text_size));
            this.setTypeface(Typeface.DEFAULT_BOLD);
            if (this.isInEditMode()) {
                this.setBackgroundColor(this.getResources().getColor(R.color.com_facebook_blue));
                this.loginText = "Log in with Facebook";
            }
            else {
                this.setBackgroundResource(R.drawable.com_facebook_button_blue);
                this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_facebook_inverse_icon, 0, 0, 0);
                this.setCompoundDrawablePadding(this.getResources().getDimensionPixelSize(R.dimen.com_facebook_loginview_compound_drawable_padding));
                this.setPadding(this.getResources().getDimensionPixelSize(R.dimen.com_facebook_loginview_padding_left), this.getResources().getDimensionPixelSize(R.dimen.com_facebook_loginview_padding_top), this.getResources().getDimensionPixelSize(R.dimen.com_facebook_loginview_padding_right), this.getResources().getDimensionPixelSize(R.dimen.com_facebook_loginview_padding_bottom));
            }
        }
        this.parseAttributes(set);
        if (!this.isInEditMode()) {
            this.initializeActiveSessionWithCachedToken(context);
        }
    }
    
    public LoginButton(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.applicationId = null;
        this.user = null;
        this.userInfoSession = null;
        this.properties = new LoginButtonProperties();
        this.loginLogoutEventName = "fb_login_view_usage";
        this.nuxStyle = ToolTipPopup.Style.BLUE;
        this.nuxMode = ToolTipMode.DEFAULT;
        this.nuxDisplayTime = 6000L;
        this.parseAttributes(set);
        this.initializeActiveSessionWithCachedToken(context);
    }
    
    private void checkNuxSettings() {
        if (this.nuxMode == ToolTipMode.DISPLAY_ALWAYS) {
            this.displayNux(this.getResources().getString(R.string.com_facebook_tooltip_default));
            return;
        }
        new AsyncTask<Void, Void, Utility.FetchedAppSettings>() {
            final /* synthetic */ String val$appId = Utility.getMetadataApplicationId(LoginButton.this.getContext());
            
            protected Utility.FetchedAppSettings doInBackground(final Void... array) {
                return Utility.queryAppSettings(this.val$appId, false);
            }
            
            protected void onPostExecute(final Utility.FetchedAppSettings fetchedAppSettings) {
                LoginButton.this.showNuxPerSettings(fetchedAppSettings);
            }
        }.execute((Object[])null);
    }
    
    private void displayNux(final String s) {
        (this.nuxPopup = new ToolTipPopup(s, (View)this)).setStyle(this.nuxStyle);
        this.nuxPopup.setNuxDisplayTime(this.nuxDisplayTime);
        this.nuxPopup.show();
    }
    
    private void fetchUserInfo() {
        if (this.fetchUserInfo) {
            final Session openSession = this.sessionTracker.getOpenSession();
            if (openSession != null) {
                if (openSession != this.userInfoSession) {
                    Request.executeBatchAsync(Request.newMeRequest(openSession, (Request.GraphUserCallback)new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(final GraphUser graphUser, final Response response) {
                            if (openSession == LoginButton.this.sessionTracker.getOpenSession()) {
                                LoginButton.this.user = graphUser;
                                if (LoginButton.this.userInfoChangedCallback != null) {
                                    LoginButton.this.userInfoChangedCallback.onUserInfoFetched(LoginButton.this.user);
                                }
                            }
                            if (response.getError() != null) {
                                LoginButton.this.handleError(response.getError().getException());
                            }
                        }
                    }));
                    this.userInfoSession = openSession;
                }
            }
            else {
                this.user = null;
                if (this.userInfoChangedCallback != null) {
                    this.userInfoChangedCallback.onUserInfoFetched(this.user);
                }
            }
        }
    }
    
    private void finishInit() {
        super.setOnClickListener((View$OnClickListener)new LoginClickListener());
        this.setButtonText();
        if (!this.isInEditMode()) {
            this.sessionTracker = new SessionTracker(this.getContext(), new LoginButtonCallback(), null, false);
            this.fetchUserInfo();
        }
    }
    
    private boolean initializeActiveSessionWithCachedToken(final Context context) {
        if (context != null) {
            final Session activeSession = Session.getActiveSession();
            if (activeSession != null) {
                return activeSession.isOpened();
            }
            if (Utility.getMetadataApplicationId(context) != null && Session.openActiveSessionFromCache(context) != null) {
                return true;
            }
        }
        return false;
    }
    
    private void parseAttributes(final AttributeSet set) {
        final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes(set, R.styleable.com_facebook_login_view);
        this.confirmLogout = obtainStyledAttributes.getBoolean(R.styleable.com_facebook_login_view_confirm_logout, true);
        this.fetchUserInfo = obtainStyledAttributes.getBoolean(R.styleable.com_facebook_login_view_fetch_user_info, true);
        this.loginText = obtainStyledAttributes.getString(R.styleable.com_facebook_login_view_login_text);
        this.logoutText = obtainStyledAttributes.getString(R.styleable.com_facebook_login_view_logout_text);
        obtainStyledAttributes.recycle();
    }
    
    private void setButtonText() {
        if (this.sessionTracker != null && this.sessionTracker.getOpenSession() != null) {
            String text;
            if (this.logoutText != null) {
                text = this.logoutText;
            }
            else {
                text = this.getResources().getString(R.string.com_facebook_loginview_log_out_button);
            }
            this.setText((CharSequence)text);
            return;
        }
        String text2;
        if (this.loginText != null) {
            text2 = this.loginText;
        }
        else {
            text2 = this.getResources().getString(R.string.com_facebook_loginview_log_in_button);
        }
        this.setText((CharSequence)text2);
    }
    
    private void showNuxPerSettings(final Utility.FetchedAppSettings fetchedAppSettings) {
        if (fetchedAppSettings != null && fetchedAppSettings.getNuxEnabled() && this.getVisibility() == 0) {
            this.displayNux(fetchedAppSettings.getNuxContent());
        }
    }
    
    public void clearPermissions() {
        this.properties.clearPermissions();
    }
    
    public void dismissToolTip() {
        if (this.nuxPopup != null) {
            this.nuxPopup.dismiss();
            this.nuxPopup = null;
        }
    }
    
    public SessionDefaultAudience getDefaultAudience() {
        return this.properties.getDefaultAudience();
    }
    
    public SessionLoginBehavior getLoginBehavior() {
        return this.properties.getLoginBehavior();
    }
    
    public OnErrorListener getOnErrorListener() {
        return this.properties.getOnErrorListener();
    }
    
    List<String> getPermissions() {
        return this.properties.getPermissions();
    }
    
    public Session.StatusCallback getSessionStatusCallback() {
        return this.properties.getSessionStatusCallback();
    }
    
    public long getToolTipDisplayTime() {
        return this.nuxDisplayTime;
    }
    
    public ToolTipMode getToolTipMode() {
        return this.nuxMode;
    }
    
    public UserInfoChangedCallback getUserInfoChangedCallback() {
        return this.userInfoChangedCallback;
    }
    
    void handleError(final Exception ex) {
        if (this.properties.onErrorListener != null) {
            if (!(ex instanceof FacebookException)) {
                this.properties.onErrorListener.onError(new FacebookException(ex));
                return;
            }
            this.properties.onErrorListener.onError((FacebookException)ex);
        }
    }
    
    public boolean onActivityResult(final int n, final int n2, final Intent intent) {
        final Session session = this.sessionTracker.getSession();
        return session != null && session.onActivityResult((Activity)this.getContext(), n, n2, intent);
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.sessionTracker != null && !this.sessionTracker.isTracking()) {
            this.sessionTracker.startTracking();
            this.fetchUserInfo();
            this.setButtonText();
        }
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.sessionTracker != null) {
            this.sessionTracker.stopTracking();
        }
        this.dismissToolTip();
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (!this.nuxChecked && this.nuxMode != ToolTipMode.NEVER_DISPLAY && !this.isInEditMode()) {
            this.nuxChecked = true;
            this.checkNuxSettings();
        }
    }
    
    public void onFinishInflate() {
        super.onFinishInflate();
        this.finishInit();
    }
    
    protected void onVisibilityChanged(final View view, final int n) {
        super.onVisibilityChanged(view, n);
        if (n != 0) {
            this.dismissToolTip();
        }
    }
    
    public void setApplicationId(final String applicationId) {
        this.applicationId = applicationId;
    }
    
    public void setDefaultAudience(final SessionDefaultAudience defaultAudience) {
        this.properties.setDefaultAudience(defaultAudience);
    }
    
    public void setFragment(final Fragment parentFragment) {
        this.parentFragment = parentFragment;
    }
    
    public void setLoginBehavior(final SessionLoginBehavior loginBehavior) {
        this.properties.setLoginBehavior(loginBehavior);
    }
    
    void setLoginLogoutEventName(final String loginLogoutEventName) {
        this.loginLogoutEventName = loginLogoutEventName;
    }
    
    public void setOnClickListener(final View$OnClickListener listenerCallback) {
        this.listenerCallback = listenerCallback;
    }
    
    public void setOnErrorListener(final OnErrorListener onErrorListener) {
        this.properties.setOnErrorListener(onErrorListener);
    }
    
    void setProperties(final LoginButtonProperties properties) {
        this.properties = properties;
    }
    
    public void setPublishPermissions(final List<String> list) {
        this.properties.setPublishPermissions(list, this.sessionTracker.getSession());
    }
    
    public void setPublishPermissions(final String... array) {
        this.properties.setPublishPermissions(Arrays.asList(array), this.sessionTracker.getSession());
    }
    
    public void setReadPermissions(final List<String> list) {
        this.properties.setReadPermissions(list, this.sessionTracker.getSession());
    }
    
    public void setReadPermissions(final String... array) {
        this.properties.setReadPermissions(Arrays.asList(array), this.sessionTracker.getSession());
    }
    
    public void setSession(final Session session) {
        this.sessionTracker.setSession(session);
        this.fetchUserInfo();
        this.setButtonText();
    }
    
    public void setSessionStatusCallback(final Session.StatusCallback sessionStatusCallback) {
        this.properties.setSessionStatusCallback(sessionStatusCallback);
    }
    
    public void setToolTipDisplayTime(final long nuxDisplayTime) {
        this.nuxDisplayTime = nuxDisplayTime;
    }
    
    public void setToolTipMode(final ToolTipMode nuxMode) {
        this.nuxMode = nuxMode;
    }
    
    public void setToolTipStyle(final ToolTipPopup.Style nuxStyle) {
        this.nuxStyle = nuxStyle;
    }
    
    public void setUserInfoChangedCallback(final UserInfoChangedCallback userInfoChangedCallback) {
        this.userInfoChangedCallback = userInfoChangedCallback;
    }
    
    private class LoginButtonCallback implements StatusCallback
    {
        @Override
        public void call(final Session session, final SessionState sessionState, final Exception ex) {
            LoginButton.this.fetchUserInfo();
            LoginButton.this.setButtonText();
            if (LoginButton.this.properties.sessionStatusCallback != null) {
                LoginButton.this.properties.sessionStatusCallback.call(session, sessionState, ex);
            }
            else if (ex != null) {
                LoginButton.this.handleError(ex);
            }
        }
    }
    
    static class LoginButtonProperties
    {
        private SessionAuthorizationType authorizationType;
        private SessionDefaultAudience defaultAudience;
        private SessionLoginBehavior loginBehavior;
        private OnErrorListener onErrorListener;
        private List<String> permissions;
        private Session.StatusCallback sessionStatusCallback;
        
        LoginButtonProperties() {
            this.defaultAudience = SessionDefaultAudience.FRIENDS;
            this.permissions = Collections.emptyList();
            this.authorizationType = null;
            this.loginBehavior = SessionLoginBehavior.SSO_WITH_FALLBACK;
        }
        
        private boolean validatePermissions(final List<String> list, final SessionAuthorizationType sessionAuthorizationType, final Session session) {
            if (SessionAuthorizationType.PUBLISH.equals(sessionAuthorizationType) && Utility.isNullOrEmpty((Collection<Object>)list)) {
                throw new IllegalArgumentException("Permissions for publish actions cannot be null or empty.");
            }
            if (session != null && session.isOpened() && !Utility.isSubset(list, session.getPermissions())) {
                Log.e(LoginButton.TAG, "Cannot set additional permissions when session is already open.");
                return false;
            }
            return true;
        }
        
        public void clearPermissions() {
            this.permissions = null;
            this.authorizationType = null;
        }
        
        public SessionDefaultAudience getDefaultAudience() {
            return this.defaultAudience;
        }
        
        public SessionLoginBehavior getLoginBehavior() {
            return this.loginBehavior;
        }
        
        public OnErrorListener getOnErrorListener() {
            return this.onErrorListener;
        }
        
        List<String> getPermissions() {
            return this.permissions;
        }
        
        public Session.StatusCallback getSessionStatusCallback() {
            return this.sessionStatusCallback;
        }
        
        public void setDefaultAudience(final SessionDefaultAudience defaultAudience) {
            this.defaultAudience = defaultAudience;
        }
        
        public void setLoginBehavior(final SessionLoginBehavior loginBehavior) {
            this.loginBehavior = loginBehavior;
        }
        
        public void setOnErrorListener(final OnErrorListener onErrorListener) {
            this.onErrorListener = onErrorListener;
        }
        
        public void setPublishPermissions(final List<String> permissions, final Session session) {
            if (SessionAuthorizationType.READ.equals(this.authorizationType)) {
                throw new UnsupportedOperationException("Cannot call setPublishPermissions after setReadPermissions has been called.");
            }
            if (this.validatePermissions(permissions, SessionAuthorizationType.PUBLISH, session)) {
                this.permissions = permissions;
                this.authorizationType = SessionAuthorizationType.PUBLISH;
            }
        }
        
        public void setReadPermissions(final List<String> permissions, final Session session) {
            if (SessionAuthorizationType.PUBLISH.equals(this.authorizationType)) {
                throw new UnsupportedOperationException("Cannot call setReadPermissions after setPublishPermissions has been called.");
            }
            if (this.validatePermissions(permissions, SessionAuthorizationType.READ, session)) {
                this.permissions = permissions;
                this.authorizationType = SessionAuthorizationType.READ;
            }
        }
        
        public void setSessionStatusCallback(final Session.StatusCallback sessionStatusCallback) {
            this.sessionStatusCallback = sessionStatusCallback;
        }
    }
    
    private class LoginClickListener implements View$OnClickListener
    {
        public void onClick(final View view) {
            final Context context = LoginButton.this.getContext();
            final Session openSession = LoginButton.this.sessionTracker.getOpenSession();
            if (openSession != null) {
                if (LoginButton.this.confirmLogout) {
                    final String string = LoginButton.this.getResources().getString(R.string.com_facebook_loginview_log_out_action);
                    final String string2 = LoginButton.this.getResources().getString(R.string.com_facebook_loginview_cancel_action);
                    String message;
                    if (LoginButton.this.user != null && LoginButton.this.user.getName() != null) {
                        message = String.format(LoginButton.this.getResources().getString(R.string.com_facebook_loginview_logged_in_as), LoginButton.this.user.getName());
                    }
                    else {
                        message = LoginButton.this.getResources().getString(R.string.com_facebook_loginview_logged_in_using_facebook);
                    }
                    final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder(context);
                    alertDialog$Builder.setMessage((CharSequence)message).setCancelable(true).setPositiveButton((CharSequence)string, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            openSession.closeAndClearTokenInformation();
                        }
                    }).setNegativeButton((CharSequence)string2, (DialogInterface$OnClickListener)null);
                    alertDialog$Builder.create().show();
                }
                else {
                    openSession.closeAndClearTokenInformation();
                }
            }
            else {
                final Session session = LoginButton.this.sessionTracker.getSession();
                Session build = null;
                Label_0344: {
                    if (session != null) {
                        build = session;
                        if (!session.getState().isClosed()) {
                            break Label_0344;
                        }
                    }
                    LoginButton.this.sessionTracker.setSession(null);
                    build = new Session.Builder(context).setApplicationId(LoginButton.this.applicationId).build();
                    Session.setActiveSession(build);
                }
                if (!build.isOpened()) {
                    final Session.OpenRequest openRequest = null;
                    Session.OpenRequest openRequest2;
                    if (LoginButton.this.parentFragment != null) {
                        openRequest2 = new Session.OpenRequest(LoginButton.this.parentFragment);
                    }
                    else if (context instanceof Activity) {
                        openRequest2 = new Session.OpenRequest((Activity)context);
                    }
                    else {
                        openRequest2 = openRequest;
                        if (context instanceof ContextWrapper) {
                            final Context baseContext = ((ContextWrapper)context).getBaseContext();
                            openRequest2 = openRequest;
                            if (baseContext instanceof Activity) {
                                openRequest2 = new Session.OpenRequest((Activity)baseContext);
                            }
                        }
                    }
                    if (openRequest2 != null) {
                        openRequest2.setDefaultAudience(LoginButton.this.properties.defaultAudience);
                        openRequest2.setPermissions(LoginButton.this.properties.permissions);
                        openRequest2.setLoginBehavior(LoginButton.this.properties.loginBehavior);
                        if (SessionAuthorizationType.PUBLISH.equals(LoginButton.this.properties.authorizationType)) {
                            build.openForPublish(openRequest2);
                        }
                        else {
                            build.openForRead(openRequest2);
                        }
                    }
                }
            }
            final AppEventsLogger logger = AppEventsLogger.newLogger(LoginButton.this.getContext());
            final Bundle bundle = new Bundle();
            int n;
            if (openSession != null) {
                n = 0;
            }
            else {
                n = 1;
            }
            bundle.putInt("logging_in", n);
            logger.logSdkEvent(LoginButton.this.loginLogoutEventName, null, bundle);
            if (LoginButton.this.listenerCallback != null) {
                LoginButton.this.listenerCallback.onClick(view);
            }
        }
    }
    
    public interface OnErrorListener
    {
        void onError(final FacebookException p0);
    }
    
    public enum ToolTipMode
    {
        DEFAULT, 
        DISPLAY_ALWAYS, 
        NEVER_DISPLAY;
    }
    
    public interface UserInfoChangedCallback
    {
        void onUserInfoFetched(final GraphUser p0);
    }
}
