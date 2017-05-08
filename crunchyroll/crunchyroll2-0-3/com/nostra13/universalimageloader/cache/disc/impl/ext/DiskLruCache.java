// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.cache.disc.impl.ext;

import java.util.Arrays;
import java.io.FilterOutputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;
import java.io.EOFException;
import java.io.FileInputStream;
import java.util.Iterator;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.io.Writer;
import java.util.concurrent.ThreadPoolExecutor;
import java.io.File;
import java.util.concurrent.Callable;
import java.io.OutputStream;
import java.util.regex.Pattern;
import java.io.Closeable;

final class DiskLruCache implements Closeable
{
    static final long ANY_SEQUENCE_NUMBER = -1L;
    private static final String CLEAN = "CLEAN";
    private static final String DIRTY = "DIRTY";
    static final String JOURNAL_FILE = "journal";
    static final String JOURNAL_FILE_BACKUP = "journal.bkp";
    static final String JOURNAL_FILE_TEMP = "journal.tmp";
    static final Pattern LEGAL_KEY_PATTERN;
    static final String MAGIC = "libcore.io.DiskLruCache";
    private static final OutputStream NULL_OUTPUT_STREAM;
    private static final String READ = "READ";
    private static final String REMOVE = "REMOVE";
    static final String VERSION_1 = "1";
    private final int appVersion;
    private final Callable<Void> cleanupCallable;
    private final File directory;
    final ThreadPoolExecutor executorService;
    private int fileCount;
    private final File journalFile;
    private final File journalFileBackup;
    private final File journalFileTmp;
    private Writer journalWriter;
    private final LinkedHashMap<String, Entry> lruEntries;
    private int maxFileCount;
    private long maxSize;
    private long nextSequenceNumber;
    private int redundantOpCount;
    private long size;
    private final int valueCount;
    
    static {
        LEGAL_KEY_PATTERN = Pattern.compile("[a-z0-9_-]{1,64}");
        NULL_OUTPUT_STREAM = new OutputStream() {
            @Override
            public void write(final int n) throws IOException {
            }
        };
    }
    
