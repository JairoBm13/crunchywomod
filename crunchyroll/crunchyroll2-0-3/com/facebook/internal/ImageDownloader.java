// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.internal;

import android.os.Looper;
import java.net.URISyntaxException;
import java.io.IOException;
import java.net.URI;
import java.net.URLConnection;
import java.io.Closeable;
import com.facebook.android.R;
import com.facebook.FacebookException;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import android.content.Context;
import java.util.HashMap;
import java.util.Map;
import android.os.Handler;

public class ImageDownloader
{
    private static final int CACHE_READ_QUEUE_MAX_CONCURRENT = 2;
    private static final int DOWNLOAD_QUEUE_MAX_CONCURRENT = 8;
    private static WorkQueue cacheReadQueue;
    private static WorkQueue downloadQueue;
    private static Handler handler;
    private static final Map<RequestKey, DownloaderContext> pendingRequests;
    
    static {
        ImageDownloader.downloadQueue = new WorkQueue(8);
        ImageDownloader.cacheReadQueue = new WorkQueue(2);
        pendingRequests = new HashMap<RequestKey, DownloaderContext>();
    }
    
    public static boolean cancelRequest(final ImageRequest imageRequest) {
        boolean b = false;
        final RequestKey requestKey = new RequestKey(imageRequest.getImageUri(), imageRequest.getCallerTag());
        synchronized (ImageDownloader.pendingRequests) {
            final DownloaderContext downloaderContext = ImageDownloader.pendingRequests.get(requestKey);
            if (downloaderContext != null) {
                b = true;
                if (downloaderContext.workItem.cancel()) {
                    ImageDownloader.pendingRequests.remove(requestKey);
                }
                else {
                    downloaderContext.isCancelled = true;
                }
            }
            return b;
        }
    }
    
    public static void clearCache(final Context context) {
        ImageResponseCache.clearCache(context);
        UrlRedirectCache.clearCache(context);
    }
    
