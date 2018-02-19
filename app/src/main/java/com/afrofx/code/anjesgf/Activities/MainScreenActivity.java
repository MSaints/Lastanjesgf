package com.afrofx.code.anjesgf.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.BottomNavHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.UserModel;
import com.afrofx.code.anjesgf.sessionController;

public class MainScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private sessionController se;


    UserModel userModel = new UserModel();

    private BottomNavigationView bottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.main_screen);

        se = new sessionController(this);

        if (!se.loggedIn()) {
            logout();
        }

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomBar  =(BottomNavigationView)findViewById(R.id.bottonNavBar);
        BottomNavHelper.disableShiftMode(bottomBar);


        final CardView cardView_Stock = (CardView) findViewById(R.id.card_entrar_stock);
        cardView_Stock.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreenActivity.this, StockActivity.class));
            }
        });

        final CardView cardView_Caixa = (CardView) findViewById(R.id.card_entrar_caixa);
        cardView_Caixa.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreenActivity.this, CaixaActivity.class));
            }
        });

        final CardView cardView_relatorio = (CardView) findViewById(R.id.card_entrar_relatorio);
        cardView_relatorio.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreenActivity.this, RelatorioActivity.class));
            }
        });

        final CardView cardView_Vendas = (CardView) findViewById(R.id.card_entrar_vendas);
        cardView_Vendas.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreenActivity.this, VendasActivity.class));
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
       View header=navigationView.getHeaderView(0);
        TextView username = (TextView)header.findViewById(R.id.user_perfil_nome);
        if(userModel!=null){

        }else{
            username.setText("Nome do Perfil");
        }


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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_topbar_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.but_vender) {
            Toast.makeText(getApplication(), "Clickei", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.txt_perfil) {

        } else if (id == R.id.txt_mensagens) {

        } else if (id == R.id.txt_sair) {
            logout();
        } else if (id == R.id.txt_definicoes) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        se.setLoggedIn(false);
        finish();
        startActivity(new Intent(MainScreenActivity.this, LoginActivity.class));
    }

}
