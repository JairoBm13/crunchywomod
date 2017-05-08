// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.beta;

import io.fabric.sdk.android.Fabric;
import java.io.FileNotFoundException;
import android.content.pm.PackageManager$NameNotFoundException;
import java.io.InputStream;
import java.io.FileInputStream;
import android.content.Context;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import io.fabric.sdk.android.services.cache.ValueLoader;

public class DeviceTokenLoader implements ValueLoader<String>
{
    String determineDeviceToken(final ZipInputStream zipInputStream) throws IOException {
        String name;
        do {
            final ZipEntry nextEntry = zipInputStream.getNextEntry();
            if (nextEntry == null) {
                return "";
            }
            name = nextEntry.getName();
        } while (!name.startsWith("assets/com.crashlytics.android.beta/dirfactor-device-token="));
        return name.substring("assets/com.crashlytics.android.beta/dirfactor-device-token=".length(), name.length() - 1);
    }
    
    ZipInputStream getZipInputStreamOfAppApkFrom(final Context context) throws PackageManager$NameNotFoundException, FileNotFoundException {
        return new ZipInputStream(new FileInputStream(context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).sourceDir));
    }
    
    @Override
    public String load(final Context context) throws Exception {
        final long nanoTime = System.nanoTime();
        final String s = "";
        String s2 = null;
        String s3 = null;
        Object determineDeviceToken = null;
        String zipInputStreamOfAppApk = null;
        while (true) {
            try {
                final ZipInputStream zipInputStream = (ZipInputStream)(determineDeviceToken = (s3 = (s2 = (zipInputStreamOfAppApk = (String)this.getZipInputStreamOfAppApkFrom(context)))));
                zipInputStreamOfAppApk = (String)(determineDeviceToken = this.determineDeviceToken(zipInputStream));
                if (zipInputStream == null) {
                    break Label_0066;
                }
                try {
                    zipInputStream.close();
                    determineDeviceToken = zipInputStreamOfAppApk;
                    Fabric.getLogger().d("Beta", "Beta device token load took " + (System.nanoTime() - nanoTime) / 1000000.0 + "ms");
                    return (String)determineDeviceToken;
                }
                catch (IOException ex) {
                    Fabric.getLogger().e("Beta", "Failed to close the APK file", ex);
                    determineDeviceToken = zipInputStreamOfAppApk;
                }
            }
            catch (PackageManager$NameNotFoundException ex2) {
                determineDeviceToken = zipInputStreamOfAppApk;
                Fabric.getLogger().e("Beta", "Failed to find this app in the PackageManager", (Throwable)ex2);
                determineDeviceToken = s;
                if (zipInputStreamOfAppApk != null) {
                    try {
                        ((ZipInputStream)zipInputStreamOfAppApk).close();
                        determineDeviceToken = s;
                        continue;
                    }
                    catch (IOException ex3) {
                        Fabric.getLogger().e("Beta", "Failed to close the APK file", ex3);
                        determineDeviceToken = s;
                        continue;
                    }
                    continue;
                }
                continue;
            }
            catch (FileNotFoundException ex4) {
                determineDeviceToken = s2;
                Fabric.getLogger().e("Beta", "Failed to find the APK file", ex4);
                determineDeviceToken = s;
                if (s2 != null) {
                    try {
                        ((ZipInputStream)s2).close();
                        determineDeviceToken = s;
                        continue;
                    }
                    catch (IOException ex5) {
                        Fabric.getLogger().e("Beta", "Failed to close the APK file", ex5);
                        determineDeviceToken = s;
                        continue;
                    }
                    continue;
                }
                continue;
            }
            catch (IOException ex6) {
                determineDeviceToken = s3;
                Fabric.getLogger().e("Beta", "Failed to read the APK file", ex6);
                determineDeviceToken = s;
                if (s3 != null) {
                    try {
                        ((ZipInputStream)s3).close();
                        determineDeviceToken = s;
                        continue;
                    }
                    catch (IOException ex7) {
                        Fabric.getLogger().e("Beta", "Failed to close the APK file", ex7);
                        determineDeviceToken = s;
                        continue;
                    }
                    continue;
                }
                continue;
            }
            finally {
                Label_0327: {
                    if (determineDeviceToken == null) {
                        break Label_0327;
                    }
                    try {
                        ((ZipInputStream)determineDeviceToken).close();
                    }
                    catch (IOException ex8) {
                        Fabric.getLogger().e("Beta", "Failed to close the APK file", ex8);
                    }
                }
            }
            break;
        }
    }
}
