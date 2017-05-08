// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.widget;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;
import com.facebook.model.GraphObjectList;
import com.facebook.model.GraphObject;
import com.facebook.FacebookGraphObjectException;
import com.facebook.model.OpenGraphObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.facebook.model.OpenGraphAction;
import android.net.Uri;
import com.facebook.internal.ServerProtocol;
import java.util.Map;
import com.facebook.NativeAppCallContentProvider;
import java.util.UUID;
import java.util.List;
import java.util.Collection;
import com.facebook.internal.Validate;
import java.io.File;
import android.graphics.Bitmap;
import java.util.HashMap;
import com.facebook.AppEventsLogger;
import com.facebook.Settings;
import com.facebook.internal.Utility;
import com.facebook.internal.NativeProtocol;
import com.facebook.FacebookException;
import java.util.ArrayList;
import android.os.Bundle;
import android.content.Intent;
import java.util.Iterator;
import java.util.EnumSet;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.app.Activity;
import com.facebook.NativeAppCallAttachmentStore;

public class FacebookDialog
{
    public static final String COMPLETION_GESTURE_CANCEL = "cancel";
    private static final String EXTRA_DIALOG_COMPLETE_KEY = "com.facebook.platform.extra.DID_COMPLETE";
    private static final String EXTRA_DIALOG_COMPLETION_GESTURE_KEY = "com.facebook.platform.extra.COMPLETION_GESTURE";
    private static final String EXTRA_DIALOG_COMPLETION_ID_KEY = "com.facebook.platform.extra.POST_ID";
    public static final String RESULT_ARGS_DIALOG_COMPLETE_KEY = "didComplete";
    public static final String RESULT_ARGS_DIALOG_COMPLETION_GESTURE_KEY = "completionGesture";
    public static final String RESULT_ARGS_DIALOG_COMPLETION_ID_KEY = "postId";
    private static NativeAppCallAttachmentStore attachmentStore;
    private Activity activity;
    private PendingCall appCall;
    private Fragment fragment;
    private OnPresentCallback onPresentCallback;
    
    private FacebookDialog(final Activity activity, final Fragment fragment, final PendingCall appCall, final OnPresentCallback onPresentCallback) {
        this.activity = activity;
        this.fragment = fragment;
        this.appCall = appCall;
        this.onPresentCallback = onPresentCallback;
    }
    
    public static boolean canPresentMessageDialog(final Context context, final MessageDialogFeature... array) {
        return handleCanPresent(context, (Iterable<? extends DialogFeature>)EnumSet.of(MessageDialogFeature.MESSAGE_DIALOG, array));
    }
    
    public static boolean canPresentOpenGraphActionDialog(final Context context, final OpenGraphActionDialogFeature... array) {
        return handleCanPresent(context, (Iterable<? extends DialogFeature>)EnumSet.of(OpenGraphActionDialogFeature.OG_ACTION_DIALOG, array));
    }
    
    public static boolean canPresentOpenGraphMessageDialog(final Context context, final OpenGraphMessageDialogFeature... array) {
        return handleCanPresent(context, (Iterable<? extends DialogFeature>)EnumSet.of(OpenGraphMessageDialogFeature.OG_MESSAGE_DIALOG, array));
    }
    
    public static boolean canPresentShareDialog(final Context context, final ShareDialogFeature... array) {
        return handleCanPresent(context, (Iterable<? extends DialogFeature>)EnumSet.of(ShareDialogFeature.SHARE_DIALOG, array));
    }
    
    private static String getActionForFeatures(final Iterable<? extends DialogFeature> iterable) {
        final String s = null;
        final Iterator<? extends DialogFeature> iterator = iterable.iterator();
        String action = s;
        if (iterator.hasNext()) {
            action = ((DialogFeature)iterator.next()).getAction();
        }
        return action;
    }
    
    private static NativeAppCallAttachmentStore getAttachmentStore() {
        if (FacebookDialog.attachmentStore == null) {
            FacebookDialog.attachmentStore = new NativeAppCallAttachmentStore();
        }
        return FacebookDialog.attachmentStore;
    }
    
    private static String getEventName(final Intent intent) {
        final String stringExtra = intent.getStringExtra("com.facebook.platform.protocol.PROTOCOL_ACTION");
        final boolean hasExtra = intent.hasExtra("com.facebook.platform.extra.PHOTOS");
        final boolean b = false;
        final Bundle bundleExtra = intent.getBundleExtra("com.facebook.platform.protocol.METHOD_ARGS");
        boolean b2 = hasExtra;
        boolean b3 = b;
        if (bundleExtra != null) {
            final ArrayList stringArrayList = bundleExtra.getStringArrayList("PHOTOS");
            final String string = bundleExtra.getString("VIDEO");
            boolean b4 = hasExtra;
            if (stringArrayList != null) {
                b4 = hasExtra;
                if (!stringArrayList.isEmpty()) {
                    b4 = true;
                }
            }
            b2 = b4;
            b3 = b;
            if (string != null) {
                b2 = b4;
                b3 = b;
                if (!string.isEmpty()) {
                    b3 = true;
                    b2 = b4;
                }
            }
        }
        return getEventName(stringExtra, b2, b3);
    }
    
