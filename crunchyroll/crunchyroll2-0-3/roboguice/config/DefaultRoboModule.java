// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.config;

import android.content.pm.PackageManager$NameNotFoundException;
import roboguice.inject.AccountManagerProvider;
import android.os.Build$VERSION;
import roboguice.inject.FragmentManagerProvider;
import roboguice.util.Ln;
import roboguice.event.ObservesTypeListener;
import roboguice.event.EventManager;
import com.google.inject.spi.TypeListener;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import android.app.SearchManager;
import roboguice.inject.ContextScopedSystemServiceProvider;
import android.view.LayoutInflater;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.hardware.SensorManager;
import android.view.inputmethod.InputMethodManager;
import android.net.wifi.WifiManager;
import android.net.ConnectivityManager;
import android.os.Vibrator;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.AlarmManager;
import android.os.PowerManager;
import android.app.ActivityManager;
import android.view.WindowManager;
import roboguice.inject.SystemServiceProvider;
import android.location.LocationManager;
import roboguice.inject.HandlerProvider;
import android.os.Handler;
import roboguice.inject.ContentResolverProvider;
import android.content.ContentResolver;
import roboguice.inject.ResourcesProvider;
import android.content.res.Resources;
import roboguice.inject.SharedPreferencesProvider;
import android.content.SharedPreferences;
import roboguice.service.RoboService;
import android.app.Service;
import roboguice.activity.RoboActivity;
import android.app.Activity;
import com.google.inject.Key;
import roboguice.inject.NullProvider;
import com.google.inject.TypeLiteral;
import javax.inject.Provider;
import roboguice.inject.AssetManagerProvider;
import android.content.res.AssetManager;
import com.google.inject.Scope;
import roboguice.inject.ContextSingleton;
import java.lang.annotation.Annotation;
import com.google.inject.name.Names;
import roboguice.util.Strings;
import android.content.pm.PackageInfo;
import android.provider.Settings$Secure;
import roboguice.event.eventListener.factory.EventListenerThreadingDecorator;
import roboguice.inject.PreferenceListener;
import roboguice.inject.ExtrasListener;
import android.content.Context;
import roboguice.inject.ViewListener;
import roboguice.inject.ResourceListener;
import roboguice.inject.ContextScope;
import android.app.Application;
import com.google.inject.AbstractModule;

public class DefaultRoboModule extends AbstractModule
{
    protected static final Class accountManagerClass;
    protected static final Class fragmentManagerClass;
    protected Application application;
    protected ContextScope contextScope;
    protected ResourceListener resourceListener;
    protected ViewListener viewListener;
    
    static {
        Class<?> clazz = null;
        while (true) {
            try {
                clazz = Class.forName("android.support.v4.app.FragmentManager");
                fragmentManagerClass = clazz;
                clazz = null;
                try {
                    clazz = Class.forName("android.accounts.AccountManager");
                    accountManagerClass = clazz;
                }
                catch (Throwable t) {}
            }
            catch (Throwable t2) {
                continue;
            }
            break;
        }
    }
    
    public DefaultRoboModule(final Application application, final ContextScope contextScope, final ViewListener viewListener, final ResourceListener resourceListener) {
        this.application = application;
        this.contextScope = contextScope;
        this.viewListener = viewListener;
        this.resourceListener = resourceListener;
    }
    
