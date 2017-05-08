// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.requests.MembershipInformationRequest;
import android.content.Context;
import com.crunchyroll.android.api.models.MembershipInfoItem;
import java.util.List;

public class MembershipInfoTask extends BaseTask<List<MembershipInfoItem>>
{
    public MembershipInfoTask(final Context context) {
        super(context);
    }
    
    @Override
    public List<MembershipInfoItem> call() throws Exception {
        return this.parseResponse(this.getApiService().run(new MembershipInformationRequest()), (TypeReference<List<MembershipInfoItem>>)new TypeReference<List<MembershipInfoItem>>() {});
    }
    
    @Override
    protected void onSuccess(final List<MembershipInfoItem> list) throws Exception {
        super.onSuccess(list);
    }
}
