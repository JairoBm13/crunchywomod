// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.app;

import java.lang.ref.WeakReference;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemClickListener;
import android.widget.SimpleCursorAdapter;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.ArrayAdapter;
import android.content.DialogInterface$OnKeyListener;
import android.widget.AdapterView$OnItemSelectedListener;
import android.content.DialogInterface$OnDismissListener;
import android.content.DialogInterface$OnMultiChoiceClickListener;
import android.content.DialogInterface$OnCancelListener;
import android.database.Cursor;
import android.content.DialogInterface$OnClickListener;
import android.view.KeyEvent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.support.v7.internal.widget.TintTypedArray;
import android.text.TextUtils;
import android.view.ViewGroup$LayoutParams;
import android.widget.LinearLayout$LayoutParams;
import android.view.ViewGroup;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.support.v7.appcompat.R;
import android.content.DialogInterface;
import android.view.Window;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ImageView;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.content.Context;
import android.os.Message;
import android.widget.Button;
import android.view.View$OnClickListener;
import android.widget.ListAdapter;

class AlertController
{
    private ListAdapter mAdapter;
    private int mAlertDialogLayout;
    private final View$OnClickListener mButtonHandler;
    private Button mButtonNegative;
    private Message mButtonNegativeMessage;
    private CharSequence mButtonNegativeText;
    private Button mButtonNeutral;
    private Message mButtonNeutralMessage;
    private CharSequence mButtonNeutralText;
    private int mButtonPanelLayoutHint;
    private int mButtonPanelSideLayout;
    private Button mButtonPositive;
    private Message mButtonPositiveMessage;
    private CharSequence mButtonPositiveText;
    private int mCheckedItem;
    private final Context mContext;
    private View mCustomTitleView;
    private final AppCompatDialog mDialog;
    private Handler mHandler;
    private Drawable mIcon;
    private int mIconId;
    private ImageView mIconView;
    private int mListItemLayout;
    private int mListLayout;
    private ListView mListView;
    private CharSequence mMessage;
    private TextView mMessageView;
    private int mMultiChoiceItemLayout;
    private ScrollView mScrollView;
    private int mSingleChoiceItemLayout;
    private CharSequence mTitle;
    private TextView mTitleView;
    private View mView;
    private int mViewLayoutResId;
    private int mViewSpacingBottom;
    private int mViewSpacingLeft;
    private int mViewSpacingRight;
    private boolean mViewSpacingSpecified;
    private int mViewSpacingTop;
    private final Window mWindow;
    
