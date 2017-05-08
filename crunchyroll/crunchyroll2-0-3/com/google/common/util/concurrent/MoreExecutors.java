// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.util.concurrent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Callable;
import java.util.Collection;

public final class MoreExecutors
{
    static <T> T invokeAnyImpl(final ListeningExecutorService p0, final Collection<? extends Callable<T>> p1, final boolean p2, final long p3) throws InterruptedException, ExecutionException, TimeoutException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_1        
        //     1: invokeinterface java/util/Collection.size:()I
        //     6: istore          5
        //     8: iload           5
        //    10: ifle            228
        //    13: iconst_1       
        //    14: istore          9
        //    16: iload           9
        //    18: invokestatic    com/google/common/base/Preconditions.checkArgument:(Z)V
        //    21: iload           5
        //    23: invokestatic    com/google/common/collect/Lists.newArrayListWithCapacity:(I)Ljava/util/ArrayList;
        //    26: astore          18
        //    28: invokestatic    com/google/common/collect/Queues.newLinkedBlockingQueue:()Ljava/util/concurrent/LinkedBlockingQueue;
        //    31: astore          19
        //    33: iload_2        
        //    34: ifeq            234
        //    37: invokestatic    java/lang/System.nanoTime:()J
        //    40: lstore          10
        //    42: aload_1        
        //    43: invokeinterface java/util/Collection.iterator:()Ljava/util/Iterator;
        //    48: astore          20
        //    50: aload           18
        //    52: aload_0        
        //    53: aload           20
        //    55: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    60: checkcast       Ljava/util/concurrent/Callable;
        //    63: aload           19
        //    65: invokestatic    com/google/common/util/concurrent/MoreExecutors.submitAndAddQueueListener:(Lcom/google/common/util/concurrent/ListeningExecutorService;Ljava/util/concurrent/Callable;Ljava/util/concurrent/BlockingQueue;)Lcom/google/common/util/concurrent/ListenableFuture;
        //    68: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //    73: pop            
        //    74: iload           5
        //    76: iconst_1       
        //    77: isub           
        //    78: istore          8
        //    80: iconst_1       
        //    81: istore          5
        //    83: aconst_null    
        //    84: astore_1       
        //    85: aload           19
        //    87: invokeinterface java/util/concurrent/BlockingQueue.poll:()Ljava/lang/Object;
        //    92: checkcast       Ljava/util/concurrent/Future;
        //    95: astore          17
        //    97: iload           5
        //    99: istore          6
        //   101: aload           17
        //   103: astore          16
        //   105: iload           8
        //   107: istore          7
        //   109: lload           10
        //   111: lstore          12
        //   113: lload_3        
        //   114: lstore          14
        //   116: aload           17
        //   118: ifnonnull       173
        //   121: iload           8
        //   123: ifle            240
        //   126: iload           8
        //   128: iconst_1       
        //   129: isub           
        //   130: istore          7
        //   132: aload           18
        //   134: aload_0        
        //   135: aload           20
        //   137: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   142: checkcast       Ljava/util/concurrent/Callable;
        //   145: aload           19
        //   147: invokestatic    com/google/common/util/concurrent/MoreExecutors.submitAndAddQueueListener:(Lcom/google/common/util/concurrent/ListeningExecutorService;Ljava/util/concurrent/Callable;Ljava/util/concurrent/BlockingQueue;)Lcom/google/common/util/concurrent/ListenableFuture;
        //   150: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   155: pop            
        //   156: iload           5
        //   158: iconst_1       
        //   159: iadd           
        //   160: istore          6
        //   162: lload_3        
        //   163: lstore          14
        //   165: lload           10
        //   167: lstore          12
        //   169: aload           17
        //   171: astore          16
        //   173: aload           16
        //   175: ifnull          407
        //   178: iload           6
        //   180: iconst_1       
        //   181: isub           
        //   182: istore          5
        //   184: aload           16
        //   186: invokeinterface java/util/concurrent/Future.get:()Ljava/lang/Object;
        //   191: astore_1       
        //   192: aload           18
        //   194: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   199: astore_0       
        //   200: aload_0        
        //   201: invokeinterface java/util/Iterator.hasNext:()Z
        //   206: ifeq            400
        //   209: aload_0        
        //   210: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   215: checkcast       Ljava/util/concurrent/Future;
        //   218: iconst_1       
        //   219: invokeinterface java/util/concurrent/Future.cancel:(Z)Z
        //   224: pop            
        //   225: goto            200
        //   228: iconst_0       
        //   229: istore          9
        //   231: goto            16
        //   234: lconst_0       
        //   235: lstore          10
        //   237: goto            42
        //   240: iload           5
        //   242: ifne            297
        //   245: aload_1        
        //   246: ifnonnull       402
        //   249: new             Ljava/util/concurrent/ExecutionException;
        //   252: dup            
        //   253: aconst_null    
        //   254: invokespecial   java/util/concurrent/ExecutionException.<init>:(Ljava/lang/Throwable;)V
        //   257: astore_0       
        //   258: aload_0        
        //   259: athrow         
        //   260: astore_0       
        //   261: aload           18
        //   263: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   268: astore_1       
        //   269: aload_1        
        //   270: invokeinterface java/util/Iterator.hasNext:()Z
        //   275: ifeq            398
        //   278: aload_1        
        //   279: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   284: checkcast       Ljava/util/concurrent/Future;
        //   287: iconst_1       
        //   288: invokeinterface java/util/concurrent/Future.cancel:(Z)Z
        //   293: pop            
        //   294: goto            269
        //   297: iload_2        
        //   298: ifeq            355
        //   301: aload           19
        //   303: lload_3        
        //   304: getstatic       java/util/concurrent/TimeUnit.NANOSECONDS:Ljava/util/concurrent/TimeUnit;
        //   307: invokeinterface java/util/concurrent/BlockingQueue.poll:(JLjava/util/concurrent/TimeUnit;)Ljava/lang/Object;
        //   312: checkcast       Ljava/util/concurrent/Future;
        //   315: astore          16
        //   317: aload           16
        //   319: ifnonnull       330
        //   322: new             Ljava/util/concurrent/TimeoutException;
        //   325: dup            
        //   326: invokespecial   java/util/concurrent/TimeoutException.<init>:()V
        //   329: athrow         
        //   330: invokestatic    java/lang/System.nanoTime:()J
        //   333: lstore          12
        //   335: lload_3        
        //   336: lload           12
        //   338: lload           10
        //   340: lsub           
        //   341: lsub           
        //   342: lstore          14
        //   344: iload           5
        //   346: istore          6
        //   348: iload           8
        //   350: istore          7
        //   352: goto            173
        //   355: aload           19
        //   357: invokeinterface java/util/concurrent/BlockingQueue.take:()Ljava/lang/Object;
        //   362: checkcast       Ljava/util/concurrent/Future;
        //   365: astore          16
        //   367: iload           5
        //   369: istore          6
        //   371: iload           8
        //   373: istore          7
        //   375: lload           10
        //   377: lstore          12
        //   379: lload_3        
        //   380: lstore          14
        //   382: goto            173
        //   385: astore_1       
        //   386: new             Ljava/util/concurrent/ExecutionException;
        //   389: dup            
        //   390: aload_1        
        //   391: invokespecial   java/util/concurrent/ExecutionException.<init>:(Ljava/lang/Throwable;)V
        //   394: astore_1       
        //   395: goto            419
        //   398: aload_0        
        //   399: athrow         
        //   400: aload_1        
        //   401: areturn        
        //   402: aload_1        
        //   403: astore_0       
        //   404: goto            258
        //   407: iload           6
        //   409: istore          5
        //   411: goto            419
        //   414: astore_0       
        //   415: goto            261
        //   418: astore_1       
        //   419: iload           7
        //   421: istore          8
        //   423: lload           12
        //   425: lstore          10
        //   427: lload           14
        //   429: lstore_3       
        //   430: goto            85
        //    Exceptions:
        //  throws java.lang.InterruptedException
        //  throws java.util.concurrent.ExecutionException
        //  throws java.util.concurrent.TimeoutException
        //    Signature:
        //  <T:Ljava/lang/Object;>(Lcom/google/common/util/concurrent/ListeningExecutorService;Ljava/util/Collection<+Ljava/util/concurrent/Callable<TT;>;>;ZJ)TT;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  37     42     260    261    Any
        //  42     74     260    261    Any
        //  85     97     414    418    Any
        //  132    156    414    418    Any
        //  184    192    418    419    Ljava/util/concurrent/ExecutionException;
        //  184    192    385    398    Ljava/lang/RuntimeException;
        //  184    192    414    418    Any
        //  249    258    414    418    Any
        //  258    260    260    261    Any
        //  301    317    414    418    Any
        //  322    330    414    418    Any
        //  330    335    414    418    Any
        //  355    367    414    418    Any
        //  386    395    414    418    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0085:
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
    
