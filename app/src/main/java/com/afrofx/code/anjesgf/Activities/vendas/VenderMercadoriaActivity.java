package com.afrofx.code.anjesgf.Activities.vendas;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrofx.code.anjesgf.Activities.home.VendasActivity;
import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.Recyclers.VendasRecyclerAdapter;
import com.afrofx.code.anjesgf.ThemeSettings.Constant;
import com.afrofx.code.anjesgf.ThemeSettings.ThemeColor;
import com.afrofx.code.anjesgf.adpters.ClienteAdapter;
import com.afrofx.code.anjesgf.adpters.ProdutoAdapter;
import com.afrofx.code.anjesgf.models.ClienteModel;
import com.afrofx.code.anjesgf.models.ContaModel;
import com.afrofx.code.anjesgf.models.RegistoVendaModel;
import com.afrofx.code.anjesgf.models.StockModel;
import com.afrofx.code.anjesgf.models.VendasModel;
import com.afrofx.code.anjesgf.sessionController;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class VenderMercadoriaActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Toolbar toolbar;
    private DatabaseHelper db;
    private EditText txtQuantidade;
    private TextView txtSubtotal, txtIva, txtTotal;
    private ArrayList<VendasModel> vendasModelList = new ArrayList<>();
    ;
    private List<StockModel> produStockModels;
    private List<ClienteModel> clienteModelList;
    private ProdutoAdapter produtoAdapter;
    private ClienteAdapter clienteAdapter;
    private AutoCompleteTextView txtProduto;
    private Button compraAdicionar, aumentarQuantidade, diminuirQuantidade, concluirVenda;
    private sessionController se;


    private double precoP = 0;
    private boolean validar;
    private boolean existeCliente;
    private boolean existeRegistoCliente;
    private String nomep;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            new ThemeColor(this);
        }

        setContentView(R.layout.topbar_vendas_screen);

        txtProduto = findViewById(R.id.escolheProduto);

        txtQuantidade = findViewById(R.id.escolheQuantidade);

        txtSubtotal = findViewById(R.id.txtSubtotal);
        txtIva = findViewById(R.id.txtIva);
        txtTotal = findViewById(R.id.txtTotal);

        compraAdicionar = findViewById(R.id.compraAdicionar);
        aumentarQuantidade = findViewById(R.id.aumentarQuantidade);
        diminuirQuantidade = findViewById(R.id.diminuirQuantidade);
        concluirVenda = findViewById(R.id.concluirVenda);

        concluirVenda.setVisibility(View.GONE);

        db = new DatabaseHelper(this);

        toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(this);

        produStockModels = addProduto();
        txtProduto.setThreshold(1);
        produtoAdapter = new ProdutoAdapter(this, R.layout.content_adicionar_produtos, android.R.layout.simple_spinner_item, produStockModels);
        txtProduto.setAdapter(produtoAdapter);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listaCompraRecycler);
        recyclerView.setHasFixedSize(true);

        final double[] precos = new double[1];
        final double[] quantidade = {0};
        final double[] q = {0};


        txtProduto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                q[0] = produStockModels.get(pos).getProduto_quantidade();
                nomep = produStockModels.get(pos).getProduto_nome();
                if (q[0] <= 0) {
                    janelaErro(null, nomep + " Acabou");
                    txtProduto.setText("");
                    validar = false;
                } else {
                    txtQuantidade.setHint("Disponivel " + q[0]);
                    precos[0] = produStockModels.get(pos).getProduto_preco_venda();
                    validar = true;
                }

            }
        });


        aumentarQuantidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((quantidade[0] <= q[0]) && validar) {
                    quantidade[0]++;
                    txtQuantidade.setText(quantidade[0] + "");
                }
            }
        });

        diminuirQuantidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantidade[0]--;
                if ((quantidade[0] < 0)) {
                    quantidade[0] = 0;
                } else {
                    txtQuantidade.setText(quantidade[0] + "");
                }
            }
        });


        concluirVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                concluirVenda();
            }
        });


        compraAdicionar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean value1 = (mSharedPreference.getBoolean("activa_limite", false));
                int limite = 1000;

                if (value1 == true) {
                    limite = 5;
                }

                if (vendasModelList.size() <= limite) {
                    String nomeProduto2 = txtProduto.getText().toString().trim();
                    double quantidadeProduto1 = !txtQuantidade.getText().toString().equals("") ?
                            Double.parseDouble(txtQuantidade.getText().toString()) : 0.0;

                    double precoUnitario = precos[0];

                    if ((nomeProduto2.equals("")) || (quantidadeProduto1 == 0.0)) {
                        janelaErro(null, "Preencha os campos");
                    } else if (!nomep.equals(nomeProduto2)) {
                        janelaErro(null, "Produto Não Encontrado");
                    } else {

                        boolean add = false;
                        for (int i = 0; i < vendasModelList.size(); i++) {
                            VendasModel model = (VendasModel) vendasModelList.get(i);
                            if (model.getNomeProduto().equals(nomeProduto2)) {
                                vendasModelList.remove(model);
                                quantidadeProduto1 += model.getVenda_quantidade();
                                model.setVenda_quantidade(quantidadeProduto1);
                                model.setVenda_preco(quantidadeProduto1 * precoUnitario);
                                vendasModelList.add(model);

                                add = true;
                            }
                        }

                        if (!add) {
                            VendasModel vendasModel = new VendasModel(quantidadeProduto1, precoUnitario, nomeProduto2);
                            vendasModelList.add(vendasModel);
                        }


                        concluirVenda.setVisibility(View.VISIBLE);

                        double iva = 0;
                        double subtotal = 0;
                        double total = 0;

                        for (int i = 0; i < vendasModelList.size(); i++) {
                            subtotal = subtotal + (vendasModelList.get(i).getVenda_preco() * vendasModelList.get(i).getVenda_quantidade());
                        }


                        boolean value = (mSharedPreference.getBoolean("activa_iva", false));
                        float imposto = (mSharedPreference.getFloat("valor", 0));

                        if (value == true) {
                            iva = (imposto / 100) * subtotal;
                        }

                        total = iva + subtotal;

                        txtSubtotal.setText(subtotal + " MT");
                        txtIva.setText(iva + " MT");
                        txtTotal.setText(total + " MT");

                        precoP = total;

                        final VendasRecyclerAdapter vendasRecyclerAdapter = new VendasRecyclerAdapter(VenderMercadoriaActivity.this, vendasModelList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(VenderMercadoriaActivity.this));
                        recyclerView.setAdapter(vendasRecyclerAdapter);

                        vendasRecyclerAdapter.notifyDataSetChanged();

                        quantidade[0] = 0;

                        txtProduto.setText(null);
                        txtQuantidade.setText(null);
                        txtQuantidade.setHint(null);
                    }
                } else {
                    janelaErro(null, "Lista Cheia");
                }
            }
        });
    }


    public void concluirVenda() {
        LayoutInflater li2 = LayoutInflater.from(this);
        final View vendaView2 = li2.inflate(R.layout.prompt_vend_estruturada, null);
        AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(this);
        alertDialogBuilder2.setView(vendaView2);

        final AutoCompleteTextView vendaClienteNome = vendaView2.findViewById(R.id.vendaClienteNome);
        final EditText vendaClienteNumero = vendaView2.findViewById(R.id.vendaClienteNumero);
        final RadioGroup tipoVenda = vendaView2.findViewById(R.id.tipoVenda);
        final Spinner nomeConta2 = vendaView2.findViewById(R.id.venderConta);
        final TextView precoTota = vendaView2.findViewById(R.id.vendaPrecoTotal);
        final TextView definitionInfoTitle = vendaView2.findViewById(R.id.definitionInfoTitle);
        final LinearLayout infoCliente = vendaView2.findViewById(R.id.dadosCliente);
        final LinearLayout dadosConta = vendaView2.findViewById(R.id.dadosConta);

        tipoVenda.check(R.id.vendaProntoPagamento);
        infoCliente.setVisibility(View.GONE);
        dadosConta.setVisibility(View.VISIBLE);

        definitionInfoTitle.setBackgroundColor(Constant.color);

        clienteModelList = addCliente();
        vendaClienteNome.setThreshold(1);
        clienteAdapter = new ClienteAdapter(this, R.layout.topbar_vendas_screen, android.R.layout.simple_spinner_item, clienteModelList);
        vendaClienteNome.setAdapter(clienteAdapter);

        final String[] cnome = new String[1];
        final int[] cid = new int[1];
        final int[] cnumero = new int[1];

        vendaClienteNome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                cnome[0] = clienteModelList.get(pos).getNomeCliente();
                cid[0] = clienteModelList.get(pos).getIdCliente();
                cnumero[0] = clienteModelList.get(pos).getNumeroCliente();
                vendaClienteNumero.setText(cnumero[0] + "");
                existeRegistoCliente = true;
            }
        });

        tipoVenda.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (group.getCheckedRadioButtonId() == R.id.vendaFacturaPagamento) {
                    infoCliente.setVisibility(View.VISIBLE);
                    dadosConta.setVisibility(View.VISIBLE);
                    existeCliente = true;
                } else {
                    infoCliente.setVisibility(View.GONE);
                    dadosConta.setVisibility(View.VISIBLE);
                    existeCliente = false;
                }
            }
        });


        List<String> labless = db.listContas();
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labless);
        nomeConta2.setAdapter(dataAdapter2);

        precoTota.setText((precoP) + " MT");

        alertDialogBuilder2.setCancelable(false).setPositiveButton("Concluir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                long id_user = (mSharedPreference.getLong("id_user", 0));
                long idConta = db.idConta(nomeConta2.getSelectedItem().toString());


                if (existeCliente) {
                    String nomef = vendaClienteNome.getText().toString();
                    int numerof = Integer.parseInt(vendaClienteNumero.getText().toString());

                    if (!existeRegistoCliente) {
                        ClienteModel clienteModel = new ClienteModel(nomef, numerof);
                        db.registarCliente(clienteModel);
                        cid[0] = db.idCliente();
                    }

                    int idRegistoConta = db.idOperacao();
                    RegistoVendaModel registoVendaModel = new RegistoVendaModel(id_user, idRegistoConta, 0, cid[0]);
                    db.registoVenda(registoVendaModel);
                    ContaModel contaModel = new ContaModel(precoP, idConta, 2);
                    db.registarValor(contaModel);
                } else {
                    int idRegistoConta = db.idOperacao();
                    RegistoVendaModel registoVendaModel = new RegistoVendaModel(id_user, idRegistoConta, 1);
                    db.registoVenda(registoVendaModel);
                    ContaModel contaModel = new ContaModel(precoP, idConta, 1);
                    db.registarValor(contaModel);
                }


                for (int i = 0; i < vendasModelList.size(); i++) {
                    String nom = vendasModelList.get(i).getNomeProduto();
                    VendasModel vendasModel = new VendasModel(db.idProduto(nom), db.idRegistoVenda(),
                            vendasModelList.get(i).getVenda_quantidade(), vendasModelList.get(i).getVenda_preco());
                    db.registoVendas(vendasModel);
                    db.updateQuatidade(nom, db.quantidadePro(nom) - vendasModelList.get(i).getVenda_quantidade());
                }
                new SweetAlertDialog(VenderMercadoriaActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setContentText("Venda Efectuada")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                finish();
                                startActivity(new Intent(VenderMercadoriaActivity.this, VenderMercadoriaActivity.class));
                            }
                        })
                        .show();
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
            new SweetAlertDialog(VenderMercadoriaActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setContentText("Sem Produtos")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            finish();
                            startActivity(new Intent(VenderMercadoriaActivity.this, VendasActivity.class));
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

    public List<ClienteModel> addCliente() {
        List<ClienteModel> listac = new ArrayList<>();
        db = new DatabaseHelper(this);
        Cursor data = db.listCliente();
        if (data.getCount() == 0) {
        } else {
            while (data.moveToNext()) {
                int id_cliente = Integer.parseInt(data.getString(data.getColumnIndex("id_cliente")));
                String nomeClien = data.getString(data.getColumnIndex("cliente_nome"));
                int number = Integer.parseInt(data.getString(data.getColumnIndex("cliente_cell")));

                ClienteModel listItem = new ClienteModel(nomeClien, id_cliente, number);
                listac.add(listItem);
            }
        }
        return listac;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(VenderMercadoriaActivity.this, VendasActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String label = parent.getItemAtPosition(position).toString();
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
