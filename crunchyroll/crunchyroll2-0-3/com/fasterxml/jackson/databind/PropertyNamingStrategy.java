// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import java.io.Serializable;

public abstract class PropertyNamingStrategy implements Serializable
{
    public static final PropertyNamingStrategy CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES;
    public static final PropertyNamingStrategy PASCAL_CASE_TO_CAMEL_CASE;
    
    static {
        CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES = new LowerCaseWithUnderscoresStrategy();
        PASCAL_CASE_TO_CAMEL_CASE = new PascalCaseStrategy();
    }
    
    public String nameForConstructorParameter(final MapperConfig<?> mapperConfig, final AnnotatedParameter annotatedParameter, final String s) {
        return s;
    }
    
    public String nameForField(final MapperConfig<?> mapperConfig, final AnnotatedField annotatedField, final String s) {
        return s;
    }
    
    public String nameForGetterMethod(final MapperConfig<?> mapperConfig, final AnnotatedMethod annotatedMethod, final String s) {
        return s;
    }
    
    public String nameForSetterMethod(final MapperConfig<?> mapperConfig, final AnnotatedMethod annotatedMethod, final String s) {
        return s;
    }
    
    public static class LowerCaseWithUnderscoresStrategy extends PropertyNamingStrategyBase
    {
        @Override
        public String translate(final String s) {
            if (s != null) {
                final int length = s.length();
                final StringBuilder sb = new StringBuilder(length * 2);
                int i = 0;
                int n = 0;
                int n2 = 0;
                while (i < length) {
                    char c = s.charAt(i);
                    int n4;
                    int n6;
                    if (i > 0 || c != '_') {
                        int n5;
                        if (Character.isUpperCase(c)) {
                            int n3 = n2;
                            if (n == 0 && (n3 = n2) > 0) {
                                n3 = n2;
                                if (sb.charAt(n2 - 1) != '_') {
                                    sb.append('_');
                                    n3 = n2 + 1;
                                }
                            }
                            c = Character.toLowerCase(c);
                            n4 = 1;
                            n5 = n3;
                        }
                        else {
                            n5 = n2;
                            n4 = 0;
                        }
                        sb.append(c);
                        n6 = n5 + 1;
                    }
                    else {
                        final int n7 = n;
                        n6 = n2;
                        n4 = n7;
                    }
                    ++i;
                    final int n8 = n4;
                    n2 = n6;
                    n = n8;
                }
                if (n2 > 0) {
                    return sb.toString();
                }
            }
            return s;
        }
    }
    
    public static class PascalCaseStrategy extends PropertyNamingStrategyBase
    {
        @Override
        public String translate(final String s) {
            if (s != null && s.length() != 0) {
                final char char1 = s.charAt(0);
                if (!Character.isUpperCase(char1)) {
                    final StringBuilder sb = new StringBuilder(s);
                    sb.setCharAt(0, Character.toUpperCase(char1));
                    return sb.toString();
                }
            }
            return s;
        }
    }
    
    public abstract static class PropertyNamingStrategyBase extends PropertyNamingStrategy
    {
        @Override
        public String nameForConstructorParameter(final MapperConfig<?> mapperConfig, final AnnotatedParameter annotatedParameter, final String s) {
            return this.translate(s);
        }
        
        @Override
        public String nameForField(final MapperConfig<?> mapperConfig, final AnnotatedField annotatedField, final String s) {
            return this.translate(s);
        }
        
        @Override
        public String nameForGetterMethod(final MapperConfig<?> mapperConfig, final AnnotatedMethod annotatedMethod, final String s) {
            return this.translate(s);
        }
        
        @Override
        public String nameForSetterMethod(final MapperConfig<?> mapperConfig, final AnnotatedMethod annotatedMethod, final String s) {
            return this.translate(s);
        }
        
        public abstract String translate(final String p0);
    }
}
