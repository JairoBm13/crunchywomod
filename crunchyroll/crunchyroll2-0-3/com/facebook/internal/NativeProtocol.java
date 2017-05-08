// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.internal;

import android.content.pm.Signature;
import android.content.pm.PackageManager$NameNotFoundException;
import android.os.Build;
import java.util.HashSet;
import android.content.pm.ResolveInfo;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import java.util.UUID;
import android.database.Cursor;
import android.content.ContentResolver;
import com.facebook.Settings;
import android.text.TextUtils;
import java.util.Collection;
import com.facebook.SessionDefaultAudience;
import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import java.util.Iterator;
import java.util.TreeSet;
import android.net.Uri;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Map;
import java.util.List;

public final class NativeProtocol
{
    public static final String ACTION_FEED_DIALOG = "com.facebook.platform.action.request.FEED_DIALOG";
    public static final String ACTION_FEED_DIALOG_REPLY = "com.facebook.platform.action.reply.FEED_DIALOG";
    public static final String ACTION_LIKE_DIALOG = "com.facebook.platform.action.request.LIKE_DIALOG";
    public static final String ACTION_LIKE_DIALOG_REPLY = "com.facebook.platform.action.reply.LIKE_DIALOG";
    public static final String ACTION_MESSAGE_DIALOG = "com.facebook.platform.action.request.MESSAGE_DIALOG";
    public static final String ACTION_MESSAGE_DIALOG_REPLY = "com.facebook.platform.action.reply.MESSAGE_DIALOG";
    public static final String ACTION_OGACTIONPUBLISH_DIALOG = "com.facebook.platform.action.request.OGACTIONPUBLISH_DIALOG";
    public static final String ACTION_OGACTIONPUBLISH_DIALOG_REPLY = "com.facebook.platform.action.reply.OGACTIONPUBLISH_DIALOG";
    public static final String ACTION_OGMESSAGEPUBLISH_DIALOG = "com.facebook.platform.action.request.OGMESSAGEPUBLISH_DIALOG";
    public static final String ACTION_OGMESSAGEPUBLISH_DIALOG_REPLY = "com.facebook.platform.action.reply.OGMESSAGEPUBLISH_DIALOG";
    public static final String AUDIENCE_EVERYONE = "everyone";
    public static final String AUDIENCE_FRIENDS = "friends";
    public static final String AUDIENCE_ME = "only_me";
    public static final String BRIDGE_ARG_ACTION_ID_STRING = "action_id";
    public static final String BRIDGE_ARG_APP_NAME_STRING = "app_name";
    public static final String BRIDGE_ARG_ERROR_BUNDLE = "error";
    public static final String BRIDGE_ARG_ERROR_CODE = "error_code";
    public static final String BRIDGE_ARG_ERROR_DESCRIPTION = "error_description";
    public static final String BRIDGE_ARG_ERROR_JSON = "error_json";
    public static final String BRIDGE_ARG_ERROR_SUBCODE = "error_subcode";
    public static final String BRIDGE_ARG_ERROR_TYPE = "error_type";
    private static final String CONTENT_SCHEME = "content://";
    public static final int DIALOG_REQUEST_CODE = 64207;
    public static final String ERROR_APPLICATION_ERROR = "ApplicationError";
    public static final String ERROR_NETWORK_ERROR = "NetworkError";
    public static final String ERROR_PERMISSION_DENIED = "PermissionDenied";
    public static final String ERROR_PROTOCOL_ERROR = "ProtocolError";
    public static final String ERROR_SERVICE_DISABLED = "ServiceDisabled";
    public static final String ERROR_UNKNOWN_ERROR = "UnknownError";
    public static final String ERROR_USER_CANCELED = "UserCanceled";
    public static final String EXTRA_ACCESS_TOKEN = "com.facebook.platform.extra.ACCESS_TOKEN";
    public static final String EXTRA_ACTION = "com.facebook.platform.extra.ACTION";
    public static final String EXTRA_ACTION_TYPE = "com.facebook.platform.extra.ACTION_TYPE";
    public static final String EXTRA_APPLICATION_ID = "com.facebook.platform.extra.APPLICATION_ID";
    public static final String EXTRA_APPLICATION_NAME = "com.facebook.platform.extra.APPLICATION_NAME";
    public static final String EXTRA_DATA_FAILURES_FATAL = "com.facebook.platform.extra.DATA_FAILURES_FATAL";
    public static final String EXTRA_DESCRIPTION = "com.facebook.platform.extra.DESCRIPTION";
    public static final String EXTRA_EXPIRES_SECONDS_SINCE_EPOCH = "com.facebook.platform.extra.EXPIRES_SECONDS_SINCE_EPOCH";
    public static final String EXTRA_FRIEND_TAGS = "com.facebook.platform.extra.FRIENDS";
    public static final String EXTRA_GET_INSTALL_DATA_PACKAGE = "com.facebook.platform.extra.INSTALLDATA_PACKAGE";
    public static final String EXTRA_IMAGE = "com.facebook.platform.extra.IMAGE";
    public static final String EXTRA_LIKE_COUNT_STRING_WITHOUT_LIKE = "com.facebook.platform.extra.LIKE_COUNT_STRING_WITHOUT_LIKE";
    public static final String EXTRA_LIKE_COUNT_STRING_WITH_LIKE = "com.facebook.platform.extra.LIKE_COUNT_STRING_WITH_LIKE";
    public static final String EXTRA_LINK = "com.facebook.platform.extra.LINK";
    public static final String EXTRA_OBJECT_ID = "com.facebook.platform.extra.OBJECT_ID";
    public static final String EXTRA_OBJECT_IS_LIKED = "com.facebook.platform.extra.OBJECT_IS_LIKED";
    public static final String EXTRA_PERMISSIONS = "com.facebook.platform.extra.PERMISSIONS";
    public static final String EXTRA_PHOTOS = "com.facebook.platform.extra.PHOTOS";
    public static final String EXTRA_PLACE_TAG = "com.facebook.platform.extra.PLACE";
    public static final String EXTRA_PREVIEW_PROPERTY_NAME = "com.facebook.platform.extra.PREVIEW_PROPERTY_NAME";
    public static final String EXTRA_PROTOCOL_ACTION = "com.facebook.platform.protocol.PROTOCOL_ACTION";
    public static final String EXTRA_PROTOCOL_BRIDGE_ARGS = "com.facebook.platform.protocol.BRIDGE_ARGS";
    public static final String EXTRA_PROTOCOL_CALL_ID = "com.facebook.platform.protocol.CALL_ID";
    public static final String EXTRA_PROTOCOL_METHOD_ARGS = "com.facebook.platform.protocol.METHOD_ARGS";
    public static final String EXTRA_PROTOCOL_METHOD_RESULTS = "com.facebook.platform.protocol.RESULT_ARGS";
    public static final String EXTRA_PROTOCOL_VERSION = "com.facebook.platform.protocol.PROTOCOL_VERSION";
    static final String EXTRA_PROTOCOL_VERSIONS = "com.facebook.platform.extra.PROTOCOL_VERSIONS";
    public static final String EXTRA_REF = "com.facebook.platform.extra.REF";
    public static final String EXTRA_SOCIAL_SENTENCE_WITHOUT_LIKE = "com.facebook.platform.extra.SOCIAL_SENTENCE_WITHOUT_LIKE";
    public static final String EXTRA_SOCIAL_SENTENCE_WITH_LIKE = "com.facebook.platform.extra.SOCIAL_SENTENCE_WITH_LIKE";
    public static final String EXTRA_SUBTITLE = "com.facebook.platform.extra.SUBTITLE";
    public static final String EXTRA_TITLE = "com.facebook.platform.extra.TITLE";
    public static final String EXTRA_UNLIKE_TOKEN = "com.facebook.platform.extra.UNLIKE_TOKEN";
    private static final NativeAppInfo FACEBOOK_APP_INFO;
    private static final String FACEBOOK_PROXY_AUTH_ACTIVITY = "com.facebook.katana.ProxyAuth";
    public static final String FACEBOOK_PROXY_AUTH_APP_ID_KEY = "client_id";
    public static final String FACEBOOK_PROXY_AUTH_E2E_KEY = "e2e";
    public static final String FACEBOOK_PROXY_AUTH_PERMISSIONS_KEY = "scope";
    private static final String FACEBOOK_TOKEN_REFRESH_ACTIVITY = "com.facebook.katana.platform.TokenRefreshService";
    public static final String IMAGE_URL_KEY = "url";
    public static final String IMAGE_USER_GENERATED_KEY = "user_generated";
    static final String INTENT_ACTION_PLATFORM_ACTIVITY = "com.facebook.platform.PLATFORM_ACTIVITY";
    static final String INTENT_ACTION_PLATFORM_SERVICE = "com.facebook.platform.PLATFORM_SERVICE";
    private static final List<Integer> KNOWN_PROTOCOL_VERSIONS;
    public static final int MESSAGE_GET_ACCESS_TOKEN_REPLY = 65537;
    public static final int MESSAGE_GET_ACCESS_TOKEN_REQUEST = 65536;
    public static final int MESSAGE_GET_INSTALL_DATA_REPLY = 65541;
    public static final int MESSAGE_GET_INSTALL_DATA_REQUEST = 65540;
    public static final int MESSAGE_GET_LIKE_STATUS_REPLY = 65543;
    public static final int MESSAGE_GET_LIKE_STATUS_REQUEST = 65542;
    static final int MESSAGE_GET_PROTOCOL_VERSIONS_REPLY = 65539;
    static final int MESSAGE_GET_PROTOCOL_VERSIONS_REQUEST = 65538;
    public static final String METHOD_ARGS_ACTION = "ACTION";
    public static final String METHOD_ARGS_ACTION_TYPE = "ACTION_TYPE";
    public static final String METHOD_ARGS_DATA_FAILURES_FATAL = "DATA_FAILURES_FATAL";
    public static final String METHOD_ARGS_DESCRIPTION = "DESCRIPTION";
    public static final String METHOD_ARGS_FRIEND_TAGS = "FRIENDS";
    public static final String METHOD_ARGS_IMAGE = "IMAGE";
    public static final String METHOD_ARGS_LINK = "LINK";
    public static final String METHOD_ARGS_OBJECT_ID = "object_id";
    public static final String METHOD_ARGS_PHOTOS = "PHOTOS";
    public static final String METHOD_ARGS_PLACE_TAG = "PLACE";
    public static final String METHOD_ARGS_PREVIEW_PROPERTY_NAME = "PREVIEW_PROPERTY_NAME";
    public static final String METHOD_ARGS_REF = "REF";
    public static final String METHOD_ARGS_SUBTITLE = "SUBTITLE";
    public static final String METHOD_ARGS_TITLE = "TITLE";
    public static final String METHOD_ARGS_VIDEO = "VIDEO";
    public static final int NO_PROTOCOL_AVAILABLE = -1;
    public static final String OPEN_GRAPH_CREATE_OBJECT_KEY = "fbsdk:create_object";
    private static final String PLATFORM_PROVIDER_VERSIONS = ".provider.PlatformProvider/versions";
    private static final String PLATFORM_PROVIDER_VERSION_COLUMN = "version";
    public static final int PROTOCOL_VERSION_20121101 = 20121101;
    public static final int PROTOCOL_VERSION_20130502 = 20130502;
    public static final int PROTOCOL_VERSION_20130618 = 20130618;
    public static final int PROTOCOL_VERSION_20131107 = 20131107;
    public static final int PROTOCOL_VERSION_20140204 = 20140204;
    public static final int PROTOCOL_VERSION_20140324 = 20140324;
    public static final int PROTOCOL_VERSION_20140701 = 20140701;
    public static final int PROTOCOL_VERSION_20141001 = 20141001;
    public static final int PROTOCOL_VERSION_20141028 = 20141028;
    public static final int PROTOCOL_VERSION_20141107 = 20141107;
    public static final int PROTOCOL_VERSION_20141218 = 20141218;
    public static final String RESULT_ARGS_ACCESS_TOKEN = "access_token";
    public static final String RESULT_ARGS_EXPIRES_SECONDS_SINCE_EPOCH = "expires_seconds_since_epoch";
    public static final String RESULT_ARGS_PERMISSIONS = "permissions";
    public static final String STATUS_ERROR_CODE = "com.facebook.platform.status.ERROR_CODE";
    public static final String STATUS_ERROR_DESCRIPTION = "com.facebook.platform.status.ERROR_DESCRIPTION";
    public static final String STATUS_ERROR_JSON = "com.facebook.platform.status.ERROR_JSON";
    public static final String STATUS_ERROR_SUBCODE = "com.facebook.platform.status.ERROR_SUBCODE";
    public static final String STATUS_ERROR_TYPE = "com.facebook.platform.status.ERROR_TYPE";
    private static Map<String, List<NativeAppInfo>> actionToAppInfoMap;
    private static List<NativeAppInfo> facebookAppInfoList;
    
