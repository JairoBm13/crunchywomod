// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.vast.widget;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.webkit.WebViewClient;
import android.view.View$OnClickListener;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.widget.RelativeLayout$LayoutParams;
import com.secondtv.android.ads.R;
import android.util.AttributeSet;
import android.content.Context;
import android.webkit.WebView;
import com.secondtv.android.ads.DeepLinker;
import android.widget.Button;
import android.app.Activity;
import android.widget.RelativeLayout;

public class ClickthroughView extends RelativeLayout
{
    private static final int BLACK = -16777216;
    private static final int CLOSE_BUTTON_ID = 1;
    public static final String GOOGLE_PLAY_STORE_BASE_URL = "https://play.google.com/store/apps/";
    private static final int WHITE = -1;
    private Activity mActivity;
    private Button mCloseButton;
    private DeepLinker mDeepLinker;
    private OnCloseListener mOnCloseListener;
    private WebView mWebView;
    
    public ClickthroughView(final Context context) {
        super(context);
        this.init();
    }
    
    public ClickthroughView(final Context context, final AttributeSet set) {
        super(context, set);
        this.init();
    }
    
    public ClickthroughView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.init();
    }
    
    private void init() {
        this.setBackgroundColor(-16777216);
        this.initCloseButton();
        this.initWebView();
    }
    
    private void initCloseButton() {
        (this.mCloseButton = new Button(this.getContext())).setId(1);
        this.mCloseButton.setBackgroundResource(R.drawable.button_clickthrough);
        this.mCloseButton.setTextColor(this.getResources().getColor(17170443));
        this.mCloseButton.setPadding(10, 0, 10, 0);
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams(-2, (int)(40.0f * this.getResources().getDisplayMetrics().density));
        relativeLayout$LayoutParams.addRule(10);
        relativeLayout$LayoutParams.addRule(11);
        relativeLayout$LayoutParams.setMargins(0, 0, 0, 5);
        this.addView((View)this.mCloseButton, (ViewGroup$LayoutParams)relativeLayout$LayoutParams);
        this.mCloseButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (ClickthroughView.this.mOnCloseListener != null) {
                    ClickthroughView.this.mOnCloseListener.onClose(ClickthroughView.this);
                }
            }
        });
        this.mCloseButton.setVisibility(0);
    }
    
    @SuppressLint({ "SetJavaScriptEnabled" })
    private void initWebView() {
        (this.mWebView = new WebView(this.getContext())).setBackgroundColor(-1);
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.setWebViewClient((WebViewClient)new WebViewClient() {
            public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
                final Uri parse = Uri.parse(s);
                if (!parse.getScheme().equals("market") || ClickthroughView.this.mOnCloseListener == null) {
                    return super.shouldOverrideUrlLoading(webView, s);
                }
                if (ClickthroughView.this.mOnCloseListener.onMarketUrlOpened(s)) {
                    return true;
                }
                webView.loadUrl("https://play.google.com/store/apps/" + parse.getHost() + "?" + parse.getQuery());
                return false;
            }
        });
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams(0, 0);
        relativeLayout$LayoutParams.addRule(3, 1);
        relativeLayout$LayoutParams.addRule(12);
        relativeLayout$LayoutParams.addRule(9);
        relativeLayout$LayoutParams.addRule(11);
        this.addView((View)this.mWebView, (ViewGroup$LayoutParams)relativeLayout$LayoutParams);
    }
    
    public void loadUrl(final String s) {
        this.mWebView.loadUrl(s);
    }
    
    public void setActivity(final Activity mActivity) {
        this.mActivity = mActivity;
    }
    
    public void setCloseString(final String text) {
        this.mCloseButton.setText((CharSequence)text);
    }
    
    public void setCloseText(final CharSequence text) {
        this.mCloseButton.setText(text);
    }
    
    public void setDeepLinker(final DeepLinker mDeepLinker) {
        this.mDeepLinker = mDeepLinker;
    }
    
    public void setOnCloseListener(final OnCloseListener mOnCloseListener) {
        this.mOnCloseListener = mOnCloseListener;
    }
    
    public interface OnCloseListener
    {
        void onClose(final ClickthroughView p0);
        
        boolean onMarketUrlOpened(final String p0);
    }
}
