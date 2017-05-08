// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.download;

import java.net.URLConnection;
import java.io.Closeable;
import com.nostra13.universalimageloader.utils.IoUtils;
import com.nostra13.universalimageloader.core.assist.ContentLengthInputStream;
import java.io.File;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import android.graphics.BitmapFactory$Options;
import android.provider.MediaStore$Video$Thumbnails;
import android.content.ContentResolver;
import android.provider.ContactsContract$Contacts;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
import android.webkit.MimeTypeMap;
import android.net.Uri;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import android.graphics.Bitmap$CompressFormat;
import java.io.ByteArrayOutputStream;
import android.media.ThumbnailUtils;
import android.os.Build$VERSION;
import java.io.InputStream;
import android.content.Context;

public class BaseImageDownloader implements ImageDownloader
{
    protected static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    protected static final int BUFFER_SIZE = 32768;
    protected static final String CONTENT_CONTACTS_URI_PREFIX = "content://com.android.contacts/";
    public static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 5000;
    public static final int DEFAULT_HTTP_READ_TIMEOUT = 20000;
    private static final String ERROR_UNSUPPORTED_SCHEME = "UIL doesn't support scheme(protocol) by default [%s]. You should implement this support yourself (BaseImageDownloader.getStreamFromOtherSource(...))";
    protected static final int MAX_REDIRECT_COUNT = 5;
    protected final int connectTimeout;
    protected final Context context;
    protected final int readTimeout;
    
    public BaseImageDownloader(final Context context) {
        this(context, 5000, 20000);
    }
    
    public BaseImageDownloader(final Context context, final int connectTimeout, final int readTimeout) {
        this.context = context.getApplicationContext();
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }
    
    @TargetApi(8)
    private InputStream getVideoThumbnailStream(final String s) {
        if (Build$VERSION.SDK_INT >= 8) {
            final Bitmap videoThumbnail = ThumbnailUtils.createVideoThumbnail(s, 2);
            if (videoThumbnail != null) {
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                videoThumbnail.compress(Bitmap$CompressFormat.PNG, 0, (OutputStream)byteArrayOutputStream);
                return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            }
        }
        return null;
    }
    
    private boolean isVideoContentUri(final Uri uri) {
        final String type = this.context.getContentResolver().getType(uri);
        return type != null && type.startsWith("video/");
    }
    
    private boolean isVideoFileUri(String s) {
        s = MimeTypeMap.getFileExtensionFromUrl(s);
        s = MimeTypeMap.getSingleton().getMimeTypeFromExtension(s);
        return s != null && s.startsWith("video/");
    }
    
    protected HttpURLConnection createConnection(final String s, final Object o) throws IOException {
        final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(Uri.encode(s, "@#&=*+-_.,:!?()/~'%")).openConnection();
        httpURLConnection.setConnectTimeout(this.connectTimeout);
        httpURLConnection.setReadTimeout(this.readTimeout);
        return httpURLConnection;
    }
    
    @TargetApi(14)
    protected InputStream getContactPhotoStream(final Uri uri) {
        final ContentResolver contentResolver = this.context.getContentResolver();
        if (Build$VERSION.SDK_INT >= 14) {
            return ContactsContract$Contacts.openContactPhotoInputStream(contentResolver, uri, true);
        }
        return ContactsContract$Contacts.openContactPhotoInputStream(contentResolver, uri);
    }
    
    @Override
    public InputStream getStream(final String s, final Object o) throws IOException {
        switch (Scheme.ofUri(s)) {
            default: {
                return this.getStreamFromOtherSource(s, o);
            }
            case HTTP:
            case HTTPS: {
                return this.getStreamFromNetwork(s, o);
            }
            case FILE: {
                return this.getStreamFromFile(s, o);
            }
            case CONTENT: {
                return this.getStreamFromContent(s, o);
            }
            case ASSETS: {
                return this.getStreamFromAssets(s, o);
            }
            case DRAWABLE: {
                return this.getStreamFromDrawable(s, o);
            }
        }
    }
    
    protected InputStream getStreamFromAssets(String crop, final Object o) throws IOException {
        crop = Scheme.ASSETS.crop(crop);
        return this.context.getAssets().open(crop);
    }
    
    protected InputStream getStreamFromContent(final String s, final Object o) throws FileNotFoundException {
        final ContentResolver contentResolver = this.context.getContentResolver();
        final Uri parse = Uri.parse(s);
        if (this.isVideoContentUri(parse)) {
            final Bitmap thumbnail = MediaStore$Video$Thumbnails.getThumbnail(contentResolver, (long)Long.valueOf(parse.getLastPathSegment()), 1, (BitmapFactory$Options)null);
            if (thumbnail != null) {
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap$CompressFormat.PNG, 0, (OutputStream)byteArrayOutputStream);
                return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            }
        }
        else if (s.startsWith("content://com.android.contacts/")) {
            return this.getContactPhotoStream(parse);
        }
        return contentResolver.openInputStream(parse);
    }
    
    protected InputStream getStreamFromDrawable(final String s, final Object o) {
        return this.context.getResources().openRawResource(Integer.parseInt(Scheme.DRAWABLE.crop(s)));
    }
    
    protected InputStream getStreamFromFile(final String s, final Object o) throws IOException {
        final String crop = Scheme.FILE.crop(s);
        if (this.isVideoFileUri(s)) {
            return this.getVideoThumbnailStream(crop);
        }
        return new ContentLengthInputStream(new BufferedInputStream(new FileInputStream(crop), 32768), (int)new File(crop).length());
    }
    
    protected InputStream getStreamFromNetwork(String o, final Object o2) throws IOException {
        o = this.createConnection((String)o, o2);
        for (int n = 0; ((HttpURLConnection)o).getResponseCode() / 100 == 3 && n < 5; o = this.createConnection(((URLConnection)o).getHeaderField("Location"), o2), ++n) {}
        InputStream inputStream;
        try {
            inputStream = ((URLConnection)o).getInputStream();
            if (!this.shouldBeProcessed((HttpURLConnection)o)) {
                IoUtils.closeSilently(inputStream);
                throw new IOException("Image request failed with response code " + ((HttpURLConnection)o).getResponseCode());
            }
        }
        catch (IOException ex) {
            IoUtils.readAndCloseStream(((HttpURLConnection)o).getErrorStream());
            throw ex;
        }
        return new ContentLengthInputStream(new BufferedInputStream(inputStream, 32768), ((URLConnection)o).getContentLength());
    }
    
    protected InputStream getStreamFromOtherSource(final String s, final Object o) throws IOException {
        throw new UnsupportedOperationException(String.format("UIL doesn't support scheme(protocol) by default [%s]. You should implement this support yourself (BaseImageDownloader.getStreamFromOtherSource(...))", s));
    }
    
    protected boolean shouldBeProcessed(final HttpURLConnection httpURLConnection) throws IOException {
        return httpURLConnection.getResponseCode() == 200;
    }
}
