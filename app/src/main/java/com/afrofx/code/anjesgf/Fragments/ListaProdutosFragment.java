package com.afrofx.code.anjesgf.Fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.ProductRecyclerAdpter;
import com.afrofx.code.anjesgf.models.StockModel;

import java.util.ArrayList;
import java.util.List;

public class ListaProdutosFragment extends Fragment {

    View v;

    private List<StockModel> lstlist;

    DatabaseHelper db;

    public ListaProdutosFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_lista_produtos, container, false);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        ProductRecyclerAdpter productRecyclerAdpter = new ProductRecyclerAdpter(getContext(), lstlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(productRecyclerAdpter);

        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(getActivity());
        lstlist = new ArrayList<>();

        Cursor data = db.listProduto();

        if (data.getCount() == 0) {
            Toast.makeText(getActivity(), "Nao Existem Produtos", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                int id_produto = Integer.parseInt(data.getString(0));
                String nome_categoria = data.getString(data.getColumnIndex("categoria_nome"));
                String nome = data.getString(4);
                double quantidade = Double.parseDouble(data.getString(5));
                double preco_venda = Double.parseDouble(data.getString(8));

                StockModel listItem = new StockModel(id_produto,nome, quantidade, preco_venda, nome_categoria);
                lstlist.add(listItem);
            }
        }
    }
}
