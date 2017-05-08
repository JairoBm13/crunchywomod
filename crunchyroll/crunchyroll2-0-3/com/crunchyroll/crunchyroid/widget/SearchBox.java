// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.widget;

import android.text.Html;
import java.util.ArrayList;
import android.view.LayoutInflater;
import java.util.List;
import android.widget.BaseAdapter;
import android.view.inputmethod.InputMethodManager;
import android.view.View$OnClickListener;
import android.view.View$OnFocusChangeListener;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView$OnEditorActionListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.app.AlertDialog;
import android.content.DialogInterface$OnShowListener;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.app.AlertDialog$Builder;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemClickListener;
import android.widget.ListAdapter;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import android.view.ViewGroup;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.ImageView;
import com.crunchyroll.crunchyroid.app.ApplicationState;
import android.widget.RelativeLayout;

public class SearchBox extends RelativeLayout
{
    private SearchListener listener;
    private ApplicationState mApplicationState;
    private ImageView mBack;
    private View mDivider;
    private LinearLayout mEditBox;
    private ImageView mFunctionImg;
    private HistoryAdapter mHistoryAdapter;
    private ListView mHistoryListView;
    private EditText mSearchEditor;
    
    public SearchBox(final Context context) {
        this(context, null);
    }
    
    public SearchBox(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public SearchBox(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        inflate(context, 2130903175, (ViewGroup)this);
        this.mApplicationState = ((CrunchyrollApplication)context.getApplicationContext()).getApplicationState();
        this.mSearchEditor = (EditText)this.findViewById(2131624344);
        this.mBack = (ImageView)this.findViewById(2131624343);
        this.mFunctionImg = (ImageView)this.findViewById(2131624345);
        this.mHistoryListView = (ListView)this.findViewById(2131624346);
        this.mDivider = this.findViewById(2131624277);
        this.mSearchEditor.setHint((CharSequence)LocalizedStrings.SEARCH_HINT.get());
        this.mHistoryAdapter = new HistoryAdapter(context);
        this.mHistoryListView.setAdapter((ListAdapter)this.mHistoryAdapter);
        this.mHistoryAdapter.notifyDataSetChanged();
        this.mHistoryListView.setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                if (SearchBox.this.mHistoryAdapter.isClearHistory(n)) {
                    final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder(view.getContext());
                    alertDialog$Builder.setMessage((CharSequence)LocalizedStrings.CLEAR_SEARCH_HISTORY_REQUEST.get());
                    alertDialog$Builder.setNegativeButton((CharSequence)LocalizedStrings.CANCEL.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.YES.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            SearchBox.this.mHistoryAdapter.clearHistory();
                            SearchBox.this.mHistoryAdapter.notifyDataSetChanged();
                        }
                    });
                    final AlertDialog create = alertDialog$Builder.create();
                    create.requestWindowFeature(1);
                    create.setOnShowListener((DialogInterface$OnShowListener)new DialogInterface$OnShowListener() {
                        public void onShow(final DialogInterface dialogInterface) {
                            create.getButton(-2).setTextColor(SearchBox.this.getResources().getColor(2131558477));
                            create.getButton(-1).setTextColor(SearchBox.this.getResources().getColor(2131558477));
                        }
                    });
                    create.show();
                }
                else {
                    final String item = ((HistoryAdapter)adapterView.getAdapter()).getItem(n);
                    SearchBox.this.mSearchEditor.setText((CharSequence)item);
                    SearchBox.this.mHistoryAdapter.addItem(item.trim());
                    SearchBox.this.mHistoryAdapter.updateShownTerms(item);
                    SearchBox.this.mHistoryAdapter.notifyDataSetChanged();
                    if (SearchBox.this.listener != null) {
                        SearchBox.this.listener.onSearch(item);
                    }
                }
                SearchBox.this.closeEdit();
            }
        });
        this.mEditBox = (LinearLayout)this.findViewById(2131624342);
        this.mSearchEditor.addTextChangedListener((TextWatcher)new TextWatcher() {
            public void afterTextChanged(final Editable editable) {
                final String trim = editable.toString().trim();
                if (SearchBox.this.listener != null && trim != null && !trim.isEmpty()) {
                    SearchBox.this.listener.onSearch(trim);
                }
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                SearchBox.this.mHistoryAdapter.updateShownTerms(charSequence.toString());
            }
        });
        this.mSearchEditor.setOnEditorActionListener((TextView$OnEditorActionListener)new TextView$OnEditorActionListener() {
            public boolean onEditorAction(final TextView textView, final int n, final KeyEvent keyEvent) {
                if (keyEvent != null && keyEvent.getKeyCode() == 66) {
                    SearchBox.this.closeEdit();
                }
                if (n == 3) {
                    SearchBox.this.mHistoryAdapter.addItem(SearchBox.this.mSearchEditor.getText().toString().trim());
                    SearchBox.this.mHistoryAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
        this.mSearchEditor.setOnFocusChangeListener((View$OnFocusChangeListener)new View$OnFocusChangeListener() {
            public void onFocusChange(final View view, final boolean b) {
                if (b) {
                    SearchBox.this.onFocus();
                    return;
                }
                SearchBox.this.onClearFocus();
            }
        });
        this.mBack.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (SearchBox.this.isOpen()) {
                    SearchBox.this.closeEdit();
                }
                else if (SearchBox.this.listener != null) {
                    SearchBox.this.listener.onSearchClosed();
                }
            }
        });
        this.mFunctionImg.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (SearchBox.this.mSearchEditor.isFocused()) {
                    SearchBox.this.mSearchEditor.setText((CharSequence)"");
                    if (SearchBox.this.listener != null) {
                        SearchBox.this.listener.onSearchCleared();
                    }
                }
            }
        });
    }
    
    public void clearFocus() {
        this.mSearchEditor.clearFocus();
        this.onClearFocus();
    }
    
    public void closeEdit() {
        this.clearFocus();
        ((InputMethodManager)this.mSearchEditor.getContext().getSystemService("input_method")).hideSoftInputFromWindow(this.mSearchEditor.getWindowToken(), 2);
    }
    
    public LinearLayout getEditBox() {
        return this.mEditBox;
    }
    
    public boolean isOpen() {
        return this.mSearchEditor.isFocused();
    }
    
    public void onClearFocus() {
        this.mDivider.setVisibility(8);
        this.mHistoryListView.setVisibility(8);
        this.mFunctionImg.setVisibility(4);
    }
    
    public void onFocus() {
        this.mHistoryListView.setVisibility(0);
        this.mFunctionImg.setVisibility(0);
        this.updateDivider();
    }
    
    public void setSearchListener(final SearchListener listener) {
        this.listener = listener;
    }
    
    public void updateDivider() {
        if ((this.mHistoryAdapter != null && this.mHistoryAdapter.getCount() == 0) || this.mHistoryListView.getVisibility() == 8) {
            this.mDivider.setVisibility(8);
            return;
        }
        this.mDivider.setVisibility(0);
    }
    
    private class HistoryAdapter extends BaseAdapter
    {
        private static final int NUM = 5;
        private static final int VIEW_CLEAR = 0;
        private static final int VIEW_HISTORY = 1;
        private List<String> mHistoryTerms;
        private LayoutInflater mInflater;
        private String mPrefix;
        private List<String> mShownTerms;
        
        public HistoryAdapter(final Context context) {
            this.mShownTerms = new ArrayList<String>();
            this.mPrefix = "";
            this.mHistoryTerms = SearchBox.this.mApplicationState.getHistoryTerms();
            this.updateShownTerms("");
            this.mInflater = (LayoutInflater)context.getSystemService("layout_inflater");
        }
        
        private void saveHistoryTerms() {
            SearchBox.this.mApplicationState.setHistoryTerms(this.mHistoryTerms);
        }
        
        public void addItem(final String s) {
            if (!this.mHistoryTerms.contains(s)) {
                this.mHistoryTerms.add(0, s);
            }
            else {
                for (int i = 0; i < this.mHistoryTerms.size(); ++i) {
                    if (this.mHistoryTerms.get(i).equals(s)) {
                        this.mHistoryTerms.remove(i);
                        this.mHistoryTerms.add(0, s);
                    }
                }
            }
            if (this.mHistoryTerms.size() > 5) {
                this.mHistoryTerms.remove(this.mHistoryTerms.size() - 1);
            }
            this.saveHistoryTerms();
        }
        
        public void clearHistory() {
            this.mHistoryTerms.clear();
            this.saveHistoryTerms();
            this.updateShownTerms("");
        }
        
        public int getCount() {
            final int size = this.mShownTerms.size();
            if (size == 0) {
                return 0;
            }
            return size + 1;
        }
        
        public String getItem(final int n) {
            if (n == this.getCount() - 1) {
                return "";
            }
            return this.mShownTerms.get(n);
        }
        
        public long getItemId(final int n) {
            return n;
        }
        
        public int getItemViewType(final int n) {
            if (n == this.getCount() - 1) {
                return 0;
            }
            return 1;
        }
        
        public View getView(final int n, final View view, final ViewGroup viewGroup) {
            View view2 = view;
            if (view == null) {
                if (this.getItemViewType(n) == 0) {
                    view2 = this.mInflater.inflate(2130903139, (ViewGroup)null);
                    ((TextView)view2.findViewById(2131624270)).setText((CharSequence)LocalizedStrings.CLEAR_SEARCH_HISTORY.get());
                }
                else {
                    view2 = this.mInflater.inflate(2130903176, (ViewGroup)null);
                }
            }
            if (this.getItemViewType(n) == 1) {
                final TextView textView = (TextView)view2.findViewById(2131624347);
                final String item = this.getItem(n);
                if (!item.isEmpty()) {
                    textView.setText((CharSequence)Html.fromHtml("<font color=#999999>" + item.substring(0, this.mPrefix.length()) + "</font>" + "<font color=#000000>" + item.substring(this.mPrefix.length(), item.length()) + "</font>"));
                }
            }
            return view2;
        }
        
        public int getViewTypeCount() {
            return 2;
        }
        
        public boolean isClearHistory(final int n) {
            return this.getItemViewType(n) == 0;
        }
        
        public void updateShownTerms(final String mPrefix) {
            this.mPrefix = mPrefix;
            this.mShownTerms.clear();
            for (int i = 0; i < this.mHistoryTerms.size(); ++i) {
                final String s = this.mHistoryTerms.get(i);
                if (s.toLowerCase().startsWith(mPrefix.toLowerCase()) || mPrefix.isEmpty()) {
                    this.mShownTerms.add(s);
                }
            }
            this.notifyDataSetChanged();
            SearchBox.this.updateDivider();
        }
    }
    
    public interface SearchListener
    {
        void onSearch(final String p0);
        
        void onSearchCleared();
        
        void onSearchClosed();
        
        void onSearchOpened();
        
        void onSearchTermChanged();
    }
}