    private static String getEventName(final String s, final boolean b, final boolean b2) {
        if (s.equals("com.facebook.platform.action.request.FEED_DIALOG")) {
            if (b2) {
                return "fb_dialogs_present_share_video";
            }
            if (b) {
                return "fb_dialogs_present_share_photo";
            }
            return "fb_dialogs_present_share";
        }
        else if (s.equals("com.facebook.platform.action.request.MESSAGE_DIALOG")) {
            if (b) {
                return "fb_dialogs_present_message_photo";
            }
            return "fb_dialogs_present_message";
        }
        else {
            if (s.equals("com.facebook.platform.action.request.OGACTIONPUBLISH_DIALOG")) {
                return "fb_dialogs_present_share_og";
            }
            if (s.equals("com.facebook.platform.action.request.OGMESSAGEPUBLISH_DIALOG")) {
                return "fb_dialogs_present_message_og";
            }
            if (s.equals("com.facebook.platform.action.request.LIKE_DIALOG")) {
                return "fb_dialogs_present_like";
            }
            throw new FacebookException("An unspecified action was presented");
        }
    }
    
    public static String getNativeDialogCompletionGesture(final Bundle bundle) {
        if (bundle.containsKey("completionGesture")) {
            return bundle.getString("completionGesture");
        }
        return bundle.getString("com.facebook.platform.extra.COMPLETION_GESTURE");
    }
    
    public static boolean getNativeDialogDidComplete(final Bundle bundle) {
        if (bundle.containsKey("didComplete")) {
            return bundle.getBoolean("didComplete");
        }
        return bundle.getBoolean("com.facebook.platform.extra.DID_COMPLETE", false);
    }
    
    public static String getNativeDialogPostId(final Bundle bundle) {
        if (bundle.containsKey("postId")) {
            return bundle.getString("postId");
        }
        return bundle.getString("com.facebook.platform.extra.POST_ID");
    }
    
    private static int getProtocolVersionForNativeDialog(final Context context, final String s, final int[] array) {
        return NativeProtocol.getLatestAvailableProtocolVersionForAction(context, s, array);
    }
    
    private static int[] getVersionSpecForFeature(final String s, final String s2, final DialogFeature dialogFeature) {
        final Utility.DialogFeatureConfig dialogFeatureConfig = Utility.getDialogFeatureConfig(s, s2, dialogFeature.name());
        if (dialogFeatureConfig != null) {
            return dialogFeatureConfig.getVersionSpec();
        }
        return new int[] { dialogFeature.getMinVersion() };
    }
    
    private static int[] getVersionSpecForFeatures(final String s, final String s2, final Iterable<? extends DialogFeature> iterable) {
        final int[] array = null;
        final Iterator<? extends DialogFeature> iterator = iterable.iterator();
        int[] intersectRanges = array;
        while (iterator.hasNext()) {
            intersectRanges = Utility.intersectRanges(intersectRanges, getVersionSpecForFeature(s, s2, (DialogFeature)iterator.next()));
        }
        return intersectRanges;
    }
    
    public static boolean handleActivityResult(final Context context, final PendingCall pendingCall, final int n, final Intent intent, final Callback callback) {
        if (n != pendingCall.getRequestCode()) {
            return false;
        }
        if (FacebookDialog.attachmentStore != null) {
            FacebookDialog.attachmentStore.cleanupAttachmentsForCall(context, pendingCall.getCallId());
        }
        if (callback != null) {
            if (NativeProtocol.isErrorResult(intent)) {
                final Bundle errorDataFromResultIntent = NativeProtocol.getErrorDataFromResultIntent(intent);
                callback.onError(pendingCall, NativeProtocol.getExceptionFromErrorData(errorDataFromResultIntent), errorDataFromResultIntent);
            }
            else {
                callback.onComplete(pendingCall, NativeProtocol.getSuccessResultsFromIntent(intent));
            }
        }
        return true;
    }
    
    private static boolean handleCanPresent(final Context context, final Iterable<? extends DialogFeature> iterable) {
        final String actionForFeatures = getActionForFeatures(iterable);
        String s;
        if (Utility.isNullOrEmpty(s = Settings.getApplicationId())) {
            s = Utility.getMetadataApplicationId(context);
        }
        return getProtocolVersionForNativeDialog(context, actionForFeatures, getVersionSpecForFeatures(s, actionForFeatures, iterable)) != -1;
    }
    
    private static void logDialogActivity(Activity activity, final Fragment fragment, final String s, final String s2) {
        if (fragment != null) {
            activity = fragment.getActivity();
        }
        final AppEventsLogger logger = AppEventsLogger.newLogger((Context)activity);
        final Bundle bundle = new Bundle();
        bundle.putString("fb_dialog_outcome", s2);
        logger.logSdkEvent(s, null, bundle);
    }
    
    public PendingCall present() {
        logDialogActivity(this.activity, this.fragment, getEventName(this.appCall.getRequestIntent()), "Completed");
        Label_0044: {
            if (this.onPresentCallback == null) {
                break Label_0044;
            }
            while (true) {
                while (true) {
                    try {
                        this.onPresentCallback.onPresent((Context)this.activity);
                        if (this.fragment != null) {
                            this.fragment.startActivityForResult(this.appCall.getRequestIntent(), this.appCall.getRequestCode());
                            return this.appCall;
                        }
                    }
                    catch (Exception ex) {
                        throw new FacebookException(ex);
                    }
                    this.activity.startActivityForResult(this.appCall.getRequestIntent(), this.appCall.getRequestCode());
                    continue;
                }
            }
        }
    }
    
