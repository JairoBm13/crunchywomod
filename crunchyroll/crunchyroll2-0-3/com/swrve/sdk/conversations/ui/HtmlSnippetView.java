// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.ui;

import java.io.InputStream;
import java.io.IOException;
import com.swrve.sdk.SwrveHelper;
import android.content.Context;
import com.swrve.sdk.conversations.engine.model.Content;
import android.webkit.WebView;

public class HtmlSnippetView extends WebView
{
    private static String DEFAULT_CSS;
    private Content model;
    
    static {
        HtmlSnippetView.DEFAULT_CSS = null;
    }
    
    public HtmlSnippetView(final Context context, final Content content) {
        super(context);
        Label_0046: {
            if (HtmlSnippetView.DEFAULT_CSS != null) {
                break Label_0046;
            }
            while (true) {
                try {
                    final InputStream open = context.getAssets().open("cio__css_defaults.css");
                    if (open != null) {
                        HtmlSnippetView.DEFAULT_CSS = SwrveHelper.readStringFromInputStream(open);
                    }
                    if (SwrveHelper.isNullOrEmpty(HtmlSnippetView.DEFAULT_CSS)) {
                        HtmlSnippetView.DEFAULT_CSS = "";
                    }
                    this.init(content);
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                    continue;
                }
                break;
            }
        }
    }
    
    protected void init(final Content model) {
        this.model = model;
        this.loadDataWithBaseURL((String)null, "<html><head><style>" + HtmlSnippetView.DEFAULT_CSS + "</style></head><body><div style='max-width:100%; overflow: hidden; word-wrap: break-word;'>" + model.getValue() + "</div></body></html>", "text/html", "UTF-8", (String)null);
    }
}
