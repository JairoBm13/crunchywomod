// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import com.google.inject.Binder;
import com.google.inject.internal.util.$Preconditions;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;

public final class TypeConverterBinding implements Element
{
    private final Object source;
    private final TypeConverter typeConverter;
    private final Matcher<? super TypeLiteral<?>> typeMatcher;
    
    public TypeConverterBinding(final Object o, final Matcher<? super TypeLiteral<?>> matcher, final TypeConverter typeConverter) {
        this.source = $Preconditions.checkNotNull(o, "source");
        this.typeMatcher = $Preconditions.checkNotNull(matcher, "typeMatcher");
        this.typeConverter = $Preconditions.checkNotNull(typeConverter, "typeConverter");
    }
    
    @Override
    public <T> T acceptVisitor(final ElementVisitor<T> elementVisitor) {
        return elementVisitor.visit(this);
    }
    
    @Override
    public void applyTo(final Binder binder) {
        binder.withSource(this.getSource()).convertToTypes(this.typeMatcher, this.typeConverter);
    }
    
    @Override
    public Object getSource() {
        return this.source;
    }
    
    public TypeConverter getTypeConverter() {
        return this.typeConverter;
    }
    
    public Matcher<? super TypeLiteral<?>> getTypeMatcher() {
        return this.typeMatcher;
    }
    
    @Override
    public String toString() {
        return this.typeConverter + " which matches " + this.typeMatcher + " (bound at " + this.source + ")";
    }
}
