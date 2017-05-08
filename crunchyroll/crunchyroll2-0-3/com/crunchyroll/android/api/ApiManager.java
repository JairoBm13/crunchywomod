// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api;

import com.crunchyroll.android.api.tasks.ContactUsTask;
import com.crunchyroll.android.api.tasks.MembershipStartTask;
import com.crunchyroll.android.api.tasks.FreeTrialStartTask;
import com.crunchyroll.android.api.models.PaymentInformation;
import com.crunchyroll.android.api.tasks.RemoveFromQueueTask;
import com.crunchyroll.android.api.tasks.LogoutTask;
import com.crunchyroll.android.api.tasks.LoginTask;
import com.crunchyroll.android.api.models.Login;
import com.google.common.base.Optional;
import com.crunchyroll.android.api.tasks.UpdatedEpisodesTask;
import com.crunchyroll.android.api.models.UpdatedEpisode;
import com.crunchyroll.android.api.tasks.SeriesInfoTask;
import com.crunchyroll.android.api.tasks.AppListTask;
import com.crunchyroll.android.api.models.RelatedApp;
import com.crunchyroll.android.api.tasks.LoadQueueTask;
import com.crunchyroll.android.api.tasks.MembershipInfoTask;
import com.crunchyroll.android.api.models.MembershipInfoItem;
import com.crunchyroll.android.api.tasks.MediaListDataTask;
import com.crunchyroll.android.api.models.SeriesInfoWithMedia;
import com.crunchyroll.android.api.tasks.LoadMediaInfoTask;
import com.crunchyroll.android.api.models.Media;
import com.crunchyroll.android.api.tasks.LocalizedStringsTask;
import com.crunchyroll.android.api.tasks.ListLocalesTask;
import com.crunchyroll.android.api.models.LocaleData;
import com.crunchyroll.android.api.tasks.LoadInitialSeriesTask;
import com.crunchyroll.android.api.models.Series;
import com.crunchyroll.android.api.tasks.InitialBrowseDataTask;
import com.crunchyroll.android.api.models.InitialBrowseData;
import com.crunchyroll.android.api.tasks.LoadHistoryTask;
import com.crunchyroll.android.api.models.RecentlyWatchedItem;
import com.crunchyroll.android.api.tasks.FreeTrialInfoTask;
import com.crunchyroll.android.api.models.FreeTrialInformationItem;
import java.util.List;
import com.crunchyroll.android.api.tasks.ForgotPasswordTask;
import com.crunchyroll.android.api.tasks.LoadEpisodeInfoTask;
import com.crunchyroll.android.api.models.EpisodeInfo;
import android.support.annotation.Nullable;
import com.crunchyroll.android.api.tasks.CollectionTask;
import com.crunchyroll.android.api.models.Collection;
import java.util.ArrayList;
import com.crunchyroll.android.api.tasks.CategoriesTask;
import com.crunchyroll.android.api.models.Categories;
import com.crunchyroll.android.api.tasks.ClientOptionsTask;
import com.crunchyroll.android.api.models.ClientOptions;
import com.crunchyroll.android.api.tasks.AuthenticateTask;
import com.crunchyroll.android.api.models.AuthenticateItem;
import com.crunchyroll.android.api.tasks.AddToQueueTask;
import com.crunchyroll.android.api.models.QueueEntry;
import android.app.Activity;
import com.crunchyroll.android.api.tasks.ApiTaskListener;
import java.util.HashMap;
import android.app.Application;
import com.crunchyroll.android.api.tasks.BaseTask;
import java.util.Map;
import android.content.Context;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import com.crunchyroll.android.api.tasks.TaskCompleteListener;

public class ApiManager implements TaskCompleteListener
{
    private static ApiManager sManager;
    CrunchyrollApplication mApplication;
    private Context mContext;
    private Map<String, BaseTask> mRunningTasks;
    
    private ApiManager(final Application application) {
        this.mRunningTasks = new HashMap<String, BaseTask>();
        this.mApplication = (CrunchyrollApplication)application;
        this.mContext = application.getApplicationContext();
    }
    
    private String executeTask(final BaseTask baseTask, final ApiTaskListener taskListener) {
        this.mRunningTasks.put(baseTask.getTaskId(), baseTask);
        baseTask.setTaskListener(taskListener);
        baseTask.setTaskCompleteListener(this);
        baseTask.execute();
        return baseTask.getTaskId();
    }
    
    public static ApiManager getInstance(final Activity activity) {
        return getInstance(activity.getApplication());
    }
    
    public static ApiManager getInstance(final Application application) {
        if (ApiManager.sManager == null) {
            ApiManager.sManager = new ApiManager(application);
        }
        return ApiManager.sManager;
    }
    
    public String addToQueue(final Long n, final ApiTaskListener<QueueEntry> apiTaskListener) {
        return this.executeTask(new AddToQueueTask(this.mContext, n), apiTaskListener);
    }
    
    public String authenticate(final String s, final Integer n, final ApiTaskListener<AuthenticateItem> apiTaskListener) {
        return this.executeTask(new AuthenticateTask(this.mContext, s, n), apiTaskListener);
    }
    
    public void cancelTask(final String s) {
        if (this.mRunningTasks.containsKey(s)) {
            this.mRunningTasks.get(s).cancel();
        }
    }
    
    public String clientOptions(final ApiTaskListener<ClientOptions> apiTaskListener) {
        return this.executeTask(new ClientOptionsTask(this.mContext), apiTaskListener);
    }
    
