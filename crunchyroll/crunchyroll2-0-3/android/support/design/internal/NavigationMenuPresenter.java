// 
// Decompiled by Procyon v0.5.30
// 

package android.support.design.internal;

import android.widget.TextView;
import android.view.SubMenu;
import android.graphics.drawable.ColorDrawable;
import java.util.ArrayList;
import android.widget.BaseAdapter;
import android.support.v7.internal.view.menu.SubMenuBuilder;
import android.util.SparseArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.content.res.Resources;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.widget.ListAdapter;
import android.support.design.R;
import android.support.v7.internal.view.menu.MenuView;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.support.annotation.NonNull;
import android.view.View;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.view.LayoutInflater;
import android.graphics.drawable.Drawable;
import android.content.res.ColorStateList;
import android.widget.LinearLayout;
import android.widget.AdapterView$OnItemClickListener;
import android.support.v7.internal.view.menu.MenuPresenter;

public class NavigationMenuPresenter implements MenuPresenter, AdapterView$OnItemClickListener
{
    private static final String STATE_HIERARCHY = "android:menu:list";
    private NavigationMenuAdapter mAdapter;
    private Callback mCallback;
    private LinearLayout mHeader;
    private ColorStateList mIconTintList;
    private int mId;
    private Drawable mItemBackground;
    private LayoutInflater mLayoutInflater;
    private MenuBuilder mMenu;
    private NavigationMenuView mMenuView;
    private int mPaddingSeparator;
    private int mPaddingTopDefault;
    private ColorStateList mTextColor;
    
    public void addHeaderView(@NonNull final View view) {
        this.mHeader.addView(view);
        this.mMenuView.setPadding(0, 0, 0, this.mMenuView.getPaddingBottom());
    }
    
    @Override
    public boolean collapseItemActionView(final MenuBuilder menuBuilder, final MenuItemImpl menuItemImpl) {
        return false;
    }
    
    @Override
    public boolean expandItemActionView(final MenuBuilder menuBuilder, final MenuItemImpl menuItemImpl) {
        return false;
    }
    
    @Override
    public boolean flagActionItems() {
        return false;
    }
    
    @Override
    public int getId() {
        return this.mId;
    }
    
    public Drawable getItemBackground() {
        return this.mItemBackground;
    }
    
    @Nullable
    public ColorStateList getItemTextColor() {
        return this.mTextColor;
    }
    
    @Nullable
    public ColorStateList getItemTintList() {
        return this.mIconTintList;
    }
    
    @Override
    public MenuView getMenuView(final ViewGroup viewGroup) {
        if (this.mMenuView == null) {
            this.mMenuView = (NavigationMenuView)this.mLayoutInflater.inflate(R.layout.design_navigation_menu, viewGroup, false);
            if (this.mAdapter == null) {
                this.mAdapter = new NavigationMenuAdapter();
            }
            this.mHeader = (LinearLayout)this.mLayoutInflater.inflate(R.layout.design_navigation_item_header, (ViewGroup)this.mMenuView, false);
            this.mMenuView.addHeaderView((View)this.mHeader);
            this.mMenuView.setAdapter((ListAdapter)this.mAdapter);
            this.mMenuView.setOnItemClickListener((AdapterView$OnItemClickListener)this);
        }
        return this.mMenuView;
    }
    
    public View inflateHeaderView(@LayoutRes final int n) {
        final View inflate = this.mLayoutInflater.inflate(n, (ViewGroup)this.mHeader, false);
        this.addHeaderView(inflate);
        return inflate;
    }
    
