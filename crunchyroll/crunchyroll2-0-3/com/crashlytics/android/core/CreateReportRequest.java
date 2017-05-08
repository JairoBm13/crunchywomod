// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

class CreateReportRequest
{
    public final String apiKey;
    public final Report report;
    
    public CreateReportRequest(final String apiKey, final Report report) {
        this.apiKey = apiKey;
        this.report = report;
    }
}
