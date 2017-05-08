// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.util;

import android.app.Application;
import android.util.Log;
import com.google.inject.Inject;

public class Ln
{
    @Inject
    protected static BaseConfig config;
    @Inject
    protected static Print print;
    
    static {
        Ln.config = new BaseConfig();
        Ln.print = new Print();
    }
    
    public static int d(final Object o, final Object... array) {
        if (Ln.config.minimumLogLevel > 3) {
            return 0;
        }
        String s = Strings.toString(o);
        if (array.length > 0) {
            s = String.format(s, array);
        }
        return Ln.print.println(3, s);
    }
    
    public static int d(final Throwable t) {
        if (Ln.config.minimumLogLevel <= 3) {
            return Ln.print.println(3, Log.getStackTraceString(t));
        }
        return 0;
    }
    
    public static int d(final Throwable t, final Object o, final Object... array) {
        if (Ln.config.minimumLogLevel > 3) {
            return 0;
        }
        final String string = Strings.toString(o);
        final StringBuilder sb = new StringBuilder();
        String format = string;
        if (array.length > 0) {
            format = String.format(string, array);
        }
        return Ln.print.println(3, sb.append(format).append('\n').append(Log.getStackTraceString(t)).toString());
    }
    
    public static int e(final Object o, final Object... array) {
        if (Ln.config.minimumLogLevel > 6) {
            return 0;
        }
        String s = Strings.toString(o);
        if (array.length > 0) {
            s = String.format(s, array);
        }
        return Ln.print.println(6, s);
    }
    
    public static int e(final Throwable t) {
        if (Ln.config.minimumLogLevel <= 6) {
            return Ln.print.println(6, Log.getStackTraceString(t));
        }
        return 0;
    }
    
    public static int e(final Throwable t, final Object o, final Object... array) {
        if (Ln.config.minimumLogLevel > 6) {
            return 0;
        }
        final String string = Strings.toString(o);
        final StringBuilder sb = new StringBuilder();
        String format = string;
        if (array.length > 0) {
            format = String.format(string, array);
        }
        return Ln.print.println(6, sb.append(format).append('\n').append(Log.getStackTraceString(t)).toString());
    }
    
    public static Config getConfig() {
        return (Config)Ln.config;
    }
    
    public static int i(final Object o, final Object... array) {
        if (Ln.config.minimumLogLevel > 4) {
            return 0;
        }
        String s = Strings.toString(o);
        if (array.length > 0) {
            s = String.format(s, array);
        }
        return Ln.print.println(4, s);
    }
    
    public static int i(final Throwable t) {
        if (Ln.config.minimumLogLevel <= 4) {
            return Ln.print.println(4, Log.getStackTraceString(t));
        }
        return 0;
    }
    
    public static int i(final Throwable t, final Object o, final Object... array) {
        if (Ln.config.minimumLogLevel > 4) {
            return 0;
        }
        final String string = Strings.toString(o);
        final StringBuilder sb = new StringBuilder();
        String format = string;
        if (array.length > 0) {
            format = String.format(string, array);
        }
        return Ln.print.println(4, sb.append(format).append('\n').append(Log.getStackTraceString(t)).toString());
    }
    
    public static boolean isDebugEnabled() {
        return Ln.config.minimumLogLevel <= 3;
    }
    
    public static boolean isVerboseEnabled() {
        return Ln.config.minimumLogLevel <= 2;
    }
    
    public static String logLevelToString(final int n) {
        switch (n) {
            default: {
                return "UNKNOWN";
            }
            case 2: {
                return "VERBOSE";
            }
            case 3: {
                return "DEBUG";
            }
            case 4: {
                return "INFO";
            }
            case 5: {
                return "WARN";
            }
            case 6: {
                return "ERROR";
            }
            case 7: {
                return "ASSERT";
            }
        }
    }
    
    public static int v(final Object o, final Object... array) {
        if (Ln.config.minimumLogLevel > 2) {
            return 0;
        }
        String s = Strings.toString(o);
        if (array.length > 0) {
            s = String.format(s, array);
        }
        return Ln.print.println(2, s);
    }
    
    public static int v(final Throwable t) {
        if (Ln.config.minimumLogLevel <= 2) {
            return Ln.print.println(2, Log.getStackTraceString(t));
        }
        return 0;
    }
    
    public static int v(final Throwable t, final Object o, final Object... array) {
        if (Ln.config.minimumLogLevel > 2) {
            return 0;
        }
        final String string = Strings.toString(o);
        final StringBuilder sb = new StringBuilder();
        String format = string;
        if (array.length > 0) {
            format = String.format(string, array);
        }
        return Ln.print.println(2, sb.append(format).append('\n').append(Log.getStackTraceString(t)).toString());
    }
    
    public static int w(final Object o, final Object... array) {
        if (Ln.config.minimumLogLevel > 5) {
            return 0;
        }
        String s = Strings.toString(o);
        if (array.length > 0) {
            s = String.format(s, array);
        }
        return Ln.print.println(5, s);
    }
    
    public static int w(final Throwable t) {
        if (Ln.config.minimumLogLevel <= 5) {
            return Ln.print.println(5, Log.getStackTraceString(t));
        }
        return 0;
    }
    
    public static int w(final Throwable t, final Object o, final Object... array) {
        if (Ln.config.minimumLogLevel > 5) {
            return 0;
        }
        final String string = Strings.toString(o);
        final StringBuilder sb = new StringBuilder();
        String format = string;
        if (array.length > 0) {
            format = String.format(string, array);
        }
        return Ln.print.println(5, sb.append(format).append('\n').append(Log.getStackTraceString(t)).toString());
    }
    
    public static class BaseConfig implements Config
    {
        protected int minimumLogLevel;
        protected String packageName;
        protected String scope;
        
        protected BaseConfig() {
            this.minimumLogLevel = 2;
            this.packageName = "";
            this.scope = "";
        }
        
        public BaseConfig(final Application application) {
            int minimumLogLevel = 2;
            this.minimumLogLevel = 2;
            this.packageName = "";
            this.scope = "";
            try {
                this.packageName = application.getPackageName();
                if ((application.getPackageManager().getApplicationInfo(this.packageName, 0).flags & 0x2) == 0x0) {
                    minimumLogLevel = 4;
                }
                this.minimumLogLevel = minimumLogLevel;
                this.scope = this.packageName.toUpperCase();
                Ln.d("Configuring Logging, minimum log level is %s", Ln.logLevelToString(this.minimumLogLevel));
            }
            catch (Exception ex) {
                Log.e(this.packageName, "Error configuring logger", (Throwable)ex);
            }
        }
        
        @Override
        public int getLoggingLevel() {
            return this.minimumLogLevel;
        }
        
        @Override
        public void setLoggingLevel(final int minimumLogLevel) {
            this.minimumLogLevel = minimumLogLevel;
        }
    }
    
    public interface Config
    {
        int getLoggingLevel();
        
        void setLoggingLevel(final int p0);
    }
    
    public static class Print
    {
        protected static String getScope(final int n) {
            if (Ln.config.minimumLogLevel <= 3) {
                final StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[n];
                return Ln.config.scope + "/" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber();
            }
            return Ln.config.scope;
        }
        
        public int println(final int n, final String s) {
            return Log.println(n, getScope(5), this.processMessage(s));
        }
        
        protected String processMessage(final String s) {
            String format = s;
            if (Ln.config.minimumLogLevel <= 3) {
                format = String.format("%s %s", Thread.currentThread().getName(), s);
            }
            return format;
        }
    }
}
