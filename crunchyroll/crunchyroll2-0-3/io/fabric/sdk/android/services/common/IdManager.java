// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.common;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.os.Build$VERSION;
import android.os.Build;
import java.util.Collections;
import java.util.HashMap;
import android.bluetooth.BluetoothAdapter;
import android.provider.Settings$Secure;
import java.util.UUID;
import android.content.SharedPreferences;
import java.util.Iterator;
import java.util.Map;
import java.util.Locale;
import org.json.JSONObject;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import java.util.Collection;
import java.util.concurrent.locks.ReentrantLock;
import android.content.Context;
import java.util.regex.Pattern;

public class IdManager
{
    private static final String FORWARD_SLASH_REGEX;
    private static final Pattern ID_PATTERN;
    AdvertisingInfo advertisingInfo;
    AdvertisingInfoProvider advertisingInfoProvider;
    private final Context appContext;
    private final String appIdentifier;
    private final String appInstallIdentifier;
    private final boolean collectHardwareIds;
    private final boolean collectUserIds;
    boolean fetchedAdvertisingInfo;
    private final ReentrantLock installationIdLock;
    private final InstallerPackageNameProvider installerPackageNameProvider;
    private final Collection<Kit> kits;
    
    static {
        ID_PATTERN = Pattern.compile("[^\\p{Alnum}]");
        FORWARD_SLASH_REGEX = Pattern.quote("/");
    }
    
    public IdManager(final Context appContext, final String appIdentifier, final String appInstallIdentifier, final Collection<Kit> kits) {
        this.installationIdLock = new ReentrantLock();
        if (appContext == null) {
            throw new IllegalArgumentException("appContext must not be null");
        }
        if (appIdentifier == null) {
            throw new IllegalArgumentException("appIdentifier must not be null");
        }
        if (kits == null) {
            throw new IllegalArgumentException("kits must not be null");
        }
        this.appContext = appContext;
        this.appIdentifier = appIdentifier;
        this.appInstallIdentifier = appInstallIdentifier;
        this.kits = kits;
        this.installerPackageNameProvider = new InstallerPackageNameProvider();
        this.advertisingInfoProvider = new AdvertisingInfoProvider(appContext);
        if (!(this.collectHardwareIds = CommonUtils.getBooleanResourceValue(appContext, "com.crashlytics.CollectDeviceIdentifiers", true))) {
            Fabric.getLogger().d("Fabric", "Device ID collection disabled for " + appContext.getPackageName());
        }
        if (!(this.collectUserIds = CommonUtils.getBooleanResourceValue(appContext, "com.crashlytics.CollectUserIdentifiers", true))) {
            Fabric.getLogger().d("Fabric", "User information collection disabled for " + appContext.getPackageName());
        }
    }
    
    private void addAppInstallIdTo(final JSONObject jsonObject) {
        try {
            jsonObject.put("APPLICATION_INSTALLATION_UUID".toLowerCase(Locale.US), (Object)this.getAppInstallIdentifier());
        }
        catch (Exception ex) {
            Fabric.getLogger().e("Fabric", "Could not write application id to JSON", ex);
        }
    }
    
    private void addDeviceIdentifiersTo(final JSONObject jsonObject) {
        for (final Map.Entry<DeviceIdentifierType, String> entry : this.getDeviceIdentifiers().entrySet()) {
            try {
                jsonObject.put(entry.getKey().name().toLowerCase(Locale.US), (Object)entry.getValue());
            }
            catch (Exception ex) {
                Fabric.getLogger().e("Fabric", "Could not write value to JSON: " + entry.getKey().name(), ex);
            }
        }
    }
    
    private void addModelName(final JSONObject jsonObject) {
        try {
            jsonObject.put("model", (Object)this.getModelName());
        }
        catch (Exception ex) {
            Fabric.getLogger().e("Fabric", "Could not write model to JSON", ex);
        }
    }
    
