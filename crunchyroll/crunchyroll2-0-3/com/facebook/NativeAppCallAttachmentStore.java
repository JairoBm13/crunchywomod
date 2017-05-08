// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook;

import java.util.List;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.io.OutputStream;
import android.graphics.Bitmap$CompressFormat;
import android.graphics.Bitmap;
import java.io.Closeable;
import com.facebook.internal.Utility;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collection;
import com.facebook.internal.Validate;
import java.util.Iterator;
import java.io.IOException;
import android.util.Log;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import android.content.Context;
import java.io.File;

public final class NativeAppCallAttachmentStore implements AttachmentDataSource
{
    static final String ATTACHMENTS_DIR_NAME = "com.facebook.NativeAppCallAttachmentStore.files";
    private static final String TAG;
    private static File attachmentsDirectory;
    
    static {
        TAG = NativeAppCallAttachmentStore.class.getName();
    }
    
    private <T> void addAttachments(Context iterator, final UUID ex, final Map<String, T> map, final ProcessAttachment<T> processAttachment) {
        if (map.size() != 0) {
            if (NativeAppCallAttachmentStore.attachmentsDirectory == null) {
                this.cleanupAllAttachments(iterator);
            }
            this.ensureAttachmentsDirectoryExists(iterator);
            iterator = (Context)new ArrayList();
            try {
                for (final Map.Entry<String, T> entry : map.entrySet()) {
                    final String s = entry.getKey();
                    final T value = entry.getValue();
                    final File attachmentFile = this.getAttachmentFile((UUID)ex, s, true);
                    ((List<File>)iterator).add(attachmentFile);
                    processAttachment.processAttachment(value, attachmentFile);
                }
            }
            catch (IOException ex) {
                Log.e(NativeAppCallAttachmentStore.TAG, "Got unexpected exception:" + ex);
                iterator = (Context)((List<Object>)iterator).iterator();
                while (((Iterator)iterator).hasNext()) {
                    final File file = ((Iterator<File>)iterator).next();
                    try {
                        file.delete();
                    }
                    catch (Exception ex2) {}
                }
                throw new FacebookException(ex);
            }
        }
    }
    
    static File getAttachmentsDirectory(final Context context) {
        synchronized (NativeAppCallAttachmentStore.class) {
            if (NativeAppCallAttachmentStore.attachmentsDirectory == null) {
                NativeAppCallAttachmentStore.attachmentsDirectory = new File(context.getCacheDir(), "com.facebook.NativeAppCallAttachmentStore.files");
            }
            return NativeAppCallAttachmentStore.attachmentsDirectory;
        }
    }
    
    public void addAttachmentFilesForCall(final Context context, final UUID uuid, final Map<String, File> map) {
        Validate.notNull(context, "context");
        Validate.notNull(uuid, "callId");
        Validate.containsNoNulls((Collection<File>)map.values(), "mediaAttachmentFiles");
        Validate.containsNoNullOrEmpty(map.keySet(), "mediaAttachmentFiles");
        this.addAttachments(context, uuid, (Map<String, T>)map, (ProcessAttachment<T>)new ProcessAttachment<File>() {
            public void processAttachment(File file, File file2) throws IOException {
                final FileOutputStream fileOutputStream = new FileOutputStream(file2);
                file2 = null;
                while (true) {
                    try {
                        file = (File)new FileInputStream(file);
                        Label_0060: {
                            try {
                                final byte[] array = new byte[1024];
                                while (true) {
                                    final int read = ((FileInputStream)file).read(array);
                                    if (read <= 0) {
                                        break Label_0060;
                                    }
                                    fileOutputStream.write(array, 0, read);
                                }
                            }
                            finally {}
                            Utility.closeQuietly(fileOutputStream);
                            Utility.closeQuietly((Closeable)file);
                            throw file2;
                        }
                        Utility.closeQuietly(fileOutputStream);
                        Utility.closeQuietly((Closeable)file);
                    }
                    finally {
                        file = file2;
                        final File file3;
                        file2 = file3;
                        continue;
                    }
                    break;
                }
            }
        });
    }
    
    public void addAttachmentsForCall(final Context context, final UUID uuid, final Map<String, Bitmap> map) {
        Validate.notNull(context, "context");
        Validate.notNull(uuid, "callId");
        Validate.containsNoNulls((Collection<Bitmap>)map.values(), "imageAttachments");
        Validate.containsNoNullOrEmpty(map.keySet(), "imageAttachments");
        this.addAttachments(context, uuid, (Map<String, T>)map, (ProcessAttachment<T>)new ProcessAttachment<Bitmap>() {
            public void processAttachment(final Bitmap bitmap, File file) throws IOException {
                file = (File)new FileOutputStream(file);
                try {
                    bitmap.compress(Bitmap$CompressFormat.JPEG, 100, (OutputStream)file);
                }
                finally {
                    Utility.closeQuietly((Closeable)file);
                }
            }
        });
    }
    
    void cleanupAllAttachments(final Context context) {
        Utility.deleteDirectory(getAttachmentsDirectory(context));
    }
    
    public void cleanupAttachmentsForCall(final Context context, final UUID uuid) {
        Utility.deleteDirectory(this.getAttachmentsDirectoryForCall(uuid, false));
    }
    
    File ensureAttachmentsDirectoryExists(final Context context) {
        final File attachmentsDirectory = getAttachmentsDirectory(context);
        attachmentsDirectory.mkdirs();
        return attachmentsDirectory;
    }
    
    File getAttachmentFile(final UUID uuid, final String s, final boolean b) throws IOException {
        final File attachmentsDirectoryForCall = this.getAttachmentsDirectoryForCall(uuid, b);
        if (attachmentsDirectoryForCall == null) {
            return null;
        }
        try {
            return new File(attachmentsDirectoryForCall, URLEncoder.encode(s, "UTF-8"));
        }
        catch (UnsupportedEncodingException ex) {
            return null;
        }
    }
    
    File getAttachmentsDirectoryForCall(final UUID uuid, final boolean b) {
        File file;
        if (NativeAppCallAttachmentStore.attachmentsDirectory == null) {
            file = null;
        }
        else {
            final File file2 = file = new File(NativeAppCallAttachmentStore.attachmentsDirectory, uuid.toString());
            if (b) {
                file = file2;
                if (!file2.exists()) {
                    file2.mkdirs();
                    return file2;
                }
            }
        }
        return file;
    }
    
    @Override
    public File openAttachment(final UUID uuid, final String s) throws FileNotFoundException {
        if (Utility.isNullOrEmpty(s) || uuid == null) {
            throw new FileNotFoundException();
        }
        try {
            return this.getAttachmentFile(uuid, s, false);
        }
        catch (IOException ex) {
            throw new FileNotFoundException();
        }
    }
    
    interface ProcessAttachment<T>
    {
        void processAttachment(final T p0, final File p1) throws IOException;
    }
}