    static {
        FACEBOOK_APP_INFO = (NativeAppInfo)new KatanaAppInfo();
        NativeProtocol.facebookAppInfoList = buildFacebookAppList();
        NativeProtocol.actionToAppInfoMap = buildActionToAppInfoMap();
        KNOWN_PROTOCOL_VERSIONS = Arrays.asList(20141218, 20141107, 20141028, 20141001, 20140701, 20140324, 20140204, 20131107, 20130618, 20130502, 20121101);
    }
    
    private static Map<String, List<NativeAppInfo>> buildActionToAppInfoMap() {
        final HashMap<String, ArrayList<MessengerAppInfo>> hashMap = (HashMap<String, ArrayList<MessengerAppInfo>>)new HashMap<String, List<MessengerAppInfo>>();
        final ArrayList<MessengerAppInfo> list = new ArrayList<MessengerAppInfo>();
        list.add(new MessengerAppInfo());
        hashMap.put("com.facebook.platform.action.request.OGACTIONPUBLISH_DIALOG", (List<MessengerAppInfo>)NativeProtocol.facebookAppInfoList);
        hashMap.put("com.facebook.platform.action.request.FEED_DIALOG", NativeProtocol.facebookAppInfoList);
        hashMap.put("com.facebook.platform.action.request.LIKE_DIALOG", NativeProtocol.facebookAppInfoList);
        hashMap.put("com.facebook.platform.action.request.MESSAGE_DIALOG", list);
        hashMap.put("com.facebook.platform.action.request.OGMESSAGEPUBLISH_DIALOG", list);
        return (Map<String, List<NativeAppInfo>>)hashMap;
    }
    