    private static void download(final RequestKey requestKey, Context context) {
        while (true) {
            URLConnection urlConnection = null;
            URLConnection urlConnection2 = null;
            URLConnection urlConnection3 = null;
            final InputStream inputStream = null;
            final Closeable closeable = null;
            final Closeable closeable2 = null;
            final Closeable closeable3 = null;
            final char[] array = null;
            final Bitmap bitmap = null;
            final StringBuilder sb = null;
            int n = 1;
            int n2 = 1;
            int n4;
            int n3 = n4 = 1;
            Closeable closeable4 = closeable3;
            int n5 = n2;
            Closeable closeable5 = closeable;
            Object o = closeable2;
            HttpURLConnection httpURLConnection;
            Object errorStream;
            Object decodeStream;
            Object o2 = null;
            int read;
            URI uri;
            String headerField;
            Label_0342_Outer:Label_0449_Outer:
            while (true) {
                Label_0821_Outer:Label_0955_Outer:
                while (true) {
                    try {
                        httpURLConnection = (HttpURLConnection)(urlConnection3 = new URL(requestKey.uri.toString()).openConnection());
                        n4 = n3;
                        closeable4 = closeable3;
                        urlConnection = httpURLConnection;
                        n5 = n2;
                        closeable5 = closeable;
                        urlConnection2 = httpURLConnection;
                        o = closeable2;
                        httpURLConnection.setInstanceFollowRedirects(false);
                        urlConnection3 = httpURLConnection;
                        n4 = n3;
                        closeable4 = closeable3;
                        urlConnection = httpURLConnection;
                        n5 = n2;
                        closeable5 = closeable;
                        urlConnection2 = httpURLConnection;
                        o = closeable2;
                        switch (httpURLConnection.getResponseCode()) {
                            case 301:
                            case 302: {
                                break Label_0821_Outer;
                            }
                            case 200: {
                                break Label_0821_Outer;
                            }
                            default: {
                                break Label_0821_Outer;
                            }
                        }
                        urlConnection3 = httpURLConnection;
                        n4 = n3;
                        closeable4 = closeable3;
                        urlConnection = httpURLConnection;
                        n5 = n2;
                        closeable5 = closeable;
                        urlConnection2 = httpURLConnection;
                        o = closeable2;
                        errorStream = httpURLConnection.getErrorStream();
                        urlConnection3 = httpURLConnection;
                        n4 = n3;
                        closeable4 = (Closeable)errorStream;
                        urlConnection = httpURLConnection;
                        n5 = n2;
                        closeable5 = (Closeable)errorStream;
                        urlConnection2 = httpURLConnection;
                        o = errorStream;
                        decodeStream = new StringBuilder();
                        // iftrue(Label_1010:, errorStream == null)
                        // iftrue(Label_0921:, read <= 0)
                        while (true) {
                            Block_3: {
                                break Block_3;
                                while (true) {
                                    urlConnection3 = httpURLConnection;
                                    n4 = n3;
                                    closeable4 = (Closeable)errorStream;
                                    urlConnection = httpURLConnection;
                                    n5 = n2;
                                    closeable5 = (Closeable)errorStream;
                                    urlConnection2 = httpURLConnection;
                                    o = errorStream;
                                    ((StringBuilder)decodeStream).append((char[])o2, 0, read);
                                    urlConnection3 = httpURLConnection;
                                    n4 = n3;
                                    closeable4 = (Closeable)errorStream;
                                    urlConnection = httpURLConnection;
                                    n5 = n2;
                                    closeable5 = (Closeable)errorStream;
                                    urlConnection2 = httpURLConnection;
                                    o = errorStream;
                                    read = ((InputStreamReader)context).read((char[])o2, 0, ((char[])o2).length);
                                    continue Label_0342_Outer;
                                }
                            }
                            urlConnection3 = httpURLConnection;
                            n4 = n3;
                            closeable4 = (Closeable)errorStream;
                            urlConnection = httpURLConnection;
                            n5 = n2;
                            closeable5 = (Closeable)errorStream;
                            urlConnection2 = httpURLConnection;
                            o = errorStream;
                            context = (Context)new InputStreamReader((InputStream)errorStream);
                            urlConnection3 = httpURLConnection;
                            n4 = n3;
                            closeable4 = (Closeable)errorStream;
                            urlConnection = httpURLConnection;
                            n5 = n2;
                            closeable5 = (Closeable)errorStream;
                            urlConnection2 = httpURLConnection;
                            o = errorStream;
                            o2 = new char[128];
                            continue Label_0449_Outer;
                        }
                    }
                    catch (IOException o2) {
                        Utility.closeQuietly(closeable4);
                        Utility.disconnectQuietly(urlConnection3);
                        decodeStream = sb;
                        if (n4 != 0) {
                            issueResponse(requestKey, (Exception)o2, (Bitmap)decodeStream, false);
                        }
                        return;
                        n4 = n3;
                        urlConnection2 = httpURLConnection;
                        o = closeable2;
                        context = (Context)ImageResponseCache.interceptAndCacheImageStream(context, httpURLConnection);
                        n4 = n3;
                        urlConnection2 = httpURLConnection;
                        o = context;
                        decodeStream = BitmapFactory.decodeStream((InputStream)context);
                        o2 = array;
                        n3 = n;
                        errorStream = context;
                        // iftrue(Label_0821:, context.isCancelled)
                    Label_0955:
                        while (true) {
                            while (true) {
                                while (true) {
                                    break Label_0821;
                                    n4 = n3;
                                    urlConnection2 = httpURLConnection;
                                    o = errorStream;
                                    o2 = new FacebookException(((StringBuilder)decodeStream).toString());
                                    decodeStream = bitmap;
                                    n3 = n;
                                    break Label_0821;
                                    Label_1010: {
                                        n4 = n3;
                                    }
                                    urlConnection2 = httpURLConnection;
                                    o = errorStream;
                                    ((StringBuilder)decodeStream).append(context.getString(R.string.com_facebook_image_download_unknown_error));
                                    continue Label_0955;
                                    Utility.closeQuietly((Closeable)errorStream);
                                    Utility.disconnectQuietly(httpURLConnection);
                                    n4 = n3;
                                    continue Label_0821_Outer;
                                    n4 = n;
                                    decodeStream = bitmap;
                                    o2 = array;
                                    n3 = n2;
                                    errorStream = inputStream;
                                    urlConnection2 = httpURLConnection;
                                    o = closeable2;
                                    n4 = n;
                                    urlConnection2 = httpURLConnection;
                                    o = closeable2;
                                    enqueueCacheRead(((DownloaderContext)context).request, new RequestKey(uri, requestKey.tag), false);
                                    errorStream = inputStream;
                                    n3 = n2;
                                    o2 = array;
                                    decodeStream = bitmap;
                                    continue Label_0955_Outer;
                                }
                                n2 = 0;
                                n = (n4 = 0);
                                urlConnection2 = httpURLConnection;
                                o = closeable2;
                                headerField = httpURLConnection.getHeaderField("location");
                                n4 = n;
                                decodeStream = bitmap;
                                o2 = array;
                                n3 = n2;
                                errorStream = inputStream;
                                urlConnection2 = httpURLConnection;
                                o = closeable2;
                                n4 = n;
                                urlConnection2 = httpURLConnection;
                                o = closeable2;
                                uri = new URI(headerField);
                                n4 = n;
                                urlConnection2 = httpURLConnection;
                                o = closeable2;
                                UrlRedirectCache.cacheUriRedirect(context, requestKey.uri, uri);
                                n4 = n;
                                urlConnection2 = httpURLConnection;
                                o = closeable2;
                                context = (Context)removePendingRequest(requestKey);
                                decodeStream = bitmap;
                                o2 = array;
                                n3 = n2;
                                errorStream = inputStream;
                                continue;
                            }
                            Label_0921: {
                                n4 = n3;
                            }
                            urlConnection2 = httpURLConnection;
                            o = errorStream;
                            Utility.closeQuietly((Closeable)context);
                            continue Label_0955;
                        }
                    }
                    // iftrue(Label_0821:, Utility.isNullOrEmpty(headerField))
                    // iftrue(Label_0821:, context == null)
                    catch (URISyntaxException o2) {
                        Utility.closeQuietly(closeable5);
                        Utility.disconnectQuietly(urlConnection);
                        decodeStream = sb;
                        n4 = n5;
                        continue;
                    }
                    finally {
                        Utility.closeQuietly((Closeable)o);
                        Utility.disconnectQuietly(urlConnection2);
                    }
                    break;
                }
                continue;
            }
        }
    }
    
