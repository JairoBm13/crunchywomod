// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.spi.ProviderBinding;
import java.lang.reflect.InvocationTargetException;
import com.google.inject.Stage;
import com.google.inject.internal.util.$Objects;
import com.google.inject.Binder;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.ConvertedConstantBinding;
import com.google.inject.internal.util.$Lists;
import java.util.Collections;
import com.google.inject.internal.util.$ToStringBuilder;
import java.util.HashSet;
import com.google.inject.Scope;
import java.lang.annotation.Annotation;
import com.google.inject.ProvisionException;
import com.google.inject.spi.Message;
import com.google.inject.ConfigurationException;
import com.google.inject.internal.util.$ImmutableMap;
import java.util.List;
import com.google.inject.ProvidedBy;
import com.google.inject.internal.util.$ImmutableList;
import com.google.inject.Module;
import com.google.inject.spi.HasDependencies;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import com.google.inject.internal.util.$ImmutableSet;
import com.google.inject.internal.util.$SourceProvider;
import java.lang.reflect.ParameterizedType;
import com.google.inject.MembersInjector;
import com.google.inject.Provider;
import com.google.inject.ImplementedBy;
import com.google.inject.spi.TypeConverterBinding;
import com.google.inject.Binding;
import com.google.inject.spi.InjectionPoint;
import java.util.Iterator;
import com.google.inject.spi.Dependency;
import java.util.Set;
import com.google.inject.internal.util.$Maps;
import com.google.inject.internal.util.$Nullable;
import com.google.inject.Key;
import java.util.Map;
import com.google.inject.TypeLiteral;
import com.google.inject.Injector;

final class InjectorImpl implements Injector, Lookups
{
    public static final TypeLiteral<String> STRING_TYPE;
    final BindingsMultimap bindingsMultimap;
    final ConstructorInjectorStore constructors;
    final Map<Key<?>, BindingImpl<?>> jitBindings;
    final ThreadLocal<Object[]> localContext;
    Lookups lookups;
    MembersInjectorStore membersInjectorStore;
    final InjectorOptions options;
    final InjectorImpl parent;
    final State state;
    
    static {
        STRING_TYPE = TypeLiteral.get(String.class);
    }
    
    InjectorImpl(@$Nullable final InjectorImpl parent, final State state, final InjectorOptions options) {
        this.bindingsMultimap = new BindingsMultimap();
        this.jitBindings = (Map<Key<?>, BindingImpl<?>>)$Maps.newHashMap();
        this.lookups = new DeferredLookups(this);
        this.constructors = new ConstructorInjectorStore(this);
        this.parent = parent;
        this.state = state;
        this.options = options;
        if (parent != null) {
            this.localContext = parent.localContext;
            return;
        }
        this.localContext = new ThreadLocal<Object[]>() {
            @Override
            protected Object[] initialValue() {
                return new Object[1];
            }
        };
    }
    
    private boolean cleanup(final BindingImpl<?> bindingImpl, final Set<Key> set) {
        boolean b = false;
        for (final Dependency<T> dependency : this.getInternalDependencies(bindingImpl)) {
            final Key<T> key = dependency.getKey();
            InjectionPoint injectionPoint = dependency.getInjectionPoint();
            if (set.add(key)) {
                final BindingImpl<?> bindingImpl2 = this.jitBindings.get(key);
                if (bindingImpl2 != null) {
                    boolean cleanup = this.cleanup(bindingImpl2, set);
                    if (bindingImpl2 instanceof ConstructorBindingImpl) {
                        final ConstructorBindingImpl<?> constructorBindingImpl = (ConstructorBindingImpl<?>)bindingImpl2;
                        final InjectionPoint internalConstructor = constructorBindingImpl.getInternalConstructor();
                        cleanup = cleanup;
                        injectionPoint = internalConstructor;
                        if (!constructorBindingImpl.isInitialized()) {
                            cleanup = true;
                            injectionPoint = internalConstructor;
                        }
                    }
                    if (!cleanup) {
                        continue;
                    }
                    this.removeFailedJitBinding(key, injectionPoint);
                    b = true;
                }
                else {
                    if (this.state.getExplicitBinding((Key<Object>)key) != null) {
                        continue;
                    }
                    b = true;
                }
            }
        }
        return b;
    }
    
    private <T> BindingImpl<T> convertConstantStringBinding(final Key<T> key, final Errors errors) throws ErrorsException {
        final BindingImpl<String> explicitBinding = this.state.getExplicitBinding(key.ofType(InjectorImpl.STRING_TYPE));
        if (explicitBinding == null || !explicitBinding.isConstant()) {
            return null;
        }
        final String s = explicitBinding.getProvider().get();
        final Object source = explicitBinding.getSource();
        final TypeLiteral<T> typeLiteral = key.getTypeLiteral();
        final TypeConverterBinding converter = this.state.getConverter(s, typeLiteral, errors, source);
        if (converter == null) {
            return null;
        }
        Object convert;
        try {
            convert = converter.getTypeConverter().convert(s, typeLiteral);
            if (convert == null) {
                throw errors.converterReturnedNull(s, source, typeLiteral, converter).toException();
            }
            goto Label_0124;
        }
        catch (ErrorsException ex) {
            throw ex;
        }
        catch (RuntimeException ex2) {
            throw errors.conversionError(s, source, typeLiteral, converter, ex2).toException();
        }
        return new ConvertedConstantBindingImpl<T>(this, (Key<Object>)key, convert, explicitBinding, converter);
    }
    
