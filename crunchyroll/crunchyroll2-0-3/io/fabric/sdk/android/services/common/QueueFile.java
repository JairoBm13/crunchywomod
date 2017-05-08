// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.common;

import java.util.logging.Level;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.io.IOException;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.logging.Logger;
import java.io.Closeable;

public class QueueFile implements Closeable
{
    private static final Logger LOGGER;
    private final byte[] buffer;
    private int elementCount;
    int fileLength;
    private Element first;
    private Element last;
    private final RandomAccessFile raf;
    
    static {
        LOGGER = Logger.getLogger(QueueFile.class.getName());
    }
    
    public QueueFile(final File file) throws IOException {
        this.buffer = new byte[16];
        if (!file.exists()) {
            initialize(file);
        }
        this.raf = open(file);
        this.readHeader();
    }
    
    private void expandIfNecessary(int n) throws IOException {
        final int n2 = n + 4;
        n = this.remainingBytes();
        if (n >= n2) {
            return;
        }
        int fileLength = this.fileLength;
        int n3;
        int n4;
        do {
            n3 = n + fileLength;
            n4 = (fileLength <<= 1);
        } while ((n = n3) < n2);
        this.setLength(n4);
        n = this.wrapPosition(this.last.position + 4 + this.last.length);
        if (n < this.first.position) {
            final FileChannel channel = this.raf.getChannel();
            channel.position(this.fileLength);
            n -= 4;
            if (channel.transferTo(16L, n, channel) != n) {
                throw new AssertionError((Object)"Copied insufficient number of bytes!");
            }
        }
        if (this.last.position < this.first.position) {
            n = this.fileLength + this.last.position - 16;
            this.writeHeader(n4, this.elementCount, this.first.position, n);
            this.last = new Element(n, this.last.length);
        }
        else {
            this.writeHeader(n4, this.elementCount, this.first.position, this.last.position);
        }
        this.fileLength = n4;
    }
    
    private static void initialize(final File file) throws IOException {
        final File file2 = new File(file.getPath() + ".tmp");
        final RandomAccessFile open = open(file2);
        try {
            open.setLength(4096L);
            open.seek(0L);
            final byte[] array = new byte[16];
            writeInts(array, 4096, 0, 0, 0);
            open.write(array);
            open.close();
            if (!file2.renameTo(file)) {
                throw new IOException("Rename failed!");
            }
        }
        finally {
            open.close();
        }
    }
    
    private static <T> T nonNull(final T t, final String s) {
        if (t == null) {
            throw new NullPointerException(s);
        }
        return t;
    }
    
    private static RandomAccessFile open(final File file) throws FileNotFoundException {
        return new RandomAccessFile(file, "rwd");
    }
    
    private Element readElement(final int n) throws IOException {
        if (n == 0) {
            return Element.NULL;
        }
        this.raf.seek(n);
        return new Element(n, this.raf.readInt());
    }
    
    private void readHeader() throws IOException {
        this.raf.seek(0L);
        this.raf.readFully(this.buffer);
        this.fileLength = readInt(this.buffer, 0);
        if (this.fileLength > this.raf.length()) {
            throw new IOException("File is truncated. Expected length: " + this.fileLength + ", Actual length: " + this.raf.length());
        }
        this.elementCount = readInt(this.buffer, 4);
        final int int1 = readInt(this.buffer, 8);
        final int int2 = readInt(this.buffer, 12);
        this.first = this.readElement(int1);
        this.last = this.readElement(int2);
    }
    
    private static int readInt(final byte[] array, final int n) {
        return ((array[n] & 0xFF) << 24) + ((array[n + 1] & 0xFF) << 16) + ((array[n + 2] & 0xFF) << 8) + (array[n + 3] & 0xFF);
    }
    
    private int remainingBytes() {
        return this.fileLength - this.usedBytes();
    }
    
    private void ringRead(int wrapPosition, final byte[] array, final int n, final int n2) throws IOException {
        wrapPosition = this.wrapPosition(wrapPosition);
        if (wrapPosition + n2 <= this.fileLength) {
            this.raf.seek(wrapPosition);
            this.raf.readFully(array, n, n2);
            return;
        }
        final int n3 = this.fileLength - wrapPosition;
        this.raf.seek(wrapPosition);
        this.raf.readFully(array, n, n3);
        this.raf.seek(16L);
        this.raf.readFully(array, n + n3, n2 - n3);
    }
    
    private void ringWrite(int wrapPosition, final byte[] array, final int n, final int n2) throws IOException {
        wrapPosition = this.wrapPosition(wrapPosition);
        if (wrapPosition + n2 <= this.fileLength) {
            this.raf.seek(wrapPosition);
            this.raf.write(array, n, n2);
            return;
        }
        final int n3 = this.fileLength - wrapPosition;
        this.raf.seek(wrapPosition);
        this.raf.write(array, n, n3);
        this.raf.seek(16L);
        this.raf.write(array, n + n3, n2 - n3);
    }
    
    private void setLength(final int n) throws IOException {
        this.raf.setLength(n);
        this.raf.getChannel().force(true);
    }
    
    private int wrapPosition(final int n) {
        if (n < this.fileLength) {
            return n;
        }
        return n + 16 - this.fileLength;
    }
    
