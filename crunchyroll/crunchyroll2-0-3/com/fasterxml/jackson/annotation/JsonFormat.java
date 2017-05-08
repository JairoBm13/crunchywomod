// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.annotation;

import java.util.TimeZone;
import java.util.Locale;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE })
public @interface JsonFormat {
    String locale() default "##default";
    
    String pattern() default "";
    
    Shape shape() default Shape.ANY;
    
    String timezone() default "##default";
    
    public enum Shape
    {
        ANY, 
        ARRAY, 
        BOOLEAN, 
        NUMBER, 
        NUMBER_FLOAT, 
        NUMBER_INT, 
        OBJECT, 
        SCALAR, 
        STRING;
        
        public boolean isNumeric() {
            return this == Shape.NUMBER || this == Shape.NUMBER_INT || this == Shape.NUMBER_FLOAT;
        }
    }
    
    public static class Value
    {
        private final Locale locale;
        private final String pattern;
        private final Shape shape;
        private final TimeZone timezone;
        
        public Value() {
            this("", Shape.ANY, "", "");
        }
        
        public Value(final JsonFormat jsonFormat) {
            this(jsonFormat.pattern(), jsonFormat.shape(), jsonFormat.locale(), jsonFormat.timezone());
        }
        
        public Value(final String s, final Shape shape, final String s2, final String s3) {
            final TimeZone timeZone = null;
            Locale locale;
            if (s2 == null || s2.length() == 0 || "##default".equals(s2)) {
                locale = null;
            }
            else {
                locale = new Locale(s2);
            }
            TimeZone timeZone2 = timeZone;
            if (s3 != null) {
                timeZone2 = timeZone;
                if (s3.length() != 0) {
                    if ("##default".equals(s3)) {
                        timeZone2 = timeZone;
                    }
                    else {
                        timeZone2 = TimeZone.getTimeZone(s3);
                    }
                }
            }
            this(s, shape, locale, timeZone2);
        }
        
        public Value(final String pattern, final Shape shape, final Locale locale, final TimeZone timezone) {
            this.pattern = pattern;
            this.shape = shape;
            this.locale = locale;
            this.timezone = timezone;
        }
        
        public Locale getLocale() {
            return this.locale;
        }
        
        public String getPattern() {
            return this.pattern;
        }
        
        public Shape getShape() {
            return this.shape;
        }
        
        public TimeZone getTimeZone() {
            return this.timezone;
        }
    }
}