    private static List<NativeAppInfo> buildFacebookAppList() {
        final ArrayList<WakizashiAppInfo> list = new ArrayList<WakizashiAppInfo>();
        list.add(NativeProtocol.FACEBOOK_APP_INFO);
        list.add(new WakizashiAppInfo());
        return (List<NativeAppInfo>)list;
    }
    
    private static Uri buildPlatformProviderVersionURI(final NativeAppInfo nativeAppInfo) {
        return Uri.parse("content://" + nativeAppInfo.getPackage() + ".provider.PlatformProvider/versions");
    }
    
    public static int computeLatestAvailableVersionFromVersionSpec(final TreeSet<Integer> set, int min, final int[] array) {
        int n = array.length - 1;
        final Iterator<Integer> descendingIterator = set.descendingIterator();
        int n2 = -1;
        while (descendingIterator.hasNext()) {
            final int intValue = descendingIterator.next();
            final int max = Math.max(n2, intValue);
            int n3;
            for (n3 = n; n3 >= 0 && array[n3] > intValue; --n3) {}
            if (n3 < 0) {
                break;
            }
            n2 = max;
            n = n3;
            if (array[n3] == intValue) {
                if (n3 % 2 == 0) {
                    min = Math.min(max, min);
                }
                else {
                    min = -1;
                }
                return min;
            }
        }
        return -1;
    }
    
