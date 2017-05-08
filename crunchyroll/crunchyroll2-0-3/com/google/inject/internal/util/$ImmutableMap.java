// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

import java.util.List;
import java.util.Collection;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.io.Serializable;

public abstract class $ImmutableMap<K, V> implements Serializable, ConcurrentMap<K, V>
{
    private static final $ImmutableMap<?, ?> EMPTY_IMMUTABLE_MAP;
    
    static {
        EMPTY_IMMUTABLE_MAP = new EmptyImmutableMap();
    }
    
    public static <K, V> $ImmutableMap<K, V> copyOf(final Map<? extends K, ? extends V> map) {
        if (map instanceof $ImmutableMap) {
            return ($ImmutableMap<K, V>)map;
        }
        final int size = map.size();
        switch (size) {
            default: {
                final Entry[] array = new Entry[size];
                int n = 0;
                for (final Map.Entry<Object, Object> entry : map.entrySet()) {
                    array[n] = entryOf((Object)entry.getKey(), (V)entry.getValue());
                    ++n;
                }
                return new RegularImmutableMap<K, V>(array);
            }
            case 0: {
                return of();
            }
            case 1: {
                final Map.Entry<Object, Object> entry2 = $Iterables.getOnlyElement(map.entrySet());
                return of((K)entry2.getKey(), (V)entry2.getValue());
            }
        }
    }
    
    private static <K, V> Entry<K, V> entryOf(final K k, final V v) {
        return $Maps.immutableEntry((K)$Preconditions.checkNotNull((K)k), (V)$Preconditions.checkNotNull((V)v));
    }
    
    public static <K, V> $ImmutableMap<K, V> of() {
        return ($ImmutableMap<K, V>)$ImmutableMap.EMPTY_IMMUTABLE_MAP;
    }
    
    public static <K, V> $ImmutableMap<K, V> of(final K k, final V v) {
        return new SingletonImmutableMap<K, V>((Object)$Preconditions.checkNotNull(k), (Object)$Preconditions.checkNotNull(v));
    }
    
    @Override
    public final void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public abstract $ImmutableSet<Entry<K, V>> entrySet();
    
    @Override
    public boolean equals(@$Nullable final Object o) {
        return o == this || (o instanceof Map && this.entrySet().equals(((Map)o).entrySet()));
    }
    
    @Override
    public abstract V get(@$Nullable final Object p0);
    
    @Override
    public int hashCode() {
        return this.entrySet().hashCode();
    }
    
    @Override
    public abstract $ImmutableSet<K> keySet();
    
