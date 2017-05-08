// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

import java.util.TreeMap;
import com.crashlytics.android.core.internal.models.ThreadData;
import java.io.IOException;
import io.fabric.sdk.android.Fabric;
import java.util.Map;
import com.crashlytics.android.core.internal.models.SessionEventData;
import com.crashlytics.android.core.internal.models.DeviceData;
import com.crashlytics.android.core.internal.models.CustomAttributeData;
import com.crashlytics.android.core.internal.models.BinaryImageData;
import com.crashlytics.android.core.internal.models.SignalData;

class NativeCrashWriter
{
    private static final SignalData DEFAULT_SIGNAL;
    private static final BinaryImageMessage[] EMPTY_BINARY_IMAGE_MESSAGES;
    private static final ProtobufMessage[] EMPTY_CHILDREN;
    private static final CustomAttributeMessage[] EMPTY_CUSTOM_ATTRIBUTE_MESSAGES;
    private static final FrameMessage[] EMPTY_FRAME_MESSAGES;
    private static final ThreadMessage[] EMPTY_THREAD_MESSAGES;
    
    static {
        DEFAULT_SIGNAL = new SignalData("", "", 0L);
        EMPTY_CHILDREN = new ProtobufMessage[0];
        EMPTY_THREAD_MESSAGES = new ThreadMessage[0];
        EMPTY_FRAME_MESSAGES = new FrameMessage[0];
        EMPTY_BINARY_IMAGE_MESSAGES = new BinaryImageMessage[0];
        EMPTY_CUSTOM_ATTRIBUTE_MESSAGES = new CustomAttributeMessage[0];
    }
    
    private static RepeatedMessage createBinaryImagesMessage(final BinaryImageData[] array) {
        BinaryImageMessage[] empty_BINARY_IMAGE_MESSAGES;
        if (array != null) {
            empty_BINARY_IMAGE_MESSAGES = new BinaryImageMessage[array.length];
        }
        else {
            empty_BINARY_IMAGE_MESSAGES = NativeCrashWriter.EMPTY_BINARY_IMAGE_MESSAGES;
        }
        for (int i = 0; i < empty_BINARY_IMAGE_MESSAGES.length; ++i) {
            empty_BINARY_IMAGE_MESSAGES[i] = new BinaryImageMessage(array[i]);
        }
        return new RepeatedMessage((ProtobufMessage[])empty_BINARY_IMAGE_MESSAGES);
    }
    
    private static RepeatedMessage createCustomAttributesMessage(final CustomAttributeData[] array) {
        CustomAttributeMessage[] empty_CUSTOM_ATTRIBUTE_MESSAGES;
        if (array != null) {
            empty_CUSTOM_ATTRIBUTE_MESSAGES = new CustomAttributeMessage[array.length];
        }
        else {
            empty_CUSTOM_ATTRIBUTE_MESSAGES = NativeCrashWriter.EMPTY_CUSTOM_ATTRIBUTE_MESSAGES;
        }
        for (int i = 0; i < empty_CUSTOM_ATTRIBUTE_MESSAGES.length; ++i) {
            empty_CUSTOM_ATTRIBUTE_MESSAGES[i] = new CustomAttributeMessage(array[i]);
        }
        return new RepeatedMessage((ProtobufMessage[])empty_CUSTOM_ATTRIBUTE_MESSAGES);
    }
    
    private static DeviceMessage createDeviceMessage(final DeviceData deviceData) {
        return new DeviceMessage(deviceData.batteryCapacity / 100.0f, deviceData.batteryVelocity, deviceData.proximity, deviceData.orientation, deviceData.totalPhysicalMemory - deviceData.availablePhysicalMemory, deviceData.totalInternalStorage - deviceData.availableInternalStorage);
    }
    
