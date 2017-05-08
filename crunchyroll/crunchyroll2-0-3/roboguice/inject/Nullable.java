// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import java.lang.annotation.Annotation;
import roboguice.util.Strings;
import java.lang.reflect.Field;

public class Nullable
{
    public static boolean isNullable(final Field field) {
        final Annotation[] annotations = field.getAnnotations();
        for (int length = annotations.length, i = 0; i < length; ++i) {
            if (Strings.equals("Nullable", annotations[i].annotationType().getSimpleName())) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean notNullable(final Field field) {
        return !isNullable(field);
    }
}