    public String getCategories(final String s, final ApiTaskListener<Categories> apiTaskListener) {
        return this.executeTask(new CategoriesTask(this.mContext, s), apiTaskListener);
    }
    
    public String getCollectionListData(final Long n, final ApiTaskListener<ArrayList<Collection>> apiTaskListener) {
        return this.executeTask(new CollectionTask(this.mContext, n), apiTaskListener);
    }
    
    public String getEpisodeInfoTask(final Long n, @Nullable final Long n2, final ApiTaskListener<EpisodeInfo> apiTaskListener) {
        return this.executeTask(new LoadEpisodeInfoTask(this.mContext, n, n2), apiTaskListener);
    }
    
    public String getForgottenPassword(final String s, final ApiTaskListener<Boolean> apiTaskListener) {
        return this.executeTask(new ForgotPasswordTask(this.mContext, s), apiTaskListener);
    }
    
    public String getFreeTrialInfo(final ApiTaskListener<List<FreeTrialInformationItem>> apiTaskListener) {
        return this.executeTask(new FreeTrialInfoTask(this.mContext), apiTaskListener);
    }
    
    public String getHistoryData(final String s, final Integer n, final Integer n2, final ApiTaskListener<List<RecentlyWatchedItem>> apiTaskListener) {
        return this.executeTask(new LoadHistoryTask(this.mContext, s, n, n2), apiTaskListener);
    }
    
    public String getInitialBrowseData(final String s, final boolean b, final ApiTaskListener<InitialBrowseData> apiTaskListener) {
        return this.executeTask(new InitialBrowseDataTask(this.mContext, s, b), apiTaskListener);
    }
    
    public String getInitialSeries(final String s, final String s2, final ApiTaskListener<ArrayList<Series>> apiTaskListener) {
        return this.executeTask(new LoadInitialSeriesTask(this.mContext, s, s2), apiTaskListener);
    }
    
    public String getLocalesList(final ApiTaskListener<List<LocaleData>> apiTaskListener) {
        return this.executeTask(new ListLocalesTask(this.mContext), apiTaskListener);
    }
    
    public String getLocalizedStrings(final ApiTaskListener<Map<String, String>> apiTaskListener) {
        return this.executeTask(new LocalizedStringsTask(this.mContext), apiTaskListener);
    }
    
    public String getMediaInfoTask(final Long n, final ApiTaskListener<Media> apiTaskListener) {
        return this.executeTask(new LoadMediaInfoTask(this.mContext, n), apiTaskListener);
    }
    
    public String getMediaListData(final Long n, final Long n2, final ApiTaskListener<SeriesInfoWithMedia> apiTaskListener) {
        return this.executeTask(new MediaListDataTask(this.mContext, n, n2), apiTaskListener);
    }
    
    public String getMembershipInfo(final ApiTaskListener<List<MembershipInfoItem>> apiTaskListener) {
        return this.executeTask(new MembershipInfoTask(this.mContext), apiTaskListener);
    }
    
    public String getQueueData(final String s, final ApiTaskListener<ArrayList<QueueEntry>> apiTaskListener) {
        return this.executeTask(new LoadQueueTask(this.mContext, s), apiTaskListener);
    }
    
    public String getRelatedApps(final ApiTaskListener<List<RelatedApp>> apiTaskListener) {
        return this.executeTask(new AppListTask(this.mContext), apiTaskListener);
    }
    
    public String getSeriesInfo(final Long n, final ApiTaskListener<Series> apiTaskListener) {
        return this.executeTask(new SeriesInfoTask(this.mContext, n), apiTaskListener);
    }
    
    public String getUpdatedEpisodes(final String s, final String s2, final Integer n, final Integer n2, final ApiTaskListener<List<UpdatedEpisode>> apiTaskListener) {
        return this.executeTask(new UpdatedEpisodesTask(this.mContext, s, s2, n, n2), apiTaskListener);
    }
    
    public void invalidateCache(final Class clazz, final Optional optional) {
        this.mApplication.getApiService().invalidate(new CacheKey(clazz, optional));
    }
    
    public String login(final String s, final String s2, final ApiTaskListener<Login> apiTaskListener) {
        return this.executeTask(new LoginTask(this.mContext, s, s2), apiTaskListener);
    }
    
    public String logout(final Optional<String> optional, final ApiTaskListener apiTaskListener) {
        return this.executeTask(new LogoutTask(this.mContext, optional), apiTaskListener);
    }
    
    @Override
    public void onTaskComplete(final String s) {
        this.mRunningTasks.remove(s);
    }
    
    public String removeFromQueue(final Series series, final ApiTaskListener<Void> apiTaskListener) {
        return this.executeTask(new RemoveFromQueueTask(this.mContext, series), apiTaskListener);
    }
    
    public String startFreeTrial(final ApiTaskListener<Void> apiTaskListener, final PaymentInformation paymentInformation) {
        return this.executeTask(new FreeTrialStartTask(this.mContext, paymentInformation), apiTaskListener);
    }
    
    public String startMembership(final ApiTaskListener<Void> apiTaskListener, final PaymentInformation paymentInformation) {
        return this.executeTask(new MembershipStartTask(this.mContext, paymentInformation), apiTaskListener);
    }
    
    public String submitContactUs(final ApiTaskListener<Void> apiTaskListener, final String s, final String s2, final String s3, final String s4, final Boolean b) {
        return this.executeTask(new ContactUsTask(this.mContext, s, s2, s3, s4, b), apiTaskListener);
    }
}