    private static EventMessage createEventMessage(final SessionEventData sessionEventData, final LogFileManager logFileManager, final Map<String, String> map) throws IOException {
        SignalData signalData;
        if (sessionEventData.signal != null) {
            signalData = sessionEventData.signal;
        }
        else {
            signalData = NativeCrashWriter.DEFAULT_SIGNAL;
        }
        final ApplicationMessage applicationMessage = new ApplicationMessage(new ExecutionMessage(new SignalMessage(signalData), createThreadsMessage(sessionEventData.threads), createBinaryImagesMessage(sessionEventData.binaryImages)), createCustomAttributesMessage(mergeCustomAttributes(sessionEventData.customAttributes, map)));
        final DeviceMessage deviceMessage = createDeviceMessage(sessionEventData.deviceData);
        final ByteString byteStringForLog = logFileManager.getByteStringForLog();
        if (byteStringForLog == null) {
            Fabric.getLogger().d("CrashlyticsCore", "No log data to include with this event.");
        }
        logFileManager.clearLog();
        ProtobufMessage protobufMessage;
        if (byteStringForLog != null) {
            protobufMessage = new LogMessage(byteStringForLog);
        }
        else {
            protobufMessage = new NullMessage();
        }
        return new EventMessage(sessionEventData.timestamp, "ndk-crash", new ProtobufMessage[] { applicationMessage, deviceMessage, protobufMessage });
    }
    
    private static RepeatedMessage createFramesMessage(final ThreadData.FrameData[] array) {
        FrameMessage[] empty_FRAME_MESSAGES;
        if (array != null) {
            empty_FRAME_MESSAGES = new FrameMessage[array.length];
        }
        else {
            empty_FRAME_MESSAGES = NativeCrashWriter.EMPTY_FRAME_MESSAGES;
        }
        for (int i = 0; i < empty_FRAME_MESSAGES.length; ++i) {
            empty_FRAME_MESSAGES[i] = new FrameMessage(array[i]);
        }
        return new RepeatedMessage((ProtobufMessage[])empty_FRAME_MESSAGES);
    }
    
    private static RepeatedMessage createThreadsMessage(final ThreadData[] array) {
        ThreadMessage[] empty_THREAD_MESSAGES;
        if (array != null) {
            empty_THREAD_MESSAGES = new ThreadMessage[array.length];
        }
        else {
            empty_THREAD_MESSAGES = NativeCrashWriter.EMPTY_THREAD_MESSAGES;
        }
        for (int i = 0; i < empty_THREAD_MESSAGES.length; ++i) {
            final ThreadData threadData = array[i];
            empty_THREAD_MESSAGES[i] = new ThreadMessage(threadData, createFramesMessage(threadData.frames));
        }
        return new RepeatedMessage((ProtobufMessage[])empty_THREAD_MESSAGES);
    }
    
    private static CustomAttributeData[] mergeCustomAttributes(final CustomAttributeData[] array, final Map<String, String> map) {
        final TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>(map);
        if (array != null) {
            for (int length = array.length, i = 0; i < length; ++i) {
                final CustomAttributeData customAttributeData = array[i];
                treeMap.put(customAttributeData.key, customAttributeData.value);
            }
        }
        final Map.Entry[] array2 = treeMap.entrySet().toArray((Map.Entry[])new Map.Entry[treeMap.size()]);
        final CustomAttributeData[] array3 = new CustomAttributeData[array2.length];
        for (int j = 0; j < array3.length; ++j) {
            array3[j] = new CustomAttributeData((String)array2[j].getKey(), (String)array2[j].getValue());
        }
        return array3;
    }
    
    public static void writeNativeCrash(final SessionEventData sessionEventData, final LogFileManager logFileManager, final Map<String, String> map, final CodedOutputStream codedOutputStream) throws IOException {
        ((ProtobufMessage)createEventMessage(sessionEventData, logFileManager, map)).write(codedOutputStream);
    }
    
    private static final class ApplicationMessage extends ProtobufMessage
    {
        public ApplicationMessage(final ExecutionMessage executionMessage, final RepeatedMessage repeatedMessage) {
            super(3, new ProtobufMessage[] { executionMessage, repeatedMessage });
        }
    }
    
