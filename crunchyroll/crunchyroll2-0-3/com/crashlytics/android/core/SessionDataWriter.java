// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

import android.os.Build$VERSION;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.common.ApiKey;
import java.util.Iterator;
import java.util.Map;
import io.fabric.sdk.android.services.common.IdManager;
import java.util.List;
import android.app.ActivityManager$RunningAppProcessInfo;
import android.content.Context;

class SessionDataWriter
{
    private static final ByteString SIGNAL_DEFAULT_BYTE_STRING;
    private final Context context;
    private StackTraceElement[] exceptionStack;
    private final int maxChainedExceptionsDepth;
    private final ByteString optionalBuildIdBytes;
    private final ByteString packageNameBytes;
    private ActivityManager$RunningAppProcessInfo runningAppProcessInfo;
    private List<StackTraceElement[]> stacks;
    private Thread[] threads;
    
    static {
        SIGNAL_DEFAULT_BYTE_STRING = ByteString.copyFromUtf8("0");
    }
    
    public SessionDataWriter(final Context context, final String s, final String s2) {
        this.maxChainedExceptionsDepth = 8;
        this.context = context;
        this.packageNameBytes = ByteString.copyFromUtf8(s2);
        ByteString copyFromUtf8;
        if (s == null) {
            copyFromUtf8 = null;
        }
        else {
            copyFromUtf8 = ByteString.copyFromUtf8(s.replace("-", ""));
        }
        this.optionalBuildIdBytes = copyFromUtf8;
    }
    
    private int getBinaryImageSize() {
        int n = 0 + CodedOutputStream.computeUInt64Size(1, 0L) + CodedOutputStream.computeUInt64Size(2, 0L) + CodedOutputStream.computeBytesSize(3, this.packageNameBytes);
        if (this.optionalBuildIdBytes != null) {
            n += CodedOutputStream.computeBytesSize(4, this.optionalBuildIdBytes);
        }
        return n;
    }
    
    private int getDeviceIdentifierSize(final IdManager.DeviceIdentifierType deviceIdentifierType, final String s) {
        return CodedOutputStream.computeEnumSize(1, deviceIdentifierType.protobufIndex) + CodedOutputStream.computeBytesSize(2, ByteString.copyFromUtf8(s));
    }
    
    private int getEventAppCustomAttributeSize(String s, final String s2) {
        final int computeBytesSize = CodedOutputStream.computeBytesSize(1, ByteString.copyFromUtf8(s));
        s = s2;
        if (s2 == null) {
            s = "";
        }
        return computeBytesSize + CodedOutputStream.computeBytesSize(2, ByteString.copyFromUtf8(s));
    }
    
    private int getEventAppExecutionExceptionSize(Throwable t, int eventAppExecutionExceptionSize) {
        final int n = 0 + CodedOutputStream.computeBytesSize(1, ByteString.copyFromUtf8(t.getClass().getName()));
        final String localizedMessage = t.getLocalizedMessage();
        int n2 = n;
        if (localizedMessage != null) {
            n2 = n + CodedOutputStream.computeBytesSize(3, ByteString.copyFromUtf8(localizedMessage));
        }
        final StackTraceElement[] stackTrace = t.getStackTrace();
        for (int length = stackTrace.length, i = 0; i < length; ++i) {
            final int frameSize = this.getFrameSize(stackTrace[i], true);
            n2 += CodedOutputStream.computeTagSize(4) + CodedOutputStream.computeRawVarint32Size(frameSize) + frameSize;
        }
        t = t.getCause();
        int n3 = n2;
        if (t != null) {
            if (eventAppExecutionExceptionSize >= this.maxChainedExceptionsDepth) {
                for (eventAppExecutionExceptionSize = 0; t != null; t = t.getCause(), ++eventAppExecutionExceptionSize) {}
                return n2 + CodedOutputStream.computeUInt32Size(7, eventAppExecutionExceptionSize);
            }
            eventAppExecutionExceptionSize = this.getEventAppExecutionExceptionSize(t, eventAppExecutionExceptionSize + 1);
            n3 = n2 + (CodedOutputStream.computeTagSize(6) + CodedOutputStream.computeRawVarint32Size(eventAppExecutionExceptionSize) + eventAppExecutionExceptionSize);
        }
        return n3;
    }
    
