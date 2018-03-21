package com.afrofx.code.anjesgf.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afrofx.code.anjesgf.R;

public class MainVendasActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CardView OpcaoVenda1, OpcaoVenda2, OpcaoVenda3, OpcaoVenda4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_vendas);

        toolbar = (Toolbar) findViewById(R.id.toolbarMainVendas);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        OpcaoVenda1 = (CardView)findViewById(R.id.OpcaoVenda1);
        OpcaoVenda2 = (CardView)findViewById(R.id.OpcaoVenda2);
        OpcaoVenda3 = (CardView)findViewById(R.id.OpcaoVenda3);
        OpcaoVenda4 = (CardView)findViewById(R.id.OpcaoVenda4);

        EscolhaOpcao();
    }

    public void EscolhaOpcao(){
        OpcaoVenda1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainVendasActivity.this, VendasActivity.class));
                finish();
            }
        });

        OpcaoVenda2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainVendasActivity.this, MercadoriaVendidaActivity.class));
                finish();
            }
        });

        OpcaoVenda3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        OpcaoVenda4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_topbar_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.but_vender) {
            return true;
        } else if (id == android.R.id.home) {
            startActivity(new Intent(MainVendasActivity.this, MainScreenActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
