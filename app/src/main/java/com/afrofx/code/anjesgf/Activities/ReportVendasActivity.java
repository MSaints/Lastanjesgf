package com.afrofx.code.anjesgf.Activities;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.ReportCliente1Recycler;
import com.afrofx.code.anjesgf.models.ClienteModel;

import java.util.ArrayList;
import java.util.List;


public class ReportVendasActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private ReportCliente1Recycler reportCliente1Recycler;
    private List<ClienteModel> clienteModelList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        db = new DatabaseHelper(this);

        listaClientes();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerViewClientsR1);
        recyclerView.setHasFixedSize(true);
        reportCliente1Recycler = new ReportCliente1Recycler(this, clienteModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(reportCliente1Recycler);
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
