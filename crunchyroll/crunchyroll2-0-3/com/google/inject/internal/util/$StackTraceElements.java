// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;

public class $StackTraceElements
{
    public static Object forMember(final Member member) {
        if (member == null) {
            return $SourceProvider.UNKNOWN_SOURCE;
        }
        final Class<?> declaringClass = member.getDeclaringClass();
        String name;
        if ($Classes.memberType(member) == Constructor.class) {
            name = "<init>";
        }
        else {
            name = member.getName();
        }
        return new StackTraceElement(declaringClass.getName(), name, null, -1);
    }
    
    public static Object forType(final Class<?> clazz) {
        return new StackTraceElement(clazz.getName(), "class", null, -1);
    }
}