    public static void downloadAsync(final ImageRequest request) {
        if (request == null) {
            return;
        }
        while (true) {
            final RequestKey requestKey = new RequestKey(request.getImageUri(), request.getCallerTag());
            synchronized (ImageDownloader.pendingRequests) {
                final DownloaderContext downloaderContext = ImageDownloader.pendingRequests.get(requestKey);
                if (downloaderContext != null) {
                    downloaderContext.request = request;
                    downloaderContext.isCancelled = false;
                    downloaderContext.workItem.moveToFront();
                    return;
                }
            }
            final ImageRequest imageRequest;
            enqueueCacheRead(imageRequest, requestKey, imageRequest.isCachedRedirectAllowed());
        }
    }
    
    private static void enqueueCacheRead(final ImageRequest imageRequest, final RequestKey requestKey, final boolean b) {
        enqueueRequest(imageRequest, requestKey, ImageDownloader.cacheReadQueue, new CacheReadWorkItem(imageRequest.getContext(), requestKey, b));
    }
    
    private static void enqueueDownload(final ImageRequest imageRequest, final RequestKey requestKey) {
        enqueueRequest(imageRequest, requestKey, ImageDownloader.downloadQueue, new DownloadImageWorkItem(imageRequest.getContext(), requestKey));
    }
    
    private static void enqueueRequest(final ImageRequest request, final RequestKey requestKey, final WorkQueue workQueue, final Runnable runnable) {
        synchronized (ImageDownloader.pendingRequests) {
            final DownloaderContext downloaderContext = new DownloaderContext();
            downloaderContext.request = request;
            ImageDownloader.pendingRequests.put(requestKey, downloaderContext);
            downloaderContext.workItem = workQueue.addActiveWorkItem(runnable);
        }
    }
    
    private static Handler getHandler() {
        synchronized (ImageDownloader.class) {
            if (ImageDownloader.handler == null) {
                ImageDownloader.handler = new Handler(Looper.getMainLooper());
            }
            return ImageDownloader.handler;
        }
    }
    
