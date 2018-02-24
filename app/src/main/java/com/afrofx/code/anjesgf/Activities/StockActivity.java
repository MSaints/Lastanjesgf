package com.afrofx.code.anjesgf.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.Fragments.AddProdutosFragment;
import com.afrofx.code.anjesgf.Fragments.FornecedorFragment;
import com.afrofx.code.anjesgf.Fragments.ListaProdutosFragment;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.PageAdapters;
import com.afrofx.code.anjesgf.models.StockModel;

import java.util.List;

/**
 * Created by Afro FX on 2/6/2018.
 */

public class StockActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private PageAdapters pageAdapters;
    private TabLayout tab1;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdpter;
    private List<StockModel> listItems;

    private DatabaseHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.recycle_view_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        toolbar.setTitle(R.string.stock);

        tab1 = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.pageViewer);

        pageAdapters = new PageAdapters(getSupportFragmentManager());


        pageAdapters.AddFragment(new ListaProdutosFragment(), "Stock");
        pageAdapters.AddFragment(new AddProdutosFragment(), "Registar");
        pageAdapters.AddFragment(new FornecedorFragment(), "Fornecedor");
        viewPager.setAdapter(pageAdapters);
        tab1.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stock_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.but_vender) {
            Toast.makeText(getApplication(), "Clickei", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == android.R.id.home) {
            startActivity(new Intent(StockActivity.this, MainScreenActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
