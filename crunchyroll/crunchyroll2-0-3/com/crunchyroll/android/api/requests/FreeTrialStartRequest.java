// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import com.crunchyroll.android.api.models.PaymentInformation;

public final class FreeTrialStartRequest extends AbstractPaymentRequest
{
    public FreeTrialStartRequest(final PaymentInformation paymentInformation) {
        super(paymentInformation);
    }
    
    @Override
    public String getApiMethod() {
        return "free_trial_start";
    }
    
    @Override
    public String toString() {
        return "StartFreeTrialRequest [getParams()=" + this.getParams() + "]";
    }
}
