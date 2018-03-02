package com.afrofx.code.anjesgf.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.DispesasRecyclerAdapter;
import com.afrofx.code.anjesgf.models.DispesasModel;

import java.util.List;

public class DespesasActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private DispesasRecyclerAdapter dispesasRecyclerAdapter;
    private List<DispesasModel> dispesasModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topbar_despesas_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar8);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        toolbar.setTitle(R.string.stock);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerViewDispesa);
        recyclerView.setHasFixedSize(true);
        dispesasRecyclerAdapter = new DispesasRecyclerAdapter(this, dispesasModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(dispesasRecyclerAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.dispesa_adicionar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), AdicionarDispesaActivity.class));
                onPause();
            }
        });

    }

}
