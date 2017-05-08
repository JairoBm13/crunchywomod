// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import java.util.Calendar;
import com.crunchyroll.crunchyroid.app.Extras;
import android.view.inputmethod.InputMethodManager;
import android.view.View;
import com.crunchyroll.android.api.tasks.ApiTaskListener;
import android.app.Activity;
import com.crunchyroll.android.api.ApiManager;
import com.crunchyroll.android.api.models.PaymentInformation;
import com.crunchyroll.crunchyroid.app.ApplicationState;
import android.support.v4.content.LocalBroadcastManager;
import android.content.Intent;
import com.crunchyroll.android.api.models.User;
import com.crunchyroll.android.api.exceptions.ApiErrorException;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import java.io.Serializable;
import android.os.Bundle;
import com.crunchyroll.crunchyroid.app.PrepareToWatch;
import java.util.Arrays;
import android.widget.SpinnerAdapter;
import com.crunchyroll.crunchyroid.events.Upsell;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import android.content.Context;
import com.crunchyroll.crunchyroid.util.Util;
import com.crunchyroll.crunchyroid.events.ErrorEvent;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import de.greenrobot.event.EventBus;
import android.widget.Button;
import android.widget.TextView;
import android.view.ViewGroup;
import com.crunchyroll.android.api.tasks.BaseListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.EditText;
import android.view.View$OnClickListener;

public class PaymentFragment extends BaseFragment implements View$OnClickListener
{
    public static final int NUM_YEARS = 10;
    private EditText cardNumberField;
    private String[] countryCodes;
    private Spinner countrySpinner;
    private ArrayAdapter<CharSequence> countrySpinnerAdapter;
    private String mCurrency;
    private boolean mDidGoForward;
    private boolean mIsFreeTrial;
    private BaseListener<Void> mListener;
    private String mMediaType;
    private ViewGroup mParent;
    private Type mPaymentType;
    private String mPriceAndSymbol;
    private String mSKU;
    private ArrayAdapter<CharSequence> monthAdapter;
    private Spinner monthSpinner;
    private String[] months;
    private TextView paymentCostText;
    private EditText securityCodeField;
    private Button startButton;
    private ArrayAdapter<CharSequence> yearAdapter;
    private Spinner yearSpinner;
    private String[] years;
    private EditText zipCodeField;
    
    public PaymentFragment() {
        this.mListener = new BaseListener<Void>() {
            @Override
            public void onException(final Exception ex) {
                EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.ERROR_UNKNOWN.get()));
            }
            
            @Override
            public void onFinally() {
                Util.hideProgressBarHideChildren((Context)PaymentFragment.this.getActivity(), PaymentFragment.this.mParent);
            }
            
            @Override
            public void onInterrupted(final Exception ex) {
                this.onException(ex);
            }
            
            @Override
            public void onPreExecute() {
                Util.showProgressBarHideChildren((Context)PaymentFragment.this.getActivity(), PaymentFragment.this.mParent, PaymentFragment.this.getResources().getColor(2131558536));
            }
            