    public abstract static class Builder<CONCRETE extends Builder<?>>
    {
        protected final Activity activity;
        protected final PendingCall appCall;
        protected final String applicationId;
        protected String applicationName;
        protected Fragment fragment;
        protected HashMap<String, Bitmap> imageAttachments;
        protected HashMap<String, File> mediaAttachmentFiles;
        
        public Builder(final Activity activity) {
            this.imageAttachments = new HashMap<String, Bitmap>();
            this.mediaAttachmentFiles = new HashMap<String, File>();
            Validate.notNull(activity, "activity");
            this.activity = activity;
            this.applicationId = Utility.getMetadataApplicationId((Context)activity);
            this.appCall = new PendingCall(64207);
        }
        
        protected CONCRETE addImageAttachment(final String s, final Bitmap bitmap) {
            this.imageAttachments.put(s, bitmap);
            return (CONCRETE)this;
        }
        
        protected CONCRETE addImageAttachment(final String s, final File file) {
            this.mediaAttachmentFiles.put(s, file);
            return (CONCRETE)this;
        }
        
        protected List<String> addImageAttachmentFiles(final Collection<File> collection) {
            final ArrayList<String> list = new ArrayList<String>();
            for (final File file : collection) {
                final String string = UUID.randomUUID().toString();
                this.addImageAttachment(string, file);
                list.add(NativeAppCallContentProvider.getAttachmentUrl(this.applicationId, this.appCall.getCallId(), string));
            }
            return list;
        }
        
        protected List<String> addImageAttachments(final Collection<Bitmap> collection) {
            final ArrayList<String> list = new ArrayList<String>();
            for (final Bitmap bitmap : collection) {
                final String string = UUID.randomUUID().toString();
                this.addImageAttachment(string, bitmap);
                list.add(NativeAppCallContentProvider.getAttachmentUrl(this.applicationId, this.appCall.getCallId(), string));
            }
            return list;
        }
        
        protected CONCRETE addVideoAttachment(final String s, final File file) {
            this.mediaAttachmentFiles.put(s, file);
            return (CONCRETE)this;
        }
        
        protected String addVideoAttachmentFile(final File file) {
            final String string = UUID.randomUUID().toString();
            this.addVideoAttachment(string, file);
            return NativeAppCallContentProvider.getAttachmentUrl(this.applicationId, this.appCall.getCallId(), string);
        }
        
        public FacebookDialog build() {
            this.validate();
            final String access$100 = getActionForFeatures(this.getDialogFeatures());
            final int access$101 = getProtocolVersionForNativeDialog((Context)this.activity, access$100, getVersionSpecForFeatures(this.applicationId, access$100, this.getDialogFeatures()));
            Bundle bundle;
            if (NativeProtocol.isVersionCompatibleWithBucketedIntent(access$101)) {
                bundle = this.getMethodArguments();
            }
            else {
                bundle = this.setBundleExtras(new Bundle());
            }
            final Intent platformActivityIntent = NativeProtocol.createPlatformActivityIntent((Context)this.activity, this.appCall.getCallId().toString(), access$100, access$101, this.applicationName, bundle);
            if (platformActivityIntent == null) {
                logDialogActivity(this.activity, this.fragment, getEventName(access$100, bundle.containsKey("com.facebook.platform.extra.PHOTOS"), false), "Failed");
                throw new FacebookException("Unable to create Intent; this likely means the Facebook app is not installed.");
            }
            this.appCall.setRequestIntent(platformActivityIntent);
            return new FacebookDialog(this.activity, this.fragment, this.appCall, this.getOnPresentCallback(), null);
        }
        
        public boolean canPresent() {
            return handleCanPresent((Context)this.activity, this.getDialogFeatures());
        }
        
        protected abstract EnumSet<? extends DialogFeature> getDialogFeatures();
        
        List<String> getImageAttachmentNames() {
            return new ArrayList<String>(this.imageAttachments.keySet());
        }
        
        protected abstract Bundle getMethodArguments();
        
        OnPresentCallback getOnPresentCallback() {
            return new OnPresentCallback() {
                @Override
                public void onPresent(final Context context) throws Exception {
                    if (Builder.this.imageAttachments != null && Builder.this.imageAttachments.size() > 0) {
                        getAttachmentStore().addAttachmentsForCall(context, Builder.this.appCall.getCallId(), Builder.this.imageAttachments);
                    }
                    if (Builder.this.mediaAttachmentFiles != null && Builder.this.mediaAttachmentFiles.size() > 0) {
                        getAttachmentStore().addAttachmentFilesForCall(context, Builder.this.appCall.getCallId(), Builder.this.mediaAttachmentFiles);
                    }
                }
            };
        }
        
        protected String getWebFallbackUrlInternal() {
            final EnumSet<? extends DialogFeature> dialogFeatures = this.getDialogFeatures();
            String name = null;
            String action = null;
            final Iterator<DialogFeature> iterator = dialogFeatures.iterator();
            if (iterator.hasNext()) {
                final DialogFeature dialogFeature = iterator.next();
                name = dialogFeature.name();
                action = dialogFeature.getAction();
            }
            final Utility.DialogFeatureConfig dialogFeatureConfig = Utility.getDialogFeatureConfig(this.applicationId, action, name);
            if (dialogFeatureConfig != null) {
                final Uri fallbackUrl = dialogFeatureConfig.getFallbackUrl();
                if (fallbackUrl != null) {
                    final Bundle queryParamsForPlatformActivityIntentWebFallback = ServerProtocol.getQueryParamsForPlatformActivityIntentWebFallback((Context)this.activity, this.appCall.getCallId().toString(), NativeProtocol.getLatestKnownVersion(), this.applicationName, this.getMethodArguments());
                    if (queryParamsForPlatformActivityIntentWebFallback != null) {
                        Uri buildUri = fallbackUrl;
                        if (fallbackUrl.isRelative()) {
                            buildUri = Utility.buildUri(ServerProtocol.getDialogAuthority(), fallbackUrl.toString(), queryParamsForPlatformActivityIntentWebFallback);
                        }
                        return buildUri.toString();
                    }
                }
            }
            return null;
        }
        
