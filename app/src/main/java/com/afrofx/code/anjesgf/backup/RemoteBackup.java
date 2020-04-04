

package com.afrofx.code.anjesgf.backup;

import android.content.Intent;
import android.content.IntentSender;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.afrofx.code.anjesgf.Activities.drawer.BackupActivity;
import com.afrofx.code.anjesgf.Activities.root.LoginActivity;
import com.afrofx.code.anjesgf.sessionController;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.drive.CreateFileActivityOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityOptions;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.drive.widget.DataBufferAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.afrofx.code.anjesgf.Activities.drawer.BackupActivity.REQUEST_CODE_CREATION;
import static com.afrofx.code.anjesgf.Activities.drawer.BackupActivity.REQUEST_CODE_OPENING;
import static com.afrofx.code.anjesgf.Activities.drawer.BackupActivity.REQUEST_CODE_SIGN_IN;
import static com.afrofx.code.anjesgf.DatabaseHelper.DB_NAME;

public class RemoteBackup {

    private static final String TAG = "Google Drive Activity";

    private DriveClient mDriveClient;
    private DriveResourceClient mDriveResourceClient;

    private sessionController se;

    public TaskCompletionSource<DriveId> mOpenItemTaskSource;

    private BackupActivity activity;

    private DataBufferAdapter<Metadata> mResultsAdapter;

    public RemoteBackup(BackupActivity activity) {
        this.activity = activity;
    }

    public void connectToDrive(boolean backup) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
        if (account == null) {
            signIn();
        } else {
            //Initialize the drive api
            mDriveClient = Drive.getDriveClient(activity, account);
            // Build a drive resource client.
            mDriveResourceClient = Drive.getDriveResourceClient(activity, account);
            if (backup)
                startDriveBackup();
            else
                startDriveRestore();
        }
    }

    private void signIn() {
        Log.i(TAG, "Start sign in");
        GoogleSignInClient GoogleSignInClient = buildGoogleSignInClient();
        activity.startActivityForResult(GoogleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    protected DriveResourceClient getDriveResourceClient() {
        return mDriveResourceClient;
    }

    private GoogleSignInClient buildGoogleSignInClient() {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(Drive.SCOPE_FILE)
                        .build();
        return GoogleSignIn.getClient(activity, signInOptions);
    }


    private void startDriveBackup() {
        mDriveResourceClient
                .createContents()
                .continueWithTask(
                        task -> createFileIntentSender(task.getResult()))
                .addOnFailureListener(
                        e -> Log.w(TAG, "Erro ao criar novos conteudos.", e));
    }

    private Task<Void> createFileIntentSender(DriveContents driveContents) {

        final String inFileName = activity.getDatabasePath(DB_NAME).toString();


        try {
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);
            OutputStream outputStream = driveContents.getOutputStream();

            // Transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }


        } catch (IOException e) {
            e.printStackTrace();
            janelaErro(null, "Ocorreu um Erro");
        }

        MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                .setTitle("econta.db")
                .setMimeType("application/db")
                .build();


        CreateFileActivityOptions createFileActivityOptions =
                new CreateFileActivityOptions.Builder()
                        .setInitialMetadata(metadataChangeSet)
                        .setInitialDriveContents(driveContents)
                        .build();

        return mDriveClient
                .newCreateFileActivityIntentSender(createFileActivityOptions)
                .continueWith(
                        task -> {
                            activity.startIntentSenderForResult(task.getResult(), REQUEST_CODE_CREATION, null, 0, 0, 0);
                            return null;
                        });
    }



    private void startDriveRestore() {
        pickFile()
                .addOnSuccessListener(activity,
                        driveId -> retrieveContents(driveId.asDriveFile()))
                .addOnFailureListener(activity, e -> {
                    Log.e(TAG, "Nenhum ficheiro selecionado", e);
                });
    }

    private void retrieveContents(DriveFile file) {

        //DB Path
        final String inFileName = activity.getDatabasePath(DB_NAME).toString();

        Task<DriveContents> openFileTask = mDriveResourceClient.openFile(file, DriveFile.MODE_READ_ONLY);

        openFileTask
                .continueWithTask(task -> {
                    DriveContents contents = task.getResult();
                    try {
                        ParcelFileDescriptor parcelFileDescriptor = contents.getParcelFileDescriptor();
                        FileInputStream fileInputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());

                        // Open the empty db as the output stream
                        OutputStream output = new FileOutputStream(inFileName);

                        // Transfer bytes from the inputfile to the outputfile
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fileInputStream.read(buffer)) > 0) {
                            output.write(buffer, 0, length);
                        }

                        // Close the streams
                        output.flush();
                        output.close();
                        fileInputStream.close();
                        janelaSucesso(null, "Dados Recuperados");

                        se.setLoggedIn(false);
                        Intent i1 = new Intent (activity, LoginActivity.class);
                        activity.startActivity(i1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return mDriveResourceClient.discardContents(contents);

                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Unable to read contents", e);
                });
    }

    private Task<DriveId> pickItem(OpenFileActivityOptions openOptions) {
        mOpenItemTaskSource = new TaskCompletionSource<>();
        mDriveClient
                .newOpenFileActivityIntentSender(openOptions)
                .continueWith((Continuation<IntentSender, Void>) task -> {
                    activity.startIntentSenderForResult(
                            task.getResult(), REQUEST_CODE_OPENING, null, 0, 0, 0);
                    return null;
                });
        return mOpenItemTaskSource.getTask();
    }

    private Task<DriveId> pickFile() {
        OpenFileActivityOptions openOptions =
                new OpenFileActivityOptions.Builder()
                        .setSelectionFilter(Filters.eq(SearchableField.MIME_TYPE, "application/db"))
                        .setActivityTitle("Selecione o econta.db")
                        .build();
        return pickItem(openOptions);
    }


    public void janelaErro(String titlo, String msg) {
        new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(titlo)
                .setContentText(msg)
                .show();
    }

    public void janelaSucesso(String titlo, String msg) {
        new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(titlo)
                .setContentText(msg)
                .show();
    }




}
