package com.afrofx.code.anjesgf;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afrofx.code.anjesgf.backup.Backup;
import com.afrofx.code.anjesgf.backup.GoogleDriveBackup;

import java.util.prefs.Preferences;

public class EcontaApplication extends Application {


    private static EcontaApplication sInstance;

    @Nullable
    private Preferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }



    @NonNull
    public Backup getBackup() {
        return new GoogleDriveBackup();
    }



    @NonNull
    public DatabaseHelper getDBHandler() {
        return new DatabaseHelper(getApplicationContext());
    }


    public static EcontaApplication getInstance() {
        if (sInstance == null) {
            sInstance = new EcontaApplication();
        }
        return sInstance;
    }
}
