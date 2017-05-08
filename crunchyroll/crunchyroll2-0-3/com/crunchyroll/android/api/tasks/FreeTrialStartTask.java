// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.requests.FreeTrialStartRequest;
import android.content.Context;
import com.crunchyroll.android.api.models.PaymentInformation;

public class FreeTrialStartTask extends BaseTask<Void>
{
    private final PaymentInformation mPaymentInformation;
    
    public FreeTrialStartTask(final Context context, final PaymentInformation mPaymentInformation) {
        super(context);
        this.mPaymentInformation = mPaymentInformation;
    }
    
    @Override
    public Void call() throws Exception {
        this.getApiService().run(new FreeTrialStartRequest(this.mPaymentInformation));
        return null;
    }
    
    @Override
    protected void onSuccess(final Void void1) throws Exception {
        super.onSuccess(void1);
    }
}