    private void addOsVersionTo(final JSONObject jsonObject) {
        try {
            jsonObject.put("os_version", (Object)this.getOsVersionString());
        }
        catch (Exception ex) {
            Fabric.getLogger().e("Fabric", "Could not write OS version to JSON", ex);
        }
    }
    
    private String createInstallationUUID(final SharedPreferences sharedPreferences) {
        this.installationIdLock.lock();
        try {
            String s;
            if ((s = sharedPreferences.getString("crashlytics.installation.id", (String)null)) == null) {
                s = this.formatId(UUID.randomUUID().toString());
                sharedPreferences.edit().putString("crashlytics.installation.id", s).commit();
            }
            return s;
        }
        finally {
            this.installationIdLock.unlock();
        }
    }
    
    private String formatId(final String s) {
        if (s == null) {
            return null;
        }
        return IdManager.ID_PATTERN.matcher(s).replaceAll("").toLowerCase(Locale.US);
    }
    
    private boolean hasPermission(final String s) {
        return this.appContext.checkCallingPermission(s) == 0;
    }
    
    private void putNonNullIdInto(final Map<DeviceIdentifierType, String> map, final DeviceIdentifierType deviceIdentifierType, final String s) {
        if (s != null) {
            map.put(deviceIdentifierType, s);
        }
    }
    
    private String removeForwardSlashesIn(final String s) {
        return s.replaceAll(IdManager.FORWARD_SLASH_REGEX, "");
    }
    
    public boolean canCollectUserIds() {
        return this.collectUserIds;
    }
    
