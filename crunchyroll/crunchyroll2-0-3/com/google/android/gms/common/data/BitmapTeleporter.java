// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.data;

import java.io.OutputStream;
import java.io.DataOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import android.os.Parcel;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.util.Log;
import java.io.Closeable;
import java.io.File;
import android.graphics.Bitmap;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class BitmapTeleporter implements SafeParcelable
{
    public static final Parcelable$Creator<BitmapTeleporter> CREATOR;
    final int zzCY;
    ParcelFileDescriptor zzCZ;
    final int zzSq;
    private Bitmap zzYm;
    private boolean zzYn;
    private File zzYo;
    
    static {
        CREATOR = (Parcelable$Creator)new zza();
    }
    
    BitmapTeleporter(final int zzCY, final ParcelFileDescriptor zzCZ, final int zzSq) {
        this.zzCY = zzCY;
        this.zzCZ = zzCZ;
        this.zzSq = zzSq;
        this.zzYm = null;
        this.zzYn = false;
    }
    
    private void zza(final Closeable closeable) {
        try {
            closeable.close();
        }
        catch (IOException ex) {
            Log.w("BitmapTeleporter", "Could not close stream", (Throwable)ex);
        }
    }
    
    private FileOutputStream zznd() {
        if (this.zzYo == null) {
            throw new IllegalStateException("setTempDir() must be called before writing this object to a parcel");
        }
        File file;
        try {
            final File tempFile;
            file = (tempFile = File.createTempFile("teleporter", ".tmp", this.zzYo));
            final FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
            final BitmapTeleporter bitmapTeleporter = this;
            final File file2 = file;
            final int n = 268435456;
            final ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.open(file2, n);
            bitmapTeleporter.zzCZ = parcelFileDescriptor;
            final File file3 = file;
            file3.delete();
            return fileOutputStream;
        }
        catch (IOException ex) {
            throw new IllegalStateException("Could not create temporary file", ex);
        }
        try {
            final File tempFile = file;
            final FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
            final BitmapTeleporter bitmapTeleporter = this;
            final File file2 = file;
            final int n = 268435456;
            final ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.open(file2, n);
            bitmapTeleporter.zzCZ = parcelFileDescriptor;
            final File file3 = file;
            file3.delete();
            return fileOutputStream;
        }
        catch (FileNotFoundException ex2) {
            throw new IllegalStateException("Temporary file is somehow already deleted");
        }
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        Label_0100: {
            if (this.zzCZ != null) {
                break Label_0100;
            }
            final Bitmap zzYm = this.zzYm;
            final ByteBuffer allocate = ByteBuffer.allocate(zzYm.getRowBytes() * zzYm.getHeight());
            zzYm.copyPixelsToBuffer((Buffer)allocate);
            final byte[] array = allocate.array();
            final DataOutputStream dataOutputStream = new DataOutputStream(this.zznd());
            try {
                dataOutputStream.writeInt(array.length);
                dataOutputStream.writeInt(zzYm.getWidth());
                dataOutputStream.writeInt(zzYm.getHeight());
                dataOutputStream.writeUTF(zzYm.getConfig().toString());
                dataOutputStream.write(array);
                this.zza(dataOutputStream);
                zza.zza(this, parcel, n | 0x1);
                this.zzCZ = null;
            }
            catch (IOException ex) {
                throw new IllegalStateException("Could not write into unlinked file", ex);
            }
            finally {
                this.zza(dataOutputStream);
            }
        }
    }
}
