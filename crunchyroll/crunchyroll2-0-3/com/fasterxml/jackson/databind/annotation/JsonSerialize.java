// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.annotation;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER })
public @interface JsonSerialize {
    Class<?> as() default NoClass.class;
    
    Class<?> contentAs() default NoClass.class;
    
    Class<? extends Converter<?, ?>> contentConverter() default Converter.None.class;
    
    Class<? extends JsonSerializer<?>> contentUsing() default JsonSerializer.None.class;
    
    Class<? extends Converter<?, ?>> converter() default Converter.None.class;
    
    @Deprecated
    Inclusion include() default Inclusion.ALWAYS;
    
    Class<?> keyAs() default NoClass.class;
    
    Class<? extends JsonSerializer<?>> keyUsing() default JsonSerializer.None.class;
    
    Typing typing() default Typing.DYNAMIC;
    
    Class<? extends JsonSerializer<?>> using() default JsonSerializer.None.class;
    
    public enum Inclusion
    {
        ALWAYS, 
        NON_DEFAULT, 
        NON_EMPTY, 
        NON_NULL;
    }
    
    public enum Typing
    {
        DYNAMIC, 
        STATIC;
    }
}