    @Override
    public void initForMenu(final Context context, final MenuBuilder mMenu) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mMenu = mMenu;
        final Resources resources = context.getResources();
        this.mPaddingTopDefault = resources.getDimensionPixelOffset(R.dimen.navigation_padding_top_default);
        this.mPaddingSeparator = resources.getDimensionPixelOffset(R.dimen.navigation_separator_vertical_padding);
    }
    
    @Override
    public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
        if (this.mCallback != null) {
            this.mCallback.onCloseMenu(menuBuilder, b);
        }
    }
    
    public void onItemClick(final AdapterView<?> adapterView, final View view, int n, final long n2) {
        n -= this.mMenuView.getHeaderViewsCount();
        if (n >= 0) {
            this.mMenu.performItemAction((MenuItem)this.mAdapter.getItem(n).getMenuItem(), this, 0);
        }
    }
    
    @Override
    public void onRestoreInstanceState(final Parcelable parcelable) {
        final SparseArray sparseParcelableArray = ((Bundle)parcelable).getSparseParcelableArray("android:menu:list");
        if (sparseParcelableArray != null) {
            this.mMenuView.restoreHierarchyState(sparseParcelableArray);
        }
    }
    
    @Override
    public Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        final SparseArray sparseArray = new SparseArray();
        if (this.mMenuView != null) {
            this.mMenuView.saveHierarchyState(sparseArray);
        }
        bundle.putSparseParcelableArray("android:menu:list", sparseArray);
        return (Parcelable)bundle;
    }
    
    @Override
    public boolean onSubMenuSelected(final SubMenuBuilder subMenuBuilder) {
        return false;
    }
    
    public void removeHeaderView(@NonNull final View view) {
        this.mHeader.removeView(view);
        if (this.mHeader.getChildCount() == 0) {
            this.mMenuView.setPadding(0, this.mPaddingTopDefault, 0, this.mMenuView.getPaddingBottom());
        }
    }
    
    @Override
    public void setCallback(final Callback mCallback) {
        this.mCallback = mCallback;
    }
    
    public void setId(final int mId) {
        this.mId = mId;
    }
    
    public void setItemBackground(final Drawable mItemBackground) {
        this.mItemBackground = mItemBackground;
    }
    
    public void setItemIconTintList(@Nullable final ColorStateList mIconTintList) {
        this.mIconTintList = mIconTintList;
    }
    
    public void setItemTextColor(@Nullable final ColorStateList mTextColor) {
        this.mTextColor = mTextColor;
    }
    
    @Override
    public void updateMenuView(final boolean b) {
        if (this.mAdapter != null) {
            this.mAdapter.notifyDataSetChanged();
        }
    }
    
    private class NavigationMenuAdapter extends BaseAdapter
    {
        private static final int VIEW_TYPE_NORMAL = 0;
        private static final int VIEW_TYPE_SEPARATOR = 2;
        private static final int VIEW_TYPE_SUBHEADER = 1;
        private final ArrayList<NavigationMenuItem> mItems;
        private ColorDrawable mTransparentIcon;
        
        NavigationMenuAdapter() {
            this.mItems = new ArrayList<NavigationMenuItem>();
            this.prepareMenuItems();
        }
        
        private void appendTransparentIconIfMissing(int i, final int n) {
            while (i < n) {
                final MenuItemImpl menuItem = this.mItems.get(i).getMenuItem();
                if (((MenuItem)menuItem).getIcon() == null) {
                    if (this.mTransparentIcon == null) {
                        this.mTransparentIcon = new ColorDrawable(17170445);
                    }
                    ((MenuItem)menuItem).setIcon((Drawable)this.mTransparentIcon);
                }
                ++i;
            }
        }
        
        private void prepareMenuItems() {
            this.mItems.clear();
            int n = -1;
            int n2 = 0;
            int n3 = 0;
            int n4;
            int groupId;
            int n5;
            for (int i = 0; i < NavigationMenuPresenter.this.mMenu.getVisibleItems().size(); ++i, n3 = n4, n = groupId, n2 = n5) {
                final MenuItemImpl menuItemImpl = NavigationMenuPresenter.this.mMenu.getVisibleItems().get(i);
                if (menuItemImpl.hasSubMenu()) {
                    final SubMenu subMenu = menuItemImpl.getSubMenu();
                    n4 = n3;
                    groupId = n;
                    n5 = n2;
                    if (subMenu.hasVisibleItems()) {
                        if (i != 0) {
                            this.mItems.add(NavigationMenuItem.separator(NavigationMenuPresenter.this.mPaddingSeparator, 0));
                        }
                        this.mItems.add(NavigationMenuItem.of(menuItemImpl));
                        int n6 = 0;
                        final int size = this.mItems.size();
                        int n7;
                        for (int j = 0; j < subMenu.size(); ++j, n6 = n7) {
                            final MenuItem item = subMenu.getItem(j);
                            n7 = n6;
                            if (item.isVisible()) {
                                if ((n7 = n6) == 0) {
                                    n7 = n6;
                                    if (item.getIcon() != null) {
                                        n7 = 1;
                                    }
                                }
                                this.mItems.add(NavigationMenuItem.of((MenuItemImpl)item));
                            }
                        }
                        n4 = n3;
                        groupId = n;
                        n5 = n2;
                        if (n6 != 0) {
                            this.appendTransparentIconIfMissing(size, this.mItems.size());
                            n5 = n2;
                            groupId = n;
                            n4 = n3;
                        }
                    }
                }
                else {
                    groupId = menuItemImpl.getGroupId();
                    int n8;
                    if (groupId != n) {
                        final int size2 = this.mItems.size();
                        boolean b;
                        if (menuItemImpl.getIcon() != null) {
                            b = true;
                        }
                        else {
                            b = false;
                        }
                        n4 = (b ? 1 : 0);
                        n8 = size2;
                        if (i != 0) {
                            n8 = size2 + 1;
                            this.mItems.add(NavigationMenuItem.separator(NavigationMenuPresenter.this.mPaddingSeparator, NavigationMenuPresenter.this.mPaddingSeparator));
                            n4 = (b ? 1 : 0);
                        }
                    }
                    else {
                        n4 = n3;
                        n8 = n2;
                        if (n3 == 0) {
                            n4 = n3;
                            n8 = n2;
                            if (menuItemImpl.getIcon() != null) {
                                n4 = 1;
                                this.appendTransparentIconIfMissing(n2, this.mItems.size());
                                n8 = n2;
                            }
                        }
                    }
                    if (n4 != 0 && menuItemImpl.getIcon() == null) {
                        menuItemImpl.setIcon(17170445);
                    }
                    this.mItems.add(NavigationMenuItem.of(menuItemImpl));
                    n5 = n8;
                }
            }
        }
        
        public boolean areAllItemsEnabled() {
            return false;
        }
        
        public int getCount() {
            return this.mItems.size();
        }
        
        public NavigationMenuItem getItem(final int n) {
            return this.mItems.get(n);
        }
        
        public long getItemId(final int n) {
            return n;
        }
        
        public int getItemViewType(final int n) {
            final NavigationMenuItem item = this.getItem(n);
            if (item.isSeparator()) {
                return 2;
            }
            if (item.getMenuItem().hasSubMenu()) {
                return 1;
            }
            return 0;
        }
        
        public View getView(final int n, final View view, final ViewGroup viewGroup) {
            final NavigationMenuItem item = this.getItem(n);
            switch (this.getItemViewType(n)) {
                default: {
                    return view;
                }
                case 0: {
                    View inflate = view;
                    if (view == null) {
                        inflate = NavigationMenuPresenter.this.mLayoutInflater.inflate(R.layout.design_navigation_item, viewGroup, false);
                    }
                    final NavigationMenuItemView navigationMenuItemView = (NavigationMenuItemView)inflate;
                    navigationMenuItemView.setIconTintList(NavigationMenuPresenter.this.mIconTintList);
                    navigationMenuItemView.setTextColor(NavigationMenuPresenter.this.mTextColor);
                    navigationMenuItemView.setBackgroundDrawable(NavigationMenuPresenter.this.mItemBackground);
                    navigationMenuItemView.initialize(item.getMenuItem(), 0);
                    return inflate;
                }
                case 1: {
                    View inflate2 = view;
                    if (view == null) {
                        inflate2 = NavigationMenuPresenter.this.mLayoutInflater.inflate(R.layout.design_navigation_item_subheader, viewGroup, false);
                    }
                    ((TextView)inflate2).setText(item.getMenuItem().getTitle());
                    return inflate2;
                }
                case 2: {
                    View inflate3 = view;
                    if (view == null) {
                        inflate3 = NavigationMenuPresenter.this.mLayoutInflater.inflate(R.layout.design_navigation_item_separator, viewGroup, false);
                    }
                    inflate3.setPadding(0, item.getPaddingTop(), 0, item.getPaddingBottom());
                    return inflate3;
                }
            }
        }
        
        public int getViewTypeCount() {
            return 3;
        }
        
        public boolean isEnabled(final int n) {
            return this.getItem(n).isEnabled();
        }
        
        public void notifyDataSetChanged() {
            this.prepareMenuItems();
            super.notifyDataSetChanged();
        }
    }
    
    private static class NavigationMenuItem
    {
        private final MenuItemImpl mMenuItem;
        private final int mPaddingBottom;
        private final int mPaddingTop;
        
        private NavigationMenuItem(final MenuItemImpl mMenuItem, final int mPaddingTop, final int mPaddingBottom) {
            this.mMenuItem = mMenuItem;
            this.mPaddingTop = mPaddingTop;
            this.mPaddingBottom = mPaddingBottom;
        }
        
        public static NavigationMenuItem of(final MenuItemImpl menuItemImpl) {
            return new NavigationMenuItem(menuItemImpl, 0, 0);
        }
        
        public static NavigationMenuItem separator(final int n, final int n2) {
            return new NavigationMenuItem(null, n, n2);
        }
        
        public MenuItemImpl getMenuItem() {
            return this.mMenuItem;
        }
        
        public int getPaddingBottom() {
            return this.mPaddingBottom;
        }
        
        public int getPaddingTop() {
            return this.mPaddingTop;
        }
        
        public boolean isEnabled() {
            return this.mMenuItem != null && !this.mMenuItem.hasSubMenu() && this.mMenuItem.isEnabled();
        }
        
        public boolean isSeparator() {
            return this.mMenuItem == null;
        }
    }
}
