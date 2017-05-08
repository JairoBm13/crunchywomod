// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.content;

import java.util.Set;
import android.net.Uri;
import android.util.Log;
import android.content.Intent;
import android.os.Message;
import android.os.Looper;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.os.Handler;
import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;

public class LocalBroadcastManager
{
    private static final boolean DEBUG = false;
    static final int MSG_EXEC_PENDING_BROADCASTS = 1;
    private static final String TAG = "LocalBroadcastManager";
    private static LocalBroadcastManager mInstance;
    private static final Object mLock;
    private final HashMap<String, ArrayList<ReceiverRecord>> mActions;
    private final Context mAppContext;
    private final Handler mHandler;
    private final ArrayList<BroadcastRecord> mPendingBroadcasts;
    private final HashMap<BroadcastReceiver, ArrayList<IntentFilter>> mReceivers;
    
    static {
        mLock = new Object();
    }
    
    private LocalBroadcastManager(final Context mAppContext) {
        this.mReceivers = new HashMap<BroadcastReceiver, ArrayList<IntentFilter>>();
        this.mActions = new HashMap<String, ArrayList<ReceiverRecord>>();
        this.mPendingBroadcasts = new ArrayList<BroadcastRecord>();
        this.mAppContext = mAppContext;
        this.mHandler = new Handler(mAppContext.getMainLooper()) {
            public void handleMessage(final Message message) {
                switch (message.what) {
                    default: {
                        super.handleMessage(message);
                    }
                    case 1: {
                        LocalBroadcastManager.this.executePendingBroadcasts();
                    }
                }
            }
        };
    }
    
    private void executePendingBroadcasts() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: getfield        android/support/v4/content/LocalBroadcastManager.mReceivers:Ljava/util/HashMap;
        //     4: astore_3       
        //     5: aload_3        
        //     6: monitorenter   
        //     7: aload_0        
        //     8: getfield        android/support/v4/content/LocalBroadcastManager.mPendingBroadcasts:Ljava/util/ArrayList;
        //    11: invokevirtual   java/util/ArrayList.size:()I
        //    14: istore_1       
        //    15: iload_1        
        //    16: ifgt            22
        //    19: aload_3        
        //    20: monitorexit    
        //    21: return         
        //    22: iload_1        
        //    23: anewarray       Landroid/support/v4/content/LocalBroadcastManager$BroadcastRecord;
        //    26: astore          4
        //    28: aload_0        
        //    29: getfield        android/support/v4/content/LocalBroadcastManager.mPendingBroadcasts:Ljava/util/ArrayList;
        //    32: aload           4
        //    34: invokevirtual   java/util/ArrayList.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //    37: pop            
        //    38: aload_0        
        //    39: getfield        android/support/v4/content/LocalBroadcastManager.mPendingBroadcasts:Ljava/util/ArrayList;
        //    42: invokevirtual   java/util/ArrayList.clear:()V
        //    45: aload_3        
        //    46: monitorexit    
        //    47: iconst_0       
        //    48: istore_1       
        //    49: iload_1        
        //    50: aload           4
        //    52: arraylength    
        //    53: if_icmpge       0
        //    56: aload           4
        //    58: iload_1        
        //    59: aaload         
        //    60: astore_3       
        //    61: iconst_0       
        //    62: istore_2       
        //    63: iload_2        
        //    64: aload_3        
        //    65: getfield        android/support/v4/content/LocalBroadcastManager$BroadcastRecord.receivers:Ljava/util/ArrayList;
        //    68: invokevirtual   java/util/ArrayList.size:()I
        //    71: if_icmpge       113
        //    74: aload_3        
        //    75: getfield        android/support/v4/content/LocalBroadcastManager$BroadcastRecord.receivers:Ljava/util/ArrayList;
        //    78: iload_2        
        //    79: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //    82: checkcast       Landroid/support/v4/content/LocalBroadcastManager$ReceiverRecord;
        //    85: getfield        android/support/v4/content/LocalBroadcastManager$ReceiverRecord.receiver:Landroid/content/BroadcastReceiver;
        //    88: aload_0        
        //    89: getfield        android/support/v4/content/LocalBroadcastManager.mAppContext:Landroid/content/Context;
        //    92: aload_3        
        //    93: getfield        android/support/v4/content/LocalBroadcastManager$BroadcastRecord.intent:Landroid/content/Intent;
        //    96: invokevirtual   android/content/BroadcastReceiver.onReceive:(Landroid/content/Context;Landroid/content/Intent;)V
        //    99: iload_2        
        //   100: iconst_1       
        //   101: iadd           
        //   102: istore_2       
        //   103: goto            63
        //   106: astore          4
        //   108: aload_3        
        //   109: monitorexit    
        //   110: aload           4
        //   112: athrow         
        //   113: iload_1        
        //   114: iconst_1       
        //   115: iadd           
        //   116: istore_1       
        //   117: goto            49
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  7      15     106    113    Any
        //  19     21     106    113    Any
        //  22     47     106    113    Any
        //  108    110    106    113    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
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
    
