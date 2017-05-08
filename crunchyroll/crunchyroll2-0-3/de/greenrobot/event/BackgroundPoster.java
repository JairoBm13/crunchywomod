// 
// Decompiled by Procyon v0.5.30
// 

package de.greenrobot.event;

final class BackgroundPoster implements Runnable
{
    private final EventBus eventBus;
    private volatile boolean executorRunning;
    private final PendingPostQueue queue;
    
    BackgroundPoster(final EventBus eventBus) {
        this.eventBus = eventBus;
        this.queue = new PendingPostQueue();
    }
    
    public void enqueue(final Subscription subscription, final Object o) {
        final PendingPost obtainPendingPost = PendingPost.obtainPendingPost(subscription, o);
        synchronized (this) {
            this.queue.enqueue(obtainPendingPost);
            if (!this.executorRunning) {
                this.executorRunning = true;
                this.eventBus.getExecutorService().execute(this);
            }
        }
    }
    
    @Override
    public void run() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: getfield        de/greenrobot/event/BackgroundPoster.queue:Lde/greenrobot/event/PendingPostQueue;
        //     4: sipush          1000
        //     7: invokevirtual   de/greenrobot/event/PendingPostQueue.poll:(I)Lde/greenrobot/event/PendingPost;
        //    10: astore_2       
        //    11: aload_2        
        //    12: astore_1       
        //    13: aload_2        
        //    14: ifnonnull       46
        //    17: aload_0        
        //    18: monitorenter   
        //    19: aload_0        
        //    20: getfield        de/greenrobot/event/BackgroundPoster.queue:Lde/greenrobot/event/PendingPostQueue;
        //    23: invokevirtual   de/greenrobot/event/PendingPostQueue.poll:()Lde/greenrobot/event/PendingPost;
        //    26: astore_1       
        //    27: aload_1        
        //    28: ifnonnull       44
        //    31: aload_0        
        //    32: iconst_0       
        //    33: putfield        de/greenrobot/event/BackgroundPoster.executorRunning:Z
        //    36: aload_0        
        //    37: monitorexit    
        //    38: aload_0        
        //    39: iconst_0       
        //    40: putfield        de/greenrobot/event/BackgroundPoster.executorRunning:Z
        //    43: return         
        //    44: aload_0        
        //    45: monitorexit    
        //    46: aload_0        
        //    47: getfield        de/greenrobot/event/BackgroundPoster.eventBus:Lde/greenrobot/event/EventBus;
        //    50: aload_1        
        //    51: invokevirtual   de/greenrobot/event/EventBus.invokeSubscriber:(Lde/greenrobot/event/PendingPost;)V
        //    54: goto            0
        //    57: astore_1       
        //    58: ldc             "Event"
        //    60: new             Ljava/lang/StringBuilder;
        //    63: dup            
        //    64: invokespecial   java/lang/StringBuilder.<init>:()V
        //    67: invokestatic    java/lang/Thread.currentThread:()Ljava/lang/Thread;
        //    70: invokevirtual   java/lang/Thread.getName:()Ljava/lang/String;
        //    73: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    76: ldc             " was interruppted"
        //    78: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    81: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    84: aload_1        
        //    85: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    88: pop            
        //    89: aload_0        
        //    90: iconst_0       
        //    91: putfield        de/greenrobot/event/BackgroundPoster.executorRunning:Z
        //    94: return         
        //    95: astore_1       
        //    96: aload_0        
        //    97: monitorexit    
        //    98: aload_1        
        //    99: athrow         
        //   100: astore_1       
        //   101: aload_0        
        //   102: iconst_0       
        //   103: putfield        de/greenrobot/event/BackgroundPoster.executorRunning:Z
        //   106: aload_1        
        //   107: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  0      11     57     95     Ljava/lang/InterruptedException;
        //  0      11     100    108    Any
        //  17     19     57     95     Ljava/lang/InterruptedException;
        //  17     19     100    108    Any
        //  19     27     95     100    Any
        //  31     38     95     100    Any
        //  44     46     95     100    Any
        //  46     54     57     95     Ljava/lang/InterruptedException;
        //  46     54     100    108    Any
        //  58     89     100    108    Any
        //  96     98     95     100    Any
        //  98     100    57     95     Ljava/lang/InterruptedException;
        //  98     100    100    108    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0044:
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
}
