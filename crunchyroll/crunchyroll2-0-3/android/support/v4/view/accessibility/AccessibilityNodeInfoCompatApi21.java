// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view.accessibility;

import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo$CollectionItemInfo;
import android.view.accessibility.AccessibilityNodeInfo$CollectionInfo;
import java.util.List;
import android.view.accessibility.AccessibilityNodeInfo$AccessibilityAction;
import android.view.accessibility.AccessibilityNodeInfo;

class AccessibilityNodeInfoCompatApi21
{
    static void addAction(final Object o, final Object o2) {
        ((AccessibilityNodeInfo)o).addAction((AccessibilityNodeInfo$AccessibilityAction)o2);
    }
    
    static int getAccessibilityActionId(final Object o) {
        return ((AccessibilityNodeInfo$AccessibilityAction)o).getId();
    }
    
    static CharSequence getAccessibilityActionLabel(final Object o) {
        return ((AccessibilityNodeInfo$AccessibilityAction)o).getLabel();
    }
    
    static List<Object> getActionList(final Object o) {
        return (List<Object>)((AccessibilityNodeInfo)o).getActionList();
    }
    
    public static CharSequence getError(final Object o) {
        return ((AccessibilityNodeInfo)o).getError();
    }
    
    static Object newAccessibilityAction(final int n, final CharSequence charSequence) {
        return new AccessibilityNodeInfo$AccessibilityAction(n, charSequence);
    }
    
    public static Object obtainCollectionInfo(final int n, final int n2, final boolean b, final int n3) {
        return AccessibilityNodeInfo$CollectionInfo.obtain(n, n2, b, n3);
    }
    
    public static Object obtainCollectionItemInfo(final int n, final int n2, final int n3, final int n4, final boolean b, final boolean b2) {
        return AccessibilityNodeInfo$CollectionItemInfo.obtain(n, n2, n3, n4, b, b2);
    }
    
    public static void setError(final Object o, final CharSequence error) {
        ((AccessibilityNodeInfo)o).setError(error);
    }
    
    public static void setLabelFor(final Object o, final View labelFor) {
        ((AccessibilityNodeInfo)o).setLabelFor(labelFor);
    }
    
    public static void setLabelFor(final Object o, final View view, final int n) {
        ((AccessibilityNodeInfo)o).setLabelFor(view, n);
    }
    
    static class CollectionItemInfo
    {
        public static boolean isSelected(final Object o) {
            return ((AccessibilityNodeInfo$CollectionItemInfo)o).isSelected();
        }
    }
}