    @Override
    public final V put(final K k, final V v) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final void putAll(final Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final V putIfAbsent(final K k, final V v) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final V remove(final Object o) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final boolean remove(final Object o, final Object o2) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final V replace(final K k, final V v) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final boolean replace(final K k, final V v, final V v2) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String toString() {
        final StringBuilder append = new StringBuilder(this.size() * 16).append('{');
        final $UnmodifiableIterator<Entry<K, V>> iterator = this.entrySet().iterator();
        append.append(iterator.next());
        while (iterator.hasNext()) {
            append.append(", ").append(iterator.next());
        }
        return append.append('}').toString();
    }
    
    @Override
    public abstract $ImmutableCollection<V> values();
    
    public static class Builder<K, V>
    {
        final List<Entry<K, V>> entries;
        
        public Builder() {
            this.entries = (List<Entry<K, V>>)$Lists.newArrayList();
        }
        
        private static <K, V> $ImmutableMap<K, V> fromEntryList(final List<Entry<K, V>> list) {
            switch (list.size()) {
                default: {
                    return new RegularImmutableMap<K, V>((Entry[])list.toArray((Map.Entry[])new Entry[list.size()]));
                }
                case 0: {
                    return $ImmutableMap.of();
                }
                case 1: {
                    return new SingletonImmutableMap<K, V>((Entry)$Iterables.getOnlyElement((Iterable<Map.Entry>)list));
                }
            }
        }
        
        public $ImmutableMap<K, V> build() {
            return fromEntryList(this.entries);
        }
        
        public Builder<K, V> put(final K k, final V v) {
            this.entries.add(entryOf(k, v));
            return this;
        }
        
        public Builder<K, V> putAll(final Map<? extends K, ? extends V> map) {
            for (final Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
                this.put(entry.getKey(), (V)entry.getValue());
            }
            return this;
        }
    }
    
    private static final class EmptyImmutableMap extends $ImmutableMap<Object, Object>
    {
        @Override
        public boolean containsKey(final Object o) {
            return false;
        }
        
        @Override
        public boolean containsValue(final Object o) {
            return false;
        }
        
        @Override
        public $ImmutableSet<Entry<Object, Object>> entrySet() {
            return $ImmutableSet.of();
        }
        
        @Override
        public boolean equals(@$Nullable final Object o) {
            return o instanceof Map && ((Map)o).isEmpty();
        }
        
        @Override
        public Object get(final Object o) {
            return null;
        }
        
        @Override
        public int hashCode() {
            return 0;
        }
        
        @Override
        public boolean isEmpty() {
            return true;
        }
        
        @Override
        public $ImmutableSet<Object> keySet() {
            return $ImmutableSet.of();
        }
        
        @Override
        public int size() {
            return 0;
        }
        
        @Override
        public String toString() {
            return "{}";
        }
        
        @Override
        public $ImmutableCollection<Object> values() {
            return $ImmutableCollection.EMPTY_IMMUTABLE_COLLECTION;
        }
    }
    
    private static final class RegularImmutableMap<K, V> extends $ImmutableMap<K, V>
    {
        private final transient Entry<K, V>[] entries;
        private transient $ImmutableSet<Entry<K, V>> entrySet;
        private transient $ImmutableSet<K> keySet;
        private final transient int keySetHashCode;
        private final transient int mask;
        private final transient Object[] table;
        private transient $ImmutableCollection<V> values;
        
        private RegularImmutableMap(final Entry<?, ?>... array) {
            this.entries = (Entry<K, V>[])array;
            final int chooseTableSize = $Hashing.chooseTableSize(((Entry<K, V>[])array).length);
            this.table = new Object[chooseTableSize * 2];
            this.mask = chooseTableSize - 1;
            int keySetHashCode = 0;
            final Entry<K, V>[] entries = this.entries;
            for (int length = entries.length, i = 0; i < length; ++i) {
                final Entry<K, V> entry = entries[i];
                final K key = entry.getKey();
                final int hashCode = key.hashCode();
                int smear = $Hashing.smear(hashCode);
                while (true) {
                    final int n = (this.mask & smear) * 2;
                    final Object o = this.table[n];
                    if (o == null) {
                        final V value = entry.getValue();
                        this.table[n] = key;
                        this.table[n + 1] = value;
                        keySetHashCode += hashCode;
                        break;
                    }
                    if (o.equals(key)) {
                        throw new IllegalArgumentException("duplicate key: " + key);
                    }
                    ++smear;
                }
            }
            this.keySetHashCode = keySetHashCode;
        }
        
        @Override
        public boolean containsKey(final Object o) {
            return this.get(o) != null;
        }
        
        @Override
        public boolean containsValue(final Object o) {
            if (o != null) {
                final Entry<K, V>[] entries = this.entries;
                for (int length = entries.length, i = 0; i < length; ++i) {
                    if (entries[i].getValue().equals(o)) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        @Override
        public $ImmutableSet<Entry<K, V>> entrySet() {
            $ImmutableSet<Entry<K, V>> entrySet;
            if ((entrySet = this.entrySet) == null) {
                entrySet = ($ImmutableSet<Entry<K, V>>)new EntrySet((RegularImmutableMap<Object, Object>)this);
                this.entrySet = entrySet;
            }
            return entrySet;
        }
        
        @Override
        public V get(final Object o) {
            if (o != null) {
                int smear = $Hashing.smear(o.hashCode());
                int n;
                while (true) {
                    n = (this.mask & smear) * 2;
                    final Object o2 = this.table[n];
                    if (o2 == null) {
                        return null;
                    }
                    if (o2.equals(o)) {
                        break;
                    }
                    ++smear;
                }
                return (V)this.table[n + 1];
            }
            return null;
        }
        
        @Override
        public boolean isEmpty() {
            return false;
        }
        
        @Override
        public $ImmutableSet<K> keySet() {
            $ImmutableSet<K> keySet;
            if ((keySet = this.keySet) == null) {
                keySet = ($ImmutableSet<K>)new KeySet((RegularImmutableMap<Object, Object>)this);
                this.keySet = keySet;
            }
            return keySet;
        }
        
        @Override
        public int size() {
            return this.entries.length;
        }
        
        @Override
        public String toString() {
            final StringBuilder append = new StringBuilder(this.size() * 16).append('{').append(this.entries[0]);
            for (int i = 1; i < this.entries.length; ++i) {
                append.append(", ").append(this.entries[i].toString());
            }
            return append.append('}').toString();
        }
        
        @Override
        public $ImmutableCollection<V> values() {
            $ImmutableCollection<V> values;
            if ((values = this.values) == null) {
                values = new Values<V>(this);
                this.values = values;
            }
            return values;
        }
        
        private static class EntrySet<K, V> extends ArrayImmutableSet<Entry<K, V>>
        {
            final RegularImmutableMap<K, V> map;
            
            EntrySet(final RegularImmutableMap<K, V> map) {
                super(((RegularImmutableMap<Object, Object>)map).entries);
                this.map = map;
            }
            
            @Override
            public boolean contains(final Object o) {
                boolean b2;
                final boolean b = b2 = false;
                if (o instanceof Entry) {
                    final Entry entry = (Entry)o;
                    final V value = this.map.get(entry.getKey());
                    b2 = b;
                    if (value != null) {
                        b2 = b;
                        if (value.equals(entry.getValue())) {
                            b2 = true;
                        }
                    }
                }
                return b2;
            }
        }
        
        private static class KeySet<K, V> extends TransformedImmutableSet<Entry<K, V>, K>
        {
            final RegularImmutableMap<K, V> map;
            
            KeySet(final RegularImmutableMap<K, V> map) {
                super((Map.Entry[])((RegularImmutableMap<Object, Object>)map).entries, ((RegularImmutableMap<Object, Object>)map).keySetHashCode);
                this.map = map;
            }
            
            @Override
            public boolean contains(final Object o) {
                return this.map.containsKey(o);
            }
            
            K transform(final Entry<K, V> entry) {
                return entry.getKey();
            }
        }
        
        private static class Values<V> extends $ImmutableCollection<V>
        {
            final RegularImmutableMap<?, V> map;
            
            Values(final RegularImmutableMap<?, V> map) {
                this.map = map;
            }
            
            @Override
            public boolean contains(final Object o) {
                return this.map.containsValue(o);
            }
            
            @Override
            public boolean isEmpty() {
                return false;
            }
            
            @Override
            public $UnmodifiableIterator<V> iterator() {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: new             Lcom/google/inject/internal/util/$ImmutableMap$RegularImmutableMap$Values$1;
                //     3: dup            
                //     4: aload_0        
                //     5: invokespecial   com/google/inject/internal/util/$ImmutableMap$RegularImmutableMap$Values$1.<init>:(Lcom/google/inject/internal/util/$ImmutableMap$RegularImmutableMap$Values;)V
                //     8: invokestatic    invokestatic   !!! ERROR
                //    11: areturn        
                //    Signature:
                //  ()Lcom/google/inject/internal/util/$UnmodifiableIterator<TV;>;
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
                return ((RegularImmutableMap<Object, Object>)this.map).entries.length;
            }
        }
    }
    
    private static class SerializedForm implements Serializable
    {
    }
    
    private static final class SingletonImmutableMap<K, V> extends $ImmutableMap<K, V>
    {
        private transient Entry<K, V> entry;
        private transient $ImmutableSet<Entry<K, V>> entrySet;
        private transient $ImmutableSet<K> keySet;
        private final transient K singleKey;
        private final transient V singleValue;
        private transient $ImmutableCollection<V> values;
        
        private SingletonImmutableMap(final K singleKey, final V singleValue) {
            this.singleKey = singleKey;
            this.singleValue = singleValue;
        }
        
        private SingletonImmutableMap(final Entry<K, V> entry) {
            this.entry = entry;
            this.singleKey = entry.getKey();
            this.singleValue = entry.getValue();
        }
        
        private Entry<K, V> entry() {
            Entry<K, V> entry;
            if ((entry = this.entry) == null) {
                entry = $Maps.immutableEntry(this.singleKey, this.singleValue);
                this.entry = entry;
            }
            return entry;
        }
        
        @Override
        public boolean containsKey(final Object o) {
            return this.singleKey.equals(o);
        }
        
        @Override
        public boolean containsValue(final Object o) {
            return this.singleValue.equals(o);
        }
        
        @Override
        public $ImmutableSet<Entry<K, V>> entrySet() {
            $ImmutableSet<Entry<K, V>> entrySet;
            if ((entrySet = this.entrySet) == null) {
                entrySet = $ImmutableSet.of(this.entry());
                this.entrySet = entrySet;
            }
            return entrySet;
        }
        
        @Override
        public boolean equals(@$Nullable final Object o) {
            if (o != this) {
                if (!(o instanceof Map)) {
                    return false;
                }
                final Map map = (Map)o;
                if (map.size() != 1) {
                    return false;
                }
                final Map.Entry<Object, V> entry = map.entrySet().iterator().next();
                if (!this.singleKey.equals(entry.getKey()) || !this.singleValue.equals(entry.getValue())) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public V get(final Object o) {
            if (this.singleKey.equals(o)) {
                return this.singleValue;
            }
            return null;
        }
        
        @Override
        public int hashCode() {
            return this.singleKey.hashCode() ^ this.singleValue.hashCode();
        }
        
        @Override
        public boolean isEmpty() {
            return false;
        }
        
        @Override
        public $ImmutableSet<K> keySet() {
            $ImmutableSet<K> keySet;
            if ((keySet = this.keySet) == null) {
                keySet = $ImmutableSet.of(this.singleKey);
                this.keySet = keySet;
            }
            return keySet;
        }
        
        @Override
        public int size() {
            return 1;
        }
        
        @Override
        public String toString() {
            return '{' + this.singleKey.toString() + '=' + this.singleValue.toString() + '}';
        }
        
        @Override
        public $ImmutableCollection<V> values() {
            $ImmutableCollection<V> values;
            if ((values = this.values) == null) {
                values = new Values<V>(this.singleValue);
                this.values = values;
            }
            return values;
        }
        
        private static class Values<V> extends $ImmutableCollection<V>
        {
            final V singleValue;
            
            Values(final V singleValue) {
                this.singleValue = singleValue;
            }
            
            @Override
            public boolean contains(final Object o) {
                return this.singleValue.equals(o);
            }
            
            @Override
            public boolean isEmpty() {
                return false;
            }
            
            @Override
            public $UnmodifiableIterator<V> iterator() {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: aload_0        
                //     1: getfield        com/google/inject/internal/util/$ImmutableMap$SingletonImmutableMap$Values.singleValue:Ljava/lang/Object;
                //     4: invokestatic    invokestatic   !!! ERROR
                //     7: areturn        
                //    Signature:
                //  ()Lcom/google/inject/internal/util/$UnmodifiableIterator<TV;>;
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
        }
    }
}
