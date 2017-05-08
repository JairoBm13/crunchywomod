// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import com.google.inject.BindingAnnotation;
import java.lang.annotation.Annotation;

@BindingAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
public @interface SharedPreferencesName {
}
