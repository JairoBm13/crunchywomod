// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.annotation;

import java.lang.reflect.Modifier;
import java.lang.reflect.Member;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE })
public @interface JsonAutoDetect {
    Visibility creatorVisibility() default Visibility.DEFAULT;
    
    Visibility fieldVisibility() default Visibility.DEFAULT;
    
    Visibility getterVisibility() default Visibility.DEFAULT;
    
    Visibility isGetterVisibility() default Visibility.DEFAULT;
    
    Visibility setterVisibility() default Visibility.DEFAULT;
    
    public enum Visibility
    {
        ANY, 
        DEFAULT, 
        NONE, 
        NON_PRIVATE, 
        PROTECTED_AND_PUBLIC, 
        PUBLIC_ONLY;
        
        public boolean isVisible(final Member member) {
            boolean b2;
            final boolean b = b2 = true;
            switch (this) {
                default: {
                    b2 = false;
                    return b2;
                }
                case ANY: {
                    return b2;
                }
                case NONE: {
                    return false;
                }
                case NON_PRIVATE: {
                    b2 = b;
                    if (Modifier.isPrivate(member.getModifiers())) {
                        return false;
                    }
                    return b2;
                }
                case PROTECTED_AND_PUBLIC: {
                    b2 = b;
                    if (!Modifier.isProtected(member.getModifiers())) {
                        return Modifier.isPublic(member.getModifiers());
                    }
                    return b2;
                }
                case PUBLIC_ONLY: {
                    return Modifier.isPublic(member.getModifiers());
                }
            }
        }
    }
}
