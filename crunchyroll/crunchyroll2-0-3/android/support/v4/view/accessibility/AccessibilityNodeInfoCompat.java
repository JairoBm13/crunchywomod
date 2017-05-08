// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view.accessibility;

import android.os.Bundle;
import android.graphics.Rect;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.os.Build$VERSION;

public class AccessibilityNodeInfoCompat
{
    public static final int ACTION_ACCESSIBILITY_FOCUS = 64;
    public static final String ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN = "ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN";
    public static final String ACTION_ARGUMENT_HTML_ELEMENT_STRING = "ACTION_ARGUMENT_HTML_ELEMENT_STRING";
    public static final String ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT = "ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT";
    public static final String ACTION_ARGUMENT_SELECTION_END_INT = "ACTION_ARGUMENT_SELECTION_END_INT";
    public static final String ACTION_ARGUMENT_SELECTION_START_INT = "ACTION_ARGUMENT_SELECTION_START_INT";
    public static final String ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE = "ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE";
    public static final int ACTION_CLEAR_ACCESSIBILITY_FOCUS = 128;
    public static final int ACTION_CLEAR_FOCUS = 2;
    public static final int ACTION_CLEAR_SELECTION = 8;
    public static final int ACTION_CLICK = 16;
    public static final int ACTION_COPY = 16384;
    public static final int ACTION_CUT = 65536;
    public static final int ACTION_FOCUS = 1;
    public static final int ACTION_LONG_CLICK = 32;
    public static final int ACTION_NEXT_AT_MOVEMENT_GRANULARITY = 256;
    public static final int ACTION_NEXT_HTML_ELEMENT = 1024;
    public static final int ACTION_PASTE = 32768;
    public static final int ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = 512;
    public static final int ACTION_PREVIOUS_HTML_ELEMENT = 2048;
    public static final int ACTION_SCROLL_BACKWARD = 8192;
    public static final int ACTION_SCROLL_FORWARD = 4096;
    public static final int ACTION_SELECT = 4;
    public static final int ACTION_SET_SELECTION = 131072;
    public static final int ACTION_SET_TEXT = 2097152;
    public static final int FOCUS_ACCESSIBILITY = 2;
    public static final int FOCUS_INPUT = 1;
    private static final AccessibilityNodeInfoImpl IMPL;
    public static final int MOVEMENT_GRANULARITY_CHARACTER = 1;
    public static final int MOVEMENT_GRANULARITY_LINE = 4;
    public static final int MOVEMENT_GRANULARITY_PAGE = 16;
    public static final int MOVEMENT_GRANULARITY_PARAGRAPH = 8;
    public static final int MOVEMENT_GRANULARITY_WORD = 2;
    private final Object mInfo;
    
    static {
        if (Build$VERSION.SDK_INT >= 22) {
            IMPL = (AccessibilityNodeInfoImpl)new AccessibilityNodeInfoApi22Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            IMPL = (AccessibilityNodeInfoImpl)new AccessibilityNodeInfoApi21Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 19) {
            IMPL = (AccessibilityNodeInfoImpl)new AccessibilityNodeInfoKitKatImpl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 18) {
            IMPL = (AccessibilityNodeInfoImpl)new AccessibilityNodeInfoJellybeanMr2Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 16) {
            IMPL = (AccessibilityNodeInfoImpl)new AccessibilityNodeInfoJellybeanImpl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 14) {
            IMPL = (AccessibilityNodeInfoImpl)new AccessibilityNodeInfoIcsImpl();
            return;
        }
        IMPL = (AccessibilityNodeInfoImpl)new AccessibilityNodeInfoStubImpl();
    }
    
    public AccessibilityNodeInfoCompat(final Object mInfo) {
        this.mInfo = mInfo;
    }
    
    private static String getActionSymbolicName(final int n) {
        switch (n) {
            default: {
                return "ACTION_UNKNOWN";
            }
            case 1: {
                return "ACTION_FOCUS";
            }
            case 2: {
                return "ACTION_CLEAR_FOCUS";
            }
            case 4: {
                return "ACTION_SELECT";
            }
            case 8: {
                return "ACTION_CLEAR_SELECTION";
            }
            case 16: {
                return "ACTION_CLICK";
            }
            case 32: {
                return "ACTION_LONG_CLICK";
            }
            case 64: {
                return "ACTION_ACCESSIBILITY_FOCUS";
            }
            case 128: {
                return "ACTION_CLEAR_ACCESSIBILITY_FOCUS";
            }
            case 256: {
                return "ACTION_NEXT_AT_MOVEMENT_GRANULARITY";
            }
            case 512: {
                return "ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY";
            }
            case 1024: {
                return "ACTION_NEXT_HTML_ELEMENT";
            }
            case 2048: {
                return "ACTION_PREVIOUS_HTML_ELEMENT";
            }
            case 4096: {
                return "ACTION_SCROLL_FORWARD";
            }
            case 8192: {
                return "ACTION_SCROLL_BACKWARD";
            }
            case 65536: {
                return "ACTION_CUT";
            }
            case 16384: {
                return "ACTION_COPY";
            }
            case 32768: {
                return "ACTION_PASTE";
            }
            case 131072: {
                return "ACTION_SET_SELECTION";
            }
        }
    }
    
    public static AccessibilityNodeInfoCompat obtain() {
        return wrapNonNullInstance(AccessibilityNodeInfoCompat.IMPL.obtain());
    }
    
    public static AccessibilityNodeInfoCompat obtain(final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        return wrapNonNullInstance(AccessibilityNodeInfoCompat.IMPL.obtain(accessibilityNodeInfoCompat.mInfo));
    }
    
    public static AccessibilityNodeInfoCompat obtain(final View view) {
        return wrapNonNullInstance(AccessibilityNodeInfoCompat.IMPL.obtain(view));
    }
    
    public static AccessibilityNodeInfoCompat obtain(final View view, final int n) {
        return wrapNonNullInstance(AccessibilityNodeInfoCompat.IMPL.obtain(view, n));
    }
    
    static AccessibilityNodeInfoCompat wrapNonNullInstance(final Object o) {
        if (o != null) {
            return new AccessibilityNodeInfoCompat(o);
        }
        return null;
    }
    
    public void addAction(final int n) {
        AccessibilityNodeInfoCompat.IMPL.addAction(this.mInfo, n);
    }
    
    public void addAction(final AccessibilityActionCompat accessibilityActionCompat) {
        AccessibilityNodeInfoCompat.IMPL.addAction(this.mInfo, accessibilityActionCompat.mAction);
    }
    
    public void addChild(final View view) {
        AccessibilityNodeInfoCompat.IMPL.addChild(this.mInfo, view);
    }
    
