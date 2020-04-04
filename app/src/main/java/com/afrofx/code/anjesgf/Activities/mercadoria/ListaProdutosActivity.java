package com.afrofx.code.anjesgf.Activities.mercadoria;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrofx.code.anjesgf.Activities.home.MercadoriaActivity;
import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.Recyclers.ProductRecyclerAdpter;
import com.afrofx.code.anjesgf.ThemeSettings.Constant;
import com.afrofx.code.anjesgf.ThemeSettings.ThemeColor;
import com.afrofx.code.anjesgf.models.StockModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListaProdutosActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {
    private Toolbar toolbar;

    private List<StockModel> lstlist;
    private SearchView searchView;
    private ProductRecyclerAdpter productRecyclerAdpter;
    private RecyclerView recyclerView;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            new ThemeColor(this);
        }

        setContentView(R.layout.activity_lista_produtos);

        toolbar = (Toolbar) findViewById(R.id.toolbarListaProdutos);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);
        lstlist = new ArrayList<>();

        Cursor data = db.listProduto();

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.produto_adicionar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListaProdutosActivity.this, AdicionarProdutosActivity.class));
            }
        });

        if (data.getCount() == 0) {
            Toast.makeText(this, "Nao Existem Produtos", Toast.LENGTH_LONG).show();
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

        recyclerView = (RecyclerView)findViewById(R.id.recycleViewListaProdutos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        productRecyclerAdpter = new ProductRecyclerAdpter(this, lstlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(productRecyclerAdpter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stock_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Procurar...");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(ListaProdutosActivity.this, MercadoriaActivity.class));
            finish();
            return true;
        }
        return false;
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
        recyclerView = findViewById(R.id.recycleViewListaProdutos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        productRecyclerAdpter = new ProductRecyclerAdpter(this, lista);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productRecyclerAdpter);


        return false;
    }

    public void resetSearch() {
        recyclerView = findViewById(R.id.recycleViewListaProdutos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        productRecyclerAdpter = new ProductRecyclerAdpter(this, lstlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