    private <T> BindingImpl<T> createImplementedByBinding(final Key<T> key, final Scoping scoping, final ImplementedBy implementedBy, final Errors errors) throws ErrorsException {
        final Class<? super T> rawType = key.getTypeLiteral().getRawType();
        final Class<?> value = implementedBy.value();
        if (value == rawType) {
            throw errors.recursiveImplementationType().toException();
        }
        if (!rawType.isAssignableFrom(value)) {
            throw errors.notASubtype(value, rawType).toException();
        }
        final Key<T> value2 = Key.get(value);
        return new LinkedBindingImpl<T>(this, (Key<Object>)key, rawType, Scoping.scope(key, this, (InternalFactory<? extends T>)new InternalFactory<T>() {
            final /* synthetic */ BindingImpl val$targetBinding = InjectorImpl.this.getBindingOrThrow((Key<Object>)value2, errors, JitLimitation.NEW_OR_EXISTING_JIT);
            
            @Override
            public T get(final Errors errors, final InternalContext internalContext, final Dependency<?> dependency, final boolean b) throws ErrorsException {
                return (T)this.val$targetBinding.getInternalFactory().get(errors.withSource(value2), internalContext, dependency, true);
            }
        }, rawType, scoping), scoping, value2);
    }
    
    private <T> BindingImpl<T> createJustInTimeBinding(final Key<T> key, final Errors errors, final boolean b, final JitLimitation jitLimitation) throws ErrorsException {
        final int size = errors.size();
        if (this.state.isBlacklisted(key)) {
            throw errors.childBindingAlreadySet(key, this.state.getSourcesForBlacklistedKey(key)).toException();
        }
        if (isProvider(key)) {
            return (BindingImpl<T>)this.createProviderBinding((Key<Provider<Object>>)key, errors);
        }
        if (isMembersInjector(key)) {
            return (BindingImpl<T>)this.createMembersInjectorBinding((Key<MembersInjector<Object>>)key, errors);
        }
        final BindingImpl<T> convertConstantStringBinding = this.convertConstantStringBinding(key, errors);
        if (convertConstantStringBinding != null) {
            return convertConstantStringBinding;
        }
        if (!isTypeLiteral(key) && b && jitLimitation != JitLimitation.NEW_OR_EXISTING_JIT) {
            throw errors.jitDisabled(key).toException();
        }
        if (key.getAnnotationType() != null) {
            if (key.hasAttributes()) {
                try {
                    return (BindingImpl<T>)this.getBindingOrThrow((Key<?>)key.withoutAttributes(), new Errors(), JitLimitation.NO_JIT);
                }
                catch (ErrorsException ex) {}
            }
            throw errors.missingImplementation(key).toException();
        }
        final BindingImpl<T> uninitializedBinding = this.createUninitializedBinding((Key<T>)key, Scoping.UNSCOPED, key.getTypeLiteral().getRawType(), errors, true);
        errors.throwIfNewErrors(size);
        this.initializeJitBinding(uninitializedBinding, errors);
        return (BindingImpl<T>)uninitializedBinding;
    }
    
    private <T> BindingImpl<T> createJustInTimeBindingRecursive(final Key<T> key, final Errors errors, final boolean b, final JitLimitation jitLimitation) throws ErrorsException {
        if (this.parent != null) {
            try {
                final InjectorImpl parent = this.parent;
                final Errors errors2 = new Errors();
                JitLimitation no_JIT;
                if (this.parent.options.jitDisabled) {
                    no_JIT = JitLimitation.NO_JIT;
                }
                else {
                    no_JIT = jitLimitation;
                }
                return (BindingImpl<T>)parent.createJustInTimeBindingRecursive((Key<Object>)key, errors2, b, no_JIT);
            }
            catch (ErrorsException ex) {}
        }
        if (this.state.isBlacklisted(key)) {
            throw errors.childBindingAlreadySet(key, this.state.getSourcesForBlacklistedKey(key)).toException();
        }
        final BindingImpl<Object> justInTimeBinding = this.createJustInTimeBinding((Key<Object>)key, errors, b, jitLimitation);
        this.state.parent().blacklist(key, justInTimeBinding.getSource());
        this.jitBindings.put(key, justInTimeBinding);
        return (BindingImpl<T>)justInTimeBinding;
    }
    
