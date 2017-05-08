// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

public final class PaymentInformation
{
    private String mAddressLine1;
    private String mAddressLine2;
    private String mCardNumber;
    private String mCity;
    private String mCountryCode;
    private String mCurrencyCode;
    private Integer mExpirationMonth;
    private Integer mExpirationYear;
    private String mFirstName;
    private String mLastName;
    private String mSecurityCode;
    private String mSku;
    private String mState;
    private String mZipCode;
    
    public String getAddressLine1() {
        return this.mAddressLine1;
    }
    
    public String getAddressLine2() {
        return this.mAddressLine2;
    }
    
    public String getCardNumber() {
        return this.mCardNumber;
    }
    
    public String getCity() {
        return this.mCity;
    }
    
    public String getCountryCode() {
        return this.mCountryCode;
    }
    
    public String getCurrencyCode() {
        return this.mCurrencyCode;
    }
    
    public Integer getExpirationMonth() {
        return this.mExpirationMonth;
    }
    
    public Integer getExpirationYear() {
        return this.mExpirationYear;
    }
    
    public String getFirstName() {
        return this.mFirstName;
    }
    
    public String getLastName() {
        return this.mLastName;
    }
    
    public String getSecurityCode() {
        return this.mSecurityCode;
    }
    
    public String getSku() {
        return this.mSku;
    }
    
    public String getState() {
        return this.mState;
    }
    
    public String getZipCode() {
        return this.mZipCode;
    }
    
    public void setAddressLine1(final String mAddressLine1) {
        this.mAddressLine1 = mAddressLine1;
    }
    
    public void setAddressLine2(final String mAddressLine2) {
        this.mAddressLine2 = mAddressLine2;
    }
    
    public void setCardNumber(final String mCardNumber) {
        this.mCardNumber = mCardNumber;
    }
    
    public void setCity(final String mCity) {
        this.mCity = mCity;
    }
    
    public void setCountryCode(final String mCountryCode) {
        this.mCountryCode = mCountryCode;
    }
    
    public void setCurrencyCode(final String mCurrencyCode) {
        this.mCurrencyCode = mCurrencyCode;
    }
    
    public void setExpirationMonth(final Integer mExpirationMonth) {
        this.mExpirationMonth = mExpirationMonth;
    }
    
    public void setExpirationYear(final Integer mExpirationYear) {
        this.mExpirationYear = mExpirationYear;
    }
    
    public void setFirstName(final String mFirstName) {
        this.mFirstName = mFirstName;
    }
    
    public void setLastName(final String mLastName) {
        this.mLastName = mLastName;
    }
    
    public void setSecurityCode(final String mSecurityCode) {
        this.mSecurityCode = mSecurityCode;
    }
    
    public void setSku(final String mSku) {
        this.mSku = mSku;
    }
    
    public void setState(final String mState) {
        this.mState = mState;
    }
    
    public void setZipCode(final String mZipCode) {
        this.mZipCode = mZipCode;
    }
    
    public static final class Builder
    {
        private final PaymentInformation mPaymentInformation;
        
        public Builder() {
            this.mPaymentInformation = new PaymentInformation(null);
        }
        
        private void validate() {
            if (this.mPaymentInformation.getSku() == null) {
                throw new NullPointerException("sku cannot be null");
            }
            if (this.mPaymentInformation.getCurrencyCode() == null) {
                throw new NullPointerException("currencyCode cannot be null");
            }
            if (this.mPaymentInformation.getFirstName() == null) {
                throw new NullPointerException("firstName cannot be null");
            }
            if (this.mPaymentInformation.getLastName() == null) {
                throw new NullPointerException("lastName cannot be null");
            }
            if (this.mPaymentInformation.getCardNumber() == null) {
                throw new NullPointerException("cardNumber cannot be null");
            }
            if (this.mPaymentInformation.getExpirationMonth() == null) {
                throw new NullPointerException("expirationMonth cannot be null");
            }
            if (this.mPaymentInformation.getExpirationYear() == null) {
                throw new NullPointerException("expirationYear cannot be null");
            }
            if (this.mPaymentInformation.getZipCode() == null) {
                throw new NullPointerException("zipCode cannot be null");
            }
        }
        
        public Builder addressLine1(final String addressLine1) {
            this.mPaymentInformation.setAddressLine1(addressLine1);
            return this;
        }
        
        public Builder addressLine2(final String addressLine2) {
            this.mPaymentInformation.setAddressLine2(addressLine2);
            return this;
        }
        
        public PaymentInformation build() {
            this.validate();
            return this.mPaymentInformation;
        }
        
        public Builder cardNumber(final String cardNumber) {
            this.mPaymentInformation.setCardNumber(cardNumber);
            return this;
        }
        
        public Builder city(final String city) {
            this.mPaymentInformation.setCity(city);
            return this;
        }
        
        public Builder countryCode(final String countryCode) {
            this.mPaymentInformation.setCountryCode(countryCode);
            return this;
        }
        
        public Builder currencyCode(final String currencyCode) {
            this.mPaymentInformation.setCurrencyCode(currencyCode);
            return this;
        }
        
        public Builder expirationMonth(final Integer expirationMonth) {
            this.mPaymentInformation.setExpirationMonth(expirationMonth);
            return this;
        }
        
        public Builder expirationYear(final Integer expirationYear) {
            this.mPaymentInformation.setExpirationYear(expirationYear);
            return this;
        }
        
        public Builder firstName(final String firstName) {
            this.mPaymentInformation.setFirstName(firstName);
            return this;
        }
        
        public Builder lastName(final String lastName) {
            this.mPaymentInformation.setLastName(lastName);
            return this;
        }
        
        public Builder securityCode(final String securityCode) {
            this.mPaymentInformation.setSecurityCode(securityCode);
            return this;
        }
        
        public Builder sku(final String sku) {
            this.mPaymentInformation.setSku(sku);
            return this;
        }
        
        public Builder state(final String state) {
            this.mPaymentInformation.setState(state);
            return this;
        }
        
        public Builder zipCode(final String zipCode) {
            this.mPaymentInformation.setZipCode(zipCode);
            return this;
        }
    }
}
