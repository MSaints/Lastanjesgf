package com.afrofx.code.anjesgf.Fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.ProductRecyclerAdpter;
import com.afrofx.code.anjesgf.models.StockModel;

import java.util.ArrayList;
import java.util.List;

public class ListaProdutosFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    View v;

    private List<StockModel> lstlist;
    private SearchView searchView;
    private ProductRecyclerAdpter productRecyclerAdpter;
    private RecyclerView recyclerView;
    private Context context;


    DatabaseHelper db;

    public ListaProdutosFragment() {


    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_lista_produtos, container, false);

        context = getActivity();
        setHasOptionsMenu(true);

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

        recyclerView = (RecyclerView) v.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        productRecyclerAdpter = new ProductRecyclerAdpter(getContext(), lstlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(productRecyclerAdpter);
        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.stock_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Procurar");

        super.onCreateOptionsMenu(menu, inflater);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (newText == null || newText.trim().isEmpty()) {
            resetSearch();
            return false;
        }

        List<StockModel>lista = new ArrayList<>(lstlist);
        for (StockModel value : lstlist) {
            if (!value.getProduto_nome().toLowerCase().contains(newText.toLowerCase())) {
                lista.remove(value);
            }
        }
        recyclerView = (RecyclerView) v.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        productRecyclerAdpter = new ProductRecyclerAdpter(getContext(), lista);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(productRecyclerAdpter);


        return false;
    }

    public void resetSearch() {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        productRecyclerAdpter = new ProductRecyclerAdpter(getContext(), lstlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(productRecyclerAdpter);
    }


    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }
}
