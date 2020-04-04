package com.afrofx.code.anjesgf.Activities.drawer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.afrofx.code.anjesgf.Activities.root.LoginActivity;
import com.afrofx.code.anjesgf.Activities.root.MainScreenActivity;
import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.ThemeSettings.ThemeColor;
import com.afrofx.code.anjesgf.backup.RemoteBackup;
import com.afrofx.code.anjesgf.sessionController;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityOptions;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BackupActivity extends AppCompatActivity {


    private sessionController se;

    private static final String TAG = "Google Drive Activity";

    public static final int REQUEST_CODE_SIGN_IN = 0;
    public static final int REQUEST_CODE_OPENING = 1;
    public static final int REQUEST_CODE_CREATION = 2;
    public static final int REQUEST_CODE_PERMISSIONS = 2;

    private boolean isBackup = true;

    private RemoteBackup remoteBackup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            new ThemeColor(this);
        }

        setContentView(R.layout.activity_backup);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBackup);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        se = new sessionController(this);

        if (!se.loggedIn()) {
            logout();
        }


        remoteBackup = new RemoteBackup(this);

        final DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.closeDB();

        Button but_restore = (Button)findViewById(R.id.activity_backup_drive_button_restore);
        Button but_backup = (Button)findViewById(R.id.activity_backup_drive_button_backup);

        but_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBackup = true;
                remoteBackup.connectToDrive(isBackup);
            }
        });

        but_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBackup = true;
                remoteBackup.connectToDrive(isBackup);
            }
        });


        but_restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBackup = false;
                remoteBackup.connectToDrive(isBackup);
            }
        });



    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case REQUEST_CODE_SIGN_IN:
                Log.i(TAG, "Sign in request code");
                // Called after user is signed in.
                if (resultCode == RESULT_OK) {
                    remoteBackup.connectToDrive(isBackup);
                }
                break;

            case REQUEST_CODE_CREATION:
                // Called after a file is saved to Drive.
                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "Backup successfully saved.");
                    janelaSucesso(null, "Dados Guardados");
                }
                break;

            case REQUEST_CODE_OPENING:
                if (resultCode == RESULT_OK) {
                    DriveId driveId = data.getParcelableExtra(
                            OpenFileActivityOptions.EXTRA_RESPONSE_DRIVE_ID);
                    remoteBackup.mOpenItemTaskSource.setResult(driveId);

                    new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Dados Recuperados")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    startActivity(new Intent(BackupActivity.this, LoginActivity.class));
                                    sDialog.setCancelable(false);
                                }
                            }).show();
                } else {
                    remoteBackup.mOpenItemTaskSource.setException(new RuntimeException("Unable to open file"));
                }
        }
    }

    public void logout(){
        se.setLoggedIn(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(BackupActivity.this, MainScreenActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void janelaErro(String titlo, String msg) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(titlo)
                .setContentText(msg)
                .show();
    }

    public void janelaSucesso(String titlo, String msg) {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(titlo)
                .setContentText(msg)
                .show();
    }

}
