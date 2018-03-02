package com.afrofx.code.anjesgf.Fragments;


import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.CategoriaAdapter;
import com.afrofx.code.anjesgf.adpters.FornecedorAdapter;
import com.afrofx.code.anjesgf.adpters.PageAdapters;
import com.afrofx.code.anjesgf.adpters.UnidadeAdapter;
import com.afrofx.code.anjesgf.models.CategoriaModel;
import com.afrofx.code.anjesgf.models.ContaModel;
import com.afrofx.code.anjesgf.models.FornecedorModel;
import com.afrofx.code.anjesgf.models.StockModel;
import com.afrofx.code.anjesgf.models.UnidadeModel;

import java.util.ArrayList;
import java.util.List;


public class AddProdutosFragment extends Fragment {

    View v;

    private EditText edit_nome, edit_quantidade, edit_preco_compra, edit_preco_venda, edit_quanti_unidade, edit_quantidade_minima;
    AutoCompleteTextView txt_categoria, txt_unidade, txt_fornecedor;

    private double saldo;

    private String sls;

    private List<CategoriaModel> mList;
    private List<UnidadeModel> unidadeList;
    private List<FornecedorModel> fornecedorList;
    private List<ContaModel> listaConta;

    CategoriaAdapter adapter;
    UnidadeAdapter unidadeAdapter;
    FornecedorAdapter fornecedorAdapter;

    DatabaseHelper db;

    public AddProdutosFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_produtos, container, false);

        edit_nome = v.findViewById(R.id.add_nome_produto);
        edit_quantidade = v.findViewById(R.id.add_quantidade);
        edit_preco_compra = v.findViewById(R.id.add_preco_compra);
        edit_preco_venda = v.findViewById(R.id.add_preco_venda);
        edit_quanti_unidade = v.findViewById(R.id.add_quantidade_unidade);
        edit_quantidade_minima = v.findViewById(R.id.add_quantidade_minima);

        txt_categoria = v.findViewById(R.id.add_categoria);
        txt_unidade = v.findViewById(R.id.add_unidade);
        txt_fornecedor = v.findViewById(R.id.add_fornecedor);

        Button but_reg_produto = v.findViewById(R.id.add_produto);

        db = new DatabaseHelper(getActivity());

        but_reg_produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduto();
            }
        });

        mList = addCategoria();
        txt_categoria.setThreshold(1);
        adapter = new CategoriaAdapter(getActivity(), R.layout.fragment_add_produtos, R.layout.linha_categoria, mList);
        txt_categoria.setAdapter(adapter);

        unidadeList = addUnidade();
        txt_unidade.setThreshold(1);
        unidadeAdapter = new UnidadeAdapter(getActivity(), R.layout.fragment_add_produtos, R.layout.linha_categoria, unidadeList);
        txt_unidade.setAdapter(unidadeAdapter);

        fornecedorList = addFornecedor();
        txt_fornecedor.setThreshold(1);
        fornecedorAdapter = new FornecedorAdapter(getActivity(), R.layout.fragment_add_produtos, R.layout.linha_categoria, fornecedorList);
        txt_fornecedor.setAdapter(fornecedorAdapter);

        return v;
    }


    public void addProduto() {
        final String produto_nome = edit_nome.getText().toString();
        final String produto_unidade = txt_unidade.getText().toString();
        final String produto_fornecedor = txt_fornecedor.getText().toString();
        final String produto_categoria = txt_categoria.getText().toString().trim();

        int id_categoria = 0;
        int id_unidade = 0;
        int id_fornecedor = 0;

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

        final double quantidade_final = produto_quantidade_unidade * produto_quantidade;

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
                    mensagem("O Fornecedor nao existe");
                } else if (!findFornecedor() || produto_fornecedor.equals("")) {
                    id_fornecedor = db.procuraFornecedor(produto_fornecedor);
                }

                if (produto_quantidade > 0) {
                    LayoutInflater li = LayoutInflater.from(getContext());
                    View pagaVia = li.inflate(R.layout.promprt_paga_via, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

                    alertDialogBuilder.setView(pagaVia);

                    final Spinner nomeConta = (Spinner) pagaVia.findViewById(R.id.venderConta);
                    final TextView saldoDisponivel = (TextView) pagaVia.findViewById(R.id.txtValorDisponivel);
                    final TextView valorTotal = (TextView) pagaVia.findViewById(R.id.txtValorTotal);

                    db = new DatabaseHelper(getContext());

                    List<String> lables = db.listContas();

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, lables);

                    nomeConta.setAdapter(dataAdapter);

                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                    nomeConta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String label = parent.getItemAtPosition(position).toString();
                            saldo = db.SaldoTotal(label);
                            saldoDisponivel.setText(saldo + " MT");
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    valorTotal.setText(produto_preco_compra + " MT");

                    final int finalId_categoria = id_categoria;
                    final int finalId_unidade = id_unidade;
                    final int finalId_fornecedor = id_fornecedor;
                    alertDialogBuilder.setCancelable(true).setPositiveButton("Concluir", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (saldo >= produto_preco_compra) {

                                stockModel[0] = new StockModel(finalId_categoria, finalId_unidade, finalId_fornecedor, quantidade_final,
                                        produto_preco_compra, produto_quantidade_minima, produto_preco_venda, produto_nome);


                                db.inserirProduto(stockModel[0]);

                                mensagem("Produto Adicionado");

                                ViewPager viewPager;
                                PageAdapters pageAdapters;
                                TabLayout tab1 = getActivity().findViewById(R.id.tabLayout);
                                viewPager = getActivity().findViewById(R.id.pageViewer);

                                pageAdapters = new PageAdapters(getActivity().getSupportFragmentManager());
                                pageAdapters.AddFragment(new ListaProdutosFragment(), "Produtos");
                                pageAdapters.AddFragment(new AddProdutosFragment(), "Adicionar");
                                pageAdapters.AddFragment(new FornecedorFragment(), "Fornecedor");

                                viewPager.setAdapter(pageAdapters);

                                tab1.setupWithViewPager(viewPager);
                            } else {
                                mensagem("Saldo Insuficiente");
                            }
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();

                    alertDialog.show();
                }

            } else {
                mensagem("produto ja foi registado");
            }
        }
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
        db = new DatabaseHelper(getActivity());
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
        db = new DatabaseHelper(getActivity());
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
        db = new DatabaseHelper(getActivity());
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
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

}
