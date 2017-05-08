// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import com.google.common.base.Optional;
import com.crunchyroll.crunchyroid.activities.ContactUsActivity;
import com.crunchyroll.crunchyroid.activities.HelpActivity;
import com.crunchyroll.crunchyroid.activities.PrivacyPolicyActivity;
import android.text.TextUtils;
import com.crunchyroll.android.api.models.User;
import android.app.Fragment;
import com.crunchyroll.crunchyroid.events.ErrorEvent;
import de.greenrobot.event.EventBus;
import com.crunchyroll.android.api.exceptions.ApiNetworkException;
import com.crunchyroll.crunchyroid.app.PrepareToWatch;
import android.widget.Button;
import android.view.ViewTreeObserver;
import android.os.Build$VERSION;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import com.crunchyroll.crunchyroid.util.Util;
import android.view.View$OnClickListener;
import android.widget.TextView;
import android.view.ViewGroup$LayoutParams;
import android.widget.AbsListView$LayoutParams;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.crunchyroll.crunchyroid.app.Extras;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import android.preference.Preference;
import android.preference.Preference$OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import com.crashlytics.android.Crashlytics;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import java.util.Iterator;
import java.util.ArrayList;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.content.pm.PackageManager$NameNotFoundException;
import java.util.ListIterator;
import com.crunchyroll.android.api.tasks.ApiTaskListener;
import com.crunchyroll.android.api.ApiManager;
import android.preference.ListPreference;
import java.io.Serializable;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.app.AlertDialog$Builder;
import android.content.Context;
import com.crunchyroll.crunchyroid.app.ApplicationState;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.view.View;
import com.crunchyroll.android.api.models.LocaleData;
import java.util.List;
import android.widget.ListView;
import android.app.AlertDialog;
import java.util.Map;
import com.crunchyroll.android.api.tasks.BaseListener;
import android.content.SharedPreferences$OnSharedPreferenceChangeListener;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences$OnSharedPreferenceChangeListener
{
    private static final String EXTRAS_LOCALIZED_STRINGS_TASK_ID = "localizedStringstTaskId";
    private BaseListener<Map<String, String>> mDownloadLocalizedStringsListener;
    private AlertDialog mDownloadingStringsDialog;
    private ListView mList;
    private BaseListener<List<LocaleData>> mListLocalesListener;
    List<LocaleData> mLocaleList;
    private String mLocalizedStringsTaskId;
    private View mParent;
    private String mPreviousLocale;
    
    public SettingsFragment() {
        this.mDownloadingStringsDialog = null;
        this.mLocalizedStringsTaskId = null;
        this.mListLocalesListener = new BaseListener<List<LocaleData>>() {
            @Override
            public void onException(final Exception ex) {
                SettingsFragment.this.getLocalePreference().setSummary((CharSequence)LocalizedStrings.LOADING_LANGUAGES_ERROR.get());
            }
            
            @Override
            public void onPreExecute() {
                SettingsFragment.this.getLocalePreference().setEnabled(false);
                SettingsFragment.this.getLocalePreference().setSelectable(false);
                SettingsFragment.this.getLocalePreference().setSummary((CharSequence)LocalizedStrings.LOADING.get());
            }
            
            @Override
            public void onSuccess(final List<LocaleData> mLocaleList) {
                SettingsFragment.this.mLocaleList = mLocaleList;
                SettingsFragment.this.updateLocaleList();
                SettingsFragment.this.getLocalePreference().setEnabled(true);
                SettingsFragment.this.getLocalePreference().setSelectable(true);
                SettingsFragment.this.updateLocalePreferenceSummary();
                ApplicationState.get((Context)SettingsFragment.this.getActivity()).getCustomLocale();
            }
        };
        this.mDownloadLocalizedStringsListener = new BaseListener<Map<String, String>>() {
            @Override
            public void onException(final Exception ex) {
                if (SettingsFragment.this.mDownloadingStringsDialog != null) {
                    SettingsFragment.this.mDownloadingStringsDialog.dismiss();
                    SettingsFragment.this.mDownloadingStringsDialog = null;
                }
                if (ex instanceof InterruptedException) {
                    return;
                }
                final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)SettingsFragment.this.getActivity());
                alertDialog$Builder.setCancelable(false);
                alertDialog$Builder.setMessage((CharSequence)LocalizedStrings.ERROR_DOWNLOADING_STRINGS.get());
                alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.RETRY.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        SettingsFragment.this.downloadLocalizedStrings();
                    }
                });
                alertDialog$Builder.setNegativeButton((CharSequence)LocalizedStrings.CANCEL.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        SettingsFragment.this.rollbackLocale();
                    }
                });
                alertDialog$Builder.show();
            }
            
            @Override
            public void onSuccess(final Map<String, String> strings) {
                if (SettingsFragment.this.mDownloadingStringsDialog != null) {
                    SettingsFragment.this.mDownloadingStringsDialog.dismiss();
                }
                LocalizedStrings.setStrings(strings);
                LocalBroadcastManager.getInstance((Context)SettingsFragment.this.getActivity()).sendBroadcast(new Intent("LOCALE_CHANGED"));
                final Intent intent = SettingsFragment.this.getActivity().getIntent();
                intent.putExtra("list", (Serializable)SettingsFragment.this.mLocaleList);
                intent.setFlags(67174400);
                SettingsFragment.this.getActivity().finish();
                SettingsFragment.this.getActivity().startActivity(intent);
            }
        };
    }
    
    private void downloadLocalizedStrings() {
        this.mLocalizedStringsTaskId = ApiManager.getInstance(this.getActivity()).getLocalizedStrings(this.mDownloadLocalizedStringsListener);
        this.showUpdatingLanguageDialog();
    }
    
    private String findLabelForLocale(final String s) {
        final ListIterator<LocaleData> listIterator = this.mLocaleList.listIterator();
        while (listIterator.hasNext()) {
            final LocaleData localeData = listIterator.next();
            if (localeData.getLocaleId().equals(s)) {
                return localeData.getLabel();
            }
        }
        return null;
    }
    
    private String getAppVersionName() {
        try {
            return this.getActivity().getPackageManager().getPackageInfo(this.getActivity().getPackageName(), 0).versionName;
        }
        catch (PackageManager$NameNotFoundException ex) {
            return "";
        }
    }
    
    private ListPreference getLocalePreference() {
        return (ListPreference)this.findPreference((CharSequence)"userLocale");
    }
    
    public static SettingsFragment newInstance() {
        final SettingsFragment settingsFragment = new SettingsFragment();
        settingsFragment.setArguments(new Bundle());
        return settingsFragment;
    }
    
    private void rollbackLocale() {
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences((Context)this.getActivity().getApplication());
        defaultSharedPreferences.unregisterOnSharedPreferenceChangeListener((SharedPreferences$OnSharedPreferenceChangeListener)this);
        defaultSharedPreferences.edit().putString("userLocale", this.mPreviousLocale).commit();
        defaultSharedPreferences.registerOnSharedPreferenceChangeListener((SharedPreferences$OnSharedPreferenceChangeListener)this);
        this.updateLocalePreferenceSummary();
    }
    
    private void showUpdatingLanguageDialog() {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this.getActivity());
        alertDialog$Builder.setMessage((CharSequence)LocalizedStrings.DOWNLOADING_STRINGS_MESSAGE.get());
        alertDialog$Builder.setCancelable(true);
        alertDialog$Builder.setNegativeButton((CharSequence)LocalizedStrings.CANCEL.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                SettingsFragment.this.mDownloadingStringsDialog = null;
                SettingsFragment.this.rollbackLocale();
                if (SettingsFragment.this.mLocalizedStringsTaskId != null) {
                    ApiManager.getInstance(SettingsFragment.this.getActivity()).cancelTask(SettingsFragment.this.mLocalizedStringsTaskId);
                }
            }
        });
        (this.mDownloadingStringsDialog = alertDialog$Builder.create()).show();
    }
    
    private void updateLocaleList() {
        final ArrayList<String> list = new ArrayList<String>();
        final ArrayList<String> list2 = new ArrayList<String>();
        for (final LocaleData localeData : this.mLocaleList) {
            list.add(localeData.getLabel());
            list2.add(localeData.getLocaleId());
        }
        final String[] entries = new String[list.size()];
        list.toArray(entries);
        final String[] entryValues = new String[list2.size()];
        list2.toArray(entryValues);
        this.getLocalePreference().setEntries((CharSequence[])entries);
        this.getLocalePreference().setEntryValues((CharSequence[])entryValues);
    }
    
    private void updateLocalePreferenceSummary() {
        String customLocale = ApplicationState.get((Context)this.getActivity()).getCustomLocale();
        final String labelForLocale = this.findLabelForLocale(customLocale);
        if (labelForLocale != null) {
            this.getLocalePreference().setSummary((CharSequence)labelForLocale);
            this.getLocalePreference().setValue(customLocale);
        }
        else {
            customLocale = "enUS";
            final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences((Context)this.getActivity().getApplication());
            defaultSharedPreferences.unregisterOnSharedPreferenceChangeListener((SharedPreferences$OnSharedPreferenceChangeListener)this);
            this.getLocalePreference().setSummary((CharSequence)this.findLabelForLocale("enUS"));
            this.getLocalePreference().setValue("enUS");
            defaultSharedPreferences.registerOnSharedPreferenceChangeListener((SharedPreferences$OnSharedPreferenceChangeListener)this);
        }
        if (!CrunchyrollApplication.isDebuggable()) {
            Crashlytics.setString("locale", customLocale);
        }
    }
    
    private void updateLocalizedStrings() {
        final ApplicationState value = ApplicationState.get((Context)this.getActivity());
        final PreferenceCategory preferenceCategory = (PreferenceCategory)this.findPreference((CharSequence)"generalCategory");
        preferenceCategory.setTitle((CharSequence)LocalizedStrings.GENERAL.get());
        final Preference preference = this.findPreference((CharSequence)"imageLoadingEnabled");
        preference.setTitle((CharSequence)LocalizedStrings.LOAD_IMAGES.get());
        preference.setSummary((CharSequence)LocalizedStrings.LOAD_IMAGES_SUMMARY.get());
        final Preference preference2 = this.findPreference((CharSequence)"userLocale");
        preference2.setOnPreferenceClickListener((Preference$OnPreferenceClickListener)new Preference$OnPreferenceClickListener() {
            public boolean onPreferenceClick(final Preference preference) {
                Tracker.swrveScreenView("settings-preferred-language");
                return false;
            }
        });
        preference2.setTitle((CharSequence)LocalizedStrings.PREFERRED_LANGUAGE.get());
        final Preference preference3 = this.findPreference((CharSequence)"videoQuality");
        if (value.isAnyPremium()) {
            preference3.setTitle((CharSequence)LocalizedStrings.VIDEO_QUALITY.get());
            preference3.setOnPreferenceClickListener((Preference$OnPreferenceClickListener)new Preference$OnPreferenceClickListener() {
                public boolean onPreferenceClick(final Preference preference) {
                    Tracker.swrveScreenView("settings-video-quality");
                    return false;
                }
            });
            this.setVideoQualities((ListPreference)preference3);
            preference3.setSummary(((ListPreference)preference3).getEntry());
        }
        else {
            preferenceCategory.removePreference(preference3);
        }
        ((PreferenceCategory)this.findPreference((CharSequence)"aboutCategory")).setTitle((CharSequence)LocalizedStrings.ABOUT.get());
        this.findPreference((CharSequence)"user").setTitle((CharSequence)LocalizedStrings.CURRENT_USER.get());
        this.findPreference((CharSequence)"privacy").setTitle((CharSequence)LocalizedStrings.PRIVACY_POLICY.get());
        this.findPreference((CharSequence)"version").setTitle((CharSequence)LocalizedStrings.VERSION.get());
        this.findPreference((CharSequence)"help").setTitle((CharSequence)LocalizedStrings.HELP.get());
        this.findPreference((CharSequence)"contact_us").setTitle((CharSequence)LocalizedStrings.CONTACT_US.get());
        ((PreferenceCategory)this.findPreference((CharSequence)"supportCategory")).setTitle((CharSequence)LocalizedStrings.SUPPORT.get());
    }
    
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mPreviousLocale = ApplicationState.get((Context)this.getActivity()).getCustomLocale();
        this.addPreferencesFromResource(2131034112);
        final Bundle extras = this.getActivity().getIntent().getExtras();
        if (bundle != null && bundle.containsKey("list")) {
            this.mLocaleList = Extras.getList(bundle, "list", LocaleData.class).get();
        }
        else if (extras != null && extras.containsKey("list")) {
            this.mLocaleList = Extras.getList(this.getActivity().getIntent().getExtras(), "list", LocaleData.class).get();
        }
        this.mLocalizedStringsTaskId = Extras.getString(bundle, "localizedStringstTaskId").orNull();
        PreferenceManager.getDefaultSharedPreferences((Context)this.getActivity()).registerOnSharedPreferenceChangeListener((SharedPreferences$OnSharedPreferenceChangeListener)this);
        Tracker.swrveScreenView("settings");
    }
    
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        this.mParent = layoutInflater.inflate(2130903131, viewGroup, false);
        (this.mList = (ListView)this.mParent.findViewById(16908298)).setDividerHeight(0);
        if (ApplicationState.get((Context)this.getActivity()).hasLoggedInUser()) {
            final View view = new View((Context)this.getActivity());
            view.setTag((Object)this.getResources().getString(2131165547));
            view.setLayoutParams((ViewGroup$LayoutParams)new AbsListView$LayoutParams(-1, 0));
            final View inflate = layoutInflater.inflate(2130903173, (ViewGroup)this.mList, false);
            ((TextView)inflate.findViewById(2131624339)).setText((CharSequence)LocalizedStrings.LOG_OUT.get());
            inflate.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    Tracker.settingsLogout();
                    Util.showLogoutPrompt(SettingsFragment.this.getActivity());
                    Tracker.swrveScreenView("settings-log-out");
                }
            });
            final ViewTreeObserver viewTreeObserver = this.mList.getViewTreeObserver();
            if (viewTreeObserver != null) {
                viewTreeObserver.addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        final ViewTreeObserver viewTreeObserver = SettingsFragment.this.mList.getViewTreeObserver();
                        if (viewTreeObserver.isAlive()) {
                            if (Build$VERSION.SDK_INT >= 16) {
                                viewTreeObserver.removeOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                            }
                            else {
                                viewTreeObserver.removeGlobalOnLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                            }
                        }
                        if (SettingsFragment.this.getActivity() != null) {
                            int n = SettingsFragment.this.mList.getDividerHeight() * (SettingsFragment.this.mList.getChildCount() - 1) + SettingsFragment.this.mList.getPaddingTop() + SettingsFragment.this.mList.getListPaddingBottom();
                            int n2;
                            for (int i = 0; i < SettingsFragment.this.mList.getChildCount(); ++i, n = n2) {
                                final View child = SettingsFragment.this.mList.getChildAt(i);
                                n2 = n;
                                if (!SettingsFragment.this.getResources().getString(2131165547).equals(child.getTag())) {
                                    n2 = n + child.getMeasuredHeight();
                                }
                            }
                            if (n < SettingsFragment.this.mParent.getMeasuredHeight()) {
                                view.setLayoutParams((ViewGroup$LayoutParams)new AbsListView$LayoutParams(-1, SettingsFragment.this.mParent.getMeasuredHeight() - n));
                            }
                        }
                    }
                });
            }
            this.mList.addFooterView(view);
            this.mList.addFooterView(inflate);
        }
        else {
            final View inflate2 = layoutInflater.inflate(2130903172, (ViewGroup)this.mList, false);
            ((Button)inflate2.findViewById(2131624241)).setText((CharSequence)LocalizedStrings.LOG_IN.get());
            inflate2.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    if (!CrunchyrollApplication.getApp((Context)SettingsFragment.this.getActivity()).isPrepareToWatchLoading()) {
                        final PrepareToWatch prepareToWatchNoMedia = CrunchyrollApplication.getApp((Context)SettingsFragment.this.getActivity()).prepareToWatchNoMedia(SettingsFragment.this.getActivity(), PrepareToWatch.Type.PREPARE_LOGIN, false, 0, null);
                        prepareToWatchNoMedia.prepare(new BaseListener<Void>() {
                            @Override
                            public void onException(final Exception ex) {
                                if (ex instanceof ApiNetworkException) {
                                    EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.ERROR_NETWORK.get()));
                                    return;
                                }
                                EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.ERROR_UNKNOWN.get()));
                            }
                            
                            @Override
                            public void onFinally() {
                                super.onFinally();
                                Util.hideProgressBar(SettingsFragment.this.getActivity());
                            }
                            
                            @Override
                            public void onPreExecute() {
                                Util.showProgressBar(SettingsFragment.this.getActivity(), SettingsFragment.this.getResources().getColor(2131558518));
                            }
                            
                            @Override
                            public void onSuccess(final Void void1) {
                                prepareToWatchNoMedia.go(PrepareToWatch.Event.NONE);
                            }
                        });
                        Tracker.settingsLogin();
                    }
                }
            });
            this.mList.addFooterView(inflate2);
        }
        return this.mParent;
    }
    
    public void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences((Context)this.getActivity()).unregisterOnSharedPreferenceChangeListener((SharedPreferences$OnSharedPreferenceChangeListener)this);
        if (this.mDownloadingStringsDialog != null) {
            this.mDownloadingStringsDialog.cancel();
            this.mDownloadingStringsDialog = null;
        }
        super.onDestroy();
    }
    
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Extras.putSerializable(bundle, "list", (Serializable)this.mLocaleList);
        Extras.putString(bundle, "localizedStringstTaskId", this.mLocalizedStringsTaskId);
    }
    
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String s) {
        if (s.equals("userLocale")) {
            CrunchyrollApplication.getApp((Context)this.getActivity()).getApiService().clearCache();
            this.downloadLocalizedStrings();
            Tracker.preferredLanguage((Context)this.getActivity(), ApplicationState.get((Context)this.getActivity()).getCustomLocale());
            Tracker.swrvePropertyLanguage(ApplicationState.get((Context)this.getActivity()).getCustomLocale());
        }
        else {
            if (s.equals("videoQuality")) {
                final ListPreference listPreference = (ListPreference)this.findPreference((CharSequence)"videoQuality");
                listPreference.setSummary(listPreference.getEntry());
                ApplicationState.get((Context)this.getActivity()).setVideoQualityPreference(listPreference.getValue());
                Tracker.changeVideoQuality((Context)this.getActivity(), listPreference.getEntry().toString());
                return;
            }
            if (s.equals("imageLoadingEnabled")) {
                Tracker.loadImages((Context)this.getActivity());
            }
        }
    }
    
    public void onStart() {
        super.onStart();
        Tracker.trackView((Fragment)this);
    }
    
    public void onViewCreated(final View view, final Bundle bundle) {
        this.updateLocalizedStrings();
        final Optional<User> loggedInUser = ApplicationState.get((Context)this.getActivity()).getLoggedInUser();
        final Preference preference = this.findPreference((CharSequence)"user");
        String summary;
        if (loggedInUser.isPresent()) {
            if (!TextUtils.isEmpty((CharSequence)loggedInUser.get().getUsername())) {
                summary = loggedInUser.get().getUsername();
            }
            else {
                summary = loggedInUser.get().getEmail();
            }
        }
        else {
            summary = LocalizedStrings.NOT_LOGGED_IN.get();
        }
        preference.setSummary((CharSequence)summary);
        this.findPreference((CharSequence)"privacy").setOnPreferenceClickListener((Preference$OnPreferenceClickListener)new Preference$OnPreferenceClickListener() {
            public boolean onPreferenceClick(final Preference preference) {
                Tracker.settingsPrivacyPolicy((Context)SettingsFragment.this.getActivity());
                PrivacyPolicyActivity.start(SettingsFragment.this.getActivity());
                return true;
            }
        });
        this.findPreference((CharSequence)"version").setSummary((CharSequence)this.getAppVersionName());
        this.findPreference((CharSequence)"help").setOnPreferenceClickListener((Preference$OnPreferenceClickListener)new Preference$OnPreferenceClickListener() {
            public boolean onPreferenceClick(final Preference preference) {
                Tracker.settingsHelp((Context)SettingsFragment.this.getActivity());
                HelpActivity.start(SettingsFragment.this.getActivity());
                return true;
            }
        });
        this.findPreference((CharSequence)"contact_us").setOnPreferenceClickListener((Preference$OnPreferenceClickListener)new Preference$OnPreferenceClickListener() {
            public boolean onPreferenceClick(final Preference preference) {
                Tracker.settingsContactUs((Context)SettingsFragment.this.getActivity());
                ContactUsActivity.start(SettingsFragment.this.getActivity());
                return true;
            }
        });
        if (this.mLocalizedStringsTaskId != null) {
            this.showUpdatingLanguageDialog();
            return;
        }
        if (this.mLocaleList == null) {
            ApiManager.getInstance(this.getActivity()).getLocalesList(this.mListLocalesListener);
            return;
        }
        this.updateLocaleList();
        this.updateLocalePreferenceSummary();
    }
    
    public void setVideoQualities(final ListPreference listPreference) {
        final ArrayList<String> list = new ArrayList<String>();
        final ArrayList<String> list2 = new ArrayList<String>();
        final ApplicationState value = ApplicationState.get((Context)this.getActivity());
        list.add(LocalizedStrings.AUTO.get());
        list.add(LocalizedStrings.HIGH.get());
        list.add(LocalizedStrings.LOW.get());
        list2.add("adaptive");
        list2.add("ultra");
        list2.add("mid");
        final String[] entries = new String[list.size()];
        list.toArray(entries);
        final String[] entryValues = new String[list2.size()];
        list2.toArray(entryValues);
        listPreference.setEntries((CharSequence[])entries);
        listPreference.setEntryValues((CharSequence[])entryValues);
        listPreference.setValue(value.getVideoQualityPreference(false));
    }
}
