package com.afrofx.code.anjesgf.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.ContaAdapter;
import com.afrofx.code.anjesgf.models.ContaModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

        double saldo = db.saldoCaixa();
        String nome = "Produtos em Stock";
        int id_conta = 1;

        ContaModel dadosConta = new ContaModel(nome, saldo, id_conta);
        listaConta.add(dadosConta);
        dadosConta = new ContaModel("M-PESA", 50000, 2);
        listaConta.add(dadosConta);
        dadosConta =  new ContaModel("Millenium Bim", 20000, 3);
        listaConta.add(dadosConta);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleViewConta);
        recyclerView.setHasFixedSize(true);
        ContaAdapter productAdpter = new ContaAdapter(this, listaConta);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(productAdpter);


    }




    public void addConta() {
        LayoutInflater li = LayoutInflater.from(this);
        View quantiView = li.inflate(R.layout.prompt_add_caixa, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(quantiView);

        final EditText add_conta = (EditText) quantiView.findViewById(R.id.caixa_add_conta);
        final EditText add_valor = (EditText) quantiView.findViewById(R.id.caixa_add_valor);

        // set dialog message
        alertDialogBuilder.setCancelable(false).setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplication(), "MATSAKITSA", Toast.LENGTH_LONG).show();
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        // Criar O Alerta
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Mostra o alerta
        alertDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_caixa_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.but_vender) {
            Toast.makeText(getApplication(), "Caixa", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == android.R.id.home) {
            startActivity(new Intent(CaixaActivity.this, MainScreenActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
