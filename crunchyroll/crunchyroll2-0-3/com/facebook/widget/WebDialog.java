// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.widget;

import android.content.Intent;
import com.facebook.FacebookServiceException;
import com.facebook.FacebookRequestError;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import com.facebook.FacebookDialogException;
import android.graphics.Bitmap;
import com.facebook.internal.Validate;
import com.facebook.Session;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import android.net.Uri;
import android.content.DialogInterface;
import android.content.DialogInterface$OnCancelListener;
import android.annotation.SuppressLint;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import com.facebook.android.R;
import android.view.View;
import android.view.View$OnClickListener;
import android.view.Display;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.facebook.internal.Utility;
import com.facebook.internal.ServerProtocol;
import android.os.Bundle;
import android.content.Context;
import android.webkit.WebView;
import android.app.ProgressDialog;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.app.Dialog;

public class WebDialog extends Dialog
{
    private static final int API_EC_DIALOG_CANCEL = 4201;
    private static final int BACKGROUND_GRAY = -872415232;
    static final String CANCEL_URI = "fbconnect://cancel";
    public static final int DEFAULT_THEME = 16973840;
    static final boolean DISABLE_SSL_CHECK_FOR_TESTING = false;
    private static final String DISPLAY_TOUCH = "touch";
    private static final String LOG_TAG = "FacebookSDK.WebDialog";
    private static final int MAX_PADDING_SCREEN_HEIGHT = 1280;
    private static final int MAX_PADDING_SCREEN_WIDTH = 800;
    private static final double MIN_SCALE_FACTOR = 0.5;
    private static final int NO_PADDING_SCREEN_HEIGHT = 800;
    private static final int NO_PADDING_SCREEN_WIDTH = 480;
    static final String REDIRECT_URI = "fbconnect://success";
    private FrameLayout contentFrameLayout;
    private ImageView crossImageView;
    private String expectedRedirectUrl;
    private boolean isDetached;
    private boolean isDismissed;
    private boolean listenerCalled;
    private OnCompleteListener onCompleteListener;
    private ProgressDialog spinner;
    private String url;
    private WebView webView;
    
    public WebDialog(final Context context, final String s) {
        this(context, s, 16973840);
    }
    
    public WebDialog(final Context context, final String url, final int n) {
        super(context, n);
        this.expectedRedirectUrl = "fbconnect://success";
        this.listenerCalled = false;
        this.isDetached = false;
        this.isDismissed = false;
        this.url = url;
    }
    
    public WebDialog(final Context context, final String s, final Bundle bundle, final int n, final OnCompleteListener onCompleteListener) {
        super(context, n);
        this.expectedRedirectUrl = "fbconnect://success";
        this.listenerCalled = false;
        this.isDetached = false;
        this.isDismissed = false;
        Bundle bundle2 = bundle;
        if (bundle == null) {
            bundle2 = new Bundle();
        }
        bundle2.putString("redirect_uri", "fbconnect://success");
        bundle2.putString("display", "touch");
        this.url = Utility.buildUri(ServerProtocol.getDialogAuthority(), ServerProtocol.getAPIVersion() + "/" + "dialog/" + s, bundle2).toString();
        this.onCompleteListener = onCompleteListener;
    }
    
    private void calculateSize() {
        final Display defaultDisplay = ((WindowManager)this.getContext().getSystemService("window")).getDefaultDisplay();
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        int n;
        if (displayMetrics.widthPixels < displayMetrics.heightPixels) {
            n = displayMetrics.widthPixels;
        }
        else {
            n = displayMetrics.heightPixels;
        }
        int n2;
        if (displayMetrics.widthPixels < displayMetrics.heightPixels) {
            n2 = displayMetrics.heightPixels;
        }
        else {
            n2 = displayMetrics.widthPixels;
        }
        this.getWindow().setLayout(Math.min(this.getScaledSize(n, displayMetrics.density, 480, 800), displayMetrics.widthPixels), Math.min(this.getScaledSize(n2, displayMetrics.density, 800, 1280), displayMetrics.heightPixels));
    }
    