        protected void putExtra(final Bundle bundle, final String s, final String s2) {
            if (s2 != null) {
                bundle.putString(s, s2);
            }
        }
        
        public CONCRETE setApplicationName(final String applicationName) {
            this.applicationName = applicationName;
            return (CONCRETE)this;
        }
        
        protected Bundle setBundleExtras(final Bundle bundle) {
            return bundle;
        }
        
        public CONCRETE setFragment(final Fragment fragment) {
            this.fragment = fragment;
            return (CONCRETE)this;
        }
        
        public CONCRETE setRequestCode(final int n) {
            this.appCall.setRequestCode(n);
            return (CONCRETE)this;
        }
        
        void validate() {
        }
    }
    
    public interface Callback
    {
        void onComplete(final PendingCall p0, final Bundle p1);
        
        void onError(final PendingCall p0, final Exception p1, final Bundle p2);
    }
    
    public interface DialogFeature
    {
        String getAction();
        
        int getMinVersion();
        
        String name();
    }
    
    public static class MessageDialogBuilder extends ShareDialogBuilderBase<MessageDialogBuilder>
    {
        public MessageDialogBuilder(final Activity activity) {
            super(activity);
        }
        
        @Override
        protected EnumSet<? extends DialogFeature> getDialogFeatures() {
            return EnumSet.of((DialogFeature)MessageDialogFeature.MESSAGE_DIALOG);
        }
        
        public MessageDialogBuilder setFriends(final List<String> list) {
            return this;
        }
        
        public MessageDialogBuilder setPlace(final String s) {
            return this;
        }
    }
    
    public enum MessageDialogFeature implements DialogFeature
    {
        MESSAGE_DIALOG(20140204), 
        PHOTOS(20140324), 
        VIDEO(20141218);
        
        private int minVersion;
        
        private MessageDialogFeature(final int minVersion) {
            this.minVersion = minVersion;
        }
        
        @Override
        public String getAction() {
            return "com.facebook.platform.action.request.MESSAGE_DIALOG";
        }
        
        @Override
        public int getMinVersion() {
            return this.minVersion;
        }
    }
    
    interface OnPresentCallback
    {
        void onPresent(final Context p0) throws Exception;
    }
    
    public static class OpenGraphActionDialogBuilder extends OpenGraphDialogBuilderBase<OpenGraphActionDialogBuilder>
    {
        public OpenGraphActionDialogBuilder(final Activity activity, final OpenGraphAction openGraphAction, final String s) {
            super(activity, openGraphAction, s);
        }
        
        public OpenGraphActionDialogBuilder(final Activity activity, final OpenGraphAction openGraphAction, final String s, final String s2) {
            super(activity, openGraphAction, s, s2);
        }
        
        @Override
        protected EnumSet<? extends DialogFeature> getDialogFeatures() {
            return EnumSet.of((DialogFeature)OpenGraphActionDialogFeature.OG_ACTION_DIALOG);
        }
    }
    
    public enum OpenGraphActionDialogFeature implements DialogFeature
    {
        OG_ACTION_DIALOG(20130618);
        
        private int minVersion;
        
        private OpenGraphActionDialogFeature(final int minVersion) {
            this.minVersion = minVersion;
        }
        
        @Override
        public String getAction() {
            return "com.facebook.platform.action.request.OGACTIONPUBLISH_DIALOG";
        }
        
        @Override
        public int getMinVersion() {
            return this.minVersion;
        }
    }
    
    private abstract static class OpenGraphDialogBuilderBase<CONCRETE extends OpenGraphDialogBuilderBase<?>> extends Builder<CONCRETE>
    {
        private OpenGraphAction action;
        private String actionType;
        private boolean dataErrorsFatal;
        private String previewPropertyName;
        
        public OpenGraphDialogBuilderBase(final Activity activity, final OpenGraphAction action, final String previewPropertyName) {
            super(activity);
            Validate.notNull(action, "action");
            Validate.notNullOrEmpty(action.getType(), "action.getType()");
            Validate.notNullOrEmpty(previewPropertyName, "previewPropertyName");
            if (action.getProperty(previewPropertyName) == null) {
                throw new IllegalArgumentException("A property named \"" + previewPropertyName + "\" was not found on the action.  The name of " + "the preview property must match the name of an action property.");
            }
            this.action = action;
            this.actionType = action.getType();
            this.previewPropertyName = previewPropertyName;
        }
        
