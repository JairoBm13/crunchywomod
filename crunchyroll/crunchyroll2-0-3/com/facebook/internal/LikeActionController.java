// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.internal;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import org.json.JSONArray;
import com.facebook.HttpMethod;
import com.facebook.Response;
import com.facebook.Request;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import com.facebook.Settings;
import com.facebook.SessionState;
import android.os.Looper;
import com.facebook.widget.FacebookDialog;
import com.facebook.LoggingBehavior;
import com.facebook.RequestBatch;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.IOException;
import android.util.Log;
import java.io.Closeable;
import android.support.v4.content.LocalBroadcastManager;
import com.facebook.FacebookRequestError;
import android.app.Activity;
import android.content.Intent;
import com.facebook.Session;
import java.util.UUID;
import android.os.Bundle;
import android.content.Context;
import com.facebook.AppEventsLogger;
import android.os.Handler;
import java.util.concurrent.ConcurrentHashMap;

public class LikeActionController
{
    public static final String ACTION_LIKE_ACTION_CONTROLLER_DID_ERROR = "com.facebook.sdk.LikeActionController.DID_ERROR";
    public static final String ACTION_LIKE_ACTION_CONTROLLER_DID_RESET = "com.facebook.sdk.LikeActionController.DID_RESET";
    public static final String ACTION_LIKE_ACTION_CONTROLLER_UPDATED = "com.facebook.sdk.LikeActionController.UPDATED";
    public static final String ACTION_OBJECT_ID_KEY = "com.facebook.sdk.LikeActionController.OBJECT_ID";
    private static final int ERROR_CODE_OBJECT_ALREADY_LIKED = 3501;
    public static final String ERROR_INVALID_OBJECT_ID = "Invalid Object Id";
    private static final String JSON_BOOL_IS_OBJECT_LIKED_KEY = "is_object_liked";
    private static final String JSON_BUNDLE_PENDING_CALL_ANALYTICS_BUNDLE = "pending_call_analytics_bundle";
    private static final String JSON_INT_VERSION_KEY = "com.facebook.internal.LikeActionController.version";
    private static final String JSON_STRING_LIKE_COUNT_WITHOUT_LIKE_KEY = "like_count_string_without_like";
    private static final String JSON_STRING_LIKE_COUNT_WITH_LIKE_KEY = "like_count_string_with_like";
    private static final String JSON_STRING_OBJECT_ID_KEY = "object_id";
    private static final String JSON_STRING_PENDING_CALL_ID_KEY = "pending_call_id";
    private static final String JSON_STRING_SOCIAL_SENTENCE_WITHOUT_LIKE_KEY = "social_sentence_without_like";
    private static final String JSON_STRING_SOCIAL_SENTENCE_WITH_LIKE_KEY = "social_sentence_with_like";
    private static final String JSON_STRING_UNLIKE_TOKEN_KEY = "unlike_token";
    private static final String LIKE_ACTION_CONTROLLER_STORE = "com.facebook.LikeActionController.CONTROLLER_STORE_KEY";
    private static final String LIKE_ACTION_CONTROLLER_STORE_OBJECT_SUFFIX_KEY = "OBJECT_SUFFIX";
    private static final String LIKE_ACTION_CONTROLLER_STORE_PENDING_OBJECT_ID_KEY = "PENDING_CONTROLLER_KEY";
    private static final int LIKE_ACTION_CONTROLLER_VERSION = 2;
    private static final String LIKE_DIALOG_RESPONSE_LIKE_COUNT_STRING_KEY = "like_count_string";
    private static final String LIKE_DIALOG_RESPONSE_OBJECT_IS_LIKED_KEY = "object_is_liked";
    private static final String LIKE_DIALOG_RESPONSE_SOCIAL_SENTENCE_KEY = "social_sentence";
    private static final String LIKE_DIALOG_RESPONSE_UNLIKE_TOKEN_KEY = "unlike_token";
    private static final int MAX_CACHE_SIZE = 128;
    private static final int MAX_OBJECT_SUFFIX = 1000;
    private static final String TAG;
    private static final ConcurrentHashMap<String, LikeActionController> cache;
    private static FileLruCache controllerDiskCache;
    private static WorkQueue diskIOWorkQueue;
    private static Handler handler;
    private static boolean isInitialized;
    private static boolean isPendingBroadcastReset;
    private static WorkQueue mruCacheWorkQueue;
    private static String objectIdForPendingController;
    private static volatile int objectSuffix;
    private AppEventsLogger appEventsLogger;
    private Context context;
    private boolean isObjectLiked;
    private boolean isObjectLikedOnServer;
    private boolean isPendingLikeOrUnlike;
    private String likeCountStringWithLike;
    private String likeCountStringWithoutLike;
    private String objectId;
    private boolean objectIsPage;
    private Bundle pendingCallAnalyticsBundle;
    private UUID pendingCallId;
    private Session session;
    private String socialSentenceWithLike;
    private String socialSentenceWithoutLike;
    private String unlikeToken;
    private String verifiedObjectId;
    
    static {
        TAG = LikeActionController.class.getSimpleName();
        cache = new ConcurrentHashMap<String, LikeActionController>();
        LikeActionController.mruCacheWorkQueue = new WorkQueue(1);
        LikeActionController.diskIOWorkQueue = new WorkQueue(1);
    }
    
    private LikeActionController(final Context context, final Session session, final String objectId) {
        this.context = context;
        this.session = session;
        this.objectId = objectId;
        this.appEventsLogger = AppEventsLogger.newLogger(context, session);
    }
    
    private static void broadcastAction(final Context context, final LikeActionController likeActionController, final String s) {
        broadcastAction(context, likeActionController, s, null);
    }
    
    private static void broadcastAction(final Context context, final LikeActionController likeActionController, final String s, final Bundle bundle) {
        final Intent intent = new Intent(s);
        Bundle bundle2 = bundle;
        if (likeActionController != null) {
            if ((bundle2 = bundle) == null) {
                bundle2 = new Bundle();
            }
            bundle2.putString("com.facebook.sdk.LikeActionController.OBJECT_ID", likeActionController.getObjectId());
        }
        if (bundle2 != null) {
            intent.putExtras(bundle2);
        }
        LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(intent);
    }
    
    private boolean canUseOGPublish() {
        return !this.objectIsPage && this.verifiedObjectId != null && this.session != null && this.session.getPermissions() != null && this.session.getPermissions().contains("publish_actions");
    }
    