    private <T> BindingImpl<MembersInjector<T>> createMembersInjectorBinding(final Key<MembersInjector<T>> key, final Errors errors) throws ErrorsException {
        final Type type = key.getTypeLiteral().getType();
        if (!(type instanceof ParameterizedType)) {
            throw errors.cannotInjectRawMembersInjector().toException();
        }
        final MembersInjectorImpl<Object> value = this.membersInjectorStore.get(TypeLiteral.get(((ParameterizedType)type).getActualTypeArguments()[0]), errors);
        return new InstanceBindingImpl<MembersInjector<T>>(this, (Key<Object>)key, $SourceProvider.UNKNOWN_SOURCE, new ConstantFactory<Object>((Initializable<Object>)Initializables.of(value)), (Set<InjectionPoint>)$ImmutableSet.of(), value);
    }
    
    private <T> BindingImpl<Provider<T>> createProviderBinding(final Key<Provider<T>> key, final Errors errors) throws ErrorsException {
        return (BindingImpl<Provider<T>>)new ProviderBindingImpl(this, (Key<Provider<Object>>)key, this.getBindingOrThrow((Key<Object>)getProvidedKey((Key<Provider<T>>)key, errors), errors, JitLimitation.NO_JIT));
    }
    
    private <T> BindingImpl<TypeLiteral<T>> createTypeLiteralBinding(final Key<TypeLiteral<T>> key, final Errors errors) throws ErrorsException {
        final Type type = key.getTypeLiteral().getType();
        if (!(type instanceof ParameterizedType)) {
            throw errors.cannotInjectRawTypeLiteral().toException();
        }
        final Type type2 = ((ParameterizedType)type).getActualTypeArguments()[0];
        if (!(type2 instanceof Class) && !(type2 instanceof GenericArrayType) && !(type2 instanceof ParameterizedType)) {
            throw errors.cannotInjectTypeLiteralOf(type2).toException();
        }
        final TypeLiteral<?> value = TypeLiteral.get(type2);
        return new InstanceBindingImpl<TypeLiteral<T>>(this, (Key<Object>)key, $SourceProvider.UNKNOWN_SOURCE, new ConstantFactory<Object>((Initializable<Object>)Initializables.of(value)), (Set<InjectionPoint>)$ImmutableSet.of(), value);
    }
    
    private Set<Dependency<?>> getInternalDependencies(final BindingImpl<?> bindingImpl) {
        if (bindingImpl instanceof ConstructorBindingImpl) {
            return (Set<Dependency<?>>)((ConstructorBindingImpl)bindingImpl).getInternalDependencies();
        }
        if (bindingImpl instanceof HasDependencies) {
            return ((ConstructorBindingImpl<?>)bindingImpl).getDependencies();
        }
        return (Set<Dependency<?>>)$ImmutableSet.of();
    }
    
    private <T> BindingImpl<T> getJustInTimeBinding(final Key<T> key, final Errors errors, final JitLimitation jitLimitation) throws ErrorsException {
        // monitorenter(lock)
        // monitorexit(lock)
        while (true) {
            Label_0112: {
                if (!isProvider(key) && !isTypeLiteral(key) && !isMembersInjector(key)) {
                    break Label_0112;
                }
                final int n = 1;
                final Object lock = this.state.lock();
                InjectorImpl parent = this;
                while (parent != null) {
                    BindingImpl<?> bindingImpl = null;
                    Label_0118: {
                        Label_0124: {
                            try {
                                bindingImpl = parent.jitBindings.get(key);
                                if (bindingImpl == null) {
                                    break Label_0124;
                                }
                                if (this.options.jitDisabled && jitLimitation == JitLimitation.NO_JIT && n == 0 && !(bindingImpl instanceof ConvertedConstantBindingImpl)) {
                                    throw errors.jitDisabled(key).toException();
                                }
                                break Label_0118;
                            }
                            finally {
                            }
                            // monitorexit(lock)
                            break Label_0112;
                        }
                        parent = parent.parent;
                        continue;
                    }
                    // monitorexit(lock)
                    return (BindingImpl<T>)bindingImpl;
                }
                return this.createJustInTimeBindingRecursive(key, errors, this.options.jitDisabled, jitLimitation);
            }
            final int n = 0;
            continue;
        }
    }
    
    private static <T> Key<T> getProvidedKey(final Key<Provider<T>> key, final Errors errors) throws ErrorsException {
        final Type type = key.getTypeLiteral().getType();
        if (!(type instanceof ParameterizedType)) {
            throw errors.cannotInjectRawProvider().toException();
        }
        return (Key<T>)key.ofType(((ParameterizedType)type).getActualTypeArguments()[0]);
    }
    
    private static boolean isMembersInjector(final Key<?> key) {
        return key.getTypeLiteral().getRawType().equals(MembersInjector.class) && key.getAnnotationType() == null;
    }
    
    private static boolean isProvider(final Key<?> key) {
        return key.getTypeLiteral().getRawType().equals(Provider.class);
    }
    
    private static boolean isTypeLiteral(final Key<?> key) {
        return key.getTypeLiteral().getRawType().equals(TypeLiteral.class);
    }
    