    @Override
    protected void configure() {
        final com.google.inject.Provider<Context> provider = this.getProvider(Context.class);
        final ExtrasListener extrasListener = new ExtrasListener(provider);
        final PreferenceListener preferenceListener = new PreferenceListener(provider, this.application, this.contextScope);
        final EventListenerThreadingDecorator eventListenerThreadingDecorator = new EventListenerThreadingDecorator();
        final String string = Settings$Secure.getString(this.application.getContentResolver(), "android_id");
        try {
            this.bind(PackageInfo.class).toInstance(this.application.getPackageManager().getPackageInfo(this.application.getPackageName(), 0));
            if (Strings.notEmpty(string)) {
                this.bindConstant().annotatedWith(Names.named("android_id")).to(string);
            }
            this.bind(ViewListener.class).toInstance(this.viewListener);
            this.bind(PreferenceListener.class).toInstance(preferenceListener);
            this.bindScope(ContextSingleton.class, this.contextScope);
            this.bind(ContextScope.class).toInstance(this.contextScope);
            this.bind(AssetManager.class).toProvider((Class<? extends Provider<?>>)AssetManagerProvider.class);
            this.bind(Context.class).toProvider((Key<? extends Provider<?>>)Key.get((TypeLiteral<? extends Provider<? extends T>>)new TypeLiteral<NullProvider<Context>>() {})).in(ContextSingleton.class);
            this.bind(Activity.class).toProvider((Key<? extends Provider<?>>)Key.get((TypeLiteral<? extends Provider<? extends T>>)new TypeLiteral<NullProvider<Activity>>() {})).in(ContextSingleton.class);
            this.bind(RoboActivity.class).toProvider((Key<? extends Provider<?>>)Key.get((TypeLiteral<? extends Provider<? extends T>>)new TypeLiteral<NullProvider<RoboActivity>>() {})).in(ContextSingleton.class);
            this.bind(Service.class).toProvider((Key<? extends Provider<?>>)Key.get((TypeLiteral<? extends Provider<? extends T>>)new TypeLiteral<NullProvider<Service>>() {})).in(ContextSingleton.class);
            this.bind(RoboService.class).toProvider((Key<? extends Provider<?>>)Key.get((TypeLiteral<? extends Provider<? extends T>>)new TypeLiteral<NullProvider<RoboService>>() {})).in(ContextSingleton.class);
            this.bind(SharedPreferences.class).toProvider((Class<? extends Provider<?>>)SharedPreferencesProvider.class);
            this.bind(Resources.class).toProvider((Class<? extends Provider<?>>)ResourcesProvider.class);
            this.bind(ContentResolver.class).toProvider((Class<? extends Provider<?>>)ContentResolverProvider.class);
            this.bind(Application.class).toInstance(this.application);
            this.bind(EventListenerThreadingDecorator.class).toInstance(eventListenerThreadingDecorator);
            this.bind(Handler.class).toProvider((Class<? extends Provider<?>>)HandlerProvider.class);
            this.bind(LocationManager.class).toProvider((com.google.inject.Provider<?>)new SystemServiceProvider<Object>("location"));
            this.bind(WindowManager.class).toProvider((com.google.inject.Provider<?>)new SystemServiceProvider<Object>("window"));
            this.bind(ActivityManager.class).toProvider((com.google.inject.Provider<?>)new SystemServiceProvider<Object>("activity"));
            this.bind(PowerManager.class).toProvider((com.google.inject.Provider<?>)new SystemServiceProvider<Object>("power"));
            this.bind(AlarmManager.class).toProvider((com.google.inject.Provider<?>)new SystemServiceProvider<Object>("alarm"));
            this.bind(NotificationManager.class).toProvider((com.google.inject.Provider<?>)new SystemServiceProvider<Object>("notification"));
            this.bind(KeyguardManager.class).toProvider((com.google.inject.Provider<?>)new SystemServiceProvider<Object>("keyguard"));
            this.bind(Vibrator.class).toProvider((com.google.inject.Provider<?>)new SystemServiceProvider<Object>("vibrator"));
            this.bind(ConnectivityManager.class).toProvider((com.google.inject.Provider<?>)new SystemServiceProvider<Object>("connectivity"));
            this.bind(WifiManager.class).toProvider((com.google.inject.Provider<?>)new SystemServiceProvider<Object>("wifi"));
            this.bind(InputMethodManager.class).toProvider((com.google.inject.Provider<?>)new SystemServiceProvider<Object>("input_method"));
            this.bind(SensorManager.class).toProvider((com.google.inject.Provider<?>)new SystemServiceProvider<Object>("sensor"));
            this.bind(TelephonyManager.class).toProvider((com.google.inject.Provider<?>)new SystemServiceProvider<Object>("phone"));
            this.bind(AudioManager.class).toProvider((com.google.inject.Provider<?>)new SystemServiceProvider<Object>("audio"));
            this.bind(LayoutInflater.class).toProvider((com.google.inject.Provider<?>)new ContextScopedSystemServiceProvider<Object>(provider, "layout_inflater"));
            this.bind(SearchManager.class).toProvider((com.google.inject.Provider<?>)new ContextScopedSystemServiceProvider<Object>(provider, "search"));
            this.bindListener(Matchers.any(), this.resourceListener);
            this.bindListener(Matchers.any(), extrasListener);
            this.bindListener(Matchers.any(), this.viewListener);
            this.bindListener(Matchers.any(), preferenceListener);
            this.bindListener(Matchers.any(), new ObservesTypeListener(this.getProvider(EventManager.class), eventListenerThreadingDecorator));
            this.requestInjection(eventListenerThreadingDecorator);
            this.requestStaticInjection(Ln.class);
            if (DefaultRoboModule.fragmentManagerClass != null) {
                this.bind((Class<Object>)DefaultRoboModule.fragmentManagerClass).toProvider(FragmentManagerProvider.class);
            }
            if (Build$VERSION.SDK_INT >= 5) {
                this.bind((Class<Object>)DefaultRoboModule.accountManagerClass).toProvider(AccountManagerProvider.class);
            }
        }
        catch (PackageManager$NameNotFoundException ex) {
            throw new RuntimeException((Throwable)ex);
        }
    }
}