    public static Intent createPlatformActivityIntent(final Context context, final String s, final String s2, final int n, final String s3, final Bundle bundle) {
        final Intent activityIntent = findActivityIntent(context, "com.facebook.platform.PLATFORM_ACTIVITY", s2);
        if (activityIntent == null) {
            return null;
        }
        activityIntent.putExtra("com.facebook.platform.protocol.PROTOCOL_VERSION", n).putExtra("com.facebook.platform.protocol.PROTOCOL_ACTION", s2).putExtra("com.facebook.platform.extra.APPLICATION_ID", Utility.getMetadataApplicationId(context));
        if (isVersionCompatibleWithBucketedIntent(n)) {
            final Bundle bundle2 = new Bundle();
            bundle2.putString("action_id", s);
            bundle2.putString("app_name", s3);
            activityIntent.putExtra("com.facebook.platform.protocol.BRIDGE_ARGS", bundle2);
            Bundle bundle3;
            if (bundle == null) {
                bundle3 = new Bundle();
            }
            else {
                bundle3 = bundle;
            }
            activityIntent.putExtra("com.facebook.platform.protocol.METHOD_ARGS", bundle3);
            return activityIntent;
        }
        activityIntent.putExtra("com.facebook.platform.protocol.CALL_ID", s);
        activityIntent.putExtra("com.facebook.platform.extra.APPLICATION_NAME", s3);
        activityIntent.putExtras(bundle);
        return activityIntent;
    }
    
