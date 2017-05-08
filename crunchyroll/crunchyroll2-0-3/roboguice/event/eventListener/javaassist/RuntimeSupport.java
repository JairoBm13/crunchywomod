// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.event.eventListener.javaassist;

import java.lang.reflect.Method;

public class RuntimeSupport
{
    private static void makeDesc(final StringBuffer sb, final Class clazz) {
        if (clazz.isArray()) {
            sb.append('[');
            makeDesc(sb, clazz.getComponentType());
            return;
        }
        if (!clazz.isPrimitive()) {
            sb.append('L').append(clazz.getName().replace('.', '/')).append(';');
            return;
        }
        if (clazz == Void.TYPE) {
            sb.append('V');
            return;
        }
        if (clazz == Integer.TYPE) {
            sb.append('I');
            return;
        }
        if (clazz == Byte.TYPE) {
            sb.append('B');
            return;
        }
        if (clazz == Long.TYPE) {
            sb.append('J');
            return;
        }
        if (clazz == Double.TYPE) {
            sb.append('D');
            return;
        }
        if (clazz == Float.TYPE) {
            sb.append('F');
            return;
        }
        if (clazz == Character.TYPE) {
            sb.append('C');
            return;
        }
        if (clazz == Short.TYPE) {
            sb.append('S');
            return;
        }
        if (clazz == Boolean.TYPE) {
            sb.append('Z');
            return;
        }
        throw new RuntimeException("bad type: " + clazz.getName());
    }
    
    public static String makeDescriptor(final Method method) {
        return makeDescriptor(method.getParameterTypes(), method.getReturnType());
    }
    
    public static String makeDescriptor(final Class[] array, final Class clazz) {
        final StringBuffer sb = new StringBuffer();
        sb.append('(');
        for (int i = 0; i < array.length; ++i) {
            makeDesc(sb, array[i]);
        }
        sb.append(')');
        makeDesc(sb, clazz);
        return sb.toString();
    }
}
