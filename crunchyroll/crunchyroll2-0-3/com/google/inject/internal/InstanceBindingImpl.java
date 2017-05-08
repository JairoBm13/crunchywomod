// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.internal.util.$ToStringBuilder;
import com.google.inject.spi.HasDependencies;
import com.google.inject.spi.Dependency;
import com.google.inject.internal.util.$Objects;
import com.google.inject.Binder;
import com.google.inject.spi.BindingTargetVisitor;
import java.util.Set;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.internal.util.$ImmutableSet;
import com.google.inject.spi.InstanceBinding;

final class InstanceBindingImpl<T> extends BindingImpl<T> implements InstanceBinding<T>
{
    final $ImmutableSet<InjectionPoint> injectionPoints;
    final T instance;
    final Provider<T> provider;
    
    public InstanceBindingImpl(final InjectorImpl p0, final Key<T> p1, final Object p2, final InternalFactory<? extends T> p3, final Set<InjectionPoint> p4, final T p5) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: aload_1        
        //     2: aload_2        
        //     3: aload_3        
        //     4: aload           4
        //     6: getstatic       com/google/inject/internal/Scoping.EAGER_SINGLETON:Lcom/google/inject/internal/Scoping;
        //     9: invokespecial   com/google/inject/internal/BindingImpl.<init>:(Lcom/google/inject/internal/InjectorImpl;Lcom/google/inject/Key;Ljava/lang/Object;Lcom/google/inject/internal/InternalFactory;Lcom/google/inject/internal/Scoping;)V
        //    12: aload_0        
        //    13: aload           5
        //    15: invokestatic    com/google/inject/internal/util/$ImmutableSet.copyOf:(Ljava/lang/Iterable;)Lcom/google/inject/internal/util/$ImmutableSet;
        //    18: putfield        com/google/inject/internal/InstanceBindingImpl.injectionPoints:Lcom/google/inject/internal/util/$ImmutableSet;
        //    21: aload_0        
        //    22: aload           6
        //    24: putfield        com/google/inject/internal/InstanceBindingImpl.instance:Ljava/lang/Object;
        //    27: aload_0        
        //    28: aload           6
        //    30: invokestatic    invokestatic   !!! ERROR
        //    33: putfield        com/google/inject/internal/InstanceBindingImpl.provider:Lcom/google/inject/Provider;
        //    36: return         
        //    Signature:
        //  (Lcom/google/inject/internal/InjectorImpl;Lcom/google/inject/Key<TT;>;Ljava/lang/Object;Lcom/google/inject/internal/InternalFactory<+TT;>;Ljava/util/Set<Lcom/google/inject/spi/InjectionPoint;>;TT;)V
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
    
    public InstanceBindingImpl(final Object p0, final Key<T> p1, final Scoping p2, final Set<InjectionPoint> p3, final T p4) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: aload_1        
        //     2: aload_2        
        //     3: aload_3        
        //     4: invokespecial   com/google/inject/internal/BindingImpl.<init>:(Ljava/lang/Object;Lcom/google/inject/Key;Lcom/google/inject/internal/Scoping;)V
        //     7: aload_0        
        //     8: aload           4
        //    10: invokestatic    com/google/inject/internal/util/$ImmutableSet.copyOf:(Ljava/lang/Iterable;)Lcom/google/inject/internal/util/$ImmutableSet;
        //    13: putfield        com/google/inject/internal/InstanceBindingImpl.injectionPoints:Lcom/google/inject/internal/util/$ImmutableSet;
        //    16: aload_0        
        //    17: aload           5
        //    19: putfield        com/google/inject/internal/InstanceBindingImpl.instance:Ljava/lang/Object;
        //    22: aload_0        
        //    23: aload           5
        //    25: invokestatic    invokestatic   !!! ERROR
        //    28: putfield        com/google/inject/internal/InstanceBindingImpl.provider:Lcom/google/inject/Provider;
        //    31: return         
        //    Signature:
        //  (Ljava/lang/Object;Lcom/google/inject/Key<TT;>;Lcom/google/inject/internal/Scoping;Ljava/util/Set<Lcom/google/inject/spi/InjectionPoint;>;TT;)V
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
        return bindingTargetVisitor.visit((InstanceBinding<? extends T>)this);
    }
    
    @Override
    public void applyTo(final Binder binder) {
        binder.withSource(this.getSource()).bind(this.getKey()).toInstance(this.instance);
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b2;
        final boolean b = b2 = false;
        if (o instanceof InstanceBindingImpl) {
            final InstanceBindingImpl instanceBindingImpl = (InstanceBindingImpl)o;
            b2 = b;
            if (this.getKey().equals(instanceBindingImpl.getKey())) {
                b2 = b;
                if (this.getScoping().equals(instanceBindingImpl.getScoping())) {
                    b2 = b;
                    if ($Objects.equal(this.instance, instanceBindingImpl.instance)) {
                        b2 = true;
                    }
                }
            }
        }
        return b2;
    }
    
    @Override
    public Set<Dependency<?>> getDependencies() {
        if (this.instance instanceof HasDependencies) {
            return (Set<Dependency<?>>)$ImmutableSet.copyOf((Iterable<?>)((HasDependencies)this.instance).getDependencies());
        }
        return Dependency.forInjectionPoints(this.injectionPoints);
    }
    
    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return this.injectionPoints;
    }
    
    @Override
    public T getInstance() {
        return this.instance;
    }
    
    @Override
    public Provider<T> getProvider() {
        return this.provider;
    }
    
    @Override
    public int hashCode() {
        return $Objects.hashCode(this.getKey(), this.getScoping());
    }
    
    @Override
    public String toString() {
        return new $ToStringBuilder(InstanceBinding.class).add("key", this.getKey()).add("source", this.getSource()).add("instance", this.instance).toString();
    }
    
    public BindingImpl<T> withKey(final Key<T> key) {
        return new InstanceBindingImpl(this.getSource(), (Key<Object>)key, this.getScoping(), this.injectionPoints, this.instance);
    }
    
    public BindingImpl<T> withScoping(final Scoping scoping) {
        return new InstanceBindingImpl(this.getSource(), this.getKey(), scoping, this.injectionPoints, this.instance);
    }
}
