// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import com.google.common.base.Optional;
import android.widget.SpinnerAdapter;
import android.widget.ArrayAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import de.greenrobot.event.EventBus;
import com.crunchyroll.crunchyroid.events.ClickEvent;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.animation.Interpolator;
import android.view.animation.AccelerateInterpolator;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.app.AlertDialog$Builder;
import com.crunchyroll.android.api.tasks.ApiTaskListener;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import com.crunchyroll.android.api.tasks.BaseListener;
import android.app.Activity;
import com.crunchyroll.android.api.ApiManager;
import android.view.animation.Animation;
import android.view.animation.Animation$AnimationListener;
import android.view.animation.AlphaAnimation;
import android.content.Context;
import com.crunchyroll.crunchyroid.app.ApplicationState;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.text.TextUtils;
import android.widget.TextView;
import java.util.ArrayList;
import com.crunchyroll.android.api.models.User;
import java.util.List;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.view.View;
import android.widget.EditText;

public class ContactUsFragment extends BaseFragment
{
    private static long ANIMATION_DURATION;
    private EditText mDescriptionText;
    private EditText mEmailText;
    private View mLoader;
    private EditText mMembershipType;
    private Spinner mProblemSpinner;
    private CheckBox mReceiveResponseCheck;
    private EditText mSubjectText;
    private List<Type> mTypeList;
    private User mUser;
    
    static {
        ContactUsFragment.ANIMATION_DURATION = 250L;
    }
    
    public ContactUsFragment() {
        this.mTypeList = new ArrayList<Type>();
        this.mUser = null;
        this.mEmailText = null;
        this.mMembershipType = null;
        this.mProblemSpinner = null;
        this.mSubjectText = null;
        this.mDescriptionText = null;
        this.mReceiveResponseCheck = null;
        this.mLoader = null;
    }
    
    private String buildSubject() {
        if (TextUtils.isEmpty((CharSequence)this.mSubjectText.getText().toString())) {
            return "";
        }
        return "[" + this.getMembershipType() + "]" + " " + this.mSubjectText.getText().toString();
    }
    
    private void createTypeList() {
        this.mTypeList.add(new Type(LocalizedStrings.WHAT_AILS_YOU.get(), null));
        this.mTypeList.add(new Type(LocalizedStrings.VIDEO_PLAYBACK.get(), "video"));
        this.mTypeList.add(new Type(LocalizedStrings.SUBTITLES.get(), "subtitles"));
        this.mTypeList.add(new Type(LocalizedStrings.ACCOUNT_LOGIN.get(), "account"));
        this.mTypeList.add(new Type(LocalizedStrings.PREMIUM_MEMBERSHIP.get(), "premium"));
        this.mTypeList.add(new Type(LocalizedStrings.OTHER.get(), "other"));
    }
    
    private String getMembershipType() {
        final ApplicationState value = ApplicationState.get((Context)this.getActivity());
        if (value.hasLoggedInUser() && value.isAnyPremium()) {
            return LocalizedStrings.PREMIUM.get();
        }
        if (value.hasLoggedInUser() && !value.isAnyPremium()) {
            return LocalizedStrings.FREE.get();
        }
        return LocalizedStrings.ANONYMOUS.get();
    }
    
    private void hideLoader() {
        final AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(ContactUsFragment.ANIMATION_DURATION);
        alphaAnimation.setAnimationListener((Animation$AnimationListener)new Animation$AnimationListener() {
            public void onAnimationEnd(final Animation animation) {
                ContactUsFragment.this.mLoader.setVisibility(8);
            }
            
            public void onAnimationRepeat(final Animation animation) {
            }
            
            public void onAnimationStart(final Animation animation) {
            }
        });
        this.mLoader.startAnimation((Animation)alphaAnimation);
    }
    
    public static ContactUsFragment newInstance() {
        return new ContactUsFragment();
    }
    
    private void sendData() {
        final String string = this.mEmailText.getText().toString();
        final String buildSubject = this.buildSubject();
        final String string2 = this.mDescriptionText.getText().toString();
        final String typeValue = ((Type)this.mProblemSpinner.getSelectedItem()).getTypeValue();
        final boolean checked = this.mReceiveResponseCheck.isChecked();
        this.showLoader();
        ApiManager.getInstance(this.getActivity()).submitContactUs(new BaseListener<Void>() {
            @Override
            public void onException(final Exception ex) {
                ContactUsFragment.this.showError(ex.getMessage());
                Tracker.settingsContactUsFailure();
            }
            
            @Override
            public void onFinally() {
                ContactUsFragment.this.hideLoader();
            }
            
            @Override
            public void onSuccess(final Void void1) {
                Tracker.submitContactUs((Context)ContactUsFragment.this.getActivity());
                ContactUsFragment.this.showSuccess();
            }
        }, string, buildSubject, string2, typeValue, checked);
        Tracker.settingsContactUsSend(typeValue, buildSubject, string);
    }
    
