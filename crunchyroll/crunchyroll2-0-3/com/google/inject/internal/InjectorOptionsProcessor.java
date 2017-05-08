// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.spi.RequireExplicitBindingsOption;
import com.google.inject.spi.DisableCircularProxiesOption;
import com.google.inject.internal.util.$Preconditions;
import com.google.inject.Stage;

class InjectorOptionsProcessor extends AbstractProcessor
{
    private boolean disableCircularProxies;
    private boolean jitDisabled;
    
    InjectorOptionsProcessor(final Errors errors) {
        super(errors);
        this.disableCircularProxies = false;
        this.jitDisabled = false;
    }
    
    InjectorImpl.InjectorOptions getOptions(final Stage stage, final InjectorImpl.InjectorOptions injectorOptions) {
        boolean b = false;
        $Preconditions.checkNotNull(stage, "stage must be set");
        if (injectorOptions == null) {
            return new InjectorImpl.InjectorOptions(stage, this.jitDisabled, this.disableCircularProxies);
        }
        $Preconditions.checkState(stage == injectorOptions.stage, (Object)"child & parent stage don't match");
        final boolean b2 = this.jitDisabled || injectorOptions.jitDisabled;
        if (this.disableCircularProxies || injectorOptions.disableCircularProxies) {
            b = true;
        }
        return new InjectorImpl.InjectorOptions(stage, b2, b);
    }
    
    @Override
    public Boolean visit(final DisableCircularProxiesOption disableCircularProxiesOption) {
        this.disableCircularProxies = true;
        return true;
    }
    
    @Override
    public Boolean visit(final RequireExplicitBindingsOption requireExplicitBindingsOption) {
        this.jitDisabled = true;
        return true;
    }
}
