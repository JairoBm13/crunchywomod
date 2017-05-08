// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class Settings
{
    public List<String> adBlocks;
    public ArrayList<String> category;
    public String contentDescription;
    public String contentID;
    public String contentTitle;
    public int maxAdTimeSeconds;
    public Map<String, String> misc;
    public String policyID;
    public PreferredOrientation preferredOrientation;
    public int userAge;
    public String userCountry;
    public Education userEducation;
    public Gender userGender;
    public IncomeRange userIncomeRange;
    public List<String> userInterests;
    public String userLanguage;
    public double userLatitude;
    public double userLongitude;
    public Race userRace;
    public String userZip;
    
    public Settings() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: invokespecial   java/lang/Object.<init>:()V
        //     4: aload_0        
        //     5: iconst_0       
        //     6: putfield        com/tremorvideo/sdk/android/videoad/Settings.userAge:I
        //     9: aload_0        
        //    10: getstatic       com/tremorvideo/sdk/android/videoad/Settings$Gender.Unknown:Lcom/tremorvideo/sdk/android/videoad/Settings$Gender;
        //    13: putfield        com/tremorvideo/sdk/android/videoad/Settings.userGender:Lcom/tremorvideo/sdk/android/videoad/Settings$Gender;
        //    16: aload_0        
        //    17: invokestatic    java/util/Locale.getDefault:()Ljava/util/Locale;
        //    20: invokevirtual   java/util/Locale.getISO3Language:()Ljava/lang/String;
        //    23: putfield        com/tremorvideo/sdk/android/videoad/Settings.userLanguage:Ljava/lang/String;
        //    26: aload_0        
        //    27: invokestatic    java/util/Locale.getDefault:()Ljava/util/Locale;
        //    30: invokevirtual   java/util/Locale.getISO3Country:()Ljava/lang/String;
        //    33: putfield        com/tremorvideo/sdk/android/videoad/Settings.userCountry:Ljava/lang/String;
        //    36: aload_0        
        //    37: ldc             ""
        //    39: putfield        com/tremorvideo/sdk/android/videoad/Settings.userZip:Ljava/lang/String;
        //    42: aload_0        
        //    43: dconst_0       
        //    44: putfield        com/tremorvideo/sdk/android/videoad/Settings.userLongitude:D
        //    47: aload_0        
        //    48: dconst_0       
        //    49: putfield        com/tremorvideo/sdk/android/videoad/Settings.userLatitude:D
        //    52: aload_0        
        //    53: getstatic       com/tremorvideo/sdk/android/videoad/Settings$IncomeRange.Unknown:Lcom/tremorvideo/sdk/android/videoad/Settings$IncomeRange;
        //    56: putfield        com/tremorvideo/sdk/android/videoad/Settings.userIncomeRange:Lcom/tremorvideo/sdk/android/videoad/Settings$IncomeRange;
        //    59: aload_0        
        //    60: getstatic       com/tremorvideo/sdk/android/videoad/Settings$Education.Unknown:Lcom/tremorvideo/sdk/android/videoad/Settings$Education;
        //    63: putfield        com/tremorvideo/sdk/android/videoad/Settings.userEducation:Lcom/tremorvideo/sdk/android/videoad/Settings$Education;
        //    66: aload_0        
        //    67: getstatic       com/tremorvideo/sdk/android/videoad/Settings$Race.Unknown:Lcom/tremorvideo/sdk/android/videoad/Settings$Race;
        //    70: putfield        com/tremorvideo/sdk/android/videoad/Settings.userRace:Lcom/tremorvideo/sdk/android/videoad/Settings$Race;
        //    73: aload_0        
        //    74: new             Ljava/util/ArrayList;
        //    77: dup            
        //    78: invokespecial   java/util/ArrayList.<init>:()V
        //    81: putfield        com/tremorvideo/sdk/android/videoad/Settings.userInterests:Ljava/util/List;
        //    84: aload_0        
        //    85: new             Ljava/util/HashMap;
        //    88: dup            
        //    89: invokespecial   java/util/HashMap.<init>:()V
        //    92: putfield        com/tremorvideo/sdk/android/videoad/Settings.misc:Ljava/util/Map;
        //    95: aload_0        
        //    96: getstatic       com/tremorvideo/sdk/android/videoad/Settings$PreferredOrientation.Any:Lcom/tremorvideo/sdk/android/videoad/Settings$PreferredOrientation;
        //    99: putfield        com/tremorvideo/sdk/android/videoad/Settings.preferredOrientation:Lcom/tremorvideo/sdk/android/videoad/Settings$PreferredOrientation;
        //   102: aload_0        
        //   103: ldc             ""
        //   105: putfield        com/tremorvideo/sdk/android/videoad/Settings.policyID:Ljava/lang/String;
        //   108: aload_0        
        //   109: new             Ljava/util/ArrayList;
        //   112: dup            
        //   113: invokespecial   java/util/ArrayList.<init>:()V
        //   116: putfield        com/tremorvideo/sdk/android/videoad/Settings.category:Ljava/util/ArrayList;
        //   119: aload_0        
        //   120: iconst_0       
        //   121: putfield        com/tremorvideo/sdk/android/videoad/Settings.maxAdTimeSeconds:I
        //   124: aload_0        
        //   125: ldc             ""
        //   127: putfield        com/tremorvideo/sdk/android/videoad/Settings.contentID:Ljava/lang/String;
        //   130: aload_0        
        //   131: ldc             ""
        //   133: putfield        com/tremorvideo/sdk/android/videoad/Settings.contentTitle:Ljava/lang/String;
        //   136: aload_0        
        //   137: ldc             ""
        //   139: putfield        com/tremorvideo/sdk/android/videoad/Settings.contentDescription:Ljava/lang/String;
        //   142: aload_0        
        //   143: new             Ljava/util/ArrayList;
        //   146: dup            
        //   147: invokespecial   java/util/ArrayList.<init>:()V
        //   150: putfield        com/tremorvideo/sdk/android/videoad/Settings.adBlocks:Ljava/util/List;
        //   153: return         
        //   154: astore_1       
        //   155: ldc             "Error getISO3Language"
        //   157: invokestatic    com/tremorvideo/sdk/android/videoad/ac.e:(Ljava/lang/String;)V
        //   160: aload_0        
        //   161: ldc             ""
        //   163: putfield        com/tremorvideo/sdk/android/videoad/Settings.userLanguage:Ljava/lang/String;
        //   166: goto            26
        //   169: astore_1       
        //   170: ldc             "Error  getISO3Country"
        //   172: invokestatic    com/tremorvideo/sdk/android/videoad/ac.e:(Ljava/lang/String;)V
        //   175: aload_0        
        //   176: ldc             ""
        //   178: putfield        com/tremorvideo/sdk/android/videoad/Settings.userCountry:Ljava/lang/String;
        //   181: goto            36
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  16     26     154    169    Ljava/lang/Exception;
        //  26     36     169    184    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0026:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:692)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:529)
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
    
    public Settings(final Settings p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: invokespecial   java/lang/Object.<init>:()V
        //     4: aload_0        
        //     5: aload_1        
        //     6: getfield        com/tremorvideo/sdk/android/videoad/Settings.userAge:I
        //     9: putfield        com/tremorvideo/sdk/android/videoad/Settings.userAge:I
        //    12: aload_0        
        //    13: aload_1        
        //    14: getfield        com/tremorvideo/sdk/android/videoad/Settings.userGender:Lcom/tremorvideo/sdk/android/videoad/Settings$Gender;
        //    17: putfield        com/tremorvideo/sdk/android/videoad/Settings.userGender:Lcom/tremorvideo/sdk/android/videoad/Settings$Gender;
        //    20: aload_0        
        //    21: aload_1        
        //    22: getfield        com/tremorvideo/sdk/android/videoad/Settings.userLanguage:Ljava/lang/String;
        //    25: putfield        com/tremorvideo/sdk/android/videoad/Settings.userLanguage:Ljava/lang/String;
        //    28: aload_0        
        //    29: aload_1        
        //    30: getfield        com/tremorvideo/sdk/android/videoad/Settings.userCountry:Ljava/lang/String;
        //    33: putfield        com/tremorvideo/sdk/android/videoad/Settings.userCountry:Ljava/lang/String;
        //    36: aload_0        
        //    37: aload_1        
        //    38: getfield        com/tremorvideo/sdk/android/videoad/Settings.userZip:Ljava/lang/String;
        //    41: putfield        com/tremorvideo/sdk/android/videoad/Settings.userZip:Ljava/lang/String;
        //    44: aload_0        
        //    45: aload_1        
        //    46: getfield        com/tremorvideo/sdk/android/videoad/Settings.userLongitude:D
        //    49: putfield        com/tremorvideo/sdk/android/videoad/Settings.userLongitude:D
        //    52: aload_0        
        //    53: aload_1        
        //    54: getfield        com/tremorvideo/sdk/android/videoad/Settings.userLatitude:D
        //    57: putfield        com/tremorvideo/sdk/android/videoad/Settings.userLatitude:D
        //    60: aload_0        
        //    61: aload_1        
        //    62: getfield        com/tremorvideo/sdk/android/videoad/Settings.userIncomeRange:Lcom/tremorvideo/sdk/android/videoad/Settings$IncomeRange;
        //    65: putfield        com/tremorvideo/sdk/android/videoad/Settings.userIncomeRange:Lcom/tremorvideo/sdk/android/videoad/Settings$IncomeRange;
        //    68: aload_0        
        //    69: aload_1        
        //    70: getfield        com/tremorvideo/sdk/android/videoad/Settings.userEducation:Lcom/tremorvideo/sdk/android/videoad/Settings$Education;
        //    73: putfield        com/tremorvideo/sdk/android/videoad/Settings.userEducation:Lcom/tremorvideo/sdk/android/videoad/Settings$Education;
        //    76: aload_0        
        //    77: aload_1        
        //    78: getfield        com/tremorvideo/sdk/android/videoad/Settings.userRace:Lcom/tremorvideo/sdk/android/videoad/Settings$Race;
        //    81: putfield        com/tremorvideo/sdk/android/videoad/Settings.userRace:Lcom/tremorvideo/sdk/android/videoad/Settings$Race;
        //    84: aload_0        
        //    85: new             Ljava/util/ArrayList;
        //    88: dup            
        //    89: aload_1        
        //    90: getfield        com/tremorvideo/sdk/android/videoad/Settings.userInterests:Ljava/util/List;
        //    93: invokespecial   java/util/ArrayList.<init>:(Ljava/util/Collection;)V
        //    96: putfield        com/tremorvideo/sdk/android/videoad/Settings.userInterests:Ljava/util/List;
        //    99: aload_0        
        //   100: new             Ljava/util/HashMap;
        //   103: dup            
        //   104: aload_1        
        //   105: getfield        com/tremorvideo/sdk/android/videoad/Settings.misc:Ljava/util/Map;
        //   108: invokespecial   java/util/HashMap.<init>:(Ljava/util/Map;)V
        //   111: putfield        com/tremorvideo/sdk/android/videoad/Settings.misc:Ljava/util/Map;
        //   114: new             Ljava/util/Locale;
        //   117: dup            
        //   118: aload_1        
        //   119: getfield        com/tremorvideo/sdk/android/videoad/Settings.userLanguage:Ljava/lang/String;
        //   122: aload_1        
        //   123: getfield        com/tremorvideo/sdk/android/videoad/Settings.userCountry:Ljava/lang/String;
        //   126: invokespecial   java/util/Locale.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //   129: astore_2       
        //   130: aload_0        
        //   131: aload_2        
        //   132: invokevirtual   java/util/Locale.getISO3Language:()Ljava/lang/String;
        //   135: putfield        com/tremorvideo/sdk/android/videoad/Settings.userLanguage:Ljava/lang/String;
        //   138: aload_0        
        //   139: aload_2        
        //   140: invokevirtual   java/util/Locale.getISO3Country:()Ljava/lang/String;
        //   143: putfield        com/tremorvideo/sdk/android/videoad/Settings.userCountry:Ljava/lang/String;
        //   146: aload_0        
        //   147: aload_1        
        //   148: getfield        com/tremorvideo/sdk/android/videoad/Settings.policyID:Ljava/lang/String;
        //   151: putfield        com/tremorvideo/sdk/android/videoad/Settings.policyID:Ljava/lang/String;
        //   154: aload_0        
        //   155: new             Ljava/util/ArrayList;
        //   158: dup            
        //   159: aload_1        
        //   160: getfield        com/tremorvideo/sdk/android/videoad/Settings.category:Ljava/util/ArrayList;
        //   163: invokespecial   java/util/ArrayList.<init>:(Ljava/util/Collection;)V
        //   166: putfield        com/tremorvideo/sdk/android/videoad/Settings.category:Ljava/util/ArrayList;
        //   169: aload_0        
        //   170: aload_1        
        //   171: getfield        com/tremorvideo/sdk/android/videoad/Settings.preferredOrientation:Lcom/tremorvideo/sdk/android/videoad/Settings$PreferredOrientation;
        //   174: putfield        com/tremorvideo/sdk/android/videoad/Settings.preferredOrientation:Lcom/tremorvideo/sdk/android/videoad/Settings$PreferredOrientation;
        //   177: aload_0        
        //   178: aload_1        
        //   179: getfield        com/tremorvideo/sdk/android/videoad/Settings.maxAdTimeSeconds:I
        //   182: putfield        com/tremorvideo/sdk/android/videoad/Settings.maxAdTimeSeconds:I
        //   185: aload_0        
        //   186: aload_1        
        //   187: getfield        com/tremorvideo/sdk/android/videoad/Settings.contentID:Ljava/lang/String;
        //   190: putfield        com/tremorvideo/sdk/android/videoad/Settings.contentID:Ljava/lang/String;
        //   193: aload_0        
        //   194: aload_1        
        //   195: getfield        com/tremorvideo/sdk/android/videoad/Settings.contentTitle:Ljava/lang/String;
        //   198: putfield        com/tremorvideo/sdk/android/videoad/Settings.contentTitle:Ljava/lang/String;
        //   201: aload_0        
        //   202: aload_1        
        //   203: getfield        com/tremorvideo/sdk/android/videoad/Settings.contentDescription:Ljava/lang/String;
        //   206: putfield        com/tremorvideo/sdk/android/videoad/Settings.contentDescription:Ljava/lang/String;
        //   209: aload_0        
        //   210: new             Ljava/util/ArrayList;
        //   213: dup            
        //   214: aload_1        
        //   215: getfield        com/tremorvideo/sdk/android/videoad/Settings.adBlocks:Ljava/util/List;
        //   218: invokespecial   java/util/ArrayList.<init>:(Ljava/util/Collection;)V
        //   221: putfield        com/tremorvideo/sdk/android/videoad/Settings.adBlocks:Ljava/util/List;
        //   224: return         
        //   225: astore_3       
        //   226: ldc             "Error getISO3Language"
        //   228: invokestatic    com/tremorvideo/sdk/android/videoad/ac.e:(Ljava/lang/String;)V
        //   231: aload_0        
        //   232: aload_1        
        //   233: getfield        com/tremorvideo/sdk/android/videoad/Settings.userLanguage:Ljava/lang/String;
        //   236: putfield        com/tremorvideo/sdk/android/videoad/Settings.userLanguage:Ljava/lang/String;
        //   239: goto            138
        //   242: astore_2       
        //   243: ldc             "Error getISO3Country"
        //   245: invokestatic    com/tremorvideo/sdk/android/videoad/ac.e:(Ljava/lang/String;)V
        //   248: aload_0        
        //   249: aload_1        
        //   250: getfield        com/tremorvideo/sdk/android/videoad/Settings.userCountry:Ljava/lang/String;
        //   253: putfield        com/tremorvideo/sdk/android/videoad/Settings.userCountry:Ljava/lang/String;
        //   256: goto            146
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  130    138    225    242    Ljava/lang/Exception;
        //  138    146    242    259    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0138:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:692)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:529)
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
    
    public enum Education
    {
        CollegeBachelor("college bachelor"), 
        CollegeMasters("college masters"), 
        CollegePhd("college phd"), 
        CollegeProfessional("college professional"), 
        Highschool("highschool"), 
        LessThanHighschool("less than highschool"), 
        SomeCollege("some college"), 
        SomeHighschool("some highschool"), 
        Unknown("unknown");
        
        private String a;
        
        private Education(final String a) {
            this.a = a;
        }
        
        @Override
        public String toString() {
            return this.a;
        }
    }
    
    public enum Gender
    {
        Felmale("f"), 
        Male("m"), 
        Unknown("unknown");
        
        private String a;
        
        private Gender(final String a) {
            this.a = a;
        }
        
        @Override
        public String toString() {
            return this.a;
        }
    }
    
    public enum IncomeRange
    {
        Above250K("250+"), 
        Between100KAnd150K("100-150"), 
        Between150KAnd200K("150-200"), 
        Between200KAnd250K("200-250"), 
        Between25KAnd50K("25-50"), 
        Between50KAnd75K("50-75"), 
        Between75KAnd100K("75-100"), 
        LessThan25K("0-25"), 
        Unknown("unknown");
        
        private String a;
        
        private IncomeRange(final String a) {
            this.a = a;
        }
        
        @Override
        public String toString() {
            return this.a;
        }
    }
    
    public enum PreferredOrientation
    {
        Any, 
        Landscape, 
        Portraite;
    }
    
    public enum Race
    {
        AlaskaNative("alaska native"), 
        AmericanIndian("american indian"), 
        Asian("asian"), 
        Black("black"), 
        Hispanic("hispanic"), 
        NativeHawaiian("native hawaiian"), 
        Other("other"), 
        PacificIslander("pacific islander"), 
        Unknown("unknown"), 
        White("white");
        
        private String a;
        
        private Race(final String a) {
            this.a = a;
        }
        
        @Override
        public String toString() {
            return this.a;
        }
    }
}
