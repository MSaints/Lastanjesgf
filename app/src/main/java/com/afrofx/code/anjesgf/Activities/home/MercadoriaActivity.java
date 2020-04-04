package com.afrofx.code.anjesgf.Activities.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.afrofx.code.anjesgf.Activities.root.MainScreenActivity;
import com.afrofx.code.anjesgf.Activities.mercadoria.AdicionarProdutosActivity;
import com.afrofx.code.anjesgf.Activities.mercadoria.FornecedoresActivity;
import com.afrofx.code.anjesgf.Activities.mercadoria.ListaProdutosActivity;
import com.afrofx.code.anjesgf.Activities.mercadoria.QuebrasActivity;
import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.Dialogs.DialogFastSale;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.ThemeSettings.Constant;
import com.afrofx.code.anjesgf.ThemeSettings.ThemeColor;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * Created by Afro FX on 2/6/2018.
 */

public class MercadoriaActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private AdView adsBanner;

    private CardView OpcaoMercadoria1, OpcaoMercadoria2, OpcaoMercadoria3, OpcaoMercadoria4;
    private TextView textOpcaoMercadoria1, textOpcaoMercadoria2, textOpcaoMercadoria3, textOpcaoMercadoria4;

    private DatabaseHelper db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            new ThemeColor(this);
        }
        setContentView(R.layout.activity_stock_screen);

        toolbar = findViewById(R.id.toolbarMercadoria);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MobileAds.initialize(this,"ca-app-pub-8010088543982376~2317414108");

        adsBanner = findViewById(R.id.adsBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adsBanner.loadAd(adRequest);

        db = new DatabaseHelper(this);

        OpcaoMercadoria1 = findViewById(R.id.OpcaoMercadoria1);
        OpcaoMercadoria2 = findViewById(R.id.OpcaoMercadoria2);
        OpcaoMercadoria3 = findViewById(R.id.OpcaoMercadoria3);
        OpcaoMercadoria4 = findViewById(R.id.OpcaoMercadoria4);

        textOpcaoMercadoria1 = findViewById(R.id.textOpcaoMercadoria1);
        textOpcaoMercadoria1.setTextColor(Constant.color);
        textOpcaoMercadoria2 = findViewById(R.id.textOpcaoMercadoria2);
        textOpcaoMercadoria2.setTextColor(Constant.color);
        textOpcaoMercadoria3 = findViewById(R.id.textOpcaoMercadoria3);
        textOpcaoMercadoria3.setTextColor(Constant.color);
        textOpcaoMercadoria4 = findViewById(R.id.textOpcaoMercadoria4);
        textOpcaoMercadoria4.setTextColor(Constant.color);

        TextView valorTotalStock = (TextView) findViewById(R.id.valorTotalStock);
        TextView valorTotalQuebras = (TextView) findViewById(R.id.valorTotalQuebras);

        valorTotalStock.setText(db.saldoValorStock()+" MT");
        valorTotalQuebras.setText(db.saldoValorQuebras()+" MT");
        EscolhaOpcao();



        Button fastsale = findViewById(R.id.fastSale);
        fastsale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFastSale dialogFastSale = new DialogFastSale(MercadoriaActivity.this);
                dialogFastSale.show();
            }
        });
    }

    public void EscolhaOpcao(){
        OpcaoMercadoria1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MercadoriaActivity.this, ListaProdutosActivity.class));
                finish();
            }
        });

        OpcaoMercadoria2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MercadoriaActivity.this, AdicionarProdutosActivity.class));
                finish();
            }
        });

        OpcaoMercadoria3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MercadoriaActivity.this, FornecedoresActivity.class));
                finish();
            }
        });

        OpcaoMercadoria4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MercadoriaActivity.this, QuebrasActivity.class));
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(MercadoriaActivity.this, MainScreenActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
