// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.activities;

import de.greenrobot.event.EventBus;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.app.AlertDialog$Builder;
import com.crunchyroll.crunchyroid.util.Util;
import com.crunchyroll.crunchyroid.events.Upsell;
import com.crunchyroll.crunchyroid.events.PopupNewFragmentEvent;
import com.crunchyroll.crunchyroid.events.PopupCloseEvent;
import android.support.design.widget.Snackbar;
import com.crunchyroll.crunchyroid.events.ErrorEvent;
import com.crunchyroll.crunchyroid.fragments.MediaInfoFragment;
import com.crunchyroll.crunchyroid.fragments.UpsellFeatureFragment;
import android.view.View;
import android.view.View$OnClickListener;
import android.os.Bundle;
import com.crunchyroll.crunchyroid.app.PrepareToWatch;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import com.crunchyroll.crunchyroid.fragments.UpsellFragment;
import com.crunchyroll.crunchyroid.fragments.CreateAccountLoginPillTabsPopupFragment;
import java.io.Serializable;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public final class PopupActivity extends FragmentActivity
{
    private Fragment mFragment;
    private boolean mIsTablet;
    
    public static void startMediaInfo(final Activity activity, final long n) {
        final Intent intent = new Intent((Context)activity, (Class)PopupActivity.class);
        intent.putExtra("startType", (Serializable)Type.START_TYPE_MEDIA_INFO);
        intent.putExtra("mediaId", n);
        activity.startActivityForResult(intent, 31);
        activity.overridePendingTransition(0, 0);
    }
    
    public static void startSignup(final Activity activity, final CreateAccountLoginPillTabsPopupFragment.Type type, final boolean b) {
        final Intent intent = new Intent((Context)activity, (Class)PopupActivity.class);
        intent.putExtra("startType", (Serializable)Type.START_TYPE_SIGNUP);
        intent.putExtra("type", (Serializable)type);
        intent.putExtra("showLogin", b);
        activity.startActivityForResult(intent, 31);
        activity.overridePendingTransition(0, 0);
    }
    
    public static void startUpsell(final Activity activity, final UpsellFragment.Type type) {
        final Intent intent = new Intent((Context)activity, (Class)PopupActivity.class);
        intent.putExtra("startType", (Serializable)Type.START_TYPE_UPSELL);
        intent.putExtra("type", (Serializable)type);
        activity.startActivityForResult(intent, 31);
        activity.overridePendingTransition(0, 0);
    }
    
    public static void startUpsellFeature(final Activity activity, final String s) {
        final Intent intent = new Intent((Context)activity, (Class)PopupActivity.class);
        intent.putExtra("startType", (Serializable)Type.START_TYPE_UPSELL_FEATURE);
        intent.putExtra("seriesName", s);
        activity.startActivityForResult(intent, 31);
        activity.overridePendingTransition(0, 0);
    }
    
    public void finish() {
        CrunchyrollApplication.getApp((Context)this).invalidatePrepareToWatch();
        CrunchyrollApplication.getApp((Context)this).stopQueueAfterLoginReceiver();
        super.finish();
        CrunchyrollApplication.getApp((Context)this).invalidatePrepareToWatch();
        this.overridePendingTransition(0, 0);
    }
    
    @Override
    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        super.onActivityResult(n, n2, intent);
        if (n == 21 || n2 == 23 || n2 == 22) {
            this.setResult(n2, intent);
            this.finish();
        }
    }
    
    @Override
    public void onBackPressed() {
        if (!CrunchyrollApplication.getApp((Context)this).prepareToWatchGo(PrepareToWatch.Event.BACK)) {
            this.finish();
        }
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (!(this.mIsTablet = (this.getResources().getInteger(2131427330) != 0))) {
            this.setRequestedOrientation(1);
        }
        this.setContentView(2130903072);
        this.findViewById(2131624060).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                CrunchyrollApplication.getApp((Context)PopupActivity.this).invalidatePrepareToWatch();
                CrunchyrollApplication.getApp((Context)PopupActivity.this).stopQueueAfterLoginReceiver();
                PopupActivity.this.finish();
                PopupActivity.this.overridePendingTransition(0, 0);
            }
        });
        final PrepareToWatch prepareToWatch = CrunchyrollApplication.getApp((Context)this).getPrepareToWatch();
        if (prepareToWatch != null) {
            prepareToWatch.setActivity(this);
        }
        if (bundle == null) {
            switch ((Type)this.getIntent().getSerializableExtra("startType")) {
                default: {
                    this.finish();
                    break;
                }
                case START_TYPE_UPSELL: {
                    this.mFragment = UpsellFragment.newInstance((UpsellFragment.Type)this.getIntent().getSerializableExtra("type"));
                    this.getSupportFragmentManager().beginTransaction().replace(2131624066, this.mFragment).commit();
                }
                case START_TYPE_UPSELL_FEATURE: {
                    this.mFragment = UpsellFeatureFragment.newInstance(UpsellFeatureFragment.Type.TYPE_QUEUE, this.getIntent().getStringExtra("seriesName"));
                    this.getSupportFragmentManager().beginTransaction().replace(2131624066, this.mFragment).commit();
                }
                case START_TYPE_SIGNUP: {
                    this.mFragment = CreateAccountLoginPillTabsPopupFragment.newInstance((CreateAccountLoginPillTabsPopupFragment.Type)this.getIntent().getSerializableExtra("type"), this.getIntent().getBooleanExtra("showLogin", false));
                    this.getSupportFragmentManager().beginTransaction().replace(2131624066, this.mFragment).commit();
                }
                case START_TYPE_MEDIA_INFO: {
                    this.mFragment = MediaInfoFragment.newInstance(this.getIntent().getLongExtra("mediaId", 0L));
                    this.getSupportFragmentManager().beginTransaction().replace(2131624066, this.mFragment).commit();
                }
            }
        }
    }
    
    public void onEvent(final ErrorEvent errorEvent) {
        Snackbar.make(this.findViewById(2131624060), errorEvent.getMessage(), 0).show();
    }
    
    public void onEvent(final PopupCloseEvent popupCloseEvent) {
        this.finish();
    }
    
    public void onEvent(final PopupNewFragmentEvent popupNewFragmentEvent) {
        this.mFragment = popupNewFragmentEvent.getFragment();
        this.getSupportFragmentManager().beginTransaction().replace(2131624066, this.mFragment).commitAllowingStateLoss();
    }
    
    public void onEvent(final Upsell.AccountCreatedEvent accountCreatedEvent) {
        CrunchyrollApplication.getApp((Context)this).prepareToWatchGo(PrepareToWatch.Event.ACCOUNT_CREATED);
    }
    
    public void onEvent(final Upsell.AlreadyPremiumEvent alreadyPremiumEvent) {
        CrunchyrollApplication.getApp((Context)this).prepareToWatchGo(PrepareToWatch.Event.ALREADY_PREMIUM);
    }
    
    public void onEvent(final Upsell.BackEvent backEvent) {
        CrunchyrollApplication.getApp((Context)this).prepareToWatchGo(PrepareToWatch.Event.BACK);
    }
    
    public void onEvent(final Upsell.CancelEvent cancelEvent) {
        CrunchyrollApplication.getApp((Context)this).prepareToWatchGo(PrepareToWatch.Event.CANCEL);
    }
    
    public void onEvent(final Upsell.CardAlreadyUsedEvent cardAlreadyUsedEvent) {
        CrunchyrollApplication.getApp((Context)this).prepareToWatchGo(PrepareToWatch.Event.CARD_ALREADY_USED);
    }
    
    public void onEvent(final Upsell.CloseEvent closeEvent) {
        CrunchyrollApplication.getApp((Context)this).prepareToWatchGo(PrepareToWatch.Event.CLOSE);
    }
    
    public void onEvent(final Upsell.CreateAccountEvent createAccountEvent) {
        CrunchyrollApplication.getApp((Context)this).prepareToWatchGo(PrepareToWatch.Event.CREATE_ACCOUNT);
    }
    
    public void onEvent(final Upsell.ForgotPasswordEvent forgotPasswordEvent) {
        CrunchyrollApplication.getApp((Context)this).prepareToWatchGo(PrepareToWatch.Event.FORGOT_PASSWORD);
    }
    
    public void onEvent(final Upsell.LearnMoreEvent learnMoreEvent) {
        CrunchyrollApplication.getApp((Context)this).prepareToWatchGo(PrepareToWatch.Event.LEARN_MORE);
    }
    
    public void onEvent(final Upsell.LoggedInEvent loggedInEvent) {
        CrunchyrollApplication.getApp((Context)this).prepareToWatchGo(PrepareToWatch.Event.LOGGED_IN);
    }
    
    public void onEvent(final Upsell.LoginEvent loginEvent) {
        CrunchyrollApplication.getApp((Context)this).prepareToWatchGo(PrepareToWatch.Event.LOGIN);
    }
    
    public void onEvent(final Upsell.MediaNotAvailableEvent mediaNotAvailableEvent) {
        Util.showErrorPopup((Context)this, mediaNotAvailableEvent.message);
    }
    
    public void onEvent(final Upsell.NotQualifiedEvent notQualifiedEvent) {
        CrunchyrollApplication.getApp((Context)this).prepareToWatchGo(PrepareToWatch.Event.NOT_QUALIFIED);
    }
    
    public void onEvent(final Upsell.OkEvent okEvent) {
        CrunchyrollApplication.getApp((Context)this).prepareToWatchGo(PrepareToWatch.Event.OK);
    }
    
    public void onEvent(final Upsell.PaymentExceptionEvent paymentExceptionEvent) {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this);
        alertDialog$Builder.setCancelable(false);
        alertDialog$Builder.setMessage((CharSequence)paymentExceptionEvent.message);
        alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.OK.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.dismiss();
            }
        });
        alertDialog$Builder.create().show();
    }
    
    public void onEvent(final Upsell.SignupExceptionEvent signupExceptionEvent) {
        Util.showErrorPopup((Context)this, signupExceptionEvent.message);
    }
    
    public void onEvent(final Upsell.UpsellDismissedEvent upsellDismissedEvent) {
        CrunchyrollApplication.getApp((Context)this).prepareToWatchGo(PrepareToWatch.Event.UPSELL_DISMISSED);
    }
    
    public void onEvent(final Upsell.ValidationErrorEvent validationErrorEvent) {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this);
        alertDialog$Builder.setTitle((CharSequence)LocalizedStrings.ERROR_VALIDATION_TITLE.get());
        alertDialog$Builder.setMessage((CharSequence)validationErrorEvent.message);
        alertDialog$Builder.setNeutralButton((CharSequence)LocalizedStrings.OK.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                if (validationErrorEvent.editText != null) {
                    validationErrorEvent.editText.requestFocus();
                }
            }
        });
        alertDialog$Builder.create().show();
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    
    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    
    public enum Type
    {
        START_TYPE_MEDIA_INFO, 
        START_TYPE_SIGNUP, 
        START_TYPE_UPSELL, 
        START_TYPE_UPSELL_FEATURE;
    }
}