    private static final class BinaryImageMessage extends ProtobufMessage
    {
        private final long baseAddr;
        private final String filePath;
        private final long imageSize;
        private final String uuid;
        
        public BinaryImageMessage(final BinaryImageData binaryImageData) {
            super(4, new ProtobufMessage[0]);
            this.baseAddr = binaryImageData.baseAddress;
            this.imageSize = binaryImageData.size;
            this.filePath = binaryImageData.path;
            this.uuid = binaryImageData.id;
        }
        
        @Override
        public int getPropertiesSize() {
            return CodedOutputStream.computeBytesSize(3, ByteString.copyFromUtf8(this.filePath)) + CodedOutputStream.computeUInt64Size(1, this.baseAddr) + CodedOutputStream.computeUInt64Size(2, this.imageSize) + CodedOutputStream.computeBytesSize(4, ByteString.copyFromUtf8(this.uuid));
        }
        
        @Override
        public void writeProperties(final CodedOutputStream codedOutputStream) throws IOException {
            codedOutputStream.writeUInt64(1, this.baseAddr);
            codedOutputStream.writeUInt64(2, this.imageSize);
            codedOutputStream.writeBytes(3, ByteString.copyFromUtf8(this.filePath));
            codedOutputStream.writeBytes(4, ByteString.copyFromUtf8(this.uuid));
        }
    }
    
    private static final class CustomAttributeMessage extends ProtobufMessage
    {
        private final String key;
        private final String value;
        
        public CustomAttributeMessage(final CustomAttributeData customAttributeData) {
            super(2, new ProtobufMessage[0]);
            this.key = customAttributeData.key;
            this.value = customAttributeData.value;
        }
        
        @Override
        public int getPropertiesSize() {
            final int computeBytesSize = CodedOutputStream.computeBytesSize(1, ByteString.copyFromUtf8(this.key));
            String value;
            if (this.value == null) {
                value = "";
            }
            else {
                value = this.value;
            }
            return computeBytesSize + CodedOutputStream.computeBytesSize(2, ByteString.copyFromUtf8(value));
        }
        
        @Override
        public void writeProperties(final CodedOutputStream codedOutputStream) throws IOException {
            codedOutputStream.writeBytes(1, ByteString.copyFromUtf8(this.key));
            String value;
            if (this.value == null) {
                value = "";
            }
            else {
                value = this.value;
            }
            codedOutputStream.writeBytes(2, ByteString.copyFromUtf8(value));
        }
    }
    
    private static final class DeviceMessage extends ProtobufMessage
    {
        private final float batteryLevel;
        private final int batteryVelocity;
        private final long diskUsed;
        private final int orientation;
        private final boolean proximityOn;
        private final long ramUsed;
        
        public DeviceMessage(final float batteryLevel, final int batteryVelocity, final boolean proximityOn, final int orientation, final long ramUsed, final long diskUsed) {
            super(5, new ProtobufMessage[0]);
            this.batteryLevel = batteryLevel;
            this.batteryVelocity = batteryVelocity;
            this.proximityOn = proximityOn;
            this.orientation = orientation;
            this.ramUsed = ramUsed;
            this.diskUsed = diskUsed;
        }
        
        @Override
        public int getPropertiesSize() {
            return 0 + CodedOutputStream.computeFloatSize(1, this.batteryLevel) + CodedOutputStream.computeSInt32Size(2, this.batteryVelocity) + CodedOutputStream.computeBoolSize(3, this.proximityOn) + CodedOutputStream.computeUInt32Size(4, this.orientation) + CodedOutputStream.computeUInt64Size(5, this.ramUsed) + CodedOutputStream.computeUInt64Size(6, this.diskUsed);
        }
        
        @Override
        public void writeProperties(final CodedOutputStream codedOutputStream) throws IOException {
            codedOutputStream.writeFloat(1, this.batteryLevel);
            codedOutputStream.writeSInt32(2, this.batteryVelocity);
            codedOutputStream.writeBool(3, this.proximityOn);
            codedOutputStream.writeUInt32(4, this.orientation);
            codedOutputStream.writeUInt64(5, this.ramUsed);
            codedOutputStream.writeUInt64(6, this.diskUsed);
        }
    }
    
