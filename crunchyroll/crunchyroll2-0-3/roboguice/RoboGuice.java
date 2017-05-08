// 
// Decompiled by Procyon v0.5.30
// 

package roboguice;

import java.util.Iterator;
import com.google.inject.Guice;
import com.google.inject.spi.ElementVisitor;
import com.google.inject.spi.StaticInjectionRequest;
import com.google.inject.spi.DefaultElementVisitor;
import com.google.inject.spi.Element;
import com.google.inject.spi.Elements;
import com.google.inject.Module;
import java.util.ArrayList;
import roboguice.inject.ContextScope;
import roboguice.config.DefaultRoboModule;
import roboguice.inject.ContextScopedRoboInjector;
import roboguice.inject.RoboInjector;
import roboguice.event.EventManager;
import android.content.Context;
import roboguice.inject.ViewListener;
import roboguice.inject.ResourceListener;
import com.google.inject.Injector;
import android.app.Application;
import java.util.WeakHashMap;
import com.google.inject.Stage;

public class RoboGuice
{
    public static Stage DEFAULT_STAGE;
    protected static WeakHashMap<Application, Injector> injectors;
    protected static int modulesResourceId;
    protected static WeakHashMap<Application, ResourceListener> resourceListeners;
    protected static WeakHashMap<Application, ViewListener> viewListeners;
    
    static {
        RoboGuice.DEFAULT_STAGE = Stage.PRODUCTION;
        RoboGuice.injectors = new WeakHashMap<Application, Injector>();
        RoboGuice.resourceListeners = new WeakHashMap<Application, ResourceListener>();
        RoboGuice.viewListeners = new WeakHashMap<Application, ViewListener>();
        RoboGuice.modulesResourceId = 0;
    }
    
    public static void destroyInjector(final Context context) {
        getInjector(context).getInstance(EventManager.class).destroy();
        RoboGuice.injectors.remove(context);
    }
    
    public static Injector getBaseApplicationInjector(final Application application) {
        final Injector injector = RoboGuice.injectors.get(application);
        if (injector != null) {
            return injector;
        }
        synchronized (RoboGuice.class) {
            final Injector injector2 = RoboGuice.injectors.get(application);
            if (injector2 != null) {
                return injector2;
            }
            return setBaseApplicationInjector(application, RoboGuice.DEFAULT_STAGE);
        }
    }
    
    public static RoboInjector getInjector(final Context context) {
        final Application application = (Application)context.getApplicationContext();
        return new ContextScopedRoboInjector(context, getBaseApplicationInjector(application), getViewListener(application));
    }
    
    protected static ResourceListener getResourceListener(final Application application) {
        final ResourceListener resourceListener = RoboGuice.resourceListeners.get(application);
        if (resourceListener != null) {
            return resourceListener;
        }
        // monitorenter(RoboGuice.class)
        Label_0042: {
            ResourceListener resourceListener2;
            if ((resourceListener2 = resourceListener) != null) {
                break Label_0042;
            }
            while (true) {
                try {
                    resourceListener2 = new ResourceListener(application);
                    try {
                        RoboGuice.resourceListeners.put(application, resourceListener2);
                        // monitorexit(RoboGuice.class)
                        return resourceListener2;
                        // monitorexit(RoboGuice.class)
                        throw;
                    }
                    finally {
                        continue;
                    }
                    return resourceListener;
                }
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    protected static ViewListener getViewListener(final Application application) {
        final ViewListener viewListener = RoboGuice.viewListeners.get(application);
        if (viewListener != null) {
            return viewListener;
        }
        // monitorenter(RoboGuice.class)
        Label_0041: {
            ViewListener viewListener2;
            if ((viewListener2 = viewListener) != null) {
                break Label_0041;
            }
            while (true) {
                try {
                    viewListener2 = new ViewListener();
                    try {
                        RoboGuice.viewListeners.put(application, viewListener2);
                        // monitorexit(RoboGuice.class)
                        return viewListener2;
                        // monitorexit(RoboGuice.class)
                        throw;
                    }
                    finally {
                        continue;
                    }
                    return viewListener;
                }
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    public static <T> T injectMembers(final Context context, final T t) {
        getInjector(context).injectMembers(t);
        return t;
    }
    
    public static DefaultRoboModule newDefaultRoboModule(final Application application) {
        return new DefaultRoboModule(application, new ContextScope(application), getViewListener(application), getResourceListener(application));
    }
    
    public static Injector setBaseApplicationInjector(final Application application, final Stage stage) {
        ArrayList<DefaultRoboModule> list = null;
        synchronized (RoboGuice.class) {
            int i;
            int length = i = RoboGuice.modulesResourceId;
            if (length == 0) {
                i = application.getResources().getIdentifier("roboguice_modules", "array", application.getPackageName());
            }
            Label_0125: {
                if (i <= 0) {
                    break Label_0125;
                }
                String[] stringArray = application.getResources().getStringArray(i);
                while (true) {
                    list = new ArrayList<DefaultRoboModule>();
                    list.add(newDefaultRoboModule(application));
                    try {
                        length = stringArray.length;
                        i = 0;
                        while (i < length) {
                            final Class<? extends Module> subclass = Class.forName(stringArray[i]).asSubclass(Module.class);
                            try {
                                list.add((DefaultRoboModule)subclass.getDeclaredConstructor(Context.class).newInstance(application));
                                ++i;
                                continue;
                                stringArray = new String[0];
                            }
                            catch (NoSuchMethodException ex2) {
                                list.add((DefaultRoboModule)subclass.newInstance());
                            }
                        }
                    }
                    catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
        final Application application2;
        final Injector setBaseApplicationInjector = setBaseApplicationInjector(application2, stage, (Module[])list.toArray(new Module[list.size()]));
        RoboGuice.injectors.put(application2, setBaseApplicationInjector);
        // monitorexit(RoboGuice.class)
        return setBaseApplicationInjector;
    }
    
    public static Injector setBaseApplicationInjector(final Application application, final Stage stage, final Module... array) {
        final Iterator<Element> iterator = Elements.getElements(array).iterator();
        while (iterator.hasNext()) {
            iterator.next().acceptVisitor((ElementVisitor<Object>)new DefaultElementVisitor<Void>() {
                @Override
                public Void visit(final StaticInjectionRequest staticInjectionRequest) {
                    RoboGuice.getResourceListener(application).requestStaticInjection(staticInjectionRequest.getType());
                    return null;
                }
            });
        }
        synchronized (RoboGuice.class) {
            final Injector injector = Guice.createInjector(stage, array);
            RoboGuice.injectors.put(application, injector);
            return injector;
        }
    }
    
    public static void setModulesResourceId(final int modulesResourceId) {
        RoboGuice.modulesResourceId = modulesResourceId;
    }
    
    public static class util
    {
        public static void reset() {
            RoboGuice.injectors.clear();
            RoboGuice.resourceListeners.clear();
            RoboGuice.viewListeners.clear();
        }
    }
}
