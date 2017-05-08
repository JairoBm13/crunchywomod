// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.Stage;
import com.google.inject.internal.util.$ImmutableList;
import com.google.inject.spi.StaticInjectionRequest;
import com.google.inject.ConfigurationException;
import com.google.inject.spi.InjectionPoint;
import java.util.Set;
import com.google.inject.spi.InjectionRequest;
import java.util.Iterator;
import com.google.inject.internal.util.$Lists;
import java.util.List;

final class InjectionRequestProcessor extends AbstractProcessor
{
    private final Initializer initializer;
    private final List<StaticInjection> staticInjections;
    
    InjectionRequestProcessor(final Errors errors, final Initializer initializer) {
        super(errors);
        this.staticInjections = (List<StaticInjection>)$Lists.newArrayList();
        this.initializer = initializer;
    }
    
    void injectMembers() {
        final Iterator<StaticInjection> iterator = this.staticInjections.iterator();
        while (iterator.hasNext()) {
            iterator.next().injectMembers();
        }
    }
    
    void validate() {
        final Iterator<StaticInjection> iterator = this.staticInjections.iterator();
        while (iterator.hasNext()) {
            iterator.next().validate();
        }
    }
    
    @Override
    public Boolean visit(final InjectionRequest<?> injectionRequest) {
        while (true) {
            try {
                final Set<InjectionPoint> injectionPoints = injectionRequest.getInjectionPoints();
                this.initializer.requestInjection(this.injector, injectionRequest.getInstance(), injectionRequest.getSource(), injectionPoints);
                return true;
            }
            catch (ConfigurationException ex) {
                this.errors.merge(ex.getErrorMessages());
                final Set<InjectionPoint> injectionPoints = ex.getPartialValue();
                continue;
            }
            break;
        }
    }
    
    @Override
    public Boolean visit(final StaticInjectionRequest staticInjectionRequest) {
        this.staticInjections.add(new StaticInjection(this.injector, staticInjectionRequest));
        return true;
    }
    
    private class StaticInjection
    {
        final InjectorImpl injector;
        $ImmutableList<SingleMemberInjector> memberInjectors;
        final StaticInjectionRequest request;
        final Object source;
        
        public StaticInjection(final InjectorImpl injector, final StaticInjectionRequest request) {
            this.injector = injector;
            this.source = request.getSource();
            this.request = request;
        }
        
        void injectMembers() {
            try {
                this.injector.callInContext((ContextualCallable<Object>)new ContextualCallable<Void>() {
                    @Override
                    public Void call(final InternalContext internalContext) {
                        for (final SingleMemberInjector singleMemberInjector : StaticInjection.this.memberInjectors) {
                            if (StaticInjection.this.injector.options.stage != Stage.TOOL || singleMemberInjector.getInjectionPoint().isToolable()) {
                                singleMemberInjector.inject(InjectionRequestProcessor.this.errors, internalContext, null);
                            }
                        }
                        return null;
                    }
                });
            }
            catch (ErrorsException ex) {
                throw new AssertionError();
            }
        }
        
        void validate() {
            final Errors withSource = InjectionRequestProcessor.this.errors.withSource(this.source);
            while (true) {
                try {
                    final Set<InjectionPoint> injectionPoints = this.request.getInjectionPoints();
                    this.memberInjectors = this.injector.membersInjectorStore.getInjectors(injectionPoints, withSource);
                }
                catch (ConfigurationException ex) {
                    InjectionRequestProcessor.this.errors.merge(ex.getErrorMessages());
                    final Set<InjectionPoint> injectionPoints = ex.getPartialValue();
                    continue;
                }
                break;
            }
        }
    }
}
