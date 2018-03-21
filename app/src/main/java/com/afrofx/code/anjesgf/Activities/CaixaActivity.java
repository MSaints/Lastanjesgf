package com.afrofx.code.anjesgf.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.ContaRecyclerAdapter;
import com.afrofx.code.anjesgf.models.ContaModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Afro FX on 2/7/2018.
 */

public class CaixaActivity extends AppCompatActivity {

    private DatabaseHelper db;

    private List<ContaModel> listaConta;

    private FloatingActionButton addCaixa;

    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        setContentView(R.layout.topbar_caixa_screen);

        addCaixa = (FloatingActionButton) findViewById(R.id.caixa_adicionar);

        addCaixa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addConta();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        listaConta = new ArrayList<>();

        Cursor cursor = db.listOperacoes();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Nao Contas", Toast.LENGTH_LONG).show();
        } else {
            while (cursor.moveToNext()) {
                if (cursor != null && cursor.getString(1) != null){
                    int id_conta = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_conta")));
                    double cont_saldo = Double.parseDouble(cursor.getString(2));
                    String nome_conta= cursor.getString(1);
                    ContaModel dadosConta = new ContaModel(nome_conta, cont_saldo, id_conta);
                    listaConta.add(dadosConta);
                }
            }
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleViewConta);
        recyclerView.setHasFixedSize(true);
        ContaRecyclerAdapter productAdpter = new ContaRecyclerAdapter(this, listaConta);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(productAdpter);

    }

    public void addConta() {
        LayoutInflater li = LayoutInflater.from(this);
        View quantiView = li.inflate(R.layout.prompt_add_conta, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(quantiView);

        final Spinner add_conta = (Spinner) quantiView.findViewById(R.id.caixa_add_conta);
        List<String> lables = db.listContas();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
        add_conta.setAdapter(dataAdapter);


        // set dialog message
        alertDialogBuilder.setCancelable(true).setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String con = add_conta.getSelectedItem().toString();
                addValor(con);
            }
        });

        // Criar O Alerta
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Mostra o alerta
        alertDialog.show();
    }


    public void addValor(final String nome){
        LayoutInflater li = LayoutInflater.from(this);
        View quantiView = li.inflate(R.layout.prompt_add_saldo, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(quantiView);
        final EditText add_valor = (EditText) quantiView.findViewById(R.id.caixa_add_valor);

        alertDialogBuilder.setCancelable(true).setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                String addValor = add_valor.getText().toString().trim();
                final double saldoInicial = !addValor.equals("") ? Double.parseDouble(addValor) : 0;
                int idConta = db.idConta(nome);
                int idOperacao  = 1;
                ContaModel clienteModel = new ContaModel(saldoInicial, idConta, idOperacao);
                db.registarValor(clienteModel);
            }
        });

        // Criar O Alerta
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Mostra o alerta
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
         if (id == android.R.id.home) {
            startActivity(new Intent(CaixaActivity.this, MainScreenActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