    private int getEventAppExecutionSignalSize() {
        return 0 + CodedOutputStream.computeBytesSize(1, SessionDataWriter.SIGNAL_DEFAULT_BYTE_STRING) + CodedOutputStream.computeBytesSize(2, SessionDataWriter.SIGNAL_DEFAULT_BYTE_STRING) + CodedOutputStream.computeUInt64Size(3, 0L);
    }
    
    private int getEventAppExecutionSize(final Thread thread, final Throwable t) {
        final int threadSize = this.getThreadSize(thread, this.exceptionStack, 4, true);
        int n = 0 + (CodedOutputStream.computeTagSize(1) + CodedOutputStream.computeRawVarint32Size(threadSize) + threadSize);
        for (int length = this.threads.length, i = 0; i < length; ++i) {
            final int threadSize2 = this.getThreadSize(this.threads[i], this.stacks.get(i), 0, false);
            n += CodedOutputStream.computeTagSize(1) + CodedOutputStream.computeRawVarint32Size(threadSize2) + threadSize2;
        }
        final int eventAppExecutionExceptionSize = this.getEventAppExecutionExceptionSize(t, 1);
        final int computeTagSize = CodedOutputStream.computeTagSize(2);
        final int computeRawVarint32Size = CodedOutputStream.computeRawVarint32Size(eventAppExecutionExceptionSize);
        final int eventAppExecutionSignalSize = this.getEventAppExecutionSignalSize();
        final int computeTagSize2 = CodedOutputStream.computeTagSize(3);
        final int computeRawVarint32Size2 = CodedOutputStream.computeRawVarint32Size(eventAppExecutionSignalSize);
        final int binaryImageSize = this.getBinaryImageSize();
        return n + (computeTagSize + computeRawVarint32Size + eventAppExecutionExceptionSize) + (computeTagSize2 + computeRawVarint32Size2 + eventAppExecutionSignalSize) + (CodedOutputStream.computeTagSize(3) + CodedOutputStream.computeRawVarint32Size(binaryImageSize) + binaryImageSize);
    }
    
    private int getEventAppSize(final Thread thread, final Throwable t, final int n, final Map<String, String> map) {
        final int eventAppExecutionSize = this.getEventAppExecutionSize(thread, t);
        int n3;
        int n2 = n3 = 0 + (CodedOutputStream.computeTagSize(1) + CodedOutputStream.computeRawVarint32Size(eventAppExecutionSize) + eventAppExecutionSize);
        if (map != null) {
            final Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
            while (true) {
                n3 = n2;
                if (!iterator.hasNext()) {
                    break;
                }
                final Map.Entry<String, String> entry = iterator.next();
                final int eventAppCustomAttributeSize = this.getEventAppCustomAttributeSize(entry.getKey(), entry.getValue());
                n2 += CodedOutputStream.computeTagSize(2) + CodedOutputStream.computeRawVarint32Size(eventAppCustomAttributeSize) + eventAppCustomAttributeSize;
            }
        }
        int n4 = n3;
        if (this.runningAppProcessInfo != null) {
            n4 = n3 + CodedOutputStream.computeBoolSize(3, this.runningAppProcessInfo.importance != 100);
        }
        return n4 + CodedOutputStream.computeUInt32Size(4, n);
    }
    
    private int getEventDeviceSize(final float n, final int n2, final boolean b, final int n3, final long n4, final long n5) {
        return 0 + CodedOutputStream.computeFloatSize(1, n) + CodedOutputStream.computeSInt32Size(2, n2) + CodedOutputStream.computeBoolSize(3, b) + CodedOutputStream.computeUInt32Size(4, n3) + CodedOutputStream.computeUInt64Size(5, n4) + CodedOutputStream.computeUInt64Size(6, n5);
    }
    
    private int getEventLogSize(final ByteString byteString) {
        return CodedOutputStream.computeBytesSize(1, byteString);
    }
    
