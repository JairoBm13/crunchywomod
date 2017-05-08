// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.requests.FreeTrialInformationRequest;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.base.Predicates;
import com.google.common.base.Predicate;
import android.content.Context;
import com.crunchyroll.android.api.models.FreeTrialInformationItem;
import java.util.List;

public class FreeTrialInfoTask extends BaseTask<List<FreeTrialInformationItem>>
{
    public FreeTrialInfoTask(final Context context) {
        super(context);
    }
    
    private List<FreeTrialInformationItem> getEligibilePlanOrThrow(final List<FreeTrialInformationItem> list) {
        final ImmutableList<FreeTrialInformationItem> copy = (ImmutableList<FreeTrialInformationItem>)ImmutableList.copyOf((Iterable<?>)Iterables.filter((Iterable<? extends E>)list, Predicates.and((Predicate<? super E>)new Predicate<FreeTrialInformationItem>() {
            @Override
            public boolean apply(final FreeTrialInformationItem freeTrialInformationItem) {
                return freeTrialInformationItem.getMediaType().contains("anime");
            }
        }, (Predicate<? super E>)new Predicate<FreeTrialInformationItem>() {
            @Override
            public boolean apply(final FreeTrialInformationItem freeTrialInformationItem) {
                if (freeTrialInformationItem == null || freeTrialInformationItem.getRecurringPrice() == null) {
                    throw new IllegalStateException("Empty trial or recurring price");
                }
                return freeTrialInformationItem.getEligible() == null || freeTrialInformationItem.getEligible();
            }
        })));
        if (copy.size() < 1) {
            throw new IneligibleException();
        }
        return copy;
    }
    
    @Override
    public List<FreeTrialInformationItem> call() throws Exception {
        return this.parseResponse(this.getApiService().run(new FreeTrialInformationRequest()), (TypeReference<List<FreeTrialInformationItem>>)new TypeReference<List<FreeTrialInformationItem>>() {});
    }
    
    @Override
    protected void onSuccess(final List<FreeTrialInformationItem> list) throws Exception {
        super.onSuccess(this.getEligibilePlanOrThrow(list));
    }
    
    private static class IneligibleException extends RuntimeException
    {
        private static final long serialVersionUID = 6611685125042088748L;
    }
}
