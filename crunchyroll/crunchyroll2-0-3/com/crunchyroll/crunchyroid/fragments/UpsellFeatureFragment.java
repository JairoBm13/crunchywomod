// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import android.content.Context;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import com.crunchyroll.crunchyroid.events.Upsell;
import de.greenrobot.event.EventBus;
import android.view.View$OnClickListener;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import com.crunchyroll.crunchyroid.app.Extras;
import android.os.Bundle;

public class UpsellFeatureFragment extends BaseFragment
{
    private Type mUpsellType;
    
    public static UpsellFeatureFragment newInstance(final Type type, final String s) {
        final UpsellFeatureFragment upsellFeatureFragment = new UpsellFeatureFragment();
        final Bundle arguments = new Bundle();
        Extras.putString(arguments, "seriesName", s);
        Extras.putSerializable(arguments, "upsellType", type);
        upsellFeatureFragment.setArguments(arguments);
        return upsellFeatureFragment;
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mUpsellType = (Type)this.getArguments().getSerializable("upsellType");
        this.trackView();
        Tracker.swrveScreenView("queue-account-needed");
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(2130903127, viewGroup, false);
        final String s = Extras.getString(this.getArguments(), "seriesName").get();
        final View viewById = inflate.findViewById(2131624081);
        final TextView textView = (TextView)inflate.findViewById(2131624224);
        final TextView textView2 = (TextView)inflate.findViewById(2131624225);
        final TextView textView3 = (TextView)inflate.findViewById(2131624240);
        final TextView textView4 = (TextView)inflate.findViewById(2131624241);
        viewById.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                EventBus.getDefault().post(new Upsell.CloseEvent());
            }
        });
        textView.setText((CharSequence)LocalizedStrings.CREATE_FREE_ACCOUNT_OR_LOGIN_TO_USE_FEATURE.get());
        textView2.setText((CharSequence)LocalizedStrings.ADD_THIS_SHOW_TO_QUEUE.get());
        textView3.setText((CharSequence)LocalizedStrings.CREATE_ACCOUNT.get());
        textView3.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                Tracker.queueCreateAccount((Context)UpsellFeatureFragment.this.getActivity(), s);
                EventBus.getDefault().post(new Upsell.CreateAccountEvent());
            }
        });
        textView4.setText((CharSequence)LocalizedStrings.LOG_IN.get());
        textView4.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                Tracker.queueLogin((Context)UpsellFeatureFragment.this.getActivity(), s);
                EventBus.getDefault().post(new Upsell.LoginEvent());
            }
        });
        return inflate;
    }
    
    @Override
    public void trackView() {
        switch (this.mUpsellType) {
            default: {}
            case TYPE_QUEUE: {
                this.trackViewExtras("queue_account_needed");
            }
        }
    }
    
    public enum Type
    {
        TYPE_QUEUE;
    }
}
