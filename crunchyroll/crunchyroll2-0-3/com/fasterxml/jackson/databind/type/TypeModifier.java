// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.type;

import java.lang.reflect.Type;
import com.fasterxml.jackson.databind.JavaType;

public abstract class TypeModifier
{
    public abstract JavaType modifyType(final JavaType p0, final Type p1, final TypeBindings p2, final TypeFactory p3);
}
