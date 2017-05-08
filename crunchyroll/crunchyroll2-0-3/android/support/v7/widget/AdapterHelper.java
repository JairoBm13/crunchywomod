// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import java.util.List;
import java.util.Collection;
import java.util.Collections;
import android.support.v4.util.Pools;
import java.util.ArrayList;

class AdapterHelper implements OpReorderer.Callback
{
    private static final boolean DEBUG = false;
    static final int POSITION_TYPE_INVISIBLE = 0;
    static final int POSITION_TYPE_NEW_OR_LAID_OUT = 1;
    private static final String TAG = "AHT";
    final Callback mCallback;
    final boolean mDisableRecycler;
    Runnable mOnItemProcessedCallback;
    final OpReorderer mOpReorderer;
    final ArrayList<UpdateOp> mPendingUpdates;
    final ArrayList<UpdateOp> mPostponedList;
    private Pools.Pool<UpdateOp> mUpdateOpPool;
    
    AdapterHelper(final Callback callback) {
        this(callback, false);
    }
    
    AdapterHelper(final Callback mCallback, final boolean mDisableRecycler) {
        this.mUpdateOpPool = new Pools.SimplePool<UpdateOp>(30);
        this.mPendingUpdates = new ArrayList<UpdateOp>();
        this.mPostponedList = new ArrayList<UpdateOp>();
        this.mCallback = mCallback;
        this.mDisableRecycler = mDisableRecycler;
        this.mOpReorderer = new OpReorderer((OpReorderer.Callback)this);
    }
    
    private void applyAdd(final UpdateOp updateOp) {
        this.postponeAndUpdateViewHolders(updateOp);
    }
    
    private void applyMove(final UpdateOp updateOp) {
        this.postponeAndUpdateViewHolders(updateOp);
    }
    
    private void applyRemove(final UpdateOp updateOp) {
        final int positionStart = updateOp.positionStart;
        int n = 0;
        int n2 = updateOp.positionStart + updateOp.itemCount;
        int n3 = -1;
        int n6;
        int n7;
        for (int i = updateOp.positionStart; i < n2; ++i, n = n7, n3 = n6) {
            int n4 = 0;
            int n5 = 0;
            if (this.mCallback.findViewHolder(i) != null || this.canFindInPreLayout(i)) {
                if (n3 == 0) {
                    this.dispatchAndUpdateViewHolders(this.obtainUpdateOp(1, positionStart, n));
                    n5 = 1;
                }
                final boolean b = true;
                n4 = n5;
                n6 = (b ? 1 : 0);
            }
            else {
                if (n3 == 1) {
                    this.postponeAndUpdateViewHolders(this.obtainUpdateOp(1, positionStart, n));
                    n4 = 1;
                }
                n6 = 0;
            }
            if (n4 != 0) {
                i -= n;
                n2 -= n;
                n7 = 1;
            }
            else {
                n7 = n + 1;
            }
        }
        UpdateOp obtainUpdateOp = updateOp;
        if (n != updateOp.itemCount) {
            this.recycleUpdateOp(updateOp);
            obtainUpdateOp = this.obtainUpdateOp(1, positionStart, n);
        }
        if (n3 == 0) {
            this.dispatchAndUpdateViewHolders(obtainUpdateOp);
            return;
        }
        this.postponeAndUpdateViewHolders(obtainUpdateOp);
    }
    
    private void applyUpdate(final UpdateOp updateOp) {
        int positionStart = updateOp.positionStart;
        int n = 0;
        final int positionStart2 = updateOp.positionStart;
        final int itemCount = updateOp.itemCount;
        int n2 = -1;
        int n5;
        for (int i = updateOp.positionStart; i < positionStart2 + itemCount; ++i, n2 = n5) {
            int n3;
            if (this.mCallback.findViewHolder(i) != null || this.canFindInPreLayout(i)) {
                n3 = n;
                int n4 = positionStart;
                if (n2 == 0) {
                    this.dispatchAndUpdateViewHolders(this.obtainUpdateOp(2, positionStart, n));
                    n3 = 0;
                    n4 = i;
                }
                n5 = 1;
                positionStart = n4;
            }
            else {
                n3 = n;
                int n6 = positionStart;
                if (n2 == 1) {
                    this.postponeAndUpdateViewHolders(this.obtainUpdateOp(2, positionStart, n));
                    n3 = 0;
                    n6 = i;
                }
                final boolean b = false;
                positionStart = n6;
                n5 = (b ? 1 : 0);
            }
            n = n3 + 1;
        }
        UpdateOp obtainUpdateOp = updateOp;
        if (n != updateOp.itemCount) {
            this.recycleUpdateOp(updateOp);
            obtainUpdateOp = this.obtainUpdateOp(2, positionStart, n);
        }
        if (n2 == 0) {
            this.dispatchAndUpdateViewHolders(obtainUpdateOp);
            return;
        }
        this.postponeAndUpdateViewHolders(obtainUpdateOp);
    }
    