    private int getFrameSize(final StackTraceElement stackTraceElement, final boolean b) {
        final int n = 2;
        int n2;
        if (stackTraceElement.isNativeMethod()) {
            n2 = 0 + CodedOutputStream.computeUInt64Size(1, Math.max(stackTraceElement.getLineNumber(), 0));
        }
        else {
            n2 = 0 + CodedOutputStream.computeUInt64Size(1, 0L);
        }
        int n4;
        final int n3 = n4 = n2 + CodedOutputStream.computeBytesSize(2, ByteString.copyFromUtf8(stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName()));
        if (stackTraceElement.getFileName() != null) {
            n4 = n3 + CodedOutputStream.computeBytesSize(3, ByteString.copyFromUtf8(stackTraceElement.getFileName()));
        }
        int n5 = n4;
        if (!stackTraceElement.isNativeMethod()) {
            n5 = n4;
            if (stackTraceElement.getLineNumber() > 0) {
                n5 = n4 + CodedOutputStream.computeUInt64Size(4, stackTraceElement.getLineNumber());
            }
        }
        int n6;
        if (b) {
            n6 = n;
        }
        else {
            n6 = 0;
        }
        return n5 + CodedOutputStream.computeUInt32Size(5, n6);
    }
    
    private int getSessionAppOrgSize() {
        return 0 + CodedOutputStream.computeBytesSize(1, ByteString.copyFromUtf8(new ApiKey().getValue(this.context)));
    }
    
    private int getSessionAppSize(final ByteString byteString, final ByteString byteString2, final ByteString byteString3, final ByteString byteString4, final int n) {
        final int computeBytesSize = CodedOutputStream.computeBytesSize(1, byteString);
        final int computeBytesSize2 = CodedOutputStream.computeBytesSize(2, byteString2);
        final int computeBytesSize3 = CodedOutputStream.computeBytesSize(3, byteString3);
        final int sessionAppOrgSize = this.getSessionAppOrgSize();
        return 0 + computeBytesSize + computeBytesSize2 + computeBytesSize3 + (CodedOutputStream.computeTagSize(5) + CodedOutputStream.computeRawVarint32Size(sessionAppOrgSize) + sessionAppOrgSize) + CodedOutputStream.computeBytesSize(6, byteString4) + CodedOutputStream.computeEnumSize(10, n);
    }
    
    private int getSessionDeviceSize(int n, final ByteString byteString, final ByteString byteString2, int deviceIdentifierSize, final long n2, final long n3, final boolean b, final Map<IdManager.DeviceIdentifierType, String> map, int computeBytesSize, final ByteString byteString3, final ByteString byteString4) {
        final int computeBytesSize2 = CodedOutputStream.computeBytesSize(1, byteString);
        final int computeEnumSize = CodedOutputStream.computeEnumSize(3, n);
        if (byteString2 == null) {
            n = 0;
        }
        else {
            n = CodedOutputStream.computeBytesSize(4, byteString2);
        }
        n = (deviceIdentifierSize = 0 + computeBytesSize2 + computeEnumSize + n + CodedOutputStream.computeUInt32Size(5, deviceIdentifierSize) + CodedOutputStream.computeUInt64Size(6, n2) + CodedOutputStream.computeUInt64Size(7, n3) + CodedOutputStream.computeBoolSize(10, b));
        if (map != null) {
            final Iterator<Map.Entry<IdManager.DeviceIdentifierType, String>> iterator = map.entrySet().iterator();
            while (true) {
                deviceIdentifierSize = n;
                if (!iterator.hasNext()) {
                    break;
                }
                final Map.Entry<IdManager.DeviceIdentifierType, String> entry = iterator.next();
                deviceIdentifierSize = this.getDeviceIdentifierSize(entry.getKey(), entry.getValue());
                n += CodedOutputStream.computeTagSize(11) + CodedOutputStream.computeRawVarint32Size(deviceIdentifierSize) + deviceIdentifierSize;
            }
        }
        final int computeUInt32Size = CodedOutputStream.computeUInt32Size(12, computeBytesSize);
        if (byteString3 == null) {
            n = 0;
        }
        else {
            n = CodedOutputStream.computeBytesSize(13, byteString3);
        }
        if (byteString4 == null) {
            computeBytesSize = 0;
        }
        else {
            computeBytesSize = CodedOutputStream.computeBytesSize(14, byteString4);
        }
        return deviceIdentifierSize + computeUInt32Size + n + computeBytesSize;
    }
    
