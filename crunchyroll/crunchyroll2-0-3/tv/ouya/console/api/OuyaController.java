// 
// Decompiled by Procyon v0.5.30
// 

package tv.ouya.console.api;

import android.view.KeyEvent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import java.util.Collections;
import java.util.HashMap;
import android.view.InputDevice;
import android.util.SparseBooleanArray;
import android.util.SparseArray;
import android.util.SparseIntArray;
import java.util.Map;
import android.content.Context;

public class OuyaController
{
    private static int[] AVAILABLE_AXES;
    private static Context appContext;
    private static final Map<String, SparseIntArray> s_axisByDeviceName;
    private static final Map<String, SparseIntArray> s_buttonByDeviceName;
    static final OuyaController[] s_controllers;
    private SparseArray<Float> axisValues;
    private SparseBooleanArray buttonValues;
    private InputDevice device;
    private SparseIntArray deviceAxis;
    private int deviceId;
    private SparseIntArray deviceKeycodes;
    private SparseIntArray thisFrameButtonValues;
    
    static {
        OuyaController.AVAILABLE_AXES = new int[] { 0, 1, 11, 14, 17, 18 };
        s_controllers = new OuyaController[4];
        final SparseIntArray sparseIntArray = new SparseIntArray();
        sparseIntArray.put(96, 96);
        sparseIntArray.put(99, 99);
        sparseIntArray.put(100, 100);
        sparseIntArray.put(97, 97);
        sparseIntArray.put(102, 102);
        sparseIntArray.put(104, 104);
        sparseIntArray.put(106, 106);
        sparseIntArray.put(103, 103);
        sparseIntArray.put(105, 105);
        sparseIntArray.put(107, 107);
        sparseIntArray.put(20, 20);
        sparseIntArray.put(19, 19);
        sparseIntArray.put(22, 22);
        sparseIntArray.put(21, 21);
        sparseIntArray.put(108, 82);
        final SparseIntArray sparseIntArray2 = new SparseIntArray();
        sparseIntArray2.put(96, 96);
        sparseIntArray2.put(99, 99);
        sparseIntArray2.put(100, 100);
        sparseIntArray2.put(97, 97);
        sparseIntArray2.put(102, 102);
        sparseIntArray2.put(104, 104);
        sparseIntArray2.put(106, 106);
        sparseIntArray2.put(103, 103);
        sparseIntArray2.put(105, 105);
        sparseIntArray2.put(107, 107);
        sparseIntArray2.put(20, 20);
        sparseIntArray2.put(19, 19);
        sparseIntArray2.put(22, 22);
        sparseIntArray2.put(21, 21);
        sparseIntArray2.put(82, 82);
        final HashMap<String, SparseIntArray> hashMap = new HashMap<String, SparseIntArray>();
        hashMap.put("Generic X-Box pad", sparseIntArray);
        hashMap.put("OUYA Game Controller", sparseIntArray2);
        s_buttonByDeviceName = Collections.unmodifiableMap((Map<?, ?>)hashMap);
        final SparseIntArray sparseIntArray3 = new SparseIntArray();
        sparseIntArray3.put(0, 0);
        sparseIntArray3.put(1, 1);
        sparseIntArray3.put(11, 11);
        sparseIntArray3.put(14, 14);
        sparseIntArray3.put(17, 17);
        sparseIntArray3.put(18, 18);
        final SparseIntArray sparseIntArray4 = new SparseIntArray();
        sparseIntArray4.put(0, 0);
        sparseIntArray4.put(1, 1);
        sparseIntArray4.put(11, 11);
        sparseIntArray4.put(14, 14);
        sparseIntArray4.put(17, 17);
        sparseIntArray4.put(18, 18);
        final HashMap<String, SparseIntArray> hashMap2 = new HashMap<String, SparseIntArray>();
        hashMap2.put("Generic X-Box pad", sparseIntArray3);
        hashMap2.put("OUYA Game Controller", sparseIntArray4);
        s_axisByDeviceName = Collections.unmodifiableMap((Map<?, ?>)hashMap2);
    }
    
    OuyaController(final int deviceId) {
        this.deviceId = deviceId;
        this.device = InputDevice.getDevice(deviceId);
        this.axisValues = (SparseArray<Float>)new SparseArray();
        this.buttonValues = new SparseBooleanArray();
        this.thisFrameButtonValues = new SparseIntArray();
        this.deviceAxis = getAxisMap(this.device);
        this.deviceKeycodes = getButtonMap(this.device);
    }
    
