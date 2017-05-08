// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook;

import org.json.JSONException;
import com.facebook.internal.Utility;
import com.facebook.android.R;
import org.json.JSONObject;
import java.net.HttpURLConnection;

public final class FacebookRequestError
{
    private static final String BODY_KEY = "body";
    private static final String CODE_KEY = "code";
    private static final int EC_APP_NOT_INSTALLED = 458;
    private static final int EC_APP_TOO_MANY_CALLS = 4;
    private static final int EC_EXPIRED = 463;
    private static final int EC_INVALID_SESSION = 102;
    private static final int EC_INVALID_TOKEN = 190;
    private static final int EC_PASSWORD_CHANGED = 460;
    private static final int EC_PERMISSION_DENIED = 10;
    private static final Range EC_RANGE_PERMISSION;
    private static final int EC_SERVICE_UNAVAILABLE = 2;
    private static final int EC_UNCONFIRMED_USER = 464;
    private static final int EC_UNKNOWN_ERROR = 1;
    private static final int EC_USER_CHECKPOINTED = 459;
    private static final int EC_USER_TOO_MANY_CALLS = 17;
    private static final String ERROR_CODE_FIELD_KEY = "code";
    private static final String ERROR_CODE_KEY = "error_code";
    private static final String ERROR_IS_TRANSIENT_KEY = "is_transient";
    private static final String ERROR_KEY = "error";
    private static final String ERROR_MESSAGE_FIELD_KEY = "message";
    private static final String ERROR_MSG_KEY = "error_msg";
    private static final String ERROR_REASON_KEY = "error_reason";
    private static final String ERROR_SUB_CODE_KEY = "error_subcode";
    private static final String ERROR_TYPE_FIELD_KEY = "type";
    private static final String ERROR_USER_MSG_KEY = "error_user_msg";
    private static final String ERROR_USER_TITLE_KEY = "error_user_title";
    private static final Range HTTP_RANGE_CLIENT_ERROR;
    private static final Range HTTP_RANGE_SERVER_ERROR;
    private static final Range HTTP_RANGE_SUCCESS;
    public static final int INVALID_ERROR_CODE = -1;
    public static final int INVALID_HTTP_STATUS_CODE = -1;
    private static final int INVALID_MESSAGE_ID = 0;
    private final Object batchRequestResult;
    private final Category category;
    private final HttpURLConnection connection;
    private final int errorCode;
    private final boolean errorIsTransient;
    private final String errorMessage;
    private final String errorType;
    private final String errorUserMessage;
    private final String errorUserTitle;
    private final FacebookException exception;
    private final JSONObject requestResult;
    private final JSONObject requestResultBody;
    private final int requestStatusCode;
    private final boolean shouldNotifyUser;
    private final int subErrorCode;
    private final int userActionMessageId;
    
    static {
        EC_RANGE_PERMISSION = new Range(200, 299);
        HTTP_RANGE_SUCCESS = new Range(200, 299);
        HTTP_RANGE_CLIENT_ERROR = new Range(400, 499);
        HTTP_RANGE_SERVER_ERROR = new Range(500, 599);
    }
    
    private FacebookRequestError(final int n, final int n2, final int n3, final String s, final String s2, final String s3, final String s4, final boolean b, final JSONObject jsonObject, final JSONObject jsonObject2, final Object o, final HttpURLConnection httpURLConnection) {
        this(n, n2, n3, s, s2, s3, s4, b, jsonObject, jsonObject2, o, httpURLConnection, null);
    }
    
