// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.test;

import roboguice.test.shadow.ShadowFragment;
import com.xtremelabs.robolectric.Robolectric;
import roboguice.test.shadow.ShadowFragmentActivity;
import java.io.File;
import com.xtremelabs.robolectric.bytecode.RobolectricClassLoader;
import com.xtremelabs.robolectric.bytecode.ClassHandler;
import com.xtremelabs.robolectric.RobolectricConfig;
import org.junit.runners.model.InitializationError;
import com.xtremelabs.robolectric.RobolectricTestRunner;

public class RobolectricRoboTestRunner extends RobolectricTestRunner
{
    public RobolectricRoboTestRunner(final Class<?> clazz) throws InitializationError {
        super((Class)clazz);
    }
    
    public RobolectricRoboTestRunner(final Class<?> clazz, final RobolectricConfig robolectricConfig) throws InitializationError {
        super((Class)clazz, robolectricConfig);
    }
    
    public RobolectricRoboTestRunner(final Class<?> clazz, final ClassHandler classHandler, final RobolectricConfig robolectricConfig) throws InitializationError {
        super((Class)clazz, classHandler, robolectricConfig);
    }
    
    public RobolectricRoboTestRunner(final Class<?> clazz, final ClassHandler classHandler, final RobolectricClassLoader robolectricClassLoader, final RobolectricConfig robolectricConfig) throws InitializationError {
        super((Class)clazz, classHandler, robolectricClassLoader, robolectricConfig);
    }
    
    public RobolectricRoboTestRunner(final Class<?> clazz, final File file) throws InitializationError {
        super((Class)clazz, file);
    }
    
    public RobolectricRoboTestRunner(final Class<?> clazz, final File file, final File file2) throws InitializationError {
        super((Class)clazz, file, file2);
    }
    
    protected void bindShadowClasses() {
        super.bindShadowClasses();
        Robolectric.bindShadowClass((Class)ShadowFragmentActivity.class);
        Robolectric.bindShadowClass((Class)ShadowFragment.class);
    }
}