    private void removeFailedJitBinding(final Key<?> key, final InjectionPoint injectionPoint) {
        this.jitBindings.remove(key);
        this.membersInjectorStore.remove(key.getTypeLiteral());
        if (injectionPoint != null) {
            this.constructors.remove(injectionPoint);
        }
    }
    
     <T> T callInContext(final ContextualCallable<T> contextualCallable) throws ErrorsException {
        final Object[] array = this.localContext.get();
        if (array[0] == null) {
            array[0] = new InternalContext();
            try {
                return contextualCallable.call((InternalContext)array[0]);
            }
            finally {
                array[0] = null;
            }
        }
        return contextualCallable.call((InternalContext)array[0]);
    }
    
    @Override
    public Injector createChildInjector(final Iterable<? extends Module> iterable) {
        return new InternalInjectorCreator().parentInjector(this).addModules(iterable).build();
    }
    
    @Override
    public Injector createChildInjector(final Module... array) {
        return this.createChildInjector($ImmutableList.of(array));
    }
    
     <T> SingleParameterInjector<T> createParameterInjector(final Dependency<T> dependency, final Errors errors) throws ErrorsException {
        return new SingleParameterInjector<T>(dependency, this.getInternalFactory(dependency.getKey(), errors, JitLimitation.NO_JIT));
    }
    
     <T> BindingImpl<T> createProvidedByBinding(final Key<T> key, final Scoping scoping, final ProvidedBy providedBy, final Errors errors) throws ErrorsException {
        final Class<? super T> rawType = key.getTypeLiteral().getRawType();
        final Class<? extends Provider<?>> value = providedBy.value();
        if (value == rawType) {
            throw errors.recursiveProviderType().toException();
        }
        final Key<Object> value2 = Key.get((Class<Object>)value);
        return new LinkedProviderBindingImpl<T>(this, (Key<Object>)key, rawType, Scoping.scope(key, this, (InternalFactory<? extends T>)new InternalFactory<T>() {
            final /* synthetic */ BindingImpl val$providerBinding = InjectorImpl.this.getBindingOrThrow(value2, errors, JitLimitation.NEW_OR_EXISTING_JIT);
            
            @Override
            public T get(Errors withSource, final InternalContext internalContext, final Dependency dependency, final boolean b) throws ErrorsException {
                withSource = withSource.withSource(value2);
                final Provider provider = (Provider)this.val$providerBinding.getInternalFactory().get(withSource, internalContext, dependency, true);
                T value;
                try {
                    value = provider.get();
                    if (value != null && !rawType.isInstance(value)) {
                        throw withSource.subtypeNotProvided(value, rawType).toException();
                    }
                }
                catch (RuntimeException ex) {
                    throw withSource.errorInProvider(ex).toException();
                }
                return value;
            }
        }, rawType, scoping), scoping, (Key<? extends javax.inject.Provider<?>>)value2);
    }
    
     <T> BindingImpl<T> createUninitializedBinding(final Key<T> key, final Scoping scoping, final Object o, final Errors errors, final boolean b) throws ErrorsException {
        final Class<? super TypeLiteral<Object>> rawType = key.getTypeLiteral().getRawType();
        if (rawType.isArray() || rawType.isEnum()) {
            throw errors.missingImplementation(key).toException();
        }
        if (rawType == TypeLiteral.class) {
            return (BindingImpl<T>)this.createTypeLiteralBinding((Key<TypeLiteral<Object>>)key, errors);
        }
        final ImplementedBy implementedBy = rawType.getAnnotation(ImplementedBy.class);
        if (implementedBy != null) {
            Annotations.checkForMisplacedScopeAnnotations(rawType, o, errors);
            return this.createImplementedByBinding(key, scoping, implementedBy, errors);
        }
        final ProvidedBy providedBy = rawType.getAnnotation(ProvidedBy.class);
        if (providedBy != null) {
            Annotations.checkForMisplacedScopeAnnotations(rawType, o, errors);
            return this.createProvidedByBinding(key, scoping, providedBy, errors);
        }
        return ConstructorBindingImpl.create(this, key, null, o, scoping, errors, b && this.options.jitDisabled);
    }
    
    @Override
    public <T> List<Binding<T>> findBindingsByType(final TypeLiteral<T> typeLiteral) {
        return this.bindingsMultimap.getAll(typeLiteral);
    }
    
    @Override
    public Map<Key<?>, Binding<?>> getAllBindings() {
        synchronized (this.state.lock()) {
            return new $ImmutableMap.Builder<Key<?>, Binding<?>>().putAll(this.state.getExplicitBindingsThisLevel()).putAll(this.jitBindings).build();
        }
    }
    
    @Override
    public <T> Binding<T> getBinding(final Class<T> clazz) {
        return (Binding<T>)this.getBinding((Key<Object>)Key.get((Class<T>)clazz));
    }
    