    private void showError(final String message) {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this.getActivity());
        alertDialog$Builder.setTitle((CharSequence)LocalizedStrings.ERROR_VALIDATION_TITLE.get());
        alertDialog$Builder.setMessage((CharSequence)message);
        alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.OK.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.dismiss();
            }
        });
        alertDialog$Builder.create().show();
    }
    
    private void showLoader() {
        final AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(ContactUsFragment.ANIMATION_DURATION);
        alphaAnimation.setAnimationListener((Animation$AnimationListener)new Animation$AnimationListener() {
            public void onAnimationEnd(final Animation animation) {
            }
            
            public void onAnimationRepeat(final Animation animation) {
            }
            
            public void onAnimationStart(final Animation animation) {
                ContactUsFragment.this.mLoader.setVisibility(0);
            }
        });
        this.mLoader.startAnimation((Animation)alphaAnimation);
    }
    
    private void showSuccess() {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this.getActivity());
        alertDialog$Builder.setTitle((CharSequence)LocalizedStrings.SUCCESS.get());
        alertDialog$Builder.setMessage((CharSequence)LocalizedStrings.CONTACT_US_SUCCESS.get());
        alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.OK.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.dismiss();
                ContactUsFragment.this.getActivity().finish();
            }
        });
        alertDialog$Builder.create().show();
    }
    
    private void switchViews(final EditText editText, final TextView textView) {
        if (editText.getText().toString().isEmpty()) {
            if (textView.getVisibility() != 4) {
                textView.setVisibility(4);
                final AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                ((Animation)alphaAnimation).setInterpolator((Interpolator)new AccelerateInterpolator());
                ((Animation)alphaAnimation).setDuration(250L);
                textView.startAnimation((Animation)alphaAnimation);
            }
        }
        else if (textView.getVisibility() != 0) {
            textView.setVisibility(0);
            final AlphaAnimation alphaAnimation2 = new AlphaAnimation(0.0f, 1.0f);
            ((Animation)alphaAnimation2).setInterpolator((Interpolator)new AccelerateInterpolator());
            ((Animation)alphaAnimation2).setDuration(250L);
            textView.startAnimation((Animation)alphaAnimation2);
        }
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        Tracker.swrveScreenView("settings-contact-us");
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(2130903115, viewGroup, false);
    }
    
    public void onEvent(final ClickEvent clickEvent) {
        this.sendData();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        this.mEmailText = (EditText)view.findViewById(2131624169);
        this.mMembershipType = (EditText)view.findViewById(2131624172);
        this.mProblemSpinner = (Spinner)view.findViewById(2131624174);
        this.mSubjectText = (EditText)view.findViewById(2131624176);
        this.mDescriptionText = (EditText)view.findViewById(2131624154);
        this.mReceiveResponseCheck = (CheckBox)view.findViewById(2131624178);
        final Optional<User> loggedInUser = ApplicationState.get((Context)this.getActivity()).getLoggedInUser();
        if (loggedInUser.isPresent()) {
            this.mUser = loggedInUser.get();
            this.mEmailText.setText((CharSequence)this.mUser.getEmail());
            this.mSubjectText.requestFocus();
        }
        this.mLoader = view.findViewById(2131624179);
        final TextView textView = (TextView)view.findViewById(2131624168);
        final TextView textView2 = (TextView)view.findViewById(2131624171);
        final TextView textView3 = (TextView)view.findViewById(2131624173);
        final TextView textView4 = (TextView)view.findViewById(2131624175);
        final TextView textView5 = (TextView)view.findViewById(2131624177);
        this.mEmailText.setHint((CharSequence)LocalizedStrings.YOUR_EMAIL.get());
        textView.setText((CharSequence)(LocalizedStrings.YOUR_EMAIL.get() + ":"));
        this.switchViews(this.mEmailText, textView);
        textView2.setText((CharSequence)(LocalizedStrings.MEMBERSHIP.get() + ":"));
        textView3.setText((CharSequence)(LocalizedStrings.PROBLEM.get() + ":"));
        this.mSubjectText.setHint((CharSequence)LocalizedStrings.SUBJECT.get());
        textView4.setText((CharSequence)(LocalizedStrings.SUBJECT.get() + ":"));
        this.switchViews(this.mSubjectText, textView4);
        this.mDescriptionText.setHint((CharSequence)LocalizedStrings.YOUR_MESSAGE.get());
        textView5.setText((CharSequence)(LocalizedStrings.YOUR_MESSAGE.get() + ":"));
        this.switchViews(this.mDescriptionText, textView5);
        this.mEmailText.addTextChangedListener((TextWatcher)new TextWatcher() {
            public void afterTextChanged(final Editable editable) {
                ContactUsFragment.this.switchViews(ContactUsFragment.this.mEmailText, textView);
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
        });
        this.mSubjectText.addTextChangedListener((TextWatcher)new TextWatcher() {
            public void afterTextChanged(final Editable editable) {
                ContactUsFragment.this.switchViews(ContactUsFragment.this.mSubjectText, textView4);
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
        });
        this.mDescriptionText.addTextChangedListener((TextWatcher)new TextWatcher() {
            public void afterTextChanged(final Editable editable) {
                ContactUsFragment.this.switchViews(ContactUsFragment.this.mDescriptionText, textView5);
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
        });
        this.mReceiveResponseCheck.setText((CharSequence)LocalizedStrings.RECEIVE_REPLY.get());
        this.mMembershipType.setText((CharSequence)this.getMembershipType());
        this.createTypeList();
        final ArrayAdapter adapter = new ArrayAdapter((Context)this.getActivity(), 2130903180, (List)this.mTypeList);
        adapter.setDropDownViewResource(2130903181);
        this.mProblemSpinner.setAdapter((SpinnerAdapter)adapter);
    }
    
    private static class Type
    {
        private String typeText;
        private String typeValue;
        
        public Type(final String typeText, final String typeValue) {
            this.typeText = null;
            this.typeValue = null;
            this.typeText = typeText;
            this.typeValue = typeValue;
        }
        
        public String getTypeValue() {
            return this.typeValue;
        }
        
        @Override
        public String toString() {
            return this.typeText;
        }
    }
}
