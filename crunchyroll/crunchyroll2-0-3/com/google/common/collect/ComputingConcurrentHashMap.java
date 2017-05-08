// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import com.google.common.base.Throwables;
import java.io.Serializable;
import java.lang.ref.ReferenceQueue;
import java.util.concurrent.ExecutionException;
import com.google.common.base.Preconditions;
import com.google.common.base.Function;

class ComputingConcurrentHashMap<K, V> extends MapMakerInternalMap<K, V>
{
    final Function<? super K, ? extends V> computingFunction;
    
    ComputingConcurrentHashMap(final MapMaker mapMaker, final Function<? super K, ? extends V> function) {
        super(mapMaker);
        this.computingFunction = Preconditions.checkNotNull(function);
    }
    
    @Override
    Segment<K, V> createSegment(final int n, final int n2) {
        return new ComputingSegment<K, V>(this, n, n2);
    }
    
    V getOrCompute(final K k) throws ExecutionException {
        final int hash = this.hash(Preconditions.checkNotNull(k));
        return this.segmentFor(hash).getOrCompute(k, hash, this.computingFunction);
    }
    
    ComputingSegment<K, V> segmentFor(final int n) {
        return (ComputingSegment<K, V>)(ComputingSegment)super.segmentFor(n);
    }
    
    private static final class ComputationExceptionReference<K, V> implements ValueReference<K, V>
    {
        final Throwable t;
        
        ComputationExceptionReference(final Throwable t) {
            this.t = t;
        }
        
        @Override
        public void clear(final ValueReference<K, V> valueReference) {
        }
        
        @Override
        public ValueReference<K, V> copyFor(final ReferenceQueue<V> referenceQueue, final V v, final ReferenceEntry<K, V> referenceEntry) {
            return this;
        }
        
        @Override
        public V get() {
            return null;
        }
        
        @Override
        public ReferenceEntry<K, V> getEntry() {
            return null;
        }
        
        @Override
        public boolean isComputingReference() {
            return false;
        }
        
        @Override
        public V waitForValue() throws ExecutionException {
            throw new ExecutionException(this.t);
        }
    }
    
    private static final class ComputedReference<K, V> implements ValueReference<K, V>
    {
        final V value;
        
        ComputedReference(final V value) {
            this.value = value;
        }
        
        @Override
        public void clear(final ValueReference<K, V> valueReference) {
        }
        
        @Override
        public ValueReference<K, V> copyFor(final ReferenceQueue<V> referenceQueue, final V v, final ReferenceEntry<K, V> referenceEntry) {
            return this;
        }
        
        @Override
        public V get() {
            return this.value;
        }
        
        @Override
        public ReferenceEntry<K, V> getEntry() {
            return null;
        }
        
        @Override
        public boolean isComputingReference() {
            return false;
        }
        
        @Override
        public V waitForValue() {
            return this.get();
        }
    }
    
    static final class ComputingMapAdapter<K, V> extends ComputingConcurrentHashMap<K, V> implements Serializable
    {
        ComputingMapAdapter(final MapMaker mapMaker, final Function<? super K, ? extends V> function) {
            super(mapMaker, (Function<? super Object, ?>)function);
        }
        
        @Override
        public V get(final Object o) {
            V orCompute;
            try {
                orCompute = this.getOrCompute((K)o);
                if (orCompute == null) {
                    throw new NullPointerException(this.computingFunction + " returned null for key " + o + ".");
                }
            }
            catch (ExecutionException ex) {
                final Throwable cause = ex.getCause();
                Throwables.propagateIfInstanceOf(cause, ComputationException.class);
                throw new ComputationException(cause);
            }
            return orCompute;
        }
    }
    
    static final class ComputingSegment<K, V> extends Segment<K, V>
    {
        ComputingSegment(final MapMakerInternalMap<K, V> mapMakerInternalMap, final int n, final int n2) {
            super(mapMakerInternalMap, n, n2);
        }
        
