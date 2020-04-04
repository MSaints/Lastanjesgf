package com.afrofx.code.anjesgf.Activities.root;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.afrofx.code.anjesgf.Activities.drawer.AnaliseActivity;
import com.afrofx.code.anjesgf.Activities.drawer.BackupActivity;
import com.afrofx.code.anjesgf.Activities.drawer.DefinitionsActivity;
import com.afrofx.code.anjesgf.Activities.drawer.PersonalizarActivity;
import com.afrofx.code.anjesgf.Activities.drawer.RelatoriosGeradosActivity;
import com.afrofx.code.anjesgf.BottomNavHelper;
import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.Dialogs.DialogAcerca;
import com.afrofx.code.anjesgf.Dialogs.DialogAjuda;
import com.afrofx.code.anjesgf.Dialogs.DialogFastSale;
import com.afrofx.code.anjesgf.Fragments.ClientFragment;
import com.afrofx.code.anjesgf.Fragments.HomeFragment;
import com.afrofx.code.anjesgf.Fragments.NotificationsFragment;
import com.afrofx.code.anjesgf.Fragments.RelatorioFragment;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.ThemeSettings.ThemeColor;
import com.afrofx.code.anjesgf.sessionController;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private sessionController se;

    private BottomNavigationView bottomBar;

    DatabaseHelper db;

    private final int REQUEST_CODE_ASK_PERMISSIONS =999;

    private ClientFragment clientFragment;
    private HomeFragment homeFragment;
    private RelatorioFragment relatorioFragment;
    private NotificationsFragment notificationsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            new ThemeColor(this);
        }

        setContentView(R.layout.main_screen);


        se = new sessionController(this);

        //requestPermission();
        if (!se.loggedIn()) {
            logout();
        }

        db = new DatabaseHelper(this);


//        int count = 100; //Declare as inatance variable
//
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        displayData();
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                            }
//                        }, 15000);
//
//                    }
//                });
//            }15000
//        }, 0, 3000);

        homeFragment = new HomeFragment();
        notificationsFragment = new NotificationsFragment();
        relatorioFragment = new RelatorioFragment();
        clientFragment = new ClientFragment();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMainScreen);
        setSupportActionBar(toolbar);
        bottomBar = (BottomNavigationView) findViewById(R.id.bottonNavBar);
        BottomNavHelper.disableShiftMode(bottomBar);

        Button fastsale = (Button) findViewById(R.id.fastSale);
        fastsale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFastSale dialogFastSale = new DialogFastSale(MainScreenActivity.this);
                dialogFastSale.show();
            }
        });


        setFragment(homeFragment);



        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String value = (mSharedPreference.getString("name_user", ""));
        long imagem = (mSharedPreference.getLong("id_user", 0));
        /*byte[] array = ;

        Bitmap bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);*/



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final View header = navigationView.getHeaderView(0);
        TextView username = (TextView) header.findViewById(R.id.user_perfil_nome);
        CircleImageView imagePerfil = (CircleImageView) header.findViewById(R.id.imagePerfil);
        try {
            imagePerfil.setImageBitmap(db.getImage(imagem));
        }catch (Exception e){
            e.printStackTrace();
        }
        username.setText(value);

        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottomMenu1:
                        setFragment(homeFragment);
                        return true;
                    case R.id.bottomMenu2:
                        setFragment(notificationsFragment);
                        return true;
                    case R.id.bottomMenu3:
                        setFragment(clientFragment);
                        return true;
                    case R.id.bottomMenu4:
                        setFragment(relatorioFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.drawerPersonalizar) {
            startActivity(new Intent(this, PersonalizarActivity.class));
            finish();
        } else if (id == R.id.drawerMensagens) {
            startActivity(new Intent(this, AnaliseActivity.class));
            finish();
        } else if (id == R.id.drawerSair) {
            logout();
        } else if (id == R.id.drawerRelatorio) {
            startActivity(new Intent(this, RelatoriosGeradosActivity.class));
            finish();
        } else if (id == R.id.drawerAcerca) {
            DialogAcerca dialogAcerca = new DialogAcerca(this);
            dialogAcerca.show();
        } else if (id == R.id.drawerAjuda) {
            DialogAjuda dialogAjuda = new DialogAjuda(this);
            dialogAjuda.show();
        } else if (id == R.id.drawerDefinicoes) {
            startActivity(new Intent(this, DefinitionsActivity.class));
            finish();
        } else if (id == R.id.drawerBackup) {
            startActivity(new Intent(this, BackupActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        se.setLoggedIn(false);
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Terminar a Sessão?")
                .setCancelText("Não")
                .setConfirmText("Sim")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.setTitleText("Sessão Terminada!")
                                .setConfirmText("OK")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        finish();
                                        startActivity(new Intent(MainScreenActivity.this, LoginActivity.class));
                                    }
                                })
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        sDialog.setCancelable(false);
                    }
                }).show();
    }
/*
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PRIVILEGED) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat
                    .requestPermissions(this, new String[]{Manifest.permission.CALL_PRIVILEGED}, REQUEST_CODE_ASK_PERMISSIONS);
        }if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat
                    .requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
        }if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat
                    .requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Acesso Aceite", Toast.LENGTH_SHORT)
                            .show();
                } else {
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }*/
}
