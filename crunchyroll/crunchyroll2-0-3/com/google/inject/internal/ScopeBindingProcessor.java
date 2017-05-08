// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.Scope;
import com.google.inject.internal.util.$Preconditions;
import java.lang.annotation.Annotation;
import com.google.inject.spi.ScopeBinding;

final class ScopeBindingProcessor extends AbstractProcessor
{
    ScopeBindingProcessor(final Errors errors) {
        super(errors);
    }
    
    @Override
    public Boolean visit(final ScopeBinding scopeBinding) {
        final Scope scope = scopeBinding.getScope();
        final Class<? extends Annotation> annotationType = scopeBinding.getAnnotationType();
        if (!Annotations.isScopeAnnotation(annotationType)) {
            this.errors.withSource(annotationType).missingScopeAnnotation();
        }
        if (!Annotations.isRetainedAtRuntime(annotationType)) {
            this.errors.withSource(annotationType).missingRuntimeRetention(scopeBinding.getSource());
        }
        final Scope scope2 = this.injector.state.getScope($Preconditions.checkNotNull(annotationType, "annotation type"));
        if (scope2 != null) {
            this.errors.duplicateScopes(scope2, annotationType, scope);
        }
        else {
            this.injector.state.putAnnotation(annotationType, $Preconditions.checkNotNull(scope, "scope"));
        }
        return true;
    }
}