    private static final class EventMessage extends ProtobufMessage
    {
        private final String crashType;
        private final long time;
        
        public EventMessage(final long time, final String crashType, final ProtobufMessage... array) {
            super(10, array);
            this.time = time;
            this.crashType = crashType;
        }
        
        @Override
        public int getPropertiesSize() {
            return CodedOutputStream.computeUInt64Size(1, this.time) + CodedOutputStream.computeBytesSize(2, ByteString.copyFromUtf8(this.crashType));
        }
        
        @Override
        public void writeProperties(final CodedOutputStream codedOutputStream) throws IOException {
            codedOutputStream.writeUInt64(1, this.time);
            codedOutputStream.writeBytes(2, ByteString.copyFromUtf8(this.crashType));
        }
    }
    
    private static final class ExecutionMessage extends ProtobufMessage
    {
        public ExecutionMessage(final SignalMessage signalMessage, final RepeatedMessage repeatedMessage, final RepeatedMessage repeatedMessage2) {
            super(1, new ProtobufMessage[] { repeatedMessage, signalMessage, repeatedMessage2 });
        }
    }
    
    private static final class FrameMessage extends ProtobufMessage
    {
        private final long address;
        private final String file;
        private final int importance;
        private final long offset;
        private final String symbol;
        
        public FrameMessage(final ThreadData.FrameData frameData) {
            super(3, new ProtobufMessage[0]);
            this.address = frameData.address;
            this.symbol = frameData.symbol;
            this.file = frameData.file;
            this.offset = frameData.offset;
            this.importance = frameData.importance;
        }
        
        @Override
        public int getPropertiesSize() {
            return CodedOutputStream.computeUInt64Size(1, this.address) + CodedOutputStream.computeBytesSize(2, ByteString.copyFromUtf8(this.symbol)) + CodedOutputStream.computeBytesSize(3, ByteString.copyFromUtf8(this.file)) + CodedOutputStream.computeUInt64Size(4, this.offset) + CodedOutputStream.computeUInt32Size(5, this.importance);
        }
        
        @Override
        public void writeProperties(final CodedOutputStream codedOutputStream) throws IOException {
            codedOutputStream.writeUInt64(1, this.address);
            codedOutputStream.writeBytes(2, ByteString.copyFromUtf8(this.symbol));
            codedOutputStream.writeBytes(3, ByteString.copyFromUtf8(this.file));
            codedOutputStream.writeUInt64(4, this.offset);
            codedOutputStream.writeUInt32(5, this.importance);
        }
    }
    
    private static final class LogMessage extends ProtobufMessage
    {
        ByteString logBytes;
        
        public LogMessage(final ByteString logBytes) {
            super(6, new ProtobufMessage[0]);
            this.logBytes = logBytes;
        }
        
        @Override
        public int getPropertiesSize() {
            return CodedOutputStream.computeBytesSize(1, this.logBytes);
        }
        
        @Override
        public void writeProperties(final CodedOutputStream codedOutputStream) throws IOException {
            codedOutputStream.writeBytes(1, this.logBytes);
        }
    }
    
    private static final class NullMessage extends ProtobufMessage
    {
        public NullMessage() {
            super(0, new ProtobufMessage[0]);
        }
        
        @Override
        public void write(final CodedOutputStream codedOutputStream) throws IOException {
        }
    }
    
    private abstract static class ProtobufMessage
    {
        private final ProtobufMessage[] children;
        private final int tag;
        
        public ProtobufMessage(final int tag, ProtobufMessage... access$000) {
            this.tag = tag;
            if (access$000 == null) {
                access$000 = NativeCrashWriter.EMPTY_CHILDREN;
            }
            this.children = access$000;
        }
        
        public int getPropertiesSize() {
            return 0;
        }
        