    public static Intent createPlatformServiceIntent(final Context context) {
        for (final NativeAppInfo nativeAppInfo : NativeProtocol.facebookAppInfoList) {
            final Intent validateServiceIntent = validateServiceIntent(context, new Intent("com.facebook.platform.PLATFORM_SERVICE").setPackage(nativeAppInfo.getPackage()).addCategory("android.intent.category.DEFAULT"), nativeAppInfo);
            if (validateServiceIntent != null) {
                return validateServiceIntent;
            }
        }
        return null;
    }
    
    public static Intent createProxyAuthIntent(final Context context, final String s, final List<String> list, final String s2, final boolean b, final SessionDefaultAudience sessionDefaultAudience) {
        for (final NativeAppInfo nativeAppInfo : NativeProtocol.facebookAppInfoList) {
            final Intent putExtra = new Intent().setClassName(nativeAppInfo.getPackage(), "com.facebook.katana.ProxyAuth").putExtra("client_id", s);
            if (!Utility.isNullOrEmpty((Collection<Object>)list)) {
                putExtra.putExtra("scope", TextUtils.join((CharSequence)",", (Iterable)list));
            }
            if (!Utility.isNullOrEmpty(s2)) {
                putExtra.putExtra("e2e", s2);
            }
            putExtra.putExtra("response_type", "token");
            putExtra.putExtra("return_scopes", "true");
            putExtra.putExtra("default_audience", sessionDefaultAudience.getNativeProtocolAudience());
            if (!Settings.getPlatformCompatibilityEnabled()) {
                putExtra.putExtra("legacy_override", "v2.2");
                if (b) {
                    putExtra.putExtra("auth_type", "rerequest");
                }
            }
            final Intent validateActivityIntent = validateActivityIntent(context, putExtra, nativeAppInfo);
            if (validateActivityIntent != null) {
                return validateActivityIntent;
            }
        }
        return null;
    }
    
    public static Intent createTokenRefreshIntent(final Context context) {
        for (final NativeAppInfo nativeAppInfo : NativeProtocol.facebookAppInfoList) {
            final Intent validateServiceIntent = validateServiceIntent(context, new Intent().setClassName(nativeAppInfo.getPackage(), "com.facebook.katana.platform.TokenRefreshService"), nativeAppInfo);
            if (validateServiceIntent != null) {
                return validateServiceIntent;
            }
        }
        return null;
    }
    
    private static Intent findActivityIntent(final Context context, final String action, final String s) {
        final List<NativeAppInfo> list = NativeProtocol.actionToAppInfoMap.get(s);
        Intent intent;
        if (list == null) {
            intent = null;
        }
        else {
            intent = null;
            for (final NativeAppInfo nativeAppInfo : list) {
                final Intent validateActivityIntent = validateActivityIntent(context, new Intent().setAction(action).setPackage(nativeAppInfo.getPackage()).addCategory("android.intent.category.DEFAULT"), nativeAppInfo);
                if ((intent = validateActivityIntent) != null) {
                    return validateActivityIntent;
                }
            }
        }
        return intent;
    }
    
    private static TreeSet<Integer> getAllAvailableProtocolVersionsForAppInfo(Context context, final NativeAppInfo nativeAppInfo) {
        final TreeSet<Integer> set = new TreeSet<Integer>();
        final ContentResolver contentResolver = context.getContentResolver();
        final Uri buildPlatformProviderVersionURI = buildPlatformProviderVersionURI(nativeAppInfo);
        context = null;
        try {
            final Object query = contentResolver.query(buildPlatformProviderVersionURI, new String[] { "version" }, (String)null, (String[])null, (String)null);
            if (query != null) {
                while (true) {
                    context = (Context)query;
                    if (!((Cursor)query).moveToNext()) {
                        break;
                    }
                    context = (Context)query;
                    set.add(((Cursor)query).getInt(((Cursor)query).getColumnIndex("version")));
                }
            }
        }
        finally {
            if (context != null) {
                ((Cursor)context).close();
            }
        }
        final Cursor cursor;
        if (cursor != null) {
            cursor.close();
        }
        return set;
    }
    
