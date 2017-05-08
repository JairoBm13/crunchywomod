// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android;

import java.util.Enumeration;
import android.os.SystemClock;
import java.util.HashMap;
import java.io.Serializable;
import java.io.Closeable;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.io.IOException;
import android.text.TextUtils;
import java.io.InputStream;
import java.util.Properties;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.Map;
import java.util.concurrent.Callable;

class FabricKitsFinder implements Callable<Map<String, KitInfo>>
{
    final String apkFileName;
    
    FabricKitsFinder(final String apkFileName) {
        this.apkFileName = apkFileName;
    }
    
    private KitInfo loadKitInfo(final ZipEntry zipEntry, ZipFile ex) {
        Object o = null;
        Object inputStream = null;
        Label_0182: {
            try {
                ex = (IOException)(o = (inputStream = ((ZipFile)ex).getInputStream(zipEntry)));
                Serializable property = new Properties();
                inputStream = ex;
                o = ex;
                ((Properties)property).load((InputStream)ex);
                inputStream = ex;
                o = ex;
                final Object property2 = ((Properties)property).getProperty("fabric-identifier");
                inputStream = ex;
                o = ex;
                final String property3 = ((Properties)property).getProperty("fabric-version");
                inputStream = ex;
                o = ex;
                property = ((Properties)property).getProperty("fabric-build-type");
                inputStream = ex;
                o = ex;
                if (!TextUtils.isEmpty((CharSequence)property2)) {
                    inputStream = ex;
                    o = ex;
                    if (!TextUtils.isEmpty((CharSequence)property3)) {
                        break Label_0182;
                    }
                }
                inputStream = ex;
                o = ex;
                throw new IllegalStateException("Invalid format of fabric file," + zipEntry.getName());
            }
            catch (IOException ex) {
                o = inputStream;
                Fabric.getLogger().e("Fabric", "Error when parsing fabric properties " + zipEntry.getName(), ex);
                return null;
                o = ex;
                final Serializable property;
                final String property3;
                final Object property2 = new KitInfo((String)property2, property3, (String)property);
                return (KitInfo)property2;
            }
            finally {
                CommonUtils.closeQuietly((Closeable)o);
            }
        }
    }
    
    @Override
    public Map<String, KitInfo> call() throws Exception {
        final HashMap<String, KitInfo> hashMap = new HashMap<String, KitInfo>();
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        int n = 0;
        final ZipFile loadApkFile = this.loadApkFile();
        final Enumeration<? extends ZipEntry> entries = loadApkFile.entries();
        while (entries.hasMoreElements()) {
            final int n2 = n + 1;
            final ZipEntry zipEntry = (ZipEntry)entries.nextElement();
            n = n2;
            if (zipEntry.getName().startsWith("fabric/")) {
                n = n2;
                if (zipEntry.getName().length() <= "fabric/".length()) {
                    continue;
                }
                final KitInfo loadKitInfo = this.loadKitInfo(zipEntry, loadApkFile);
                n = n2;
                if (loadKitInfo == null) {
                    continue;
                }
                hashMap.put(loadKitInfo.getIdentifier(), loadKitInfo);
                Fabric.getLogger().v("Fabric", String.format("Found kit:[%s] version:[%s]", loadKitInfo.getIdentifier(), loadKitInfo.getVersion()));
                n = n2;
            }
        }
        while (true) {
            if (loadApkFile == null) {
                break Label_0169;
            }
            try {
                loadApkFile.close();
                Fabric.getLogger().v("Fabric", "finish scanning in " + (SystemClock.elapsedRealtime() - elapsedRealtime) + " reading:" + n);
                return hashMap;
            }
            catch (IOException ex) {
                continue;
            }
            break;
        }
    }
    
    protected ZipFile loadApkFile() throws IOException {
        return new ZipFile(this.apkFileName);
    }
}
