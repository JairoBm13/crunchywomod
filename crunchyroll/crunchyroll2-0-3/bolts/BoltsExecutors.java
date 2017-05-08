// 
// Decompiled by Procyon v0.5.30
// 

package bolts;

import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

final class BoltsExecutors
{
    private static final BoltsExecutors INSTANCE;
    private final ExecutorService background;
    private final Executor immediate;
    
    static {
        INSTANCE = new BoltsExecutors();
    }
    
    private BoltsExecutors() {
        ExecutorService background;
        if (!isAndroidRuntime()) {
            background = Executors.newCachedThreadPool();
        }
        else {
            background = AndroidExecutors.newCachedThreadPool();
        }
        this.background = background;
        this.immediate = new ImmediateExecutor();
    }
    
    public static ExecutorService background() {
        return BoltsExecutors.INSTANCE.background;
    }
    
    static Executor immediate() {
        return BoltsExecutors.INSTANCE.immediate;
    }
    
    private static boolean isAndroidRuntime() {
        final String property = System.getProperty("java.runtime.name");
        return property != null && property.toLowerCase(Locale.US).contains("android");
    }
    
    private static class ImmediateExecutor implements Executor
    {
        private ThreadLocal<Integer> executionDepth;
        
        private ImmediateExecutor() {
            this.executionDepth = new ThreadLocal<Integer>();
        }
        
        private int decrementDepth() {
            Integer value;
            if ((value = this.executionDepth.get()) == null) {
                value = 0;
            }
            final int n = value - 1;
            if (n == 0) {
                this.executionDepth.remove();
                return n;
            }
            this.executionDepth.set(n);
            return n;
        }
        
        private int incrementDepth() {
            Integer value;
            if ((value = this.executionDepth.get()) == null) {
                value = 0;
            }
            final int n = value + 1;
            this.executionDepth.set(n);
            return n;
        }
        
        @Override
        public void execute(final Runnable runnable) {
            Label_0021: {
                if (this.incrementDepth() > 15) {
                    break Label_0021;
                }
                try {
                    runnable.run();
                    return;
                    BoltsExecutors.background().execute(runnable);
                }
                finally {
                    this.decrementDepth();
                }
            }
        }
    }
}