    public void addChild(final View view, final int n) {
        AccessibilityNodeInfoCompat.IMPL.addChild(this.mInfo, view, n);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = (AccessibilityNodeInfoCompat)o;
            if (this.mInfo == null) {
                if (accessibilityNodeInfoCompat.mInfo != null) {
                    return false;
                }
            }
            else if (!this.mInfo.equals(accessibilityNodeInfoCompat.mInfo)) {
                return false;
            }
        }
        return true;
    }
    
    public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText(final String s) {
        final ArrayList<AccessibilityNodeInfoCompat> list = new ArrayList<AccessibilityNodeInfoCompat>();
        final List<Object> accessibilityNodeInfosByText = AccessibilityNodeInfoCompat.IMPL.findAccessibilityNodeInfosByText(this.mInfo, s);
        for (int size = accessibilityNodeInfosByText.size(), i = 0; i < size; ++i) {
            list.add(new AccessibilityNodeInfoCompat(accessibilityNodeInfosByText.get(i)));
        }
        return list;
    }
    
    public AccessibilityNodeInfoCompat findFocus(final int n) {
        return wrapNonNullInstance(AccessibilityNodeInfoCompat.IMPL.findFocus(this.mInfo, n));
    }
    
    public AccessibilityNodeInfoCompat focusSearch(final int n) {
        return wrapNonNullInstance(AccessibilityNodeInfoCompat.IMPL.focusSearch(this.mInfo, n));
    }
    
    public List<AccessibilityActionCompat> getActionList() {
        final List<Object> actionList = AccessibilityNodeInfoCompat.IMPL.getActionList(this.mInfo);
        List<AccessibilityActionCompat> emptyList;
        if (actionList != null) {
            final ArrayList<AccessibilityActionCompat> list = new ArrayList<AccessibilityActionCompat>();
            final int size = actionList.size();
            int n = 0;
            while (true) {
                emptyList = list;
                if (n >= size) {
                    break;
                }
                list.add(new AccessibilityActionCompat(actionList.get(n)));
                ++n;
            }
        }
        else {
            emptyList = Collections.emptyList();
        }
        return emptyList;
    }
    
    public int getActions() {
        return AccessibilityNodeInfoCompat.IMPL.getActions(this.mInfo);
    }
    
    public void getBoundsInParent(final Rect rect) {
        AccessibilityNodeInfoCompat.IMPL.getBoundsInParent(this.mInfo, rect);
    }
    
    public void getBoundsInScreen(final Rect rect) {
        AccessibilityNodeInfoCompat.IMPL.getBoundsInScreen(this.mInfo, rect);
    }
    
    public AccessibilityNodeInfoCompat getChild(final int n) {
        return wrapNonNullInstance(AccessibilityNodeInfoCompat.IMPL.getChild(this.mInfo, n));
    }
    
    public int getChildCount() {
        return AccessibilityNodeInfoCompat.IMPL.getChildCount(this.mInfo);
    }
    
    public CharSequence getClassName() {
        return AccessibilityNodeInfoCompat.IMPL.getClassName(this.mInfo);
    }
    
    public CollectionInfoCompat getCollectionInfo() {
        final Object collectionInfo = AccessibilityNodeInfoCompat.IMPL.getCollectionInfo(this.mInfo);
        if (collectionInfo == null) {
            return null;
        }
        return new CollectionInfoCompat(collectionInfo);
    }
    
    public CollectionItemInfoCompat getCollectionItemInfo() {
        final Object collectionItemInfo = AccessibilityNodeInfoCompat.IMPL.getCollectionItemInfo(this.mInfo);
        if (collectionItemInfo == null) {
            return null;
        }
        return new CollectionItemInfoCompat(collectionItemInfo);
    }
    
    public CharSequence getContentDescription() {
        return AccessibilityNodeInfoCompat.IMPL.getContentDescription(this.mInfo);
    }
    
    public CharSequence getError() {
        return AccessibilityNodeInfoCompat.IMPL.getError(this.mInfo);
    }
    
    public Object getInfo() {
        return this.mInfo;
    }
    
    public int getLiveRegion() {
        return AccessibilityNodeInfoCompat.IMPL.getLiveRegion(this.mInfo);
    }
    
    public int getMovementGranularities() {
        return AccessibilityNodeInfoCompat.IMPL.getMovementGranularities(this.mInfo);
    }
    
    public CharSequence getPackageName() {
        return AccessibilityNodeInfoCompat.IMPL.getPackageName(this.mInfo);
    }
    
    public AccessibilityNodeInfoCompat getParent() {
        return wrapNonNullInstance(AccessibilityNodeInfoCompat.IMPL.getParent(this.mInfo));
    }
    
    public RangeInfoCompat getRangeInfo() {
        final Object rangeInfo = AccessibilityNodeInfoCompat.IMPL.getRangeInfo(this.mInfo);
        if (rangeInfo == null) {
            return null;
        }
        return new RangeInfoCompat(rangeInfo);
    }
    
    public CharSequence getText() {
        return AccessibilityNodeInfoCompat.IMPL.getText(this.mInfo);
    }
    
    public String getViewIdResourceName() {
        return AccessibilityNodeInfoCompat.IMPL.getViewIdResourceName(this.mInfo);
    }
    
    public int getWindowId() {
        return AccessibilityNodeInfoCompat.IMPL.getWindowId(this.mInfo);
    }
    
    @Override
    public int hashCode() {
        if (this.mInfo == null) {
            return 0;
        }
        return this.mInfo.hashCode();
    }
    
    public boolean isAccessibilityFocused() {
        return AccessibilityNodeInfoCompat.IMPL.isAccessibilityFocused(this.mInfo);
    }
    
    public boolean isCheckable() {
        return AccessibilityNodeInfoCompat.IMPL.isCheckable(this.mInfo);
    }
    
    public boolean isChecked() {
        return AccessibilityNodeInfoCompat.IMPL.isChecked(this.mInfo);
    }
    
    public boolean isClickable() {
        return AccessibilityNodeInfoCompat.IMPL.isClickable(this.mInfo);
    }
    
    public boolean isContentInvalid() {
        return AccessibilityNodeInfoCompat.IMPL.isContentInvalid(this.mInfo);
    }
    
    public boolean isEnabled() {
        return AccessibilityNodeInfoCompat.IMPL.isEnabled(this.mInfo);
    }
    
    public boolean isFocusable() {
        return AccessibilityNodeInfoCompat.IMPL.isFocusable(this.mInfo);
    }
    
    public boolean isFocused() {
        return AccessibilityNodeInfoCompat.IMPL.isFocused(this.mInfo);
    }
    
    public boolean isLongClickable() {
        return AccessibilityNodeInfoCompat.IMPL.isLongClickable(this.mInfo);
    }
    
    public boolean isPassword() {
        return AccessibilityNodeInfoCompat.IMPL.isPassword(this.mInfo);
    }
    
    public boolean isScrollable() {
        return AccessibilityNodeInfoCompat.IMPL.isScrollable(this.mInfo);
    }
    
    public boolean isSelected() {
        return AccessibilityNodeInfoCompat.IMPL.isSelected(this.mInfo);
    }
    
    public boolean isVisibleToUser() {
        return AccessibilityNodeInfoCompat.IMPL.isVisibleToUser(this.mInfo);
    }
    
    public boolean performAction(final int n) {
        return AccessibilityNodeInfoCompat.IMPL.performAction(this.mInfo, n);
    }
    
    public boolean performAction(final int n, final Bundle bundle) {
        return AccessibilityNodeInfoCompat.IMPL.performAction(this.mInfo, n, bundle);
    }
    
    public void recycle() {
        AccessibilityNodeInfoCompat.IMPL.recycle(this.mInfo);
    }
    
    public void setAccessibilityFocused(final boolean b) {
        AccessibilityNodeInfoCompat.IMPL.setAccessibilityFocused(this.mInfo, b);
    }
    
    public void setBoundsInParent(final Rect rect) {
        AccessibilityNodeInfoCompat.IMPL.setBoundsInParent(this.mInfo, rect);
    }
    
    public void setBoundsInScreen(final Rect rect) {
        AccessibilityNodeInfoCompat.IMPL.setBoundsInScreen(this.mInfo, rect);
    }
    
    public void setCheckable(final boolean b) {
        AccessibilityNodeInfoCompat.IMPL.setCheckable(this.mInfo, b);
    }
    
    public void setChecked(final boolean b) {
        AccessibilityNodeInfoCompat.IMPL.setChecked(this.mInfo, b);
    }
    
    public void setClassName(final CharSequence charSequence) {
        AccessibilityNodeInfoCompat.IMPL.setClassName(this.mInfo, charSequence);
    }
    
    public void setClickable(final boolean b) {
        AccessibilityNodeInfoCompat.IMPL.setClickable(this.mInfo, b);
    }
    
    public void setCollectionInfo(final Object o) {
        AccessibilityNodeInfoCompat.IMPL.setCollectionInfo(this.mInfo, ((CollectionInfoCompat)o).mInfo);
    }
    
    public void setCollectionItemInfo(final Object o) {
        AccessibilityNodeInfoCompat.IMPL.setCollectionItemInfo(this.mInfo, ((CollectionItemInfoCompat)o).mInfo);
    }
    
    public void setContentDescription(final CharSequence charSequence) {
        AccessibilityNodeInfoCompat.IMPL.setContentDescription(this.mInfo, charSequence);
    }
    
    public void setContentInvalid(final boolean b) {
        AccessibilityNodeInfoCompat.IMPL.setContentInvalid(this.mInfo, b);
    }
    
    public void setEnabled(final boolean b) {
        AccessibilityNodeInfoCompat.IMPL.setEnabled(this.mInfo, b);
    }
    
    public void setError(final CharSequence charSequence) {
        AccessibilityNodeInfoCompat.IMPL.setError(this.mInfo, charSequence);
    }
    
    public void setFocusable(final boolean b) {
        AccessibilityNodeInfoCompat.IMPL.setFocusable(this.mInfo, b);
    }
    
    public void setFocused(final boolean b) {
        AccessibilityNodeInfoCompat.IMPL.setFocused(this.mInfo, b);
    }
    
    public void setLabelFor(final View view) {
        AccessibilityNodeInfoCompat.IMPL.setLabelFor(this.mInfo, view);
    }
    
    public void setLabelFor(final View view, final int n) {
        AccessibilityNodeInfoCompat.IMPL.setLabelFor(this.mInfo, view, n);
    }
    
    public void setLiveRegion(final int n) {
        AccessibilityNodeInfoCompat.IMPL.setLiveRegion(this.mInfo, n);
    }
    
    public void setLongClickable(final boolean b) {
        AccessibilityNodeInfoCompat.IMPL.setLongClickable(this.mInfo, b);
    }
    
    public void setMovementGranularities(final int n) {
        AccessibilityNodeInfoCompat.IMPL.setMovementGranularities(this.mInfo, n);
    }
    
    public void setPackageName(final CharSequence charSequence) {
        AccessibilityNodeInfoCompat.IMPL.setPackageName(this.mInfo, charSequence);
    }
    
    public void setParent(final View view) {
        AccessibilityNodeInfoCompat.IMPL.setParent(this.mInfo, view);
    }
    
    public void setParent(final View view, final int n) {
        AccessibilityNodeInfoCompat.IMPL.setParent(this.mInfo, view, n);
    }
    
    public void setPassword(final boolean b) {
        AccessibilityNodeInfoCompat.IMPL.setPassword(this.mInfo, b);
    }
    
    public void setScrollable(final boolean b) {
        AccessibilityNodeInfoCompat.IMPL.setScrollable(this.mInfo, b);
    }
    
    public void setSelected(final boolean b) {
        AccessibilityNodeInfoCompat.IMPL.setSelected(this.mInfo, b);
    }
    
    public void setSource(final View view) {
        AccessibilityNodeInfoCompat.IMPL.setSource(this.mInfo, view);
    }
    
    public void setSource(final View view, final int n) {
        AccessibilityNodeInfoCompat.IMPL.setSource(this.mInfo, view, n);
    }
    
    public void setText(final CharSequence charSequence) {
        AccessibilityNodeInfoCompat.IMPL.setText(this.mInfo, charSequence);
    }
    
    public void setViewIdResourceName(final String s) {
        AccessibilityNodeInfoCompat.IMPL.setViewIdResourceName(this.mInfo, s);
    }
    
    public void setVisibleToUser(final boolean b) {
        AccessibilityNodeInfoCompat.IMPL.setVisibleToUser(this.mInfo, b);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        final Rect rect = new Rect();
        this.getBoundsInParent(rect);
        sb.append("; boundsInParent: " + rect);
        this.getBoundsInScreen(rect);
        sb.append("; boundsInScreen: " + rect);
        sb.append("; packageName: ").append(this.getPackageName());
        sb.append("; className: ").append(this.getClassName());
        sb.append("; text: ").append(this.getText());
        sb.append("; contentDescription: ").append(this.getContentDescription());
        sb.append("; viewId: ").append(this.getViewIdResourceName());
        sb.append("; checkable: ").append(this.isCheckable());
        sb.append("; checked: ").append(this.isChecked());
        sb.append("; focusable: ").append(this.isFocusable());
        sb.append("; focused: ").append(this.isFocused());
        sb.append("; selected: ").append(this.isSelected());
        sb.append("; clickable: ").append(this.isClickable());
        sb.append("; longClickable: ").append(this.isLongClickable());
        sb.append("; enabled: ").append(this.isEnabled());
        sb.append("; password: ").append(this.isPassword());
        sb.append("; scrollable: " + this.isScrollable());
        sb.append("; [");
        int n2;
        for (int i = this.getActions(); i != 0; i = n2) {
            final int n = 1 << Integer.numberOfTrailingZeros(i);
            n2 = (i & ~n);
            sb.append(getActionSymbolicName(n));
            if ((i = n2) != 0) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    public static class AccessibilityActionCompat
    {
        private final Object mAction;
        
        public AccessibilityActionCompat(final int n, final CharSequence charSequence) {
            this(AccessibilityNodeInfoCompat.IMPL.newAccessibilityAction(n, charSequence));
        }
        
        private AccessibilityActionCompat(final Object mAction) {
            this.mAction = mAction;
        }
        
        public int getId() {
            return AccessibilityNodeInfoCompat.IMPL.getAccessibilityActionId(this.mAction);
        }
        
        public CharSequence getLabel() {
            return AccessibilityNodeInfoCompat.IMPL.getAccessibilityActionLabel(this.mAction);
        }
    }
    
    static class AccessibilityNodeInfoApi21Impl extends AccessibilityNodeInfoKitKatImpl
    {
        @Override
        public void addAction(final Object o, final Object o2) {
            AccessibilityNodeInfoCompatApi21.addAction(o, o2);
        }
        
        @Override
        public int getAccessibilityActionId(final Object o) {
            return AccessibilityNodeInfoCompatApi21.getAccessibilityActionId(o);
        }
        
        @Override
        public CharSequence getAccessibilityActionLabel(final Object o) {
            return AccessibilityNodeInfoCompatApi21.getAccessibilityActionLabel(o);
        }
        
        @Override
        public List<Object> getActionList(final Object o) {
            return AccessibilityNodeInfoCompatApi21.getActionList(o);
        }
        
        @Override
        public CharSequence getError(final Object o) {
            return AccessibilityNodeInfoCompatApi21.getError(o);
        }
        
        @Override
        public boolean isCollectionItemSelected(final Object o) {
            return AccessibilityNodeInfoCompatApi21.CollectionItemInfo.isSelected(o);
        }
        
        @Override
        public Object newAccessibilityAction(final int n, final CharSequence charSequence) {
            return AccessibilityNodeInfoCompatApi21.newAccessibilityAction(n, charSequence);
        }
        
        @Override
        public Object obtainCollectionInfo(final int n, final int n2, final boolean b, final int n3) {
            return AccessibilityNodeInfoCompatApi21.obtainCollectionInfo(n, n2, b, n3);
        }
        
        @Override
        public Object obtainCollectionItemInfo(final int n, final int n2, final int n3, final int n4, final boolean b, final boolean b2) {
            return AccessibilityNodeInfoCompatApi21.obtainCollectionItemInfo(n, n2, n3, n4, b, b2);
        }
        
        @Override
        public void setError(final Object o, final CharSequence charSequence) {
            AccessibilityNodeInfoCompatApi21.setError(o, charSequence);
        }
        
        @Override
        public void setLabelFor(final Object o, final View view) {
            AccessibilityNodeInfoCompatApi21.setLabelFor(o, view);
        }
        
        @Override
        public void setLabelFor(final Object o, final View view, final int n) {
            AccessibilityNodeInfoCompatApi21.setLabelFor(o, view, n);
        }
    }
    
    static class AccessibilityNodeInfoApi22Impl extends AccessibilityNodeInfoApi21Impl
    {
        @Override
        public AccessibilityNodeInfoCompat getTraversalAfter(Object traversalAfter) {
            traversalAfter = AccessibilityNodeInfoCompatApi22.getTraversalAfter(traversalAfter);
            if (traversalAfter == null) {
                return null;
            }
            return new AccessibilityNodeInfoCompat(traversalAfter);
        }
        
        @Override
        public AccessibilityNodeInfoCompat getTraversalBefore(Object traversalBefore) {
            traversalBefore = AccessibilityNodeInfoCompatApi22.getTraversalBefore(traversalBefore);
            if (traversalBefore == null) {
                return null;
            }
            return new AccessibilityNodeInfoCompat(traversalBefore);
        }
        
        @Override
        public void setTraversalAfter(final Object o, final View view) {
            AccessibilityNodeInfoCompatApi22.setTraversalAfter(o, view);
        }
        
        @Override
        public void setTraversalAfter(final Object o, final View view, final int n) {
            AccessibilityNodeInfoCompatApi22.setTraversalAfter(o, view, n);
        }
        
        @Override
        public void setTraversalBefore(final Object o, final View view) {
            AccessibilityNodeInfoCompatApi22.setTraversalBefore(o, view);
        }
        
        @Override
        public void setTraversalBefore(final Object o, final View view, final int n) {
            AccessibilityNodeInfoCompatApi22.setTraversalBefore(o, view, n);
        }
    }
    
    static class AccessibilityNodeInfoIcsImpl extends AccessibilityNodeInfoStubImpl
    {
        @Override
        public void addAction(final Object o, final int n) {
            AccessibilityNodeInfoCompatIcs.addAction(o, n);
        }
        
        @Override
        public void addChild(final Object o, final View view) {
            AccessibilityNodeInfoCompatIcs.addChild(o, view);
        }
        
        @Override
        public List<Object> findAccessibilityNodeInfosByText(final Object o, final String s) {
            return AccessibilityNodeInfoCompatIcs.findAccessibilityNodeInfosByText(o, s);
        }
        
        @Override
        public int getActions(final Object o) {
            return AccessibilityNodeInfoCompatIcs.getActions(o);
        }
        
        @Override
        public void getBoundsInParent(final Object o, final Rect rect) {
            AccessibilityNodeInfoCompatIcs.getBoundsInParent(o, rect);
        }
        
        @Override
        public void getBoundsInScreen(final Object o, final Rect rect) {
            AccessibilityNodeInfoCompatIcs.getBoundsInScreen(o, rect);
        }
        
        @Override
        public Object getChild(final Object o, final int n) {
            return AccessibilityNodeInfoCompatIcs.getChild(o, n);
        }
        
        @Override
        public int getChildCount(final Object o) {
            return AccessibilityNodeInfoCompatIcs.getChildCount(o);
        }
        
        @Override
        public CharSequence getClassName(final Object o) {
            return AccessibilityNodeInfoCompatIcs.getClassName(o);
        }
        
        @Override
        public CharSequence getContentDescription(final Object o) {
            return AccessibilityNodeInfoCompatIcs.getContentDescription(o);
        }
        
        @Override
        public CharSequence getPackageName(final Object o) {
            return AccessibilityNodeInfoCompatIcs.getPackageName(o);
        }
        
        @Override
        public Object getParent(final Object o) {
            return AccessibilityNodeInfoCompatIcs.getParent(o);
        }
        
        @Override
        public CharSequence getText(final Object o) {
            return AccessibilityNodeInfoCompatIcs.getText(o);
        }
        
        @Override
        public int getWindowId(final Object o) {
            return AccessibilityNodeInfoCompatIcs.getWindowId(o);
        }
        
        @Override
        public boolean isCheckable(final Object o) {
            return AccessibilityNodeInfoCompatIcs.isCheckable(o);
        }
        
        @Override
        public boolean isChecked(final Object o) {
            return AccessibilityNodeInfoCompatIcs.isChecked(o);
        }
        
        @Override
        public boolean isClickable(final Object o) {
            return AccessibilityNodeInfoCompatIcs.isClickable(o);
        }
        
        @Override
        public boolean isEnabled(final Object o) {
            return AccessibilityNodeInfoCompatIcs.isEnabled(o);
        }
        
        @Override
        public boolean isFocusable(final Object o) {
            return AccessibilityNodeInfoCompatIcs.isFocusable(o);
        }
        
        @Override
        public boolean isFocused(final Object o) {
            return AccessibilityNodeInfoCompatIcs.isFocused(o);
        }
        
        @Override
        public boolean isLongClickable(final Object o) {
            return AccessibilityNodeInfoCompatIcs.isLongClickable(o);
        }
        
        @Override
        public boolean isPassword(final Object o) {
            return AccessibilityNodeInfoCompatIcs.isPassword(o);
        }
        
        @Override
        public boolean isScrollable(final Object o) {
            return AccessibilityNodeInfoCompatIcs.isScrollable(o);
        }
        
        @Override
        public boolean isSelected(final Object o) {
            return AccessibilityNodeInfoCompatIcs.isSelected(o);
        }
        
        @Override
        public Object obtain() {
            return AccessibilityNodeInfoCompatIcs.obtain();
        }
        
        @Override
        public Object obtain(final View view) {
            return AccessibilityNodeInfoCompatIcs.obtain(view);
        }
        
        @Override
        public Object obtain(final Object o) {
            return AccessibilityNodeInfoCompatIcs.obtain(o);
        }
        
        @Override
        public boolean performAction(final Object o, final int n) {
            return AccessibilityNodeInfoCompatIcs.performAction(o, n);
        }
        
        @Override
        public void recycle(final Object o) {
            AccessibilityNodeInfoCompatIcs.recycle(o);
        }
        
        @Override
        public void setBoundsInParent(final Object o, final Rect rect) {
            AccessibilityNodeInfoCompatIcs.setBoundsInParent(o, rect);
        }
        
        @Override
        public void setBoundsInScreen(final Object o, final Rect rect) {
            AccessibilityNodeInfoCompatIcs.setBoundsInScreen(o, rect);
        }
        
        @Override
        public void setCheckable(final Object o, final boolean b) {
            AccessibilityNodeInfoCompatIcs.setCheckable(o, b);
        }
        
        @Override
        public void setChecked(final Object o, final boolean b) {
            AccessibilityNodeInfoCompatIcs.setChecked(o, b);
        }
        
        @Override
        public void setClassName(final Object o, final CharSequence charSequence) {
            AccessibilityNodeInfoCompatIcs.setClassName(o, charSequence);
        }
        
        @Override
        public void setClickable(final Object o, final boolean b) {
            AccessibilityNodeInfoCompatIcs.setClickable(o, b);
        }
        
        @Override
        public void setContentDescription(final Object o, final CharSequence charSequence) {
            AccessibilityNodeInfoCompatIcs.setContentDescription(o, charSequence);
        }
        
        @Override
        public void setEnabled(final Object o, final boolean b) {
            AccessibilityNodeInfoCompatIcs.setEnabled(o, b);
        }
        
        @Override
        public void setFocusable(final Object o, final boolean b) {
            AccessibilityNodeInfoCompatIcs.setFocusable(o, b);
        }
        
        @Override
        public void setFocused(final Object o, final boolean b) {
            AccessibilityNodeInfoCompatIcs.setFocused(o, b);
        }
        
        @Override
        public void setLongClickable(final Object o, final boolean b) {
            AccessibilityNodeInfoCompatIcs.setLongClickable(o, b);
        }
        
        @Override
        public void setPackageName(final Object o, final CharSequence charSequence) {
            AccessibilityNodeInfoCompatIcs.setPackageName(o, charSequence);
        }
        
        @Override
        public void setParent(final Object o, final View view) {
            AccessibilityNodeInfoCompatIcs.setParent(o, view);
        }
        
        @Override
        public void setPassword(final Object o, final boolean b) {
            AccessibilityNodeInfoCompatIcs.setPassword(o, b);
        }
        
        @Override
        public void setScrollable(final Object o, final boolean b) {
            AccessibilityNodeInfoCompatIcs.setScrollable(o, b);
        }
        
        @Override
        public void setSelected(final Object o, final boolean b) {
            AccessibilityNodeInfoCompatIcs.setSelected(o, b);
        }
        
        @Override
        public void setSource(final Object o, final View view) {
            AccessibilityNodeInfoCompatIcs.setSource(o, view);
        }
        
        @Override
        public void setText(final Object o, final CharSequence charSequence) {
            AccessibilityNodeInfoCompatIcs.setText(o, charSequence);
        }
    }
    
    interface AccessibilityNodeInfoImpl
    {
        void addAction(final Object p0, final int p1);
        
        void addAction(final Object p0, final Object p1);
        
        void addChild(final Object p0, final View p1);
        
        void addChild(final Object p0, final View p1, final int p2);
        
        List<Object> findAccessibilityNodeInfosByText(final Object p0, final String p1);
        
        Object findFocus(final Object p0, final int p1);
        
        Object focusSearch(final Object p0, final int p1);
        
        int getAccessibilityActionId(final Object p0);
        
        CharSequence getAccessibilityActionLabel(final Object p0);
        
        List<Object> getActionList(final Object p0);
        
        int getActions(final Object p0);
        
        void getBoundsInParent(final Object p0, final Rect p1);
        
        void getBoundsInScreen(final Object p0, final Rect p1);
        
        Object getChild(final Object p0, final int p1);
        
        int getChildCount(final Object p0);
        
        CharSequence getClassName(final Object p0);
        
        Object getCollectionInfo(final Object p0);
        
        int getCollectionInfoColumnCount(final Object p0);
        
        int getCollectionInfoRowCount(final Object p0);
        
        int getCollectionItemColumnIndex(final Object p0);
        
        int getCollectionItemColumnSpan(final Object p0);
        
        Object getCollectionItemInfo(final Object p0);
        
        int getCollectionItemRowIndex(final Object p0);
        
        int getCollectionItemRowSpan(final Object p0);
        
        CharSequence getContentDescription(final Object p0);
        
        CharSequence getError(final Object p0);
        
        int getLiveRegion(final Object p0);
        
        int getMovementGranularities(final Object p0);
        
        CharSequence getPackageName(final Object p0);
        
        Object getParent(final Object p0);
        
        Object getRangeInfo(final Object p0);
        
        CharSequence getText(final Object p0);
        
        AccessibilityNodeInfoCompat getTraversalAfter(final Object p0);
        
        AccessibilityNodeInfoCompat getTraversalBefore(final Object p0);
        
        String getViewIdResourceName(final Object p0);
        
        int getWindowId(final Object p0);
        
        boolean isAccessibilityFocused(final Object p0);
        
        boolean isCheckable(final Object p0);
        
        boolean isChecked(final Object p0);
        
        boolean isClickable(final Object p0);
        
        boolean isCollectionInfoHierarchical(final Object p0);
        
        boolean isCollectionItemHeading(final Object p0);
        
        boolean isCollectionItemSelected(final Object p0);
        
        boolean isContentInvalid(final Object p0);
        
        boolean isEnabled(final Object p0);
        
        boolean isFocusable(final Object p0);
        
        boolean isFocused(final Object p0);
        
        boolean isLongClickable(final Object p0);
        
        boolean isPassword(final Object p0);
        
        boolean isScrollable(final Object p0);
        
        boolean isSelected(final Object p0);
        
        boolean isVisibleToUser(final Object p0);
        
        Object newAccessibilityAction(final int p0, final CharSequence p1);
        
        Object obtain();
        
        Object obtain(final View p0);
        
        Object obtain(final View p0, final int p1);
        
        Object obtain(final Object p0);
        
        Object obtainCollectionInfo(final int p0, final int p1, final boolean p2, final int p3);
        
        Object obtainCollectionItemInfo(final int p0, final int p1, final int p2, final int p3, final boolean p4, final boolean p5);
        
        boolean performAction(final Object p0, final int p1);
        
        boolean performAction(final Object p0, final int p1, final Bundle p2);
        
        void recycle(final Object p0);
        
        void setAccessibilityFocused(final Object p0, final boolean p1);
        
        void setBoundsInParent(final Object p0, final Rect p1);
        
        void setBoundsInScreen(final Object p0, final Rect p1);
        
        void setCheckable(final Object p0, final boolean p1);
        
        void setChecked(final Object p0, final boolean p1);
        
        void setClassName(final Object p0, final CharSequence p1);
        
        void setClickable(final Object p0, final boolean p1);
        
        void setCollectionInfo(final Object p0, final Object p1);
        
        void setCollectionItemInfo(final Object p0, final Object p1);
        
        void setContentDescription(final Object p0, final CharSequence p1);
        
        void setContentInvalid(final Object p0, final boolean p1);
        
        void setEnabled(final Object p0, final boolean p1);
        
        void setError(final Object p0, final CharSequence p1);
        
        void setFocusable(final Object p0, final boolean p1);
        
        void setFocused(final Object p0, final boolean p1);
        
        void setLabelFor(final Object p0, final View p1);
        
        void setLabelFor(final Object p0, final View p1, final int p2);
        
        void setLiveRegion(final Object p0, final int p1);
        
        void setLongClickable(final Object p0, final boolean p1);
        
        void setMovementGranularities(final Object p0, final int p1);
        
        void setPackageName(final Object p0, final CharSequence p1);
        
        void setParent(final Object p0, final View p1);
        
        void setParent(final Object p0, final View p1, final int p2);
        
        void setPassword(final Object p0, final boolean p1);
        
        void setScrollable(final Object p0, final boolean p1);
        
        void setSelected(final Object p0, final boolean p1);
        
        void setSource(final Object p0, final View p1);
        
        void setSource(final Object p0, final View p1, final int p2);
        
        void setText(final Object p0, final CharSequence p1);
        
        void setTraversalAfter(final Object p0, final View p1);
        
        void setTraversalAfter(final Object p0, final View p1, final int p2);
        
        void setTraversalBefore(final Object p0, final View p1);
        
        void setTraversalBefore(final Object p0, final View p1, final int p2);
        
        void setViewIdResourceName(final Object p0, final String p1);
        
        void setVisibleToUser(final Object p0, final boolean p1);
    }
    
    static class AccessibilityNodeInfoJellybeanImpl extends AccessibilityNodeInfoIcsImpl
    {
        @Override
        public void addChild(final Object o, final View view, final int n) {
            AccessibilityNodeInfoCompatJellyBean.addChild(o, view, n);
        }
        
        @Override
        public Object findFocus(final Object o, final int n) {
            return AccessibilityNodeInfoCompatJellyBean.findFocus(o, n);
        }
        
        @Override
        public Object focusSearch(final Object o, final int n) {
            return AccessibilityNodeInfoCompatJellyBean.focusSearch(o, n);
        }
        
        @Override
        public int getMovementGranularities(final Object o) {
            return AccessibilityNodeInfoCompatJellyBean.getMovementGranularities(o);
        }
        
        @Override
        public boolean isAccessibilityFocused(final Object o) {
            return AccessibilityNodeInfoCompatJellyBean.isAccessibilityFocused(o);
        }
        
        @Override
        public boolean isVisibleToUser(final Object o) {
            return AccessibilityNodeInfoCompatJellyBean.isVisibleToUser(o);
        }
        
        @Override
        public Object obtain(final View view, final int n) {
            return AccessibilityNodeInfoCompatJellyBean.obtain(view, n);
        }
        
        @Override
        public boolean performAction(final Object o, final int n, final Bundle bundle) {
            return AccessibilityNodeInfoCompatJellyBean.performAction(o, n, bundle);
        }
        
        @Override
        public void setAccessibilityFocused(final Object o, final boolean b) {
            AccessibilityNodeInfoCompatJellyBean.setAccesibilityFocused(o, b);
        }
        
        @Override
        public void setMovementGranularities(final Object o, final int n) {
            AccessibilityNodeInfoCompatJellyBean.setMovementGranularities(o, n);
        }
        
        @Override
        public void setParent(final Object o, final View view, final int n) {
            AccessibilityNodeInfoCompatJellyBean.setParent(o, view, n);
        }
        
        @Override
        public void setSource(final Object o, final View view, final int n) {
            AccessibilityNodeInfoCompatJellyBean.setSource(o, view, n);
        }
        
        @Override
        public void setVisibleToUser(final Object o, final boolean b) {
            AccessibilityNodeInfoCompatJellyBean.setVisibleToUser(o, b);
        }
    }
    
    static class AccessibilityNodeInfoJellybeanMr2Impl extends AccessibilityNodeInfoJellybeanImpl
    {
        @Override
        public String getViewIdResourceName(final Object o) {
            return AccessibilityNodeInfoCompatJellybeanMr2.getViewIdResourceName(o);
        }
        
        @Override
        public void setViewIdResourceName(final Object o, final String s) {
            AccessibilityNodeInfoCompatJellybeanMr2.setViewIdResourceName(o, s);
        }
    }
    
    static class AccessibilityNodeInfoKitKatImpl extends AccessibilityNodeInfoJellybeanMr2Impl
    {
        @Override
        public Object getCollectionInfo(final Object o) {
            return AccessibilityNodeInfoCompatKitKat.getCollectionInfo(o);
        }
        
        @Override
        public int getCollectionInfoColumnCount(final Object o) {
            return AccessibilityNodeInfoCompatKitKat.CollectionInfo.getColumnCount(o);
        }
        
        @Override
        public int getCollectionInfoRowCount(final Object o) {
            return AccessibilityNodeInfoCompatKitKat.CollectionInfo.getRowCount(o);
        }
        
        @Override
        public int getCollectionItemColumnIndex(final Object o) {
            return AccessibilityNodeInfoCompatKitKat.CollectionItemInfo.getColumnIndex(o);
        }
        
        @Override
        public int getCollectionItemColumnSpan(final Object o) {
            return AccessibilityNodeInfoCompatKitKat.CollectionItemInfo.getColumnSpan(o);
        }
        
        @Override
        public Object getCollectionItemInfo(final Object o) {
            return AccessibilityNodeInfoCompatKitKat.getCollectionItemInfo(o);
        }
        
        @Override
        public int getCollectionItemRowIndex(final Object o) {
            return AccessibilityNodeInfoCompatKitKat.CollectionItemInfo.getRowIndex(o);
        }
        
        @Override
        public int getCollectionItemRowSpan(final Object o) {
            return AccessibilityNodeInfoCompatKitKat.CollectionItemInfo.getRowSpan(o);
        }
        
        @Override
        public int getLiveRegion(final Object o) {
            return AccessibilityNodeInfoCompatKitKat.getLiveRegion(o);
        }
        
        @Override
        public Object getRangeInfo(final Object o) {
            return AccessibilityNodeInfoCompatKitKat.getRangeInfo(o);
        }
        
        @Override
        public boolean isCollectionInfoHierarchical(final Object o) {
            return AccessibilityNodeInfoCompatKitKat.CollectionInfo.isHierarchical(o);
        }
        
        @Override
        public boolean isCollectionItemHeading(final Object o) {
            return AccessibilityNodeInfoCompatKitKat.CollectionItemInfo.isHeading(o);
        }
        
        @Override
        public boolean isContentInvalid(final Object o) {
            return AccessibilityNodeInfoCompatKitKat.isContentInvalid(o);
        }
        
        @Override
        public Object obtainCollectionInfo(final int n, final int n2, final boolean b, final int n3) {
            return AccessibilityNodeInfoCompatKitKat.obtainCollectionInfo(n, n2, b, n3);
        }
        
        @Override
        public Object obtainCollectionItemInfo(final int n, final int n2, final int n3, final int n4, final boolean b, final boolean b2) {
            return AccessibilityNodeInfoCompatKitKat.obtainCollectionItemInfo(n, n2, n3, n4, b);
        }
        
        @Override
        public void setCollectionInfo(final Object o, final Object o2) {
            AccessibilityNodeInfoCompatKitKat.setCollectionInfo(o, o2);
        }
        
        @Override
        public void setCollectionItemInfo(final Object o, final Object o2) {
            AccessibilityNodeInfoCompatKitKat.setCollectionItemInfo(o, o2);
        }
        
        @Override
        public void setContentInvalid(final Object o, final boolean b) {
            AccessibilityNodeInfoCompatKitKat.setContentInvalid(o, b);
        }
        
        @Override
        public void setLiveRegion(final Object o, final int n) {
            AccessibilityNodeInfoCompatKitKat.setLiveRegion(o, n);
        }
    }
    
    static class AccessibilityNodeInfoStubImpl implements AccessibilityNodeInfoImpl
    {
        @Override
        public void addAction(final Object o, final int n) {
        }
        
        @Override
        public void addAction(final Object o, final Object o2) {
        }
        
        @Override
        public void addChild(final Object o, final View view) {
        }
        
        @Override
        public void addChild(final Object o, final View view, final int n) {
        }
        
        @Override
        public List<Object> findAccessibilityNodeInfosByText(final Object o, final String s) {
            return Collections.emptyList();
        }
        
        @Override
        public Object findFocus(final Object o, final int n) {
            return null;
        }
        
        @Override
        public Object focusSearch(final Object o, final int n) {
            return null;
        }
        
        @Override
        public int getAccessibilityActionId(final Object o) {
            return 0;
        }
        
        @Override
        public CharSequence getAccessibilityActionLabel(final Object o) {
            return null;
        }
        
        @Override
        public List<Object> getActionList(final Object o) {
            return null;
        }
        
        @Override
        public int getActions(final Object o) {
            return 0;
        }
        
        @Override
        public void getBoundsInParent(final Object o, final Rect rect) {
        }
        
        @Override
        public void getBoundsInScreen(final Object o, final Rect rect) {
        }
        
        @Override
        public Object getChild(final Object o, final int n) {
            return null;
        }
        
        @Override
        public int getChildCount(final Object o) {
            return 0;
        }
        
        @Override
        public CharSequence getClassName(final Object o) {
            return null;
        }
        
        @Override
        public Object getCollectionInfo(final Object o) {
            return null;
        }
        
        @Override
        public int getCollectionInfoColumnCount(final Object o) {
            return 0;
        }
        
        @Override
        public int getCollectionInfoRowCount(final Object o) {
            return 0;
        }
        
        @Override
        public int getCollectionItemColumnIndex(final Object o) {
            return 0;
        }
        
        @Override
        public int getCollectionItemColumnSpan(final Object o) {
            return 0;
        }
        
        @Override
        public Object getCollectionItemInfo(final Object o) {
            return null;
        }
        
        @Override
        public int getCollectionItemRowIndex(final Object o) {
            return 0;
        }
        
        @Override
        public int getCollectionItemRowSpan(final Object o) {
            return 0;
        }
        
        @Override
        public CharSequence getContentDescription(final Object o) {
            return null;
        }
        
        @Override
        public CharSequence getError(final Object o) {
            return null;
        }
        
        @Override
        public int getLiveRegion(final Object o) {
            return 0;
        }
        
        @Override
        public int getMovementGranularities(final Object o) {
            return 0;
        }
        
        @Override
        public CharSequence getPackageName(final Object o) {
            return null;
        }
        
        @Override
        public Object getParent(final Object o) {
            return null;
        }
        
        @Override
        public Object getRangeInfo(final Object o) {
            return null;
        }
        
        @Override
        public CharSequence getText(final Object o) {
            return null;
        }
        
        @Override
        public AccessibilityNodeInfoCompat getTraversalAfter(final Object o) {
            return null;
        }
        
        @Override
        public AccessibilityNodeInfoCompat getTraversalBefore(final Object o) {
            return null;
        }
        
        @Override
        public String getViewIdResourceName(final Object o) {
            return null;
        }
        
        @Override
        public int getWindowId(final Object o) {
            return 0;
        }
        
        @Override
        public boolean isAccessibilityFocused(final Object o) {
            return false;
        }
        
        @Override
        public boolean isCheckable(final Object o) {
            return false;
        }
        
        @Override
        public boolean isChecked(final Object o) {
            return false;
        }
        
        @Override
        public boolean isClickable(final Object o) {
            return false;
        }
        
        @Override
        public boolean isCollectionInfoHierarchical(final Object o) {
            return false;
        }
        
        @Override
        public boolean isCollectionItemHeading(final Object o) {
            return false;
        }
        
        @Override
        public boolean isCollectionItemSelected(final Object o) {
            return false;
        }
        
        @Override
        public boolean isContentInvalid(final Object o) {
            return false;
        }
        
        @Override
        public boolean isEnabled(final Object o) {
            return false;
        }
        
        @Override
        public boolean isFocusable(final Object o) {
            return false;
        }
        
        @Override
        public boolean isFocused(final Object o) {
            return false;
        }
        
        @Override
        public boolean isLongClickable(final Object o) {
            return false;
        }
        
        @Override
        public boolean isPassword(final Object o) {
            return false;
        }
        
        @Override
        public boolean isScrollable(final Object o) {
            return false;
        }
        
        @Override
        public boolean isSelected(final Object o) {
            return false;
        }
        
        @Override
        public boolean isVisibleToUser(final Object o) {
            return false;
        }
        
        @Override
        public Object newAccessibilityAction(final int n, final CharSequence charSequence) {
            return null;
        }
        
        @Override
        public Object obtain() {
            return null;
        }
        
        @Override
        public Object obtain(final View view) {
            return null;
        }
        
        @Override
        public Object obtain(final View view, final int n) {
            return null;
        }
        
        @Override
        public Object obtain(final Object o) {
            return null;
        }
        
        @Override
        public Object obtainCollectionInfo(final int n, final int n2, final boolean b, final int n3) {
            return null;
        }
        
        @Override
        public Object obtainCollectionItemInfo(final int n, final int n2, final int n3, final int n4, final boolean b, final boolean b2) {
            return null;
        }
        
        @Override
        public boolean performAction(final Object o, final int n) {
            return false;
        }
        
        @Override
        public boolean performAction(final Object o, final int n, final Bundle bundle) {
            return false;
        }
        
        @Override
        public void recycle(final Object o) {
        }
        
        @Override
        public void setAccessibilityFocused(final Object o, final boolean b) {
        }
        
        @Override
        public void setBoundsInParent(final Object o, final Rect rect) {
        }
        
        @Override
        public void setBoundsInScreen(final Object o, final Rect rect) {
        }
        
        @Override
        public void setCheckable(final Object o, final boolean b) {
        }
        
        @Override
        public void setChecked(final Object o, final boolean b) {
        }
        
        @Override
        public void setClassName(final Object o, final CharSequence charSequence) {
        }
        
        @Override
        public void setClickable(final Object o, final boolean b) {
        }
        
        @Override
        public void setCollectionInfo(final Object o, final Object o2) {
        }
        
        @Override
        public void setCollectionItemInfo(final Object o, final Object o2) {
        }
        
        @Override
        public void setContentDescription(final Object o, final CharSequence charSequence) {
        }
        
        @Override
        public void setContentInvalid(final Object o, final boolean b) {
        }
        
        @Override
        public void setEnabled(final Object o, final boolean b) {
        }
        
        @Override
        public void setError(final Object o, final CharSequence charSequence) {
        }
        
        @Override
        public void setFocusable(final Object o, final boolean b) {
        }
        
        @Override
        public void setFocused(final Object o, final boolean b) {
        }
        
        @Override
        public void setLabelFor(final Object o, final View view) {
        }
        
        @Override
        public void setLabelFor(final Object o, final View view, final int n) {
        }
        
        @Override
        public void setLiveRegion(final Object o, final int n) {
        }
        
        @Override
        public void setLongClickable(final Object o, final boolean b) {
        }
        
        @Override
        public void setMovementGranularities(final Object o, final int n) {
        }
        
        @Override
        public void setPackageName(final Object o, final CharSequence charSequence) {
        }
        
        @Override
        public void setParent(final Object o, final View view) {
        }
        
        @Override
        public void setParent(final Object o, final View view, final int n) {
        }
        
        @Override
        public void setPassword(final Object o, final boolean b) {
        }
        
        @Override
        public void setScrollable(final Object o, final boolean b) {
        }
        
        @Override
        public void setSelected(final Object o, final boolean b) {
        }
        
        @Override
        public void setSource(final Object o, final View view) {
        }
        
        @Override
        public void setSource(final Object o, final View view, final int n) {
        }
        
        @Override
        public void setText(final Object o, final CharSequence charSequence) {
        }
        
        @Override
        public void setTraversalAfter(final Object o, final View view) {
        }
        
        @Override
        public void setTraversalAfter(final Object o, final View view, final int n) {
        }
        
        @Override
        public void setTraversalBefore(final Object o, final View view) {
        }
        
        @Override
        public void setTraversalBefore(final Object o, final View view, final int n) {
        }
        
        @Override
        public void setViewIdResourceName(final Object o, final String s) {
        }
        
        @Override
        public void setVisibleToUser(final Object o, final boolean b) {
        }
    }
    
    public static class CollectionInfoCompat
    {
        public static final int SELECTION_MODE_MULTIPLE = 2;
        public static final int SELECTION_MODE_NONE = 0;
        public static final int SELECTION_MODE_SINGLE = 1;
        final Object mInfo;
        
        private CollectionInfoCompat(final Object mInfo) {
            this.mInfo = mInfo;
        }
        
        public static CollectionInfoCompat obtain(final int n, final int n2, final boolean b, final int n3) {
            return new CollectionInfoCompat(AccessibilityNodeInfoCompat.IMPL.obtainCollectionInfo(n, n2, b, n3));
        }
        
        public int getColumnCount() {
            return AccessibilityNodeInfoCompat.IMPL.getCollectionInfoColumnCount(this.mInfo);
        }
        
        public int getRowCount() {
            return AccessibilityNodeInfoCompat.IMPL.getCollectionInfoRowCount(this.mInfo);
        }
        
        public boolean isHierarchical() {
            return AccessibilityNodeInfoCompat.IMPL.isCollectionInfoHierarchical(this.mInfo);
        }
    }
    
    public static class CollectionItemInfoCompat
    {
        private final Object mInfo;
        
        private CollectionItemInfoCompat(final Object mInfo) {
            this.mInfo = mInfo;
        }
        
        public static CollectionItemInfoCompat obtain(final int n, final int n2, final int n3, final int n4, final boolean b, final boolean b2) {
            return new CollectionItemInfoCompat(AccessibilityNodeInfoCompat.IMPL.obtainCollectionItemInfo(n, n2, n3, n4, b, b2));
        }
        
        public int getColumnIndex() {
            return AccessibilityNodeInfoCompat.IMPL.getCollectionItemColumnIndex(this.mInfo);
        }
        
        public int getColumnSpan() {
            return AccessibilityNodeInfoCompat.IMPL.getCollectionItemColumnSpan(this.mInfo);
        }
        
        public int getRowIndex() {
            return AccessibilityNodeInfoCompat.IMPL.getCollectionItemRowIndex(this.mInfo);
        }
        
        public int getRowSpan() {
            return AccessibilityNodeInfoCompat.IMPL.getCollectionItemRowSpan(this.mInfo);
        }
        
        public boolean isHeading() {
            return AccessibilityNodeInfoCompat.IMPL.isCollectionItemHeading(this.mInfo);
        }
        
        public boolean isSelected() {
            return AccessibilityNodeInfoCompat.IMPL.isCollectionItemSelected(this.mInfo);
        }
    }
    
    public static class RangeInfoCompat
    {
        public static final int RANGE_TYPE_FLOAT = 1;
        public static final int RANGE_TYPE_INT = 0;
        public static final int RANGE_TYPE_PERCENT = 2;
        private final Object mInfo;
        
        private RangeInfoCompat(final Object mInfo) {
            this.mInfo = mInfo;
        }
        
        public float getCurrent() {
            return AccessibilityNodeInfoCompatKitKat.RangeInfo.getCurrent(this.mInfo);
        }
        
        public float getMax() {
            return AccessibilityNodeInfoCompatKitKat.RangeInfo.getMax(this.mInfo);
        }
        
        public float getMin() {
            return AccessibilityNodeInfoCompatKitKat.RangeInfo.getMin(this.mInfo);
        }
        
        public int getType() {
            return AccessibilityNodeInfoCompatKitKat.RangeInfo.getType(this.mInfo);
        }
    }
}