    private void createCrossImage() {
        (this.crossImageView = new ImageView(this.getContext())).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                WebDialog.this.dismiss();
            }
        });
        this.crossImageView.setImageDrawable(this.getContext().getResources().getDrawable(R.drawable.com_facebook_close));
        this.crossImageView.setVisibility(4);
    }
    
    private int getScaledSize(final int n, final float n2, final int n3, final int n4) {
        final int n5 = (int)(n / n2);
        double n6;
        if (n5 <= n3) {
            n6 = 1.0;
        }
        else if (n5 >= n4) {
            n6 = 0.5;
        }
        else {
            n6 = 0.5 + (n4 - n5) / (n4 - n3) * 0.5;
        }
        return (int)(n * n6);
    }
    
    @SuppressLint({ "SetJavaScriptEnabled" })
    private void setUpWebView(final int n) {
        final LinearLayout linearLayout = new LinearLayout(this.getContext());
        (this.webView = new WebView(this.getContext()) {
            public void onWindowFocusChanged(final boolean b) {
                try {
                    super.onWindowFocusChanged(b);
                }
                catch (NullPointerException ex) {}
            }
        }).setVerticalScrollBarEnabled(false);
        this.webView.setHorizontalScrollBarEnabled(false);
        this.webView.setWebViewClient((WebViewClient)new DialogWebViewClient());
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.loadUrl(this.url);
        this.webView.setLayoutParams((ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -1));
        this.webView.setVisibility(4);
        this.webView.getSettings().setSavePassword(false);
        this.webView.getSettings().setSaveFormData(false);
        linearLayout.setPadding(n, n, n, n);
        linearLayout.addView((View)this.webView);
        linearLayout.setBackgroundColor(-872415232);
        this.contentFrameLayout.addView((View)linearLayout);
    }
    
    public void dismiss() {
        if (!this.isDismissed) {
            this.isDismissed = true;
            if (!this.listenerCalled) {
                this.sendCancelToListener();
            }
            if (this.webView != null) {
                this.webView.stopLoading();
            }
            if (!this.isDetached) {
                if (this.spinner.isShowing()) {
                    this.spinner.dismiss();
                }
                super.dismiss();
            }
        }
    }
    
    public OnCompleteListener getOnCompleteListener() {
        return this.onCompleteListener;
    }
    
    protected WebView getWebView() {
        return this.webView;
    }
    
    protected boolean isListenerCalled() {
        return this.listenerCalled;
    }
    
    public void onAttachedToWindow() {
        this.isDetached = false;
        super.onAttachedToWindow();
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        (this.spinner = new ProgressDialog(this.getContext())).requestWindowFeature(1);
        this.spinner.setMessage((CharSequence)this.getContext().getString(R.string.com_facebook_loading));
        this.spinner.setOnCancelListener((DialogInterface$OnCancelListener)new DialogInterface$OnCancelListener() {
            public void onCancel(final DialogInterface dialogInterface) {
                WebDialog.this.dismiss();
            }
        });
        this.requestWindowFeature(1);
        this.contentFrameLayout = new FrameLayout(this.getContext());
        this.calculateSize();
        this.getWindow().setGravity(17);
        this.getWindow().setSoftInputMode(16);
        this.createCrossImage();
        this.setUpWebView(this.crossImageView.getDrawable().getIntrinsicWidth() / 2 + 1);
        this.contentFrameLayout.addView((View)this.crossImageView, new ViewGroup$LayoutParams(-2, -2));
        this.setContentView((View)this.contentFrameLayout);
    }
    
    public void onDetachedFromWindow() {
        this.isDetached = true;
        super.onDetachedFromWindow();
    }
    
    protected Bundle parseResponseUri(final String s) {
        final Uri parse = Uri.parse(s);
        final Bundle urlQueryString = Utility.parseUrlQueryString(parse.getQuery());
        urlQueryString.putAll(Utility.parseUrlQueryString(parse.getFragment()));
        return urlQueryString;
    }
    
    protected void sendCancelToListener() {
        this.sendErrorToListener(new FacebookOperationCanceledException());
    }
    
    protected void sendErrorToListener(final Throwable t) {
        if (this.onCompleteListener != null && !this.listenerCalled) {
            this.listenerCalled = true;
            FacebookException ex;
            if (t instanceof FacebookException) {
                ex = (FacebookException)t;
            }
            else {
                ex = new FacebookException(t);
            }
            this.onCompleteListener.onComplete(null, ex);
            this.dismiss();
        }
    }
    
    protected void sendSuccessToListener(final Bundle bundle) {
        if (this.onCompleteListener != null && !this.listenerCalled) {
            this.listenerCalled = true;
            this.onCompleteListener.onComplete(bundle, null);
            this.dismiss();
        }
    }
    
    protected void setExpectedRedirectUrl(final String expectedRedirectUrl) {
        this.expectedRedirectUrl = expectedRedirectUrl;
    }
    
    public void setOnCompleteListener(final OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }
    
    public static class Builder extends BuilderBase<Builder>
    {
        public Builder(final Context context, final Session session, final String s, final Bundle bundle) {
            super(context, session, s, bundle);
        }
        
        public Builder(final Context context, final String s) {
            super(context, s);
        }
        
        public Builder(final Context context, final String s, final String s2, final Bundle bundle) {
            super(context, s, s2, bundle);
        }
    }
    
    private static class BuilderBase<CONCRETE extends BuilderBase<?>>
    {
        private String action;
        private String applicationId;
        private Context context;
        private OnCompleteListener listener;
        private Bundle parameters;
        private Session session;
        private int theme;
        
        protected BuilderBase(final Context context, final Session session, final String s, final Bundle bundle) {
            this.theme = 16973840;
            Validate.notNull(session, "session");
            if (!session.isOpened()) {
                throw new FacebookException("Attempted to use a Session that was not open.");
            }
            this.session = session;
            this.finishInit(context, s, bundle);
        }
        
        protected BuilderBase(final Context context, final String s) {
            this.theme = 16973840;
            final Session activeSession = Session.getActiveSession();
            if (activeSession != null && activeSession.isOpened()) {
                this.session = activeSession;
            }
            else {
                final String metadataApplicationId = Utility.getMetadataApplicationId(context);
                if (metadataApplicationId == null) {
                    throw new FacebookException("Attempted to create a builder without an open Active Session or a valid default Application ID.");
                }
                this.applicationId = metadataApplicationId;
            }
            this.finishInit(context, s, null);
        }
        
        protected BuilderBase(final Context context, final String s, final String s2, final Bundle bundle) {
            this.theme = 16973840;
            String metadataApplicationId = s;
            if (s == null) {
                metadataApplicationId = Utility.getMetadataApplicationId(context);
            }
            Validate.notNullOrEmpty(metadataApplicationId, "applicationId");
            this.applicationId = metadataApplicationId;
            this.finishInit(context, s2, bundle);
        }
        
        private void finishInit(final Context context, final String action, final Bundle parameters) {
            this.context = context;
            this.action = action;
            if (parameters != null) {
                this.parameters = parameters;
                return;
            }
            this.parameters = new Bundle();
        }
        
        public WebDialog build() {
            if (this.session != null && this.session.isOpened()) {
                this.parameters.putString("app_id", this.session.getApplicationId());
                this.parameters.putString("access_token", this.session.getAccessToken());
            }
            else {
                this.parameters.putString("app_id", this.applicationId);
            }
            return new WebDialog(this.context, this.action, this.parameters, this.theme, this.listener);
        }
        
        protected String getApplicationId() {
            return this.applicationId;
        }
        
        protected Context getContext() {
            return this.context;
        }
        
        protected OnCompleteListener getListener() {
            return this.listener;
        }
        
        protected Bundle getParameters() {
            return this.parameters;
        }
        
        protected int getTheme() {
            return this.theme;
        }
        
        public CONCRETE setOnCompleteListener(final OnCompleteListener listener) {
            this.listener = listener;
            return (CONCRETE)this;
        }
        
        public CONCRETE setTheme(final int theme) {
            this.theme = theme;
            return (CONCRETE)this;
        }
    }
    
    private class DialogWebViewClient extends WebViewClient
    {
        public void onPageFinished(final WebView webView, final String s) {
            super.onPageFinished(webView, s);
            if (!WebDialog.this.isDetached) {
                WebDialog.this.spinner.dismiss();
            }
            WebDialog.this.contentFrameLayout.setBackgroundColor(0);
            WebDialog.this.webView.setVisibility(0);
            WebDialog.this.crossImageView.setVisibility(0);
        }
        
        public void onPageStarted(final WebView webView, final String s, final Bitmap bitmap) {
            Utility.logd("FacebookSDK.WebDialog", "Webview loading URL: " + s);
            super.onPageStarted(webView, s, bitmap);
            if (!WebDialog.this.isDetached) {
                WebDialog.this.spinner.show();
            }
        }
        
        public void onReceivedError(final WebView webView, final int n, final String s, final String s2) {
            super.onReceivedError(webView, n, s, s2);
            WebDialog.this.sendErrorToListener(new FacebookDialogException(s, n, s2));
        }
        
        public void onReceivedSslError(final WebView webView, final SslErrorHandler sslErrorHandler, final SslError sslError) {
            super.onReceivedSslError(webView, sslErrorHandler, sslError);
            sslErrorHandler.cancel();
            WebDialog.this.sendErrorToListener(new FacebookDialogException(null, -11, null));
        }
        
        public boolean shouldOverrideUrlLoading(WebView string, String s) {
            Utility.logd("FacebookSDK.WebDialog", "Redirect URL: " + s);
            if (s.startsWith(WebDialog.this.expectedRedirectUrl)) {
                final Bundle responseUri = WebDialog.this.parseResponseUri(s);
                s = responseUri.getString("error");
                if ((string = (WebView)s) == null) {
                    string = (WebView)responseUri.getString("error_type");
                }
                if ((s = responseUri.getString("error_msg")) == null) {
                    s = responseUri.getString("error_description");
                }
                final String string2 = responseUri.getString("error_code");
                int int1 = -1;
                while (true) {
                    if (Utility.isNullOrEmpty(string2)) {
                        break Label_0120;
                    }
                    try {
                        int1 = Integer.parseInt(string2);
                        if (Utility.isNullOrEmpty((String)string) && Utility.isNullOrEmpty(s) && int1 == -1) {
                            WebDialog.this.sendSuccessToListener(responseUri);
                            return true;
                        }
                    }
                    catch (NumberFormatException ex) {
                        int1 = -1;
                        continue;
                    }
                    break;
                }
                if (string != null && (((String)string).equals("access_denied") || ((String)string).equals("OAuthAccessDeniedException"))) {
                    WebDialog.this.sendCancelToListener();
                    return true;
                }
                if (int1 == 4201) {
                    WebDialog.this.sendCancelToListener();
                    return true;
                }
                WebDialog.this.sendErrorToListener(new FacebookServiceException(new FacebookRequestError(int1, (String)string, s), s));
                return true;
            }
            else {
                if (s.startsWith("fbconnect://cancel")) {
                    WebDialog.this.sendCancelToListener();
                    return true;
                }
                if (s.contains("touch")) {
                    return false;
                }
                WebDialog.this.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(s)));
                return true;
            }
        }
    }
    
    public static class FeedDialogBuilder extends BuilderBase<FeedDialogBuilder>
    {
        private static final String CAPTION_PARAM = "caption";
        private static final String DESCRIPTION_PARAM = "description";
        private static final String FEED_DIALOG = "feed";
        private static final String FROM_PARAM = "from";
        private static final String LINK_PARAM = "link";
        private static final String NAME_PARAM = "name";
        private static final String PICTURE_PARAM = "picture";
        private static final String SOURCE_PARAM = "source";
        private static final String TO_PARAM = "to";
        
        public FeedDialogBuilder(final Context context) {
            super(context, "feed");
        }
        
        public FeedDialogBuilder(final Context context, final Session session) {
            super(context, session, "feed", null);
        }
        
        public FeedDialogBuilder(final Context context, final Session session, final Bundle bundle) {
            super(context, session, "feed", bundle);
        }
        
        public FeedDialogBuilder(final Context context, final String s, final Bundle bundle) {
            super(context, s, "feed", bundle);
        }
        
        public FeedDialogBuilder setCaption(final String s) {
            ((BuilderBase)this).getParameters().putString("caption", s);
            return this;
        }
        
        public FeedDialogBuilder setDescription(final String s) {
            ((BuilderBase)this).getParameters().putString("description", s);
            return this;
        }
        
        public FeedDialogBuilder setFrom(final String s) {
            ((BuilderBase)this).getParameters().putString("from", s);
            return this;
        }
        
        public FeedDialogBuilder setLink(final String s) {
            ((BuilderBase)this).getParameters().putString("link", s);
            return this;
        }
        
        public FeedDialogBuilder setName(final String s) {
            ((BuilderBase)this).getParameters().putString("name", s);
            return this;
        }
        
        public FeedDialogBuilder setPicture(final String s) {
            ((BuilderBase)this).getParameters().putString("picture", s);
            return this;
        }
        
        public FeedDialogBuilder setSource(final String s) {
            ((BuilderBase)this).getParameters().putString("source", s);
            return this;
        }
        
        public FeedDialogBuilder setTo(final String s) {
            ((BuilderBase)this).getParameters().putString("to", s);
            return this;
        }
    }
    
    public interface OnCompleteListener
    {
        void onComplete(final Bundle p0, final FacebookException p1);
    }
    
    public static class RequestsDialogBuilder extends BuilderBase<RequestsDialogBuilder>
    {
        private static final String APPREQUESTS_DIALOG = "apprequests";
        private static final String DATA_PARAM = "data";
        private static final String MESSAGE_PARAM = "message";
        private static final String TITLE_PARAM = "title";
        private static final String TO_PARAM = "to";
        
        public RequestsDialogBuilder(final Context context) {
            super(context, "apprequests");
        }
        
        public RequestsDialogBuilder(final Context context, final Session session) {
            super(context, session, "apprequests", null);
        }
        
        public RequestsDialogBuilder(final Context context, final Session session, final Bundle bundle) {
            super(context, session, "apprequests", bundle);
        }
        
        public RequestsDialogBuilder(final Context context, final String s, final Bundle bundle) {
            super(context, s, "apprequests", bundle);
        }
        
        public RequestsDialogBuilder setData(final String s) {
            ((BuilderBase)this).getParameters().putString("data", s);
            return this;
        }
        
        public RequestsDialogBuilder setMessage(final String s) {
            ((BuilderBase)this).getParameters().putString("message", s);
            return this;
        }
        
        public RequestsDialogBuilder setTitle(final String s) {
            ((BuilderBase)this).getParameters().putString("title", s);
            return this;
        }
        
        public RequestsDialogBuilder setTo(final String s) {
            ((BuilderBase)this).getParameters().putString("to", s);
            return this;
        }
    }
}
