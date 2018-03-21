package com.afrofx.code.anjesgf.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.ProdutoAdapter;
import com.afrofx.code.anjesgf.adpters.VendasRecyclerAdapter;
import com.afrofx.code.anjesgf.models.ContaModel;
import com.afrofx.code.anjesgf.models.RegistoVendaModel;
import com.afrofx.code.anjesgf.models.StockModel;
import com.afrofx.code.anjesgf.models.VendasModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Afro FX on 2/11/2018.
 */

public class VendasActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Toolbar toolbar;
    private DatabaseHelper db;
    private EditText txtQuantidade;
    private TextView txtSubtotal, txtIva, txtTotal;
    private ArrayList<VendasModel> vendasModelList;
    private List<StockModel> produStockModels;
    private ProdutoAdapter produtoAdapter;
    private AutoCompleteTextView txtProduto;
    private Button compraAdicionar, aumentarQuantidade, diminuirQuantidade, concluirVenda;

    private double precoP = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topbar_vendas_screen);

        txtProduto = (AutoCompleteTextView) findViewById(R.id.escolheProduto);

        txtQuantidade = (EditText) findViewById(R.id.escolheQuantidade);

        txtSubtotal = (TextView) findViewById(R.id.txtSubtotal);
        txtIva = (TextView) findViewById(R.id.txtIva);
        txtTotal = (TextView) findViewById(R.id.txtTotal);

        compraAdicionar = (Button) findViewById(R.id.compraAdicionar);
        aumentarQuantidade = (Button) findViewById(R.id.aumentarQuantidade);
        diminuirQuantidade = (Button) findViewById(R.id.diminuirQuantidade);
        concluirVenda = (Button) findViewById(R.id.concluirVenda);

        db = new DatabaseHelper(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        produStockModels = addProduto();
        txtProduto.setThreshold(1);
        produtoAdapter = new ProdutoAdapter(this, R.layout.fragment_add_produtos, R.layout.linha_categoria, produStockModels);
        txtProduto.setAdapter(produtoAdapter);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listaCompraRecycler);
        recyclerView.setHasFixedSize(true);

        final double[] precos = new double[1];
        final int[] quantidade = {0};

        txtProduto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                txtQuantidade.setHint("Disponivel " + produStockModels.get(pos).getProduto_quantidade());
                precos[0] = produStockModels.get(pos).getProduto_preco_venda();
            }
        });

        aumentarQuantidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantidade[0]++;
                txtQuantidade.setText(quantidade[0] + "");
            }
        });


        diminuirQuantidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantidade[0]--;
                if (quantidade[0] < 0) {
                    quantidade[0] = 0;
                } else {
                    txtQuantidade.setText(quantidade[0] + "");
                }
            }
        });


        vendasModelList = new ArrayList<>();

        concluirVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VendasActivity.this, "O produto ja existe na lista", Toast.LENGTH_LONG).show();
                concluirVenda();
            }
        });

        compraAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeProduto2 = txtProduto.getText().toString();
                final double quantidadeProduto1 = !txtQuantidade.getText().toString().equals("") ?
                        Double.parseDouble(txtQuantidade.getText().toString()) : 0.0;


                double precoUnitario = precos[0];
                String nome = "";
                for (int i = 0; i < vendasModelList.size(); i++) {
                    nome = vendasModelList.get(i).getNomeProduto();
                    if (nome == nomeProduto2) {
                        nome = nomeProduto2;
                    }
                }

                if (nome == nomeProduto2) {
                    Toast.makeText(VendasActivity.this, "O produto ja existe na lista", Toast.LENGTH_LONG).show();
                }
                if (nomeProduto2.equals("") || quantidadeProduto1 == 0.0) {
                    Toast.makeText(VendasActivity.this, "Preencha todos os campos", Toast.LENGTH_LONG).show();
                } else {
                    VendasModel vendasModel = new VendasModel(quantidadeProduto1, precoUnitario, nomeProduto2);

                    vendasModelList.add(vendasModel);

                    double iva = (quantidadeProduto1 * precoUnitario) * 0.17;

                    double subtotal = (quantidadeProduto1 * precoUnitario) - iva;

                    for (int i = 0; i < vendasModelList.size(); i++) {
                        subtotal = subtotal + (vendasModelList.get(i).getVenda_preco());
                    }

                    double ivas = quantidadeProduto1 * precos[0] * 0.17;
                    double total = ivas + subtotal;

                    txtSubtotal.setText(subtotal + " MT");
                    txtIva.setText(ivas + " MT");
                    txtTotal.setText(total + " MT");

                    precoP = total;

                    final VendasRecyclerAdapter vendasRecyclerAdapter = new VendasRecyclerAdapter(VendasActivity.this, vendasModelList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(VendasActivity.this));
                    recyclerView.setAdapter(vendasRecyclerAdapter);

                    vendasRecyclerAdapter.notifyDataSetChanged();


                    quantidade[0] = 0;

                    txtProduto.setText(null);
                    txtQuantidade.setText(null);
                    txtQuantidade.setHint(null);
                }
            }
        });
    }


    public void concluirVenda() {
        LayoutInflater li2 = LayoutInflater.from(this);
        View vendaView2 = li2.inflate(R.layout.prompt_vend_estruturada, null);
        AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(this);
        alertDialogBuilder2.setView(vendaView2);

        final AutoCompleteTextView vendaCliente = (AutoCompleteTextView) vendaView2.findViewById(R.id.vendaCliente);
        final EditText vendaDesconto = (EditText) vendaView2.findViewById(R.id.vendaDesconto);
        final Spinner nomeConta2 = (Spinner) vendaView2.findViewById(R.id.venderConta);
        final TextView precoTota = (TextView) vendaView2.findViewById(R.id.vendaPrecoTotal);

        List<String> labless = db.listContas();
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labless);
        nomeConta2.setAdapter(dataAdapter2);

        precoTota.setText((precoP) + " MT");

        double pre = !vendaDesconto.getText().toString().equals("") ?
                Double.parseDouble(vendaDesconto.getText().toString()) : 0.0;

        vendaDesconto.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double ff = !String.valueOf(s).equals("") ? Double.parseDouble(String.valueOf(s)) : 0.0;
                precoTota.setText((precoP - ff) + " MT");
            }
        });
        alertDialogBuilder2.setCancelable(false).setPositiveButton("Concluir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                int id_user = (mSharedPreference.getInt("id_user", 0));
                int idConta = db.idConta(nomeConta2.getSelectedItem().toString());

                        ContaModel contaModel = new ContaModel(precoP, idConta, 1);
                        db.registarValor(contaModel);

                        int idRegistoConta = db.idOperacao();
                        RegistoVendaModel registoVendaModel = new RegistoVendaModel(id_user, idRegistoConta, 1);
                        db.registoVenda(registoVendaModel);

                        for (int i = 0; i < vendasModelList.size(); i++) {
                            String nom = vendasModelList.get(i).getNomeProduto();
                            VendasModel vendasModel = new VendasModel(db.idProduto(nom), db.idRegistoVenda(),
                                    vendasModelList.get(i).getVenda_quantidade(), vendasModelList.get(i).getVenda_preco());
                            db.registoVendas(vendasModel);
                            db.updateQuatidade(nom, db.quantidadePro(nom)-vendasModelList.get(i).getVenda_quantidade());
                        }

                Toast.makeText(VendasActivity.this, "Vendido", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(VendasActivity.this, VendasActivity.class));
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder2.create();
        alertDialog.show();
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


    @Override
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
            startActivity(new Intent(VendasActivity.this, MainVendasActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                    Toast.makeText(VendasActivity.this, "Preencha Todos Campos", Toast.LENGTH_SHORT).show();
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
                            new SweetAlertDialog(VendasActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Produto Vendido")
                                    .show();
                        } else {
                            new SweetAlertDialog(VendasActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Erro Tecnico")
                                    .show();
                        }
                    } else {
                        new SweetAlertDialog(VendasActivity.this, SweetAlertDialog.ERROR_TYPE)
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

}
