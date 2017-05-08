// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import com.crunchyroll.android.api.tasks.ApiTaskListener;
import com.crunchyroll.crunchyroid.util.Util;
import com.crunchyroll.android.api.exceptions.ApiErrorException;
import com.crunchyroll.android.api.tasks.BaseListener;
import android.app.Activity;
import com.crunchyroll.android.api.ApiManager;
import android.content.Context;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import com.crunchyroll.crunchyroid.events.Upsell;
import de.greenrobot.event.EventBus;
import android.view.View$OnClickListener;
import android.widget.TextView;
import android.view.View;
import android.view.LayoutInflater;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;

public class ForgotPasswordFragment extends BaseFragment
{
    private EditText mEmail;
    private ViewGroup mView;
    
    public static ForgotPasswordFragment newInstance() {
        return new ForgotPasswordFragment();
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        Tracker.swrveScreenView("forgot-password");
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return (View)(this.mView = (ViewGroup)layoutInflater.inflate(2130903119, viewGroup, false));
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        final View viewById = view.findViewById(2131624197);
        final TextView textView = (TextView)view.findViewById(2131624199);
        final TextView textView2 = (TextView)view.findViewById(2131624200);
        final TextView textView3 = (TextView)view.findViewById(2131624020);
        final TextView textView4 = (TextView)view.findViewById(2131624198);
        viewById.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                EventBus.getDefault().post(new Upsell.BackEvent());
                Tracker.forgotPasswordActions("back");
            }
        });
        (this.mEmail = (EditText)view.findViewById(2131624169)).setHint((CharSequence)LocalizedStrings.YOUR_EMAIL.get());
        textView3.setText((CharSequence)LocalizedStrings.RESET_PASSWORD.get());
        textView4.setText((CharSequence)LocalizedStrings.FORGOT_PASSWORD_DIALOG_MESSAGE.get());
        textView.setText((CharSequence)LocalizedStrings.RESET_PASSWORD.get());
        textView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                Tracker.forgotPasswordSent((Context)ForgotPasswordFragment.this.getActivity());
                ApiManager.getInstance(ForgotPasswordFragment.this.getActivity()).getForgottenPassword(ForgotPasswordFragment.this.mEmail.getText().toString(), new BaseListener<Boolean>() {
                    @Override
                    public void onException(final Exception ex) {
                        String s = LocalizedStrings.ERROR_UNKNOWN.get();
                        if (ex instanceof ApiErrorException) {
                            if (ApiErrorException.ApiErrorCode.INTERNAL_SERVER_ERROR.equals(((ApiErrorException)ex).getErrorCode())) {
                                s = LocalizedStrings.ERROR_SERVER_PROBLEMS.get();
                            }
                            else {
                                s = ex.getLocalizedMessage();
                            }
                        }
                        EventBus.getDefault().post(new Upsell.SignupExceptionEvent(s));
                        Tracker.forgotPasswordActions("failure");
                    }
                    
                    @Override
                    public void onFinally() {
                        super.onFinally();
                        Util.hideProgressBar((Context)ForgotPasswordFragment.this.getActivity(), ForgotPasswordFragment.this.mView);
                    }
                    
                    @Override
                    public void onPreExecute() {
                        super.onPreExecute();
                        Util.showProgressBar((Context)ForgotPasswordFragment.this.getActivity(), ForgotPasswordFragment.this.mView, ForgotPasswordFragment.this.getResources().getColor(2131558519));
                    }
                    
                    @Override
                    public void onSuccess(final Boolean b) {
                        super.onSuccess(b);
                        if (b) {
                            EventBus.getDefault().post(new Upsell.OkEvent());
                            Tracker.forgotPasswordActions("sent");
                            return;
                        }
                        this.onException(new Exception());
                    }
                });
            }
        });
        textView2.setText((CharSequence)LocalizedStrings.CANCEL.get());
        textView2.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                EventBus.getDefault().post(new Upsell.BackEvent());
                Tracker.forgotPasswordActions("cancel");
            }
        });
    }
}
