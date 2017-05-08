// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import android.content.Context;
import com.crunchyroll.android.api.models.BIFFile;

public class ProcessBifFileTask extends BaseTask<BIFFile>
{
    private String url;
    
    public ProcessBifFileTask(final Context context, final String url) {
        super(context);
        this.url = null;
        this.url = url;
    }
    
    @Override
    public BIFFile call() throws Exception {
        final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(this.url).openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setDoInput(true);
        httpURLConnection.connect();
        final InputStream inputStream = httpURLConnection.getInputStream();
        final byte[] array = new byte[1024];
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (true) {
            final int read = inputStream.read(array, 0, array.length);
            if (read == -1) {
                break;
            }
            byteArrayOutputStream.write(array, 0, read);
        }
        byteArrayOutputStream.flush();
        httpURLConnection.disconnect();
        final BIFFile bifFile = new BIFFile(byteArrayOutputStream.toByteArray());
        bifFile.prepare();
        if (bifFile.isReadyForDisplay()) {
            return bifFile;
        }
        return null;
    }
    
    @Override
    protected void onSuccess(final BIFFile bifFile) throws Exception {
        if (!this.isCancelled()) {
            super.onSuccess(bifFile);
        }
    }
}
