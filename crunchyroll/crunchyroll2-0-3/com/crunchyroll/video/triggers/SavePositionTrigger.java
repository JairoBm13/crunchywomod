// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.video.triggers;

import com.crunchyroll.crunchyroid.app.Extras;
import com.google.common.base.Optional;
import android.os.Bundle;
import com.crunchyroll.video.fragments.AbstractVideoPlayerFragment;

public final class SavePositionTrigger extends AbstractVideoTrigger
{
    private int mPosition;
    
    public SavePositionTrigger(final AbstractVideoPlayerFragment abstractVideoPlayerFragment, final Bundle bundle, final Optional<Integer> optional) {
        super(abstractVideoPlayerFragment);
        Optional<Integer> optional2 = Optional.absent();
        if (optional.isPresent()) {
            optional2 = Optional.of(optional.get() * 1000);
        }
        Optional<Integer> or = optional2;
        if (bundle != null) {
            or = Extras.getInt(bundle.getBundle("SaveVastPositionTrigger"), "position").or(optional2);
        }
        this.mPosition = or.or(Integer.valueOf(0));
    }
    
    public int getPosition() {
        return this.mPosition;
    }
    
    @Override
    protected void runTrigger(final AbstractVideoPlayerFragment abstractVideoPlayerFragment, final int mPosition) throws Exception {
        this.mPosition = mPosition;
    }
    
    @Override
    public void saveInstanceState(final Bundle bundle) {
        if (bundle == null) {
            return;
        }
        final Bundle bundle2 = new Bundle();
        Extras.putInt(bundle2, "position", Integer.valueOf(this.mPosition));
        bundle.putBundle("SaveVastPositionTrigger", bundle2);
    }
    
    public void setPosition(final int mPosition) {
        this.mPosition = mPosition;
    }
}
