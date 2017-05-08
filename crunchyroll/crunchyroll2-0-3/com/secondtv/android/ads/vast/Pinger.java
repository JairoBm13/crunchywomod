// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.vast;

import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.Collection;

public class Pinger
{
    public void ping(final String s) {
        new Thread(new UrlPinger(s)).start();
    }
    
    public void ping(final Collection<String> collection) {
        final Iterator<String> iterator = collection.iterator();
        while (iterator.hasNext()) {
            this.ping(iterator.next());
        }
    }
    
    private static class UrlPinger implements Runnable
    {
        private String mUrl;
        
        UrlPinger(final String mUrl) {
            this.mUrl = mUrl;
        }
        
        @Override
        public void run() {
            try {
                final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(this.mUrl).openConnection();
                httpURLConnection.setRequestProperty("Connection", "close");
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() == 200) {}
            }
            catch (Exception ex) {}
        }
    }
}
