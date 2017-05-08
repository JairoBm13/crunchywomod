// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.internal.safeparcel;

import android.os.Bundle;
import android.os.IBinder;
import java.math.BigDecimal;
import java.math.BigInteger;
import android.os.Parcelable;
import android.os.Parcelable$Creator;
import java.util.ArrayList;
import android.os.Parcel;

public class zza
{
    public static String[] zzA(final Parcel parcel, int zza) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final String[] stringArray = parcel.createStringArray();
        parcel.setDataPosition(zza + dataPosition);
        return stringArray;
    }
    
    public static ArrayList<String> zzC(final Parcel parcel, int zza) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final ArrayList stringArrayList = parcel.createStringArrayList();
        parcel.setDataPosition(zza + dataPosition);
        return (ArrayList<String>)stringArrayList;
    }
    
    public static Parcel zzD(final Parcel parcel, int zza) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final Parcel obtain = Parcel.obtain();
        obtain.appendFrom(parcel, dataPosition, zza);
        parcel.setDataPosition(zza + dataPosition);
        return obtain;
    }
    
    public static Parcel[] zzE(final Parcel parcel, int i) {
        final int zza = zza(parcel, i);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final int int1 = parcel.readInt();
        final Parcel[] array = new Parcel[int1];
        int int2;
        int dataPosition2;
        Parcel obtain;
        for (i = 0; i < int1; ++i) {
            int2 = parcel.readInt();
            if (int2 != 0) {
                dataPosition2 = parcel.dataPosition();
                obtain = Parcel.obtain();
                obtain.appendFrom(parcel, dataPosition2, int2);
                array[i] = obtain;
                parcel.setDataPosition(int2 + dataPosition2);
            }
            else {
                array[i] = null;
            }
        }
        parcel.setDataPosition(dataPosition + zza);
        return array;
    }
    
    public static int zza(final Parcel parcel, final int n) {
        if ((n & 0xFFFF0000) != 0xFFFF0000) {
            return n >> 16 & 0xFFFF;
        }
        return parcel.readInt();
    }
    
    public static <T extends Parcelable> T zza(final Parcel parcel, int zza, final Parcelable$Creator<T> parcelable$Creator) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final Parcelable parcelable = (Parcelable)parcelable$Creator.createFromParcel(parcel);
        parcel.setDataPosition(zza + dataPosition);
        return (T)parcelable;
    }
    
    private static void zza(final Parcel parcel, int zza, final int n) {
        zza = zza(parcel, zza);
        if (zza != n) {
            throw new zza("Expected size " + n + " got " + zza + " (0x" + Integer.toHexString(zza) + ")", parcel);
        }
    }
    
    public static int zzaa(final Parcel parcel) {
        return parcel.readInt();
    }
    
    public static int zzab(final Parcel parcel) {
        final int zzaa = zzaa(parcel);
        final int zza = zza(parcel, zzaa);
        final int dataPosition = parcel.dataPosition();
        if (zzbA(zzaa) != 20293) {
            throw new zza("Expected object header. Got 0x" + Integer.toHexString(zzaa), parcel);
        }
        final int n = dataPosition + zza;
        if (n < dataPosition || n > parcel.dataSize()) {
            throw new zza("Size read is invalid start=" + dataPosition + " end=" + n, parcel);
        }
        return n;
    }
    
    public static void zzb(final Parcel parcel, final int n) {
        parcel.setDataPosition(zza(parcel, n) + parcel.dataPosition());
    }
    
    public static <T> T[] zzb(final Parcel parcel, int zza, final Parcelable$Creator<T> parcelable$Creator) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final Object[] typedArray = parcel.createTypedArray((Parcelable$Creator)parcelable$Creator);
        parcel.setDataPosition(zza + dataPosition);
        return (T[])typedArray;
    }
    
    public static int zzbA(final int n) {
        return 0xFFFF & n;
    }
    
    public static <T> ArrayList<T> zzc(final Parcel parcel, int zza, final Parcelable$Creator<T> parcelable$Creator) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final ArrayList typedArrayList = parcel.createTypedArrayList((Parcelable$Creator)parcelable$Creator);
        parcel.setDataPosition(zza + dataPosition);
        return (ArrayList<T>)typedArrayList;
    }
    
    public static boolean zzc(final Parcel parcel, final int n) {
        zza(parcel, n, 4);
        return parcel.readInt() != 0;
    }
    
    public static int zzg(final Parcel parcel, final int n) {
        zza(parcel, n, 4);
        return parcel.readInt();
    }
    
    public static long zzi(final Parcel parcel, final int n) {
        zza(parcel, n, 8);
        return parcel.readLong();
    }
    
    public static BigInteger zzk(final Parcel parcel, int zza) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final byte[] byteArray = parcel.createByteArray();
        parcel.setDataPosition(zza + dataPosition);
        return new BigInteger(byteArray);
    }
    
    public static float zzl(final Parcel parcel, final int n) {
        zza(parcel, n, 4);
        return parcel.readFloat();
    }
    
    public static double zzm(final Parcel parcel, final int n) {
        zza(parcel, n, 8);
        return parcel.readDouble();
    }
    
    public static BigDecimal zzn(final Parcel parcel, int zza) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final byte[] byteArray = parcel.createByteArray();
        final int int1 = parcel.readInt();
        parcel.setDataPosition(zza + dataPosition);
        return new BigDecimal(new BigInteger(byteArray), int1);
    }
    
    public static String zzo(final Parcel parcel, int zza) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final String string = parcel.readString();
        parcel.setDataPosition(zza + dataPosition);
        return string;
    }
    
    public static IBinder zzp(final Parcel parcel, int zza) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final IBinder strongBinder = parcel.readStrongBinder();
        parcel.setDataPosition(zza + dataPosition);
        return strongBinder;
    }
    
    public static Bundle zzq(final Parcel parcel, int zza) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final Bundle bundle = parcel.readBundle();
        parcel.setDataPosition(zza + dataPosition);
        return bundle;
    }
    
    public static byte[] zzr(final Parcel parcel, int zza) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final byte[] byteArray = parcel.createByteArray();
        parcel.setDataPosition(zza + dataPosition);
        return byteArray;
    }
    
    public static boolean[] zzt(final Parcel parcel, int zza) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final boolean[] booleanArray = parcel.createBooleanArray();
        parcel.setDataPosition(zza + dataPosition);
        return booleanArray;
    }
    
    public static int[] zzu(final Parcel parcel, int zza) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final int[] intArray = parcel.createIntArray();
        parcel.setDataPosition(zza + dataPosition);
        return intArray;
    }
    
    public static long[] zzv(final Parcel parcel, int zza) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final long[] longArray = parcel.createLongArray();
        parcel.setDataPosition(zza + dataPosition);
        return longArray;
    }
    
    public static BigInteger[] zzw(final Parcel parcel, int i) {
        final int zza = zza(parcel, i);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final int int1 = parcel.readInt();
        final BigInteger[] array = new BigInteger[int1];
        for (i = 0; i < int1; ++i) {
            array[i] = new BigInteger(parcel.createByteArray());
        }
        parcel.setDataPosition(dataPosition + zza);
        return array;
    }
    
    public static float[] zzx(final Parcel parcel, int zza) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final float[] floatArray = parcel.createFloatArray();
        parcel.setDataPosition(zza + dataPosition);
        return floatArray;
    }
    
    public static double[] zzy(final Parcel parcel, int zza) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final double[] doubleArray = parcel.createDoubleArray();
        parcel.setDataPosition(zza + dataPosition);
        return doubleArray;
    }
    
    public static BigDecimal[] zzz(final Parcel parcel, int i) {
        final int zza = zza(parcel, i);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final int int1 = parcel.readInt();
        final BigDecimal[] array = new BigDecimal[int1];
        for (i = 0; i < int1; ++i) {
            array[i] = new BigDecimal(new BigInteger(parcel.createByteArray()), parcel.readInt());
        }
        parcel.setDataPosition(dataPosition + zza);
        return array;
    }
    
    public static class zza extends RuntimeException
    {
        public zza(final String s, final Parcel parcel) {
            super(s + " Parcel: pos=" + parcel.dataPosition() + " size=" + parcel.dataSize());
        }
    }
}