            @Override
            public void onSuccess(final Void void1) {
                if (PaymentFragment.this.mIsFreeTrial && CrunchyrollApplication.getApp((Context)PaymentFragment.this.getActivity()).getPrepareToWatch().getMembershipInfoItem() != null) {
                    PaymentFragment.this.mIsFreeTrial = false;
                    EventBus.getDefault().post(new Upsell.NotQualifiedEvent());
                }
                PaymentFragment.this.finalizeUI();
            }
        };
    }
    
    private void finalizeUI() {
        final PrepareToWatch prepareToWatch = CrunchyrollApplication.getApp((Context)this.getActivity()).getPrepareToWatch();
        if (prepareToWatch.getMembershipInfoItem() != null) {
            this.mSKU = prepareToWatch.getMembershipInfoItem().getSku();
            this.mMediaType = prepareToWatch.getMembershipInfoItem().getMediaType();
            this.mPriceAndSymbol = prepareToWatch.getMembershipInfoItem().getPriceAndSymbol();
            this.mCurrency = prepareToWatch.getMembershipInfoItem().getCurrency();
            this.mIsFreeTrial = false;
        }
        else if (prepareToWatch.getFreeTrialInfoItem() != null) {
            this.mSKU = prepareToWatch.getFreeTrialInfoItem().getSku();
            this.mMediaType = prepareToWatch.getFreeTrialInfoItem().getMediaType();
            this.mPriceAndSymbol = prepareToWatch.getFreeTrialInfoItem().getPriceAndSymbol();
            this.mCurrency = prepareToWatch.getFreeTrialInfoItem().getCurrency();
            this.mIsFreeTrial = true;
        }
        this.mParent.findViewById(2131624066).setVisibility(0);
        if (this.mIsFreeTrial) {
            this.paymentCostText.setText((CharSequence)String.format(LocalizedStrings.FREETRIAL_COST_2.get(), this.mPriceAndSymbol));
            this.startButton.setText((CharSequence)LocalizedStrings.START_FREE_TRIAL.get());
        }
        else {
            this.paymentCostText.setText((CharSequence)String.format(LocalizedStrings.FREETRIAL_COST_3.get(), this.mPriceAndSymbol));
            this.startButton.setText((CharSequence)LocalizedStrings.UPGRADE_NOW.get());
        }
        this.startButton.setOnClickListener((View$OnClickListener)this);
        (this.countrySpinnerAdapter = (ArrayAdapter<CharSequence>)new ArrayAdapter((Context)this.getActivity(), 2130903182, 2131624348, (Object[])this.getActivity().getResources().getStringArray(2131492865))).setDropDownViewResource(2130903183);
        this.countrySpinner.setAdapter((SpinnerAdapter)this.countrySpinnerAdapter);
        (this.monthAdapter = (ArrayAdapter<CharSequence>)new ArrayAdapter((Context)this.getActivity(), 2130903182, 2131624348, (Object[])this.months)).setDropDownViewResource(2130903183);
        this.monthSpinner.setAdapter((SpinnerAdapter)this.monthAdapter);
        (this.yearAdapter = (ArrayAdapter<CharSequence>)new ArrayAdapter((Context)this.getActivity(), 2130903182, 2131624348, (Object[])this.years)).setDropDownViewResource(2130903183);
        this.yearSpinner.setAdapter((SpinnerAdapter)this.yearAdapter);
        final int index = Arrays.asList(this.countryCodes).indexOf(this.getApplicationState().getSession().getCountryCode());
        if (index != -1) {
            this.countrySpinner.setSelection(index);
        }
        Util.hideProgressBarHideChildren((Context)this.getActivity(), this.mParent);
    }
    
    public static PaymentFragment newInstance(final boolean b, final Type type) {
        final PaymentFragment paymentFragment = new PaymentFragment();
        final Bundle arguments = new Bundle();
        arguments.putBoolean("isFreeTrial", b);
        arguments.putSerializable("paymentType", (Serializable)type);
        paymentFragment.setArguments(arguments);
        return paymentFragment;
    }
    
    private void startPayment(final String s, final String s2, final String s3, final String s4, final String s5, final Integer n, final Integer n2, final String s6, final String s7, final String s8) {
        final BaseListener<Void> baseListener = new BaseListener<Void>() {
            @Override
            public void onException(final Exception ex) {
                if (PaymentFragment.this.mIsFreeTrial) {
                    Tracker.freeTrialFail((Context)PaymentFragment.this.getActivity());
                }
                else {
                    Tracker.membershipUpgradeFail((Context)PaymentFragment.this.getActivity());
                }
                boolean b = false;
                if (ex instanceof ApiErrorException) {
                    b = b;
                    if (((ApiErrorException)ex).getErrorCode() == ApiErrorException.ApiErrorCode.DUPLICATE_OBJECT) {
                        b = true;
                    }
                }
                if (b) {
                    EventBus.getDefault().post(new Upsell.CardAlreadyUsedEvent());
                    return;
                }
                String s = LocalizedStrings.ERROR_UNKNOWN.get();
                if (ex instanceof ApiErrorException) {
                    s = ex.getLocalizedMessage();
                }
                EventBus.getDefault().post(new Upsell.PaymentExceptionEvent(s));
            }
            
            @Override
            public void onFinally() {
                super.onFinally();
                Util.hideProgressBar((Context)PaymentFragment.this.getActivity(), PaymentFragment.this.mParent);
            }
            
            @Override
            public void onPreExecute() {
                Util.showProgressBar((Context)PaymentFragment.this.getActivity(), PaymentFragment.this.mParent, PaymentFragment.this.getResources().getColor(2131558473));
            }
            
            @Override
            public void onSuccess(final Void void1) {
                if (PaymentFragment.this.mIsFreeTrial) {
                    Tracker.freeTrialSuccess((Context)PaymentFragment.this.getActivity());
                }
                else {
                    Tracker.membershipUpgradeSuccess((Context)PaymentFragment.this.getActivity());
                }
                final ApplicationState applicationState = PaymentFragment.this.getApplicationState();
                final User loggedInUser = applicationState.getLoggedInUser().get();
                loggedInUser.setPremium(PaymentFragment.this.mMediaType);
                applicationState.setLoggedInUser(loggedInUser);
                EventBus.getDefault().post(new Upsell.OkEvent());
                final Intent intent = new Intent();
                intent.setAction("PAYMENT_INFO_SENT");
                LocalBroadcastManager.getInstance((Context)PaymentFragment.this.getActivity()).sendBroadcast(intent);
            }
        };
        final PaymentInformation build = new PaymentInformation.Builder().sku(s).currencyCode(s2).firstName(s3).lastName(s4).cardNumber(s5).expirationMonth(n).expirationYear(n2).securityCode(s6).zipCode(s7).countryCode(s8).build();
        if (this.mIsFreeTrial) {
            ApiManager.getInstance(this.getActivity()).startFreeTrial(baseListener, build);
            return;
        }
        ApiManager.getInstance(this.getActivity()).startMembership(baseListener, build);
    }
    
    private boolean validateForm() {
        return this.validateFormFieldWithErrorString(this.cardNumberField, LocalizedStrings.ERROR_MISSING_CC_NUMBER) && this.validateFormFieldWithErrorString(this.securityCodeField, LocalizedStrings.ERROR_MISSING_CC_SEC_CODE) && this.validateFormFieldWithErrorString(this.zipCodeField, LocalizedStrings.ERROR_MISSING_ZIPCODE);
    }
    
    private boolean validateFormFieldWithErrorString(final EditText editText, final LocalizedStrings localizedStrings) {
        if (editText.getText().toString().trim().length() == 0) {
            EventBus.getDefault().post(new Upsell.ValidationErrorEvent(localizedStrings.get(), editText));
            return false;
        }
        return true;
    }
    
    public void onClick(final View view) {
        if (view == this.startButton) {
            this.mDidGoForward = true;
            Tracker.upsellPayment((Context)this.getActivity(), this.mIsFreeTrial);
            if (this.validateForm()) {
                ((InputMethodManager)view.getContext().getSystemService("input_method")).hideSoftInputFromWindow(this.getView().getWindowToken(), 0);
                this.startPayment(this.mSKU, this.mCurrency, "", "", this.cardNumberField.getText().toString(), Integer.parseInt(this.months[this.monthSpinner.getSelectedItemPosition()]), Integer.parseInt(this.years[this.yearSpinner.getSelectedItemPosition()]), this.securityCodeField.getText().toString(), this.zipCodeField.getText().toString(), this.countryCodes[this.countrySpinner.getSelectedItemPosition()]);
            }
        }
    }
    
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.trackView();
        final Bundle arguments = this.getArguments();
        this.mIsFreeTrial = Extras.getBoolean(arguments, "isFreeTrial").get();
        this.mPaymentType = (Type)arguments.getSerializable("paymentType");
        if (bundle != null) {
            this.mIsFreeTrial = bundle.getBoolean("isFreeTrial");
        }
        this.countryCodes = this.getResources().getStringArray(2131492864);
        this.months = this.getResources().getStringArray(2131492866);
        this.years = new String[10];
        final int value = Calendar.getInstance().get(1);
        for (int i = 0; i < 10; ++i) {
            this.years[i] = Integer.toString(value + i);
        }
        if (!this.getApplicationState().hasLoggedInUser()) {
            this.getActivity().setResult(12);
            this.getActivity().finish();
        }
        Tracker.swrveScreenView("payment-info");
        this.mDidGoForward = false;
    }
    
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        this.mParent = (ViewGroup)layoutInflater.inflate(2130903121, viewGroup, false);
        final View viewById = this.mParent.findViewById(2131624197);
        final TextView textView = (TextView)this.mParent.findViewById(2131624207);
        final TextView textView2 = (TextView)this.mParent.findViewById(2131624209);
        final TextView textView3 = (TextView)this.mParent.findViewById(2131624213);
        this.cardNumberField = (EditText)this.mParent.findViewById(2131624205);
        this.securityCodeField = (EditText)this.mParent.findViewById(2131624211);
        this.zipCodeField = (EditText)this.mParent.findViewById(2131624212);
        final TextView textView4 = (TextView)this.mParent.findViewById(2131624020);
        this.paymentCostText = (TextView)this.mParent.findViewById(2131624214);
        this.startButton = (Button)this.mParent.findViewById(2131624215);
        this.monthSpinner = (Spinner)this.mParent.findViewById(2131624208);
        this.yearSpinner = (Spinner)this.mParent.findViewById(2131624210);
        this.countrySpinner = (Spinner)this.mParent.findViewById(2131624170);
        viewById.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                EventBus.getDefault().post(new Upsell.BackEvent());
            }
        });
        textView.setText((CharSequence)LocalizedStrings.PAYMENT_MONTH.get());
        textView2.setText((CharSequence)LocalizedStrings.PAYMENT_YEAR.get());
        textView3.setText((CharSequence)LocalizedStrings.FREETRIAL_COUNTRY.get());
        this.cardNumberField.setHint((CharSequence)Html.fromHtml(LocalizedStrings.CREDIT_CARD_NUMBER.get()));
        this.securityCodeField.setHint((CharSequence)Html.fromHtml(LocalizedStrings.CVV.get()));
        this.zipCodeField.setHint((CharSequence)Html.fromHtml(LocalizedStrings.FREETRIAL_ZIP_CODE.get()));
        switch (this.mPaymentType) {
            case TYPE_ONLY_STEP: {
                textView4.setText((CharSequence)LocalizedStrings.PAYMENT_INFO.get());
                break;
            }
            case TYPE_SECOND_STEP: {
                textView4.setText((CharSequence)(LocalizedStrings.STEP_2_OF_2.get() + " " + LocalizedStrings.PAYMENT_INFO.get()));
                break;
            }
        }
        return (View)this.mParent;
    }
    
    public void onDestroy() {
        super.onDestroy();
        if (!this.mDidGoForward) {
            Tracker.upsellPaymentExit();
        }
    }
    
    public void onEvent(final Upsell.ContinueUpgradeEvent continueUpgradeEvent) {
        this.mIsFreeTrial = false;
        this.paymentCostText.setText((CharSequence)String.format(LocalizedStrings.FREETRIAL_COST_3.get(), this.mPriceAndSymbol));
        this.startButton.setText((CharSequence)LocalizedStrings.UPGRADE_NOW.get());
    }
    
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("isFreeTrial", this.mIsFreeTrial);
    }
    
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    
    public void onViewCreated(final View view, @Nullable final Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (bundle != null) {
            this.finalizeUI();
            return;
        }
        final PrepareToWatch prepareToWatch = CrunchyrollApplication.getApp((Context)this.getActivity()).getPrepareToWatch();
        if (this.mIsFreeTrial) {
            prepareToWatch.refreshFreeTrialInfo(this.mListener);
            return;
        }
        prepareToWatch.refreshMembershipTrialInfo(this.mListener);
    }
    
    public enum Type
    {
        TYPE_ONLY_STEP, 
        TYPE_SECOND_STEP;
    }
}
