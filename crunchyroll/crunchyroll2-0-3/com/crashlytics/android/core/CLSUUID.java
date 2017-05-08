// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

import java.util.Date;
import android.os.Process;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.util.Locale;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.common.IdManager;
import java.util.concurrent.atomic.AtomicLong;

class CLSUUID
{
    private static String _clsId;
    private static final AtomicLong _sequenceNumber;
    
    static {
        _sequenceNumber = new AtomicLong(0L);
    }
    
    public CLSUUID(final IdManager idManager) {
        final byte[] array = new byte[10];
        this.populateTime(array);
        this.populateSequenceNumber(array);
        this.populatePID(array);
        final String sha1 = CommonUtils.sha1(idManager.getAppInstallIdentifier());
        final String hexify = CommonUtils.hexify(array);
        CLSUUID._clsId = String.format(Locale.US, "%s-%s-%s-%s", hexify.substring(0, 12), hexify.substring(12, 16), hexify.subSequence(16, 20), sha1.substring(0, 12)).toUpperCase(Locale.US);
    }
    
    private static byte[] convertLongToFourByteBuffer(final long n) {
        final ByteBuffer allocate = ByteBuffer.allocate(4);
        allocate.putInt((int)n);
        allocate.order(ByteOrder.BIG_ENDIAN);
        allocate.position(0);
        return allocate.array();
    }
    
    private static byte[] convertLongToTwoByteBuffer(final long n) {
        final ByteBuffer allocate = ByteBuffer.allocate(2);
        allocate.putShort((short)n);
        allocate.order(ByteOrder.BIG_ENDIAN);
        allocate.position(0);
        return allocate.array();
    }
    
    private void populatePID(final byte[] array) {
        final byte[] convertLongToTwoByteBuffer = convertLongToTwoByteBuffer((short)(Object)Integer.valueOf(Process.myPid()));
        array[8] = convertLongToTwoByteBuffer[0];
        array[9] = convertLongToTwoByteBuffer[1];
    }
    
    private void populateSequenceNumber(final byte[] array) {
        final byte[] convertLongToTwoByteBuffer = convertLongToTwoByteBuffer(CLSUUID._sequenceNumber.incrementAndGet());
        array[6] = convertLongToTwoByteBuffer[0];
        array[7] = convertLongToTwoByteBuffer[1];
    }
    
    private void populateTime(final byte[] array) {
        final long time = new Date().getTime();
        final byte[] convertLongToFourByteBuffer = convertLongToFourByteBuffer(time / 1000L);
        array[0] = convertLongToFourByteBuffer[0];
        array[1] = convertLongToFourByteBuffer[1];
        array[2] = convertLongToFourByteBuffer[2];
        array[3] = convertLongToFourByteBuffer[3];
        final byte[] convertLongToTwoByteBuffer = convertLongToTwoByteBuffer(time % 1000L);
        array[4] = convertLongToTwoByteBuffer[0];
        array[5] = convertLongToTwoByteBuffer[1];
    }
    
    @Override
    public String toString() {
        return CLSUUID._clsId;
    }
}
