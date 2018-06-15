package com.afrofx.code.anjesgf.Activities.home;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afrofx.code.anjesgf.Activities.root.MainScreenActivity;
import com.afrofx.code.anjesgf.Activities.vendas.DividasClienteActivity;
import com.afrofx.code.anjesgf.Activities.vendas.MaisVendidosActivity;
import com.afrofx.code.anjesgf.Activities.vendas.MercadoriaVendidaActivity;
import com.afrofx.code.anjesgf.Activities.vendas.VenderMercadoriaActivity;
import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.Dialogs.DialogFastSale;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.ThemeSettings.Constant;
import com.afrofx.code.anjesgf.ThemeSettings.ThemeColor;
import com.afrofx.code.anjesgf.adpters.ProdutoAdapter;
import com.afrofx.code.anjesgf.models.StockModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

public class VendasActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private CardView OpcaoVenda1, OpcaoVenda2, OpcaoVenda3, OpcaoVenda4;
    private TextView textOpcaoMercadoria1, textOpcaoMercadoria2, textOpcaoMercadoria3, textOpcaoMercadoria4;

    private AdView adsBanner;

    private List<StockModel> produStockModels;
    private ProdutoAdapter produtoAdapter;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            new ThemeColor(this);
        }

        setContentView(R.layout.activity_main_vendas);

        MobileAds.initialize(this,"ca-app-pub-8010088543982376~2317414108");

        adsBanner = findViewById(R.id.adsBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adsBanner.loadAd(adRequest);

        toolbar = (Toolbar) findViewById(R.id.toolbarMainVendas);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        OpcaoVenda1 = (CardView)findViewById(R.id.OpcaoVenda1);
        OpcaoVenda2 = (CardView)findViewById(R.id.OpcaoVenda2);
        OpcaoVenda3 = (CardView)findViewById(R.id.OpcaoVenda3);
        OpcaoVenda4 = (CardView)findViewById(R.id.OpcaoVenda4);

        textOpcaoMercadoria1 = findViewById(R.id.textOpcaoVendas1);
        textOpcaoMercadoria1.setTextColor(Constant.color);
        textOpcaoMercadoria2 = findViewById(R.id.textOpcaoVendas2);
        textOpcaoMercadoria2.setTextColor(Constant.color);
        textOpcaoMercadoria3 = findViewById(R.id.textOpcaoVendas3);
        textOpcaoMercadoria3.setTextColor(Constant.color);
        textOpcaoMercadoria4 = findViewById(R.id.textOpcaoVendas4);
        textOpcaoMercadoria4.setTextColor(Constant.color);

        TextView vendaPendente = (TextView)findViewById(R.id.vendaPendente);
        TextView vendaRecebido = (TextView)findViewById(R.id.vendaRecebido);

        EscolhaOpcao();

        db = new DatabaseHelper(this);

        vendaPendente.setText(db.saldoValorDividas()+" MZ");
        vendaRecebido.setText(db.saldoValorVendas()+" MZ");

        Button fastsale = (Button) findViewById(R.id.fastSale);
        fastsale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFastSale dialogFastSale = new DialogFastSale(VendasActivity.this);
                dialogFastSale.show();
            }
        });
    }

    public void EscolhaOpcao(){
        OpcaoVenda1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VendasActivity.this, VenderMercadoriaActivity.class));
                finish();
            }
        });

        OpcaoVenda2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VendasActivity.this, MercadoriaVendidaActivity.class));
                finish();
            }
        });

        OpcaoVenda3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VendasActivity.this, DividasClienteActivity.class));
                finish();
            }
        });

        OpcaoVenda4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VendasActivity.this, MaisVendidosActivity.class));
                finish();
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(VendasActivity.this, MainScreenActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