    public static Bundle getBridgeArgumentsFromIntent(final Intent intent) {
        if (!isVersionCompatibleWithBucketedIntent(getProtocolVersionFromIntent(intent))) {
            return null;
        }
        return intent.getBundleExtra("com.facebook.platform.protocol.BRIDGE_ARGS");
    }
    
    public static UUID getCallIdFromIntent(final Intent intent) {
        if (intent != null) {
            final int protocolVersionFromIntent = getProtocolVersionFromIntent(intent);
            final String s = null;
            Label_0051: {
                if (!isVersionCompatibleWithBucketedIntent(protocolVersionFromIntent)) {
                    break Label_0051;
                }
                final Bundle bundleExtra = intent.getBundleExtra("com.facebook.platform.protocol.BRIDGE_ARGS");
                String s2 = s;
                if (bundleExtra != null) {
                    s2 = bundleExtra.getString("action_id");
                }
                while (true) {
                    if (s2 == null) {
                        return null;
                    }
                    try {
                        return UUID.fromString(s2);
                        s2 = intent.getStringExtra("com.facebook.platform.protocol.CALL_ID");
                        continue;
                    }
                    catch (IllegalArgumentException ex) {
                        return null;
                    }
                    break;
                }
            }
        }
        return null;
    }
    
    public static Bundle getErrorDataFromResultIntent(final Intent intent) {
        if (!isErrorResult(intent)) {
            return null;
        }
        final Bundle bridgeArgumentsFromIntent = getBridgeArgumentsFromIntent(intent);
        if (bridgeArgumentsFromIntent != null) {
            return bridgeArgumentsFromIntent.getBundle("error");
        }
        return intent.getExtras();
    }
    
    public static Exception getExceptionFromErrorData(final Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        String s;
        if ((s = bundle.getString("error_type")) == null) {
            s = bundle.getString("com.facebook.platform.status.ERROR_TYPE");
        }
        String s2;
        if ((s2 = bundle.getString("error_description")) == null) {
            s2 = bundle.getString("com.facebook.platform.status.ERROR_DESCRIPTION");
        }
        if (s != null && s.equalsIgnoreCase("UserCanceled")) {
            return new FacebookOperationCanceledException(s2);
        }
        return new FacebookException(s2);
    }
    
    public static int getLatestAvailableProtocolVersionForAction(final Context context, final String s, final int[] array) {
        return getLatestAvailableProtocolVersionForAppInfoList(context, NativeProtocol.actionToAppInfoMap.get(s), array);
    }
    
    private static int getLatestAvailableProtocolVersionForAppInfo(final Context context, final NativeAppInfo nativeAppInfo, final int[] array) {
        return computeLatestAvailableVersionFromVersionSpec(getAllAvailableProtocolVersionsForAppInfo(context, nativeAppInfo), getLatestKnownVersion(), array);
    }
    
    private static int getLatestAvailableProtocolVersionForAppInfoList(final Context context, final List<NativeAppInfo> list, final int[] array) {
        if (list == null) {
            return -1;
        }
        final Iterator<NativeAppInfo> iterator = list.iterator();
        while (iterator.hasNext()) {
            final int latestAvailableProtocolVersionForAppInfo = getLatestAvailableProtocolVersionForAppInfo(context, iterator.next(), array);
            if (latestAvailableProtocolVersionForAppInfo != -1) {
                return latestAvailableProtocolVersionForAppInfo;
            }
        }
        return -1;
    }
    
    public static int getLatestAvailableProtocolVersionForService(final Context context, final int n) {
        return getLatestAvailableProtocolVersionForAppInfoList(context, NativeProtocol.facebookAppInfoList, new int[] { n });
    }
    
    public static final int getLatestKnownVersion() {
        return NativeProtocol.KNOWN_PROTOCOL_VERSIONS.get(0);
    }
    
    public static int getProtocolVersionFromIntent(final Intent intent) {
        return intent.getIntExtra("com.facebook.platform.protocol.PROTOCOL_VERSION", 0);
    }
    