    private DiskLruCache(final File directory, final int appVersion, final int valueCount, final long maxSize, final int maxFileCount) {
        this.size = 0L;
        this.fileCount = 0;
        this.lruEntries = new LinkedHashMap<String, Entry>(0, 0.75f, true);
        this.nextSequenceNumber = 0L;
        this.executorService = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        this.cleanupCallable = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                synchronized (DiskLruCache.this) {
                    if (DiskLruCache.this.journalWriter == null) {
                        return null;
                    }
                    DiskLruCache.this.trimToSize();
                    DiskLruCache.this.trimToFileCount();
                    if (DiskLruCache.this.journalRebuildRequired()) {
                        DiskLruCache.this.rebuildJournal();
                        DiskLruCache.this.redundantOpCount = 0;
                    }
                    return null;
                }
            }
        };
        this.directory = directory;
        this.appVersion = appVersion;
        this.journalFile = new File(directory, "journal");
        this.journalFileTmp = new File(directory, "journal.tmp");
        this.journalFileBackup = new File(directory, "journal.bkp");
        this.valueCount = valueCount;
        this.maxSize = maxSize;
        this.maxFileCount = maxFileCount;
    }
    
    private void checkNotClosed() {
        if (this.journalWriter == null) {
            throw new IllegalStateException("cache is closed");
        }
    }
    
    private void completeEdit(final Editor editor, final boolean b) throws IOException {
        final Entry access$1500;
        synchronized (this) {
            access$1500 = editor.entry;
            if (access$1500.currentEditor != editor) {
                throw new IllegalStateException();
            }
        }
    Label_0108:
        while (true) {
            if (b && !access$1500.readable) {
                for (int i = 0; i < this.valueCount; ++i) {
                    final Editor editor2;
                    if (!editor2.written[i]) {
                        editor2.abort();
                        throw new IllegalStateException("Newly created entry didn't create value for index " + i);
                    }
                    if (!access$1500.getDirtyFile(i).exists()) {
                        editor2.abort();
                        break Label_0108;
                    }
                }
            }
            Label_0118: {
                break Label_0118;
                return;
            }
            for (int j = 0; j < this.valueCount; ++j) {
                final File dirtyFile = access$1500.getDirtyFile(j);
                if (b) {
                    if (dirtyFile.exists()) {
                        final File cleanFile = access$1500.getCleanFile(j);
                        dirtyFile.renameTo(cleanFile);
                        final long n = access$1500.lengths[j];
                        final long length = cleanFile.length();
                        access$1500.lengths[j] = length;
                        this.size = this.size - n + length;
                        ++this.fileCount;
                    }
                }
                else {
                    deleteIfExists(dirtyFile);
                }
            }
            ++this.redundantOpCount;
            access$1500.currentEditor = null;
            if (access$1500.readable | b) {
                access$1500.readable = true;
                this.journalWriter.write("CLEAN " + access$1500.key + access$1500.getLengths() + '\n');
                if (b) {
                    final long nextSequenceNumber = this.nextSequenceNumber;
                    this.nextSequenceNumber = 1L + nextSequenceNumber;
                    access$1500.sequenceNumber = nextSequenceNumber;
                }
            }
            else {
                this.lruEntries.remove(access$1500.key);
                this.journalWriter.write("REMOVE " + access$1500.key + '\n');
            }
            this.journalWriter.flush();
            if (this.size > this.maxSize || this.fileCount > this.maxFileCount || this.journalRebuildRequired()) {
                this.executorService.submit(this.cleanupCallable);
            }
            continue Label_0108;
        }
    }
    // monitorexit(this)
    
    private static void deleteIfExists(final File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }
    
    private Editor edit(final String s, final long n) throws IOException {
        while (true) {
            Editor editor = null;
            while (true) {
                final Entry entry;
                synchronized (this) {
                    this.checkNotClosed();
                    this.validateKey(s);
                    entry = this.lruEntries.get(s);
                    Label_0067: {
                        if (n == -1L) {
                            break Label_0067;
                        }
                        Editor editor2 = editor;
                        if (entry != null) {
                            if (entry.sequenceNumber == n) {
                                break Label_0067;
                            }
                            editor2 = editor;
                        }
                        return editor2;
                    }
                    if (entry == null) {
                        final Entry entry2 = new Entry(s);
                        this.lruEntries.put(s, entry2);
                        editor = new Editor(entry2);
                        entry2.currentEditor = editor;
                        this.journalWriter.write("DIRTY " + s + '\n');
                        this.journalWriter.flush();
                        return editor;
                    }
                }
                final Editor access$800 = entry.currentEditor;
                final Entry entry2 = entry;
                if (access$800 != null) {
                    return editor;
                }
                continue;
            }
        }
    }
    
    private static String inputStreamToString(final InputStream inputStream) throws IOException {
        return Util.readFully(new InputStreamReader(inputStream, Util.UTF_8));
    }
    
    private boolean journalRebuildRequired() {
        return this.redundantOpCount >= 2000 && this.redundantOpCount >= this.lruEntries.size();
    }
    
    public static DiskLruCache open(final File file, final int n, final int n2, final long n3, final int n4) throws IOException {
        if (n3 <= 0L) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        if (n4 <= 0) {
            throw new IllegalArgumentException("maxFileCount <= 0");
        }
        if (n2 <= 0) {
            throw new IllegalArgumentException("valueCount <= 0");
        }
        Object o = new File(file, "journal.bkp");
        Label_0168: {
            if (((File)o).exists()) {
                final File file2 = new File(file, "journal");
                if (!file2.exists()) {
                    break Label_0168;
                }
                ((File)o).delete();
            }
            while (true) {
                o = new DiskLruCache(file, n, n2, n3, n4);
                if (!((DiskLruCache)o).journalFile.exists()) {
                    break Label_0168;
                }
                try {
                    ((DiskLruCache)o).readJournal();
                    ((DiskLruCache)o).processJournal();
                    ((DiskLruCache)o).journalWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(((DiskLruCache)o).journalFile, true), Util.US_ASCII));
                    return (DiskLruCache)o;
                    final File file2;
                    renameTo((File)o, file2, false);
                    continue;
                }
                catch (IOException ex) {
                    System.out.println("DiskLruCache " + file + " is corrupt: " + ex.getMessage() + ", removing");
                    ((DiskLruCache)o).delete();
                }
                break;
            }
        }
        file.mkdirs();
        final DiskLruCache diskLruCache = new DiskLruCache(file, n, n2, n3, n4);
        diskLruCache.rebuildJournal();
        return diskLruCache;
    }
    
    private void processJournal() throws IOException {
        deleteIfExists(this.journalFileTmp);
        final Iterator<Entry> iterator = this.lruEntries.values().iterator();
        while (iterator.hasNext()) {
            final Entry entry = iterator.next();
            if (entry.currentEditor == null) {
                for (int i = 0; i < this.valueCount; ++i) {
                    this.size += entry.lengths[i];
                    ++this.fileCount;
                }
            }
            else {
                entry.currentEditor = null;
                for (int j = 0; j < this.valueCount; ++j) {
                    deleteIfExists(entry.getCleanFile(j));
                    deleteIfExists(entry.getDirtyFile(j));
                }
                iterator.remove();
            }
        }
    }
    
    private void readJournal() throws IOException {
        final StrictLineReader strictLineReader = new StrictLineReader(new FileInputStream(this.journalFile), Util.US_ASCII);
        try {
            final String line = strictLineReader.readLine();
            final String line2 = strictLineReader.readLine();
            final String line3 = strictLineReader.readLine();
            final String line4 = strictLineReader.readLine();
            final String line5 = strictLineReader.readLine();
            if (!"libcore.io.DiskLruCache".equals(line) || !"1".equals(line2) || !Integer.toString(this.appVersion).equals(line3) || !Integer.toString(this.valueCount).equals(line4) || !"".equals(line5)) {
                throw new IOException("unexpected journal header: [" + line + ", " + line2 + ", " + line4 + ", " + line5 + "]");
            }
        }
        finally {
            Util.closeQuietly(strictLineReader);
        }
        int n = 0;
        try {
            while (true) {
                this.readJournalLine(strictLineReader.readLine());
                ++n;
            }
        }
        catch (EOFException ex) {
            this.redundantOpCount = n - this.lruEntries.size();
            Util.closeQuietly(strictLineReader);
        }
    }
    
    private void readJournalLine(final String s) throws IOException {
        final int index = s.indexOf(32);
        if (index == -1) {
            throw new IOException("unexpected journal line: " + s);
        }
        final int n = index + 1;
        final int index2 = s.indexOf(32, n);
        String s2 = null;
        Label_0112: {
            if (index2 != -1) {
                s2 = s.substring(n, index2);
                break Label_0112;
            }
            final String s3 = s2 = s.substring(n);
            if (index != "REMOVE".length()) {
                break Label_0112;
            }
            s2 = s3;
            if (!s.startsWith("REMOVE")) {
                break Label_0112;
            }
            this.lruEntries.remove(s3);
            return;
        }
        Entry entry;
        if ((entry = this.lruEntries.get(s2)) == null) {
            entry = new Entry(s2);
            this.lruEntries.put(s2, entry);
        }
        if (index2 != -1 && index == "CLEAN".length() && s.startsWith("CLEAN")) {
            final String[] split = s.substring(index2 + 1).split(" ");
            entry.readable = true;
            entry.currentEditor = null;
            entry.setLengths(split);
            return;
        }
        if (index2 == -1 && index == "DIRTY".length() && s.startsWith("DIRTY")) {
            entry.currentEditor = new Editor(entry);
            return;
        }
        if (index2 != -1 || index != "READ".length() || !s.startsWith("READ")) {
            throw new IOException("unexpected journal line: " + s);
        }
    }
    
    private void rebuildJournal() throws IOException {
        BufferedWriter bufferedWriter;
        while (true) {
            while (true) {
                Entry entry = null;
                synchronized (this) {
                    if (this.journalWriter != null) {
                        this.journalWriter.close();
                    }
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFileTmp), Util.US_ASCII));
                    try {
                        bufferedWriter.write("libcore.io.DiskLruCache");
                        bufferedWriter.write("\n");
                        bufferedWriter.write("1");
                        bufferedWriter.write("\n");
                        bufferedWriter.write(Integer.toString(this.appVersion));
                        bufferedWriter.write("\n");
                        bufferedWriter.write(Integer.toString(this.valueCount));
                        bufferedWriter.write("\n");
                        bufferedWriter.write("\n");
                        final Iterator<Entry> iterator = this.lruEntries.values().iterator();
                        while (iterator.hasNext()) {
                            entry = iterator.next();
                            if (entry.currentEditor == null) {
                                break;
                            }
                            bufferedWriter.write("DIRTY " + entry.key + '\n');
                        }
                    }
                    finally {
                        bufferedWriter.close();
                    }
                }
                bufferedWriter.write("CLEAN " + entry.key + entry.getLengths() + '\n');
                continue;
            }
        }
        bufferedWriter.close();
        if (this.journalFile.exists()) {
            renameTo(this.journalFile, this.journalFileBackup, true);
        }
        renameTo(this.journalFileTmp, this.journalFile, false);
        this.journalFileBackup.delete();
        this.journalWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFile, true), Util.US_ASCII));
    }
    // monitorexit(this)
    
    private static void renameTo(final File file, final File file2, final boolean b) throws IOException {
        if (b) {
            deleteIfExists(file2);
        }
        if (!file.renameTo(file2)) {
            throw new IOException();
        }
    }
    
    private void trimToFileCount() throws IOException {
        while (this.fileCount > this.maxFileCount) {
            this.remove(this.lruEntries.entrySet().iterator().next().getKey());
        }
    }
    
    private void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            this.remove(this.lruEntries.entrySet().iterator().next().getKey());
        }
    }
    
    private void validateKey(final String s) {
        if (!DiskLruCache.LEGAL_KEY_PATTERN.matcher(s).matches()) {
            throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,64}: \"" + s + "\"");
        }
    }
    
    @Override
    public void close() throws IOException {
        while (true) {
            synchronized (this) {
                if (this.journalWriter == null) {
                    return;
                }
                for (final Entry entry : new ArrayList<Entry>(this.lruEntries.values())) {
                    if (entry.currentEditor != null) {
                        entry.currentEditor.abort();
                    }
                }
            }
            this.trimToSize();
            this.trimToFileCount();
            this.journalWriter.close();
            this.journalWriter = null;
        }
    }
    
    public void delete() throws IOException {
        this.close();
        Util.deleteContents(this.directory);
    }
    
    public Editor edit(final String s) throws IOException {
        return this.edit(s, -1L);
    }
    
    public long fileCount() {
        synchronized (this) {
            return this.fileCount;
        }
    }
    
    public void flush() throws IOException {
        synchronized (this) {
            this.checkNotClosed();
            this.trimToSize();
            this.trimToFileCount();
            this.journalWriter.flush();
        }
    }
    
    public Snapshot get(final String s) throws IOException {
        final Snapshot snapshot = null;
        synchronized (this) {
            this.checkNotClosed();
            this.validateKey(s);
            final Entry entry = this.lruEntries.get(s);
            Snapshot snapshot2;
            if (entry == null) {
                snapshot2 = snapshot;
            }
            else {
                snapshot2 = snapshot;
                if (entry.readable) {
                    final File[] array = new File[this.valueCount];
                    final InputStream[] array2 = new InputStream[this.valueCount];
                    int i = 0;
                    try {
                        while (i < this.valueCount) {
                            final File cleanFile = entry.getCleanFile(i);
                            array[i] = cleanFile;
                            array2[i] = new FileInputStream(cleanFile);
                            ++i;
                        }
                    }
                    catch (FileNotFoundException ex) {
                        int n = 0;
                        while (true) {
                            snapshot2 = snapshot;
                            if (n >= this.valueCount) {
                                return snapshot2;
                            }
                            snapshot2 = snapshot;
                            if (array2[n] == null) {
                                return snapshot2;
                            }
                            Util.closeQuietly(array2[n]);
                            ++n;
                        }
                    }
                    ++this.redundantOpCount;
                    this.journalWriter.append((CharSequence)("READ " + s + '\n'));
                    if (this.journalRebuildRequired()) {
                        this.executorService.submit(this.cleanupCallable);
                    }
                    snapshot2 = new Snapshot(s, entry.sequenceNumber, array, array2, entry.lengths);
                }
            }
            return snapshot2;
        }
    }
    
    public File getDirectory() {
        return this.directory;
    }
    
    public int getMaxFileCount() {
        synchronized (this) {
            return this.maxFileCount;
        }
    }
    
    public long getMaxSize() {
        synchronized (this) {
            return this.maxSize;
        }
    }
    
    public boolean isClosed() {
        synchronized (this) {
            return this.journalWriter == null;
        }
    }
    
    public boolean remove(final String s) throws IOException {
        while (true) {
        Label_0156:
            while (true) {
                final Entry entry;
                int n;
                synchronized (this) {
                    this.checkNotClosed();
                    this.validateKey(s);
                    entry = this.lruEntries.get(s);
                    if (entry == null || entry.currentEditor != null) {
                        return false;
                    }
                    n = 0;
                    if (n >= this.valueCount) {
                        break Label_0156;
                    }
                    final File cleanFile = entry.getCleanFile(n);
                    if (cleanFile.exists() && !cleanFile.delete()) {
                        throw new IOException("failed to delete " + cleanFile);
                    }
                }
                this.size -= entry.lengths[n];
                --this.fileCount;
                entry.lengths[n] = 0L;
                ++n;
                continue;
            }
            ++this.redundantOpCount;
            final String s2;
            this.journalWriter.append((CharSequence)("REMOVE " + s2 + '\n'));
            this.lruEntries.remove(s2);
            if (this.journalRebuildRequired()) {
                this.executorService.submit(this.cleanupCallable);
            }
            return true;
        }
    }
    
    public void setMaxSize(final long maxSize) {
        synchronized (this) {
            this.maxSize = maxSize;
            this.executorService.submit(this.cleanupCallable);
        }
    }
    
    public long size() {
        synchronized (this) {
            return this.size;
        }
    }
    
    public final class Editor
    {
        private boolean committed;
        private final Entry entry;
        private boolean hasErrors;
        private final boolean[] written;
        
        private Editor(final Entry entry) {
            this.entry = entry;
            boolean[] written;
            if (entry.readable) {
                written = null;
            }
            else {
                written = new boolean[DiskLruCache.this.valueCount];
            }
            this.written = written;
        }
        
        public void abort() throws IOException {
            DiskLruCache.this.completeEdit(this, false);
        }
        
        public void abortUnlessCommitted() {
            if (this.committed) {
                return;
            }
            try {
                this.abort();
            }
            catch (IOException ex) {}
        }
        
        public void commit() throws IOException {
            if (this.hasErrors) {
                DiskLruCache.this.completeEdit(this, false);
                DiskLruCache.this.remove(this.entry.key);
            }
            else {
                DiskLruCache.this.completeEdit(this, true);
            }
            this.committed = true;
        }
        
        public String getString(final int n) throws IOException {
            final InputStream inputStream = this.newInputStream(n);
            if (inputStream != null) {
                return inputStreamToString(inputStream);
            }
            return null;
        }
        
        public InputStream newInputStream(final int n) throws IOException {
            synchronized (DiskLruCache.this) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
            }
            if (!this.entry.readable) {
                // monitorexit(diskLruCache)
                return null;
            }
            try {
                // monitorexit(diskLruCache)
                return new FileInputStream(this.entry.getCleanFile(n));
            }
            catch (FileNotFoundException ex) {
                // monitorexit(diskLruCache)
                return null;
            }
        }
        
        public OutputStream newOutputStream(final int n) throws IOException {
            synchronized (DiskLruCache.this) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
            }
            if (!this.entry.readable) {
                this.written[n] = true;
            }
            final File dirtyFile = this.entry.getDirtyFile(n);
            try {
                final FileOutputStream fileOutputStream = new FileOutputStream(dirtyFile);
                // monitorexit(diskLruCache)
                return new FaultHidingOutputStream((OutputStream)fileOutputStream);
            }
            catch (FileNotFoundException ex) {
                DiskLruCache.this.directory.mkdirs();
                try {
                    final FileOutputStream fileOutputStream = new FileOutputStream(dirtyFile);
                }
                catch (FileNotFoundException ex2) {
                    // monitorexit(diskLruCache)
                    return DiskLruCache.NULL_OUTPUT_STREAM;
                }
            }
        }
        
        public void set(final int n, final String s) throws IOException {
            final Closeable closeable = null;
            OutputStreamWriter outputStreamWriter;
            try {
                final OutputStreamWriter outputStreamWriter2;
                outputStreamWriter = (outputStreamWriter2 = new OutputStreamWriter(this.newOutputStream(n), Util.UTF_8));
                final String s2 = s;
                outputStreamWriter2.write(s2);
                final OutputStreamWriter outputStreamWriter3 = outputStreamWriter;
                Util.closeQuietly(outputStreamWriter3);
                return;
            }
            finally {
                final OutputStreamWriter outputStreamWriter4;
                outputStreamWriter = outputStreamWriter4;
                final Closeable closeable2 = closeable;
            }
            while (true) {
                try {
                    final OutputStreamWriter outputStreamWriter2 = outputStreamWriter;
                    final String s2 = s;
                    outputStreamWriter2.write(s2);
                    final OutputStreamWriter outputStreamWriter3 = outputStreamWriter;
                    Util.closeQuietly(outputStreamWriter3);
                    return;
                    final Closeable closeable2;
                    Util.closeQuietly(closeable2);
                    throw outputStreamWriter;
                }
                finally {
                    final Closeable closeable2 = outputStreamWriter;
                    final OutputStreamWriter outputStreamWriter5;
                    outputStreamWriter = outputStreamWriter5;
                    continue;
                }
                break;
            }
        }
        
        private class FaultHidingOutputStream extends FilterOutputStream
        {
            private FaultHidingOutputStream(final OutputStream outputStream) {
                super(outputStream);
            }
            
            @Override
            public void close() {
                try {
                    this.out.close();
                }
                catch (IOException ex) {
                    Editor.this.hasErrors = true;
                }
            }
            
            @Override
            public void flush() {
                try {
                    this.out.flush();
                }
                catch (IOException ex) {
                    Editor.this.hasErrors = true;
                }
            }
            
            @Override
            public void write(final int n) {
                try {
                    this.out.write(n);
                }
                catch (IOException ex) {
                    Editor.this.hasErrors = true;
                }
            }
            
            @Override
            public void write(final byte[] array, final int n, final int n2) {
                try {
                    this.out.write(array, n, n2);
                }
                catch (IOException ex) {
                    Editor.this.hasErrors = true;
                }
            }
        }
    }
    
    private final class Entry
    {
        private Editor currentEditor;
        private final String key;
        private final long[] lengths;
        private boolean readable;
        private long sequenceNumber;
        
        private Entry(final String key) {
            this.key = key;
            this.lengths = new long[DiskLruCache.this.valueCount];
        }
        
        private IOException invalidLengths(final String[] array) throws IOException {
            throw new IOException("unexpected journal line: " + Arrays.toString(array));
        }
        
        private void setLengths(final String[] array) throws IOException {
            if (array.length != DiskLruCache.this.valueCount) {
                throw this.invalidLengths(array);
            }
            int i = 0;
            try {
                while (i < array.length) {
                    this.lengths[i] = Long.parseLong(array[i]);
                    ++i;
                }
            }
            catch (NumberFormatException ex) {
                throw this.invalidLengths(array);
            }
        }
        
        public File getCleanFile(final int n) {
            return new File(DiskLruCache.this.directory, this.key + "." + n);
        }
        
        public File getDirtyFile(final int n) {
            return new File(DiskLruCache.this.directory, this.key + "." + n + ".tmp");
        }
        
        public String getLengths() throws IOException {
            final StringBuilder sb = new StringBuilder();
            final long[] lengths = this.lengths;
            for (int length = lengths.length, i = 0; i < length; ++i) {
                sb.append(' ').append(lengths[i]);
            }
            return sb.toString();
        }
    }
    
    public final class Snapshot implements Closeable
    {
        private File[] files;
        private final InputStream[] ins;
        private final String key;
        private final long[] lengths;
        private final long sequenceNumber;
        
        private Snapshot(final String key, final long sequenceNumber, final File[] files, final InputStream[] ins, final long[] lengths) {
            this.key = key;
            this.sequenceNumber = sequenceNumber;
            this.files = files;
            this.ins = ins;
            this.lengths = lengths;
        }
        
        @Override
        public void close() {
            final InputStream[] ins = this.ins;
            for (int length = ins.length, i = 0; i < length; ++i) {
                Util.closeQuietly(ins[i]);
            }
        }
        
        public Editor edit() throws IOException {
            return DiskLruCache.this.edit(this.key, this.sequenceNumber);
        }
        
        public File getFile(final int n) {
            return this.files[n];
        }
        
        public InputStream getInputStream(final int n) {
            return this.ins[n];
        }
        
        public long getLength(final int n) {
            return this.lengths[n];
        }
        
        public String getString(final int n) throws IOException {
            return inputStreamToString(this.getInputStream(n));
        }
    }
}
