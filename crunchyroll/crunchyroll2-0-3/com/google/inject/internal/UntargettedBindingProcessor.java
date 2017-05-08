// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.Key;
import com.google.inject.spi.UntargettedBinding;
import com.google.inject.Binding;

class UntargettedBindingProcessor extends AbstractBindingProcessor
{
    UntargettedBindingProcessor(final Errors errors, final ProcessedBindingData processedBindingData) {
        super(errors, processedBindingData);
    }
    
    @Override
    public <T> Boolean visit(final Binding<T> binding) {
        return binding.acceptTargetVisitor((BindingTargetVisitor<? super Object, Boolean>)new Processor<T, Boolean>((BindingImpl<Object>)binding) {
            @Override
            public Boolean visit(final UntargettedBinding<? extends T> untargettedBinding) {
                ((Processor)this).prepareBinding();
                if (this.key.getAnnotationType() != null) {
                    UntargettedBindingProcessor.this.errors.missingImplementation(this.key);
                    UntargettedBindingProcessor.this.putBinding(UntargettedBindingProcessor.this.invalidBinding(UntargettedBindingProcessor.this.injector, this.key, this.source));
                    return true;
                }
                try {
                    final BindingImpl<T> uninitializedBinding = UntargettedBindingProcessor.this.injector.createUninitializedBinding(this.key, this.scoping, this.source, UntargettedBindingProcessor.this.errors, false);
                    ((Processor)this).scheduleInitialization(uninitializedBinding);
                    UntargettedBindingProcessor.this.putBinding(uninitializedBinding);
                    return true;
                }
                catch (ErrorsException ex) {
                    UntargettedBindingProcessor.this.errors.merge(ex.getErrors());
                    UntargettedBindingProcessor.this.putBinding(UntargettedBindingProcessor.this.invalidBinding(UntargettedBindingProcessor.this.injector, this.key, this.source));
                    return true;
                }
            }
            
            @Override
            protected Boolean visitOther(final Binding<? extends T> binding) {
                return false;
            }
        });
    }
}
