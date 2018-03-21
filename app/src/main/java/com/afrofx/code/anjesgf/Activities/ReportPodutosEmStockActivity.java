package com.afrofx.code.anjesgf.Activities;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.ReportCliente1Recycler;
import com.afrofx.code.anjesgf.adpters.ReportStockRecycler;
import com.afrofx.code.anjesgf.models.ClienteModel;
import com.afrofx.code.anjesgf.models.StockModel;

import java.util.ArrayList;
import java.util.List;


public class ReportPodutosEmStockActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private ReportStockRecycler reportStockRecycler;
    private List<StockModel> stockModelList ;
    private TextView dat1, dat2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_produtos_stock);

        db = new DatabaseHelper(this);


        dat1 = (TextView)findViewById(R.id.stockDataInicial);
        dat2 = (TextView)findViewById(R.id.stockDataFinal);



        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String data1 = (mSharedPreference.getString("dataInicio", "2010-02-02"));
        String data2 = (mSharedPreference.getString("dataFim", "2019-02-02"));

        listaClientes(data1, data2);

        dat1.setText(data1);
        dat2.setText(data2);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerViewStockReport);
        recyclerView.setHasFixedSize(true);
        reportStockRecycler = new ReportStockRecycler(this, stockModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(reportStockRecycler);
    }


    public void listaClientes(String d1, String d2){
        stockModelList = new ArrayList<>();
        Cursor dados = db.listProdutoRelatorio(d1, d2);

        if(dados.getCount()==0) {
            Mensagem("Nenhum Registo Encontrado");
        }else{
            while(dados.moveToNext()){
                String nome = dados.getString(dados.getColumnIndex("produto_nome"));
                int numero = Integer.parseInt(dados.getString(dados.getColumnIndex("produto_quantidade")));
                double data = Double.parseDouble(dados.getString(dados.getColumnIndex("produto_preco_venda")));
                StockModel stockModel = new StockModel(numero, data, nome);
                stockModelList.add(stockModel);
            }
        }
    }


    public void Mensagem(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT);
    }
}
