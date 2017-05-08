// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook;

import android.graphics.Bitmap$CompressFormat;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import com.facebook.model.GraphMultiResult;
import java.net.URLEncoder;
import android.util.Pair;
import org.json.JSONException;
import java.util.HashMap;
import java.io.OutputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import com.facebook.model.GraphObjectList;
import java.util.Map;
import java.text.SimpleDateFormat;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.ArrayList;
import com.facebook.model.OpenGraphObject;
import com.facebook.model.OpenGraphAction;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.facebook.internal.AttributionIdentifiers;
import android.content.Context;
import java.util.Date;
import android.os.ParcelFileDescriptor;
import java.util.regex.Matcher;
import java.io.FileNotFoundException;
import java.io.File;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Handler;
import java.util.HashSet;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collection;
import com.facebook.internal.Validate;
import java.io.IOException;
import java.util.Locale;
import java.net.HttpURLConnection;
import java.util.Iterator;
import android.net.Uri$Builder;
import android.util.Log;
import com.facebook.internal.Utility;
import com.facebook.internal.Logger;
import java.util.List;
import java.net.URL;
import com.facebook.internal.ServerProtocol;
import android.os.Bundle;
import com.facebook.model.GraphObject;
import java.util.regex.Pattern;

public class Request
{
    private static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language";
    private static final String ACCESS_TOKEN_PARAM = "access_token";
    private static final String ATTACHED_FILES_PARAM = "attached_files";
    private static final String ATTACHMENT_FILENAME_PREFIX = "file";
    private static final String BATCH_APP_ID_PARAM = "batch_app_id";
    private static final String BATCH_BODY_PARAM = "body";
    private static final String BATCH_ENTRY_DEPENDS_ON_PARAM = "depends_on";
    private static final String BATCH_ENTRY_NAME_PARAM = "name";
    private static final String BATCH_ENTRY_OMIT_RESPONSE_ON_SUCCESS_PARAM = "omit_response_on_success";
    private static final String BATCH_METHOD_PARAM = "method";
    private static final String BATCH_PARAM = "batch";
    private static final String BATCH_RELATIVE_URL_PARAM = "relative_url";
    private static final String CONTENT_ENCODING_HEADER = "Content-Encoding";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String FORMAT_JSON = "json";
    private static final String FORMAT_PARAM = "format";
    private static final String ISO_8601_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final int MAXIMUM_BATCH_SIZE = 50;
    private static final String ME = "me";
    private static final String MIME_BOUNDARY = "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f";
    private static final String MY_ACTION_FORMAT = "me/%s";
    private static final String MY_FEED = "me/feed";
    private static final String MY_FRIENDS = "me/friends";
    private static final String MY_OBJECTS_FORMAT = "me/objects/%s";
    private static final String MY_PHOTOS = "me/photos";
    private static final String MY_STAGING_RESOURCES = "me/staging_resources";
    private static final String MY_VIDEOS = "me/videos";
    private static final String OBJECT_PARAM = "object";
    private static final String PICTURE_PARAM = "picture";
    private static final String SDK_ANDROID = "android";
    private static final String SDK_PARAM = "sdk";
    private static final String SEARCH = "search";
    private static final String STAGING_PARAM = "file";
    public static final String TAG;
    private static final String USER_AGENT_BASE = "FBAndroidSDK";
    private static final String USER_AGENT_HEADER = "User-Agent";
    private static final String VIDEOS_SUFFIX = "/videos";
    private static String defaultBatchApplicationId;
    private static volatile String userAgent;
    private static Pattern versionPattern;
    private String batchEntryDependsOn;
    private String batchEntryName;
    private boolean batchEntryOmitResultOnSuccess;
    private Callback callback;
    private GraphObject graphObject;
    private String graphPath;
    private HttpMethod httpMethod;
    private String overriddenURL;
    private Bundle parameters;
    private Session session;
    private boolean skipClientToken;
    private Object tag;
    private String version;
    
    static {
        TAG = Request.class.getSimpleName();
        Request.versionPattern = Pattern.compile("^/?v\\d+\\.\\d+/(.*)");
    }
    
    public Request() {
        this(null, null, null, null, null);
    }
    
    public Request(final Session session, final String s) {
        this(session, s, null, null, null);
    }
    
    public Request(final Session session, final String s, final Bundle bundle, final HttpMethod httpMethod) {
        this(session, s, bundle, httpMethod, null);
    }
    
    public Request(final Session session, final String s, final Bundle bundle, final HttpMethod httpMethod, final Callback callback) {
        this(session, s, bundle, httpMethod, callback, null);
    }
    
    public Request(final Session session, final String graphPath, final Bundle bundle, final HttpMethod httpMethod, final Callback callback, final String version) {
        this.batchEntryOmitResultOnSuccess = true;
        this.skipClientToken = false;
        this.session = session;
        this.graphPath = graphPath;
        this.callback = callback;
        this.version = version;
        this.setHttpMethod(httpMethod);
        if (bundle != null) {
            this.parameters = new Bundle(bundle);
        }
        else {
            this.parameters = new Bundle();
        }
        if (this.version == null) {
            this.version = ServerProtocol.getAPIVersion();
        }
    }
    
    Request(final Session session, final URL url) {
        this.batchEntryOmitResultOnSuccess = true;
        this.skipClientToken = false;
        this.session = session;
        this.overriddenURL = url.toString();
        this.setHttpMethod(HttpMethod.GET);
        this.parameters = new Bundle();
    }
    
    private void addCommonParameters() {
        if (this.session != null) {
            if (!this.session.isOpened()) {
                throw new FacebookException("Session provided to a Request in un-opened state.");
            }
            if (!this.parameters.containsKey("access_token")) {
                final String accessToken = this.session.getAccessToken();
                Logger.registerAccessToken(accessToken);
                this.parameters.putString("access_token", accessToken);
            }
        }
        else if (!this.skipClientToken && !this.parameters.containsKey("access_token")) {
            final String applicationId = Settings.getApplicationId();
            final String clientToken = Settings.getClientToken();
            if (!Utility.isNullOrEmpty(applicationId) && !Utility.isNullOrEmpty(clientToken)) {
                this.parameters.putString("access_token", applicationId + "|" + clientToken);
            }
            else {
                Log.d(Request.TAG, "Warning: Sessionless Request needs token but missing either application ID or client token.");
            }
        }
        this.parameters.putString("sdk", "android");
        this.parameters.putString("format", "json");
    }
    
    private String appendParametersToBaseUrl(String value) {
        final Uri$Builder encodedPath = new Uri$Builder().encodedPath(value);
        for (final String s : this.parameters.keySet()) {
            if ((value = (String)this.parameters.get(s)) == null) {
                value = "";
            }
            if (isSupportedParameterType(value)) {
                encodedPath.appendQueryParameter(s, parameterToString(value).toString());
            }
            else {
                if (this.httpMethod == HttpMethod.GET) {
                    throw new IllegalArgumentException(String.format("Unsupported parameter type for GET request: %s", value.getClass().getSimpleName()));
                }
                continue;
            }
        }
        return encodedPath.toString();
    }
    
    private static HttpURLConnection createConnection(final URL url) throws IOException {
        final HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        httpURLConnection.setRequestProperty("User-Agent", getUserAgent());
        httpURLConnection.setRequestProperty("Accept-Language", Locale.getDefault().toString());
        httpURLConnection.setChunkedStreamingMode(0);
        return httpURLConnection;
    }
    
    public static Response executeAndWait(final Request request) {
        final List<Response> executeBatchAndWait = executeBatchAndWait(request);
        if (executeBatchAndWait == null || executeBatchAndWait.size() != 1) {
            throw new FacebookException("invalid state: expected a single response");
        }
        return executeBatchAndWait.get(0);
    }
    
    public static List<Response> executeBatchAndWait(final RequestBatch requestBatch) {
        Validate.notEmptyAndContainsNoNulls((Collection<Object>)requestBatch, "requests");
        try {
            return executeConnectionAndWait(toHttpConnection(requestBatch), requestBatch);
        }
        catch (Exception ex) {
            final List<Response> constructErrorResponses = Response.constructErrorResponses(requestBatch.getRequests(), null, new FacebookException(ex));
            runCallbacks(requestBatch, constructErrorResponses);
            return constructErrorResponses;
        }
    }
    
