// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import javax.inject.Provider;
import com.google.inject.internal.util.$ImmutableSet;
import java.util.Set;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.TypeLiteral;
import java.lang.reflect.Constructor;
import com.google.inject.internal.util.$Preconditions;
import com.google.inject.binder.ScopedBindingBuilder;
import java.util.Iterator;
import com.google.inject.spi.Message;
import com.google.inject.ConfigurationException;
import com.google.inject.Key;
import com.google.inject.spi.Element;
import java.util.List;
import com.google.inject.Binder;
import com.google.inject.binder.AnnotatedBindingBuilder;

public class BindingBuilder<T> extends AbstractBindingBuilder<T> implements AnnotatedBindingBuilder<T>
{
    public BindingBuilder(final Binder binder, final List<Element> list, final Object o, final Key<T> key) {
        super(binder, list, o, key);
    }
    
    private void copyErrorsToBinder(final ConfigurationException ex) {
        final Iterator<Message> iterator = ex.getErrorMessages().iterator();
        while (iterator.hasNext()) {
            this.binder.addError(iterator.next());
        }
    }
    
    @Override
    public BindingBuilder<T> to(final Key<? extends T> key) {
        $Preconditions.checkNotNull(key, "linkedKey");
        this.checkNotTargetted();
        final BindingImpl<T> binding = this.getBinding();
        this.setBinding(new LinkedBindingImpl<T>(binding.getSource(), (Key<Object>)binding.getKey(), binding.getScoping(), key));
        return this;
    }
    