    public static LocalBroadcastManager getInstance(final Context context) {
        synchronized (LocalBroadcastManager.mLock) {
            if (LocalBroadcastManager.mInstance == null) {
                LocalBroadcastManager.mInstance = new LocalBroadcastManager(context.getApplicationContext());
            }
            return LocalBroadcastManager.mInstance;
        }
    }
    
    public void registerReceiver(final BroadcastReceiver broadcastReceiver, final IntentFilter intentFilter) {
        synchronized (this.mReceivers) {
            final ReceiverRecord receiverRecord = new ReceiverRecord(intentFilter, broadcastReceiver);
            ArrayList<IntentFilter> list;
            if ((list = this.mReceivers.get(broadcastReceiver)) == null) {
                list = new ArrayList<IntentFilter>(1);
                this.mReceivers.put(broadcastReceiver, list);
            }
            list.add(intentFilter);
            for (int i = 0; i < intentFilter.countActions(); ++i) {
                final String action = intentFilter.getAction(i);
                ArrayList<ReceiverRecord> list2;
                if ((list2 = this.mActions.get(action)) == null) {
                    list2 = new ArrayList<ReceiverRecord>(1);
                    this.mActions.put(action, list2);
                }
                list2.add(receiverRecord);
            }
        }
    }
    
    public boolean sendBroadcast(final Intent intent) {
        // monitorexit(hashMap)
        while (true) {
            while (true) {
                while (true) {
                    String action;
                    String resolveTypeIfNeeded;
                    Uri data;
                    String scheme;
                    Set categories;
                    int n;
                    ArrayList<ReceiverRecord> list;
                    ArrayList<ReceiverRecord> list3 = null;
                    ArrayList<ReceiverRecord> list2 = null;
                    int n2 = 0;
                    ReceiverRecord receiverRecord;
                    int match;
                    String s;
                    Block_13_Outer:Label_0161_Outer:
                    while (true) {
                    Label_0305_Outer:
                        while (true) {
                            Label_0510: {
                                Label_0502: {
                                    synchronized (this.mReceivers) {
                                        action = intent.getAction();
                                        resolveTypeIfNeeded = intent.resolveTypeIfNeeded(this.mAppContext.getContentResolver());
                                        data = intent.getData();
                                        scheme = intent.getScheme();
                                        categories = intent.getCategories();
                                        if ((intent.getFlags() & 0x8) == 0x0) {
                                            break Label_0305_Outer;
                                        }
                                        n = 1;
                                        if (n != 0) {
                                            Log.v("LocalBroadcastManager", "Resolving type " + resolveTypeIfNeeded + " scheme " + scheme + " of intent " + intent);
                                        }
                                        list = this.mActions.get(intent.getAction());
                                        if (list == null) {
                                            break;
                                        }
                                        if (n != 0) {
                                            Log.v("LocalBroadcastManager", "Action list: " + list);
                                        }
                                        break Label_0502;
                                        // iftrue(Label_0305:, n == 0)
                                        // iftrue(Label_0510:, n == 0)
                                        // iftrue(Label_0554:, n2 >= list.size())
                                        // iftrue(Label_0346:, match < 0)
                                        // iftrue(Label_0245:, !receiverRecord.broadcasting)
                                        // iftrue(Label_0214:, n == 0)
                                        // iftrue(Label_0323:, list2 = list3 != null)
                                        while (true) {
                                        Label_0214_Outer:
                                            while (true) {
                                                while (true) {
                                                    Block_12: {
                                                        Label_0323: {
                                                        Block_11:
                                                            while (true) {
                                                                while (true) {
                                                                    break Label_0214_Outer;
                                                                    list2 = list3;
                                                                    Block_14: {
                                                                        break Block_14;
                                                                        break Block_11;
                                                                        list2 = new ArrayList<ReceiverRecord>();
                                                                        break Label_0323;
                                                                    }
                                                                    Log.v("LocalBroadcastManager", "  Filter's target already added");
                                                                    list2 = list3;
                                                                    break Label_0510;
                                                                    Label_0245: {
                                                                        match = receiverRecord.filter.match(action, resolveTypeIfNeeded, scheme, data, categories, "LocalBroadcastManager");
                                                                    }
                                                                    continue Block_13_Outer;
                                                                }
                                                                continue Label_0161_Outer;
                                                            }
                                                            receiverRecord = list.get(n2);
                                                            break Block_12;
                                                        }
                                                        list2.add(receiverRecord);
                                                        receiverRecord.broadcasting = true;
                                                        break Label_0510;
                                                    }
                                                    Log.v("LocalBroadcastManager", "Matching against filter " + receiverRecord.filter);
                                                    continue Label_0305_Outer;
                                                }
                                                continue Label_0214_Outer;
                                            }
                                            Log.v("LocalBroadcastManager", "  Filter matched!  match=0x" + Integer.toHexString(match));
                                            continue;
                                        }
                                    }
                                    Label_0346: {
                                        list2 = list3;
                                    }
                                    if (n != 0) {
                                        switch (match) {
                                            default: {
                                                s = "unknown reason";
                                                break;
                                            }
                                            case -3: {
                                                s = "action";
                                                break;
                                            }
                                            case -4: {
                                                s = "category";
                                                break;
                                            }
                                            case -2: {
                                                s = "data";
                                                break;
                                            }
                                            case -1: {
                                                s = "type";
                                                break;
                                            }
                                        }
                                        Log.v("LocalBroadcastManager", "  Filter did not match: " + s);
                                        list2 = list3;
                                    }
                                    break Label_0510;
                                }
                                list3 = null;
                                n2 = 0;
                                continue;
                            }
                            ++n2;
                            list3 = list2;
                            continue;
                        }
                        n = 0;
                        continue;
                    }
                    int i = 0;
                    while (i < list3.size()) {
                        list3.get(i).broadcasting = false;
                        ++i;
                    }
                    this.mPendingBroadcasts.add(new BroadcastRecord(intent, list3));
                    // iftrue(Label_0492:, this.mHandler.hasMessages(1))
                    this.mHandler.sendEmptyMessage(1);
                    Label_0492: {
                        return true;
                    }
                }
                // monitorexit(hashMap)
                return false;
                Label_0554: {
                    final ArrayList<ReceiverRecord> list3;
                    if (list3 != null) {
                        final int i = 0;
                        continue;
                    }
                }
                break;
            }
            continue;
        }
    }
    
