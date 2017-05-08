// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.exceptions;

import java.util.Iterator;
import java.util.EnumSet;

public class ApiErrorException extends ApiException
{
    private static final long serialVersionUID = -8880082922494476305L;
    protected final ApiErrorCode errorCode;
    protected final String message;
    protected final Throwable throwable;
    
    ApiErrorException(final Throwable throwable, final String message, final ApiErrorCode errorCode) {
        this.throwable = throwable;
        this.message = message;
        this.errorCode = errorCode;
    }
    
    public static Builder withErrorCode(final ApiErrorCode apiErrorCode) {
        return new Builder(apiErrorCode);
    }
    
    public static Builder withMessage(final String s) {
        return new Builder(s);
    }
    
    public static Builder withThrowable(final Throwable t) {
        return new Builder(t);
    }
    
    public ApiErrorCode getErrorCode() {
        return this.errorCode;
    }
    
    @Override
    public String getMessage() {
        return this.message;
    }
    
    public Throwable getThrowable() {
        return this.throwable;
    }
    
    @Override
    public String toString() {
        return "ApiErrorException [throwable=" + this.throwable + ", message=" + this.message + ", errorCode=" + this.errorCode + "]";
    }
    
    public enum ApiErrorCode
    {
        BAD_AUTH_PARAMS("bad_auth_params"), 
        BAD_REQUEST("bad_request"), 
        BAD_SESSION("bad_session"), 
        DUPLICATE_OBJECT("duplicate_object"), 
        FORBIDDEN("forbidden"), 
        INTERNAL_SERVER_ERROR("internal_server_error"), 
        OBJECT_NOT_FOUND("object_not_found");
        
        private final String errorCode;
        
        private ApiErrorCode(final String errorCode) {
            this.errorCode = errorCode;
        }
        
        public static ApiErrorCode getErrorStatus(final String s) {
            for (final ApiErrorCode apiErrorCode : EnumSet.allOf(ApiErrorCode.class)) {
                if (apiErrorCode.getErrorCodeString().equals(s)) {
                    return apiErrorCode;
                }
            }
            return null;
        }
        
        public String getErrorCodeString() {
            return this.errorCode;
        }
    }
    
    public static class Builder
    {
        private ApiErrorCode errorCode;
        private String message;
        private Throwable throwable;
        
        protected Builder(final ApiErrorCode errorCode) {
            this.errorCode = errorCode;
        }
        
        protected Builder(final String message) {
            this.message = message;
        }
        
        protected Builder(final Throwable throwable) {
            this.throwable = throwable;
        }
        
        public Builder apiErrorCode(final ApiErrorCode errorCode) {
            this.errorCode = errorCode;
            return this;
        }
        
        public ApiErrorException build() {
            if (ApiErrorCode.BAD_SESSION.equals(this.errorCode)) {
                return new ApiBadSessionException(this.throwable, this.message, this.errorCode);
            }
            return new ApiErrorException(this.throwable, this.message, this.errorCode);
        }
        
        public Builder message(final String message) {
            this.message = message;
            return this;
        }
        
        public Builder throwable(final Throwable throwable) {
            this.throwable = throwable;
            return this;
        }
    }
}
