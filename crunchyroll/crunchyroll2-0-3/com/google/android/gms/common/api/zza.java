// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.api;

import android.os.DeadObjectException;
import android.app.PendingIntent;
import android.os.RemoteException;
import com.google.android.gms.common.internal.zzu;
import java.util.concurrent.atomic.AtomicReference;

public class zza
{
    public abstract static class zza<R extends Result, A extends Api.Client> extends AbstractPendingResult<R> implements com.google.android.gms.common.api.zza.zzb<R>, zze<A>
    {
        private final Api.ClientKey<A> zzVu;
        private AtomicReference<zzc> zzWm;
        
        protected zza(final Api.ClientKey<A> clientKey, final GoogleApiClient googleApiClient) {
            super(zzu.zzb(googleApiClient, "GoogleApiClient must not be null").getLooper());
            this.zzWm = new AtomicReference<zzc>();
            this.zzVu = (Api.ClientKey<A>)zzu.zzu((Api.ClientKey)clientKey);
        }
        
        private void zza(final RemoteException ex) {
            this.zzr(new Status(8, ex.getLocalizedMessage(), null));
        }
        
        @Override
        protected void onResultConsumed() {
            final zzc zzc = this.zzWm.getAndSet(null);
            if (zzc != null) {
                zzc.zzc(this);
            }
        }
        
        protected abstract void zza(final A p0) throws RemoteException;
        
        @Override
        public void zza(final zzc zzc) {
            this.zzWm.set(zzc);
        }
        
        @Override
        public final void zzb(final A a) throws DeadObjectException {
            try {
                this.zza(a);
            }
            catch (DeadObjectException ex) {
                this.zza((RemoteException)ex);
                throw ex;
            }
            catch (RemoteException ex2) {
                this.zza(ex2);
            }
        }
        
        @Override
        public final Api.ClientKey<A> zzms() {
            return this.zzVu;
        }
        
        @Override
        public int zzmv() {
            return 0;
        }
        
        @Override
        public final void zzr(final Status status) {
            zzu.zzb(!status.isSuccess(), "Failed result must not be success");
            this.setResult(this.createFailedResult(status));
        }
    }
    
    public interface zzb<R>
    {
        void zzm(final R p0);
    }
}
