// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import android.graphics.BitmapFactory;
import java.util.Arrays;
import java.util.List;
import android.graphics.Bitmap;

public class BIFFile
{
    private int bifVersion;
    private byte[] id;
    private Bitmap mBmp;
    private int thumbnailCount;
    private byte[] thumbnailData;
    private List<BIFFrameDescriptor> thumbnailFramesList;
    private boolean thumbnailReady;
    private int thumbnailTimestampMultiplier;
    
    public BIFFile(final byte[] thumbnailData) {
        this.thumbnailData = thumbnailData;
        this.thumbnailReady = false;
        this.mBmp = null;
    }
    
    public static long byteArrayToInt(final byte[] array) {
        return (array[3] & 0xFF) | (array[2] & 0xFF) << 8 | (array[1] & 0xFF) << 16 | (array[0] & 0xFF) << 24;
    }
    
    public static int getInteger(final byte[] array) {
        reverse(array);
        return (int)byteArrayToInt(array);
    }
    
    public static void reverse(final byte[] array) {
        if (array != null) {
            for (int n = 0, i = array.length - 1; i > n; --i, ++n) {
                final byte b = array[i];
                array[i] = array[n];
                array[n] = b;
            }
        }
    }
    
    public Bitmap getImageAtSeconds(int i) {
        final long n = i * 1000;
        BIFFrameDescriptor bifFrameDescriptor = null;
        int n2 = 0;
        long n3 = Long.MAX_VALUE;
        BIFFrameDescriptor bifFrameDescriptor2;
        long abs;
        long n4;
        for (i = 0; i < this.thumbnailFramesList.size(); ++i, n3 = n4) {
            bifFrameDescriptor2 = this.thumbnailFramesList.get(i);
            abs = Math.abs(bifFrameDescriptor2.Timestamp * this.thumbnailTimestampMultiplier - n);
            n4 = n3;
            if (abs < n3) {
                n4 = abs;
                bifFrameDescriptor = bifFrameDescriptor2;
                n2 = i;
            }
        }
        i = n2 + 1;
        if (i >= this.thumbnailCount) {
            return null;
        }
        i = this.thumbnailFramesList.get(i).DataOffset - bifFrameDescriptor.DataOffset;
        if (this.thumbnailData.length < bifFrameDescriptor.DataOffset + i) {
            return null;
        }
        final int dataOffset = bifFrameDescriptor.DataOffset;
        final byte[] copyOfRange = Arrays.copyOfRange(this.thumbnailData, dataOffset, dataOffset + i);
        if (this.mBmp != null && !this.mBmp.isRecycled()) {
            this.mBmp.recycle();
        }
        return this.mBmp = BitmapFactory.decodeByteArray(copyOfRange, 0, copyOfRange.length);
    }
    
    public boolean isReadyForDisplay() {
        return this.thumbnailReady;
    }
    
    public void prepare() {
        this.thumbnailReady = false;
        this.id = Arrays.copyOfRange(this.thumbnailData, 0, 8);
        while (true) {
            try {
                new String(this.id, "UTF-8");
                this.bifVersion = getInteger(Arrays.copyOfRange(this.thumbnailData, 8, 12));
                this.thumbnailCount = getInteger(Arrays.copyOfRange(this.thumbnailData, 12, 16));
                int integer;
                if ((integer = getInteger(Arrays.copyOfRange(this.thumbnailData, 16, 20))) == 0) {
                    integer = 1000;
                }
                this.thumbnailTimestampMultiplier = integer;
                int n = 64;
                this.thumbnailFramesList = new ArrayList<BIFFrameDescriptor>();
                for (int i = 0; i < this.thumbnailCount + 1; ++i, n += 8) {
                    final BIFFrameDescriptor bifFrameDescriptor = new BIFFrameDescriptor();
                    bifFrameDescriptor.Timestamp = getInteger(Arrays.copyOfRange(this.thumbnailData, n, n + 4));
                    bifFrameDescriptor.DataOffset = getInteger(Arrays.copyOfRange(this.thumbnailData, n + 4, n + 8));
                    this.thumbnailFramesList.add(bifFrameDescriptor);
                }
            }
            catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
                continue;
            }
            break;
        }
        this.thumbnailReady = true;
    }
    
    public class BIFFrameDescriptor
    {
        public int DataOffset;
        public int Timestamp;
    }
}