        public int getSize() {
            final int sizeNoTag = this.getSizeNoTag();
            return sizeNoTag + CodedOutputStream.computeRawVarint32Size(sizeNoTag) + CodedOutputStream.computeTagSize(this.tag);
        }
        
        public int getSizeNoTag() {
            int propertiesSize = this.getPropertiesSize();
            final ProtobufMessage[] children = this.children;
            for (int length = children.length, i = 0; i < length; ++i) {
                propertiesSize += children[i].getSize();
            }
            return propertiesSize;
        }
        
        public void write(final CodedOutputStream codedOutputStream) throws IOException {
            codedOutputStream.writeTag(this.tag, 2);
            codedOutputStream.writeRawVarint32(this.getSizeNoTag());
            this.writeProperties(codedOutputStream);
            final ProtobufMessage[] children = this.children;
            for (int length = children.length, i = 0; i < length; ++i) {
                children[i].write(codedOutputStream);
            }
        }
        
        public void writeProperties(final CodedOutputStream codedOutputStream) throws IOException {
        }
    }
    
    private static final class RepeatedMessage extends ProtobufMessage
    {
        private final ProtobufMessage[] messages;
        
        public RepeatedMessage(final ProtobufMessage... messages) {
            super(0, new ProtobufMessage[0]);
            this.messages = messages;
        }
        
        @Override
        public int getSize() {
            int n = 0;
            final ProtobufMessage[] messages = this.messages;
            for (int length = messages.length, i = 0; i < length; ++i) {
                n += messages[i].getSize();
            }
            return n;
        }
        
        @Override
        public void write(final CodedOutputStream codedOutputStream) throws IOException {
            final ProtobufMessage[] messages = this.messages;
            for (int length = messages.length, i = 0; i < length; ++i) {
                messages[i].write(codedOutputStream);
            }
        }
    }
    
    private static final class SignalMessage extends ProtobufMessage
    {
        private final long sigAddr;
        private final String sigCode;
        private final String sigName;
        
        public SignalMessage(final SignalData signalData) {
            super(3, new ProtobufMessage[0]);
            this.sigName = signalData.name;
            this.sigCode = signalData.code;
            this.sigAddr = signalData.faultAddress;
        }
        
        @Override
        public int getPropertiesSize() {
            return CodedOutputStream.computeBytesSize(1, ByteString.copyFromUtf8(this.sigName)) + CodedOutputStream.computeBytesSize(2, ByteString.copyFromUtf8(this.sigCode)) + CodedOutputStream.computeUInt64Size(3, this.sigAddr);
        }
        
        @Override
        public void writeProperties(final CodedOutputStream codedOutputStream) throws IOException {
            codedOutputStream.writeBytes(1, ByteString.copyFromUtf8(this.sigName));
            codedOutputStream.writeBytes(2, ByteString.copyFromUtf8(this.sigCode));
            codedOutputStream.writeUInt64(3, this.sigAddr);
        }
    }
    
    private static final class ThreadMessage extends ProtobufMessage
    {
        private final int importance;
        private final String name;
        
        public ThreadMessage(final ThreadData threadData, final RepeatedMessage repeatedMessage) {
            super(1, new ProtobufMessage[] { repeatedMessage });
            this.name = threadData.name;
            this.importance = threadData.importance;
        }
        
        private boolean hasName() {
            return this.name != null && this.name.length() > 0;
        }
        
        @Override
        public int getPropertiesSize() {
            int computeBytesSize;
            if (this.hasName()) {
                computeBytesSize = CodedOutputStream.computeBytesSize(1, ByteString.copyFromUtf8(this.name));
            }
            else {
                computeBytesSize = 0;
            }
            return CodedOutputStream.computeUInt32Size(2, this.importance) + computeBytesSize;
        }
        
        @Override
        public void writeProperties(final CodedOutputStream codedOutputStream) throws IOException {
            if (this.hasName()) {
                codedOutputStream.writeBytes(1, ByteString.copyFromUtf8(this.name));
            }
            codedOutputStream.writeUInt32(2, this.importance);
        }
    }
}
