// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.preference;

import android.text.TextUtils;
import android.widget.TextView;
import android.view.View;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import android.util.AttributeSet;
import android.content.Context;
import android.preference.Preference;

public class CustomTextPreference extends Preference
{
    private String mLocale;
    
    public CustomTextPreference(final Context context, final AttributeSet set) {
        super(context, set);
        this.mLocale = CrunchyrollApplication.getApp(context).getApplicationState().getCustomLocale();
    }
    
    protected void onBindView(final View view) {
        super.onBindView(view);
        final TextView textView = (TextView)view.findViewById(16908304);
        if (textView != null) {
            if (this.mLocale.equalsIgnoreCase("arME")) {
                textView.setGravity(5);
            }
            else {
                textView.setGravity(3);
            }
            final CharSequence summary = this.getSummary();
            if (TextUtils.isEmpty(summary)) {
                textView.setVisibility(8);
                return;
            }
            textView.setText(summary);
            textView.setVisibility(0);
        }
    }
}