    @Override
    public <T> BindingImpl<T> getBinding(final Key<T> key) {
        final Errors errors = new Errors(key);
        try {
            final BindingImpl<T> bindingOrThrow = this.getBindingOrThrow(key, errors, JitLimitation.EXISTING_JIT);
            errors.throwConfigurationExceptionIfErrorsExist();
            return bindingOrThrow;
        }
        catch (ErrorsException ex) {
            throw new ConfigurationException(errors.merge(ex.getErrors()).getMessages());
        }
    }
    
     <T> BindingImpl<T> getBindingOrThrow(final Key<T> key, final Errors errors, final JitLimitation jitLimitation) throws ErrorsException {
        final BindingImpl<T> explicitBinding = this.state.getExplicitBinding(key);
        if (explicitBinding != null) {
            return explicitBinding;
        }
        return (BindingImpl<T>)this.getJustInTimeBinding((Key<Object>)key, errors, jitLimitation);
    }
    
    @Override
    public Map<Key<?>, Binding<?>> getBindings() {
        return this.state.getExplicitBindingsThisLevel();
    }
    
    @Override
    public <T> BindingImpl<T> getExistingBinding(Key<T> key) {
        final BindingImpl<T> explicitBinding = this.state.getExplicitBinding((Key<T>)key);
        if (explicitBinding != null) {
            return explicitBinding;
        }
        final Object lock = this.state.lock();
        // monitorenter(lock)
        InjectorImpl parent = this;
        BindingImpl<?> bindingImpl;
        InjectorImpl injectorImpl;
        Key<Provider<T>> key2;
        Errors errors;
        Key<T> key3;
        BindingImpl<Object> bindingImpl2;
        InjectorImpl injectorImpl2;
        Object o;
        BindingImpl<Object> binding;
        BindingImpl<Object> binding2;
        final Key<T> key4;
        Block_6_Outer:Label_0068_Outer:
        while (true) {
            while (true) {
                if (parent != null) {
                    try {
                        bindingImpl = parent.jitBindings.get(key);
                        if (bindingImpl != null) {
                            return (BindingImpl<T>)bindingImpl;
                        }
                        parent = parent.parent;
                        continue Block_6_Outer;
                        while (true) {
                            injectorImpl = this;
                            key2 = key;
                            errors = new Errors();
                            key3 = getProvidedKey(key2, errors);
                            bindingImpl2 = injectorImpl.getExistingBinding((Key<Object>)key3);
                            injectorImpl2 = this;
                            o = key;
                            binding = injectorImpl2.getBinding((Key<Object>)o);
                            binding2 = binding;
                            return (BindingImpl<T>)binding2;
                            continue Label_0068_Outer;
                        }
                    }
                    // iftrue(Label_0124:, bindingImpl2 == null)
                    // monitorexit(lock)
                    // iftrue(Label_0124:, !isProvider((Key<?>)key))
                    finally {
                        key = (Key<Provider<T>>)key4;
                    }
                    // monitorexit(lock)
                    try {
                        injectorImpl = this;
                        key2 = key;
                        errors = new Errors();
                        key3 = getProvidedKey(key2, errors);
                        bindingImpl2 = injectorImpl.getExistingBinding((Key<Object>)key3);
                        if (bindingImpl2 != null) {
                            injectorImpl2 = this;
                            o = key;
                            binding = (binding2 = injectorImpl2.getBinding((Key<Object>)o));
                            return (BindingImpl<T>)binding2;
                        }
                    }
                    catch (ErrorsException ex) {
                        throw new ConfigurationException(ex.getErrors().getMessages());
                    }
                    break;
                }
                continue;
            }
        }
        Label_0124: {
            return null;
        }
    }
    
    @Override
    public <T> T getInstance(final Key<T> key) {
        return this.getProvider(key).get();
    }
    
    @Override
    public <T> T getInstance(final Class<T> clazz) {
        return this.getProvider(clazz).get();
    }
    
     <T> InternalFactory<? extends T> getInternalFactory(final Key<T> key, final Errors errors, final JitLimitation jitLimitation) throws ErrorsException {
        return this.getBindingOrThrow(key, errors, jitLimitation).getInternalFactory();
    }
    
    @Override
    public <T> MembersInjector<T> getMembersInjector(final TypeLiteral<T> typeLiteral) {
        final Errors errors = new Errors(typeLiteral);
        try {
            return this.membersInjectorStore.get(typeLiteral, errors);
        }
        catch (ErrorsException ex) {
            throw new ConfigurationException(errors.merge(ex.getErrors()).getMessages());
        }
    }
    
    @Override
    public <T> MembersInjector<T> getMembersInjector(final Class<T> clazz) {
        return this.getMembersInjector((TypeLiteral<T>)TypeLiteral.get((Class<T>)clazz));
    }
    
