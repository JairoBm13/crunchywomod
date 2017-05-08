// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

import java.util.Arrays;
import java.util.Comparator;
import java.io.FilenameFilter;
import java.io.File;

final class Utils
{
    public static void capFileCount(final File file, final FilenameFilter filenameFilter, final int n, final Comparator<File> comparator) {
        final File[] listFiles = file.listFiles(filenameFilter);
        if (listFiles != null && listFiles.length > n) {
            Arrays.sort(listFiles, comparator);
            int length = listFiles.length;
            for (int length2 = listFiles.length, i = 0; i < length2; ++i) {
                final File file2 = listFiles[i];
                if (length <= n) {
                    break;
                }
                file2.delete();
                --length;
            }
        }
    }
}