    public void sendBroadcastSync(final Intent intent) {
        if (this.sendBroadcast(intent)) {
            this.executePendingBroadcasts();
        }
    }
    
    public void unregisterReceiver(final BroadcastReceiver broadcastReceiver) {
    Label_0031_Outer:
        while (true) {
        Label_0031:
            while (true) {
                int n4 = 0;
            Block_7_Outer:
                while (true) {
                    int n = 0;
                    Label_0181: {
                        while (true) {
                            int n2 = 0;
                            Label_0173: {
                                Label_0168: {
                                    synchronized (this.mReceivers) {
                                        final ArrayList<IntentFilter> list = this.mReceivers.remove(broadcastReceiver);
                                        if (list == null) {
                                            return;
                                        }
                                        break Label_0168;
                                        // iftrue(Label_0181:, list2.size() > 0)
                                        // iftrue(Label_0190:, n >= intentFilter.countActions())
                                        Block_5: {
                                            Block_9: {
                                                break Block_9;
                                                break Block_5;
                                            }
                                            final String action;
                                            this.mActions.remove(action);
                                            break Label_0181;
                                        }
                                        IntentFilter intentFilter = null;
                                        final String action = intentFilter.getAction(n);
                                        final ArrayList<ReceiverRecord> list2 = this.mActions.get(action);
                                        // iftrue(Label_0181:, list2 == null)
                                        // iftrue(Label_0173:, (ReceiverRecord)list2.get(n3).receiver != broadcastReceiver2)
                                        // iftrue(Label_0158:, n4 >= list.size())
                                        // iftrue(Label_0137:, n3 >= list2.size())
                                        int n3 = 0;
                                    Block_8:
                                        while (true) {
                                            Block_6: {
                                                break Block_6;
                                                n2 = n3;
                                                break Block_8;
                                                while (true) {
                                                    intentFilter = list.get(n4);
                                                    n = 0;
                                                    continue Block_7_Outer;
                                                    continue Label_0031_Outer;
                                                }
                                                Label_0158: {
                                                    return;
                                                }
                                            }
                                            n3 = 0;
                                            continue Label_0031_Outer;
                                        }
                                        list2.remove(n3);
                                        n2 = n3 - 1;
                                        break Label_0173;
                                    }
                                }
                                n4 = 0;
                                continue Label_0031;
                            }
                            int n3 = n2 + 1;
                            continue;
                        }
                    }
                    ++n;
                    continue Label_0031_Outer;
                }
                Label_0190: {
                    ++n4;
                }
                continue Label_0031;
            }
        }
    }
    
    private static class BroadcastRecord
    {
        final Intent intent;
        final ArrayList<ReceiverRecord> receivers;
        
        BroadcastRecord(final Intent intent, final ArrayList<ReceiverRecord> receivers) {
            this.intent = intent;
            this.receivers = receivers;
        }
    }
    
    private static class ReceiverRecord
    {
        boolean broadcasting;
        final IntentFilter filter;
        final BroadcastReceiver receiver;
        
        ReceiverRecord(final IntentFilter filter, final BroadcastReceiver receiver) {
            this.filter = filter;
            this.receiver = receiver;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(128);
            sb.append("Receiver{");
            sb.append(this.receiver);
            sb.append(" filter=");
            sb.append(this.filter);
            sb.append("}");
            return sb.toString();
        }
    }
}