    public static Bundle getSuccessResultsFromIntent(final Intent intent) {
        final int protocolVersionFromIntent = getProtocolVersionFromIntent(intent);
        final Bundle extras = intent.getExtras();
        if (!isVersionCompatibleWithBucketedIntent(protocolVersionFromIntent) || extras == null) {
            return extras;
        }
        return extras.getBundle("com.facebook.platform.protocol.RESULT_ARGS");
    }
    
    public static boolean isErrorResult(final Intent intent) {
        final Bundle bridgeArgumentsFromIntent = getBridgeArgumentsFromIntent(intent);
        if (bridgeArgumentsFromIntent != null) {
            return bridgeArgumentsFromIntent.containsKey("error");
        }
        return intent.hasExtra("com.facebook.platform.status.ERROR_TYPE");
    }
    
    public static boolean isVersionCompatibleWithBucketedIntent(final int n) {
        return NativeProtocol.KNOWN_PROTOCOL_VERSIONS.contains(n) && n >= 20140701;
    }
    
    static Intent validateActivityIntent(final Context context, Intent intent, final NativeAppInfo nativeAppInfo) {
        if (intent == null) {
            intent = null;
        }
        else {
            final ResolveInfo resolveActivity = context.getPackageManager().resolveActivity(intent, 0);
            if (resolveActivity == null) {
                return null;
            }
            if (!nativeAppInfo.validateSignature(context, resolveActivity.activityInfo.packageName)) {
                return null;
            }
        }
        return intent;
    }
    
    static Intent validateServiceIntent(final Context context, Intent intent, final NativeAppInfo nativeAppInfo) {
        if (intent == null) {
            intent = null;
        }
        else {
            final ResolveInfo resolveService = context.getPackageManager().resolveService(intent, 0);
            if (resolveService == null) {
                return null;
            }
            if (!nativeAppInfo.validateSignature(context, resolveService.serviceInfo.packageName)) {
                return null;
            }
        }
        return intent;
    }
    
    private static class KatanaAppInfo extends NativeAppInfo
    {
        static final String KATANA_PACKAGE = "com.facebook.katana";
        
        @Override
        protected String getPackage() {
            return "com.facebook.katana";
        }
    }
    
    private static class MessengerAppInfo extends NativeAppInfo
    {
        static final String MESSENGER_PACKAGE = "com.facebook.orca";
        
        @Override
        protected String getPackage() {
            return "com.facebook.orca";
        }
    }
    
    private abstract static class NativeAppInfo
    {
        private static final String FBI_HASH = "a4b7452e2ed8f5f191058ca7bbfd26b0d3214bfc";
        private static final String FBL_HASH = "5e8f16062ea3cd2c4a0d547876baa6f38cabf625";
        private static final String FBR_HASH = "8a3c4b262d721acd49a4bf97d5213199c86fa2b9";
        private static final HashSet<String> validAppSignatureHashes;
        
        static {
            validAppSignatureHashes = buildAppSignatureHashes();
        }
        
        private static HashSet<String> buildAppSignatureHashes() {
            final HashSet<String> set = new HashSet<String>();
            set.add("8a3c4b262d721acd49a4bf97d5213199c86fa2b9");
            set.add("a4b7452e2ed8f5f191058ca7bbfd26b0d3214bfc");
            set.add("5e8f16062ea3cd2c4a0d547876baa6f38cabf625");
            return set;
        }
        
        protected abstract String getPackage();
        
        public boolean validateSignature(final Context context, String sha1hash) {
            final String brand = Build.BRAND;
            final int flags = context.getApplicationInfo().flags;
            if (!brand.startsWith("generic") || (flags & 0x2) == 0x0) {
                try {
                    final Signature[] signatures = context.getPackageManager().getPackageInfo(sha1hash, 64).signatures;
                    for (int length = signatures.length, i = 0; i < length; ++i) {
                        sha1hash = Utility.sha1hash(signatures[i].toByteArray());
                        if (NativeAppInfo.validAppSignatureHashes.contains(sha1hash)) {
                            return true;
                        }
                    }
                }
                catch (PackageManager$NameNotFoundException ex) {
                    return false;
                }
                return false;
            }
            return true;
        }
    }
    
    private static class WakizashiAppInfo extends NativeAppInfo
    {
        static final String WAKIZASHI_PACKAGE = "com.facebook.wakizashi";
        
        @Override
        protected String getPackage() {
            return "com.facebook.wakizashi";
        }
    }
}
