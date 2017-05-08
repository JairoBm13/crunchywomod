// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import com.crunchyroll.crunchyroid.app.ApplicationState;
import com.crunchyroll.crunchyroid.tracking.FacebookTracker;
import com.crunchyroll.crunchyroid.util.Util;
import com.crunchyroll.android.api.exceptions.ApiErrorException;
import com.crunchyroll.android.api.AbstractApiRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.crunchyroll.android.api.ApiService;
import com.fasterxml.jackson.core.ObjectCodec;
import com.crunchyroll.android.api.ApiRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.crunchyroll.android.api.requests.LoginRequest;
import com.crunchyroll.android.api.requests.SignupRequest;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import com.crunchyroll.android.api.models.Login;
import com.crunchyroll.android.util.SafeAsyncTask;
import com.crunchyroll.crunchyroid.events.Upsell;
import de.greenrobot.event.EventBus;
import android.content.Context;
import android.view.View$OnClickListener;
import android.text.Html;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.view.View;
import android.view.LayoutInflater;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import com.crunchyroll.crunchyroid.app.Extras;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

public class SignupFragment extends BaseFragment
{
    private Button continueButton;
    private TextView forgotPasswordButton;
    private EditText loginField;
    private boolean mIsNewUser;
    private CreateAccountLoginPillTabsPopupFragment.Type mType;
    private ViewGroup mView;
    private EditText passwordField;
    