    private int getSessionEventSize(final Thread thread, final Throwable t, final String s, final long n, final Map<String, String> map, final float n2, int n3, final boolean b, int n4, final long n5, final long n6, final ByteString byteString) {
        final int computeUInt64Size = CodedOutputStream.computeUInt64Size(1, n);
        final int computeBytesSize = CodedOutputStream.computeBytesSize(2, ByteString.copyFromUtf8(s));
        final int eventAppSize = this.getEventAppSize(thread, t, n4, map);
        final int computeTagSize = CodedOutputStream.computeTagSize(3);
        final int computeRawVarint32Size = CodedOutputStream.computeRawVarint32Size(eventAppSize);
        n3 = this.getEventDeviceSize(n2, n3, b, n4, n5, n6);
        n4 = (n3 = 0 + computeUInt64Size + computeBytesSize + (computeTagSize + computeRawVarint32Size + eventAppSize) + (CodedOutputStream.computeTagSize(5) + CodedOutputStream.computeRawVarint32Size(n3) + n3));
        if (byteString != null) {
            n3 = this.getEventLogSize(byteString);
            n3 = n4 + (CodedOutputStream.computeTagSize(6) + CodedOutputStream.computeRawVarint32Size(n3) + n3);
        }
        return n3;
    }
    
    private int getSessionOSSize(final ByteString byteString, final ByteString byteString2, final boolean b) {
        return 0 + CodedOutputStream.computeEnumSize(1, 3) + CodedOutputStream.computeBytesSize(2, byteString) + CodedOutputStream.computeBytesSize(3, byteString2) + CodedOutputStream.computeBoolSize(4, b);
    }
    
    private int getThreadSize(final Thread thread, final StackTraceElement[] array, int i, final boolean b) {
        int n = CodedOutputStream.computeBytesSize(1, ByteString.copyFromUtf8(thread.getName())) + CodedOutputStream.computeUInt32Size(2, i);
        int length;
        int frameSize;
        for (length = array.length, i = 0; i < length; ++i) {
            frameSize = this.getFrameSize(array[i], b);
            n += CodedOutputStream.computeTagSize(3) + CodedOutputStream.computeRawVarint32Size(frameSize) + frameSize;
        }
        return n;
    }
    
    private ByteString stringToByteString(final String s) {
        if (s == null) {
            return null;
        }
        return ByteString.copyFromUtf8(s);
    }
    
    private void writeFrame(final CodedOutputStream codedOutputStream, int n, final StackTraceElement stackTraceElement, final boolean b) throws Exception {
        final int n2 = 4;
        codedOutputStream.writeTag(n, 2);
        codedOutputStream.writeRawVarint32(this.getFrameSize(stackTraceElement, b));
        if (stackTraceElement.isNativeMethod()) {
            codedOutputStream.writeUInt64(1, Math.max(stackTraceElement.getLineNumber(), 0));
        }
        else {
            codedOutputStream.writeUInt64(1, 0L);
        }
        codedOutputStream.writeBytes(2, ByteString.copyFromUtf8(stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName()));
        if (stackTraceElement.getFileName() != null) {
            codedOutputStream.writeBytes(3, ByteString.copyFromUtf8(stackTraceElement.getFileName()));
        }
        if (!stackTraceElement.isNativeMethod() && stackTraceElement.getLineNumber() > 0) {
            codedOutputStream.writeUInt64(4, stackTraceElement.getLineNumber());
        }
        if (b) {
            n = n2;
        }
        else {
            n = 0;
        }
        codedOutputStream.writeUInt32(5, n);
    }
    
