// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.RandomAccess;
import java.util.List;

public abstract class $ImmutableList<E> extends $ImmutableCollection<E> implements List<E>, RandomAccess
{
    private static final $ImmutableList<?> EMPTY_IMMUTABLE_LIST;
    
    static {
        EMPTY_IMMUTABLE_LIST = new EmptyImmutableList();
    }
    
    private static Object[] copyIntoArray(final Object... array) {
        final Object[] array2 = new Object[array.length];
        for (int length = array.length, i = 0, n = 0; i < length; ++i, ++n) {
            final Object o = array[i];
            if (o == null) {
                throw new NullPointerException("at index " + n);
            }
            array2[n] = o;
        }
        return array2;
    }
    
    public static <E> $ImmutableList<E> copyOf(final Iterable<? extends E> iterable) {
        if (iterable instanceof $ImmutableList) {
            return ($ImmutableList<E>)iterable;
        }
        if (iterable instanceof Collection) {
            return copyOfInternal((Collection<? extends E>)iterable);
        }
        return copyOfInternal($Lists.newArrayList(iterable));
    }
    
    private static Object[] copyOf(final Object[] array, final int n) {
        final Object[] array2 = new Object[n];
        System.arraycopy(array, 0, array2, 0, Math.min(array.length, n));
        return array2;
    }
    
    private static <E> $ImmutableList<E> copyOfInternal(final ArrayList<? extends E> list) {
        if (list.isEmpty()) {
            return of();
        }
        return new RegularImmutableList<E>(nullChecked(list.toArray()));
    }
    
    private static <E> $ImmutableList<E> copyOfInternal(final Collection<? extends E> collection) {
        final int size = collection.size();
        if (size == 0) {
            return of();
        }
        return ($ImmutableList<E>)createFromIterable(collection, size);
    }
    
    private static <E> $ImmutableList<E> createFromIterable(final Iterable<?> iterable, int n) {
        final Object[] array = new Object[n];
        final int n2 = 0;
        final Iterator<?> iterator = iterable.iterator();
        int n3 = n;
        n = n2;
        Object[] array2 = array;
        while (iterator.hasNext()) {
            final Object next = iterator.next();
            Object[] copy = array2;
            int n4;
            if (n == (n4 = n3)) {
                n4 = (n3 / 2 + 1) * 3;
                copy = copyOf(array2, n4);
            }
            if (next == null) {
                throw new NullPointerException("at index " + n);
            }
            copy[n] = next;
            ++n;
            array2 = copy;
            n3 = n4;
        }
        if (n == 0) {
            return of();
        }
        Object[] copy2 = array2;
        if (n != n3) {
            copy2 = copyOf(array2, n);
        }
        return new RegularImmutableList<E>(copy2, 0, n);
    }
    
