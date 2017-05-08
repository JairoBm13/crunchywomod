// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.answers;

import android.app.Activity;
import java.util.Collections;
import java.util.Map;

final class SessionEvent
{
    public final Map<String, Object> customAttributes;
    public final String customType;
    public final Map<String, String> details;
    public final Map<String, Object> predefinedAttributes;
    public final String predefinedType;
    public final SessionEventMetadata sessionEventMetadata;
    private String stringRepresentation;
    public final long timestamp;
    public final Type type;
    
    private SessionEvent(final SessionEventMetadata sessionEventMetadata, final long timestamp, final Type type, final Map<String, String> details, final String customType, final Map<String, Object> customAttributes, final String predefinedType, final Map<String, Object> predefinedAttributes) {
        this.sessionEventMetadata = sessionEventMetadata;
        this.timestamp = timestamp;
        this.type = type;
        this.details = details;
        this.customType = customType;
        this.customAttributes = customAttributes;
        this.predefinedType = predefinedType;
        this.predefinedAttributes = predefinedAttributes;
    }
    
    public static Builder crashEventBuilder(final String s) {
        return new Builder(Type.CRASH).details(Collections.singletonMap("sessionId", s));
    }
    
    public static Builder installEventBuilder() {
        return new Builder(Type.INSTALL);
    }
    
    public static Builder lifecycleEventBuilder(final Type type, final Activity activity) {
        return new Builder(type).details(Collections.singletonMap("activity", activity.getClass().getName()));
    }
    
    @Override
    public String toString() {
        if (this.stringRepresentation == null) {
            this.stringRepresentation = "[" + this.getClass().getSimpleName() + ": " + "timestamp=" + this.timestamp + ", type=" + this.type + ", details=" + this.details.toString() + ", customType=" + this.customType + ", customAttributes=" + this.customAttributes.toString() + ", predefinedType=" + this.predefinedType + ", predefinedAttributes=" + this.predefinedAttributes.toString() + ", metadata=[" + this.sessionEventMetadata + "]]";
        }
        return this.stringRepresentation;
    }
    
    static class Builder
    {
        Map<String, Object> customAttributes;
        String customType;
        Map<String, String> details;
        Map<String, Object> predefinedAttributes;
        String predefinedType;
        final long timestamp;
        final Type type;
        
        public Builder(final Type type) {
            this.type = type;
            this.timestamp = System.currentTimeMillis();
            this.details = Collections.emptyMap();
            this.customType = null;
            this.customAttributes = Collections.emptyMap();
            this.predefinedType = null;
            this.predefinedAttributes = Collections.emptyMap();
        }
        
        public SessionEvent build(final SessionEventMetadata sessionEventMetadata) {
            return new SessionEvent(sessionEventMetadata, this.timestamp, this.type, this.details, this.customType, this.customAttributes, this.predefinedType, this.predefinedAttributes, null);
        }
        
        public Builder details(final Map<String, String> details) {
            this.details = details;
            return this;
        }
    }
    
    enum Type
    {
        CRASH, 
        CREATE, 
        CUSTOM, 
        DESTROY, 
        ERROR, 
        INSTALL, 
        PAUSE, 
        PREDEFINED, 
        RESUME, 
        SAVE_INSTANCE_STATE, 
        START, 
        STOP;
    }
}
