// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
public @interface JsonTypeInfo {
    Class<?> defaultImpl() default None.class;
    
    As include() default As.PROPERTY;
    
    String property() default "";
    
    Id use();
    
    boolean visible() default false;
    
    public enum As
    {
        EXTERNAL_PROPERTY, 
        PROPERTY, 
        WRAPPER_ARRAY, 
        WRAPPER_OBJECT;
    }
    
    public enum Id
    {
        CLASS("@class"), 
        CUSTOM((String)null), 
        MINIMAL_CLASS("@c"), 
        NAME("@type"), 
        NONE((String)null);
        
        private final String _defaultPropertyName;
        
        private Id(final String defaultPropertyName) {
            this._defaultPropertyName = defaultPropertyName;
        }
        
        public String getDefaultPropertyName() {
            return this._defaultPropertyName;
        }
    }
    
    public abstract static class None
    {
    }
}