    public static ListeningExecutorService sameThreadExecutor() {
        return new SameThreadExecutorService();
    }
    
    private static <T> ListenableFuture<T> submitAndAddQueueListener(final ListeningExecutorService listeningExecutorService, final Callable<T> callable, final BlockingQueue<Future<T>> blockingQueue) {
        final ListenableFuture<T> submit = listeningExecutorService.submit(callable);
        submit.addListener(new Runnable() {
            @Override
            public void run() {
                blockingQueue.add(submit);
            }
        }, sameThreadExecutor());
        return submit;
    }
    
    private static class SameThreadExecutorService extends AbstractListeningExecutorService
    {
        private final Lock lock;
        private int runningTasks;
        private boolean shutdown;
        private final Condition termination;
        
        private SameThreadExecutorService() {
            this.lock = new ReentrantLock();
            this.termination = this.lock.newCondition();
            this.runningTasks = 0;
            this.shutdown = false;
        }
        
        private void endTask() {
            this.lock.lock();
            try {
                --this.runningTasks;
                if (this.isTerminated()) {
                    this.termination.signalAll();
                }
            }
            finally {
                this.lock.unlock();
            }
        }
        
        private void startTask() {
            this.lock.lock();
            try {
                if (this.isShutdown()) {
                    throw new RejectedExecutionException("Executor already shutdown");
                }
            }
            finally {
                this.lock.unlock();
            }
            ++this.runningTasks;
            this.lock.unlock();
        }
        
        @Override
        public boolean awaitTermination(long n, final TimeUnit timeUnit) throws InterruptedException {
            n = timeUnit.toNanos(n);
            this.lock.lock();
            try {
                while (!this.isTerminated()) {
                    if (n <= 0L) {
                        return false;
                    }
                    n = this.termination.awaitNanos(n);
                }
                return true;
            }
            finally {
                this.lock.unlock();
            }
        }
        
        @Override
        public void execute(final Runnable runnable) {
            this.startTask();
            try {
                runnable.run();
            }
            finally {
                this.endTask();
            }
        }
        
        @Override
        public boolean isShutdown() {
            this.lock.lock();
            try {
                return this.shutdown;
            }
            finally {
                this.lock.unlock();
            }
        }
        
        @Override
        public boolean isTerminated() {
            this.lock.lock();
            try {
                return this.shutdown && this.runningTasks == 0;
            }
            finally {
                this.lock.unlock();
            }
        }
        
        @Override
        public void shutdown() {
            this.lock.lock();
            try {
                this.shutdown = true;
            }
            finally {
                this.lock.unlock();
            }
        }
        
        @Override
        public List<Runnable> shutdownNow() {
            this.shutdown();
            return Collections.emptyList();
        }
    }
}
