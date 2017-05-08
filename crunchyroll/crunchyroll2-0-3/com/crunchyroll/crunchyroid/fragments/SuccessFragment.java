// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import com.crunchyroll.crunchyroid.events.Upsell;
import de.greenrobot.event.EventBus;
import android.content.Context;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import android.view.View$OnClickListener;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import java.io.Serializable;
import android.os.Bundle;

public class SuccessFragment extends BaseFragment
{
    private Type mSuccessType;
    
    public static SuccessFragment newInstance(final Type type) {
        final SuccessFragment successFragment = new SuccessFragment();
        final Bundle arguments = new Bundle();
        arguments.putSerializable("successType", (Serializable)type);
        successFragment.setArguments(arguments);
        return successFragment;
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mSuccessType = (Type)this.getArguments().getSerializable("successType");
        this.trackView();
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(2130903124, viewGroup, false);
        final View viewById = inflate.findViewById(2131624081);
        final TextView textView = (TextView)inflate.findViewById(2131624222);
        final TextView textView2 = (TextView)inflate.findViewById(2131624223);
        final TextView textView3 = (TextView)inflate.findViewById(2131624220);
        final TextView textView4 = (TextView)inflate.findViewById(2131624221);
        viewById.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                Tracker.upsellSuccessExit((Context)SuccessFragment.this.getActivity());
                EventBus.getDefault().post(new Upsell.CloseEvent());
            }
        });
        textView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                Tracker.upsellSuccessStartWatching((Context)SuccessFragment.this.getActivity());
                EventBus.getDefault().post(new Upsell.OkEvent());
            }
        });
        textView.setText((CharSequence)LocalizedStrings.START_WATCHING.get());
        textView2.setText((CharSequence)LocalizedStrings.LEARN_MORE.get());
        switch (this.mSuccessType) {
            default: {
                return inflate;
            }
            case TYPE_ACCOUNT_CREATED_UPSELL: {
                textView3.setText((CharSequence)LocalizedStrings.THANKS_BANG.get());
                textView4.setText((CharSequence)LocalizedStrings.ACCOUNT_CREATED.get());
                textView2.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        Tracker.upsellSuccessLearnPremium((Context)SuccessFragment.this.getActivity());
                        EventBus.getDefault().post(new Upsell.LearnMoreEvent());
                    }
                });
                return inflate;
            }
            case TYPE_PREMIUM_NO_UPSELL: {
                textView3.setText((CharSequence)LocalizedStrings.SUCCESS_BANG.get());
                textView4.setText((CharSequence)LocalizedStrings.YOU_ARE_NOW_PREMIUM.get());
                textView2.setVisibility(8);
                return inflate;
            }
        }
    }
    
    @Override
    public void trackView() {
        String s = "";
        switch (this.mSuccessType) {
            case TYPE_ACCOUNT_CREATED_UPSELL: {
                Tracker.swrveScreenView("account-create-success");
                s = "account_create_success";
                break;
            }
            case TYPE_PREMIUM_NO_UPSELL: {
                Tracker.swrveScreenView("payment-success");
                s = "payment_success";
                break;
            }
        }
        this.trackViewExtras(s);
    }
    
    public enum Type
    {
        TYPE_ACCOUNT_CREATED_UPSELL, 
        TYPE_PREMIUM_NO_UPSELL;
    }
}