    @Override
    public <S extends T> ScopedBindingBuilder toConstructor(final Constructor<S> p0, final TypeLiteral<? extends S> p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_1        
        //     1: ldc             "constructor"
        //     3: invokestatic    com/google/inject/internal/util/$Preconditions.checkNotNull:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //     6: pop            
        //     7: aload_2        
        //     8: ldc             "type"
        //    10: invokestatic    com/google/inject/internal/util/$Preconditions.checkNotNull:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    13: pop            
        //    14: aload_0        
        //    15: invokevirtual   com/google/inject/internal/BindingBuilder.checkNotTargetted:()V
        //    18: aload_0        
        //    19: invokevirtual   com/google/inject/internal/BindingBuilder.getBinding:()Lcom/google/inject/internal/BindingImpl;
        //    22: astore          4
        //    24: aload_2        
        //    25: invokestatic    com/google/inject/spi/InjectionPoint.forInstanceMethodsAndFields:(Lcom/google/inject/TypeLiteral;)Ljava/util/Set;
        //    28: astore_3       
        //    29: aload_1        
        //    30: aload_2        
        //    31: invokestatic    com/google/inject/spi/InjectionPoint.forConstructor:(Ljava/lang/reflect/Constructor;Lcom/google/inject/TypeLiteral;)Lcom/google/inject/spi/InjectionPoint;
        //    34: astore_1       
        //    35: aload_0        
        //    36: new             Lcom/google/inject/internal/ConstructorBindingImpl;
        //    39: dup            
        //    40: aload           4
        //    42: invokevirtual   com/google/inject/internal/BindingImpl.getKey:()Lcom/google/inject/Key;
        //    45: aload           4
        //    47: invokevirtual   com/google/inject/internal/BindingImpl.getSource:()Ljava/lang/Object;
        //    50: aload           4
        //    52: invokevirtual   com/google/inject/internal/BindingImpl.getScoping:()Lcom/google/inject/internal/Scoping;
        //    55: aload_1        
        //    56: aload_3        
        //    57: invokespecial   com/google/inject/internal/ConstructorBindingImpl.<init>:(Lcom/google/inject/Key;Ljava/lang/Object;Lcom/google/inject/internal/Scoping;Lcom/google/inject/spi/InjectionPoint;Ljava/util/Set;)V
        //    60: invokevirtual   com/google/inject/internal/BindingBuilder.setBinding:(Lcom/google/inject/internal/BindingImpl;)Lcom/google/inject/internal/BindingImpl;
        //    63: pop            
        //    64: aload_0        
        //    65: areturn        
        //    66: astore_3       
        //    67: aload_0        
        //    68: aload_3        
        //    69: invokespecial   com/google/inject/internal/BindingBuilder.copyErrorsToBinder:(Lcom/google/inject/ConfigurationException;)V
        //    72: aload_3        
        //    73: invokevirtual   com/google/inject/ConfigurationException.getPartialValue:()Ljava/lang/Object;
        //    76: checkcast       Ljava/util/Set;
        //    79: astore_3       
        //    80: goto            29
        //    83: astore_1       
        //    84: aload_0        
        //    85: aload_1        
        //    86: invokespecial   com/google/inject/internal/BindingBuilder.copyErrorsToBinder:(Lcom/google/inject/ConfigurationException;)V
        //    89: aload_0        
        //    90: areturn        
        //    Signature:
        //  <S:TT;>(Ljava/lang/reflect/Constructor<TS;>;Lcom/google/inject/TypeLiteral<+TS;>;)Lcom/google/inject/binder/ScopedBindingBuilder;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                      
        //  -----  -----  -----  -----  ------------------------------------------
        //  24     29     66     83     Lcom/google/inject/ConfigurationException;
        //  29     64     83     91     Lcom/google/inject/ConfigurationException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0029:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
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
    public void toInstance(final T t) {
        this.checkNotTargetted();
        while (true) {
            Label_0064: {
                if (t == null) {
                    break Label_0064;
                }
                try {
                    final Object o = InjectionPoint.forInstanceMethodsAndFields(t.getClass());
                    final BindingImpl<T> binding = this.getBinding();
                    this.setBinding(new InstanceBindingImpl<T>(binding.getSource(), (Key<Object>)binding.getKey(), Scoping.EAGER_SINGLETON, (Set<InjectionPoint>)o, t));
                    return;
                }
                catch (ConfigurationException ex) {
                    this.copyErrorsToBinder(ex);
                    final Object o = ex.getPartialValue();
                    continue;
                }
            }
            this.binder.addError("Binding to null instances is not allowed. Use toProvider(Providers.of(null)) if this is your intended behaviour.", new Object[0]);
            final Object o = $ImmutableSet.of();
            continue;
        }
    }
    
    @Override
    public BindingBuilder<T> toProvider(final Key<? extends Provider<? extends T>> key) {
        $Preconditions.checkNotNull(key, "providerKey");
        this.checkNotTargetted();
        final BindingImpl<T> binding = this.getBinding();
        this.setBinding(new LinkedProviderBindingImpl<T>(binding.getSource(), (Key<Object>)binding.getKey(), binding.getScoping(), key));
        return this;
    }
    
    @Override
    public BindingBuilder<T> toProvider(final com.google.inject.Provider<? extends T> provider) {
        $Preconditions.checkNotNull(provider, "provider");
        this.checkNotTargetted();
        while (true) {
            try {
                final Set<InjectionPoint> forInstanceMethodsAndFields = InjectionPoint.forInstanceMethodsAndFields(provider.getClass());
                final BindingImpl<T> binding = this.getBinding();
                this.setBinding(new ProviderInstanceBindingImpl<T>(binding.getSource(), (Key<Object>)binding.getKey(), binding.getScoping(), forInstanceMethodsAndFields, provider));
                return this;
            }
            catch (ConfigurationException ex) {
                this.copyErrorsToBinder(ex);
                final Set<InjectionPoint> forInstanceMethodsAndFields = ex.getPartialValue();
                continue;
            }
            break;
        }
    }
    
    @Override
    public BindingBuilder<T> toProvider(final Class<? extends Provider<? extends T>> clazz) {
        return this.toProvider(Key.get(clazz));
    }
    
    @Override
    public String toString() {
        return "BindingBuilder<" + this.getBinding().getKey().getTypeLiteral() + ">";
    }
}
