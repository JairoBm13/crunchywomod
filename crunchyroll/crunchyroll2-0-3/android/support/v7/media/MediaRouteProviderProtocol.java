// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.media;

import android.os.IBinder;
import android.os.Messenger;

abstract class MediaRouteProviderProtocol
{
    public static final String CLIENT_DATA_ROUTE_ID = "routeId";
    public static final String CLIENT_DATA_UNSELECT_REASON = "unselectReason";
    public static final String CLIENT_DATA_VOLUME = "volume";
    public static final int CLIENT_MSG_CREATE_ROUTE_CONTROLLER = 3;
    public static final int CLIENT_MSG_REGISTER = 1;
    public static final int CLIENT_MSG_RELEASE_ROUTE_CONTROLLER = 4;
    public static final int CLIENT_MSG_ROUTE_CONTROL_REQUEST = 9;
    public static final int CLIENT_MSG_SELECT_ROUTE = 5;
    public static final int CLIENT_MSG_SET_DISCOVERY_REQUEST = 10;
    public static final int CLIENT_MSG_SET_ROUTE_VOLUME = 7;
    public static final int CLIENT_MSG_UNREGISTER = 2;
    public static final int CLIENT_MSG_UNSELECT_ROUTE = 6;
    public static final int CLIENT_MSG_UPDATE_ROUTE_VOLUME = 8;
    public static final int CLIENT_VERSION_1 = 1;
    public static final int CLIENT_VERSION_CURRENT = 1;
    public static final String SERVICE_DATA_ERROR = "error";
    public static final String SERVICE_INTERFACE = "android.media.MediaRouteProviderService";
    public static final int SERVICE_MSG_CONTROL_REQUEST_FAILED = 4;
    public static final int SERVICE_MSG_CONTROL_REQUEST_SUCCEEDED = 3;
    public static final int SERVICE_MSG_DESCRIPTOR_CHANGED = 5;
    public static final int SERVICE_MSG_GENERIC_FAILURE = 0;
    public static final int SERVICE_MSG_GENERIC_SUCCESS = 1;
    public static final int SERVICE_MSG_REGISTERED = 2;
    public static final int SERVICE_VERSION_1 = 1;
    public static final int SERVICE_VERSION_CURRENT = 1;
    
    public static boolean isValidRemoteMessenger(final Messenger messenger) {
        boolean b = false;
        if (messenger == null) {
            return b;
        }
        try {
            final IBinder binder = messenger.getBinder();
            b = b;
            if (binder != null) {
                b = true;
            }
            return b;
        }
        catch (NullPointerException ex) {
            return false;
        }
    }
}
