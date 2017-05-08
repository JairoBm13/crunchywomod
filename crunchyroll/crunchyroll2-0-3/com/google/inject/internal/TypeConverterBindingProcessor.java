// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.spi.TypeConverterBinding;
import com.google.inject.internal.util.$SourceProvider;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.google.inject.internal.util.$Strings;
import java.lang.reflect.Type;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeConverter;

final class TypeConverterBindingProcessor extends AbstractProcessor
{
    TypeConverterBindingProcessor(final Errors errors) {
        super(errors);
    }
    
    private <T> void convertToClass(final Class<T> clazz, final TypeConverter typeConverter) {
        this.convertToClasses(Matchers.identicalTo(clazz), typeConverter);
    }
    
    private void convertToClasses(final Matcher<? super Class<?>> matcher, final TypeConverter typeConverter) {
        this.internalConvertToTypes(new AbstractMatcher<TypeLiteral<?>>() {
            @Override
            public boolean matches(final TypeLiteral<?> typeLiteral) {
                final Type type = typeLiteral.getType();
                return type instanceof Class && matcher.matches(type);
            }
            
            @Override
            public String toString() {
                return matcher.toString();
            }
        }, typeConverter);
    }
    
    private <T> void convertToPrimitiveType(final Class<T> clazz, final Class<T> clazz2) {
        try {
            this.convertToClass(clazz2, new TypeConverter() {
                final /* synthetic */ Method val$parser = clazz2.getMethod("parse" + $Strings.capitalize(clazz.getName()), String.class);
                
                @Override
                public Object convert(final String s, final TypeLiteral<?> typeLiteral) {
                    try {
                        return this.val$parser.invoke(null, s);
                    }
                    catch (IllegalAccessException ex) {
                        throw new AssertionError((Object)ex);
                    }
                    catch (InvocationTargetException ex2) {
                        throw new RuntimeException(ex2.getTargetException().getMessage());
                    }
                }
                
                @Override
                public String toString() {
                    return "TypeConverter<" + clazz2.getSimpleName() + ">";
                }
            });
        }
        catch (NoSuchMethodException ex) {
            throw new AssertionError((Object)ex);
        }
    }
    
    private void internalConvertToTypes(final Matcher<? super TypeLiteral<?>> matcher, final TypeConverter typeConverter) {
        this.injector.state.addConverter(new TypeConverterBinding($SourceProvider.UNKNOWN_SOURCE, matcher, typeConverter));
    }
    
    void prepareBuiltInConverters(final InjectorImpl injector) {
        this.injector = injector;
        try {
            this.convertToPrimitiveType(Integer.TYPE, Integer.class);
            this.convertToPrimitiveType(Long.TYPE, Long.class);
            this.convertToPrimitiveType(Boolean.TYPE, Boolean.class);
            this.convertToPrimitiveType(Byte.TYPE, Byte.class);
            this.convertToPrimitiveType(Short.TYPE, Short.class);
            this.convertToPrimitiveType(Float.TYPE, Float.class);
            this.convertToPrimitiveType(Double.TYPE, Double.class);
            this.convertToClass(Character.class, new TypeConverter() {
                @Override
                public Object convert(String trim, final TypeLiteral<?> typeLiteral) {
                    trim = trim.trim();
                    if (trim.length() != 1) {
                        throw new RuntimeException("Length != 1.");
                    }
                    return trim.charAt(0);
                }
                
                @Override
                public String toString() {
                    return "TypeConverter<Character>";
                }
            });
            this.convertToClasses(Matchers.subclassesOf(Enum.class), new TypeConverter() {
                @Override
                public Object convert(final String s, final TypeLiteral<?> typeLiteral) {
                    return Enum.valueOf(typeLiteral.getRawType(), s);
                }
                
                @Override
                public String toString() {
                    return "TypeConverter<E extends Enum<E>>";
                }
            });
            this.internalConvertToTypes(new AbstractMatcher<TypeLiteral<?>>() {
                @Override
                public boolean matches(final TypeLiteral<?> typeLiteral) {
                    return typeLiteral.getRawType() == Class.class;
                }
                
                @Override
                public String toString() {
                    return "Class<?>";
                }
            }, new TypeConverter() {
                @Override
                public Object convert(final String s, final TypeLiteral<?> typeLiteral) {
                    try {
                        return Class.forName(s);
                    }
                    catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex.getMessage());
                    }
                }
                
                @Override
                public String toString() {
                    return "TypeConverter<Class<?>>";
                }
            });
        }
        finally {
            this.injector = null;
        }
    }
    
    @Override
    public Boolean visit(final TypeConverterBinding typeConverterBinding) {
        this.injector.state.addConverter(new TypeConverterBinding(typeConverterBinding.getSource(), typeConverterBinding.getTypeMatcher(), typeConverterBinding.getTypeConverter()));
        return true;
    }
}
