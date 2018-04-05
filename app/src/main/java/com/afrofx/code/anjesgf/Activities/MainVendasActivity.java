package com.afrofx.code.anjesgf.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.ProdutoAdapter;
import com.afrofx.code.anjesgf.models.ContaModel;
import com.afrofx.code.anjesgf.models.RegistoVendaModel;
import com.afrofx.code.anjesgf.models.StockModel;
import com.afrofx.code.anjesgf.models.VendasModel;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainVendasActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Toolbar toolbar;
    private CardView OpcaoVenda1, OpcaoVenda2, OpcaoVenda3, OpcaoVenda4;

    private List<StockModel> produStockModels;
    private ProdutoAdapter produtoAdapter;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_vendas);

        toolbar = (Toolbar) findViewById(R.id.toolbarMainVendas);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        OpcaoVenda1 = (CardView)findViewById(R.id.OpcaoVenda1);
        OpcaoVenda2 = (CardView)findViewById(R.id.OpcaoVenda2);
        OpcaoVenda3 = (CardView)findViewById(R.id.OpcaoVenda3);
        OpcaoVenda4 = (CardView)findViewById(R.id.OpcaoVenda4);

        EscolhaOpcao();

        db = new DatabaseHelper(this);
    }

    public void EscolhaOpcao(){
        OpcaoVenda1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainVendasActivity.this, VendasActivity.class));
                finish();
            }
        });

        OpcaoVenda2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainVendasActivity.this, MercadoriaVendidaActivity.class));
                finish();
            }
        });

        OpcaoVenda3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        OpcaoVenda4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainVendasActivity.this, MaisVendidosActivity.class));
                finish();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_topbar_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.but_vender) {
            fastSale();
            return true;
        } else if (id == android.R.id.home) {
            startActivity(new Intent(MainVendasActivity.this, MainScreenActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private List<StockModel> addProduto() {
        List<StockModel> lista = new ArrayList<>();
        db = new DatabaseHelper(this);
        Cursor data = db.listProduto();
        if (data.getCount() == 0) {
            new SweetAlertDialog(MainVendasActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setContentText("Sem Produtos")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            finish();
                            startActivity(new Intent(MainVendasActivity.this, MainVendasActivity.class));
                        }
                    })
                    .show();
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
        db = new DatabaseHelper(this);

        produStockModels = addProduto();
        nomeProduto.setThreshold(1);
        produtoAdapter = new ProdutoAdapter(this, R.layout.topbar_vendas_screen, R.layout.linha_categoria, produStockModels);
        nomeProduto.setAdapter(produtoAdapter);

        nomeConta.setOnItemSelectedListener(this);

        final DatabaseHelper db = new DatabaseHelper(this);

        // Spinner Drop down elements
        List<String> lables = db.listContas();

        final double[] preco = {0};
        final double[] totl = {0};
        final double[] qua = {0};

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button

        // attaching data adapter to spinner
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

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

        /*As operacoes que serao feitas no alert Dialog*/
        alertDialogBuilder.setCancelable(false).setPositiveButton("Concluir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                double quant = !quantidadeProduto.getText().toString().equals("") ?
                        Double.parseDouble(quantidadeProduto.getText().toString()) : 0.0;
                int id_user = (mSharedPreference.getInt("id_user", 0));
                int idConta = db.idConta(nomeConta.getSelectedItem().toString());

                if (quant == 0.0 || nomeProduto.equals("")) {
                    Toast.makeText(MainVendasActivity.this, "Preencha Todos Campos", Toast.LENGTH_SHORT).show();
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
                            new SweetAlertDialog(MainVendasActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Produto Vendido")
                                    .show();
                        } else {
                            new SweetAlertDialog(MainVendasActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Erro Tecnico")
                                    .show();
                        }
                    } else {
                        new SweetAlertDialog(MainVendasActivity.this, SweetAlertDialog.ERROR_TYPE)
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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