    private void writeSessionEventApp(final CodedOutputStream codedOutputStream, final Thread thread, final Throwable t, final int n, final Map<String, String> map) throws Exception {
        codedOutputStream.writeTag(3, 2);
        codedOutputStream.writeRawVarint32(this.getEventAppSize(thread, t, n, map));
        this.writeSessionEventAppExecution(codedOutputStream, thread, t);
        if (map != null && !map.isEmpty()) {
            this.writeSessionEventAppCustomAttributes(codedOutputStream, map);
        }
        if (this.runningAppProcessInfo != null) {
            codedOutputStream.writeBool(3, this.runningAppProcessInfo.importance != 100);
        }
        codedOutputStream.writeUInt32(4, n);
    }
    
    private void writeSessionEventAppCustomAttributes(final CodedOutputStream codedOutputStream, final Map<String, String> map) throws Exception {
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            codedOutputStream.writeTag(2, 2);
            codedOutputStream.writeRawVarint32(this.getEventAppCustomAttributeSize(entry.getKey(), entry.getValue()));
            codedOutputStream.writeBytes(1, ByteString.copyFromUtf8(entry.getKey()));
            String s;
            if ((s = entry.getValue()) == null) {
                s = "";
            }
            codedOutputStream.writeBytes(2, ByteString.copyFromUtf8(s));
        }
    }
    
    private void writeSessionEventAppExecution(final CodedOutputStream codedOutputStream, final Thread thread, final Throwable t) throws Exception {
        codedOutputStream.writeTag(1, 2);
        codedOutputStream.writeRawVarint32(this.getEventAppExecutionSize(thread, t));
        this.writeThread(codedOutputStream, thread, this.exceptionStack, 4, true);
        for (int length = this.threads.length, i = 0; i < length; ++i) {
            this.writeThread(codedOutputStream, this.threads[i], this.stacks.get(i), 0, false);
        }
        this.writeSessionEventAppExecutionException(codedOutputStream, t, 1, 2);
        codedOutputStream.writeTag(3, 2);
        codedOutputStream.writeRawVarint32(this.getEventAppExecutionSignalSize());
        codedOutputStream.writeBytes(1, SessionDataWriter.SIGNAL_DEFAULT_BYTE_STRING);
        codedOutputStream.writeBytes(2, SessionDataWriter.SIGNAL_DEFAULT_BYTE_STRING);
        codedOutputStream.writeUInt64(3, 0L);
        codedOutputStream.writeTag(4, 2);
        codedOutputStream.writeRawVarint32(this.getBinaryImageSize());
        codedOutputStream.writeUInt64(1, 0L);
        codedOutputStream.writeUInt64(2, 0L);
        codedOutputStream.writeBytes(3, this.packageNameBytes);
        if (this.optionalBuildIdBytes != null) {
            codedOutputStream.writeBytes(4, this.optionalBuildIdBytes);
        }
    }
    
    private void writeSessionEventAppExecutionException(final CodedOutputStream codedOutputStream, Throwable t, int n, int i) throws Exception {
        codedOutputStream.writeTag(i, 2);
        codedOutputStream.writeRawVarint32(this.getEventAppExecutionExceptionSize(t, 1));
        codedOutputStream.writeBytes(1, ByteString.copyFromUtf8(t.getClass().getName()));
        final String localizedMessage = t.getLocalizedMessage();
        if (localizedMessage != null) {
            codedOutputStream.writeBytes(3, ByteString.copyFromUtf8(localizedMessage));
        }
        final StackTraceElement[] stackTrace = t.getStackTrace();
        int length;
        for (length = stackTrace.length, i = 0; i < length; ++i) {
            this.writeFrame(codedOutputStream, 4, stackTrace[i], true);
        }
        t = t.getCause();
        if (t != null) {
            if (n >= this.maxChainedExceptionsDepth) {
                for (n = 0; t != null; t = t.getCause(), ++n) {}
                codedOutputStream.writeUInt32(7, n);
                return;
            }
            this.writeSessionEventAppExecutionException(codedOutputStream, t, n + 1, 6);
        }
    }
    
    private void writeSessionEventDevice(final CodedOutputStream codedOutputStream, final float n, final int n2, final boolean b, final int n3, final long n4, final long n5) throws Exception {
        codedOutputStream.writeTag(5, 2);
        codedOutputStream.writeRawVarint32(this.getEventDeviceSize(n, n2, b, n3, n4, n5));
        codedOutputStream.writeFloat(1, n);
        codedOutputStream.writeSInt32(2, n2);
        codedOutputStream.writeBool(3, b);
        codedOutputStream.writeUInt32(4, n3);
        codedOutputStream.writeUInt64(5, n4);
        codedOutputStream.writeUInt64(6, n5);
    }
    
    private void writeSessionEventLog(final CodedOutputStream codedOutputStream, final ByteString byteString) throws Exception {
        if (byteString != null) {
            codedOutputStream.writeTag(6, 2);
            codedOutputStream.writeRawVarint32(this.getEventLogSize(byteString));
            codedOutputStream.writeBytes(1, byteString);
        }
    }
    
    private void writeThread(final CodedOutputStream codedOutputStream, final Thread thread, final StackTraceElement[] array, int i, final boolean b) throws Exception {
        codedOutputStream.writeTag(1, 2);
        codedOutputStream.writeRawVarint32(this.getThreadSize(thread, array, i, b));
        codedOutputStream.writeBytes(1, ByteString.copyFromUtf8(thread.getName()));
        codedOutputStream.writeUInt32(2, i);
        int length;
        for (length = array.length, i = 0; i < length; ++i) {
            this.writeFrame(codedOutputStream, 3, array[i], b);
        }
    }
    
    public void writeBeginSession(final CodedOutputStream codedOutputStream, final String s, final String s2, final long n) throws Exception {
        codedOutputStream.writeBytes(1, ByteString.copyFromUtf8(s2));
        codedOutputStream.writeBytes(2, ByteString.copyFromUtf8(s));
        codedOutputStream.writeUInt64(3, n);
    }
    
    public void writeSessionApp(final CodedOutputStream codedOutputStream, final String s, final String s2, final String s3, final String s4, final int n) throws Exception {
        final ByteString copyFromUtf8 = ByteString.copyFromUtf8(s);
        final ByteString copyFromUtf9 = ByteString.copyFromUtf8(s2);
        final ByteString copyFromUtf10 = ByteString.copyFromUtf8(s3);
        final ByteString copyFromUtf11 = ByteString.copyFromUtf8(s4);
        codedOutputStream.writeTag(7, 2);
        codedOutputStream.writeRawVarint32(this.getSessionAppSize(copyFromUtf8, copyFromUtf9, copyFromUtf10, copyFromUtf11, n));
        codedOutputStream.writeBytes(1, copyFromUtf8);
        codedOutputStream.writeBytes(2, copyFromUtf9);
        codedOutputStream.writeBytes(3, copyFromUtf10);
        codedOutputStream.writeTag(5, 2);
        codedOutputStream.writeRawVarint32(this.getSessionAppOrgSize());
        codedOutputStream.writeString(1, new ApiKey().getValue(this.context));
        codedOutputStream.writeBytes(6, copyFromUtf11);
        codedOutputStream.writeEnum(10, n);
    }
    
    public void writeSessionDevice(final CodedOutputStream codedOutputStream, final String s, final int n, final String s2, final int n2, final long n3, final long n4, final boolean b, final Map<IdManager.DeviceIdentifierType, String> map, final int n5, final String s3, final String s4) throws Exception {
        final ByteString copyFromUtf8 = ByteString.copyFromUtf8(s);
        final ByteString stringToByteString = this.stringToByteString(s2);
        final ByteString stringToByteString2 = this.stringToByteString(s4);
        final ByteString stringToByteString3 = this.stringToByteString(s3);
        codedOutputStream.writeTag(9, 2);
        codedOutputStream.writeRawVarint32(this.getSessionDeviceSize(n, copyFromUtf8, stringToByteString, n2, n3, n4, b, map, n5, stringToByteString3, stringToByteString2));
        codedOutputStream.writeBytes(1, copyFromUtf8);
        codedOutputStream.writeEnum(3, n);
        codedOutputStream.writeBytes(4, stringToByteString);
        codedOutputStream.writeUInt32(5, n2);
        codedOutputStream.writeUInt64(6, n3);
        codedOutputStream.writeUInt64(7, n4);
        codedOutputStream.writeBool(10, b);
        for (final Map.Entry<IdManager.DeviceIdentifierType, String> entry : map.entrySet()) {
            codedOutputStream.writeTag(11, 2);
            codedOutputStream.writeRawVarint32(this.getDeviceIdentifierSize(entry.getKey(), entry.getValue()));
            codedOutputStream.writeEnum(1, entry.getKey().protobufIndex);
            codedOutputStream.writeBytes(2, ByteString.copyFromUtf8(entry.getValue()));
        }
        codedOutputStream.writeUInt32(12, n5);
        if (stringToByteString3 != null) {
            codedOutputStream.writeBytes(13, stringToByteString3);
        }
        if (stringToByteString2 != null) {
            codedOutputStream.writeBytes(14, stringToByteString2);
        }
    }
    
    public void writeSessionEvent(final CodedOutputStream codedOutputStream, final long n, final Thread thread, final Throwable t, final String s, final Thread[] threads, final float n2, final int n3, final boolean b, final int n4, final long n5, final long n6, final ActivityManager$RunningAppProcessInfo runningAppProcessInfo, final List<StackTraceElement[]> stacks, final StackTraceElement[] exceptionStack, final LogFileManager logFileManager, final Map<String, String> map) throws Exception {
        this.runningAppProcessInfo = runningAppProcessInfo;
        this.stacks = stacks;
        this.exceptionStack = exceptionStack;
        this.threads = threads;
        final ByteString byteStringForLog = logFileManager.getByteStringForLog();
        if (byteStringForLog == null) {
            Fabric.getLogger().d("CrashlyticsCore", "No log data to include with this event.");
        }
        logFileManager.clearLog();
        codedOutputStream.writeTag(10, 2);
        codedOutputStream.writeRawVarint32(this.getSessionEventSize(thread, t, s, n, map, n2, n3, b, n4, n5, n6, byteStringForLog));
        codedOutputStream.writeUInt64(1, n);
        codedOutputStream.writeBytes(2, ByteString.copyFromUtf8(s));
        this.writeSessionEventApp(codedOutputStream, thread, t, n4, map);
        this.writeSessionEventDevice(codedOutputStream, n2, n3, b, n4, n5, n6);
        this.writeSessionEventLog(codedOutputStream, byteStringForLog);
    }
    
    public void writeSessionOS(final CodedOutputStream codedOutputStream, final boolean b) throws Exception {
        final ByteString copyFromUtf8 = ByteString.copyFromUtf8(Build$VERSION.RELEASE);
        final ByteString copyFromUtf9 = ByteString.copyFromUtf8(Build$VERSION.CODENAME);
        codedOutputStream.writeTag(8, 2);
        codedOutputStream.writeRawVarint32(this.getSessionOSSize(copyFromUtf8, copyFromUtf9, b));
        codedOutputStream.writeEnum(1, 3);
        codedOutputStream.writeBytes(2, copyFromUtf8);
        codedOutputStream.writeBytes(3, copyFromUtf9);
        codedOutputStream.writeBool(4, b);
    }
    
    public void writeSessionUser(final CodedOutputStream codedOutputStream, final String s, final String s2, final String s3) throws Exception {
        String s4 = s;
        if (s == null) {
            s4 = "";
        }
        final ByteString copyFromUtf8 = ByteString.copyFromUtf8(s4);
        final ByteString stringToByteString = this.stringToByteString(s2);
        final ByteString stringToByteString2 = this.stringToByteString(s3);
        int n2;
        final int n = n2 = 0 + CodedOutputStream.computeBytesSize(1, copyFromUtf8);
        if (s2 != null) {
            n2 = n + CodedOutputStream.computeBytesSize(2, stringToByteString);
        }
        int n3 = n2;
        if (s3 != null) {
            n3 = n2 + CodedOutputStream.computeBytesSize(3, stringToByteString2);
        }
        codedOutputStream.writeTag(6, 2);
        codedOutputStream.writeRawVarint32(n3);
        codedOutputStream.writeBytes(1, copyFromUtf8);
        if (s2 != null) {
            codedOutputStream.writeBytes(2, stringToByteString);
        }
        if (s3 != null) {
            codedOutputStream.writeBytes(3, stringToByteString2);
        }
    }
}