    public static SignupFragment newInstance(final boolean b, final CreateAccountLoginPillTabsPopupFragment.Type type) {
        final SignupFragment signupFragment = new SignupFragment();
        final Bundle arguments = new Bundle();
        Extras.putBoolean(arguments, "newUser", b);
        Extras.putSerializable(arguments, "type", type);
        signupFragment.setArguments(arguments);
        return signupFragment;
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.trackView();
        this.mIsNewUser = Extras.getBoolean(this.getArguments(), "newUser").get();
        this.mType = (CreateAccountLoginPillTabsPopupFragment.Type)this.getArguments().getSerializable("type");
        if (this.mIsNewUser) {
            if (this.mType == CreateAccountLoginPillTabsPopupFragment.Type.TYPE_BACKABLE) {
                Tracker.swrveScreenView("create-account");
                return;
            }
            Tracker.swrveScreenView("free-account-create");
        }
        else {
            if (this.mType == CreateAccountLoginPillTabsPopupFragment.Type.TYPE_BACKABLE) {
                Tracker.swrveScreenView("login");
                return;
            }
            Tracker.swrveScreenView("free-account-login");
        }
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return (View)(this.mView = (ViewGroup)layoutInflater.inflate(2130903123, viewGroup, false));
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.loginField = (EditText)view.findViewById(2131624216);
        this.passwordField = (EditText)view.findViewById(2131624217);
        this.continueButton = (Button)view.findViewById(2131624218);
        this.forgotPasswordButton = (TextView)view.findViewById(2131624219);
        if (this.mIsNewUser) {
            this.loginField.setHint((CharSequence)Html.fromHtml(LocalizedStrings.YOUR_EMAIL.get()));
        }
        else {
            this.loginField.setHint((CharSequence)Html.fromHtml(LocalizedStrings.YOUR_EMAIL_OR_USERNAME.get()));
        }
        this.passwordField.setHint((CharSequence)Html.fromHtml(LocalizedStrings.PASSWORD_PROMPT.get()));
        this.continueButton.setText((CharSequence)LocalizedStrings.CONTINUE_STR.get());
        this.continueButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (SignupFragment.this.mIsNewUser) {
                    if (SignupFragment.this.mType == CreateAccountLoginPillTabsPopupFragment.Type.TYPE_BACKABLE) {
                        Tracker.upsellCreateAccount((Context)SignupFragment.this.getActivity());
                    }
                }
                else if (SignupFragment.this.mType == CreateAccountLoginPillTabsPopupFragment.Type.TYPE_BACKABLE) {
                    Tracker.upsellLogin((Context)SignupFragment.this.getActivity());
                }
                new SignupTask(SignupFragment.this.loginField.getText().toString(), SignupFragment.this.passwordField.getText().toString()).execute();
            }
        });
        this.forgotPasswordButton.setText((CharSequence)LocalizedStrings.FORGOT_PASSWORD.get());
        if (this.mIsNewUser) {
            this.forgotPasswordButton.setVisibility(4);
            return;
        }
        this.forgotPasswordButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                Tracker.forgotPassword((Context)SignupFragment.this.getActivity());
                EventBus.getDefault().post(new Upsell.ForgotPasswordEvent());
            }
        });
    }
    
    private class SignupTask extends SafeAsyncTask<Login>
    {
        private final String login;
        private final String password;
        
        public SignupTask(final String login, final String password) {
            this.login = login;
            this.password = password;
        }
        
        @Override
        public Login call() throws Exception {
            final CrunchyrollApplication crunchyrollApplication = (CrunchyrollApplication)SignupFragment.this.getActivity().getApplication();
            final ApiService apiService = crunchyrollApplication.getApiService();
            final ObjectMapper objectMapper = crunchyrollApplication.getObjectMapper();
            AbstractApiRequest abstractApiRequest;
            if (SignupFragment.this.mIsNewUser) {
                Tracker.createAccountAttempt((Context)SignupFragment.this.getActivity());
                Tracker.userCreateAccount("attempt");
                abstractApiRequest = new SignupRequest(this.login, this.password);
            }
            else {
                Tracker.logInAttempt((Context)SignupFragment.this.getActivity());
                Tracker.userLogin("attempt");
                abstractApiRequest = new LoginRequest(this.login, this.password, null);
            }
            return objectMapper.readValue(((JsonNode)apiService.run(abstractApiRequest).body.asParser(objectMapper).readValueAsTree()).path("data").traverse(), Login.class);
        }
        
        @Override
        protected void onException(final Exception ex) throws RuntimeException {
            if (SignupFragment.this.mIsNewUser) {
                if (SignupFragment.this.mType == CreateAccountLoginPillTabsPopupFragment.Type.TYPE_BACKABLE) {
                    Tracker.upsellCreateAccountFailure((Context)SignupFragment.this.getActivity());
                }
                else {
                    Tracker.createAccountFail((Context)SignupFragment.this.getActivity());
                }
                Tracker.userCreateAccount("failure");
            }
            else {
                if (SignupFragment.this.mType == CreateAccountLoginPillTabsPopupFragment.Type.TYPE_BACKABLE) {
                    Tracker.upsellLoginFailure((Context)SignupFragment.this.getActivity());
                }
                Tracker.userLogin("failure");
            }
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
        }
        
        @Override
        protected void onFinally() throws RuntimeException {
            SignupFragment.this.loginField.setEnabled(true);
            SignupFragment.this.passwordField.setEnabled(true);
            SignupFragment.this.continueButton.setEnabled(true);
            Util.hideProgressBar((Context)SignupFragment.this.getActivity(), SignupFragment.this.mView);
        }
        
        @Override
        protected void onPreExecute() {
            SignupFragment.this.loginField.setEnabled(false);
            SignupFragment.this.passwordField.setEnabled(false);
            SignupFragment.this.continueButton.setEnabled(false);
            Util.showProgressBar((Context)SignupFragment.this.getActivity(), SignupFragment.this.mView, SignupFragment.this.getResources().getColor(2131558519));
        }
        
        @Override
        protected void onSuccess(final Login login) throws Exception {
            final ApplicationState applicationState = SignupFragment.this.getApplicationState();
            applicationState.setAuth(login.getAuth());
            applicationState.setLoggedInUser(login.getUser());
            if (SignupFragment.this.mIsNewUser) {
                if (SignupFragment.this.mType == CreateAccountLoginPillTabsPopupFragment.Type.TYPE_BACKABLE) {
                    Tracker.upsellCreateAccountSuccess((Context)SignupFragment.this.getActivity());
                }
                else {
                    Tracker.createAccountSuccess((Context)SignupFragment.this.getActivity());
                }
                Tracker.userCreateAccount("success");
            }
            else {
                if (SignupFragment.this.mType == CreateAccountLoginPillTabsPopupFragment.Type.TYPE_BACKABLE) {
                    Tracker.upsellLoginSuccess((Context)SignupFragment.this.getActivity());
                }
                Tracker.userLogin("success");
            }
            if (applicationState.isAnyPremium() && !SignupFragment.this.mIsNewUser) {
                EventBus.getDefault().post(new Upsell.AlreadyPremiumEvent(LocalizedStrings.OK.get(), false));
                return;
            }
            FacebookTracker.signup((Context)SignupFragment.this.getActivity());
            if (SignupFragment.this.mIsNewUser) {
                EventBus.getDefault().post(new Upsell.AccountCreatedEvent());
                return;
            }
            EventBus.getDefault().post(new Upsell.LoggedInEvent());
        }
    }
}
