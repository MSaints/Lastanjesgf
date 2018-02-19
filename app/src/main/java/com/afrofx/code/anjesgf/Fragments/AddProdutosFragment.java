package com.afrofx.code.anjesgf.Fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.CategoriaAdapter;
import com.afrofx.code.anjesgf.adpters.PageAdapters;
import com.afrofx.code.anjesgf.adpters.UnidadeAdapter;
import com.afrofx.code.anjesgf.models.CategoriaModel;
import com.afrofx.code.anjesgf.models.StockModel;
import com.afrofx.code.anjesgf.models.UnidadeModel;

import java.util.ArrayList;
import java.util.List;


public class AddProdutosFragment extends Fragment {

    View v;

    private EditText edit_nome, edit_quantidade, edit_preco_compra, edit_preco_venda, edit_quanti_unidade, edit_quantidade_minima;
    AutoCompleteTextView txt_categoria, txt_unidade, txt_fornecedor;

    List<CategoriaModel> mList;
    List<UnidadeModel> unidadeList;
    CategoriaAdapter adapter;
    UnidadeAdapter unidadeAdapter;

    DatabaseHelper db;

    public AddProdutosFragment() {}

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

        return v;
    }



    public void addProduto() {
        String produto_nome = edit_nome.getText().toString();
        String produto_unidade = txt_unidade.getText().toString();
        String produto_fornecedor = txt_fornecedor.getText().toString();
        String produto_categoria = txt_categoria.getText().toString();

        int id_categoria = 0;
        String cate;
        int id_unidade = 0;
        String uni;
        int id_fornecedor = 0;
        String forne;

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
        StockModel stockModel = db.procuraProduto(produto_nome);

        if (produto_nome.equals("")) {
            mensagem("Preencha os Campos");
        } else {
            if (stockModel == null) {
                if (addCategory()) {
                    id_categoria = db.procuraCategoria(produto_categoria);
                } else {
                    id_categoria = db.procuraCategoria(produto_categoria);
                }

                if(addUnit()){
                    id_unidade = db.procuraUnidade(produto_unidade);
                }else{
                    id_categoria = db.procuraCategoria(produto_categoria);
                }

                stockModel = new StockModel(id_categoria, id_unidade, id_fornecedor, produto_quantidade, produto_preco_compra,
                        produto_quantidade_minima, produto_preco_venda, produto_quantidade_unidade, produto_nome);

                db.inserirProduto(stockModel);

                mensagem("Produto Adicionado");

                ViewPager viewPager;
                PageAdapters pageAdapters;
                TabLayout tab1 = getActivity().findViewById(R.id.tabLayout);
                viewPager = getActivity().findViewById(R.id.pageViewer);

                pageAdapters = new PageAdapters(getActivity().getSupportFragmentManager());
                pageAdapters.AddFragment(new ListaProdutosFragment(), "Estoque");
                pageAdapters.AddFragment(new AddProdutosFragment(), "Adicionar");

                viewPager.setAdapter(pageAdapters);

                tab1.setupWithViewPager(viewPager);
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

    private List<CategoriaModel> addCategoria() {
        List<CategoriaModel> lista = new ArrayList<>();
        db = new DatabaseHelper(getActivity());
        Cursor data = db.listCategoria();
        if (data.getCount() == 0) {
            Toast.makeText(getActivity(), "Nao Existem Categorias", Toast.LENGTH_LONG).show();
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
            Toast.makeText(getActivity(), "Nao Existem Unidades", Toast.LENGTH_LONG).show();
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
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}
