// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;

public class MinimalClassNameIdResolver extends ClassNameIdResolver
{
    protected final String _basePackageName;
    protected final String _basePackagePrefix;
    
    protected MinimalClassNameIdResolver(final JavaType javaType, final TypeFactory typeFactory) {
        super(javaType, typeFactory);
        final String name = javaType.getRawClass().getName();
        final int lastIndex = name.lastIndexOf(46);
        if (lastIndex < 0) {
            this._basePackageName = "";
            this._basePackagePrefix = ".";
            return;
        }
        this._basePackagePrefix = name.substring(0, lastIndex + 1);
        this._basePackageName = name.substring(0, lastIndex);
    }
    
    @Override
    public String idFromValue(final Object o) {
        String s2;
        final String s = s2 = o.getClass().getName();
        if (s.startsWith(this._basePackagePrefix)) {
            s2 = s.substring(this._basePackagePrefix.length() - 1);
        }
        return s2;
    }
    
    @Override
    public JavaType typeFromId(final String s) {
        String string = s;
        if (s.startsWith(".")) {
            final StringBuilder sb = new StringBuilder(s.length() + this._basePackageName.length());
            if (this._basePackageName.length() == 0) {
                sb.append(s.substring(1));
            }
            else {
                sb.append(this._basePackageName).append(s);
            }
            string = sb.toString();
        }
        return super.typeFromId(string);
    }
}