    private boolean canFindInPreLayout(final int n) {
        for (int size = this.mPostponedList.size(), i = 0; i < size; ++i) {
            final UpdateOp updateOp = this.mPostponedList.get(i);
            if (updateOp.cmd == 3) {
                if (this.findPositionOffset(updateOp.itemCount, i + 1) == n) {
                    return true;
                }
            }
            else if (updateOp.cmd == 0) {
                for (int positionStart = updateOp.positionStart, itemCount = updateOp.itemCount, j = updateOp.positionStart; j < positionStart + itemCount; ++j) {
                    if (this.findPositionOffset(j, i + 1) == n) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private void dispatchAndUpdateViewHolders(UpdateOp obtainUpdateOp) {
        if (obtainUpdateOp.cmd == 0 || obtainUpdateOp.cmd == 3) {
            throw new IllegalArgumentException("should not dispatch add or move for pre layout");
        }
        int updatePositionWithPostponed = this.updatePositionWithPostponed(obtainUpdateOp.positionStart, obtainUpdateOp.cmd);
        int n = 1;
        int positionStart = obtainUpdateOp.positionStart;
        int n2 = 0;
        switch (obtainUpdateOp.cmd) {
            default: {
                throw new IllegalArgumentException("op should be remove or update." + obtainUpdateOp);
            }
            case 2: {
                n2 = 1;
                break;
            }
            case 1: {
                n2 = 0;
                break;
            }
        }
        int n4;
        for (int i = 1; i < obtainUpdateOp.itemCount; ++i, n = n4) {
            final int updatePositionWithPostponed2 = this.updatePositionWithPostponed(obtainUpdateOp.positionStart + n2 * i, obtainUpdateOp.cmd);
            int n3 = 0;
            switch (obtainUpdateOp.cmd) {
                case 2: {
                    if (updatePositionWithPostponed2 == updatePositionWithPostponed + 1) {
                        n3 = 1;
                    }
                    else {
                        n3 = 0;
                    }
                    break;
                }
                case 1: {
                    if (updatePositionWithPostponed2 == updatePositionWithPostponed) {
                        n3 = 1;
                    }
                    else {
                        n3 = 0;
                    }
                    break;
                }
            }
            if (n3 != 0) {
                n4 = n + 1;
            }
            else {
                final UpdateOp obtainUpdateOp2 = this.obtainUpdateOp(obtainUpdateOp.cmd, updatePositionWithPostponed, n);
                this.dispatchFirstPassAndUpdateViewHolders(obtainUpdateOp2, positionStart);
                this.recycleUpdateOp(obtainUpdateOp2);
                int n5 = positionStart;
                if (obtainUpdateOp.cmd == 2) {
                    n5 = positionStart + n;
                }
                updatePositionWithPostponed = updatePositionWithPostponed2;
                final int n6 = 1;
                positionStart = n5;
                n4 = n6;
            }
        }
        this.recycleUpdateOp(obtainUpdateOp);
        if (n > 0) {
            obtainUpdateOp = this.obtainUpdateOp(obtainUpdateOp.cmd, updatePositionWithPostponed, n);
            this.dispatchFirstPassAndUpdateViewHolders(obtainUpdateOp, positionStart);
            this.recycleUpdateOp(obtainUpdateOp);
        }
    }
    
    private void postponeAndUpdateViewHolders(final UpdateOp updateOp) {
        this.mPostponedList.add(updateOp);
        switch (updateOp.cmd) {
            default: {
                throw new IllegalArgumentException("Unknown update op type for " + updateOp);
            }
            case 0: {
                this.mCallback.offsetPositionsForAdd(updateOp.positionStart, updateOp.itemCount);
            }
            case 3: {
                this.mCallback.offsetPositionsForMove(updateOp.positionStart, updateOp.itemCount);
            }
            case 1: {
                this.mCallback.offsetPositionsForRemovingLaidOutOrNewView(updateOp.positionStart, updateOp.itemCount);
            }
            case 2: {
                this.mCallback.markViewHoldersUpdated(updateOp.positionStart, updateOp.itemCount);
            }
        }
    }
    
    private int updatePositionWithPostponed(int i, final int n) {
        int j = this.mPostponedList.size() - 1;
        int n2 = i;
        while (j >= 0) {
            final UpdateOp updateOp = this.mPostponedList.get(j);
            if (updateOp.cmd == 3) {
                int n3;
                if (updateOp.positionStart < updateOp.itemCount) {
                    n3 = updateOp.positionStart;
                    i = updateOp.itemCount;
                }
                else {
                    n3 = updateOp.itemCount;
                    i = updateOp.positionStart;
                }
                if (n2 >= n3 && n2 <= i) {
                    if (n3 == updateOp.positionStart) {
                        if (n == 0) {
                            ++updateOp.itemCount;
                        }
                        else if (n == 1) {
                            --updateOp.itemCount;
                        }
                        i = n2 + 1;
                    }
                    else {
                        if (n == 0) {
                            ++updateOp.positionStart;
                        }
                        else if (n == 1) {
                            --updateOp.positionStart;
                        }
                        i = n2 - 1;
                    }
                }
                else if ((i = n2) < updateOp.positionStart) {
                    if (n == 0) {
                        ++updateOp.positionStart;
                        ++updateOp.itemCount;
                        i = n2;
                    }
                    else {
                        i = n2;
                        if (n == 1) {
                            --updateOp.positionStart;
                            --updateOp.itemCount;
                            i = n2;
                        }
                    }
                }
            }
            else if (updateOp.positionStart <= n2) {
                if (updateOp.cmd == 0) {
                    i = n2 - updateOp.itemCount;
                }
                else {
                    i = n2;
                    if (updateOp.cmd == 1) {
                        i = n2 + updateOp.itemCount;
                    }
                }
            }
            else if (n == 0) {
                ++updateOp.positionStart;
                i = n2;
            }
            else {
                i = n2;
                if (n == 1) {
                    --updateOp.positionStart;
                    i = n2;
                }
            }
            --j;
            n2 = i;
        }
        UpdateOp updateOp2;
        for (i = this.mPostponedList.size() - 1; i >= 0; --i) {
            updateOp2 = this.mPostponedList.get(i);
            if (updateOp2.cmd == 3) {
                if (updateOp2.itemCount == updateOp2.positionStart || updateOp2.itemCount < 0) {
                    this.mPostponedList.remove(i);
                    this.recycleUpdateOp(updateOp2);
                }
            }
            else if (updateOp2.itemCount <= 0) {
                this.mPostponedList.remove(i);
                this.recycleUpdateOp(updateOp2);
            }
        }
        return n2;
    }
    
    AdapterHelper addUpdateOp(final UpdateOp... array) {
        Collections.addAll(this.mPendingUpdates, array);
        return this;
    }
    
    public int applyPendingUpdatesToPosition(int itemCount) {
        final int size = this.mPendingUpdates.size();
        int n = 0;
        int n2 = itemCount;
    Label_0078_Outer:
        while (true) {
            itemCount = n2;
            if (n >= size) {
                break;
            }
            final UpdateOp updateOp = this.mPendingUpdates.get(n);
            itemCount = n2;
            while (true) {
                switch (updateOp.cmd) {
                    default: {
                        itemCount = n2;
                    }
                    case 2: {
                        ++n;
                        n2 = itemCount;
                        continue Label_0078_Outer;
                    }
                    case 0: {
                        itemCount = n2;
                        if (updateOp.positionStart <= n2) {
                            itemCount = n2 + updateOp.itemCount;
                        }
                        continue;
                    }
                    case 1: {
                        itemCount = n2;
                        if (updateOp.positionStart > n2) {
                            continue;
                        }
                        if (updateOp.positionStart + updateOp.itemCount > n2) {
                            itemCount = -1;
                            break Label_0078_Outer;
                        }
                        itemCount = n2 - updateOp.itemCount;
                        continue;
                    }
                    case 3: {
                        if (updateOp.positionStart == n2) {
                            itemCount = updateOp.itemCount;
                            continue;
                        }
                        int n3;
                        if (updateOp.positionStart < (n3 = n2)) {
                            n3 = n2 - 1;
                        }
                        if (updateOp.itemCount <= (itemCount = n3)) {
                            itemCount = n3 + 1;
                        }
                        continue;
                    }
                }
                break;
            }
        }
        return itemCount;
    }
    
    void consumePostponedUpdates() {
        for (int size = this.mPostponedList.size(), i = 0; i < size; ++i) {
            this.mCallback.onDispatchSecondPass((UpdateOp)this.mPostponedList.get(i));
        }
        this.recycleUpdateOpsAndClearList(this.mPostponedList);
    }
    
    void consumeUpdatesInOnePass() {
        this.consumePostponedUpdates();
        for (int size = this.mPendingUpdates.size(), i = 0; i < size; ++i) {
            final UpdateOp updateOp = this.mPendingUpdates.get(i);
            switch (updateOp.cmd) {
                case 0: {
                    this.mCallback.onDispatchSecondPass(updateOp);
                    this.mCallback.offsetPositionsForAdd(updateOp.positionStart, updateOp.itemCount);
                    break;
                }
                case 1: {
                    this.mCallback.onDispatchSecondPass(updateOp);
                    this.mCallback.offsetPositionsForRemovingInvisible(updateOp.positionStart, updateOp.itemCount);
                    break;
                }
                case 2: {
                    this.mCallback.onDispatchSecondPass(updateOp);
                    this.mCallback.markViewHoldersUpdated(updateOp.positionStart, updateOp.itemCount);
                    break;
                }
                case 3: {
                    this.mCallback.onDispatchSecondPass(updateOp);
                    this.mCallback.offsetPositionsForMove(updateOp.positionStart, updateOp.itemCount);
                    break;
                }
            }
            if (this.mOnItemProcessedCallback != null) {
                this.mOnItemProcessedCallback.run();
            }
        }
        this.recycleUpdateOpsAndClearList(this.mPendingUpdates);
    }
    
    void dispatchFirstPassAndUpdateViewHolders(final UpdateOp updateOp, final int n) {
        this.mCallback.onDispatchFirstPass(updateOp);
        switch (updateOp.cmd) {
            default: {
                throw new IllegalArgumentException("only remove and update ops can be dispatched in first pass");
            }
            case 1: {
                this.mCallback.offsetPositionsForRemovingInvisible(n, updateOp.itemCount);
            }
            case 2: {
                this.mCallback.markViewHoldersUpdated(n, updateOp.itemCount);
            }
        }
    }
    
    int findPositionOffset(final int n) {
        return this.findPositionOffset(n, 0);
    }
    
    int findPositionOffset(int itemCount, int n) {
        final int size = this.mPostponedList.size();
        int n2 = n;
        n = itemCount;
        while (true) {
            itemCount = n;
            if (n2 >= size) {
                break;
            }
            final UpdateOp updateOp = this.mPostponedList.get(n2);
            if (updateOp.cmd == 3) {
                if (updateOp.positionStart == n) {
                    itemCount = updateOp.itemCount;
                }
                else {
                    int n3;
                    if (updateOp.positionStart < (n3 = n)) {
                        n3 = n - 1;
                    }
                    if (updateOp.itemCount <= (itemCount = n3)) {
                        itemCount = n3 + 1;
                    }
                }
            }
            else if (updateOp.positionStart <= (itemCount = n)) {
                if (updateOp.cmd == 1) {
                    if (n < updateOp.positionStart + updateOp.itemCount) {
                        itemCount = -1;
                        break;
                    }
                    itemCount = n - updateOp.itemCount;
                }
                else {
                    itemCount = n;
                    if (updateOp.cmd == 0) {
                        itemCount = n + updateOp.itemCount;
                    }
                }
            }
            ++n2;
            n = itemCount;
        }
        return itemCount;
    }
    
    boolean hasPendingUpdates() {
        return this.mPendingUpdates.size() > 0;
    }
    
    @Override
    public UpdateOp obtainUpdateOp(final int cmd, final int positionStart, final int itemCount) {
        final UpdateOp updateOp = this.mUpdateOpPool.acquire();
        if (updateOp == null) {
            return new UpdateOp(cmd, positionStart, itemCount);
        }
        updateOp.cmd = cmd;
        updateOp.positionStart = positionStart;
        updateOp.itemCount = itemCount;
        return updateOp;
    }
    
    boolean onItemRangeChanged(final int n, final int n2) {
        this.mPendingUpdates.add(this.obtainUpdateOp(2, n, n2));
        return this.mPendingUpdates.size() == 1;
    }
    
    boolean onItemRangeInserted(final int n, final int n2) {
        this.mPendingUpdates.add(this.obtainUpdateOp(0, n, n2));
        return this.mPendingUpdates.size() == 1;
    }
    
    boolean onItemRangeMoved(final int n, final int n2, final int n3) {
        boolean b = true;
        if (n == n2) {
            return false;
        }
        if (n3 != 1) {
            throw new IllegalArgumentException("Moving more than 1 item is not supported yet");
        }
        this.mPendingUpdates.add(this.obtainUpdateOp(3, n, n2));
        if (this.mPendingUpdates.size() != 1) {
            b = false;
        }
        return b;
    }
    
    boolean onItemRangeRemoved(final int n, final int n2) {
        this.mPendingUpdates.add(this.obtainUpdateOp(1, n, n2));
        return this.mPendingUpdates.size() == 1;
    }
    
    void preProcess() {
        this.mOpReorderer.reorderOps(this.mPendingUpdates);
        for (int size = this.mPendingUpdates.size(), i = 0; i < size; ++i) {
            final UpdateOp updateOp = this.mPendingUpdates.get(i);
            switch (updateOp.cmd) {
                case 0: {
                    this.applyAdd(updateOp);
                    break;
                }
                case 1: {
                    this.applyRemove(updateOp);
                    break;
                }
                case 2: {
                    this.applyUpdate(updateOp);
                    break;
                }
                case 3: {
                    this.applyMove(updateOp);
                    break;
                }
            }
            if (this.mOnItemProcessedCallback != null) {
                this.mOnItemProcessedCallback.run();
            }
        }
        this.mPendingUpdates.clear();
    }
    
    @Override
    public void recycleUpdateOp(final UpdateOp updateOp) {
        if (!this.mDisableRecycler) {
            this.mUpdateOpPool.release(updateOp);
        }
    }
    
    void recycleUpdateOpsAndClearList(final List<UpdateOp> list) {
        for (int size = list.size(), i = 0; i < size; ++i) {
            this.recycleUpdateOp(list.get(i));
        }
        list.clear();
    }
    
    void reset() {
        this.recycleUpdateOpsAndClearList(this.mPendingUpdates);
        this.recycleUpdateOpsAndClearList(this.mPostponedList);
    }
    
    interface Callback
    {
        RecyclerView.ViewHolder findViewHolder(final int p0);
        
        void markViewHoldersUpdated(final int p0, final int p1);
        
        void offsetPositionsForAdd(final int p0, final int p1);
        
        void offsetPositionsForMove(final int p0, final int p1);
        
        void offsetPositionsForRemovingInvisible(final int p0, final int p1);
        
        void offsetPositionsForRemovingLaidOutOrNewView(final int p0, final int p1);
        
        void onDispatchFirstPass(final UpdateOp p0);
        
        void onDispatchSecondPass(final UpdateOp p0);
    }
    
    static class UpdateOp
    {
        static final int ADD = 0;
        static final int MOVE = 3;
        static final int POOL_SIZE = 30;
        static final int REMOVE = 1;
        static final int UPDATE = 2;
        int cmd;
        int itemCount;
        int positionStart;
        
        UpdateOp(final int cmd, final int positionStart, final int itemCount) {
            this.cmd = cmd;
            this.positionStart = positionStart;
            this.itemCount = itemCount;
        }
        
        String cmdToString() {
            switch (this.cmd) {
                default: {
                    return "??";
                }
                case 0: {
                    return "add";
                }
                case 1: {
                    return "rm";
                }
                case 2: {
                    return "up";
                }
                case 3: {
                    return "mv";
                }
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this != o) {
                if (o == null || this.getClass() != o.getClass()) {
                    return false;
                }
                final UpdateOp updateOp = (UpdateOp)o;
                if (this.cmd != updateOp.cmd) {
                    return false;
                }
                if (this.cmd != 3 || Math.abs(this.itemCount - this.positionStart) != 1 || this.itemCount != updateOp.positionStart || this.positionStart != updateOp.itemCount) {
                    if (this.itemCount != updateOp.itemCount) {
                        return false;
                    }
                    if (this.positionStart != updateOp.positionStart) {
                        return false;
                    }
                }
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            return (this.cmd * 31 + this.positionStart) * 31 + this.itemCount;
        }
        
        @Override
        public String toString() {
            return "[" + this.cmdToString() + ",s:" + this.positionStart + "c:" + this.itemCount + "]";
        }
    }
}
