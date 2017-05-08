// 
// Decompiled by Procyon v0.5.30
// 

package tv.ouya.console.api;

import android.os.IBinder;
import android.content.ComponentName;
import android.os.Build;
import android.util.Log;
import java.util.ArrayList;
import tv.ouya.console.internal.IIapServiceDefinition;
import java.util.List;
import android.content.ServiceConnection;

public class OuyaFacade implements ServiceConnection
{
    private static final String[] ALL_DATA_COLUMNS;
    private static final String[] USER_DATA_COLUMNS;
    private static OuyaFacade instance;
    private boolean bindRequestHasBeenMade;
    private List<Runnable> pendingRequests;
    private IIapServiceDefinition remoteService;
    
    static {
        USER_DATA_COLUMNS = new String[] { "value" };
        ALL_DATA_COLUMNS = new String[] { "property_name", "value" };
        OuyaFacade.instance = new OuyaFacade();
    }
    
    OuyaFacade() {
        this.pendingRequests = new ArrayList<Runnable>();
        Log.v("OUYAF", "ODK version number: 62");
    }
    
    public static OuyaFacade getInstance() {
        return OuyaFacade.instance;
    }
    
    public boolean isRunningOnOUYAHardware() {
        final boolean b = "cardhu".equals(Build.DEVICE) || "ouya_1_1".equals(Build.DEVICE);
        if (!b) {
            Log.w("OUYAF", "Not running on Ouya hardware: " + Build.DEVICE);
        }
        return b;
    }
    
    public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
        Log.d("inAppPurchase", "Successfully bound to IapService");
        this.remoteService = IIapServiceDefinition.Stub.asInterface(binder);
        while (this.pendingRequests.size() > 0) {
            this.pendingRequests.remove(0).run();
        }
    }
    
    public void onServiceDisconnected(final ComponentName componentName) {
        this.remoteService = null;
        this.bindRequestHasBeenMade = false;
    }
}