    private static SparseIntArray getAxisMap(final InputDevice inputDevice) {
        String name;
        if (inputDevice != null) {
            name = inputDevice.getName();
        }
        else {
            name = "OUYA Game Controller";
        }
        String s = name;
        if (name.contains("OUYA Game Controller")) {
            s = "OUYA Game Controller";
        }
        SparseIntArray sparseIntArray;
        if ((sparseIntArray = OuyaController.s_axisByDeviceName.get(s)) == null) {
            sparseIntArray = OuyaController.s_axisByDeviceName.get("OUYA Game Controller");
        }
        return sparseIntArray;
    }
    
    private static SparseIntArray getButtonMap(final InputDevice inputDevice) {
        String name;
        if (inputDevice != null) {
            name = inputDevice.getName();
        }
        else {
            name = "OUYA Game Controller";
        }
        String s = name;
        if (name.contains("OUYA Game Controller")) {
            s = "OUYA Game Controller";
        }
        SparseIntArray sparseIntArray;
        if ((sparseIntArray = OuyaController.s_buttonByDeviceName.get(s)) == null) {
            sparseIntArray = OuyaController.s_buttonByDeviceName.get("OUYA Game Controller");
        }
        return sparseIntArray;
    }
    
    public static OuyaController getControllerByDeviceId(final int n) {
        final OuyaController[] s_controllers = OuyaController.s_controllers;
        for (int length = s_controllers.length, i = 0; i < length; ++i) {
            final OuyaController ouyaController = s_controllers[i];
            if (ouyaController != null && ouyaController.getDeviceId() == n) {
                return ouyaController;
            }
        }
        return null;
    }
    
    private static OuyaController getOrCreateControllerByDeviceId(final int n) {
        final OuyaController controllerByDeviceId = getControllerByDeviceId(n);
        if (controllerByDeviceId != null) {
            return controllerByDeviceId;
        }
        final int playerNumFromDeviceId = getPlayerNumFromDeviceId(n);
        if (playerNumFromDeviceId < 0 || playerNumFromDeviceId >= 4) {
            return null;
        }
        if (OuyaController.s_controllers[playerNumFromDeviceId] != null) {
            Log.e("OuyaController", "Controller for player " + playerNumFromDeviceId + " already found, but doesn't match device id " + "(expected " + n + " got " + OuyaController.s_controllers[playerNumFromDeviceId].getDeviceId() + ")");
            return OuyaController.s_controllers[playerNumFromDeviceId];
        }
        return OuyaController.s_controllers[playerNumFromDeviceId] = new OuyaController(n);
    }
    
    private static int getPlayerNumFromDeviceId(int int1) {
        final int n = -1;
        if (OuyaController.appContext == null) {
            int1 = n;
            return int1;
        }
        final Cursor query = OuyaController.appContext.getContentResolver().query(Uri.parse("content://tv.ouya.controllerdata/"), new String[] { "player_num" }, "input_device_id = ?", new String[] { String.valueOf(int1) }, (String)null);
        Label_0087: {
            if (query == null) {
                break Label_0087;
            }
            try {
                if (query.moveToNext()) {
                    return int1 = query.getInt(0);
                }
                int1 = n;
                return -1;
            }
            finally {
                if (query != null) {
                    query.close();
                }
            }
        }
    }
    
    private int mapKeyCode(final KeyEvent keyEvent) {
        if (this.deviceKeycodes.indexOfKey(keyEvent.getKeyCode()) < 0) {
            return -1;
        }
        return this.deviceKeycodes.get(keyEvent.getKeyCode());
    }
    
    public static boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        return onKeyEvent(n, keyEvent);
    }
    
    private static boolean onKeyEvent(int n, final KeyEvent keyEvent) {
        final OuyaController orCreateControllerByDeviceId = getOrCreateControllerByDeviceId(keyEvent.getDeviceId());
        if (orCreateControllerByDeviceId == null) {
            return false;
        }
        final int mapKeyCode = orCreateControllerByDeviceId.mapKeyCode(keyEvent);
        if (mapKeyCode < 0) {
            return false;
        }
        boolean b = false;
        switch (keyEvent.getAction()) {
            case 0:
            case 2: {
                b = true;
                break;
            }
            case 1: {
                b = false;
                break;
            }
        }
        if (orCreateControllerByDeviceId.buttonValues.indexOfKey(mapKeyCode) < 0 || orCreateControllerByDeviceId.buttonValues.get(mapKeyCode) != b) {
            if (b) {
                n = 1;
            }
            else {
                n = 2;
            }
            orCreateControllerByDeviceId.thisFrameButtonValues.put(mapKeyCode, n | orCreateControllerByDeviceId.thisFrameButtonValues.get(mapKeyCode, 0));
        }
        orCreateControllerByDeviceId.buttonValues.put(mapKeyCode, b);
        return true;
    }
    
    public static boolean onKeyUp(final int n, final KeyEvent keyEvent) {
        return onKeyEvent(n, keyEvent);
    }
    
    public int getDeviceId() {
        return this.deviceId;
    }
}