    SingleParameterInjector<?>[] getParametersInjectors(List<Dependency<?>> iterator, final Errors errors) throws ErrorsException {
        if (((List)iterator).isEmpty()) {
            return null;
        }
        final int size = errors.size();
        final SingleParameterInjector[] array = new SingleParameterInjector[((List)iterator).size()];
        int n = 0;
        iterator = ((List<Dependency<T>>)iterator).iterator();
    Label_0073_Outer:
        while (true) {
            Label_0080: {
                if (!iterator.hasNext()) {
                    break Label_0080;
                }
                final Dependency<T> dependency = iterator.next();
                while (true) {
                    try {
                        array[n] = this.createParameterInjector((Dependency<Object>)dependency, errors.withSource(dependency));
                        ++n;
                        continue Label_0073_Outer;
                        errors.throwIfNewErrors(size);
                        return (SingleParameterInjector<?>[])array;
                    }
                    catch (ErrorsException ex) {
                        continue;
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public Injector getParent() {
        return this.parent;
    }
    
    @Override
    public <T> Provider<T> getProvider(final Key<T> key) {
        final Errors errors = new Errors(key);
        try {
            final Provider<T> providerOrThrow = this.getProviderOrThrow(key, errors);
            errors.throwIfNewErrors(0);
            return providerOrThrow;
        }
        catch (ErrorsException ex) {
            throw new ConfigurationException(errors.merge(ex.getErrors()).getMessages());
        }
    }
    
    @Override
    public <T> Provider<T> getProvider(final Class<T> clazz) {
        return this.getProvider((Key<T>)Key.get((Class<T>)clazz));
    }
    
     <T> Provider<T> getProviderOrThrow(final Key<T> key, final Errors errors) throws ErrorsException {
        return new Provider<T>() {
            final /* synthetic */ Dependency val$dependency = Dependency.get(key);
            final /* synthetic */ InternalFactory val$factory = InjectorImpl.this.getInternalFactory(key, errors, JitLimitation.NO_JIT);
            
            @Override
            public T get() {
                final Errors errors = new Errors(this.val$dependency);
                try {
                    final T callInContext = InjectorImpl.this.callInContext((ContextualCallable<T>)new ContextualCallable<T>() {
                        @Override
                        public T call(final InternalContext internalContext) throws ErrorsException {
                            final Dependency setDependency = internalContext.setDependency(Provider.this.val$dependency);
                            try {
                                return Provider.this.val$factory.get(errors, internalContext, Provider.this.val$dependency, false);
                            }
                            finally {
                                internalContext.setDependency(setDependency);
                            }
                        }
                    });
                    errors.throwIfNewErrors(0);
                    return callInContext;
                }
                catch (ErrorsException ex) {
                    throw new ProvisionException(errors.merge(ex.getErrors()).getMessages());
                }
            }
            
            @Override
            public String toString() {
                return this.val$factory.toString();
            }
        };
    }
    
    @Override
    public Map<Class<? extends Annotation>, Scope> getScopeBindings() {
        return (Map<Class<? extends Annotation>, Scope>)$ImmutableMap.copyOf((Map<?, ?>)this.state.getScopes());
    }
    
    @Override
    public Set<TypeConverterBinding> getTypeConverterBindings() {
        return (Set<TypeConverterBinding>)$ImmutableSet.copyOf((Iterable<?>)this.state.getConvertersThisLevel());
    }
    
    void index() {
        final Iterator<Binding<?>> iterator = this.state.getExplicitBindingsThisLevel().values().iterator();
        while (iterator.hasNext()) {
            this.index((Binding<Object>)iterator.next());
        }
    }
    
     <T> void index(final Binding<T> binding) {
        this.bindingsMultimap.put(binding.getKey().getTypeLiteral(), binding);
    }
    
     <T> void initializeBinding(final BindingImpl<T> bindingImpl, final Errors errors) throws ErrorsException {
        if (bindingImpl instanceof ConstructorBindingImpl) {
            ((ConstructorBindingImpl)bindingImpl).initialize(this, errors);
        }
    }
    
     <T> void initializeJitBinding(final BindingImpl<T> bindingImpl, final Errors errors) throws ErrorsException {
        if (!(bindingImpl instanceof ConstructorBindingImpl)) {
            return;
        }
        final Key<T> key = bindingImpl.getKey();
        this.jitBindings.put(key, bindingImpl);
        final ConstructorBindingImpl constructorBindingImpl = (ConstructorBindingImpl<T>)bindingImpl;
        try {
            constructorBindingImpl.initialize(this, errors);
            if (!true) {
                this.removeFailedJitBinding(key, null);
                this.cleanup(bindingImpl, new HashSet<Key>());
            }
        }
        finally {
            if (!false) {
                this.removeFailedJitBinding(key, null);
                this.cleanup(bindingImpl, new HashSet<Key>());
            }
        }
    }
    
    @Override
    public void injectMembers(final Object o) {
        this.getMembersInjector(o.getClass()).injectMembers(o);
    }
    
    @Override
    public String toString() {
        return new $ToStringBuilder(Injector.class).add("bindings", this.state.getExplicitBindingsThisLevel().values()).toString();
    }
    
    private static class BindingsMultimap
    {
        final Map<TypeLiteral<?>, List<Binding<?>>> multimap;
        
        private BindingsMultimap() {
            this.multimap = (Map<TypeLiteral<?>, List<Binding<?>>>)$Maps.newHashMap();
        }
        
         <T> List<Binding<T>> getAll(final TypeLiteral<T> typeLiteral) {
            if (this.multimap.get(typeLiteral) != null) {
                return Collections.unmodifiableList((List<? extends Binding<T>>)this.multimap.get(typeLiteral));
            }
            return (List<Binding<T>>)$ImmutableList.of();
        }
        
         <T> void put(final TypeLiteral<T> typeLiteral, final Binding<T> binding) {
            Object arrayList;
            if ((arrayList = this.multimap.get(typeLiteral)) == null) {
                arrayList = $Lists.newArrayList();
                this.multimap.put(typeLiteral, (List<Binding<?>>)arrayList);
            }
            ((List<Binding<T>>)arrayList).add(binding);
        }
    }
    
    private static class ConvertedConstantBindingImpl<T> extends BindingImpl<T> implements ConvertedConstantBinding<T>
    {
        final Binding<String> originalBinding;
        final Provider<T> provider;
        final TypeConverterBinding typeConverterBinding;
        final T value;
        
        ConvertedConstantBindingImpl(final InjectorImpl p0, final Key<T> p1, final T p2, final Binding<String> p3, final TypeConverterBinding p4) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: aload_1        
            //     2: aload_2        
            //     3: aload           4
            //     5: invokeinterface com/google/inject/Binding.getSource:()Ljava/lang/Object;
            //    10: new             Lcom/google/inject/internal/ConstantFactory;
            //    13: dup            
            //    14: aload_3        
            //    15: invokestatic    com/google/inject/internal/Initializables.of:(Ljava/lang/Object;)Lcom/google/inject/internal/Initializable;
            //    18: invokespecial   com/google/inject/internal/ConstantFactory.<init>:(Lcom/google/inject/internal/Initializable;)V
            //    21: getstatic       com/google/inject/internal/Scoping.UNSCOPED:Lcom/google/inject/internal/Scoping;
            //    24: invokespecial   com/google/inject/internal/BindingImpl.<init>:(Lcom/google/inject/internal/InjectorImpl;Lcom/google/inject/Key;Ljava/lang/Object;Lcom/google/inject/internal/InternalFactory;Lcom/google/inject/internal/Scoping;)V
            //    27: aload_0        
            //    28: aload_3        
            //    29: putfield        com/google/inject/internal/InjectorImpl$ConvertedConstantBindingImpl.value:Ljava/lang/Object;
            //    32: aload_0        
            //    33: aload_3        
            //    34: invokestatic    invokestatic   !!! ERROR
            //    37: putfield        com/google/inject/internal/InjectorImpl$ConvertedConstantBindingImpl.provider:Lcom/google/inject/Provider;
            //    40: aload_0        
            //    41: aload           4
            //    43: putfield        com/google/inject/internal/InjectorImpl$ConvertedConstantBindingImpl.originalBinding:Lcom/google/inject/Binding;
            //    46: aload_0        
            //    47: aload           5
            //    49: putfield        com/google/inject/internal/InjectorImpl$ConvertedConstantBindingImpl.typeConverterBinding:Lcom/google/inject/spi/TypeConverterBinding;
            //    52: return         
            //    Signature:
            //  (Lcom/google/inject/internal/InjectorImpl;Lcom/google/inject/Key<TT;>;TT;Lcom/google/inject/Binding<Ljava/lang/String;>;Lcom/google/inject/spi/TypeConverterBinding;)V
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalArgumentException: Argument 'typeArguments' must not have any null elements.
            //     at com.strobel.core.VerifyArgument.noNullElementsAndNotEmpty(VerifyArgument.java:145)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.makeGenericType(CoreMetadataFactory.java:570)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory.makeParameterizedType(CoreMetadataFactory.java:156)
            //     at com.strobel.assembler.metadata.signatures.Reifier.visitClassTypeSignature(Reifier.java:125)
            //     at com.strobel.assembler.metadata.signatures.ClassTypeSignature.accept(ClassTypeSignature.java:46)
            //     at com.strobel.assembler.metadata.MetadataParser.parseClassSignature(MetadataParser.java:404)
            //     at com.strobel.assembler.metadata.ClassFileReader.populateBaseTypes(ClassFileReader.java:665)
            //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:438)
            //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:366)
            //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:124)
            //     at com.strobel.decompiler.NoRetryMetadataSystem.resolveType(DecompilerDriver.java:463)
            //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:76)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:589)
            //     at com.strobel.assembler.metadata.ClassFileReader.populateAnonymousInnerTypes(ClassFileReader.java:764)
            //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:443)
            //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:366)
            //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:124)
            //     at com.strobel.decompiler.NoRetryMetadataSystem.resolveType(DecompilerDriver.java:463)
            //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:76)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:589)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:128)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:599)
            //     at com.strobel.assembler.metadata.MethodReference.resolve(MethodReference.java:172)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2428)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1061)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
            //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:692)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:529)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:556)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public <V> V acceptTargetVisitor(final BindingTargetVisitor<? super T, V> bindingTargetVisitor) {
            return bindingTargetVisitor.visit((ConvertedConstantBinding<? extends T>)this);
        }
        
        @Override
        public void applyTo(final Binder binder) {
            throw new UnsupportedOperationException("This element represents a synthetic binding.");
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b2;
            final boolean b = b2 = false;
            if (o instanceof ConvertedConstantBindingImpl) {
                final ConvertedConstantBindingImpl convertedConstantBindingImpl = (ConvertedConstantBindingImpl)o;
                b2 = b;
                if (this.getKey().equals(convertedConstantBindingImpl.getKey())) {
                    b2 = b;
                    if (this.getScoping().equals(convertedConstantBindingImpl.getScoping())) {
                        b2 = b;
                        if ($Objects.equal(this.value, convertedConstantBindingImpl.value)) {
                            b2 = true;
                        }
                    }
                }
            }
            return b2;
        }
        
        @Override
        public Set<Dependency<?>> getDependencies() {
            return (Set<Dependency<?>>)$ImmutableSet.of(Dependency.get(this.getSourceKey()));
        }
        
        @Override
        public Provider<T> getProvider() {
            return this.provider;
        }
        
        public Key<String> getSourceKey() {
            return this.originalBinding.getKey();
        }
        
        @Override
        public int hashCode() {
            return $Objects.hashCode(this.getKey(), this.getScoping(), this.value);
        }
        
        @Override
        public String toString() {
            return new $ToStringBuilder(ConvertedConstantBinding.class).add("key", this.getKey()).add("sourceKey", this.getSourceKey()).add("value", this.value).toString();
        }
    }
    
    static class InjectorOptions
    {
        final boolean disableCircularProxies;
        final boolean jitDisabled;
        final Stage stage;
        
        InjectorOptions(final Stage stage, final boolean jitDisabled, final boolean disableCircularProxies) {
            this.stage = stage;
            this.jitDisabled = jitDisabled;
            this.disableCircularProxies = disableCircularProxies;
        }
        
        @Override
        public String toString() {
            return new $ToStringBuilder(this.getClass()).add("stage", this.stage).add("jitDisabled", this.jitDisabled).add("disableCircularProxies", this.disableCircularProxies).toString();
        }
    }
    
    enum JitLimitation
    {
        EXISTING_JIT, 
        NEW_OR_EXISTING_JIT, 
        NO_JIT;
    }
    
    interface MethodInvoker
    {
        Object invoke(final Object p0, final Object... p1) throws IllegalAccessException, InvocationTargetException;
    }
    
    private static class ProviderBindingImpl<T> extends BindingImpl<Provider<T>> implements HasDependencies, ProviderBinding<Provider<T>>
    {
        final BindingImpl<T> providedBinding;
        
        ProviderBindingImpl(final InjectorImpl injectorImpl, final Key<Provider<T>> key, final Binding<T> binding) {
            super(injectorImpl, key, binding.getSource(), createInternalFactory(binding), Scoping.UNSCOPED);
            this.providedBinding = (BindingImpl<T>)binding;
        }
        
        static <T> InternalFactory<Provider<T>> createInternalFactory(final Binding<T> binding) {
            return new InternalFactory<Provider<T>>() {
                final /* synthetic */ Provider val$provider = binding.getProvider();
                
                @Override
                public Provider<T> get(final Errors errors, final InternalContext internalContext, final Dependency dependency, final boolean b) {
                    return (Provider<T>)this.val$provider;
                }
            };
        }
        
        @Override
        public <V> V acceptTargetVisitor(final BindingTargetVisitor<? super Provider<T>, V> bindingTargetVisitor) {
            return bindingTargetVisitor.visit(this);
        }
        
        @Override
        public void applyTo(final Binder binder) {
            throw new UnsupportedOperationException("This element represents a synthetic binding.");
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b2;
            final boolean b = b2 = false;
            if (o instanceof ProviderBindingImpl) {
                final ProviderBindingImpl providerBindingImpl = (ProviderBindingImpl)o;
                b2 = b;
                if (this.getKey().equals(providerBindingImpl.getKey())) {
                    b2 = b;
                    if (this.getScoping().equals(providerBindingImpl.getScoping())) {
                        b2 = b;
                        if ($Objects.equal(this.providedBinding, providerBindingImpl.providedBinding)) {
                            b2 = true;
                        }
                    }
                }
            }
            return b2;
        }
        
        @Override
        public Set<Dependency<?>> getDependencies() {
            return (Set<Dependency<?>>)$ImmutableSet.of(Dependency.get(this.getProvidedKey()));
        }
        
        public Key<? extends T> getProvidedKey() {
            return (Key<? extends T>)this.providedBinding.getKey();
        }
        
        @Override
        public int hashCode() {
            return $Objects.hashCode(this.getKey(), this.getScoping(), this.providedBinding);
        }
        
        @Override
        public String toString() {
            return new $ToStringBuilder(ProviderBinding.class).add("key", this.getKey()).add("providedKey", this.getProvidedKey()).toString();
        }
    }
}