    public String createIdHeaderValue(final String p0, final String p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_2        
        //     1: ldc_w           "\\."
        //     4: new             Ljava/lang/StringBuilder;
        //     7: dup            
        //     8: new             Ljava/lang/String;
        //    11: dup            
        //    12: iconst_3       
        //    13: newarray        C
        //    15: dup            
        //    16: iconst_0       
        //    17: ldc_w           115
        //    20: castore        
        //    21: dup            
        //    22: iconst_1       
        //    23: ldc_w           108
        //    26: castore        
        //    27: dup            
        //    28: iconst_2       
        //    29: ldc_w           99
        //    32: castore        
        //    33: invokespecial   java/lang/String.<init>:([C)V
        //    36: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //    39: invokevirtual   java/lang/StringBuilder.reverse:()Ljava/lang/StringBuilder;
        //    42: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    45: invokevirtual   java/lang/String.replaceAll:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //    48: astore_2       
        //    49: iconst_1       
        //    50: new             Ljava/lang/StringBuilder;
        //    53: dup            
        //    54: invokespecial   java/lang/StringBuilder.<init>:()V
        //    57: aload_1        
        //    58: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    61: aload_2        
        //    62: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    65: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    68: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.sha1:(Ljava/lang/String;)Ljava/lang/String;
        //    71: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.createCipher:(ILjava/lang/String;)Ljavax/crypto/Cipher;
        //    74: astore_2       
        //    75: new             Lorg/json/JSONObject;
        //    78: dup            
        //    79: invokespecial   org/json/JSONObject.<init>:()V
        //    82: astore_3       
        //    83: aload_0        
        //    84: aload_3        
        //    85: invokespecial   io/fabric/sdk/android/services/common/IdManager.addAppInstallIdTo:(Lorg/json/JSONObject;)V
        //    88: aload_0        
        //    89: aload_3        
        //    90: invokespecial   io/fabric/sdk/android/services/common/IdManager.addDeviceIdentifiersTo:(Lorg/json/JSONObject;)V
        //    93: aload_0        
        //    94: aload_3        
        //    95: invokespecial   io/fabric/sdk/android/services/common/IdManager.addOsVersionTo:(Lorg/json/JSONObject;)V
        //    98: aload_0        
        //    99: aload_3        
        //   100: invokespecial   io/fabric/sdk/android/services/common/IdManager.addModelName:(Lorg/json/JSONObject;)V
        //   103: ldc_w           ""
        //   106: astore_1       
        //   107: aload_3        
        //   108: invokevirtual   org/json/JSONObject.length:()I
        //   111: ifle            129
        //   114: aload_2        
        //   115: aload_3        
        //   116: invokevirtual   org/json/JSONObject.toString:()Ljava/lang/String;
        //   119: invokevirtual   java/lang/String.getBytes:()[B
        //   122: invokevirtual   javax/crypto/Cipher.doFinal:([B)[B
        //   125: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.hexify:([B)Ljava/lang/String;
        //   128: astore_1       
        //   129: aload_1        
        //   130: areturn        
        //   131: astore_1       
        //   132: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   135: ldc             "Fabric"
        //   137: ldc_w           "Could not create cipher to encrypt headers."
        //   140: aload_1        
        //   141: invokeinterface io/fabric/sdk/android/Logger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   146: ldc_w           ""
        //   149: areturn        
        //   150: astore_1       
        //   151: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   154: ldc             "Fabric"
        //   156: ldc_w           "Could not encrypt IDs"
        //   159: aload_1        
        //   160: invokeinterface io/fabric/sdk/android/Logger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   165: ldc_w           ""
        //   168: areturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                    
        //  -----  -----  -----  -----  ----------------------------------------
        //  0      75     131    150    Ljava/security/GeneralSecurityException;
        //  114    129    150    169    Ljava/security/GeneralSecurityException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0129:
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
    
    public String getAdvertisingId() {
        String advertisingId = null;
        if (this.collectHardwareIds) {
            final AdvertisingInfo advertisingInfo = this.getAdvertisingInfo();
            advertisingId = advertisingId;
            if (advertisingInfo != null) {
                advertisingId = advertisingInfo.advertisingId;
            }
        }
        return advertisingId;
    }
    
    AdvertisingInfo getAdvertisingInfo() {
        synchronized (this) {
            if (!this.fetchedAdvertisingInfo) {
                this.advertisingInfo = this.advertisingInfoProvider.getAdvertisingInfo();
                this.fetchedAdvertisingInfo = true;
            }
            return this.advertisingInfo;
        }
    }
    
    public String getAndroidId() {
        String formatId = null;
        if (this.collectHardwareIds) {
            final String string = Settings$Secure.getString(this.appContext.getContentResolver(), "android_id");
            formatId = formatId;
            if (!"9774d56d682e549c".equals(string)) {
                formatId = this.formatId(string);
            }
        }
        return formatId;
    }
    
    public String getAppIdentifier() {
        return this.appIdentifier;
    }
    
    public String getAppInstallIdentifier() {
        String s;
        if ((s = this.appInstallIdentifier) == null) {
            final SharedPreferences sharedPrefs = CommonUtils.getSharedPrefs(this.appContext);
            if ((s = sharedPrefs.getString("crashlytics.installation.id", (String)null)) == null) {
                s = this.createInstallationUUID(sharedPrefs);
            }
        }
        return s;
    }
    
    public String getBluetoothMacAddress() {
        Label_0034: {
            if (!this.collectHardwareIds || !this.hasPermission("android.permission.BLUETOOTH")) {
                break Label_0034;
            }
            try {
                final BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
                if (defaultAdapter != null) {
                    this.formatId(defaultAdapter.getAddress());
                }
                return null;
            }
            catch (Exception ex) {
                Fabric.getLogger().e("Fabric", "Utils#getBluetoothMacAddress failed, returning null. Requires prior call to BluetoothAdatpter.getDefaultAdapter() on thread that has called Looper.prepare()", ex);
                return null;
            }
        }
    }
    
    public Map<DeviceIdentifierType, String> getDeviceIdentifiers() {
        final HashMap<DeviceIdentifierType, String> hashMap = new HashMap<DeviceIdentifierType, String>();
        for (final Kit kit : this.kits) {
            if (kit instanceof DeviceIdentifierProvider) {
                for (final Map.Entry<DeviceIdentifierType, String> entry : ((DeviceIdentifierProvider)kit).getDeviceIdentifiers().entrySet()) {
                    this.putNonNullIdInto(hashMap, entry.getKey(), entry.getValue());
                }
            }
        }
        this.putNonNullIdInto(hashMap, DeviceIdentifierType.ANDROID_ID, this.getAndroidId());
        this.putNonNullIdInto(hashMap, DeviceIdentifierType.ANDROID_DEVICE_ID, this.getTelephonyId());
        this.putNonNullIdInto(hashMap, DeviceIdentifierType.ANDROID_SERIAL, this.getSerialNumber());
        this.putNonNullIdInto(hashMap, DeviceIdentifierType.WIFI_MAC_ADDRESS, this.getWifiMacAddress());
        this.putNonNullIdInto(hashMap, DeviceIdentifierType.BLUETOOTH_MAC_ADDRESS, this.getBluetoothMacAddress());
        this.putNonNullIdInto(hashMap, DeviceIdentifierType.ANDROID_ADVERTISING_ID, this.getAdvertisingId());
        return (Map<DeviceIdentifierType, String>)Collections.unmodifiableMap((Map<?, ?>)hashMap);
    }
    
    public String getDeviceUUID() {
        String s = "";
        if (this.collectHardwareIds && (s = this.getAndroidId()) == null) {
            final SharedPreferences sharedPrefs = CommonUtils.getSharedPrefs(this.appContext);
            if ((s = sharedPrefs.getString("crashlytics.installation.id", (String)null)) == null) {
                s = this.createInstallationUUID(sharedPrefs);
            }
        }
        return s;
    }
    
    public String getInstallerPackageName() {
        return this.installerPackageNameProvider.getInstallerPackageName(this.appContext);
    }
    
    public String getModelName() {
        return String.format(Locale.US, "%s/%s", this.removeForwardSlashesIn(Build.MANUFACTURER), this.removeForwardSlashesIn(Build.MODEL));
    }
    
    public String getOsVersionString() {
        return String.format(Locale.US, "%s/%s", this.removeForwardSlashesIn(Build$VERSION.RELEASE), this.removeForwardSlashesIn(Build$VERSION.INCREMENTAL));
    }
    
    public String getSerialNumber() {
        if (this.collectHardwareIds && Build$VERSION.SDK_INT >= 9) {
            try {
                return this.formatId((String)Build.class.getField("SERIAL").get(null));
            }
            catch (Exception ex) {
                Fabric.getLogger().e("Fabric", "Could not retrieve android.os.Build.SERIAL value", ex);
            }
        }
        return null;
    }
    
    public String getTelephonyId() {
        if (this.collectHardwareIds && this.hasPermission("android.permission.READ_PHONE_STATE")) {
            final TelephonyManager telephonyManager = (TelephonyManager)this.appContext.getSystemService("phone");
            if (telephonyManager != null) {
                return this.formatId(telephonyManager.getDeviceId());
            }
        }
        return null;
    }
    
    public String getWifiMacAddress() {
        if (this.collectHardwareIds && this.hasPermission("android.permission.ACCESS_WIFI_STATE")) {
            final WifiManager wifiManager = (WifiManager)this.appContext.getSystemService("wifi");
            if (wifiManager != null) {
                final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null) {
                    return this.formatId(connectionInfo.getMacAddress());
                }
            }
        }
        return null;
    }
    
    public Boolean isLimitAdTrackingEnabled() {
        Boolean value = null;
        if (this.collectHardwareIds) {
            final AdvertisingInfo advertisingInfo = this.getAdvertisingInfo();
            value = value;
            if (advertisingInfo != null) {
                value = advertisingInfo.limitAdTrackingEnabled;
            }
        }
        return value;
    }
    
    public enum DeviceIdentifierType
    {
        ANDROID_ADVERTISING_ID(103), 
        ANDROID_DEVICE_ID(101), 
        ANDROID_ID(100), 
        ANDROID_SERIAL(102), 
        BLUETOOTH_MAC_ADDRESS(2), 
        FONT_TOKEN(53), 
        WIFI_MAC_ADDRESS(1);
        
        public final int protobufIndex;
        
        private DeviceIdentifierType(final int protobufIndex) {
            this.protobufIndex = protobufIndex;
        }
    }
}
