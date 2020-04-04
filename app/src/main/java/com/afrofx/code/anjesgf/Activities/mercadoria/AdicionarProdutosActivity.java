package com.afrofx.code.anjesgf.Activities.mercadoria;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.afrofx.code.anjesgf.Activities.home.MercadoriaActivity;
import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.ThemeSettings.Constant;
import com.afrofx.code.anjesgf.ThemeSettings.ThemeColor;
import com.afrofx.code.anjesgf.adpters.CategoriaAdapter;
import com.afrofx.code.anjesgf.adpters.FornecedorAdapter;
import com.afrofx.code.anjesgf.adpters.UnidadeAdapter;
import com.afrofx.code.anjesgf.models.CategoriaModel;
import com.afrofx.code.anjesgf.models.ContaModel;
import com.afrofx.code.anjesgf.models.FornecedorModel;
import com.afrofx.code.anjesgf.models.StockModel;
import com.afrofx.code.anjesgf.models.UnidadeModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdicionarProdutosActivity extends AppCompatActivity {
    
    private Toolbar toolbar;

    private EditText edit_quantidade, edit_preco_compra, edit_preco_venda, edit_quanti_unidade, edit_quantidade_minima;
    AutoCompleteTextView txt_categoria, txt_unidade, txt_fornecedor,edit_nome;
    private Button but_validade_produto;
    
    private DatabaseHelper db;

    private double saldo;

    private int id_categoria = 0;
    private int id_unidade = 0;
    private int id_fornecedor = 0;

    private double quantidade_final;

    private List<CategoriaModel> mList;
    private List<UnidadeModel> unidadeList;
    private List<FornecedorModel> fornecedorList;

    CategoriaAdapter adapter;
    UnidadeAdapter unidadeAdapter;
    FornecedorAdapter fornecedorAdapter;
    private SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = Calendar.getInstance();

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd'/'MM'/'yyyy");
    String dataInicio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            new ThemeColor(this);
        }

        setContentView(R.layout.activity_adicionar_produtos);

        toolbar = findViewById(R.id.toolbarAdicionarProdutos);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);
        String text1 = simpleDateFormat.format(calendar.getTime());

        but_validade_produto = findViewById(R.id.add_data_validade);
        but_validade_produto.setText(text1);


        edit_quantidade = findViewById(R.id.add_quantidade);
        edit_preco_compra = findViewById(R.id.add_preco_compra);
        edit_preco_venda = findViewById(R.id.add_preco_venda);
        edit_quanti_unidade = findViewById(R.id.add_quantidade_unidade);
        edit_quantidade_minima = findViewById(R.id.add_quantidade_minima);

        edit_nome = findViewById(R.id.add_nome_produto);
        txt_categoria = findViewById(R.id.add_categoria);
        txt_unidade = findViewById(R.id.add_unidade);
        txt_fornecedor = findViewById(R.id.add_fornecedor);

        Button but_reg_produto = findViewById(R.id.add_produto);


        but_validade_produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataInicio();
            }
        });

        but_reg_produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduto();
            }
        });

        mList = addCategoria();
        txt_categoria.setThreshold(1);
        adapter = new CategoriaAdapter(this, R.layout.content_adicionar_produtos, android.R.layout.simple_spinner_item, mList);
        txt_categoria.setAdapter(adapter);

        unidadeList = addUnidade();
        txt_unidade.setThreshold(1);
        unidadeAdapter = new UnidadeAdapter(this, R.layout.content_adicionar_produtos, android.R.layout.simple_spinner_item, unidadeList);
        txt_unidade.setAdapter(unidadeAdapter);

        fornecedorList = addFornecedor();
        txt_fornecedor.setThreshold(1);
        fornecedorAdapter = new FornecedorAdapter(this, R.layout.content_adicionar_produtos, android.R.layout.simple_spinner_item, fornecedorList);
        txt_fornecedor.setAdapter(fornecedorAdapter);


    }

    public void setDataInicio() {
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int ano = calendar.get(Calendar.YEAR);

        android.app.DatePickerDialog dialog = new android.app.DatePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog, new android.app.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar calendario = new GregorianCalendar(year, month, day);
                dataInicio = simpleDate.format(calendario.getTime());
                but_validade_produto.setText(simpleDateFormat.format(calendario.getTime()));
            }
        }, ano, mes, dia);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(AdicionarProdutosActivity.this, MercadoriaActivity.class));
            finish();
            return true;
        }
        return false;
    }


    public void addProduto() {

        final String produto_nome = edit_nome.getText().toString();
        final String produto_unidade = txt_unidade.getText().toString();
        final String produto_fornecedor = txt_fornecedor.getText().toString();
        final String produto_categoria = txt_categoria.getText().toString().trim();

        final double produto_quantidade = !edit_quantidade.getText().toString().equals("") ?
                Double.parseDouble(edit_quantidade.getText().toString()) : 0.0;
        final double produto_preco_compra = !edit_preco_compra.getText().toString().equals("") ?
                Double.parseDouble(edit_preco_compra.getText().toString()) : 0.0;
        final double produto_preco_venda = !edit_preco_venda.getText().toString().equals("") ?
                Double.parseDouble(edit_preco_venda.getText().toString()) : 0.0;
        final double produto_quantidade_unidade = !edit_quanti_unidade.getText().toString().equals("") ?
                Double.parseDouble(edit_quanti_unidade.getText().toString()) : 0.0;
        final double produto_quantidade_minima = !edit_quantidade_minima.getText().toString().equals("") ?
                Double.parseDouble(edit_quantidade_minima.getText().toString()) : 0.0;
        final StockModel[] stockModel = {db.procuraProduto(produto_nome)};
        final String prazo = dataInicio;

        quantidade_final =  produto_quantidade;

        if(produto_quantidade_unidade>0){
            quantidade_final = produto_quantidade_unidade * produto_quantidade;
        }


        final long[] idConta = new long[1];

        if (produto_nome.equals("")) {
            mensagem("Preencha os Campos");
        } else {
            if (stockModel[0] == null) {
                if (addCategory()) {
                    id_categoria = db.procuraCategoria(produto_categoria);
                } else {
                    id_categoria = db.procuraCategoria(produto_categoria);
                }
                if (addUnit()) {
                    id_unidade = db.procuraUnidade(produto_unidade);
                } else {
                    id_categoria = db.procuraCategoria(produto_categoria);
                }
                if (findFornecedor() && produto_fornecedor.length() > 2) {
                    mensagem("O Fornecedor não existe");
                } else if (!findFornecedor() || produto_fornecedor.equals("")) {
                    id_fornecedor = db.procuraFornecedor(produto_fornecedor);
                }

                if (produto_quantidade > 0) {
                    LayoutInflater li = LayoutInflater.from(this);
                    View pagaVia = li.inflate(R.layout.promprt_paga_via, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                    alertDialogBuilder.setView(pagaVia);

                    final Spinner nomeConta = pagaVia.findViewById(R.id.venderConta);
                    final TextView saldoDisponivel = pagaVia.findViewById(R.id.txtValorDisponivel);
                    final TextView valorTotal = pagaVia.findViewById(R.id.txtValorTotal);
                    final View v1 = pagaVia.findViewById(R.id.v1);
                    final TextView definitionInfoTitle = pagaVia.findViewById(R.id.definitionInfoTitle);


                    v1.setBackgroundColor(Constant.color);

                    definitionInfoTitle.setBackgroundColor(Constant.color);

                    db = new DatabaseHelper(this);

                    final List<String> lables = db.listContas2();

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);

                    nomeConta.setAdapter(dataAdapter);

                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                    nomeConta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String label = parent.getItemAtPosition(position).toString();
                            saldo = db.SaldoTotal(label);
                            idConta[0] = db.idConta(label);
                            saldoDisponivel.setText(saldo + " MT");
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    valorTotal.setText(produto_preco_compra + " MT");
                    alertDialogBuilder.setCancelable(true).setPositiveButton("Concluir", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (saldo >= produto_preco_compra) {
                                ContaModel contaModel = new ContaModel(produto_preco_compra, idConta[0], 0);
                                db.registarValor(contaModel);

                                stockModel[0] = new StockModel(id_categoria, id_unidade, id_fornecedor, quantidade_final,
                                        db.idOperacao(), produto_quantidade_minima, produto_preco_venda, produto_nome, prazo);

                                db.inserirProduto(stockModel[0]);

                                new SweetAlertDialog(AdicionarProdutosActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText(null)
                                        .setContentText("Produto Adicionado")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                startActivity(new Intent(AdicionarProdutosActivity.this, ListaProdutosActivity.class));
                                            }
                                        })
                                        .show();
                            } else {
                                mensagem("Saldo Insuficiente");
                            }
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();

                    alertDialog.show();
                }

            } else {
                mensagem("Produto ja foi registado");
            }
        }
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


    private boolean addCategory() {
        boolean r;
        String produto_categoria = txt_categoria.getText().toString();
        CategoriaModel categoriaModel = db.procurarCategoria(produto_categoria);

        if (categoriaModel == null) {
            categoriaModel = new CategoriaModel(produto_categoria);
            db.registarCategoria(categoriaModel);
            r = false;
        } else {
            db.procuraCategoria(produto_categoria);
            r = true;
        }
        return r;
    }

    private boolean findFornecedor() {
        boolean r = false;
        String fornecedores = txt_fornecedor.getText().toString();
        FornecedorModel fornecedorModel = db.procurarForndecedor(fornecedores);

        if (fornecedorModel == null) {
            db.procuraFornecedor(fornecedores);
            r = true;
        }
        return r;
    }

    private List<CategoriaModel> addCategoria() {
        List<CategoriaModel> lista = new ArrayList<>();
        db = new DatabaseHelper(this);
        Cursor data = db.listCategoria();
        if (data.getCount() == 0) {
        } else {
            while (data.moveToNext()) {
                int id_categoria = Integer.parseInt(data.getString(0));
                String nome = data.getString(1);
                CategoriaModel listItem = new CategoriaModel(id_categoria, nome);
                lista.add(listItem);
            }
        }
        return lista;
    }

    private List<UnidadeModel> addUnidade() {
        List<UnidadeModel> lista = new ArrayList<>();
        db = new DatabaseHelper(this);
        Cursor data = db.listUnidade();
        if (data.getCount() == 0) {
        } else {
            while (data.moveToNext()) {
                int id_unidade = Integer.parseInt(data.getString(0));
                String nome = data.getString(1);
                UnidadeModel listItem = new UnidadeModel(id_unidade, nome);
                lista.add(listItem);
            }
        }
        return lista;
    }

    private List<FornecedorModel> addFornecedor() {
        List<FornecedorModel> lista = new ArrayList<>();
        db = new DatabaseHelper(this);
        Cursor data = db.listFornecedor();
        if (data.getCount() == 0) {
        } else {
            while (data.moveToNext()) {
                int id_fornecedor = Integer.parseInt(data.getString(0));
                String nome = data.getString(2);
                FornecedorModel listItem = new FornecedorModel(id_fornecedor, nome);
                lista.add(listItem);
            }
        }
        return lista;
    }

    private boolean addUnit() {
        boolean r;
        String produto_unidade = txt_unidade.getText().toString();
        UnidadeModel unidadeModel = db.procurarUnidade(produto_unidade);

        if (unidadeModel == null) {
            unidadeModel = new UnidadeModel(produto_unidade);
            db.registarUnidade(unidadeModel);
            r = false;
        } else {
            db.procuraUnidade(produto_unidade);
            r = true;
        }
        return r;
    }


    private void mensagem(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }



}