    private FacebookRequestError(final int requestStatusCode, int n, final int subErrorCode, final String errorType, final String errorMessage, final String errorUserTitle, final String errorUserMessage, final boolean errorIsTransient, final JSONObject requestResultBody, final JSONObject requestResult, final Object batchRequestResult, final HttpURLConnection connection, final FacebookException exception) {
        this.requestStatusCode = requestStatusCode;
        this.errorCode = n;
        this.subErrorCode = subErrorCode;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
        this.requestResultBody = requestResultBody;
        this.requestResult = requestResult;
        this.batchRequestResult = batchRequestResult;
        this.connection = connection;
        this.errorUserTitle = errorUserTitle;
        this.errorUserMessage = errorUserMessage;
        this.errorIsTransient = errorIsTransient;
        boolean b = false;
        if (exception != null) {
            this.exception = exception;
            b = true;
        }
        else {
            this.exception = new FacebookServiceException(this, errorMessage);
        }
        Category category = null;
        final boolean b2 = false;
        Category category2;
        if (b) {
            category2 = Category.CLIENT;
            n = 0;
        }
        else {
            int n2 = 0;
            Label_0178: {
                if (n == 1 || n == 2) {
                    category = Category.SERVER;
                    n2 = (b2 ? 1 : 0);
                }
                else if (n == 4 || n == 17) {
                    category = Category.THROTTLING;
                    n2 = (b2 ? 1 : 0);
                }
                else if (n == 10 || FacebookRequestError.EC_RANGE_PERMISSION.contains(n)) {
                    category = Category.PERMISSION;
                    n2 = R.string.com_facebook_requesterror_permissions;
                }
                else {
                    if (n != 102) {
                        n2 = (b2 ? 1 : 0);
                        if (n != 190) {
                            break Label_0178;
                        }
                    }
                    if (subErrorCode == 459 || subErrorCode == 464) {
                        category = Category.AUTHENTICATION_RETRY;
                        n2 = R.string.com_facebook_requesterror_web_login;
                    }
                    else {
                        category = Category.AUTHENTICATION_REOPEN_SESSION;
                        if (subErrorCode == 458 || subErrorCode == 463) {
                            n2 = R.string.com_facebook_requesterror_relogin;
                        }
                        else if (subErrorCode == 460) {
                            n2 = R.string.com_facebook_requesterror_password_changed;
                        }
                        else {
                            n2 = R.string.com_facebook_requesterror_reconnect;
                        }
                    }
                }
            }
            category2 = category;
            n = n2;
            if (category == null) {
                if (FacebookRequestError.HTTP_RANGE_CLIENT_ERROR.contains(requestStatusCode)) {
                    category2 = Category.BAD_REQUEST;
                    n = n2;
                }
                else if (FacebookRequestError.HTTP_RANGE_SERVER_ERROR.contains(requestStatusCode)) {
                    category2 = Category.SERVER;
                    n = n2;
                }
                else {
                    category2 = Category.OTHER;
                    n = n2;
                }
            }
        }
        final boolean shouldNotifyUser = errorUserMessage != null && errorUserMessage.length() > 0;
        this.category = category2;
        this.userActionMessageId = n;
        this.shouldNotifyUser = shouldNotifyUser;
    }
    
    public FacebookRequestError(final int n, final String s, final String s2) {
        this(-1, n, -1, s, s2, null, null, false, null, null, null, null, null);
    }
    
    FacebookRequestError(final HttpURLConnection httpURLConnection, final Exception ex) {
        FacebookException ex2;
        if (ex instanceof FacebookException) {
            ex2 = (FacebookException)ex;
        }
        else {
            ex2 = new FacebookException(ex);
        }
        this(-1, -1, -1, null, null, null, null, false, null, null, null, httpURLConnection, ex2);
    }
    
