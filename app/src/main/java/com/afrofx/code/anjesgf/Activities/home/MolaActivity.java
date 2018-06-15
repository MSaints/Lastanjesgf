package com.afrofx.code.anjesgf.Activities.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.Activities.root.MainScreenActivity;
import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.Dialogs.DialogFastSale;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.Recyclers.ContaRecyclerAdapter;
import com.afrofx.code.anjesgf.ThemeSettings.Constant;
import com.afrofx.code.anjesgf.ThemeSettings.ThemeColor;
import com.afrofx.code.anjesgf.models.ContaModel;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static java.lang.String.format;


public class MolaActivity extends AppCompatActivity {

    private DatabaseHelper db;

    private List<ContaModel> listaConta;

    private FloatingActionButton addCaixa;

    private Toolbar toolbar;

    private double saldoCaixa;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            new ThemeColor(this);
        }

        setContentView(R.layout.topbar_caixa_screen);

        toolbar = (Toolbar) findViewById(R.id.toolbarCaixa);
        toolbar.setBackgroundColor(Constant.color);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button fastsale = (Button) findViewById(R.id.fastSale);
        fastsale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFastSale dialogFastSale = new DialogFastSale(MolaActivity.this);
                dialogFastSale.show();
            }
        });

        addCaixa = (FloatingActionButton) findViewById(R.id.caixa_adicionar);

        addCaixa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addConta();
            }
        });



        db = new DatabaseHelper(this);

        listaConta = new ArrayList<>();

        Cursor cursor = db.listOperacoes();

        TextView sal = (TextView)findViewById(R.id.saldoCaixa);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Nao Contas", Toast.LENGTH_LONG).show();
        } else {
            while (cursor.moveToNext()) {
                if (cursor != null && cursor.getString(1) != null){
                    int id_conta = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_conta")));
                    double cont_saldo = Double.parseDouble(cursor.getString(2));
                    String nome_conta= cursor.getString(1);
                    if(nome_conta.equals("Caixa")){
                        saldoCaixa = cont_saldo;
                    }else{
                        ContaModel dadosConta = new ContaModel(nome_conta, cont_saldo, id_conta);
                        listaConta.add(dadosConta);
                    }
                }
            }
        }

        String yourFormattedString = format("%,.2f",saldoCaixa);

        sal.setText(yourFormattedString);

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

        final android.widget.AutoCompleteTextView add_conta = quantiView.findViewById(R.id.caixa_add_conta);
        List<String> lables = db.listContas();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
         dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        add_conta.setAdapter(dataAdapter);

        TextView view = (TextView) quantiView.findViewById(R.id.definitionInfoTitle);
        view.setBackgroundColor(Constant.color);
        // set dialog message
        alertDialogBuilder.setCancelable(true).setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                boolean r;
        String nome_conta = add_conta.getText().toString();
        ContaModel contaModel = db.procurarConta(nome_conta);

        if (contaModel == null) {
            contaModel = new ContaModel(nome_conta);
            db.registaConta(contaModel);
            r = false;
        } else {
            db.procurarConta(nome_conta);
            r = true;
        }

        if(!r || r){
            addValor(nome_conta);
        }

            }
        });

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
        TextView view = (TextView) quantiView.findViewById(R.id.definitionInfoTitle);
        view.setBackgroundColor(Constant.color);

        alertDialogBuilder.setCancelable(true).setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                String addValor = add_valor.getText().toString().trim();
                final double saldoInicial = !addValor.equals("") ? Double.parseDouble(addValor) : 0;
                long idConta = db.idConta(nome);
                int idOperacao  = 1;
                ContaModel clienteModel = new ContaModel(saldoInicial, idConta, idOperacao);
                db.registarValor(clienteModel);

                new SweetAlertDialog(MolaActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Conta Registada")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                finish();
                                startActivity(new Intent(MolaActivity.this, MolaActivity.class));
                                sDialog.dismiss();
                                sDialog.setCancelable(false);
                            }
                        })
                        .show();
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
            startActivity(new Intent(MolaActivity.this, MainScreenActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
