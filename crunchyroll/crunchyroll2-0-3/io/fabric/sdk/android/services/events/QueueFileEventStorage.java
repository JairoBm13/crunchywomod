// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.events;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.io.OutputStream;
import java.io.Closeable;
import java.io.InputStream;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import io.fabric.sdk.android.services.common.QueueFile;
import android.content.Context;

public class QueueFileEventStorage implements EventsStorage
{
    private final Context context;
    private QueueFile queueFile;
    private File targetDirectory;
    private final String targetDirectoryName;
    private final File workingDirectory;
    private final File workingFile;
    
    public QueueFileEventStorage(final Context context, final File workingDirectory, final String s, final String targetDirectoryName) throws IOException {
        this.context = context;
        this.workingDirectory = workingDirectory;
        this.targetDirectoryName = targetDirectoryName;
        this.workingFile = new File(this.workingDirectory, s);
        this.queueFile = new QueueFile(this.workingFile);
        this.createTargetDirectory();
    }
    
    private void createTargetDirectory() {
        this.targetDirectory = new File(this.workingDirectory, this.targetDirectoryName);
        if (!this.targetDirectory.exists()) {
            this.targetDirectory.mkdirs();
        }
    }
    
    private void move(final File file, final File file2) throws IOException {
        final Closeable closeable = null;
        Closeable moveOutputStream = null;
        final Closeable closeable2 = null;
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
            moveOutputStream = closeable;
            final QueueFileEventStorage queueFileEventStorage = this;
            final File file3 = file2;
            final OutputStream moveOutputStream2 = queueFileEventStorage.getMoveOutputStream(file3);
            moveOutputStream = moveOutputStream2;
            final FileInputStream fileInputStream2 = fileInputStream;
            final OutputStream outputStream = moveOutputStream2;
            final int n = 1024;
            final byte[] array = new byte[n];
            CommonUtils.copyStream(fileInputStream2, outputStream, array);
            final FileInputStream fileInputStream3 = fileInputStream;
            final String s = "Failed to close file input stream";
            CommonUtils.closeOrLog(fileInputStream3, s);
            final OutputStream outputStream2 = moveOutputStream2;
            final String s2 = "Failed to close output stream";
            CommonUtils.closeOrLog(outputStream2, s2);
            final File file4 = file;
            file4.delete();
            return;
        }
        finally {
            final FileInputStream fileInputStream4;
            fileInputStream = fileInputStream4;
            final Closeable closeable3 = closeable2;
        }
        while (true) {
            try {
                final QueueFileEventStorage queueFileEventStorage = this;
                final File file3 = file2;
                final OutputStream moveOutputStream2 = (OutputStream)(moveOutputStream = queueFileEventStorage.getMoveOutputStream(file3));
                final FileInputStream fileInputStream2 = fileInputStream;
                final OutputStream outputStream = moveOutputStream2;
                final int n = 1024;
                final byte[] array = new byte[n];
                CommonUtils.copyStream(fileInputStream2, outputStream, array);
                final FileInputStream fileInputStream3 = fileInputStream;
                final String s = "Failed to close file input stream";
                CommonUtils.closeOrLog(fileInputStream3, s);
                final OutputStream outputStream2 = moveOutputStream2;
                final String s2 = "Failed to close output stream";
                CommonUtils.closeOrLog(outputStream2, s2);
                final File file4 = file;
                file4.delete();
                return;
                final Closeable closeable3;
                CommonUtils.closeOrLog(closeable3, "Failed to close file input stream");
                CommonUtils.closeOrLog(moveOutputStream, "Failed to close output stream");
                file.delete();
                throw fileInputStream;
            }
            finally {
                final Closeable closeable3 = fileInputStream;
                final FileInputStream fileInputStream5;
                fileInputStream = fileInputStream5;
                continue;
            }
            break;
        }
    }
    
    @Override
    public void add(final byte[] array) throws IOException {
        this.queueFile.add(array);
    }
    
    @Override
    public boolean canWorkingFileStore(final int n, final int n2) {
        return this.queueFile.hasSpaceFor(n, n2);
    }
    
    @Override
    public void deleteFilesInRollOverDirectory(final List<File> list) {
        for (final File file : list) {
            CommonUtils.logControlled(this.context, String.format("deleting sent analytics file %s", file.getName()));
            file.delete();
        }
    }
    
    @Override
    public void deleteWorkingFile() {
        while (true) {
            try {
                this.queueFile.close();
                this.workingFile.delete();
            }
            catch (IOException ex) {
                continue;
            }
            break;
        }
    }
    
    @Override
    public List<File> getAllFilesInRollOverDirectory() {
        return Arrays.asList(this.targetDirectory.listFiles());
    }
    
    @Override
    public List<File> getBatchOfFilesToSend(final int n) {
        final ArrayList<File> list = new ArrayList<File>();
        final File[] listFiles = this.targetDirectory.listFiles();
        for (int length = listFiles.length, i = 0; i < length; ++i) {
            list.add(listFiles[i]);
            if (list.size() >= n) {
                break;
            }
        }
        return list;
    }
    
    public OutputStream getMoveOutputStream(final File file) throws IOException {
        return new FileOutputStream(file);
    }
    
    @Override
    public int getWorkingFileUsedSizeInBytes() {
        return this.queueFile.usedBytes();
    }
    
    @Override
    public boolean isWorkingFileEmpty() {
        return this.queueFile.isEmpty();
    }
    
    @Override
    public void rollOver(final String s) throws IOException {
        this.queueFile.close();
        this.move(this.workingFile, new File(this.targetDirectory, s));
        this.queueFile = new QueueFile(this.workingFile);
    }
}
