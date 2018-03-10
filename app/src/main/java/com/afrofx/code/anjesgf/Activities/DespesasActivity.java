package com.afrofx.code.anjesgf.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.Fragments.AddProdutosFragment;
import com.afrofx.code.anjesgf.Fragments.DespesasFragment;
import com.afrofx.code.anjesgf.Fragments.FornecedorFragment;
import com.afrofx.code.anjesgf.Fragments.ListaProdutosFragment;
import com.afrofx.code.anjesgf.Fragments.RedimentosFragment;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.DispesasRecyclerAdapter;
import com.afrofx.code.anjesgf.adpters.PageAdapters;
import com.afrofx.code.anjesgf.models.DispesasModel;

import java.util.ArrayList;
import java.util.List;

public class DespesasActivity extends AppCompatActivity {



    private ViewPager viewPager;
    private PageAdapters pageAdapters;
    private TabLayout tab1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topbar_despesas_screen);

        tab1 = (TabLayout) findViewById(R.id.tabLayout2);
        viewPager = (ViewPager) findViewById(R.id.pageViewer2);

        pageAdapters = new PageAdapters(getSupportFragmentManager());

        pageAdapters.AddFragment(new DespesasFragment(), "Despesas");
        pageAdapters.AddFragment(new RedimentosFragment(), "Redimentos");
        viewPager.setAdapter(pageAdapters);
        tab1.setupWithViewPager(viewPager);
    }
}
