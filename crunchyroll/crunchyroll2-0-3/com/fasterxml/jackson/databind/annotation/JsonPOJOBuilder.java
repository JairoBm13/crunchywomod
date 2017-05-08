// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE })
public @interface JsonPOJOBuilder {
    String buildMethodName() default "build";
    
    String withPrefix() default "with";
    
    public static class Value
    {
        public final String buildMethodName;
        public final String withPrefix;
        
        public Value(final JsonPOJOBuilder jsonPOJOBuilder) {
            this.buildMethodName = jsonPOJOBuilder.buildMethodName();
            this.withPrefix = jsonPOJOBuilder.withPrefix();
        }
    }
}
