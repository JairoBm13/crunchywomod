// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.annotation;

import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.util.Converter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER })
public @interface JsonDeserialize {
    Class<?> as() default NoClass.class;
    
    Class<?> builder() default NoClass.class;
    
    Class<?> contentAs() default NoClass.class;
    
    Class<? extends Converter<?, ?>> contentConverter() default Converter.None.class;
    
    Class<? extends JsonDeserializer<?>> contentUsing() default JsonDeserializer.None.class;
    
    Class<? extends Converter<?, ?>> converter() default Converter.None.class;
    
    Class<?> keyAs() default NoClass.class;
    
    Class<? extends KeyDeserializer> keyUsing() default KeyDeserializer.None.class;
    
    Class<? extends JsonDeserializer<?>> using() default JsonDeserializer.None.class;
}