        V compute(final K k, final int n, final ReferenceEntry<K, V> referenceEntry, final ComputingValueReference<K, V> computingValueReference) throws ExecutionException {
            V v = null;
            final V v2 = null;
            System.nanoTime();
            long nanoTime;
            final long n2 = nanoTime = 0L;
            try {
                // monitorenter(referenceEntry)
                nanoTime = n2;
                v = v2;
                try {
                    final V compute = computingValueReference.compute(k, n);
                    nanoTime = n2;
                    v = compute;
                    final long n3 = nanoTime = System.nanoTime();
                    v = compute;
                    // monitorexit(referenceEntry)
                    if (compute != null) {
                        nanoTime = n3;
                        v = compute;
                        if (this.put(k, n, compute, true) != null) {
                            nanoTime = n3;
                            v = compute;
                            this.enqueueNotification(k, n, compute, MapMaker.RemovalCause.REPLACED);
                        }
                    }
                    return compute;
                }
                finally {
                }
                // monitorexit(referenceEntry)
            }
            finally {
                if (nanoTime == 0L) {
                    System.nanoTime();
                }
                if (v == null) {
                    this.clearValue(k, n, computingValueReference);
                }
            }
        }
        
        V getOrCompute(final K p0, final int p1, final Function<? super K, ? extends V> p2) throws ExecutionException {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: aload_1        
            //     2: iload_2        
            //     3: invokevirtual   com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.getEntry:(Ljava/lang/Object;I)Lcom/google/common/collect/MapMakerInternalMap$ReferenceEntry;
            //     6: astore          10
            //     8: aload           10
            //    10: ifnull          39
            //    13: aload_0        
            //    14: aload           10
            //    16: invokevirtual   com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.getLiveValue:(Lcom/google/common/collect/MapMakerInternalMap$ReferenceEntry;)Ljava/lang/Object;
            //    19: astore          9
            //    21: aload           9
            //    23: ifnull          39
            //    26: aload_0        
            //    27: aload           10
            //    29: invokevirtual   com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.recordRead:(Lcom/google/common/collect/MapMakerInternalMap$ReferenceEntry;)V
            //    32: aload_0        
            //    33: invokevirtual   com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.postReadCleanup:()V
            //    36: aload           9
            //    38: areturn        
            //    39: aload           10
            //    41: ifnull          63
            //    44: aload           10
            //    46: astore          9
            //    48: aload           10
            //    50: invokeinterface com/google/common/collect/MapMakerInternalMap$ReferenceEntry.getValueReference:()Lcom/google/common/collect/MapMakerInternalMap$ValueReference;
            //    55: invokeinterface com/google/common/collect/MapMakerInternalMap$ValueReference.isComputingReference:()Z
            //    60: ifne            456
            //    63: iconst_1       
            //    64: istore          5
            //    66: aconst_null    
            //    67: astore          11
            //    69: aload_0        
            //    70: invokevirtual   com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.lock:()V
            //    73: aload_0        
            //    74: invokevirtual   com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.preWriteCleanup:()V
            //    77: aload_0        
            //    78: getfield        com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.count:I
            //    81: istore          7
            //    83: aload_0        
            //    84: getfield        com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.table:Ljava/util/concurrent/atomic/AtomicReferenceArray;
            //    87: astore          13
            //    89: iload_2        
            //    90: aload           13
            //    92: invokevirtual   java/util/concurrent/atomic/AtomicReferenceArray.length:()I
            //    95: iconst_1       
            //    96: isub           
            //    97: iand           
            //    98: istore          6
            //   100: aload           13
            //   102: iload           6
            //   104: invokevirtual   java/util/concurrent/atomic/AtomicReferenceArray.get:(I)Ljava/lang/Object;
            //   107: checkcast       Lcom/google/common/collect/MapMakerInternalMap$ReferenceEntry;
            //   110: astore          12
            //   112: aload           12
            //   114: astore          9
            //   116: iload           5
            //   118: istore          4
            //   120: aload           9
            //   122: ifnull          184
            //   125: aload           9
            //   127: invokeinterface com/google/common/collect/MapMakerInternalMap$ReferenceEntry.getKey:()Ljava/lang/Object;
            //   132: astore          10
            //   134: aload           9
            //   136: invokeinterface com/google/common/collect/MapMakerInternalMap$ReferenceEntry.getHash:()I
            //   141: iload_2        
            //   142: if_icmpne       424
            //   145: aload           10
            //   147: ifnull          424
            //   150: aload_0        
            //   151: getfield        com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.map:Lcom/google/common/collect/MapMakerInternalMap;
            //   154: getfield        com/google/common/collect/MapMakerInternalMap.keyEquivalence:Lcom/google/common/base/Equivalence;
            //   157: aload_1        
            //   158: aload           10
            //   160: invokevirtual   com/google/common/base/Equivalence.equivalent:(Ljava/lang/Object;Ljava/lang/Object;)Z
            //   163: ifeq            424
            //   166: aload           9
            //   168: invokeinterface com/google/common/collect/MapMakerInternalMap$ReferenceEntry.getValueReference:()Lcom/google/common/collect/MapMakerInternalMap$ValueReference;
            //   173: invokeinterface com/google/common/collect/MapMakerInternalMap$ValueReference.isComputingReference:()Z
            //   178: ifeq            278
            //   181: iconst_0       
            //   182: istore          4
            //   184: aload           9
            //   186: astore          10
            //   188: iload           4
            //   190: ifeq            244
            //   193: new             Lcom/google/common/collect/ComputingConcurrentHashMap$ComputingValueReference;
            //   196: dup            
            //   197: aload_3        
            //   198: invokespecial   com/google/common/collect/ComputingConcurrentHashMap$ComputingValueReference.<init>:(Lcom/google/common/base/Function;)V
            //   201: astore          10
            //   203: aload           9
            //   205: ifnonnull       436
            //   208: aload_0        
            //   209: aload_1        
            //   210: iload_2        
            //   211: aload           12
            //   213: invokevirtual   com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.newEntry:(Ljava/lang/Object;ILcom/google/common/collect/MapMakerInternalMap$ReferenceEntry;)Lcom/google/common/collect/MapMakerInternalMap$ReferenceEntry;
            //   216: astore          9
            //   218: aload           9
            //   220: aload           10
            //   222: invokeinterface com/google/common/collect/MapMakerInternalMap$ReferenceEntry.setValueReference:(Lcom/google/common/collect/MapMakerInternalMap$ValueReference;)V
            //   227: aload           13
            //   229: iload           6
            //   231: aload           9
            //   233: invokevirtual   java/util/concurrent/atomic/AtomicReferenceArray.set:(ILjava/lang/Object;)V
            //   236: aload           10
            //   238: astore          11
            //   240: aload           9
            //   242: astore          10
            //   244: aload_0        
            //   245: invokevirtual   com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.unlock:()V
            //   248: aload_0        
            //   249: invokevirtual   com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.postWriteCleanup:()V
            //   252: aload           10
            //   254: astore          9
            //   256: iload           4
            //   258: ifeq            456
            //   261: aload_0        
            //   262: aload_1        
            //   263: iload_2        
            //   264: aload           10
            //   266: aload           11
            //   268: invokevirtual   com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.compute:(Ljava/lang/Object;ILcom/google/common/collect/MapMakerInternalMap$ReferenceEntry;Lcom/google/common/collect/ComputingConcurrentHashMap$ComputingValueReference;)Ljava/lang/Object;
            //   271: astore_1       
            //   272: aload_0        
            //   273: invokevirtual   com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.postReadCleanup:()V
            //   276: aload_1        
            //   277: areturn        
            //   278: aload           9
            //   280: invokeinterface com/google/common/collect/MapMakerInternalMap$ReferenceEntry.getValueReference:()Lcom/google/common/collect/MapMakerInternalMap$ValueReference;
            //   285: invokeinterface com/google/common/collect/MapMakerInternalMap$ValueReference.get:()Ljava/lang/Object;
            //   290: astore          14
            //   292: aload           14
            //   294: ifnonnull       366
            //   297: aload_0        
            //   298: aload           10
            //   300: iload_2        
            //   301: aload           14
            //   303: getstatic       com/google/common/collect/MapMaker$RemovalCause.COLLECTED:Lcom/google/common/collect/MapMaker$RemovalCause;
            //   306: invokevirtual   com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.enqueueNotification:(Ljava/lang/Object;ILjava/lang/Object;Lcom/google/common/collect/MapMaker$RemovalCause;)V
            //   309: aload_0        
            //   310: getfield        com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.evictionQueue:Ljava/util/Queue;
            //   313: aload           9
            //   315: invokeinterface java/util/Queue.remove:(Ljava/lang/Object;)Z
            //   320: pop            
            //   321: aload_0        
            //   322: getfield        com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.expirationQueue:Ljava/util/Queue;
            //   325: aload           9
            //   327: invokeinterface java/util/Queue.remove:(Ljava/lang/Object;)Z
            //   332: pop            
            //   333: aload_0        
            //   334: iload           7
            //   336: iconst_1       
            //   337: isub           
            //   338: putfield        com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.count:I
            //   341: iload           5
            //   343: istore          4
            //   345: goto            184
            //   348: astore_1       
            //   349: aload_0        
            //   350: invokevirtual   com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.unlock:()V
            //   353: aload_0        
            //   354: invokevirtual   com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.postWriteCleanup:()V
            //   357: aload_1        
            //   358: athrow         
            //   359: astore_1       
            //   360: aload_0        
            //   361: invokevirtual   com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.postReadCleanup:()V
            //   364: aload_1        
            //   365: athrow         
            //   366: aload_0        
            //   367: getfield        com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.map:Lcom/google/common/collect/MapMakerInternalMap;
            //   370: invokevirtual   com/google/common/collect/MapMakerInternalMap.expires:()Z
            //   373: ifeq            403
            //   376: aload_0        
            //   377: getfield        com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.map:Lcom/google/common/collect/MapMakerInternalMap;
            //   380: aload           9
            //   382: invokevirtual   com/google/common/collect/MapMakerInternalMap.isExpired:(Lcom/google/common/collect/MapMakerInternalMap$ReferenceEntry;)Z
            //   385: ifeq            403
            //   388: aload_0        
            //   389: aload           10
            //   391: iload_2        
            //   392: aload           14
            //   394: getstatic       com/google/common/collect/MapMaker$RemovalCause.EXPIRED:Lcom/google/common/collect/MapMaker$RemovalCause;
            //   397: invokevirtual   com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.enqueueNotification:(Ljava/lang/Object;ILjava/lang/Object;Lcom/google/common/collect/MapMaker$RemovalCause;)V
            //   400: goto            309
            //   403: aload_0        
            //   404: aload           9
            //   406: invokevirtual   com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.recordLockedRead:(Lcom/google/common/collect/MapMakerInternalMap$ReferenceEntry;)V
            //   409: aload_0        
            //   410: invokevirtual   com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.unlock:()V
            //   413: aload_0        
            //   414: invokevirtual   com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.postWriteCleanup:()V
            //   417: aload_0        
            //   418: invokevirtual   com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.postReadCleanup:()V
            //   421: aload           14
            //   423: areturn        
            //   424: aload           9
            //   426: invokeinterface com/google/common/collect/MapMakerInternalMap$ReferenceEntry.getNext:()Lcom/google/common/collect/MapMakerInternalMap$ReferenceEntry;
            //   431: astore          9
            //   433: goto            116
            //   436: aload           9
            //   438: aload           10
            //   440: invokeinterface com/google/common/collect/MapMakerInternalMap$ReferenceEntry.setValueReference:(Lcom/google/common/collect/MapMakerInternalMap$ValueReference;)V
            //   445: aload           10
            //   447: astore          11
            //   449: aload           9
            //   451: astore          10
            //   453: goto            244
            //   456: aload           9
            //   458: invokestatic    java/lang/Thread.holdsLock:(Ljava/lang/Object;)Z
            //   461: ifne            506
            //   464: iconst_1       
            //   465: istore          8
            //   467: iload           8
            //   469: ldc             "Recursive computation"
            //   471: invokestatic    com/google/common/base/Preconditions.checkState:(ZLjava/lang/Object;)V
            //   474: aload           9
            //   476: invokeinterface com/google/common/collect/MapMakerInternalMap$ReferenceEntry.getValueReference:()Lcom/google/common/collect/MapMakerInternalMap$ValueReference;
            //   481: invokeinterface com/google/common/collect/MapMakerInternalMap$ValueReference.waitForValue:()Ljava/lang/Object;
            //   486: astore          10
            //   488: aload           10
            //   490: ifnull          0
            //   493: aload_0        
            //   494: aload           9
            //   496: invokevirtual   com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.recordRead:(Lcom/google/common/collect/MapMakerInternalMap$ReferenceEntry;)V
            //   499: aload_0        
            //   500: invokevirtual   com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.postReadCleanup:()V
            //   503: aload           10
            //   505: areturn        
            //   506: iconst_0       
            //   507: istore          8
            //   509: goto            467
            //   512: astore_1       
            //   513: goto            349
            //    Exceptions:
            //  throws java.util.concurrent.ExecutionException
            //    Signature:
            //  (TK;ILcom/google/common/base/Function<-TK;+TV;>;)TV;
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  ----
            //  0      8      359    366    Any
            //  13     21     359    366    Any
            //  26     32     359    366    Any
            //  48     63     359    366    Any
            //  69     73     359    366    Any
            //  73     112    348    349    Any
            //  125    145    348    349    Any
            //  150    181    348    349    Any
            //  193    203    348    349    Any
            //  208    236    512    516    Any
            //  244    252    359    366    Any
            //  261    272    359    366    Any
            //  278    292    348    349    Any
            //  297    309    348    349    Any
            //  309    341    348    349    Any
            //  349    359    359    366    Any
            //  366    400    348    349    Any
            //  403    409    348    349    Any
            //  409    417    359    366    Any
            //  424    433    348    349    Any
            //  436    445    512    516    Any
            //  456    464    359    366    Any
            //  467    488    359    366    Any
            //  493    499    359    366    Any
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0244:
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
    }
    
