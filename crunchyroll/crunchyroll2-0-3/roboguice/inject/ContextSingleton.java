// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import com.google.inject.ScopeAnnotation;
import java.lang.annotation.Annotation;

@ScopeAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
public @interface ContextSingleton {
}
