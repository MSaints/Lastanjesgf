package com.afrofx.code.anjesgf.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.BottomNavHelper;
import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.Fragments.ClientFragment;
import com.afrofx.code.anjesgf.Fragments.RelatorioFragment;
import com.afrofx.code.anjesgf.Fragments.HomeFragment;
import com.afrofx.code.anjesgf.Fragments.NotificationsFragment;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.ProdutoAdapter;
import com.afrofx.code.anjesgf.models.StockModel;
import com.afrofx.code.anjesgf.models.UserModel;
import com.afrofx.code.anjesgf.sessionController;

import java.util.ArrayList;
import java.util.List;

public class MainScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private sessionController se;

    private List<StockModel> produStockModels;

    private ProdutoAdapter produtoAdapter;

    UserModel userModel = new UserModel();

    private BottomNavigationView bottomBar;

    private FrameLayout mainFrameLayout;

    private DatabaseHelper db;

    private ClientFragment clientFragment;
    private HomeFragment homeFragment;
    private RelatorioFragment relatorioFragment;
    private NotificationsFragment notificationsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        setContentView(R.layout.main_screen);

        se = new sessionController(this);


        if (!se.loggedIn()) {
            logout();
        }


        homeFragment = new HomeFragment();
        notificationsFragment = new NotificationsFragment();
        relatorioFragment = new RelatorioFragment();
        clientFragment = new ClientFragment();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomBar = (BottomNavigationView) findViewById(R.id.bottonNavBar);
        mainFrameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        BottomNavHelper.disableShiftMode(bottomBar);


        setFragment(homeFragment);

        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String value = (mSharedPreference.getString("NameOfShared", "Default_Value"));


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        final View header = navigationView.getHeaderView(0);
        TextView username = (TextView) header.findViewById(R.id.user_perfil_nome);

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

    private void setFragment(android.support.v4.app.Fragment fragment) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        /*Coloca todos os itens no Actions Bar, isto e os menus*/
        getMenuInflater().inflate(R.menu.main_topbar_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*Responsavel por rastrear os cliques no action bar*/
        int id = item.getItemId();
        /*O icone de venda pre definido*/
        if (id == R.id.but_vender) {
            fastSale();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        /*Elementos do Navegation Drawer, icons e suas funcionalidades*/
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


    public void fastSale() {
        LayoutInflater li = LayoutInflater.from(this);
        View vendaView = li.inflate(R.layout.prompt_vender, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        /*Responsavel por colocar o layout no alertDialog*/
        alertDialogBuilder.setView(vendaView);

        final AutoCompleteTextView nomeProduto = (AutoCompleteTextView) vendaView.findViewById(R.id.venderNome);
        final EditText quantidadeProduto = (EditText) vendaView.findViewById(R.id.venderQuantidade);
        final Spinner nomeConta = (Spinner) vendaView.findViewById(R.id.venderConta);

        nomeConta.setOnItemSelectedListener(this);

        DatabaseHelper db = new DatabaseHelper(this);

        produStockModels = addProduto();
        nomeProduto.setThreshold(1);
        produtoAdapter = new ProdutoAdapter(this, R.layout.topbar_body_main_screen, R.layout.linha_categoria, produStockModels);
        nomeProduto.setAdapter(produtoAdapter);

        // Spinner Drop down elements
        List<String> lables = db.listContas();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        nomeConta.setAdapter(dataAdapter);

        /*As operacoes que serao feitas no alert Dialog*/
        alertDialogBuilder.setCancelable(false).setPositiveButton("Concluir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String nomePro = nomeProduto.getText().toString();
                int quantiPro = Integer.parseInt(quantidadeProduto.getText().toString());

            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        // Criar O Alerta
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Mostra o alerta
        alertDialog.show();
    }


    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "You selected: " + label,
                Toast.LENGTH_LONG).show();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private List<StockModel> addProduto() {
        List<StockModel> lista = new ArrayList<>();
        db = new DatabaseHelper(this);
        Cursor data = db.listProduto();
        if (data.getCount() == 0) {
            Toast.makeText(this, "Nao Produtos para vender", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                int id_produto = Integer.parseInt(data.getString(0));
                double quantide_produto = Double.parseDouble(data.getString(5));
                double preco_venda = Double.parseDouble(data.getString(8));
                String nome_produto = data.getString(4);

                StockModel listItem = new StockModel(id_produto, quantide_produto, preco_venda, nome_produto);
                lista.add(listItem);
            }
        }
        return lista;
    }
}