        public OpenGraphDialogBuilderBase(final Activity activity, final OpenGraphAction action, final String actionType, final String previewPropertyName) {
            super(activity);
            Validate.notNull(action, "action");
            Validate.notNullOrEmpty(actionType, "actionType");
            Validate.notNullOrEmpty(previewPropertyName, "previewPropertyName");
            if (action.getProperty(previewPropertyName) == null) {
                throw new IllegalArgumentException("A property named \"" + previewPropertyName + "\" was not found on the action.  The name of " + "the preview property must match the name of an action property.");
            }
            final String type = action.getType();
            if (!Utility.isNullOrEmpty(type) && !type.equals(actionType)) {
                throw new IllegalArgumentException("'actionType' must match the type of 'action' if it is specified. Consider using OpenGraphDialogBuilderBase(Activity activity, OpenGraphAction action, String previewPropertyName) instead.");
            }
            this.action = action;
            this.actionType = actionType;
            this.previewPropertyName = previewPropertyName;
        }
        
        private JSONObject flattenChildrenOfGraphObject(JSONObject o) {
            try {
                o = new JSONObject(((JSONObject)o).toString());
                try {
                    final Iterator keys = ((JSONObject)o).keys();
                    while (keys.hasNext()) {
                        final String s = keys.next();
                        if (!s.equalsIgnoreCase("image")) {
                            ((JSONObject)o).put(s, this.flattenObject(((JSONObject)o).get(s)));
                        }
                    }
                    return (JSONObject)o;
                }
                catch (JSONException ex) {}
                throw new FacebookException((Throwable)o);
            }
            catch (JSONException o) {
                throw new FacebookException((Throwable)o);
            }
        }
        
        private Object flattenObject(final Object o) throws JSONException {
            Object o2;
            if (o == null) {
                o2 = null;
            }
            else if (o instanceof JSONObject) {
                final JSONObject jsonObject = (JSONObject)o;
                o2 = o;
                if (!jsonObject.optBoolean("fbsdk:create_object")) {
                    if (jsonObject.has("id")) {
                        return jsonObject.getString("id");
                    }
                    o2 = o;
                    if (jsonObject.has("url")) {
                        return jsonObject.getString("url");
                    }
                }
            }
            else {
                o2 = o;
                if (o instanceof JSONArray) {
                    final JSONArray jsonArray = (JSONArray)o;
                    final JSONArray jsonArray2 = new JSONArray();
                    for (int length = jsonArray.length(), i = 0; i < length; ++i) {
                        jsonArray2.put(this.flattenObject(jsonArray.get(i)));
                    }
                    return jsonArray2;
                }
            }
            return o2;
        }
        
        private void updateActionAttachmentUrls(final List<String> list, final boolean b) {
            List<JSONObject> image;
            if ((image = this.action.getImage()) == null) {
                image = new ArrayList<JSONObject>(list.size());
            }
            for (final String s : list) {
                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("url", (Object)s);
                    if (b) {
                        jsonObject.put("user_generated", true);
                    }
                    image.add(jsonObject);
                    continue;
                }
                catch (JSONException ex) {
                    throw new FacebookException("Unable to attach images", (Throwable)ex);
                }
                break;
            }
            this.action.setImage(image);
        }
        
        @Override
        protected Bundle getMethodArguments() {
            final Bundle bundle = new Bundle();
            this.putExtra(bundle, "PREVIEW_PROPERTY_NAME", this.previewPropertyName);
            this.putExtra(bundle, "ACTION_TYPE", this.actionType);
            bundle.putBoolean("DATA_FAILURES_FATAL", this.dataErrorsFatal);
            this.putExtra(bundle, "ACTION", this.flattenChildrenOfGraphObject(this.action.getInnerJSONObject()).toString());
            return bundle;
        }
        
        @Override
        protected Bundle setBundleExtras(final Bundle bundle) {
            this.putExtra(bundle, "com.facebook.platform.extra.PREVIEW_PROPERTY_NAME", this.previewPropertyName);
            this.putExtra(bundle, "com.facebook.platform.extra.ACTION_TYPE", this.actionType);
            bundle.putBoolean("com.facebook.platform.extra.DATA_FAILURES_FATAL", this.dataErrorsFatal);
            this.putExtra(bundle, "com.facebook.platform.extra.ACTION", this.flattenChildrenOfGraphObject(this.action.getInnerJSONObject()).toString());
            return bundle;
        }
        
        public CONCRETE setDataErrorsFatal(final boolean dataErrorsFatal) {
            this.dataErrorsFatal = dataErrorsFatal;
            return (CONCRETE)this;
        }
        
        public CONCRETE setImageAttachmentFilesForAction(final List<File> list) {
            return this.setImageAttachmentFilesForAction(list, false);
        }
        
        public CONCRETE setImageAttachmentFilesForAction(final List<File> list, final boolean b) {
            Validate.containsNoNulls((Collection<Object>)list, "bitmapFiles");
            if (this.action == null) {
                throw new FacebookException("Can not set attachments prior to setting action.");
            }
            this.updateActionAttachmentUrls(this.addImageAttachmentFiles(list), b);
            return (CONCRETE)this;
        }
        
        public CONCRETE setImageAttachmentFilesForObject(final String s, final List<File> list) {
            return this.setImageAttachmentFilesForObject(s, list, false);
        }
        
        public CONCRETE setImageAttachmentFilesForObject(final String s, final List<File> list, final boolean b) {
            Validate.notNull(s, "objectProperty");
            Validate.containsNoNulls((Collection<Object>)list, "bitmapFiles");
            if (this.action == null) {
                throw new FacebookException("Can not set attachments prior to setting action.");
            }
            this.updateObjectAttachmentUrls(s, this.addImageAttachmentFiles(list), b);
            return (CONCRETE)this;
        }
        