    public static List<Response> executeBatchAndWait(final Collection<Request> collection) {
        return executeBatchAndWait(new RequestBatch(collection));
    }
    
    public static List<Response> executeBatchAndWait(final Request... array) {
        Validate.notNull(array, "requests");
        return executeBatchAndWait(Arrays.asList(array));
    }
    
    public static RequestAsyncTask executeBatchAsync(final RequestBatch requestBatch) {
        Validate.notEmptyAndContainsNoNulls((Collection<Object>)requestBatch, "requests");
        final RequestAsyncTask requestAsyncTask = new RequestAsyncTask(requestBatch);
        requestAsyncTask.executeOnSettingsExecutor();
        return requestAsyncTask;
    }
    
    public static RequestAsyncTask executeBatchAsync(final Collection<Request> collection) {
        return executeBatchAsync(new RequestBatch(collection));
    }
    
    public static RequestAsyncTask executeBatchAsync(final Request... array) {
        Validate.notNull(array, "requests");
        return executeBatchAsync(Arrays.asList(array));
    }
    
    public static List<Response> executeConnectionAndWait(final HttpURLConnection httpURLConnection, final RequestBatch requestBatch) {
        final List<Response> fromHttpConnection = Response.fromHttpConnection(httpURLConnection, requestBatch);
        Utility.disconnectQuietly(httpURLConnection);
        final int size = requestBatch.size();
        if (size != fromHttpConnection.size()) {
            throw new FacebookException(String.format("Received %d responses while expecting %d", fromHttpConnection.size(), size));
        }
        runCallbacks(requestBatch, fromHttpConnection);
        final HashSet<Session> set = new HashSet<Session>();
        for (final Request request : requestBatch) {
            if (request.session != null) {
                set.add(request.session);
            }
        }
        final Iterator<Session> iterator2 = set.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().extendAccessTokenIfNeeded();
        }
        return fromHttpConnection;
    }
    
    public static List<Response> executeConnectionAndWait(final HttpURLConnection httpURLConnection, final Collection<Request> collection) {
        return executeConnectionAndWait(httpURLConnection, new RequestBatch(collection));
    }
    
    public static RequestAsyncTask executeConnectionAsync(final Handler callbackHandler, final HttpURLConnection httpURLConnection, final RequestBatch requestBatch) {
        Validate.notNull(httpURLConnection, "connection");
        final RequestAsyncTask requestAsyncTask = new RequestAsyncTask(httpURLConnection, requestBatch);
        requestBatch.setCallbackHandler(callbackHandler);
        requestAsyncTask.executeOnSettingsExecutor();
        return requestAsyncTask;
    }
    
    public static RequestAsyncTask executeConnectionAsync(final HttpURLConnection httpURLConnection, final RequestBatch requestBatch) {
        return executeConnectionAsync(null, httpURLConnection, requestBatch);
    }
    
    @Deprecated
    public static RequestAsyncTask executeGraphPathRequestAsync(final Session session, final String s, final Callback callback) {
        return newGraphPathRequest(session, s, callback).executeAsync();
    }
    
    @Deprecated
    public static RequestAsyncTask executeMeRequestAsync(final Session session, final GraphUserCallback graphUserCallback) {
        return newMeRequest(session, graphUserCallback).executeAsync();
    }
    
    @Deprecated
    public static RequestAsyncTask executeMyFriendsRequestAsync(final Session session, final GraphUserListCallback graphUserListCallback) {
        return newMyFriendsRequest(session, graphUserListCallback).executeAsync();
    }
    
    @Deprecated
    public static RequestAsyncTask executePlacesSearchRequestAsync(final Session session, final Location location, final int n, final int n2, final String s, final GraphPlaceListCallback graphPlaceListCallback) {
        return newPlacesSearchRequest(session, location, n, n2, s, graphPlaceListCallback).executeAsync();
    }
    
    @Deprecated
    public static RequestAsyncTask executePostRequestAsync(final Session session, final String s, final GraphObject graphObject, final Callback callback) {
        return newPostRequest(session, s, graphObject, callback).executeAsync();
    }
    
    @Deprecated
    public static RequestAsyncTask executeStatusUpdateRequestAsync(final Session session, final String s, final Callback callback) {
        return newStatusUpdateRequest(session, s, callback).executeAsync();
    }
    
    @Deprecated
    public static RequestAsyncTask executeUploadPhotoRequestAsync(final Session session, final Bitmap bitmap, final Callback callback) {
        return newUploadPhotoRequest(session, bitmap, callback).executeAsync();
    }
    
    @Deprecated
    public static RequestAsyncTask executeUploadPhotoRequestAsync(final Session session, final File file, final Callback callback) throws FileNotFoundException {
        return newUploadPhotoRequest(session, file, callback).executeAsync();
    }
    
    private static String getBatchAppId(final RequestBatch requestBatch) {
        if (!Utility.isNullOrEmpty(requestBatch.getBatchApplicationId())) {
            return requestBatch.getBatchApplicationId();
        }
        final Iterator<Request> iterator = requestBatch.iterator();
        while (iterator.hasNext()) {
            final Session session = iterator.next().session;
            if (session != null) {
                return session.getApplicationId();
            }
        }
        return Request.defaultBatchApplicationId;
    }
    
    public static final String getDefaultBatchApplicationId() {
        return Request.defaultBatchApplicationId;
    }
    
    private String getGraphPathWithVersion() {
        if (Request.versionPattern.matcher(this.graphPath).matches()) {
            return this.graphPath;
        }
        return String.format("%s/%s", this.version, this.graphPath);
    }
    
    private static String getMimeContentType() {
        return String.format("multipart/form-data; boundary=%s", "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f");
    }
    
    private static String getUserAgent() {
        if (Request.userAgent == null) {
            Request.userAgent = String.format("%s.%s", "FBAndroidSDK", "3.23.0");
        }
        return Request.userAgent;
    }
    
    private static boolean hasOnProgressCallbacks(final RequestBatch requestBatch) {
        final Iterator<RequestBatch.Callback> iterator = requestBatch.getCallbacks().iterator();
        while (iterator.hasNext()) {
            if (((RequestBatch.Callback)iterator.next()) instanceof RequestBatch.OnProgressCallback) {
                return true;
            }
        }
        final Iterator<Request> iterator2 = requestBatch.iterator();
        while (iterator2.hasNext()) {
            if (iterator2.next().getCallback() instanceof OnProgressCallback) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean isGzipCompressible(final RequestBatch requestBatch) {
        for (final Request request : requestBatch) {
            final Iterator iterator2 = request.parameters.keySet().iterator();
            while (iterator2.hasNext()) {
                if (isSupportedAttachmentType(request.parameters.get((String)iterator2.next()))) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private static boolean isMeRequest(String group) {
        final Matcher matcher = Request.versionPattern.matcher(group);
        if (matcher.matches()) {
            group = matcher.group(1);
        }
        return group.startsWith("me/") || group.startsWith("/me/");
    }
    
    private static boolean isSupportedAttachmentType(final Object o) {
        return o instanceof Bitmap || o instanceof byte[] || o instanceof ParcelFileDescriptor || o instanceof ParcelFileDescriptorWithMimeType;
    }
    
    private static boolean isSupportedParameterType(final Object o) {
        return o instanceof String || o instanceof Boolean || o instanceof Number || o instanceof Date;
    }
    
    public static Request newCustomAudienceThirdPartyIdRequest(final Session session, final Context context, final Callback callback) {
        return newCustomAudienceThirdPartyIdRequest(session, context, null, callback);
    }
    
    public static Request newCustomAudienceThirdPartyIdRequest(final Session session, final Context context, String string, final Callback callback) {
        Session activeSession = session;
        if (session == null) {
            activeSession = Session.getActiveSession();
        }
        Session session2;
        if ((session2 = activeSession) != null) {
            session2 = activeSession;
            if (!activeSession.isOpened()) {
                session2 = null;
            }
        }
        String s;
        if ((s = string) == null) {
            if (session2 != null) {
                s = session2.getApplicationId();
            }
            else {
                s = Utility.getMetadataApplicationId(context);
            }
        }
        if (s == null) {
            throw new FacebookException("Facebook App ID cannot be determined");
        }
        string = s + "/custom_audience_third_party_id";
        final AttributionIdentifiers attributionIdentifiers = AttributionIdentifiers.getAttributionIdentifiers(context);
        final Bundle bundle = new Bundle();
        if (session2 == null) {
            String s2;
            if (attributionIdentifiers.getAttributionId() != null) {
                s2 = attributionIdentifiers.getAttributionId();
            }
            else {
                s2 = attributionIdentifiers.getAndroidAdvertiserId();
            }
            if (attributionIdentifiers.getAttributionId() != null) {
                bundle.putString("udid", s2);
            }
        }
        if (Settings.getLimitEventAndDataUsage(context) || attributionIdentifiers.isTrackingLimited()) {
            bundle.putString("limit_event_usage", "1");
        }
        return new Request(session2, string, bundle, HttpMethod.GET, callback);
    }
    
    public static Request newDeleteObjectRequest(final Session session, final String s, final Callback callback) {
        return new Request(session, s, null, HttpMethod.DELETE, callback);
    }
    
    public static Request newGraphPathRequest(final Session session, final String s, final Callback callback) {
        return new Request(session, s, null, null, callback);
    }
    
    public static Request newMeRequest(final Session session, final GraphUserCallback graphUserCallback) {
        return new Request(session, "me", null, null, (Callback)new Callback() {
            @Override
            public void onCompleted(final Response response) {
                if (graphUserCallback != null) {
                    graphUserCallback.onCompleted(response.getGraphObjectAs(GraphUser.class), response);
                }
            }
        });
    }
    
    public static Request newMyFriendsRequest(final Session session, final GraphUserListCallback graphUserListCallback) {
        return new Request(session, "me/friends", null, null, (Callback)new Callback() {
            @Override
            public void onCompleted(final Response response) {
                if (graphUserListCallback != null) {
                    graphUserListCallback.onCompleted(typedListFromResponse(response, (Class<GraphObject>)GraphUser.class), response);
                }
            }
        });
    }
    
    public static Request newPlacesSearchRequest(final Session session, final Location location, final int n, final int n2, final String s, final GraphPlaceListCallback graphPlaceListCallback) {
        if (location == null && Utility.isNullOrEmpty(s)) {
            throw new FacebookException("Either location or searchText must be specified.");
        }
        final Bundle bundle = new Bundle(5);
        bundle.putString("type", "place");
        bundle.putInt("limit", n2);
        if (location != null) {
            bundle.putString("center", String.format(Locale.US, "%f,%f", location.getLatitude(), location.getLongitude()));
            bundle.putInt("distance", n);
        }
        if (!Utility.isNullOrEmpty(s)) {
            bundle.putString("q", s);
        }
        return new Request(session, "search", bundle, HttpMethod.GET, (Callback)new Callback() {
            @Override
            public void onCompleted(final Response response) {
                if (graphPlaceListCallback != null) {
                    graphPlaceListCallback.onCompleted(typedListFromResponse(response, (Class<GraphObject>)GraphPlace.class), response);
                }
            }
        });
    }
    
    public static Request newPostOpenGraphActionRequest(final Session session, final OpenGraphAction openGraphAction, final Callback callback) {
        if (openGraphAction == null) {
            throw new FacebookException("openGraphAction cannot be null");
        }
        if (Utility.isNullOrEmpty(openGraphAction.getType())) {
            throw new FacebookException("openGraphAction must have non-null 'type' property");
        }
        return newPostRequest(session, String.format("me/%s", openGraphAction.getType()), openGraphAction, callback);
    }
    
    public static Request newPostOpenGraphObjectRequest(final Session session, final OpenGraphObject openGraphObject, final Callback callback) {
        if (openGraphObject == null) {
            throw new FacebookException("openGraphObject cannot be null");
        }
        if (Utility.isNullOrEmpty(openGraphObject.getType())) {
            throw new FacebookException("openGraphObject must have non-null 'type' property");
        }
        if (Utility.isNullOrEmpty(openGraphObject.getTitle())) {
            throw new FacebookException("openGraphObject must have non-null 'title' property");
        }
        final String format = String.format("me/objects/%s", openGraphObject.getType());
        final Bundle bundle = new Bundle();
        bundle.putString("object", openGraphObject.getInnerJSONObject().toString());
        return new Request(session, format, bundle, HttpMethod.POST, callback);
    }
    
    public static Request newPostOpenGraphObjectRequest(final Session session, final String s, final String s2, final String s3, final String s4, final String s5, final GraphObject data, final Callback callback) {
        final OpenGraphObject forPost = OpenGraphObject.Factory.createForPost(OpenGraphObject.class, s, s2, s3, s4, s5);
        if (data != null) {
            forPost.setData(data);
        }
        return newPostOpenGraphObjectRequest(session, forPost, callback);
    }
    
    public static Request newPostRequest(final Session session, final String s, final GraphObject graphObject, final Callback callback) {
        final Request request = new Request(session, s, null, HttpMethod.POST, callback);
        request.setGraphObject(graphObject);
        return request;
    }
    
    public static Request newStatusUpdateRequest(final Session session, final String s, final Callback callback) {
        return newStatusUpdateRequest(session, s, null, (List<String>)null, callback);
    }
    
    public static Request newStatusUpdateRequest(final Session session, final String s, final GraphPlace graphPlace, final List<GraphUser> list, final Callback callback) {
        List<String> list2 = null;
        if (list != null) {
            final ArrayList<String> list3 = new ArrayList<String>(list.size());
            final Iterator<GraphUser> iterator = list.iterator();
            while (true) {
                list2 = list3;
                if (!iterator.hasNext()) {
                    break;
                }
                list3.add(iterator.next().getId());
            }
        }
        String id;
        if (graphPlace == null) {
            id = null;
        }
        else {
            id = graphPlace.getId();
        }
        return newStatusUpdateRequest(session, s, id, list2, callback);
    }
    
    private static Request newStatusUpdateRequest(final Session session, final String s, final String s2, final List<String> list, final Callback callback) {
        final Bundle bundle = new Bundle();
        bundle.putString("message", s);
        if (s2 != null) {
            bundle.putString("place", s2);
        }
        if (list != null && list.size() > 0) {
            bundle.putString("tags", TextUtils.join((CharSequence)",", (Iterable)list));
        }
        return new Request(session, "me/feed", bundle, HttpMethod.POST, callback);
    }
    
    public static Request newUpdateOpenGraphObjectRequest(final Session session, final OpenGraphObject openGraphObject, final Callback callback) {
        if (openGraphObject == null) {
            throw new FacebookException("openGraphObject cannot be null");
        }
        final String id = openGraphObject.getId();
        if (id == null) {
            throw new FacebookException("openGraphObject must have an id");
        }
        final Bundle bundle = new Bundle();
        bundle.putString("object", openGraphObject.getInnerJSONObject().toString());
        return new Request(session, id, bundle, HttpMethod.POST, callback);
    }
    
    public static Request newUpdateOpenGraphObjectRequest(final Session session, final String id, final String s, final String s2, final String s3, final String s4, final GraphObject data, final Callback callback) {
        final OpenGraphObject forPost = OpenGraphObject.Factory.createForPost(OpenGraphObject.class, null, s, s2, s3, s4);
        forPost.setId(id);
        forPost.setData(data);
        return newUpdateOpenGraphObjectRequest(session, forPost, callback);
    }
    
    public static Request newUploadPhotoRequest(final Session session, final Bitmap bitmap, final Callback callback) {
        final Bundle bundle = new Bundle(1);
        bundle.putParcelable("picture", (Parcelable)bitmap);
        return new Request(session, "me/photos", bundle, HttpMethod.POST, callback);
    }
    
    public static Request newUploadPhotoRequest(final Session session, final File file, final Callback callback) throws FileNotFoundException {
        final ParcelFileDescriptor open = ParcelFileDescriptor.open(file, 268435456);
        final Bundle bundle = new Bundle(1);
        bundle.putParcelable("picture", (Parcelable)open);
        return new Request(session, "me/photos", bundle, HttpMethod.POST, callback);
    }
    
    public static Request newUploadStagingResourceWithImageRequest(final Session session, final Bitmap bitmap, final Callback callback) {
        final Bundle bundle = new Bundle(1);
        bundle.putParcelable("file", (Parcelable)bitmap);
        return new Request(session, "me/staging_resources", bundle, HttpMethod.POST, callback);
    }
    
    public static Request newUploadStagingResourceWithImageRequest(final Session session, final File file, final Callback callback) throws FileNotFoundException {
        final ParcelFileDescriptorWithMimeType parcelFileDescriptorWithMimeType = new ParcelFileDescriptorWithMimeType(ParcelFileDescriptor.open(file, 268435456), "image/png");
        final Bundle bundle = new Bundle(1);
        bundle.putParcelable("file", (Parcelable)parcelFileDescriptorWithMimeType);
        return new Request(session, "me/staging_resources", bundle, HttpMethod.POST, callback);
    }
    
    public static Request newUploadVideoRequest(final Session session, final File file, final Callback callback) throws FileNotFoundException {
        final ParcelFileDescriptor open = ParcelFileDescriptor.open(file, 268435456);
        final Bundle bundle = new Bundle(1);
        bundle.putParcelable(file.getName(), (Parcelable)open);
        return new Request(session, "me/videos", bundle, HttpMethod.POST, callback);
    }
    
    private static String parameterToString(final Object o) {
        if (o instanceof String) {
            return (String)o;
        }
        if (o instanceof Boolean || o instanceof Number) {
            return o.toString();
        }
        if (o instanceof Date) {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US).format(o);
        }
        throw new IllegalArgumentException("Unsupported parameter type.");
    }
    
    private static void processGraphObject(final GraphObject graphObject, final String s, final KeyValueSerializer keyValueSerializer) throws IOException {
        boolean b = false;
        if (isMeRequest(s)) {
            final int index = s.indexOf(":");
            final int index2 = s.indexOf("?");
            if (index > 3 && (index2 == -1 || index < index2)) {
                b = true;
            }
            else {
                b = false;
            }
        }
        for (final Map.Entry<String, Object> entry : graphObject.asMap().entrySet()) {
            processGraphObjectProperty(entry.getKey(), entry.getValue(), keyValueSerializer, b && entry.getKey().equalsIgnoreCase("image"));
        }
    }
    
    private static void processGraphObjectProperty(final String s, final Object o, final KeyValueSerializer keyValueSerializer, final boolean b) throws IOException {
        final Class<?> class1 = o.getClass();
        Object o2;
        Class<?> clazz;
        if (GraphObject.class.isAssignableFrom(class1)) {
            o2 = ((GraphObject)o).getInnerJSONObject();
            clazz = ((JSONObject)o2).getClass();
        }
        else {
            clazz = class1;
            o2 = o;
            if (GraphObjectList.class.isAssignableFrom(class1)) {
                o2 = ((GraphObjectList)o).getInnerJSONArray();
                clazz = ((JSONObject)o2).getClass();
            }
        }
        if (JSONObject.class.isAssignableFrom(clazz)) {
            final JSONObject jsonObject = (JSONObject)o2;
            if (b) {
                final Iterator keys = jsonObject.keys();
                while (keys.hasNext()) {
                    final String s2 = keys.next();
                    processGraphObjectProperty(String.format("%s[%s]", s, s2), jsonObject.opt(s2), keyValueSerializer, b);
                }
            }
            else if (jsonObject.has("id")) {
                processGraphObjectProperty(s, jsonObject.optString("id"), keyValueSerializer, b);
            }
            else {
                if (jsonObject.has("url")) {
                    processGraphObjectProperty(s, jsonObject.optString("url"), keyValueSerializer, b);
                    return;
                }
                if (jsonObject.has("fbsdk:create_object")) {
                    processGraphObjectProperty(s, jsonObject.toString(), keyValueSerializer, b);
                }
            }
        }
        else if (JSONArray.class.isAssignableFrom(clazz)) {
            final JSONArray jsonArray = (JSONArray)o2;
            for (int length = jsonArray.length(), i = 0; i < length; ++i) {
                processGraphObjectProperty(String.format("%s[%d]", s, i), jsonArray.opt(i), keyValueSerializer, b);
            }
        }
        else {
            if (String.class.isAssignableFrom(clazz) || Number.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz)) {
                keyValueSerializer.writeString(s, o2.toString());
                return;
            }
            if (Date.class.isAssignableFrom(clazz)) {
                keyValueSerializer.writeString(s, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US).format((Date)o2));
            }
        }
    }
    
    private static void processRequest(final RequestBatch requestBatch, final Logger logger, final int n, final URL url, final OutputStream outputStream, final boolean b) throws IOException, JSONException {
        final Serializer serializer = new Serializer(outputStream, logger, b);
        if (n == 1) {
            final Request value = requestBatch.get(0);
            final HashMap<String, Attachment> hashMap = new HashMap<String, Attachment>();
            for (final String s : value.parameters.keySet()) {
                final Object value2 = value.parameters.get(s);
                if (isSupportedAttachmentType(value2)) {
                    hashMap.put(s, new Attachment(value, value2));
                }
            }
            if (logger != null) {
                logger.append("  Parameters:\n");
            }
            serializeParameters(value.parameters, serializer, value);
            if (logger != null) {
                logger.append("  Attachments:\n");
            }
            serializeAttachments(hashMap, serializer);
            if (value.graphObject != null) {
                processGraphObject(value.graphObject, url.getPath(), (KeyValueSerializer)serializer);
            }
            return;
        }
        final String batchAppId = getBatchAppId(requestBatch);
        if (Utility.isNullOrEmpty(batchAppId)) {
            throw new FacebookException("At least one request in a batch must have an open Session, or a default app ID must be specified.");
        }
        serializer.writeString("batch_app_id", batchAppId);
        final HashMap<String, Attachment> hashMap2 = new HashMap<String, Attachment>();
        serializeRequestsAsJSON(serializer, requestBatch, hashMap2);
        if (logger != null) {
            logger.append("  Attachments:\n");
        }
        serializeAttachments(hashMap2, serializer);
    }
    
    static void runCallbacks(final RequestBatch requestBatch, final List<Response> list) {
        final int size = requestBatch.size();
        final ArrayList<Pair> list2 = new ArrayList<Pair>();
        for (int i = 0; i < size; ++i) {
            final Request value = requestBatch.get(i);
            if (value.callback != null) {
                list2.add(new Pair((Object)value.callback, (Object)list.get(i)));
            }
        }
        if (list2.size() > 0) {
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    for (final Pair pair : list2) {
                        ((Callback)pair.first).onCompleted((Response)pair.second);
                    }
                    final Iterator<RequestBatch.Callback> iterator2 = requestBatch.getCallbacks().iterator();
                    while (iterator2.hasNext()) {
                        ((RequestBatch.Callback)iterator2.next()).onBatchCompleted(requestBatch);
                    }
                }
            };
            final Handler callbackHandler = requestBatch.getCallbackHandler();
            if (callbackHandler != null) {
                callbackHandler.post((Runnable)runnable);
                return;
            }
            runnable.run();
        }
    }
    
    private static void serializeAttachments(final Map<String, Attachment> map, final Serializer serializer) throws IOException {
        for (final String s : map.keySet()) {
            final Attachment attachment = map.get(s);
            if (isSupportedAttachmentType(attachment.getValue())) {
                serializer.writeObject(s, attachment.getValue(), attachment.getRequest());
            }
        }
    }
    
    private static void serializeParameters(final Bundle bundle, final Serializer serializer, final Request request) throws IOException {
        for (final String s : bundle.keySet()) {
            final Object value = bundle.get(s);
            if (isSupportedParameterType(value)) {
                serializer.writeObject(s, value, request);
            }
        }
    }
    
    private static void serializeRequestsAsJSON(final Serializer serializer, final Collection<Request> collection, final Map<String, Attachment> map) throws JSONException, IOException {
        final JSONArray jsonArray = new JSONArray();
        final Iterator<Request> iterator = collection.iterator();
        while (iterator.hasNext()) {
            iterator.next().serializeToBatch(jsonArray, map);
        }
        serializer.writeRequestsAsJson("batch", jsonArray, collection);
    }
    
    private void serializeToBatch(final JSONArray jsonArray, final Map<String, Attachment> map) throws JSONException, IOException {
        final JSONObject jsonObject = new JSONObject();
        if (this.batchEntryName != null) {
            jsonObject.put("name", (Object)this.batchEntryName);
            jsonObject.put("omit_response_on_success", this.batchEntryOmitResultOnSuccess);
        }
        if (this.batchEntryDependsOn != null) {
            jsonObject.put("depends_on", (Object)this.batchEntryDependsOn);
        }
        final String urlForBatchedRequest = this.getUrlForBatchedRequest();
        jsonObject.put("relative_url", (Object)urlForBatchedRequest);
        jsonObject.put("method", (Object)this.httpMethod);
        if (this.session != null) {
            Logger.registerAccessToken(this.session.getAccessToken());
        }
        final ArrayList<String> list = new ArrayList<String>();
        final Iterator<String> iterator = this.parameters.keySet().iterator();
        while (iterator.hasNext()) {
            final Object value = this.parameters.get((String)iterator.next());
            if (isSupportedAttachmentType(value)) {
                final String format = String.format("%s%d", "file", map.size());
                list.add(format);
                map.put(format, new Attachment(this, value));
            }
        }
        if (!list.isEmpty()) {
            jsonObject.put("attached_files", (Object)TextUtils.join((CharSequence)",", (Iterable)list));
        }
        if (this.graphObject != null) {
            final ArrayList list2 = new ArrayList();
            processGraphObject(this.graphObject, urlForBatchedRequest, (KeyValueSerializer)new KeyValueSerializer() {
                @Override
                public void writeString(final String s, final String s2) throws IOException {
                    list2.add(String.format("%s=%s", s, URLEncoder.encode(s2, "UTF-8")));
                }
            });
            jsonObject.put("body", (Object)TextUtils.join((CharSequence)"&", (Iterable)list2));
        }
        jsonArray.put((Object)jsonObject);
    }
    
    static final void serializeToUrlConnection(final RequestBatch p0, final HttpURLConnection p1) throws IOException, JSONException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: new             Lcom/facebook/internal/Logger;
        //     3: dup            
        //     4: getstatic       com/facebook/LoggingBehavior.REQUESTS:Lcom/facebook/LoggingBehavior;
        //     7: ldc_w           "Request"
        //    10: invokespecial   com/facebook/internal/Logger.<init>:(Lcom/facebook/LoggingBehavior;Ljava/lang/String;)V
        //    13: astore          7
        //    15: aload_0        
        //    16: invokevirtual   com/facebook/RequestBatch.size:()I
        //    19: istore_3       
        //    20: aload_0        
        //    21: invokestatic    com/facebook/Request.isGzipCompressible:(Lcom/facebook/RequestBatch;)Z
        //    24: istore          4
        //    26: iload_3        
        //    27: iconst_1       
        //    28: if_icmpne       166
        //    31: aload_0        
        //    32: iconst_0       
        //    33: invokevirtual   com/facebook/RequestBatch.get:(I)Lcom/facebook/Request;
        //    36: getfield        com/facebook/Request.httpMethod:Lcom/facebook/HttpMethod;
        //    39: astore          5
        //    41: aload_1        
        //    42: aload           5
        //    44: invokevirtual   com/facebook/HttpMethod.name:()Ljava/lang/String;
        //    47: invokevirtual   java/net/HttpURLConnection.setRequestMethod:(Ljava/lang/String;)V
        //    50: aload_1        
        //    51: iload           4
        //    53: invokestatic    com/facebook/Request.setConnectionContentType:(Ljava/net/HttpURLConnection;Z)V
        //    56: aload_1        
        //    57: invokevirtual   java/net/HttpURLConnection.getURL:()Ljava/net/URL;
        //    60: astore          8
        //    62: aload           7
        //    64: ldc_w           "Request:\n"
        //    67: invokevirtual   com/facebook/internal/Logger.append:(Ljava/lang/String;)V
        //    70: aload           7
        //    72: ldc_w           "Id"
        //    75: aload_0        
        //    76: invokevirtual   com/facebook/RequestBatch.getId:()Ljava/lang/String;
        //    79: invokevirtual   com/facebook/internal/Logger.appendKeyValue:(Ljava/lang/String;Ljava/lang/Object;)V
        //    82: aload           7
        //    84: ldc_w           "URL"
        //    87: aload           8
        //    89: invokevirtual   com/facebook/internal/Logger.appendKeyValue:(Ljava/lang/String;Ljava/lang/Object;)V
        //    92: aload           7
        //    94: ldc_w           "Method"
        //    97: aload_1        
        //    98: invokevirtual   java/net/HttpURLConnection.getRequestMethod:()Ljava/lang/String;
        //   101: invokevirtual   com/facebook/internal/Logger.appendKeyValue:(Ljava/lang/String;Ljava/lang/Object;)V
        //   104: aload           7
        //   106: ldc             "User-Agent"
        //   108: aload_1        
        //   109: ldc             "User-Agent"
        //   111: invokevirtual   java/net/HttpURLConnection.getRequestProperty:(Ljava/lang/String;)Ljava/lang/String;
        //   114: invokevirtual   com/facebook/internal/Logger.appendKeyValue:(Ljava/lang/String;Ljava/lang/Object;)V
        //   117: aload           7
        //   119: ldc             "Content-Type"
        //   121: aload_1        
        //   122: ldc             "Content-Type"
        //   124: invokevirtual   java/net/HttpURLConnection.getRequestProperty:(Ljava/lang/String;)Ljava/lang/String;
        //   127: invokevirtual   com/facebook/internal/Logger.appendKeyValue:(Ljava/lang/String;Ljava/lang/Object;)V
        //   130: aload_1        
        //   131: aload_0        
        //   132: invokevirtual   com/facebook/RequestBatch.getTimeout:()I
        //   135: invokevirtual   java/net/HttpURLConnection.setConnectTimeout:(I)V
        //   138: aload_1        
        //   139: aload_0        
        //   140: invokevirtual   com/facebook/RequestBatch.getTimeout:()I
        //   143: invokevirtual   java/net/HttpURLConnection.setReadTimeout:(I)V
        //   146: aload           5
        //   148: getstatic       com/facebook/HttpMethod.POST:Lcom/facebook/HttpMethod;
        //   151: if_acmpne       174
        //   154: iconst_1       
        //   155: istore_2       
        //   156: iload_2        
        //   157: ifne            179
        //   160: aload           7
        //   162: invokevirtual   com/facebook/internal/Logger.log:()V
        //   165: return         
        //   166: getstatic       com/facebook/HttpMethod.POST:Lcom/facebook/HttpMethod;
        //   169: astore          5
        //   171: goto            41
        //   174: iconst_0       
        //   175: istore_2       
        //   176: goto            156
        //   179: aload_1        
        //   180: iconst_1       
        //   181: invokevirtual   java/net/HttpURLConnection.setDoOutput:(Z)V
        //   184: aconst_null    
        //   185: astore          5
        //   187: new             Ljava/io/BufferedOutputStream;
        //   190: dup            
        //   191: aload_1        
        //   192: invokevirtual   java/net/HttpURLConnection.getOutputStream:()Ljava/io/OutputStream;
        //   195: invokespecial   java/io/BufferedOutputStream.<init>:(Ljava/io/OutputStream;)V
        //   198: astore          6
        //   200: aload           6
        //   202: astore_1       
        //   203: iload           4
        //   205: ifeq            222
        //   208: aload           6
        //   210: astore          5
        //   212: new             Ljava/util/zip/GZIPOutputStream;
        //   215: dup            
        //   216: aload           6
        //   218: invokespecial   java/util/zip/GZIPOutputStream.<init>:(Ljava/io/OutputStream;)V
        //   221: astore_1       
        //   222: aload_1        
        //   223: astore          5
        //   225: aload_0        
        //   226: invokestatic    com/facebook/Request.hasOnProgressCallbacks:(Lcom/facebook/RequestBatch;)Z
        //   229: ifeq            338
        //   232: aload_1        
        //   233: astore          5
        //   235: new             Lcom/facebook/ProgressNoopOutputStream;
        //   238: dup            
        //   239: aload_0        
        //   240: invokevirtual   com/facebook/RequestBatch.getCallbackHandler:()Landroid/os/Handler;
        //   243: invokespecial   com/facebook/ProgressNoopOutputStream.<init>:(Landroid/os/Handler;)V
        //   246: astore          6
        //   248: aload_1        
        //   249: astore          5
        //   251: aload_0        
        //   252: aconst_null    
        //   253: iload_3        
        //   254: aload           8
        //   256: aload           6
        //   258: iload           4
        //   260: invokestatic    com/facebook/Request.processRequest:(Lcom/facebook/RequestBatch;Lcom/facebook/internal/Logger;ILjava/net/URL;Ljava/io/OutputStream;Z)V
        //   263: aload_1        
        //   264: astore          5
        //   266: aload           6
        //   268: invokevirtual   com/facebook/ProgressNoopOutputStream.getMaxProgress:()I
        //   271: istore_2       
        //   272: aload_1        
        //   273: astore          5
        //   275: new             Lcom/facebook/ProgressOutputStream;
        //   278: dup            
        //   279: aload_1        
        //   280: aload_0        
        //   281: aload           6
        //   283: invokevirtual   com/facebook/ProgressNoopOutputStream.getProgressMap:()Ljava/util/Map;
        //   286: iload_2        
        //   287: i2l            
        //   288: invokespecial   com/facebook/ProgressOutputStream.<init>:(Ljava/io/OutputStream;Lcom/facebook/RequestBatch;Ljava/util/Map;J)V
        //   291: astore_1       
        //   292: aload_1        
        //   293: astore          5
        //   295: aload_0        
        //   296: aload           7
        //   298: iload_3        
        //   299: aload           8
        //   301: aload_1        
        //   302: iload           4
        //   304: invokestatic    com/facebook/Request.processRequest:(Lcom/facebook/RequestBatch;Lcom/facebook/internal/Logger;ILjava/net/URL;Ljava/io/OutputStream;Z)V
        //   307: aload_1        
        //   308: ifnull          315
        //   311: aload_1        
        //   312: invokevirtual   java/io/OutputStream.close:()V
        //   315: aload           7
        //   317: invokevirtual   com/facebook/internal/Logger.log:()V
        //   320: return         
        //   321: astore_0       
        //   322: aload           5
        //   324: ifnull          332
        //   327: aload           5
        //   329: invokevirtual   java/io/OutputStream.close:()V
        //   332: aload_0        
        //   333: athrow         
        //   334: astore_0       
        //   335: goto            322
        //   338: goto            292
        //    Exceptions:
        //  throws java.io.IOException
        //  throws org.json.JSONException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  187    200    321    322    Any
        //  212    222    334    338    Any
        //  225    232    334    338    Any
        //  235    248    334    338    Any
        //  251    263    334    338    Any
        //  266    272    334    338    Any
        //  275    292    334    338    Any
        //  295    307    321    322    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0222:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static void setConnectionContentType(final HttpURLConnection httpURLConnection, final boolean b) {
        if (b) {
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Content-Encoding", "gzip");
            return;
        }
        httpURLConnection.setRequestProperty("Content-Type", getMimeContentType());
    }
    
    public static final void setDefaultBatchApplicationId(final String defaultBatchApplicationId) {
        Request.defaultBatchApplicationId = defaultBatchApplicationId;
    }
    
    public static HttpURLConnection toHttpConnection(final RequestBatch p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: invokevirtual   com/facebook/RequestBatch.size:()I
        //     4: iconst_1       
        //     5: if_icmpne       36
        //     8: new             Ljava/net/URL;
        //    11: dup            
        //    12: aload_0        
        //    13: iconst_0       
        //    14: invokevirtual   com/facebook/RequestBatch.get:(I)Lcom/facebook/Request;
        //    17: invokevirtual   com/facebook/Request.getUrlForSingleRequest:()Ljava/lang/String;
        //    20: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
        //    23: astore_1       
        //    24: aload_1        
        //    25: invokestatic    com/facebook/Request.createConnection:(Ljava/net/URL;)Ljava/net/HttpURLConnection;
        //    28: astore_1       
        //    29: aload_0        
        //    30: aload_1        
        //    31: invokestatic    com/facebook/Request.serializeToUrlConnection:(Lcom/facebook/RequestBatch;Ljava/net/HttpURLConnection;)V
        //    34: aload_1        
        //    35: areturn        
        //    36: new             Ljava/net/URL;
        //    39: dup            
        //    40: invokestatic    com/facebook/internal/ServerProtocol.getGraphUrlBase:()Ljava/lang/String;
        //    43: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
        //    46: astore_1       
        //    47: goto            24
        //    50: astore_0       
        //    51: new             Lcom/facebook/FacebookException;
        //    54: dup            
        //    55: ldc_w           "could not construct URL for request"
        //    58: aload_0        
        //    59: invokespecial   com/facebook/FacebookException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //    62: athrow         
        //    63: astore_0       
        //    64: new             Lcom/facebook/FacebookException;
        //    67: dup            
        //    68: ldc_w           "could not construct request body"
        //    71: aload_0        
        //    72: invokespecial   com/facebook/FacebookException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //    75: athrow         
        //    76: astore_0       
        //    77: new             Lcom/facebook/FacebookException;
        //    80: dup            
        //    81: ldc_w           "could not construct request body"
        //    84: aload_0        
        //    85: invokespecial   com/facebook/FacebookException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //    88: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  0      24     50     63     Ljava/net/MalformedURLException;
        //  24     34     63     76     Ljava/io/IOException;
        //  24     34     76     89     Lorg/json/JSONException;
        //  36     47     50     63     Ljava/net/MalformedURLException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0024:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static HttpURLConnection toHttpConnection(final Collection<Request> collection) {
        Validate.notEmptyAndContainsNoNulls((Collection<Object>)collection, "requests");
        return toHttpConnection(new RequestBatch(collection));
    }
    
    public static HttpURLConnection toHttpConnection(final Request... array) {
        return toHttpConnection(Arrays.asList(array));
    }
    
    private static <T extends GraphObject> List<T> typedListFromResponse(final Response response, final Class<T> clazz) {
        final GraphMultiResult graphMultiResult = response.getGraphObjectAs(GraphMultiResult.class);
        if (graphMultiResult != null) {
            final GraphObjectList<GraphObject> data = graphMultiResult.getData();
            if (data != null) {
                return data.castToListOf(clazz);
            }
        }
        return null;
    }
    
    public final Response executeAndWait() {
        return executeAndWait(this);
    }
    
    public final RequestAsyncTask executeAsync() {
        return executeBatchAsync(this);
    }
    
    public final String getBatchEntryDependsOn() {
        return this.batchEntryDependsOn;
    }
    
    public final String getBatchEntryName() {
        return this.batchEntryName;
    }
    
    public final boolean getBatchEntryOmitResultOnSuccess() {
        return this.batchEntryOmitResultOnSuccess;
    }
    
    public final Callback getCallback() {
        return this.callback;
    }
    
    public final GraphObject getGraphObject() {
        return this.graphObject;
    }
    
    public final String getGraphPath() {
        return this.graphPath;
    }
    
    public final HttpMethod getHttpMethod() {
        return this.httpMethod;
    }
    
    public final Bundle getParameters() {
        return this.parameters;
    }
    
    public final Session getSession() {
        return this.session;
    }
    
    public final Object getTag() {
        return this.tag;
    }
    
    final String getUrlForBatchedRequest() {
        if (this.overriddenURL != null) {
            throw new FacebookException("Can't override URL for a batch request");
        }
        final String graphPathWithVersion = this.getGraphPathWithVersion();
        this.addCommonParameters();
        return this.appendParametersToBaseUrl(graphPathWithVersion);
    }
    
    final String getUrlForSingleRequest() {
        if (this.overriddenURL != null) {
            return this.overriddenURL.toString();
        }
        String s;
        if (this.getHttpMethod() == HttpMethod.POST && this.graphPath != null && this.graphPath.endsWith("/videos")) {
            s = ServerProtocol.getGraphVideoUrlBase();
        }
        else {
            s = ServerProtocol.getGraphUrlBase();
        }
        final String format = String.format("%s/%s", s, this.getGraphPathWithVersion());
        this.addCommonParameters();
        return this.appendParametersToBaseUrl(format);
    }
    
    public final String getVersion() {
        return this.version;
    }
    
    public final void setBatchEntryDependsOn(final String batchEntryDependsOn) {
        this.batchEntryDependsOn = batchEntryDependsOn;
    }
    
    public final void setBatchEntryName(final String batchEntryName) {
        this.batchEntryName = batchEntryName;
    }
    
    public final void setBatchEntryOmitResultOnSuccess(final boolean batchEntryOmitResultOnSuccess) {
        this.batchEntryOmitResultOnSuccess = batchEntryOmitResultOnSuccess;
    }
    
    public final void setCallback(final Callback callback) {
        this.callback = callback;
    }
    
    public final void setGraphObject(final GraphObject graphObject) {
        this.graphObject = graphObject;
    }
    
    public final void setGraphPath(final String graphPath) {
        this.graphPath = graphPath;
    }
    
    public final void setHttpMethod(HttpMethod get) {
        if (this.overriddenURL != null && get != HttpMethod.GET) {
            throw new FacebookException("Can't change HTTP method on request with overridden URL.");
        }
        if (get == null) {
            get = HttpMethod.GET;
        }
        this.httpMethod = get;
    }
    
    public final void setParameters(final Bundle parameters) {
        this.parameters = parameters;
    }
    
    public final void setSession(final Session session) {
        this.session = session;
    }
    
    public final void setSkipClientToken(final boolean skipClientToken) {
        this.skipClientToken = skipClientToken;
    }
    
    public final void setTag(final Object tag) {
        this.tag = tag;
    }
    
    public final void setVersion(final String version) {
        this.version = version;
    }
    
    @Override
    public String toString() {
        return "{Request: " + " session: " + this.session + ", graphPath: " + this.graphPath + ", graphObject: " + this.graphObject + ", httpMethod: " + this.httpMethod + ", parameters: " + this.parameters + "}";
    }
    
    private static class Attachment
    {
        private final Request request;
        private final Object value;
        
        public Attachment(final Request request, final Object value) {
            this.request = request;
            this.value = value;
        }
        
        public Request getRequest() {
            return this.request;
        }
        
        public Object getValue() {
            return this.value;
        }
    }
    
    public interface Callback
    {
        void onCompleted(final Response p0);
    }
    
    public interface GraphPlaceListCallback
    {
        void onCompleted(final List<GraphPlace> p0, final Response p1);
    }
    
    public interface GraphUserCallback
    {
        void onCompleted(final GraphUser p0, final Response p1);
    }
    
    public interface GraphUserListCallback
    {
        void onCompleted(final List<GraphUser> p0, final Response p1);
    }
    
    private interface KeyValueSerializer
    {
        void writeString(final String p0, final String p1) throws IOException;
    }
    
    public interface OnProgressCallback extends Callback
    {
        void onProgress(final long p0, final long p1);
    }
    
    private static class ParcelFileDescriptorWithMimeType implements Parcelable
    {
        public static final Parcelable$Creator<ParcelFileDescriptorWithMimeType> CREATOR;
        private final ParcelFileDescriptor fileDescriptor;
        private final String mimeType;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<ParcelFileDescriptorWithMimeType>() {
                public ParcelFileDescriptorWithMimeType createFromParcel(final Parcel parcel) {
                    return new ParcelFileDescriptorWithMimeType(parcel);
                }
                
                public ParcelFileDescriptorWithMimeType[] newArray(final int n) {
                    return new ParcelFileDescriptorWithMimeType[n];
                }
            };
        }
        
        private ParcelFileDescriptorWithMimeType(final Parcel parcel) {
            this.mimeType = parcel.readString();
            this.fileDescriptor = parcel.readFileDescriptor();
        }
        
        public ParcelFileDescriptorWithMimeType(final ParcelFileDescriptor fileDescriptor, final String mimeType) {
            this.mimeType = mimeType;
            this.fileDescriptor = fileDescriptor;
        }
        
        public int describeContents() {
            return 1;
        }
        
        public ParcelFileDescriptor getFileDescriptor() {
            return this.fileDescriptor;
        }
        
        public String getMimeType() {
            return this.mimeType;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            parcel.writeString(this.mimeType);
            parcel.writeFileDescriptor(this.fileDescriptor.getFileDescriptor());
        }
    }
    
    private static class Serializer implements KeyValueSerializer
    {
        private boolean firstWrite;
        private final Logger logger;
        private final OutputStream outputStream;
        private boolean useUrlEncode;
        
        public Serializer(final OutputStream outputStream, final Logger logger, final boolean useUrlEncode) {
            this.firstWrite = true;
            this.useUrlEncode = false;
            this.outputStream = outputStream;
            this.logger = logger;
            this.useUrlEncode = useUrlEncode;
        }
        
        public void write(final String s, final Object... array) throws IOException {
            if (!this.useUrlEncode) {
                if (this.firstWrite) {
                    this.outputStream.write("--".getBytes());
                    this.outputStream.write("3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f".getBytes());
                    this.outputStream.write("\r\n".getBytes());
                    this.firstWrite = false;
                }
                this.outputStream.write(String.format(s, array).getBytes());
                return;
            }
            this.outputStream.write(URLEncoder.encode(String.format(s, array), "UTF-8").getBytes());
        }
        
        public void writeBitmap(final String s, final Bitmap bitmap) throws IOException {
            this.writeContentDisposition(s, s, "image/png");
            bitmap.compress(Bitmap$CompressFormat.PNG, 100, this.outputStream);
            this.writeLine("", new Object[0]);
            this.writeRecordBoundary();
            if (this.logger != null) {
                this.logger.appendKeyValue("    " + s, "<Image>");
            }
        }
        
        public void writeBytes(final String s, final byte[] array) throws IOException {
            this.writeContentDisposition(s, s, "content/unknown");
            this.outputStream.write(array);
            this.writeLine("", new Object[0]);
            this.writeRecordBoundary();
            if (this.logger != null) {
                this.logger.appendKeyValue("    " + s, String.format("<Data: %d>", array.length));
            }
        }
        
        public void writeContentDisposition(final String s, final String s2, final String s3) throws IOException {
            if (!this.useUrlEncode) {
                this.write("Content-Disposition: form-data; name=\"%s\"", s);
                if (s2 != null) {
                    this.write("; filename=\"%s\"", s2);
                }
                this.writeLine("", new Object[0]);
                if (s3 != null) {
                    this.writeLine("%s: %s", "Content-Type", s3);
                }
                this.writeLine("", new Object[0]);
                return;
            }
            this.outputStream.write(String.format("%s=", s).getBytes());
        }
        
        public void writeFile(final String p0, final ParcelFileDescriptor p1, final String p2) throws IOException {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: aload_3        
            //     1: astore          6
            //     3: aload_3        
            //     4: ifnonnull       11
            //     7: ldc             "content/unknown"
            //     9: astore          6
            //    11: aload_0        
            //    12: aload_1        
            //    13: aload_1        
            //    14: aload           6
            //    16: invokevirtual   com/facebook/Request$Serializer.writeContentDisposition:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
            //    19: iconst_0       
            //    20: istore          4
            //    22: iconst_0       
            //    23: istore          5
            //    25: aload_0        
            //    26: getfield        com/facebook/Request$Serializer.outputStream:Ljava/io/OutputStream;
            //    29: instanceof      Lcom/facebook/ProgressNoopOutputStream;
            //    32: ifeq            114
            //    35: aload_0        
            //    36: getfield        com/facebook/Request$Serializer.outputStream:Ljava/io/OutputStream;
            //    39: checkcast       Lcom/facebook/ProgressNoopOutputStream;
            //    42: aload_2        
            //    43: invokevirtual   android/os/ParcelFileDescriptor.getStatSize:()J
            //    46: invokevirtual   com/facebook/ProgressNoopOutputStream.addProgress:(J)V
            //    49: aload_0        
            //    50: ldc             ""
            //    52: iconst_0       
            //    53: anewarray       Ljava/lang/Object;
            //    56: invokevirtual   com/facebook/Request$Serializer.writeLine:(Ljava/lang/String;[Ljava/lang/Object;)V
            //    59: aload_0        
            //    60: invokevirtual   com/facebook/Request$Serializer.writeRecordBoundary:()V
            //    63: aload_0        
            //    64: getfield        com/facebook/Request$Serializer.logger:Lcom/facebook/internal/Logger;
            //    67: ifnull          113
            //    70: aload_0        
            //    71: getfield        com/facebook/Request$Serializer.logger:Lcom/facebook/internal/Logger;
            //    74: new             Ljava/lang/StringBuilder;
            //    77: dup            
            //    78: invokespecial   java/lang/StringBuilder.<init>:()V
            //    81: ldc             "    "
            //    83: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    86: aload_1        
            //    87: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    90: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //    93: ldc             "<Data: %d>"
            //    95: iconst_1       
            //    96: anewarray       Ljava/lang/Object;
            //    99: dup            
            //   100: iconst_0       
            //   101: iload           5
            //   103: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   106: aastore        
            //   107: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
            //   110: invokevirtual   com/facebook/internal/Logger.appendKeyValue:(Ljava/lang/String;Ljava/lang/Object;)V
            //   113: return         
            //   114: aconst_null    
            //   115: astore          6
            //   117: aconst_null    
            //   118: astore_3       
            //   119: new             Landroid/os/ParcelFileDescriptor$AutoCloseInputStream;
            //   122: dup            
            //   123: aload_2        
            //   124: invokespecial   android/os/ParcelFileDescriptor$AutoCloseInputStream.<init>:(Landroid/os/ParcelFileDescriptor;)V
            //   127: astore_2       
            //   128: new             Ljava/io/BufferedInputStream;
            //   131: dup            
            //   132: aload_2        
            //   133: invokespecial   java/io/BufferedInputStream.<init>:(Ljava/io/InputStream;)V
            //   136: astore          7
            //   138: sipush          8192
            //   141: newarray        B
            //   143: astore_3       
            //   144: aload           7
            //   146: aload_3        
            //   147: invokevirtual   java/io/BufferedInputStream.read:([B)I
            //   150: istore          5
            //   152: iload           5
            //   154: iconst_m1      
            //   155: if_icmpeq       179
            //   158: aload_0        
            //   159: getfield        com/facebook/Request$Serializer.outputStream:Ljava/io/OutputStream;
            //   162: aload_3        
            //   163: iconst_0       
            //   164: iload           5
            //   166: invokevirtual   java/io/OutputStream.write:([BII)V
            //   169: iload           4
            //   171: iload           5
            //   173: iadd           
            //   174: istore          4
            //   176: goto            144
            //   179: aload           7
            //   181: ifnull          189
            //   184: aload           7
            //   186: invokevirtual   java/io/BufferedInputStream.close:()V
            //   189: iload           4
            //   191: istore          5
            //   193: aload_2        
            //   194: ifnull          49
            //   197: aload_2        
            //   198: invokevirtual   android/os/ParcelFileDescriptor$AutoCloseInputStream.close:()V
            //   201: iload           4
            //   203: istore          5
            //   205: goto            49
            //   208: astore_2       
            //   209: aload           6
            //   211: astore_1       
            //   212: aload_3        
            //   213: ifnull          220
            //   216: aload_3        
            //   217: invokevirtual   java/io/BufferedInputStream.close:()V
            //   220: aload_1        
            //   221: ifnull          228
            //   224: aload_1        
            //   225: invokevirtual   android/os/ParcelFileDescriptor$AutoCloseInputStream.close:()V
            //   228: aload_2        
            //   229: athrow         
            //   230: astore          6
            //   232: aload_2        
            //   233: astore_1       
            //   234: aload           6
            //   236: astore_2       
            //   237: goto            212
            //   240: astore          6
            //   242: aload           7
            //   244: astore_3       
            //   245: aload_2        
            //   246: astore_1       
            //   247: aload           6
            //   249: astore_2       
            //   250: goto            212
            //    Exceptions:
            //  throws java.io.IOException
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  ----
            //  119    128    208    212    Any
            //  128    138    230    240    Any
            //  138    144    240    253    Any
            //  144    152    240    253    Any
            //  158    169    240    253    Any
            // 
            // The error that occurred was:
            // 
            // java.lang.IndexOutOfBoundsException: Index: 131, Size: 131
            //     at java.util.ArrayList.rangeCheck(ArrayList.java:653)
            //     at java.util.ArrayList.get(ArrayList.java:429)
            //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3303)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:556)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        public void writeFile(final String s, final ParcelFileDescriptorWithMimeType parcelFileDescriptorWithMimeType) throws IOException {
            this.writeFile(s, parcelFileDescriptorWithMimeType.getFileDescriptor(), parcelFileDescriptorWithMimeType.getMimeType());
        }
        
        public void writeLine(final String s, final Object... array) throws IOException {
            this.write(s, array);
            if (!this.useUrlEncode) {
                this.write("\r\n", new Object[0]);
            }
        }
        
        public void writeObject(final String s, final Object o, final Request currentRequest) throws IOException {
            if (this.outputStream instanceof RequestOutputStream) {
                ((RequestOutputStream)this.outputStream).setCurrentRequest(currentRequest);
            }
            if (isSupportedParameterType(o)) {
                this.writeString(s, parameterToString(o));
                return;
            }
            if (o instanceof Bitmap) {
                this.writeBitmap(s, (Bitmap)o);
                return;
            }
            if (o instanceof byte[]) {
                this.writeBytes(s, (byte[])o);
                return;
            }
            if (o instanceof ParcelFileDescriptor) {
                this.writeFile(s, (ParcelFileDescriptor)o, null);
                return;
            }
            if (o instanceof ParcelFileDescriptorWithMimeType) {
                this.writeFile(s, (ParcelFileDescriptorWithMimeType)o);
                return;
            }
            throw new IllegalArgumentException("value is not a supported type: String, Bitmap, byte[]");
        }
        
        public void writeRecordBoundary() throws IOException {
            if (!this.useUrlEncode) {
                this.writeLine("--%s", "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f");
                return;
            }
            this.outputStream.write("&".getBytes());
        }
        
        public void writeRequestsAsJson(final String s, final JSONArray jsonArray, final Collection<Request> collection) throws IOException, JSONException {
            if (!(this.outputStream instanceof RequestOutputStream)) {
                this.writeString(s, jsonArray.toString());
            }
            else {
                final RequestOutputStream requestOutputStream = (RequestOutputStream)this.outputStream;
                this.writeContentDisposition(s, null, null);
                this.write("[", new Object[0]);
                int n = 0;
                for (final Request currentRequest : collection) {
                    final JSONObject jsonObject = jsonArray.getJSONObject(n);
                    requestOutputStream.setCurrentRequest(currentRequest);
                    if (n > 0) {
                        this.write(",%s", jsonObject.toString());
                    }
                    else {
                        this.write("%s", jsonObject.toString());
                    }
                    ++n;
                }
                this.write("]", new Object[0]);
                if (this.logger != null) {
                    this.logger.appendKeyValue("    " + s, jsonArray.toString());
                }
            }
        }
        
        @Override
        public void writeString(final String s, final String s2) throws IOException {
            this.writeContentDisposition(s, null, null);
            this.writeLine("%s", s2);
            this.writeRecordBoundary();
            if (this.logger != null) {
                this.logger.appendKeyValue("    " + s, s2);
            }
        }
    }
}
