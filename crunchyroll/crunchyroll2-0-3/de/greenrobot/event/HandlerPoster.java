// 
// Decompiled by Procyon v0.5.30
// 

package de.greenrobot.event;

import android.os.Message;
import android.os.Looper;
import android.os.Handler;

final class HandlerPoster extends Handler
{
    private final EventBus eventBus;
    private boolean handlerActive;
    private final int maxMillisInsideHandleMessage;
    private final PendingPostQueue queue;
    
    HandlerPoster(final EventBus eventBus, final Looper looper, final int maxMillisInsideHandleMessage) {
        super(looper);
        this.eventBus = eventBus;
        this.maxMillisInsideHandleMessage = maxMillisInsideHandleMessage;
        this.queue = new PendingPostQueue();
    }
    
    void enqueue(final Subscription subscription, final Object o) {
        final PendingPost obtainPendingPost = PendingPost.obtainPendingPost(subscription, o);
        synchronized (this) {
            this.queue.enqueue(obtainPendingPost);
            if (!this.handlerActive) {
                this.handlerActive = true;
                if (!this.sendMessage(this.obtainMessage())) {
                    throw new EventBusException("Could not send handler message");
                }
            }
        }
    }
    // monitorexit(this)
    
    public void handleMessage(final Message p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: invokestatic    android/os/SystemClock.uptimeMillis:()J
        //     3: lstore_2       
        //     4: aload_0        
        //     5: getfield        de/greenrobot/event/HandlerPoster.queue:Lde/greenrobot/event/PendingPostQueue;
        //     8: invokevirtual   de/greenrobot/event/PendingPostQueue.poll:()Lde/greenrobot/event/PendingPost;
        //    11: astore          4
        //    13: aload           4
        //    15: astore_1       
        //    16: aload           4
        //    18: ifnonnull       50
        //    21: aload_0        
        //    22: monitorenter   
        //    23: aload_0        
        //    24: getfield        de/greenrobot/event/HandlerPoster.queue:Lde/greenrobot/event/PendingPostQueue;
        //    27: invokevirtual   de/greenrobot/event/PendingPostQueue.poll:()Lde/greenrobot/event/PendingPost;
        //    30: astore_1       
        //    31: aload_1        
        //    32: ifnonnull       48
        //    35: aload_0        
        //    36: iconst_0       
        //    37: putfield        de/greenrobot/event/HandlerPoster.handlerActive:Z
        //    40: aload_0        
        //    41: monitorexit    
        //    42: aload_0        
        //    43: iconst_0       
        //    44: putfield        de/greenrobot/event/HandlerPoster.handlerActive:Z
        //    47: return         
        //    48: aload_0        
        //    49: monitorexit    
        //    50: aload_0        
        //    51: getfield        de/greenrobot/event/HandlerPoster.eventBus:Lde/greenrobot/event/EventBus;
        //    54: aload_1        
        //    55: invokevirtual   de/greenrobot/event/EventBus.invokeSubscriber:(Lde/greenrobot/event/PendingPost;)V
        //    58: invokestatic    android/os/SystemClock.uptimeMillis:()J
        //    61: lload_2        
        //    62: lsub           
        //    63: aload_0        
        //    64: getfield        de/greenrobot/event/HandlerPoster.maxMillisInsideHandleMessage:I
        //    67: i2l            
        //    68: lcmp           
        //    69: iflt            4
        //    72: aload_0        
        //    73: aload_0        
        //    74: invokevirtual   de/greenrobot/event/HandlerPoster.obtainMessage:()Landroid/os/Message;
        //    77: invokevirtual   de/greenrobot/event/HandlerPoster.sendMessage:(Landroid/os/Message;)Z
        //    80: ifne            106
        //    83: new             Lde/greenrobot/event/EventBusException;
        //    86: dup            
        //    87: ldc             "Could not send handler message"
        //    89: invokespecial   de/greenrobot/event/EventBusException.<init>:(Ljava/lang/String;)V
        //    92: athrow         
        //    93: astore_1       
        //    94: aload_0        
        //    95: iconst_0       
        //    96: putfield        de/greenrobot/event/HandlerPoster.handlerActive:Z
        //    99: aload_1        
        //   100: athrow         
        //   101: astore_1       
        //   102: aload_0        
        //   103: monitorexit    
        //   104: aload_1        
        //   105: athrow         
        //   106: aload_0        
        //   107: iconst_1       
        //   108: putfield        de/greenrobot/event/HandlerPoster.handlerActive:Z
        //   111: return         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  0      4      93     101    Any
        //  4      13     93     101    Any
        //  21     23     93     101    Any
        //  23     31     101    106    Any
        //  35     42     101    106    Any
        //  48     50     101    106    Any
        //  50     93     93     101    Any
        //  102    104    101    106    Any
        //  104    106    93     101    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0048:
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
