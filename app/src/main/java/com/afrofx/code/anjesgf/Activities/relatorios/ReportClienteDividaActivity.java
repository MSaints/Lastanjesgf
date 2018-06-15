package com.afrofx.code.anjesgf.Activities.relatorios;


import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.*;
import android.content.*;
import android.view.*;
import android.preference.*;
import android.widget.*;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.Recyclers.Report.ReportSaldoContasRecycler;
import com.afrofx.code.anjesgf.ThemeSettings.*;
import com.afrofx.code.anjesgf.models.ClienteModel;

import java.util.ArrayList;
import java.util.List;


public class ReportClienteDividaActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private ReportSaldoContasRecycler reportCliente1Recycler;
    private List<ClienteModel> clienteModelList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            new ThemeColor(this);
        }

        setContentView(R.layout.activity_clientes_registados);

        db = new DatabaseHelper(this);


        TextView dat1 = (TextView) findViewById(R.id.stockDataInicial);
        View line = (View)findViewById(R.id.line);

        line.setBackgroundColor(Constant.color);


        db = new DatabaseHelper(this);

        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String data1 = (mSharedPreference.getString("dataInicio", "2018-01-01"));
        String data2 = (mSharedPreference.getString("dataFim", "2018-12-02"));

        dat1.setText("Do dia "+data1+" Para o dia "+data2);


        listaClientes();

    }


    public void listaClientes(){
        clienteModelList = new ArrayList<>();
        Cursor dados = db.listCliente();

        if(dados.getCount()==0) {
            Mensagem("Nenhum Registo Encontrado");
        }else{
            while(dados.moveToNext()){
                String nome = dados.getString(dados.getColumnIndex("cliente_nome"));
                int numero = Integer.parseInt(dados.getString(dados.getColumnIndex("cliente_cell")));
                String data = dados.getString(dados.getColumnIndex("cliente_data_registo"));
                String email = dados.getString(dados.getColumnIndex("cliente_email"));
                int id = Integer.parseInt(dados.getString(dados.getColumnIndex("id_cliente")));
                ClienteModel clienteModel = new ClienteModel(nome, email, data, id, numero);
                clienteModelList.add(clienteModel);
            }
        }
    }


    public void Mensagem(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT);
    }
}
