// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public abstract class $ImmutableSet<E> extends $ImmutableCollection<E> implements Set<E>
{
    private static final $ImmutableSet<?> EMPTY_IMMUTABLE_SET;
    
    static {
        EMPTY_IMMUTABLE_SET = new EmptyImmutableSet();
    }
    
    public static <E> Builder<E> builder() {
        return new Builder<E>();
    }
    
    public static <E> $ImmutableSet<E> copyOf(final Iterable<? extends E> iterable) {
        if (iterable instanceof $ImmutableSet) {
            return ($ImmutableSet<E>)iterable;
        }
        return copyOfInternal((Collection<? extends E>)$Collections2.toCollection((Iterable<? extends E>)iterable));
    }
    
    private static <E> $ImmutableSet<E> copyOfInternal(final Collection<? extends E> collection) {
        switch (collection.size()) {
            default: {
                return create((Iterable<? extends E>)collection, collection.size());
            }
            case 0: {
                return of();
            }
            case 1: {
                return of((E)collection.iterator().next());
            }
        }
    }
    
    private static <E> $ImmutableSet<E> create(final Iterable<? extends E> iterable, int n) {
        final int chooseTableSize = $Hashing.chooseTableSize(n);
        final Object[] array = new Object[chooseTableSize];
        final int n2 = chooseTableSize - 1;
        final ArrayList<E> list = new ArrayList<E>(n);
        n = 0;
    Label_0034:
        for (final E next : iterable) {
            final int hashCode = next.hashCode();
            int smear = $Hashing.smear(hashCode);
            int n3;
            while (true) {
                n3 = (smear & n2);
                final Object o = array[n3];
                if (o == null) {
                    break;
                }
                if (o.equals(next)) {
                    continue Label_0034;
                }
                ++smear;
            }
            list.add((E)(array[n3] = next));
            n += hashCode;
        }
        if (list.size() == 1) {
            return new SingletonImmutableSet<E>(list.get(0), n);
        }
        return new RegularImmutableSet<E>(list.toArray(), n, array, n2);
    }
    
    public static <E> $ImmutableSet<E> of() {
        return ($ImmutableSet<E>)$ImmutableSet.EMPTY_IMMUTABLE_SET;
    }
    
    public static <E> $ImmutableSet<E> of(final E e) {
        return new SingletonImmutableSet<E>(e, e.hashCode());
    }
    
    public static <E> $ImmutableSet<E> of(final E... array) {
        switch (array.length) {
            default: {
                return create((Iterable<? extends E>)Arrays.asList(array), array.length);
            }
            case 0: {
                return of();
            }
            case 1: {
                return of(array[0]);
            }
        }
    }
    
    @Override
    public boolean equals(@$Nullable final Object o) {
        return o == this || ((!(o instanceof $ImmutableSet) || !this.isHashCodeFast() || !(($ImmutableSet)o).isHashCodeFast() || this.hashCode() == o.hashCode()) && $Collections2.setEquals(this, o));
    }
    
    @Override
    public int hashCode() {
        int n = 0;
        final Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            n += iterator.next().hashCode();
        }
        return n;
    }
    
    boolean isHashCodeFast() {
        return false;
    }
    
    @Override
    public abstract $UnmodifiableIterator<E> iterator();
    
    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "[]";
        }
        final $UnmodifiableIterator<Object> iterator = this.iterator();
        final StringBuilder sb = new StringBuilder(this.size() * 16);
        sb.append('[').append(iterator.next().toString());
        for (int i = 1; i < this.size(); ++i) {
            sb.append(", ").append(iterator.next().toString());
        }
        return sb.append(']').toString();
    }
    
    abstract static class ArrayImmutableSet<E> extends $ImmutableSet<E>
    {
        final Object[] elements;
        
        ArrayImmutableSet(final Object[] elements) {
            this.elements = elements;
        }
        
        @Override
        public boolean containsAll(final Collection<?> collection) {
            if (collection != this) {
                if (!(collection instanceof ArrayImmutableSet)) {
                    return super.containsAll(collection);
                }
                if (collection.size() > this.size()) {
                    return false;
                }
                final Object[] elements = ((ArrayImmutableSet<?>)collection).elements;
                for (int length = elements.length, i = 0; i < length; ++i) {
                    if (!this.contains(elements[i])) {
                        return false;
                    }
                }
            }
            return true;
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
            //     1: getfield        com/google/inject/internal/util/$ImmutableSet$ArrayImmutableSet.elements:[Ljava/lang/Object;
            //     4: invokestatic    invokestatic   !!! ERROR
            //     7: areturn        
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
        public int size() {
            return this.elements.length;
        }
        
        @Override
        public Object[] toArray() {
            final Object[] array = new Object[this.size()];
            System.arraycopy(this.elements, 0, array, 0, this.size());
            return array;
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            final int size = this.size();
            T[] array2;
            if (array.length < size) {
                array2 = $ObjectArrays.newArray(array, size);
            }
            else {
                array2 = array;
                if (array.length > size) {
                    array[size] = null;
                    array2 = array;
                }
            }
            System.arraycopy(this.elements, 0, array2, 0, size);
            return array2;
        }
    }
    
    public static class Builder<E>
    {
        final ArrayList<E> contents;
        
        public Builder() {
            this.contents = $Lists.newArrayList();
        }
        
        public Builder<E> add(final E e) {
            $Preconditions.checkNotNull(e, "element cannot be null");
            this.contents.add(e);
            return this;
        }
        
        public Builder<E> addAll(final Iterable<? extends E> iterable) {
            if (iterable instanceof Collection) {
                this.contents.ensureCapacity(this.contents.size() + ((Collection<Object>)iterable).size());
            }
            for (final E next : iterable) {
                $Preconditions.checkNotNull(next, "elements contains a null");
                this.contents.add(next);
            }
            return this;
        }
        
        public $ImmutableSet<E> build() {
            return $ImmutableSet.copyOf((Iterable<? extends E>)this.contents);
        }
    }
    
    private static final class EmptyImmutableSet extends $ImmutableSet<Object>
    {
        private static final Object[] EMPTY_ARRAY;
        
        static {
            EMPTY_ARRAY = new Object[0];
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
            return o instanceof Set && ((Set)o).isEmpty();
        }
        
        @Override
        public final int hashCode() {
            return 0;
        }
        
        @Override
        public boolean isEmpty() {
            return true;
        }
        
        @Override
        boolean isHashCodeFast() {
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
        public int size() {
            return 0;
        }
        
        @Override
        public Object[] toArray() {
            return EmptyImmutableSet.EMPTY_ARRAY;
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
    
    private static final class RegularImmutableSet<E> extends ArrayImmutableSet<E>
    {
        final int hashCode;
        final int mask;
        final Object[] table;
        
        RegularImmutableSet(final Object[] array, final int hashCode, final Object[] table, final int mask) {
            super(array);
            this.table = table;
            this.mask = mask;
            this.hashCode = hashCode;
        }
        
        @Override
        public boolean contains(final Object o) {
            if (o != null) {
                int smear = $Hashing.smear(o.hashCode());
                while (true) {
                    final Object o2 = this.table[this.mask & smear];
                    if (o2 == null) {
                        return false;
                    }
                    if (o2.equals(o)) {
                        break;
                    }
                    ++smear;
                }
                return true;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.hashCode;
        }
        
        @Override
        boolean isHashCodeFast() {
            return true;
        }
    }
    
    private static class SerializedForm implements Serializable
    {
    }
    
    private static final class SingletonImmutableSet<E> extends $ImmutableSet<E>
    {
        final E element;
        final int hashCode;
        
        SingletonImmutableSet(final E element, final int hashCode) {
            this.element = element;
            this.hashCode = hashCode;
        }
        
        @Override
        public boolean contains(final Object o) {
            return this.element.equals(o);
        }
        
        @Override
        public boolean equals(@$Nullable final Object o) {
            if (o != this) {
                if (!(o instanceof Set)) {
                    return false;
                }
                final Set set = (Set)o;
                if (set.size() != 1 || !this.element.equals(set.iterator().next())) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public final int hashCode() {
            return this.hashCode;
        }
        
        @Override
        public boolean isEmpty() {
            return false;
        }
        
        @Override
        boolean isHashCodeFast() {
            return true;
        }
        
        @Override
        public $UnmodifiableIterator<E> iterator() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: aload_0        
            //     1: getfield        com/google/inject/internal/util/$ImmutableSet$SingletonImmutableSet.element:Ljava/lang/Object;
            //     4: invokestatic    invokestatic   !!! ERROR
            //     7: areturn        
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
        public int size() {
            return 1;
        }
        
        @Override
        public Object[] toArray() {
            return new Object[] { this.element };
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            T[] array2;
            if (array.length == 0) {
                array2 = $ObjectArrays.newArray(array, 1);
            }
            else {
                array2 = array;
                if (array.length > 1) {
                    array[1] = null;
                    array2 = array;
                }
            }
            array2[0] = (T)this.element;
            return array2;
        }
        
        @Override
        public String toString() {
            final String string = this.element.toString();
            return new StringBuilder(string.length() + 2).append('[').append(string).append(']').toString();
        }
    }
    
    abstract static class TransformedImmutableSet<D, E> extends $ImmutableSet<E>
    {
        final int hashCode;
        final D[] source;
        
        TransformedImmutableSet(final D[] source, final int hashCode) {
            this.source = source;
            this.hashCode = hashCode;
        }
        
        @Override
        public final int hashCode() {
            return this.hashCode;
        }
        
        @Override
        public boolean isEmpty() {
            return false;
        }
        
        @Override
        boolean isHashCodeFast() {
            return true;
        }
        
        @Override
        public $UnmodifiableIterator<E> iterator() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: new             Lcom/google/inject/internal/util/$ImmutableSet$TransformedImmutableSet$1;
            //     3: dup            
            //     4: aload_0        
            //     5: invokespecial   com/google/inject/internal/util/$ImmutableSet$TransformedImmutableSet$1.<init>:(Lcom/google/inject/internal/util/$ImmutableSet$TransformedImmutableSet;)V
            //     8: invokestatic    invokestatic   !!! ERROR
            //    11: areturn        
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
        public int size() {
            return this.source.length;
        }
        
        @Override
        public Object[] toArray() {
            return this.toArray(new Object[this.size()]);
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            final int size = this.size();
            T[] array2;
            if (array.length < size) {
                array2 = $ObjectArrays.newArray(array, size);
            }
            else {
                array2 = array;
                if (array.length > size) {
                    array[size] = null;
                    array2 = array;
                }
            }
            for (int i = 0; i < this.source.length; ++i) {
                array2[i] = (T)this.transform(this.source[i]);
            }
            return array2;
        }
        
        abstract E transform(final D p0);
    }
}