    private static void issueResponse(final RequestKey requestKey, final Exception ex, final Bitmap bitmap, final boolean b) {
        final DownloaderContext removePendingRequest = removePendingRequest(requestKey);
        if (removePendingRequest != null && !removePendingRequest.isCancelled) {
            final ImageRequest request = removePendingRequest.request;
            final ImageRequest.Callback callback = request.getCallback();
            if (callback != null) {
                getHandler().post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        callback.onCompleted(new ImageResponse(request, ex, b, bitmap));
                    }
                });
            }
        }
    }
    
    public static void prioritizeRequest(final ImageRequest imageRequest) {
        final RequestKey requestKey = new RequestKey(imageRequest.getImageUri(), imageRequest.getCallerTag());
        synchronized (ImageDownloader.pendingRequests) {
            final DownloaderContext downloaderContext = ImageDownloader.pendingRequests.get(requestKey);
            if (downloaderContext != null) {
                downloaderContext.workItem.moveToFront();
            }
        }
    }
    
    private static void readFromCache(final RequestKey requestKey, final Context context, final boolean b) {
        final InputStream inputStream = null;
        final boolean b2 = false;
        InputStream inputStream2 = inputStream;
        boolean b3 = b2;
        if (b) {
            final URI redirectedUri = UrlRedirectCache.getRedirectedUri(context, requestKey.uri);
            inputStream2 = inputStream;
            b3 = b2;
            if (redirectedUri != null) {
                inputStream2 = ImageResponseCache.getCachedImageStream(redirectedUri, context);
                b3 = (inputStream2 != null);
            }
        }
        if (!b3) {
            inputStream2 = ImageResponseCache.getCachedImageStream(requestKey.uri, context);
        }
        if (inputStream2 != null) {
            final Bitmap decodeStream = BitmapFactory.decodeStream(inputStream2);
            Utility.closeQuietly(inputStream2);
            issueResponse(requestKey, null, decodeStream, b3);
        }
        else {
            final DownloaderContext removePendingRequest = removePendingRequest(requestKey);
            if (removePendingRequest != null && !removePendingRequest.isCancelled) {
                enqueueDownload(removePendingRequest.request, requestKey);
            }
        }
    }
    
    private static DownloaderContext removePendingRequest(final RequestKey requestKey) {
        synchronized (ImageDownloader.pendingRequests) {
            return ImageDownloader.pendingRequests.remove(requestKey);
        }
    }
    
    private static class CacheReadWorkItem implements Runnable
    {
        private boolean allowCachedRedirects;
        private Context context;
        private RequestKey key;
        
        CacheReadWorkItem(final Context context, final RequestKey key, final boolean allowCachedRedirects) {
            this.context = context;
            this.key = key;
            this.allowCachedRedirects = allowCachedRedirects;
        }
        
        @Override
        public void run() {
            readFromCache(this.key, this.context, this.allowCachedRedirects);
        }
    }
    
    private static class DownloadImageWorkItem implements Runnable
    {
        private Context context;
        private RequestKey key;
        
        DownloadImageWorkItem(final Context context, final RequestKey key) {
            this.context = context;
            this.key = key;
        }
        
        @Override
        public void run() {
            download(this.key, this.context);
        }
    }
    
    private static class DownloaderContext
    {
        boolean isCancelled;
        ImageRequest request;
        WorkQueue.WorkItem workItem;
    }
    
    private static class RequestKey
    {
        private static final int HASH_MULTIPLIER = 37;
        private static final int HASH_SEED = 29;
        Object tag;
        URI uri;
        
        RequestKey(final URI uri, final Object tag) {
            this.uri = uri;
            this.tag = tag;
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = false;
            if (o != null) {
                b = b;
                if (o instanceof RequestKey) {
                    final RequestKey requestKey = (RequestKey)o;
                    if (requestKey.uri != this.uri || requestKey.tag != this.tag) {
                        return false;
                    }
                    b = true;
                }
            }
            return b;
        }
        
        @Override
        public int hashCode() {
            return (this.uri.hashCode() + 1073) * 37 + this.tag.hashCode();
        }
    }
}
