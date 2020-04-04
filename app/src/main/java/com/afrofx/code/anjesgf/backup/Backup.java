package com.afrofx.code.anjesgf.backup;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;

public interface Backup {
    void init(@NonNull final Activity activity);

    void start();

    void stop();

    GoogleApiClient getClient();
}