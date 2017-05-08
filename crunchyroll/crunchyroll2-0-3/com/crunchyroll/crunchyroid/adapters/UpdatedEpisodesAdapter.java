// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.res.TypedArray;
import com.crunchyroll.crunchyroid.events.LoadMore;
import com.crunchyroll.crunchyroid.activities.SeriesDetailActivity;
import com.crunchyroll.crunchyroid.activities.PopupActivity;
import android.support.v4.app.Fragment;
import com.crunchyroll.crunchyroid.events.PopupNewFragmentEvent;
import com.crunchyroll.crunchyroid.fragments.MediaInfoFragment;
import de.greenrobot.event.EventBus;
import com.crunchyroll.crunchyroid.util.ActionsUtil;
import android.view.MenuItem;
import android.widget.PopupMenu$OnMenuItemClickListener;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.widget.PopupMenu;
import com.crunchyroll.android.api.models.Series;
import android.view.ViewGroup$LayoutParams;
import android.widget.LinearLayout$LayoutParams;
import com.crunchyroll.crunchyroid.fragments.EpisodeListBaseFragment;
import com.google.common.base.Optional;
import com.crunchyroll.crunchyroid.app.WatchStatus;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import com.crunchyroll.crunchyroid.util.Util;
import com.crunchyroll.crunchyroid.app.PrepareToWatch;
import com.crunchyroll.android.api.tasks.BaseListener;
import android.app.Activity;
import android.content.Context;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import android.view.View;
import com.crunchyroll.android.api.models.Media;
import android.view.View$OnClickListener;
import android.support.v7.widget.RecyclerView;
import com.google.common.collect.Lists;
import com.crunchyroll.android.api.models.UpdatedEpisode;
import java.util.List;
import com.crunchyroll.crunchyroid.viewHolders.ListItemLoadingViewHolder;
import android.support.v4.app.FragmentActivity;

public class UpdatedEpisodesAdapter extends MediaCardAdapter
{
    public static final int VIEW_CALENDER_LINE = 3;
    public static final int VIEW_HEADER = 2;
    private FragmentActivity mActivity;
    private ListItemLoadingViewHolder mListItemLoadingViewHolder;
    private LoadingState mLoadingState;
    private List<UpdatedEpisodeEntry> mUpdatedEpisodeEntries;
    private List<UpdatedEpisode> mUpdatedEpisodes;
    
    public UpdatedEpisodesAdapter(final FragmentActivity mActivity, final List<UpdatedEpisode> mUpdatedEpisodes, final int n) {
        this.mLoadingState = LoadingState.LOADING_STATE_LOADING;
        this.mActivity = mActivity;
        this.mUpdatedEpisodes = mUpdatedEpisodes;
        this.mUpdatedEpisodeEntries = (List<UpdatedEpisodeEntry>)Lists.newArrayList();
        this.notifyEpisodesChanged(n);
    }
    
    @Override
    public int getItemCount() {
        final int size = this.mUpdatedEpisodeEntries.size();
        if (size == 0) {
            return 0;
        }
        int n = size;
        if (this.mLoadingState != LoadingState.LOADING_STATE_NONE) {
            n = size + 1;
        }
        return n;
    }
    
    @Override
    public int getItemViewType(final int n) {
        if (n == this.mUpdatedEpisodeEntries.size()) {
            return 1;
        }
        return this.mUpdatedEpisodeEntries.get(n).getType();
    }
    
    public List<UpdatedEpisodeEntry> getUpdatedEpisodeEntries() {
        return this.mUpdatedEpisodeEntries;
    }
    
    @Override
    public void hideLoading() {
        this.mLoadingState = LoadingState.LOADING_STATE_NONE;
    }
    