        public CONCRETE setImageAttachmentsForAction(final List<Bitmap> list) {
            return this.setImageAttachmentsForAction(list, false);
        }
        
        public CONCRETE setImageAttachmentsForAction(final List<Bitmap> list, final boolean b) {
            Validate.containsNoNulls((Collection<Object>)list, "bitmaps");
            if (this.action == null) {
                throw new FacebookException("Can not set attachments prior to setting action.");
            }
            this.updateActionAttachmentUrls(this.addImageAttachments(list), b);
            return (CONCRETE)this;
        }
        
        public CONCRETE setImageAttachmentsForObject(final String s, final List<Bitmap> list) {
            return this.setImageAttachmentsForObject(s, list, false);
        }
        
        public CONCRETE setImageAttachmentsForObject(final String s, final List<Bitmap> list, final boolean b) {
            Validate.notNull(s, "objectProperty");
            Validate.containsNoNulls((Collection<Object>)list, "bitmaps");
            if (this.action == null) {
                throw new FacebookException("Can not set attachments prior to setting action.");
            }
            this.updateObjectAttachmentUrls(s, this.addImageAttachments(list), b);
            return (CONCRETE)this;
        }
        
        void updateObjectAttachmentUrls(final String s, final List<String> list, final boolean b) {
            OpenGraphObject openGraphObject;
            try {
                openGraphObject = this.action.getPropertyAs(s, OpenGraphObject.class);
                if (openGraphObject == null) {
                    throw new IllegalArgumentException("Action does not contain a property '" + s + "'");
                }
            }
            catch (FacebookGraphObjectException ex) {
                throw new IllegalArgumentException("Property '" + s + "' is not a graph object");
            }
            if (!openGraphObject.getCreateObject()) {
                throw new IllegalArgumentException("The Open Graph object in '" + s + "' is not marked for creation");
            }
            GraphObjectList<GraphObject> image;
            if ((image = openGraphObject.getImage()) == null) {
                image = GraphObject.Factory.createList(GraphObject.class);
            }
            for (final String s2 : list) {
                final GraphObject create = GraphObject.Factory.create();
                create.setProperty("url", s2);
                if (b) {
                    create.setProperty("user_generated", true);
                }
                image.add(create);
            }
            openGraphObject.setImage(image);
        }
    }
    
    public static class OpenGraphMessageDialogBuilder extends OpenGraphDialogBuilderBase<OpenGraphMessageDialogBuilder>
    {
        public OpenGraphMessageDialogBuilder(final Activity activity, final OpenGraphAction openGraphAction, final String s) {
            super(activity, openGraphAction, s);
        }
        
        @Override
        protected EnumSet<? extends DialogFeature> getDialogFeatures() {
            return EnumSet.of((DialogFeature)OpenGraphMessageDialogFeature.OG_MESSAGE_DIALOG);
        }
    }
    
    public enum OpenGraphMessageDialogFeature implements DialogFeature
    {
        OG_MESSAGE_DIALOG(20140204);
        
        private int minVersion;
        
        private OpenGraphMessageDialogFeature(final int minVersion) {
            this.minVersion = minVersion;
        }
        
        @Override
        public String getAction() {
            return "com.facebook.platform.action.request.OGMESSAGEPUBLISH_DIALOG";
        }
        
        @Override
        public int getMinVersion() {
            return this.minVersion;
        }
    }
    
    public static class PendingCall implements Parcelable
    {
        public static final Parcelable$Creator<PendingCall> CREATOR;
        private UUID callId;
        private int requestCode;
        private Intent requestIntent;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<PendingCall>() {
                public PendingCall createFromParcel(final Parcel parcel) {
                    return new PendingCall(parcel);
                }
                
                public PendingCall[] newArray(final int n) {
                    return new PendingCall[n];
                }
            };
        }
        
        public PendingCall(final int requestCode) {
            this.callId = UUID.randomUUID();
            this.requestCode = requestCode;
        }
        
        private PendingCall(final Parcel parcel) {
            this.callId = UUID.fromString(parcel.readString());
            this.requestIntent = (Intent)parcel.readParcelable((ClassLoader)null);
            this.requestCode = parcel.readInt();
        }
        
        private void setRequestCode(final int requestCode) {
            this.requestCode = requestCode;
        }
        
        private void setRequestIntent(final Intent requestIntent) {
            this.requestIntent = requestIntent;
        }
        
        public int describeContents() {
            return 0;
        }
        
        public UUID getCallId() {
            return this.callId;
        }
        
        public int getRequestCode() {
            return this.requestCode;
        }
        
        public Intent getRequestIntent() {
            return this.requestIntent;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            parcel.writeString(this.callId.toString());
            parcel.writeParcelable((Parcelable)this.requestIntent, 0);
            parcel.writeInt(this.requestCode);
        }
    }
    
    private abstract static class PhotoDialogBuilderBase<CONCRETE extends PhotoDialogBuilderBase<?>> extends Builder<CONCRETE>
    {
        static int MAXIMUM_PHOTO_COUNT;
        private ArrayList<String> friends;
        private ArrayList<String> imageAttachmentUrls;
        private String place;
        
        static {
            PhotoDialogBuilderBase.MAXIMUM_PHOTO_COUNT = 6;
        }
        
        public PhotoDialogBuilderBase(final Activity activity) {
            super(activity);
            this.imageAttachmentUrls = new ArrayList<String>();
        }
        
        public CONCRETE addPhotoFiles(final Collection<File> collection) {
            this.imageAttachmentUrls.addAll(this.addImageAttachmentFiles(collection));
            return (CONCRETE)this;
        }
        
        public CONCRETE addPhotos(final Collection<Bitmap> collection) {
            this.imageAttachmentUrls.addAll(this.addImageAttachments(collection));
            return (CONCRETE)this;
        }
        
        abstract int getMaximumNumberOfPhotos();
        
        @Override
        protected Bundle getMethodArguments() {
            final Bundle bundle = new Bundle();
            this.putExtra(bundle, "PLACE", this.place);
            bundle.putStringArrayList("PHOTOS", (ArrayList)this.imageAttachmentUrls);
            if (!Utility.isNullOrEmpty(this.friends)) {
                bundle.putStringArrayList("FRIENDS", (ArrayList)this.friends);
            }
            return bundle;
        }
        
        @Override
        protected Bundle setBundleExtras(final Bundle bundle) {
            this.putExtra(bundle, "com.facebook.platform.extra.APPLICATION_ID", this.applicationId);
            this.putExtra(bundle, "com.facebook.platform.extra.APPLICATION_NAME", this.applicationName);
            this.putExtra(bundle, "com.facebook.platform.extra.PLACE", this.place);
            bundle.putStringArrayList("com.facebook.platform.extra.PHOTOS", (ArrayList)this.imageAttachmentUrls);
            if (!Utility.isNullOrEmpty(this.friends)) {
                bundle.putStringArrayList("com.facebook.platform.extra.FRIENDS", (ArrayList)this.friends);
            }
            return bundle;
        }
        
        public CONCRETE setFriends(final List<String> list) {
            ArrayList<String> friends;
            if (list == null) {
                friends = null;
            }
            else {
                friends = new ArrayList<String>(list);
            }
            this.friends = friends;
            return (CONCRETE)this;
        }
        
        public CONCRETE setPlace(final String place) {
            this.place = place;
            return (CONCRETE)this;
        }
        
        @Override
        void validate() {
            super.validate();
            if (this.imageAttachmentUrls.isEmpty()) {
                throw new FacebookException("Must specify at least one photo.");
            }
            if (this.imageAttachmentUrls.size() > this.getMaximumNumberOfPhotos()) {
                throw new FacebookException(String.format("Cannot add more than %d photos.", this.getMaximumNumberOfPhotos()));
            }
        }
    }
    
    public static class PhotoMessageDialogBuilder extends PhotoDialogBuilderBase<PhotoMessageDialogBuilder>
    {
        public PhotoMessageDialogBuilder(final Activity activity) {
            super(activity);
        }
        
        @Override
        protected EnumSet<? extends DialogFeature> getDialogFeatures() {
            return EnumSet.of((DialogFeature)MessageDialogFeature.MESSAGE_DIALOG, MessageDialogFeature.PHOTOS);
        }
        
        @Override
        int getMaximumNumberOfPhotos() {
            return PhotoMessageDialogBuilder.MAXIMUM_PHOTO_COUNT;
        }
        
        public PhotoMessageDialogBuilder setFriends(final List<String> list) {
            return this;
        }
        
        public PhotoMessageDialogBuilder setPlace(final String s) {
            return this;
        }
    }
    
    public static class PhotoShareDialogBuilder extends PhotoDialogBuilderBase<PhotoShareDialogBuilder>
    {
        public PhotoShareDialogBuilder(final Activity activity) {
            super(activity);
        }
        
        @Override
        protected EnumSet<? extends DialogFeature> getDialogFeatures() {
            return EnumSet.of((DialogFeature)ShareDialogFeature.SHARE_DIALOG, ShareDialogFeature.PHOTOS);
        }
        
        @Override
        int getMaximumNumberOfPhotos() {
            return PhotoShareDialogBuilder.MAXIMUM_PHOTO_COUNT;
        }
    }
    
    public static class ShareDialogBuilder extends ShareDialogBuilderBase<ShareDialogBuilder>
    {
        public ShareDialogBuilder(final Activity activity) {
            super(activity);
        }
        
        @Override
        protected EnumSet<? extends DialogFeature> getDialogFeatures() {
            return EnumSet.of((DialogFeature)ShareDialogFeature.SHARE_DIALOG);
        }
    }
    
    private abstract static class ShareDialogBuilderBase<CONCRETE extends ShareDialogBuilderBase<?>> extends Builder<CONCRETE>
    {
        private String caption;
        private boolean dataErrorsFatal;
        private String description;
        private ArrayList<String> friends;
        protected String link;
        private String name;
        private String picture;
        private String place;
        private String ref;
        
        public ShareDialogBuilderBase(final Activity activity) {
            super(activity);
        }
        
        @Override
        protected Bundle getMethodArguments() {
            final Bundle bundle = new Bundle();
            this.putExtra(bundle, "TITLE", this.name);
            this.putExtra(bundle, "SUBTITLE", this.caption);
            this.putExtra(bundle, "DESCRIPTION", this.description);
            this.putExtra(bundle, "LINK", this.link);
            this.putExtra(bundle, "IMAGE", this.picture);
            this.putExtra(bundle, "PLACE", this.place);
            this.putExtra(bundle, "REF", this.ref);
            bundle.putBoolean("DATA_FAILURES_FATAL", this.dataErrorsFatal);
            if (!Utility.isNullOrEmpty(this.friends)) {
                bundle.putStringArrayList("FRIENDS", (ArrayList)this.friends);
            }
            return bundle;
        }
        
        @Override
        protected Bundle setBundleExtras(final Bundle bundle) {
            this.putExtra(bundle, "com.facebook.platform.extra.APPLICATION_ID", this.applicationId);
            this.putExtra(bundle, "com.facebook.platform.extra.APPLICATION_NAME", this.applicationName);
            this.putExtra(bundle, "com.facebook.platform.extra.TITLE", this.name);
            this.putExtra(bundle, "com.facebook.platform.extra.SUBTITLE", this.caption);
            this.putExtra(bundle, "com.facebook.platform.extra.DESCRIPTION", this.description);
            this.putExtra(bundle, "com.facebook.platform.extra.LINK", this.link);
            this.putExtra(bundle, "com.facebook.platform.extra.IMAGE", this.picture);
            this.putExtra(bundle, "com.facebook.platform.extra.PLACE", this.place);
            this.putExtra(bundle, "com.facebook.platform.extra.REF", this.ref);
            bundle.putBoolean("com.facebook.platform.extra.DATA_FAILURES_FATAL", this.dataErrorsFatal);
            if (!Utility.isNullOrEmpty(this.friends)) {
                bundle.putStringArrayList("com.facebook.platform.extra.FRIENDS", (ArrayList)this.friends);
            }
            return bundle;
        }
        
        public CONCRETE setCaption(final String caption) {
            this.caption = caption;
            return (CONCRETE)this;
        }
        
        public CONCRETE setDataErrorsFatal(final boolean dataErrorsFatal) {
            this.dataErrorsFatal = dataErrorsFatal;
            return (CONCRETE)this;
        }
        
        public CONCRETE setDescription(final String description) {
            this.description = description;
            return (CONCRETE)this;
        }
        
        public CONCRETE setFriends(final List<String> list) {
            ArrayList<String> friends;
            if (list == null) {
                friends = null;
            }
            else {
                friends = new ArrayList<String>(list);
            }
            this.friends = friends;
            return (CONCRETE)this;
        }
        
        public CONCRETE setLink(final String link) {
            this.link = link;
            return (CONCRETE)this;
        }
        
        public CONCRETE setName(final String name) {
            this.name = name;
            return (CONCRETE)this;
        }
        
        public CONCRETE setPicture(final String picture) {
            this.picture = picture;
            return (CONCRETE)this;
        }
        
        public CONCRETE setPlace(final String place) {
            this.place = place;
            return (CONCRETE)this;
        }
        
        public CONCRETE setRef(final String ref) {
            this.ref = ref;
            return (CONCRETE)this;
        }
    }
    
    public enum ShareDialogFeature implements DialogFeature
    {
        PHOTOS(20140204), 
        SHARE_DIALOG(20130618), 
        VIDEO(20141028);
        
        private int minVersion;
        
        private ShareDialogFeature(final int minVersion) {
            this.minVersion = minVersion;
        }
        
        @Override
        public String getAction() {
            return "com.facebook.platform.action.request.FEED_DIALOG";
        }
        
        @Override
        public int getMinVersion() {
            return this.minVersion;
        }
    }
    
    private abstract static class VideoDialogBuilderBase<CONCRETE extends VideoDialogBuilderBase<?>> extends Builder<CONCRETE>
    {
        private String place;
        private String videoAttachmentUrl;
        
        public VideoDialogBuilderBase(final Activity activity) {
            super(activity);
        }
        
        public CONCRETE addVideoFile(final File file) {
            this.videoAttachmentUrl = this.addVideoAttachmentFile(file);
            return (CONCRETE)this;
        }
        
        @Override
        protected Bundle getMethodArguments() {
            final Bundle bundle = new Bundle();
            this.putExtra(bundle, "PLACE", this.place);
            bundle.putString("VIDEO", this.videoAttachmentUrl);
            return bundle;
        }
        
        public CONCRETE setPlace(final String place) {
            this.place = place;
            return (CONCRETE)this;
        }
        
        public CONCRETE setVideoUrl(final String videoAttachmentUrl) {
            this.videoAttachmentUrl = videoAttachmentUrl;
            return (CONCRETE)this;
        }
        
        @Override
        void validate() {
            super.validate();
            if (this.videoAttachmentUrl == null || this.videoAttachmentUrl.isEmpty()) {
                throw new FacebookException("Must specify at least one video.");
            }
        }
    }
    
    public static class VideoMessageDialogBuilder extends VideoDialogBuilderBase<VideoMessageDialogBuilder>
    {
        public VideoMessageDialogBuilder(final Activity activity) {
            super(activity);
        }
        
        @Override
        protected EnumSet<MessageDialogFeature> getDialogFeatures() {
            return EnumSet.of(MessageDialogFeature.MESSAGE_DIALOG, MessageDialogFeature.VIDEO);
        }
        
        public VideoMessageDialogBuilder setPlace(final String s) {
            return this;
        }
    }
    
    public static class VideoShareDialogBuilder extends VideoDialogBuilderBase<VideoShareDialogBuilder>
    {
        public VideoShareDialogBuilder(final Activity activity) {
            super(activity);
        }
        
        @Override
        protected EnumSet<? extends DialogFeature> getDialogFeatures() {
            return EnumSet.of((DialogFeature)ShareDialogFeature.SHARE_DIALOG, ShareDialogFeature.VIDEO);
        }
    }
}