    public AlertController(final Context mContext, final AppCompatDialog mDialog, final Window mWindow) {
        this.mViewSpacingSpecified = false;
        this.mIconId = 0;
        this.mCheckedItem = -1;
        this.mButtonPanelLayoutHint = 0;
        this.mButtonHandler = (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                Message message;
                if (view == AlertController.this.mButtonPositive && AlertController.this.mButtonPositiveMessage != null) {
                    message = Message.obtain(AlertController.this.mButtonPositiveMessage);
                }
                else if (view == AlertController.this.mButtonNegative && AlertController.this.mButtonNegativeMessage != null) {
                    message = Message.obtain(AlertController.this.mButtonNegativeMessage);
                }
                else if (view == AlertController.this.mButtonNeutral && AlertController.this.mButtonNeutralMessage != null) {
                    message = Message.obtain(AlertController.this.mButtonNeutralMessage);
                }
                else {
                    message = null;
                }
                if (message != null) {
                    message.sendToTarget();
                }
                AlertController.this.mHandler.obtainMessage(1, (Object)AlertController.this.mDialog).sendToTarget();
            }
        };
        this.mContext = mContext;
        this.mDialog = mDialog;
        this.mWindow = mWindow;
        this.mHandler = new ButtonHandler((DialogInterface)mDialog);
        final TypedArray obtainStyledAttributes = mContext.obtainStyledAttributes((AttributeSet)null, R.styleable.AlertDialog, R.attr.alertDialogStyle, 0);
        this.mAlertDialogLayout = obtainStyledAttributes.getResourceId(R.styleable.AlertDialog_android_layout, 0);
        this.mButtonPanelSideLayout = obtainStyledAttributes.getResourceId(R.styleable.AlertDialog_buttonPanelSideLayout, 0);
        this.mListLayout = obtainStyledAttributes.getResourceId(R.styleable.AlertDialog_listLayout, 0);
        this.mMultiChoiceItemLayout = obtainStyledAttributes.getResourceId(R.styleable.AlertDialog_multiChoiceItemLayout, 0);
        this.mSingleChoiceItemLayout = obtainStyledAttributes.getResourceId(R.styleable.AlertDialog_singleChoiceItemLayout, 0);
        this.mListItemLayout = obtainStyledAttributes.getResourceId(R.styleable.AlertDialog_listItemLayout, 0);
        obtainStyledAttributes.recycle();
    }
    
    static boolean canTextInput(final View view) {
        if (view.onCheckIsTextEditor()) {
            return true;
        }
        if (!(view instanceof ViewGroup)) {
            return false;
        }
        final ViewGroup viewGroup = (ViewGroup)view;
        int i = viewGroup.getChildCount();
        while (i > 0) {
            if (canTextInput(viewGroup.getChildAt(--i))) {
                return true;
            }
        }
        return false;
    }
    
    private void centerButton(final Button button) {
        final LinearLayout$LayoutParams layoutParams = (LinearLayout$LayoutParams)button.getLayoutParams();
        layoutParams.gravity = 1;
        layoutParams.weight = 0.5f;
        button.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
    }
    
    private int selectContentView() {
        if (this.mButtonPanelSideLayout == 0) {
            return this.mAlertDialogLayout;
        }
        if (this.mButtonPanelLayoutHint == 1) {
            return this.mButtonPanelSideLayout;
        }
        return this.mAlertDialogLayout;
    }
    
    private boolean setupButtons() {
        int n = 0;
        (this.mButtonPositive = (Button)this.mWindow.findViewById(16908313)).setOnClickListener(this.mButtonHandler);
        if (TextUtils.isEmpty(this.mButtonPositiveText)) {
            this.mButtonPositive.setVisibility(8);
        }
        else {
            this.mButtonPositive.setText(this.mButtonPositiveText);
            this.mButtonPositive.setVisibility(0);
            n = ((false | true) ? 1 : 0);
        }
        (this.mButtonNegative = (Button)this.mWindow.findViewById(16908314)).setOnClickListener(this.mButtonHandler);
        if (TextUtils.isEmpty(this.mButtonNegativeText)) {
            this.mButtonNegative.setVisibility(8);
        }
        else {
            this.mButtonNegative.setText(this.mButtonNegativeText);
            this.mButtonNegative.setVisibility(0);
            n |= 0x2;
        }
        (this.mButtonNeutral = (Button)this.mWindow.findViewById(16908315)).setOnClickListener(this.mButtonHandler);
        if (TextUtils.isEmpty(this.mButtonNeutralText)) {
            this.mButtonNeutral.setVisibility(8);
        }
        else {
            this.mButtonNeutral.setText(this.mButtonNeutralText);
            this.mButtonNeutral.setVisibility(0);
            n |= 0x4;
        }
        if (shouldCenterSingleButton(this.mContext)) {
            if (n == 1) {
                this.centerButton(this.mButtonPositive);
            }
            else if (n == 2) {
                this.centerButton(this.mButtonNegative);
            }
            else if (n == 4) {
                this.centerButton(this.mButtonNeutral);
            }
        }
        return n != 0;
    }
    
    private void setupContent(ViewGroup viewGroup) {
        (this.mScrollView = (ScrollView)this.mWindow.findViewById(R.id.scrollView)).setFocusable(false);
        this.mMessageView = (TextView)this.mWindow.findViewById(16908299);
        if (this.mMessageView == null) {
            return;
        }
        if (this.mMessage != null) {
            this.mMessageView.setText(this.mMessage);
            return;
        }
        this.mMessageView.setVisibility(8);
        this.mScrollView.removeView((View)this.mMessageView);
        if (this.mListView != null) {
            viewGroup = (ViewGroup)this.mScrollView.getParent();
            final int indexOfChild = viewGroup.indexOfChild((View)this.mScrollView);
            viewGroup.removeViewAt(indexOfChild);
            viewGroup.addView((View)this.mListView, indexOfChild, new ViewGroup$LayoutParams(-1, -1));
            return;
        }
        viewGroup.setVisibility(8);
    }
    
    private boolean setupTitle(final ViewGroup viewGroup) {
        boolean b = false;
        if (this.mCustomTitleView != null) {
            viewGroup.addView(this.mCustomTitleView, 0, new ViewGroup$LayoutParams(-1, -2));
            this.mWindow.findViewById(R.id.title_template).setVisibility(8);
            return true;
        }
        this.mIconView = (ImageView)this.mWindow.findViewById(16908294);
        if (!TextUtils.isEmpty(this.mTitle)) {
            b = true;
        }
        if (!b) {
            this.mWindow.findViewById(R.id.title_template).setVisibility(8);
            this.mIconView.setVisibility(8);
            viewGroup.setVisibility(8);
            return false;
        }
        (this.mTitleView = (TextView)this.mWindow.findViewById(R.id.alertTitle)).setText(this.mTitle);
        if (this.mIconId != 0) {
            this.mIconView.setImageResource(this.mIconId);
            return true;
        }
        if (this.mIcon != null) {
            this.mIconView.setImageDrawable(this.mIcon);
            return true;
        }
        this.mTitleView.setPadding(this.mIconView.getPaddingLeft(), this.mIconView.getPaddingTop(), this.mIconView.getPaddingRight(), this.mIconView.getPaddingBottom());
        this.mIconView.setVisibility(8);
        return true;
    }
    
    private void setupView() {
        this.setupContent((ViewGroup)this.mWindow.findViewById(R.id.contentPanel));
        final boolean setupButtons = this.setupButtons();
        final ViewGroup viewGroup = (ViewGroup)this.mWindow.findViewById(R.id.topPanel);
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.mContext, null, R.styleable.AlertDialog, R.attr.alertDialogStyle, 0);
        this.setupTitle(viewGroup);
        final View viewById = this.mWindow.findViewById(R.id.buttonPanel);
        if (!setupButtons) {
            viewById.setVisibility(8);
            final View viewById2 = this.mWindow.findViewById(R.id.textSpacerNoButtons);
            if (viewById2 != null) {
                viewById2.setVisibility(0);
            }
        }
        final FrameLayout frameLayout = (FrameLayout)this.mWindow.findViewById(R.id.customPanel);
        View view;
        if (this.mView != null) {
            view = this.mView;
        }
        else if (this.mViewLayoutResId != 0) {
            view = LayoutInflater.from(this.mContext).inflate(this.mViewLayoutResId, (ViewGroup)frameLayout, false);
        }
        else {
            view = null;
        }
        boolean b;
        if (view != null) {
            b = true;
        }
        else {
            b = false;
        }
        if (!b || !canTextInput(view)) {
            this.mWindow.setFlags(131072, 131072);
        }
        if (b) {
            final FrameLayout frameLayout2 = (FrameLayout)this.mWindow.findViewById(R.id.custom);
            frameLayout2.addView(view, new ViewGroup$LayoutParams(-1, -1));
            if (this.mViewSpacingSpecified) {
                frameLayout2.setPadding(this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
            }
            if (this.mListView != null) {
                ((LinearLayout$LayoutParams)frameLayout.getLayoutParams()).weight = 0.0f;
            }
        }
        else {
            frameLayout.setVisibility(8);
        }
        final ListView mListView = this.mListView;
        if (mListView != null && this.mAdapter != null) {
            mListView.setAdapter(this.mAdapter);
            final int mCheckedItem = this.mCheckedItem;
            if (mCheckedItem > -1) {
                mListView.setItemChecked(mCheckedItem, true);
                mListView.setSelection(mCheckedItem);
            }
        }
        obtainStyledAttributes.recycle();
    }
    
    private static boolean shouldCenterSingleButton(final Context context) {
        final TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.alertDialogCenterButtons, typedValue, true);
        return typedValue.data != 0;
    }
    
    public Button getButton(final int n) {
        switch (n) {
            default: {
                return null;
            }
            case -1: {
                return this.mButtonPositive;
            }
            case -2: {
                return this.mButtonNegative;
            }
            case -3: {
                return this.mButtonNeutral;
            }
        }
    }
    
    public int getIconAttributeResId(final int n) {
        final TypedValue typedValue = new TypedValue();
        this.mContext.getTheme().resolveAttribute(n, typedValue, true);
        return typedValue.resourceId;
    }
    
    public ListView getListView() {
        return this.mListView;
    }
    
    public void installContent() {
        this.mDialog.supportRequestWindowFeature(1);
        this.mDialog.setContentView(this.selectContentView());
        this.setupView();
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        return this.mScrollView != null && this.mScrollView.executeKeyEvent(keyEvent);
    }
    
    public boolean onKeyUp(final int n, final KeyEvent keyEvent) {
        return this.mScrollView != null && this.mScrollView.executeKeyEvent(keyEvent);
    }
    
    public void setButton(final int n, final CharSequence mButtonNeutralText, final DialogInterface$OnClickListener dialogInterface$OnClickListener, final Message message) {
        Message obtainMessage = message;
        if (message == null) {
            obtainMessage = message;
            if (dialogInterface$OnClickListener != null) {
                obtainMessage = this.mHandler.obtainMessage(n, (Object)dialogInterface$OnClickListener);
            }
        }
        switch (n) {
            default: {
                throw new IllegalArgumentException("Button does not exist");
            }
            case -1: {
                this.mButtonPositiveText = mButtonNeutralText;
                this.mButtonPositiveMessage = obtainMessage;
            }
            case -2: {
                this.mButtonNegativeText = mButtonNeutralText;
                this.mButtonNegativeMessage = obtainMessage;
            }
            case -3: {
                this.mButtonNeutralText = mButtonNeutralText;
                this.mButtonNeutralMessage = obtainMessage;
            }
        }
    }
    
    public void setButtonPanelLayoutHint(final int mButtonPanelLayoutHint) {
        this.mButtonPanelLayoutHint = mButtonPanelLayoutHint;
    }
    
    public void setCustomTitle(final View mCustomTitleView) {
        this.mCustomTitleView = mCustomTitleView;
    }
    
    public void setIcon(final int mIconId) {
        this.mIcon = null;
        this.mIconId = mIconId;
        if (this.mIconView != null) {
            if (mIconId == 0) {
                this.mIconView.setVisibility(8);
                return;
            }
            this.mIconView.setImageResource(this.mIconId);
        }
    }
    
    public void setIcon(final Drawable drawable) {
        this.mIcon = drawable;
        this.mIconId = 0;
        if (this.mIconView != null) {
            if (drawable == null) {
                this.mIconView.setVisibility(8);
                return;
            }
            this.mIconView.setImageDrawable(drawable);
        }
    }
    
    public void setMessage(final CharSequence charSequence) {
        this.mMessage = charSequence;
        if (this.mMessageView != null) {
            this.mMessageView.setText(charSequence);
        }
    }
    
    public void setTitle(final CharSequence charSequence) {
        this.mTitle = charSequence;
        if (this.mTitleView != null) {
            this.mTitleView.setText(charSequence);
        }
    }
    
    public void setView(final int mViewLayoutResId) {
        this.mView = null;
        this.mViewLayoutResId = mViewLayoutResId;
        this.mViewSpacingSpecified = false;
    }
    
    public void setView(final View mView) {
        this.mView = mView;
        this.mViewLayoutResId = 0;
        this.mViewSpacingSpecified = false;
    }
    
    public void setView(final View mView, final int mViewSpacingLeft, final int mViewSpacingTop, final int mViewSpacingRight, final int mViewSpacingBottom) {
        this.mView = mView;
        this.mViewLayoutResId = 0;
        this.mViewSpacingSpecified = true;
        this.mViewSpacingLeft = mViewSpacingLeft;
        this.mViewSpacingTop = mViewSpacingTop;
        this.mViewSpacingRight = mViewSpacingRight;
        this.mViewSpacingBottom = mViewSpacingBottom;
    }
    
    public static class AlertParams
    {
        public ListAdapter mAdapter;
        public boolean mCancelable;
        public int mCheckedItem;
        public boolean[] mCheckedItems;
        public final Context mContext;
        public Cursor mCursor;
        public View mCustomTitleView;
        public boolean mForceInverseBackground;
        public Drawable mIcon;
        public int mIconAttrId;
        public int mIconId;
        public final LayoutInflater mInflater;
        public String mIsCheckedColumn;
        public boolean mIsMultiChoice;
        public boolean mIsSingleChoice;
        public CharSequence[] mItems;
        public String mLabelColumn;
        public CharSequence mMessage;
        public DialogInterface$OnClickListener mNegativeButtonListener;
        public CharSequence mNegativeButtonText;
        public DialogInterface$OnClickListener mNeutralButtonListener;
        public CharSequence mNeutralButtonText;
        public DialogInterface$OnCancelListener mOnCancelListener;
        public DialogInterface$OnMultiChoiceClickListener mOnCheckboxClickListener;
        public DialogInterface$OnClickListener mOnClickListener;
        public DialogInterface$OnDismissListener mOnDismissListener;
        public AdapterView$OnItemSelectedListener mOnItemSelectedListener;
        public DialogInterface$OnKeyListener mOnKeyListener;
        public OnPrepareListViewListener mOnPrepareListViewListener;
        public DialogInterface$OnClickListener mPositiveButtonListener;
        public CharSequence mPositiveButtonText;
        public boolean mRecycleOnMeasure;
        public CharSequence mTitle;
        public View mView;
        public int mViewLayoutResId;
        public int mViewSpacingBottom;
        public int mViewSpacingLeft;
        public int mViewSpacingRight;
        public boolean mViewSpacingSpecified;
        public int mViewSpacingTop;
        
        public AlertParams(final Context mContext) {
            this.mIconId = 0;
            this.mIconAttrId = 0;
            this.mViewSpacingSpecified = false;
            this.mCheckedItem = -1;
            this.mRecycleOnMeasure = true;
            this.mContext = mContext;
            this.mCancelable = true;
            this.mInflater = (LayoutInflater)mContext.getSystemService("layout_inflater");
        }
        
        private void createListView(final AlertController alertController) {
            final ListView listView = (ListView)this.mInflater.inflate(alertController.mListLayout, (ViewGroup)null);
            Object mAdapter;
            if (this.mIsMultiChoice) {
                if (this.mCursor == null) {
                    mAdapter = new ArrayAdapter<CharSequence>(this.mContext, alertController.mMultiChoiceItemLayout, 16908308, this.mItems) {
                        public View getView(final int n, View view, final ViewGroup viewGroup) {
                            view = super.getView(n, view, viewGroup);
                            if (AlertParams.this.mCheckedItems != null && AlertParams.this.mCheckedItems[n]) {
                                listView.setItemChecked(n, true);
                            }
                            return view;
                        }
                    };
                }
                else {
                    mAdapter = new CursorAdapter(this.mContext, this.mCursor, false) {
                        private final int mIsCheckedIndex;
                        private final int mLabelIndex;
                        
                        {
                            final Cursor cursor2 = this.getCursor();
                            this.mLabelIndex = cursor2.getColumnIndexOrThrow(AlertParams.this.mLabelColumn);
                            this.mIsCheckedIndex = cursor2.getColumnIndexOrThrow(AlertParams.this.mIsCheckedColumn);
                        }
                        
                        public void bindView(final View view, final Context context, final Cursor cursor) {
                            boolean b = true;
                            ((CheckedTextView)view.findViewById(16908308)).setText((CharSequence)cursor.getString(this.mLabelIndex));
                            final ListView val$listView = listView;
                            final int position = cursor.getPosition();
                            if (cursor.getInt(this.mIsCheckedIndex) != 1) {
                                b = false;
                            }
                            val$listView.setItemChecked(position, b);
                        }
                        
                        public View newView(final Context context, final Cursor cursor, final ViewGroup viewGroup) {
                            return AlertParams.this.mInflater.inflate(alertController.mMultiChoiceItemLayout, viewGroup, false);
                        }
                    };
                }
            }
            else {
                int n;
                if (this.mIsSingleChoice) {
                    n = alertController.mSingleChoiceItemLayout;
                }
                else {
                    n = alertController.mListItemLayout;
                }
                if (this.mCursor == null) {
                    if (this.mAdapter != null) {
                        mAdapter = this.mAdapter;
                    }
                    else {
                        mAdapter = new CheckedItemAdapter(this.mContext, n, 16908308, this.mItems);
                    }
                }
                else {
                    mAdapter = new SimpleCursorAdapter(this.mContext, n, this.mCursor, new String[] { this.mLabelColumn }, new int[] { 16908308 });
                }
            }
            if (this.mOnPrepareListViewListener != null) {
                this.mOnPrepareListViewListener.onPrepareListView(listView);
            }
            alertController.mAdapter = (ListAdapter)mAdapter;
            alertController.mCheckedItem = this.mCheckedItem;
            if (this.mOnClickListener != null) {
                listView.setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
                    public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                        AlertParams.this.mOnClickListener.onClick((DialogInterface)alertController.mDialog, n);
                        if (!AlertParams.this.mIsSingleChoice) {
                            alertController.mDialog.dismiss();
                        }
                    }
                });
            }
            else if (this.mOnCheckboxClickListener != null) {
                listView.setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
                    public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                        if (AlertParams.this.mCheckedItems != null) {
                            AlertParams.this.mCheckedItems[n] = listView.isItemChecked(n);
                        }
                        AlertParams.this.mOnCheckboxClickListener.onClick((DialogInterface)alertController.mDialog, n, listView.isItemChecked(n));
                    }
                });
            }
            if (this.mOnItemSelectedListener != null) {
                listView.setOnItemSelectedListener(this.mOnItemSelectedListener);
            }
            if (this.mIsSingleChoice) {
                listView.setChoiceMode(1);
            }
            else if (this.mIsMultiChoice) {
                listView.setChoiceMode(2);
            }
            alertController.mListView = listView;
        }
        
        public void apply(final AlertController alertController) {
            if (this.mCustomTitleView != null) {
                alertController.setCustomTitle(this.mCustomTitleView);
            }
            else {
                if (this.mTitle != null) {
                    alertController.setTitle(this.mTitle);
                }
                if (this.mIcon != null) {
                    alertController.setIcon(this.mIcon);
                }
                if (this.mIconId != 0) {
                    alertController.setIcon(this.mIconId);
                }
                if (this.mIconAttrId != 0) {
                    alertController.setIcon(alertController.getIconAttributeResId(this.mIconAttrId));
                }
            }
            if (this.mMessage != null) {
                alertController.setMessage(this.mMessage);
            }
            if (this.mPositiveButtonText != null) {
                alertController.setButton(-1, this.mPositiveButtonText, this.mPositiveButtonListener, null);
            }
            if (this.mNegativeButtonText != null) {
                alertController.setButton(-2, this.mNegativeButtonText, this.mNegativeButtonListener, null);
            }
            if (this.mNeutralButtonText != null) {
                alertController.setButton(-3, this.mNeutralButtonText, this.mNeutralButtonListener, null);
            }
            if (this.mItems != null || this.mCursor != null || this.mAdapter != null) {
                this.createListView(alertController);
            }
            if (this.mView != null) {
                if (!this.mViewSpacingSpecified) {
                    alertController.setView(this.mView);
                    return;
                }
                alertController.setView(this.mView, this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
            }
            else if (this.mViewLayoutResId != 0) {
                alertController.setView(this.mViewLayoutResId);
            }
        }
        
        public interface OnPrepareListViewListener
        {
            void onPrepareListView(final ListView p0);
        }
    }
    
    private static final class ButtonHandler extends Handler
    {
        private static final int MSG_DISMISS_DIALOG = 1;
        private WeakReference<DialogInterface> mDialog;
        
        public ButtonHandler(final DialogInterface dialogInterface) {
            this.mDialog = new WeakReference<DialogInterface>(dialogInterface);
        }
        
        public void handleMessage(final Message message) {
            switch (message.what) {
                default: {}
                case -3:
                case -2:
                case -1: {
                    ((DialogInterface$OnClickListener)message.obj).onClick((DialogInterface)this.mDialog.get(), message.what);
                }
                case 1: {
                    ((DialogInterface)message.obj).dismiss();
                }
            }
        }
    }
    
    private static class CheckedItemAdapter extends ArrayAdapter<CharSequence>
    {
        public CheckedItemAdapter(final Context context, final int n, final int n2, final CharSequence[] array) {
            super(context, n, n2, (Object[])array);
        }
        
        public long getItemId(final int n) {
            return n;
        }
        
        public boolean hasStableIds() {
            return true;
        }
    }
}
