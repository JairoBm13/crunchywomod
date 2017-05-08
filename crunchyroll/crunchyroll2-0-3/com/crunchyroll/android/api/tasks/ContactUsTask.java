// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.requests.ContactUsRequest;
import android.content.Context;

public class ContactUsTask extends BaseTask<Void>
{
    private String mDescription;
    private String mEmail;
    private Boolean mReceiveResponse;
    private String mSubject;
    private String mType;
    
    public ContactUsTask(final Context context, final String mEmail, final String mSubject, final String mDescription, final String mType, final Boolean mReceiveResponse) {
        super(context);
        this.mEmail = mEmail;
        this.mSubject = mSubject;
        this.mType = mType;
        this.mDescription = mDescription;
        this.mReceiveResponse = mReceiveResponse;
    }
    
    @Override
    public Void call() throws Exception {
        this.getApiService().run(new ContactUsRequest(this.mEmail, this.mSubject, this.mType, this.mDescription, this.mReceiveResponse));
        return null;
    }
}
