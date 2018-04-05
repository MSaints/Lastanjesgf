package com.afrofx.code.anjesgf.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
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
import android.text.Editable;
import android.text.TextWatcher;
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
import com.afrofx.code.anjesgf.models.ContaModel;
import com.afrofx.code.anjesgf.models.RegistoVendaModel;
import com.afrofx.code.anjesgf.models.StockModel;
import com.afrofx.code.anjesgf.models.UserModel;
import com.afrofx.code.anjesgf.models.VendasModel;
import com.afrofx.code.anjesgf.sessionController;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private sessionController se;

    private List<StockModel> produStockModels;

    private ProdutoAdapter produtoAdapter;

    private BottomNavigationView bottomBar;

    private DatabaseHelper db;

    private ClientFragment clientFragment;
    private HomeFragment homeFragment;
    private RelatorioFragment relatorioFragment;
    private NotificationsFragment notificationsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        se = new sessionController(this);


        if (!se.loggedIn()) {
            logout();
        }


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


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomBar = (BottomNavigationView) findViewById(R.id.bottonNavBar);
        FrameLayout mainFrameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        BottomNavHelper.disableShiftMode(bottomBar);


        setFragment(homeFragment);

        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String value = (mSharedPreference.getString("name_user", "Default_Value"));


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

    public void displayData() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View v = layoutInflater.inflate(R.layout.prompt_lock_screen, null);
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
        aBuilder.setView(v);

        aBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });

        AlertDialog alertDialog = aBuilder.create();
        alertDialog.show();
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

        if (id == R.id.drawerBanca) {
            startActivity(new Intent(this, MinhaBancaActivity.class));
            finish();
        } else if (id == R.id.drawerMensagens) {
            startActivity(new Intent(this, MensagensActivity.class));
            finish();
        } else if (id == R.id.drawerSair) {
            logout();
        } else if (id == R.id.drawerRelatorio) {
            startActivity(new Intent(this, RelatoriosGeradosActivity.class));
            finish();
        } else if (id == R.id.drawerAcerca) {
            startActivity(new Intent(this, DefinitionsActivity.class));
            finish();
        } else if (id == R.id.drawerAjuda) {

        } else if (id == R.id.drawerDefinicoes) {
            startActivity(new Intent(this, DefinitionsActivity.class));
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
                })
                .show();
    }


    public void fastSale() {
        LayoutInflater li = LayoutInflater.from(this);
        View vendaView = li.inflate(R.layout.prompt_vender, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        /*Responsavel por colocar o layout no alertDialog*/
        alertDialogBuilder.setView(vendaView);

        final AutoCompleteTextView nomeProduto = (AutoCompleteTextView) vendaView.findViewById(R.id.MainVenderNome);
        final EditText quantidadeProduto = (EditText) vendaView.findViewById(R.id.MainVenderQuantidade);
        final Spinner nomeConta = (Spinner) vendaView.findViewById(R.id.MainVenderConta);
        final TextView precoUn = (TextView) vendaView.findViewById(R.id.MainPrecoUn);
        final TextView precoTo = (TextView) vendaView.findViewById(R.id.MainPrecoTo);

        nomeConta.setOnItemSelectedListener(this);

        final DatabaseHelper db = new DatabaseHelper(this);

        final double[] preco = {0};
        final double[] totl = {0};
        final double[] qua = {0};
        final double precoP = 0;

        produStockModels = addProduto();
        nomeProduto.setThreshold(1);
        produtoAdapter = new ProdutoAdapter(this, R.layout.topbar_body_main_screen, R.layout.linha_categoria, produStockModels);
        nomeProduto.setAdapter(produtoAdapter);

        List<String> lables = db.listContas();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nomeConta.setAdapter(dataAdapter);

        nomeProduto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                qua[0] = produStockModels.get(pos).getProduto_quantidade();
                quantidadeProduto.setHint("" + qua[0]);
                preco[0] = produStockModels.get(pos).getProduto_preco_venda();
                precoUn.setText(preco[0] + "");
            }
        });


        quantidadeProduto.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double quantiPro = !String.valueOf(s).equals("") ? Double.parseDouble(String.valueOf(s)) : 0.0;
                totl[0] = quantiPro * preco[0];
                precoTo.setText(totl[0] + "");
            }
        });


        alertDialogBuilder.setCancelable(false).setPositiveButton("Concluir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                double quant = !quantidadeProduto.getText().toString().equals("") ?
                        Double.parseDouble(quantidadeProduto.getText().toString()) : 0.0;
                int id_user = (mSharedPreference.getInt("id_user", 0));
                int idConta = db.idConta(nomeConta.getSelectedItem().toString());

                if (quant == 0.0 || nomeProduto.equals("")) {
                    Toast.makeText(MainScreenActivity.this, "Preencha Todos Campos", Toast.LENGTH_SHORT).show();
                } else {
                    if (qua[0] >= quant) {
                        ContaModel contaModel = new ContaModel(totl[0], idConta, 1);
                        db.registarValor(contaModel);

                        int idRegistoConta = db.idOperacao();
                        RegistoVendaModel registoVendaModel = new RegistoVendaModel(id_user, idRegistoConta, 1);
                        db.registoVenda(registoVendaModel);


                        VendasModel vendasModel = new VendasModel(db.idProduto(nomeProduto.getText().toString()), db.idRegistoVenda(), quant, preco[0]);
                        db.registoVendas(vendasModel);

                        if (db.updateQuatidade(nomeProduto.getText().toString(), qua[0] - quant)) {
                            new SweetAlertDialog(MainScreenActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Produto Vendido")
                                    .show();
                        } else {
                            new SweetAlertDialog(MainScreenActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Erro Tecnico")
                                    .show();
                        }
                    } else {
                        new SweetAlertDialog(MainScreenActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Fora de Stock")
                                .show();
                    }
                }
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
        String label = parent.getItemAtPosition(position).toString();
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
