// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

import java.net.URLClassLoader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.URL;
import java.lang.ref.Reference;
import java.util.logging.Level;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class $FinalizableReferenceQueue
{
    private static final Logger logger;
    private static final Method startFinalizer;
    final ReferenceQueue<Object> queue;
    final boolean threadStarted;
    
    static {
        logger = Logger.getLogger($FinalizableReferenceQueue.class.getName());
        startFinalizer = getStartFinalizer(loadFinalizer(new SystemLoader(), new DecoupledLoader(), new DirectLoader()));
    }
    
    public $FinalizableReferenceQueue() {
        boolean threadStarted = false;
        while (true) {
            try {
                final ReferenceQueue<Object> queue = (ReferenceQueue<Object>)$FinalizableReferenceQueue.startFinalizer.invoke(null, $FinalizableReference.class, this);
                threadStarted = true;
                this.queue = queue;
                this.threadStarted = threadStarted;
            }
            catch (IllegalAccessException ex) {
                throw new AssertionError((Object)ex);
            }
            catch (Throwable t) {
                $FinalizableReferenceQueue.logger.log(Level.INFO, "Failed to start reference finalizer thread. Reference cleanup will only occur when new references are created.", t);
                final ReferenceQueue<Object> queue = new ReferenceQueue<Object>();
                continue;
            }
            break;
        }
    }
    
    static Method getStartFinalizer(final Class<?> clazz) {
        try {
            return clazz.getMethod("startFinalizer", Class.class, Object.class);
        }
        catch (NoSuchMethodException ex) {
            throw new AssertionError((Object)ex);
        }
    }
    
    private static Class<?> loadFinalizer(final FinalizerLoader... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            final Class<?> loadFinalizer = array[i].loadFinalizer();
            if (loadFinalizer != null) {
                return loadFinalizer;
            }
        }
        throw new AssertionError();
    }
    
    void cleanUp() {
        if (!this.threadStarted) {
            while (true) {
                final Reference<?> poll = this.queue.poll();
                if (poll == null) {
                    break;
                }
                poll.clear();
                try {
                    (($FinalizableReference)poll).finalizeReferent();
                }
                catch (Throwable t) {
                    $FinalizableReferenceQueue.logger.log(Level.SEVERE, "Error cleaning up after reference.", t);
                }
            }
        }
    }
    
    static class DecoupledLoader implements FinalizerLoader
    {
        URL getBaseUrl() throws IOException {
            final String string = "com.google.inject.internal.util.$Finalizer".replace('.', '/') + ".class";
            final URL resource = this.getClass().getClassLoader().getResource(string);
            if (resource == null) {
                throw new FileNotFoundException(string);
            }
            final String string2 = resource.toString();
            if (!string2.endsWith(string)) {
                throw new IOException("Unsupported path style: " + string2);
            }
            return new URL(string2.substring(0, string2.length() - string.length()));
        }
        
        @Override
        public Class<?> loadFinalizer() {
            try {
                return this.newLoader(this.getBaseUrl()).loadClass("com.google.inject.internal.util.$Finalizer");
            }
            catch (Exception ex) {
                $FinalizableReferenceQueue.logger.log(Level.WARNING, "Could not load Finalizer in its own class loader. Loading Finalizer in the current class loader instead. As a result, you will not be able to garbage collect this class loader. To support reclaiming this class loader, either resolve the underlying issue, or move Google Collections to your system class path.", ex);
                return null;
            }
        }
        
        URLClassLoader newLoader(final URL url) {
            return new URLClassLoader(new URL[] { url });
        }
    }
    
    static class DirectLoader implements FinalizerLoader
    {
        @Override
        public Class<?> loadFinalizer() {
            try {
                return Class.forName("com.google.inject.internal.util.$Finalizer");
            }
            catch (ClassNotFoundException ex) {
                throw new AssertionError((Object)ex);
            }
        }
    }
    
    interface FinalizerLoader
    {
        Class<?> loadFinalizer();
    }
    
    static class SystemLoader implements FinalizerLoader
    {
        @Override
        public Class<?> loadFinalizer() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: aconst_null    
            //     1: astore_1       
            //     2: invokestatic    java/lang/ClassLoader.getSystemClassLoader:()Ljava/lang/ClassLoader;
            //     5: astore_2       
            //     6: aload_2        
            //     7: ifnull          17
            //    10: aload_2        
            //    11: ldc             "com.google.inject.internal.util.$Finalizer"
            //    13: invokevirtual   java/lang/ClassLoader.loadClass:(Ljava/lang/String;)Ljava/lang/Class;
            //    16: astore_1       
            //    17: aload_1        
            //    18: areturn        
            //    19: astore_1       
            //    20: invokestatic    com/google/inject/internal/util/$FinalizableReferenceQueue.access$000:()Ljava/util/logging/Logger;
            //    23: ldc             "Not allowed to access system class loader."
            //    25: invokevirtual   java/util/logging/Logger.info:(Ljava/lang/String;)V
            //    28: aconst_null    
            //    29: areturn        
            //    30: astore_1       
            //    31: aconst_null    
            //    32: areturn        
            //    Signature:
            //  ()Ljava/lang/Class<*>;
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                              
            //  -----  -----  -----  -----  ----------------------------------
            //  2      6      19     30     Ljava/lang/SecurityException;
            //  10     17     30     33     Ljava/lang/ClassNotFoundException;
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0017:
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
}
