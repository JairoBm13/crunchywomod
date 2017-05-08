// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.ui;

import android.view.View;
import android.webkit.WebChromeClient$CustomViewCallback;
import android.net.Uri;
import android.content.Intent;
import android.util.Log;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.widget.Toast;
import com.swrve.sdk.SwrveHelper;
import android.content.Context;
import com.swrve.sdk.conversations.engine.model.Content;
import android.webkit.WebView;

public class HtmlVideoView extends WebView
{
    private String errorHtml;
    private ConversationFullScreenVideoFrame fullScreenContainer;
    private int height;
    private Content model;
    private String url;
    private String videoHtml;
    
    public HtmlVideoView(final Context context, final Content model, final ConversationFullScreenVideoFrame fullScreenContainer) {
        super(context);
        this.model = model;
        this.fullScreenContainer = fullScreenContainer;
    }
    
    protected void init(final Content content) {
        this.url = content.getValue();
        this.height = Integer.parseInt(content.getHeight());
        if (this.height <= 0) {
            this.height = 220;
        }
        this.errorHtml = "<p " + "style='text-align:center; margin-top:8px'" + ">" + "<a " + "style='font-size: 0.6875em; color: #666; width:100%;'" + " href='" + this.url.toString() + "&swrveexternal" + "'>" + "Can't see the video?</a>";
        if (SwrveHelper.isNullOrEmpty(this.url)) {
            Toast.makeText(this.getContext(), (CharSequence)"Unknown Video Player Detected", 0).show();
            this.videoHtml = "<p>Sorry, a malformed URL was detected. This video cannot be played.</p> ";
        }
        else if (this.url.toLowerCase().contains("youtube") || this.url.toLowerCase().contains("vimeo")) {
            this.videoHtml = "<iframe type='text/html' width='100%' height='" + this.height + "' src=" + this.url + " frameborder='0' webkitAllowFullScreen mozallowfullscreen allowFullScreen></iframe>" + this.errorHtml;
        }
        else {
            Toast.makeText(this.getContext(), (CharSequence)"Unknown Video Player Detected", 0).show();
            this.videoHtml = this.errorHtml;
        }
        final String string = "<html><body style=\"margin: 0; padding: 0\">" + this.videoHtml + "</body></html>";
        this.setWebChromeClient((WebChromeClient)new SwrveWebCromeClient());
        this.setWebViewClient((WebViewClient)new SwrveVideoWebViewClient());
        this.getSettings().setJavaScriptEnabled(true);
        this.loadDataWithBaseURL((String)null, string, "text/html", "utf-8", (String)null);
    }
    
    protected void onWindowVisibilityChanged(final int n) {
        super.onWindowVisibilityChanged(n);
        if (n != 0) {
            Log.i("SwrveSDK", "Stopping the Video!");
            this.stopLoading();
            this.loadData("<p></p>", "text/html", "utf8");
            return;
        }
        this.stopLoading();
        this.init(this.model);
    }
    
    private class SwrveVideoWebViewClient extends WebViewClient
    {
        public void onReceivedError(final WebView webView, final int n, final String s, final String s2) {
            Log.e("SwrveVideoWebViewClient", "Could not display url: " + s2 + "\n" + "Error code: " + Integer.toString(n) + "\n" + "Message: " + s);
            HtmlVideoView.this.loadDataWithBaseURL((String)null, "<html><body style=\"margin: 0; padding: 0;\">" + ("<div style=\"width: 100%; height: " + HtmlVideoView.this.height + "px\"></div>") + HtmlVideoView.this.errorHtml + "</body></html>", "text/html", "utf-8", (String)null);
        }
        
        public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
            if (s.contains("&swrveexternal")) {
                final Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(s.replace("&swrveexternal", "")));
                HtmlVideoView.this.getContext().startActivity(intent);
                return true;
            }
            return false;
        }
    }
    
    private class SwrveWebCromeClient extends WebChromeClient
    {
        private WebChromeClient$CustomViewCallback mCustomViewCallback;
        private View mView;
        
        public void onHideCustomView() {
            if (this.mView != null) {
                this.mView = null;
                HtmlVideoView.this.fullScreenContainer.removeWebCromeClient(this);
                HtmlVideoView.this.fullScreenContainer.removeView(this.mView);
                this.mCustomViewCallback.onCustomViewHidden();
                this.mCustomViewCallback = null;
                HtmlVideoView.this.fullScreenContainer.setVisibility(8);
            }
            super.onHideCustomView();
        }
        
        public void onShowCustomView(final View mView, final WebChromeClient$CustomViewCallback mCustomViewCallback) {
            super.onShowCustomView(mView, mCustomViewCallback);
            this.mCustomViewCallback = mCustomViewCallback;
            this.mView = mView;
            HtmlVideoView.this.fullScreenContainer.setWebCromeClient(this);
            HtmlVideoView.this.fullScreenContainer.setVisibility(0);
            HtmlVideoView.this.fullScreenContainer.addView(mView, -1);
        }
    }
}