    private static Object[] nullChecked(final Object[] array) {
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == null) {
                throw new NullPointerException("at index " + i);
            }
        }
        return array;
    }
    
    public static <E> $ImmutableList<E> of() {
        return ($ImmutableList<E>)$ImmutableList.EMPTY_IMMUTABLE_LIST;
    }
    
    public static <E> $ImmutableList<E> of(final E e) {
        return new RegularImmutableList<E>(copyIntoArray(e));
    }
    
    public static <E> $ImmutableList<E> of(final E e, final E e2, final E e3) {
        return new RegularImmutableList<E>(copyIntoArray(e, e2, e3));
    }
    
    public static <E> $ImmutableList<E> of(final E... array) {
        if (array.length == 0) {
            return of();
        }
        return new RegularImmutableList<E>(copyIntoArray((Object[])array));
    }
    
    @Override
    public final void add(final int n, final E e) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final boolean addAll(final int n, final Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public abstract $UnmodifiableIterator<E> iterator();
    
    @Override
    public final E remove(final int n) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final E set(final int n, final E e) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public abstract $ImmutableList<E> subList(final int p0, final int p1);
    
    public static class Builder<E>
    {
        private final ArrayList<E> contents;
        
        public Builder() {
            this.contents = $Lists.newArrayList();
        }
    }
    
    private static final class EmptyImmutableList extends $ImmutableList<Object>
    {
        private static final Object[] EMPTY_ARRAY;
        
        static {
            EMPTY_ARRAY = new Object[0];
        }
        
        private EmptyImmutableList() {
            super(null);
        }
        
        @Override
        public boolean contains(final Object o) {
            return false;
        }
        
        @Override
        public boolean containsAll(final Collection<?> collection) {
            return collection.isEmpty();
        }
        
        @Override
        public boolean equals(@$Nullable final Object o) {
            return o instanceof List && ((List)o).isEmpty();
        }
        
        @Override
        public Object get(final int n) {
            $Preconditions.checkElementIndex(n, 0);
            throw new AssertionError((Object)"unreachable");
        }
        
        @Override
        public int hashCode() {
            return 1;
        }
        
        @Override
        public int indexOf(final Object o) {
            return -1;
        }
        
        @Override
        public boolean isEmpty() {
            return true;
        }
        
        @Override
        public $UnmodifiableIterator<Object> iterator() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: invokestatic    invokestatic   !!! ERROR
            //     3: areturn        
            //    Signature:
            //  ()Lcom/google/inject/internal/util/$UnmodifiableIterator<Ljava/lang/Object;>;
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
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
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
        public int lastIndexOf(final Object o) {
            return -1;
        }
        
        @Override
        public ListIterator<Object> listIterator() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: invokestatic    invokestatic   !!! ERROR
            //     3: areturn        
            //    Signature:
            //  ()Ljava/util/ListIterator<Ljava/lang/Object;>;
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
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
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
        public ListIterator<Object> listIterator(final int p0) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: iload_1        
            //     1: iconst_0       
            //     2: invokestatic    com/google/inject/internal/util/$Preconditions.checkPositionIndex:(II)V
            //     5: invokestatic    invokestatic   !!! ERROR
            //     8: areturn        
            //    Signature:
            //  (I)Ljava/util/ListIterator<Ljava/lang/Object;>;
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
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
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
        public int size() {
            return 0;
        }
        
        @Override
        public $ImmutableList<Object> subList(final int n, final int n2) {
            $Preconditions.checkPositionIndexes(n, n2, 0);
            return this;
        }
        
        @Override
        public Object[] toArray() {
            return EmptyImmutableList.EMPTY_ARRAY;
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            if (array.length > 0) {
                array[0] = null;
            }
            return array;
        }
        
        @Override
        public String toString() {
            return "[]";
        }
    }
    
    private static final class RegularImmutableList<E> extends $ImmutableList<E>
    {
        private final Object[] array;
        private final int offset;
        private final int size;
        
        private RegularImmutableList(final Object[] array) {
            this(array, 0, array.length);
        }
        
        private RegularImmutableList(final Object[] array, final int offset, final int size) {
            super(null);
            this.offset = offset;
            this.size = size;
            this.array = array;
        }
        
        @Override
        public boolean contains(final Object o) {
            return this.indexOf(o) != -1;
        }
        
        @Override
        public boolean equals(@$Nullable final Object o) {
            if (o != this) {
                if (!(o instanceof List)) {
                    return false;
                }
                final List list = (List)o;
                if (this.size() != list.size()) {
                    return false;
                }
                int offset = this.offset;
                if (o instanceof RegularImmutableList) {
                    final RegularImmutableList list2 = (RegularImmutableList)o;
                    for (int i = list2.offset; i < list2.offset + list2.size; ++i, ++offset) {
                        if (!this.array[offset].equals(list2.array[i])) {
                            return false;
                        }
                    }
                }
                else {
                    final Iterator<Object> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        if (!this.array[offset].equals(iterator.next())) {
                            return false;
                        }
                        ++offset;
                    }
                }
            }
            return true;
        }
        
        @Override
        public E get(final int n) {
            $Preconditions.checkElementIndex(n, this.size);
            return (E)this.array[this.offset + n];
        }
        
        @Override
        public int hashCode() {
            int n = 1;
            for (int i = this.offset; i < this.offset + this.size; ++i) {
                n = n * 31 + this.array[i].hashCode();
            }
            return n;
        }
        
        @Override
        public int indexOf(final Object o) {
            if (o != null) {
                for (int i = this.offset; i < this.offset + this.size; ++i) {
                    if (this.array[i].equals(o)) {
                        return i - this.offset;
                    }
                }
            }
            return -1;
        }
        
        @Override
        public boolean isEmpty() {
            return false;
        }
        
        @Override
        public $UnmodifiableIterator<E> iterator() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: aload_0        
            //     1: getfield        com/google/inject/internal/util/$ImmutableList$RegularImmutableList.array:[Ljava/lang/Object;
            //     4: aload_0        
            //     5: getfield        com/google/inject/internal/util/$ImmutableList$RegularImmutableList.offset:I
            //     8: aload_0        
            //     9: getfield        com/google/inject/internal/util/$ImmutableList$RegularImmutableList.size:I
            //    12: invokestatic    invokestatic   !!! ERROR
            //    15: areturn        
            //    Signature:
            //  ()Lcom/google/inject/internal/util/$UnmodifiableIterator<TE;>;
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
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
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
        public int lastIndexOf(final Object o) {
            if (o != null) {
                for (int i = this.offset + this.size - 1; i >= this.offset; --i) {
                    if (this.array[i].equals(o)) {
                        return i - this.offset;
                    }
                }
            }
            return -1;
        }
        
        @Override
        public ListIterator<E> listIterator() {
            return this.listIterator(0);
        }
        
        @Override
        public ListIterator<E> listIterator(final int n) {
            $Preconditions.checkPositionIndex(n, this.size);
            return new ListIterator<E>() {
                int index = n;
                
                @Override
                public void add(final E e) {
                    throw new UnsupportedOperationException();
                }
                
                @Override
                public boolean hasNext() {
                    return this.index < RegularImmutableList.this.size;
                }
                
                @Override
                public boolean hasPrevious() {
                    return this.index > 0;
                }
                
                @Override
                public E next() {
                    try {
                        final E value = RegularImmutableList.this.get(this.index);
                        ++this.index;
                        return value;
                    }
                    catch (IndexOutOfBoundsException ex) {
                        throw new NoSuchElementException();
                    }
                }
                
                @Override
                public int nextIndex() {
                    return this.index;
                }
                
                @Override
                public E previous() {
                    try {
                        final E value = RegularImmutableList.this.get(this.index - 1);
                        --this.index;
                        return value;
                    }
                    catch (IndexOutOfBoundsException ex) {
                        throw new NoSuchElementException();
                    }
                }
                
                @Override
                public int previousIndex() {
                    return this.index - 1;
                }
                
                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
                
                @Override
                public void set(final E e) {
                    throw new UnsupportedOperationException();
                }
            };
        }
        
        @Override
        public int size() {
            return this.size;
        }
        
        @Override
        public $ImmutableList<E> subList(final int n, final int n2) {
            $Preconditions.checkPositionIndexes(n, n2, this.size);
            if (n == n2) {
                return $ImmutableList.of();
            }
            return new RegularImmutableList(this.array, this.offset + n, n2 - n);
        }
        
        @Override
        public Object[] toArray() {
            final Object[] array = new Object[this.size()];
            System.arraycopy(this.array, this.offset, array, 0, this.size);
            return array;
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            T[] array2;
            if (array.length < this.size) {
                array2 = $ObjectArrays.newArray(array, this.size);
            }
            else {
                array2 = array;
                if (array.length > this.size) {
                    array[this.size] = null;
                    array2 = array;
                }
            }
            System.arraycopy(this.array, this.offset, array2, 0, this.size);
            return array2;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(this.size() * 16);
            sb.append('[').append(this.array[this.offset]);
            for (int i = this.offset + 1; i < this.offset + this.size; ++i) {
                sb.append(", ").append(this.array[i]);
            }
            return sb.append(']').toString();
        }
    }
    
    private static class SerializedForm implements Serializable
    {
    }
}