    static FacebookRequestError checkResponseAndCreateError(final JSONObject jsonObject, final Object o, final HttpURLConnection httpURLConnection) {
        try {
            if (jsonObject.has("code")) {
                final int int1 = jsonObject.getInt("code");
                final Object stringPropertyAsJSON = Utility.getStringPropertyAsJSON(jsonObject, "body", "FACEBOOK_NON_JSON_RESULT");
                if (stringPropertyAsJSON != null && stringPropertyAsJSON instanceof JSONObject) {
                    final JSONObject jsonObject2 = (JSONObject)stringPropertyAsJSON;
                    String s = null;
                    String s2 = null;
                    final String s3 = null;
                    final String s4 = null;
                    final boolean b = false;
                    int n = -1;
                    int n2 = -1;
                    int n3 = 0;
                    String optString = null;
                    String optString2 = null;
                    boolean optBoolean = false;
                    Label_0165: {
                        if (jsonObject2.has("error")) {
                            final JSONObject jsonObject3 = (JSONObject)Utility.getStringPropertyAsJSON(jsonObject2, "error", null);
                            s = jsonObject3.optString("type", (String)null);
                            s2 = jsonObject3.optString("message", (String)null);
                            n = jsonObject3.optInt("code", -1);
                            n2 = jsonObject3.optInt("error_subcode", -1);
                            optString = jsonObject3.optString("error_user_msg", (String)null);
                            optString2 = jsonObject3.optString("error_user_title", (String)null);
                            optBoolean = jsonObject3.optBoolean("is_transient", false);
                            n3 = 1;
                        }
                        else {
                            if (!jsonObject2.has("error_code") && !jsonObject2.has("error_msg")) {
                                optString2 = s4;
                                optString = s3;
                                optBoolean = b;
                                if (!jsonObject2.has("error_reason")) {
                                    break Label_0165;
                                }
                            }
                            s = jsonObject2.optString("error_reason", (String)null);
                            s2 = jsonObject2.optString("error_msg", (String)null);
                            n = jsonObject2.optInt("error_code", -1);
                            n2 = jsonObject2.optInt("error_subcode", -1);
                            n3 = 1;
                            optString2 = s4;
                            optString = s3;
                            optBoolean = b;
                        }
                    }
                    if (n3 != 0) {
                        return new FacebookRequestError(int1, n, n2, s, s2, optString2, optString, optBoolean, jsonObject2, jsonObject, o, httpURLConnection);
                    }
                }
                if (!FacebookRequestError.HTTP_RANGE_SUCCESS.contains(int1)) {
                    JSONObject jsonObject4;
                    if (jsonObject.has("body")) {
                        jsonObject4 = (JSONObject)Utility.getStringPropertyAsJSON(jsonObject, "body", "FACEBOOK_NON_JSON_RESULT");
                    }
                    else {
                        jsonObject4 = null;
                    }
                    return new FacebookRequestError(int1, -1, -1, null, null, null, null, false, jsonObject4, jsonObject, o, httpURLConnection);
                }
            }
        }
        catch (JSONException ex) {}
        return null;
    }
    
    public Object getBatchRequestResult() {
        return this.batchRequestResult;
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public HttpURLConnection getConnection() {
        return this.connection;
    }
    
    public int getErrorCode() {
        return this.errorCode;
    }
    
    public boolean getErrorIsTransient() {
        return this.errorIsTransient;
    }
    
    public String getErrorMessage() {
        if (this.errorMessage != null) {
            return this.errorMessage;
        }
        return this.exception.getLocalizedMessage();
    }
    
    public String getErrorType() {
        return this.errorType;
    }
    
    public String getErrorUserMessage() {
        return this.errorUserMessage;
    }
    
    public String getErrorUserTitle() {
        return this.errorUserTitle;
    }
    
    public FacebookException getException() {
        return this.exception;
    }
    
    public JSONObject getRequestResult() {
        return this.requestResult;
    }
    
    public JSONObject getRequestResultBody() {
        return this.requestResultBody;
    }
    
    public int getRequestStatusCode() {
        return this.requestStatusCode;
    }
    
    public int getSubErrorCode() {
        return this.subErrorCode;
    }
    
    public int getUserActionMessageId() {
        return this.userActionMessageId;
    }
    
    public boolean shouldNotifyUser() {
        return this.shouldNotifyUser;
    }
    
    @Override
    public String toString() {
        return "{HttpStatus: " + this.requestStatusCode + ", errorCode: " + this.errorCode + ", errorType: " + this.errorType + ", errorMessage: " + this.getErrorMessage() + "}";
    }
    
    public enum Category
    {
        AUTHENTICATION_REOPEN_SESSION, 
        AUTHENTICATION_RETRY, 
        BAD_REQUEST, 
        CLIENT, 
        OTHER, 
        PERMISSION, 
        SERVER, 
        THROTTLING;
    }
    
    private static class Range
    {
        private final int end;
        private final int start;
        
        private Range(final int start, final int end) {
            this.start = start;
            this.end = end;
        }
        
        boolean contains(final int n) {
            return this.start <= n && n <= this.end;
        }
    }
}