    private static void createControllerForObjectId(final Context context, final String s, final CreationCallback creationCallback) {
        final LikeActionController controllerFromInMemoryCache = getControllerFromInMemoryCache(s);
        if (controllerFromInMemoryCache != null) {
            invokeCallbackWithController(creationCallback, controllerFromInMemoryCache);
            return;
        }
        LikeActionController deserializeFromDiskSynchronously;
        if ((deserializeFromDiskSynchronously = deserializeFromDiskSynchronously(context, s)) == null) {
            deserializeFromDiskSynchronously = new LikeActionController(context, Session.getActiveSession(), s);
            serializeToDiskAsync(deserializeFromDiskSynchronously);
        }
        putControllerInMemoryCache(s, deserializeFromDiskSynchronously);
        LikeActionController.handler.post((Runnable)new Runnable() {
            @Override
            public void run() {
                deserializeFromDiskSynchronously.refreshStatusAsync();
            }
        });
        invokeCallbackWithController(creationCallback, deserializeFromDiskSynchronously);
    }
    
    private static LikeActionController deserializeFromDiskSynchronously(final Context context, String cacheKeyForObjectId) {
        final LikeActionController likeActionController = null;
        final Closeable closeable = null;
        Closeable closeable2 = null;
        Closeable closeable3 = closeable;
        try {
            cacheKeyForObjectId = getCacheKeyForObjectId(cacheKeyForObjectId);
            closeable2 = closeable2;
            closeable3 = closeable;
            final InputStream value = LikeActionController.controllerDiskCache.get(cacheKeyForObjectId);
            LikeActionController deserializeFromJson = likeActionController;
            if (value != null) {
                closeable2 = value;
                closeable3 = value;
                final String streamToString = Utility.readStreamToString(value);
                deserializeFromJson = likeActionController;
                closeable2 = value;
                closeable3 = value;
                if (!Utility.isNullOrEmpty(streamToString)) {
                    closeable2 = value;
                    closeable3 = value;
                    deserializeFromJson = deserializeFromJson(context, streamToString);
                }
            }
            LikeActionController likeActionController2 = deserializeFromJson;
            if (value != null) {
                Utility.closeQuietly(value);
                likeActionController2 = deserializeFromJson;
            }
            return likeActionController2;
        }
        catch (IOException ex) {
            closeable3 = closeable2;
            Log.e(LikeActionController.TAG, "Unable to deserialize controller from disk", (Throwable)ex);
            final LikeActionController likeActionController2 = null;
            return null;
        }
        finally {
            if (closeable3 != null) {
                Utility.closeQuietly(closeable3);
            }
        }
    }
    