    public List<UpdatedEpisodeEntry> notifyEpisodesChanged(final int p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: getfield        com/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter.mUpdatedEpisodeEntries:Ljava/util/List;
        //     4: invokeinterface java/util/List.clear:()V
        //     9: ldc             ""
        //    11: astore          5
        //    13: iconst_0       
        //    14: istore_2       
        //    15: aload_0        
        //    16: getfield        com/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter.mUpdatedEpisodes:Ljava/util/List;
        //    19: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //    24: astore          9
        //    26: aload           9
        //    28: invokeinterface java/util/Iterator.hasNext:()Z
        //    33: ifeq            319
        //    36: aload           9
        //    38: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    43: checkcast       Lcom/crunchyroll/android/api/models/UpdatedEpisode;
        //    46: astore          10
        //    48: aload           10
        //    50: invokevirtual   com/crunchyroll/android/api/models/UpdatedEpisode.getMedia:()Lcom/crunchyroll/android/api/models/Media;
        //    53: invokevirtual   com/crunchyroll/android/api/models/Media.getAvailableTime:()Ljava/lang/String;
        //    56: iconst_0       
        //    57: bipush          19
        //    59: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //    62: astore          6
        //    64: aload           6
        //    66: invokestatic    com/crunchyroll/crunchyroid/util/Util.createUpdatedTimeInfo:(Ljava/lang/String;)Ljava/lang/String;
        //    69: astore          11
        //    71: aload           6
        //    73: invokestatic    com/crunchyroll/crunchyroid/util/Util.getDateInfo:(Ljava/lang/String;)Ljava/lang/String;
        //    76: astore          7
        //    78: aload           6
        //    80: invokestatic    com/crunchyroll/crunchyroid/util/Util.dayOfWeek:(Ljava/lang/String;)I
        //    83: istore          4
        //    85: aload           5
        //    87: invokevirtual   java/lang/String.isEmpty:()Z
        //    90: ifeq            244
        //    93: new             Lcom/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter$UpdatedEpisodeEntry;
        //    96: dup            
        //    97: aload_0        
        //    98: invokespecial   com/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter$UpdatedEpisodeEntry.<init>:(Lcom/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter;)V
        //   101: astore          8
        //   103: aload           8
        //   105: astore          6
        //   107: aload           8
        //   109: iconst_2       
        //   110: invokevirtual   com/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter$UpdatedEpisodeEntry.setViewType:(I)V
        //   113: aload           8
        //   115: astore          6
        //   117: aload           8
        //   119: getstatic       com/crunchyroll/crunchyroid/app/LocalizedStrings.JUST_ARRIVED:Lcom/crunchyroll/crunchyroid/app/LocalizedStrings;
        //   122: invokevirtual   com/crunchyroll/crunchyroid/app/LocalizedStrings.get:()Ljava/lang/String;
        //   125: invokevirtual   java/lang/String.toUpperCase:()Ljava/lang/String;
        //   128: invokevirtual   com/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter$UpdatedEpisodeEntry.setDayName:(Ljava/lang/String;)V
        //   131: aload           8
        //   133: astore          6
        //   135: aload           8
        //   137: iload           4
        //   139: invokevirtual   com/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter$UpdatedEpisodeEntry.setDay:(I)V
        //   142: aload           8
        //   144: astore          6
        //   146: aload_0        
        //   147: getfield        com/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter.mUpdatedEpisodeEntries:Ljava/util/List;
        //   150: aload           8
        //   152: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   157: pop            
        //   158: iconst_0       
        //   159: istore_3       
        //   160: aload           7
        //   162: astore          5
        //   164: iload_3        
        //   165: istore_2       
        //   166: iload_2        
        //   167: iload_1        
        //   168: iconst_1       
        //   169: isub           
        //   170: irem           
        //   171: ifne            202
        //   174: new             Lcom/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter$UpdatedEpisodeEntry;
        //   177: dup            
        //   178: aload_0        
        //   179: invokespecial   com/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter$UpdatedEpisodeEntry.<init>:(Lcom/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter;)V
        //   182: astore          6
        //   184: aload           6
        //   186: iconst_3       
        //   187: invokevirtual   com/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter$UpdatedEpisodeEntry.setViewType:(I)V
        //   190: aload_0        
        //   191: getfield        com/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter.mUpdatedEpisodeEntries:Ljava/util/List;
        //   194: aload           6
        //   196: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   201: pop            
        //   202: new             Lcom/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter$UpdatedEpisodeEntry;
        //   205: dup            
        //   206: aload_0        
        //   207: invokespecial   com/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter$UpdatedEpisodeEntry.<init>:(Lcom/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter;)V
        //   210: astore          6
        //   212: aload           6
        //   214: iconst_0       
        //   215: invokevirtual   com/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter$UpdatedEpisodeEntry.setViewType:(I)V
        //   218: aload           6
        //   220: aload           10
        //   222: invokevirtual   com/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter$UpdatedEpisodeEntry.setUpdatedEpisode:(Lcom/crunchyroll/android/api/models/UpdatedEpisode;)V
        //   225: aload_0        
        //   226: getfield        com/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter.mUpdatedEpisodeEntries:Ljava/util/List;
        //   229: aload           6
        //   231: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   236: pop            
        //   237: iload_2        
        //   238: iconst_1       
        //   239: iadd           
        //   240: istore_2       
        //   241: goto            26
        //   244: iload_2        
        //   245: istore_3       
        //   246: aload           5
        //   248: aload           7
        //   250: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   253: ifne            160
        //   256: new             Lcom/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter$UpdatedEpisodeEntry;
        //   259: dup            
        //   260: aload_0        
        //   261: invokespecial   com/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter$UpdatedEpisodeEntry.<init>:(Lcom/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter;)V
        //   264: astore          8
        //   266: aload           8
        //   268: astore          6
        //   270: aload           8
        //   272: iconst_2       
        //   273: invokevirtual   com/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter$UpdatedEpisodeEntry.setViewType:(I)V
        //   276: aload           8
        //   278: astore          6
        //   280: aload           8
        //   282: aload           11
        //   284: invokevirtual   com/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter$UpdatedEpisodeEntry.setDayName:(Ljava/lang/String;)V
        //   287: aload           8
        //   289: astore          6
        //   291: aload           8
        //   293: iload           4
        //   295: invokevirtual   com/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter$UpdatedEpisodeEntry.setDay:(I)V
        //   298: aload           8
        //   300: astore          6
        //   302: aload_0        
        //   303: getfield        com/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter.mUpdatedEpisodeEntries:Ljava/util/List;
        //   306: aload           8
        //   308: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   313: pop            
        //   314: iconst_0       
        //   315: istore_3       
        //   316: goto            160
        //   319: aload_0        
        //   320: getfield        com/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter.mUpdatedEpisodeEntries:Ljava/util/List;
        //   323: areturn        
        //   324: astore          6
        //   326: goto            166
        //   329: astore          6
        //   331: goto            166
        //    Signature:
        //  (I)Ljava/util/List<Lcom/crunchyroll/crunchyroid/adapters/UpdatedEpisodesAdapter$UpdatedEpisodeEntry;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                      
        //  -----  -----  -----  -----  --------------------------
        //  64     103    324    329    Ljava/text/ParseException;
        //  107    113    329    334    Ljava/text/ParseException;
        //  117    131    329    334    Ljava/text/ParseException;
        //  135    142    329    334    Ljava/text/ParseException;
        //  146    158    329    334    Ljava/text/ParseException;
        //  246    266    324    329    Ljava/text/ParseException;
        //  270    276    329    334    Ljava/text/ParseException;
        //  280    287    329    334    Ljava/text/ParseException;
        //  291    298    329    334    Ljava/text/ParseException;
        //  302    314    329    334    Ljava/text/ParseException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0160:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int backgroundResource) {
        if (viewHolder instanceof NormalViewHolder) {
            final UpdatedEpisode updatedEpisode = this.mUpdatedEpisodeEntries.get(backgroundResource).getUpdatedEpisode();
            final Media media = updatedEpisode.getMedia();
            final Series series = updatedEpisode.getSeries();
            if (media != null) {
                viewHolder.itemView.setFocusable(true);
                final TypedArray obtainStyledAttributes = this.mActivity.obtainStyledAttributes(new int[] { 2130772199 });
                backgroundResource = obtainStyledAttributes.getResourceId(0, 0);
                viewHolder.itemView.setBackgroundResource(backgroundResource);
                obtainStyledAttributes.recycle();
                final String name = media.getName();
                String text = media.getSeriesName().get();
                ((NormalViewHolder)viewHolder).itemView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        boolean b = false;
                        if (!CrunchyrollApplication.getApp((Context)UpdatedEpisodesAdapter.this.mActivity).isPrepareToWatchLoading()) {
                            final PrepareToWatch prepareToWatch = CrunchyrollApplication.getApp((Context)UpdatedEpisodesAdapter.this.mActivity).prepareToWatch(UpdatedEpisodesAdapter.this.mActivity, media, false, 0);
                            prepareToWatch.prepare(new BaseListener<Void>() {
                                @Override
                                public void onException(final Exception ex) {
                                }
                                
                                @Override
                                public void onFinally() {
                                    Util.hideProgressBar(UpdatedEpisodesAdapter.this.mActivity);
                                }
                                
                                @Override
                                public void onPreExecute() {
                                    Util.showProgressBar(UpdatedEpisodesAdapter.this.mActivity, UpdatedEpisodesAdapter.this.mActivity.getResources().getColor(2131558518));
                                }
                                
                                @Override
                                public void onSuccess(final Void void1) {
                                    prepareToWatch.go(PrepareToWatch.Event.NONE);
                                }
                            });
                            Tracker.screenEpisodeSelected("main-new", text, "episode-" + media.getEpisodeNumber());
                            final String val$seriesName = text;
                            final String string = "episode-" + media.getEpisodeNumber();
                            if (WatchStatus.getVideoType(media) == 2) {
                                b = true;
                            }
                            Tracker.episodeSelected(val$seriesName, string, b);
                        }
                    }
                });
                final Optional<Media> fromNullable = Optional.fromNullable(media);
                EpisodeListBaseFragment.loadLargeImageIntoImageView((Context)this.mActivity, fromNullable, ((NormalViewHolder)viewHolder).image);
                int n = 0;
                backgroundResource = 1;
                if (fromNullable.isPresent()) {
                    n = (int)(fromNullable.get().getPercentWatched() * 100.0);
                    backgroundResource = 100 - n;
                }
                if (n == 0) {
                    ((NormalViewHolder)viewHolder).mediaProgress.setVisibility(8);
                }
                else {
                    ((NormalViewHolder)viewHolder).mediaProgressPercent.setLayoutParams((ViewGroup$LayoutParams)new LinearLayout$LayoutParams(0, -1, (float)n));
                    ((NormalViewHolder)viewHolder).mediaProgressRemainder.setLayoutParams((ViewGroup$LayoutParams)new LinearLayout$LayoutParams(0, -1, (float)backgroundResource));
                }
                final TextView seriesTitle = ((NormalViewHolder)viewHolder).seriesTitle;
                if (text == null) {
                    text = "";
                }
                seriesTitle.setText((CharSequence)text);
                final TextView mediaTitle = ((NormalViewHolder)viewHolder).mediaTitle;
                String text2;
                if (name != null) {
                    text2 = name;
                }
                else {
                    text2 = "";
                }
                mediaTitle.setText((CharSequence)text2);
                if (fromNullable.isPresent() && WatchStatus.getVideoType(fromNullable.get()) == 2) {
                    ((NormalViewHolder)viewHolder).premiumIcon.setVisibility(0);
                }
                else {
                    ((NormalViewHolder)viewHolder).premiumIcon.setVisibility(8);
                }
                ((NormalViewHolder)viewHolder).menu.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        final PopupMenu popupMenu = new PopupMenu((Context)UpdatedEpisodesAdapter.this.mActivity, view);
                        popupMenu.getMenuInflater().inflate(2131689473, popupMenu.getMenu());
                        popupMenu.getMenu().getItem(0).setTitle((CharSequence)LocalizedStrings.PLAY.get());
                        popupMenu.getMenu().getItem(1).setTitle((CharSequence)LocalizedStrings.SHARE.get());
                        popupMenu.getMenu().getItem(2).setTitle((CharSequence)LocalizedStrings.INFORMATION.get());
                        popupMenu.getMenu().getItem(3).setTitle((CharSequence)LocalizedStrings.VIDEOS.get());
                        popupMenu.getMenu().getItem(4).setTitle((CharSequence)LocalizedStrings.REMOVE_FROM_QUEUE.get());
                        popupMenu.getMenu().getItem(4).setVisible(false);
                        popupMenu.setOnMenuItemClickListener((PopupMenu$OnMenuItemClickListener)new PopupMenu$OnMenuItemClickListener() {
                            public boolean onMenuItemClick(final MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case 2131624350: {
                                        if (!CrunchyrollApplication.getApp((Context)UpdatedEpisodesAdapter.this.mActivity).isPrepareToWatchLoading()) {
                                            final PrepareToWatch prepareToWatch = CrunchyrollApplication.getApp((Context)UpdatedEpisodesAdapter.this.mActivity).prepareToWatch(UpdatedEpisodesAdapter.this.mActivity, media, false, 0);
                                            prepareToWatch.prepare(new BaseListener<Void>() {
                                                @Override
                                                public void onException(final Exception ex) {
                                                }
                                                
                                                @Override
                                                public void onFinally() {
                                                    Util.hideProgressBar(UpdatedEpisodesAdapter.this.mActivity);
                                                }
                                                
                                                @Override
                                                public void onPreExecute() {
                                                    Util.showProgressBar(UpdatedEpisodesAdapter.this.mActivity, UpdatedEpisodesAdapter.this.mActivity.getResources().getColor(2131558518));
                                                }
                                                
                                                @Override
                                                public void onSuccess(final Void void1) {
                                                    prepareToWatch.go(PrepareToWatch.Event.NONE);
                                                }
                                            });
                                            break;
                                        }
                                        break;
                                    }
                                    case 2131624351: {
                                        ActionsUtil.share((Context)UpdatedEpisodesAdapter.this.mActivity, series.getName(), null, series.getUrl());
                                        break;
                                    }
                                    case 2131624352: {
                                        EventBus.getDefault().post(new PopupNewFragmentEvent(MediaInfoFragment.newInstance(media.getMediaId())));
                                        PopupActivity.startMediaInfo(UpdatedEpisodesAdapter.this.mActivity, media.getMediaId());
                                        break;
                                    }
                                    case 2131624353: {
                                        SeriesDetailActivity.start((Context)UpdatedEpisodesAdapter.this.mActivity, series.getSeriesId(), 0, false);
                                        break;
                                    }
                                }
                                return true;
                            }
                        });
                        popupMenu.show();
                    }
                });
            }
        }
        else if (viewHolder instanceof HeaderViewHolder) {
            final UpdatedEpisodeEntry updatedEpisodeEntry = this.mUpdatedEpisodeEntries.get(backgroundResource);
            ((HeaderViewHolder)viewHolder).dayName.setText((CharSequence)updatedEpisodeEntry.getDayName());
            if (LocalizedStrings.JUST_ARRIVED.get().equalsIgnoreCase(updatedEpisodeEntry.getDayName())) {
                ((HeaderViewHolder)viewHolder).upperLine.setVisibility(4);
                ((HeaderViewHolder)viewHolder).twoStars.setVisibility(0);
                ((HeaderViewHolder)viewHolder).oneStar.setVisibility(0);
            }
            else {
                ((HeaderViewHolder)viewHolder).upperLine.setVisibility(0);
                ((HeaderViewHolder)viewHolder).twoStars.setVisibility(4);
                ((HeaderViewHolder)viewHolder).oneStar.setVisibility(4);
            }
            backgroundResource = updatedEpisodeEntry.getDay();
            final ImageView dayImg = ((HeaderViewHolder)viewHolder).dayImg;
            switch (backgroundResource) {
                default: {}
                case 1: {
                    dayImg.setImageResource(2130837738);
                }
                case 2: {
                    dayImg.setImageResource(2130837736);
                }
                case 3: {
                    dayImg.setImageResource(2130837740);
                }
                case 4: {
                    dayImg.setImageResource(2130837741);
                }
                case 5: {
                    dayImg.setImageResource(2130837739);
                }
                case 6: {
                    dayImg.setImageResource(2130837735);
                }
                case 7: {
                    dayImg.setImageResource(2130837737);
                }
            }
        }
        else if (viewHolder instanceof ListItemLoadingViewHolder && this.mLoadingState == LoadingState.LOADING_STATE_LOADING) {
            this.mListItemLoadingViewHolder.setShake(false);
            EventBus.getDefault().post(new LoadMore.UpdatedEpisodesEvent());
        }
    }
    
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        if (n == 0) {
            return new NormalViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2130903083, viewGroup, false));
        }
        if (n == 2) {
            return new HeaderViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2130903151, viewGroup, false));
        }
        if (n == 3) {
            return new CalenderLineViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2130903150, viewGroup, false));
        }
        if (this.mListItemLoadingViewHolder == null) {
            this.mListItemLoadingViewHolder = new ListItemLoadingViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2130903154, viewGroup, false));
            this.mListItemLoadingViewHolder.mRetry.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    UpdatedEpisodesAdapter.this.mListItemLoadingViewHolder.setShake(true);
                    EventBus.getDefault().post(new LoadMore.UpdatedEpisodesEvent());
                }
            });
        }
        return this.mListItemLoadingViewHolder;
    }
    
    @Override
    public void showLoadingError(final String s) {
        this.mLoadingState = LoadingState.LOADING_STATE_ERROR;
        if (this.mListItemLoadingViewHolder != null) {
            this.mListItemLoadingViewHolder.showError(s);
        }
    }
    
    @Override
    public void showLoadingProgress() {
        this.mLoadingState = LoadingState.LOADING_STATE_LOADING;
        if (this.mListItemLoadingViewHolder != null) {
            this.mListItemLoadingViewHolder.showProgress();
        }
    }
    
    public static class CalenderLineViewHolder extends ViewHolder
    {
        public CalenderLineViewHolder(final View view) {
            super(view);
        }
    }
    
    public static class HeaderViewHolder extends ViewHolder
    {
        public final ImageView dayImg;
        public final TextView dayName;
        public final View lowerLine;
        public final ImageView oneStar;
        public final ImageView twoStars;
        public final View upperLine;
        
        public HeaderViewHolder(final View view) {
            super(view);
            this.upperLine = view.findViewById(2131624288);
            this.lowerLine = view.findViewById(2131624289);
            this.dayImg = (ImageView)view.findViewById(2131624290);
            this.dayName = (TextView)view.findViewById(2131624291);
            this.twoStars = (ImageView)view.findViewById(2131624292);
            this.oneStar = (ImageView)view.findViewById(2131624293);
        }
    }
    
    public enum LoadingState
    {
        LOADING_STATE_ERROR, 
        LOADING_STATE_LOADING, 
        LOADING_STATE_NONE;
    }
    
    public class UpdatedEpisodeEntry
    {
        private int mDay;
        private String mDayName;
        private boolean mShowCalenderLine;
        private UpdatedEpisode mUpdatedEpisode;
        private int mViewType;
        
        public UpdatedEpisodeEntry() {
            this.mShowCalenderLine = false;
        }
        
        public int getDay() {
            return this.mDay;
        }
        
        public String getDayName() {
            return this.mDayName;
        }
        
        public int getType() {
            return this.mViewType;
        }
        
        public UpdatedEpisode getUpdatedEpisode() {
            return this.mUpdatedEpisode;
        }
        
        public boolean isShowCalenderLine() {
            return this.mShowCalenderLine;
        }
        
        public void setDay(final int mDay) {
            this.mDay = mDay;
        }
        
        public void setDayName(final String mDayName) {
            this.mDayName = mDayName;
        }
        
        public void setShowCalenderLine() {
            this.mShowCalenderLine = true;
        }
        
        public void setUpdatedEpisode(final UpdatedEpisode mUpdatedEpisode) {
            this.mUpdatedEpisode = mUpdatedEpisode;
        }
        
        public void setViewType(final int mViewType) {
            this.mViewType = mViewType;
        }
    }
}
