// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import com.crunchyroll.android.api.models.PaymentInformation;

public final class MembershipStartRequest extends AbstractPaymentRequest
{
    public MembershipStartRequest(final PaymentInformation paymentInformation) {
        super(paymentInformation);
    }
    
    @Override
    public String getApiMethod() {
        return "membership_start";
    }
}