    private static LikeActionController deserializeFromJson(final Context context, final String s) {
        LikeActionController likeActionController2;
        try {
            final JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.optInt("com.facebook.internal.LikeActionController.version", -1) != 2) {
                return null;
            }
            final LikeActionController likeActionController = new LikeActionController(context, Session.getActiveSession(), jsonObject.getString("object_id"));
            likeActionController.likeCountStringWithLike = jsonObject.optString("like_count_string_with_like", (String)null);
            likeActionController.likeCountStringWithoutLike = jsonObject.optString("like_count_string_without_like", (String)null);
            likeActionController.socialSentenceWithLike = jsonObject.optString("social_sentence_with_like", (String)null);
            likeActionController.socialSentenceWithoutLike = jsonObject.optString("social_sentence_without_like", (String)null);
            likeActionController.isObjectLiked = jsonObject.optBoolean("is_object_liked");
            likeActionController.unlikeToken = jsonObject.optString("unlike_token", (String)null);
            final String optString = jsonObject.optString("pending_call_id", (String)null);
            if (!Utility.isNullOrEmpty(optString)) {
                likeActionController.pendingCallId = UUID.fromString(optString);
            }
            final JSONObject optJSONObject = jsonObject.optJSONObject("pending_call_analytics_bundle");
            likeActionController2 = likeActionController;
            if (optJSONObject != null) {
                likeActionController.pendingCallAnalyticsBundle = BundleJSONConverter.convertToBundle(optJSONObject);
                return likeActionController;
            }
        }
        catch (JSONException ex) {
            Log.e(LikeActionController.TAG, "Unable to deserialize controller from JSON", (Throwable)ex);
            likeActionController2 = null;
        }
        return likeActionController2;
    }
    
    private void fallbackToDialog(final Activity activity, final Bundle bundle, final boolean b) {
        this.updateState(b, this.likeCountStringWithLike, this.likeCountStringWithoutLike, this.socialSentenceWithLike, this.socialSentenceWithoutLike, this.unlikeToken);
        this.presentLikeDialog(activity, bundle);
    }
    
    private void fetchVerifiedObjectId(final RequestCompletionCallback requestCompletionCallback) {
        if (!Utility.isNullOrEmpty(this.verifiedObjectId)) {
            if (requestCompletionCallback != null) {
                requestCompletionCallback.onComplete();
            }
            return;
        }
        final GetOGObjectIdRequestWrapper getOGObjectIdRequestWrapper = new GetOGObjectIdRequestWrapper(this.objectId);
        final GetPageIdRequestWrapper getPageIdRequestWrapper = new GetPageIdRequestWrapper(this.objectId);
        final RequestBatch requestBatch = new RequestBatch();
        ((AbstractRequestWrapper)getOGObjectIdRequestWrapper).addToBatch(requestBatch);
        ((AbstractRequestWrapper)getPageIdRequestWrapper).addToBatch(requestBatch);
        requestBatch.addCallback((RequestBatch.Callback)new RequestBatch.Callback() {
            @Override
            public void onBatchCompleted(final RequestBatch requestBatch) {
                LikeActionController.this.verifiedObjectId = getOGObjectIdRequestWrapper.verifiedObjectId;
                if (Utility.isNullOrEmpty(LikeActionController.this.verifiedObjectId)) {
                    LikeActionController.this.verifiedObjectId = getPageIdRequestWrapper.verifiedObjectId;
                    LikeActionController.this.objectIsPage = getPageIdRequestWrapper.objectIsPage;
                }
                if (Utility.isNullOrEmpty(LikeActionController.this.verifiedObjectId)) {
                    Logger.log(LoggingBehavior.DEVELOPER_ERRORS, LikeActionController.TAG, "Unable to verify the FB id for '%s'. Verify that it is a valid FB object or page", LikeActionController.this.objectId);
                    final LikeActionController this$0 = LikeActionController.this;
                    FacebookRequestError facebookRequestError;
                    if (getPageIdRequestWrapper.error != null) {
                        facebookRequestError = getPageIdRequestWrapper.error;
                    }
                    else {
                        facebookRequestError = getOGObjectIdRequestWrapper.error;
                    }
                    this$0.logAppEventForError("get_verified_id", facebookRequestError);
                }
                if (requestCompletionCallback != null) {
                    requestCompletionCallback.onComplete();
                }
            }
        });
        requestBatch.executeAsync();
    }
    
    private static String getCacheKeyForObjectId(final String s) {
        final String s2 = null;
        final Session activeSession = Session.getActiveSession();
        String accessToken = s2;
        if (activeSession != null) {
            accessToken = s2;
            if (activeSession.isOpened()) {
                accessToken = activeSession.getAccessToken();
            }
        }
        String md5hash;
        if ((md5hash = accessToken) != null) {
            md5hash = Utility.md5hash(accessToken);
        }
        return String.format("%s|%s|com.fb.sdk.like|%d", s, Utility.coerceValueIfNullOrEmpty(md5hash, ""), LikeActionController.objectSuffix);
    }
    
    public static void getControllerForObjectId(final Context context, final String s, final CreationCallback creationCallback) {
        if (!LikeActionController.isInitialized) {
            performFirstInitialize(context);
        }
        final LikeActionController controllerFromInMemoryCache = getControllerFromInMemoryCache(s);
        if (controllerFromInMemoryCache != null) {
            invokeCallbackWithController(creationCallback, controllerFromInMemoryCache);
            return;
        }
        LikeActionController.diskIOWorkQueue.addActiveWorkItem(new CreateLikeActionControllerWorkItem(context, s, creationCallback));
    }
    
    private static LikeActionController getControllerFromInMemoryCache(String cacheKeyForObjectId) {
        cacheKeyForObjectId = getCacheKeyForObjectId(cacheKeyForObjectId);
        final LikeActionController likeActionController = LikeActionController.cache.get(cacheKeyForObjectId);
        if (likeActionController != null) {
            LikeActionController.mruCacheWorkQueue.addActiveWorkItem(new MRUCacheWorkItem(cacheKeyForObjectId, false));
        }
        return likeActionController;
    }
    
    private FacebookDialog.Callback getFacebookDialogCallback(final Bundle bundle) {
        return new FacebookDialog.Callback() {
            @Override
            public void onComplete(final PendingCall pendingCall, final Bundle bundle) {
                if (bundle == null || !bundle.containsKey("object_is_liked")) {
                    return;
                }
                final boolean boolean1 = bundle.getBoolean("object_is_liked");
                String access$800 = LikeActionController.this.likeCountStringWithLike;
                String s = LikeActionController.this.likeCountStringWithoutLike;
                if (bundle.containsKey("like_count_string")) {
                    access$800 = (s = bundle.getString("like_count_string"));
                }
                String access$801 = LikeActionController.this.socialSentenceWithLike;
                String s2 = LikeActionController.this.socialSentenceWithoutLike;
                if (bundle.containsKey("social_sentence")) {
                    access$801 = (s2 = bundle.getString("social_sentence"));
                }
                String s3;
                if (bundle.containsKey("object_is_liked")) {
                    s3 = bundle.getString("unlike_token");
                }
                else {
                    s3 = LikeActionController.this.unlikeToken;
                }
                Bundle val$analyticsParameters;
                if (bundle == null) {
                    val$analyticsParameters = new Bundle();
                }
                else {
                    val$analyticsParameters = bundle;
                }
                val$analyticsParameters.putString("call_id", pendingCall.getCallId().toString());
                LikeActionController.this.appEventsLogger.logSdkEvent("fb_like_control_dialog_did_succeed", null, val$analyticsParameters);
                LikeActionController.this.updateState(boolean1, access$800, s, access$801, s2, s3);
            }
            
            @Override
            public void onError(final PendingCall pendingCall, final Exception ex, final Bundle bundle) {
                Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Like Dialog failed with error : %s", ex);
                Bundle val$analyticsParameters;
                if (bundle == null) {
                    val$analyticsParameters = new Bundle();
                }
                else {
                    val$analyticsParameters = bundle;
                }
                val$analyticsParameters.putString("call_id", pendingCall.getCallId().toString());
                LikeActionController.this.logAppEventForError("present_dialog", val$analyticsParameters);
                broadcastAction(LikeActionController.this.context, LikeActionController.this, "com.facebook.sdk.LikeActionController.DID_ERROR", bundle);
            }
        };
    }
    
    public static boolean handleOnActivityResult(final Context context, final int n, final int n2, final Intent intent) {
        final UUID callIdFromIntent = NativeProtocol.getCallIdFromIntent(intent);
        if (callIdFromIntent != null) {
            if (Utility.isNullOrEmpty(LikeActionController.objectIdForPendingController)) {
                LikeActionController.objectIdForPendingController = context.getSharedPreferences("com.facebook.LikeActionController.CONTROLLER_STORE_KEY", 0).getString("PENDING_CONTROLLER_KEY", (String)null);
            }
            if (!Utility.isNullOrEmpty(LikeActionController.objectIdForPendingController)) {
                getControllerForObjectId(context, LikeActionController.objectIdForPendingController, (CreationCallback)new CreationCallback() {
                    @Override
                    public void onComplete(final LikeActionController likeActionController) {
                        likeActionController.onActivityResult(n, n2, intent, callIdFromIntent);
                    }
                });
                return true;
            }
        }
        return false;
    }
    
    private static void invokeCallbackWithController(final CreationCallback creationCallback, final LikeActionController likeActionController) {
        if (creationCallback == null) {
            return;
        }
        LikeActionController.handler.post((Runnable)new Runnable() {
            @Override
            public void run() {
                creationCallback.onComplete(likeActionController);
            }
        });
    }
    
    private void logAppEventForError(final String s, Bundle bundle) {
        bundle = new Bundle(bundle);
        bundle.putString("object_id", this.objectId);
        bundle.putString("current_action", s);
        this.appEventsLogger.logSdkEvent("fb_like_control_error", null, bundle);
    }
    
    private void logAppEventForError(final String s, final FacebookRequestError facebookRequestError) {
        final Bundle bundle = new Bundle();
        if (facebookRequestError != null) {
            final JSONObject requestResult = facebookRequestError.getRequestResult();
            if (requestResult != null) {
                bundle.putString("error", requestResult.toString());
            }
        }
        this.logAppEventForError(s, bundle);
    }
    
    private boolean onActivityResult(final int n, final int n2, final Intent intent, final UUID uuid) {
        if (this.pendingCallId != null && this.pendingCallId.equals(uuid)) {
            final FacebookDialog.PendingCall pendingCallById = PendingCallStore.getInstance().getPendingCallById(this.pendingCallId);
            if (pendingCallById != null) {
                FacebookDialog.handleActivityResult(this.context, pendingCallById, n, intent, this.getFacebookDialogCallback(this.pendingCallAnalyticsBundle));
                this.stopTrackingPendingCall();
                return true;
            }
        }
        return false;
    }
    
    private static void performFirstInitialize(final Context context) {
        synchronized (LikeActionController.class) {
            if (!LikeActionController.isInitialized) {
                LikeActionController.handler = new Handler(Looper.getMainLooper());
                LikeActionController.objectSuffix = context.getSharedPreferences("com.facebook.LikeActionController.CONTROLLER_STORE_KEY", 0).getInt("OBJECT_SUFFIX", 1);
                LikeActionController.controllerDiskCache = new FileLruCache(context, LikeActionController.TAG, new FileLruCache.Limits());
                registerSessionBroadcastReceivers(context);
                LikeActionController.isInitialized = true;
            }
        }
    }
    
    private void performLikeOrUnlike(final Activity activity, final boolean b, final Bundle bundle) {
        if (!this.canUseOGPublish()) {
            this.presentLikeDialog(activity, bundle);
            return;
        }
        if (b) {
            this.publishLikeAsync(activity, bundle);
            return;
        }
        if (!Utility.isNullOrEmpty(this.unlikeToken)) {
            this.publishUnlikeAsync(activity, bundle);
            return;
        }
        this.fallbackToDialog(activity, bundle, true);
    }
    
    private void presentLikeDialog(final Activity activity, final Bundle bundle) {
        final LikeDialogBuilder likeDialogBuilder = new LikeDialogBuilder(activity, this.objectId);
        if (((FacebookDialog.Builder)likeDialogBuilder).canPresent()) {
            this.trackPendingCall(((FacebookDialog.Builder)likeDialogBuilder).build().present(), bundle);
            this.appEventsLogger.logSdkEvent("fb_like_control_did_present_dialog", null, bundle);
        }
        else {
            final String webFallbackUrl = likeDialogBuilder.getWebFallbackUrl();
            if (!Utility.isNullOrEmpty(webFallbackUrl) && FacebookWebFallbackDialog.presentWebFallback((Context)activity, webFallbackUrl, likeDialogBuilder.getApplicationId(), likeDialogBuilder.getAppCall(), this.getFacebookDialogCallback(bundle))) {
                this.appEventsLogger.logSdkEvent("fb_like_control_did_present_fallback_dialog", null, bundle);
            }
        }
    }
    
    private void publishLikeAsync(final Activity activity, final Bundle bundle) {
        this.isPendingLikeOrUnlike = true;
        this.fetchVerifiedObjectId((RequestCompletionCallback)new RequestCompletionCallback() {
            @Override
            public void onComplete() {
                if (Utility.isNullOrEmpty(LikeActionController.this.verifiedObjectId)) {
                    final Bundle bundle = new Bundle();
                    bundle.putString("com.facebook.platform.status.ERROR_DESCRIPTION", "Invalid Object Id");
                    broadcastAction(LikeActionController.this.context, LikeActionController.this, "com.facebook.sdk.LikeActionController.DID_ERROR", bundle);
                    return;
                }
                final RequestBatch requestBatch = new RequestBatch();
                final PublishLikeRequestWrapper publishLikeRequestWrapper = new PublishLikeRequestWrapper(LikeActionController.this.verifiedObjectId);
                ((AbstractRequestWrapper)publishLikeRequestWrapper).addToBatch(requestBatch);
                requestBatch.addCallback((RequestBatch.Callback)new RequestBatch.Callback() {
                    @Override
                    public void onBatchCompleted(final RequestBatch requestBatch) {
                        LikeActionController.this.isPendingLikeOrUnlike = false;
                        if (publishLikeRequestWrapper.error != null) {
                            LikeActionController.this.fallbackToDialog(activity, bundle, false);
                            return;
                        }
                        LikeActionController.this.unlikeToken = Utility.coerceValueIfNullOrEmpty(publishLikeRequestWrapper.unlikeToken, null);
                        LikeActionController.this.isObjectLikedOnServer = true;
                        LikeActionController.this.appEventsLogger.logSdkEvent("fb_like_control_did_like", null, bundle);
                        LikeActionController.this.toggleAgainIfNeeded(activity, bundle);
                    }
                });
                requestBatch.executeAsync();
            }
        });
    }
    
    private void publishUnlikeAsync(final Activity activity, final Bundle bundle) {
        this.isPendingLikeOrUnlike = true;
        final RequestBatch requestBatch = new RequestBatch();
        final PublishUnlikeRequestWrapper publishUnlikeRequestWrapper = new PublishUnlikeRequestWrapper(this.unlikeToken);
        ((AbstractRequestWrapper)publishUnlikeRequestWrapper).addToBatch(requestBatch);
        requestBatch.addCallback((RequestBatch.Callback)new RequestBatch.Callback() {
            @Override
            public void onBatchCompleted(final RequestBatch requestBatch) {
                LikeActionController.this.isPendingLikeOrUnlike = false;
                if (publishUnlikeRequestWrapper.error != null) {
                    LikeActionController.this.fallbackToDialog(activity, bundle, true);
                    return;
                }
                LikeActionController.this.unlikeToken = null;
                LikeActionController.this.isObjectLikedOnServer = false;
                LikeActionController.this.appEventsLogger.logSdkEvent("fb_like_control_did_unlike", null, bundle);
                LikeActionController.this.toggleAgainIfNeeded(activity, bundle);
            }
        });
        requestBatch.executeAsync();
    }
    
    private static void putControllerInMemoryCache(String cacheKeyForObjectId, final LikeActionController likeActionController) {
        cacheKeyForObjectId = getCacheKeyForObjectId(cacheKeyForObjectId);
        LikeActionController.mruCacheWorkQueue.addActiveWorkItem(new MRUCacheWorkItem(cacheKeyForObjectId, true));
        LikeActionController.cache.put(cacheKeyForObjectId, likeActionController);
    }
    
    private void refreshStatusAsync() {
        if (this.session == null || this.session.isClosed() || SessionState.CREATED.equals(this.session.getState())) {
            this.refreshStatusViaService();
        }
        else if (this.session.isOpened()) {
            this.fetchVerifiedObjectId((RequestCompletionCallback)new RequestCompletionCallback() {
                @Override
                public void onComplete() {
                    final GetOGObjectLikesRequestWrapper getOGObjectLikesRequestWrapper = new GetOGObjectLikesRequestWrapper(LikeActionController.this.verifiedObjectId);
                    final GetEngagementRequestWrapper getEngagementRequestWrapper = new GetEngagementRequestWrapper(LikeActionController.this.verifiedObjectId);
                    final RequestBatch requestBatch = new RequestBatch();
                    ((AbstractRequestWrapper)getOGObjectLikesRequestWrapper).addToBatch(requestBatch);
                    ((AbstractRequestWrapper)getEngagementRequestWrapper).addToBatch(requestBatch);
                    requestBatch.addCallback((RequestBatch.Callback)new RequestBatch.Callback() {
                        @Override
                        public void onBatchCompleted(final RequestBatch requestBatch) {
                            if (getOGObjectLikesRequestWrapper.error != null || getEngagementRequestWrapper.error != null) {
                                Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Unable to refresh like state for id: '%s'", LikeActionController.this.objectId);
                                return;
                            }
                            LikeActionController.this.updateState(getOGObjectLikesRequestWrapper.objectIsLiked, getEngagementRequestWrapper.likeCountStringWithLike, getEngagementRequestWrapper.likeCountStringWithoutLike, getEngagementRequestWrapper.socialSentenceStringWithLike, getEngagementRequestWrapper.socialSentenceStringWithoutLike, getOGObjectLikesRequestWrapper.unlikeToken);
                        }
                    });
                    requestBatch.executeAsync();
                }
            });
        }
    }
    
    private void refreshStatusViaService() {
        final LikeStatusClient likeStatusClient = new LikeStatusClient(this.context, Settings.getApplicationId(), this.objectId);
        if (!likeStatusClient.start()) {
            return;
        }
        likeStatusClient.setCompletedListener((PlatformServiceClient.CompletedListener)new PlatformServiceClient.CompletedListener() {
            @Override
            public void completed(final Bundle bundle) {
                if (bundle == null || !bundle.containsKey("com.facebook.platform.extra.OBJECT_IS_LIKED")) {
                    return;
                }
                final boolean boolean1 = bundle.getBoolean("com.facebook.platform.extra.OBJECT_IS_LIKED");
                String s;
                if (bundle.containsKey("com.facebook.platform.extra.LIKE_COUNT_STRING_WITH_LIKE")) {
                    s = bundle.getString("com.facebook.platform.extra.LIKE_COUNT_STRING_WITH_LIKE");
                }
                else {
                    s = LikeActionController.this.likeCountStringWithLike;
                }
                String s2;
                if (bundle.containsKey("com.facebook.platform.extra.LIKE_COUNT_STRING_WITHOUT_LIKE")) {
                    s2 = bundle.getString("com.facebook.platform.extra.LIKE_COUNT_STRING_WITHOUT_LIKE");
                }
                else {
                    s2 = LikeActionController.this.likeCountStringWithoutLike;
                }
                String s3;
                if (bundle.containsKey("com.facebook.platform.extra.SOCIAL_SENTENCE_WITH_LIKE")) {
                    s3 = bundle.getString("com.facebook.platform.extra.SOCIAL_SENTENCE_WITH_LIKE");
                }
                else {
                    s3 = LikeActionController.this.socialSentenceWithLike;
                }
                String s4;
                if (bundle.containsKey("com.facebook.platform.extra.SOCIAL_SENTENCE_WITHOUT_LIKE")) {
                    s4 = bundle.getString("com.facebook.platform.extra.SOCIAL_SENTENCE_WITHOUT_LIKE");
                }
                else {
                    s4 = LikeActionController.this.socialSentenceWithoutLike;
                }
                String s5;
                if (bundle.containsKey("com.facebook.platform.extra.UNLIKE_TOKEN")) {
                    s5 = bundle.getString("com.facebook.platform.extra.UNLIKE_TOKEN");
                }
                else {
                    s5 = LikeActionController.this.unlikeToken;
                }
                LikeActionController.this.updateState(boolean1, s, s2, s3, s4, s5);
            }
        });
    }
    
    private static void registerSessionBroadcastReceivers(final Context context) {
        final LocalBroadcastManager instance = LocalBroadcastManager.getInstance(context);
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.facebook.sdk.ACTIVE_SESSION_UNSET");
        intentFilter.addAction("com.facebook.sdk.ACTIVE_SESSION_CLOSED");
        intentFilter.addAction("com.facebook.sdk.ACTIVE_SESSION_OPENED");
        instance.registerReceiver(new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (LikeActionController.isPendingBroadcastReset) {
                    return;
                }
                final String action = intent.getAction();
                final boolean b = Utility.areObjectsEqual("com.facebook.sdk.ACTIVE_SESSION_UNSET", action) || Utility.areObjectsEqual("com.facebook.sdk.ACTIVE_SESSION_CLOSED", action);
                LikeActionController.isPendingBroadcastReset = true;
                LikeActionController.handler.postDelayed((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        if (b) {
                            LikeActionController.objectSuffix = (LikeActionController.objectSuffix + 1) % 1000;
                            context.getSharedPreferences("com.facebook.LikeActionController.CONTROLLER_STORE_KEY", 0).edit().putInt("OBJECT_SUFFIX", LikeActionController.objectSuffix).apply();
                            LikeActionController.cache.clear();
                            LikeActionController.controllerDiskCache.clearCache();
                        }
                        broadcastAction(context, null, "com.facebook.sdk.LikeActionController.DID_RESET");
                        LikeActionController.isPendingBroadcastReset = false;
                    }
                }, 100L);
            }
        }, intentFilter);
    }
    
    private static void serializeToDiskAsync(final LikeActionController likeActionController) {
        final String serializeToJson = serializeToJson(likeActionController);
        final String cacheKeyForObjectId = getCacheKeyForObjectId(likeActionController.objectId);
        if (!Utility.isNullOrEmpty(serializeToJson) && !Utility.isNullOrEmpty(cacheKeyForObjectId)) {
            LikeActionController.diskIOWorkQueue.addActiveWorkItem(new SerializeToDiskWorkItem(cacheKeyForObjectId, serializeToJson));
        }
    }
    
    private static void serializeToDiskSynchronously(final String s, final String s2) {
        Closeable closeable = null;
        Closeable openPutStream = null;
        try {
            ((OutputStream)(closeable = (openPutStream = LikeActionController.controllerDiskCache.openPutStream(s)))).write(s2.getBytes());
        }
        catch (IOException ex) {
            closeable = openPutStream;
            Log.e(LikeActionController.TAG, "Unable to serialize controller to disk", (Throwable)ex);
        }
        finally {
            if (closeable != null) {
                Utility.closeQuietly(closeable);
            }
        }
    }
    
    private static String serializeToJson(final LikeActionController likeActionController) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("com.facebook.internal.LikeActionController.version", 2);
            jsonObject.put("object_id", (Object)likeActionController.objectId);
            jsonObject.put("like_count_string_with_like", (Object)likeActionController.likeCountStringWithLike);
            jsonObject.put("like_count_string_without_like", (Object)likeActionController.likeCountStringWithoutLike);
            jsonObject.put("social_sentence_with_like", (Object)likeActionController.socialSentenceWithLike);
            jsonObject.put("social_sentence_without_like", (Object)likeActionController.socialSentenceWithoutLike);
            jsonObject.put("is_object_liked", likeActionController.isObjectLiked);
            jsonObject.put("unlike_token", (Object)likeActionController.unlikeToken);
            if (likeActionController.pendingCallId != null) {
                jsonObject.put("pending_call_id", (Object)likeActionController.pendingCallId.toString());
            }
            if (likeActionController.pendingCallAnalyticsBundle != null) {
                final JSONObject convertToJSON = BundleJSONConverter.convertToJSON(likeActionController.pendingCallAnalyticsBundle);
                if (convertToJSON != null) {
                    jsonObject.put("pending_call_analytics_bundle", (Object)convertToJSON);
                }
            }
            return jsonObject.toString();
        }
        catch (JSONException ex) {
            Log.e(LikeActionController.TAG, "Unable to serialize controller to JSON", (Throwable)ex);
            return null;
        }
    }
    
    private void stopTrackingPendingCall() {
        PendingCallStore.getInstance().stopTrackingPendingCall(this.pendingCallId);
        this.pendingCallId = null;
        this.pendingCallAnalyticsBundle = null;
        this.storeObjectIdForPendingController(null);
    }
    
    private void storeObjectIdForPendingController(final String objectIdForPendingController) {
        LikeActionController.objectIdForPendingController = objectIdForPendingController;
        this.context.getSharedPreferences("com.facebook.LikeActionController.CONTROLLER_STORE_KEY", 0).edit().putString("PENDING_CONTROLLER_KEY", LikeActionController.objectIdForPendingController).apply();
    }
    
    private void toggleAgainIfNeeded(final Activity activity, final Bundle bundle) {
        if (this.isObjectLiked != this.isObjectLikedOnServer) {
            this.performLikeOrUnlike(activity, this.isObjectLiked, bundle);
        }
    }
    
    private void trackPendingCall(final FacebookDialog.PendingCall pendingCall, final Bundle pendingCallAnalyticsBundle) {
        PendingCallStore.getInstance().trackPendingCall(pendingCall);
        this.pendingCallId = pendingCall.getCallId();
        this.storeObjectIdForPendingController(this.objectId);
        this.pendingCallAnalyticsBundle = pendingCallAnalyticsBundle;
        serializeToDiskAsync(this);
    }
    
    private void updateState(final boolean isObjectLiked, String coerceValueIfNullOrEmpty, String coerceValueIfNullOrEmpty2, String coerceValueIfNullOrEmpty3, String coerceValueIfNullOrEmpty4, String coerceValueIfNullOrEmpty5) {
        coerceValueIfNullOrEmpty = Utility.coerceValueIfNullOrEmpty(coerceValueIfNullOrEmpty, null);
        coerceValueIfNullOrEmpty2 = Utility.coerceValueIfNullOrEmpty(coerceValueIfNullOrEmpty2, null);
        coerceValueIfNullOrEmpty3 = Utility.coerceValueIfNullOrEmpty(coerceValueIfNullOrEmpty3, null);
        coerceValueIfNullOrEmpty4 = Utility.coerceValueIfNullOrEmpty(coerceValueIfNullOrEmpty4, null);
        coerceValueIfNullOrEmpty5 = Utility.coerceValueIfNullOrEmpty(coerceValueIfNullOrEmpty5, null);
        int n;
        if (isObjectLiked != this.isObjectLiked || !Utility.areObjectsEqual(coerceValueIfNullOrEmpty, this.likeCountStringWithLike) || !Utility.areObjectsEqual(coerceValueIfNullOrEmpty2, this.likeCountStringWithoutLike) || !Utility.areObjectsEqual(coerceValueIfNullOrEmpty3, this.socialSentenceWithLike) || !Utility.areObjectsEqual(coerceValueIfNullOrEmpty4, this.socialSentenceWithoutLike) || !Utility.areObjectsEqual(coerceValueIfNullOrEmpty5, this.unlikeToken)) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (n == 0) {
            return;
        }
        this.isObjectLiked = isObjectLiked;
        this.likeCountStringWithLike = coerceValueIfNullOrEmpty;
        this.likeCountStringWithoutLike = coerceValueIfNullOrEmpty2;
        this.socialSentenceWithLike = coerceValueIfNullOrEmpty3;
        this.socialSentenceWithoutLike = coerceValueIfNullOrEmpty4;
        this.unlikeToken = coerceValueIfNullOrEmpty5;
        serializeToDiskAsync(this);
        broadcastAction(this.context, this, "com.facebook.sdk.LikeActionController.UPDATED");
    }
    
    public String getLikeCountString() {
        if (this.isObjectLiked) {
            return this.likeCountStringWithLike;
        }
        return this.likeCountStringWithoutLike;
    }
    
    public String getObjectId() {
        return this.objectId;
    }
    
    public String getSocialSentence() {
        if (this.isObjectLiked) {
            return this.socialSentenceWithLike;
        }
        return this.socialSentenceWithoutLike;
    }
    
    public boolean isObjectLiked() {
        return this.isObjectLiked;
    }
    
    public void toggleLike(final Activity activity, final Bundle bundle) {
        this.appEventsLogger.logSdkEvent("fb_like_control_did_tap", null, bundle);
        final boolean b = !this.isObjectLiked;
        if (this.canUseOGPublish()) {
            this.updateState(b, this.likeCountStringWithLike, this.likeCountStringWithoutLike, this.socialSentenceWithLike, this.socialSentenceWithoutLike, this.unlikeToken);
            if (this.isPendingLikeOrUnlike) {
                this.appEventsLogger.logSdkEvent("fb_like_control_did_undo_quickly", null, bundle);
                return;
            }
        }
        this.performLikeOrUnlike(activity, b, bundle);
    }
    
    private abstract class AbstractRequestWrapper
    {
        FacebookRequestError error;
        protected String objectId;
        private Request request;
        
        protected AbstractRequestWrapper(final String objectId) {
            this.objectId = objectId;
        }
        
        void addToBatch(final RequestBatch requestBatch) {
            requestBatch.add(this.request);
        }
        
        protected void processError(final FacebookRequestError facebookRequestError) {
            Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Error running request for object '%s' : %s", this.objectId, facebookRequestError);
        }
        
        protected abstract void processSuccess(final Response p0);
        
        protected void setRequest(final Request request) {
            (this.request = request).setVersion("v2.2");
            request.setCallback((Request.Callback)new Request.Callback() {
                @Override
                public void onCompleted(final Response response) {
                    AbstractRequestWrapper.this.error = response.getError();
                    if (AbstractRequestWrapper.this.error != null) {
                        AbstractRequestWrapper.this.processError(AbstractRequestWrapper.this.error);
                        return;
                    }
                    AbstractRequestWrapper.this.processSuccess(response);
                }
            });
        }
    }
    
    private static class CreateLikeActionControllerWorkItem implements Runnable
    {
        private CreationCallback callback;
        private Context context;
        private String objectId;
        
        CreateLikeActionControllerWorkItem(final Context context, final String objectId, final CreationCallback callback) {
            this.context = context;
            this.objectId = objectId;
            this.callback = callback;
        }
        
        @Override
        public void run() {
            createControllerForObjectId(this.context, this.objectId, this.callback);
        }
    }
    
    public interface CreationCallback
    {
        void onComplete(final LikeActionController p0);
    }
    
    private class GetEngagementRequestWrapper extends AbstractRequestWrapper
    {
        String likeCountStringWithLike;
        String likeCountStringWithoutLike;
        String socialSentenceStringWithLike;
        String socialSentenceStringWithoutLike;
        
        GetEngagementRequestWrapper(final String s) {
            super(s);
            this.likeCountStringWithLike = LikeActionController.this.likeCountStringWithLike;
            this.likeCountStringWithoutLike = LikeActionController.this.likeCountStringWithoutLike;
            this.socialSentenceStringWithLike = LikeActionController.this.socialSentenceWithLike;
            this.socialSentenceStringWithoutLike = LikeActionController.this.socialSentenceWithoutLike;
            final Bundle bundle = new Bundle();
            bundle.putString("fields", "engagement.fields(count_string_with_like,count_string_without_like,social_sentence_with_like,social_sentence_without_like)");
            ((AbstractRequestWrapper)this).setRequest(new Request(LikeActionController.this.session, s, bundle, HttpMethod.GET));
        }
        
        @Override
        protected void processError(final FacebookRequestError facebookRequestError) {
            Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Error fetching engagement for object '%s' : %s", this.objectId, facebookRequestError);
            LikeActionController.this.logAppEventForError("get_engagement", facebookRequestError);
        }
        
        @Override
        protected void processSuccess(final Response response) {
            final JSONObject tryGetJSONObjectFromResponse = Utility.tryGetJSONObjectFromResponse(response.getGraphObject(), "engagement");
            if (tryGetJSONObjectFromResponse != null) {
                this.likeCountStringWithLike = tryGetJSONObjectFromResponse.optString("count_string_with_like", this.likeCountStringWithLike);
                this.likeCountStringWithoutLike = tryGetJSONObjectFromResponse.optString("count_string_without_like", this.likeCountStringWithoutLike);
                this.socialSentenceStringWithLike = tryGetJSONObjectFromResponse.optString("social_sentence_with_like", this.socialSentenceStringWithLike);
                this.socialSentenceStringWithoutLike = tryGetJSONObjectFromResponse.optString("social_sentence_without_like", this.socialSentenceStringWithoutLike);
            }
        }
    }
    
    private class GetOGObjectIdRequestWrapper extends AbstractRequestWrapper
    {
        String verifiedObjectId;
        
        GetOGObjectIdRequestWrapper(final String s) {
            super(s);
            final Bundle bundle = new Bundle();
            bundle.putString("fields", "og_object.fields(id)");
            bundle.putString("ids", s);
            ((AbstractRequestWrapper)this).setRequest(new Request(LikeActionController.this.session, "", bundle, HttpMethod.GET));
        }
        
        @Override
        protected void processError(final FacebookRequestError facebookRequestError) {
            if (facebookRequestError.getErrorMessage().contains("og_object")) {
                this.error = null;
                return;
            }
            Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Error getting the FB id for object '%s' : %s", this.objectId, facebookRequestError);
        }
        
        @Override
        protected void processSuccess(final Response response) {
            final JSONObject tryGetJSONObjectFromResponse = Utility.tryGetJSONObjectFromResponse(response.getGraphObject(), this.objectId);
            if (tryGetJSONObjectFromResponse != null) {
                final JSONObject optJSONObject = tryGetJSONObjectFromResponse.optJSONObject("og_object");
                if (optJSONObject != null) {
                    this.verifiedObjectId = optJSONObject.optString("id");
                }
            }
        }
    }
    
    private class GetOGObjectLikesRequestWrapper extends AbstractRequestWrapper
    {
        boolean objectIsLiked;
        String unlikeToken;
        
        GetOGObjectLikesRequestWrapper(final String s) {
            super(s);
            this.objectIsLiked = LikeActionController.this.isObjectLiked;
            final Bundle bundle = new Bundle();
            bundle.putString("fields", "id,application");
            bundle.putString("object", s);
            ((AbstractRequestWrapper)this).setRequest(new Request(LikeActionController.this.session, "me/og.likes", bundle, HttpMethod.GET));
        }
        
        @Override
        protected void processError(final FacebookRequestError facebookRequestError) {
            Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Error fetching like status for object '%s' : %s", this.objectId, facebookRequestError);
            LikeActionController.this.logAppEventForError("get_og_object_like", facebookRequestError);
        }
        
        @Override
        protected void processSuccess(final Response response) {
            final JSONArray tryGetJSONArrayFromResponse = Utility.tryGetJSONArrayFromResponse(response.getGraphObject(), "data");
            if (tryGetJSONArrayFromResponse != null) {
                for (int i = 0; i < tryGetJSONArrayFromResponse.length(); ++i) {
                    final JSONObject optJSONObject = tryGetJSONArrayFromResponse.optJSONObject(i);
                    if (optJSONObject != null) {
                        this.objectIsLiked = true;
                        final JSONObject optJSONObject2 = optJSONObject.optJSONObject("application");
                        if (optJSONObject2 != null && Utility.areObjectsEqual(LikeActionController.this.session.getApplicationId(), optJSONObject2.optString("id"))) {
                            this.unlikeToken = optJSONObject.optString("id");
                        }
                    }
                }
            }
        }
    }
    
    private class GetPageIdRequestWrapper extends AbstractRequestWrapper
    {
        boolean objectIsPage;
        String verifiedObjectId;
        
        GetPageIdRequestWrapper(final String s) {
            super(s);
            final Bundle bundle = new Bundle();
            bundle.putString("fields", "id");
            bundle.putString("ids", s);
            ((AbstractRequestWrapper)this).setRequest(new Request(LikeActionController.this.session, "", bundle, HttpMethod.GET));
        }
        
        @Override
        protected void processError(final FacebookRequestError facebookRequestError) {
            Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Error getting the FB id for object '%s' : %s", this.objectId, facebookRequestError);
        }
        
        @Override
        protected void processSuccess(final Response response) {
            final JSONObject tryGetJSONObjectFromResponse = Utility.tryGetJSONObjectFromResponse(response.getGraphObject(), this.objectId);
            if (tryGetJSONObjectFromResponse != null) {
                this.verifiedObjectId = tryGetJSONObjectFromResponse.optString("id");
                this.objectIsPage = !Utility.isNullOrEmpty(this.verifiedObjectId);
            }
        }
    }
    
    private static class LikeDialogBuilder extends Builder<LikeDialogBuilder>
    {
        private String objectId;
        
        public LikeDialogBuilder(final Activity activity, final String objectId) {
            super(activity);
            this.objectId = objectId;
        }
        
        public PendingCall getAppCall() {
            return this.appCall;
        }
        
        public String getApplicationId() {
            return this.applicationId;
        }
        
        @Override
        protected EnumSet<? extends DialogFeature> getDialogFeatures() {
            return EnumSet.of((DialogFeature)LikeDialogFeature.LIKE_DIALOG);
        }
        
        @Override
        protected Bundle getMethodArguments() {
            final Bundle bundle = new Bundle();
            bundle.putString("object_id", this.objectId);
            return bundle;
        }
        
        public String getWebFallbackUrl() {
            return ((FacebookDialog.Builder)this).getWebFallbackUrlInternal();
        }
    }
    
    private enum LikeDialogFeature implements DialogFeature
    {
        LIKE_DIALOG(20140701);
        
        private int minVersion;
        
        private LikeDialogFeature(final int minVersion) {
            this.minVersion = minVersion;
        }
        
        @Override
        public String getAction() {
            return "com.facebook.platform.action.request.LIKE_DIALOG";
        }
        
        @Override
        public int getMinVersion() {
            return this.minVersion;
        }
    }
    
    private static class MRUCacheWorkItem implements Runnable
    {
        private static ArrayList<String> mruCachedItems;
        private String cacheItem;
        private boolean shouldTrim;
        
        static {
            MRUCacheWorkItem.mruCachedItems = new ArrayList<String>();
        }
        
        MRUCacheWorkItem(final String cacheItem, final boolean shouldTrim) {
            this.cacheItem = cacheItem;
            this.shouldTrim = shouldTrim;
        }
        
        @Override
        public void run() {
            if (this.cacheItem != null) {
                MRUCacheWorkItem.mruCachedItems.remove(this.cacheItem);
                MRUCacheWorkItem.mruCachedItems.add(0, this.cacheItem);
            }
            if (this.shouldTrim && MRUCacheWorkItem.mruCachedItems.size() >= 128) {
                while (64 < MRUCacheWorkItem.mruCachedItems.size()) {
                    LikeActionController.cache.remove(MRUCacheWorkItem.mruCachedItems.remove(MRUCacheWorkItem.mruCachedItems.size() - 1));
                }
            }
        }
    }
    
    private class PublishLikeRequestWrapper extends AbstractRequestWrapper
    {
        String unlikeToken;
        
        PublishLikeRequestWrapper(final String s) {
            super(s);
            final Bundle bundle = new Bundle();
            bundle.putString("object", s);
            ((AbstractRequestWrapper)this).setRequest(new Request(LikeActionController.this.session, "me/og.likes", bundle, HttpMethod.POST));
        }
        
        @Override
        protected void processError(final FacebookRequestError facebookRequestError) {
            if (facebookRequestError.getErrorCode() == 3501) {
                this.error = null;
                return;
            }
            Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Error liking object '%s' : %s", this.objectId, facebookRequestError);
            LikeActionController.this.logAppEventForError("publish_like", facebookRequestError);
        }
        
        @Override
        protected void processSuccess(final Response response) {
            this.unlikeToken = Utility.safeGetStringFromResponse(response.getGraphObject(), "id");
        }
    }
    
    private class PublishUnlikeRequestWrapper extends AbstractRequestWrapper
    {
        private String unlikeToken;
        
        PublishUnlikeRequestWrapper(final String unlikeToken) {
            super(null);
            this.unlikeToken = unlikeToken;
            ((AbstractRequestWrapper)this).setRequest(new Request(LikeActionController.this.session, unlikeToken, null, HttpMethod.DELETE));
        }
        
        @Override
        protected void processError(final FacebookRequestError facebookRequestError) {
            Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Error unliking object with unlike token '%s' : %s", this.unlikeToken, facebookRequestError);
            LikeActionController.this.logAppEventForError("publish_unlike", facebookRequestError);
        }
        
        @Override
        protected void processSuccess(final Response response) {
        }
    }
    
    private interface RequestCompletionCallback
    {
        void onComplete();
    }
    
    private static class SerializeToDiskWorkItem implements Runnable
    {
        private String cacheKey;
        private String controllerJson;
        
        SerializeToDiskWorkItem(final String cacheKey, final String controllerJson) {
            this.cacheKey = cacheKey;
            this.controllerJson = controllerJson;
        }
        
        @Override
        public void run() {
            serializeToDiskSynchronously(this.cacheKey, this.controllerJson);
        }
    }
}