    private static final class ComputingValueReference<K, V> implements ValueReference<K, V>
    {
        volatile ValueReference<K, V> computedReference;
        final Function<? super K, ? extends V> computingFunction;
        
        public ComputingValueReference(final Function<? super K, ? extends V> computingFunction) {
            this.computedReference = MapMakerInternalMap.unset();
            this.computingFunction = computingFunction;
        }
        
        @Override
        public void clear(final ValueReference<K, V> valueReference) {
            this.setValueReference(valueReference);
        }
        
        V compute(final K k, final int n) throws ExecutionException {
            try {
                final V apply = (V)this.computingFunction.apply((Object)k);
                this.setValueReference(new ComputedReference<K, V>(apply));
                return apply;
            }
            catch (Throwable t) {
                this.setValueReference(new ComputationExceptionReference<K, V>(t));
                throw new ExecutionException(t);
            }
        }
        
        @Override
        public ValueReference<K, V> copyFor(final ReferenceQueue<V> referenceQueue, final V v, final ReferenceEntry<K, V> referenceEntry) {
            return this;
        }
        
        @Override
        public V get() {
            return null;
        }
        
        @Override
        public ReferenceEntry<K, V> getEntry() {
            return null;
        }
        
        @Override
        public boolean isComputingReference() {
            return true;
        }
        
        void setValueReference(final ValueReference<K, V> computedReference) {
            synchronized (this) {
                if (this.computedReference == MapMakerInternalMap.UNSET) {
                    this.computedReference = computedReference;
                    this.notifyAll();
                }
            }
        }
        
        @Override
        public V waitForValue() throws ExecutionException {
            Label_0059: {
                if (this.computedReference != MapMakerInternalMap.UNSET) {
                    break Label_0059;
                }
                boolean b = false;
                final boolean b2 = false;
                try {
                    // monitorenter(this)
                    b = b2;
                    try {
                        while (this.computedReference == MapMakerInternalMap.UNSET) {
                            try {
                                this.wait();
                            }
                            catch (InterruptedException ex) {
                                b = true;
                            }
                        }
                        // monitorexit(this)
                        if (b) {
                            Thread.currentThread().interrupt();
                        }
                        return this.computedReference.waitForValue();
                    }
                    finally {
                    }
                    // monitorexit(this)
                }
                finally {
                    if (b) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
}