    private void writeHeader(final int n, final int n2, final int n3, final int n4) throws IOException {
        writeInts(this.buffer, n, n2, n3, n4);
        this.raf.seek(0L);
        this.raf.write(this.buffer);
    }
    
    private static void writeInt(final byte[] array, final int n, final int n2) {
        array[n] = (byte)(n2 >> 24);
        array[n + 1] = (byte)(n2 >> 16);
        array[n + 2] = (byte)(n2 >> 8);
        array[n + 3] = (byte)n2;
    }
    
    private static void writeInts(final byte[] array, final int... array2) {
        int n = 0;
        for (int length = array2.length, i = 0; i < length; ++i) {
            writeInt(array, n, array2[i]);
            n += 4;
        }
    }
    
    public void add(final byte[] array) throws IOException {
        this.add(array, 0, array.length);
    }
    
    public void add(final byte[] array, int n, final int n2) throws IOException {
        synchronized (this) {
            nonNull(array, "buffer");
            if ((n | n2) < 0 || n2 > array.length - n) {
                throw new IndexOutOfBoundsException();
            }
        }
        this.expandIfNecessary(n2);
        final boolean empty = this.isEmpty();
        int wrapPosition;
        if (empty) {
            wrapPosition = 16;
        }
        else {
            wrapPosition = this.wrapPosition(this.last.position + 4 + this.last.length);
        }
        final Element last = new Element(wrapPosition, n2);
        writeInt(this.buffer, 0, n2);
        this.ringWrite(last.position, this.buffer, 0, 4);
        final byte[] array2;
        this.ringWrite(last.position + 4, array2, n, n2);
        if (empty) {
            n = last.position;
        }
        else {
            n = this.first.position;
        }
        this.writeHeader(this.fileLength, this.elementCount + 1, n, last.position);
        this.last = last;
        ++this.elementCount;
        if (empty) {
            this.first = this.last;
        }
    }
    // monitorexit(this)
    
    @Override
    public void close() throws IOException {
        synchronized (this) {
            this.raf.close();
        }
    }
    
    public void forEach(final ElementReader elementReader) throws IOException {
        synchronized (this) {
            int n = this.first.position;
            for (int i = 0; i < this.elementCount; ++i) {
                final Element element = this.readElement(n);
                elementReader.read(new ElementInputStream(element), element.length);
                n = this.wrapPosition(element.position + 4 + element.length);
            }
        }
    }
    
    public boolean hasSpaceFor(final int n, final int n2) {
        return this.usedBytes() + 4 + n <= n2;
    }
    
    public boolean isEmpty() {
        synchronized (this) {
            return this.elementCount == 0;
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName()).append('[');
        sb.append("fileLength=").append(this.fileLength);
        sb.append(", size=").append(this.elementCount);
        sb.append(", first=").append(this.first);
        sb.append(", last=").append(this.last);
        sb.append(", element lengths=[");
        while (true) {
            try {
                this.forEach((ElementReader)new ElementReader() {
                    boolean first = true;
                    
                    @Override
                    public void read(final InputStream inputStream, final int n) throws IOException {
                        if (this.first) {
                            this.first = false;
                        }
                        else {
                            sb.append(", ");
                        }
                        sb.append(n);
                    }
                });
                sb.append("]]");
                return sb.toString();
            }
            catch (IOException ex) {
                QueueFile.LOGGER.log(Level.WARNING, "read error", ex);
                continue;
            }
            break;
        }
    }
    
    public int usedBytes() {
        if (this.elementCount == 0) {
            return 16;
        }
        if (this.last.position >= this.first.position) {
            return this.last.position - this.first.position + 4 + this.last.length + 16;
        }
        return this.last.position + 4 + this.last.length + this.fileLength - this.first.position;
    }
    
    static class Element
    {
        static final Element NULL;
        final int length;
        final int position;
        
        static {
            NULL = new Element(0, 0);
        }
        
        Element(final int position, final int length) {
            this.position = position;
            this.length = length;
        }
        
        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "[" + "position = " + this.position + ", length = " + this.length + "]";
        }
    }
    
    private final class ElementInputStream extends InputStream
    {
        private int position;
        private int remaining;
        
        private ElementInputStream(final Element element) {
            this.position = QueueFile.this.wrapPosition(element.position + 4);
            this.remaining = element.length;
        }
        
        @Override
        public int read() throws IOException {
            if (this.remaining == 0) {
                return -1;
            }
            QueueFile.this.raf.seek(this.position);
            final int read = QueueFile.this.raf.read();
            this.position = QueueFile.this.wrapPosition(this.position + 1);
            --this.remaining;
            return read;
        }
        
        @Override
        public int read(final byte[] array, final int n, final int n2) throws IOException {
            nonNull(array, "buffer");
            if ((n | n2) < 0 || n2 > array.length - n) {
                throw new ArrayIndexOutOfBoundsException();
            }
            if (this.remaining > 0) {
                int remaining;
                if ((remaining = n2) > this.remaining) {
                    remaining = this.remaining;
                }
                QueueFile.this.ringRead(this.position, array, n, remaining);
                this.position = QueueFile.this.wrapPosition(this.position + remaining);
                this.remaining -= remaining;
                return remaining;
            }
            return -1;
        }
    }
    
    public interface ElementReader
    {
        void read(final InputStream p0, final int p1) throws IOException;
    }
}
