// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.models.PaymentInformation;
import com.crunchyroll.android.api.AbstractApiRequest;

public abstract class AbstractPaymentRequest extends AbstractApiRequest
{
    protected final PaymentInformation mPaymentInformation;
    
    public AbstractPaymentRequest(final PaymentInformation mPaymentInformation) {
        this.mPaymentInformation = mPaymentInformation;
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.POST;
    }
    
    @Override
    public ImmutableMap<String, String> getParams() {
        final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        builder.put("sku", this.mPaymentInformation.getSku());
        builder.put("currency_code", this.mPaymentInformation.getCurrencyCode());
        builder.put("first_name", this.mPaymentInformation.getFirstName());
        builder.put("last_name", this.mPaymentInformation.getLastName());
        builder.put("cc", this.mPaymentInformation.getCardNumber());
        builder.put("exp_month", this.mPaymentInformation.getExpirationMonth().toString());
        builder.put("exp_year", this.mPaymentInformation.getExpirationYear().toString());
        builder.put("zip", this.mPaymentInformation.getZipCode());
        if (this.mPaymentInformation.getSecurityCode() != null) {
            builder.put("cvv", this.mPaymentInformation.getSecurityCode());
        }
        if (this.mPaymentInformation.getAddressLine1() != null) {
            builder.put("address_1", this.mPaymentInformation.getAddressLine1());
        }
        if (this.mPaymentInformation.getAddressLine2() != null) {
            builder.put("address_2", this.mPaymentInformation.getAddressLine2());
        }
        if (this.mPaymentInformation.getCity() != null) {
            builder.put("city", this.mPaymentInformation.getCity());
        }
        if (this.mPaymentInformation.getState() != null) {
            builder.put("state", this.mPaymentInformation.getState());
        }
        if (this.mPaymentInformation.getCountryCode() != null) {
            builder.put("country_code", this.mPaymentInformation.getCountryCode());
        }
        return builder.build();
    }
}
